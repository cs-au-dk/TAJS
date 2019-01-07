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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Local must-equals information.
 *
 * Map from singleton object label and property key to set of registers that must have the same value as the memory location.
 * (Register values may be garbage collected without affecting the must-equals information, to support alias filtering.)
 */
public class MustEquals {// TODO: use copy-on-write?

    public static boolean ALIAS_TRACKING = true; // if true, enable tracking variable writes

    /**
     * Map from singleton object label to fixed property key to set of registers that must have the same value.
     */
    private Map<ObjectLabel, Map<PKey, Set<Integer>>> mustEquals;

    private Map<Integer, Set<ObjectProperty>> mustEqualsReverse;

    /**
     * Constructs a new empty MustEquals.
     */
    public MustEquals() {
        mustEquals = newMap();
        mustEqualsReverse = newMap();
    }

    /**
     * Constructs a new MustEquals as a copy of the given one.
     */
    public MustEquals(MustEquals old) {
        mustEquals = newMap();
        old.mustEquals.forEach((objlabel, m) -> {
            Map<PKey, Set<Integer>> mn = newMap();
            mustEquals.put(objlabel, mn);
            m.forEach((pkey, s) -> mn.put(pkey, newSet(s)));
        });
        mustEqualsReverse = newMap();
        old.mustEqualsReverse.forEach((reg, s) -> mustEqualsReverse.put(reg, newSet(s)));
    }

    /**
     * Empties this MustEquals.
     */
    public void setToBottom() {
        mustEquals.clear();
        mustEqualsReverse.clear();
    }

    /**
     * Empties this MustEquals for the given object label.
     */
    public void setToBottom(ObjectLabel objlabel) {
        Map<PKey, Set<Integer>> m = mustEquals.remove(objlabel);
        if (m != null)
            m.values().forEach(regs -> regs.forEach(reg -> {
                Set<ObjectProperty> s = mustEqualsReverse.get(reg);
                if (s != null) {
                    s.removeIf(objprop -> objprop.getObjectLabel().equals(objlabel));
                    if (s.isEmpty())
                        mustEqualsReverse.remove(reg);
                }
            }));
    }

    /**
     * Empties this MustEquals for the given object label and property key.
     */
    public void setToBottom(ObjectLabel objlabel, PKey pkey) {
        Map<PKey, Set<Integer>> m = mustEquals.get(objlabel);
        if (m != null) {
            Set<Integer> regs = m.remove(pkey);
            if (regs != null)
                regs.forEach(reg -> {
                    Set<ObjectProperty> s = mustEqualsReverse.get(reg);
                    if (s != null) {
                        s.remove(ObjectProperty.makeOrdinary(objlabel, pkey));
                        if (s.isEmpty())
                            mustEqualsReverse.remove(reg);
                    }
                });
            if (m.isEmpty())
                mustEquals.remove(objlabel);
        }
    }

    /**
     * Empties this MustEquals for the given object label and property key.
     */
    public void setToBottom(ObjectProperty objprop) {
        if (objprop.getProperty().getKind() == Property.Kind.ORDINARY)
            setToBottom(objprop.getObjectLabel(), objprop.getPropertyName());
        else
            setToBottom(objprop.getObjectLabel());
    }

    /**
     * Removes the given register from all must-equal sets.
     */
    public void setToBottom(int reg) {
        Set<ObjectProperty> ops = mustEqualsReverse.remove(reg);
        if (ops != null)
            ops.forEach(op -> {
                Map<PKey, Set<Integer>> m = mustEquals.get(op.getObjectLabel());
                if (m != null) {
                    m.remove(op.getPropertyName());
                    if (m.isEmpty())
                        mustEquals.remove(op.getObjectLabel());
                }
            });
    }

    /**
     * Adds a must-equals fact.
     */
    public void addMustEquals(int reg, ObjectLabel objlabel, PKey pkey) {
        if (objlabel == null || pkey == null)
            return;
        if (ALIAS_TRACKING) {
            Map<PKey, Set<Integer>> m = mustEquals.get(objlabel);
            if (m != null)
                for (int aliasreg : m.getOrDefault(pkey, java.util.Collections.emptySet())) {
                    Set<ObjectProperty> aliases = mustEqualsReverse.get(aliasreg);
                    if (aliases != null) {
                        for (ObjectProperty alias : aliases)
                            if (!alias.getObjectLabel().equals(objlabel) || !alias.getPropertyName().equals(pkey)) {
                                Collections.addToMapMapSet(mustEquals, alias.getObjectLabel(), alias.getPropertyName(), reg);
                                Collections.addToMapSet(mustEqualsReverse, reg, alias);
                            }
                    }
                }
        }
        Collections.addToMapMapSet(mustEquals, objlabel, pkey, reg);
        Collections.addToMapSet(mustEqualsReverse, reg, ObjectProperty.makeOrdinary(objlabel, pkey));
    }

    /**
     * Returns the must-equals facts for the given object label and property key.
     * Should not be modified by the caller.
     */
    public Set<Integer> getMustEquals(ObjectLabel objlabel, PKey pkey) {
        return mustEquals.getOrDefault(objlabel, java.util.Collections.emptyMap()).getOrDefault(pkey, java.util.Collections.emptySet());
    }

    /**
     * Returns the must-equal facts for the given register.
     */
    public Set<ObjectProperty> getMustEquals(int ref) {
        return mustEqualsReverse.getOrDefault(ref, java.util.Collections.emptySet());
    }

    /**
     * Propagates the given MustEquals into this one.
     * @return if this MustEquals changed
     */
    public boolean propagate(MustEquals other) {
        boolean changed = false;
        for (Iterator<Map.Entry<ObjectLabel, Map<PKey, Set<Integer>>>> it1 = mustEquals.entrySet().iterator(); it1.hasNext();) {
            Map.Entry<ObjectLabel, Map<PKey, Set<Integer>>> thisme1 = it1.next();
            ObjectLabel obj = thisme1.getKey();
            Map<PKey, Set<Integer>> otherm = other.mustEquals.get(obj);
            if (otherm != null) {
                Map<PKey, Set<Integer>> thism = thisme1.getValue();
                for (Iterator<Map.Entry<PKey, Set<Integer>>> it2 = thism.entrySet().iterator(); it2.hasNext();) {
                    Map.Entry<PKey, Set<Integer>> thisme2 = it2.next();
                    PKey pkey = thisme2.getKey();
                    Set<Integer> others = otherm.get(pkey);
                    if (others != null) {
                        Set<Integer> thiss = thisme2.getValue();
                        changed |= thiss.retainAll(others);
                        if (thiss.isEmpty())
                            it2.remove();
                    } else {
                        it2.remove();
                        changed = true;
                    }
                }
                if (thism.isEmpty()) {
                    it1.remove();
                    changed = true;
                }
            } else {
                it1.remove();
                changed = true;
            }
        }
        for (Iterator<Map.Entry<Integer, Set<ObjectProperty>>> it = mustEqualsReverse.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Integer, Set<ObjectProperty>> thisme = it.next();
            int reg = thisme.getKey();
            Set<ObjectProperty> others = other.mustEqualsReverse.get(reg);
            if (others != null) {
                Set<ObjectProperty> thiss = thisme.getValue();
                thiss.retainAll(others);
                if (thiss.isEmpty())
                    it.remove();
            } else
                it.remove();
        }
        return changed;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        mustEquals.forEach((objlabel, m) ->
                m.forEach((pkey, regs) -> b.append("\n    ").append(objlabel).append(".").append(pkey).append(": ").append(regs.stream().map(r -> "v" + r).collect(Collectors.toList())))
        );
        mustEqualsReverse.forEach((reg, objprops) ->
                b.append("\n    v").append(reg).append(": ").append(objprops)
        );
        return b.toString();
    }

    /**
     * If this is a singleton set containing a singleton object label, return that object label, otherwise null.
     */
    public static ObjectLabel getSingleton(Set<ObjectLabel> objs) {
        if (objs != null && objs.size() == 1) {
            ObjectLabel objlabel = objs.iterator().next();
            if (objlabel.isSingleton())
                return objlabel;
        }
        return null;
    }

    /**
     * If this is a singleton string or a symbol, then return the corresponding PKey, otherwise null.
     */
    public static PKey getSingleton(Value propertystr) {
        if (propertystr.isMaybeSingleStr() && !propertystr.isMaybeOtherThanStr())
            return PKey.StringPKey.make(propertystr.getStr());
        else if (propertystr.isMaybeSymbol() && !propertystr.isMaybeOtherThanSymbol() && propertystr.getObjectLabels().size() == 1)
            return PKey.SymbolPKey.make(propertystr.getObjectLabels().iterator().next());
        return null;
    }
}
