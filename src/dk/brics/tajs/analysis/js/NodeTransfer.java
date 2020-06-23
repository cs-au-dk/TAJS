/*
 * Copyright 2009-2020 Aarhus University
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
import dk.brics.tajs.analysis.nativeobjects.TAJSFunction;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
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
import dk.brics.tajs.flowgraph.jsnodes.UnaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.FunctionPartitions;
import dk.brics.tajs.lattice.FunctionTypeSignatures;
import dk.brics.tajs.lattice.MustEquals;
import dk.brics.tajs.lattice.ObjProperties;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.PKey.StringPKey;
import dk.brics.tajs.lattice.PKeys;
import dk.brics.tajs.lattice.PartitionToken;
import dk.brics.tajs.lattice.PartitionedValue;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.CallDependencies;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.CallKind;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;

import java.util.Collection;
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

    private Filtering filtering;

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
        filtering = c.getAnalysis().getFiltering();
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
                               Context edge_context, CallKind callKind) {
        if (call_node instanceof BeginForInNode) {
            for (EndForInNode endNode : ((BeginForInNode) call_node).getEndNodes()) {
                for (State end_state : c.getAnalysisLatticeElement().getStatesWithEntryContext(endNode.getBlock(), callee_context))
                    c.withStateAndNode(end_state, endNode, () -> {
                        transferEndForIn(endNode);
                        return null;
                    });
            }
        } else { // call_node is an ordinary call node or an implicit call
            Function callee = callee_entry.getFunction();
            NodeAndContext<Context> caller = new NodeAndContext<>(call_node, caller_context);
            BasicBlock ordinary_exit = callee.getOrdinaryExit();
            for (State ordinary_exit_state : c.getAnalysisLatticeElement().getStatesWithEntryContext(ordinary_exit, callee_context))
                if (ordinary_exit.getFirstNode() instanceof ReturnNode) {
                    ReturnNode returnNode = (ReturnNode) ordinary_exit.getFirstNode();
                    transferReturn(returnNode.getReturnValueRegister(), ordinary_exit, ordinary_exit_state.clone(), caller, edge_context, callKind);
                } else
                    throw new AnalysisException("ReturnNode expected");
            BasicBlock exceptional_exit = callee.getExceptionalExit();
            for (State exceptional_exit_state : c.getAnalysisLatticeElement().getStatesWithEntryContext(exceptional_exit, callee_context))
                transferExceptionalReturn(exceptional_exit, exceptional_exit_state.clone(), caller, edge_context, callKind);
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
        v = Partitioning.useOnlyValueForAlivePartitions(v, c.getState().getPartitioning());
        if (n.getResultRegister() != AbstractNode.NO_VALUE) {
            c.getState().writeRegister(n.getResultRegister(), v);
            c.getState().getMustReachingDefs().addReachingDef(n.getResultRegister(), n);
        }
    }

    /**
     * 11.1.5 object initializer.
     */
    @Override
    public void visit(NewObjectNode n) {
        Context heapContext = c.getAnalysis().getContextSensitivityStrategy().makeObjectLiteralHeapContext(n, c.getState(), c);
        ObjectLabel objlabel = ObjectLabel.make(n, Kind.OBJECT, heapContext);
        c.getState().newObject(objlabel);
        c.getState().writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        if (n.getResultRegister() != AbstractNode.NO_VALUE) {
            c.getState().writeRegister(n.getResultRegister(), Value.makeObject(objlabel));
            c.getState().getMustReachingDefs().addReachingDef(n.getResultRegister(), n);
        }
    }

    /**
     * 11.13 and 11.4 assignment with unary operator.
     */
    @Override
    public void visit(UnaryOperatorNode n) {
        Value arg = UnknownValueResolver.getRealValue(c.getState().readRegister(n.getArgRegister()), c.getState());
        Map<AbstractNode, Set<PartitionToken>> partitions = Partitioning.getPartitionsForUnop(n, arg);
        Value v = Partitioning.applyPartitions(partitions, arg, v1 -> unop(n, v1));
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToBottom();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE) {
            c.getState().writeRegister(n.getResultRegister(), v);
            c.getState().getMustReachingDefs().addReachingDef(n.getResultRegister(), n);
        }
    }

    private Value unop(UnaryOperatorNode n, Value arg) {
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
            case TYPEOF:
                v = Operators.typeof(arg, arg.isMaybeAbsent());
                break;
            default:
                throw new AnalysisException();
        }
        return v;
    }

    /**
     * 11.13 and 11.5/6/7/8 assignment with binary operator.
     */
    @Override
    public void visit(BinaryOperatorNode n) {
        Value arg1 = UnknownValueResolver.getRealValue(c.getState().readRegister(n.getArg1Register()), c.getState());
        Value arg2 = UnknownValueResolver.getRealValue(c.getState().readRegister(n.getArg2Register()), c.getState());
        Map<AbstractNode, Set<PartitionToken>> partitions = Partitioning.getPartitionsForBinop(n, arg1, arg2);
        Value v = Partitioning.applyPartitions(partitions, arg1, arg2, (v1, v2) -> binop(n, v1, v2));
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToBottom();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE) {
            c.getState().writeRegister(n.getResultRegister(), v);
            c.getState().getMustReachingDefs().addReachingDef(n.getResultRegister(), n);
        }
    }

    private Value binop(BinaryOperatorNode n, Value arg1, Value arg2) {
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
        return v;
    }

    /**
     * 11.1.2 assignment with right-hand-side identifier reference.
     */
    @Override
    public void visit(ReadVariableNode n) {
        String varname = n.getVariableName();
        Value v;
        Set<ObjectLabel> base_objs = null;
        if (varname.equals("this")) {
            // 11.1.1 read 'this' from the execution context
            v = c.getState().readThis();
            m.visitReadThis(n, v, c.getState(), InitialStateBuilder.GLOBAL);
        } else { // ordinary variable
            int result_base_reg = n.getResultBaseRegister();
            base_objs = newSet();
            v = pv.readVariable(varname, base_objs);
            if (Options.get().isBlendedAnalysisEnabled()) {
                v = c.getAnalysis().getBlendedAnalysis().getVariableValue(v, n, c.getState());
            }
            if (!Options.get().isNoPropNamePartitioning()) {
                // if varname is a free variable with partitioned value, then refine using the context
                v = Partitioning.getVariableValueFromPartition(v, c);
            }
            m.visitPropertyRead(n, base_objs, Value.makeTemporaryStr(varname), c.getState(), true);
            m.visitVariableAsRead(n, varname, v, c.getState());
            m.visitVariableOrProperty(n, varname, n.getSourceLocation(), v, c.getState().getContext(), c.getState());
            if (!n.isKeepAbsent())
                m.visitReadNonThisVariable(n, v);
            if (!n.isKeepAbsent() && v.isMaybeAbsent())
                Exceptions.throwReferenceError(c, v.isMaybePresent());
            if (!n.isKeepAbsent() && v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
                c.getState().setToBottom();
                return;
            }
            if (result_base_reg != AbstractNode.NO_VALUE)
                c.getState().writeRegister(result_base_reg, Value.makeObject(base_objs)); // see 10.1.4
            m.visitRead(n, v, c.getState());
            m.visitReadVariable(n, v, c.getState()); // TODO: combine some of these m.visitXYZ methods?
        }
        if (!n.isKeepAbsent() && v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToBottom();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE) {
            c.getState().writeRegister(n.getResultRegister(), n.isKeepAbsent() ? v : v.restrictToNotAbsent());
            c.getState().getMustReachingDefs().addReachingDef(n.getResultRegister(), n);
            c.getState().getMustEquals().addMustEquals(n.getResultRegister(), MustEquals.getSingleton(base_objs), StringPKey.make(varname));
        }
    }

    /**
     * 11.13 and 11.1.2 assignment with left-hand-side identifier reference.
     */
    @Override
    public void visit(WriteVariableNode n) {
        Value v = c.getState().readRegister(n.getValueRegister());
        if (Options.get().isBlendedAnalysisEnabled()) {
            v = c.getAnalysis().getBlendedAnalysis().getVariableValue(v, n, c.getState());
            if (v.isNone()) {
                c.getState().setToBottom();
                return;
            }
        }
        Pair<Set<ObjectLabel>,Boolean> objsDef = pv.writeVariable(n.getVariableName(), v, true);
        Function f = n.getBlock().getFunction();
        if (f.getParameterNames().contains(n.getVariableName())) { // TODO: review
            ObjectLabel arguments_obj = ObjectLabel.make(f.getEntry().getFirstNode(), Kind.ARGUMENTS);
            pv.writeProperty(arguments_obj, StringPKey.make(Integer.toString(f.getParameterNames().indexOf(n.getVariableName()))), v);
        }
        m.visitPropertyWrite(n, objsDef.getFirst(), Value.makeTemporaryStr(n.getVariableName()));
        m.visitVariableOrProperty(n, n.getVariableName(), n.getSourceLocation(), v, c.getState().getContext(), c.getState());
        if (objsDef.getSecond())
            c.getState().getMustEquals().addMustEquals(n.getValueRegister(), MustEquals.getSingleton(objsDef.getFirst()), StringPKey.make(n.getVariableName()));
    }

    /**
     * 11.2.1 assignment with right-hand-side property accessor.
     */
    @Override
    public void visit(ReadPropertyNode n) {
        // get the base value, coerce with ToObject
        Value baseval = c.getState().readRegister(n.getBaseRegister());
        baseval = UnknownValueResolver.getRealValue(baseval, c.getState());
        if (Options.get().isBlendedAnalysisEnabled())
            baseval = Value.join(c.getAnalysis().getBlendedAnalysis().getBase(baseval, n, c.getState())); // join does not decrease precision due to base being object labels only
        m.visitPropertyAccess(n, baseval);
        Value newBaseVal = Conversion.toObject(n, baseval, c); // models exception if null/undefined
        Set<ObjectLabel> objlabels = newBaseVal.getObjectLabels();
        if (objlabels.isEmpty() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToBottom();
            return;
        }
        if (filtering.assumeNotNullUndef(n.getBaseRegister()))
            return;
        if (!baseval.restrictToNotNullNotUndef().equals(newBaseVal))
            c.getState().writeRegister(n.getBaseRegister(), newBaseVal); // write coerced value back to the base register (maybe used by CallNode later)
        // get the property name value, separate the undefined/null/NaN components, coerce with ToString
        Value propertyval;
        if (n.isPropertyFixed()) {
            propertyval = Value.makeStr(n.getPropertyString());
        } else {
            propertyval = c.getState().readRegister(n.getPropertyRegister());
            propertyval = UnknownValueResolver.getRealValue(propertyval, c.getState());
        }
        if (Options.get().isBlendedAnalysisEnabled())
            propertyval = Value.join(c.getAnalysis().getBlendedAnalysis().getPropertyName(propertyval, baseval, n, c.getState())); // join does not decrease precision, since the resulting value will be the merge of all property reads and also through blended analysis
        Value originalPropertyVal = propertyval;
        boolean maybe_undef = propertyval.isMaybeUndef();
        boolean maybe_null = propertyval.isMaybeNull();
        boolean maybe_nan = propertyval.isMaybeNaN();
        Set<ObjectLabel> symbols = propertyval.getSymbols();
        propertyval = propertyval.restrictToNotNullNotUndef().restrictToNotNaN().restrictToNotSymbol();
        Value propertystr = Conversion.toString(propertyval, c).join(Value.makeObject(symbols));
        if ((propertystr.isNone() && !maybe_undef && !maybe_null && !maybe_nan) && !Options.get().isPropagateDeadFlow()) { // TODO: maybe need more aborts like this one?
            c.getState().setToBottom();
            return;
        }
        // read the object property value, as fixed property name or unknown property name, and separately for "undefined"/"null"/"NaN"
        Value v;
        boolean undefinedCovered = false;
        boolean nullCovered = false;
        boolean nanCovered = false;
        Set<ObjectLabel> base_objs = newSet();
        if (propertystr.isMaybeSingleStr()) { // fast-track for single-string property name
            String propertyname = propertystr.getStr();
            if (c.isScanning())
                m.visitReadProperty(n, objlabels, propertystr, maybe_undef || maybe_null || maybe_nan, c.getState(), pv.readPropertyWithAttributes(objlabels, propertystr), InitialStateBuilder.GLOBAL);
            if (newBaseVal instanceof PartitionedValue) {
                v = newBaseVal.applyFunction(baseVal -> PartitionedValue.ignorePartitions(UnknownValueResolver.getRealValue(pv.readPropertyValue(baseVal.getObjectLabels(), propertyname, base_objs), c.getState())));
            } else {
                v = pv.readPropertyValue(objlabels, propertyname, base_objs);
            }
            m.visitPropertyRead(n, objlabels, propertystr, c.getState(), true);
        } else { // fuzzy property name
            if (!propertystr.isNotStr() || propertystr.isMaybeSymbol()) {
                if (c.isScanning())
                    m.visitReadProperty(n, objlabels, propertystr, true, c.getState(), pv.readPropertyWithAttributes(objlabels, propertystr), InitialStateBuilder.GLOBAL);
                m.visitPropertyRead(n, objlabels, propertystr, c.getState(), true);
            }
            if (Options.get().isNoPropNamePartitioning() || !propertystr.isMaybeFuzzyStr() || propertystr.restrictToNotStrOtherNum().restrictToNotStrUInt().isNone()) {
                // don't use value partitioning (if not enabled, if not fuzzy string, or if only numeric)
                v = pv.readPropertyValue(objlabels, propertystr, base_objs);
            } else { // potentially use value partitioning
                v = Partitioning.partitionPropValue(n, n.getPropertyRegister(), objlabels, originalPropertyVal, propertystr, base_objs, true, c);
                if (v == null) { // no partitioning performed, fall back to non-partitioned reading
                    v = pv.readPropertyValue(objlabels, propertystr, base_objs);
                } else {
                    undefinedCovered = nullCovered = nanCovered = true; //covered by partitionPropValue
                }
            }
        }
        if (maybe_undef && !undefinedCovered) {
            if (c.isScanning())
                m.visitReadProperty(n, objlabels, Value.makeTemporaryStr("undefined"), true, c.getState(), pv.readPropertyWithAttributes(objlabels, propertystr), InitialStateBuilder.GLOBAL);
            v = UnknownValueResolver.join(v, pv.readPropertyValue(objlabels, "undefined"), c.getState());
        }
        if (maybe_null && !nullCovered) {
            if (c.isScanning())
                m.visitReadProperty(n, objlabels, Value.makeTemporaryStr("null"), true, c.getState(), pv.readPropertyWithAttributes(objlabels, propertystr), InitialStateBuilder.GLOBAL);
            v = UnknownValueResolver.join(v, pv.readPropertyValue(objlabels, "null"), c.getState());
        }
        if (maybe_nan && !nanCovered) {
            if (c.isScanning())
                m.visitReadProperty(n, objlabels, Value.makeTemporaryStr("NaN"), true, c.getState(), pv.readPropertyWithAttributes(objlabels, propertystr), InitialStateBuilder.GLOBAL);
            v = UnknownValueResolver.join(v, pv.readPropertyValue(objlabels, "NaN"), c.getState());
        }
        if (Options.get().isBlendedAnalysisEnabled())
            v = Value.join(c.getAnalysis().getBlendedAnalysis().getValue(v, baseval, originalPropertyVal, n, c.getState()));
        m.visitVariableOrProperty(n, n.getPropertyString(), n.getSourceLocation(), v, c.getState().getContext(), c.getState());
        m.visitRead(n, v, c.getState());
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToBottom();
            return;
        }
        // store the resulting value
        if (n.getResultRegister() != AbstractNode.NO_VALUE) {
            c.getState().writeRegister(n.getResultRegister(), v);
            c.getState().getMustReachingDefs().addReachingDef(n.getResultRegister(), n);
            c.getState().getMustEquals().addMustEquals(n.getResultRegister(), MustEquals.getSingleton(base_objs), MustEquals.getSingleton(propertystr));
        }
    }

    /**
     * 11.2.1 assignment with left-hand-side property accessor.
     */
    @Override
    public void visit(WritePropertyNode n) {
        // get the base value, coerce with ToObject
        Value baseval = c.getState().readRegister(n.getBaseRegister());
        baseval = UnknownValueResolver.getRealValue(baseval, c.getState());
        if (Options.get().isBlendedAnalysisEnabled())
            baseval = UnknownValueResolver.join(c.getAnalysis().getBlendedAnalysis().getBase(baseval, n, c.getState()), c.getState()); // join does not decrease precision due to base being object labels only
        m.visitPropertyAccess(n, baseval);
        Value coercedBaseval = Conversion.toObject(n, baseval, c); // models exception if null/undefined
        Set<ObjectLabel> objlabels = coercedBaseval.getObjectLabels();
        if (filtering.assumeNotNullUndef(n.getBaseRegister()))
            return;
        // get the property name value, separate the undefined/null/NaN components, coerce with ToString
        Value propertyval;
        if (n.isPropertyFixed()) {
            propertyval = Value.makeStr(n.getPropertyString());
        } else {
            propertyval = c.getState().readRegister(n.getPropertyRegister());
            propertyval = UnknownValueResolver.getRealValue(propertyval, c.getState());
        }
        Set<Value> propertyvalues;
        if (Options.get().isBlendedAnalysisEnabled()) { // do not toString coerce before refinements are made
            propertyvalues = c.getAnalysis().getBlendedAnalysis().getPropertyName(propertyval, baseval, n, c.getState()).stream().collect(Collectors.toSet());
        } else {
            propertyvalues = singleton(propertyval);
        }
        ParallelTransfer pt = new ParallelTransfer(c);
        for (Value pval : propertyvalues) {
            boolean maybe_undef = pval.isMaybeUndef();
            boolean maybe_null = pval.isMaybeNull();
            boolean maybe_nan = pval.isMaybeNaN();
            Set<ObjectLabel> symbols = pval.getSymbols();
            Value v = c.getState().readRegister(n.getValueRegister());
            if (Options.get().isBlendedAnalysisEnabled()) {
                v = Value.join(c.getAnalysis().getBlendedAnalysis().getValue(v, baseval, pval, n, c.getState())); // join does not decrease precision, since the values are all written to this property anyway
            }
            if (v.isNone()) {
                continue;
            }
            Value originalPVal = pval;
            pval = pval.restrictToNotNullNotUndef().restrictToNotNaN().restrictToNotSymbol();
            Value propertystr = Conversion.toString(pval, c).join(Value.makeObject(symbols));
            if ((propertystr.isNone() && !maybe_undef && !maybe_null && !maybe_nan) && !Options.get().isPropagateDeadFlow()) { // TODO: maybe need more aborts like this one?
                continue;
            }
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
            if (!Partitioning.usePartitionedWriteProperty(coercedBaseval, propertystr, v)) { // not using value partitioning
                if (propertystr instanceof PartitionedValue && !propertystr.isMaybeAllKnownStr() &&
                        ((PartitionedValue) propertystr).getPartitionValues().stream().allMatch(Value::isMaybeAllKnownStr)) {
                    // propertystr is a partitioned value, each partition value is precise but the single value is imprecise (due to widening), so use a string set of the partition values instead
                    propertystr = Value.join(((PartitionedValue) propertystr).getPartitionValues());
                }
                Value finalV = PartitionedValue.ignorePartitions(v);
                // write the object property value, and separately for "undefined"/"null"/"NaN"
                if (!propertystr.isNone()) {
                    Value finalPropertystr = propertystr;
                    pt.add(() -> pv.writeProperty(objlabels, finalPropertystr, finalV, false, n.isDecl()));
                }
                if (maybe_undef && !propertystr.isMaybeStr("undefined"))
                    pt.add(() -> pv.writeProperty(objlabels, Value.makeTemporaryStr("undefined"), finalV, false, n.isDecl()));
                if (maybe_null && !propertystr.isMaybeStr("null"))
                    pt.add(() -> pv.writeProperty(objlabels, Value.makeTemporaryStr("null"), finalV, false, n.isDecl()));
                if (maybe_nan && !propertystr.isMaybeStr("NaN"))
                    pt.add(() -> pv.writeProperty(objlabels, Value.makeTemporaryStr("NaN"), finalV, false, n.isDecl()));
            } else { // using value partitioning
                Value propertystrall = propertystr;
                if (maybe_undef || maybe_null || maybe_nan)
                    propertystrall = Partitioning.joinUndefNullNaNStrings(originalPVal, propertystr);
                Partitioning.writePropertyWithPartitioning(pt, objlabels, coercedBaseval, propertystrall, v, n, c, pv);
            }
            m.visitPropertyWrite(n, objlabels, propertystr); // TODO: more monitoring around here?
            if (Options.get().isEvalStatistics()
                    && propertystr.isMaybeSingleStr()
                    && propertystr.getStr().equals("innerHTML")) {
                m.visitInnerHTMLWrite(n, v);
            }
            m.visitVariableOrProperty(n, n.getPropertyString(), n.getSourceLocation(), v, c.getState().getContext(), c.getState());
        }
        pt.complete();
    }

    /**
     * 11.13 and 11.4.1 assignment with 'delete' operator.
     */
    @Override
    public void visit(DeletePropertyNode n) {
        Value v;
        if (n.isVariable()) {
            v = pv.deleteVariable(n.getVariableName());
            m.visitVariableOrProperty(n, n.getVariableName(), n.getSourceLocation(), v, c.getState().getContext(), c.getState());
        } else {
            Value baseval = c.getState().readRegister(n.getBaseRegister());
            baseval = UnknownValueResolver.getRealValue(baseval, c.getState());
            m.visitPropertyAccess(n, baseval);
            if (baseval.isMaybeNull() || baseval.isMaybeUndef()) {
                Exceptions.throwTypeError(c);
                if (baseval.isNullOrUndef() && !Options.get().isPropagateDeadFlow()) {
                    c.getState().setToBottom();
                    return;
                }
            }
            baseval = baseval.restrictToNotNullNotUndef();
            c.getState().writeRegister(n.getBaseRegister(), baseval);
            Set<ObjectLabel> objlabels = Conversion.toObjectLabels(n, baseval, c);
            if (objlabels.isEmpty() && !Options.get().isPropagateDeadFlow()) {
                c.getState().setToBottom();
                return;
            }
            PKeys propertystr;
            if (n.isPropertyFixed()) {
                propertystr = Value.makeStr(n.getPropertyString());
            } else {
                Value propertyval = c.getState().readRegister(n.getPropertyRegister());
                propertyval = UnknownValueResolver.getRealValue(propertyval, c.getState());
                propertystr = Conversion.toString(propertyval.restrictToNotSymbol(), c).join(propertyval.restrictToSymbol());
            }
            v = pv.deleteProperty(objlabels, propertystr, false);
        }
        if (v.isNotPresent() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToBottom();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE) {
            c.getState().writeRegister(n.getResultRegister(), v);
            c.getState().getMustReachingDefs().addReachingDef(n.getResultRegister(), n);
        }
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
        if (n.getTajsFunctionName() != null) {
            FunctionCalls.callFunction(new OrdinaryCallInfo(n, c) {
                @Override
                public Value getFunctionValue() {
                    return Value.makeObject(ObjectLabel.make(new TAJSFunction(n.getTajsFunctionName()), Kind.FUNCTION));
                }

                @Override
                public Value getThis() {
                    return Value.makeNull();
                }
            }, c);
            return;
        }
        Partitioning.applyTypePartitioning(n, c);
        if (n.getFunctionRegister() != AbstractNode.NO_VALUE) { // old style call (where the function is given as a variable read)
            State state = c.getState();
            Value originalFunctionValue = state.readRegister(n.getFunctionRegister());
            Collection<Value> refinedFunctionValues = Options.get().isBlendedAnalysisEnabled() ?
                    c.getAnalysis().getBlendedAnalysis().getValue(originalFunctionValue, null, null, n, state) :
                    singleton(originalFunctionValue);
            if (refinedFunctionValues.isEmpty()) {
                state.setToBottom();
                return;
            }
            Value functionValue = UnknownValueResolver.getRealValue(Value.join(refinedFunctionValues), state);
            FunctionCalls.callFunction(new OrdinaryCallInfo(n, c, functionValue.getFunctionPartitions(), functionValue.getFunctionTypeSignatures()) {

                @Override
                public Value getFunctionValue() {
                    if (n.getLiteralConstructorKind() != null) {
                        // these literal invocations can not be spurious in ES5
                        switch (n.getLiteralConstructorKind()) {
                            case ARRAY:
                                return Value.makeObject(ObjectLabel.make(ECMAScriptObjects.ARRAY, Kind.FUNCTION));
                            case REGEXP:
                                return Value.makeObject(ObjectLabel.make(ECMAScriptObjects.REGEXP, Kind.FUNCTION));
                            default:
                                throw new AnalysisException("Unhandled literal constructor type: " + n.getLiteralConstructorKind());
                        }
                    }
                    return functionValue;
                }

                @Override
                public Value getThis() {
                    if (n.getBaseRegister() == AbstractNode.NO_VALUE) {
                        return Value.makeUndef(); // ES3 11.2.3 step 6, ES5: use undefined instead of null (11.2.3#7)
                    } else { // ES3 11.2.3 step 7 and 10.1.6: replace activation objects by null, ES511.2.3#7: use undefined instead of null
                        Set<ObjectLabel> t = state.readRegister(n.getBaseRegister()).getObjectLabels();
                        Set<ObjectLabel> this_obj = newSet();
                        boolean is_maybe_undefined = false;
                        for (ObjectLabel objlabel : t) {
                            if (objlabel.getKind() == Kind.ACTIVATION) {
                                is_maybe_undefined = true;
                            } else {
                                this_obj.add(objlabel);
                            }
                        }
                        Value v = Value.makeObject(this_obj);
                        if (is_maybe_undefined) {
                            v = v.joinUndef();
                        }
                        return v;
                    }
                }

                @Override
                public boolean assumeFunction() {
                    return filtering.assumeFunction(n.getFunctionRegister());
                }
            }, c);
        } else { // getPropertyString / getPropertyRegister - like ReadPropertyNode
            Value baseval = c.getState().readRegister(n.getBaseRegister());
            baseval = UnknownValueResolver.getRealValue(baseval, c.getState());
            if (Options.get().isBlendedAnalysisEnabled())
                baseval = UnknownValueResolver.join(c.getAnalysis().getBlendedAnalysis().getBase(baseval, n, c.getState()), c.getState()); // we do not lose precision by joining here, only using objectlabels later
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
            Value propertystr = Conversion.toString(propertyval.restrictToNotSymbol(), c).join(Value.makeObject(propertyval.getSymbols()));
            // read the object property value, as fixed property name or unknown property name, and separately for "undefined"/"null"/"NaN"
            Map<Pair<Pair<FunctionPartitions, FunctionTypeSignatures>, ObjectLabel>, Set<ObjectLabel>> target2this = newMap();
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
                } else if (!propertystr.isNotStr() || propertystr.isMaybeSymbol()) {
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
                if (Options.get().isBlendedAnalysisEnabled())
                    v = Value.join(c.getAnalysis().getBlendedAnalysis().getValue(v, Value.makeObject(objlabel), null, n, c.getState())); // we do not lose precision by joining here, only using objectlabels later
                for (ObjectLabel target : v.getObjectLabels()) {
                    if (target.getKind() == Kind.FUNCTION) {
                        addToMapSet(target2this, Pair.make(Pair.make(v.getFunctionPartitions(), v.getFunctionTypeSignatures()), target), objlabel); // the single target function paired with the freeVariablePartitioning is associated with objlabel as 'this' value
                    } else {
                        nonfunctions.add(Value.makeObject(target));
                    }
                }
                if (v.isMaybePrimitiveOrSymbol()) {
                    nonfunctions.add(v.restrictToNotObject());
                }
            }
            // do calls to each target with the corresponding values of this
            for (Entry<Pair<Pair<FunctionPartitions, FunctionTypeSignatures>, ObjectLabel>, Set<ObjectLabel>> me : target2this.entrySet()) {
                FunctionPartitions partitions = me.getKey().getFirst().getFirst();
                FunctionTypeSignatures signatures = me.getKey().getFirst().getSecond();
                ObjectLabel target = me.getKey().getSecond();
                Set<ObjectLabel> this_objs = me.getValue();
                Value thisValue = baseval.restrictToObject(this_objs);
                Value funval = Value.makeObject(target).addFunctionTypeSignatures(signatures);
                FunctionCalls.callFunction(new OrdinaryCallInfo(n, c, partitions, signatures) {

                    @Override
                    public Value getFunctionValue() {
                        return funval;
                    }

                    @Override
                    public Value getThis() {
                        return thisValue;
                    }

                    @Override
                    public boolean assumeFunction() {
                        return filtering.assumeFunction(this_objs, n.getPropertyString());
                    }
                }, c);
            }
            // also model calls to non-function values
            FunctionCalls.callFunction(new OrdinaryCallInfo(n, c) {

                @Override
                public Value getFunctionValue() {
                    return Value.join(nonfunctions);
                }

                @Override
                public Value getThis() {
                    throw new AnalysisException("Unexpected call to getThis for non-function value");
                }
            }, c);
        }
    }

    /**
     * 12.9 and 13.2.1 'return' statement.
     */
    @Override
    public void visit(ReturnNode n) {
        if (Options.get().isChargedCallsDisabled() || !CallDependencies.DELAY_RETURN_FLOW_UNTIL_DISCHARGED) {
            transferReturn(n.getReturnValueRegister(), n.getBlock(), c.getState(), null, null, CallKind.ORDINARY);
        } else { // transferReturn is invoked when the call edges are discharged
            c.getCallDependencies().registerDelayedReturn(n.getBlock(), c.getState().getContext());
        }
    }

    /**
     * Transfer for a return statement.
     *
     * @param caller if non-null, only consider this caller
     */
    private void transferReturn(int valueReg, BasicBlock block, State state, NodeAndContext<Context> caller, Context edge_context, CallKind callKind) {
        Value v;
        if (valueReg != AbstractNode.NO_VALUE)
            v = state.readRegister(valueReg);
        else
            v = Value.makeUndef();
        UserFunctionCalls.leaveUserFunction(v, false, block.getFunction(), state, c, caller, edge_context, callKind);
    }

    /**
     * 13.2.1 exceptional return.
     */
    @Override
    public void visit(ExceptionalReturnNode n) {
        if (Options.get().isChargedCallsDisabled() || !CallDependencies.DELAY_RETURN_FLOW_UNTIL_DISCHARGED || n.getBlock().getFunction().isMain()) {
            transferExceptionalReturn(n.getBlock(), c.getState(), null, null, CallKind.ORDINARY);
        } else { // transferExceptionalReturn is invoked when the call edges are discharged (unless exiting from main)
            c.getCallDependencies().registerDelayedReturn(n.getBlock(), c.getState().getContext());
        }
    }

    /**
     * Transfer for an exceptional-return statement.
     *
     * @param caller if non-null, only consider this caller
     */
    private void transferExceptionalReturn(BasicBlock block, State state, NodeAndContext<Context> caller, Context edge_context, CallKind callKind) {
        Value v = state.readRegister(AbstractNode.EXCEPTION_REG);
        state.removeRegister(AbstractNode.EXCEPTION_REG);
        UserFunctionCalls.leaveUserFunction(v, true, block.getFunction(), state, c, caller, edge_context, callKind);
    }

    /**
     * 12.13 'throw' statement.
     */
    @Override
    public void visit(ThrowNode n) {
        Value v = c.getState().readRegister(n.getValueRegister());
        Exceptions.throwException(c.getState().clone(), v, c, n);
        c.getState().setToBottom();
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
            ObjectLabel objlabel = ObjectLabel.make(n, Kind.OBJECT);
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
            c.getState().setToBottom();
            return;
        }
        if (filtering.assumeNotNullUndef(n.getObjectRegister()))
            return;
        c.getState().pushScopeChain(objs);
    }

    /**
     * 12.10 leave 'with' statement.
     */
    @Override
    public void visit(EndWithNode n) {
        c.getState().popScopeChain();
        c.getState().getMustReachingDefs().setToBottom();
    }

    /**
     * 12.6.4 begin 'for-in' statement.
     */
    @Override
    public void visit(BeginForInNode n) {
        Value v1 = c.getState().readRegister(n.getObjectRegister());
        c.getState().writeRegister(n.getObjectRegister(), v1.makeExtendedScope()); // preserve the register value
        v1 = UnknownValueResolver.getRealValue(v1, c.getState());
        v1 = v1.restrictToNotNullNotUndef(); // ES5: "If experValue is null or undefined, return (normal, empty, empty)."
        Set<ObjectLabel> objs = Conversion.toObjectLabels(n, v1, c);
        ObjProperties p = c.getState().getProperties(objs, ObjProperties.PropertyQuery.makeQuery().onlyEnumerable().usePrototypes());

        if (Options.get().isForInSpecializationEnabled()) {
            // 1. Find properties to iterate through
            Collection<Value> propertyNameValues = newList(p.getGroupedPropertyNames());

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
                    c.propagateToFunctionEntry(n, c.getState().getContext(), new CallEdge(specialized_state, null), specialized_context, successor, CallKind.ORDINARY);
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
            c.getState().setToBottom();
        } else { // fall back to simple mode without context specialization
            Value proplist = Value.join(p.getGroupedPropertyNames());
            m.visitPropertyRead(n, objs, proplist, c.getState(), true);
            c.getState().writeRegister(n.getPropertyListRegister(), proplist.joinNull());
        }
    }

    /**
     * 12.6.4 get next property of 'for-in' statement.
     */
    @Override
    public void visit(NextPropertyNode n) {
        Value property_name = c.getState().readRegister(n.getPropertyListRegister()).restrictToNotNull(); // remove Null (the end-of-list marker)
        if (property_name.isNone()) { // possible if branch pruning in EdgeTransfer is disabled
            c.getState().setToBottom();
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
        Value res = (!v.isNotStr() || v.isMaybeSymbol()) ? Value.makeBool(true) : Value.makeNone(); // string/symbol values represent property names
        if (v.isMaybeNull()) // null marks end-of-list
            res = res.joinBool(false);
        c.getState().writeRegister(n.getResultRegister(), res);
    }

    /**
     * 12.6.4 end of loop of 'for-in' statement.
     */
    @Override
    public void visit(EndForInNode n) {
        if (Options.get().isForInSpecializationEnabled()) {
            if (Options.get().isChargedCallsDisabled() || !CallDependencies.DELAY_RETURN_FLOW_UNTIL_DISCHARGED) {
                transferEndForIn(n);
            } else { // transferEndForIn is invoked when the call edges are discharged
                c.getCallDependencies().registerDelayedReturn(n.getBlock(), c.getState().getContext());
            }
            c.getState().setToBottom();
        }
    }

    private void transferEndForIn(EndForInNode n) {
        if (Options.get().isForInSpecializationEnabled()) {
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
                    returnValue = UserFunctionCalls.mergeFunctionReturn(nonSpecializedMergeState, beginState, edgeState, beginEntryState, nonSpecializedMergeState.getRenamings(), returnValue, exValue);
                    if (returnValue != null) {
                        nonSpecializedMergeState.writeRegister(AbstractNode.RETURN_REG, returnValue);
                    }

                    // 3. Propagate only to the non-specialized successor
                    nonSpecializedMergeState.setBasicBlock(c.getState().getBasicBlock().getSingleSuccessor()); // change location so that values will get recovered correctly
                    nonSpecializedMergeState.setContext(beginContext);
                    UserFunctionCalls.attemptMaterializeVariableObj(nonSpecializedMergeState);
                    c.propagateToBasicBlock(nonSpecializedMergeState, c.getState().getBasicBlock().getSingleSuccessor(), beginContext);
                }
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
            c.getState().setToBottom();
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
        c.getState().setToBottom();
    }

    /**
     * Event dispatch.
     */
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
