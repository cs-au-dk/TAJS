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

package dk.brics.tajs.monitoring;

import dk.brics.tajs.flowgraph.AbstractNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Read/write information for the properties of an abstract object.
 */
class ObjReadsWrites {

    /**
     * Read status of a property in the abstract object.
     */
    public enum R_Status {
        NOT_READ, MAYBE_READ, READ
    }

    /**
     * Write status of a property in the abstract object.
     */
    public enum W_Status {
        NOT_WRITTEN, MAYBE_WRITTEN, WRITTEN
    }

    /**
     * Map describing which nodes write to which properties.
     */
    private Map<String, Set<AbstractNode>> definite_write_nodes;

    /**
     * Set of nodes where a default write occurs.
     */
    private Set<AbstractNode> unknown_write_nodes;

    /**
     * Read and write status of each property.
     */
    private Map<String, R_Status> reads;

    /**
     * Read and write status of each property.
     */
    private Map<String, W_Status> writes;

    /**
     * Indicates if a read from an unknown property has been done.
     */
    private boolean unknown_read = false;

    /**
     * Indicates if a write to an unknown property has been done.
     */
    private boolean unknown_written = false;

    @Override
    public String toString() {
        return "ObjReadsWrites [reads=" + reads + ", writes=" + writes + ", unknown_read=" + unknown_read + ", unknown_written=" + unknown_written + "]";
    }

    /**
     * Creates a new empty read/write information object.
     */
    public ObjReadsWrites() {
        reads = newMap();
        writes = newMap();
        definite_write_nodes = newMap();
        unknown_write_nodes = newSet();
    }

    /**
     * Indicates that the given property has definitely been read.
     */
    public void readDefinite(String prop) {
        reads.put(prop, R_Status.READ);
    }

    /**
     * Indicate that the given property has maybe been read.
     */
    public void readMaybe(String prop) {
        R_Status r = reads.get(prop);
        if (r != null && r == R_Status.READ) {
            return;
        }
        reads.put(prop, R_Status.MAYBE_READ);
    }

    /**
     * Indicates that the given property has definitely been read.
     */
    public void writeDefinite(String prop, AbstractNode write_node) {
        writes.put(prop, W_Status.WRITTEN);
        addToMapSet(definite_write_nodes, prop, write_node);
    }

    /**
     * Indicates that the given property has maybe been read.
     */
    public void writeMaybe(String prop) {
        W_Status r = writes.get(prop);
        if (r != null && r == W_Status.WRITTEN) {
            return;
        }
        writes.put(prop, W_Status.MAYBE_WRITTEN);
    }

    /**
     * Indicates that some unknown property maybe has been read.
     */
    public void readUnknown() {
        for (Entry<String, R_Status> g : new HashMap<>(reads).entrySet()) {
            if (g.getValue() == R_Status.NOT_READ) {
                reads.put(g.getKey(), R_Status.MAYBE_READ);
            }
        }
        unknown_read = true;
    }

    /**
     * Indicates that some unknown property has been written at the given node.
     */
    public void writeUnknown(AbstractNode write_node) {
        for (Entry<String, W_Status> g : new HashMap<>(writes).entrySet()) {
            if (g.getValue() == W_Status.NOT_WRITTEN) {
                writes.put(g.getKey(), W_Status.MAYBE_WRITTEN);
            }
        }
        unknown_written = true;
        unknown_write_nodes.add(write_node);
    }

    /**
     * Returns the read status of the given property.
     */
    public R_Status getReadStatus(String prop) {
        R_Status r = reads.get(prop);
        r = r == null ? R_Status.NOT_READ : r;
        if (r == R_Status.NOT_READ && unknown_read) {
            r = R_Status.MAYBE_READ;
        }
        return r;
    }

    /**
     * Returns the write status of the given property.
     */
    public W_Status getWriteStatus(String prop) {
        W_Status w = writes.get(prop);
        w = w == null ? W_Status.NOT_WRITTEN : w;
        if (w == W_Status.NOT_WRITTEN && unknown_written) {
            w = W_Status.MAYBE_WRITTEN;
        }
        return w;
    }

    /**
     * Returns the set of locations where the given property is definitely written.
     */
    public Set<AbstractNode> getDefiniteWriteLocations(String prop) {
        if (definite_write_nodes.containsKey(prop)) {
            return Collections.unmodifiableSet(definite_write_nodes.get(prop));
        }
        return Collections.emptySet();
    }

    /**
     * Get all properties that are read or written (definitely/maybe),
     * ignoring unknown reads/writes.
     */
    public Set<String> getProperties() {
        Set<String> res = newSet();
        res.addAll(reads.keySet());
        res.addAll(writes.keySet());
        return Collections.unmodifiableSet(res);
    }

    /**
     * Checks whether some property is maybe read.
     */
    public boolean isSomePropertyRead() {
        if (unknown_read)
            return true;
        for (R_Status r : reads.values())
            if (r != R_Status.NOT_READ)
                return true;
        return false;
    }

    /**
     * Checks whether an unknown property has been written.
     */
    public boolean isUnknownWritten() {
        return unknown_written;
    }

    /**
     * Returns the set of nodes where an unknown write may have occurred.
     */
    public Set<AbstractNode> getDefaultWriteLocations() {
        return unknown_write_nodes;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (unknown_read ? 1231 : 1237);
        result = prime * result + (unknown_written ? 1231 : 1237);
        result = prime * result + ((reads == null) ? 0 : reads.hashCode());
        result = prime * result + ((definite_write_nodes == null) ? 0 : definite_write_nodes.hashCode());
        result = prime * result + ((unknown_write_nodes == null) ? 0 : unknown_write_nodes.hashCode());
        result = prime * result + ((writes == null) ? 0 : writes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ObjReadsWrites other = (ObjReadsWrites) obj;
        if (unknown_read != other.unknown_read) {
            return false;
        }
        if (unknown_written != other.unknown_written) {
            return false;
        }
        if (reads == null) {
            if (other.reads != null) {
                return false;
            }
        } else if (!reads.equals(other.reads)) {
            return false;
        }
        if (definite_write_nodes == null) {
            if (other.definite_write_nodes != null) {
                return false;
            }
        } else if (!definite_write_nodes.equals(other.definite_write_nodes)) {
            return false;
        }
        if (unknown_write_nodes == null) {
            if (other.unknown_write_nodes != null)
                return false;
        } else if (!unknown_write_nodes.equals(other.unknown_write_nodes)) {
            return false;
        }
        if (writes == null) {
            if (other.writes != null) {
                return false;
            }
        } else if (!writes.equals(other.writes)) {
            return false;
        }
        return true;
    }
}