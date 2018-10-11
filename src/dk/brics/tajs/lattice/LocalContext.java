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

package dk.brics.tajs.lattice;

import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.DeepImmutable;

import java.util.Collections;
import java.util.Map;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * Local context.
 * Represents number of times loops have been unrolled, variable splittings, etc.
 */
public class LocalContext implements DeepImmutable {

    private final Map<Qualifier, Value> qualifiers;

    private final int hashCode;

    private LocalContext(Map<Qualifier, Value> qualifiers) {
        this.qualifiers = newMap(qualifiers);
        this.hashCode = this.qualifiers.hashCode();
    }

    /**
     * Provides a local context with the given qualifiers.
     */
    public static LocalContext make(Map<Qualifier, Value> qualifiers) {
        return Canonicalizer.get().canonicalize(new LocalContext(qualifiers));
    }

    @Override
    public String toString() {
        return qualifiers.toString();
    }

    public Map<Qualifier, Value> getQualifiers() {
        return Collections.unmodifiableMap(qualifiers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalContext that = (LocalContext) o;

        return qualifiers.equals(that.qualifiers);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
     * Qualifier for local contexts. Implementations should implement structural equality in their {@link #equals(Object)} method.
     */
    public interface Qualifier {

    }

    /**
     * Qualifier for loop unrolling.
     */
    public static class LoopUnrollingQualifier implements Qualifier {

        private final BeginLoopNode node;

        public LoopUnrollingQualifier(BeginLoopNode node) {
            this.node = (BeginLoopNode)node.getThisOrDuplicateOf();
        }

        public static LoopUnrollingQualifier make(BeginLoopNode node) {
            return new LoopUnrollingQualifier(node);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LoopUnrollingQualifier that = (LoopUnrollingQualifier) o;

            return node.equals(that.node);
        }

        @Override
        public int hashCode() {
            return node.hashCode();
        }

        @Override
        public String toString() {
            return "loopUnrolling:" + node.getIndex();
        }
    }
}
