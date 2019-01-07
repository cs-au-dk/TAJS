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

import dk.brics.tajs.util.AnalysisException;

/**
 * Represention of a value restriction.
 */
public class Restriction {

    private Value val;

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
    }

    private Kind kind;

    /**
     * Constructs a new restriction object of the given kind.
     */
    public Restriction(Kind kind) {
        this.kind = kind;
    }

    /**
     * Sets the value (only for {@link Kind#STRICT_EQUAL}, {@link Kind#STRICT_NOT_EQUAL} {@link Kind#LOOSE_EQUAL}, and {@link Kind#LOOSE_NOT_EQUAL}).
     */
    public Restriction set(Value v) {
        val = v;
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
            default:
                return v;
        }
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
            default: // no need for negated versions of NOT_NULL_UNDEF and FUNCTION
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
}

