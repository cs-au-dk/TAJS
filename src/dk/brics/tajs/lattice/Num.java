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

/**
 * 'Number' facet for abstract values.
 */
public interface Num {

    /**
     * Returns true if this value is maybe any number.
     */
    boolean isMaybeAnyNum();

    /**
     * Returns true if this value is maybe a singleton number, excluding NaN and +/-Inf.
     */
    boolean isMaybeSingleNum();

    /**
     * Returns true if this value is maybe a singleton UInt number.
     */
    boolean isMaybeSingleNumUInt();

    /**
     * Returns true if this value is maybe a non-singleton number or NaN or +/-Inf.
     */
    boolean isMaybeFuzzyNum();

    /**
     * Returns true if this value is maybe NaN.
     */
    boolean isMaybeNaN();

    /**
     * Returns true if this value is definitely NaN.
     */
    boolean isNaN();

    /**
     * Returns true if this value is maybe infinite.
     */
    boolean isMaybeInf();

    /**
     * Returns true if this value is definitely infinite.
     */
    boolean isInf();

    /**
     * Returns true is this value may be any positive UInt32.
     */
    boolean isMaybeNumUIntPos();

    /**
     * Returns true is this value may be the number 0.
     */
    boolean isMaybeZero();

    /**
     * Returns true if the given number is matched by this value.
     */
    boolean isMaybeNum(double num);

    /**
     * Returns true if this value is maybe any UInt number.
     */
    boolean isMaybeNumUInt();

    /**
     * Returns true if this value is maybe any non-UInt, non-Inf, and non-NaN number.
     */
    boolean isMaybeNumOther();

    /**
     * Returns the singleton number value, or null if definitely not a singleton number.
     */
    Double getNum();

    /**
     * Returns true if this value is definitely not a number.
     */
    boolean isNotNum();

    /**
     * Returns true if this value is maybe a non-number.
     */
    boolean isMaybeOtherThanNum();

    /**
     * Returns true if this value is maybe a non-UInt-number.
     */
    boolean isMaybeOtherThanNumUInt();

    /**
     * Returns true if this value is maybe any number but not NaN or infinite.
     */
    boolean isMaybeAnyNumNotNaNInf();

    /**
     * Returns true if this number value is maybe the same as the given one.
     */
    boolean isMaybeSameNumber(Value v);

    /**
     * Returns true if this number value is maybe the same as the given one when negated.
     */
    boolean isMaybeSameNumberWhenNegated(Value v);

    /**
     * Constructs a value as the join of this value and any number.
     */
    Value joinAnyNum();

    /**
     * Constructs a value as the join of this value and any UInt number.
     */
    Value joinAnyNumUInt();

    /**
     * Constructs a value as the join of this value and any non-UInt number (excluding NaN and +/-Infinity).
     */
    Value joinAnyNumOther();

    /**
     * Constructs a value as the join of this value and the given concrete number.
     */
    Value joinNum(double v);

    /**
     * Constructs a value as the join of this value and NaN.
     */
    Value joinNumNaN();

    /**
     * Constructs a value as the join of this value and +/-Inf.
     */
    Value joinNumInf();

    /**
     * Constructs a value as a copy of this value but definitely not NaN.
     */
    Value restrictToNotNaN();

    /**
     * Constructs a value as a copy of this value but definitely not +/- Infinity.
     */
    Value restrictToNotInf();

    /**
     * Constructs a value from this value where only the number facet is considered.
     */
    Value restrictToNum();

    /**
     * Constructs a value from this value but definitely not a number.
     */
    Value restrictToNotNum();

    /**
     * Constructs a value from this value but definitely not a UInt number.
     */
    Value restrictToNotNumUInt();

    /**
     * Constructs a value from this value but definitely not an "other" number.
     */
    Value restrictToNotNumOther();

    /**
     * Constructs a value from this value but definitely not zero.
     */
    Value restrictToNotNumZero();

    /**
     * Constructs a value from this value but definitely not +/- infinity.
     */
    Value restrictToNotNumInf();
}
