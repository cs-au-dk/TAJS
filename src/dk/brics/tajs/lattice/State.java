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

package dk.brics.tajs.lattice;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.PKey.StringPKey;
import dk.brics.tajs.options.OptionValues;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.solver.IState;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Strings;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

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

    private GenericSolver<State, Context, CallEdge, ? extends ILatticeMonitoring, ?>.SolverInterface c;

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
     * Maybe/definitely renamings objects since function entry. (Contains the singleton object labels.)
     */
    private Renamings renamings;

    /**
     * Register values.
     */
    private List<Value> registers; // register values never have attributes or modified flag

    private boolean writable_registers; // for copy-on-write

    /**
     * Object labels that appear on the stack.
     */
    private Set<ObjectLabel> stacked_objlabels; // not used if lazy propagation is enabled

    /**
     * Set of (function entry, context) in call stack.
     * Used for recursion detection.
     */
    private Set<BlockAndContext<Context>> stacked_funentries;

    private boolean writable_stacked; // for copy-on-write for stacked_objlabels and stacked_funentries

    private StateExtras extras;

    private MustReachingDefs must_reaching_defs;

    private MustEquals must_equals;

    private static int number_of_states_created;

    private static int number_of_makewritable_store;

    private static int number_of_makewritable_registers; // TODO: currently not used

    private PartitioningInfo partitioning;

    /**
     * Constructs a new none-state (representing the empty set of concrete states).
     */
    public State(GenericSolver<State, Context, CallEdge, ? extends ILatticeMonitoring, ?>.SolverInterface c, BasicBlock block) {
        this.c = c;
        this.block = block;
        renamings = new Renamings();
        extras = new StateExtras();
        must_reaching_defs = new MustReachingDefs();
        must_equals = new MustEquals();
        setToBottom();
        number_of_states_created++;
    }

    /**
     * Constructs a new state as a copy of the given state.
     */
    private State(State x) {
        c = x.c;
        block = x.block;
        context = x.context;
        setToState(x);
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
     * Sets this state to the same as the given one.
     */
    private void setToState(State x) {
        renamings = new Renamings(x.renamings);
        store_default = x.store_default = Canonicalizer.get().canonicalizeViaImmutableBox(x.store_default.freeze());
        extras = new StateExtras(x.extras);
        must_reaching_defs = new MustReachingDefs(x.must_reaching_defs);
        must_equals = new MustEquals(x.must_equals);
//        if (Options.get().isCopyOnWriteDisabled()) {
        store = newMap();
        for (Map.Entry<ObjectLabel, Obj> xs : x.store.entrySet()) {
            Obj obj =  Canonicalizer.get().canonicalizeViaImmutableBox(xs.getValue().freeze());
            writeToStore(xs.getKey(), obj);
            xs.setValue(obj); // write back canonicalized object
        }
        basis_store = x.basis_store;
        writable_store = true;
        execution_context = x.execution_context.clone();
        writable_execution_context = true;
        registers = newList(x.registers);
        writable_registers = true;
        stacked_objlabels = newSet(x.stacked_objlabels);
        stacked_funentries = newSet(x.stacked_funentries);
        writable_stacked = true;
        partitioning = new PartitioningInfo(x.partitioning);
//        } else {
//            store = x.store;
//            basis_store = x.basis_store;
//            execution_context = x.execution_context;
//            registers = x.registers;
//            stacked_objlabels = x.stacked_objlabels;
//            stacked_funentries = x.stacked_funentries;
//            x.writable_execution_context = writable_execution_context = false;
//            x.writable_store = writable_store = false;
//            x.writable_registers = writable_registers = false;
//            x.writable_stacked = writable_stacked = false;
//        }
    }

    /**
     * Returns the solver interface.
     */
    public GenericSolver<State, Context, CallEdge, ? extends ILatticeMonitoring, ?>.SolverInterface getSolverInterface() {
        return c;
    }

    /**
     * Returns the extra stuff.
     */
    public StateExtras getExtras() {
        return extras;
    }

    /**
     * Returns the reaching definitions information.
     */
    public MustReachingDefs getMustReachingDefs() {
        return must_reaching_defs;
    }

    /**
     * Returns the must-equals information.
     */
    public MustEquals getMustEquals() {
        return must_equals;
    }

    @Override
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
     * Returns the store (excluding the basis store).
     * Only for reading!
     */
    public Map<ObjectLabel, Obj> getStore() {
        return store;
    }

    /**
     * Sets an object in the store.
     */
    public void putObject(ObjectLabel objlabel, Obj obj) {
        makeWritableStore();
        writeToStore(objlabel, obj);
    }

    private void writeToStore(ObjectLabel objlabel, Obj obj) {
        store.put(objlabel, obj);
    }

    /**
     * Removes an object in the store.
     */
    public void removeObject(ObjectLabel objlabel) {
        makeWritableStore();
        store.remove(objlabel);
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
            writeToStore(objlabel, obj);
//            if (log.isDebugEnabled())
//                log.debug("making writable object from store: " + objlabel);
        }
        if (obj == null && basis_store != null) {
            // check the basis_store
            obj = basis_store.get(objlabel);
            if (obj != null && writable) {
                obj = new Obj(obj);
                writeToStore(objlabel, obj);
//                if (log.isDebugEnabled())
//                    log.debug("making writable object from basis store: " + objlabel);
            }
        }
        if (obj == null) {
            // take the default
            obj = store_default;
            if (writable) {
                obj = new Obj(obj);
                writeToStore(objlabel, obj);
//                if (log.isDebugEnabled())
//                    log.debug("making writable object from store default: " + objlabel + " at " + block.getSourceLocation());
            }
        }
        return obj;
    }

    /**
     * Returns the store default object.
     */
    public Obj getStoreDefault() {
        return store_default;
    }

    /**
     * Sets the store default object.
     */
    public void setStoreDefault(Obj obj) {
        store_default = obj;
    }

    /**
     * Removes objects that are equal to the default object.
     */
    public void removeObjectsEqualToDefault(boolean default_none_at_entry) {
        for (Iterator<Map.Entry<ObjectLabel, Obj>> it = store.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<ObjectLabel, Obj> me = it.next();
            if (me.getValue().equals(store_default)) {
                if (log.isDebugEnabled())
                    log.debug("removing object equal to the default: " + me.getKey());
                it.remove();
            } else if (default_none_at_entry && store_default.isUnknown() && me.getValue().isSomeNone()) {
                if (log.isDebugEnabled())
                    log.debug("removing none object: " + me.getKey());
                it.remove();
            }
        }
    }

    /**
     * Returns the renamings sets.
     */
    public Renamings getRenamings() {
        return renamings;
    }

    /**
     * Sets the current store contents as the basis store.
     * After this, objects in the basis store should never be renamings.
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
    public void makeWritableStore() {
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
     * Returns the object labels that appear on the stack.
     * (Not used if lazy propagation is enabled.)
     */
    public Set<ObjectLabel> getStackedObjects() {
        return stacked_objlabels;
    }

    /**
     * Returns the functions that appear on the stack.
     */
    public Set<BlockAndContext<Context>> getStackedFunctions() {
        return stacked_funentries;
    }

    /**
     * Sets the object labels and functions that appear on the stack.
     */
    public void setStacked(Set<ObjectLabel> so, Set<BlockAndContext<Context>> sf) {
        if (so != null)
            stacked_objlabels = so;
        stacked_funentries = sf;
        writable_stacked = true;
    }

    /**
     * Makes stacked objects and functions writable (for copy-on-write).
     */
    private void makeWritableStacked() {
        if (writable_stacked)
            return;
        if (Options.get().isLazyDisabled())
            stacked_objlabels = newSet(stacked_objlabels);
        stacked_funentries = newSet(stacked_funentries);
        writable_stacked = true;
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
        number_of_makewritable_registers = 0;
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
        Map<ObjectLabel, Obj> oldStore = store;
        store = newMap();
        for (Map.Entry<ObjectLabel, Obj> xs : oldStore.entrySet()) {
            Obj obj = xs.getValue();
            if (obj.isSomeModified()) {
                obj = new Obj(obj);
                obj.clearModified();
            }
            writeToStore(xs.getKey(), obj);
        }
        writable_store = true;
        number_of_makewritable_store++;
        log.debug("clearModified()");
    }

    @Override
    public void setToBottom() {
        basis_store = null;
        renamings.clear();
        extras.setToBottom();
        must_reaching_defs.setToBottom();
        must_equals.setToBottom();
//        if (Options.get().isCopyOnWriteDisabled()) {
        store = newMap();
        writable_store = true;
        registers = new ArrayList<>();
        writable_registers = true;
        stacked_objlabels = newSet();
        stacked_funentries = newSet();
        writable_stacked = true;
//        } else {
//            store = Collections.emptyMap();
//            writable_store = false;
//            registers = Collections.emptyList();
//            writable_registers = false;
//            stacked_objlabels = Collections.emptySet();
//            stacked_funentries = Collections.emptySet();
//            writable_stacked = false;
//        }
        execution_context = new ExecutionContext();
        writable_execution_context = true;
        store_default = Obj.makeNone();
        partitioning = new PartitioningInfo(newSet(), newSet());
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
    public boolean isBottom() {
        return execution_context.isEmpty();
    }

//    /**
//     * Merges the modified parts of other state into this one.
//     * When both this and other write to the same location, take the least upper bound.
//     * Also merges reads_other into reads.
//     *
//     * @return true if read/write conflict, i.e. writes of this overlap with reads_other or writes of other overlap with reads.
//     */
//    public boolean mergeForInSpecialization(State other,
//                                            State reads,
//                                            State reads_other,
//                                            boolean careAboutConflicts, boolean mergeWritesWeakly, boolean overrideWrites) { // (currently unused)
//        makeWritableStore();
//        if (basis_store != other.basis_store)
//            throw new AnalysisException("Not identical basis stores");
//        if (log.isDebugEnabled()) {
//            // assuming that store_default, execution_context, stacked_objlabels, and registers are identical in this and other
//            if (!store_default.equals(other.store_default) ||
//                    !execution_context.equals(other.execution_context) ||
//                    !stacked_objlabels.equals(other.stacked_objlabels) || // FIXME: also consider stacked_funentries
//                    !reads.store_default.equals(reads_other.store_default) ||
//                    !reads.execution_context.equals(reads_other.execution_context) ||
//                    !reads.stacked_objlabels.equals(reads_other.stacked_objlabels))
//                throw new AnalysisException("Not identical store_default / execution_context / stacked_objlabels");
//        }
//        boolean conflict = false;
//        if (careAboutConflicts && !(extras.equals(other.extras))) {
//            log.debug("mergeForInSpecialization: conflict at state extras");
//            conflict = true;
//        }
//        // report conflict if writes of this overlap with reads_other or writes of other overlap with reads
//        if (careAboutConflicts && (checkReadWriteConflict(this, reads_other) || checkReadWriteConflict(other, reads))) {
//            log.debug("mergeForInSpecialization: read/write conflict");
//            conflict = true;
//        }
//        // merge reads_other into reads (if both read, take least upper bound)
//        for (Map.Entry<ObjectLabel, Obj> me : reads_other.store.entrySet()) {
//            ObjectLabel objlabel = me.getKey();
//            Obj reads_other_obj = me.getValue();
//            Obj reads_obj = reads.getObject(objlabel, true);
//            for (Map.Entry<PKey, Value> me2 : reads_other_obj.getProperties().entrySet()) {
//                String propertyname = me2.getKey();
//                Value other_val = me2.getValue();
//                if (!other_val.isUnknown()) {
//                    Value reads_val = reads_obj.getProperty(propertyname);
//                    Value new_reads_val = reads_val.join(other_val);
//                    reads_obj.setProperty(propertyname, new_reads_val);
//                }
//            }
//            Value reads_other_defaultarray_val = reads_other_obj.getDefaultArrayProperty();
//            if (!reads_other_defaultarray_val.isUnknown()) {
//                reads_obj.setDefaultArrayProperty(reads_other_defaultarray_val);
//                // also merge with the explicit array properties in reads_obj that are not already handled
//                for (Map.Entry<PKey, Value> me2 : reads_obj.getProperties().entrySet()) {
//                    PKey propertyname = me2.getKey();
//                    if (propertyname.isArrayIndex() && !reads_other_obj.getProperties().containsKey(propertyname)) {
//                        Value reads_val = me2.getValue();
//                        Value new_reads_val = reads_val.join(reads_other_defaultarray_val);
//                        reads_obj.setProperty(propertyname, new_reads_val);
//                    }
//                }
//            }
//            Value reads_other_defaultnonarray_val = reads_other_obj.getDefaultNonArrayProperty();
//            if (!reads_other_defaultnonarray_val.isUnknown()) {
//                reads_obj.setDefaultNonArrayProperty(reads_other_defaultnonarray_val);
//                // also merge with the explicit array properties in reads_obj that are not already handled
//                for (Map.Entry<PKey, Value> me2 : reads_obj.getProperties().entrySet()) {
//                    PKey propertyname = me2.getKey();
//                    if (!propertyname.isArrayIndex() && !reads_other_obj.getProperties().containsKey(propertyname)) {
//                        Value reads_val = me2.getValue();
//                        Value new_reads_val = reads_val.join(reads_other_defaultnonarray_val);
//                        reads_obj.setProperty(propertyname, new_reads_val);
//                    }
//                }
//            }
//            Value reads_other_internalprototype_val = reads_other_obj.getInternalPrototype();
//            if (!reads_other_internalprototype_val.isUnknown()) {
//                Value reads_internalprototype_val = reads_obj.getInternalPrototype();
//                Value new_reads_internalprototype_val = reads_internalprototype_val.join(reads_other_internalprototype_val);
//                reads_obj.setInternalPrototype(new_reads_internalprototype_val);
//            }
//            Value reads_other_internalvalue_val = reads_other_obj.getInternalValue();
//            if (!reads_other_internalvalue_val.isUnknown()) {
//                Value reads_internalvalue_val = reads_obj.getInternalValue();
//                Value new_reads_internalvalue_val = reads_internalvalue_val.join(reads_other_internalvalue_val);
//                reads_obj.setInternalValue(new_reads_internalvalue_val);
//            }
//            if (!reads_other_obj.isScopeChainUnknown()) {
//                ScopeChain new_reads_scopechain;
//                if (!reads_obj.isScopeChainUnknown()) {
//                    new_reads_scopechain = ScopeChain.add(reads_obj.getScopeChain(), reads_other_obj.getScopeChain());
//                } else {
//                    new_reads_scopechain = reads_other_obj.getScopeChain();
//                }
//                reads_obj.setScopeChain(new_reads_scopechain);
//            }
//        }
//        // merge writes of other into this (if both write, take least upper bound)
//        for (Map.Entry<ObjectLabel, Obj> me : other.store.entrySet()) {
//            ObjectLabel objlabel = me.getKey();
//            Obj other_obj = me.getValue();
//            Obj this_obj = getObject(objlabel, true);
//            // merge properties of other_obj into this_obj
//            for (Map.Entry<PKey, Value> me2 : other_obj.getProperties().entrySet()) {
//                PKey propertyname = me2.getKey();
//                Value other_val = me2.getValue();
//                if (other_val.isMaybeModified()) {
//                    Value this_val = this_obj.getProperty(propertyname);
//                    Value new_this_val = mergeForInSpecializationValue(this_val, other_val, other, mergeWritesWeakly, overrideWrites);
//                    this_obj.setProperty(propertyname, new_this_val);
//                }
//            }
//            Value other_defaultarray_val = other_obj.getDefaultArrayProperty();
//            if (other_defaultarray_val.isMaybeModified()) {
//                Value this_defaultarray_val = this_obj.getDefaultArrayProperty();
//                Value new_this_defaultarray_val = mergeForInSpecializationValue(this_defaultarray_val, other_defaultarray_val, other, mergeWritesWeakly, overrideWrites);
//                this_obj.setDefaultArrayProperty(new_this_defaultarray_val);
//                // also merge with the explicit array properties in this_obj that are not already handled
//                for (Map.Entry<PKey, Value> me2 : this_obj.getProperties().entrySet()) {
//                    PKey propertyname = me2.getKey();
//                    if (propertyname.isArrayIndex() && !other_obj.getProperties().containsKey(propertyname)) {
//                        Value this_val = me2.getValue();
//                        Value new_this_val = mergeForInSpecializationValue(this_val, other_defaultarray_val, other, mergeWritesWeakly, overrideWrites);
//                        this_obj.setProperty(propertyname, new_this_val);
//                    }
//                }
//            }
//            Value other_defaultnonarray_val = other_obj.getDefaultNonArrayProperty();
//            if (other_defaultnonarray_val.isMaybeModified()) {
//                Value this_defaultnonarray_val = this_obj.getDefaultNonArrayProperty();
//                Value new_this_defaultnonarray_val = mergeForInSpecializationValue(this_defaultnonarray_val, other_defaultnonarray_val, other, mergeWritesWeakly, overrideWrites);
//                this_obj.setDefaultNonArrayProperty(new_this_defaultnonarray_val);
//                // also merge with the explicit array properties in this_obj that are not already handled
//                for (Map.Entry<PKey, Value> me2 : this_obj.getProperties().entrySet()) {
//                    PKey propertyname = me2.getKey();
//                    if (!propertyname.isArrayIndex() && !other_obj.getProperties().containsKey(propertyname)) {
//                        Value this_val = me2.getValue();
//                        Value new_this_val = mergeForInSpecializationValue(this_val, other_defaultarray_val, other, mergeWritesWeakly, overrideWrites);
//                        this_obj.setProperty(propertyname, new_this_val);
//                    }
//                }
//            }
//            Value other_internalprototype_val = other_obj.getInternalPrototype();
//            if (other_internalprototype_val.isMaybeModified()) {
//                Value this_internalprototype_val = this_obj.getInternalPrototype();
//                Value new_this_internalprototype_val = mergeForInSpecializationValue(this_internalprototype_val, other_internalprototype_val, other, mergeWritesWeakly, overrideWrites);
//                this_obj.setInternalPrototype(new_this_internalprototype_val);
//            }
//            Value other_internalvalue_val = other_obj.getInternalValue();
//            if (other_internalvalue_val.isMaybeModified()) {
//                Value this_internalvalue_val = this_obj.getInternalValue();
//                Value new_this_internalvalue_val = mergeForInSpecializationValue(this_internalvalue_val, other_internalvalue_val, other, mergeWritesWeakly, overrideWrites);
//                this_obj.setInternalValue(new_this_internalvalue_val);
//            }
//            if (!other_obj.isScopeChainUnknown()) {
//                ScopeChain new_this_scopechain;
//                if (!this_obj.isScopeChainUnknown()) {
//                    new_this_scopechain = ScopeChain.add(this_obj.getScopeChain(), other_obj.getScopeChain());
//                } else {
//                    new_this_scopechain = other_obj.getScopeChain();
//                }
//                this_obj.setScopeChain(new_this_scopechain);
//            }
//        }
//        renamings.getMaybeSummarized().addAll(other.renamings.getMaybeSummarized());
//        renamings.getDefinitelySummarized().addAll(other.renamings.getDefinitelySummarized());
//        return conflict;
//    }

//    /**
//     * Checks whether modified parts of the writes store overlap with the non-unknown parts of the reads store.
//     *
//     * @return true if conflict
//     */
//    private static boolean checkReadWriteConflict(State writes, State reads) { // (currently unused)
//        for (Map.Entry<ObjectLabel, Obj> me : writes.store.entrySet()) {
//            ObjectLabel objlabel = me.getKey();
//            Obj writes_obj = me.getValue();
//            Obj reads_obj = reads.getObject(objlabel, true);
//            for (Map.Entry<PKey, Value> me2 : writes_obj.getProperties().entrySet()) {
//                PKey propertyname = me2.getKey();
//                Value writes_val = me2.getValue();
//                if (writes_val.isMaybeModified()) {
//                    Value reads_val = reads_obj.getProperty(propertyname);
//                    if (checkReadWriteConflictValue(writes_val, reads_val)) {
//                        if (log.isDebugEnabled())
//                            log.debug("checkReadWriteConflict: writing " + writes_val + " reading " + reads_val + " from " + objlabel + "." + propertyname);
//                        return true;
//                    }
//                }
//            }
//            Value writes_defaultarray_val = writes_obj.getDefaultArrayProperty();
//            if (writes_defaultarray_val.isMaybeModified()) {
//                Value reads_defaultarray_val = reads_obj.getDefaultArrayProperty();
//                if (checkReadWriteConflictValue(writes_defaultarray_val, reads_defaultarray_val)) {
//                    if (log.isDebugEnabled())
//                        log.debug("checkReadWriteConflict: writing " + writes_defaultarray_val + " reading " + reads_defaultarray_val + " from " + objlabel + ".[[defaultarray]]");
//                    return true;
//                }
//                // also check the explicit array properties in reads_obj that are not already handled
//                for (Map.Entry<PKey, Value> me2 : reads_obj.getProperties().entrySet()) {
//                    PKey propertyname = me2.getKey();
//                    if (propertyname.isArrayIndex() && !writes_obj.getProperties().containsKey(propertyname)) {
//                        Value reads_val = me2.getValue();
//                        if (checkReadWriteConflictValue(writes_defaultarray_val, reads_val)) {
//                            if (log.isDebugEnabled())
//                                log.debug("checkReadWriteConflict: writing " + writes_defaultarray_val + " reading " + reads_val + " from " + objlabel + "." + propertyname);
//                            return true;
//                        }
//                    }
//                }
//            }
//            Value writes_defaultnonarray_val = writes_obj.getDefaultNonArrayProperty();
//            if (writes_defaultnonarray_val.isMaybeModified()) {
//                Value reads_defaultnonarray_val = reads_obj.getDefaultNonArrayProperty();
//                if (checkReadWriteConflictValue(writes_defaultnonarray_val, reads_defaultnonarray_val)) {
//                    if (log.isDebugEnabled())
//                        log.debug("checkReadWriteConflict: writing " + writes_defaultnonarray_val + " reading " + reads_defaultnonarray_val + " from " + objlabel + ".[[defaultnonarray]]");
//                    return true;
//                }
//                // also check the explicit nonarray properties in reads_obj that are not already handled
//                for (Map.Entry<PKey, Value> me2 : reads_obj.getProperties().entrySet()) {
//                    PKey propertyname = me2.getKey();
//                    if (!propertyname.isArrayIndex() && !writes_obj.getProperties().containsKey(propertyname)) {
//                        Value reads_val = me2.getValue();
//                        if (checkReadWriteConflictValue(writes_defaultnonarray_val, reads_val)) {
//                            if (log.isDebugEnabled())
//                                log.debug("checkReadWriteConflict: writing " + writes_defaultnonarray_val + " reading " + reads_val + " from " + objlabel + "." + propertyname);
//                            return true;
//                        }
//                    }
//                }
//            }
//            Value writes_internalprototype_val = writes_obj.getInternalPrototype();
//            if (writes_internalprototype_val.isMaybeModified()) {
//                Value reads_internalprototype_val = reads_obj.getInternalPrototype();
//                if (checkReadWriteConflictValue(writes_internalprototype_val, reads_internalprototype_val)) {
//                    if (log.isDebugEnabled())
//                        log.debug("checkReadWriteConflict: writing " + writes_internalprototype_val + " reading " + reads_internalprototype_val + " from " + objlabel + ".[[Prototype]]");
//                    return true;
//                }
//            }
//            Value writes_internalvalue_val = writes_obj.getInternalValue();
//            if (writes_internalvalue_val.isMaybeModified()) {
//                Value reads_internalvalue_val = reads_obj.getInternalValue();
//                if (checkReadWriteConflictValue(writes_internalvalue_val, reads_internalvalue_val)) {
//                    if (log.isDebugEnabled())
//                        log.debug("checkReadWriteConflict: writing " + writes_internalvalue_val + " reading " + reads_internalvalue_val + " from " + objlabel + ".[[Value]]");
//                    return true;
//                }
//            }
//            if (!writes_obj.isScopeChainUnknown()) {
//                if (!reads_obj.isScopeChainUnknown()) {
//                    if (log.isDebugEnabled())
//                        log.debug("checkReadWriteConflict: writing " + writes_obj.getScopeChain() + " reading " + reads_obj.getScopeChain() + " from " + objlabel + ".[[Scope]]");
//                    return checkReadWriteConflictScopeChain(writes_obj.getScopeChain(), reads_obj.getScopeChain());
//                }
//            }
//        }
//        return false;
//    }

//    /**
//     * Checks whether the written value may affect the read value.
//     *
//     * @return true if potential conflict, false if definitely no conflict
//     */
//    private static boolean checkReadWriteConflictValue(Value writes_val, Value reads_val) { // (currently unused)
//        if (reads_val.isUnknown()) { // if reads_val is 'unknown', the location has not been read
//            return false;
//        }
//        if (reads_val.isPolymorphic()) { // if reads_val is a polymorphic value, the location has been partially read (i.e. only its attributes)
//            return !writes_val.lessEqualAttributes(reads_val);
//        }
//        return !writes_val.lessEqual(reads_val);
//    }

//    /**
//     * Checks whether the written value may affect the read value.
//     *
//     * @return true if potential conflict, false if definitely no conflict
//     */
//    private static boolean checkReadWriteConflictScopeChain(ScopeChain writes_scope, ScopeChain reads_scope) { // (currently unused)
//        return !((writes_scope == null && reads_scope == null) || (writes_scope != null && writes_scope.equals(reads_scope)));
//    }

//    /**
//     * Merges this value with the other value.
//     * Assumes that the other value is maybe modified.
//     * This value is ignored if it is not maybe modified.
//     */
//    private Value mergeForInSpecializationValue(Value this_val, Value other_val, State other, boolean mergeWritesWeakly, boolean overrideWrites) { // (currently unused)
//        Value new_this_val;
//        if (!overrideWrites && (mergeWritesWeakly || this_val.isMaybeModified())) {
//            // maybe modified in this state and in other state, so join the two values
//            this_val = UnknownValueResolver.getRealValue(this_val, this);
//            other_val = UnknownValueResolver.getRealValue(other_val, other);
//            new_this_val = this_val.join(other_val);
//        } else {
//            // not modified in this state, so get the value directly from the other state
//            new_this_val = other_val;
//        }
//        return new_this_val;
//    }

    /**
     * Propagates the given state into this state.
     * Replaces 'unknown' and polymorphic values when necessary.
     * Assumes that the states belong to the same block and context.
     *
     * @return true if an object changed (note there may be other changes due to recoveries)
     */
    @Override
    public boolean propagate(State s, boolean funentry, boolean widen) {
        if (Options.get().isDebugOrTestEnabled() && !store_default.isAllNone() && !s.store_default.isAllNone() && !store_default.equals(s.store_default))
            throw new AnalysisException("Expected store default objects to be equal");
        if (log.isDebugEnabled() && Options.get().isIntermediateStatesEnabled()) {
            log.debug("join this state: " + this);
            log.debug("join other state: " + s);
        }
        if (s.isBottom()) {
            if (log.isDebugEnabled())
                log.debug("propagate(...) - other is bottom");
            return false;
        }
        if (isBottom()) {
            setToState(s);
            if (log.isDebugEnabled())
                log.debug("propagate(...) - this is bottom, other is non-bottom");
            return true; // s is not none
        }
        makeWritableStore();
        makeWritableExecutionContext();
        makeWritableRegisters();
        makeWritableStacked();
        boolean changed = execution_context.add(s.execution_context);
        Set<ObjectLabel> labs = newSet();
        labs.addAll(store.keySet());
        labs.addAll(s.store.keySet());
        for (ObjectLabel lab : labs)
            changed |= propagateObj(lab, s, lab, false, widen);
        if (Options.get().isLazyDisabled())
            changed |= stacked_objlabels.addAll(s.stacked_objlabels);
        changed |= stacked_funentries.addAll(s.stacked_funentries);
        changed |= extras.propagate(s.extras);
        changed |= must_reaching_defs.propagate(s.must_reaching_defs);
        changed |= must_equals.propagate(s.must_equals);
        changed |= renamings.join(s.renamings);
        changed |= partitioning.propagate(s.partitioning);
        if (!funentry) {
            for (int i = 0; i < registers.size() || i < s.registers.size(); i++) {
                Value v1 = i < registers.size() ? registers.get(i) : null;
                Value v2 = i < s.registers.size() ? s.registers.get(i) : null;
                Value v;
                if (v1 == null || v2 == null)
                    v = null;
                else
                    v = UnknownValueResolver.join(v1, this, v2, s, widen);
                if (i < registers.size())
                    registers.set(i, v);
                else
                    registers.add(v);
                if (v != null && !v.equals(v1)) {
                    changed = true;
                }
            }
        }
        if (store_default.isAllNone() && !s.store_default.isAllNone()) {
            for (ObjectLabel lab : s.store.keySet()) { // materialize before changing default
                if (!store.containsKey(lab)) {
                    writeToStore(lab, store_default);
                }
            }
            store_default = s.store_default = Canonicalizer.get().canonicalizeViaImmutableBox(s.store_default.freeze());
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
    public boolean propagateObj(ObjectLabel objlabel_to, State state_from, ObjectLabel objlabel_from, boolean modified, boolean widen) {
        Obj obj_from = state_from.getObject(objlabel_from, false);
        Obj obj_to = getObject(objlabel_to, false);
        if (obj_from == obj_to && !modified) {
            // identical objects, so nothing to do
            return false;
        }
        if (obj_from.isAllNone()) { // may be a call edge or function entry state where not all properties have been propagated, so don't use isSomeNone here
            // obj_from object is none, so nothing to do
            return false;
        }
        // join all properties from obj_from into obj_to
        boolean changed = false;
        Value default_numeric_property_to = obj_to.getDefaultNumericProperty();
        Value default_numeric_property_from = obj_from.getDefaultNumericProperty();
        Value default_numeric_property_to_original = default_numeric_property_to;
        if (modified || !default_numeric_property_to.isUnknown() || !default_numeric_property_from.isUnknown()) {
            if (default_numeric_property_to.isUnknown())
                default_numeric_property_to = UnknownValueResolver.getDefaultNumericProperty(objlabel_to, this);
            if (default_numeric_property_from.isUnknown())
                default_numeric_property_from = UnknownValueResolver.getDefaultNumericProperty(objlabel_from, state_from);
            default_numeric_property_to = default_numeric_property_to.join(default_numeric_property_from, widen);
            if (modified)
                default_numeric_property_to = default_numeric_property_to.joinModified();
            if (default_numeric_property_to != default_numeric_property_to_original) {
                if (!obj_to.isWritable())
                    obj_to = getObject(objlabel_to, true);
                obj_to.setDefaultNumericProperty(default_numeric_property_to);
                changed = true;
            }
        }
        Value default_other_property_to = obj_to.getDefaultOtherProperty();
        Value default_other_property_from = obj_from.getDefaultOtherProperty();
        Value default_other_property_to_original = default_other_property_to;
        if (modified || !default_other_property_to.isUnknown() || !default_other_property_from.isUnknown()) {
            if (default_other_property_to.isUnknown())
                default_other_property_to = UnknownValueResolver.getDefaultOtherProperty(objlabel_to, this);
            if (default_other_property_from.isUnknown())
                default_other_property_from = UnknownValueResolver.getDefaultOtherProperty(objlabel_from, state_from);
            default_other_property_to = default_other_property_to.join(default_other_property_from, widen);
            if (modified)
                default_other_property_to = default_other_property_to.joinModified();
            if (default_other_property_to != default_other_property_to_original) {
                if (!obj_to.isWritable())
                    obj_to = getObject(objlabel_to, true);
                obj_to.setDefaultOtherProperty(default_other_property_to);
                changed = true;
            }
        }
        obj_from = state_from.getObject(objlabel_from, false); // propagating defaults may have materialized properties, so get the latest version
        for (PKey propertyname : obj_from.getProperties().keySet()) {
            if (!obj_to.getProperties().containsKey(propertyname)) {
                Value v = propertyname.isNumeric() ? default_numeric_property_to_original : default_other_property_to_original;
                if (!obj_to.isWritable())
                    obj_to = getObject(objlabel_to, true);
                obj_to.setProperty(propertyname, v); // materializing from default doesn't affect 'changed'
//                if (log.isDebugEnabled())
//                  log.debug("Materialized " + objlabel_to + "." + propertyname + " = " + v);
            }
        }
        for (PKey propertyname : newList(obj_to.getPropertyNames())) { // TODO: need newList (to avoid ConcurrentModificationException)?
            Value v_to = obj_to.getProperty(propertyname);
            Value v_from = obj_from.getProperty(propertyname);
            if (modified || !v_to.isUnknown() || !v_from.isUnknown()) {
                Value v_to_original = v_to;
                if (v_to.isUnknown())
                    v_to = UnknownValueResolver.getProperty(objlabel_to, propertyname, this, v_from.isPolymorphic());
                if (v_from.isUnknown())
                    v_from = UnknownValueResolver.getProperty(objlabel_from, propertyname, state_from, v_to.isPolymorphic());
                v_to = UnknownValueResolver.join(v_to, this, v_from, state_from, widen);
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
        if (modified || !internal_prototype_to.isUnknown() || !internal_prototype_from.isUnknown()) {
            Value internal_prototype_to_original = internal_prototype_to;
            if (internal_prototype_to.isUnknown())
                internal_prototype_to = UnknownValueResolver.getInternalPrototype(objlabel_to, this, internal_prototype_from.isPolymorphic());
            if (internal_prototype_from.isUnknown())
                internal_prototype_from = UnknownValueResolver.getInternalPrototype(objlabel_from, state_from, internal_prototype_to.isPolymorphic());
            internal_prototype_to = UnknownValueResolver.join(internal_prototype_to, this, internal_prototype_from, state_from, widen);
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
        if (modified || !internal_value_to.isUnknown() || !internal_value_from.isUnknown()) {
            Value internal_value_to_original = internal_value_to;
            if (internal_value_to.isUnknown())
                internal_value_to = UnknownValueResolver.getInternalValue(objlabel_to, this, internal_value_from.isPolymorphic());
            if (internal_value_from.isUnknown())
                internal_value_from = UnknownValueResolver.getInternalValue(objlabel_from, state_from, internal_value_to.isPolymorphic());
            internal_value_to = UnknownValueResolver.join(internal_value_to, this, internal_value_from, state_from, widen);
            if (modified)
                internal_value_to = internal_value_to.joinModified();
            if (internal_value_to != internal_value_to_original) {
                if (!obj_to.isWritable())
                    obj_to = getObject(objlabel_to, true);
                obj_to.setInternalValue(internal_value_to);
                changed = true;
            }
        }
        if (modified || !obj_to.isScopeChainUnknown() || !obj_from.isScopeChainUnknown()) {
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
     * Returns a description of the names of the [enumerable] properties of the given objects [and their prototypes].
     */
    public ObjProperties getProperties(Collection<ObjectLabel> objlabels, ObjProperties.PropertyQuery flags) {
        return ObjProperties.getProperties(objlabels, this, flags);
    }

    /**
     * Returns the set of objects in the prototype chain that contain the property.
     */
    public Set<ObjectLabel> getPrototypeWithProperty(ObjectLabel objlabel, PKeys propertyName) { // TODO: review -- see PropVarOperations.readPropertyRaw
        if (Options.get().isDebugOrTestEnabled() && propertyName.isMaybeOtherThanStr()) {
            throw new AnalysisException("Uncoerced property name: " + propertyName);
        }
        Set<ObjectLabel> ol = Collections.singleton(objlabel);
        Set<ObjectLabel> visited = newSet();
        Set<ObjectLabel> res = newSet();
        while (!ol.isEmpty()) {
            Set<ObjectLabel> ol2 = newSet();
            for (ObjectLabel l : ol)
                if (!visited.contains(l)) {
                    visited.add(l);

                    Collection<Value> values = newList();
                    if (propertyName.isMaybeFuzzyStrOrSymbol()) {
                        if (propertyName.isMaybeStrSomeNonNumeric()) {
                            values.add(UnknownValueResolver.getDefaultOtherProperty(l, this));
                        }
                        if (propertyName.isMaybeStrSomeNumeric()) {
                            values.add(UnknownValueResolver.getDefaultNumericProperty(l, this));
                        }
                        // relevant properties have been materialized now
                        values.addAll(getObject(l, false).getProperties().keySet().stream()
                                .filter(k -> k instanceof StringPKey && propertyName.isMaybeStr(((StringPKey)k).getStr())) // FIXME: doesn't support Symbols?
                                .map(n -> UnknownValueResolver.getProperty(l, n, this, true))
                                .collect(Collectors.toList()));
                    } else { // FIXME: doesn't support Symbols?
                        values.add(UnknownValueResolver.getProperty(l, StringPKey.make(propertyName.getStr()), this, true));
                    }

                    boolean definitelyAbsent = values.stream().allMatch(Value::isNotPresent);
                    boolean maybeAbsent = values.stream().anyMatch(Value::isMaybeAbsent);

                    if (definitelyAbsent) {
                        Value proto = UnknownValueResolver.getInternalPrototype(l, this, false);
                        ol2.addAll(proto.getObjectLabels());
                    } else if (maybeAbsent) {
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

    // TODO: replace with getPrototypeWithProperty, but check messages!
    public Set<ObjectLabel> getPrototypesUsedForUnknown(ObjectLabel objlabel) { // TODO: review (used only in AnalysisMonitor)
        State state = c.getState();
        Set<ObjectLabel> ol = Collections.singleton(objlabel);
        Set<ObjectLabel> visited = newSet();
        Set<ObjectLabel> res = newSet();
        while (!ol.isEmpty()) {
            Set<ObjectLabel> ol2 = newSet();
            for (ObjectLabel l : ol)
                if (!visited.contains(l)) {
                    visited.add(l);
                    Value v = UnknownValueResolver.getDefaultNumericProperty(objlabel, state);
                    if (v.isMaybeAbsent()) {
                        Value proto = UnknownValueResolver.getInternalPrototype(l, state, false);
                        ol2.addAll(proto.getObjectLabels());
                        res.add(l);
                    }
                }
            ol = ol2;
        }
        return res;
    }

    /**
     * Materializes a singleton object from the given summary object.
     * @param definitely_only_one set to true if the object has been created only once since function entry
     * @return object label of the materialized singleton
     */
    public ObjectLabel materializeObj(ObjectLabel summary, boolean definitely_only_one) {
        if (basis_store != null && basis_store.containsKey(summary))
            throw new AnalysisException("Attempt to summarize object from basis store");
        if (summary.isSingleton())
            throw new AnalysisException("Expected summary object");
        if (Options.get().isRecencyDisabled())
            throw new AnalysisException("Can't materialize when recency is disabled");
        makeWritableStore();
        ObjectLabel singleton = summary.makeSingleton();
        Obj oldSummaryObj = getObject(summary, true);
        summarizeObj(singleton, summary, new Obj(oldSummaryObj));
        renamings.removeSummarized(singleton, definitely_only_one);
        if (log.isDebugEnabled())
            log.debug("materializeObj(" + summary + ")");
        return singleton;
    }
    /**
     * Adds an object label, representing a new empty object, to the store.
     * Takes recency abstraction into account.
     * Updates sets of renamings objects.
     */
    public void newObject(ObjectLabel objlabel) {
        if (basis_store != null && basis_store.containsKey(objlabel))
            throw new AnalysisException("Attempt to summarize object from basis store");
        makeWritableStore();
        c.getMonitoring().visitNewObject(c.getNode(), objlabel, this);
        if (!Options.get().isRecencyDisabled() && (objlabel.getKind() != Kind.SYMBOL || objlabel.isHostObject())) { // non-host symbol objects always modeled as summary objects to avoid problem when summarizing objects with symbol property keys and polymorphic values
            if (!objlabel.isSingleton())
                throw new AnalysisException("Expected singleton object label");
            summarizeObj(objlabel, objlabel.makeSummary(), Obj.makeAbsentModified());
        } else {
            // join the empty object into oldobj (only relevant if recency abstraction is disabled)
            Obj obj = getObject(objlabel, true);
            Value old_numeric = UnknownValueResolver.getDefaultNumericProperty(objlabel, this);
            Value old_other = UnknownValueResolver.getDefaultOtherProperty(objlabel, this);
            obj.setDefaultNumericProperty(old_numeric.joinAbsentModified());
            obj.setDefaultOtherProperty(old_other.joinAbsentModified());
            for (Map.Entry<PKey, Value> me : newSet(UnknownValueResolver.getProperties(objlabel, this).entrySet())) {
                PKey propertyname = me.getKey();
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

    private void summarizeObj(ObjectLabel singleton, ObjectLabel summary, Obj newObj) {
        Obj oldobj = getObject(singleton, false);
        if (!oldobj.isSomeNone()) {
            // join singleton object into its summary object
            // FIXME Support c.getMonitoring().visitRenameObject(c.getNode(), singleton, summary, this); (GitHub #413)
            propagateObj(summary, this, singleton, true, false);
            // update references
            Map<ScopeChain, ScopeChain> cache = new HashMap<>();
            for (ObjectLabel objlabel2 : newList(store.keySet())) {
                if (getObject(objlabel2, false).containsObjectLabel(singleton)) {
                    Obj obj = getObject(objlabel2, true);
                    obj.replaceObjectLabel(singleton, summary, cache);
                }
            }
            makeWritableExecutionContext();
            execution_context.replaceObjectLabel(singleton, summary, cache);
            makeWritableRegisters();
            for (int i = 0; i < registers.size(); i++) {
                Value v = registers.get(i);
                if (v != null) {
                    registers.set(i, v.replaceObjectLabel(singleton, summary));
                }
            }
            extras.replaceObjectLabel(singleton, summary);
            must_equals.setToBottom(singleton);
            if (Options.get().isLazyDisabled())
                if (stacked_objlabels.contains(singleton)) {
                    makeWritableStacked();
                    stacked_objlabels.remove(singleton);
                    stacked_objlabels.add(summary);
                }
            if (getObject(summary, false).isUnknown() && store_default.isUnknown())
                store.remove(summary);
        }
        // now the old object is gone
        renamings.addDefinitelySummarized(singleton);
        makeWritableStore();
        writeToStore(singleton, newObj);
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
        if (objlabel.isSingleton()) { // TODO merge this implementation with the one in #newObject?
            // move the object
            ObjectLabel summarylabel = objlabel.makeSummary();
            c.getMonitoring().visitRenameObject(c.getNode(), objlabel, summarylabel, this);
            propagateObj(summarylabel, this, objlabel, true, false);
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
            extras.replaceObjectLabel(objlabel, summarylabel);
            must_equals.setToBottom(objlabel);
            if (Options.get().isLazyDisabled())
                if (stacked_objlabels.contains(objlabel)) {
                    makeWritableStacked();
                    stacked_objlabels.remove(objlabel);
                    stacked_objlabels.add(summarylabel);
                }
            if (log.isDebugEnabled())
                log.debug("multiplyObject(" + objlabel + ")");
        }
    }

    /**
     * Returns a copy of this state where the given object label has been replaced.
     */
    public void replaceObjectLabel(ObjectLabel oldlabel, ObjectLabel newlabel) {
        makeWritableStore();
        Map<ScopeChain, ScopeChain> cache = new HashMap<>();
        for (ObjectLabel objlabel2 : newList(store.keySet())) {
            if (getObject(objlabel2, false).containsObjectLabel(oldlabel)) {
                Obj obj = getObject(objlabel2, true);
                obj.replaceObjectLabel(oldlabel, newlabel, cache);
            }
            if (objlabel2.equals(oldlabel))
                store.put(newlabel, store.remove(oldlabel));
        }
        makeWritableExecutionContext();
        execution_context.replaceObjectLabel(oldlabel, newlabel, cache);
        makeWritableRegisters();
        for (int i = 0; i < registers.size(); i++) {
            Value v = registers.get(i);
            if (v != null) {
                registers.set(i, v.replaceObjectLabel(oldlabel, newlabel));
            }
        }
        extras.replaceObjectLabel(oldlabel, newlabel);
        must_equals.replaceObjectLabel(oldlabel, newlabel);
        if (Options.get().isLazyDisabled())
            if (stacked_objlabels.contains(oldlabel)) {
                makeWritableStacked();
                stacked_objlabels.remove(oldlabel);
                stacked_objlabels.add(newlabel);
            }
    }

    /**
     * Reads a variable directly from the current variable object, without considering the full scope chain.
     * (Only to be used for testing.)
     */
    public Value readVariableDirect(String var) {
        Collection<Value> values = newList();
        for (ObjectLabel objlabel : execution_context.getVariableObject()) {
            values.add(readProperty(ObjectProperty.makeOrdinary(objlabel, StringPKey.make(var)), false));
        }
        return UnknownValueResolver.join(values, this);
    }

    /**
     * Reads the designated property value.
     */
    public Value readProperty(ObjectProperty p, boolean partial) {
        ObjectLabel objlabel = p.getObjectLabel();
        switch (p.getKind()) {
            case ORDINARY:
                return UnknownValueResolver.getProperty(objlabel, p.getPropertyName(), this, partial);
            case DEFAULT_NUMERIC:
                return UnknownValueResolver.getDefaultNumericProperty(objlabel, this);
            case DEFAULT_OTHER:
                return UnknownValueResolver.getDefaultOtherProperty(objlabel, this);
            case INTERNAL_PROTOTYPE:
                return UnknownValueResolver.getInternalPrototype(objlabel, this, partial);
            case INTERNAL_VALUE:
                return UnknownValueResolver.getInternalValue(objlabel, this, partial);
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
        for (ObjectLabel objlabel : objlabels) {
            Value newval;
            if (objlabels.size() == 1 && objlabel.isSingleton()) // strong update
                newval = value;
            else { // weak update
                Value oldval = UnknownValueResolver.getInternalPrototype(objlabel, this, true);
                newval = UnknownValueResolver.join(oldval, value, this);
            }
            newval = newval.joinModified();
            Obj obj = getObject(objlabel, true);
            // FIXME only null or object values are actually written! (see JSObject -> OBJECT_SETPROTOTYPEOF for example) (GitHub #356)
            // FIXME Property.__PROTO__ should be assigned `absent` when `newval.isMaybeNull` (GitHub #356)
            obj.setProperty(StringPKey.__PROTO__, newval.setAttributes(true, true, false));
            obj.setInternalPrototype(newval); // TODO: redundant, the value is also available as __proto__
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
        execution_context.setVariableObject(dk.brics.tajs.util.Collections.newSet());
    }

    /**
     * Sets the execution context.
     */
    public void setExecutionContext(ExecutionContext e) {
        execution_context = e;
        writable_execution_context = true;
    }

//    protected void remove(State other) { // (currently unused)
//        makeWritableStore();
//        makeWritableExecutionContext();
//        makeWritableRegisters();
//        makeWritableStackedObjects();
//        store_default = new Obj(store_default);
//        store_default.remove(other.store_default);
//        for (ObjectLabel objlabel : store.keySet()) {
//            Obj obj = getObject(objlabel, true);
//            Obj other_obj = other.getObject(objlabel, false);
//            obj.remove(other_obj);
//        }
//        execution_context.remove(other.execution_context);
//        // don't remove from renamings (lattice order of definitely_summarized is reversed, so removal isn't trivial)
//        for (int i = 0; i < registers.size(); i++) {
//            Value v_this = registers.get(i);
//            if (v_this != null) {
//                Value v_other = other.registers.get(i);
//                if (v_other != null)
//                    registers.set(i, v_this.remove(v_other));
//            }
//        }
//        stacked_objlabels.removeAll(other.stacked_objlabels);
//        stacked_funentries.removeAll(other.stacked_funentries);
//        extras.remove(other.extras);
//    }

    /**
     * Returns a string description of the differences between this state and the given one.
     */
    @Override
    public String diff(State old) {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<ObjectLabel, Obj> me : sortedEntries(store, new ObjectLabel.Comparator())) {
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
        if (!execution_context.getThis().equals(old.execution_context.getThis())) {
            b.append("\n      new this: ");
            execution_context.getThis().diff(old.execution_context.getThis(), b);
        }
        if (!ScopeChain.isEmpty(ScopeChain.remove(execution_context.getScopeChain(), old.execution_context.getScopeChain())))
            b.append("\n      new scope chain: ").append(ScopeChain.remove(execution_context.getScopeChain(), old.execution_context.getScopeChain()));
        renamings.diff(old.renamings, b);
        temp = newSet(stacked_objlabels);
        temp.removeAll(old.stacked_objlabels);
        if (!temp.isEmpty())
            b.append("\n      new stacked object labels: ").append(temp);
        Set<BlockAndContext<Context>> temp2 = newSet(stacked_funentries);
        temp2.removeAll(old.stacked_funentries);
        if (!temp2.isEmpty())
            b.append("\n      new stacked functions: ").append(temp2);
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
        b.append("\n  Renamings: ").append(renamings);
        b.append("\n  Store (excluding basis and default objects): ");
        for (Map.Entry<ObjectLabel, Obj> me : sortedEntries(store, new ObjectLabel.Comparator())) {
            b.append("\n    ").append(me.getKey()).append(" (").append(me.getKey().getSourceLocation()).append("): ").append(me.getValue());
        }
//        b.append("\n  Default object: ").append(store_default);
//        b.append("\n  Store default: ").append(store_default);
        b.append("\n  Registers: ");
        for (int i = 0; i < registers.size(); i++)
            if (registers.get(i) != null)
                b.append("\n    v").append(i).append("=").append(registers.get(i));
        b.append(extras);
        String mrd = must_reaching_defs.toString();
        if (!mrd.isEmpty())
            b.append("\n  MustReachingDefs: ").append(mrd);
        String me = must_equals.toString();
        if (!me.isEmpty())
            b.append("\n  MustEquals: ").append(me);
        if (Options.get().isLazyDisabled())
            b.append("\n  Objects used by outer scopes: ").append(stacked_objlabels);
        b.append("\n  Functions in stack: ").append(stacked_funentries);
        if (!partitioning.isEmpty())
            b.append("\n  Partitioning: ").append(partitioning);
        return b.toString();
    }

    /**
     * Prints the objects of the given value.
     */
    public String printObject(Value v) {
        StringBuilder b = new StringBuilder();
        v.getObjectLabels().stream().sorted(new ObjectLabel.Comparator()).forEach(obj -> {
            if (b.length() > 0)
                b.append(", ");
            b.append(getObject(obj, false)); // TODO: .append(" at ").append(obj.getSourceLocation());
        });
        return b.toString();
    }

    /**
     * As {@link #toString()} but excludes registers and non-modified objects and properties.
     */
    @Override
    public String toStringBrief() {
        StringBuilder b = new StringBuilder("Abstract state:");
        b.append("\n  Execution context: ").append(execution_context);
        b.append("\n  Renamings: ").append(renamings);
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
        for (Map.Entry<ObjectLabel, Obj> me : sortedEntries(store, new ObjectLabel.Comparator()))
            b.append("\n  ").append(me.getKey()).append(" (").append(me.getKey().getSourceLocation()).append("):").append(me.getValue().printModified());
    }

    @Override
    public String toDot() {
        StringBuilder ns = new StringBuilder("\n\t/* Nodes */\n");
        StringBuilder es = new StringBuilder("\n\t/* Edges */\n");
        // nodes
        TreeSet<ObjectLabel> objs = new TreeSet<>(new ObjectLabel.Comparator());
        for (Map.Entry<ObjectLabel, Obj> e : sortedEntries(store, new ObjectLabel.Comparator())) {
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
                for (Map.Entry<PKey, Value> ee : obj.getProperties().entrySet()) {
                    s.append("|").append("<f").append(index++).append("> ").append(ee.getKey()).append("=").append(esc(ee.getValue().restrictToNotObject().toString()));
                }
                if (!obj.getDefaultNumericProperty().isUnknown()) {
                    s.append("|").append("<f").append(index++).append("> [[DefaultNumeric]]=").append(esc(obj.getDefaultNumericProperty().restrictToNotObject().toString()));
                }
                if (!obj.getDefaultOtherProperty().isUnknown()) {
                    s.append("|").append("<f").append(index++).append("> [[DefaultOther]]=").append(esc(obj.getDefaultOtherProperty().restrictToNotObject().toString()));
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
        for (Map.Entry<ObjectLabel, Obj> e : sortedEntries(store, new ObjectLabel.Comparator())) {
            ObjectLabel sourceLabel = e.getKey();
            Obj obj = e.getValue();
            int index = 0;
            for (Map.Entry<PKey, Value> ee : obj.getProperties().entrySet()) {
                Value value = ee.getValue();
                String source = node(sourceLabel) + ":f" + index;
                for (ObjectLabel targetLabel : value.getObjectLabels()) {
                    String target = node(targetLabel);
                    es.append("\t").append(source).append(" -> ").append(target).append(";\n");
                }
                index++;
            }
            if (!obj.getDefaultNumericProperty().isUnknown()) {
                String source = node(sourceLabel) + ":f" + index;
                for (ObjectLabel targetLabel : obj.getDefaultNumericProperty().getObjectLabels()) {
                    String target = node(targetLabel);
                    es.append("\t").append(source).append(" -> ").append(target).append(";\n");
                }
                index++;
            }
            if (!obj.getDefaultOtherProperty().isUnknown()) {
                String source = node(sourceLabel) + ":f" + index;
                for (ObjectLabel targetLabel : obj.getDefaultOtherProperty().getObjectLabels()) {
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
        for (ObjectLabel objlabel : execution_context.getThis().getObjectLabels())
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

// TODO: toDotDOM() ?
//    public String toDotDOM() { // (currently unused)
//           StringBuilder ns = new StringBuilder("\n\t/* Nodes */\n");
//        StringBuilder es = new StringBuilder("\n\t/* Edges */\n");
//
//        for (Map.Entry<ObjectLabel, Obj> e : sortedEntries(store)) {
//            ObjectLabel label = e.getKey();
//            Obj object = e.getValue();
//            if (!label.isHostObject()) {
//                // Ignore non-host objects
//                continue;
//            }
//            if (object.getInternalPrototype().getObjectLabels().contains(FUNCTION_PROTOTYPE)) {
//                // Ignore functions objects
//                continue;
//            }
//            if (Options.get().isDOMEnabled()) {
//                if ((label.getHostObject().getAPI() != HostAPIs.DOCUMENT_OBJECT_MODEL
//                        && label.getHostObject().getAPI() != HostAPIs.DSL_OBJECT_MODEL)) {
//                    // Ignore non-DOM objects (if DOM is enabled)
//                    continue;
//                }
//            }
//
//            // Build label (i.e. the box)
//            // The intended format is: FOO [shape=record label="{NAME\lEntry_1\lEntry2\l}"]
//            String name = label.toString();
//            StringBuilder lbl = new StringBuilder();
//            lbl.append(name);
//            Map<String, Value> properties = new TreeMap<String, Value>(object.getProperties());
//            Iterator<String> iter = properties.keySet().iterator();
//            int i = 0;
//            int limit = 10;
//            while (i < limit && iter.hasNext()) {
//                if (i == 0) {
//                    lbl.append("|");
//                } else {
//                    lbl.append("\\l");
//                }
//                String property = iter.next();
//                lbl.append(property);
//                i++;
//            }
//            lbl.append("\\l");
//            if (iter.hasNext()) {
//                lbl.append("...\\l");
//            }
//            ns.append("\t").append("\"").append(name).append("\"").append(" [shape=record label=\"{").append(lbl).append("}\"]").append("\n");
//
//            // Build arrows
//            for (ObjectLabel prototype : object.getInternalPrototype().getObjectLabels()) {
//                es.append("\t").append("\"").append(name).append("\"").append(" -> ").append("\"").append(prototype).append("\"").append("\n");
//            }
//        }
//
//        // Put everything together
//        StringBuilder sb = new StringBuilder();
//        sb.append("digraph {\n");
//        sb.append("\tcompound=true\n");
//        sb.append("\trankdir=\"BT\"\n");
//        sb.append("\tnode [fontname=\"Arial\"]\n");
//        sb.append(ns);
//        sb.append(es);
//        sb.append("}");
//        return sb.toString();
//    }

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
                writeToStore(objlabel, Obj.makeNoneModified());
        }
        // don't remove from renamings (it may contain dead object labels)
        if (Options.get().isIntermediateStatesEnabled())
            if (log.isDebugEnabled())
                log.debug("gc(): After: " + this);
    }

    /**
     * Returns true if the given object label is definitely the none object at the given function entry state.
     */
    private static boolean noneAtEntry(ObjectLabel objlabel, State entry_state) {
        return entry_state.getObject(objlabel, false).getDefaultNumericProperty().isNone();
    }

    /**
     * Finds live object labels (i.e. those reachable from the execution context, registers, or stacked object labels).
     * Note that the renamings sets may contain dead object labels.
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
                // unless it is a singleton object marked as definitely new or it is 'none' at function entry
                if (!((objlabel.isSingleton() && renamings.isDefinitelyNew(objlabel)) ||
                        (noneAtEntry(objlabel, entry_state) && (objlabel.isSingleton() || (renamings.isMaybeNew(objlabel.makeSingleton()) && noneAtEntry(objlabel.makeSingleton(), entry_state))))))
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
            objlabels.addAll(v.getAllObjectLabels());
        objlabels.addAll(fo.getDefaultNumericProperty().getAllObjectLabels());
        objlabels.addAll(fo.getDefaultOtherProperty().getAllObjectLabels());
        objlabels.addAll(fo.getInternalPrototype().getAllObjectLabels());
        objlabels.addAll(fo.getInternalValue().getAllObjectLabels());
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
        if (v.isMaybePrimitiveOrSymbol())
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
        writeRegister(reg, value, true);
    }
    /**
     * Assigns the given value to the given register (strong update).
     * All attribute information is cleared. 'unknown' values are not permitted.
     * @param ordinary if set, kill must-equals information for that register
     */
    public void writeRegister(int reg, Value value, boolean ordinary) {
        value = value.setBottomPropertyData();
        if (value.isUnknown())
            throw new AnalysisException("Unexpected 'unknown'");
        makeWritableRegisters();
        while (reg >= registers.size())
            registers.add(null);
        registers.set(reg, value);
        if (ordinary)
            must_equals.setToBottom(reg);
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
        must_reaching_defs.setToBottom(reg);
        must_equals.setToBottom(reg);
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
        if (res == null) {
            if (Options.get().isPropagateDeadFlow())
                res = Value.makeNone();
            else
                throw new AnalysisException("Reading undefined register v" + reg);
        }
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
        writable_registers = true;
    }

    /**
     * Sets the must reaching definitions.
     */
    public void setMustReachingDefs(MustReachingDefs must_reaching_defs) {
        this.must_reaching_defs = must_reaching_defs;
    }

    /**
     * Adds object labels used in current registers and execution
     * context to stacked object labels.
     */
    public void stackObjectLabels() {
        if (!Options.get().isLazyDisabled())
            return;
        makeWritableStacked();
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
        else
            registers = Collections.emptyList();
    }

    /**
     * Returns the value of 'this'.
     */
    public Value readThis() {
        Value res = execution_context.getThis();
        if (log.isDebugEnabled())
            log.debug("readThis() = " + res);
        return res;
    }

    /**
     * Returns the value of 'this'.
     */
    public Set<ObjectLabel> readThisObjects() {
        Set<ObjectLabel> objs = readThis().getObjectLabels(); // TODO: assert no primitive values (Github #479) ? -- see OrdinaryCallBindThis in the ECMAScript spec
        if (objs.isEmpty())
            throw new AnalysisException("empty result from readThisObjects");
        return objs;
    }

    /**
     * Introduces 'unknown' values in this state according to the given function entry state.
     * Also clears modified flags and renamings sets.
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
                for (ObjectLabel objlabel : s.store.keySet()) {
                    if (!store.containsKey(objlabel)) {
                        getObject(objlabel, true); // materialize default objects
                    }
                }
                for (ObjectLabel objlabel : newList(store.keySet())) {
                    Obj obj = getObject(objlabel, true);
                    Obj other = s.getObject(objlabel, false);
                    obj.localize(other, objlabel, this);
                }
                // remove all-unknown objects
                Map<ObjectLabel, Obj> oldStore = store;
                store = newMap();
                for (Map.Entry<ObjectLabel, Obj> xs : oldStore.entrySet())
                    if (!xs.getValue().isUnknown())
                        writeToStore(xs.getKey(), xs.getValue());
                store_default = Obj.makeUnknown();
            }
        } else {
            clearModified();
        }
        renamings.clear();
        must_equals.setToBottom();
    }

    /**
     * Clears effects and renamings sets (for function entry).
     */
    public void clearEffects() {
        clearModified();
        renamings.clear();
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

    public PartitioningInfo getPartitioning() {
        return partitioning;
    }

    /**
     * Applies the given function to all (non-unknown) values in this state.
     */
    public void applyToAllValues(Function<Value, Value> func) {
        // values in execution context
        makeWritableExecutionContext();
        execution_context.setThis(func.apply(execution_context.getThis()));
        // values in registers
        makeWritableRegisters();
        for (int i = 0; i < registers.size(); i++) {
            Value v1 = registers.get(i);
            if (v1 == null)
                continue;
            registers.set(i, func.apply(v1));
        }
        // values in store
        makeWritableStore();
        for (ObjectLabel lab : store.keySet()) {
            Obj obj = getObject(lab, false);

            for (PKey propertyname : newList(obj.getPropertyNames())) { // TODO: need newList (to avoid ConcurrentModificationException)?
                Value v = obj.getProperty(propertyname);
                if (!v.isUnknown()) {
                    Value new_v_to = func.apply(v);
                    if (!new_v_to.equals(v)) {
                        if (!obj.isWritable())
                            obj = getObject(lab, true);
                        obj.setProperty(propertyname, new_v_to);
                    }
                }
            }

            Value default_numeric = obj.getDefaultNumericProperty();
            if (!default_numeric.isUnknown()) {
                Value new_v1 = func.apply(default_numeric);
                if (!new_v1.equals(default_numeric)) {
                    if (!obj.isWritable())
                        obj = getObject(lab, true);
                    obj.setDefaultNumericProperty(new_v1);
                }
            }

            Value default_other = obj.getDefaultOtherProperty();
            if (!default_other.isUnknown()) {
                Value new_default_other = func.apply(default_other);
                if (!new_default_other.equals(default_other)) {
                    if (!obj.isWritable())
                        obj = getObject(lab, true);
                    obj.setDefaultOtherProperty(new_default_other);
                }
            }

            Value internal_value = obj.getInternalValue();
            if (!internal_value.isUnknown()) {
                Value new_internal_value = func.apply(internal_value);
                if (!new_internal_value.equals(internal_value)) {
                    if (!obj.isWritable())
                        obj = getObject(lab, true);
                    obj.setInternalValue(new_internal_value);
                }
            }

            Value internal_prototype = obj.getInternalPrototype();
            if (!internal_prototype.isUnknown()) {
                Value new_internal_prototype = func.apply(internal_prototype);
                if (!new_internal_prototype.equals(internal_prototype)) {
                    if (!obj.isWritable())
                        obj = getObject(lab, true);
                    obj.setInternalPrototype(new_internal_prototype);
                }
            }
        }
    }
}
