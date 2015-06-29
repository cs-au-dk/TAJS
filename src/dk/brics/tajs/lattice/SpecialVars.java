/*
 * Copyright 2009-2015 Aarhus University
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

import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.util.Collections;

import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Variables relevant for context sensitivity.
 */
public class SpecialVars {

    /**
     * Map from functions to names of special variables used for context sensitivity.
     */
    private Map<Function, Set<String>> specialvars;

    /**
     * Constructs a new empty SpecialVars object.
     */
    public SpecialVars() {
        specialvars = Collections.newMap();
    }

    /**
     * Add elements to the map for context sensitivity.
     * Inner functions inherit special variables from outer functions.
     *
     * @param f   function
     * @param var variable that should be taken into account for context sensitivity
     */
    public void addToSpecialVars(Function f, String var) {
        Collections.addToMapSet(specialvars, f, var);
    }

    /**
     * Returns the names of the special variables for the given function.
     */
    public Set<String> getSpecialVars(Function f) {
        Set<String> vars = newSet();
        for (; f != null; f = f.getOuterFunction()) {
            Set<String> vs = specialvars.get(f);
            if (vs != null)
                vars.addAll(vs);
        }
        if (vars.isEmpty())
            vars = null;
        return vars;
    }
}
