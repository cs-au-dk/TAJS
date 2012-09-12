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

package dk.brics.tajs.unevalizer;

import java.util.Set;

/**
 * The interface that the Unevalizer uses for querying the driving analysis.
 */
public interface AnalyzerCallback {
	
    /**
     * Returns true if any variable in the set s might be a declared variable.
     */
    public boolean anyDeclared(Set<String> s);

    /**
     * Returns true if the variable s is definitely JSON Data.
     */
    public boolean isDefinitelyJSONData(String s);

    /**
     * Returns true if the variable s is definitely an integer.
     */
    public boolean isDefinitelyInteger(String s);

    /**
     * Returns true if the variable s is definitely a boolean.
     */
    public boolean isDefinitelyBoolean(String s);

    /**
     * Returns true if the variable s is a legal identifier fragment.
     */
    public boolean isDefinitelyIdentifierFragment(String s);

    /**
     * Returns true if the variable s is a legal identifier.
     */
    public boolean isDefinitelyIdentifier(String s);

    /**
     *  Get the set of identifiers bound in the non global scope
     */
    public Set<String> getNonGlobalIdentifiers();

    /**
     * Gives the full expression for the variable placeHolder. Useful for debugging purposes.
     */
    public String getFullExpression(String placeHolder);
}
