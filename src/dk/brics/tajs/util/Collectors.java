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

package dk.brics.tajs.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Alternative to {@link java.util.stream.Collectors} that makes use of the data structures in {@link dk.brics.tajs.util.Collections}.
 */
public class Collectors {

    /**
     * @see java.util.stream.Collectors#toSet()
     */
    public static <T> Collector<T, ?, Set<T>> toSet() {
        return java.util.stream.Collectors.toCollection(Collections::newSet);
    }

    /**
     * @see java.util.stream.Collectors#toList()
     */
    public static <T> Collector<T, ?, List<T>> toList() {
        return java.util.stream.Collectors.toCollection(Collections::newList);
    }

    /**
     * @see java.util.stream.Collectors#toMap(Function, Function)
     */
    public static <T, K, U>
    Collector<T, ?, Map<K,U>> toMap(Function<? super T, ? extends K> keyMapper,
                                    Function<? super T, ? extends U> valueMapper) {
        return java.util.stream.Collectors.toMap(keyMapper, valueMapper, (u, v) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", u));
        }, Collectors::newMap_workaround);
    }

    /**
     * @see java.util.stream.Collectors#groupingBy(Function)
     */
    public static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(Function<? super T, ? extends K> classifier) {
        return java.util.stream.Collectors.groupingBy(classifier, Collectors::newMap_workaround, toList());
    }

    /**
     * @see java.util.stream.Collectors#groupingBy(Function, Collector)
     */
    public static <T, K, A, D>
    Collector<T, ?, Map<K, D>> groupingBy(Function<? super T, ? extends K> classifier,
                                          Collector<? super T, A, D> downstream) {
        return java.util.stream.Collectors.groupingBy(classifier, Collectors::newMap_workaround, downstream);
    }

    private static <K, V> Map<K, V> newMap_workaround() {
        // XXX replace with Collections::newMap and inline once dk.brics.tajs.test.CollectionsTypeErrorBug succeeds (GitHub #434)
        return new HashMap<>();
    }
}
