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

package dk.brics.tajs.lattice;

import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.DeepImmutable;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Heap context for context sensitive analysis.
 * Immutable.
 */
public final class HeapContext implements DeepImmutable {

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
    private HeapContext(ContextArguments funargs, Map<String, Value> concreteSemanticValueQualifiers) {
        this.funargs = funargs;
        this.concreteSemanticValueQualifiers = concreteSemanticValueQualifiers;
        int hashcode = 1;
        hashcode = 31 * hashcode + (funargs != null ? funargs.hashCode() : 0);
        hashcode = 31 * hashcode + (concreteSemanticValueQualifiers != null ? concreteSemanticValueQualifiers.hashCode() : 0);
        this.hashcode = hashcode;
    }

    /**
     * Constructs a new heap context object.
     */
    public static HeapContext make(ContextArguments funargs, Map<String, Value> concreteSemanticValueQualifiers) {
        HeapContext instance = new HeapContext(funargs, concreteSemanticValueQualifiers);
        return Canonicalizer.get().canonicalize(instance);
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

    /**
     * Utility function for extracting object labels
     */
    public static Set<ObjectLabel> extractTopLevelObjectLabels(HeapContext context) { // TODO: review (+used where?)
        if (context == null) {
            return newSet();
        }
        return Stream.concat(
                context.concreteSemanticValueQualifiers != null ?
                        context.concreteSemanticValueQualifiers.values().stream().flatMap(v -> v.getObjectLabels().stream()) :
                        Stream.empty(),
                ContextArguments.extractTopLevelObjectLabels(context.getFunctionArguments()).stream())
                .collect(Collectors.toSet());
    }

}
