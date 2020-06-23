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

import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Represention of a value restriction.
 */
public class Restriction {

    private Value val;

    private FunctionTypeSignatures signatures;

    private Collection<Restriction> union;

    private ObjectLabel.Kind objkind;

    public Value getValue() {
        return val;
    }

    public FunctionTypeSignatures getFunctionTypeSignatures() {
        return signatures;
    }

    public Collection<Restriction> getUnion() {
        return union;
    }

    public enum Kind {

        /**
         * Value is truthy.
         */
        TRUTHY,

        /**
         * Value is falsy.
         */
        FALSY,

        /**
         * Value is not null or undefined (or absent).
         */
        NOT_NULL_UNDEF,

        /**
         * Value is a function.
         */
        FUNCTION,

        /**
         * Value is a function with a TypeScript type signature.
         */
        TYPED_FUNCTION,

        /**
         * Value is strictly equal to the selected value.
         */
        STRICT_EQUAL,

        /**
         * Value is strictly not equal to the selected value.
         */
        STRICT_NOT_EQUAL,

        /**
         * Value is loosely equal to the selected value.
         */
        LOOSE_EQUAL,

        /**
         * Value is loosely not equal to the selected value.
         */
        LOOSE_NOT_EQUAL,

        /**
         * Type-of value is "function".
         */
        TYPEOF_FUNCTION,

        /**
         * Type-of value is "object".
         */
        TYPEOF_OBJECT,

        /**
         * Type-of value is "symbol".
         */
        TYPEOF_SYMBOL,

        /**
         * Type-of value is not "function".
         */
        NOT_TYPEOF_FUNCTION,

        /**
         * Type-of value is not "object".
         */
        NOT_TYPEOF_OBJECT,

        /**
         * Type-of value is not "symbol".
         */
        NOT_TYPEOF_SYMBOL,

        /**
         * Value has the partitions of the selected value.
         */
        PARTITIONS,

        /**
         * Object.prototype.toString of value is the selected value.
         */
        OBJECT_TO_STRING,

        /**
         * Object.prototype.toString of value is not the selected value.
         */
        NOT_OBJECT_TO_STRING,

        /**
         * Type-of value is "object" or "function".
         */
        TYPEOF_OBJECT_OR_FUNCTION,

        /**
         * Value is restricted to the selected union of restrictions.
         */
        UNION,

        /**
         * Value is object of the select kind.
         */
        OBJKIND,
    }

    private Kind kind;

    /**
     * Constructs a new restriction object of the given kind.
     */
    public Restriction(Kind kind) {
        this.kind = kind;
    }

    /**
     * Sets the value (only for {@link Kind#STRICT_EQUAL}, {@link Kind#STRICT_NOT_EQUAL} {@link Kind#LOOSE_EQUAL}, {@link Kind#LOOSE_NOT_EQUAL},
     * and {@link Kind#OBJECT_TO_STRING}).
     */
    public Restriction set(Value v) {
        val = v;
        return this;
    }

    /**
     * Sets the function type signatures (only for {@link Kind#TYPED_FUNCTION}).
     */
    public Restriction set(FunctionTypeSignatures s) {
        signatures = s;
        return this;
    }

    /**
     * Sets the union of restrictions (only for {@link Kind#UNION}).
     */
    public Restriction set(Collection<Restriction> u) {
        union = u;
        return this;
    }

    /**
     * Sets the object kind (only for {@link Kind#OBJKIND}).
     */
    public Restriction set(ObjectLabel.Kind k) {
        objkind = k;
        return this;
    }

    public Kind getKind() {
        return kind;
    }

    /**
     * Restricts the value according to this restriction.
     */
    public Value restrict(Value v) {
        if (v.isUnknown())
            throw new AnalysisException("Unexpected 'unknown' value!");
        switch (kind) {
            case TRUTHY:
                return v.restrictToTruthy();
            case FALSY:
                return v.restrictToFalsy();
            case NOT_NULL_UNDEF:
                return v.restrictToNotNullNotUndef().restrictToNotAbsent();
            case FUNCTION:
                return v.restrictToFunction().joinGettersSetters(v).restrictToNotNullNotUndef().restrictToNotAbsent();
            case TYPED_FUNCTION:
                Value v2 = v.restrictToFunction().joinGettersSetters(v).addFunctionTypeSignatures(signatures);
                Value v3 = v2.restrictToNotNullNotUndef().restrictToNotAbsent();
                if (v3.isNone())
                    v3 = v2; // don't remove null, undef, and absent if it leads to bottom (to be a little resilient to bugs in TypeScript declaration files)
                return v3;
            case STRICT_EQUAL:
                return v.restrictToStrictEquals(val);
            case STRICT_NOT_EQUAL:
                return v.restrictToStrictNotEquals(val);
            case LOOSE_EQUAL:
                return v.restrictToLooseEquals(val);
            case LOOSE_NOT_EQUAL:
                return v.restrictToLooseNotEquals(val);
            case TYPEOF_FUNCTION:
                return v.restrictToFunction().restrictToNotAbsent().joinGettersSetters(v);
            case TYPEOF_OBJECT:
                return v.restrictToTypeofObject().joinMeta(v).restrictToNotAbsent().joinGettersSetters(v);
            case TYPEOF_SYMBOL:
                return v.restrictToSymbol().restrictToNotAbsent().joinGettersSetters(v);
            case NOT_TYPEOF_FUNCTION:
                return v.restrictToNotFunction();
            case NOT_TYPEOF_OBJECT:
                return v.restrictToNotTypeofObject();
            case NOT_TYPEOF_SYMBOL:
                return v.restrictToNotSymbol();
            case PARTITIONS:
                if (!(val instanceof PartitionedValue))
                    throw new AnalysisException("PARTITIONS should only be used with partitioned values");
                if (v instanceof PartitionedValue) {// v already partitioned, add the new partitioning
                    if (Collections.disjoint(((PartitionedValue) val).getPartitionNodes(), ((PartitionedValue) v).getPartitionNodes()))
                        return ((PartitionedValue) v).addPartitions((PartitionedValue) val.joinMeta(v));
                    else
                        return v.join(val);
                } else // v not already partitioned
                    return val.joinMeta(v);
            case OBJECT_TO_STRING:
                if (val.isMaybeSingleStr()) {
                    Optional<ObjectLabel.Kind> kind = getObjectLabelKindFromString(val.getStr());
                    if (kind.isPresent()) {
                        Value value = Value.makeObject(v.getObjectLabels().stream().filter(obj -> obj.getKind() == kind.get()).collect(Collectors.toSet())).joinMeta(v);
                        switch (kind.get()) {
                            case BOOLEAN:
                                value = value.join(v.restrictToBool());
                                break;
                            case NUMBER:
                                value = value.join(v.restrictToNum());
                                break;
                            case STRING:
                                value = value.join(v.restrictToStr());
                                break;
                        }
                        return value;
                    }
                }
                return v;
            case NOT_OBJECT_TO_STRING:
                if (val.isMaybeSingleStr()) {
                    Optional<ObjectLabel.Kind> kind = getObjectLabelKindFromString(val.getStr());
                    if (kind.isPresent()) {
                        Value value = v.removeObjects(v.getObjectLabels().stream().filter(obj -> obj.getKind() == kind.get()).collect(Collectors.toSet()));
                        switch (kind.get()) {
                            case BOOLEAN:
                                value = value.restrictToNotBool();
                                break;
                            case NUMBER:
                                value = value.restrictToNotNum();
                                break;
                            case STRING:
                                value = value.restrictToNotStr();
                                break;
                        }
                        return value;
                    }
                }
                return v;
            case TYPEOF_OBJECT_OR_FUNCTION:
                return v.restrictToTypeofObject().joinMeta(v).join(v.restrictToFunction().joinGettersSetters(v)).restrictToNotNullNotUndef().restrictToNotAbsent();
            case UNION:
                return Value.join(union.stream().map(r -> r.restrict(v)).collect(java.util.stream.Collectors.toList()));
            case OBJKIND:
                return v.restrictToObjKind(objkind);
            default:
                return v;
        }
    }

    /**
     * Finds the object label kind described by str.
     */
    private Optional<ObjectLabel.Kind> getObjectLabelKindFromString(String str) {
        String kindName = str.substring(str.indexOf(' ') + 1, str.length() - 1);
        return Arrays.stream(ObjectLabel.Kind.values()).filter(k -> k.toString().equals(kindName)).findAny();
    }

    /**
     * Returns the negated restriction, or null if not supported.
     */
    public Restriction negate() {
        switch (kind) {
            case TRUTHY:
                return new Restriction(Kind.FALSY);
            case FALSY:
                return new Restriction(Kind.TRUTHY);
            case STRICT_EQUAL:
                return new Restriction(Kind.STRICT_NOT_EQUAL).set(val);
            case STRICT_NOT_EQUAL:
                return new Restriction(Kind.STRICT_EQUAL).set(val);
            case LOOSE_EQUAL:
                return new Restriction(Kind.LOOSE_NOT_EQUAL).set(val);
            case LOOSE_NOT_EQUAL:
                return new Restriction(Kind.LOOSE_EQUAL).set(val);
            case TYPEOF_FUNCTION:
                return new Restriction(Kind.NOT_TYPEOF_FUNCTION);
            case TYPEOF_OBJECT:
                return new Restriction(Kind.NOT_TYPEOF_OBJECT);
            case TYPEOF_SYMBOL:
                return new Restriction(Kind.NOT_TYPEOF_SYMBOL);
            case NOT_TYPEOF_FUNCTION:
                return new Restriction(Kind.TYPEOF_FUNCTION);
            case NOT_TYPEOF_OBJECT:
                return new Restriction(Kind.TYPEOF_OBJECT);
            case NOT_TYPEOF_SYMBOL:
                return new Restriction(Kind.TYPEOF_SYMBOL);
            case OBJECT_TO_STRING:
                return new Restriction(Kind.NOT_OBJECT_TO_STRING).set(val);
            case NOT_OBJECT_TO_STRING:
                return new Restriction(Kind.OBJECT_TO_STRING).set(val);
            default: // no need for negated versions of NOT_NULL_UNDEF, FUNCTION, TYPED_FUNCTION, TYPEOF_OBJECT_OR_FUNCTION, UNION
                return null;
        }
    }

    public static Restriction typeofToRestriction(String s) {
        switch (s) {
            case "undefined":
                return new Restriction(Kind.STRICT_EQUAL).set(Value.makeUndef());
            case "boolean":
                return new Restriction(Kind.STRICT_EQUAL).set(Value.makeAnyBool());
            case "number":
                return new Restriction(Kind.STRICT_EQUAL).set(Value.makeAnyNum());
            case "string":
                return new Restriction(Kind.STRICT_EQUAL).set(Value.makeAnyStr());
            case "object":
                return new Restriction(Kind.TYPEOF_OBJECT);
            case "symbol":
                return new Restriction(Kind.TYPEOF_SYMBOL);
            case "function":
                return new Restriction(Kind.TYPEOF_FUNCTION);
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "(" + kind + ", " + val + ", " + signatures + "," + union + ")";
    }
}
