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

package dk.brics.tajs.lattice;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.sortedEntries;

/**
 * Abstract object.
 */
public final class Obj {

    private Map<String, Value> properties;

    private boolean writable_properties; // for copy-on-write (for properties, not this object)

    private Value default_array_property; // represents all other possible properties that are valid array indices

    private Value default_nonarray_property; // represents all other possible properties

    private Value internal_prototype; // the [[Prototype]] property

    private Value internal_value; // the [[Value]] property

    private ScopeChain scope; // the [[Scope]] property, null if empty or unknown

    private boolean scope_unknown; // if set, the value of scope is unknown (and the 'scope' field is then not used)

    private boolean writable; // object is immutable if writable is false (for copy-on-write)

    private int hash_code; // hash code, only used if non-writable, 0 means uninitialized

    private static int number_of_objs_created;

    private static int number_of_makewritable_properties;

    private static Obj the_absent_modified;

    private static Obj the_none;

    private static Obj the_none_modified;

    private static Obj the_unknown;

    static {
        reset();
    }

    private Obj() {
        number_of_objs_created++;
    }

    /**
     * Creates a new abstract object as a copy of the given.
     * The object is initially writable.
     */
    public Obj(Obj x) {
        default_nonarray_property = x.default_nonarray_property;
        default_array_property = x.default_array_property;
        internal_prototype = x.internal_prototype;
        internal_value = x.internal_value;
        scope = x.scope;
        scope_unknown = x.scope_unknown;
        if (Options.get().isCopyOnWriteDisabled()) {
            properties = newMap(x.properties);
        } else {
            properties = x.properties;
            x.writable_properties = writable_properties = false;
        }
        writable = true;
        number_of_objs_created++;
    }

    /**
     * Makes this object non-writable (to allow sharing).
     *
     * @return this object
     */
    public Obj freeze() {
        writable = false;
        return this;
    }

    /**
     * Checks whether this object is writable.
     */
    public boolean isWritable() {
        return writable;
    }

    /**
     * Sets all properties to none and scope to empty.
     */
    private void setToNone() {
        checkWritable();
        default_nonarray_property = default_array_property = internal_prototype = internal_value = Value.makeNone();
        properties = Collections.emptyMap();
        scope = null;
        scope_unknown = false;
        writable_properties = false;
    }

//    /**
//     * Copies all properties from the given object.
//     */
//    public void setTo(Obj x) {
//        checkWritable();
//        default_nonarray_property = x.default_nonarray_property;
//        default_array_property = x.default_array_property;
//        internal_prototype = x.internal_prototype;
//        internal_value = x.internal_value;
//        scope = x.scope;
//        scope_unknown = x.scope_unknown;
//        if (Options.get().isCopyOnWriteDisabled()) {
//            properties = newMap(x.properties);
//        } else {
//            properties = x.properties;
//            x.writable_properties = writable_properties = false;
//        }
//    }

    /**
     * Constructs an abstract object where all properties are absent (but modified) and scope is set to empty.
     */
    private static Obj makeTheAbsentModified() {
        Obj obj = new Obj();
        obj.properties = Collections.emptyMap();
        obj.default_nonarray_property = obj.default_array_property = obj.internal_prototype = obj.internal_value = Value.makeAbsentModified();
        return obj;
    }

    /**
     * Returns an abstract object where all properties are absent (but modified) and scope is set to empty.
     */
    public static Obj makeAbsentModified() {
        return the_absent_modified;
    }

    /**
     * Constructs an abstract object where all properties are none and scope is set to empty.
     */
    private static Obj makeTheNone() {
        Obj obj = new Obj();
        obj.properties = Collections.emptyMap();
        obj.default_nonarray_property = obj.default_array_property = obj.internal_prototype = obj.internal_value = Value.makeNone();
        return obj;
    }

    /**
     * Returns an abstract object where all properties are none and scope is set to empty.
     */
    public static Obj makeNone() {
        return the_none;
    }

    /**
     * Constructs an abstract object where all properties are none, but modified, and scope is set to empty.
     */
    private static Obj makeTheNoneModified() {
        Obj obj = new Obj();
        obj.properties = Collections.emptyMap();
        obj.default_nonarray_property = obj.default_array_property = obj.internal_prototype = obj.internal_value = Value.makeNoneModified();
        return obj;
    }

    /**
     * Returns an abstract object where all properties are none, but modified, and scope is set to empty.
     */
    public static Obj makeNoneModified() {
        return the_none_modified;
    }

    /**
     * Constructs an abstract object where all properties have 'unknown' value.
     */
    private static Obj makeTheUnknown() {
        Obj obj = new Obj();
        obj.properties = Collections.emptyMap();
        obj.default_array_property = obj.default_nonarray_property = obj.internal_prototype = obj.internal_value = Value.makeUnknown();
        obj.scope_unknown = true;
        return obj;
    }

    /**
     * Returns an abstract object where all properties have 'unknown' value.
     */
    public static Obj makeUnknown() {
        return the_unknown;
    }

    /**
     * Checks whether all properties have 'unknown' value.
     */
    public boolean isUnknown() {
        for (Value v : properties.values())
            if (!v.isUnknown())
                return false;
        return default_array_property.isUnknown() && default_nonarray_property.isUnknown() && internal_prototype.isUnknown()
                && internal_value.isUnknown() && scope_unknown;
    }

    /**
     * Checks whether all properties have the none value.
     */
    public boolean isAllNone() {
        for (Value v : properties.values())
            if (!v.isNone())
                return false;
        return default_array_property.isNone() && default_nonarray_property.isNone() && internal_prototype.isNone()
                && internal_value.isNone() && !scope_unknown && scope == null;
    }

    /**
     * Checks whether some property has the none value.
     * The internal scope property is ignored.
     */
    public boolean isSomeNone() {
        for (Value v : properties.values())
            if (v.isNone())
                return true;
        return default_array_property.isNone() || default_nonarray_property.isNone() || internal_prototype.isNone() || internal_value.isNone();
    }

    /**
     * Checks whether some property is maybe modified.
     */
    public boolean isSomeModified() {
        for (Value v : properties.values())
            if (v.isMaybeModified())
                return true;
        return default_array_property.isMaybeModified() || default_nonarray_property.isMaybeModified() || internal_prototype.isMaybeModified() || internal_value.isMaybeModified();
    }

    private void checkWritable() {
        if (!writable)
            throw new AnalysisException("Attempt to modify non-writable Obj");
    }

    /**
     * Summarizes the object labels in this object.
     *
     * @return new object
     */
    public Obj summarize(Summarized s) {
        Obj res = new Obj();
        res.properties = newMap();
        for (Entry<String, Value> me : properties.entrySet())
            res.properties.put(me.getKey(), me.getValue().summarize(s));
        res.writable_properties = true;
        res.default_array_property = default_array_property.summarize(s);
        res.default_nonarray_property = default_nonarray_property.summarize(s);
        res.internal_prototype = internal_prototype.summarize(s);
        res.internal_value = internal_value.summarize(s);
        res.scope = ScopeChain.summarize(scope, s);
        res.scope_unknown = scope_unknown;
        return res;
    }

    /**
     * Replaces all definitely non-modified properties in this object by the corresponding properties of other.
     */
    public void replaceNonModifiedParts(Obj other) {
        checkWritable();
        Map<String, Value> newproperties = newMap();
        for (Entry<String, Value> me : properties.entrySet()) {
            Value v = me.getValue();
            if (!v.isMaybeModified()) // property is definitely not modified, so replace it (don't consider the defaults here)
                v = other.properties.get(me.getKey());
            if (v != null) // if the property is definitely not modified *and* it doesn't appear in the other object, then remove it
                newproperties.put(me.getKey(), v);
        }
        boolean default_array_property_maybe_modified = default_array_property.isMaybeModified();
        boolean default_nonarray_property_maybe_modified = default_nonarray_property.isMaybeModified();
        if (!default_array_property_maybe_modified || !default_nonarray_property_maybe_modified)
            for (Entry<String, Value> me : other.properties.entrySet())
                if (!newproperties.containsKey(me.getKey())
                        && (Strings.isArrayIndex(me.getKey()) ? !default_array_property_maybe_modified : !default_nonarray_property_maybe_modified))
                    newproperties.put(me.getKey(), me.getValue());
        properties = newproperties;
        writable_properties = true;
        if (!default_array_property_maybe_modified)
            default_array_property = other.default_array_property;
        if (!default_nonarray_property_maybe_modified)
            default_nonarray_property = other.default_nonarray_property;
        if (!internal_prototype.isMaybeModified())
            internal_prototype = other.internal_prototype;
        if (!internal_value.isMaybeModified())
            internal_value = other.internal_value;
        if (scope_unknown && !other.scope_unknown) {
            scope = other.scope;
            scope_unknown = false;
        }
        if (isSomeNone())
            setToNone();
    }

    /**
     * Makes properties writable.
     */
    private void makeWritableProperties() {
        if (writable_properties)
            return;
        properties = newMap(properties);
        writable_properties = true;
        number_of_makewritable_properties++;
    }

    /**
     * Returns the total number of Obj objects created.
     */
    public static int getNumberOfObjsCreated() {
        return number_of_objs_created;
    }

    /**
     * Resets the global counters.
     * {@link Value#reset()} must be called before, not after, this method.
     */
    public static void reset() {
        the_absent_modified = makeTheAbsentModified();
        the_none = makeTheNone();
        the_none_modified = makeTheNoneModified();
        the_unknown = makeTheUnknown();
        number_of_objs_created = 0;
        number_of_makewritable_properties = 0;
    }

    /**
     * Returns the total number of makeWritableProperties operations.
     */
    public static int getNumberOfMakeWritablePropertiesCalls() {
        return number_of_makewritable_properties;
    }

    /**
     * Clears modified flags for all values.
     */
    public void clearModified() {
        checkWritable();
        Map<String, Value> new_properties = newMap();
        for (Entry<String, Value> me : properties.entrySet())
            new_properties.put(me.getKey(), me.getValue().restrictToNotModified());
        properties = new_properties;
        writable_properties = true;
        default_nonarray_property = default_nonarray_property.restrictToNotModified();
        default_array_property = default_array_property.restrictToNotModified();
        internal_prototype = internal_prototype.restrictToNotModified();
        internal_value = internal_value.restrictToNotModified();
    }

    /**
     * Returns the value of the given property, considering defaults if necessary.
     * Never returns null, may return 'unknown'.
     */
    public Value getProperty(String propertyname) {
        Value v = properties.get(propertyname);
        if (v == null)
            if (Strings.isArrayIndex(propertyname))
                v = getDefaultArrayProperty();
            else
                v = getDefaultNonArrayProperty();
        return v;
    }

    /**
     * Sets the given property.
     */
    public void setProperty(String propertyname, Value v) {
        checkWritable();
        makeWritableProperties();
        properties.put(propertyname, v);
    }

    /**
     * Returns all property names, excluding the defaults and internal properties.
     */
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    /**
     * Returns all properties, excluding the defaults and internal properties.
     * The returned map is *only* for reading.
     */
    public Map<String, Value> getProperties() {
        return properties;
    }

    /**
     * Sets the property map.
     */
    public void setProperties(Map<String, Value> properties) {
        checkWritable();
        this.properties = properties;
        writable_properties = true;
    }

    /**
     * Returns the value of the default array property.
     */
    public Value getDefaultArrayProperty() {
        return default_array_property;
    }

    /**
     * Sets the value of the default array property.
     */
    public void setDefaultArrayProperty(Value v) {
        checkWritable();
        if (!v.isUnknown() && v.isMaybePresent() && !v.isMaybeAbsent())
            throw new AnalysisException("Illegal default array property: " + v);
        default_array_property = v;
    }

    /**
     * Returns the value of the default non-array property.
     */
    public Value getDefaultNonArrayProperty() {
        return default_nonarray_property;
    }

    /**
     * Sets the value of the default non-array property.
     */
    public void setDefaultNonArrayProperty(Value v) {
        checkWritable();
        if (!v.isUnknown() && v.isMaybePresent() && !v.isMaybeAbsent())
            throw new AnalysisException("Illegal default nonarray property: " + v);
        default_nonarray_property = v;
    }

    /**
     * Returns the value of the internal [[Value]] property.
     */
    public Value getInternalValue() {
        return internal_value;
    }

    /**
     * Sets the internal [[Value]] property.
     */
    public void setInternalValue(Value v) {
        checkWritable();
        internal_value = v;
    }

    /**
     * Returns the value of the internal [[Prototype]] property.
     */
    public Value getInternalPrototype() {
        return internal_prototype;
    }

    /**
     * Sets the internal [[Prototype]] property.
     */
    public void setInternalPrototype(Value v) {
        checkWritable();
        internal_prototype = v;
    }

    /**
     * Returns the value of the internal [[Scope]] property.
     * Assumed to be non-'unknown'.
     */
    public ScopeChain getScopeChain() {
        if (scope_unknown)
            throw new AnalysisException("Calling getScopeChain when scope is 'unknown'");
        return scope;
    }

    /**
     * Sets the internal [[Scope]] property.
     */
    public void setScopeChain(ScopeChain scope) {
        checkWritable();
        this.scope = scope;
        scope_unknown = false;
    }

    /**
     * Sets the internal [[Scope]] property to 'unknown'.
     */
    public void setScopeChainUnknown() {
        checkWritable();
        this.scope = null;
        scope_unknown = true;
    }

    /**
     * Adds to the internal [[Scope]] property.
     *
     * @return true if changed
     */
    public boolean addToScopeChain(ScopeChain newscope) {
        checkWritable();
        if (scope_unknown)
            throw new AnalysisException("Calling addToScopeChain when scope is 'unknown'");
        ScopeChain res = ScopeChain.add(scope, newscope);
        boolean changed = res != null && !res.equals(scope);
        scope = res;
        return changed;
    }

    /**
     * Returns true if internal [[Scope]] property is 'unknown'.
     */
    public boolean isScopeChainUnknown() {
        return scope_unknown;
    }

    /**
     * Replaces all occurrences of oldlabel by newlabel.
     * Ignores 'unknown' values.
     */
    public void replaceObjectLabel(ObjectLabel oldlabel, ObjectLabel newlabel, Map<ScopeChain, ScopeChain> cache) {
        checkWritable();
        Map<String, Value> newproperties = newMap();
        for (Entry<String, Value> me : properties.entrySet())
            newproperties.put(me.getKey(), me.getValue().replaceObjectLabel(oldlabel, newlabel));
        properties = newproperties;
        scope = ScopeChain.replaceObjectLabel(scope, oldlabel, newlabel, cache);
        default_nonarray_property = default_nonarray_property.replaceObjectLabel(oldlabel, newlabel);
        default_array_property = default_array_property.replaceObjectLabel(oldlabel, newlabel);
        internal_prototype = internal_prototype.replaceObjectLabel(oldlabel, newlabel);
        internal_value = internal_value.replaceObjectLabel(oldlabel, newlabel);
        writable_properties = true;
    }

    /**
     * Checks whether the given abstract object is equal to this one.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Obj))
            return false;
        Obj x = (Obj) obj;
        if ((scope == null) != (x.scope == null))
            return false;
        return properties.equals(x.properties) &&
                (scope == null || x.scope == null || scope.equals(x.scope)) &&
                (scope_unknown == x.scope_unknown) &&
                default_nonarray_property.equals(x.default_nonarray_property) &&
                default_array_property.equals(x.default_array_property) &&
                internal_prototype.equals(x.internal_prototype) &&
                internal_value.equals(x.internal_value);
    }

    /**
     * Returns a description of the changes from the old object to this object.
     * It is assumed that the old object is less than this object
     * and that no explicit property has been moved to default_array_property or default_nonarray_property.
     */
    public void diff(Obj old, StringBuilder b) {
        for (Entry<String, Value> me : sortedEntries(properties)) {
            Value v = old.properties.get(me.getKey());
            if (v == null) {
                b.append("\n        new property: ").append(me.getKey());
            } else if (!me.getValue().equals(v)) {
                b.append("\n        changed property: ").append(me.getKey()).append(": ");
                me.getValue().diff(v, b);
                b.append(" was: ").append(v);
            }
        }
        if (!default_array_property.equals(old.default_array_property)) {
            b.append("\n        changed default_array_property: ");
            default_array_property.diff(old.default_array_property, b);
            b.append(" was: ").append(old.default_array_property);
        }
        if (!default_nonarray_property.equals(old.default_nonarray_property)) {
            b.append("\n        changed default_nonarray_property: ");
            default_nonarray_property.diff(old.default_nonarray_property, b);
            b.append(" was: ").append(old.default_nonarray_property);
        }
        if (!internal_prototype.equals(old.internal_prototype)) {
            b.append("\n        changed internal_prototype: ");
            internal_prototype.diff(old.internal_prototype, b);
            b.append(" was: ").append(old.internal_prototype);
        }
        if (!internal_value.equals(old.internal_value)) {
            b.append("\n        changed internal_value: ");
            internal_value.diff(old.internal_value, b);
            b.append(" was: ").append(old.internal_value);
        }
        if (scope_unknown != old.scope_unknown) {
            b.append("\n        changed scope_unknown: ").append(scope_unknown).append(" was: ").append(old.scope_unknown);
        }
    }

    /**
     * Computes the hash code for this abstract object.
     */
    @Override
    public int hashCode() { // (currently never used)
        if (writable || hash_code == 0) { // recompute if writable or uninitialized
            int h = properties.hashCode() * 3
                    + (scope != null ? scope.hashCode() * 7 : 0)
                    + (scope_unknown ? 13 : 0)
                    + internal_prototype.hashCode() * 11
                    + internal_value.hashCode() * 113
                    + default_nonarray_property.hashCode() * 23
                    + default_array_property.hashCode() * 31;
            if (h == 0)
                h = 1;
            if (!writable)
                hash_code = h;
            return h;
        }
        return hash_code;
    }

    /**
     * Produces a string description of this abstract object.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        boolean any = false;
        b.append("{");
        if (default_array_property.isNone()) {
            any = true;
            b.append("<none>");
        }
        for (Entry<String, Value> me : sortedEntries(properties)) {
            Value v = me.getValue();
            if (v == (Strings.isArrayIndex(me.getKey()) ? default_array_property : default_nonarray_property))
                continue;
            if (me.getKey().equals(Property.__PROTO__))
                continue;
            if (any)
                b.append(",");
            else
                any = true;
            b.append(Strings.escape(me.getKey())).append(":").append(v);
        }
        if (default_array_property.isMaybePresentOrUnknown()) {
            if (any)
                b.append(",");
            else
                any = true;
            b.append("[[DefaultArray]]=").append(default_array_property);
        }
        if (default_nonarray_property.isMaybePresentOrUnknown()) {
            if (any)
                b.append(",");
            else
                any = true;
            b.append("[[DefaultNonArray]]=").append(default_nonarray_property);
        }
        if (internal_prototype.isMaybePresentOrUnknown()) {
            if (any)
                b.append(",");
            else
                any = true;
            b.append("[[Prototype]]=").append(internal_prototype);
        }
        if (internal_value.isMaybePresentOrUnknown()) {
            if (any)
                b.append(",");
            else
                any = true;
            b.append("[[Value]]=").append(internal_value);
        }
        if (scope != null || scope_unknown) {
            if (any)
                b.append(",");
//            else
//                any = true;
            b.append("[[Scope]]=");
            if (scope != null)
                b.append(scope);
            else
                b.append("?");
        }
        b.append("}");
        return b.toString();
    }

    /**
     * Prints the maybe modified properties.
     * Internal properties are ignored.
     */
    public String printModified() {
        StringBuilder b = new StringBuilder();
        for (Entry<String, Value> me : sortedEntries(properties)) {
            Value v = me.getValue();
            if (me.getKey().equals(Property.__PROTO__)) {
                continue;
            }
            if (v.isMaybeModified() && v.isMaybePresentOrUnknown())
                b.append("\n    ").append(Strings.escape(me.getKey())).append(": ").append(v);
        }
        if ((default_array_property.isMaybeModified()) && default_array_property.isMaybePresentOrUnknown())
            b.append("\n    ").append("[[DefaultArray]] = ").append(default_array_property);
        if ((default_nonarray_property.isMaybeModified()) && default_nonarray_property.isMaybePresentOrUnknown())
            b.append("\n    ").append("[[DefaultNonArray]] = ").append(default_nonarray_property);
        if (internal_prototype.isMaybeModified() && internal_prototype.isMaybePresentOrUnknown())
            b.append("\n    [[Prototype]] = ").append(internal_prototype);
        if (internal_value.isMaybeModified() && internal_value.isMaybePresentOrUnknown())
            b.append("\n    [[Value]] = ").append(internal_value);
        return b.toString();
    }

    /**
     * Returns the set of all object labels used in this abstract object
     * 'unknown' values are ignored.
     */
    public Set<ObjectLabel> getAllObjectLabels() {
        Set<ObjectLabel> objlabels = newSet();
        for (Value v : properties.values())
            objlabels.addAll(v.getAllObjectLabels());
        objlabels.addAll(default_array_property.getAllObjectLabels());
        objlabels.addAll(default_nonarray_property.getAllObjectLabels());
        objlabels.addAll(internal_prototype.getAllObjectLabels());
        objlabels.addAll(internal_value.getAllObjectLabels());
        objlabels.addAll(ScopeChain.getObjectLabels(scope));
        return objlabels;
    }

    /**
     * Checks whether this object contains the given object label.
     */
    public boolean containsObjectLabel(ObjectLabel objlabel) {
        if (default_array_property.containsObjectLabel(objlabel) ||
                default_nonarray_property.containsObjectLabel(objlabel) ||
                internal_prototype.containsObjectLabel(objlabel) ||
                internal_value.containsObjectLabel(objlabel)) {
            return true;
        }
        for (Value v : properties.values())
            if (v.containsObjectLabel(objlabel))
                return true;
        return ScopeChain.containsObjectLabels(scope, objlabel);
    }

    /**
     * Returns the designated property value of this object.
     * Note that the object label of the property reference is not used.
     */
    public Value getValue(ObjectProperty prop) {
        switch (prop.getKind()) {
            case ORDINARY:
                return getProperty(prop.getPropertyName());
            case DEFAULT_ARRAY:
                return getDefaultArrayProperty();
            case DEFAULT_NONARRAY:
                return getDefaultNonArrayProperty();
            case INTERNAL_VALUE:
                return getInternalValue();
            case INTERNAL_PROTOTYPE:
                return getInternalPrototype();
            default:
                throw new AnalysisException("Unexpected property reference kind");
        }
    }

    /**
     * Sets the designated property value of this object.
     * Note that the object label of the property reference is not used.
     */
    public void setValue(ObjectProperty prop, Value v) {
        switch (prop.getKind()) {
            case ORDINARY:
                setProperty(prop.getPropertyName(), v);
                break;
            case DEFAULT_ARRAY:
                setDefaultArrayProperty(v);
                break;
            case DEFAULT_NONARRAY:
                setDefaultNonArrayProperty(v);
                break;
            case INTERNAL_VALUE:
                setInternalValue(v);
                break;
            case INTERNAL_PROTOTYPE:
                setInternalPrototype(v);
                break;
            default:
                throw new AnalysisException("Unexpected property reference kind");
        }
    }

    /**
     * Trims this object according to the given existing object.
     */
    public void localize(Obj obj, ObjectLabel objlabel, State s) {
        checkWritable();
        makeWritableProperties();
        // materialize properties before changing the default properties
        for (String propertyname : obj.properties.keySet()) {
            properties.put(propertyname, getProperty(propertyname));
        }
        // reduce those properties that are unknown or polymorphic in obj
        default_array_property = UnknownValueResolver.localize(default_array_property, obj.default_array_property, s,
                ObjectProperty.makeDefaultArray(objlabel));
        default_nonarray_property = UnknownValueResolver.localize(default_nonarray_property, obj.default_nonarray_property, s,
                ObjectProperty.makeDefaultNonArray(objlabel));
        internal_value = UnknownValueResolver.localize(internal_value, obj.internal_value, s,
                ObjectProperty.makeInternalValue(objlabel));
        internal_prototype = UnknownValueResolver.localize(internal_prototype, obj.internal_prototype, s,
                ObjectProperty.makeInternalPrototype(objlabel));
        UnknownValueResolver.localizeScopeChain(objlabel, this, obj, s);
        Map<String, Value> new_properties = newMap();
        for (Entry<String, Value> me : properties.entrySet()) { // obj is writable, so materializations from defaults will appear here
            String propertyname = me.getKey();
            Value v = me.getValue();
            Value obj_v = obj.getProperty(propertyname);
            new_properties.put(propertyname, UnknownValueResolver.localize(v, obj_v, s,
                    ObjectProperty.makeOrdinary(objlabel, propertyname)));
        }
        properties = new_properties;
    }

//    /**
//     * Removes the parts of this object that are also in the given object.
//     * It is assumed that this object subsumes the given object, but the defaults may not cover the same properties.
//     */
//    public void remove(Obj obj) {
//        checkWritable();
//        Map<String, Value> new_properties = newMap();
//        for (Entry<String, Value> me : properties.entrySet()) {
//            String propertyname = me.getKey();
//            Value value = me.getValue();
//            Value other_value = obj.getProperty(propertyname); // may look up in default
//            Value new_value = value.remove(other_value);
//            new_properties.put(propertyname, new_value);
//        }
//        properties = new_properties;
//        writable_properties = true;
//        // careful with defaults when they don't cover the same properties - here, obj.default
//        // has already been propagated further (to the function entry state), so we don't need
//        // to consider which non-default properties in obj correspond to this.default
//        default_array_property = default_array_property.remove(obj.default_array_property);
//        default_nonarray_property = default_nonarray_property.remove(obj.default_nonarray_property);
//        internal_prototype = internal_prototype.remove(obj.internal_prototype);
//        internal_value = internal_value.remove(obj.internal_value);
//        scope = ScopeChain.remove(scope, obj.scope);
//        // TODO: could remove properties that have same value as their default?
//    }
}
