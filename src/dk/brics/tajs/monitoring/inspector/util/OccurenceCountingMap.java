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

package dk.brics.tajs.monitoring.inspector.util;

import dk.brics.tajs.monitoring.inspector.util.OccurenceCountingMap.CountingResult.ByOccurencesThenElementToStringComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Utility class for counting occurences of various objects
 */
public class OccurenceCountingMap<E> {

	public void init(E element) {
		count(element, 0);
	}

	public static class CountingResult<F> {
		public static class ByElementToStringComparator implements Comparator<CountingResult<?>> {
			@Override
			public int compare(CountingResult<?> o1, CountingResult<?> o2) {
				return o1.getElement().toString().compareTo(o2.getElement().toString());
			}
		}

		public static class ByOccurencesComparator implements Comparator<CountingResult<?>> {
			@Override
			public int compare(CountingResult<?> o1, CountingResult<?> o2) {
				return o1.getOccurences() - o2.getOccurences();
			}
		}

		public static class ByOccurencesThenElementToStringComparator implements Comparator<CountingResult<?>> {
			ByOccurencesComparator byOccurences = new ByOccurencesComparator();
			ByElementToStringComparator byElementToString = new ByElementToStringComparator();

			@Override
			public int compare(CountingResult<?> o1, CountingResult<?> o2) {
				int cmp = byOccurences.compare(o1, o2);
				if (cmp != 0)
					return cmp;
				return byElementToString.compare(o1, o2);
			}

		}

		private final F element;
		private final Integer occurences;

		public CountingResult(F element, Integer occurences) {
			this.element = element;
			this.occurences = occurences;
		}

		public F getElement() {
			return element;
		}

		public Integer getOccurences() {
			return occurences;
		}

	}

	/**
	 * Creates the positive diff of two versions of the same occurence counting.
	 *
	 * It is assumed that the second version contains all the counts of the first version, plus some more counts.
	 */
	public static <E> OccurenceCountingMap<E> makeDiff(OccurenceCountingMap<E> first, OccurenceCountingMap<E> second) {
		final OccurenceCountingMap<E> diff = new OccurenceCountingMap<>();
		for (Entry<E, Integer> entry : second.map.entrySet()) {
			final E key = entry.getKey();
			final int diffValue;
			if (first.map.containsKey(key)) {
				diffValue = entry.getValue() - first.map.get(key);
			} else {
				diffValue = entry.getValue();
			}
			if (diffValue != 0) {
				diff.map.put(key, diffValue);
			}
		}
		return diff;
	}

	public static <E> List<CountingResult<E>> sortByOccurencesThenByToString(Collection<CountingResult<E>> results) {
		List<CountingResult<E>> sorted = new ArrayList<>(results);
		sorted.sort(new ByOccurencesThenElementToStringComparator());
		return sorted;
	}

	private Map<E, Integer> map = new HashMap<>();

	public void addAll(OccurenceCountingMap<E> otherMap) {
		this.map.putAll(otherMap.map);
	}

	public OccurenceCountingMap<E> copy() {
		final OccurenceCountingMap<E> copy = new OccurenceCountingMap<>();
		copy.map = new HashMap<>();
		copy.map.putAll(map);
		return copy;
	}

	public void count(E key) {
		count(key, 1);
	}

	public void count(E key, int toCount) {
		if (key == null) {
			throw new IllegalArgumentException("null is not a supported key");
		}
		if (!map.containsKey(key)) {
			map.put(key, 0);
		}
		map.put(key, map.get(key) + toCount);
	}

	public void countAll(OccurenceCountingMap<E> all) {
		for (Entry<E, Integer> entry : all.map.entrySet()) {
			this.count(entry.getKey(), entry.getValue());
		}
	}

	public Integer getResult(E key) {
		return map.getOrDefault(key, 0);
	}

	public Collection<CountingResult<E>> getResults() {
		Collection<CountingResult<E>> results = new ArrayList<>();
		for (Entry<E, Integer> entry : map.entrySet()) {
			final E element = entry.getKey();
			final Integer occurences = entry.getValue();
			final CountingResult<E> result = new CountingResult<>(element, occurences);
			results.add(result);
		}
		return results;
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-10s%-10s%n", "key", "occurences"));
		for (CountingResult<E> result : sortByOccurencesThenByToString(getResults())) {
			sb.append(String.format("%10s %10d%n", result.getElement(), result.getOccurences()));
		}
		return sb.toString();
	}

	public Map<E, Integer> getMapView(){
		return Collections.unmodifiableMap(map);
	}
}
