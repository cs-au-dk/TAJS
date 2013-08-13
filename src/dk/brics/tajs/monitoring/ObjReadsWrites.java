/*
 * Copyright 2009-2013 Aarhus University
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

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dk.brics.tajs.flowgraph.AbstractNode;

/**
 * Read/write information for the properties of an abstract object.
 */
class ObjReadsWrites { // TODO: update javadoc

	/**
	 * Read status of a property in the abstract object.
	 */
	public enum R_Status {NOT_READ, MAYBE_READ, READ};

	/**
	 * Write status of a property in the abstract object.
	 */
	public enum W_Status {NOT_WRITTEN, MAYBE_WRITTEN, WRITTEN};

	/**
	 * Map describing which nodes write to which properties.
	 */
	private Map<String, Set<AbstractNode>> definite_write_nodes;
	
	/**
	 * Set of nodes where a default write occurs.
	 */
	private Set<AbstractNode> default_write_nodes;

	/**
	 * Read and write status of each property.
	 */
	private Map<String, ObjReadsWrites.R_Status> reads;

	/**
	 * Read and write status of each property.
	 */
	private Map<String, ObjReadsWrites.W_Status> writes;

	/**
	 * Indicates if a read to the default property has been done.
	 */
	boolean default_read = false;

	/**
	 * Indicates if a write to the default property has been done.
	 */
	boolean default_written = false;

	@Override
	public String toString() {
		return "ObjReadsWrites [reads=" + reads + ", writes=" + writes
				+ ", default_read=" + default_read + ", default_written="
				+ default_written + "]";
	}

	/**
	 * Creates a new empty read/write information object.
	 */
	public ObjReadsWrites() {
		reads = newMap();
		writes = newMap();
		definite_write_nodes = newMap();
		default_write_nodes = newSet();
	}

	/**
	 * Indicate that prop definitely read by read_node
	 */
	public void readDefinite(String prop) { 
		reads.put(prop, R_Status.READ);
	}

	/**
	 * Indicate that prop maybe read by read_node.
	 */		
	public void readMaybe(String prop) {
		ObjReadsWrites.R_Status r = reads.get(prop);
		if (r != null && r == R_Status.READ) {
			return;
		}
		reads.put(prop, R_Status.MAYBE_READ);
	}

	/**
	 * Indicate that the property prop was definitely read by a write_node
	 */
	public void writeDefinite(String prop, AbstractNode write_node) {
		writes.put(prop, W_Status.WRITTEN);
		addToMapSet(definite_write_nodes, prop, write_node);
	}

	/**
	 * Indicate that prop is Maybe written.
	 */
	public void writeMaybe(String prop) { 
		ObjReadsWrites.W_Status r = writes.get(prop);
		if (r != null && r == W_Status.WRITTEN) {
			return;
		}
		writes.put(prop, W_Status.MAYBE_WRITTEN);
	}

	/**
	 * Indicate that a read to the default property took place.
	 */
	public void readAny() {
		for (Entry<String, ObjReadsWrites.R_Status> g : new HashMap<>(reads).entrySet()) {
			if (g.getValue() == R_Status.NOT_READ) {
				reads.put(g.getKey(), R_Status.MAYBE_READ);
			}
		}
		default_read = true;
	}

	/**
	 * Indicate that a write to the default property took place.
	 */
	public void writeAny(AbstractNode write_node) {
		for (Entry<String, ObjReadsWrites.W_Status> g : new HashMap<>(writes).entrySet()) {
			if (g.getValue() == W_Status.NOT_WRITTEN) {
				writes.put(g.getKey(), W_Status.MAYBE_WRITTEN);
			}
		}
		default_written = true;
		default_write_nodes.add(write_node);
	}

	/**
	 * Returns the read status of property p.
	 */
	public ObjReadsWrites.R_Status getReadStatus(String p) {
		ObjReadsWrites.R_Status r = reads.get(p);
		r = r == null ? R_Status.NOT_READ : r;
		if (r == R_Status.NOT_READ && default_read) {
			r = R_Status.MAYBE_READ;
		}
		return r;
	}

	/**
	 * Return the write status of property p.
	 */
	public ObjReadsWrites.W_Status getWriteStatus(String p) {
		ObjReadsWrites.W_Status w = writes.get(p);
		w = w == null ? W_Status.NOT_WRITTEN : w;
		if (w == W_Status.NOT_WRITTEN && default_written) {
			w = W_Status.MAYBE_WRITTEN;
		}
		return w;
	}

	/**
	 * Returns the set of locations where p is definitely written.
	 */
	public Set<AbstractNode> getDefiniteWriteLocations(String p) {
		if (definite_write_nodes.containsKey(p)) {
			return java.util.Collections.unmodifiableSet(definite_write_nodes.get(p));
		}
		return java.util.Collections.emptySet();
	}
	
	/**
	 * Get all properties that either read or written (including maybe read/writes)
	 */
	public Set<String> getProperties() {
		Set<String> res = newSet();
		res.addAll(reads.keySet());
		res.addAll(writes.keySet());
		return java.util.Collections.unmodifiableSet(res);
	}
	
	/**
	 * Checks whether some property is maybe read.
	 */
	public boolean isSomePropertyRead() {
		if (default_read)
			return true;
		for (R_Status r : reads.values())
			if (r != R_Status.NOT_READ)
				return true;
		return false;
	}

	/**
	 * Checks whether a default write may have occurred.
	 */
	public boolean isDefaultWritten() {
		return default_written;
	}
	
	/**
	 * Returns the set of nodes where a default write may have occurred.
	 */
	public Set<AbstractNode> getDefaultWriteLocations() {
		return default_write_nodes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (default_read ? 1231 : 1237);
		result = prime * result + (default_written ? 1231 : 1237);
		result = prime * result + ((reads == null) ? 0 : reads.hashCode());
		result = prime * result + ((definite_write_nodes == null) ? 0 : definite_write_nodes.hashCode());
		result = prime * result + ((default_write_nodes == null) ? 0 : default_write_nodes.hashCode());
		result = prime * result	+ ((writes == null) ? 0 : writes.hashCode());
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
		if (default_read != other.default_read) {
			return false;
		}
		if (default_written != other.default_written) {
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
		if (default_write_nodes == null) {
			if (other.default_write_nodes != null)
				return false;
		} else if (!default_write_nodes.equals(other.default_write_nodes)) {
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