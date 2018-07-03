/*
 * Copyright 2009-2018 Aarhus University
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
import dk.brics.tajs.util.Strings;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * The properties of an object given a query, with their value.
 *
 * This class exploit the absent attribute of properties with the meaning that
 * the property is definitely absent, hence the array/non-array properties are
 * *not* representing the value of such properties.
 */
public class ObjProperties {

    private final Map<PKey, Value> properties;

    private final Value default_array_property;

    private final Value default_nonarray_property;

    /**
     * Creates a bottom ObjProperties.
     */
    private ObjProperties() {
        this.default_array_property = Value.makeNone();
        this.default_nonarray_property = Value.makeNone();
        this.properties = newMap();
    }

    /**
     * Creates an ObjProperties with the given properties.
     */
    private ObjProperties(Value default_array_property, Value default_nonarray_property, Map<PKey, Value> properties) {
        assert default_array_property != null;
        assert default_nonarray_property != null;
        assert properties != null;
        assert properties.values().stream().noneMatch(Value::isPolymorphicOrUnknown);
        this.default_array_property = default_array_property;
        this.default_nonarray_property = default_nonarray_property;
        this.properties = properties;
    }

    /**
     * Creates an ObjProperties for the selected object (ignoring prototype chain) and query.
     */
    private static ObjProperties makeFromObjectLabel(ObjectLabel l, State s, PropertyQuery flags) {
        Map<PKey, Value> prop = newMap();
        Value array_property = UnknownValueResolver.getDefaultArrayProperty(l, s);
        Value nonarray_property = UnknownValueResolver.getDefaultNonArrayProperty(l, s);
        Map<PKey, Value> objProperties = UnknownValueResolver.getProperties(l, s);
        for (PKey p : objProperties.keySet()) {
            if ((flags.isWithoutProto() && StringPKey.__PROTO__.equals(p)) ||
                    (!flags.isIncludeSymbols() && p instanceof SymbolPKey) ||
                    (flags.isOnlySymbols() && !(p instanceof SymbolPKey)))
                continue;
            Value propValue = UnknownValueResolver.getProperty(l, p, s, false);
            prop.put(p, propValue);
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
                array_property = Value.makeAnyStr().joinNotDontEnum();
        }
        return new ObjProperties(array_property, nonarray_property, prop);
    }

    /**
     * Materializes the given properties from the defaults.
     */
    private void materialize(Set<PKey> ps) {
        for (PKey p : ps) {
            if (!properties.containsKey(p)) {
                Value v;
                if (p.isArrayIndex())
                    v = default_array_property;
                else
                    v = default_nonarray_property;
                properties.put(p, v);
            }
        }
    }

    /**
     * Computes the joins of this ObjProperties and the given one.
     */
    private ObjProperties join(ObjProperties other) {
        assert other != null;
        Value array = Value.join(default_array_property, other.default_array_property);
        Value nonarray = Value.join(default_nonarray_property, other.default_nonarray_property);
        this.materialize(other.properties.keySet());
        other.materialize(this.properties.keySet());
        Map<PKey, Value> newP = newMap();
        newP.putAll(properties);
        for (PKey p : this.properties.keySet()) {
            Value thisv = this.properties.get(p);
            Value otherv = other.properties.get(p);
            newP.put(p, thisv.join(otherv));
        }
        return new ObjProperties(array, nonarray, newP);
    }

    /**
     * Computes ObjProperties as copy of inheritor that inherits from this one.
     */
    private ObjProperties addPropertiesOfInheritor(ObjProperties inheritor) {
        Value newArray = Value.join(inheritor.default_array_property, default_array_property);
        Value newNonArray = Value.join(inheritor.default_nonarray_property, default_nonarray_property);
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
        return new ObjProperties(newArray, newNonArray, newP);
    }

    /**
     * Computes ObjProperties for the given objects and query.
     */
    public static ObjProperties getProperties(Collection<ObjectLabel> objlabels, State s, PropertyQuery flags) {
        Map<ObjectLabel, Set<ObjectLabel>> inverse_proto = newMap();
        Set<ObjectLabel> roots = newSet();
        if (flags.isUsePrototypes()) {
            // find relevant objects, prepare inverse_proto
            LinkedList<ObjectLabel> worklist = new LinkedList<>(objlabels);
            Set<ObjectLabel> visited = newSet(objlabels);
            while (!worklist.isEmpty()) {
                ObjectLabel ol = worklist.removeFirst();
                if (!inverse_proto.containsKey(ol))
                    inverse_proto.put(ol, dk.brics.tajs.util.Collections.newSet());
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
        } else {
            roots.addAll(objlabels);
        }

        // find properties with fixpoint computation starting from the roots
        Map<ObjectLabel, ObjProperties> props = newMap();
        Set<ObjectLabel> workset = newSet(roots);
        while (!workset.isEmpty()) {
            ObjectLabel ol = workset.iterator().next();
            workset.remove(ol);
            if (flags.isUsePrototypes()) {
                // inherit from prototypes
                Value proto = UnknownValueResolver.getInternalPrototype(ol, s, false);
                ObjProperties protoProperties = proto.getAllObjectLabels().stream()
                        .map(pl -> props.getOrDefault(pl, new ObjProperties()))
                        .reduce(new ObjProperties(), ObjProperties::join);

                // find properties of the current object
                ObjProperties inheritorProperties = ObjProperties.makeFromObjectLabel(ol, s, flags).join(props.getOrDefault(ol, new ObjProperties()));

                // overwrite with properties in the current object
                ObjProperties thisChainProperties = protoProperties.addPropertiesOfInheritor(inheritorProperties);

                ObjProperties oldp = props.get(ol);
                props.put(ol, thisChainProperties);
                if (oldp == null || !oldp.equals(thisChainProperties))
                    workset.addAll(inverse_proto.get(ol));
            } else {
                ObjProperties p = ObjProperties.makeFromObjectLabel(ol, s, flags);
                props.put(ol, p);
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
            if (value.isMaybePresent() && !value.isDontEnum())
                newP.put(key, value);
        });
        Value array = (default_array_property.isMaybePresent() && default_array_property.isDontEnum()) ? Value.makeAbsent() : default_array_property;
        Value nonarray = (default_nonarray_property.isMaybePresent() && default_nonarray_property.isDontEnum()) ? Value.makeAbsent() : default_nonarray_property;
        return new ObjProperties(array, nonarray, newP);
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
        return !default_array_property.isMaybePresent() && !default_nonarray_property.isMaybePresent() &&
                properties.values().stream().noneMatch(Value::isMaybeAbsent);
    }

    /**
     * Returns true if the default array property is maybe present.
     */
    public boolean isDefaultArrayMaybePresent() {
        return default_array_property.isMaybePresent();
    }

    /**
     * Returns true if the default non-array property is maybe present.
     */
    public boolean isDefaultNonArrayMaybePresent() {
        return default_nonarray_property.isMaybePresent();
    }

    /**
     * Returns the join of the values of the selected properties.
     */
    public Value getValue(PKeys p) {
        List<Value> vs = newList();
        if (p.isMaybeSingleStr()) {
            Value v = properties.get(StringPKey.make(p.getStr()));
            if (v == null) {
                if (Strings.isArrayIndex(p.getStr()))
                    v = default_array_property;
                else
                    v = default_nonarray_property;
            }
            vs.add(v);
        }
        if (p.isMaybeFuzzyStr() || p.isMaybeSymbol()) {
            for (Map.Entry<PKey,Value> me : properties.entrySet()) {
                PKey k = me.getKey();
                Value v = me.getValue();
                if (k.isMaybeValue(p))
                    vs.add(v);
            }
            if (p.isMaybeStrSomeUInt())
                vs.add(default_array_property);
            if (p.isMaybeStrSomeNonUInt())
                vs.add(default_nonarray_property);

        }
        return Value.join(vs);
    }

    /**
     * Returns a collection of values that represents the property names.
     * Properties that are definitely absent are ignored.
     */
    public Collection<Value> getMaybePresentPropertyNames() {
        Collection<Value> vs = getMaybe().stream().map(PKey::toValue).collect(Collectors.toList());
        if (default_array_property.isMaybePresent()) {
            vs.add(Value.makeAnyStrUInt());
            vs.removeIf(prop -> prop.isMaybeSingleStr() && Strings.isArrayIndex(prop.getStr())); // no need to include the names that are covered by AnyStrUInt
        }
        if (default_nonarray_property.isMaybePresent()) {
            vs.add(Value.makeAnyStrNotUInt());
            vs.removeIf(prop -> prop.isMaybeSingleStr() && !Strings.isArrayIndex(prop.getStr())); // no need to include the names that are covered by AnyStrNotUInt
        }
        return vs;
    }

    /**
     * Returns all collected property names and values.
     */
    public Map<PKeys, Value> getProperties() {
        Map<PKeys, Value> m = newMap();
        for (Map.Entry<PKey, Value> me : properties.entrySet())
            m.put(me.getKey().toValue(), me.getValue());
        m.put(Value.makeAnyStrUInt(), default_array_property);
        m.put(Value.makeAnyStrNotUInt(), default_nonarray_property);
        return m;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjProperties that = (ObjProperties) o;
        return Objects.equals(properties, that.properties) &&
                Objects.equals(default_array_property, that.default_array_property) &&
                Objects.equals(default_nonarray_property, that.default_nonarray_property);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties, default_array_property, default_nonarray_property);
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
