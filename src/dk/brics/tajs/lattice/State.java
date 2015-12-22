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

package dk.brics.tajs.lattice;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.OptionValues;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.solver.IState;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.sortedEntries;

/**
 * Abstract state for block entries.
 * Mutable.
 */
public class State implements IState<State, Context, CallEdge> {

    private static Logger log = Logger.getLogger(State.class);

    private GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, ?>.SolverInterface c;

    /**
     * The basic block owning this state.
     */
    private BasicBlock block;

    /**
     * The context for this state.
     */
    private Context context; // may be shared by other State objects

    /**
     * Map from ObjectLabel to Object.
     */
    private Map<ObjectLabel, Obj> store;

    private Obj store_default; // either the none obj (for program entry) or the unknown obj (all other locations)

    private boolean writable_store; // for copy-on-write

    /**
     * Reusable immutable part of the store.
     * Entries may be overridden by 'store'.
     * Not used if lazy propagation is enabled.
     */
    private Map<ObjectLabel, Obj> basis_store;

    /**
     * Current execution context.
     */
    private ExecutionContext execution_context;

    private boolean writable_execution_context; // for copy-on-write

    /**
     * Maybe/definitely summarized objects since function entry. (Contains the singleton object labels.)
     */
    private Summarized summarized;

    /**
     * Register values.
     */
    private List<Value> registers; // register values never have attributes or modified flag

    private boolean writable_registers; // for copy-on-write

    /**
     * Object labels that appear on the stack.
     */
    private Set<ObjectLabel> stacked_objlabels; // not used if lazy propagation is enabled

    private boolean writable_stacked_objlabels; // for copy-on-write

    private StateExtras extras;

    private static int number_of_states_created;

    private static int number_of_makewritable_store;

    private static int number_of_makewritable_registers; // TODO: currently not used

    /**
     * Constructs a new none-state (representing the empty set of concrete states).
     */
    public State(GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, ?>.SolverInterface c, BasicBlock block) {
        this.c = c;
        this.block = block;
        summarized = new Summarized();
        extras = new StateExtras();
        setToNone();
        number_of_states_created++;
    }

    /**
     * Constructs a new state as a copy of the given state.
     */
    private State(State x) {
        c = x.c;
        block = x.block;
        context = x.context;
        summarized = new Summarized(x.summarized);
        store_default = x.store_default.freeze();
        extras = new StateExtras(x.extras);
//        if (Options.get().isCopyOnWriteDisabled()) {
        store = newMap();
        for (Map.Entry<ObjectLabel, Obj> xs : x.store.entrySet())
            store.put(xs.getKey(), xs.getValue().freeze());
        basis_store = x.basis_store;
        writable_store = true;
        execution_context = x.execution_context.clone();
        registers = newList(x.registers);
        writable_registers = true;
        stacked_objlabels = newSet(x.stacked_objlabels);
        writable_stacked_objlabels = true;
//        } else {
//            store = x.store;
//            basis_store = x.basis_store;
//            execution_context = x.execution_context;
//            registers = x.registers;
//            stacked_objlabels = x.stacked_objlabels;
//            x.writable_execution_context = writable_execution_context = false;
//            x.writable_store = writable_store = false;
//            x.writable_registers = writable_registers = false;
//            x.writable_stacked_objlabels = writable_stacked_objlabels = false;
//        }
        number_of_states_created++;
    }

    /**
     * Constructs a new state as a copy of this state.
     */
    @Override
    public State clone() {
        return new State(this);
    }

    /**
     * Returns the solver interface.
     */
    public GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, ?>.SolverInterface getSolverInterface() {
        return c;
    }

    /**
     * Returns the extra stuff.
     */
    public StateExtras getExtras() {
        return extras;
    }

    /**
     * Returns the basic block owning this state.
     */
    public BasicBlock getBasicBlock() {
        return block;
    }

    @Override
    public Context getContext() {
        return context;
    }

    /**
     * Checks whether the return register has a value.
     */
    public boolean hasReturnRegisterValue() {
        return AbstractNode.RETURN_REG < registers.size() && registers.get(AbstractNode.RETURN_REG) != null;
    }

    /**
     * Checks whether the exception register has a value.
     */
    public boolean hasExceptionRegisterValue() {
        return AbstractNode.EXCEPTION_REG < registers.size() && registers.get(AbstractNode.EXCEPTION_REG) != null;
    }

    /**
     * Sets the basic block owning this state.
     */
    public void setBasicBlock(BasicBlock block) {
        this.block = block;
    }

    /**
     * Sets the context.
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Checks that the owner block and context are the given.
     *
     * @throws AnalysisException if mismatch
     */
    public void checkOwner(BasicBlock other_block, Context other_context) {
        if (!block.equals(other_block) || !context.equals(other_context))
            throw new AnalysisException("State owner block/context mismatch!");
    }

    /**
     * Returns the store (excluding the basis store).
     * Only for reading!
     */
    public Map<ObjectLabel, Obj> getStore() {
        return store;
    }

    /**
     * Sets an object in the store.
     */
    private void putObject(ObjectLabel objlabel, Obj obj) {
        makeWritableStore();
        store.put(objlabel, obj);
    }

    /**
     * Looks up an object in the store.
     */
    public Obj getObject(ObjectLabel objlabel, boolean writable) {
        if (writable)
            makeWritableStore();
        Obj obj = store.get(objlabel);
        if (obj != null && writable && !obj.isWritable()) {
            // object exists but isn't yet writable, make it writable
            obj = new Obj(obj);
            store.put(objlabel, obj);
            if (log.isDebugEnabled())
                log.debug("making writable object from store: " + objlabel);
        }
        if (obj == null && basis_store != null) {
            // check the basis_store
            obj = basis_store.get(objlabel);
            if (obj != null && writable) {
                obj = new Obj(obj);
                store.put(objlabel, obj);
                if (log.isDebugEnabled())
                    log.debug("making writable object from basis store: " + objlabel);
            }
        }
        if (obj == null) {
            // take the default
            obj = store_default;
            if (writable) {
                obj = new Obj(obj);
                store.put(objlabel, obj);
                if (log.isDebugEnabled())
                    log.debug("making writable object from store default: " + objlabel);
            }
        }
        return obj;
    }

    /**
     * Returns the summarized sets.
     */
    public Summarized getSummarized() {
        return summarized;
    }

    /**
     * Sets the current store contents as the basis store.
     * After this, objects in the basis store should never be summarized.
     * Ignored if lazy propagation is enabled.
     */
    public void freezeBasisStore() {
        if (Options.get().isLazyDisabled()) {
            basis_store = store;
            store = newMap();
            writable_store = true;
            log.debug("freezeBasisStore()");
        }
    }

    /**
     * Makes store writable (for copy-on-write).
     */
    private void makeWritableStore() {
        if (writable_store)
            return;
        store = newMap(store);
        writable_store = true;
        number_of_makewritable_store++;
    }

    /**
     * Makes execution_context writable (for copy-on-write).
     */
    private void makeWritableExecutionContext() {
        if (writable_execution_context)
            return;
        execution_context = execution_context.clone();
        writable_execution_context = true;
    }

    /**
     * Makes registers writable (for copy-on-write).
     */
    private void makeWritableRegisters() {
        if (writable_registers)
            return;
        registers = newList(registers);
        writable_registers = true;
        number_of_makewritable_registers++;
    }

    /**
     * Makes stacked object set writable (for copy-on-write).
     */
    private void makeWritableStackedObjects() {
        if (!Options.get().isLazyDisabled())
            return;
        if (writable_stacked_objlabels)
            return;
        stacked_objlabels = newSet(stacked_objlabels);
        writable_stacked_objlabels = true;
    }

    /**
     * Returns the total number of State objects created.
     */
    public static int getNumberOfStatesCreated() {
        return number_of_states_created;
    }

    /**
     * Resets the global counters.
     */
    public static void reset() {
        number_of_states_created = 0;
        number_of_makewritable_store = 0;
    }

    /**
     * Returns the total number of makeWritableStore operations.
     */
    public static int getNumberOfMakeWritableStoreCalls() {
        return number_of_makewritable_store;
    }

    /**
     * Clears modified flags for all values in the store.
     * Ignores the basis store.
     */
    private void clearModified() {
        Map<ObjectLabel, Obj> new_store = newMap();
        for (Map.Entry<ObjectLabel, Obj> xs : store.entrySet()) {
            Obj obj = xs.getValue();
            if (obj.isSomeModified()) {
                obj = new Obj(obj);
                obj.clearModified();
            }
            new_store.put(xs.getKey(), obj);
        }
        store = new_store;
        writable_store = true;
        number_of_makewritable_store++;
        log.debug("clearModified()");
    }

    /**
     * Sets this state to the none-state (representing the empty set of concrete states).
     * Used for representing 'no flow'.
     */
    public void setToNone() {
        basis_store = null;
        summarized.clear();
        extras.setToNone();
//        if (Options.get().isCopyOnWriteDisabled()) {
        store = newMap();
        writable_store = true;
        if (registers == null) {
            registers = new ArrayList<>();
        } else {
            for (int i = 0; i < registers.size(); i++) {
                registers.set(i, Value.makeNone());
            }
        }
        writable_registers = true;
        stacked_objlabels = newSet();
        writable_stacked_objlabels = true;
//        } else {
//            store = Collections.emptyMap();
//            writable_store = false;
//            registers = Collections.emptyList();
//            writable_registers = false;
//            stacked_objlabels = Collections.emptySet();
//            writable_stacked_objlabels = false;
//        }
        execution_context = new ExecutionContext();
        writable_execution_context = true;
        store_default = Obj.makeNone();
    }

//    /**
//     * Sets all object properties to 'unknown'.
//     */
//    public void setToUnknown() { // TODO: currently unused
//        if (!Options.get().isLazyDisabled()) {
//            store = newMap();
//            store_default = Obj.makeUnknown();
//            writable_store = true;
//            registers = Collections.emptyList();
//            writable_registers = false;
//        }
//    }

    @Override
    public boolean isNone() {
        return execution_context.isEmpty();
    }

    /**
     * Replaces all definitely non-modified parts of this state by the corresponding parts of the given states.
     * The store is restored from the call edge state; the stack is restored from the caller state.
     * The caller_entry_state is used for resolving polymorphic values.
     * Returns the updated returnval if non-null.
     */
    public Value mergeFunctionReturn(State caller_state,
                                     State calledge_state,
                                     State caller_entry_state,
                                     Summarized callee_summarized,
                                     Value returnval, Value exval) {
        makeWritableStore();
        store_default = caller_state.store_default.freeze();
        if (basis_store != caller_state.basis_store || basis_store != calledge_state.basis_store)
            throw new AnalysisException("Not identical basis stores");
        // strengthen each object and replace polymorphic values
        State summarized_calledge = calledge_state.clone();
        summarized_calledge.summarizeStore(summarized);
        for (ObjectLabel objlabel : store.keySet()) {
            Obj obj = getObject(objlabel, true); // always preparing for object updates, even if no changes are made
            replacePolymorphicValues(obj, calledge_state, caller_entry_state, callee_summarized);
            Obj calledge_obj = summarized_calledge.getObject(objlabel, false);
            if (log.isDebugEnabled())
                log.debug("strengthenNonModifiedParts on " + objlabel);
            obj.replaceNonModifiedParts(calledge_obj);
        }
        // restore objects that were not used by the callee (i.e. either 'unknown' or never retrieved from basis_store to store)
        for (Map.Entry<ObjectLabel, Obj> me : summarized_calledge.getStore().entrySet())
            if (!store.containsKey(me.getKey()))
                putObject(me.getKey(), me.getValue()); // obj is freshly created at summarizeStore, so freeze() unnecessary
        // remove objects that are equal to the default object
        for (Iterator<Map.Entry<ObjectLabel, Obj>> it = store.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<ObjectLabel, Obj> me = it.next();
            if (me.getValue().equals(store_default)) {
                if (log.isDebugEnabled())
                    log.debug("removing object equal to the default: " + me.getKey());
                it.remove();
            } else if (caller_entry_state.store_default.isAllNone() && store_default.isUnknown() && me.getValue().isSomeNone()) {
                if (log.isDebugEnabled())
                    log.debug("removing none object: " + me.getKey());
                it.remove();
            }
        }
        // restore execution_context and stacked_objlabels from caller
        execution_context = caller_state.execution_context.clone();
        execution_context.summarize(callee_summarized);
        writable_execution_context = true;
        registers = summarize(caller_state.registers, callee_summarized);
        writable_registers = true;
        if (Options.get().isLazyDisabled())
            stacked_objlabels = callee_summarized.summarize(caller_state.stacked_objlabels);
        writable_stacked_objlabels = false;
        // merge summarized sets
        summarized.add(calledge_state.summarized);
        Value res = returnval == null ? null : replacePolymorphicValue(returnval, calledge_state, caller_entry_state, callee_summarized);
        if (exval != null) {
            writeRegister(AbstractNode.EXCEPTION_REG, replacePolymorphicValue(exval, calledge_state, caller_entry_state, callee_summarized));
        }
        log.debug("mergeFunctionReturn(...) done");
        return res;
    }

    /**
     * Replaces the polymorphic properties of the given object.
     * Used by {@link #mergeFunctionReturn(State, State, State, Summarized, Value, Value)}.
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
        PropertyReference p = v.getPropertyReference();
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
     * Summarizes the store according to the given summarization.
     * Used by {@link #mergeFunctionReturn(State, State, State, Summarized, Value, Value)}.
     */
    private void summarizeStore(Summarized s) {
        makeWritableStore();
        for (ObjectLabel objlabel : newList(store.keySet())) {
            // summarize the object property values
            Obj obj = getObject(objlabel, false);
            Obj summarized_obj = obj.summarize(s);
            if (!summarized_obj.equals(obj))
                putObject(objlabel, summarized_obj);
            if (objlabel.isSingleton()) {
                if (s.isMaybeSummarized(objlabel))
                    propagateObj(objlabel.makeSummary(), this, objlabel, true);
                if (s.isDefinitelySummarized(objlabel))
                    store.remove(objlabel);
            }
        }
    }

    /**
     * Propagates the given state into this state.
     * Replaces 'unknown' and polymorphic values when necessary.
     * Assumes that the states belong to the same block and context.
     *
     * @return true if an object changed (note there may be other changes due to recoveries)
     */
    @Override
    public boolean propagate(State s, boolean funentry) {
        if (Options.get().isDebugOrTestEnabled() && !store_default.isAllNone() && !s.store_default.isAllNone() && !store_default.equals(s.store_default))
            throw new AnalysisException("Expected store default objects to be equal");
        if (log.isDebugEnabled() && Options.get().isIntermediateStatesEnabled()) {
            log.debug("join this state: " + this);
            log.debug("join other state: " + s);
        }
        if (s.isNone()) {
            return false;
        }
        boolean thisIsNone = isNone();
        makeWritableStore();
        makeWritableExecutionContext();
        makeWritableRegisters();
        makeWritableStackedObjects();
        boolean changed = execution_context.add(s.execution_context);
        Set<ObjectLabel> labs = newSet();
        labs.addAll(store.keySet());
        labs.addAll(s.store.keySet());
        for (ObjectLabel lab : labs)
            changed |= propagateObj(lab, s, lab, false);
        if (Options.get().isLazyDisabled())
            changed |= stacked_objlabels.addAll(s.stacked_objlabels);
        changed |= extras.propagate(s.extras);
        if (!funentry) {
            for (int i = 0; i < registers.size() || i < s.registers.size(); i++) {
                Value v1 = i < registers.size() ? registers.get(i) : null;
                Value v2 = i < s.registers.size() ? s.registers.get(i) : null;
                Value v = (v1 != null && v2 != null) ? UnknownValueResolver.join(v1, this, v2, s) : null; // if either is null, it must be dead anyway
                if (v1 != null && !v1.equals(v)) {
                    if (i < registers.size())
                        registers.set(i, v);
                    else
                        registers.add(v);
                    changed = true;
                }
            }
            if (thisIsNone) {
                summarized = new Summarized(s.summarized); // naively joining definitely_summarized with bottom would lose precision
                changed = true;
            } else {
                changed |= summarized.join(s.summarized);
            }
        }
        if (store_default.isAllNone() && !s.store_default.isAllNone()) {
            for (ObjectLabel lab : s.store.keySet()) { // materialize before changing default
                if (!store.containsKey(lab)) {
                    store.put(lab, store_default);
                }
            }
            store_default = s.store_default;
            store_default.freeze();
            changed = true;
        }
        if (log.isDebugEnabled()) {
            if (Options.get().isIntermediateStatesEnabled())
                log.debug("propagate result state: " + this);
            else
                log.debug("propagate(...)");
        }
        return changed;
    }

    /**
     * Propagates objlabel2 from state2 into objlabel1 in this state.
     * Replaces 'unknown' and polymorphic values when necessary.
     * Assumes that the states belong to the same block and context.
     *
     * @param modified if true, set modified flag on written values
     * @return true if the object changed (note there may be other changes due to recoveries)
     */
    private boolean propagateObj(ObjectLabel objlabel_to, State state_from, ObjectLabel objlabel_from, boolean modified) {
        Obj obj_from = state_from.getObject(objlabel_from, false);
        Obj obj_to = getObject(objlabel_to, false);
        if (obj_from == obj_to) {
            // identical objects, so nothing to do
            return false;
        }
        if (obj_from.isAllNone()) { // may be a call edge or function entry state where not all properties have been propagated, so don't use isSomeNone here
            // obj_from object is none, so nothing to do
            return false;
        }
        if (obj_to.isAllNone()) { // may be a call edge or function entry state where not all properties have been propagated, so don't use isSomeNone here
            // obj_to is none, so just copy from obj_from
            makeWritableStore();
            obj_to = new Obj(obj_from);
            store.put(objlabel_to, obj_to);
            return true;
        }
        // join all properties from obj_from into obj_to
        boolean changed = false;
        Value default_array_property_to = obj_to.getDefaultArrayProperty();
        Value default_array_property_from = obj_from.getDefaultArrayProperty();
        Value default_array_property_to_original = default_array_property_to;
        if (!default_array_property_to.isUnknown() || !default_array_property_from.isUnknown()) {
            if (default_array_property_to.isUnknown())
                default_array_property_to = UnknownValueResolver.getDefaultArrayProperty(objlabel_to, this);
            if (default_array_property_from.isUnknown())
                default_array_property_from = UnknownValueResolver.getDefaultArrayProperty(objlabel_from, state_from);
            default_array_property_to = default_array_property_to.join(default_array_property_from);
            if (modified)
                default_array_property_to = default_array_property_to.joinModified();
            if (default_array_property_to != default_array_property_to_original) {
                if (!obj_to.isWritable())
                    obj_to = getObject(objlabel_to, true);
                obj_to.setDefaultArrayProperty(default_array_property_to);
                changed = true;
            }
        }
        Value default_nonarray_property_to = obj_to.getDefaultNonArrayProperty();
        Value default_nonarray_property_from = obj_from.getDefaultNonArrayProperty();
        Value default_nonarray_property_to_original = default_nonarray_property_to;
        if (!default_nonarray_property_to.isUnknown() || !default_nonarray_property_from.isUnknown()) {
            if (default_nonarray_property_to.isUnknown())
                default_nonarray_property_to = UnknownValueResolver.getDefaultNonArrayProperty(objlabel_to, this);
            if (default_nonarray_property_from.isUnknown())
                default_nonarray_property_from = UnknownValueResolver.getDefaultNonArrayProperty(objlabel_from, state_from);
            default_nonarray_property_to = default_nonarray_property_to.join(default_nonarray_property_from);
            if (modified)
                default_nonarray_property_to = default_nonarray_property_to.joinModified();
            if (default_nonarray_property_to != default_nonarray_property_to_original) {
                if (!obj_to.isWritable())
                    obj_to = getObject(objlabel_to, true);
                obj_to.setDefaultNonArrayProperty(default_nonarray_property_to);
                changed = true;
            }
        }
        obj_from = state_from.getObject(objlabel_from, false); // propagating defaults may have materialized properties, so get the latest version
        for (String propertyname : obj_from.getProperties().keySet()) {
            if (!obj_to.getProperties().containsKey(propertyname)) {
                Value v = Strings.isArrayIndex(propertyname) ? default_array_property_to_original : default_nonarray_property_to_original;
                if (!obj_to.isWritable())
                    obj_to = getObject(objlabel_to, true);
                obj_to.setProperty(propertyname, v); // materializing from default doesn't affect 'changed'
//                if (log.isDebugEnabled())
//                  log.debug("Materialized " + objlabel_to + "." + propertyname + " = " + v);
            }
        }
        for (String propertyname : newList(obj_to.getPropertyNames())) { // TODO: need newList (to avoid ConcurrentModificationException)?
            Value v_to = obj_to.getProperty(propertyname);
            Value v_from = obj_from.getProperty(propertyname);
            if (!v_to.isUnknown() || !v_from.isUnknown()) {
                Value v_to_original = v_to;
                if (v_to.isUnknown())
                    v_to = UnknownValueResolver.getProperty(objlabel_to, propertyname, this, v_from.isPolymorphic());
                if (v_from.isUnknown())
                    v_from = UnknownValueResolver.getProperty(objlabel_from, propertyname, state_from, v_to.isPolymorphic());
                v_to = UnknownValueResolver.join(v_to, this, v_from, state_from);
                if (modified)
                    v_to = v_to.joinModified();
                if (v_to != v_to_original) {
                    if (!obj_to.isWritable())
                        obj_to = getObject(objlabel_to, true);
                    obj_to.setProperty(propertyname, v_to);
                    changed = true;
                }
            }
        }
        Value internal_prototype_to = obj_to.getInternalPrototype();
        Value internal_prototype_from = obj_from.getInternalPrototype();
        if (!internal_prototype_to.isUnknown() || !internal_prototype_from.isUnknown()) {
            Value internal_prototype_to_original = internal_prototype_to;
            if (internal_prototype_to.isUnknown())
                internal_prototype_to = UnknownValueResolver.getInternalPrototype(objlabel_to, this, internal_prototype_from.isPolymorphic());
            if (internal_prototype_from.isUnknown())
                internal_prototype_from = UnknownValueResolver.getInternalPrototype(objlabel_from, state_from, internal_prototype_to.isPolymorphic());
            internal_prototype_to = UnknownValueResolver.join(internal_prototype_to, this, internal_prototype_from, state_from);
            if (modified)
                internal_prototype_to = internal_prototype_to.joinModified();
            if (internal_prototype_to != internal_prototype_to_original) {
                if (!obj_to.isWritable())
                    obj_to = getObject(objlabel_to, true);
                obj_to.setInternalPrototype(internal_prototype_to);
                changed = true;
            }
        }
        Value internal_value_to = obj_to.getInternalValue();
        Value internal_value_from = obj_from.getInternalValue();
        if (!internal_value_to.isUnknown() || !internal_value_from.isUnknown()) {
            Value internal_value_to_original = internal_value_to;
            if (internal_value_to.isUnknown())
                internal_value_to = UnknownValueResolver.getInternalValue(objlabel_to, this, internal_value_from.isPolymorphic());
            if (internal_value_from.isUnknown())
                internal_value_from = UnknownValueResolver.getInternalValue(objlabel_from, state_from, internal_value_to.isPolymorphic());
            internal_value_to = UnknownValueResolver.join(internal_value_to, this, internal_value_from, state_from);
            if (modified)
                internal_value_to = internal_value_to.joinModified();
            if (internal_value_to != internal_value_to_original) {
                if (!obj_to.isWritable())
                    obj_to = getObject(objlabel_to, true);
                obj_to.setInternalValue(internal_value_to);
                changed = true;
            }
        }
        if (!obj_to.isScopeChainUnknown() || !obj_from.isScopeChainUnknown()) {
            boolean scopechain_to_unknown = obj_to.isScopeChainUnknown();
            ScopeChain scope_chain_to = obj_to.isScopeChainUnknown() ? UnknownValueResolver.getScopeChain(objlabel_to, this) : obj_to.getScopeChain();
            ScopeChain scope_chain_from = obj_from.isScopeChainUnknown() ? UnknownValueResolver.getScopeChain(objlabel_from, state_from) : obj_from.getScopeChain();
            ScopeChain new_scope_chain = ScopeChain.add(scope_chain_to, scope_chain_from);
            if ((new_scope_chain != null && !new_scope_chain.equals(scope_chain_to)) || scopechain_to_unknown) {
                if (!obj_to.isWritable())
                    obj_to = getObject(objlabel_to, true);
                obj_to.setScopeChain(new_scope_chain);
                changed = true;
            }
        }
        return changed;
    }

    /**
     * 8.6.2.1 [[Get]]
     * Returns the value of the given property in the given objects.
     * The internal prototype chains are used.
     * Absent is converted to undefined. Attributes are set to bottom.
     */
    public Value readPropertyValue(Collection<ObjectLabel> objlabels, String propertyname) {
        return readPropertyValue(objlabels, Value.makeTemporaryStr(propertyname));
    }

    /**
     * 8.6.2.1 [[Get]]
     * Returns the value of the given property in the given objects.
     * The internal prototype chains are used.
     * Absent is converted to undefined. Attributes are set to bottom.
     */
    public Value readPropertyValue(Collection<ObjectLabel> objlabels, Str propertystr) {
        Value v = readPropertyRaw(objlabels, propertystr, false);
        if (v.isMaybeAbsent())
            v = v.restrictToNotAbsent().joinUndef();
        v = v.setBottomPropertyData();
        if (log.isDebugEnabled())
            log.debug("readPropertyValue(" + objlabels + "," + propertystr + ") = " + v);
        return v;
    }

    /**
     * Returns the value of the given property in the given objects.
     * The internal prototype chains are used.
     */
    public Value readPropertyWithAttributes(Collection<ObjectLabel> objlabels, String propertyname) {
        return readPropertyWithAttributes(objlabels, Value.makeTemporaryStr(propertyname));
    }

    /**
     * Returns the value of the given property in the given objects.
     * The internal prototype chains are used.
     */
    public Value readPropertyWithAttributes(Collection<ObjectLabel> objlabels, Str propertystr) {
        Value v = readPropertyRaw(objlabels, propertystr, false);
        if (log.isDebugEnabled())
            log.debug("readPropertyWithAttributes(" + objlabels + "," + propertystr + ") = " + v);
        return v;
    }

    /**
     * Returns the value of the given property in the given objects.
     * The internal prototype chains are used.
     *
     * @param only_attributes if set, only attributes (incl. pseudo-attributes) are considered
     */
    private Value readPropertyRaw(Collection<ObjectLabel> objlabels, Str propertystr, boolean only_attributes) {
        Collection<Value> values = newList();
        Collection<ObjectLabel> ol = objlabels;
        Set<ObjectLabel> visited = newSet();
        while (!ol.isEmpty()) {
            Set<ObjectLabel> ol2 = newSet();
            for (ObjectLabel l : ol)
                if (!visited.contains(l)) {
                    visited.add(l);
                    Value v = readPropertyDirect(l, propertystr);
                    // String objects have their own [[GetOwnProperty]], see 15.5.5.2 in the ECMAScript 5 standard.
/*                    if (v.isMaybeAbsent() && l.getKind() == Kind.STRING && (propertystr.isMaybeStrOnlyUInt() || (propertystr.getStr() != null && Strings.isArrayIndex(propertystr.getStr())))) { // TODO: review (note that this is new in ES5)
                        Value tmp = null;
                        Value internal_value = UnknownValueResolver.getInternalValue(l, this, true);
                        Value lenV = UnknownValueResolver.getProperty(l, "length", this, true).restrictToNum();
                        String value = internal_value.getStr();
                        Double obj_len = lenV.getNum();
                        Integer prop_len = propertystr.getStr() == null ? -1 : Integer.valueOf(propertystr.getStr());
                        if (obj_len != null && value != null && prop_len > -1 && prop_len < obj_len)
                            tmp = Value.makeStr(value.substring(prop_len, prop_len + 1));
                        else if (obj_len != null && prop_len > -1 && prop_len >= obj_len)
                            tmp = Value.makeUndef();
                        if (tmp != null)
                            v = v.isNotPresent() ? tmp : v.join(tmp);
                }*/
                    Value v2 = v.restrictToNotAbsent();
                    if (!v2.isNone()) {
                        if (only_attributes)
                            v2 = v2.restrictToAttributes();
                        values.add(v2);
                    }
                    if (v.isMaybeAbsent() || v.isNotPresent()) {
                        Value proto = UnknownValueResolver.getInternalPrototype(l, this, false);
                        ol2.addAll(proto.getObjectLabels());
                        if (proto.isMaybeAbsent() || proto.isMaybeNull()) {
                            values.add(Value.makeAbsent());
                        }
                    }
                }
            ol = ol2;
        }
        return UnknownValueResolver.join(values, this);
    }

    /**
     * Collection of property names.
     */
    public static class Properties {

        /**
         * Property names that are maybe (including definitely) included.
         */
        private Collection<String> maybe;

        /**
         * Property names that are definitely included.
         */
        private Collection<String> definitely;

        /**
         * If true, all array properties are maybe included.
         */
        private boolean array;

        /**
         * If true, all non-array properties are maybe included.
         */
        private boolean nonarray;

        public Properties() {
            maybe = newSet();
            definitely = newSet();
            array = false;
            nonarray = false;
        }

        /**
         * Returns the property names that are maybe (including definitely) included.
         */
        public Collection<String> getMaybe() {
            return maybe;
        }

        /**
         * Returns the property names that are definitely included.
         */
        public Collection<String> getDefinitely() {
            return definitely;
        }

        /**
         * Returns true if all array properties are maybe included.
         */
        public boolean isArray() {
            return array;
        }

        /**
         * Returns true if all non-array properties are maybe included.
         */
        public boolean isNonArray() {
            return nonarray;
        }

        @Override
        public int hashCode() {
            return maybe.hashCode() * 7 + definitely.hashCode() * 31 + (array ? 3 : 17) + (nonarray ? 5 : 113);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Properties other = (Properties) obj;
            if (array != other.array)
                return false;
            if (nonarray != other.nonarray)
                return false;
            return definitely.equals(other.definitely) && maybe.equals(other.maybe);
        }

        /**
         * Returns true if the exact set of property names is known and finite.
         */
        public boolean isDefinite() {
            return !array && !nonarray && maybe.equals(definitely);
        }

        /**
         * Returns a value that represents the least upper bound of the property names.
         */
        public Value toValue() {
            Collection<Value> vs = new ArrayList<>();
            if (array)
                vs.add(Value.makeAnyStrUInt());
            if (nonarray)
                vs.add(Value.makeAnyStrNotUInt());
            for (String k : maybe) // maybe includes definitely
                vs.add(Value.makeStr(k));
            return Value.join(vs);
        }

        /**
         * Returns a collection of values that represents the property names.
         */
        public Collection<Value> toValues() {
            Collection<Value> vs = maybe.stream().map(Value::makeStr).collect(Collectors.toList());
            if (array) {
                vs.add(Value.makeAnyStrUInt());
                vs.removeIf(prop -> prop.isMaybeSingleStr() && Strings.isArrayIndex(prop.getStr()));
            }
            if (nonarray) {
                vs.add(Value.makeAnyStrNotUInt());
                vs.removeIf(prop -> prop.isMaybeSingleStr() && !Strings.isArrayIndex(prop.getStr()));
            }
            return vs;
        }
    }

    /**
     * Returns a description of the names of the enumerable properties of the given objects.
     */
    public Properties getEnumProperties(Collection<ObjectLabel> objlabels) {
        Map<ObjectLabel, Set<ObjectLabel>> inverse_proto = newMap();
        // find relevant objects, prepare inverse_proto
        LinkedList<ObjectLabel> worklist = new LinkedList<>(objlabels);
        Set<ObjectLabel> visited = newSet(objlabels);
        Set<ObjectLabel> roots = newSet();
        while (!worklist.isEmpty()) {
            ObjectLabel ol = worklist.removeFirst();
            if (!inverse_proto.containsKey(ol))
                inverse_proto.put(ol, dk.brics.tajs.util.Collections.<ObjectLabel>newSet());
            Value proto = UnknownValueResolver.getInternalPrototype(ol, this, false);
            if (proto.isMaybeNull())
                roots.add(ol);
            for (ObjectLabel p : proto.getObjectLabels()) {
                addToMapSet(inverse_proto, p, ol);
                if (!visited.contains(p)) {
                    worklist.add(p);
                    visited.add(p);
                }
            }
        }
        // find properties info with fixpoint computation starting from the roots
        Map<ObjectLabel, Properties> props = newMap();
        Set<ObjectLabel> workset = newSet(roots);
        while (!workset.isEmpty()) {
            ObjectLabel ol = workset.iterator().next();
            workset.remove(ol);
            // inherit from prototypes
            Value proto = UnknownValueResolver.getInternalPrototype(ol, this, false);
            Properties p = mergeEnumProperties(proto.getObjectLabels(), props);
            // overwrite with properties in the current object
            if (UnknownValueResolver.getDefaultArrayProperty(ol, this).isMaybeNotDontEnum())
                p.array = true;
            if (UnknownValueResolver.getDefaultNonArrayProperty(ol, this).isMaybeNotDontEnum())
                p.nonarray = true;
            for (Map.Entry<String, Value> me : UnknownValueResolver.getProperties(ol, this).entrySet()) {
                String propertyname = me.getKey();
                Value v = UnknownValueResolver.getProperty(ol, propertyname, this, true);
                if (v.isMaybeNotDontEnum()) {
                    p.maybe.add(propertyname);
                    if (!v.isMaybeDontEnum())
                        p.definitely.add(propertyname);
                }
            }
            Properties oldp = props.get(ol);
            if (oldp == null || !oldp.equals(p)) {
                props.put(ol, p);
                workset.addAll(inverse_proto.get(ol));
            }
        }
        return mergeEnumProperties(objlabels, props);
    }

    private static Properties mergeEnumProperties(Collection<ObjectLabel> objlabels, Map<ObjectLabel, Properties> props) {
        Properties res = new Properties();
        boolean first = true;
        for (ObjectLabel objlabel : objlabels) {
            Properties p = props.get(objlabel);
            if (p != null) {
                if (first) {
                    res.maybe.addAll(p.maybe);
                    res.definitely.addAll(p.definitely);
                    res.array = p.array;
                    res.nonarray = p.nonarray;
                    first = false;
                } else {
                    res.maybe.addAll(p.maybe);
                    res.definitely.retainAll(p.definitely);
                    res.array |= p.array;
                    res.nonarray |= p.nonarray;
                }
            }
        }
        return res;
    }

    /**
     * [[HasProperty]].
     * The internal prototype chains are used.
     */
    private Bool hasPropertyRaw(Collection<ObjectLabel> objlabels, String propertyname) {
        Value v = readPropertyRaw(objlabels, Value.makeTemporaryStr(propertyname), true);
        boolean maybe_present = v.isMaybePresent();
        boolean maybe_absent = v.isMaybeAbsent();
        return maybe_present ? (maybe_absent ? Value.makeAnyBool() : Value.makeBool(true)) : (maybe_absent ? Value.makeBool(false) : Value.makeNone());
    }

    /**
     * Returns the set of objects in the prototype chain that contain the property.
     */
    public Set<ObjectLabel> getPrototypeWithProperty(ObjectLabel objlabel, String propertyname) { // TODO: review (used only in Monitoring)
        Set<ObjectLabel> ol = Collections.singleton(objlabel);
        Set<ObjectLabel> visited = newSet();
        Set<ObjectLabel> res = newSet();
        while (!ol.isEmpty()) {
            Set<ObjectLabel> ol2 = newSet();
            for (ObjectLabel l : ol)
                if (!visited.contains(l)) {
                    visited.add(l);
                    Value v = UnknownValueResolver.getProperty(l, propertyname, this, true);
                    /*if (v.isMaybeAbsent()) {
                        Value proto = UnknownValueResolver.getInternalPrototype(l, this, false);
                        ol2.addAll(proto.getObjectLabels());
                    } else {
                        res.add(l);
                    }*/
                    if (v.isNotPresent()) { //defintely absent
                        Value proto = UnknownValueResolver.getInternalPrototype(l, this, false);
                        ol2.addAll(proto.getObjectLabels());
                    } else if (v.isMaybeAbsent()) {
                        Value proto = UnknownValueResolver.getInternalPrototype(l, this, false);
                        ol2.addAll(proto.getObjectLabels());
                        res.add(l);
                    } else {
                        res.add(l);
                    }
                }
            ol = ol2;
        }
        return res;
    }

    public Set<ObjectLabel> getPrototypesUsedForUnknown(ObjectLabel objlabel) { // TODO: review (used only in Monitoring)
        Set<ObjectLabel> ol = Collections.singleton(objlabel);
        Set<ObjectLabel> visited = newSet();
        Set<ObjectLabel> res = newSet();
        while (!ol.isEmpty()) {
            Set<ObjectLabel> ol2 = newSet();
            for (ObjectLabel l : ol)
                if (!visited.contains(l)) {
                    visited.add(l);
                    Value v = readPropertyDirect(l, Value.makeAnyStr());
                    if (v.isMaybeAbsent()) {
                        Value proto = UnknownValueResolver.getInternalPrototype(l, this, false);
                        ol2.addAll(proto.getObjectLabels());
                        res.add(l);
                    }
                }
            ol = ol2;
        }
        return res;
    }

    /**
     * Checks whether the given property is present in the given objects.
     * The internal prototype chains are used.
     */
    public Bool hasProperty(Collection<ObjectLabel> objlabels, String propertyname) {
        Bool b = hasPropertyRaw(objlabels, propertyname);
        if (log.isDebugEnabled())
            log.debug("hasProperty(" + objlabels + "," + propertyname + ") = " + b);
        return b;
    }

    /**
     * 8.6.2.3 [[CanPut]] Checks whether the given property can be assigned in the given object.
     * The internal prototype chains are used.
     *
     * @param propertystr description of the property names to consider (assumed not to be a single string)
     */
    private Bool canPut(ObjectLabel objlabel, Str propertystr) {
        /*
        ReadOnly properties:
        - the function name in scope object of a function expression
        - 'length' of built-in function objects and string objects
        - 'prototype' of Object, Function, Array, String, etc.
        - Number.MAX_VALUE and related constants in Number
        - 'source', 'global', etc. of regexp objects
        (note: CanPut considers the internal prototype chain...)
        */
        if (Options.get().isAlwaysCanPut())
            return Value.makeBool(true);
        Value v = readPropertyRaw(Collections.singleton(objlabel), propertystr, true); // TODO: possible to optimize modeling of [[CanPut]]? i.e. in some cases omit this call to readPropertyRaw?
        Bool b;
        if (v.isNotPresent() || v.isNotReadOnly())
            b = Value.makeBool(true);
        else if (v.isReadOnly() && !v.isMaybeAbsent())
            b = Value.makeBool(false);
        else
            b = Value.makeAnyBool();
        if (log.isDebugEnabled())
            log.debug("canPut(" + objlabel + ") = " + b);
        return b;
    }

    /**
     * Returns the value of the given property in the objects.
     * The internal prototype chains and scope chains are <em>not</em> used.
     */
    public Value readPropertyDirect(Collection<ObjectLabel> objlabels, String propertyname) {
        Collection<Value> values = newList();
        for (ObjectLabel obj : objlabels)
            values.add(UnknownValueResolver.getProperty(obj, propertyname, this, true));
        Value v = UnknownValueResolver.join(values, this);
        if (log.isDebugEnabled())
            log.debug("readPropertyDirect(" + objlabels + "," + propertyname + ") = " + v);
        return v;
    }

    /**
     * Returns the join of the values of the given properties of an object.
     * The internal prototype chains and scope chains are <em>not</em> used.
     */
    private Value readPropertyDirect(ObjectLabel objlabel, Str propertystr) {
        if (propertystr.isMaybeSingleStr())
            return UnknownValueResolver.getProperty(objlabel, propertystr.getStr(), this, true);
        Collection<Value> values = newList();
        if (propertystr.isMaybeStrSomeUInt())
            values.add(UnknownValueResolver.getDefaultArrayProperty(objlabel, this));
        if (propertystr.isMaybeStrSomeNonUInt())
            values.add(UnknownValueResolver.getDefaultNonArrayProperty(objlabel, this));
        // the calls to UnknownValueResolver above have materialized all relevant properties
        for (String propertyname : getObject(objlabel, false).getPropertyNames())
            if (propertystr.isMaybeStr(propertyname))
                values.add(UnknownValueResolver.getProperty(objlabel, propertyname, this, false));
        return UnknownValueResolver.join(values, this);
    }

    /**
     * Adds an object label, representing a new empty object, to the store.
     * Takes recency abstraction into account.
     * Updates sets of summarized objects.
     */
    public void newObject(ObjectLabel objlabel) { // FIXME: update id/name/tagname/classname
        if (basis_store != null && basis_store.containsKey(objlabel))
            throw new AnalysisException("Attempt to summarize object from basis store");
        makeWritableStore();
        Obj oldobj = getObject(objlabel, false);
        if (!Options.get().isRecencyDisabled()) {
            if (!objlabel.isSingleton())
                throw new AnalysisException("Expected singleton object label");
            if (!oldobj.isSomeNone()) {
                // join singleton object into its summary object
                ObjectLabel summarylabel = objlabel.makeSummary();
                propagateObj(summarylabel, this, objlabel, true);
                // update references
                Map<ScopeChain, ScopeChain> cache = new HashMap<>();
                for (ObjectLabel objlabel2 : newList(store.keySet())) {
                    if (getObject(objlabel2, false).containsObjectLabel(objlabel)) {
                        Obj obj = getObject(objlabel2, true);
                        obj.replaceObjectLabel(objlabel, summarylabel, cache);
                    }
                }
                makeWritableExecutionContext();
                execution_context.replaceObjectLabel(objlabel, summarylabel, cache);
                makeWritableRegisters();
                for (int i = 0; i < registers.size(); i++) {
                    Value v = registers.get(i);
                    if (v != null) {
                        registers.set(i, v.replaceObjectLabel(objlabel, summarylabel));
                    }
                }
                if (Options.get().isLazyDisabled())
                    if (stacked_objlabels.contains(objlabel)) {
                        makeWritableStackedObjects();
                        stacked_objlabels.remove(objlabel);
                        stacked_objlabels.add(summarylabel);
                    }
                if (getObject(summarylabel, false).isUnknown() && store_default.isUnknown())
                    store.remove(summarylabel);
            }
            // now the old object is gone
            summarized.addDefinitelySummarized(objlabel);
            makeWritableStore();
            store.put(objlabel, Obj.makeAbsentModified());
        } else {
            // join the empty object into oldobj (only relevant if recency abstraction is disabled)
            Obj obj = getObject(objlabel, true);
            Value old_array = UnknownValueResolver.getDefaultArrayProperty(objlabel, this);
            Value old_nonarray = UnknownValueResolver.getDefaultNonArrayProperty(objlabel, this);
            obj.setDefaultArrayProperty(old_array.joinAbsentModified());
            obj.setDefaultNonArrayProperty(old_nonarray.joinAbsentModified());
            for (Map.Entry<String, Value> me : newSet(UnknownValueResolver.getProperties(objlabel, this).entrySet())) {
                String propertyname = me.getKey();
                Value v = me.getValue();
                if (v.isUnknown())
                    v = UnknownValueResolver.getProperty(objlabel, propertyname, this, true);
                obj.setProperty(propertyname, v.joinAbsentModified());
            }
            obj.setInternalPrototype(UnknownValueResolver.getInternalPrototype(objlabel, this, true).joinAbsentModified());
            obj.setInternalValue(UnknownValueResolver.getInternalValue(objlabel, this, true).joinAbsentModified());
        }
        if (log.isDebugEnabled())
            log.debug("newObject(" + objlabel + ")");
    }

    /**
     * Summarizes the given objects.
     * Moves the given objects from singleton to summary, such that each represents
     * an unknown number of concrete objects.
     */
    public void summarize(Set<ObjectLabel> objs) {
        for (ObjectLabel objlabel : objs) {
            if (store.containsKey(objlabel)) {
                multiplyObject(objlabel);
            }
        }
    }

    /**
     * Moves the given object from singleton to summary, such that it represents
     * an unknown number of concrete objects.
     */
    public void multiplyObject(ObjectLabel objlabel) {
        if (!store.containsKey(objlabel))
            throw new AnalysisException("Object " + objlabel + " not found!?");
        makeWritableStore();
        if (objlabel.isSingleton()) {
            // move the object
            ObjectLabel summarylabel = objlabel.makeSummary();
            propagateObj(summarylabel, this, objlabel, true);
            store.remove(objlabel);
            // update references
            Map<ScopeChain, ScopeChain> cache = new HashMap<>();
            for (ObjectLabel objlabel2 : newList(store.keySet())) {
                if (getObject(objlabel2, false).containsObjectLabel(objlabel)) {
                    Obj obj = getObject(objlabel2, true);
                    obj.replaceObjectLabel(objlabel, summarylabel, cache);
                }
            }
            makeWritableExecutionContext();
            execution_context.replaceObjectLabel(objlabel, summarylabel, cache);
            makeWritableRegisters();
            for (int i = 0; i < registers.size(); i++) {
                Value v = registers.get(i);
                if (v != null) {
                    registers.set(i, v.replaceObjectLabel(objlabel, summarylabel));
                }
            }
            if (Options.get().isLazyDisabled())
                if (stacked_objlabels.contains(objlabel)) {
                    makeWritableStackedObjects();
                    stacked_objlabels.remove(objlabel);
                    stacked_objlabels.add(summarylabel);
                }
            if (log.isDebugEnabled())
                log.debug("multiplyObject(" + objlabel + ")");
        }
    }

    /**
     * 8.6.2.2 [[Put]]
     * Assigns the given value to the given property of the given objects.
     * Modified is set on all values being written (not considering the put flag).
     *
     * @param put        if set, attributes are cleared or copied from the old value according to 8.6.2.2
     * @param force_weak if set, force weak update disregarding objlabels
     */
    public void writeProperty(Collection<ObjectLabel> objlabels, Str propertystr, Value value, boolean put, boolean force_weak) {
        // 8.6.2.2 [[Put]]
        // 1. Call the [[CanPut]] method of O with name P.
        // 2. If Result(1) is false, return.
        // 3. If O doesn't have a property with name P, go to step 6.
        // 4. Set the value of the property to V. The attributes of the property are not changed.
        // 5. Return.
        // 6. Create a property with name P, set its value to V and give it empty attributes.
        value.assertNonEmpty();
        if (propertystr.isMaybeSingleStr()) {
            writePropertyRaw(objlabels, propertystr.getStr(), value, put, true, true, force_weak);
        } else {
            for (ObjectLabel objlabel : objlabels) {
                if (propertystr.isMaybeStrSomeUInt()) {
                    Value v = value;
                    if (!put || canPut(objlabel, Value.makeAnyStrUInt()).isMaybeTrue()) { // we can ignore isMaybeFalse since we only do weak update anyway
                        if (put)
                            v = v.removeAttributes(); // 8.6.2.2 item 6
                        Value oldval = UnknownValueResolver.getDefaultArrayProperty(objlabel, this);
                        if (!oldval.isNone()) {
                            Value newval = UnknownValueResolver.join(oldval, v, this);
                            getObject(objlabel, true).setDefaultArrayProperty(newval.joinModified());
                        } // otherwise, the abstract object represents zero concrete objects (due to restoring of unmodified properties), so ignore
                    }
                    // TODO: generate warning if [[CanPut]] gives false?
                }
                if (propertystr.isMaybeStrSomeNonUInt()) {
                    Value v = value;
                    if (!put || canPut(objlabel, Value.makeAnyStrNotUInt()).isMaybeTrue()) { // we can ignore isMaybeFalse since we only do weak update anyway
                        if (put)
                            v = v.removeAttributes(); // 8.6.2.2 item 6
                        Value oldval = UnknownValueResolver.getDefaultNonArrayProperty(objlabel, this);
                        if (!oldval.isNone()) {
                            Value newval = UnknownValueResolver.join(oldval, v, this);
                            getObject(objlabel, true).setDefaultNonArrayProperty(newval.joinModified());
                        } // otherwise, the abstract object represents zero concrete objects (due to restoring of unmodified properties), so ignore
                    }
                    // TODO: generate warning if [[CanPut]] gives false?
                }
                for (String propertyname : newSet(getObject(objlabel, false).getPropertyNames())) // calls to UnknownValueResolver above have materialized all relevant properties
                    if (propertystr.isMaybeStr(propertyname))
                        writePropertyIfCanPut(objlabel, propertyname, value, put, true, true, true);
            }
        }
        if (log.isDebugEnabled())
            log.debug("writeProperty(" + objlabels + "," + propertystr + "," + value + ")");
    }

    /**
     * Assigns the given value to the given property of the given object.
     * Attributes are taken from the given value.
     * Modified is set on all values being written.
     */
    public void writeProperty(ObjectLabel objlabel, String propertyname, Value value) {
        writePropertyWithAttributes(objlabel, propertyname, value.setAttributes(false, false, false));
    }

    /**
     * Assigns the given value to the given property of the given objects, with attributes.
     * Attributes are taken from the given value.
     *
     * @param real if set, the modified flag is set on written values
     */
    public void writePropertyWithAttributes(Collection<ObjectLabel> objlabels, String propertyname, Value value, boolean real) {
        writePropertyWithAttributes(objlabels, propertyname, value, real, false);
    }

    /**
     * Assigns the given value to the given property of the given objects, with attributes.
     * Attributes are taken from the given value.
     *
     * @param real if set, the modified flag is set on written values
     */
    public void writePropertyWithAttributes(Collection<ObjectLabel> objlabels, String propertyname, Value value, boolean real, boolean force_weak) {
        writePropertyRaw(objlabels, propertyname, value, false, real, true, force_weak);
        if (log.isDebugEnabled())
            log.debug("writePropertyWithAttributes(" + objlabels + "," + propertyname + "," + value + "," + value.printAttributes() + ")");
    }

    /**
     * Assigns the given value to the given property of the given objects, with attributes.
     * Attributes are taken from the given value.
     * Modified is set on all values being written.
     */
    public void writePropertyWithAttributes(Collection<ObjectLabel> objlabels, String propertyname, Value value) {
        writePropertyWithAttributes(objlabels, propertyname, value, true);
    }

    /**
     * Assigns the given value to the given property of the given object, with attributes.
     * Attributes are taken from the given value.
     * Modified is set on all values being written.
     */
    public void writePropertyWithAttributes(ObjectLabel objlabel, String propertyname, Value value) {
        writePropertyWithAttributes(Collections.singleton(objlabel), propertyname, value, true);
    }

    /**
     * Assigns the given value to the given property of the given objects, with attributes.
     *
     * @param put             if set, attributes are cleared or copied from the old value according to 8.6.2.2
     * @param real            if set, the modified flag is set on written values
     * @param allow_overwrite if set, allow overwriting of existing values (if not set, do nothing if the property already exists)
     * @param force_weak      if set, force weak update disregarding objlabels
     */
    private void writePropertyRaw(Collection<ObjectLabel> objlabels, String propertyname, Value value,
                                  boolean put, boolean real, boolean allow_overwrite, boolean force_weak) {
        for (ObjectLabel objlabel : objlabels)
            writePropertyIfCanPut(objlabel, propertyname, value, put, real, allow_overwrite, force_weak || objlabels.size() != 1);
    }

    /**
     * Assigns the given value to the given object property if [[CanPut]] is true.
     * Takes ReadOnly into account.
     *
     * @param put             if set, attributes are cleared or copied from the old value according to 8.6.2.2
     * @param real            if set, the modified flag is set on written values
     * @param allow_overwrite if set, allow overwriting of existing values (if not set, do nothing if the property already exists)
     * @param force_weak      if set, force weak update
     */
    private void writePropertyIfCanPut(ObjectLabel objlabel, String propertyname, Value value,
                                       boolean put, boolean real, boolean allow_overwrite, boolean force_weak) {
        // 8.6.2.2 [[Put]]
        // 1. Call the [[CanPut]] method of O with name P.
        // 2. If Result(1) is false, return.
        // 3. If O doesn't have a property with name P, go to step 6.
        // 4. Set the value of the property to V. The attributes of the property are not changed.
        // 5. Return.
        // 6. Create a property with name P, set its value to V and give it empty attributes.
        Bool canput = put ? canPut(objlabel, Value.makeTemporaryStr(propertyname)) : Value.makeBool(true);
        if (canput.isMaybeTrue())
            writeProperty(objlabel, propertyname, value, put, real, allow_overwrite, force_weak || canput.isMaybeFalse());
        // TODO: generate warning if [[CanPut]] gives false?
    }

    /**
     * Assigns the given value to the given property of an object.
     *
     * @param put             if set, attributes are cleared or copied from the old value according to 8.6.2.2
     * @param real            if set, the modified flag is set on written values
     * @param allow_overwrite if set, allow overwriting of existing values (if not set, do nothing if the property already exists)
     * @param force_weak      if set, force weak update
     */
    private void writeProperty(ObjectLabel objlabel, String propertyname, Value value,
                               boolean put, boolean real, boolean allow_overwrite, boolean force_weak) {
        Value oldvalue = UnknownValueResolver.getProperty(objlabel, propertyname, this, true);
        if (!allow_overwrite && oldvalue.isNotAbsent()) // not allowed to overwrite and definitely present already, so just return
            return; // TODO: warn that the operation has no effect? (double declaration of variable)
        if (put) {
            if (oldvalue.isNotPresent()) // definitely not present already
                value = value.removeAttributes(); // 8.6.2.2 item 6
            else if (oldvalue.isNotAbsent()) // definitely present already
                value = value.setAttributes(oldvalue); // 8.6.2.2 item 4
            else // maybe present already
                value = value.setAttributes(oldvalue).join(value.removeAttributes());
        }
        if (force_weak || !objlabel.isSingleton() || (!allow_overwrite && oldvalue.isMaybePresent())) // weak update
            value = UnknownValueResolver.join(value, oldvalue, this);
        if (real || oldvalue.isMaybeModified())
            value = value.joinModified();
        checkProperty(value);
        if (put && real && objlabel.isHostObject() && !execution_context.isEmpty()) { // FIXME: also call evaluateSetter on dynamic property writes with unknown property
            objlabel.getHostObject().evaluateSetter(objlabel, Value.makeTemporaryStr(propertyname), value, this);
        }
        if (value.equals(oldvalue))
            return; // don't request writable obj if the value doesn't change anyway
        if (objlabel.getKind() == Kind.ARRAY && propertyname.equals("length") && put)
            return; // (hopefully) already taken care of by NativeFunctions.updateArrayLength
        getObject(objlabel, true).setProperty(propertyname, value);
    }

    /**
     * Checks for the given value that attributes are non-bottom if the value is nonempty.
     */
    private static void checkProperty(Value v) {
        if (v.isMaybePresent() && (!v.hasDontDelete() || !v.hasDontEnum() || !v.hasReadOnly()))
            throw new AnalysisException("Missing attribute information at property value " + v);
    }

    /**
     * Deletes the given property.
     * Ignored if the property has attribute DontDelete.
     * Returns false if attempting to delete a property with attribute DontDelete, true otherwise.
     */
    public Value deleteProperty(Collection<ObjectLabel> objlabels, Str propertystr, boolean force_weak) {
        Value res = Value.makeNone();
        for (ObjectLabel objlabel : objlabels)
            if (!force_weak && objlabels.size() == 1 && objlabel.isSingleton() && propertystr.isMaybeSingleStr())
                res = strongDeleteProperty(objlabel, propertystr.getStr());
            else
                res = res.joinBool(weakDeleteProperty(objlabel, propertystr));
        if (log.isDebugEnabled())
            log.debug("deleteProperty(" + objlabels + "," + propertystr + ") = " + res);
        return res;
    }

    /**
     * Deletes the given property (strong update).
     */
    private Value strongDeleteProperty(ObjectLabel objlabel, String propertyname) {
        // 8.6.2.5 [[Delete]] (P)
        // When the [[Delete]] method of O is called with property name P, the following steps are taken:
        // 1. If O doesn't have a property with name P, return true.
        // 2. If the property has the DontDelete attribute, return false.
        // 3. Remove the property with name P from O.
        // 4. Return true.
        Value res;
        Value v = UnknownValueResolver.getProperty(objlabel, propertyname, this, true);
        if (!v.isMaybePresent()) // property is definitely absent already
            res = Value.makeBool(true);
        else if (!v.isMaybeAbsent() && v.isDontDelete()) // definitely present already, definitely can't delete
            res = Value.makeBool(false);
        else {
            Value newval;
            if (v.isNotDontDelete()) { // maybe present already, definitely can delete
                newval = Value.makeAbsentModified(); // FIXME: deleting magic properties? (also other places...)
                res = Value.makeBool(true);
            } else { // don't know, maybe delete
                newval = v.joinAbsentModified();
                res = Value.makeAnyBool();
            }
            Obj obj = getObject(objlabel, true);
            obj.setProperty(propertyname, newval);
        }
        return res;
    }

    /**
     * Deletes the given property (weak update).
     */
    private Value weakDeleteProperty(ObjectLabel objlabel, Str propertystr) {
        // 8.6.2.5 [[Delete]] (P)
        // When the [[Delete]] method of O is called with property name P, the following steps are taken:
        // 1. If O doesn't have a property with name P, return true.
        // 2. If the property has the DontDelete attribute, return false.
        // 3. Remove the property with name P from O.
        // 4. Return true.
        Value res = Value.makeNone();
        if (propertystr.isMaybeSingleStr())
            res = res.joinBool(weakDeleteProperty(PropertyReference.makeOrdinaryPropertyReference(objlabel, propertystr.getStr())));
        else {
            if (propertystr.isMaybeStrSomeUInt())
                res = res.joinBool(weakDeleteProperty(PropertyReference.makeDefaultArrayPropertyReference(objlabel)));
            if (propertystr.isMaybeStrSomeNonUInt())
                res = res.joinBool(weakDeleteProperty(PropertyReference.makeDefaultNonArrayPropertyReference(objlabel)));
            // the calls to UnknownValueResolver above have materialized all relevant properties
            for (String propertyname : newSet(getObject(objlabel, false).getPropertyNames()))
                if (propertystr.isMaybeStr(propertyname))
                    res = res.joinBool(weakDeleteProperty(PropertyReference.makeOrdinaryPropertyReference(objlabel, propertyname)));
        }
        return res;
    }

    private Value weakDeleteProperty(PropertyReference p) {
        Value res;
        Value v = readProperty(p, true);
        if (!v.isMaybePresent()) // property is definitely absent already
            res = Value.makeBool(true);
        else if (!v.isMaybeAbsent() && v.isDontDelete()) // definitely present already, definitely can't delete
            res = Value.makeBool(false);
        else {
            if (v.isNotDontDelete()) // maybe present already, definitely can delete (but still only do weak update)
                res = Value.makeBool(true);
            else // don't know, maybe delete
                res = Value.makeAnyBool();
            writeProperty(p, v.joinAbsentModified());
        }
        return res;
    }

    /**
     * Reads the designated property value.
     */
    public Value readProperty(PropertyReference p, boolean partial) {
        ObjectLabel objlabel = p.getObjectLabel();
        switch (p.getKind()) {
            case ORDINARY:
                return UnknownValueResolver.getProperty(objlabel, p.getPropertyName(), this, partial);
            case DEFAULT_ARRAY:
                return UnknownValueResolver.getDefaultArrayProperty(objlabel, this);
            case DEFAULT_NONARRAY:
                return UnknownValueResolver.getDefaultNonArrayProperty(objlabel, this);
            case INTERNAL_PROTOTYPE:
                return UnknownValueResolver.getInternalPrototype(objlabel, this, partial);
            case INTERNAL_VALUE:
                return UnknownValueResolver.getInternalValue(objlabel, this, partial);
            default:
                throw new AnalysisException("Unexpected property reference");
        }
    }

    private void writeProperty(PropertyReference p, Value v) {
        Obj obj = getObject(p.getObjectLabel(), true);
        switch (p.getKind()) {
            case ORDINARY:
                obj.setProperty(p.getPropertyName(), v);
                break;
            case DEFAULT_ARRAY:
                obj.setDefaultArrayProperty(v);
                break;
            case DEFAULT_NONARRAY:
                obj.setDefaultNonArrayProperty(v);
                break;
            case INTERNAL_PROTOTYPE:
                obj.setInternalPrototype(v);
                break;
            case INTERNAL_VALUE:
                obj.setInternalValue(v);
                break;
            default:
                throw new AnalysisException("Unexpected property reference");
        }
    }

    /**
     * Assigns the given value to the internal prototype links of the given objects.
     * Modified is set on all values being written.
     */
    public void writeInternalPrototype(Collection<ObjectLabel> objlabels, Value value) {
        value.assertNonEmpty();
        for (ObjectLabel objlabel : objlabels)
            if (objlabels.size() == 1 && objlabel.isSingleton()) // strong update
                getObject(objlabel, true).setInternalPrototype(value.joinModified());
            else { // weak update
                Value oldval = UnknownValueResolver.getInternalPrototype(objlabel, this, true);
                Value newval = UnknownValueResolver.join(oldval, value, this);
                getObject(objlabel, true).setInternalPrototype(newval.joinModified());
            }
        if (log.isDebugEnabled())
            log.debug("writeInternalPrototype(" + objlabels + "," + value + ")");
    }

    /**
     * Assigns the given value to the internal prototype link of the given object.
     * Modified is set on all values being written.
     */
    public void writeInternalPrototype(ObjectLabel objlabel, Value value) {
        writeInternalPrototype(Collections.singleton(objlabel), value);
    }

    /**
     * Assign the given value to the internal [[Value]] property of the given objects.
     * Modified is set on all values being written.
     */
    public void writeInternalValue(Collection<ObjectLabel> objlabels, Value value) {
        value.assertNonEmpty();
        for (ObjectLabel objlabel : objlabels)
            if (objlabels.size() == 1 && objlabel.isSingleton()) // strong update
                getObject(objlabel, true).setInternalValue(value.joinModified());
            else { // weak update
                Value oldval = UnknownValueResolver.getInternalValue(objlabel, this, true);
                Value newval = UnknownValueResolver.join(oldval, value, this);
                getObject(objlabel, true).setInternalValue(newval.joinModified());
            }
        if (log.isDebugEnabled())
            log.debug("writeInternalValue(" + objlabels + "," + value + ")");
    }

    /**
     * Assigns the given value to the internal [[Value]] property of the given object.
     * Modified is set on all values being written.
     */
    public void writeInternalValue(ObjectLabel objlabel, Value value) {
        writeInternalValue(Collections.singleton(objlabel), value);
    }

    /**
     * Returns the value of the internal value property of the given objects.
     */
    public Value readInternalValue(Collection<ObjectLabel> objlabels) {
        Collection<Value> values = newList();
        for (ObjectLabel obj : objlabels)
            values.add(UnknownValueResolver.getInternalValue(obj, this, true));
        Value v = UnknownValueResolver.join(values, this);
        if (log.isDebugEnabled())
            log.debug("readInternalValue(" + objlabels + ") = " + v);
        return v;
    }

    /**
     * Returns the value of the internal prototype of the given objects.
     */
    public Value readInternalPrototype(Collection<ObjectLabel> objlabels) {
        Collection<Value> values = newList();
        for (ObjectLabel obj : objlabels)
            values.add(UnknownValueResolver.getInternalPrototype(obj, this, true));
        Value v = UnknownValueResolver.join(values, this);
        if (log.isDebugEnabled())
            log.debug("readInternalPrototype(" + objlabels + ") = " + v);
        return v;
    }

    /**
     * Returns the value of the internal scope property of the given objects.
     *
     * @return unmodifiable set
     */
    public ScopeChain readObjectScope(ObjectLabel objlabel) {
        ScopeChain scope = UnknownValueResolver.getScopeChain(objlabel, this);
        if (log.isDebugEnabled())
            log.debug("readObjectScope(" + objlabel + ") = " + scope);
        return scope;
    }

    /**
     * Assigns a copy of the given scope chain to the internal scope property of the given object.
     */
    public void writeObjectScope(ObjectLabel objlabel, ScopeChain scope) {
        if (objlabel.getKind() == Kind.FUNCTION && !objlabel.isHostObject() && scope == null)
            throw new AnalysisException("Empty scope chain for function!?");
        getObject(objlabel, true).setScopeChain(scope);
        if (log.isDebugEnabled())
            log.debug("writeObjectScope(" + objlabel + "," + scope + ")");
    }

    /**
     * Returns the scope chain.
     *
     * @return new set
     */
    public ScopeChain getScopeChain() {
        ScopeChain scope = execution_context.getScopeChain();
        if (log.isDebugEnabled())
            log.debug("getScopeChain() = " + scope);
        return scope;
    }

    /**
     * Returns the execution context.
     */
    public ExecutionContext getExecutionContext() {
        return execution_context;
    }

    /**
     * Pushes a new item onto the scope chain.
     */
    public void pushScopeChain(Set<ObjectLabel> objlabels) {
        makeWritableExecutionContext();
        execution_context.pushScopeChain(objlabels);
    }

    /**
     * Pops the top item off the scope chain.
     */
    public void popScopeChain() {
        makeWritableExecutionContext();
        execution_context.popScopeChain();
    }

    /**
     * Clears the variable object pointer in the execution context.
     */
    public void clearVariableObject() {
        makeWritableExecutionContext();
        execution_context.setVariableObject(dk.brics.tajs.util.Collections.<ObjectLabel>newSet());
    }

    /**
     * Sets the execution context.
     */
    public void setExecutionContext(ExecutionContext e) {
        execution_context = e;
        writable_execution_context = true;
        if (log.isDebugEnabled())
            log.debug("setExecutionContext(" + e + ")");
    }

    /**
     * Returns a string description of the differences between this state and the given one.
     */
    @Override
    public String diff(State old) {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<ObjectLabel, Obj> me : sortedEntries(store)) {
            Obj xo = old.getObject(me.getKey(), false);
            if (!me.getValue().equals(xo)) {
                b.append("\n      changed object ").append(me.getKey()).append(" at ").append(me.getKey().getSourceLocation()).append(": ");
                me.getValue().diff(xo, b);
            }
        }
        Set<ObjectLabel> temp = newSet(execution_context.getVariableObject());
        temp.removeAll(old.execution_context.getVariableObject());
        if (!temp.isEmpty())
            b.append("\n      new varobj: ").append(temp);
        temp = newSet(execution_context.getThisObject());
        temp.removeAll(old.execution_context.getThisObject());
        if (!temp.isEmpty())
            b.append("\n      new this: ").append(temp);
        if (!ScopeChain.isEmpty(ScopeChain.remove(execution_context.getScopeChain(), old.execution_context.getScopeChain())))
            b.append("\n      new scope chain: ").append(ScopeChain.remove(execution_context.getScopeChain(), old.execution_context.getScopeChain()));
        temp = newSet(summarized.getMaybeSummarized());
        temp.removeAll(old.summarized.getMaybeSummarized());
        if (!temp.isEmpty())
            b.append("\n      new maybe-summarized: ").append(temp);
        temp = newSet(summarized.getDefinitelySummarized());
        temp.removeAll(old.summarized.getDefinitelySummarized());
        if (!temp.isEmpty())
            b.append("\n      new definitely-summarized: ").append(temp);
        temp = newSet(stacked_objlabels);
        temp.removeAll(old.stacked_objlabels);
        if (!temp.isEmpty())
            b.append("\n      new stacked object labels: ").append(temp);
        if (!registers.equals(old.registers))
            b.append("\n      registers changed");
        // TODO: implement diff for StateExtras?
        return b.toString();
    }

    /**
     * Returns a description of this abstract state.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Abstract state:");
        b.append("\n  Execution context: ").append(execution_context);
        b.append("\n  Summarized: ").append(summarized);
        b.append("\n  Store (excluding basis and default objects): ");
        for (Map.Entry<ObjectLabel, Obj> me : sortedEntries(store))
            b.append("\n    ").append(me.getKey()).append(" (").append(me.getKey().getSourceLocation()).append("): ").append(me.getValue()).append("");
        //b.append("\n  Default object: ").append(store_default);
        b.append("\n  Registers: ");
        for (int i = 0; i < registers.size(); i++)
            if (registers.get(i) != null)
                b.append("\n    v").append(i).append("=").append(registers.get(i));
        b.append(extras);
        if (Options.get().isLazyDisabled())
            b.append("\n  Objects used by outer scopes: ").append(stacked_objlabels);
        return b.toString();
    }

    /**
     * Prints the objects of the given value.
     */
    public String printObject(Value v) {
        StringBuilder b = new StringBuilder();
        for (ObjectLabel obj : new TreeSet<>(v.getObjectLabels())) {
            if (b.length() > 0)
                b.append(", ");
            b.append(getObject(obj, false)); // TODO: .append(" at ").append(obj.getSourceLocation());
        }
        return b.toString();
    }

    /**
     * As {@link #toString()} but excludes registers and non-modified objects and properties.
     */
    @Override
    public String toStringBrief() {
        StringBuilder b = new StringBuilder("Abstract state:");
        b.append("\n  Execution context: ").append(execution_context);
        b.append("\n  Summarized: ").append(summarized);
        b.append("\n  Store (excluding non-modified): ");
        printModifiedStore(b);
        //b.append("\n  Default object: ").append(store_default);
        return b.toString();
    }

    /**
     * Prints the modified parts of the store.
     */
    public String toStringModified() {
        StringBuilder b = new StringBuilder();
        printModifiedStore(b);
        return b.toString();
    }

    /**
     * Prints the modified parts of the store.
     */
    private void printModifiedStore(StringBuilder b) {
        for (Map.Entry<ObjectLabel, Obj> me : sortedEntries(store))
            b.append("\n  ").append(me.getKey()).append(" (").append(me.getKey().getSourceLocation()).append("):").append(me.getValue().printModified());
    }

    @Override
    public String toDot() {
        StringBuilder ns = new StringBuilder("\n\t/* Nodes */\n");
        StringBuilder es = new StringBuilder("\n\t/* Edges */\n");
        // nodes
        TreeSet<ObjectLabel> objs = new TreeSet<>();
        for (Map.Entry<ObjectLabel, Obj> e : sortedEntries(store)) {
            ObjectLabel label = e.getKey();
            Obj obj = e.getValue();
            objs.add(label);
            objs.addAll(obj.getAllObjectLabels());
        }
        objs.addAll(execution_context.getObjectLabels());
        for (ObjectLabel label : objs) {
            StringBuilder s = new StringBuilder();
            String boxName = label.toString().replaceAll("<", "\\\\<").replaceAll(">", "\\\\>");
            s.append("\t").append(node(label)).append("[label=\"").append(boxName);
            int index = 0;
            Obj obj = store.get(label);
            if (obj != null) {
                for (Map.Entry<String, Value> ee : obj.getProperties().entrySet()) {
                    s.append("|").append("<f").append(index++).append("> ").append(ee.getKey()).append("=").append(esc(ee.getValue().restrictToNotObject().toString()));
                }
                if (!obj.getDefaultArrayProperty().isUnknown()) {
                    s.append("|").append("<f").append(index++).append("> [[DefaultArray]]=").append(esc(obj.getDefaultArrayProperty().restrictToNotObject().toString()));
                }
                if (!obj.getDefaultNonArrayProperty().isUnknown()) {
                    s.append("|").append("<f").append(index++).append("> [[DefaultNonArray]]=").append(esc(obj.getDefaultNonArrayProperty().restrictToNotObject().toString()));
                }
                if (!obj.getInternalPrototype().isUnknown()) {
                    s.append("|").append("<f").append(index++).append("> [[Prototype]]=").append(esc(obj.getInternalPrototype().restrictToNotObject().toString()));
                }
                if (!obj.getInternalValue().isUnknown()) {
                    s.append("|").append("<f").append(index++).append("> [[Value]]=").append(esc(obj.getInternalValue().restrictToNotObject().toString()));
                }
                if (!obj.isScopeChainUnknown()) {
                    s.append("|").append("<f").append(index).append("> [[Scope]]=");
                }
            }
            s.append("\"];\n");
            ns.append(s);
        }
        es.append("\tthis[label=this,shape=none];\n");
        es.append("\tvar[label=var,shape=none];\n");
        es.append("\tscope[label=scope,shape=none];\n");
        // edges
        for (Map.Entry<ObjectLabel, Obj> e : sortedEntries(store)) {
            ObjectLabel sourceLabel = e.getKey();
            Obj obj = e.getValue();
            int index = 0;
            for (Map.Entry<String, Value> ee : obj.getProperties().entrySet()) {
                Value value = ee.getValue();
                String source = node(sourceLabel) + ":f" + index;
                for (ObjectLabel targetLabel : value.getObjectLabels()) {
                    String target = node(targetLabel);
                    es.append("\t").append(source).append(" -> ").append(target).append(";\n");
                }
                index++;
            }
            if (!obj.getDefaultArrayProperty().isUnknown()) {
                String source = node(sourceLabel) + ":f" + index;
                for (ObjectLabel targetLabel : obj.getDefaultArrayProperty().getObjectLabels()) {
                    String target = node(targetLabel);
                    es.append("\t").append(source).append(" -> ").append(target).append(";\n");
                }
                index++;
            }
            if (!obj.getDefaultNonArrayProperty().isUnknown()) {
                String source = node(sourceLabel) + ":f" + index;
                for (ObjectLabel targetLabel : obj.getDefaultArrayProperty().getObjectLabels()) {
                    String target = node(targetLabel);
                    es.append("\t").append(source).append(" -> ").append(target).append(";\n");
                }
                index++;
            }
            if (!obj.getInternalPrototype().isUnknown()) {
                String source = node(sourceLabel) + ":f" + index;
                for (ObjectLabel targetLabel : obj.getInternalPrototype().getObjectLabels()) {
                    String target = node(targetLabel);
                    es.append("\t").append(source).append(" -> ").append(target).append(";\n");
                }
                index++;
            }
            if (!obj.getInternalValue().isUnknown()) {
                String source = node(sourceLabel) + ":f" + index;
                for (ObjectLabel targetLabel : obj.getInternalValue().getObjectLabels()) {
                    String target = node(targetLabel);
                    es.append("\t").append(source).append(" -> ").append(target).append(";\n");
                }
                index++;
            }
            if (!obj.isScopeChainUnknown() && obj.getScopeChain() != null) {
                String source = node(sourceLabel) + ":f" + index;
                for (ObjectLabel objlabel : obj.getScopeChain().getObject()) {
                    String target = node(objlabel);
                    es.append("\t").append(source).append(" -> ").append(target).append(";\n");
                }
            }
        }
        for (ObjectLabel objlabel : execution_context.getThisObject())
            es.append("\tthis -> ").append(node(objlabel)).append(";\n");
        for (ObjectLabel objlabel : execution_context.getVariableObject())
            es.append("\tvar -> ").append(node(objlabel)).append(";\n");
        for (Set<ObjectLabel> sc : ScopeChain.iterable(execution_context.getScopeChain()))
            for (ObjectLabel objlabel : sc)
                es.append("\tscope -> ").append(node(objlabel)).append(";\n");
        return "digraph {\n" +
                "\tnode [shape=record];\n" +
                "\trankdir=\"LR\"\n" +
                ns + es + "}";
    }

    private static String node(ObjectLabel objlabel) {
        int h = objlabel.hashCode();
        if (h > 0)
            return "node" + h;
        else if (h != Integer.MIN_VALUE) // :-)
            return "node_" + -h;
        else
            return "node_";
    }

    private static String esc(String s) {
        return Strings.escape(s).replace("|", " \\| ");
    }

    /**
     * Reduces this state.
     *
     * @see #gc(Value)
     */
    public void reduce(Value extra) {
        gc(extra);
    }

    /**
     * Runs garbage collection on the contents of this state.
     * Ignored if {@link OptionValues#isGCDisabled()} or {@link OptionValues#isRecencyDisabled()} is set.
     */
    public void gc(Value extra) {
        if (Options.get().isGCDisabled() || Options.get().isRecencyDisabled())
            return;
        if (Options.get().isIntermediateStatesEnabled())
            if (log.isDebugEnabled())
                log.debug("gc(): Before: " + this);
        Set<ObjectLabel> dead = newSet(store.keySet());
        State entry_state = c.getAnalysisLatticeElement().getState(BlockAndContext.makeEntry(block, context));
        dead.removeAll(findLiveObjectLabels(extra, entry_state));
        if (log.isDebugEnabled()) {
            log.debug("gc(): Unreachable objects: " + dead);
        }
        makeWritableStore();
        for (ObjectLabel objlabel : dead) {
            if (noneAtEntry(objlabel, entry_state))
                store.remove(objlabel);
            else
                store.put(objlabel, Obj.makeNoneModified());
        }
        // don't remove from summarized (it may contain dead object labels)
        if (Options.get().isIntermediateStatesEnabled())
            if (log.isDebugEnabled())
                log.debug("gc(): After: " + this);
    }

    /**
     * Returns true if the given object label is definitely the none object at the given function entry state.
     */
    private static boolean noneAtEntry(ObjectLabel objlabel, State entry_state) {
        return entry_state.getObject(objlabel, false).getDefaultArrayProperty().isNone();
    }

    /**
     * Finds live object labels (i.e. those reachable from the execution context, registers, or stacked object labels).
     * Note that the summarized sets may contain dead object labels.
     *
     * @param extra       extra value that should be treated as root, ignored if null
     * @param entry_state at function entry
     */
    private Set<ObjectLabel> findLiveObjectLabels(Value extra, State entry_state) {
        Set<ObjectLabel> live = execution_context.getObjectLabels();
        if (extra != null)
            live.addAll(extra.getObjectLabels());
        for (Value v : registers)
            if (v != null)
                live.addAll(v.getObjectLabels());
        live.addAll(stacked_objlabels);
        extras.getAllObjectLabels(live);
        if (!Options.get().isLazyDisabled())
            for (ObjectLabel objlabel : store.keySet()) {
                // some object represented by objlabel may originate from the caller (so it must be treated as live),
                // unless it is a singleton object marked as definitely summarized or it is 'none' at function entry
                if (!((objlabel.isSingleton() && summarized.isDefinitelySummarized(objlabel)) ||
                        noneAtEntry(objlabel, entry_state)))
                    live.add(objlabel);
            }
        LinkedHashSet<ObjectLabel> pending = new LinkedHashSet<>(live);
        while (!pending.isEmpty()) {
            Iterator<ObjectLabel> it = pending.iterator();
            ObjectLabel objlabel = it.next();
            it.remove();
            live.add(objlabel);
            for (ObjectLabel obj2 : getAllObjectLabels(objlabel))
                if (!live.contains(obj2))
                    pending.add(obj2);
        }
        return live;
    }

    /**
     * Returns the set of all object labels used in the given abstract object.
     * Does not resolve unknown values.
     */
    private Set<ObjectLabel> getAllObjectLabels(ObjectLabel objlabel) {
        Set<ObjectLabel> objlabels = newSet();
        Obj fo = getObject(objlabel, false);
        for (Value v : fo.getProperties().values())
            objlabels.addAll(v.getObjectLabels());
        objlabels.addAll(fo.getDefaultArrayProperty().getObjectLabels());
        objlabels.addAll(fo.getDefaultNonArrayProperty().getObjectLabels());
        objlabels.addAll(fo.getInternalPrototype().getObjectLabels());
        objlabels.addAll(fo.getInternalValue().getObjectLabels());
        if (!fo.isScopeChainUnknown())
            for (Set<ObjectLabel> ls : ScopeChain.iterable(fo.getScopeChain()))
                objlabels.addAll(ls);
        return objlabels;
    }

    /**
     * Models [[HasInstance]] (for instanceof).
     *
     * @param prototype external prototype of the second argument to instanceof
     * @param v         first argument to instanceof
     */
    public Value hasInstance(Collection<ObjectLabel> prototype, Value v) {
        boolean maybe_true = false;
        boolean maybe_false = false;
        if (v.isMaybePrimitive())
            maybe_false = true;
        List<ObjectLabel> pending = newList(v.getObjectLabels());
        Set<ObjectLabel> visited = newSet(v.getObjectLabels());
        while (!pending.isEmpty()) {
            ObjectLabel obj = pending.remove(pending.size() - 1);
            Value proto = UnknownValueResolver.getInternalPrototype(obj, this, false);
            if (proto.isMaybeNull())
                maybe_false = true;
            for (ObjectLabel p : proto.getObjectLabels()) {
                if (prototype.contains(p))
                    maybe_true = true;
                else if (!visited.contains(p)) {
                    pending.add(p);
                    visited.add(p);
                }
            }
            if (maybe_true && maybe_false)
                return Value.makeAnyBool();
        }
        return maybe_true ? (maybe_false ? Value.makeAnyBool() : Value.makeBool(true))
                : (maybe_false ? Value.makeBool(false) : Value.makeNone());
    }

    /**
     * Assigns the given value to the given register (strong update).
     * All attribute information is cleared. 'unknown' values are not permitted.
     */
    public void writeRegister(int reg, Value value) {
        value.assertNonEmpty();
        value = value.setBottomPropertyData();
        if (value.isUnknown())
            throw new AnalysisException("Unexpected 'unknown'");
        makeWritableRegisters();
        while (reg >= registers.size())
            registers.add(null);
        registers.set(reg, value);
        if (log.isDebugEnabled())
            log.debug("writeRegister(v" + reg + "," + value + ")");
    }

    /**
     * Removes the given register (strong update).
     */
    public void removeRegister(int reg) {
        makeWritableRegisters();
        while (reg >= registers.size())
            registers.add(null);
        registers.set(reg, null);
        if (log.isDebugEnabled())
            log.debug("removeRegister(v" + reg + ")");
    }

    /**
     * Returns true if the given register is defined.
     */
    public boolean isRegisterDefined(int reg) {
        return reg >= 0 && reg < registers.size() && registers.get(reg) != null;
    }

    /**
     * Reads the value of the given register.
     */
    public Value readRegister(int reg) {
        Value res;
        if (reg >= registers.size())
            res = null;
        else
            res = registers.get(reg);
        if (res == null)
            throw new AnalysisException("Reading undefined register v" + reg);
        if (log.isDebugEnabled())
            log.debug("readRegister(v" + reg + ") = " + res);
        return res;
    }

    /**
     * Returns the list of registers.
     */
    public List<Value> getRegisters() {
        return registers;
    }

    /**
     * Sets the list of registers.
     */
    public void setRegisters(List<Value> registers) {
        this.registers = registers;
    }

    /**
     * Adds object labels used in current registers and execution
     * context to stacked object labels.
     */
    public void stackObjectLabels() {
        if (!Options.get().isLazyDisabled())
            return;
        makeWritableStackedObjects();
        for (Value v : registers)
            if (v != null)
                stacked_objlabels.addAll(v.getObjectLabels());
        stacked_objlabels.addAll(execution_context.getObjectLabels());
    }

    /**
     * Clears all registers.
     */
    public void clearRegisters() {
        if (writable_registers)
            registers.clear();
        else {
            registers = Collections.emptyList();
            writable_registers = false;
        }
    }

    /**
     * Clears the registers, starting from {@link AbstractNode#FIRST_ORDINARY_REG}, and excluding property list values.
     */
    public void clearOrdinaryRegisters() {
        List<Value> new_registers = newList();
        int reg = 0;
        for (Value v : registers) {
            if (reg++ >= AbstractNode.FIRST_ORDINARY_REG && v != null && !v.isExtendedScope())
                v = null;
            new_registers.add(v);
        }
        registers = new_registers;
        writable_registers = true;
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
     * Assigns the given value to the given variable.
     *
     * @param varname the variable name
     * @param value   the new value
     * @param real    if set, the modified flag is set on written values
     * @return the set of objects where the variable may be stored (i.e. the base objects)
     */
    public Set<ObjectLabel> writeVariable(String varname, Value value, boolean real) {
        value.assertNonEmpty();
        // 10.1.4 Identifier Resolution
        // 1. Get the next object in the scope chain. If there isn't one, go to step 5.
        // 2. Call the [[HasProperty]] method of Result(1), passing the Identifier as the property.
        // 3. If Result(2) is true, return a value of type Reference whose base object is Result(1) and whose property name is the Identifier.
        // 4. Go to step 1.
        // 5. Return a value of type Reference whose base object is null and whose property name is the Identifier. 
        Set<ObjectLabel> objlabels = newSet();
        boolean definitely_found;
        for (Iterator<Set<ObjectLabel>> it = ScopeChain.iterable(execution_context.getScopeChain()).iterator(); it.hasNext(); ) {
            Set<ObjectLabel> sc = it.next();
            definitely_found = true;
            for (ObjectLabel objlabel : sc) {
                Bool h = hasPropertyRaw(Collections.singleton(objlabel), varname);
                if (h.isMaybeTrue() || !it.hasNext())
                    objlabels.add(objlabel);
                if (h.isMaybeFalse())
                    definitely_found = false;
            }
            if (definitely_found)
                break;
        }
        // 8.6.2.2 [[Put]]
        // 1. Call the [[CanPut]] method of O with name P.
        // 2. If Result(1) is false, return.
        // 3. If O doesn't have a property with name P, go to step 6.
        // 4. Set the value of the property to V. The attributes of the property are not changed.
        // 5. Return.
        // 6. Create a property with name P, set its value to V and give it empty attributes.
        for (ObjectLabel objlabel : objlabels)
            writePropertyIfCanPut(objlabel, varname, value, true, real, true, objlabels.size() != 1);
        if (log.isDebugEnabled())
            log.debug("writeVariable(" + varname + "," + value + ")");
        return objlabels;
    }

    /**
     * Declares the given variable (or function) and assigns the given value to it.
     */
    public void declareAndWriteVariable(String varname, Value value, boolean allow_overwrite) {
        // For function declarations (p.46): If the variable object already has a property with this name,
        // replace its value and attributes.
        // For variable declarations: If there is already a property of the variable object with
        // the name of a declared variable, the value of the property and its attributes are not changed.
        // For global code (10.2.1): Variable instantiation is performed using the global object as the variable
        // object and using property attributes { DontDelete }.
        // For eval code (10.2.2): Variable instantiation is performed using the calling context's variable
        // object and using empty property attributes.
        // For function code (10.2.3): Variable instantiation is performed using the activation object as the
        // variable object and using property attributes { DontDelete }.
        // TODO: variable instantiation (excluding the activation object arguments property) in eval code should use empty attributes, i.e. without DontDelete (10.2.2)
        value.assertNonEmpty();
        writePropertyRaw(execution_context.getVariableObject(), varname, value.restrictToNotAbsent().setAttributes(false, true, false), false, true, allow_overwrite, false);
        if (log.isDebugEnabled())
            log.debug("declareAndWriteVariable(" + varname + "," + value + ")");
    }

    /**
     * Returns the value of the given variable.
     *
     * @param varname   the variable name
     * @param base_objs collection where base objects are added (ignored if null)
     */
    public Value readVariable(String varname, Collection<ObjectLabel> base_objs) {
        Collection<Value> values = newList();
        boolean definitely_found = false;
        for (Set<ObjectLabel> sc : ScopeChain.iterable(execution_context.getScopeChain())) {
            definitely_found = true;
            for (ObjectLabel objlabel : sc) {
                Value v = readPropertyRaw(Collections.singleton(objlabel), Value.makeTemporaryStr(varname), false);
                if (v.isMaybePresent()) { // found one (maybe)
                    values.add(v.setBottomPropertyData());
                    if (base_objs != null)
                        base_objs.add(objlabel); // collecting the object from the scope chain (although the property may be in its prototype chain)
                }
                if (v.isMaybeAbsent())
                    definitely_found = false;
            }
            if (definitely_found)
                break;
        }
        if (!definitely_found)
            values.add(Value.makeAbsent()); // end of scope chain, so add absent
        Value res = UnknownValueResolver.join(values, this);
        if (log.isDebugEnabled())
            log.debug("readVariable(" + varname + ") = " + res + (base_objs != null ? " at " + base_objs : ""));
        return res;
    }

    /**
     * Deletes the given variable.
     *
     * @see #deleteProperty(Collection, Str, boolean)
     */
    public Value deleteVariable(String varname) {
        // 10.1.4 Identifier Resolution
        // 1. Get the next object in the scope chain. If there isn't one, go to step 5.
        // 2. Call the [[HasProperty]] method of Result(1), passing the Identifier as the property.
        // 3. If Result(2) is true, return a value of type Reference whose base object is Result(1) and whose property name is the Identifier.
        // 4. Go to step 1.
        // 5. Return a value of type Reference whose base object is null and whose property name is the Identifier.
        Set<ObjectLabel> objlabels = newSet();
        boolean definitely_found = false;
        for (Set<ObjectLabel> sc : ScopeChain.iterable(execution_context.getScopeChain())) {
            definitely_found = true;
            for (ObjectLabel objlabel : sc) {
                Bool h = hasPropertyRaw(Collections.singleton(objlabel), varname);
                if (h.isMaybeTrue())
                    objlabels.add(objlabel);
                if (h.isMaybeFalse())
                    definitely_found = false;
            }
            if (definitely_found)
                break;
        }
        // 11.4.1 The delete Operator
        // 1. Evaluate UnaryExpression.
        // 2. If Type(Result(1)) is not Reference, return true.
        // 3. Call GetBase(Result(1)).
        // 4. Call GetPropertyName(Result(1)).
        // 5. Call the [[Delete]] method on Result(3), providing Result(4) as the property name to delete.
        // 6. Return Result(5).
        Value res = deleteProperty(objlabels, Value.makeTemporaryStr(varname), false);
        if (!definitely_found)
            res = res.joinBool(true);
        if (log.isDebugEnabled())
            log.debug("deleteVariable(" + varname + ") = " + res);
        return res;
    }

    /**
     * Returns the value of 'this'.
     */
    public Value readThis() {
        Value res = Value.makeObject(execution_context.getThisObject());
        if (log.isDebugEnabled())
            log.debug("readThis() = " + res);
        return res;
    }

    /**
     * Returns the object value of 'this'.
     */
    public Set<ObjectLabel> readThisObjects() {
        Set<ObjectLabel> this_objs = execution_context.getThisObject();
        if (log.isDebugEnabled())
            log.debug("readThisObjects() = " + this_objs);
        return this_objs;
    }

    /**
     * Introduces 'unknown' values in this state according to the given function entry state.
     * Also clears modified flags and summarized sets.
     */
    @Override
    public void localize(State s) {
        if (!Options.get().isLazyDisabled()) {
            if (s == null) {
                // set everything to unknown
                store = newMap();
                writable_store = true;
                store_default = Obj.makeUnknown();
            } else {
                // localize each object
                makeWritableStore();
                for (ObjectLabel objlabel : newList(store.keySet())) {
                    Obj obj = getObject(objlabel, true);
                    Obj other = s.getObject(objlabel, false);
                    obj.localize(other, objlabel, this);
                }
                // remove all-unknown objects
                Map<ObjectLabel, Obj> new_store = newMap();
                for (Map.Entry<ObjectLabel, Obj> xs : store.entrySet())
                    if (!xs.getValue().isUnknown())
                        new_store.put(xs.getKey(), xs.getValue());
                store = new_store;
                store_default = Obj.makeUnknown();
            }
        } else {
            clearModified();
        }
        summarized.clear();
    }

    /**
     * Clears effects and summarized sets (for function entry).
     */
    public void clearEffects() {
        clearModified();
        summarized.clear();
    }

    @Override
    public Context transform(CallEdge edge, Context edge_context,
                             Map<Context, State> callee_entry_states, BasicBlock callee) {
//        if (log.isDebugEnabled()) 
//            log.debug("transform from call " + edge.getState().getBasicBlock().getSourceLocation() + " to " + callee.getSourceLocation());
        return edge_context;
    }

    @Override
    public boolean transformInverse(CallEdge edge, BasicBlock callee, Context callee_context) {
        return false;
    }
}
