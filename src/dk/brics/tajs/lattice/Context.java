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

package dk.brics.tajs.lattice;

import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.solver.IContext;

import java.util.Map;
import java.util.Set;

/**
 * Context for context sensitive analysis.
 * Immutable.
 */
public final class Context implements IContext<Context> { // TODO: canonicalize? (#140)

    /**
     * Cached hashcode for immutable instance.
     */
    private final int hashcode;

    private final Set<ObjectLabel> thisval; // TODO: canonicalize? (#140)

    /**
     * Values of function arguments at function entry, or null if none.
     */
    private final ContextArguments funArgs;

    /**
     * Values of special registers, or null if none.
     */
    private final Map<Integer, Value> specialRegs;

    /**
     * The number of times loops have been unrolled, or null if none.
     */
    private final Map<BeginLoopNode, Integer> loopUnrolling;

    /**
     * Unrolling information at the function entry, or null if none.
     */
    private final Map<BeginLoopNode, Integer> loopUnrollingsAtEntry;

    /**
     * Constructs a new context object.
     */
    public Context(Set<ObjectLabel> thisval, ContextArguments funArgs, Map<Integer, Value> specialRegs,
                   Map<BeginLoopNode, Integer> loopUnrolling, Map<BeginLoopNode, Integer> loopUnrollingsAtEntry) {
        this.thisval = thisval;
        this.funArgs = funArgs;
        this.specialRegs = specialRegs;
        this.loopUnrolling = loopUnrolling;
        this.loopUnrollingsAtEntry = loopUnrollingsAtEntry;

        int hashcode = 1;
        hashcode = 31 * hashcode + (thisval != null ? thisval.hashCode() : 0);
        hashcode = 31 * hashcode + (funArgs != null ? funArgs.hashCode() : 0);
        hashcode = 31 * hashcode + (specialRegs != null ? specialRegs.hashCode() : 0);
        hashcode = 31 * hashcode + (loopUnrolling != null ? loopUnrolling.hashCode() : 0);
        this.hashcode = hashcode;
    }

    /**
     * Returns the this-value.
     */
    public Set<ObjectLabel> getThisVal() {
        return thisval;
    }

    /**
     * Returns the function arguments.
     */
    public ContextArguments getFunArgs() {
        return funArgs;
    }

    /**
     * Returns the special registers map.
     */
    public Map<Integer, Value> getSpecialRegisters() {
        return specialRegs;
    }

    /**
     * Returns the loop unrolling information.
     */
    public Map<BeginLoopNode, Integer> getLoopUnrolling() {
        return loopUnrolling;
    }

    /**
     * Returns the loop unrolling at entry information.
     */
    public Map<BeginLoopNode, Integer> getLoopUnrollingsAtEntry() {
        return loopUnrollingsAtEntry;
    }

    /**
     * Reconstructs the context at function or for-in entry.
     */
    @Override
    public Context makeEntryContext() {
        // reconstruct loopUnrolling from loopUnrollingsAtEntry (all other properties are unchanged within a function or for-in body)
        if (loopUnrollingsAtEntry != null && loopUnrollingsAtEntry.equals(loopUnrolling)) {
            return this;
        }
        return new Context(thisval, funArgs, specialRegs, loopUnrollingsAtEntry, loopUnrollingsAtEntry);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Context))
            return false;
        Context c = (Context) obj;
        if (this.hashcode != c.hashcode) {
            return false;
        }
        if ((thisval == null) != (c.thisval == null))
            return false;
        if (thisval != null && !thisval.equals(c.thisval)) // using collection equality
            return false;
        if ((funArgs == null) != (c.funArgs == null))
            return false;
        if (funArgs != null && !funArgs.equals(c.funArgs)) // using collection equality
            return false;
        if ((specialRegs == null) != (c.specialRegs == null))
            return false;
        if (specialRegs != null && !specialRegs.equals(c.specialRegs))
            return false;
        if ((loopUnrolling == null) != (c.loopUnrolling == null))
            return false;
        return !(loopUnrolling != null && !loopUnrolling.equals(c.loopUnrolling));
    }

    @Override
    public int hashCode() {
        return this.hashcode;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{");
        boolean any = false;
        if (thisval != null) {
            s.append("this=").append(thisval);
            any = true;
        }
        if (funArgs != null) {
            if (any)
                s.append(", ");
            s.append("funArgs=").append(funArgs);
            any = true;
        }
        if (specialRegs != null) {
            if (any)
                s.append(", ");
            s.append("specialRegs=").append(specialRegs);
            any = true;
        }
        if (loopUnrolling != null) {
            if (any)
                s.append(", ");
            s.append("loopUnrolling=").append(loopUnrolling);
            //any = true;
        }
        s.append("}");
        return s.toString();
    }
}
