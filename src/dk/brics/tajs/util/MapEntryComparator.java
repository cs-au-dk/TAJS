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

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Comparator for map entries using the natural order of the entry keys.
 */
public class MapEntryComparator<K extends Comparable<K>, V> implements Comparator<Map.Entry<K, V>>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Entry<K, V> e1, Entry<K, V> e2) {
        return e1.getKey().compareTo(e2.getKey());
    }
}

