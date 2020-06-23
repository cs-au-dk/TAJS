/*
 * Copyright 2009-2020 Aarhus University
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

package dk.brics.tajs.flowgraph.syntaticinfo;

import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * A reference to a variable.
 */
public class Variable extends SyntacticReference {

    /**
     * The name of the variable.
     */
    public final String name;

    /**
     * Constructs a new variable reference.
     */
    public Variable(String name, SourceLocation location) {
        super(Type.Variable, location);
        this.name = name;
    }
}
