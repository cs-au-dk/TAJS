/*
 * Copyright 2009-2016 Aarhus University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.brics.tajs.analysis.js;

import dk.brics.tajs.analysis.AsyncEvents;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.OrdinaryCallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.ParallelTransfer;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMEvents;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.analysis.nativeobjects.JSGlobal;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.AssumeNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginWithNode;
import dk.brics.tajs.flowgraph.jsnodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.CatchNode;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.DeletePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.EndForInNode;
import dk.brics.tajs.flowgraph.jsnodes.EndLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.EndWithNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.ExceptionalReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.HasNextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.NewObjectNode;
import dk.brics.tajs.flowgraph.jsnodes.NextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.NodeVisitor;
import dk.brics.tajs.flowgraph.jsnodes.NopNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.ReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.ThrowNode;
import dk.brics.tajs.flowgraph.jsnodes.TypeofNode;
import dk.brics.tajs.flowgraph.jsnodes.UnaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.State.Properties;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.AnalysisException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Transfer for flow graph nodes.
 */
public class NodeTransfer implements NodeVisitor {

    private Solver.SolverInterface c;

    private PropVarOperations pv;

    private IAnalysisMonitoring m;

    /**
     * Constructs a new TransferFunctions object.
     */
    public NodeTransfer() {
    }

    /**
     * Initializes the connection to the solver.
     */
    public void setSolverInterface(Solver.SolverInterface c) {
        this.c = c;
        m = c.getMonitoring();
        pv = c.getAnalysis().getPropVarOperations();
    }

    /**
     * Returns the solver interface.
     */
    public Solver.SolverInterface getSolverInterface() {
        return c;
    }

    /**
     * Transfer ordinary and exceptional return for the given call node and callee entry.
     */
    public void transferReturn(AbstractNode call_node, BasicBlock callee_entry, Context caller_context, Context callee_context,
                               Context edge_context, boolean implicit) {
        if (call_node instanceof BeginForInNode) {
            for (EndForInNode endNode : ((BeginForInNode) call_node).getEndNodes()) {
                BasicBlock end_block = endNode.getBlock();
                if (c.getAnalysisLatticeElement().getState(end_block, callee_context) != null)// (EndForInNode uses same context as corresponding BeginForInNode)
                    c.addToWorklist(end_block, callee_context);
            }
        } else { // call_node is an ordinary call node
            Function callee = callee_entry.getFunction();
            BasicBlock ordinary_exit = callee.getOrdinaryExit();
            BasicBlock exceptional_exit = callee.getExceptionalExit();
            State ordinary_exit_state = c.getAnalysisLatticeElement().getState(ordinary_exit, callee_context);// (ReturnNode uses same context as corresponding function entry node)
            State exceptional_exit_state = c.getAnalysisLatticeElement().getState(exceptional_exit, callee_context);
            NodeAndContext<Context> caller = new NodeAndContext<>(call_node, caller_context);
            if (ordinary_exit_state != null) {
                if (ordinary_exit.getFirstNode() instanceof ReturnNode) {
                    ReturnNode returnNode = (ReturnNode) ordinary_exit.getFirstNode();
                    transferReturn(returnNode.getReturnValueRegister(), ordinary_exit, ordinary_exit_state.clone(), caller, edge_context, implicit);
                } else
                    throw new AnalysisException("ReturnNode expected");
            }
            if (exceptional_exit_state != null) {
                transferExceptionalReturn(exceptional_exit, exceptional_exit_state.clone(), caller, edge_context, implicit);
            }
        }
    }

    /**
     * 12.3 empty statement.
     */
    @Override
    public void visit(NopNode n) {
        // do nothing
    }

    /**
     * 12.2 variable declaration.
     */
    @Override
    public void visit(DeclareVariableNode n) {
        pv.declareAndWriteVariable(n.getVariableName(), Value.makeUndef(), false);
    }

    /**
     * 11.13 and 7.8 assignment with literal.
     */
    @Override
    public void visit(ConstantNode n) {
        Value v;
        switch (n.getType()) {
            case NULL:
                v = Value.makeNull();
                break;
            case UNDEFINED:
                v = Value.makeUndef();
                break;
            case BOOLEAN:
                v = Value.makeBool(n.getBoolean());
                break;
            case NUMBER:
                v = Value.makeNum(n.getNumber());
                break;
            case STRING:
                v = Value.makeStr(n.getString());
                break;
            default:
                throw new AnalysisException();
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            c.getState().writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.1.5 object initializer.
     */
    @Override
    public void visit(NewObjectNode n) {
        HeapContext heapContext = c.getAnalysis().getContextSensitivityStrategy().makeObjectLiteralHeapContext(n, c.getState());
        ObjectLabel objlabel = new ObjectLabel(n, Kind.OBJECT, heapContext);
        c.getState().newObject(objlabel);
        c.getState().writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            c.getState().writeRegister(n.getResultRegister(), Value.makeObject(objlabel));
    }

    /**
     * 11.13 and 11.4 assignment with unary operator.
     */
    @Override
    public void visit(UnaryOperatorNode n) {
        Value arg = c.getState().readRegister(n.getArgRegister());
        arg = UnknownValueResolver.getRealValue(arg, c.getState());
        Value v;
        switch (n.getOperator()) {
            case COMPLEMENT:
                v = Operators.complement(arg, c);
                break;
            case MINUS:
                v = Operators.uminus(arg, c);
                break;
            case NOT:
                v = Operators.not(arg);
                break;
            case PLUS:
                v = Operators.uplus(arg, c);
                break;
            default:
                throw new AnalysisException();
        }
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            c.getState().writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.13 and 11.5/6/7/8 assignment with binary operator.
     */
    @Override
    public void visit(BinaryOperatorNode n) {
        Value arg1 = c.getState().readRegister(n.getArg1Register());
        Value arg2 = c.getState().readRegister(n.getArg2Register());
        arg1 = UnknownValueResolver.getRealValue(arg1, c.getState());
        arg2 = UnknownValueResolver.getRealValue(arg2, c.getState());
        Value v;
        switch (n.getOperator()) {
            case ADD:
                v = Operators.add(arg1, arg2, c); // TODO: test07.js could improve messages if keeping conversions of the two args separate
                break;
            case AND:
                v = Operators.and(arg1, arg2, c);
                break;
            case DIV:
                v = Operators.div(arg1, arg2, c);
                break;
            case EQ:
                v = Operators.eq(arg1, arg2, c);
                break;
            case GE:
                v = Operators.ge(arg1, arg2, c);
                break;
            case GT:
                v = Operators.gt(arg1, arg2, c);
                break;
            case IN:
                v = Operators.in(arg1, arg2, c);
                break;
            case INSTANCEOF:
                v = Operators.instof(arg1, arg2, c);
                break;
            case LE:
                v = Operators.le(arg1, arg2, c);
                break;
            case LT:
                v = Operators.lt(arg1, arg2, c);
                break;
            case MUL:
                v = Operators.mul(arg1, arg2, c);
                break;
            case NE:
                v = Operators.neq(arg1, arg2, c);
                break;
            case OR:
                v = Operators.or(arg1, arg2, c);
                break;
            case REM:
                v = Operators.rem(arg1, arg2, c);
                break;
            case SEQ:
                v = Operators.stricteq(arg1, arg2);
                break;
            case SHL:
                v = Operators.shl(arg1, arg2, c);
                break;
            case SHR:
                v = Operators.shr(arg1, arg2, c);
                break;
            case SNE:
                v = Operators.strictneq(arg1, arg2);
                break;
            case SUB:
                v = Operators.sub(arg1, arg2, c);
                break;
            case USHR:
                v = Operators.ushr(arg1, arg2, c);
                break;
            case XOR:
                v = Operators.xor(arg1, arg2, c);
                break;
            default:
                throw new AnalysisException();
        }
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            c.getState().writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.1.2 assignment with right-hand-side identifier reference.
     */
    @Override
    public void visit(ReadVariableNode n) {
        String varname = n.getVariableName();
        Value v;
        if (varname.equals("this")) {
            // 11.1.1 read 'this' from the execution context
            v = c.getState().readThis();
            m.visitReadThis(n, v, c.getState(), InitialStateBuilder.GLOBAL);
        } else { // ordinary variable
            int result_base_reg = n.getResultBaseRegister();
            Set<ObjectLabel> base_objs = null;
            if (c.isScanning() || result_base_reg != AbstractNode.NO_VALUE)
                base_objs = newSet();
            v = pv.readVariable(varname, base_objs);
            m.visitPropertyRead(n, base_objs, Value.makeTemporaryStr(varname), c.getState(), true);
            m.visitVariableAsRead(n, v, c.getState());
            m.visitVariableOrProperty(varname, n.getSourceLocation(), v, c.getState().getContext(), c.getState());
            m.visitReadNonThisVariable(n, v);
            if (v.isMaybeAbsent())
                Exceptions.throwReferenceError(c);
            if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
                c.getState().setToNone();
                return;
            }
            if (result_base_reg != AbstractNode.NO_VALUE)
                c.getState().writeRegister(result_base_reg, Value.makeObject(base_objs)); // see 10.1.4
            m.visitRead(n, v, c.getState());
            m.visitReadVariable(n, v, c.getState()); // TODO: combine some of these m.visitXYZ methods?
        }
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            c.getState().writeRegister(n.getResultRegister(), v.restrictToNotAbsent());
    }

    /**
     * 11.13 and 11.1.2 assignment with left-hand-side identifier reference.
     */
    @Override
    public void visit(WriteVariableNode n) {
        Value v = c.getState().readRegister(n.getValueRegister());
        Set<ObjectLabel> objs = pv.writeVariable(n.getVariableName(), v, true);
        Function f = n.getBlock().getFunction();
        if (f.getParameterNames().contains(n.getVariableName())) { // TODO: review
            ObjectLabel arguments_obj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ARGUMENTS);
            pv.writeProperty(arguments_obj, Integer.toString(f.getParameterNames().indexOf(n.getVariableName())), v);
        }
        m.visitPropertyWrite(n, objs, Value.makeTemporaryStr(n.getVariableName()));
        m.visitVariableOrProperty(n.getVariableName(), n.getSourceLocation(), v, c.getState().getContext(), c.getState());
    }

    /**
     * 11.2.1 assignment with right-hand-side property accessor.
     */
    @Override
    public void visit(ReadPropertyNode n) { // FIXME: use ParallelTransfer, github #315
        // get the base value, coerce with ToObject
        Value baseval = c.getState().readRegister(n.getBaseRegister());
        baseval = UnknownValueResolver.getRealValue(baseval, c.getState());
        m.visitPropertyAccess(n, baseval);
        Set<ObjectLabel> objlabels = Conversion.toObjectLabels(n, baseval, c);
        if (objlabels.isEmpty() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToNone();
            return;
        }
        c.getState().writeRegister(n.getBaseRegister(), Value.makeObject(objlabels)); // if null/undefined, an exception would have been thrown via toObjectLabels
        // get the property name value, separate the undefined/null/NaN components, coerce with ToString
        Value propertyval;
        if (n.isPropertyFixed()) {
            propertyval = Value.makeStr(n.getPropertyString());
        } else {
            propertyval = c.getState().readRegister(n.getPropertyRegister());
            propertyval = UnknownValueResolver.getRealValue(propertyval, c.getState());
        }
        boolean maybe_undef = propertyval.isMaybeUndef();
        boolean maybe_null = propertyval.isMaybeNull();
        boolean maybe_nan = propertyval.isMaybeNaN();
        propertyval = propertyval.restrictToNotNullNotUndef().restrictToNotNaN();
        Str propertystr = Conversion.toString(propertyval, c);
        // read the object property value, as fixed property name or unknown property name, and separately for "undefined"/"null"/"NaN"
        Value v;
        boolean read_undefined = false;
        boolean read_null = false;
        boolean read_nan = false;
        if (propertystr.isMaybeSingleStr()) {
            String propertyname = propertystr.getStr();
            if (c.isScanning())
                m.visitReadProperty(n, objlabels, propertystr, maybe_undef || maybe_null || maybe_nan, c.getState(), pv.readPropertyWithAttributes(objlabels, propertystr));
            v = pv.readPropertyValue(objlabels, propertyname);
            m.visitPropertyRead(n, objlabels, propertystr, c.getState(), true);
        } else if (!propertystr.isNotStr()) {
            if (c.isScanning())
                m.visitReadProperty(n, objlabels, propertystr, true, c.getState(), pv.readPropertyWithAttributes(objlabels, propertystr));
            m.visitPropertyRead(n, objlabels, propertystr, c.getState(), true);
            v = pv.readPropertyValue(objlabels, propertystr);
            read_undefined = propertystr.isMaybeStr("undefined");
            read_null = propertystr.isMaybeStr("null");
            read_nan = propertystr.isMaybeStr("NaN");
        } else
            v = Value.makeNone();
        if (maybe_undef && !read_undefined) {
            if (c.isScanning())
                m.visitReadProperty(n, objlabels, Value.makeTemporaryStr("undefined"), true, c.getState(), pv.readPropertyWithAttributes(objlabels, propertystr));
            v = UnknownValueResolver.join(v, pv.readPropertyValue(objlabels, "undefined"), c.getState());
        }
        if (maybe_null && !read_null) {
            if (c.isScanning())
                m.visitReadProperty(n, objlabels, Value.makeTemporaryStr("null"), true, c.getState(), pv.readPropertyWithAttributes(objlabels, propertystr));
            v = UnknownValueResolver.join(v, pv.readPropertyValue(objlabels, "null"), c.getState());
        }
        if (maybe_nan && !read_nan) {
            if (c.isScanning())
                m.visitReadProperty(n, objlabels, Value.makeTemporaryStr("NaN"), true, c.getState(), pv.readPropertyWithAttributes(objlabels, propertystr));
            v = UnknownValueResolver.join(v, pv.readPropertyValue(objlabels, "NaN"), c.getState());
        }
        // remove all the TAJS hooks, which are spurious if accessed through a dynamic property
        if (!n.isPropertyFixed()) {
            v = JSGlobal.removeTAJSSpecificFunctions(v);
        }
        m.visitVariableOrProperty(n.getPropertyString(), n.getSourceLocation(), v, c.getState().getContext(), c.getState());
        m.visitRead(n, v, c.getState());
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToNone();
            return;
        }
        // store the resulting value
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            c.getState().writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.2.1 assignment with left-hand-side property accessor.
     */
    @Override
    public void visit(WritePropertyNode n) {
        // get the base value, coerce with ToObject
        Value baseval = c.getState().readRegister(n.getBaseRegister());
        baseval = UnknownValueResolver.getRealValue(baseval, c.getState());
        m.visitPropertyAccess(n, baseval);
        Set<ObjectLabel> objlabels = Conversion.toObjectLabels(n, baseval, c);
        if (objlabels.isEmpty() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToNone();
            return;
        }
        c.getState().writeRegister(n.getBaseRegister(), Value.makeObject(objlabels)); // if null/undefined, an exception would have been thrown via toObjectLabels
        // get the property name value, separate the undefined/null/NaN components, coerce with ToString
        Value propertyval;
        if (n.isPropertyFixed()) {
            propertyval = Value.makeStr(n.getPropertyString());
        } else {
            propertyval = c.getState().readRegister(n.getPropertyRegister());
            propertyval = UnknownValueResolver.getRealValue(propertyval, c.getState());
        }
        boolean maybe_undef = propertyval.isMaybeUndef();
        boolean maybe_null = propertyval.isMaybeNull();
        boolean maybe_nan = propertyval.isMaybeNaN();
        propertyval = propertyval.restrictToNotNullNotUndef().restrictToNotNaN();
        Value propertystr = Conversion.toString(propertyval, c);
        if ((propertystr.isNone() && !maybe_undef && !maybe_null && !maybe_nan) && !Options.get().isPropagateDeadFlow()) { // TODO: maybe need more aborts like this one?
            c.getState().setToNone();
            return;
        }

        Value v = c.getState().readRegister(n.getValueRegister());
        switch (n.getKind()) {
            case GETTER:
                v = v.makeGetter();
                break;
            case SETTER:
                v = v.makeSetter();
                break;
            case ORDINARY:
                // do nothing
                break;
            default:
                throw new AnalysisException("Unexpected case: " + n.getKind());
        }
        // write the object property value, and separately for "undefined"/"null"/"NaN"
        ParallelTransfer pt = new ParallelTransfer(c);
        Value finalV = v;
        if (!propertystr.isNone())
            pt.add(() -> pv.writeProperty(objlabels, propertystr, finalV, false, n.isDecl()));
        if (maybe_undef && !propertystr.isMaybeStr("undefined"))
            pt.add(() -> pv.writeProperty(objlabels, Value.makeTemporaryStr("undefined"), finalV, false, n.isDecl()));
        if (maybe_null && !propertystr.isMaybeStr("null"))
            pt.add(() -> pv.writeProperty(objlabels, Value.makeTemporaryStr("null"), finalV, false, n.isDecl()));
        if (maybe_nan && !propertystr.isMaybeStr("NaN"))
            pt.add(() -> pv.writeProperty(objlabels, Value.makeTemporaryStr("NaN"), finalV, false, n.isDecl()));
        pt.complete();
        m.visitPropertyWrite(n, objlabels, propertystr); // TODO: more monitoring around here?
        if (Options.get().isEvalStatistics()
                && propertystr.getStr() != null
                && propertystr.getStr().equals("innerHTML")) {
            m.visitInnerHTMLWrite(n, v);
        }
        m.visitVariableOrProperty(n.getPropertyString(), n.getSourceLocation(), v, c.getState().getContext(), c.getState());
    }

    /**
     * 11.13 and 11.4.1 assignment with 'delete' operator.
     */
    @Override
    public void visit(DeletePropertyNode n) {
        Value v;
        if (n.isVariable()) {
            v = pv.deleteVariable(n.getVariableName());
            m.visitVariableOrProperty(n.getVariableName(), n.getSourceLocation(), v, c.getState().getContext(), c.getState());
        } else {
            Value baseval = c.getState().readRegister(n.getBaseRegister());
            baseval = UnknownValueResolver.getRealValue(baseval, c.getState());
            m.visitPropertyAccess(n, baseval);
            if (baseval.isMaybeNull() || baseval.isMaybeUndef()) {
                Exceptions.throwTypeError(c);
                if (baseval.isNullOrUndef() && !Options.get().isPropagateDeadFlow()) {
                    c.getState().setToNone();
                    return;
                }
            }
            baseval = baseval.restrictToNotNullNotUndef();
            c.getState().writeRegister(n.getBaseRegister(), baseval);
            Set<ObjectLabel> objlabels = Conversion.toObjectLabels(n, baseval, c);
            if (objlabels.isEmpty() && !Options.get().isPropagateDeadFlow()) {
                c.getState().setToNone();
                return;
            }
            Str propertystr;
            if (n.isPropertyFixed()) {
                propertystr = Value.makeStr(n.getPropertyString());
            } else {
                Value propertyval = c.getState().readRegister(n.getPropertyRegister());
                propertystr = Conversion.toString(propertyval, c);
            }
            v = pv.deleteProperty(objlabels, propertystr, false);
        }
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            c.getState().writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.13 and 11.4.3 assignment with 'typeof' operator.
     */
    @Override
    public void visit(TypeofNode n) {
        Value v;
        if (n.isVariable()) {
            Value val = pv.readVariable(n.getVariableName(), null); // TODO: should also count as a variable read in Monitoring?
            val = UnknownValueResolver.getRealValue(val, c.getState());
            v = Operators.typeof(val, val.isMaybeAbsent());
            m.visitVariableOrProperty(n.getVariableName(), n.getOperandSourceLocation(), val, c.getState().getContext(), c.getState());
        } else {
            Value val = c.getState().readRegister(n.getArgRegister());
            val = UnknownValueResolver.getRealValue(val, c.getState());
            v = Operators.typeof(val, false);
        }
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            c.getState().writeRegister(n.getResultRegister(), v);
    }

    /**
     * 12.5 and 12.6 'if'/iteration statement.
     */
    @Override
    public void visit(IfNode n) {
        // do nothing (but see EdgeTransfer)
        Value val = c.getState().readRegister(n.getConditionRegister());
        val = UnknownValueResolver.getRealValue(val, c.getState());
        m.visitIf(n, Conversion.toBoolean(val));
    }

    /**
     * 13 function definition.
     */
    @Override
    public void visit(DeclareFunctionNode n) {
        UserFunctionCalls.declareFunction(n, c);
    }

    /**
     * 11.2.2, 11.2.3, 13.2.1, and 13.2.2 'new' / function call.
     */
    @Override
    public void visit(CallNode n) {
        if (n.getFunctionRegister() != AbstractNode.NO_VALUE) // old style call (where the function is given as a variable read)
            FunctionCalls.callFunction(new OrdinaryCallInfo(n, c.getState()) {

                @Override
                public Value getFunctionValue() {
                    Value functionValue = c.getState().readRegister(n.getFunctionRegister());
                    if (n.getLiteralConstructorKind() != null) {
                        // these literal invocations can not be spurious in ES5
                        switch (n.getLiteralConstructorKind()) {
                            case ARRAY:
                                functionValue = Value.makeObject(new ObjectLabel(ECMAScriptObjects.ARRAY, Kind.FUNCTION));
                                break;
                            case REGEXP:
                                functionValue = Value.makeObject(new ObjectLabel(ECMAScriptObjects.REGEXP, Kind.FUNCTION));
                                break;
                            default:
                                throw new AnalysisException("Unhandled literal constructor type: " + n.getLiteralConstructorKind());
                        }
                    }
                    return functionValue;
                }

                @Override
                public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
                    return UserFunctionCalls.determineThis(n, caller_state, callee_state, c, n.getBaseRegister());
                }
            }, c);
        else { // getPropertyString / getPropertyRegister - like ReadPropertyNode
            Value baseval = c.getState().readRegister(n.getBaseRegister());
            baseval = UnknownValueResolver.getRealValue(baseval, c.getState());
            Set<ObjectLabel> objlabels = baseval.getObjectLabels(); // the ReadPropertyNode has updated baseval to account for ToObject
            Value propertyval;
            if (n.isPropertyFixed()) {
                propertyval = Value.makeStr(n.getPropertyString());
            } else {
                propertyval = c.getState().readRegister(n.getPropertyRegister());
                propertyval = UnknownValueResolver.getRealValue(propertyval, c.getState());
            }
            boolean maybe_undef = propertyval.isMaybeUndef();
            boolean maybe_null = propertyval.isMaybeNull();
            boolean maybe_nan = propertyval.isMaybeNaN();
            propertyval = propertyval.restrictToNotNullNotUndef().restrictToNotNaN();
            Str propertystr = Conversion.toString(propertyval, c);
            // read the object property value, as fixed property name or unknown property name, and separately for "undefined"/"null"/"NaN"
            Map<ObjectLabel, Set<ObjectLabel>> target2this = newMap();
            List<Value> nonfunctions = newList();
            for (ObjectLabel objlabel : objlabels) { // find possible targets for each possible base object
                Set<ObjectLabel> singleton = singleton(objlabel);
                Value v;
                boolean read_undefined = false;
                boolean read_null = false;
                boolean read_nan = false;
                if (propertystr.isMaybeSingleStr()) {
                    String propertyname = propertystr.getStr();
                    v = pv.readPropertyValue(singleton, propertyname);
                } else if (!propertystr.isNotStr()) {
                    v = pv.readPropertyValue(singleton, propertystr);
                    read_undefined = propertystr.isMaybeStr("undefined");
                    read_null = propertystr.isMaybeStr("null");
                    read_nan = propertystr.isMaybeStr("NaN");
                } else
                    v = Value.makeNone();
                if (maybe_undef && !read_undefined) {
                    v = UnknownValueResolver.join(v, pv.readPropertyValue(singleton, "undefined"), c.getState());
                }
                if (maybe_null && !read_null) {
                    v = UnknownValueResolver.join(v, pv.readPropertyValue(singleton, "null"), c.getState());
                }
                if (maybe_nan && !read_nan) {
                    v = UnknownValueResolver.join(v, pv.readPropertyValue(singleton, "NaN"), c.getState());
                }
                v = UnknownValueResolver.getRealValue(v, c.getState());

                // finally, remove all the TAJS hooks, which are spurious if accessed through a dynamic property
                if (!n.isPropertyFixed()) {
                    v = JSGlobal.removeTAJSSpecificFunctions(v);
                }

                for (ObjectLabel target : v.getObjectLabels()) {
                    if (target.getKind() == Kind.FUNCTION) {
                        addToMapSet(target2this, target, objlabel);
                    } else {
                        nonfunctions.add(Value.makeObject(target));
                    }
                }
                if (v.isMaybePrimitive()) {
                    nonfunctions.add(v.restrictToNotObject());
                }
            }
            // do calls to each target with the corresponding values of this
            for (Entry<ObjectLabel, Set<ObjectLabel>> me : target2this.entrySet()) {
                ObjectLabel target = me.getKey();
                Set<ObjectLabel> this_objs = me.getValue();
                FunctionCalls.callFunction(new OrdinaryCallInfo(n, c.getState()) {

                    @Override
                    public Value getFunctionValue() {
                        return Value.makeObject(target);
                    }

                    @Override
                    public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
                        return this_objs;
                    }
                }, c);
            }
            // also model calls to non-function values
            FunctionCalls.callFunction(new OrdinaryCallInfo(n, c.getState()) {

                @Override
                public Value getFunctionValue() {
                    return Value.join(nonfunctions);
                }

                @Override
                public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
                    return Collections.emptySet();
                }
            }, c);
        }
    }

    /**
     * 12.9 and 13.2.1 'return' statement.
     */
    @Override
    public void visit(ReturnNode n) {
        transferReturn(n.getReturnValueRegister(), n.getBlock(), c.getState(), null, null, false);
    }

    /**
     * Transfer for a return statement.
     *
     * @param caller if non-null, only consider this caller
     */
    private void transferReturn(int valueReg, BasicBlock block, State state, NodeAndContext<Context> caller, Context edge_context, boolean implicit) {
        Value v;
        if (valueReg != AbstractNode.NO_VALUE)
            v = state.readRegister(valueReg);
        else
            v = Value.makeUndef();
        UserFunctionCalls.leaveUserFunction(v, false, block.getFunction(), state, c, caller, edge_context, implicit);
    }

    /**
     * 13.2.1 exceptional return.
     */
    @Override
    public void visit(ExceptionalReturnNode n) {
        transferExceptionalReturn(n.getBlock(), c.getState(), null, null, false);
    }

    /**
     * Transfer for an exceptional-return statement.
     *
     * @param caller if non-null, only consider this caller
     */
    private void transferExceptionalReturn(BasicBlock block, State state, NodeAndContext<Context> caller, Context edge_context, boolean implicit) {
        Value v = state.readRegister(AbstractNode.EXCEPTION_REG);
        state.removeRegister(AbstractNode.EXCEPTION_REG);
        UserFunctionCalls.leaveUserFunction(v, true, block.getFunction(), state, c, caller, edge_context, implicit);
    }

    /**
     * 12.13 'throw' statement.
     */
    @Override
    public void visit(ThrowNode n) {
        Value v = c.getState().readRegister(n.getValueRegister());
        Exceptions.throwException(c.getState().clone(), v, c, n);
        c.getState().setToNone();
    }

    /**
     * 12.14 'catch' block.
     */
    @Override
    public void visit(CatchNode n) {
        Value v = c.getState().readRegister(AbstractNode.EXCEPTION_REG);
        c.getState().removeRegister(AbstractNode.EXCEPTION_REG);
        if (n.getValueRegister() != AbstractNode.NO_VALUE) {
            c.getState().writeRegister(n.getValueRegister(), v.makeExtendedScope());
        } else {
            ObjectLabel objlabel = new ObjectLabel(n, Kind.OBJECT);
            c.getState().newObject(objlabel);
            c.getState().writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
            pv.writePropertyWithAttributes(objlabel, n.getVariableName(), v.setAttributes(false, true, false));
            c.getState().writeRegister(n.getScopeObjRegister(), Value.makeObject(objlabel));
            /* From ES5, Annex D:
             In Edition 3, an object is created, as if by new Object() to serve as the scope for resolving 
             the name of the exception parameter passed to a catch clause of a try statement. If the actual 
             exception object is a function and it is called from within the catch clause, the scope object 
             will be passed as the this value of the call. The body of the function can then define new 
             properties on its this value and those property names become visible identifiers bindings 
             within the scope of the catch clause after the function returns. In Edition 5, when an 
             exception parameter is called as a function, undefined is passed as the this value.
             */ // TODO: use ES5 semantics of catch object?
        }
    }

    /**
     * 12.10 enter 'with' statement.
     */
    @Override
    public void visit(BeginWithNode n) {
        Value v = c.getState().readRegister(n.getObjectRegister());
        v = UnknownValueResolver.getRealValue(v, c.getState());
        Set<ObjectLabel> objs = Conversion.toObjectLabels(n, v, c);
        if (objs.isEmpty() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToNone();
            return;
        }
        c.getState().pushScopeChain(objs);
    }

    /**
     * 12.10 leave 'with' statement.
     */
    @Override
    public void visit(EndWithNode n) {
        c.getState().popScopeChain();
    }

    /**
     * 12.6.4 begin 'for-in' statement.
     */
    @Override
    public void visit(BeginForInNode n) {
        if (!Options.get().isForInSpecializationDisabled()) {
            // 1. Find properties to iterate through
            Value v1 = c.getState().readRegister(n.getObjectRegister());
            c.getState().writeRegister(n.getObjectRegister(), v1.makeExtendedScope()); // preserve the register value
            v1 = UnknownValueResolver.getRealValue(v1, c.getState());
            Set<ObjectLabel> objs = Conversion.toObjectLabels(n, v1, c);
            State.Properties p = c.getState().getEnumProperties(objs);
            Collection<Value> propertyNameValues = newList(p.toValues());

            // Add the no-iteration case
            propertyNameValues.add(Value.makeNull().makeExtendedScope());

            // 2. Make specialized context for each iteration
            int it = n.getPropertyListRegister();
//            List<Context> specialized_contexts = newList();
            BasicBlock successor = n.getBlock().getSingleSuccessor();
            for (Value k : propertyNameValues) {
                m.visitPropertyRead(n, objs, k, c.getState(), true);
                if (!c.isScanning()) {
                    // 2.1 Make specialized context
                    State specialized_state = c.getState().clone();
                    specialized_state.writeRegister(it, k);
                    Context specialized_context = c.getAnalysis().getContextSensitivityStrategy().makeForInEntryContext(c.getState().getContext(), n, k);
//                    specialized_contexts.add(specialized_context);
                    // 2.2  Propagate specialized context
                    c.propagateToFunctionEntry(n, c.getState().getContext(), specialized_state, specialized_context, successor, false);
                }
            }
            if (!c.isScanning()) {
                // TODO: could kill flow to specializations if covered by other context
                // List<Context> previousSpecializations = c.getAnalysis().getForInSpecializations(n, s.getContext());
                // if (previousSpecializations != null && (p.isArray() || p.isNonArray())) {
                //    previousSpecializations.forEach(c -> {
                //        // covered.setToNone();
                //    });
                //  }

                // TODO: could kill null flow unless all iterations has reached at least one EndForInNode
            }
            c.getState().setToNone();
        } else { // fall back to simple mode without context specialization
            Value v1 = c.getState().readRegister(n.getObjectRegister());
            c.getState().writeRegister(n.getObjectRegister(), v1.makeExtendedScope()); // preserve the register value
            v1 = UnknownValueResolver.getRealValue(v1, c.getState());
            Set<ObjectLabel> objs = Conversion.toObjectLabels(n, v1, c);
            Properties p = c.getState().getEnumProperties(objs);
            Value proplist = p.toValue().joinNull();
            m.visitPropertyRead(n, objs, proplist, c.getState(), true);
            c.getState().writeRegister(n.getPropertyListRegister(), proplist);
        }
    }

    /**
     * 12.6.4 get next property of 'for-in' statement.
     */
    @Override
    public void visit(NextPropertyNode n) {
        Value property_name = c.getState().readRegister(n.getPropertyListRegister()).restrictToStr(); // restrictToStr to remove Null (the end-of-list marker)
        if (property_name.isNone()) { // possible if branch pruning in EdgeTransfer is disabled
            c.getState().setToNone();
            return;
        }
        c.getState().writeRegister(n.getPropertyRegister(), property_name);
    }

    /**
     * 12.6.4 check for more properties of 'for-in' statement.
     */
    @Override
    public void visit(HasNextPropertyNode n) {
        Value v = UnknownValueResolver.getRealValue(c.getState().readRegister(n.getPropertyListRegister()), c.getState());
        Value res = !v.isNotStr() ? Value.makeBool(true) : Value.makeNone(); // string values represent property names
        if (v.isMaybeNull()) // null marks end-of-list
            res = res.joinBool(false);
        c.getState().writeRegister(n.getResultRegister(), res);
    }

    /**
     * 12.6.4 end of loop of 'for-in' statement.
     */
    @Override
    public void visit(EndForInNode n) {
        if (!Options.get().isForInSpecializationDisabled()) {
            if (!c.isScanning()) {
                // 1. Find successor block, context and base-state
                for (CallGraph.ReverseEdge<Context> re : c.getAnalysisLatticeElement().getCallGraph().getSources(BlockAndContext.makeEntry(c.getState().getBasicBlock(), c.getState().getContext()))) {
                    Context beginContext = re.getCallerContext();
                    Context edgeContext = re.getEdgeContext();

                    // 2. Use State.mergeFunctionReturn to do the merge
                    State beginState = c.getAnalysisLatticeElement().getState(n.getBeginNode().getBlock(), beginContext);
                    State beginEntryState = c.getAnalysisLatticeElement().getState(BlockAndContext.makeEntry(n.getBeginNode().getBlock(), beginContext));
                    State edgeState = c.getAnalysisLatticeElement().getCallGraph().getCallEdge(n.getBeginNode(), beginContext, c.getState().getBasicBlock().getEntryBlock(), edgeContext).getState();
                    State nonSpecializedMergeState = c.getState().clone();
                    Value returnValue = c.getState().hasReturnRegisterValue() ? c.getState().readRegister(AbstractNode.RETURN_REG) : null;
                    Value exValue = c.getState().hasExceptionRegisterValue() ? c.getState().readRegister(AbstractNode.EXCEPTION_REG) : null;
                    returnValue = UserFunctionCalls.mergeFunctionReturn(nonSpecializedMergeState, beginState, edgeState, beginEntryState, nonSpecializedMergeState.getSummarized(), returnValue, exValue);
                    if (returnValue != null) {
                        nonSpecializedMergeState.writeRegister(AbstractNode.RETURN_REG, returnValue);
                    }

                    // 3. Propagate only to the non-specialized successor
                    nonSpecializedMergeState.setBasicBlock(c.getState().getBasicBlock().getSingleSuccessor()); // change location so that values will get recovered correctly
                    nonSpecializedMergeState.setContext(beginContext);
                    c.propagateToBasicBlock(nonSpecializedMergeState, c.getState().getBasicBlock().getSingleSuccessor(), beginContext);
                }
                c.getState().setToNone();
            }
        }
    }

    /**
     * Beginning of loop.
     */
    @Override
    public void visit(BeginLoopNode n) {
        // TODO: do nothing if loop unrolling is disabled or in scanning mode

        Value v = c.getState().readRegister(n.getIfNode().getConditionRegister());
        v = Conversion.toBoolean(UnknownValueResolver.getRealValue(v, c.getState()));
        if (v.isMaybeTrueButNotFalse() || v.isMaybeFalseButNotTrue()) {
            // branch condition is determinate, switch context and propagate only to specialized successor
            Context specializedContext = c.getAnalysis().getContextSensitivityStrategy().makeNextLoopUnrollingContext(c.getState().getContext(), n);
            c.propagateToBasicBlock(c.getState().clone(), c.getState().getBasicBlock().getSingleSuccessor(), specializedContext);
            c.getState().setToNone();
        } // otherwise, just ordinary propagation like no-op
    }

    /**
     * End of loop.
     */
    @Override
    public void visit(EndLoopNode n) {
        // TODO: do nothing if loop unrolling is disabled or in scanning mode

        // branch condition is determinate, switch context and propagate only to generalized successor
        Context generalizedContext = c.getAnalysis().getContextSensitivityStrategy().makeLoopExitContext(c.getState().getContext(), n);
        c.propagateToBasicBlock(c.getState().clone(), c.getState().getBasicBlock().getSingleSuccessor(), generalizedContext);
        c.getState().setToNone();
    }

    /**
     * Assumption.
     */
    @Override
    public void visit(AssumeNode n) {
        if (Options.get().isControlSensitivityDisabled())
            return;
        switch (n.getKind()) {

            case VARIABLE_NON_NULL_UNDEF: {
                Value v = pv.readVariable(n.getVariableName(), null);
                v = UnknownValueResolver.getRealValue(v, c.getState()); // TODO: limits use of polymorphic values?
                v = v.restrictToNotNullNotUndef().restrictToNotAbsent();
                if (v.isNotPresent() && !Options.get().isPropagateDeadFlow())
                    c.getState().setToNone();
                else
                    pv.writeVariable(n.getVariableName(), v, false);
                break;
            }

            case PROPERTY_NON_NULL_UNDEF: {
                String propname;
                if (n.isPropertyFixed())
                    propname = n.getPropertyString();
                else {
                    Value propval = c.getState().readRegister(n.getPropertyRegister());
                    propval = UnknownValueResolver.getRealValue(propval, c.getState()); // TODO: limits use of polymorphic values?
                    if (!propval.isMaybeSingleStr() || propval.isMaybeOtherThanStr())
                        break; // safe to do nothing here if it gets complicated
                    propname = propval.getStr();
                }
                Set<ObjectLabel> baseobjs = Conversion.toObjectLabels(n, c.getState().readRegister(n.getBaseRegister()), null); // TODO: omitting side-effects here
                Value v = pv.readPropertyWithAttributes(baseobjs, propname);
                if (v.isNotPresent() && !Options.get().isPropagateDeadFlow())
                    c.getState().setToNone();
                else if (baseobjs.size() == 1 && baseobjs.iterator().next().isSingleton()) {
                    v = UnknownValueResolver.getRealValue(v, c.getState()); // TODO: limits use of polymorphic values?
                    v = v.restrictToNotNullNotUndef().restrictToNotAbsent();
                    pv.writePropertyWithAttributes(baseobjs, propname, v, false, true);
                }
                break;
            }

            case UNREACHABLE: {
                if (Options.get().isIgnoreUnreachableEnabled()) {
                    c.getState().setToNone();
                }
                break;
            }

            default:
                throw new AnalysisException("Unhandled AssumeNode kind: " + n.getKind());
        }
    }

    @Override
    public void visit(EventDispatcherNode n) {
        if (Options.get().isDOMEnabled()) {
            DOMEvents.emit(n, c);
        }
        if (Options.get().isAsyncEventsEnabled()) {
            AsyncEvents.emit(n, c);
        }
    }
}
