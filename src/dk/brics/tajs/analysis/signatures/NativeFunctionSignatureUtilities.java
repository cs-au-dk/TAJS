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

package dk.brics.tajs.analysis.signatures;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.signatures.types.Coercion;
import dk.brics.tajs.analysis.signatures.types.Parameter;
import dk.brics.tajs.analysis.signatures.types.Requirement;
import dk.brics.tajs.analysis.signatures.types.ValueDescription;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Collectors;

import java.util.Set;

/**
 * Utility values for defining signatures of native functions.
 */
public class NativeFunctionSignatureUtilities {

    public static ValueDescription None = new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.empty());

    /**
     * Utility class for grouping the coercions.
     * <p>
     * - Some coercions are simple (e.g. {@link Coercions#string(Value, Solver.SolverInterface)}.
     * <p>
     * - Some coercions are conditionally performed (e.g. {@link #stringIfNotUndefined(Value, Solver.SolverInterface)}).
     */
    private static class Coercions {

        public static boolean number(Value v, Solver.SolverInterface c) {
            return Conversion.toNumber(v, c).isNone();
        }

        public static boolean integer(Value v, Solver.SolverInterface c) {
            return Conversion.toInteger(v, c).isNone();
        }

        public static boolean string(Value v, Solver.SolverInterface c) {
            return Conversion.toString(v, c).isNone();
        }

        public static boolean bool(Value v, Solver.SolverInterface c) {
            return false;
        }

        public static boolean object(Value v, Solver.SolverInterface c) {
            return Conversion.toObject(c.getNode(), v, c).isNone();
        }

        public static boolean regExp(Value v, Solver.SolverInterface c) {
            Value nonRegExps = v.removeObjects(v.getObjectLabels().stream()
                    .filter(l -> l.getKind() != ObjectLabel.Kind.REGEXP)
                    .collect(Collectors.toSet()));
            return Conversion.toString(nonRegExps, c).isNone(); // TODO this might involve exceptions from the RegExp constructor!
        }

        public static boolean regExpIfNotString(Value v, Solver.SolverInterface c) {
            Set<ObjectLabel> nonStringObjects = v.getObjectLabels().stream().filter(l -> l.getKind() != ObjectLabel.Kind.STRING).collect(Collectors.toSet());
            Value left = v.restrictToNotStr().restrictToNotObject().join(Value.makeObject(nonStringObjects));
            if (left.isNone()) {
                return false;
            }
            return regExp(left, c);
        }

        public static boolean stringIfNotFunction(Value v, Solver.SolverInterface c) {
            Set<ObjectLabel> nonFunctions = v.getObjectLabels().stream().filter(l -> l.getKind() != ObjectLabel.Kind.FUNCTION).collect(Collectors.toSet());
            Value left = v.restrictToNotObject().join(Value.makeObject(nonFunctions));
            if (left.restrictToNotObject().isNone()) {
                return false;
            }
            return string(left, c);
        }

        public static boolean stringIfNotRegExpOrUndefined(Value v, Solver.SolverInterface c) {
            Set<ObjectLabel> nonRegExps = v.getObjectLabels().stream().filter(l -> l.getKind() != ObjectLabel.Kind.REGEXP).collect(Collectors.toSet());
            Value left = v.restrictToNotUndef().restrictToNotObject().join(Value.makeObject(nonRegExps));
            if (left.isNone()) {
                return false;
            }
            return string(left, c);
        }

        public static boolean stringIfNotUndefined(Value v, Solver.SolverInterface c) {
            Value left = v.restrictToNotUndef();
            if (left.isNone()) {
                return false;
            }
            return string(left, c);
        }

        public static boolean stringIfNotSymbol(Value v, Solver.SolverInterface c) {
            Value left = v.restrictToNotSymbol();
            if (left.isNone()) {
                return false;
            }
            return string(left, c);
        }

        public static boolean objectIfNotNullUndefined(Value v, Solver.SolverInterface c) {
            Value left = v.restrictToNotNullNotUndef();
            if (left.isNone()) {
                return false;
            }
            return object(left, c);
        }

        public static boolean numberIfNotUndefined(Value v, Solver.SolverInterface c) {
            Value left = v.restrictToNotUndef();
            if (left.isNone()) {
                return false;
            }
            return number(left, c);
        }

        public static boolean stringIfNotRegExp(Value v, Solver.SolverInterface c) {
            Set<ObjectLabel> nonRegExps = v.getObjectLabels().stream().filter(l -> l.getKind() != ObjectLabel.Kind.REGEXP).collect(Collectors.toSet());
            Value left = v.restrictToNotObject().join(Value.makeObject(nonRegExps));
            if (left.isNone()) {
                return false;
            }
            return string(left, c);
        }

        public static boolean numberIfNotString(Value v, Solver.SolverInterface c) {
            Set<ObjectLabel> nonStringObjects = v.getObjectLabels().stream().filter(l -> l.getKind() != ObjectLabel.Kind.STRING).collect(Collectors.toSet());
            Value left = v.restrictToNotStr().restrictToNotObject().join(Value.makeObject(nonStringObjects));
            if (left.isNone()) {
                return false;
            }
            return number(left, c);
        }
    }

    /**
     * Optional parameter in a signature.
     */
    private static class Optional implements Parameter {

        private final ValueDescription valueDescription;

        public Optional(ValueDescription valueDescription) {
            this.valueDescription = valueDescription;
        }

        @Override
        public ValueDescription getValueDescription() {
            return valueDescription;
        }

        @Override
        public boolean isMandatory() {
            return false;
        }
    }

    /**
     * Mandatory parameter in a signature.
     */
    private static class Mandatory implements Parameter {

        private final ValueDescription valueDescription;

        public Mandatory(ValueDescription valueDescription) {
            this.valueDescription = valueDescription;
        }

        Optional toMaybe() {
            return new Optional(this.valueDescription);
        }

        @Override
        public ValueDescription getValueDescription() {
            return valueDescription;
        }

        @Override
        public boolean isMandatory() {
            return true;
        }
    }

    private static class ValueDescriptionImpl implements ValueDescription {

        private java.util.Optional<Requirement> requirement;

        private java.util.Optional<Coercion> coercion;

        public ValueDescriptionImpl(java.util.Optional<Requirement> requirement, java.util.Optional<Coercion> coercion) {
            this.requirement = requirement;
            this.coercion = coercion;
        }

        @Override
        public java.util.Optional<Requirement> getRequirement() {
            return requirement;
        }

        @Override
        public java.util.Optional<Coercion> getCoercion() {
            return coercion;
        }
    }

    /**
     * Utility class for grouping mandatory parameters.
     */
    public static class MandatoryParameters {

        public static final Mandatory Number = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::number)));

        public static final Mandatory Boolean = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::bool)));

        public static final Mandatory String = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::string)));

        public static final Mandatory Integer = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::integer)));

        public static final Mandatory RegExp = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::regExp)));

        public static final Mandatory StringIfNotRegExp = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::stringIfNotRegExp)));

        public static final Mandatory StringThrowOnRegExp = new Mandatory(new ValueDescriptionImpl(java.util.Optional.of(Requirements.isNotRegExp), java.util.Optional.of(Coercions::stringIfNotRegExp)));

        public static final Mandatory StringIfNotFunction = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::stringIfNotFunction)));

        public static final Mandatory StringIfNotRegExpOfUndefined = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::stringIfNotRegExpOrUndefined)));

        public static final Mandatory StringIfNotUndefined = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::stringIfNotUndefined)));

        public static final Mandatory ObjectIfNotNullUndefined = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::objectIfNotNullUndefined)));

        public static final Mandatory NumberIfNotUndefined = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::numberIfNotUndefined)));

        public static final Mandatory NumberIfNotString = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::numberIfNotString)));

        public static final Mandatory DontCare = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.empty()));

        public static final Mandatory ObjectNoCoercion = new Mandatory(new ValueDescriptionImpl(java.util.Optional.of(Requirements.isObject), java.util.Optional.empty()));

        public static final Mandatory Object = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::object)));

        public static final Mandatory NotRegExpCoerceString = new Mandatory(new ValueDescriptionImpl(java.util.Optional.of(Requirements.isNotRegExp), java.util.Optional.of(Coercions::string)));

        public static final Mandatory Function = new Mandatory(new ValueDescriptionImpl(java.util.Optional.of(Requirements.isFunction), java.util.Optional.empty()));

        public static final Mandatory Symbol = new Mandatory(new ValueDescriptionImpl(java.util.Optional.of(Requirements.isSymbol), java.util.Optional.empty()));

        public static final Mandatory StringOrSymbol = new Mandatory(new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::stringIfNotSymbol)));
    }

    /**
     * Utility class for grouping optional parameters.
     */
    public static class OptionalParameters {

        public static final Optional Number = MandatoryParameters.Number.toMaybe();

        public static final Optional Boolean = MandatoryParameters.Boolean.toMaybe();

        public static final Optional String = MandatoryParameters.String.toMaybe();

        public static final Optional Integer = MandatoryParameters.Integer.toMaybe();

        public static final Optional RegExp = MandatoryParameters.RegExp.toMaybe();

        public static final Optional Object = MandatoryParameters.Object.toMaybe();

        public static final Optional StringIfNotRegExp = MandatoryParameters.StringIfNotRegExp.toMaybe();

        public static final Optional StringIfNotFunction = MandatoryParameters.StringIfNotFunction.toMaybe();

        public static final Optional StringIfNotRegExpOfUndefined = MandatoryParameters.StringIfNotRegExpOfUndefined.toMaybe();

        public static final Optional StringIfNotUndefined = MandatoryParameters.StringIfNotUndefined.toMaybe();

        public static final Optional ObjectIfNotNullUndefined = MandatoryParameters.ObjectIfNotNullUndefined.toMaybe();

        public static final Optional NumberIfNotUndefined = MandatoryParameters.NumberIfNotUndefined.toMaybe();

        public static final Optional NumberIfNotString = MandatoryParameters.NumberIfNotString.toMaybe();

        public static final Optional DontCare = MandatoryParameters.DontCare.toMaybe();

        public static final Optional NotRegExpCoerceString = MandatoryParameters.NotRegExpCoerceString.toMaybe();

        public static final Optional Symbol = MandatoryParameters.Symbol.toMaybe();

        public static final Optional StringOrSymbol = MandatoryParameters.StringOrSymbol.toMaybe();
    }

    /**
     * Utility class for grouping the requirements.
     */
    public static class Requirements {

        // TODO unify implementations in this class

        public static Requirement isBoolean = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                boolean b = v.isMaybeTrue() || v.isMaybeFalse()
                        || v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() == ObjectLabel.Kind.BOOLEAN);
                return b;
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.restrictToNotObject().isMaybeOtherThanBool()
                        || v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() != ObjectLabel.Kind.BOOLEAN);
            }
        };

        public static Requirement isNotNullUndefined = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                return !v.restrictToNotNullNotUndef().isNone();
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.isNullOrUndef();
            }
        };

        public static Requirement isNotRegExp = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                return v.getObjectLabels().stream().noneMatch(l -> l.getKind() == ObjectLabel.Kind.REGEXP);
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.getObjectLabels().stream().anyMatch(l -> l.getKind() == ObjectLabel.Kind.REGEXP);
            }
        };

        public static Requirement isString = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                boolean b = v.isMaybeFuzzyStr()
                        || v.isMaybeSingleStr()
                        || v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() == ObjectLabel.Kind.STRING);
                return b;
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.restrictToNotObject().isMaybeOtherThanStr()
                        || v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() != ObjectLabel.Kind.STRING);
            }
        };

        public static Requirement isNumber = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                boolean b = v.isMaybeFuzzyNum()
                        || v.isMaybeSingleNum()
                        || v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() == ObjectLabel.Kind.NUMBER);
                return b;
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.restrictToNotObject().isMaybeOtherThanNum()
                        || v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() != ObjectLabel.Kind.NUMBER);
            }
        };

        public static Requirement isRegExp = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                return v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() == ObjectLabel.Kind.REGEXP);
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.isMaybePrimitiveOrSymbol() ||
                        v.getObjectLabels().stream()
                                .anyMatch(l -> l.getKind() != ObjectLabel.Kind.REGEXP);
            }
        };

        public static Requirement isObject = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                return v.isMaybeObject();
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.isMaybePrimitiveOrSymbol();
            }
        };

        public static Requirement isFunction = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                return v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() == ObjectLabel.Kind.FUNCTION);
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.isMaybePrimitiveOrSymbol() ||
                        v.getObjectLabels().stream()
                                .anyMatch(l -> l.getKind() != ObjectLabel.Kind.FUNCTION);
            }
        };

        public static Requirement isDate = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                return v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() == ObjectLabel.Kind.DATE);
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.isMaybePrimitiveOrSymbol() ||
                        v.getObjectLabels().stream()
                                .anyMatch(l -> l.getKind() != ObjectLabel.Kind.DATE);
            }
        };

        public static Requirement isSymbol = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                return v.isMaybeSymbol();
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.isMaybeOtherThanSymbol();
            }
        };
    }

    /**
     * Utility class for grouping receivers..
     */
    public static class Receivers {

        public static final ValueDescription String = new ValueDescriptionImpl(java.util.Optional.of(Requirements.isString), java.util.Optional.empty());

        public static final ValueDescription NotNullUndefCoerceString = new ValueDescriptionImpl(java.util.Optional.of(Requirements.isNotNullUndefined), java.util.Optional.of(Coercions::string));

        public static final ValueDescription Boolean = new ValueDescriptionImpl(java.util.Optional.of(Requirements.isBoolean), java.util.Optional.empty());

        public static final ValueDescription RegExp = new ValueDescriptionImpl(java.util.Optional.of(Requirements.isRegExp), java.util.Optional.empty());

        public static final ValueDescription Number = new ValueDescriptionImpl(java.util.Optional.of(Requirements.isNumber), java.util.Optional.empty());

        public static final ValueDescription Function = new ValueDescriptionImpl(java.util.Optional.of(Requirements.isFunction), java.util.Optional.empty());

        public static final ValueDescription Date = new ValueDescriptionImpl(java.util.Optional.of(Requirements.isDate), java.util.Optional.empty());

        public static final ValueDescription CoerceObject = new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.of(Coercions::object));

        public static final ValueDescription DontCare = new ValueDescriptionImpl(java.util.Optional.empty(), java.util.Optional.empty());

        public static final ValueDescription Symbol = new ValueDescriptionImpl(java.util.Optional.of(Requirements.isSymbol), java.util.Optional.empty());
    }
}
