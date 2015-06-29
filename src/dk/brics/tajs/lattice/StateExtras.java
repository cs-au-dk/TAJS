/*
 * Copyright 2009-2015 Aarhus University
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

import dk.brics.tajs.options.Options;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addAllToMapSet;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newMapMapSet;
import static dk.brics.tajs.util.Collections.newMapSet;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Extra components for abstract states.
 * Can be used to keep track of registered event handlers of different kinds and special HTML elements.
 */
public class StateExtras {

    private static Logger log = Logger.getLogger(StateExtras.class);

    private Map<String, Set<ObjectLabel>> may_sets;

    private boolean writable_may_sets;

    private Map<String, Set<ObjectLabel>> must_sets;

    private boolean writable_must_sets;

    private Map<String, Map<String, Set<ObjectLabel>>> may_maps;

    private Map<String, Set<ObjectLabel>> may_maps_default;

    private boolean writable_may_maps;

    protected StateExtras() {
        setToNone();
    }

    protected StateExtras(StateExtras x) {
        if (Options.get().isCopyOnWriteDisabled()) {
            may_sets = newMapSet(x.may_sets);
            must_sets = newMapSet(x.must_sets);
            may_maps = newMapMapSet(x.may_maps);
            may_maps_default = newMapSet(x.may_maps_default);
        } else {
            may_sets = x.may_sets;
            writable_may_sets = x.writable_may_sets = false;
            must_sets = x.must_sets;
            writable_must_sets = x.writable_must_sets = false;
            may_maps = x.may_maps;
            may_maps_default = x.may_maps_default;
            writable_may_maps = x.writable_may_maps = false;
        }
    }

    /**
     * Makes the may-sets writable.
     */
    private void makeMaySetsWritable() {
        if (writable_may_sets) {
            return;
        }
        may_sets = newMapSet(may_sets);
        writable_may_sets = true;
    }

    /**
     * Makes the must-sets writable.
     */
    private void makeMustSetsWritable() {
        if (writable_must_sets) {
            return;
        }
        must_sets = newMapSet(must_sets);
        writable_must_sets = true;
    }

    /**
     * Makes the may-maps writable.
     */
    private void makeMayMapsWritable() {
        if (writable_may_maps) {
            return;
        }
        may_maps = newMapMapSet(may_maps);
        may_maps_default = newMapSet(may_maps_default);
        writable_may_maps = true;
    }

    /**
     * Resets all maps.
     */
    public void setToNone() {
        if (Options.get().isCopyOnWriteDisabled()) {
            may_sets = newMap();
            writable_may_sets = true;
            must_sets = newMap();
            writable_must_sets = true;
            may_maps = newMap();
            may_maps_default = newMap();
            writable_may_maps = true;
        } else {
            may_sets = Collections.emptyMap();
            writable_may_sets = false;
            must_sets = Collections.emptyMap();
            writable_must_sets = false;
            may_maps = Collections.emptyMap();
            may_maps_default = Collections.emptyMap();
            writable_may_maps = false;
        }
    }

    /**
     * Checks whether the sets are empty.
     */
    public boolean isNone() {
        for (Set<ObjectLabel> s : may_sets.values())
            if (!s.isEmpty())
                return false;
        for (Set<ObjectLabel> s : must_sets.values())
            if (!s.isEmpty())
                return false;
        for (Set<ObjectLabel> s : may_maps_default.values())
            if (!s.isEmpty())
                return false;
        for (Map<String, Set<ObjectLabel>> s1 : may_maps.values())
            for (Set<ObjectLabel> s2 : s1.values())
                if (!s2.isEmpty())
                    return false;
        return true;
    }

    protected boolean propagate(StateExtras s) {
        makeMaySetsWritable();
        makeMustSetsWritable();
        makeMayMapsWritable();
        boolean changed = false;
        // MaySets
        for (Entry<String, Set<ObjectLabel>> e : s.may_sets.entrySet()) {
            Set<ObjectLabel> thismayset = may_sets.get(e.getKey());
            thismayset = (thismayset == null) ? dk.brics.tajs.util.Collections.<ObjectLabel>newSet() : thismayset;
            may_sets.put(e.getKey(), thismayset);
            changed |= thismayset.addAll(e.getValue());
        }
        // MustSets
        for (Entry<String, Set<ObjectLabel>> e : s.must_sets.entrySet()) {
            Set<ObjectLabel> thismustset = must_sets.get(e.getKey());
            thismustset = (thismustset == null) ? dk.brics.tajs.util.Collections.<ObjectLabel>newSet() : thismustset;
            must_sets.put(e.getKey(), thismustset);
            changed |= thismustset.retainAll(e.getValue());
        }
        // MayMaps
        for (Entry<String, Map<String, Set<ObjectLabel>>> e : s.may_maps.entrySet()) {
            Map<String, Set<ObjectLabel>> thismaymap = may_maps.get(e.getKey());
            Map<String, Set<ObjectLabel>> thatMayMaps = e.getValue();
            thismaymap = (thismaymap == null) ? dk.brics.tajs.util.Collections.<String, Set<ObjectLabel>>newMap() : thismaymap;
            may_maps.put(e.getKey(), thismaymap);
            for (Entry<String, Set<ObjectLabel>> ee : thatMayMaps.entrySet()) {
                Set<ObjectLabel> thismayset = thismaymap.get(ee.getKey());
                Set<ObjectLabel> thatMaySet = ee.getValue();
                thismayset = (thismayset == null) ? dk.brics.tajs.util.Collections.<ObjectLabel>newSet() : thismayset;
                thismaymap.put(ee.getKey(), thismayset);
                changed |= thismayset.addAll(thatMaySet);
            }
        }
        // MayMapsDefault
        for (Entry<String, Set<ObjectLabel>> e : s.may_maps_default.entrySet()) {
            Set<ObjectLabel> thisDefault = may_maps_default.get(e.getKey());
            thisDefault = (thisDefault == null) ? dk.brics.tajs.util.Collections.<ObjectLabel>newSet() : thisDefault;
            may_maps_default.put(e.getKey(), thisDefault);
            changed |= thisDefault.addAll(e.getValue());
        }
        return changed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StateExtras)) {
            return false;
        }
        StateExtras x = (StateExtras) obj;
        if (!may_sets.equals(x.may_sets)) {
            log.debug("equals(...)=false, maysets differ");
            return false;
        }
        if (!must_sets.equals(x.must_sets)) {
            log.debug("equals(...)=false, mustsets differ");
            return false;
        }
        if (!may_maps.equals(x.may_maps)) {
            log.debug("equals(...)=false, maymaps differ");
            return false;
        }
        if (!may_maps_default.equals(x.may_maps_default)) {
            log.debug("equals(...)=false, maymapsDefault differ");
            return false;
        }
        return true;
    }

    /**
     * Computes the hash code for this object.
     */
    @Override
    public int hashCode() {
        return may_sets.hashCode() * 61
                + must_sets.hashCode() * 67
                + may_maps.hashCode() * 71
                + may_maps_default.hashCode() * 79;
    }

    /**
     * Returns a description of the sets and maps.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (!may_sets.isEmpty()) {
            b.append("\n  MaySets: ").append(may_sets);
        }
        if (!must_sets.isEmpty()) {
            b.append("\n  MustSets: ").append(must_sets);
        }
        if (!may_maps.isEmpty()) {
            b.append("\n  MayMaps: ").append(may_maps);
        }
        if (!may_maps_default.isEmpty()) {
            b.append("\n  MayMapsDefault: ").append(may_maps_default);
        }
        return b.toString();
    }

    /**
     * Adds all registered object labels to the given set.
     */
    public void getAllObjectLabels(Set<ObjectLabel> live) {
        for (Set<ObjectLabel> mayset : may_sets.values()) {
            live.addAll(mayset);
        }
        for (Set<ObjectLabel> mustset : must_sets.values()) {
            live.addAll(mustset);
        }
        for (Map<String, Set<ObjectLabel>> m : may_maps.values()) {
            for (Set<ObjectLabel> s : m.values()) {
                live.addAll(s);
            }
        }
        for (Set<ObjectLabel> s : may_maps_default.values()) {
            live.addAll(s);
        }
    }

    /**
     * Adds a collection of object labels to a named may set.
     */
    public void addToMaySet(String name, Collection<ObjectLabel> labels) {
        makeMaySetsWritable();
        addAllToMapSet(may_sets, name, labels);
    }

    /**
     * Returns the set of object labels identified by the given name.
     */
    public Set<ObjectLabel> getFromMaySet(String name) {
        Set<ObjectLabel> labelset = may_sets.get(name);
        if (labelset == null) {
            return Collections.unmodifiableSet(dk.brics.tajs.util.Collections.<ObjectLabel>newSet());
        }
        return Collections.unmodifiableSet(labelset);
    }

    /**
     * Adds a collection of object labels to the named must set.
     */
    public void addToMustSet(String name, Collection<ObjectLabel> labels) {
        makeMustSetsWritable();
        addAllToMapSet(must_sets, name, labels);
    }

    /**
     * Returns the set of object labels identified by the given name.
     */
    public Set<ObjectLabel> getFromMustSet(String name) {
        Set<ObjectLabel> labelset = must_sets.get(name);
        if (labelset == null) {
            return Collections.unmodifiableSet(dk.brics.tajs.util.Collections.<ObjectLabel>newSet());
        }
        return Collections.unmodifiableSet(labelset);
    }

    /**
     * Adds the given key and set of object labels to the map identified by name.
     */
    public void addToMayMap(String name, String key, Collection<ObjectLabel> labels) {
        makeMayMapsWritable();
        Map<String, Set<ObjectLabel>> maymap = may_maps.get(name);
        if (maymap == null) {
            maymap = newMap();
            may_maps.put(name, maymap);
        }
        addAllToMapSet(maymap, key, labels);
    }

    /**
     * Adds the given object labels to the 'default' of the map identified by name.
     */
    public void addToDefaultMayMap(String name, Collection<ObjectLabel> labels) {
        makeMayMapsWritable();
        addAllToMapSet(may_maps_default, name, labels);
    }

    /**
     * Retrieves a set of object labels from the named map given the key.
     */
    public Set<ObjectLabel> getFromMayMap(String name, String key) {
        Set<ObjectLabel> result = newSet();
        Map<String, Set<ObjectLabel>> maymap = may_maps.get(name);
        if (maymap != null) {
            Set<ObjectLabel> mayset = maymap.get(key);
            if (mayset != null) {
                result.addAll(mayset);
            }
        }
        Set<ObjectLabel> maydefault = may_maps_default.get(name);
        maydefault = maydefault == null ? Collections.<ObjectLabel>emptySet() : maydefault;
        result.addAll(maydefault);
        return Collections.unmodifiableSet(result);
    }

//    /**
//     * Replaces all object labels according to the given map.
//     */
//    public void replaceObjectLabels(Map<ObjectLabel, ObjectLabel> m) {
//        makeMayMapsWritable();
//        makeMaySetsWritable();
//        makeMustSetsWritable();
//        replaceObjectLabels2(may_sets, m);
//        replaceObjectLabels2(must_sets, m);
//        replaceObjectLabels3(may_maps, m);
//        replaceObjectLabels2(may_maps_default, m);
//    }
//
//    private static void replaceObjectLabels2(Map<String, Set<ObjectLabel>> x, Map<ObjectLabel, ObjectLabel> m) {
//        for (Entry<String, Set<ObjectLabel>> me : x.entrySet())
//            me.setValue(Renaming.apply(m, me.getValue()));
//    }
//
//    private static void replaceObjectLabels3(Map<String, Map<String, Set<ObjectLabel>>> x, Map<ObjectLabel, ObjectLabel> m) {
//        for (Entry<String, Map<String, Set<ObjectLabel>>> me : x.entrySet())
//            for (Entry<String, Set<ObjectLabel>> me2 : me.getValue().entrySet())
//                me2.setValue(Renaming.apply(m, me2.getValue()));
//    }

//    /**
//     * Removes the parts that are also in 'other'.
//     */
//    public void remove(StateExtras other) {
//        makeMaySetsWritable();
//        makeMustSetsWritable();
//        makeMayMapsWritable();
//        for (Entry<String, Set<ObjectLabel>> me : may_sets.entrySet()) {
//            String s = me.getKey();
//            Set<ObjectLabel> so = me.getValue();
//            Set<ObjectLabel> so_other = other.may_sets.get(s);
//            if (so_other != null)
//                so.removeAll(so_other);
//        }
//        for (Entry<String, Set<ObjectLabel>> me : must_sets.entrySet()) {
//            String s = me.getKey();
//            Set<ObjectLabel> so = me.getValue();
//            Set<ObjectLabel> so_other = other.must_sets.get(s);
//            if (so_other != null)
//                so.removeAll(so_other);
//        }
//        for (Entry<String, Map<String, Set<ObjectLabel>>> me1 : may_maps.entrySet()) {
//            String s1 = me1.getKey();
//            Map<String, Set<ObjectLabel>> mso = me1.getValue();
//            Map<String, Set<ObjectLabel>> mso_other = other.may_maps.get(s1);
//            for (Entry<String, Set<ObjectLabel>> me2 : mso.entrySet()) {
//                String s2 = me2.getKey();
//                Set<ObjectLabel> so = me2.getValue();
//                if (mso_other != null) {
//                    Set<ObjectLabel> so_other = mso_other.get(s2);
//                    if (so_other != null)
//                        so.removeAll(so_other);
//                }
//            }
//        }
//        for (Entry<String, Set<ObjectLabel>> me : may_maps_default.entrySet()) {
//            String s = me.getKey();
//            Set<ObjectLabel> so = me.getValue();
//            Set<ObjectLabel> so_other = other.may_maps_default.get(s);
//            if (so_other != null)
//                so.removeAll(so_other);
//        }
//    }
}
