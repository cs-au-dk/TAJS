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
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.DeepImmutable;

import java.util.Objects;

/**
 * Qualifier for differentiating different partitions in a PartitionedValue.
 */
public class PartitioningQualifier implements DeepImmutable {

    /**
     * The node where the partitioning is introduced.
     */
    private final AbstractNode n;

    /**
     * Property name used for distinguish partitions introduced at the same location.
     * Null represents "rest" properties.
     */
    private final Property prop;

    private final int hashcode;

    /**
     * Creates the qualifier introduced for properties described by the given PKey.
     */
    private PartitioningQualifier(AbstractNode n, Property prop) {
        this.n = n;
        this.prop = prop;
        this.hashcode = Objects.hash(prop, n);
    }

    public static PartitioningQualifier make(AbstractNode n, Property prop) {
        return Canonicalizer.get().canonicalize(new PartitioningQualifier(n, prop));
    }

    public AbstractNode getNode() {
        return n;
    }

    @Override
    public boolean equals(Object o) {
        if (!Canonicalizer.get().isCanonicalizing())
            return this == o;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartitioningQualifier that = (PartitioningQualifier) o;
        return Objects.equals(prop, that.prop) &&
                Objects.equals(n, that.n);
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public String toString() {
        return "(node" + n.getIndex() + "," + prop + ")";
    }

    public Property getProperty() {
        return prop;
    }
}
