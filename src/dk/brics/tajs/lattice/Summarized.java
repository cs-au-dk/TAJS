/*
 * Copyright 2009-2016 Aarhus University
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

import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Maybe/definitely summarized object label sets.
 */
public final class Summarized {

    /**
     * Maybe summarized objects since function entry. (Contains the singleton object labels.)
     */
    private Set<ObjectLabel> maybe_summarized;

    /**
     * Definitely summarized objects since function entry. (Contains the singleton object labels.)
     */
    private Set<ObjectLabel> definitely_summarized; // note: lattice order reversed for this component! but always a subset of maybe_summarized

    /**
     * Constructs a new pair of empty summarized sets.
     */
    public Summarized() {
        maybe_summarized = newSet();
        definitely_summarized = newSet();
    }

    /**
     * Constructs a new pair of summarized sets.
     * The sets are copied.
     */
    public Summarized(Summarized s) {
        maybe_summarized = newSet(s.maybe_summarized);
        definitely_summarized = newSet(s.definitely_summarized);
    }

    /**
     * Returns the maybe summarized object labels.
     */
    public Set<ObjectLabel> getMaybeSummarized() {
        return maybe_summarized;
    }

    /**
     * Returns the definitely summarized object labels.
     */
    public Set<ObjectLabel> getDefinitelySummarized() {
        return definitely_summarized;
    }

    /**
     * Adds the specified object label as definitely summarized.
     */
    public void addDefinitelySummarized(ObjectLabel objlabel) {
        definitely_summarized.add(objlabel);
        maybe_summarized.add(objlabel);
    }

    /**
     * Checks whether the given object label is marked as maybe summarized.
     */
    public boolean isMaybeSummarized(ObjectLabel objlabel) {
        return maybe_summarized.contains(objlabel);
    }

    /**
     * Checks whether the given object label is marked as definitely summarized.
     */
    public boolean isDefinitelySummarized(ObjectLabel objlabel) {
        return definitely_summarized.contains(objlabel);
    }

    /**
     * Clears the sets.
     */
    public void clear() {
        maybe_summarized.clear();
        definitely_summarized.clear();
    }

    /**
     * Joins the given summarized sets into this pair.
     *
     * @return true if changed
     */
    public boolean join(Summarized s) {
        return maybe_summarized.addAll(s.maybe_summarized) | definitely_summarized.retainAll(s.definitely_summarized);
    }

    /**
     * Adds the given summarized sets to this pair.
     *
     * @return true if changed
     */
    public boolean add(Summarized s) {
        return maybe_summarized.addAll(s.maybe_summarized) | definitely_summarized.addAll(s.definitely_summarized);
    }

    /**
     * Indicates whether some other object is equal to this one.
     */
    @Override
    public boolean equals(Object obj) { // XXX: Summarized.equals and hashCode are currently not used
        if (!(obj instanceof Summarized))
            return false;
        Summarized x = (Summarized) obj;
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
     * Summarizes the given set of object labels.
     * May return the given set unchanged rather than a new set.
     */
    public Set<ObjectLabel> summarize(Set<ObjectLabel> objlabels) {
        if (objlabels == null)
            return null;
        boolean changed = false;
        for (ObjectLabel ol : objlabels)
            if (ol.isSingleton() && isMaybeSummarized(ol)) {
                changed = true;
                break;
            }
        if (!changed)
            return objlabels;
        Set<ObjectLabel> new_objs = newSet();
        for (ObjectLabel ol : objlabels) {
            if (ol.isSingleton()) {
                if (isMaybeSummarized(ol)) {
                    new_objs.add(ol.makeSummary());
                    if (!isDefinitelySummarized(ol))
                        new_objs.add(ol);
                } else
                    new_objs.add(ol);
            } else
                new_objs.add(ol);
        }
        return new_objs;
    }

//    /**
//     * Replaces all object labels according to the given map.
//     */
//    public void replaceObjectLabels(Map<ObjectLabel, ObjectLabel> m) {
//        maybe_summarized = Renaming.apply(m, maybe_summarized);
//        definitely_summarized = Renaming.apply(m, definitely_summarized);
//    }
}
