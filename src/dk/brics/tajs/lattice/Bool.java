/*
 * Copyright 2009-2013 Aarhus University
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
 * 'Boolean' facet for abstract values.
 */
public interface Bool {

	/**
	 * Returns true if this value is maybe any boolean.
	 */
	public boolean isMaybeAnyBool();

	/**
	 * Returns true if this value is maybe true.
	 */
	public boolean isMaybeTrueButNotFalse();

	/**
	 * Returns true if this value is maybe false.
	 */
	public boolean isMaybeFalseButNotTrue();
	
	/**
	 * Returns true if this value is maybe true.
	 */
	public boolean isMaybeTrue();

	/**
	 * Returns true if this value is maybe false.
	 */
	public boolean isMaybeFalse();

	/**
	 * Returns true if this value is definitely not a boolean.
	 */
	public boolean isNotBool(); 
	
	/**
	 * Returns true if this value is maybe a non-boolean.
	 */
	public boolean isMaybeOtherThanBool();
	
	/**
	 * Constructs a value as the join of this value and maybe any boolean.
	 */
	public Value joinAnyBool();

	/**
	 * Constructs a value as the join of this value and the given concrete boolean value.
	 */
	public Value joinBool(boolean x);

	/**
	 * Constructs a value as the join of this value and the given concrete boolean value.
	 */
	public Value joinBool(Value x);
}
