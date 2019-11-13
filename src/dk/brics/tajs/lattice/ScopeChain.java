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

package dk.brics.tajs.lattice;

import dk.brics.tajs.util.Collections;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Abstract scope chain.
 * Immutable.
 */
public final class ScopeChain {

    private Set<ObjectLabel> obj; // TODO: canonicalize? (#140)

    private ScopeChain next;

    private int hashcode;

    private static Map<ScopeChain, WeakReference<ScopeChain>> cache = new WeakHashMap<>();

    private static int cache_hits;

    private static int cache_misses;

    /**
     * Clears the canonicalization cache.
     */
    public static void clearCache() {
        cache.clear();
    }

    /**
     * Returns the canonicalization cache size.
     */
    public static int getCacheSize() {
        return cache.size();
    }

    /**
     * Returns the number of cache misses.
     */
    public static int getNumberOfCacheMisses() {
        return cache_misses;
    }

    /**
     * Returns the number of cache hits.
     */
    public static int getNumberOfCacheHits() {
        return cache_hits;
    }

    /**
     * Resets the cache numbers.
     */
    public static void reset() {
        cache_hits = 0;
        cache_misses = 0;
        clearCache();
    }

    /**
     * Creates a new scope chain.
     */
    private ScopeChain(Set<ObjectLabel> obj, ScopeChain next) {
        this.obj = obj;
        this.next = next;
    }

    /**
     * Creates a scope chain.
     * This object gets ownership of the set.
     */
    public static ScopeChain make(Set<ObjectLabel> obj, ScopeChain next) {
        return canonicalize(new ScopeChain(obj, next));
    }

    /**
     * Creates a scope chain with a single element.
     */
    public static ScopeChain make(ObjectLabel obj) {
        return make(Collections.singleton(obj), null);
    }

    private static ScopeChain canonicalize(ScopeChain e) {
        WeakReference<ScopeChain> ref = cache.get(e);
        ScopeChain c = ref != null ? ref.get() : null;
        if (c == null) {
            cache.put(e, new WeakReference<>(e));
            cache_misses++;
        } else {
            e = c;
            cache_hits++;
        }
        return e;
    }

    /**
     * Returns the tail of this scope chain, or null if none.
     */
    public ScopeChain next() {
        return next;
    }

    /**
     * Returns the top-most object in this scope chain.
     */
    public Set<ObjectLabel> getObject() {
        return obj;
    }

    /**
     * Replaces all occurrences of oldlabel by newlabel.
     */
    public static ScopeChain replaceObjectLabel(ScopeChain sc, ObjectLabel oldlabel, ObjectLabel newlabel, Map<ScopeChain, ScopeChain> replace_cache) {
        if (sc == null)
            return null;
        ScopeChain c = replace_cache.get(sc);
        if (c == null) {
            Set<ObjectLabel> newobj;
            if (sc.obj.contains(oldlabel)) {
                newobj = newSet(sc.obj);
                newobj.remove(oldlabel);
                newobj.add(newlabel);
            } else
                newobj = sc.obj;
            ScopeChain n = replaceObjectLabel(sc.next, oldlabel, newlabel, replace_cache);
            if (newobj != sc.obj || n != sc.next)
                c = make(newobj, n);
            else
                c = sc;
            replace_cache.put(sc, c);
        }
        return c;
    }

//    /**
//     * Replaces all object labels according to the given map.
//     */
//    public static ScopeChain replaceObjectLabels(ScopeChain sc, Map<ObjectLabel, ObjectLabel> m, Map<ScopeChain, ScopeChain> replace_cache) {
//        if (sc == null)
//            return null;
//        ScopeChain c = replace_cache.get(sc);
//        if (c == null) {
//            Set<ObjectLabel> newobj = Renaming.apply(m, sc.obj);
//            ScopeChain n = replaceObjectLabels(sc.next, m, replace_cache);
//            if (newobj != sc.obj || n != sc.next)
//                c = make(newobj, n);
//            else
//                c = sc;
//            replace_cache.put(sc, c);
//        }
//        return c;
//    }

    /**
     * Constructs a scope chain as a copy of the given one but with object labels renamed.
     */
    public static ScopeChain rename(ScopeChain sc, Renamings s) {
        return rename(sc, s, Collections.newMap());
    }

    /**
     * Renames objects in this scope chain.
     */
    public static ScopeChain rename(ScopeChain sc, Renamings s, Map<ScopeChain, ScopeChain> renaming_cache) {
        if (sc == null)
            return null;
        if (s == null)
            return sc;
        ScopeChain cs = renaming_cache.get(sc);
        if (cs == null) {
            Set<ObjectLabel> newobj = newSet();
            for (ObjectLabel l : sc.obj)
                newobj.addAll(s.rename(l));
            cs = make(newobj, rename(sc.next, s, renaming_cache));
            renaming_cache.put(sc, cs);
        }
        return cs;
    }

    /**
     * Returns the least upper bound of the two scope chains.
     */
    public static ScopeChain add(ScopeChain s1, ScopeChain s2) {
        if (s1 == null)
            return s2;
        if (s2 == null)
            return s1;
        ScopeChain n = add(s1.next, s2.next);
        Set<ObjectLabel> newobj = newSet();
        newobj.addAll(s1.obj);
        newobj.addAll(s2.obj);
        return make(newobj, n);
    }

    /**
     * Returns a copy of s1 where s2 has been removed.
     */
    public static ScopeChain remove(ScopeChain s1, ScopeChain s2) {
        if (s1 == null || s2 == null)
            return s1;
        ScopeChain n = remove(s1.next, s2.next);
        Set<ObjectLabel> newobj = newSet(s1.obj);
        newobj.removeAll(s2.obj);
        return make(newobj, n);
    }

    /**
     * Checks whether there are no objects in the scope chain.
     */
    public static boolean isEmpty(ScopeChain s) {
        if (s == null)
            return true;
        return s.obj.isEmpty() && isEmpty(s.next);
    }

    /**
     * Checks whether the given scope chain is equal to this one.
     */
    @Override
    public boolean equals(Object x) {
        if (x == this)
            return true;
        if (!(x instanceof ScopeChain))
            return false;
        ScopeChain e = (ScopeChain) x;
        if ((next == null) != (e.next == null) || (next != null && !next.equals(e.next)))
            return false;
        return obj.equals(e.obj);
    }

    /**
     * Computes the hash code for this scope chain.
     */
    @Override
    public int hashCode() {
        if (hashcode == 0) {
            hashcode = obj.hashCode() * 17 + (next != null ? next.hashCode() : 0) * 3;
            if (hashcode == 0)
                hashcode = 1;
        }
        return hashcode;
    }

    /**
     * Returns a string representation of this scope chain.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[");
        ScopeChain e = this;
        do {
            b.append(e.obj);
            if (e.next != null)
                b.append(",");
            e = e.next;
        } while (e != null);
        b.append("]");
        return b.toString();
    }

    /**
     * Returns a object label set iterable for the given scope chain (where null represents empty).
     */
    public static Iterable<Set<ObjectLabel>> iterable(ScopeChain s) {
        if (s == null)
            return java.util.Collections.emptySet();
        return () -> new Iterator<Set<ObjectLabel>>() {

            private ScopeChain c = s;

            @Override
            public boolean hasNext() {
                return c != null;
            }

            @Override
            public Set<ObjectLabel> next() {
                Set<ObjectLabel> objlabel = c.obj;
                c = c.next;
                return objlabel;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Returns the set of object labels appearing in the given scope chain.
     */
    public static Set<ObjectLabel> getObjectLabels(ScopeChain scope) {
        if (scope == null)
            return java.util.Collections.emptySet();
        Set<ObjectLabel> objlabels = newSet();
        for (Set<ObjectLabel> ls : iterable(scope))
            objlabels.addAll(ls);
        return objlabels;
    }

    /**
     * Checks whether the scope chain contains the given object label.
     */
    public static boolean containsObjectLabels(ScopeChain scope, ObjectLabel objlabel) {
        if (scope == null)
            return false;
        for (Set<ObjectLabel> ls : iterable(scope))
            if (ls.contains(objlabel))
                return true;
        return false;
    }
}
