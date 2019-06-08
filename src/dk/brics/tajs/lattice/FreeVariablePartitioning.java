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

import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.DeepImmutable;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * FreeVariablePartitioning is basically a map from variable names to a set of
 * partitioning qualifiers. It describes that it is sound to only use
 * these partitions of a variable.
 */
public class FreeVariablePartitioning implements DeepImmutable {

    /**
     * Describes which partitions is sound to use for each variable.
     */
    @Nonnull
    private final Map<String, Partitionings> freeVariablePartitioning;

    private final int hashcode;

    private FreeVariablePartitioning(Map<String, Partitionings> freeVariablePartitioning) {
        this.freeVariablePartitioning = freeVariablePartitioning;
        this.hashcode = Objects.hash(freeVariablePartitioning);
    }

    private static FreeVariablePartitioning makeAndCanonicalize(Map<String, Partitionings> partitionings) {
        Map<String, Partitionings> partitionings1 = Canonicalizer.get().canonicalizeMap(partitionings);
        return Canonicalizer.get().canonicalize(new FreeVariablePartitioning(Canonicalizer.get().canonicalizeMap(partitionings1)));
    }

    public static FreeVariablePartitioning make(Map<String, Set<PartitioningQualifier>> freeVariablePartitioning) {
        return makeAndCanonicalize(freeVariablePartitioning.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Partitionings.make(e.getValue()))));
    }

    /**
     * Creates a FreeVariablePartitioning where all varnames has the information q.
     */
    public static FreeVariablePartitioning make(Set<String> keySet, PartitioningQualifier q) {
        return make(keySet.stream().collect(Collectors.toMap(name -> name, name -> singleton(q))));
    }

    /**
     * Computes a new FreeVariablePartitioning based on two other FreeVariablePartitioning.
     */
    public FreeVariablePartitioning join(FreeVariablePartitioning other) {
        if (other == null) {
            return this;
        }
        Set<String> variableNames = newSet(freeVariablePartitioning.keySet());
        variableNames.addAll(other.freeVariablePartitioning.keySet());
        Map<String, Set<PartitioningQualifier>> newFreeVariablePartitioning =
                variableNames.stream().collect(Collectors.toMap(v -> v, v -> freeVariablePartitioning.get(v) != null ? freeVariablePartitioning.get(v).join(other.freeVariablePartitioning.get(v)) : other.freeVariablePartitioning.get(v).partitionings));
        return FreeVariablePartitioning.make(newFreeVariablePartitioning);
    }

    /**
     * Returns whether this freeVariablePartitioning has information for varname.
     */
    public boolean hasInfoForVariable(String varname) {
        return freeVariablePartitioning.containsKey(varname);
    }

    /**
     * Returns the information for the given varname.
     * Throws exception, if no value for varname is present.
     */
    public Set<PartitioningQualifier> getInfoForVariable(String varname) {
        if (!freeVariablePartitioning.containsKey(varname)) {
            throw new AnalysisException("No information about variable: " + varname);
        }
        return freeVariablePartitioning.get(varname).partitionings;
    }

    @Override
    public boolean equals(Object o) {
        if (!Canonicalizer.get().isCanonicalizing())
            return this == o;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FreeVariablePartitioning that = (FreeVariablePartitioning) o;
        return Objects.equals(freeVariablePartitioning, that.freeVariablePartitioning);
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public String toString() {
        return freeVariablePartitioning.toString();
    }

    /**
     * Wrapper around a set of partitioningQualifiers.
     */
    private static class Partitionings implements DeepImmutable {

        private final Set<PartitioningQualifier> partitionings;

        private final int hashcode;

        private Partitionings(Set<PartitioningQualifier> partitionings) {
            this.partitionings = partitionings;
            this.hashcode = Objects.hash(partitionings);
        }

        public static Partitionings make(Set<PartitioningQualifier> partitionings) {
            return Canonicalizer.get().canonicalize(new Partitionings(Canonicalizer.get().canonicalizeSet(partitionings)));
        }

        /**
         * Returns a new set of partitiongs, which is the join of two partitionings.
         */
        private Set<PartitioningQualifier> join(Partitionings other) {
            if (other == null)
                return partitionings;
            Set<PartitioningQualifier> res = newSet(partitionings);
            res.addAll(other.partitionings);
            return res;
        }

        @Override
        public boolean equals(Object o) {
            if (!Canonicalizer.get().isCanonicalizing())
                return this == o;
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Partitionings that = (Partitionings) o;
            return Objects.equals(partitionings, that.partitionings);
        }

        @Override
        public int hashCode() {
            return hashcode;
        }

        @Override
        public String toString() {
            return partitionings.toString();
        }
    }
}
