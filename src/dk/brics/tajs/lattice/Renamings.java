/*
 * Copyright 2009-2020 Aarhus University
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

import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Object label renamings.
 * Consists of maybe/definitely summarized object label sets.
 */
public final class Renamings {

    /**
     * Maybe summarized objects since function entry. (Contains the singleton object labels.)
     */
    private Set<ObjectLabel> maybe_summarized;

    /**
     * Definitely summarized objects since function entry. (Contains the singleton object labels.)
     */
    private Set<ObjectLabel> definitely_summarized; // note: lattice order reversed for this component! but always a subset of maybe_summarized

    /**
     * Constructs a new empty renaming.
     */
    public Renamings() {
        maybe_summarized = newSet();
        definitely_summarized = newSet();
    }

    /**
     * Constructs a new renaming as a copy of an existing one.
     * The sets are copied.
     */
    public Renamings(Renamings s) {
        maybe_summarized = newSet(s.maybe_summarized);
        definitely_summarized = newSet(s.definitely_summarized);
    }

    /**
     * Constructs a renaming representing the given object label being definitely summarized.
     */
    public Renamings(ObjectLabel objlabel) {
        this();
        maybe_summarized.add(objlabel);
        definitely_summarized.add(objlabel);
    }

    /**
     * Marks the specified object label as definitely not summarized.
     * @param strong if set, remove from both 'definitely' and 'maybe'; otherwise, remove only from 'definitely'
     */
    public void removeSummarized(ObjectLabel objlabel, boolean strong) {
        definitely_summarized.remove(objlabel);
        if (strong) {
            maybe_summarized.remove(objlabel);
        }
    }

    /**
     * Adds the specified object label as definitely summarized.
     */
    public void addDefinitelySummarized(ObjectLabel objlabel) {
        definitely_summarized.add(objlabel);
        maybe_summarized.add(objlabel);
    }

    /**
     * Checks whether the given object label is marked as maybe new in this function.
     */
    public boolean isMaybeNew(ObjectLabel objlabel) {
        return maybe_summarized.contains(objlabel);
    }

    /**
     * Checks whether the given object label is marked as definitely new in this function.
     */
    public boolean isDefinitelyNew(ObjectLabel objlabel) {
        return definitely_summarized.contains(objlabel);
    }

    /**
     * Clears the renaming.
     */
    public void clear() {
        maybe_summarized.clear();
        definitely_summarized.clear();
    }

    /**
     * Joins the given renamings into this.
     *
     * @return true if changed
     */
    public boolean join(Renamings s) {
        return maybe_summarized.addAll(s.maybe_summarized) | definitely_summarized.retainAll(s.definitely_summarized);
    }

    /**
     * Adds the given renamings to this.
     *
     * @return true if changed
     */
    public boolean add(Renamings s) {
        return maybe_summarized.addAll(s.maybe_summarized) | definitely_summarized.addAll(s.definitely_summarized);
    }

    /**
     * Indicates whether some other object is equal to this one.
     */
    @Override
    public boolean equals(Object obj) { // NOTE: Renamings.equals and hashCode are currently not used
        if (!(obj instanceof Renamings))
            return false;
        Renamings x = (Renamings) obj;
        return maybe_summarized.equals(x.maybe_summarized) && definitely_summarized.equals(x.definitely_summarized);
    }

    /**
     * Returns a hash code value for the object.
     */
    @Override
    public int hashCode() {
        return maybe_summarized.hashCode() * 3 + definitely_summarized.hashCode() * 17;
    }

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return "maybe=" + maybe_summarized + ", definitely=" + definitely_summarized;
    }

    /**
     * Renames the given object label.
     */
    public Set<ObjectLabel> rename(ObjectLabel objlabel) {
        if (objlabel.isSingleton()) {
            if (isMaybeNew(objlabel)) {
                if (!isDefinitelyNew(objlabel)) {
                    Set<ObjectLabel> res = newSet();
                    res.add(objlabel);
                    res.add(objlabel.makeSummary());
                    return res;
                } else
                    return Collections.singleton(objlabel.makeSummary());
            } else
                return Collections.singleton(objlabel);
        } else
            return Collections.singleton(objlabel);
    }

    /**
     * Renames the given set of object labels.
     * May return the given set unchanged rather than a new set.
     */
    public Set<ObjectLabel> rename(Set<ObjectLabel> objlabels) {
        if (objlabels == null)
            return null;
        boolean changed = false;
        for (ObjectLabel ol : objlabels)
            if (ol.isSingleton() && isMaybeNew(ol)) {
                changed = true;
                break;
            }
        if (!changed)
            return objlabels;
        Set<ObjectLabel> new_objs = newSet();
        for (ObjectLabel ol : objlabels) {
            if (ol.isSingleton()) {
                if (isMaybeNew(ol)) {
                    new_objs.add(ol.makeSummary());
                    if (!isDefinitelyNew(ol))
                        new_objs.add(ol);
                } else
                    new_objs.add(ol);
            } else
                new_objs.add(ol);
        }
        return new_objs;
    }

    /**
     * Inversely renames the given object property.
     */
    public Set<ObjectProperty> renameInverse(ObjectProperty prop) {
        Set<ObjectProperty> res = newSet();
        res.add(prop);
        if (!prop.getObjectLabel().isSingleton() && isMaybeNew(prop.getObjectLabel().makeSingleton()))
            res.add(prop.makeSingleton());
        return res;
    }

    /**
     * Returns a string description of the differences between this renamings and the given one.
     */
    public void diff(Renamings old, StringBuilder b) {
        Set<ObjectLabel>  temp = newSet(maybe_summarized);
        temp.removeAll(old.maybe_summarized);
        if (!temp.isEmpty())
            b.append("\n      new maybe-renamings: ").append(temp);
        temp = newSet(definitely_summarized);
        temp.removeAll(old.definitely_summarized);
        if (!temp.isEmpty())
            b.append("\n      new definitely-renamings: ").append(temp);
    }

//    /**
//     * Replaces all object labels according to the given map.
//     */
//    public void replaceObjectLabels(Map<ObjectLabel, ObjectLabel> m) {
//        maybe_summarized = Renaming.apply(m, maybe_summarized);
//        definitely_summarized = Renaming.apply(m, definitely_summarized);
//    }
}
