/*
 * Copyright 2009-2017 Aarhus University
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

import dk.brics.tajs.flowgraph.BasicBlock;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Work list used by solver.
 */
public class WorkList<ContextType extends IContext<?>> {

    private static Logger log = Logger.getLogger(WorkList.class);

    // invariant: pending_set is a subset of pending_queue (not necessarily equal)

    private static int next_serial;

    private Set<Entry> pending_set;

    private PriorityQueue<Entry> pending_queue;

    private IWorkListStrategy<ContextType> worklist_strategy;

    /**
     * Constructs a new empty work list.
     */
    public WorkList(IWorkListStrategy<ContextType> w) {
        worklist_strategy = w;
        pending_set = new HashSet<>();
        pending_queue = new PriorityQueue<>();
    }

    /**
     * Adds an entry.
     *
     * @return true if changed
     */
    public boolean add(Entry e) {
        if (pending_set.add(e)) {
            pending_queue.add(e);
            if (log.isDebugEnabled())
                log.debug("Adding worklist entry for block " + e.b.getIndex());
            return true;
        }
        return false;
    }

    /**
     * Checks whether the work list is empty.
     */
    public boolean isEmpty() {
        return pending_queue.isEmpty();
    }

    /**
     * Picks and removes the next entry.
     * Returns null if the entry has been removed.
     */
    public Entry removeNext() {
        Entry p = pending_queue.remove();
        if (!pending_set.remove(p)) {
            if (log.isDebugEnabled())
                log.debug("Skipping removed entry " + p);
            return null;
        }
        return p;
    }

    /**
     * Returns the number of entries in the work list.
     */
    public int size() {
        return pending_set.size();
    }

//    /**
//     * Removes the given entry.
//     */
//    public void remove(Entry e) {
//        if (pending_set.remove(e))
//            if (log.isDebugEnabled())
//                log.debug("Removing entry " + e);
//    }

    /**
     * Returns a string description of this work list.
     */
    @Override
    public String toString() {
        return pending_set.toString();
    }

    /**
     * Work list entry.
     * Consists of a block and a context.
     */
    public class Entry implements IWorkListStrategy.IEntry<ContextType>, Comparable<Entry> {

        private BasicBlock b;

        private ContextType c;

        private int serial;

        private int hash;

        /**
         * Constructs a new entry.
         */
        public Entry(BasicBlock b, ContextType c) {
            this.b = b;
            this.c = c;
            serial = next_serial++;
            hash = b.getIndex() + c.hashCode();
        }

        @Override
        public BasicBlock getBlock() {
            return b;
        }

        @Override
        public ContextType getContext() {
            return c;
        }

        @Override
        public int getSerial() {
            return serial;
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
            return p.b == b && p.c.equals(c);
        }

        /**
         * Computes a hash code for this entry.
         */
        @Override
        public int hashCode() {
            return hash;
        }

        /**
         * Compares this and the given entry.
         * This method defines the work list priority using the work list strategy.
         */
        @Override
        public int compareTo(@Nonnull  Entry p) {
            return worklist_strategy.compare(this, p);
        }

        /**
         * Returns a string description of this entry.
         */
        @Override
        public String toString() {
            return Integer.toString(b.getIndex());
        }
    }
}
