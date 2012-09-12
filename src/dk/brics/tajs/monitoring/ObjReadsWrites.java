/*
 * Copyright 2012 Aarhus University
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

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dk.brics.tajs.flowgraph.AbstractNode;

/**
 * Reads and writes for an abstract object.
 */
class ObjReadsWrites { // XXX: javadoc

	public enum R_Status {NOT_READ, MAYBE_READ, READ};
	public enum W_Status {NOT_WRITTEN, MAYBE_WRITTEN, WRITTEN};

	private Map<String, Set<AbstractNode>> write_nodes;

	private Map<String, ObjReadsWrites.R_Status> reads;
	private Map<String, ObjReadsWrites.W_Status> writes;

	boolean default_read = false;
	boolean default_written = false;

	@Override
	public String toString() {
		return "RWInfo [reads=" + reads + ", writes=" + writes
				+ ", default_read=" + default_read + ", default_written="
				+ default_written + "]";
	}

	public ObjReadsWrites() {
		reads = newMap();
		writes = newMap();
		write_nodes = newMap();
	}

	public void readDefinite(String prop, AbstractNode read_node) { // TODO: parameter read_node unused
		reads.put(prop, R_Status.READ);
	}

	public void readMaybe(String prop, AbstractNode read_node) { // TODO: parameter read_node unused
		ObjReadsWrites.R_Status r = reads.get(prop);
		if (r != null && r == R_Status.READ) {
			return;
		}
		reads.put(prop, R_Status.MAYBE_READ);
	}

	public void writeDefinite(String prop, AbstractNode write_node) {
		writes.put(prop, W_Status.WRITTEN);
		Set<AbstractNode> ns = write_nodes.get(prop);
		if (ns == null) {
			ns = newSet();
			write_nodes.put(prop, ns);
		}
		ns.add(write_node);		
	}

	public void writeMaybe(String prop, AbstractNode write_node) { // TODO: parameter write_node unused
		ObjReadsWrites.W_Status r = writes.get(prop);
		if (r != null && r == W_Status.WRITTEN) {
			return;
		}
		writes.put(prop, W_Status.MAYBE_WRITTEN);
	}

	public void readAny() {
		for (Entry<String, ObjReadsWrites.R_Status> g : new HashMap<>(reads).entrySet()) {
			if (g.getValue() == R_Status.NOT_READ) {
				reads.put(g.getKey(), R_Status.MAYBE_READ);
			}
		}
		default_read = true;
	}

	public void writeAny() {
		for (Entry<String, ObjReadsWrites.W_Status> g : new HashMap<>(writes).entrySet()) {
			if (g.getValue() == W_Status.NOT_WRITTEN) {
				writes.put(g.getKey(), W_Status.MAYBE_WRITTEN);
			}
		}
		default_written = true;
	}

	public ObjReadsWrites.R_Status getReadStatus(String p) {
		ObjReadsWrites.R_Status r = reads.get(p);
		r = r == null ? R_Status.NOT_READ : r;
		if (r == R_Status.NOT_READ && default_read) {
			r = R_Status.MAYBE_READ;
		}
		return r;
	}

	public ObjReadsWrites.W_Status getWriteStatus(String p) {
		ObjReadsWrites.W_Status w = writes.get(p);
		w = w == null ? W_Status.NOT_WRITTEN : w;
		if (w == W_Status.NOT_WRITTEN && default_written) {
			w = W_Status.MAYBE_WRITTEN;
		}
		return w;
	}

	public Set<AbstractNode> getWriteLocations(String p) {
		if (write_nodes.containsKey(p)) {
			return java.util.Collections.unmodifiableSet(write_nodes.get(p));
		}
		return java.util.Collections.emptySet();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (default_read ? 1231 : 1237);
		result = prime * result + (default_written ? 1231 : 1237);
		result = prime * result + ((reads == null) ? 0 : reads.hashCode());
		result = prime * result
				+ ((write_nodes == null) ? 0 : write_nodes.hashCode());
		result = prime * result
				+ ((writes == null) ? 0 : writes.hashCode());
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
		if (write_nodes == null) {
			if (other.write_nodes != null) {
				return false;
			}
		} else if (!write_nodes.equals(other.write_nodes)) {
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

	public Set<String> getProperties() {
		Set<String> res = newSet();
		res.addAll(reads.keySet());
		res.addAll(writes.keySet());
		return java.util.Collections.unmodifiableSet(res);
	}
}