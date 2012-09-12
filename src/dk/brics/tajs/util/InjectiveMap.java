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

package dk.brics.tajs.util;

import static dk.brics.tajs.util.Collections.newMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Injective map with efficient operations for reverse lookup.
 */
public class InjectiveMap<K,V> implements Map<K,V> {
	
	private Map<K,V> forward;

	private Map<V,K> backward;
	
	/**
	 * Constructs a new empty map.
	 */
	public InjectiveMap() {
		forward = newMap();
		backward = newMap();
	}

	@Override
	public int size() {
		return forward.size();
	}

	@Override
	public boolean isEmpty() {
		return forward.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return forward.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return backward.containsKey(value);
	}

	@Override
	public V get(Object key) {
		return forward.get(key);
	}

	/**
	 * Returns the key that maps to the specified value, or null if this map contains no mapping for the value. 
	 */
	public K getInverse(Object value) {
		return backward.get(value);
	}
	
	/**
	 * Returns an immutable view of the forward map.
	 */
	public Map<K,V> getForwardMap() {
		return java.util.Collections.unmodifiableMap(forward);
	}

	/**
	 * Returns an immutable view of the backward map.
	 */
	public Map<V,K> getBackwardMap() {
		return java.util.Collections.unmodifiableMap(backward);
	}

	@Override
	public V put(K key, V value) {
		V old_value = forward.put(key, value);
		if (old_value != null && !old_value.equals(value))
			throw new java.lang.IllegalArgumentException("The key already maps to a different value");
		K old_key = backward.put(value, key);
		if (old_key != null && !old_key.equals(key))
			throw new java.lang.IllegalArgumentException("A different key already maps to the value");
		return old_value;
	}

	@Override
	public V remove(Object key) {
		V old_value = forward.remove(key);
		if (old_value != null)
			backward.remove(old_value);
		return old_value;
	}

	/**
	 * Removes the mapping for each of the given keys.
	 */
	public void removeAll(Collection<? extends K> keys) {
		for (Object k : keys)
			remove(k);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Map.Entry<? extends K,? extends V> me : m.entrySet()) {
			forward.put(me.getKey(), me.getValue());
			backward.put(me.getValue(), me.getKey());
		}
	}

	@Override
	public void clear() {
		forward.clear();
		backward.clear();
	}

	@Override
	public Set<K> keySet() {
		return forward.keySet();
	}

	@Override
	public Collection<V> values() {
		return forward.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return forward.entrySet();
	}
	
	@Override
	public int hashCode() {
		return forward.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof InjectiveMap<?,?>))
			return false;
		return forward.equals(((InjectiveMap<?,?>)obj).forward);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append('[');
		boolean first = true;
		for (Map.Entry<K,V> me : forward.entrySet()) {
			if (first)
				first = false;
			else
				b.append(',');
			b.append(me.getKey()).append("->").append(me.getValue());
		}
		b.append(']');
		return b.toString();
	}
	
	/**
	 * Returns a string description of this map, excluding identity items.
	 */
	public String toStringNonIdentityItems() {
		StringBuilder b = new StringBuilder();
		b.append('[');
		boolean first = true;
		for (Map.Entry<K,V> me : forward.entrySet())
			if (!me.getKey().equals(me.getValue())) {
				if (first)
					first = false;
				else
					b.append(',');
				b.append(me.getKey()).append("->").append(me.getValue());
			}
		b.append(']');
		return b.toString();
	}
}
