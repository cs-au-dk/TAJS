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

import java.util.Set;

/**
 * 'String or symbol' facet for abstract values.
 */
public interface PKeys extends Str { // XXX: rename to StrOrSymbol

    /**
     * Returns true if this value may be a symbol.
     */
    public boolean isMaybeSymbol();

    /**
     * Returns true if this value may be a non-property-key.
     */
    boolean isMaybeOtherThanStrOrSymbol();

    /**
     * Returns true if this value is maybe a singleton string or a singleton symbol (but not both).
     */
    boolean isMaybeSingleStrOrSymbol();

    /**
     * Returns true if this value is maybe a non-singleton string or a non-singleton symbol.
     */
    boolean isMaybeFuzzyStrOrSymbol();

    /**
     * Returns the (immutable) set of object labels representing symbols.
     */
    Set<ObjectLabel> getSymbols();
}
