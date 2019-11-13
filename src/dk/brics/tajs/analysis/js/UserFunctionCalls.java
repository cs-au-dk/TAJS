/*
 * Copyright 2009-2019 Aarhus University
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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMEvents;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.NopNode;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.MustReachingDefs;
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.ObjectProperty;
import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.PKey.StringPKey;
import dk.brics.tajs.lattice.PartitionedValue;
import dk.brics.tajs.lattice.Renamings;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.CallKind;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.typescript.TypeFiltering;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.Collectors;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Models calls to user-defined (non-host) functions.
 */
public class UserFunctionCalls {

    private static Logger log = Logger.getLogger(UserFunctionCalls.class);

    private UserFunctionCalls() {
    }

    /**
     * Declares a function in the current state.
     *
     * @see #instantiateFunction(Function, ScopeChain, AbstractNode, State, GenericSolver.SolverInterface)
     */
    public static void declareFunction(DeclareFunctionNode n, Solver.SolverInterface c) {
        State state = c.getState();
        Function fun = n.getFunction();
        ObjectLabel fn = instantiateFunction(fun, state.getScopeChain(), n, state, c);
        Value fnval = Partitioning.getInstantiatedFunctions(fn, fun, n, c);

        if (!n.isExpression() && fun.getName() != null) {
            // p.79 (function declaration)
            c.getAnalysis().getPropVarOperations().declareAndWriteVariable(fun.getName(), fnval, true);
        }

        if (Options.get().isDOMEnabled() && n.getDomEventType() != null) {
            DOMEvents.addEventHandler(fnval, n.getDomEventType(), c);
        }

        int result_reg = n.getResultRegister();
        if (result_reg != AbstractNode.NO_VALUE) {
            c.getState().writeRegister(result_reg, fnval);
            c.getState().getMustReachingDefs().addReachingDef(n.getResultRegister(), n);
        }
    }

    /**
     * Instantiates a function at the given node and state.
     *
     * @param fun   the function to instantiate
     * @param scope the scope of the function
     * @param node  the node the function is instantiated at
     * @param state the state the function is instantiated in
     * @param c     SolverInterface
     * @return a label for the instantiated function
     */
    public static ObjectLabel instantiateFunction(Function fun, ScopeChain scope, AbstractNode node, State state, Solver.SolverInterface c) {
        // TODO: join function objects (p.72)? (if same n and same scope)
        Context functionHeapContext = c.getAnalysis().getContextSensitivityStrategy().makeFunctionHeapContext(fun, c);
        ObjectLabel fn = ObjectLabel.make(fun, functionHeapContext);

        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        // 13.2 step 2 and 3
        state.newObject(fn);
        // 13.2 step 4
        state.writeInternalPrototype(fn, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        // 13.2 step 7
        /// old code, for ES3 semantics
//        if (is_expression && fun.getName() != null) {
//            // p.79 (function expression with identifier)
//            ObjectLabel front = ObjectLabel.make(fun.getEntry().getFirstNode(), Kind.OBJECT);
//            state.newObject(front);
//            scope = ScopeChain.make(Collections.singleton(front), scope);
//            pv.writePropertyWithAttributes(front, fun.getName(), f.setAttributes(false, true, true));
//            /* From ES5, Annex D:
//             In Edition 3, the algorithm for the production FunctionExpression with an Identifier adds an object
//             created as if by new Object() to the scope chain to serve as a scope for looking up the name of the
//             function. The identifier resolution rules (10.1.4 in Edition 3) when applied to such an object will,
//             if necessary, follow the object's prototype chain when attempting to resolve an identifier. This
//             means all the properties of Object.prototype are visible as identifiers within that scope. In
//             practice most implementations of Edition 3 have not implemented this semantics. Edition 5 changes
//             the specified semantics by using a Declarative Environment Record to bind the name of the function.
//             */
//        }

        state.writeObjectScope(fn, scope);
        // 13.2 step 8
        pv.writePropertyWithAttributes(fn, "length", Value.makeNum(fun.getParameterNames().size()).setAttributes(true, false, true));
        // 13.2 step 9
        ObjectLabel prototype = ObjectLabel.make(node, Kind.OBJECT, functionHeapContext);
        state.newObject(prototype);
        state.writeInternalPrototype(prototype, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        // 13.2 step 10
        pv.writePropertyWithAttributes(prototype, "constructor", Value.makeObject(fn).setAttributes(true, false, false));
        // 13.2 step 11
        pv.writePropertyWithAttributes(fn, "prototype", Value.makeObject(prototype).setAttributes(true, true, false));
        state.writeInternalValue(prototype, Value.makeNum(Double.NaN)); // TODO: as in Rhino (?)

        // FIXME: "" is sometimes incorrect (if the function is anonymous but used in a simple variable initializer) (GitHub #410)
        String name = fun.getName() == null ? "" : fun.getName();
        pv.writePropertyWithAttributes(fn, "name", Value.makeStr(name).setAttributes(true, false, true));

        return fn;
    }

    /**
     * Instantiates a function with the scope set to global.
     *
     * @see #instantiateFunction(Function, ScopeChain, AbstractNode, State, GenericSolver.SolverInterface)
     */
    public static ObjectLabel instantiateGlobalScopeFunction(Function function, AbstractNode node, State state, Solver.SolverInterface c) {
        return instantiateFunction(function, ScopeChain.make(InitialStateBuilder.GLOBAL), node, state, c);
    }

    /**
     * Enters a user-defined function.
     */
    public static void enterUserFunction(ObjectLabel obj_f, CallInfo call, boolean implicit, Solver.SolverInterface c) {
        State caller_state = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        ScopeChain obj_f_sc = caller_state.readObjectScope(obj_f);
        Value prototype = pv.readPropertyDirect(Collections.singleton(obj_f), StringPKey.PROTOTYPE);
        if (obj_f_sc == null || prototype.isNone())
            return; // must be spurious dataflow

        Function f = obj_f.getFunction();
        AbstractNode n = call.getSourceNode();

        if (log.isDebugEnabled())
            log.debug("enterUserFunction " + (implicit ? "(implicit)" : "") + "from node " + n.getIndex() + " at " + n.getSourceLocation()
                    + " to " + f + " at " + f.getSourceLocation());

        State edge_state = caller_state.clone();
        c.withState(edge_state, () -> {
            if (call.assumeFunction())
                return;
            final Value thisVal;
            Renamings extra_renamings = new Renamings();
            if (call.isConstructorCall()) {
                // 13.2.2.1-2 create new object
                Context thisHeapContext = c.getAnalysis().getContextSensitivityStrategy().makeConstructorHeapContext(edge_state, obj_f, call, c);
                ObjectLabel new_obj = ObjectLabel.make(n, Kind.OBJECT, thisHeapContext);
                edge_state.newObject(new_obj);
                extra_renamings.addDefinitelySummarized(new_obj);
                thisVal = Value.makeObject(new_obj);
                // 13.2.2.3-5 provide [[Prototype]]
                Value prototypeFinal = UnknownValueResolver.getRealValue(prototype, edge_state);
                if (prototypeFinal.isMaybePrimitiveOrSymbol())
                    prototypeFinal = prototypeFinal.restrictToNonSymbolObject().joinObject(InitialStateBuilder.OBJECT_PROTOTYPE);
                edge_state.writeInternalPrototype(new_obj, prototypeFinal);
            } else { // see ES5 10.4.3
                Value rawThisVal = call.getThis();
                if (f.isStrict()) {
                    thisVal = rawThisVal;
                } else {
                    Value coercedThisVal = Conversion.toObject(call.getSourceNode(), rawThisVal.restrictToNotNullNotUndef(), c);
                    if (rawThisVal.isMaybeNull() || rawThisVal.isMaybeUndef()) {
                        thisVal = coercedThisVal.joinObject(InitialStateBuilder.GLOBAL);
                    } else {
                        thisVal = coercedThisVal;
                    }
                }
            }
            edge_state.setExecutionContext(edge_state.getExecutionContext().copyWithThis(thisVal)); // must set 'this' before creating heapContext
            Context heapContext = c.getAnalysis().getContextSensitivityStrategy().makeActivationAndArgumentsHeapContext(edge_state, obj_f, call, c);

            // 10.2.3 enter new execution context, 13.2.1 transfer parameters, 10.1.6/8 provide 'arguments' object
            ObjectLabel varobj = ObjectLabel.make(f.getEntry().getFirstNode(), Kind.ACTIVATION, heapContext); // better to use entry than invoke here
            edge_state.newObject(varobj);
            extra_renamings.addDefinitelySummarized(varobj);
            ObjectLabel argobj = ObjectLabel.make(f.getEntry().getFirstNode(), Kind.ARGUMENTS, heapContext);
            edge_state.newObject(argobj);
            extra_renamings.addDefinitelySummarized(argobj);
            ScopeChain sc = ScopeChain.make(Collections.singleton(varobj), obj_f_sc);

            edge_state.setExecutionContext(new ExecutionContext(sc, singleton(varobj), thisVal));
            pv.declareAndWriteVariable("arguments", Value.makeObject(argobj), true);
            edge_state.writeInternalPrototype(argobj, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
            pv.writePropertyWithAttributes(argobj, "callee", Value.makeObject(obj_f).setAttributes(true, false, false));
            Value argumentCount = call.isUnknownNumberOfArgs() ? Value.makeAnyNumUInt() : Value.makeNum(call.getNumberOfArgs());
            pv.writePropertyWithAttributes(argobj, "length", argumentCount.setAttributes(true, false, false));

            DeclareFunctionNode node = f.getNode();
            if (node != null) {
                if (node.isExpression() && f.getName() != null) {
                    Value objVal = Value.makeObject(obj_f);
                    pv.declareAndWriteVariable(f.getName(), objVal, true); // 10.1.3
                }
            }

            // if unknown number of arguments, fuzzy write unknown arg to arguments object
            if (call.isUnknownNumberOfArgs()) {
                Value v = call.getUnknownArg();
                if (Options.get().isDebugOrTestEnabled() && !v.isMaybeUndef()) {
                    throw new AnalysisException("Unknown arg not possibly undefined?");
                }
                Value renamed = v.rename(extra_renamings);
                pv.writeProperty(singleton(argobj), Value.makeAnyStrUInt(), renamed); // the first arguments will be overwritten below with something more precise
            }
            // write argument values to the arguments object and the named parameters
            final int numberOfUnknownArgumentsToKeepDisjoint = Options.Constants.NUMBER_OF_UNKNOWN_ARGUMENTS_TO_KEEP_DISJOINT; // number of parameters to keep separate, if the actual number is unknown
            for (int i = 0; i < f.getParameterNames().size() || i < (call.isUnknownNumberOfArgs() ? numberOfUnknownArgumentsToKeepDisjoint : call.getNumberOfArgs()); i++) {
                Value v = call.getArg(i);
                if (v.isNone()) // can happen because of type filtering and blended analysis
                    return;
                Value renamed = v.rename(extra_renamings);
                pv.writeProperty(argobj, StringPKey.make(Integer.toString(i)), renamed); // from ES5 Annex E: "In Edition 5 the array indexed properties of argument objects that correspond to actual formal parameters are enumerable. In Edition 3, such properties were not enumerable."
                if (i < f.getParameterNames().size()) {
                    if (renamed.isMaybeAbsent())
                        renamed = renamed.restrictToNotAbsent().joinUndef(); // convert absent to undefined
                    pv.declareAndWriteVariable(f.getParameterNames().get(i), renamed, true); // 10.1.3
                }
            }
            // FIXME: properties of 'arguments' should be shared with the formal parameters (see 10.1.8 item 4) - easy solution that does not require Reference types? - github #21
            // (see comment at NodeTransfer/WriteVariable... - also needs the other way around...)

            if (c.isScanning())
                return;

            edge_state.stackObjectLabels();
            edge_state.clearRegisters();
            edge_state.getMustReachingDefs().setToBottom();
            edge_state.getPartitioning().clearMaybePartitioned();

            if (thisVal.getObjectLabels().size() > 1
                    && (Options.get().isContextSpecializationEnabled()
                    && thisVal.getObjectLabels().size() < Options.Constants.MAX_CONTEXT_SPECIALIZATION)) {
                // specialize edge_state such that 'this' becomes a singleton
                if (log.isDebugEnabled())
                    log.debug("specializing edge state, this = " + thisVal.getObjectLabels());
                for (Iterator<ObjectLabel> it = thisVal.getObjectLabels().iterator(); it.hasNext(); ) {
                    ObjectLabel this_obj = it.next();
                    State next_edge_state = it.hasNext() ? edge_state.clone() : edge_state;
                    next_edge_state.getExecutionContext().setThis(Value.makeObject(this_obj)); // (execution context should be writable here)
                    propagateToFunctionEntry(new CallEdge(next_edge_state, call.getFunctionTypeSignatures()), n, obj_f, call, implicit, c);
                }
            } else
                propagateToFunctionEntry(new CallEdge(edge_state, call.getFunctionTypeSignatures()), n, obj_f, call, implicit, c);
        });
    }

    private static void propagateToFunctionEntry(CallEdge edge, AbstractNode n, ObjectLabel obj_f, CallInfo call, boolean implicit, Solver.SolverInterface c) {
        Context edge_context = c.getAnalysis().getContextSensitivityStrategy().makeFunctionEntryContext(edge.getState(), obj_f, call, c);
        CallKind callKind = !implicit ? CallKind.ORDINARY : call.isConstructorCall() ? CallKind.IMPLICIT_CONSTRUCTOR : CallKind.IMPLICIT;
        c.propagateToFunctionEntry(n, edge.getState().getContext(), edge, edge_context, obj_f.getFunction().getEntry(), callKind);
    }

    /**
     * Leaves a user-defined function.
     */
    public static void leaveUserFunction(Value returnval, boolean exceptional, Function f, State state, Solver.SolverInterface c,
                                         NodeAndContext<Context> specific_caller, Context specific_edge_context, CallKind callKind) {

        if (f.isMain()) { // TODO: also report uncaught exceptions and return immediately for event handlers
            final String msgkey = "Uncaught exception";
            AbstractNode n = f.getOrdinaryExit().getLastNode();
            if (exceptional) {
                returnval = UnknownValueResolver.getRealValue(returnval, state);
                List<SourceLocation> objs = returnval.getObjectSourceLocations().stream().sorted(new SourceLocation.Comparator()).collect(Collectors.toList());
                if (!objs.isEmpty()) { // use object source locations
                    List<String> locationStrings = objs.stream().map(SourceLocation::toString).collect(Collectors.toList());
                    String msg = String.format("Uncaught exception, constructed at [%s]", String.join(", ", locationStrings));
                    c.getMonitoring().addMessage(n, Severity.LOW, msgkey, msg); // TODO: give user-defined exceptions higher severity level?
                } else { // alternatively, use the primitive values in the message
                    Value v = returnval.restrictToNotObject(); // TODO: may be <none> if returnval only contains exceptions from host env
                    c.getMonitoring().addMessage(n, Severity.LOW, msgkey, "Uncaught exception: " + v);
                }
            }
            return; // do nothing when leaving the main function
        }

        if (c.isScanning())
            return;

        if (log.isDebugEnabled())
            log.debug("leaveUserFunction from " + f + " at " + f.getSourceLocation());

        state.clearVariableObject();
        state.clearRegisters();
        state.getMustReachingDefs().setToBottom();
        state.getMustEquals().setToBottom();

        if (specific_caller != null)
            returnToCaller(specific_caller.getNode(), specific_caller.getContext(), specific_edge_context, callKind, returnval, exceptional, f, state, c);
        else {
            // try each call node that calls f with the current callee context
            CallGraph<State, Context, CallEdge> cg = c.getAnalysisLatticeElement().getCallGraph();
            for (Iterator<CallGraph.ReverseEdge<Context>> i = cg.getSources(BlockAndContext.makeEntry(state.getBasicBlock(), state.getContext())).iterator(); i.hasNext(); ) {
                CallGraph.ReverseEdge<Context> re = i.next();
                if (c.isCallEdgeCharged(re.getCallNode(), re.getCallerContext(), re.getEdgeContext(), BlockAndContext.makeEntry(state.getBasicBlock(), state.getContext())))
                    returnToCaller(re.getCallNode(), re.getCallerContext(), re.getEdgeContext(), callKind, returnval, exceptional, f, i.hasNext() ? state.clone() : state, c);
                else if (log.isDebugEnabled())
                    log.debug("skipping call edge from node " + re.getCallNode().getIndex() + ", call context " + re.getCallerContext() + ", edge context " + re.getEdgeContext());

            }
        }
    }

    private static void returnToCaller(AbstractNode node, Context caller_context, Context edge_context, CallKind callInfo, Value returnval, boolean exceptional, Function f, State state, Solver.SolverInterface c) {
        final boolean is_constructor;
        final int result_reg;
        if (callInfo.isImplicit()) { // implicit function call, e.g. valueOf/toString
            is_constructor = callInfo.isImplicitConstructorCall();
            result_reg = AbstractNode.RETURN_REG;
        } else if (node instanceof CallNode) {
            CallNode callnode = (CallNode) node;
            is_constructor = callnode.isConstructorCall();
            result_reg = callnode.getResultRegister();
        } else if (node instanceof EventDispatcherNode) {
            is_constructor = false;
            result_reg = AbstractNode.NO_VALUE;
        } else
            throw new AnalysisException();

        if (log.isDebugEnabled())
            log.debug("trying call node " + node.getIndex() + ": " + node + " at " + node.getSourceLocation() +
                    ", caller context: " + caller_context + ", callee context: " + state.getContext());

        // apply inverse transform
        state.writeRegister(0, returnval); // TODO: pass returnval explicitly through returnFromFunctionExit instead of using a register
        c.returnFromFunctionExit(state, node, caller_context, f.getEntry(), edge_context, callInfo);
        returnval = state.readRegister(0);
        state.clearRegisters();
        state.getMustReachingDefs().setToBottom();
        state.getMustEquals().setToBottom();
        if (state.isBottom())
            return; // flow was cancelled, probably something needs to be recomputed

        // merge newstate with caller state and call edge state
        Renamings callee_renamings = new Renamings(state.getRenamings());
        Context heapContext = state.getScopeChain().getObject().iterator().next().getHeapContext(); // this should give us the heapContext that was created at enterUserFunction
        if (is_constructor) {
            ObjectLabel this_obj = ObjectLabel.make(node, Kind.OBJECT, heapContext);
            callee_renamings.addDefinitelySummarized(this_obj);
        }
        ObjectLabel activation_obj = ObjectLabel.make(f.getEntry().getFirstNode(), Kind.ACTIVATION, heapContext);
        callee_renamings.addDefinitelySummarized(activation_obj);
        ObjectLabel arguments_obj = ObjectLabel.make(f.getEntry().getFirstNode(), Kind.ARGUMENTS, heapContext);
        callee_renamings.addDefinitelySummarized(arguments_obj);
        CallEdge calledge = c.getAnalysisLatticeElement().getCallGraph().getCallEdge(node, caller_context, f.getEntry(), edge_context);

        State caller_state = c.getAnalysisLatticeElement().getStates(node.getBlock()).get(caller_context);
        if (node instanceof CallNode) {
            // need to re-apply type-partitioning here, since the caller-state is the one at the
            // entry of the call-node and not the one that was updated during the transfer of the call node, which originally did the type-partitioning
            c.withState(caller_state, () -> Partitioning.applyTypePartitioning((CallNode) node, c));
        }
        returnval = mergeFunctionReturn(state, caller_state,
                calledge.getState(),
                c.getAnalysisLatticeElement().getState(BlockAndContext.makeEntry(node.getBlock(), caller_context)),
                callee_renamings,
                returnval, null); // TODO: not obvious why this part is in dk.brics.tajs.analysis and the renaming and localization is done via dk.brics.tajs.solver...
        if (callInfo.isImplicit()) {
            state.setBasicBlock(node.getImplicitAfterCall());
            state.setContext(caller_context);
        } else {
            state.setBasicBlock(node.getBlock().getSingleSuccessor());
            state.setContext(caller_context);
        }

        if (exceptional) {
            if (!callInfo.isImplicit()) {
                // collect garbage (but not if implicit, because some objects may only be reachable via registers which we don't have here)
                state.reduce(returnval);
            }

            // transfer exception value to caller
            Exceptions.throwException(state, returnval, c, node);
        } else {
            returnval = UnknownValueResolver.getRealValue(returnval, state);
            if (!is_constructor && calledge.getFunctionTypeSignatures() != null)
                returnval = new TypeFiltering(c).assumeReturnValueType(returnval, calledge.getFunctionTypeSignatures()); // FIXME: is current state in c the right one here?
            if (!returnval.isNone()) { // skip if no value (can happen when propagateToFunctionEntry calls transferReturn)

                if (is_constructor && returnval.isMaybePrimitiveOrSymbol()) {
                    // 13.2.2.7-8 replace non-object by the new object (which is kept in 'this' at the call edge)
                    returnval = returnval.restrictToNonSymbolObject().join(calledge.getState().getExecutionContext().getThis());
                }

                if (!callInfo.isImplicit()) {
                    // collect garbage (but not if implicit, because some objects may only be reachable via registers which we don't have here)
                    state.reduce(returnval);
                }

                // attempt to materialize variable object (for recursive functions), better to do after gc
                attemptMaterializeVariableObj(state);

                // transfer ordinary return value to caller
                if (result_reg != AbstractNode.NO_VALUE) {
                    state.writeRegister(result_reg, returnval);
                    state.getMustReachingDefs().addReachingDef(result_reg, node);
                }

                if (callInfo.isImplicit()) { // implicit call, trigger re-processing of the basic block containing the caller
                    boolean changed = c.propagate(state, new BlockAndContext<>(node.getImplicitAfterCall(), caller_context), false);
                    if (changed) { // note: this cannot be changed into calling propagateToBasicBlock, since we add a different block to the worklist
                        c.addToWorklist(node.getBlock(), caller_context);
                    }
                } else { // ordinary call, flow to next basic block after call node
                    c.propagateToBasicBlock(state, node.getBlock().getSingleSuccessor(), caller_context);
                }
            }
        }
    }

    /**
     * Materialize singleton object for the variable object and the top of the scope chain, if necessary and if possible.
     */
    public static void attemptMaterializeVariableObj(State state) {
        if (Options.get().isRecencyDisabled())
            return;
        ExecutionContext ec = state.getExecutionContext();
        Set<ObjectLabel> varObj = ec.getVariableObject();
        ScopeChain scopeChain = ec.getScopeChain();
        if (varObj.size() == 1 && scopeChain != null) {
            ObjectLabel objlabel = varObj.iterator().next();
            Set<ObjectLabel> scopeObj = scopeChain.getObject();
            if (!objlabel.isSingleton() && scopeObj.size() == 1 && scopeObj.iterator().next().equals(objlabel)) {
                ObjectLabel m = state.materializeObj(objlabel, true);
                state.setExecutionContext(new ExecutionContext(ScopeChain.make(singleton(m), scopeChain.next()), singleton(m), ec.getThis()));
            }
        }
    }

    /**
     * Replaces all definitely non-modified parts of the return state by the corresponding parts of the given states.
     * The store is restored from the call edge state; the stack is restored from the caller state.
     * The caller_entry_state is used for resolving polymorphic values.
     * Returns the updated returnval if non-null.
     */
    public static Value mergeFunctionReturn(State return_state, State caller_state, State calledge_state, State caller_entry_state,
                                            Renamings callee_renamings, Value returnval, Value exval) {
        return_state.makeWritableStore();
        Obj store_default = Canonicalizer.get().canonicalizeViaImmutableBox(caller_state.getStoreDefault().freeze());
        return_state.setStoreDefault(store_default);
        caller_state.setStoreDefault(store_default); // write back canonicalized object
        // strengthen each object and replace polymorphic values
        State renamed_calledge = calledge_state.clone();
        renameStoreAndRegisters(renamed_calledge, return_state.getRenamings());
        for (ObjectLabel objlabel : return_state.getStore().keySet()) {
            Obj obj = return_state.getObject(objlabel, true); // always preparing for object updates, even if no changes are made
            replacePolymorphicValues(obj, calledge_state, caller_entry_state, return_state);
            Obj calledge_obj = renamed_calledge.getObject(objlabel, false);
//            if (log.isDebugEnabled())
//                log.debug("strengthenNonModifiedParts on " + objlabel);
            obj.replaceNonModifiedParts(calledge_obj, return_state.getPartitioning().getMaybePartitionNodes());
        }
        // restore objects that were not used by the callee (i.e. either 'unknown' or never retrieved from basis_store to store)
        for (Map.Entry<ObjectLabel, Obj> me : renamed_calledge.getStore().entrySet())
            if (!return_state.getStore().containsKey(me.getKey()))
                return_state.putObject(me.getKey(), me.getValue()); // obj is freshly created at renameStoreAndRegisters, so freeze() unnecessary
        // remove objects that are equal to the default object
        return_state.removeObjectsEqualToDefault(caller_entry_state.getStoreDefault().isAllNone());
        // restore execution_context, stacked_objlabels, and stacked_funentries from caller
        return_state.setExecutionContext(caller_state.getExecutionContext().clone());
        return_state.getExecutionContext().rename(callee_renamings);
        return_state.setRegisters(renameAndRemovePartitions(caller_state.getRegisters(), callee_renamings, return_state.getPartitioning().getMaybePartitionNodes()));
        return_state.setMustReachingDefs(new MustReachingDefs(caller_state.getMustReachingDefs()));
        return_state.getMustEquals().setToBottom(); // TODO: could kill only the heap locations that may have been written by the function?
        return_state.setStacked(Options.get().isLazyDisabled() ? newSet(callee_renamings.rename(caller_state.getStackedObjects())) : null,
                newSet(caller_state.getStackedFunctions()));
        // replace polymorphic values in returnval and exval
        Value res = returnval == null ? null : replacePolymorphicValue(returnval, calledge_state, caller_entry_state, return_state);
        if (exval != null) {
            return_state.writeRegister(AbstractNode.EXCEPTION_REG, replacePolymorphicValue(exval, calledge_state, caller_entry_state, return_state));
        }
        // merge renamings
        return_state.getRenamings().add(calledge_state.getRenamings());
        return_state.getPartitioning().addMaybePartitionedNodes(caller_state.getPartitioning().getMaybePartitionNodes());
        log.debug("mergeFunctionReturn(...) done");
        return res;
    }

    /**
     * Replaces the polymorphic properties of the given object.
     * Used by {@link #mergeFunctionReturn(State, State, State, State, Renamings, Value, Value)}.
     */
    private static void replacePolymorphicValues(Obj obj,
                                                 State calledge_state,
                                                 State caller_entry_state,
                                                 State return_state) {
        Map<PKey, Value> newproperties = newMap();
        for (Map.Entry<PKey, Value> me : obj.getProperties().entrySet()) {
            Value v = me.getValue();
            v = replacePolymorphicValue(v, calledge_state, caller_entry_state, return_state);
            newproperties.put(me.getKey(), v);
        }
        obj.setProperties(newproperties);
        obj.setDefaultNumericProperty(replacePolymorphicValue(obj.getDefaultNumericProperty(), calledge_state, caller_entry_state, return_state));
        obj.setDefaultOtherProperty(replacePolymorphicValue(obj.getDefaultOtherProperty(), calledge_state, caller_entry_state, return_state));
        obj.setInternalPrototype(replacePolymorphicValue(obj.getInternalPrototype(), calledge_state, caller_entry_state, return_state));
        obj.setInternalValue(replacePolymorphicValue(obj.getInternalValue(), calledge_state, caller_entry_state, return_state));
        // TODO: scope chain polymorphic?
    }

    /**
     * Replaces the value if polymorphic.
     * Used by {@link #replacePolymorphicValues(Obj, State, State, State)}.
     */
    private static Value replacePolymorphicValue(Value v,
                                                 State calledge_state,
                                                 State caller_entry_state,
                                                 State return_state) {
        if (!v.isPolymorphic())
            return v;
        ObjectProperty p = v.getObjectProperty();
        ObjectLabel edge_objlabel = p.getObjectLabel();
        Obj calledge_obj = calledge_state.getObject(edge_objlabel, false);
        Value res;
        switch (p.getKind()) {
            case ORDINARY:
                res = calledge_obj.getProperty(p.getPropertyName());
                break;
            case INTERNAL_VALUE:
                res = calledge_obj.getInternalValue();
                break;
            case INTERNAL_PROTOTYPE:
                res = calledge_obj.getInternalPrototype();
                break;
            case INTERNAL_SCOPE:
            default:
                throw new AnalysisException("Unexpected value variable");
        }
        if (res.isUnknown()) {
            Obj caller_entry_obj = caller_entry_state.getObject(edge_objlabel, false);
            switch (p.getKind()) {
                case ORDINARY:
                    res = caller_entry_obj.getProperty(p.getPropertyName());
                    break;
                case INTERNAL_VALUE:
                    res = caller_entry_obj.getInternalValue();
                    break;
                case INTERNAL_PROTOTYPE:
                    res = caller_entry_obj.getInternalPrototype();
                    break;
                case INTERNAL_SCOPE:
                default:
                    throw new AnalysisException("Unexpected value variable");
            }
            if (res.isUnknown())
                throw new AnalysisException("Unexpected value (property reference: " + p + ", edge object label: " + edge_objlabel + ")");
            res = res.rename(calledge_state.getRenamings());
        }
        res = res.rename(return_state.getRenamings());
        return v.replaceValue(res);
    }

    /**
     * Renames the specified list of values and removes the partitions origined from nodes in removePartitionNodes.
     * Always returns a new list.
     */
    private static List<Value> renameAndRemovePartitions(List<Value> vs, Renamings s, Set<AbstractNode> removePartitionNodes) {
        List<Value> res = newList();
        for (int i = 0; i < vs.size(); i++) {
            Value v = vs.get(i);
            res.add(i, v != null ? PartitionedValue.removePartitions(v.rename(s), removePartitionNodes) : null);
        }
        return res;
    }

    /**
     * Renames the store and registers according to the given renamings.
     * Used by {@link #mergeFunctionReturn(State, State, State, State, Renamings, Value, Value)}.
     */
    private static void renameStoreAndRegisters(State state, Renamings s) {
        state.makeWritableStore();
        for (ObjectLabel objlabel : newList(state.getStore().keySet())) {
            // rename the object property values
            Obj obj = state.getObject(objlabel, false);
            Obj renamed_obj = obj.rename(s);
            if (!renamed_obj.equals(obj))
                state.putObject(objlabel, renamed_obj);
            Set<ObjectLabel> newobjlabels = s.rename(objlabel);
            for (ObjectLabel newobjlabel : newobjlabels)
                if (!newobjlabel.equals(objlabel))
                    state.propagateObj(newobjlabel, state, objlabel, true, false);
            if (!newobjlabels.contains(objlabel))
                state.removeObject(objlabel);
        }
        List<Value> registers = state.getRegisters();
        for (int i = 0; i < registers.size(); i++)
            if (registers.get(i) != null)
                registers.set(i, registers.get(i).rename(s));
    }

    /**
     * Implicit call to a user function.
     *
     * @param obj_f the function to call
     * @param callinfo information about the call
     * @return new or exising implicit-after-call block
     */
    public static BasicBlock implicitUserFunctionCall(ObjectLabel obj_f, CallInfo callinfo, Solver.SolverInterface c) {
        // create implicit after-call block if not already there
        BasicBlock implicitAfterCall = c.getNode().getImplicitAfterCall();
        if (implicitAfterCall == null) {
            implicitAfterCall = new BasicBlock(c.getNode().getBlock().getFunction());
            AbstractNode dummyNode = new NopNode(c.getNode().getSourceLocation());
            dummyNode.setArtificial();
            implicitAfterCall.addNode(dummyNode);
            implicitAfterCall.setEntryBlock(c.getNode().getBlock().getEntryBlock());
            c.getNode().setImplicitAfterCall(implicitAfterCall);
        }
        // call the function
        enterUserFunction(obj_f, callinfo, true, c);
        return implicitAfterCall;
    }

    /**
     * Extract return flow from implicit call to a user function.
     *
     * @param result            list of values containing results (may be extended by this call), or null if n/a
     * @param weak              if set, keep current state
     * @param implicitAfterCall the implicit-after-call block, or null if n/a
     * @return return value, none if no ordinary return flow, or null if result parameter is null
     */
    public static Value implicitUserFunctionReturn(Collection<Value> result, boolean weak, BasicBlock implicitAfterCall, Solver.SolverInterface c) {
        if (implicitAfterCall != null) {
            List<Value> registers = dk.brics.tajs.util.Collections.newList(c.getState().getRegisters());
            if (!weak) {
                c.getState().setToBottom();
            }
            State s = c.getAnalysisLatticeElement().getState(implicitAfterCall, c.getState().getContext());
            if (s != null) {
                if (result != null) {
                    result.add(s.readRegister(AbstractNode.RETURN_REG));
                }
                BlockAndContext<Context> from = new BlockAndContext<>(s.getBasicBlock(), s.getContext());
                BlockAndContext<Context> to = new BlockAndContext<>(c.getState().getBasicBlock(), c.getState().getContext());
                c.getMonitoring().visitPropagationPre(from, to);
                boolean changed = c.getState().propagate(s, false, false);
                c.getState().setRegisters(registers);
                c.getMonitoring().visitPropagationPost(from, to, changed);
            } // otherwise, treat as bottom (but don't kill flow - there may be no ordinary return flow)
        }
        if (result != null)
            return UnknownValueResolver.join(result, c.getState());
        else
            return null;
    }
}
