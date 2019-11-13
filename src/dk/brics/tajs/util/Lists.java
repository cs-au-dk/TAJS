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

package dk.brics.tajs.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains convenient methods for List operations.
 */
public final class Lists {

    /**
     * Returns the first item in the given list, or null if not found.
     *
     * @param <T> The generic list type.
     * @param list The list that may have a first item.
     *
     * @return null if the list is null or there is no first item.
     */
    public static <T> T getFirst(final List<T> list) {
        return getFirst(list, null);
    }

    /**
     * Returns the last item in the given list, or null if not found.
     *
     * @param <T> The generic list type.
     * @param list The list that may have a last item.
     *
     * @return null if the list is null or there is no last item.
     */
    public static <T> T getLast(final List<T> list) {
        return getLast(list, null);
    }

    /**
     * Returns the first item in the given list, or t if not found.
     *
     * @param <T> The generic list type.
     * @param list The list that may have a first item.
     * @param defaultValue The default return value.
     *
     * @return null if the list is null or there is no first item.
     */
    public static <T> T getFirst(final List<T> list, final T defaultValue) {
        return isEmpty(list) ? defaultValue : list.get(0);
    }

    /**
     * Returns the last item in the given list, or t if not found.
     *
     * @param <T> The generic list type.
     * @param list The list that may have a last item.
     * @param defaultValue The default return value.
     *
     * @return null if the list is null or there is no last item.
     */
    public static <T> T getLast(final List<T> list, final T defaultValue) {
        return isEmpty(list) ? defaultValue : list.get(list.size() - 1);
    }

    /**
     * Returns true if the given list is null or empty.
     *
     * @param <T> The generic list type.
     * @param list The list that has a last item.
     *
     * @return true The list is empty.
     */
    public static <T> boolean isEmpty(final List<T> list) {
        return list == null || list.isEmpty();
    }

    /**
     * Returns a new list that contains the concatenation of the given lists.
     */
    @SafeVarargs
    public static <T> List<T> concat(List<T> ...lists) {
        List<T> res = new ArrayList<>();
        for (List<T> list : lists)
            res.addAll(list);
        return res;
    }
}
