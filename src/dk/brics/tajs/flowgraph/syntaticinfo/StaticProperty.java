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

package dk.brics.tajs.flowgraph.syntaticinfo;

import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * A static property reference.
 */
public class StaticProperty extends Property {

    /**
     * The property name.
     */
    public final String propertyName;

    /**
     * Constructs a new static property reference.
     */
    public StaticProperty(SyntacticReference base, int baseRegister, String propertyName, SourceLocation location) {
        super(Type.StaticProperty, base, baseRegister, location);
        this.propertyName = propertyName;
    }
}
