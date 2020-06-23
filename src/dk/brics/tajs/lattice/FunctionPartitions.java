/*
 * Copyright 2009-2020 Aarhus University
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

import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.DeepImmutable;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * FunctionPartitions is a set of partitions that are sound to use for partitioned variables.
 */
public class FunctionPartitions implements DeepImmutable {

    @Nonnull
    private final Set<PartitionToken.FunctionPartitionToken> partitions;

    private final int hashcode;

    private FunctionPartitions(Set<PartitionToken.FunctionPartitionToken> partitions) {
        this.partitions = partitions;
        this.hashcode = Objects.hash(partitions);
    }

    private static FunctionPartitions makeAndCanonicalize(Set<PartitionToken.FunctionPartitionToken> partitionings) {
        return Canonicalizer.get().canonicalize(new FunctionPartitions(Canonicalizer.get().canonicalizeSet(partitionings)));
    }

    public static FunctionPartitions make(PartitionToken.FunctionPartitionToken q) {
        return makeAndCanonicalize(singleton(q));
    }

    public Set<PartitionToken.FunctionPartitionToken> getPartitionings() {
        return partitions;
    }

    public FunctionPartitions join(FunctionPartitions other) {
        if (other == null) {
            return this;
        }
        Set<PartitionToken.FunctionPartitionToken> res = newSet(partitions);
        if (!res.addAll(other.partitions)) { // If no new partitionings added
            return this;
        }
        return FunctionPartitions.makeAndCanonicalize(res);
    }

    @Override
    public boolean equals(Object o) {
        if (!Canonicalizer.get().isCanonicalizing())
            return this == o;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionPartitions that = (FunctionPartitions) o;
        return Objects.equals(partitions, that.partitions);
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public String toString() {
        return partitions.toString();
    }

    /**
     * Removes partitions that are not relevant for the given function.
     */
    public FunctionPartitions filterByFunction(ObjectLabel function) {
        Set<Function> outerFunctions = newSet();
        Function fun = function.getFunction();
        while (fun != null) {
            outerFunctions.add(fun);
            fun = fun.getOuterFunction();
        }
        Set<PartitionToken.FunctionPartitionToken> partitions = this.partitions.stream().filter(q -> outerFunctions.contains(q.getNode().getBlock().getFunction())).collect(Collectors.toSet());
        if (partitions.isEmpty())
            return null;
        return FunctionPartitions.makeAndCanonicalize(partitions);
    }
}
