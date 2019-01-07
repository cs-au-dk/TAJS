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
import dk.brics.tajs.util.AnalysisException;

/**
 * Reference type.
 */
public abstract class SyntacticReference {
    /**
     * The type of the reference.
     */
    public final Type type;

    /**
     * The source location of the reference.
     */
    public final SourceLocation location;

    public SyntacticReference(Type type, SourceLocation location) {
        this.type = type;
        this.location = location;
    }

    /**
     * Casts this reference to a dynamic property reference.
     */
    public DynamicProperty asDynamicProperty() {
        return (DynamicProperty) this;
    }

    /**
     * Casts this reference to a static property reference.
     */
    public StaticProperty asStaticProperty() {
        return (StaticProperty) this;
    }

    /**
     * Casts this reference to a property reference.
     */
    public Property asProperty() {
        return (Property) this;
    }

    /**
     * Casts this reference to a variable reference.
     */
    public Variable asVariable() {
        return (Variable) this;
    }

    /**
     * Creates a new reference as a copy of this one but with the given source location.
     */
    SyntacticReference changeSourceLocation(SourceLocation sourceLocation) {
        switch (type) {
            case Variable:
                Variable variable = asVariable();
                return new Variable(variable.name, sourceLocation);
            case StaticProperty:
                StaticProperty staticProperty = asStaticProperty();
                return new StaticProperty(staticProperty.base, staticProperty.baseRegister, staticProperty.propertyName, sourceLocation);
            case DynamicProperty:
                DynamicProperty dynamicProperty = asDynamicProperty();
                return new DynamicProperty(dynamicProperty.base, dynamicProperty.baseRegister, dynamicProperty.property, dynamicProperty.propertyRegister, sourceLocation);
            default:
                throw new AnalysisException("Unexpected reference type");
        }
    }

    public enum Type {
        Variable, StaticProperty, DynamicProperty
    }
}
