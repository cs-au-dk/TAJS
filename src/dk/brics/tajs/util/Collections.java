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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import dk.brics.tajs.options.Options;

/**
 * Miscellaneous collection construction methods.
 * If {@link Options#isTestEnabled()} is enabled, the methods return collections with predictable iteration order.
 */
public class Collections {
	
	private Collections() {}
	
	/**
	 * Constructs a new empty map.
	 */
	public static <T1,T2> Map<T1,T2> newMap() {
		if (Options.isTestEnabled()) 
			return new LinkedHashMap<>();
		else if (!Options.isHybridCollectionsDisabled())
			return new HybridArrayHashMap<>();
		else
			return new HashMap<>(8);
	}

	/**
	 * Constructs a new map as a copy of the given map.
	 */
	public static <T1,T2> Map<T1,T2> newMap(Map<T1,T2> m) {
		if (Options.isTestEnabled()) 
			return new LinkedHashMap<>(m);
		else if (!Options.isHybridCollectionsDisabled())
			return new HybridArrayHashMap<>(m);
		else 
			return new HashMap<>(m);
	}

	/**
	 * Adds an element to a map of lists. Creates a new list for the key if it does not already exist in the map.
	 */
	public static <T1,T2> void addToMapList(Map<T1, List<T2>> map, T1 key, T2 value) {
		List<T2> list = map.get(key);
		if (list == null) {
			list = newList();
			map.put(key, list);
		}
		list.add(value);
	}

	/**
	 * Adds an element to a map of sets. Creates a new set for the key if it does not already exist in the map.
	 */
	public static <T1,T2> void addToMapSet(Map<T1, Set<T2>> map, T1 key, T2 value) {
		Set<T2> set = map.get(key);
		if (set == null) {
			set = newSet();
			map.put(key, set);
		}
		set.add(value);
	}

	/**
	 * Adds elements to a map of sets. Creates a new set for the key if it does not already exist in the map.
	 */
	public static <T1,T2> void addAllToMapSet(Map<T1, Set<T2>> map, T1 key, Collection<T2> values) {
		Set<T2> set = map.get(key);
		if (set == null) {
			set = newSet();
			map.put(key, set);
		}
		set.addAll(values);
	}

	/**
	 * Constructs a new map as a copy of the given map (with copies of its values which are sets)
	 */
	public static <T1,T2> Map<T1,Set<T2>> newMapSet(Map<T1,Set<T2>> m) {
        Map<T1, Set<T2>> result;
		if (Options.isTestEnabled()) {
			result = new LinkedHashMap<>();
        } else if (!Options.isHybridCollectionsDisabled()) {
			result = new HybridArrayHashMap<>();
        } else {
			result = new HashMap<>();
        }
        for (Map.Entry<T1, Set<T2>> e : m.entrySet()) {
            result.put(e.getKey(), newSet(e.getValue()));
        }
        return result;
	}

	/**
	 * Constructs a new map as a copy of the given map (with copies of its values which are map sets)
	 */
	public static <T1,T2, T3> Map<T1,Map<T2, Set<T3>>> newMapMapSet(Map<T1,Map<T2, Set<T3>>> m) {
        Map<T1, Map<T2, Set<T3>>> result;
		if (Options.isTestEnabled()) {
			result = new LinkedHashMap<>();
        } else if (!Options.isHybridCollectionsDisabled()) {
			result = new HybridArrayHashMap<>();
        } else {
			result = new HashMap<>();
        }
        for (Map.Entry<T1, Map<T2, Set<T3>>> e : m.entrySet()) {
            result.put(e.getKey(), newMapSet(e.getValue()));
        }
        return result;
	}

	/**
	 * Constructs a new mutable singleton set containing the given element.
	 * Note that the set is mutable (created by {@link #newSet()}), unlike java.util.Collections.singleton.
	 */
    public static <T> Set<T> singleton(T t) {
        Set<T> set = newSet();
        set.add(t);
        return set;
    }

	/**
	 * Constructs a new empty set.
	 */
	public static <T> Set<T> newSet() {
		if (Options.isTestEnabled()) 
			return new LinkedHashSet<>();
		else if (!Options.isHybridCollectionsDisabled())
			return new HybridArrayHashSet<>();
		else
			return new HashSet<>(8);
	}
	
	/**
	 * Constructs a new set from the given collection.
	 */
	public static <T> Set<T> newSet(Collection<T> s) {
		if (Options.isTestEnabled()) 
			return new LinkedHashSet<>(s);
		else if (!Options.isHybridCollectionsDisabled())
			return new HybridArrayHashSet<>(s);
		else
			return new HashSet<>(s);
	}
	
	/**
	 * Constructs a new empty list.
	 */
	public static <T> List<T> newList() {
		return new ArrayList<>();
	}
	
	/**
	 * Constructs a new list from the given collection.
	 */
	public static <T> List<T> newList(Collection<T> s) {
		return new ArrayList<>(s);
	}

    /**
     * Constructs a new empty queue.
     */
    public static <T> Queue<T> newQueue() {
        return new LinkedList<>();
    }

	/**
	 * Returns a string description of the differences between the two maps.
	 * Only the first 10 differences are reported.
	 */
	public static <K,V> String diff(Map<K,V> m1, Map<K,V> m2) {
		int count = 0;
		final int MAX = 10;
		StringBuilder b = new StringBuilder();
		for (Map.Entry<K,V> me : m1.entrySet()) {
			V v = m2.get(me.getKey());
			if (v == null) {
				b.append("Only in first map: ").append(me.getKey()).append("\n");
				if (count++ >= MAX)
					break;
			} else if (!v.equals(me.getValue())) {
				b.append("Values differ for ").append(me.getKey()).append(": ").append(me.getValue()).append(" vs. ").append(v).append("\n");
				if (count++ >= MAX)
					break;
			}
		}
		if (count < MAX)
			for (Map.Entry<K,V> me : m2.entrySet()) {
				V v = m1.get(me.getKey());
				if (v == null) {
					b.append("Only in second map: ").append(me.getKey()).append("\n");
					if (count++ >= MAX)
						break;
				}
			}
		return b.toString();
	}
	
	/**
	 * Returns an ordered set of map entries, sorted by the natural order of the entry keys.
	 */
	public static <K extends Comparable<K>,V>TreeSet<Map.Entry<K,V>> sortedEntries(Map<K,V> m) {
		TreeSet<Map.Entry<K,V>> s = new TreeSet<>(new MapEntryComparator<K,V>());
		for (Map.Entry<K,V> me : m.entrySet())
			s.add(new MapEntry<>(me.getKey(), me.getValue()));
		return s;
	}
}
