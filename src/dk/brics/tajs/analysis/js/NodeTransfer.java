/*
 * Copyright 2009-2015 Aarhus University
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

import dk.brics.tajs.analysis.Context;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.OrdinaryCallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMEventLoop;
import dk.brics.tajs.analysis.nativeobjects.JSGlobal;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.AssumeNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginWithNode;
import dk.brics.tajs.flowgraph.jsnodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.CatchNode;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.DeletePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.EndForInNode;
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
import dk.brics.tajs.lattice.BlockState;
import dk.brics.tajs.lattice.BlockState.Properties;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.Options;
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
public class NodeTransfer implements NodeVisitor<State> {

    private Solver.SolverInterface c;

    private IAnalysisMonitoring<State, Context, CallEdge<State>> m;

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
    }

    /**
     * Transfer ordinary and exceptional return for the given call node and callee entry.
     */
    public void transferReturn(AbstractNode call_node, BasicBlock callee_entry, Context caller_context, Context callee_context,
                               Context edge_context) {
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
                    transferReturn(returnNode.getReturnValueRegister(), ordinary_exit, ordinary_exit_state.clone(), caller, edge_context);
                } else
                    throw new AnalysisException("ReturnNode expected");
            }
            if (exceptional_exit_state != null) {
                transferExceptionalReturn(exceptional_exit, exceptional_exit_state.clone(), caller, edge_context);
            }
        }
    }

    /**
     * 12.3 empty statement.
     */
    @Override
    public void visit(NopNode n, State state) {
        // do nothing
    }

    /**
     * 12.2 variable declaration.
     */
    @Override
    public void visit(DeclareVariableNode n, State state) {
        state.declareAndWriteVariable(n.getVariableName(), Value.makeUndef(), false);
    }

    /**
     * 11.13 and 7.8 assignment with literal.
     */
    @Override
    public void visit(ConstantNode n, State state) {
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
            state.writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.1.5 object initializer.
     */
    @Override
    public void visit(NewObjectNode n, State state) {
        ObjectLabel objlabel = new ObjectLabel(n, Kind.OBJECT);
        state.newObject(objlabel);
        state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), Value.makeObject(objlabel));
    }

    /**
     * 11.13 and 11.4 assignment with unary operator.
     */
    @Override
    public void visit(UnaryOperatorNode n, State state) {
        Value arg = state.readRegister(n.getArgRegister());
        arg = UnknownValueResolver.getRealValue(arg, state);
        Value v;
        switch (n.getOperator()) {
            case COMPLEMENT:
                v = Operators.complement(arg, n.getArgRegister(), c);
                break;
            case MINUS:
                v = Operators.uminus(arg, n.getArgRegister(), c);
                break;
            case NOT:
                v = Operators.not(arg, c);
                break;
            case PLUS:
                v = Operators.uplus(arg, n.getArgRegister(), c);
                break;
            default:
                throw new AnalysisException();
        }
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.13 and 11.5/6/7/8 assignment with binary operator.
     */
    @Override
    public void visit(BinaryOperatorNode n, State state) {
        Value arg1 = state.readRegister(n.getArg1Register());
        Value arg2 = state.readRegister(n.getArg2Register());
        arg1 = UnknownValueResolver.getRealValue(arg1, state);
        arg2 = UnknownValueResolver.getRealValue(arg2, state);
        Value v;
        switch (n.getOperator()) {
            case ADD:
                v = Operators.add(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c); // TODO: test07.js could improve messages if keeping conversions of the two args separate
                break;
            case AND:
                v = Operators.and(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case DIV:
                v = Operators.div(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case EQ:
                v = Operators.eq(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case GE:
                v = Operators.ge(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case GT:
                v = Operators.gt(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case IN:
                v = Operators.in(state, arg1, n.getArg1Register(), arg2, c);
                break;
            case INSTANCEOF:
                v = Operators.instof(state, arg1, arg2, c);
                break;
            case LE:
                v = Operators.le(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case LT:
                v = Operators.lt(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case MUL:
                v = Operators.mul(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case NE:
                v = Operators.neq(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case OR:
                v = Operators.or(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case REM:
                v = Operators.rem(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case SEQ:
                v = Operators.stricteq(arg1, arg2, c);
                break;
            case SHL:
                v = Operators.shl(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case SHR:
                v = Operators.shr(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case SNE:
                v = Operators.strictneq(arg1, arg2, c);
                break;
            case SUB:
                v = Operators.sub(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case USHR:
                v = Operators.ushr(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case XOR:
                v = Operators.xor(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            default:
                throw new AnalysisException();
        }
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.1.2 assignment with right-hand-side identifier reference.
     */
    @Override
    public void visit(ReadVariableNode n, State state) {
        String varname = n.getVariableName();
        Value v;
        if (varname.equals("this")) {
            // 11.1.1 read 'this' from the execution context
            v = state.readThis();
            m.visitReadThis(n, v, state, InitialStateBuilder.GLOBAL);
        } else { // ordinary variable
            int result_base_reg = n.getResultBaseRegister();
            Set<ObjectLabel> base_objs = null;
            if (c.isScanning() || result_base_reg != AbstractNode.NO_VALUE)
                base_objs = newSet();
            v = state.readVariable(varname, base_objs);
            m.visitPropertyRead(n, base_objs, Value.makeTemporaryStr(varname), state, true);
            m.visitVariableAsRead(n, v, state);
            m.visitVariableOrProperty(varname, n.getSourceLocation(), v, state.getContext(), state);
            m.visitReadNonThisVariable(n, v);
            if (v.isMaybeAbsent())
                Exceptions.throwReferenceError(state, c);
            if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
                state.setToNone();
                return;
            }
            if (result_base_reg != AbstractNode.NO_VALUE)
                state.writeRegister(result_base_reg, Value.makeObject(base_objs)); // see 10.1.4
            m.visitRead(n, v, state);
            m.visitReadVariable(n, v, state); // TODO: combine some of these m.visitXYZ methods?
        }
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v.restrictToNotAbsent());
    }

    /**
     * 11.13 and 11.1.2 assignment with left-hand-side identifier reference.
     */
    @Override
    public void visit(WriteVariableNode n, State state) {
        Value v = state.readRegister(n.getValueRegister());
        m.visitWriteVariable(n, v, state);
        Set<ObjectLabel> objs = state.writeVariable(n.getVariableName(), v, true);
        Function f = n.getBlock().getFunction();
        if (f.getParameterNames().contains(n.getVariableName())) { // TODO: review
            ObjectLabel arguments_obj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ARGUMENTS);
            state.writeProperty(arguments_obj, Integer.toString(f.getParameterNames().indexOf(n.getVariableName())), v);
        }
        m.visitPropertyWrite(n, objs, Value.makeTemporaryStr(n.getVariableName()));
        m.visitVariableOrProperty(n.getVariableName(), n.getSourceLocation(), v, state.getContext(), state);
    }

    /**
     * 11.2.1 assignment with right-hand-side property accessor.
     */
    @Override
    public void visit(ReadPropertyNode n, State state) {
        // get the base value, coerce with ToObject
        Value baseval = state.readRegister(n.getBaseRegister());
        baseval = UnknownValueResolver.getRealValue(baseval, state);
        m.visitPropertyAccess(n, baseval);
        Set<ObjectLabel> objlabels = Conversion.toObjectLabels(state, n, baseval, c);
        if (objlabels.isEmpty() && !Options.get().isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        state.writeRegister(n.getBaseRegister(), Value.makeObject(objlabels)); // if null/undefined, an exception would have been thrown via toObjectLabels
        // get the property name value, separate the undefined/null/NaN components, coerce with ToString
        Value propertyval;
        int propertyreg;
        if (n.isPropertyFixed()) {
            propertyreg = AbstractNode.NO_VALUE;
            propertyval = Value.makeStr(n.getPropertyString());
        } else {
            propertyreg = n.getPropertyRegister();
            propertyval = state.readRegister(propertyreg);
            propertyval = UnknownValueResolver.getRealValue(propertyval, state);
        }
        boolean maybe_undef = propertyval.isMaybeUndef();
        boolean maybe_null = propertyval.isMaybeNull();
        boolean maybe_nan = propertyval.isMaybeNaN();
        propertyval = propertyval.restrictToNotNullNotUndef().restrictToNotNaN();
        Str propertystr = Conversion.toString(propertyval, propertyreg, c);
        // read the object property value, as fixed property name or unknown property name, and separately for "undefined"/"null"/"NaN"
        Value v;
        boolean read_undefined = false;
        boolean read_null = false;
        boolean read_nan = false;
        if (propertystr.isMaybeSingleStr()) {
            String propertyname = propertystr.getStr();
            m.visitReadProperty(n, objlabels, propertystr, maybe_undef || maybe_null || maybe_nan, state);
            v = state.readPropertyValue(objlabels, propertyname);
            m.visitPropertyRead(n, objlabels, propertystr, state, true);
        } else if (!propertystr.isNotStr()) {
            m.visitReadProperty(n, objlabels, propertystr, true, state);
            m.visitPropertyRead(n, objlabels, propertystr, state, true);
            v = state.readPropertyValue(objlabels, propertystr);
            read_undefined = propertystr.isMaybeStr("undefined");
            read_null = propertystr.isMaybeStr("null");
            read_nan = propertystr.isMaybeStr("NaN");
        } else
            v = Value.makeNone();
        if (maybe_undef && !read_undefined) {
            m.visitReadProperty(n, objlabels, Value.makeTemporaryStr("undefined"), true, state);
            v = UnknownValueResolver.join(v, state.readPropertyValue(objlabels, "undefined"), state);
        }
        if (maybe_null && !read_null) {
            m.visitReadProperty(n, objlabels, Value.makeTemporaryStr("null"), true, state);
            v = UnknownValueResolver.join(v, state.readPropertyValue(objlabels, "null"), state);
        }
        if (maybe_nan && !read_nan) {
            m.visitReadProperty(n, objlabels, Value.makeTemporaryStr("NaN"), true, state);
            v = UnknownValueResolver.join(v, state.readPropertyValue(objlabels, "NaN"), state);
        }
        // remove all the TAJS hooks, which are spurious if accessed through a dynamic property
        if (!n.isPropertyFixed()) {
            v = JSGlobal.removeTAJSSpecificFunctions(v);
        }
        m.visitVariableOrProperty(n.getPropertyString(), n.getSourceLocation(), v, state.getContext(), state);
        m.visitRead(n, v, state);
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        // store the resulting value
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.2.1 assignment with left-hand-side property accessor.
     */
    @Override
    public void visit(WritePropertyNode n, State state) {
        // get the base value, coerce with ToObject
        Value baseval = state.readRegister(n.getBaseRegister());
        baseval = UnknownValueResolver.getRealValue(baseval, state);
        m.visitPropertyAccess(n, baseval);
        Set<ObjectLabel> objlabels = Conversion.toObjectLabels(state, n, baseval, c);
        if (objlabels.isEmpty() && !Options.get().isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        state.writeRegister(n.getBaseRegister(), Value.makeObject(objlabels)); // if null/undefined, an exception would have been thrown via toObjectLabels
        // get the property name value, separate the undefined/null/NaN components, coerce with ToString
        Value propertyval;
        int propertyreg;
        if (n.isPropertyFixed()) {
            propertyreg = AbstractNode.NO_VALUE;
            propertyval = Value.makeStr(n.getPropertyString());
        } else {
            propertyreg = n.getPropertyRegister();
            propertyval = state.readRegister(propertyreg);
            propertyval = UnknownValueResolver.getRealValue(propertyval, state);
        }
        boolean maybe_undef = propertyval.isMaybeUndef();
        boolean maybe_null = propertyval.isMaybeNull();
        boolean maybe_nan = propertyval.isMaybeNaN();
        propertyval = propertyval.restrictToNotNullNotUndef().restrictToNotNaN();
        Str propertystr = Conversion.toString(propertyval, propertyreg, c);
        // get the value to be written
        Value v = state.readRegister(n.getValueRegister());
        NativeFunctions.updateArrayLength(n, state, objlabels, propertystr, v, n.getValueRegister(), c);
        // write the object property value, as fixed property name or unknown property name, and separately for "undefined"/"null"/"NaN"
        state.writeProperty(objlabels, propertystr, v, true, maybe_undef || maybe_null || maybe_nan);
        if (maybe_undef && !propertystr.isMaybeStr("undefined"))
            state.writeProperty(objlabels, Value.makeTemporaryStr("undefined"), v, true, !propertystr.isNotStr());
        if (maybe_null && !propertystr.isMaybeStr("null"))
            state.writeProperty(objlabels, Value.makeTemporaryStr("null"), v, true, !propertystr.isNotStr());
        if (maybe_nan && !propertystr.isMaybeStr("NaN"))
            state.writeProperty(objlabels, Value.makeTemporaryStr("NaN"), v, true, !propertystr.isNotStr());
        m.visitPropertyWrite(n, objlabels, propertystr); // TODO: more monitoring around here?
        if (Options.get().isEvalStatistics()
                && propertystr.getStr() != null
                && propertystr.getStr().equals("innerHTML")) {
            m.visitInnerHTMLWrite(n, v);
        }
        m.visitVariableOrProperty(n.getPropertyString(), n.getSourceLocation(), v, state.getContext(), state);
    }

    /**
     * 11.13 and 11.4.1 assignment with 'delete' operator.
     */
    @Override
    public void visit(DeletePropertyNode n, State state) {
        Value v;
        if (n.isVariable()) {
            v = state.deleteVariable(n.getVariableName());
            m.visitVariableOrProperty(n.getVariableName(), n.getSourceLocation(), v, state.getContext(), state);
        } else {
            Value baseval = state.readRegister(n.getBaseRegister());
            baseval = UnknownValueResolver.getRealValue(baseval, state);
            m.visitPropertyAccess(n, baseval);
            if (baseval.isMaybeNull() || baseval.isMaybeUndef()) {
                Exceptions.throwTypeError(state, c);
                if (baseval.isNullOrUndef() && !Options.get().isPropagateDeadFlow()) {
                    state.setToNone();
                    return;
                }
            }
            baseval = baseval.restrictToNotNullNotUndef();
            state.writeRegister(n.getBaseRegister(), baseval);
            Set<ObjectLabel> objlabels = Conversion.toObjectLabels(state, n, baseval, c);
            if (objlabels.isEmpty() && !Options.get().isPropagateDeadFlow()) {
                state.setToNone();
                return;
            }
            Str propertystr;
            if (n.isPropertyFixed()) {
                propertystr = Value.makeStr(n.getPropertyString());
            } else {
                Value propertyval = state.readRegister(n.getPropertyRegister());
                propertystr = Conversion.toString(propertyval, n.getPropertyRegister(), c);
            }
            v = state.deleteProperty(objlabels, propertystr,false);
        }
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.13 and 11.4.3 assignment with 'typeof' operator.
     */
    @Override
    public void visit(TypeofNode n, State state) {
        Value v;
        if (n.isVariable()) {
            Value val = state.readVariable(n.getVariableName(), null); // TODO: should also count as a variable read in Monitoring?
            val = UnknownValueResolver.getRealValue(val, state);
            v = Operators.typeof(val, val.isMaybeAbsent());
            m.visitVariableOrProperty(n.getVariableName(), n.getOperandSourceLocation(), val, state.getContext(), state);
        } else {
            Value val = state.readRegister(n.getArgRegister());
            val = UnknownValueResolver.getRealValue(val, state);
            v = Operators.typeof(val, false);
        }
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v);
    }

    /**
     * 12.5 and 12.6 'if'/iteration statement.
     */
    @Override
    public void visit(IfNode n, State state) {
        // do nothing (but see EdgeTransfer)
        Value val = state.readRegister(n.getConditionRegister());
        val = UnknownValueResolver.getRealValue(val, state);
        m.visitIf(n, Conversion.toBoolean(val));
    }

    /**
     * 13 function definition.
     */
    @Override
    public void visit(DeclareFunctionNode n, State state) {
        UserFunctionCalls.declareFunction(n, state);
    }

    /**
     * 11.2.2, 11.2.3, 13.2.1, and 13.2.2 'new' / function call.
     */
    @Override
    public void visit(CallNode n, State state) {
        if (n.getFunctionRegister() != AbstractNode.NO_VALUE) // old style call (where the function is given as a variable read)
            FunctionCalls.callFunction(new OrdinaryCallInfo(n, state) {

                @Override
                public Value getFunctionValue() {
                    return state.readRegister(n.getFunctionRegister());
                }

                @Override
                public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
                    return UserFunctionCalls.determineThis(n, caller_state, callee_state, c, n.getBaseRegister());
                }
            }, state, c);
        else { // getPropertyString / getPropertyRegister - like ReadPropertyNode
            Value baseval = state.readRegister(n.getBaseRegister());
            baseval = UnknownValueResolver.getRealValue(baseval, state);
            Set<ObjectLabel> objlabels = baseval.getObjectLabels(); // the ReadPropertyNode has updated baseval to account for ToObject
            Value propertyval;
            int propertyreg;
            if (n.isPropertyFixed()) {
                propertyreg = AbstractNode.NO_VALUE;
                propertyval = Value.makeStr(n.getPropertyString());
            } else {
                propertyreg = n.getPropertyRegister();
                propertyval = state.readRegister(propertyreg);
                propertyval = UnknownValueResolver.getRealValue(propertyval, state);
            }
            boolean maybe_undef = propertyval.isMaybeUndef();
            boolean maybe_null = propertyval.isMaybeNull();
            boolean maybe_nan = propertyval.isMaybeNaN();
            propertyval = propertyval.restrictToNotNullNotUndef().restrictToNotNaN();
            Str propertystr = Conversion.toString(propertyval, propertyreg, c);
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
                    v = state.readPropertyValue(singleton, propertyname);
                } else if (!propertystr.isNotStr()) {
                    v = state.readPropertyValue(singleton, propertystr);
                    read_undefined = propertystr.isMaybeStr("undefined");
                    read_null = propertystr.isMaybeStr("null");
                    read_nan = propertystr.isMaybeStr("NaN");
                } else
                    v = Value.makeNone();
                if (maybe_undef && !read_undefined) {
                    v = UnknownValueResolver.join(v, state.readPropertyValue(singleton, "undefined"), state);
                }
                if (maybe_null && !read_null) {
                    v = UnknownValueResolver.join(v, state.readPropertyValue(singleton, "null"), state);
                }
                if (maybe_nan && !read_nan) {
                    v = UnknownValueResolver.join(v, state.readPropertyValue(singleton, "NaN"), state);
                }
                v = UnknownValueResolver.getRealValue(v, state);

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
                FunctionCalls.callFunction(new OrdinaryCallInfo(n, state) {

                    @Override
                    public Value getFunctionValue() {
                        return Value.makeObject(target);
                    }

                    @Override
                    public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
                        return this_objs;
                    }
                }, state, c);
            }
            // also model calls to non-function values
            FunctionCalls.callFunction(new OrdinaryCallInfo(n, state) {

                @Override
                public Value getFunctionValue() {
                    return Value.join(nonfunctions);
                }

                @Override
                public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
                    return Collections.emptySet();
                }
            }, state, c);
        }
    }

    /**
     * 12.9 and 13.2.1 'return' statement.
     */
    @Override
    public void visit(ReturnNode n, State state) {
        transferReturn(n.getReturnValueRegister(), n.getBlock(), state, null, null);
    }

    /**
     * Transfer for a return statement.
     *
     * @param caller if non-null, only consider this caller
     */
    public void transferReturn(int valueReg, BasicBlock block, State state, NodeAndContext<Context> caller, Context edge_context) {
        Value v;
        if (valueReg != AbstractNode.NO_VALUE)
            v = state.readRegister(valueReg);
        else
            v = Value.makeUndef();
        UserFunctionCalls.leaveUserFunction(v, false, block.getFunction(), state, c, caller, edge_context);
    }

    /**
     * 13.2.1 exceptional return.
     */
    @Override
    public void visit(ExceptionalReturnNode n, State state) {
        transferExceptionalReturn(n.getBlock(), state, null, null);
    }

    /**
     * Transfer for an exceptional-return statement.
     *
     * @param caller if non-null, only consider this caller
     */
    public void transferExceptionalReturn(BasicBlock block, State state, NodeAndContext<Context> caller, Context edge_context) {
        Value v = state.readRegister(AbstractNode.EXCEPTION_REG);
        state.removeRegister(AbstractNode.EXCEPTION_REG);
        UserFunctionCalls.leaveUserFunction(v, true, block.getFunction(), state, c, caller, edge_context);
    }

    /**
     * 12.13 'throw' statement.
     */
    @Override
    public void visit(ThrowNode n, State state) {
        Value v = state.readRegister(n.getValueRegister());
        Exceptions.throwException(state.clone(), v, c, n);
        state.setToNone();
    }

    /**
     * 12.14 'catch' block.
     */
    @Override
    public void visit(CatchNode n, State state) {
        Value v = state.readRegister(AbstractNode.EXCEPTION_REG);
        state.removeRegister(AbstractNode.EXCEPTION_REG);
        if (n.getValueRegister() != AbstractNode.NO_VALUE) {
            state.writeRegister(n.getValueRegister(), v.makeExtendedScope());
        } else {
            ObjectLabel objlabel = new ObjectLabel(n, Kind.OBJECT);
            state.newObject(objlabel);
            state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
            state.writePropertyWithAttributes(objlabel, n.getVariableName(), v.setAttributes(false, true, false));
            state.writeRegister(n.getScopeObjRegister(), Value.makeObject(objlabel));
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
    public void visit(BeginWithNode n, State state) {
        Value v = state.readRegister(n.getObjectRegister());
        v = UnknownValueResolver.getRealValue(v, state);
        Set<ObjectLabel> objs = Conversion.toObjectLabels(state, n, v, c);
        if (objs.isEmpty() && !Options.get().isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        state.pushScopeChain(objs);
    }

    /**
     * 12.10 leave 'with' statement.
     */
    @Override
    public void visit(EndWithNode n, State state) {
        state.popScopeChain();
    }

    /**
     * 12.6.4 begin 'for-in' statement.
     */
    @Override
    public void visit(BeginForInNode n, State state) {
        if (!Options.get().isForInSpecializationDisabled()) {
            // 1. Find properties to iterate through
            Value v1 = state.readRegister(n.getObjectRegister());
            state.writeRegister(n.getObjectRegister(), v1.makeExtendedScope()); // preserve the register value
            v1 = UnknownValueResolver.getRealValue(v1, state);
            Set<ObjectLabel> objs = Conversion.toObjectLabels(state, n, v1, c);
            BlockState.Properties p = state.getEnumProperties(objs);
            Collection<Value> propertyNameValues = p.toValues();

            // 2. Make specialized context for each iteration
            int it = n.getPropertyListRegister();
            List<Context> specialized_contexts = newList();
            BasicBlock successor = n.getBlock().getSingleSuccessor();
            for (Value k : propertyNameValues) {
                m.visitPropertyRead(n, objs, k, state, true);
                if (!c.isScanning()) {
                    // 2.1 Make specialized context
                    State specialized_state = state.clone();
                    specialized_state.writeRegister(it, k);
                    Context specialized_context = Context.makeForInEntryContext(state.getContext(), successor, it, k);
                    specialized_contexts.add(specialized_context);
                    specialized_state.setContext(specialized_context);

                    // 2.2  Propagate specialized context
                    c.propagateToFunctionEntry(n, state.getContext(), specialized_state, specialized_context, successor);
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

                state.writeRegister(it, Value.makeNull().makeExtendedScope());
            }
        } else { // fall back to simple mode without context specialization
            Value v1 = state.readRegister(n.getObjectRegister());
            state.writeRegister(n.getObjectRegister(), v1.makeExtendedScope()); // preserve the register value
            v1 = UnknownValueResolver.getRealValue(v1, state);
            Set<ObjectLabel> objs = Conversion.toObjectLabels(state, n, v1, c);
            Properties p = state.getEnumProperties(objs);
            Value proplist = p.toValue().joinNull();
            m.visitPropertyRead(n, objs, proplist, state, true);
            state.writeRegister(n.getPropertyListRegister(), proplist);
        }
    }

    /**
     * 12.6.4 get next property of 'for-in' statement.
     */
    @Override
    public void visit(NextPropertyNode n, State state) {
        Value property_name = state.readRegister(n.getPropertyListRegister()).restrictToStr(); // restrictToStr to remove Null (the end-of-list marker)
        if (property_name.isNone()) { // possible if branch pruning in EdgeTransfer is disabled
            state.setToNone();
            return;
        }
        state.writeRegister(n.getPropertyRegister(), property_name);
    }

    /**
     * 12.6.4 check for more properties of 'for-in' statement.
     */
    @Override
    public void visit(HasNextPropertyNode n, State state) {
        Value v = UnknownValueResolver.getRealValue(state.readRegister(n.getPropertyListRegister()), state);
        Value res = !v.isNotStr() ? Value.makeBool(true) : Value.makeNone(); // string values represent property names
        if (v.isMaybeNull()) // null marks end-of-list
            res = res.joinBool(false);
        state.writeRegister(n.getResultRegister(), res);
    }

    /**
     * 12.6.4 end of loop of 'for-in' statement.
     */
    @Override
    public void visit(EndForInNode n, State state) {
        if (!Options.get().isForInSpecializationDisabled()) {
            if (!c.isScanning()) {
                // 1. Find successor block, context and base-state
                BasicBlock successor = state.getBasicBlock().getSingleSuccessor();
                State nonSpecializedMergeState = state.clone();
                Context nonSpecializedContext = state.getContext().getEnclosingContext();

                // 2. Use BlockState.mergeFunctionReturn to do the merge
                State forInBeginState = c.getAnalysisLatticeElement().getState(n.getBeginNode().getBlock(), nonSpecializedContext);
                State forInEntryState = c.getAnalysisLatticeElement().getState(nonSpecializedContext.getEntryBlockAndContext());
                Value returnValue = nonSpecializedMergeState.mergeFunctionReturn(forInBeginState, forInBeginState, forInEntryState, nonSpecializedMergeState.getSummarized(), nonSpecializedMergeState.readRegister(AbstractNode.RETURN_REG));
                nonSpecializedMergeState.setContext(nonSpecializedContext);
                nonSpecializedMergeState.writeRegister(AbstractNode.RETURN_REG, returnValue);
                if (state.hasExceptionRegisterValue()) {
                    nonSpecializedMergeState.writeRegister(AbstractNode.EXCEPTION_REG, state.readRegister(AbstractNode.EXCEPTION_REG));
                }

                // 3. Propagate only to the non-specialized successor
                c.propagateToBasicBlock(nonSpecializedMergeState, successor, Context.makeSuccessorContext(nonSpecializedMergeState, successor));
                state.setToNone();
            }
        }
    }

    /**
     * Assumption.
     */
    @Override
    public void visit(AssumeNode n, State state) {
        if (Options.get().isControlSensitivityDisabled())
            return;
        switch (n.getKind()) {

            case VARIABLE_NON_NULL_UNDEF: {
                Value v = state.readVariable(n.getVariableName(), null);
                v = UnknownValueResolver.getRealValue(v, state); // TODO: limits use of polymorphic values?
                v = v.restrictToNotNullNotUndef().restrictToNotAbsent();
                if (v.isNotPresent() && !Options.get().isPropagateDeadFlow())
                    state.setToNone();
                else
                    state.writeVariable(n.getVariableName(), v, false);
                break;
            }

            case PROPERTY_NON_NULL_UNDEF: {
                String propname;
                if (n.isPropertyFixed())
                    propname = n.getPropertyString();
                else {
                    Value propval = state.readRegister(n.getPropertyRegister());
                    propval = UnknownValueResolver.getRealValue(propval, state); // TODO: limits use of polymorphic values?
                    if (!propval.isMaybeSingleStr() || propval.isMaybeOtherThanStr())
                        break; // safe to do nothing here if it gets complicated
                    propname = propval.getStr();
                }
                Set<ObjectLabel> baseobjs = Conversion.toObjectLabels(state, n, state.readRegister(n.getBaseRegister()), null); // TODO: omitting side-effects here
                Value v = state.readPropertyWithAttributes(baseobjs, propname);
                if (v.isNotPresent() && !Options.get().isPropagateDeadFlow())
                    state.setToNone();
                else if (baseobjs.size() == 1 && baseobjs.iterator().next().isSingleton()) {
                    v = UnknownValueResolver.getRealValue(v, state); // TODO: limits use of polymorphic values?
                    v = v.restrictToNotNullNotUndef().restrictToNotAbsent();
                    state.writePropertyWithAttributes(baseobjs, propname, v, false);
                }
                break;
            }

            case UNREACHABLE: {
                if (Options.get().isIgnoreUnreachableEnabled()) {
                    state.setToNone();
                }
                break;
            }

            default:
                throw new AnalysisException("Unhandled AssumeNode kind: " + n.getKind());
        }
    }

    @Override
    public void visit(EventDispatcherNode n, State state) {
        DOMEventLoop.multipleNondeterministicEventLoops(n, state, c);
    }
}
