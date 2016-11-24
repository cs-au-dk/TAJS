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

package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.Bool;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.solver.GenericSolver;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Property descriptor.
 * (ES5 8.10)
 * Used by Object.defineProperty and Object.prototype.__defineGetter/Setter__.
 */
public class PropertyDescriptor {

    private final Value enumerable;

    private final Value configurable;

    private final Value writable;

    private final Value value;

    private final Set<ObjectLabel> get;

    private final Set<ObjectLabel> set;

    private PropertyDescriptor(Value enumerable, Value configurable, Value writable, Value value, Set<ObjectLabel> get, Set<ObjectLabel> set) {
        this.enumerable = enumerable;
        this.configurable = configurable;
        this.writable = writable;
        this.value = value;
        this.get = get;
        this.set = set;
    }

    /**
     * ES5 8.10.5
     */
    public static PropertyDescriptor toDefinePropertyPropertyDescriptor(Value obj, Solver.SolverInterface c) { // FIXME: check for "TypeError: Invalid property descriptor. Cannot both specify accessors and a value or writable attribute"
        obj = UnknownValueResolver.getRealValue(obj, c.getState());
        if (!obj.isMaybeObject()) {
            Exceptions.throwTypeError(c); // FIXME: should also throw type error if *maybe* non-object? (but in that case only weakly)
            c.getState().setToNone();
        }

        Set<ObjectLabel> objects = obj.getObjectLabels();

        Value enumerable = readProperty(objects, "enumerable", true, c);
        Value configurable = readProperty(objects, "configurable", true, c);
        Value writable = readProperty(objects, "writable", true, c);
        Value value = readProperty(objects, "value", false, c);
        Value get = readProperty(objects, "get", false, c);
        Value set = readProperty(objects, "set", false, c);

        return constructAndCheck(enumerable, configurable, writable, value, get, set, c);
    }

    /**
     * ES5 8.10.5
     */
    public static PropertyDescriptor toDefineGetterSetterPropertyDescriptor(Value fun, boolean getter, Solver.SolverInterface c) {
        fun = UnknownValueResolver.getRealValue(fun, c.getState());

        Value enumerable = Value.makeBool(true);
        Value configurable = Value.makeBool(true);
        Value writable = Value.makeBool(false);
        Value value = Value.makeAbsent();
        Value get = getter ? fun : Value.makeAbsent();
        Value set = getter ? Value.makeAbsent() : fun;

        return constructAndCheck(enumerable, configurable, writable, value, get, set, c);
    }

    public static PropertyDescriptor fromProperty(Value property) {
        Value configurable = Value.makeNone();
        if (property.isMaybeDontDelete()) {
            configurable = configurable.joinBool(false);
        }
        if (property.isMaybeNotDontDelete()) {
            configurable = configurable.joinBool(true);
        }
        Value writable = Value.makeNone();
        if (property.isMaybeReadOnly()) {
            writable = writable.joinBool(false);
        }
        if (property.isMaybeNotReadOnly()) {
            writable = writable.joinBool(true);
        }
        Value enumerable = Value.makeNone();
        if (property.isMaybeDontEnum()) {
            enumerable = enumerable.joinBool(false);
        }
        if (property.isMaybeNotDontEnum()) {
            enumerable = enumerable.joinBool(true);
        }
        Value value = Value.makeNone();
        if (property.isMaybePresentData()) {
            value = property.restrictToNonAttributes().restrictToNotGetterSetter();
        }
        Set<ObjectLabel> get = newSet(property.getGetters());
        get.remove(ObjectLabel.absent_accessor_function);
        Set<ObjectLabel> set = newSet(property.getSetters());
        set.remove(ObjectLabel.absent_accessor_function);

        return new PropertyDescriptor(
                enumerable.isNone() ? Value.makeAbsent() : enumerable,
                configurable.isNone() ? Value.makeAbsent() : configurable,
                writable.isNone() ? Value.makeAbsent() : writable,
                value.isNone() ? Value.makeAbsent() : value,
                get,
                set);
    }

    private static PropertyDescriptor constructAndCheck(Value enumerable, Value configurable, Value writable, Value value, Value get, Value set, GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, Analysis>.SolverInterface c) {
        boolean definitelyInvalid = false;
        if (!get.isMaybeAbsent()) {
            definitelyInvalid |= checkCallableGetterSetter(get, c); // 8.10.5#7.b
        }
        if (!set.isMaybeAbsent()) {
            definitelyInvalid |= checkCallableGetterSetter(set, c); // 8.10.5#8.b
        }

        Set<ObjectLabel> getLabels = get.getObjectLabels().stream().filter(l -> l.getKind() == ObjectLabel.Kind.FUNCTION).collect(Collectors.toSet());
        Set<ObjectLabel> setLabels = set.getObjectLabels().stream().filter(l -> l.getKind() == ObjectLabel.Kind.FUNCTION).collect(Collectors.toSet());

        PropertyDescriptor descriptor = new PropertyDescriptor(enumerable, configurable, writable, value, getLabels, setLabels);

        definitelyInvalid |= checkUnambiguous(descriptor, c); // 8.10.5#9

        if (definitelyInvalid) {
            return new PropertyDescriptor(enumerable, configurable, writable, value, getLabels, setLabels) {
                @Override
                public Value makePropertyWithAttributes() {
                    return Value.makeNone();
                }

                @Override
                public Optional<ObjectLabel> newPropertyDescriptorObject(Solver.SolverInterface c) {
                    return Optional.empty();
                }
            };
        }

        return descriptor;
    }

    private static boolean checkUnambiguous(PropertyDescriptor descriptor, Solver.SolverInterface c) {
        if (descriptor.isMaybeAccessorDescriptor()) {
            if (descriptor.isMaybeDataDescriptor()) {
                Exceptions.throwTypeError(c);
            }
        }
        if (!descriptor.get.isEmpty() || !descriptor.set.isEmpty()) {
            if (descriptor.writable.isMaybeTrue() || descriptor.value.isNotAbsent()) {
                // definite exception
                return true;
            }
        }
        return false;
    }

    private static boolean checkCallableGetterSetter(Value f, Solver.SolverInterface c) {
        f = UnknownValueResolver.getRealValue(f, c.getState());
        if (f.restrictToNotUndef().isNone()) {
            return false; // an explicitly `undefined` getter/setter is ignored
        }

        Set<ObjectLabel> labels = f.getObjectLabels();
        boolean maybeNonCallable = f.isMaybeOtherThanObject() || labels.stream().anyMatch(l -> l.getKind() != ObjectLabel.Kind.FUNCTION);
        boolean onlyNonCallable = !f.isMaybeObject() || labels.stream().allMatch(l -> l.getKind() != ObjectLabel.Kind.FUNCTION);

        if (maybeNonCallable) {
            Exceptions.throwTypeError(c);
        }

        if (onlyNonCallable) {
            // definite exception
            return true;
        }
        return false;
    }

    private static Value readProperty(Set<ObjectLabel> objects, String propertyName, boolean coerceToBoolean, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        Value result = Value.makeNone();
        Bool hasPropertyName = pv.hasProperty(objects, propertyName);
        if (hasPropertyName.isMaybeTrue()) {
            // opportunity for small precision gain: the property can be assumed not to be absent
            Value propertyValue = UnknownValueResolver.getRealValue(pv.readPropertyValue(objects, propertyName), c.getState());
            c.getMonitoring().visitPropertyRead(c.getNode(), objects, Value.makeTemporaryStr(propertyName), c.getState(), false);
            if (coerceToBoolean) {
                propertyValue = Conversion.toBoolean(UnknownValueResolver.getRealValue(propertyValue, c.getState()));
            }
            result = result.join(propertyValue);
        }
        if (hasPropertyName.isMaybeFalse()) {
            result = result.joinAbsent();
        }
        return result;
    }

    /**
     * 8.10.1
     */
    private boolean isMaybeDataDescriptor() {
        return value.isMaybePresent(); // we don't track absence/presence of [[Writable]], but usually [[Writable]] is present if [[Value]] is present, so we just omit "|| writable.isMaybeTrue()"
    }

    /**
     * 8.10.2
     */
    private boolean isMaybeAccessorDescriptor() {
        return !get.isEmpty() || !set.isEmpty();
    }

    /**
     * 8.10.3
     */
    private boolean isMaybeGenericDescriptor() {
        return !isMaybeDataDescriptor() && !isMaybeAccessorDescriptor();
    }

    private Value getValueOrDefault() {
        return getOrDefault(value, Value.makeUndef());
    }

    private Value getEnumerableOrDefault() {
        return getOrDefault(enumerable, Value.makeBool(false));
    }

    private Value getWritableOrDefault() {
        return getOrDefault(writable, Value.makeBool(false));
    }

    private Value getConfigurableOrDefault() {
        return getOrDefault(configurable, Value.makeBool(false));
    }

    /**
     * Helper function for 8.6.1 Table 7.
     */
    private Value getOrDefault(Value result, Value defaultValue) {
        if (result.isMaybeAbsent()) {
            result = result.restrictToNotAbsent().join(defaultValue);
        }
        return result;
    }

    /**
     * Creates value to be used in {@link PropVarOperations#writePropertyWithAttributes(ObjectLabel, String, Value)}.
     */
    public Value makePropertyWithAttributes() {
        Value value = getValueOrDefault();
        Value enumerable = getEnumerableOrDefault();
        Value writable = getWritableOrDefault();
        Value configurable = getConfigurableOrDefault();

        Value resultValue = Value.makeNone();

        if (isMaybeDataDescriptor() || isMaybeGenericDescriptor()) {
            resultValue = resultValue.join(value.restrictToNotAbsent());
        }
        if (isMaybeAccessorDescriptor()) {
            resultValue = resultValue.join(Value.makeObject(get).makeGetter());
            resultValue = resultValue.join(Value.makeObject(set).makeSetter());
        }

        // TODO: clean up ES3 vs. ES5 terminology
        if (enumerable.isMaybeTrue())
            resultValue = resultValue.setNotDontEnum();
        if (enumerable.isMaybeFalse())
            resultValue = resultValue.setDontEnum();
        if (writable.isMaybeTrue())
            resultValue = resultValue.setNotReadOnly();
        if (writable.isMaybeFalse())
            resultValue = resultValue.setReadOnly();
        if (configurable.isMaybeTrue())
            resultValue = resultValue.setNotDontDelete();
        if (configurable.isMaybeFalse())
            resultValue = resultValue.setDontDelete();

        return resultValue;
    }

    /**
     * Instantiates a valid property-descriptor object. Invalid descriptor objects are not constructed.
     */
    public Optional<ObjectLabel> newPropertyDescriptorObject(Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        ObjectLabel desc = new ObjectLabel(c.getNode(), ObjectLabel.Kind.OBJECT);
        c.getState().newObject(desc);
        c.getState().writeInternalPrototype(desc, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        if (isMaybeDataDescriptor()) {
            if (value.isMaybePresent()) {
                pv.writeProperty(desc, "value", value);
            }
            pv.writeProperty(desc, "writable", writable);
        }
        if (isMaybeAccessorDescriptor()) {
            if (!get.isEmpty()) {
                pv.writeProperty(desc, "get", Value.makeObject(get));
            }
            if (!set.isEmpty()) {
                pv.writeProperty(desc, "set", Value.makeObject(set));
            }
        }
        pv.writeProperty(desc, "enumerable", enumerable);
        pv.writeProperty(desc, "configurable", configurable);
        return Optional.of(desc);
    }
}
