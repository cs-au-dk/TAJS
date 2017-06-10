/*
 * Copyright 2009-2017 Aarhus University
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
 * 'Null' facet for abstract values.
 */
public interface Null {

    /**
     * Returns true if this value is maybe null.
     */
    boolean isMaybeNull();

    /**
     * Returns true if this value is definitely not null.
     */
    boolean isNotNull();

    /**
     * Returns true if this value is maybe some other than null.
     */
    boolean isMaybeOtherThanNull();

    /**
     * Constructs a value as the join of this value and null.
     */
    Value joinNull();

    /**
     * Constructs a value as a copy of this value but definitely not null.
     */
    Value restrictToNotNull();

    /**
     * Constructs a value as a copy of this value but only considering its null facet.
     */
    Value restrictToNull();
}