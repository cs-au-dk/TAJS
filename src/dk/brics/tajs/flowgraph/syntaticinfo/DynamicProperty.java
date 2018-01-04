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

package dk.brics.tajs.flowgraph.syntaticinfo;

import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * A dynamic property reference.
 */
public class DynamicProperty extends Property {

    /**
     * The register where the property value is stored.
     */
    public final int propertyRegister;

    public final SyntacticReference property;

    /**
     * Constructs a new dynamic property reference.
     */
    public DynamicProperty(SyntacticReference base, int baseRegister, SyntacticReference property, int propertyRegister, SourceLocation location) {
        super(Type.DynamicProperty, base, baseRegister, location);
        this.propertyRegister = propertyRegister;
        this.property = property;
    }
}
