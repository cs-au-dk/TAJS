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

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * A canonicalizer based on object equality and weak references.
 */
public class Canonicalizer {

    private static Canonicalizer instance;

    private final Map<DeepImmutable, WeakReference<DeepImmutable>> canonicalInstances = newMap();

    private int cacheHits = 0;

    private int cacheMisses = 0;

    private boolean canonicalizing;

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

    /**
     * Checks whether an object is currently being canonicalized.
     */
    public boolean isCanonicalizing() {
        return canonicalizing;
    }

    /**
     * Canonicalizes the given instance.
     */
    @SuppressWarnings("unchecked")
    public <T extends DeepImmutable> T canonicalize(T instance) {
        if (canonicalizing)
            throw new AnalysisException("Already canonicalizing!");
        canonicalizing = true;
        WeakReference<T> canonical = (WeakReference<T>) canonicalInstances.get(instance);
        if (canonical == null || canonical.get() == null) {
            canonicalInstances.put(instance, new WeakReference<>(instance));
            canonicalizing = false;
            cacheMisses++;
            return instance;
        } else {
            canonicalizing = false;
            cacheHits++;
            return canonical.get();
        }
    }

    /**
     * Canonicalizes a set into an immutable version.
     */
    public <T extends DeepImmutable> Set<T> canonicalizeSet(Set<T> set) {
        return canonicalizeViaImmutableBox(set);
    }

    /**
     * Canonicalizes a map into an immutable version.
     */
    public <K, V extends DeepImmutable> Map<K, V> canonicalizeMap(Map<K, V> map) {
        if (map == null || map.isEmpty())
            return null;

        return canonicalizeViaImmutableBox(map);
    }

    /**
     * Canonicalizes a set of strings into an immutable version.
     */
    public Set<String> canonicalizeStringSet(Set<String> strings) {
        return canonicalizeViaImmutableBox(strings);
    }

    /**
     * Canonicalizes an Obj into an immutable version.
     */
    public <T> T canonicalizeViaImmutableBox(T obj) {
        return canonicalize(new ImmutableBox<>(obj)).get();
    }

    private <T> Set<T> canonicalizeViaImmutableBox(Set<T> set) {
        return canonicalize(new ImmutableBox<>(java.util.Collections.unmodifiableSet(set))).get();
    }

    private <K, V> Map<K, V> canonicalizeViaImmutableBox(Map<K, V> map) {
        return canonicalize(new ImmutableBox<>(java.util.Collections.unmodifiableMap(map))).get();
    }

    /**
     * Immutable-type wrapper for mutable objects. The wrapped object must be immutable in practice!
     */
    private static class ImmutableBox<T> implements DeepImmutable {

        private final T element;

        private final int hashCode;

        public ImmutableBox(T element) {
            this.element = element;
            this.hashCode = element.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ImmutableBox<?> that = (ImmutableBox<?>) o;
            return Objects.equals(element, that.element);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        public T get() {
            return element;
        }
    }
}
