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

import java.util.Map;

/**
 * Heap context for context sensitive analysis.
 * Immutable.
 */
public final class HeapContext {

    /**
     * Values of special variables at function entry in the context where the object was created, or null if none.
     */
    private final ContextArguments funargs;

    /**
     * Values used to differentiate objects from the same allocation site which are created with ConcreteSemantics.
     */
    private final Map<String, Value> concreteSemanticValueQualifiers;

    /**
     * Cached hashcode for immutable instance.
     */
    private final int hashcode;

    /**
     * Constructs a new heap context object.
     */
    public HeapContext(ContextArguments funargs, Map<String, Value> concreteSemanticValueQualifiers) {
        this.funargs = funargs;
        this.concreteSemanticValueQualifiers = concreteSemanticValueQualifiers;
        int hashcode = 1;
        hashcode = 31 * hashcode + (funargs != null ? funargs.hashCode() : 0);
        hashcode = 31 * hashcode + (concreteSemanticValueQualifiers != null ? concreteSemanticValueQualifiers.hashCode() : 0);
        this.hashcode = hashcode;
    }

    /**
     * Returns the values for the context arguments of this heap context, or null if none.
     */
    public ContextArguments getFunctionArguments() {
        return funargs;
    }

    /**
     * Produces a string representation of this heap context.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (funargs != null)
            b.append(funargs);
        if (concreteSemanticValueQualifiers != null)
            b.append(concreteSemanticValueQualifiers);
        return b.toString();
    }

    /**
     * Checks whether the given heap context is equal to this one.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof HeapContext))
            return false;
        HeapContext x = (HeapContext) obj;
        if (this.hashcode != x.hashcode) {
            return false;
        }
        if ((funargs == null) != (x.funargs == null))
            return false;
        if (funargs != null && !funargs.equals(x.funargs)) // using collection equality
            return false;
        if ((concreteSemanticValueQualifiers == null) != (x.concreteSemanticValueQualifiers == null))
            return false;
        return !(concreteSemanticValueQualifiers != null && !concreteSemanticValueQualifiers.equals(x.concreteSemanticValueQualifiers));
    }

    @Override
    public int hashCode() {
        return hashcode;
    }
}
