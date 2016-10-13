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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
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
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.ObjectProperty;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Summarized;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.AnalysisException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
     * Declares a function.
     */
    public static void declareFunction(DeclareFunctionNode n, Solver.SolverInterface c) {
        State state = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        Function fun = n.getFunction();
        boolean is_expression = n.isExpression();
        int result_reg = n.getResultRegister();
        // TODO: join function objects (p.72)? (if same n and same scope)
        HeapContext functionHeapContext = c.getAnalysis().getContextSensitivityStrategy().makeFunctionHeapContext(fun, c);
        ObjectLabel fn = new ObjectLabel(fun, functionHeapContext);
        // 13.2 step 2 and 3
        state.newObject(fn);
        Value f = Value.makeObject(fn);
        // 13.2 step 4
        state.writeInternalPrototype(fn, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        // 13.2 step 7
        ScopeChain scope = state.getScopeChain();
        if (is_expression && fun.getName() != null) {
            // p.79 (function expression with identifier)
            ObjectLabel front = new ObjectLabel(fun.getEntry().getFirstNode(), Kind.OBJECT);
            state.newObject(front);
            scope = ScopeChain.make(Collections.singleton(front), scope);
            pv.writePropertyWithAttributes(front, fun.getName(), f.setAttributes(false, true, true));
            /* From ES5, Annex D:
             In Edition 3, the algorithm for the production FunctionExpression with an Identifier adds an object 
             created as if by new Object() to the scope chain to serve as a scope for looking up the name of the 
             function. The identifier resolution rules (10.1.4 in Edition 3) when applied to such an object will, 
             if necessary, follow the object's prototype chain when attempting to resolve an identifier. This
             means all the properties of Object.prototype are visible as identifiers within that scope. In 
             practice most implementations of Edition 3 have not implemented this semantics. Edition 5 changes 
             the specified semantics by using a Declarative Environment Record to bind the name of the function.
             */ // TODO: use ES5 semantics of function expressions with identifier?
        } else if (!is_expression && fun.getName() != null) {
            // p.79 (function declaration)
            pv.declareAndWriteVariable(fun.getName(), f, true);
        }
        state.writeObjectScope(fn, scope);
        // 13.2 step 8
        pv.writePropertyWithAttributes(fn, "length", Value.makeNum(fun.getParameterNames().size()).setAttributes(true, true, true));
        // 13.2 step 9 
        ObjectLabel prototype = new ObjectLabel(n, Kind.OBJECT);
        state.newObject(prototype);
        state.writeInternalPrototype(prototype, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        // 13.2 step 10
        pv.writePropertyWithAttributes(prototype, "constructor", Value.makeObject(fn).setAttributes(true, false, false));
        // 13.2 step 11
        pv.writePropertyWithAttributes(fn, "prototype", Value.makeObject(prototype).setAttributes(false, true, false));
        state.writeInternalValue(prototype, Value.makeNum(Double.NaN)); // TODO: as in Rhino (?)
        if (result_reg != AbstractNode.NO_VALUE)
            state.writeRegister(result_reg, f);
    }

    /**
     * Enters a user-defined function.
     */
    public static void enterUserFunction(ObjectLabel obj_f, CallInfo call, boolean implicit, Solver.SolverInterface c) {
        State caller_state = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        ScopeChain obj_f_sc = caller_state.readObjectScope(obj_f);
        Value prototype = pv.readPropertyDirect(Collections.singleton(obj_f), "prototype");
        if (obj_f_sc == null || prototype.isNone())
            return; // must be spurious dataflow

        Function f = obj_f.getFunction();
        AbstractNode n = call.getSourceNode();

        if (log.isDebugEnabled())
            log.debug("enterUserFunction " + (implicit ? "(implicit)" : "") + "from node " + n.getIndex() + " at " + n.getSourceLocation()
                    + " to " + f + " at " + f.getSourceLocation());

        State edge_state = caller_state.clone();
        c.setState(edge_state);
        HeapContext heapContext = c.getAnalysis().getContextSensitivityStrategy().makeActivationAndArgumentsHeapContext(edge_state, obj_f, call, c);

        Set<ObjectLabel> this_objs;
        Summarized extra_summarized = new Summarized();
        if (call.isConstructorCall()) {
            // 13.2.2.1-2 create new object
            this_objs = newSet();
            ObjectLabel new_obj = new ObjectLabel(n, Kind.OBJECT, heapContext);
            edge_state.newObject(new_obj);
            extra_summarized.addDefinitelySummarized(new_obj);
            this_objs.add(new_obj);
            // 13.2.2.3-5 provide [[Prototype]]
            prototype = UnknownValueResolver.getRealValue(prototype, caller_state);
            if (prototype.isMaybePrimitive())
                prototype = prototype.restrictToObject().joinObject(InitialStateBuilder.OBJECT_PROTOTYPE);
            edge_state.writeInternalPrototype(new_obj, prototype);
        } else {
            this_objs = call.prepareThis(caller_state, edge_state);
        }

        // collect arguments and special arguments for context sensitivity
        int num_actuals = call.getNumberOfArgs();
        boolean num_actuals_unknown = call.isUnknownNumberOfArgs();
        Value unknown_arg = null;
        List<Value> actuals = new ArrayList<>();
        if (num_actuals_unknown)
            unknown_arg = call.getUnknownArg();
        else {
            for (int i = 0; i < num_actuals; i++)
                actuals.add(call.getArg(i));
        }

        // 10.2.3 enter new execution context, 13.2.1 transfer parameters, 10.1.6/8 provide 'arguments' object
        ObjectLabel varobj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ACTIVATION, heapContext); // better to use entry than invoke here
        edge_state.newObject(varobj);
        extra_summarized.addDefinitelySummarized(varobj);
        ObjectLabel argobj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ARGUMENTS, heapContext);
        edge_state.newObject(argobj);
        extra_summarized.addDefinitelySummarized(argobj);
        ScopeChain sc = ScopeChain.make(Collections.singleton(varobj), obj_f_sc);

        edge_state.setExecutionContext(new ExecutionContext(sc, singleton(varobj), newSet(this_objs)));
        pv.declareAndWriteVariable("arguments", Value.makeObject(argobj), true);
        edge_state.writeInternalPrototype(argobj, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writePropertyWithAttributes(argobj, "callee", Value.makeObject(obj_f).setAttributes(true, false, false));
        int num_formals = f.getParameterNames().size();
        pv.writePropertyWithAttributes(argobj, "length",
                (num_actuals_unknown ? Value.makeAnyNumUInt() : Value.makeNum(num_actuals)).setAttributes(true, false, false));
        for (int i = 0; i < num_formals || (!num_actuals_unknown && i < num_actuals); i++) {
            Value v;
            if (num_actuals_unknown)
                v = unknown_arg.summarize(extra_summarized);
            else if (i < num_actuals) {
                v = actuals.get(i).summarize(extra_summarized);
                pv.writePropertyWithAttributes(argobj, Integer.toString(i), v.setAttributes(false, false, false)); // from ES5 Annex E: "In Edition 5 the array indexed properties of argument objects that correspond to actual formal parameters are enumerable. In Edition 3, such properties were not enumerable."
            } else
                v = Value.makeUndef(); // 10.1.3
            if (i < num_formals)
                pv.declareAndWriteVariable(f.getParameterNames().get(i), v, true); // 10.1.3
        }
        // FIXME: properties of 'arguments' should be shared with the formal parameters (see 10.1.8 item 4) - easy solution that does not require Reference types? - github #21
        // (see comment at NodeTransfer/WriteVariable... - also needs the other way around...)

        if (c.isScanning())
            return;

        edge_state.stackObjectLabels();
        edge_state.clearRegisters();

        if (this_objs.size() > 1 && Options.get().isContextSpecializationEnabled()) {
            // specialize edge_state such that 'this' becomes a singleton
            if (log.isDebugEnabled())
                log.debug("specializing edge state, this = " + this_objs);
            for (Iterator<ObjectLabel> it = this_objs.iterator(); it.hasNext(); ) {
                ObjectLabel this_obj = it.next();
                State next_edge_state = it.hasNext() ? edge_state.clone() : edge_state;
                next_edge_state.getExecutionContext().setThisObject(singleton(this_obj)); // (execution context should be writable here)
                propagateToFunctionEntry(next_edge_state, n, obj_f, call, implicit, c);
            }
        } else
            propagateToFunctionEntry(edge_state, n, obj_f, call, implicit, c);

        c.setState(caller_state);
    }

    private static void propagateToFunctionEntry(State edge_state, AbstractNode n, ObjectLabel obj_f, CallInfo callInfo, boolean implicit, Solver.SolverInterface c) {
        Context edge_context = c.getAnalysis().getContextSensitivityStrategy().makeFunctionEntryContext(edge_state, obj_f, callInfo, c);
        c.propagateToFunctionEntry(n, edge_state.getContext(), edge_state, edge_context, obj_f.getFunction().getEntry(), implicit);
    }

    /**
     * Determines the value of 'this' when entering a function (other than call/apply and other than constructor call to host function).
     * May have side-effects on callee_state due to wrapping of primitive values.
     */
    public static Set<ObjectLabel> determineThis(AbstractNode n,
                                                 State caller_state, State callee_state, Solver.SolverInterface c,
                                                 int base_reg) {
        Set<ObjectLabel> this_obj = newSet();
        // 10.2.3.3 and 11.2.3.6
        if (base_reg == AbstractNode.NO_VALUE) { // 11.2.1.5
            this_obj.add(InitialStateBuilder.GLOBAL);
        } else {
            // FIXME: see 10.2.3, 11.2.3 and 11.2.1.5 - is following call to toObjectLabels correct?
            State ts = c.getState();
            c.setState(callee_state);
            Set<ObjectLabel> t = Conversion.toObjectLabels(n, caller_state.readRegister(base_reg), c); // TODO: likely loss of precision if multiple object labels (or a summary object) as 'this' value
            c.setState(ts);
            // TODO: disable conversion warnings for this call to Conversion.toObjectLabels? (test/micro/test163.js)
            // 11.2.3 and 10.1.6: replace activation objects by the global object
            for (ObjectLabel objlabel : t)
                if (objlabel.getKind() == Kind.ACTIVATION)
                    this_obj.add(InitialStateBuilder.GLOBAL);
                else
                    this_obj.add(objlabel);
        }
        return this_obj;
    }

    /**
     * Leaves a user-defined function.
     */
    public static void leaveUserFunction(Value returnval, boolean exceptional, Function f, State state, Solver.SolverInterface c,
                                         NodeAndContext<Context> specific_caller, Context specific_edge_context, boolean implicit) {

        if (f.isMain()) { // TODO: also report uncaught exceptions and return immediately for event handlers
            final String msgkey = "Uncaught exception";
            AbstractNode n = f.getOrdinaryExit().getLastNode();
            if (exceptional) {
                returnval = UnknownValueResolver.getRealValue(returnval, state);
                TreeSet<SourceLocation> objs = new TreeSet<>(returnval.getObjectSourceLocations());
                if (!objs.isEmpty()) { // use object source locations
                    c.getMonitoring().addMessage(n, Severity.LOW, msgkey, "Uncaught exception, constructed at " + objs); // TODO: give user-defined exceptions higher severity level?
                } else { // alternatively, use the primitive values in the message
                    Value v = returnval.restrictToNotObject();
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

        if (specific_caller != null)
            returnToCaller(specific_caller.getNode(), specific_caller.getContext(), specific_edge_context, implicit, returnval, exceptional, f, state, c);
        else {
            // try each call node that calls f with the current callee context
            CallGraph<State, Context, CallEdge> cg = c.getAnalysisLatticeElement().getCallGraph();
            for (Iterator<CallGraph.ReverseEdge<Context>> i = cg.getSources(BlockAndContext.makeEntry(state.getBasicBlock(), state.getContext())).iterator(); i.hasNext(); ) {
                CallGraph.ReverseEdge<Context> re = i.next();
                if (c.isCallEdgeCharged(re.getCallNode().getBlock(), re.getCallerContext(), re.getEdgeContext(), BlockAndContext.makeEntry(state.getBasicBlock(), state.getContext())))
                    returnToCaller(re.getCallNode(), re.getCallerContext(), re.getEdgeContext(), re.isImplicit(), returnval, exceptional, f, i.hasNext() ? state.clone() : state, c);
                else if (log.isDebugEnabled())
                    log.debug("skipping call edge from " + re.getCallNode() + ", call context " + re.getCallerContext() + ", edge context " + re.getEdgeContext());

            }
        }
    }

    public static State returnToCaller(AbstractNode node, Context caller_context, Context edge_context, boolean implicit, Value returnval, boolean exceptional, Function f, State state, Solver.SolverInterface c) {
        final boolean is_constructor;
        final int result_reg;
        if (implicit) { // implicit function call, e.g. valueOf/toString
            is_constructor = false;
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
            log.debug("trying call node " + node.getIndex() + ": " + node
                    + " at " + node.getSourceLocation() + "\n" +
                    "caller context: " + caller_context + ", callee context: " + state.getContext());

        // apply inverse transform
        state.writeRegister(0, returnval); // TODO: pass returnval explicitly through returnFromFunctionExit instead of using a register
        c.returnFromFunctionExit(state, node, caller_context, f.getEntry(), edge_context, implicit);
        returnval = state.readRegister(0);
        state.clearRegisters();
        if (state.isNone())
            return state; // flow was cancelled, probably something needs to be recomputed

        // merge newstate with caller state and call edge state
        Summarized callee_summarized = new Summarized(state.getSummarized());
        HeapContext scopeHeapContext = state.getScopeChain().getObject().iterator().next().getHeapContext();
        HeapContext heapContext = scopeHeapContext; // this should give us the activation object that was created at enterUserFunction
        ObjectLabel activation_obj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ACTIVATION, heapContext);
        callee_summarized.addDefinitelySummarized(activation_obj);
        ObjectLabel arguments_obj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ARGUMENTS, heapContext);
        callee_summarized.addDefinitelySummarized(arguments_obj);
        if (is_constructor) {
            ObjectLabel this_obj = new ObjectLabel(node, Kind.OBJECT, heapContext);
            callee_summarized.addDefinitelySummarized(this_obj);
        } // FIXME: determineThis may create additional objects (wrapped primitives in toObjectLabels conversion) that should be included in callee_summarized!
        State calledge_state = c.getAnalysisLatticeElement().getCallGraph().getCallEdge(node, caller_context, f.getEntry(), edge_context).getState();
        returnval = mergeFunctionReturn(state, c.getAnalysisLatticeElement().getStates(node.getBlock()).get(caller_context),
                calledge_state,
                c.getAnalysisLatticeElement().getState(BlockAndContext.makeEntry(node.getBlock(), caller_context)),
                callee_summarized,
                returnval, null); // TODO: not obvious why this part is in dk.brics.tajs.analysis and the renaming and localization is done via dk.brics.tajs.solver...
        if (node.isRegistersDone())
            state.clearOrdinaryRegisters();
        if (implicit) {
            state.setBasicBlock(node.getImplicitAfterCall());
            state.setContext(caller_context);
        } else {
            state.setBasicBlock(node.getBlock().getSingleSuccessor());
            state.setContext(caller_context);
        }

        if (exceptional) {
            // collect garbage
            state.reduce(returnval);

            // transfer exception value to caller
            Exceptions.throwException(state, returnval, c, node);
        } else {
            returnval = UnknownValueResolver.getRealValue(returnval, state);
            if (!returnval.isNone()) { // skip if no value (can happen when propagateToFunctionEntry calls transferReturn)

                if (is_constructor && returnval.isMaybePrimitive()) {
                    // 13.2.2.7-8 replace non-object by the new object (which is kept in 'this' at the call edge)
                    returnval = returnval.restrictToObject().join(Value.makeObject(calledge_state.getExecutionContext().getThisObject()));
                }

                if (!implicit) {
                    // collect garbage (but not if implicit, because some objects may only be reachable via registers which we don't have here)
                    state.reduce(returnval);
                }

                // transfer ordinary return value to caller
                if (result_reg != AbstractNode.NO_VALUE)
                    state.writeRegister(result_reg, returnval);

                if (implicit) { // implicit call, trigger re-processing of the basic block containing the caller
                    c.getAnalysisLatticeElement().propagate(state, node.getImplicitAfterCall(), caller_context, false);
                    c.addToWorklist(node.getBlock(), caller_context);
                } else { // ordinary call, flow to next basic block after call node
                    c.propagateToBasicBlock(state, node.getBlock().getSingleSuccessor(), caller_context);
                }
            }
        }
        return state;
    }

    /**
     * Replaces all definitely non-modified parts of the return state by the corresponding parts of the given states.
     * The store is restored from the call edge state; the stack is restored from the caller state.
     * The caller_entry_state is used for resolving polymorphic values.
     * Returns the updated returnval if non-null.
     */
    public static Value mergeFunctionReturn(State return_state, State caller_state, State calledge_state, State caller_entry_state,
                                            Summarized callee_summarized, Value returnval, Value exval) {
        return_state.makeWritableStore();
        return_state.setStoreDefault(caller_state.getStoreDefault().freeze());
        // strengthen each object and replace polymorphic values
        State summarized_calledge = calledge_state.clone();
        summarizeStoreAndRegisters(summarized_calledge, return_state.getSummarized());
        for (ObjectLabel objlabel : return_state.getStore().keySet()) {
            Obj obj = return_state.getObject(objlabel, true); // always preparing for object updates, even if no changes are made
            replacePolymorphicValues(obj, calledge_state, caller_entry_state, callee_summarized);
            Obj calledge_obj = summarized_calledge.getObject(objlabel, false);
            if (log.isDebugEnabled())
                log.debug("strengthenNonModifiedParts on " + objlabel);
            obj.replaceNonModifiedParts(calledge_obj);
        }
        // restore objects that were not used by the callee (i.e. either 'unknown' or never retrieved from basis_store to store)
        for (Map.Entry<ObjectLabel, Obj> me : summarized_calledge.getStore().entrySet())
            if (!return_state.getStore().containsKey(me.getKey()))
                return_state.putObject(me.getKey(), me.getValue()); // obj is freshly created at summarizeStoreAndRegisters, so freeze() unnecessary
        // remove objects that are equal to the default object
        return_state.removeObjectsEqualToDefault(caller_entry_state.getStoreDefault().isAllNone());
        // restore execution_context and stacked_objlabels from caller
        return_state.setExecutionContext(caller_state.getExecutionContext().clone());
        return_state.getExecutionContext().summarize(callee_summarized);
        return_state.setRegisters(summarize(caller_state.getRegisters(), callee_summarized));
        if (Options.get().isLazyDisabled()) {
            return_state.setStackedObjects(newSet(callee_summarized.summarize(caller_state.getStackedObjects())));
        }
        // merge summarized sets
        return_state.getSummarized().add(calledge_state.getSummarized());
        Value res = returnval == null ? null : replacePolymorphicValue(returnval, calledge_state, caller_entry_state, callee_summarized);
        if (exval != null) {
            return_state.writeRegister(AbstractNode.EXCEPTION_REG, replacePolymorphicValue(exval, calledge_state, caller_entry_state, callee_summarized));
        }
        log.debug("mergeFunctionReturn(...) done");
        return res;
    }

    /**
     * Replaces the polymorphic properties of the given object.
     * Used by {@link #mergeFunctionReturn(State, State, State, State, Summarized, Value, Value)}.
     */
    private static void replacePolymorphicValues(Obj obj,
                                                 State calledge_state,
                                                 State caller_entry_state,
                                                 Summarized callee_summarized) {
        Map<String, Value> newproperties = newMap();
        for (Map.Entry<String, Value> me : obj.getProperties().entrySet()) {
            Value v = me.getValue();
            v = replacePolymorphicValue(v, calledge_state, caller_entry_state, callee_summarized);
            newproperties.put(me.getKey(), v);
        }
        obj.setProperties(newproperties);
        obj.setDefaultArrayProperty(replacePolymorphicValue(obj.getDefaultArrayProperty(), calledge_state, caller_entry_state, callee_summarized));
        obj.setDefaultNonArrayProperty(replacePolymorphicValue(obj.getDefaultNonArrayProperty(), calledge_state, caller_entry_state, callee_summarized));
        obj.setInternalPrototype(replacePolymorphicValue(obj.getInternalPrototype(), calledge_state, caller_entry_state, callee_summarized));
        obj.setInternalValue(replacePolymorphicValue(obj.getInternalValue(), calledge_state, caller_entry_state, callee_summarized));
        // TODO: scope chain polymorphic?
    }

    /**
     * Replaces the value if polymorphic.
     * Used by {@link #replacePolymorphicValues(Obj, State, State, Summarized)}.
     */
    private static Value replacePolymorphicValue(Value v,
                                                 State calledge_state,
                                                 State caller_entry_state,
                                                 Summarized callee_summarized) {
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
            res = res.summarize(calledge_state.getSummarized());
        }
        res = res.summarize(callee_summarized);
        return v.replaceValue(res);
    }

    /**
     * Summarizes the specified list of values.
     * Always returns a new list.
     */
    private static List<Value> summarize(List<Value> vs, Summarized s) {
        List<Value> res = newList();
        for (int i = 0; i < vs.size(); i++) {
            Value v = vs.get(i);
            res.add(i, v != null ? v.summarize(s) : null);
        }
        return res;
    }

    /**
     * Summarizes the store and registers according to the given summarization.
     * Used by {@link #mergeFunctionReturn(State, State, State, State, Summarized, Value, Value)}.
     */
    private static void summarizeStoreAndRegisters(State state, Summarized s) {
        state.makeWritableStore();
        for (ObjectLabel objlabel : newList(state.getStore().keySet())) {
            // summarize the object property values
            Obj obj = state.getObject(objlabel, false);
            Obj summarized_obj = obj.summarize(s);
            if (!summarized_obj.equals(obj))
                state.putObject(objlabel, summarized_obj);
            if (objlabel.isSingleton()) {
                if (s.isMaybeSummarized(objlabel))
                    state.propagateObj(objlabel.makeSummary(), state, objlabel, true);
                if (s.isDefinitelySummarized(objlabel))
                    state.removeObject(objlabel);
            }
        }
        List<Value> registers = state.getRegisters();
        for (int i = 0; i < registers.size(); i++)
            if (registers.get(i) != null)
                registers.set(i, registers.get(i).summarize(s));
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
                c.getState().setToNone();
            }
            State s = c.getAnalysisLatticeElement().getState(implicitAfterCall, c.getState().getContext());
            if (s != null) {
                if (result != null) {
                    result.add(s.readRegister(AbstractNode.RETURN_REG));
                }
                c.getState().propagate(s, false);
                c.getState().setRegisters(registers);
            } // otherwise, treat as bottom (but don't kill flow - there may be no ordinary return flow)
        }
        if (result != null)
            return UnknownValueResolver.join(result, c.getState());
        else
            return null;
    }
}
