/*
 * Copyright 2009-2016 Aarhus University
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

package dk.brics.tajs.js2flowgraph;

import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;

/**
 * Reference-type.
 */
public abstract class Reference {

    public enum Type {
        Variable, StaticProperty, DynamicProperty
    }

    /**
     * The type of the reference.
     */
    public final Type type;

    /**
     * The source location of the reference.
     */
    public final SourceLocation location;

    private Reference(Type type, SourceLocation location) {
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
    Reference changeSourceLocation(SourceLocation sourceLocation) {
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

    /**
     * A reference to a variable.
     */
    public static class Variable extends Reference {

        /**
         * The name of the variable.
         */
        public final String name;

        /**
         * Constructs a new variable reference.
         */
        Variable(String name, SourceLocation location) {
            super(Type.Variable, location);
            this.name = name;
        }
    }

    /**
     * Abstract super-class for property references.
     */
    public abstract static class Property extends Reference {

        /**
         * The base value reference.
         */
        public final Reference base;

        /**
         * The register where the base value is stored.
         */
        public final int baseRegister;

        private Property(Type type, Reference base, int baseRegister, SourceLocation location) {
            super(type, location);
            this.base = base;
            this.baseRegister = baseRegister;
        }
    }

    /**
     * A dynamic property reference.
     */
    public static class DynamicProperty extends Property {

        /**
         * The register where the property value is stored.
         */
        public final int propertyRegister;

        public final Reference property;

        /**
         * Constructs a new dynamic property reference.
         */
        DynamicProperty(Reference base, int baseRegister, Reference property, int propertyRegister, SourceLocation location) {
            super(Type.DynamicProperty, base, baseRegister, location);
            this.propertyRegister = propertyRegister;
            this.property = property;
        }
    }

    /**
     * A static property reference.
     */
    public static class StaticProperty extends Property {

        /**
         * The property name.
         */
        public final String propertyName;

        /**
         * Constructs a new static property reference.
         */
        StaticProperty(Reference base, int baseRegister, String propertyName, SourceLocation location) {
            super(Type.StaticProperty, base, baseRegister, location);
            this.propertyName = propertyName;
        }
    }
}
