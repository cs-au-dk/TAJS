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

import java.util.Set;

/**
 * 'String' facet for abstract values.
 */
public interface Str {

    /**
     * Returns true if this value is maybe any string (ignoring excluded strings).
     */
    boolean isMaybeAnyStr();

    /**
     * Returns true if value is maybe a singleton string.
     */
    boolean isMaybeSingleStr();

    /**
     * Returns true if this value is maybe any UInt string (ignoring excluded strings).
     */
    boolean isMaybeStrUInt();

    /**
     * Returns true if this value is maybe some UInt string.
     */
    boolean isMaybeStrSomeUInt();

    /**
     * Returns true if this value is maybe some numeric string.
     */
    boolean isMaybeStrSomeNumeric();

    /**
     * Returns true if this value is maybe a non-UInt string.
     */
    boolean isMaybeStrSomeNonUInt();

    /**
     * Returns true if this value is maybe a non-numeric string.
     */
    boolean isMaybeStrSomeNonNumeric();

    /**
     * Returns true if this value is maybe any (unbounded) non-UInt number string, including Infinity, -Infinity, and NaN (ignoring excluded strings).
     */
    boolean isMaybeStrOtherNum();

    /**
     * Returns true if this value is maybe any identifier string (ignoring excluded strings).
     */
    boolean isMaybeStrIdentifier();

    /**
     * Returns true if this value is maybe any string consisting of identifier parts,
     * ignoring identifier strings and UInt strings (and ignoring excluded strings).
     */
    boolean isMaybeStrOtherIdentifierParts();

    /**
     * Returns true if this value is maybe a fixed nonempty prefix string (ignoring excluded strings).
     */
    boolean isMaybeStrPrefix();

    /**
     * Returns true if this value is maybe any non-number, non-identifier-parts string (ignoring excluded strings).
     */
    boolean isMaybeStrOther();

    /**
     * Returns true if this value maybe originates from a JSON source.
     */
    boolean isMaybeStrJSON();

    /**
     * Returns true if this value is definitely originating from a JSON source.
     */
    boolean isStrJSON();

    /**
     * Returns true if this value is definitely an identifier-parts string.
     */
    boolean isStrIdentifierParts();

    /**
     * Returns true if this value is definitely an identifier string.
     */
    boolean isStrIdentifier();

    /**
     * Returns true if this value is maybe any UInt string but not a non-UInt string (ignoring excluded strings).
     */
    boolean isMaybeStrOnlyUInt();

    /**
     * Returns true if this value may be a non-string.
     */
    boolean isMaybeOtherThanStr();

    /**
     * Returns true if this value is maybe a non-singleton string.
     */
    boolean isMaybeFuzzyStr();

    /**
     * Returns the singleton string value.
     * Only to be called if {@link #isMaybeSingleStr()} returns true.
     */
    String getStr();

    /**
     * Returns the prefix value.
     * Only to be called if {@link #isMaybeStrPrefix()} returns true.
     */
    String getPrefix();

    /**
     * Returns true if this value is definitely not a string.
     */
    boolean isNotStr();

    /**
     * Constructs a value as the join of this value and any string.
     */
    Value joinAnyStr();

    /**
     * Constructs a value as the join of this value and any UInt string.
     */
    Value joinAnyStrUInt();

    /**
     * Constructs a value as the join of this value and any non-UInt number string (excluding NaN and +/-Infinity).
     */
    Value joinAnyStrOtherNum();

    /**
     * Constructs a value as the join of this value and any identifier string.
     */
    Value joinAnyStrIdentifier();

    /**
     * Constructs a value as the join of this value and any identifier-parts string.
     */
    Value joinAnyStrIdentifierParts();

    /**
     * Constructs a value as the join of this value and any non-number, non-identifier-parts string (including NaN and +/-Infinity).
     */
    Value joinAnyStrOther();

    /**
     * Constructs a value as the join of this value and the given concrete string.
     */
    Value joinStr(String v);

    /**
     * Constructs a value as the join of this value and the given prefix string.
     */
    Value joinPrefix(String v);

    /**
     * Constructs a value from this value where only the string facet is considered.
     */
    Value restrictToStr();

    /**
     * Constructs a value from this value but definitely not a string.
     */
    Value restrictToNotStr();

    /**
     * Constructs a value from this value but excluding the category of all strings that consist of identifier parts.
     */
    Value restrictToNotStrIdentifierParts();

    /**
     * Constructs a value from this value but excluding the category of all strings that consist of a fixed nonempty prefix string.
     */
    Value restrictToNotStrPrefix();

    /**
     * Constructs a value from this value but excluding the category of all UInt strings.
     */
    Value restrictToNotStrUInt();

    /**
     * Constructs a value from this value but excluding the category of all strings that
     * represent unbounded non-UInt32 numbers, including Infinity, -Infinity, and NaN.
     */
    Value restrictToNotStrOtherNum();

    /**
     * Checks whether the given string is matched by this value.
     */
    boolean isMaybeStr(String s);

//    /**
//     * Checks whether the given abstract string value is definitely different from this abstract string string.
//     * (Conservative, true means certainly yes, false means maybe no.)
//     * @throws AnalysisException if the abstract values are maybe non-strings
//     */
//    boolean isStrDisjoint(Str other);

    /**
     * Checks whether this string value may contain the given substring.
     * (Conservative, true means certainly yes, false means maybe no.)
     * @throws AnalysisException if the abstract values are maybe non-strings
     */
    boolean isStrMayContainSubstring(Str other);

    /**
     * Checks whether all strings represented by this abstract value contain a non-identifier character.
     * (Conservative, true means certainly yes, false means maybe no.)
     */
    boolean mustContainNonIdentifierCharacters();

    /**
     * Checks whether all strings represented by this abstract value contain only identifier characters.
     * (Conservative, true means certainly yes, false means maybe no.)
     */
    boolean mustOnlyBeIdentifierCharacters();

    /**
     * Returns the strings that are explicitly excluded, or null if none.
     */
    Set<String> getExcludedStrings();

    /**
     * Returns the strings that are explicitly included, or null if none.
     */
    Set<String> getIncludedStrings();

    /**
     * Constructs a value from this value but, if possible, with the given strings removed.
     */
    Value restrictToNotStrings(Set<String> strings);

    /**
     * Constructs a value from this value but with no excluded or included strings.
     */
    Value forgetExcludedIncludedStrings();

    /**
     * Returns true if the value contains only known strings (possibly beside values of other type).
     */
    boolean isMaybeAllKnownStr();

    /**
     * Returns the set of all known strings.
     * Only invoke if {@link #isMaybeAllKnownStr} returns true.
     */
    Set<String> getAllKnownStr();
}
