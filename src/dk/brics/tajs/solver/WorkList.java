/*
 * Copyright 2009-2018 Aarhus University
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

package dk.brics.tajs.solver;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Work list used by solver.
 */
public class WorkList<ContextType extends IContext<ContextType>> {

    private static Logger log = Logger.getLogger(WorkList.class);

    private TreeSet<Entry> pending_queue;

    private Set<Entry> pending_set; // only for test mode

    private CallGraph<?, ContextType, ?> call_graph;

    /**
     * Constructs a new empty work list.
     */
    public WorkList(CallGraph<?, ContextType, ?> call_graph) {
        this.call_graph = call_graph;
        pending_queue = new TreeSet<>();
        pending_set = newSet();
    }

    /**
     * Adds an entry.
     *
     * @return true if changed
     */
    public boolean add(BlockAndContext<ContextType> bc) {
        Entry e = new Entry(bc);
        boolean added = pending_queue.add(e);
        if (added)
            log.debug("Adding worklist entry for " + bc);
        if (Options.get().isTestEnabled() && pending_set.add(e) != added)
            throw new AnalysisException("Failed to add to worklist - entries perhaps not totally ordered?");
        return added;
    }

    /**
     * Checks whether the work list is empty.
     */
    public boolean isEmpty() {
        return pending_queue.isEmpty();
    }

    /**
     * Picks and removes the next entry.
     */
    public BlockAndContext<ContextType> removeNext() {
        Entry e = Objects.requireNonNull(pending_queue.pollFirst());
        if (Options.get().isTestEnabled() && !pending_set.remove(e))
            throw new AnalysisException("Failed to remove from worklist - entries perhaps not totally ordered?");
        return e.bc;
    }

    /**
     * Returns the number of entries in the work list.
     */
    public int size() {
        return pending_queue.size();
    }

    /**
     * Returns a string description of this work list.
     */
    @Override
    public String toString() {
        return pending_queue.toString();
    }

    /**
     * Work list entry.
     * Consists of a block and a context.
     */
    private class Entry implements Comparable<Entry> {

        private BlockAndContext<ContextType> bc;

        private int hash; // uniquely determined by bc

        private BlockAndContext<ContextType> funentry; // uniquely determined by bc

        private int funentry_order; // uniquely determined by bc

        private int context_order; // uniquely determined by bc

        /**
         * Constructs a new entry.
         */
        public Entry(BlockAndContext<ContextType> bc) {
            this.bc = bc;
            hash = bc.hashCode();
            funentry = BlockAndContext.makeEntry(bc.getBlock(), bc.getContext());
            funentry_order = call_graph.getFunctionEntryOrder(funentry);
            context_order = call_graph.getContextOrder(bc.getContext());
        }

        /**
         * Checks whether this entry is equal to the given one.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null || !getClass().equals(obj.getClass()))
                return false;
            @SuppressWarnings("unchecked")
            WorkList<?>.Entry p = (WorkList<?>.Entry) obj;
            return p.bc.equals(bc);
        }

        /**
         * Computes a hash code for this entry.
         */
        @Override
        public int hashCode() {
            return hash;
        }

        /**
         * Returns a string description of this entry.
         */
        @Override
        public String toString() {
            return "(" + bc + ")";
//            return Integer.toString(bc.getBlock().getIndex());
        }

        /**
         * Compares this and the given entry.
         * This method defines the work list priority using the work list strategy.
         * A negative return value means that this first has higher priority than the other,
         * a positive return value means that the other has higher priority than this.
         */
        @Override
        public int compareTo(@Nonnull Entry other) {
            if (bc.equals(other.bc))
                return 0;

            final int THIS_FIRST = -1;
            final int OTHER_FIRST = 1;

//            // low priority for event dispatcher node
//            if (bc.getBlock().getFirstNode() instanceof EventDispatcherNode && !(other.bc.getBlock().getFirstNode() instanceof EventDispatcherNode))
//                return OTHER_FIRST;
//            if (other.bc.getBlock().getFirstNode() instanceof EventDispatcherNode && !(bc.getBlock().getFirstNode() instanceof EventDispatcherNode))
//                return THIS_FIRST;

//            // low priority for exceptional return nodes
//            if (bc.getBlock().getFirstNode() instanceof ExceptionalReturnNode && !(other.bc.getBlock().getFirstNode() instanceof ExceptionalReturnNode))
//                return OTHER_FIRST;
//            if (other.bc.getBlock().getFirstNode() instanceof ExceptionalReturnNode && !(bc.getBlock().getFirstNode() instanceof ExceptionalReturnNode))
//                return THIS_FIRST;

            if (funentry.equals(other.funentry)) {
                // same function and same context at entry: use block order (reverse post order)
                if (bc.getBlock().getOrder() < other.bc.getBlock().getOrder())
                    return THIS_FIRST;
                else if (other.bc.getBlock().getOrder() < bc.getBlock().getOrder())
                    return OTHER_FIRST;
                // same block, same function and context at entry, but different context: order by context number (not important, but need a tiebreaker)
                return context_order - other.context_order;
            } else {
                // different function/context at entry: order by entry occurrence number (lower first)
                if (funentry_order < other.funentry_order)
                    return THIS_FIRST;
                else if (other.funentry_order < funentry_order)
                    return OTHER_FIRST;
                throw new AnalysisException("Failed to compare " + this + " + and " + other);
            }
        }
    }
}
