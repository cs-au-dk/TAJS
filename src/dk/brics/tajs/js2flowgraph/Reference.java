package dk.brics.tajs.js2flowgraph;

import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;

/**
 * Reference-type.
 */
abstract class Reference {

    enum Type {
        Variable, StaticProperty, DynamicProperty
    }

    /**
     * The type of the reference.
     */
    final Type type;

    /**
     * The source location of the reference.
     */
    final SourceLocation location;

    private Reference(Type type, SourceLocation location) {
        this.type = type;
        this.location = location;
    }

    /**
     * Casts this reference to a dynamic property reference.
     */
    DynamicProperty asDynamicProperty() {
        return (DynamicProperty) this;
    }

    /**
     * Casts this reference to a static property reference.
     */
    StaticProperty asStaticProperty() {
        return (StaticProperty) this;
    }

    /**
     * Casts this reference to a property reference.
     */
    Property asProperty() {
        return (Property) this;
    }

    /**
     * Casts this reference to a variable reference.
     */
    Variable asVariable() {
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
                return new DynamicProperty(dynamicProperty.base, dynamicProperty.baseRegister, dynamicProperty.propertyRegister, sourceLocation);
            default:
                throw new AnalysisException("Unexpected reference type");
        }
    }

    /**
     * A reference to a variable.
     */
    static class Variable extends Reference {

        /**
         * The name of the variable.
         */
        final String name;

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
    abstract static class Property extends Reference {

        /**
         * The base value reference.
         */
        final Reference base;

        /**
         * The register where the base value is stored.
         */
        final int baseRegister;

        private Property(Type type, Reference base, int baseRegister, SourceLocation location) {
            super(type, location);
            this.base = base;
            this.baseRegister = baseRegister;
        }
    }

    /**
     * A dynamic property reference.
     */
    static class DynamicProperty extends Property {

        /**
         * The register where the property value is stored.
         */
        final int propertyRegister;

        /**
         * Constructs a new dynamic property reference.
         */
        DynamicProperty(Reference base, int baseRegister, int propertyRegister, SourceLocation location) {
            super(Type.DynamicProperty, base, baseRegister, location);
            this.propertyRegister = propertyRegister;
        }
    }

    /**
     * A static property reference.
     */
    static class StaticProperty extends Property {

        /**
         * The property name.
         */
        final String propertyName;

        /**
         * Constructs a new static property reference.
         */
        StaticProperty(Reference base, int baseRegister, String propertyName, SourceLocation location) {
            super(Type.StaticProperty, base, baseRegister, location);
            this.propertyName = propertyName;
        }
    }
}
