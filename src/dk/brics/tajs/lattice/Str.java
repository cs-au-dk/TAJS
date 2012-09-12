/*
 * Copyright 2012 Aarhus University
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
 * 'String' facet for abstract values.
 */
public interface Str {

	/**
	 * Returns true if this value is maybe any string.
	 */
	public boolean isMaybeAnyStr();

	/**
	 * Returns true if value is maybe a singleton string.
	 */
	public boolean isMaybeSingleStr();

	/**
	 * Returns true if this value is maybe *any* UInt string.
	 */
	public boolean isMaybeStrUInt();

	/**
	 * Returns true if this value is maybe *some* UInt string.
	 */
	public boolean isMaybeStrSomeUInt();

	/**
	 * Returns true if this value is maybe a non-UInt string.
	 */
	public boolean isMaybeStrSomeNonUInt();

	/**
	 * Returns true if this value is maybe any (unbounded) non-UInt number string, including Infinity, -Infinity, and NaN.
	 */
	public boolean isMaybeStrOtherNum();

	/**
	 * Returns true if this value is maybe any identifier string.
	 */
	public boolean isMaybeStrIdentifier();

	/**
	 * Returns true if this value is maybe any string consisting of identifier parts.
	 */
	public boolean isMaybeStrIdentifierParts();

	/**
	 * Returns true if this value is maybe a fixed nonempty string followed by identifier parts.
	 */
	public boolean isMaybeStrPrefixedIdentifierParts();

	/**
	 * Returns true if this value is maybe any non-number, non-identifier-parts string.
	 */
	public boolean isMaybeStrOther();

    /**
     * Returns true if this value maybe originates from a JSON source.
     */
    public boolean isMaybeStrJSON();

    /**
     * Returns true if this value is definitely originating from a JSON source.
     */
    public boolean isStrJSON();

    /**
     * Returns true if this value is definitely an identifier or identifier-parts string.
     */
    public boolean isStrIdentifierOrIdentifierParts();

    /**
     * Returns true if this value is definitely an identifier string.
     */
    public boolean isStrIdentifier();

	/**
	 * Returns true if this value is maybe any UInt string but not a non-UInt string.
	 */
	public boolean isMaybeStrOnlyUInt();
	
	/**
	 * Returns true if this value may be a non-string.
	 */
	public boolean isMaybeOtherThanStr();

	/**
	 * Returns true if this value is maybe a non-singleton string.
	 */
	public boolean isMaybeFuzzyStr();

	/**
	 * Returns the singleton string value, or null if definitely not a singleton string.
	 */
	public String getStr();

	/**
	 * Returns the prefix value, or null if definitely not a fixed nonempty string followed by identifier parts.
	 */
	public String getPrefix();

	/**
	 * Returns true if this value is definitely not a string.
	 */
	public boolean isNotStr();

	/**
	 * Constructs a value as the join of this value and maybe any string.
	 */
	public Value joinAnyStr();

	/**
	 * Constructs a value as the join of this value and maybe any UInt string.
	 */
	public Value joinAnyStrUInt();

	/**
	 * Constructs a value as the join of this value and maybe any non-UInt number string (excluding NaN and +/-Infinity).
	 */
	public Value joinAnyStrOtherNum();

	/**
	 * Constructs a value as the join of this value and maybe any identifier string.
	 */
	public Value joinAnyStrIdentifier();

	/**
	 * Constructs a value as the join of this value and maybe any identifier-parts string.
	 */
	public Value joinAnyStrIdentifierParts();

	/**
	 * Constructs a value as the join of this value and maybe any non-number, non-identifier-parts string (including NaN and +/-Infinity).
	 */
	public Value joinAnyStrOther();

	/**
	 * Constructs a value as the join of this value and the given concrete string.
	 */
	public Value joinStr(String v);
	
	/**
	 * Constructs a value as the join of this value and the given prefixed identifier-parts string.
	 */
	public Value joinPrefixedIdentifierParts(String v);
	
	/**
	 * Constructs a value from this value where only the string facet is considered.
	 */
	public Value restrictToStr();
	
	/**
	 * Constructs a value from this value but definitely not a string.
	 */
	public Value restrictToNotStr();
	
	/**
	 * Checks whether the given string is matched by this value.
	 */
	public boolean isMaybeStr(String s);
}
