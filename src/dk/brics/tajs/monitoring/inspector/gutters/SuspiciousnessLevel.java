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

package dk.brics.tajs.monitoring.inspector.gutters;

import dk.brics.tajs.analysis.HostAPIs;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * A measure of how suspicious an abstract value is. As a rule of thumb: the more heterogeneous a value is, the more suspicious it is.
 */
public class SuspiciousnessLevel {

    private final static Set<ObjectLabel> knownPrototypes;

    static {
        knownPrototypes = new HashSet<>();
        knownPrototypes.addAll(Arrays.asList(
                // InitialStateBuilder:
                InitialStateBuilder.OBJECT_PROTOTYPE,
                InitialStateBuilder.FUNCTION_PROTOTYPE,
                InitialStateBuilder.ARRAY_PROTOTYPE,
                InitialStateBuilder.STRING_PROTOTYPE,
                InitialStateBuilder.BOOLEAN_PROTOTYPE,
                InitialStateBuilder.NUMBER_PROTOTYPE,
                InitialStateBuilder.DATE_PROTOTYPE,
                InitialStateBuilder.REGEXP_PROTOTYPE,
                InitialStateBuilder.ERROR_PROTOTYPE,
                InitialStateBuilder.EVAL_ERROR_PROTOTYPE,
                InitialStateBuilder.RANGE_ERROR_PROTOTYPE,
                InitialStateBuilder.REFERENCE_ERROR_PROTOTYPE,
                InitialStateBuilder.SYNTAX_ERROR_PROTOTYPE,
                InitialStateBuilder.TYPE_ERROR_PROTOTYPE,
                InitialStateBuilder.URI_ERROR_PROTOTYPE
                // DOM:
        ));
    }

    private final Set<SuspiciousnessIndicator> indicators;

    public SuspiciousnessLevel(Value value) {
        this.indicators = new HashSet<>();

        int numberOfTypes = getNumberOfTypes(value);
        int maxNumberOfTypes = 6;
        double numberOfTypesValue = numberOfTypes == 1 ? 0 : numberOfTypes / ((double) maxNumberOfTypes);
        indicators.add(new LinearSuspiciousnessIndicator("|types|", numberOfTypesValue, SuspiciousnessCategory.MEDIUM));

        int numberOfObjectTypes = getNumberOfObjectTypes(value);
        int maxNumberOfObjectTypes = ObjectLabel.Kind.values().length;
        double numberOfObjectTypesValue = numberOfObjectTypes == 1 ? 0 : numberOfObjectTypes / ((double) maxNumberOfObjectTypes);
        indicators.add(new LinearSuspiciousnessIndicator("|object types|", numberOfObjectTypesValue, SuspiciousnessCategory.MEDIUM));

        indicators.add(new LinearSuspiciousnessIndicator("mixed (non-)array-likes", isArrayLikeAndNonArrayLikeObjects(value), SuspiciousnessCategory.MEDIUM));

        indicators.add(new LinearSuspiciousnessIndicator("mixed (non-)functions", isFunctionAndNonFunction(value), SuspiciousnessCategory.HIGH));

        indicators.add(new LinearSuspiciousnessIndicator("mixed (non-)ECMA", isECMAObjectAndNonECMAObject(value), SuspiciousnessCategory.LOW));

        indicators.add(new LinearSuspiciousnessIndicator("wrapped primitives", isWrappedPrimitiveValue(value), SuspiciousnessCategory.LOW));

        indicators.add(new LinearSuspiciousnessIndicator("mixed (non-)arguments", isArgsAndNonArgs(value), SuspiciousnessCategory.LOW));

        indicators.add(new LinearSuspiciousnessIndicator("mixed (non-)DOM", isDOMandNonDOM(value), SuspiciousnessCategory.LOW));

        indicators.add(new LinearSuspiciousnessIndicator("global", isGlobalObject(value), SuspiciousnessCategory.HIGH));

        indicators.add(new LinearSuspiciousnessIndicator("known constructor/prototype", isKnownConstructorOrPrototype(value), SuspiciousnessCategory.HIGH));

        indicators.add(new LinearSuspiciousnessIndicator("mixed primitives and objects", isPrimitivesAndObjectsValue(value), SuspiciousnessCategory.MEDIUM));

        indicators.add(new LinearSuspiciousnessIndicator("function count", getFunctionCountValue(value), SuspiciousnessCategory.MEDIUM));

        indicators.add(new LinearSuspiciousnessIndicator("object label count", getObjectLabelCountValue(value), SuspiciousnessCategory.MEDIUM));
        // TODO o[x] and o is native prototype: ``why is he prototype being passed around like that!!?''
        // TODO f.apply(``unknown args'')
    }

    private double getFunctionCountValue(Value value) {

        final Set<ObjectLabel> objectLabels = value.getObjectLabels();
        int functionCount = 0;
        for (ObjectLabel objectLabel : objectLabels) {
            if (objectLabel.getKind() == Kind.FUNCTION)
                functionCount++;
        }
        switch (functionCount) {
            case 0:
                return 0;
            case 1:
                return 0.2;
            case 2:
                return 0.4;
            case 3:
                return 0.6;
            case 4:
                return 0.8;
            default:
                return 1;
        }
    }

    private boolean isPrimitivesAndObjectsValue(Value value) {
        if (value.isPolymorphicOrUnknown()) {
            return true;
        }
        // allow spurious undefined
        boolean isMaybePrimitive =
                (value.isMaybeTrue() || value.isMaybeFalse()) ||
                        (value.isMaybeFuzzyNum() || value.isMaybeSingleNum()) ||
                        (value.isMaybeFuzzyStr() || value.isMaybeSingleStr());
        boolean isMaybeObjects = value.isMaybeObject();
        return isMaybeObjects && isMaybePrimitive;
    }

    public Set<SuspiciousnessIndicator> getIndicators() {
        return indicators;
    }

    private int getNumberOfObjectTypes(Value value) {
        Set<ObjectLabel> objectLabels = value.getObjectLabels();
        Set<Kind> objectTypes = new HashSet<>();
        for (ObjectLabel label : objectLabels)
            objectTypes.add(label.getKind());
        return objectTypes.size();
    }

    private int getNumberOfTypes(Value value) {
        int count = 0;
        if (value.isPolymorphicOrUnknown()) {
            return 1;
        }
        if (value.isMaybeTrue() || value.isMaybeFalse())
            count++;
        if (value.isMaybeFuzzyNum() || value.isMaybeSingleNum())
            count++;
        if (value.isMaybeFuzzyStr() || value.isMaybeSingleStr())
            count++;
        if (value.isMaybeObject())
            count++;
        if (value.isMaybeNull())
            count++;
        if (value.isMaybeUndef())
            count++;
        if (value.isMaybeSymbol())
            count++;
        return count;
    }

    public int getNumericLevel() {
        double numericLevel = 1;

        for (SuspiciousnessIndicator indicator : indicators) {
            final double suspiciousnessIndicatorValue = indicator.getSuspiciousnessIndicatorValue();
            numericLevel = numericLevel * suspiciousnessIndicatorValue;
        }
        return (int) numericLevel - 1;
    }

    private double getObjectLabelCountValue(Value value) {
        int size = value.getObjectLabels().size();
        if (size <= 2) {
            return 0;
        }
        if (size <= 4) {
            return 0.1;
        }
        if (size <= 8) {
            return 0.3;
        }
        if (size <= 16) {
            return 0.6;
        }
        return 1;
    }

    private boolean isArgsAndNonArgs(Value value) {
        Set<ObjectLabel> labels = value.getObjectLabels();
        boolean isArgs = false;
        boolean isNonArgs = false;
        for (ObjectLabel l : labels) {
            if (l.getKind() == Kind.ARGUMENTS) {
                isArgs = true;
            } else {
                isNonArgs = true;
            }
            if (isArgs && isNonArgs)
                return true;
        }
        return false;
    }

    private boolean isArrayLikeAndNonArrayLikeObjects(Value value) {
        Set<ObjectLabel> labels = value.getObjectLabels();
        boolean isArrayLike = false;
        boolean isNonArrayLike = false;
        for (ObjectLabel l : labels) {
            if (l.getKind() == Kind.ARGUMENTS || l.getKind() == Kind.ARRAY) {
                isArrayLike = true;
            } else {
                isNonArrayLike = true;
            }
            if (isArrayLike && isNonArrayLike)
                return true;
        }
        return false;
    }

    private boolean isDOMandNonDOM(Value value) {
        Set<ObjectLabel> labels = value.getObjectLabels();
        boolean isDOMObject = false;
        boolean isNonDOMObject = false;
        for (ObjectLabel l : labels) {
            if (l.isHostObject() && l.getHostObject().getAPI() == HostAPIs.DOCUMENT_OBJECT_MODEL) {
                isDOMObject = true;
            } else {
                isNonDOMObject = true;
            }
            if (isDOMObject && isNonDOMObject)
                return true;
        }
        return false;
    }

    private boolean isECMAObjectAndNonECMAObject(Value value) {
        Set<ObjectLabel> labels = value.getObjectLabels();
        boolean isECMAObject = false;
        boolean isNonECMAObject = false;
        for (ObjectLabel l : labels) {
            if (l.isHostObject() && l.getHostObject().getAPI() == HostAPIs.ECMASCRIPT_NATIVE) {
                isECMAObject = true;
            } else {
                isNonECMAObject = true;
            }
            if (isECMAObject && isNonECMAObject)
                return true;
        }
        return false;
    }

    private boolean isFunctionAndNonFunction(Value value) {
        Set<ObjectLabel> labels = value.getObjectLabels();
        boolean isFunction = false;
        boolean isNonFunction = false;
        for (ObjectLabel l : labels) {
            if (l.getKind() == Kind.FUNCTION) {
                isFunction = true;
            } else {
                isNonFunction = true;
            }
            if (isFunction && isNonFunction)
                return true;
        }
        return false;
    }

    private boolean isGlobalObject(Value value) {
        Set<ObjectLabel> labels = value.getObjectLabels();
        for (ObjectLabel l : labels) {
            if (l == InitialStateBuilder.GLOBAL) {
                return true;
            }
        }
        return false;
    }

    private boolean isKnownConstructorOrPrototype(Value value) {
        Set<ObjectLabel> labels = value.getObjectLabels();
        for (ObjectLabel label : labels) {
            // TODO handle constructors and DOM prototypes+constructors
            if (knownPrototypes.contains(label))
                return true;
        }
        return false;
    }

    private boolean isWrappedPrimitiveValue(Value value) {
        Set<ObjectLabel> labels = value.getObjectLabels();
        for (ObjectLabel l : labels) {
            final Kind kind = l.getKind();
            if (kind == Kind.NUMBER || kind == Kind.STRING || kind == Kind.BOOLEAN) {
                return true;
            }
        }
        return false;
    }

    public enum SuspiciousnessCategory {
        HIGH, MEDIUM, LOW;

        public double toExponentialNumeric() {
            switch (this) {
                case HIGH:
                    return 8;
                case MEDIUM:
                    return 4;
                case LOW:
                    return 2;
                default:
                    throw new IllegalStateException("Unhandled enum: " + this);
            }
        }

        public double toLinearNumeric() {
            switch (this) {
                case HIGH:
                    return 4;
                case MEDIUM:
                    return 3;
                case LOW:
                    return 2;
                default:
                    throw new IllegalStateException("Unhandled enum: " + this);
            }
        }
    }

    public static class NumericComparator implements Comparator<SuspiciousnessLevel> {

        @Override
        public int compare(SuspiciousnessLevel o1, SuspiciousnessLevel o2) {
            return o1.getNumericLevel() - o2.getNumericLevel();
        }
    }

    public static abstract class SuspiciousnessIndicator {

        final String description;

        final double value;

        final SuspiciousnessCategory category;

        public SuspiciousnessIndicator(String description, double value, SuspiciousnessCategory category) {
            this.description = description;
            this.value = value;
            this.category = category;
        }

        public SuspiciousnessIndicator(String description, boolean active, SuspiciousnessCategory category) {
            this.description = description;
            this.value = active ? 1 : 0;
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public double getValue() {
            return value;
        }

        public abstract double getSuspiciousnessIndicatorValue();
    }

    private static class LinearSuspiciousnessIndicator extends SuspiciousnessIndicator {

        public LinearSuspiciousnessIndicator(String description, boolean active, SuspiciousnessCategory category) {
            super(description, active, category);
        }

        public LinearSuspiciousnessIndicator(String description, double value, SuspiciousnessCategory category) {
            super(description, value, category);
        }

        @Override
        public double getSuspiciousnessIndicatorValue() {
            final double product = this.value * this.category.toLinearNumeric();
            return Math.max(product, 1);
        }
    }

    @java.lang.SuppressWarnings("unused")
    private static class ExponentialSuspiciousnessIndicator extends SuspiciousnessIndicator {

        public ExponentialSuspiciousnessIndicator(String description, double value, SuspiciousnessCategory category) {
            super(description, value, category);
        }

        @Override
        public double getSuspiciousnessIndicatorValue() {
            return Math.max(Math.pow(2, this.value * this.category.toExponentialNumeric()), 1);
        }
    }
}
