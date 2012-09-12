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
 * 'Number' facet for abstract values.
 */
public interface Num {

	/**
	 * Returns true if this value is maybe any number.
	 */
	public boolean isMaybeAnyNum();
	
	/**
	 * Returns true if this value is maybe a singleton number, excluding NaN and +/-Inf.
	 */
	public boolean isMaybeSingleNum();

	/**
	 * Returns true if this value is maybe a singleton UInt number.
	 */
	public boolean isMaybeSingleNumUInt();

	/**
	 * Returns true if this value is maybe a non-singleton number or NaN or +/-Inf.
	 */
	public boolean isMaybeFuzzyNum();

	/**
	 * Returns true if this value is maybe NaN.
	 */
	public boolean isMaybeNaN();
	
	/**
	 * Returns true if this value is definitely NaN.
	 */
	public boolean isNaN();
	
	/**
	 * Returns true if this value is maybe infinite.
	 */
	public boolean isMaybeInf();
	
	/**
	 * Returns true if this value is maybe any UInt number.
	 */
	public boolean isMaybeNumUInt();
	
	/**
	 * Returns true if this value is maybe any non-UInt, non-Inf, non-NaN number.
	 */
	public boolean isMaybeNumOther();
	
	/**
	 * Returns the singleton number value, or null if definitely not a singleton number.
	 */
	public Double getNum();

	/**
	 * Returns true if this value is definitely not a number.
	 */
	public boolean isNotNum(); 
	
	/**
	 * Returns true if this value is maybe a non-number.
	 */
	public boolean isMaybeOtherThanNum();
	
	/**
	 * Returns true if this value is maybe not an UInt-number.
	 */
	public boolean isMaybeOtherThanNumUInt();
	
	/**
	 * Constructs a value as the join of this value and maybe any number.
	 */
	public Value joinAnyNum();
	
	/**
	 * Constructs a value as the join of this value and maybe any UInt number.
	 */
	public Value joinAnyNumUInt();
	
	/**
	 * Constructs a value as the join of this value and maybe any non-UInt number (excluding NaN and +/-Infinity).
	 */
	public Value joinAnyNumOther();
	
	/**
	 * Constructs a value as the join of this value and the given concrete number.
	 */
	public Value joinNum(double v);

	/**
	 * Constructs a value as the join of this value and NaN.
	 */
	public Value joinNumNaN();

	/**
	 * Constructs a value as the join of this value and +/-Inf.
	 */
	public Value joinNumInf();

	/**
	 * Constructs a value as a copy of this value but definitely not NaN.
	 */
	public Value restrictToNotNaN();
	
	/**
	 * Constructs a value from this value where only the number facet is considered.
	 */
	public Value restrictToNum();
	
	/**
	 * Constructs a value from this value but definitely not a number.
	 */
	public Value restrictToNotNum();
}
