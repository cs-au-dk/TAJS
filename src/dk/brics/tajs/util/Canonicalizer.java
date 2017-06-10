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

import java.lang.ref.WeakReference;
import java.util.Map;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * A canonicalizer based on object equality and weak references.
 */
public class Canonicalizer {

    private static Canonicalizer instance;

    private final Map<DeepImmutable, WeakReference<DeepImmutable>> canonicalInstances = newMap();

    private int cacheHits = 0;

    private int cacheMisses = 0;

    public static void reset() {
        instance = null;
    }

    public static Canonicalizer get() {
        if (instance == null) {
            instance = new Canonicalizer();
        }
        return instance;
    }

    public int getCacheHits() {
        return cacheHits;
    }

    public int getCacheMisses() {
        return cacheMisses;
    }

    @SuppressWarnings("unchecked")
    public <T extends DeepImmutable> T canonicalize(T instance) {
        WeakReference<T> canonical = (WeakReference<T>) canonicalInstances.get(instance);
        if (canonical == null || canonical.get() == null) {
            canonicalInstances.put(instance, new WeakReference<>(instance));
            cacheMisses++;
            return instance;
        }
        cacheHits++;
        return canonical.get();
    }
}
