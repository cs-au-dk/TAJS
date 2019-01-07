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

import dk.brics.tajs.lattice.PKey.StringPKey;
import dk.brics.tajs.lattice.PKey.SymbolPKey;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Strings;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * The properties of an object given a query, with their value.
 */
public class ObjProperties {

    private final Map<PKey, Value> properties;

    private final Value default_numeric_property;

    private final Value default_other_property;

    private final Set<PKey> own_properties;

    /**
     * Creates a bottom ObjProperties.
     */
    private ObjProperties() {
        this.default_numeric_property = Value.makeNone();
        this.default_other_property = Value.makeNone();
        this.properties = newMap();
        this.own_properties = newSet();
    }

    /**
     * Creates an ObjProperties with the given properties.
     */
    private ObjProperties(Value default_numeric_property, Value default_other_property, Map<PKey, Value> properties, Set<PKey> own_properties) {
        assert default_numeric_property != null;
        assert default_other_property != null;
        assert properties != null;
        assert properties.values().stream().noneMatch(Value::isPolymorphicOrUnknown);
        this.default_numeric_property = default_numeric_property;
        this.default_other_property = default_other_property;
        this.properties = properties;
        this.own_properties = own_properties;
    }

    /**
     * Creates an ObjProperties for the selected object (ignoring prototype chain) and query.
     */
    private static ObjProperties makeFromObjectLabel(ObjectLabel l, State s, PropertyQuery flags) {
        Map<PKey, Value> prop = newMap();
        Value numeric_property = UnknownValueResolver.getDefaultNumericProperty(l, s);
        Value other_property = UnknownValueResolver.getDefaultOtherProperty(l, s);
        Set<PKey> own_properties = newSet(UnknownValueResolver.getProperties(l, s).keySet());
        for (PKey p : own_properties) {
            if ((flags.isWithoutProto() && StringPKey.__PROTO__.equals(p)) ||
                    (!flags.isIncludeSymbols() && p instanceof SymbolPKey) ||
                    (flags.isOnlySymbols() && !(p instanceof SymbolPKey)))
                continue;
            Value v = UnknownValueResolver.getProperty(l, p, s, false);
            if (p instanceof StringPKey) {
                if (v.equals(p.isNumeric() ? numeric_property : other_property))
                    continue;

            }
            prop.put(p, v);
        }
        if (l.getKind() == ObjectLabel.Kind.STRING) {
            // String objects have index-property
            Value internalValue = UnknownValueResolver.getInternalValue(l, s, false);
            if (internalValue.isMaybeSingleStr()) {
                String str = internalValue.getStr();
                for (int i = 0; i < str.length(); i++)
                    prop.put(StringPKey.make(Integer.toString(i)), Value.makeStr(String.valueOf(str.charAt(i))).joinNotDontEnum());
            }
            if (internalValue.isMaybeStrPrefix()) {
                String str = internalValue.getPrefix();
                for (int i = 0; i < internalValue.getPrefix().length(); i++)
                    prop.put(StringPKey.make(Integer.toString(i)), Value.makeStr(String.valueOf(str.charAt(i))).joinNotDontEnum());
            }
            if (internalValue.isMaybeFuzzyStr())
                numeric_property = Value.makeAnyStr().joinNotDontEnum();
        }
        own_properties.removeIf(p -> !UnknownValueResolver.getProperty(l, p, s, false).isMaybePresent());
        return new ObjProperties(numeric_property, other_property, prop, own_properties);
    }

    /**
     * Materializes the given properties from the defaults.
     */
    private void materialize(Set<PKey> ps) {
        for (PKey p : ps) {
            if (!properties.containsKey(p)) {
                Value v;
                if (p.isNumeric())
                    v = default_numeric_property;
                else
                    v = default_other_property;
                properties.put(p, v);
            }
        }
    }

    /**
     * Computes the joins of this ObjProperties and the given one.
     */
    private ObjProperties join(ObjProperties other) {
        assert other != null;
        Value numeric_property = default_numeric_property.join(other.default_numeric_property);
        Value other_property = default_other_property.join(other.default_other_property);
        this.materialize(other.properties.keySet());
        other.materialize(this.properties.keySet());
        Map<PKey, Value> newP = newMap();
        newP.putAll(properties);
        for (PKey p : this.properties.keySet()) {
            Value thisv = this.properties.get(p);
            Value otherv = other.properties.get(p);
            newP.put(p, thisv.join(otherv));
        }
        Set<PKey> newO = newSet(this.own_properties);
        newO.addAll(other.own_properties);
        return new ObjProperties(numeric_property, other_property, newP, newO);
    }

    /**
     * Computes ObjProperties as copy of inheritor that inherits from this one.
     */
    private ObjProperties addPropertiesOfInheritor(ObjProperties inheritor) {
        Value numeric_property = Value.join(inheritor.default_numeric_property, default_numeric_property);
        Value other_property = Value.join(inheritor.default_other_property, default_other_property);
        this.materialize(inheritor.properties.keySet());
        inheritor.materialize(this.properties.keySet());
        Map<PKey, Value> newP = newMap();
        for (PKey k : this.properties.keySet()) {
            Value protoValue = this.properties.get(k);
            Value inheritorValue = inheritor.properties.get(k);
            Value finalValue;
            if (inheritorValue.isMaybeAbsent())
                finalValue = protoValue.join(inheritorValue.restrictToNotAbsent());
            else
                finalValue = inheritorValue;
            newP.put(k, finalValue);
        }
        return new ObjProperties(numeric_property, other_property, newP, newSet(inheritor.own_properties));
    }

    /**
     * Computes ObjProperties for the given objects and query.
     */
    public static ObjProperties getProperties(Collection<ObjectLabel> objlabels, State s, PropertyQuery flags) {
        Map<ObjectLabel, Set<ObjectLabel>> inverse_proto = newMap();
        Set<ObjectLabel> roots = newSet();
        Map<ObjectLabel, ObjProperties> props = newMap();
        if (flags.isUsePrototypes()) {
            // find relevant objects, prepare inverse_proto
            LinkedList<ObjectLabel> worklist = new LinkedList<>(objlabels);
            Set<ObjectLabel> visited = newSet(objlabels);
            while (!worklist.isEmpty()) {
                ObjectLabel ol = worklist.removeFirst();
                if (!inverse_proto.containsKey(ol))
                    inverse_proto.put(ol, newSet());
                Value proto = UnknownValueResolver.getInternalPrototype(ol, s, false);
                if (proto.isMaybeNull())
                    roots.add(ol);
                for (ObjectLabel p : proto.getObjectLabels()) {
                    addToMapSet(inverse_proto, p, ol);
                    if (!visited.contains(p)) {
                        worklist.add(p);
                        visited.add(p);
                    }
                }
            }
            // find properties with fixpoint computation starting from the roots
            Set<ObjectLabel> workset = newSet(roots);
            while (!workset.isEmpty()) {
                ObjectLabel ol = workset.iterator().next();
                workset.remove(ol);
                Value proto = UnknownValueResolver.getInternalPrototype(ol, s, false);
                ObjProperties protoProperties = proto.getAllObjectLabels().stream().map(p -> props.getOrDefault(p, new ObjProperties())).reduce(new ObjProperties(), ObjProperties::join);
                ObjProperties oldProperties = props.get(ol);
                ObjProperties newProperties = protoProperties.addPropertiesOfInheritor(oldProperties != null ? oldProperties : makeFromObjectLabel(ol, s, flags));
                props.put(ol, newProperties);
                if (oldProperties == null || !oldProperties.equals(newProperties))
                    workset.addAll(inverse_proto.get(ol));
            }
        } else {
            // find each object's own properties
            for (ObjectLabel ol : objlabels) {
                props.put(ol, makeFromObjectLabel(ol, s, flags));
            }
        }
        ObjProperties candidate = props.entrySet().stream()
                .filter(x -> objlabels.contains(x.getKey()))
                .map(x -> props.get(x.getKey()))
                .reduce(new ObjProperties(), ObjProperties::join);
        if (flags.isOnlyEnumerable())
            candidate = candidate.filterEnumerableObjectProperties();
        return candidate;
    }

    /**
     * Filter away properties that are not enumerable.
     */
    private ObjProperties filterEnumerableObjectProperties() {
        Map<PKey, Value> newP = newMap();
        properties.forEach((key, value) -> {
            if (value.isMaybeNotDontEnum())
                newP.put(key, value);
        });
        Value numeric_property = default_numeric_property.isMaybeNotDontEnum() ? default_numeric_property : Value.makeNone();
        Value other_property = default_other_property.isMaybeNotDontEnum() ? default_other_property : Value.makeNone();
        return new ObjProperties(numeric_property, other_property, newP, own_properties);
    }

    /**
     * Extracts the PKeys that are maybe present (ignoring the defaults).
     */
    public Set<PKey> getMaybe() {
        return properties.entrySet().stream()
                .filter(e -> e.getValue().isMaybePresent())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    /**
     * Extracts the PKeys that are definitely present.
     */
    public Set<PKey> getDefinitely() {
        return properties.entrySet().stream()
                .filter(e -> e.getValue().isNotAbsent())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    /**
     * Returns true if the set of property names is certain.
     */
    public boolean isDefinite() {
        return !default_numeric_property.isMaybePresent() && !default_other_property.isMaybePresent() &&
                properties.values().stream().noneMatch(Value::isMaybeAbsent);
    }

    /**
     * Returns true if the default numeric property is maybe present.
     */
    public boolean isDefaultNumericMaybePresent() {
        return default_numeric_property.isMaybePresent();
    }

    /**
     * Returns true if the default non-numeric property is maybe present.
     */
    public boolean isDefaultOtherMaybePresent() {
        return default_other_property.isMaybePresent();
    }

    /**
     * Returns the join of the values of the selected properties.
     */
    public Value getValue(PKeys p) {
        List<Value> vs = newList();
        for (Map.Entry<PKey,Value> me : properties.entrySet()) {
            PKey k = me.getKey();
            Value v = me.getValue();
            if (k.isMaybeValue(p))
                vs.add(v);
        }
        boolean add_default_numeric = false; // set to true if some numeric property of p is not in 'properties'
        boolean add_default_other = false; // set to true if some non-numeric property of p is not in 'properties'
        if (p.isMaybeSingleStr()) {
            if (!properties.containsKey(PKey.StringPKey.make(p.getStr())))
                if (Strings.isNumeric(p.getStr()))
                    add_default_numeric = true;
                else
                    add_default_other = true;
        } else if (p.getIncludedStrings() != null) {
            for (String s : p.getIncludedStrings()) {
                if (!properties.containsKey(PKey.StringPKey.make(s)))
                    if (Strings.isNumeric(s))
                        add_default_numeric = true;
                    else
                        add_default_other = true;
            }
        } else if (p.isMaybeFuzzyStr()) {
            if (p.isMaybeStrSomeNumeric())
                add_default_numeric = true;
            if (p.isMaybeStrSomeNonNumeric())
                add_default_other = true;
        }
        if (add_default_numeric)
            vs.add(default_numeric_property);
        if (add_default_other)
            vs.add(default_other_property);
        return Value.join(vs);
    }

    /**
     * Returns all collected property names and values.
     */
    public Map<PKeys, Value> getProperties() {
        Map<PKeys, Value> m = newMap();
        Set<String> numeric = newSet();
        Set<String> other = newSet();
        for (Map.Entry<PKey, Value> me : properties.entrySet()) {
            PKey key = me.getKey();
            Value value = me.getValue();
            m.put(key.toValue(), value);
            if (key instanceof PKey.StringPKey) {
                String stringkey = ((PKey.StringPKey) key).getStr();
                if (Strings.isNumeric(stringkey))
                    numeric.add(stringkey);
                else
                    other.add(stringkey);
            }
        }
        m.put(Value.makeAnyStrNumeric().restrictToNotStrings(numeric), default_numeric_property);
        m.put(Value.makeAnyStrNotNumeric().restrictToNotStrings(other), default_other_property);
        return m;
    }

    /**
     * Returns a collection of values that represents the names of potentially present properties.
     */
    public Collection<Value> getGroupedPropertyNames() {
        if (Options.get().isNoStringSets()) {
            // strategy: not using excluded_strings but instead removing the names covered by the defaults
            Collection<Value> vs = getMaybe().stream().map(PKey::toValue).collect(Collectors.toList());
            if (default_numeric_property.isMaybePresent()) {
                vs.add(Value.makeAnyStrNumeric());
                vs.removeIf(prop -> prop.isMaybeSingleStr() && Strings.isNumeric(prop.getStr())); // no need to include the names that are covered by default_numeric
            }
            if (default_other_property.isMaybePresent()) {
                vs.add(Value.makeAnyStrNotNumeric());
                vs.removeIf(prop -> prop.isMaybeSingleStr() && !Strings.isNumeric(prop.getStr())); // no need to include the names that are covered by default_other
            }
            return vs;
        }
        // strategy: use excluded_stringa, omit a property name if its value is same as the default, and group inherited
        Set<Value> m = newSet();
        Set<String> numeric = newSet();
        Set<String> other = newSet();
        Set<Value> inherited = newSet();
        for (Map.Entry<PKey, Value> me : properties.entrySet()) {
            Value value = me.getValue();
            if (value.isMaybePresent()) {
                PKey key = me.getKey();
                boolean own_or_numeric_or_symbol = false;
                if (key instanceof PKey.StringPKey) {
                    String stringkey = ((PKey.StringPKey) key).getStr();
                    if (Strings.isNumeric(stringkey)) {
                        if (!value.equals(default_numeric_property)) {
                            numeric.add(stringkey);
                            m.add(key.toValue());
                            own_or_numeric_or_symbol = true;
                        }
                    } else {
                        if (!value.equals(default_other_property)) {
                            other.add(stringkey);
                            if (own_properties.contains(key)) {
                                m.add(key.toValue());
                                own_or_numeric_or_symbol = true;
                            }
                        }
                    }
                } else {
                    m.add(key.toValue());
                    own_or_numeric_or_symbol = true;
                }
                if (!own_or_numeric_or_symbol)
                    inherited.add(key.toValue());
            }
        }
        if (default_numeric_property.isMaybePresent())
            m.add(Value.makeAnyStrNumeric().restrictToNotStrings(numeric));
        if (default_other_property.isMaybePresent())
            m.add(Value.makeAnyStrNotNumeric().restrictToNotStrings(other));
        if (!inherited.isEmpty())
            m.add(Value.join(inherited));
        return m;
/*      // strategy: use excluded_strings, group together all UInt property names
        Set<Value> m = newSet();
        boolean any_numeric = default_numeric_property.isMaybePresent();
        Set<String> other = newSet();
        for (Map.Entry<PKey, Value> me : properties.entrySet()) {
            if (me.getValue().isMaybePresent()) {
                PKey key = me.getKey();
                if (key instanceof PKey.StringPKey) {
                    String stringkey = ((PKey.StringPKey) key).getStr();
                    if (Strings.isArrayIndex(stringkey))
                        any_numeric = true;
                    else {
                        other.add(stringkey);
                        m.add(key.toValue());
                    }
                } else
                    m.add(key.toValue());
            }
        }
        if (any_numeric)
            m.add(Value.makeAnyStrUInt());
        if (default_other_property.isMaybePresent())
            m.add(Value.makeAnyStrNotUInt().restrictToNotStrings(other));
        return m;
*/
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjProperties that = (ObjProperties) o;
        return Objects.equals(properties, that.properties) &&
                Objects.equals(default_numeric_property, that.default_numeric_property) &&
                Objects.equals(default_other_property, that.default_other_property);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties, default_numeric_property, default_other_property);
    }

    /**
     * Query for {@link #getProperties(Collection, State, PropertyQuery)}.
     */
    public static class PropertyQuery {

        private static final int ONLY_ENUMERABLE = 0b00001;

        private static final int USE_PROTOTYPES = 0b00010;

        private static final int WITHOUT_PROTO = 0b00100;

        private static final int INCLUDE_SYMBOLS = 0b01000;

        private static final int ONLY_SYMBOLS = 0b10000;

        private int flags;

        private PropertyQuery(int flags) {
            this.flags = flags;
        }

        /**
         * Creates a new query with default flags.
         */
        public static PropertyQuery makeQuery() {
            return new PropertyQuery(0);
        }

        /**
         * Only include properties that are enumerable.
         */
        public PropertyQuery onlyEnumerable() {
            this.flags |= ONLY_ENUMERABLE;
            return this;
        }

        /**
         * Decides whether to include only properties that are enumerable.
         */
        public PropertyQuery setOnlyEnumerable(boolean v) {
            if (v)
                this.flags |= ONLY_ENUMERABLE;
            else
                this.flags &= ~ONLY_ENUMERABLE;
            return this;
        }

        /**
         * Decides whether to include symbols.
         */
        public PropertyQuery setIncludeSymbols(boolean v) {
            if (v)
                this.flags |= INCLUDE_SYMBOLS;
            else
                this.flags &= ~INCLUDE_SYMBOLS;
            return this;
        }

        /**
         * Consider prototype chain for absent properties.
         */
        public PropertyQuery usePrototypes() {
            this.flags |= USE_PROTOTYPES;
            return this;
        }

        /**
         * Exclude __proto__.
         */
        public PropertyQuery withoutProto() { // FIXME: never used??
            this.flags |= WITHOUT_PROTO;
            return this;
        }

        /**
         * Include symbols.
         */
        public PropertyQuery includeSymbols() {
            this.flags |= INCLUDE_SYMBOLS;
            return this;
        }

        /**
         * Decides whether to include symbols only.
         */
        public PropertyQuery onlySymbols() { // FIXME: never used??
            this.flags |= ONLY_SYMBOLS;
            return this;
        }

        public boolean isOnlyEnumerable() {
            return (flags & ONLY_ENUMERABLE) != 0;
        }

        public boolean isUsePrototypes() {
            return (flags & USE_PROTOTYPES) != 0;
        }

        public boolean isWithoutProto() {
            return (flags & WITHOUT_PROTO) != 0;
        }

        public boolean isIncludeSymbols() {
            return (flags & INCLUDE_SYMBOLS) != 0;
        }

        public boolean isOnlySymbols() {
            return (flags & ONLY_SYMBOLS) != 0;
        }
    }
}
