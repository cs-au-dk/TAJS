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

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.DeepImmutable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Partitioned abstract value.
 */
public final class PartitionedValue extends Value implements Undef, Null, Bool, Num, Str, PKeys, DeepImmutable {

    /**
     * Partitioning of the abstract value.
     * Each partition value is generally more precise than the single value.
     * This map groups the partitions according to the abstract nodes (represents the program locations where the partitionings are introduced).
     * If no value is available in a Partitions map for a given PartitionsQualifier, the default value is 'none'.
     */
    @Nonnull
    private Map<AbstractNode, Partitions> partitions;

    /**
     * Constructs a new (not canonicalized) partitioned value.
     */
    private PartitionedValue(Value v, Map<AbstractNode, Partitions> partitions) {
        super(v);
        this.partitions = partitions;
        hashcode = computeHashCode();
        if (Options.get().isDebugOrTestEnabled())
            if (v instanceof PartitionedValue || partitions.values().stream().flatMap(e -> e.values().stream()).anyMatch(x -> x instanceof PartitionedValue))
                throw new AnalysisException("Unexpected PartitionValue");
    }

    protected int computeHashCode() {
        return 3 * super.computeHashCode() + partitions.hashCode();
    }

    /**
     * Constructs a new (canonicalized) partitioned value.
     * @param v single abstract value that overapproximates the partitions
     */
    private static PartitionedValue make(Value v, Map<AbstractNode, Map<PartitioningQualifier, Value>> partitions) {
        Map<AbstractNode, Partitions> transformedMap =
                partitions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Partitions.make(e.getValue())));
        canonicalizing = true;
        PartitionedValue canonicalize = Canonicalizer.get().canonicalize(new PartitionedValue(v, Canonicalizer.get().canonicalizeMap(transformedMap)));
        canonicalizing = false;
        return canonicalize;
    }

    /**
     * Constructs a new (canonicalized) partitioned value.
     * @return a partitioned value with a single abstract value that is the join of the partition values
     */
    public static PartitionedValue make(AbstractNode node, Map<PartitioningQualifier, Value> partitions) {
        Map<AbstractNode, Map<PartitioningQualifier, Value>> mp = newMap();
        mp.put(node, partitions);
        return make(mp);
    }

    /**
     * Constructs a new (canonicalized) partitioned value.
     * @return a partitioned value with a single abstract value that is the join of the partition values
     */
    public static PartitionedValue make(Map<AbstractNode, Map<PartitioningQualifier, Value>> partitions) {
        return make(Value.join(partitions.values().stream().flatMap(e -> e.values().stream()).collect(Collectors.toSet())), partitions);
    }

    /**
     * Constructs a value as the join of the given values.
     * @param widen if true, apply widening
     */
    public static Value join(Value v1, Value v2, boolean widen) {
        if (v1 instanceof PartitionedValue) {
            if (v2 instanceof PartitionedValue) {
                Set<AbstractNode> pns = newSet(((PartitionedValue)v1).partitions.keySet());
                pns.addAll(((PartitionedValue)v2).partitions.keySet());
                Map<AbstractNode, Map<PartitioningQualifier, Value>> resPartitionings = newMap();
                for (AbstractNode n : pns) {
                    // find the union of the partition qualifiers
                    Set<PartitioningQualifier> ps = newSet(((PartitionedValue) v1).getPartitionQualifiers(n));
                    ps.addAll(((PartitionedValue) v2).getPartitionQualifiers(n));
                    Map<PartitioningQualifier, Value> partitioningsForNode =
                            ps.stream().collect(Collectors.toMap(q -> q, q -> ((PartitionedValue)v1).getPartitionValue(n, q, false).joinSingleValue(((PartitionedValue)v2).getPartitionValue(n, q, false), widen)));
                    resPartitionings.put(n, partitioningsForNode);
                }
                return make(new Value(v1.joinSingleValue(v2, widen)), resPartitionings);
            } else {
                // v2 is not a PartitionValue, so just use v2 for each partition in this value
                return make(new Value(v1.joinSingleValue(v2, widen)), ((PartitionedValue)v1).applyForEachPartition((e, n) -> e.getValue().joinSingleValue(v2, widen)));
            }
        } else {
            if (v2 instanceof PartitionedValue) {
                // v1 is not a PartitionValue, so just use v1 for each partition in this value
                return make(new Value(v1.joinSingleValue(v2, widen)), ((PartitionedValue)v2).applyForEachPartition((e, n) -> v1.joinSingleValue(e.getValue(), widen)));
            } else {
                return v1.joinSingleValue(v2, widen);
            }
        }
    }

    /**
     * Constructs a value as the join of the given collection of values.
     */
    public static Value join(Collection<Value> values) {
        if (values.size() == 1)
            return values.iterator().next();
        Value r = null;
        for (Value v : values) {
            if (r == null) {
                if (v instanceof PartitionedValue)
                    r = ((PartitionedValue) v).makeMutable();
                else
                    r = new Value(v);
            } else {
                if (r instanceof PartitionedValue || !(v instanceof PartitionedValue))
                    joinMutable(r, v);
                else { // r is single value and v is partitioned value
                    Value t = ((PartitionedValue) v).makeMutable();
                    joinMutable(t, r);
                    r = t;
                }
            }
        }
        if (r == null)
            r = makeNone();
        if (r instanceof PartitionedValue) // canonicalize partition values
            ((PartitionedValue) r).partitions = ((PartitionedValue)r).partitions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Partitions.make(e.getValue().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e2 -> canonicalize(e2.getValue()))))));
        return canonicalize(r);
    }

    /**
     * Creates a mutable copy of this PartitionedValue.
     */
    private PartitionedValue makeMutable() {
        return new PartitionedValue(new Value(this), partitions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new Partitions(e.getValue().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e2 -> new Value(e2.getValue())))))));
    }

    /**
     * Joins v2 into v1, mutating v1.
     * If v2 is a PartitionedValue then v1 must also be a PartitionedValue.
     */
    private static void joinMutable(Value v1, Value v2) {
        if (v1 instanceof PartitionedValue) {
            if (v2 instanceof PartitionedValue) {
                Set<AbstractNode> pns = newSet(((PartitionedValue)v1).partitions.keySet());
                pns.addAll(((PartitionedValue)v2).partitions.keySet());
                for (AbstractNode n : pns) {
                    // find the union of the partition qualifiers
                    Set<PartitioningQualifier> ps = newSet(((PartitionedValue) v1).getPartitionQualifiers(n));
                    ps.addAll(((PartitionedValue) v2).getPartitionQualifiers(n));
                    ps.forEach(q -> ((PartitionedValue)v1).getPartitionValue(n, q, true).joinMutableSingleValue(((PartitionedValue)v2).getPartitionValue(n, q, false), false));
                }
                v1.joinMutableSingleValue(v2, false);
            } else {
                // v2 is not a PartitionValue, so just use v2 for each partition in this value
                v1.joinMutableSingleValue(v2, false);
                ((PartitionedValue)v1).applyMutableForEachPartition((e, n) -> e.getValue().joinMutableSingleValue(v2, false));
            }
        } else {
            if (v2 instanceof PartitionedValue) {
                throw new AnalysisException("joinMutable called with single value and partitioned value");
            } else {
                v1.joinMutableSingleValue(v2, false);
            }
        }
    }

    /**
     * Returns the node where the partitioning was introduced.
     */
    public Set<AbstractNode> getPartitionNodes() {
        return partitions.keySet();
    }

    /**
     * Creates an abstract value as a copy of this value but without the partitions.
     */
    public Value ignorePartitions() {
        return Value.canonicalize(new Value(this));
    }

    /**
     * Creates an abstract value as a copy of the given value but without the partitions.
     */
    public static Value ignorePartitions(Value v) {
        return v instanceof PartitionedValue ? ((PartitionedValue)v).ignorePartitions() : v;
    }

    /**
     * Returns the selected partition value.
     */
    private Value getPartitionValue(AbstractNode node, PartitioningQualifier q, boolean mutable) {
        Partitions partitionsForNode = partitions.get(node);
        if (partitionsForNode == null) {
            return ignorePartitions();
        }
        Value res = partitionsForNode.get(q);
        if (res == null) {
            if (mutable) {
                Value mutableValue = new Value();
                partitions.get(node).put(q, mutableValue);
                res = mutableValue;
            } else {
                res = Value.makeNone();
            }
        }
        return res;
    }

    /**
     * Returns the selected partition value.
     */
    public Value getPartition(AbstractNode n, PartitioningQualifier q) {
        Partitions partitionsAtNode = partitions.get(n);
        if (partitionsAtNode == null)
            return ignorePartitions();
        Value p = partitionsAtNode.get(q);
        if (p == null)
            return Value.makeNone();
        return p;
    }

    /**
     * Returns the selected partition value, or the full value if not partitioned.
     */
    public static Value getPartition(Value v, AbstractNode n, PartitioningQualifier q) {
        return v instanceof PartitionedValue ? ((PartitionedValue)v).getPartition(n, q) : v;
    }

    /**
     * Adds additional partitions to this partitioned value.
     */
    public PartitionedValue addPartitions(PartitionedValue v) {
        Map<AbstractNode, Map<PartitioningQualifier, Value>> newPartitions = partitions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().partitions));
        v.partitions.forEach((key, value) -> newPartitions.put(key, value.partitions));
        return make(new Value(this), newPartitions);
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!Canonicalizer.get().isCanonicalizing())
            return this == obj;
        if (obj == this)
            return true;
        if (!(obj instanceof PartitionedValue))
            return false;
        PartitionedValue v = (PartitionedValue) obj;
        return super.equals(v) && Objects.equals(partitions, v.partitions);
    }

    /**
     * Applies the given function to the single value and to each partition value.
     */
    private PartitionedValue applyFunction(Value v, BiFunction<Value, Value, Value> func) {
        if (v instanceof PartitionedValue) {
            // v is also a PartitionedValue, so for each partition in this value, look up the corresponding partition value in v
            return make(func.apply(new Value(this), v), applyForEachPartition((e, n) -> func.apply(e.getValue(), ((PartitionedValue) v).getPartitionValue(n, e.getKey(), false))));
        } else {
            return make(func.apply(new Value(this), v), applyForEachPartition((e, n) -> func.apply(e.getValue(), v)));
        }
    }

    /**
     * Applies the given function to the single value and each partition value.
     */
    private PartitionedValue applyFunction(Function<Value, Value> func) {
        return make(func.apply(new Value(this)), applyForEachPartition((e, n) -> func.apply(e.getValue())));
    }

    /**
     * Applies the given function to each partition value.
     */
    private Map<AbstractNode, Map<PartitioningQualifier, Value>> applyForEachPartition(BiFunction<Map.Entry<PartitioningQualifier, Value>, AbstractNode, Value> func) {
        return getPartitionNodes().stream().collect(Collectors.toMap(n -> n,
                        n -> partitions.get(n).entrySet().stream().collect(
                                Collectors.toMap(Map.Entry::getKey, e -> func.apply(e, n)))));
    }

    /**
     * Applies the given function to each partition value.
     */
    private void applyMutableForEachPartition(BiConsumer<Map.Entry<PartitioningQualifier, Value>, AbstractNode> func) {
        getPartitionNodes().forEach(n -> partitions.get(n).entrySet().forEach(e -> func.accept(e, n)));
    }

    /**
     * Returns all the partition qualifiers.
     */
    public Map<AbstractNode, Set<PartitioningQualifier>> getPartitionQualifiers() {
        return partitions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().keySet()));
    }

    /**
     * Returns the set of all partition qualifiers used by this partitioned value.
     */
    public Set<PartitioningQualifier> getPartitionQualifiers(AbstractNode n) {
        Partitions partitionsForNode = partitions.get(n);
        if (partitionsForNode == null)
            return Collections.emptySet();
        return partitionsForNode.keySet();
    }

    @Override
    public String toString() {
        return super.toString() + "[partitions: " + partitions.values() + "]"; // omitting keys since they are also in values
    }

    @Override
    public Value makePolymorphic(ObjectProperty prop) {
        return applyFunction(v -> v.makePolymorphic(prop));
    }

    @Override
    public Value makeNonPolymorphic() {
        return applyFunction(Value::makeNonPolymorphic);
    }

    @Override
    public Value joinModified() {
        return applyFunction(Value::joinModified);
    }

    @Override
    public Value restrictToNotModified() {
        return applyFunction(Value::restrictToNotModified);
    }

    @Override
    public Value restrictToNotAbsent() {
        return applyFunction(Value::restrictToNotAbsent);
    }

    @Override
    public Value restrictToGetterSetter() {
        return applyFunction(Value::restrictToGetterSetter);
    }

    @Override
    public Value restrictToGetter() {
        return applyFunction(Value::restrictToGetter);
    }

    @Override
    public Value restrictToSetter() {
        return applyFunction(Value::restrictToSetter);
    }

    @Override
    public Value restrictToNotGetterSetter() {
        return applyFunction(Value::restrictToNotGetterSetter);
    }

    @Override
    public Value restrictToNotGetter() {
        return applyFunction(Value::restrictToNotGetter);
    }

    @Override
    public Value restrictToNotSetter() {
        return applyFunction(Value::restrictToNotSetter);
    }

    @Override
    public Value joinAbsent() {
        return applyFunction(Value::joinAbsent);
    }

    @Override
    public Value joinAbsentModified() {
        return applyFunction(Value::joinAbsentModified);
    }

    @Override
    public Value removeAttributes() {
        return applyFunction(Value::removeAttributes);
    }

    @Override
    public Value setAttributes(Value from) {
        return applyFunction(from, Value::setAttributes);
    }

    @Override
    public Value setBottomPropertyData() {
        return applyFunction(Value::setBottomPropertyData);
    }

    @Override
    public Value setDontEnum() {
        return applyFunction(Value::setDontEnum);
    }

    @Override
    public Value setNotDontEnum() {
        return applyFunction(Value::setNotDontEnum);
    }

    @Override
    public Value joinNotDontEnum() {
        return applyFunction(Value::joinNotDontEnum);
    }

    @Override
    public Value setDontDelete() {
        return applyFunction(Value::setDontDelete);
    }

    @Override
    public Value setNotDontDelete() {
        return applyFunction(Value::setNotDontDelete);
    }

    @Override
    public Value joinNotDontDelete() {
        return applyFunction(Value::joinNotDontDelete);
    }

    @Override
    public Value setReadOnly() {
        return applyFunction(Value::setReadOnly);
    }

    @Override
    public Value setNotReadOnly() {
        return applyFunction(Value::setNotReadOnly);
    }

    @Override
    public Value joinNotReadOnly() {
        return applyFunction(Value::joinNotReadOnly);
    }

    @Override
    public Value setAttributes(boolean dontenum, boolean dontdelete, boolean readonly) {
        return applyFunction(v1 -> v1.setAttributes(dontenum, dontdelete, readonly));
    }

    @Override
    public void diff(Value old, StringBuilder b) {
        super.diff(old, b); //TODO: Incorporate partitions (for both 'this' and 'old')?
    }

    @Override
    public Value joinMeta(Value v) {
        return applyFunction(v, Value::joinMeta);
    }

    @Override
    public Value joinUndef() {
        return applyFunction(Value::joinUndef);
    }

    @Override
    public Value restrictToNotUndef() {
        return applyFunction(Value::restrictToNotUndef);
    }

    @Override
    public Value restrictToUndef() {
        return applyFunction(Value::restrictToUndef);
    }

    @Override
    public Value joinNull() {
        return applyFunction(Value::joinNull);
    }

    @Override
    public Value restrictToNotNull() {
        return applyFunction(Value::restrictToNotNull);
    }

    @Override
    public Value restrictToNull() {
        return applyFunction(Value::restrictToNull);
    }

    @Override
    public Value restrictToNotNullNotUndef() {
        return applyFunction(Value::restrictToNotNullNotUndef);
    }

    @Override
    public Value joinAnyBool() {
        return applyFunction(Value::joinAnyBool);
    }

    @Override
    public Value joinBool(boolean x) {
        return applyFunction(v -> v.joinBool(x));
    }

    @Override
    public Value joinBool(Value x) {
        return applyFunction(x, Value::joinBool);
    }

    @Override
    public Value restrictToNotBool() {
        return applyFunction(Value::restrictToNotBool);
    }

    @Override
    public Value restrictToBool() {
        return applyFunction(Value::restrictToBool);
    }

    @Override
    public Value restrictToTruthy() {
        return applyFunction(Value::restrictToTruthy);
    }

    @Override
    public Value restrictToFalsy() {
        return applyFunction(Value::restrictToFalsy);
    }

    @Override
    public Value restrictToStrBoolNum() {
        return applyFunction(Value::restrictToStrBoolNum);
    }

    @Override
    public Value joinAnyNum() {
        return applyFunction(Value::joinAnyNum);
    }

    @Override
    public Value joinAnyNumUInt() {
        return applyFunction(Value::joinAnyNumUInt);
    }

    @Override
    public Value joinAnyNumOther() {
        return applyFunction(Value::joinAnyNumOther);
    }

    @Override
    public Value restrictToNotNaN() {
        return applyFunction(Value::restrictToNotNaN);
    }

    @Override
    public Value restrictToNotInf() {
        return applyFunction(Value::restrictToNotInf);
    }

    @Override
    public Value joinNum(double v) {
        return applyFunction(val -> val.joinNum(v));
    }

    @Override
    public Value joinNumNaN() {
        return applyFunction(Value::joinNumNaN);
    }

    @Override
    public Value joinNumInf() {
        return applyFunction(Value::joinNumInf);
    }

    @Override
    public Value restrictToNum() {
        return applyFunction(Value::restrictToNum);
    }

    @Override
    public Value restrictToNotNum() {
        return applyFunction(Value::restrictToNotNum);
    }

    @Override
    public Value restrictToNotNumUInt() {
        return applyFunction(Value::restrictToNotNumUInt);
    }

    @Override
    public Value restrictToNotNumOther() {
        return applyFunction(Value::restrictToNotNumOther);
    }

    @Override
    public Value restrictToNotSymbol() {
        return applyFunction(Value::restrictToNotSymbol);
    }

    @Override
    public Value restrictToSymbol() {
        return applyFunction(Value::restrictToSymbol);
    }

    @Override
    public Value joinAnyStr() {
        return applyFunction(Value::joinAnyStr);
    }

    @Override
    public Value joinAnyStrUInt() {
        return applyFunction(Value::joinAnyStrUInt);
    }

    @Override
    public Value joinAnyStrOtherNum() {
        return applyFunction(Value::joinAnyStrOtherNum);
    }

    @Override
    public Value joinAnyStrIdentifier() {
        return applyFunction(Value::joinAnyStrIdentifier);
    }

    @Override
    public Value joinAnyStrIdentifierParts() {
        return applyFunction(Value::joinAnyStrIdentifierParts);
    }

    @Override
    public Value joinAnyStrOther() {
        return applyFunction(Value::joinAnyStrOther);
    }

    @Override
    public Value joinStr(String s) {
        return applyFunction(v -> v.joinStr(s));
    }

    @Override
    public Value joinPrefix(String s) {
        return applyFunction(v -> v.joinPrefix(s));
    }

    @Override
    public Value restrictToStr() {
        return applyFunction(Value::restrictToStr);
    }

    @Override
    public Value restrictToNotStr() {
        return applyFunction(Value::restrictToNotStr);
    }

    @Override
    public Value restrictToNotStrIdentifierParts() {
        return applyFunction(Value::restrictToNotStrIdentifierParts);
    }

    @Override
    public Value restrictToNotStrPrefix() {
        return applyFunction(Value::restrictToNotStrPrefix);
    }

    @Override
    public Value restrictToNotStrUInt() {
        return applyFunction(Value::restrictToNotStrUInt);
    }

    @Override
    public Value restrictToNotStrOtherNum() {
        return applyFunction(Value::restrictToNotStrOtherNum);
    }

    @Override
    public Value restrictToNotStrings(Set<String> strings) {
        return applyFunction(v -> v.restrictToNotStrings(strings));
    }

    @Override
    public Value forgetExcludedIncludedStrings() {
        return applyFunction(Value::forgetExcludedIncludedStrings);
    }

    @Override
    public Value joinObject(ObjectLabel objlabel) {
        return applyFunction(v -> v.joinObject(objlabel));
    }

    @Override
    public Value restrictToNonSymbolObject() {
        return applyFunction(Value::restrictToNonSymbolObject);
    }

    @Override
    public Value restrictToTypeofObject() {
        return applyFunction(Value::restrictToTypeofObject);
    }

    @Override
    public Value restrictToNotTypeofObject() {
        return applyFunction(Value::restrictToNotTypeofObject);
    }

    @Override
    public Value restrictToFunction() {
        return applyFunction(Value::restrictToFunction);
    }

    @Override
    public Value restrictToNotFunction() {
        return applyFunction(Value::restrictToNotFunction);
    }

    @Override
    public Value restrictToNotObject() {
        return applyFunction(Value::restrictToNotObject);
    }

    @Override
    public Value removeObjects(Set<ObjectLabel> objs) {
        return applyFunction(v1 -> v1.removeObjects(objs));
    }

    @Override
    public Value makeGetter() {
        return applyFunction(Value::makeGetter);
    }

    @Override
    public Value makeSetter() {
        return applyFunction(Value::makeSetter);
    }

    @Override
    public Value summarize(Summarized s) {
        return applyFunction(v1 -> v1.summarize(s));
    }

    @Override
    public Value makeExtendedScope() {
        return applyFunction(Value::makeExtendedScope);
    }

    @Override
    public Value replaceObjectLabel(ObjectLabel oldlabel, ObjectLabel newlabel) {
        return applyFunction(v1 -> v1.replaceObjectLabel(oldlabel, newlabel));
    }

    @Override
    public Value restrictToAttributes() {
        return applyFunction(Value::restrictToAttributes);
    }

    @Override
    public Value restrictToNonAttributes() {
        return applyFunction(Value::restrictToNonAttributes);
    }

    @Override
    public Value replaceValue(Value v) {
        return applyFunction(v, Value::replaceValue);
    }

    @Override
    public Value restrictToNotNumZero() {
        return applyFunction(Value::restrictToNotNumZero);
    }

    @Override
    public Value restrictToNotNumInf() {
        return applyFunction(Value::restrictToNotNumInf);
    }

    @Override
    public Value restrictToStrictEquals(Value v) { // TODO: consider the case where v is a PartitionedValue and this is not, upgrade to PartitionedValue? (also for other methods with a Value parameter)
        return applyFunction(v, Value::restrictToStrictEquals);
    }

    @Override
    public Value restrictToStrictNotEquals(Value v) {
        return applyFunction(v, Value::restrictToStrictNotEquals);
    }

    @Override
    public Value restrictToLooseEquals(Value v) {
        return applyFunction(v, Value::restrictToLooseEquals);
    }

    @Override
    public Value restrictToLooseNotEquals(Value v) {
        return applyFunction(v, Value::restrictToLooseNotEquals);
    }

    @Override
    public Value joinGettersSetters(Value v) {
        return applyFunction(v, Value::joinGettersSetters);
    }

    /**
     * Map from partition qualifier to value.
     */
    static private class Partitions implements DeepImmutable {

        private final Map<PartitioningQualifier, Value> partitions;

        private Partitions(Map<PartitioningQualifier, Value> partitions) {
            this.partitions = partitions;
        }

        static Partitions make(Map<PartitioningQualifier, Value> partitions) {
            return Canonicalizer.get().canonicalize(new Partitions(Canonicalizer.get().canonicalizeMap(partitions)));
        }

        public Value get(PartitioningQualifier q) {
            return partitions.get(q);
        }

        public Collection<Value> values() {
            return partitions.values();
        }

        public Set<Map.Entry<PartitioningQualifier, Value>> entrySet() {
            return partitions.entrySet();
        }

        public Set<PartitioningQualifier> keySet() {
            return partitions.keySet();
        }

        public void put(PartitioningQualifier p, Value v) {
            partitions.put(p, v);
        }

        @Override
        public boolean equals(Object o) {
            if (!Canonicalizer.get().isCanonicalizing())
                return this == o;
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            return Objects.equals(partitions, ((Partitions) o).partitions);
        }

        @Override
        public int hashCode() {
            return Objects.hash(partitions);
        }

        @Override
        public String toString() {
            return partitions.toString();
        }
    }
}
