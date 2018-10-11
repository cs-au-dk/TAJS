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

package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeObjectToString;
import dk.brics.tajs.analysis.ParallelTransfer;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ObjProperties;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.PKey.StringPKey;
import dk.brics.tajs.lattice.PKeys;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.singleton;
import static dk.brics.tajs.util.Collections.singletonList;

/**
 * 15.2 native Object functions.
 */
public class JSObject {

    private JSObject() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, Solver.SolverInterface c) {
        State state = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        switch (nativeobject) {

            case OBJECT: { // 15.2.1 and 15.2.2
                Value arg = FunctionCalls.readParameter(call, state, 0);
                Value arg2 = arg.restrictToNotNullNotUndef().restrictToNotObject();
                boolean arg_maybe_other = arg2.isMaybeOtherThanUndef() && arg2.isMaybeOtherThanNull();
                // 15.2.1.1 step 2 for objects and 15.2.2.1 step 3 in one swoop. Slightly cheating, but toObject(Obj) = Obj.
                Value res = arg.restrictToObject();
                // 15.2.1.1 step 2 for non-objects and 15.2.2.1 step 5-7.
                res = arg_maybe_other ? res.join(Conversion.toObject(call.getSourceNode(), arg2.restrictToStrBoolNum(), c)) : res;
                if (arg.isMaybeNull() || arg.isMaybeUndef()) {
                    // 15.2.1.1 step 1 and 15.2.2.1 step 8
                    ObjectLabel obj = ObjectLabel.make(call.getSourceNode(), Kind.OBJECT);
                    state.newObject(obj);
                    state.writeInternalPrototype(obj, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
                    res = res.joinObject(obj);
                }
                return res;
            }

            case OBJECT_CREATE: {
                ObjectLabel obj = ObjectLabel.make(call.getSourceNode(), Kind.OBJECT);
                state.newObject(obj);
                Value prototype = FunctionCalls.readParameter(call, state, 0);
                if (prototype.restrictToNotNull().isMaybePrimitiveOrSymbol()) {
                    Exceptions.throwTypeError(c);
                }
                if (prototype.restrictToObject().isNone() && prototype.restrictToNull().isNone()) {
                    return Value.makeNone();
                }
                state.writeInternalPrototype(obj, prototype);

                Value properties = FunctionCalls.readParameter(call, state, 1);
                Value propertiesObject = Conversion.toObject(call.getSourceNode(), properties.restrictToNotUndef(), c);
                boolean stopPropagation = false;
                if (propertiesObject.isMaybeObject()) {
                    stopPropagation = definePropertiesFromDescriptorsObject(Value.makeObject(obj), propertiesObject.getObjectLabels(), state, c);
                }

                return stopPropagation ? Value.makeNone() : Value.makeObject(obj);
            }

            case OBJECT_DEFINE_PROPERTY: { // 15.2.3.6
                Value o = FunctionCalls.readParameter(call, state, 0);
                Value propertyName = Conversion.toProperty(FunctionCalls.readParameter(call, state, 1), c);
                Value argument = FunctionCalls.readParameter(call, state, 2);
                return defineProperty(o, propertyName, argument, false, c);
            }

            case OBJECT_DEFINE_PROPERTIES: { // 15.2.3.6
                Value o = FunctionCalls.readParameter(call, state, 0);
                Value propertiesArgument = FunctionCalls.readParameter(call, state, 1);
                propertiesArgument = Conversion.toObject(call.getSourceNode(), propertiesArgument, c);
                boolean stopPropagation = definePropertiesFromDescriptorsObject(o, propertiesArgument.getObjectLabels(), state, c);
                return stopPropagation ? Value.makeNone() : o;
            }

            case OBJECT_DEFINESETTER:
            case OBJECT_DEFINEGETTER: {
                State state1 = c.getState();
                Set<ObjectLabel> objs = state1.readThisObjects();
                Value propertyName = Conversion.toProperty(FunctionCalls.readParameter(call, state1, 0), c);
                Value argument = FunctionCalls.readParameter(call, state1, 1);
                boolean getter = nativeobject == ECMAScriptObjects.OBJECT_DEFINEGETTER;
                PropertyDescriptor desc = PropertyDescriptor.toDefineGetterSetterPropertyDescriptor(argument, getter, c);
                Value value = desc.makePropertyWithAttributes();
                if (value.isNone())
                    return Value.makeNone();
                pv.writeProperty(objs, propertyName, value, true, false, false, true);
                return Value.makeUndef();
            }

            case OBJECT_TOSTRING: // 15.2.4.2
            case OBJECT_TOLOCALESTRING: { // 15.2.4.3
                Value v = evaluateToString(state.readThis(), c);
                if (nativeobject == ECMAScriptObjects.OBJECT_TOLOCALESTRING && !c.getAnalysis().getUnsoundness().mayAssumeFixedLocale(call.getSourceNode())) {
                    return Value.makeAnyStr();
                }
                return v;
            }

            case OBJECT_VALUEOF: { // 15.2.4.4
                return state.readThis();
            }

            case OBJECT_HASOWNPROPERTY: { // 15.2.4.5
                Set<ObjectLabel> thisobj = Conversion.toObject(c.getNode(), state.readThis(), c).getObjectLabels();
                Value v = FunctionCalls.readParameter(call, state, 0);
                // Result is only defined when called with a parameter. Return false if not, just like Safari.
                if (!v.isMaybeOtherThanUndef())
                    return Value.makeBool(false);
                Value propval = Conversion.toProperty(v, c);
                ObjProperties properties = state.getProperties(thisobj, ObjProperties.PropertyQuery.makeQuery().setIncludeSymbols(true));
                Value v2 = properties.getValue(propval);
                if (v2.isMaybeAbsent() && v2.isMaybePresent())
                    return Value.makeAnyBool();
                else if (v2.isMaybePresent())
                    return Value.makeBool(true);
                else if (v2.isMaybeAbsent())
                    return Value.makeBool(false);
                else
                    return Value.makeNone();
            }

            case OBJECT_ISPROTOTYPEOF: { // 15.2.4.6
                return Value.makeAnyBool(); // TODO: improve precision for OBJECT_ISPROTOTYPEOF
            }

            case OBJECT_PROPERTYISENUMERABLE: { // 15.2.4.7
                Set<ObjectLabel> thisobj = state.readThisObjects();
                Value propval = Conversion.toString(FunctionCalls.readParameter(call, state, 0), c);
                if (propval.isNotStr())
                    return Value.makeNone();
                else if (propval.isMaybeSingleStr()) {
                    String propname = propval.getStr();
                    Value val = pv.readPropertyDirect(thisobj, StringPKey.make(propname));
                    Value res = Value.makeNone();
                    if (val.isMaybeAbsent() || val.isMaybeDontEnum() || val.isNotPresent())
                        res = res.joinBool(false);
                    if (val.isMaybePresent() && val.isMaybeNotDontEnum())
                        res = res.joinBool(true);
                    return res;
                }
                for (ObjectLabel ol : thisobj) {
                    Obj o = state.getObject(ol, false);
                    for (PKey propname : o.getProperties().keySet()) {
                        if (UnknownValueResolver.getProperty(ol, propname, state, true).isMaybeNotDontEnum())
                            return Value.makeAnyBool();
                    }
                    if (UnknownValueResolver.getDefaultArrayProperty(ol, state).isMaybeNotDontEnum() ||
                            UnknownValueResolver.getDefaultNonArrayProperty(ol, state).isMaybeNotDontEnum())
                        return Value.makeAnyBool();
                }
                return Value.makeBool(false);
            }

            case OBJECT_KEYS:
            case OBJECT_GETOWNPROPERTYNAMES:
                return getPropertyNamesOrSymbolsArray(nativeobject, call, c, false);

            case OBJECT_GETOWNPROPERTYSYMBOLS:
                return getPropertyNamesOrSymbolsArray(nativeobject, call, c, true);

            case OBJECT_FREEZE:
            case OBJECT_PREVENTEXTENSIONS:
            case OBJECT_SEAL:
                if (c.getAnalysis().getUnsoundness().maySkipMissingModelOfNativeFunction(call.getSourceNode(), nativeobject)) {
                    c.getMonitoring().addMessage(call.getJSSourceNode(), Message.Severity.TAJS_ERROR, "Warning: Calling " + nativeobject + ", but no side-effects have been implemented for it...");
                    return FunctionCalls.readParameter(call, state, 0);
                }
                // TODO: GitHub #249
                throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getSourceNode().getSourceLocation() + ": No transfer function for native function " + nativeobject);

            case OBJECT_GETOWNPROPERTYDESCRIPTOR: // FIXME: handle toObject/missing-object-typeerror cases (GitHub #354)
                Set<ObjectLabel> receivers = Conversion.toObjectLabels(c.getNode(), FunctionCalls.readParameter(call, state, 0), c);
                Value name = FunctionCalls.readParameter(call, state, 1);
                PKeys nameStr = Conversion.toString(name, c);
                c.getMonitoring().visitPropertyRead(c.getNode(), receivers, nameStr, c.getState(), true);
                Value property = Value.join(dk.brics.tajs.util.Collections.map(receivers, objlabel ->
                        UnknownValueResolver.getRealValue(pv.readPropertyDirect(objlabel, nameStr), state)
                ));
                Value result = Value.makeNone();
                if (!property.isNotPresent()) {
                    Optional<ObjectLabel> descriptorObject = PropertyDescriptor.fromProperty(property).newPropertyDescriptorObject(c);
                    if (!descriptorObject.isPresent()) {
                        throw new AnalysisException("It should not be possible to construct an invalid descriptor from an existing property!");
                    }
                    result = result.joinObject(descriptorObject.get());
                }
                if (property.isMaybeAbsent()) {
                    result = result.joinUndef();
                }
                return result;

            case OBJECT_GETPROTOTYPEOF: {
                Value arg = UnknownValueResolver.getRealValue(FunctionCalls.readParameter(call, state, 0), state);
                Set<ObjectLabel> labels = Conversion.toObject(call.getSourceNode(), arg, c).getObjectLabels();
                if (labels.isEmpty()) {
                    Exceptions.throwTypeError(c);
                }
                return state.readInternalPrototype(labels);
            }

            case OBJECT_ISFROZEN: {
                return Value.makeAnyBool();
            }

            case OBJECT_SETPROTOTYPEOF: {
                Set<ObjectLabel> rec = FunctionCalls.readParameter(call, state, 0).getObjectLabels();
                Value proto = FunctionCalls.readParameter(call, state, 1);
                boolean notNullOrObject = proto.restrictToNotObject().isMaybeOtherThanNull() || proto.restrictToNotNull().isMaybePrimitiveOrSymbol();
                if (notNullOrObject) {
                    Exceptions.throwTypeError(c);
                }
                Value nullOrObject = proto.restrictToNull().join(proto.restrictToObject());
                if (nullOrObject.isNone()) {
                    return Value.makeNone();
                }
                state.writeInternalPrototype(rec, nullOrObject);
                return Value.makeObject(rec);
            }

            case OBJECT_VALUES:
                return getPropertyValuesArray(nativeobject, call, c);

            case OBJECT_IS: {
                return Value.makeAnyBool();
            }

            case OBJECT_ASSIGN: {
                return assign(call, c);
            }

            default:
                return null;
        }
    }

    /**
     * Implementation of 'Object.defineProperties(target, propertyDescriptorsObject)' after argument resolution.
     *
     * @return true if propagation should be stopped (e.g. due to definite exception)
     */
    private static boolean definePropertiesFromDescriptorsObject(Value target, Set<ObjectLabel> propertyDescriptorsObject, State state, Solver.SolverInterface c) {
        final boolean[] stopPropagation = {false};
        ParallelTransfer.process(propertyDescriptorsObject, props -> {
            ObjProperties properties = state.getProperties(singleton(props), ObjProperties.PropertyQuery.makeQuery().onlyEnumerable().includeSymbols());
            properties.getProperties().forEach((name, val) -> {
                if (val.isMaybePresent())
                    stopPropagation[0] |= definePropertyFromDescriptorsObjectProperty(target, props, name, val.isMaybeAbsent(), c).isNone();
            });
        }, c);
        return stopPropagation[0];
    }

    private static Value definePropertyFromDescriptorsObjectProperty(Value target, ObjectLabel propertyDescriptorsObject, PKeys propertyName, boolean forceWeak, Solver.SolverInterface c) {
        c.getMonitoring().visitPropertyRead(c.getNode(), singleton(propertyDescriptorsObject), propertyName, c.getState(), true);
        Value propertyDescriptorObject = c.getAnalysis().getPropVarOperations().readPropertyValue(singleton(propertyDescriptorsObject), propertyName);
        return defineProperty(target, propertyName, propertyDescriptorObject, forceWeak, c);
    }

    private static Value defineProperty(Value o, PKeys propertyName, Value propertyDescriptorObject, boolean forceWeak, Solver.SolverInterface c) {
        PropertyDescriptor desc = PropertyDescriptor.toDefinePropertyPropertyDescriptor(propertyDescriptorObject, c);
        Value value = desc.makePropertyWithAttributes();
        if (value.isNone())
            return Value.makeNone();
        c.getAnalysis().getPropVarOperations().writeProperty(o.getObjectLabels(), propertyName, value, true, true, forceWeak, true); // FIXME: trigger TypeError exception if attempt to overwrite non-writable (#291)
        return o;
    }

    private static Value getPropertyNamesOrSymbolsArray(ECMAScriptObjects nativeobject, CallInfo call, Solver.SolverInterface c, boolean symbols) {
        State state = c.getState();
        Value objectArg = Conversion.toObject(c.getNode(), FunctionCalls.readParameter(call, state, 0), c);
        if (objectArg.isNone()) {
            return Value.makeNone();
        }
        ObjectLabel array = JSArray.makeArray(call.getSourceNode(), c);
        boolean onlyEnumerable = nativeobject == ECMAScriptObjects.OBJECT_KEYS;
        ObjProperties properties = state.getProperties(objectArg.getObjectLabels(), ObjProperties.PropertyQuery.makeQuery().setOnlyEnumerable(onlyEnumerable).setIncludeSymbols(symbols));
        if (!properties.getMaybe().isEmpty()) {
            if ((properties.getMaybe().size() < 2 || c.getAnalysis().getUnsoundness().mayUseSortedObjectKeys(call.getSourceNode())) && properties.isDefinite()) {
                // we know the *order* of property names
                List<PKey> sortedNames = newList(properties.getDefinitely());
                Collections.sort(sortedNames);
                JSArray.setEntries(array, sortedNames.stream().map(PKey::toValue).collect(Collectors.toList()), c);
            } else {
                // Order of properties is the same as for-in: unspecified
                if (nativeobject == ECMAScriptObjects.OBJECT_KEYS) {
                    // Special case: we can ignore all non-enumerable strings (see SplittingUtil!)
                    Value propertyNames = Value.join(properties.getMaybe().stream().map(PKey::toValue).collect(Collectors.toList()));
                    if (properties.isDefinite() && properties.getDefinitely().size() <= 1) {
                        // we know the *number* of property names
                        List<Value> joinedPropertyNamesArray = properties.getDefinitely().stream().map(x -> propertyNames).collect(Collectors.toList());
                        JSArray.setEntries(array, joinedPropertyNamesArray, c);
                    } else {
                        // we know nothing
                        JSArray.setUnknownEntries(array, propertyNames, c);
                    }
                } else {
                    // we know nothing
                    JSArray.setUnknownEntries(array, Value.join(properties.getMaybe().stream().map(PKey::toValue).collect(Collectors.toList())), c);
                }
            }
        }
        if (properties.isDefaultArrayMaybePresent()) {
            JSArray.setUnknownEntries(array, Value.makeAnyStrUInt(), c);
        }
        if (properties.isDefaultNonArrayMaybePresent()) {
            JSArray.setUnknownEntries(array, Value.makeAnyStrNotUInt(), c);
        }
        return Value.makeObject(array);
    }


    /**
     * FIXME: review...
     * TODO: This function can be made more precise, see getPropertyNamesOrSymbolsArray for reference.
     * TODO: This is probably not sound??? (set includeSymbols to true?)
     */
    private static Value getPropertyValuesArray(ECMAScriptObjects nativeobject, CallInfo call, Solver.SolverInterface c) {
        State state = c.getState();
        DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
        Value objectArg = Conversion.toObject(c.getNode(), FunctionCalls.readParameter(call, state, 0), c);
        if (objectArg.isNone()) {
            return Value.makeNone();
        }
        ObjectLabel array = JSArray.makeArray(call.getSourceNode(), c);
        ObjProperties properties = state.getProperties(objectArg.getObjectLabels(), ObjProperties.PropertyQuery.makeQuery().onlyEnumerable());

        PropVarOperations pv = c.getAnalysis().getPropVarOperations();

        if (!properties.getMaybe().isEmpty()) {
            // Order of properties is the same as for-in: unspecified
            Value propertyValues = Value.makeNone();
            for (PKey property : properties.getMaybe()) {
                Value valueOfProperty = pv.readPropertyValue(objectArg.getObjectLabels(), property.toValue());
                propertyValues = propertyValues.join(valueOfProperty);
            }
            JSArray.setUnknownEntries(array, propertyValues, c);
        }
        if (properties.isDefaultArrayMaybePresent()) {
            Value property = Value.makeAnyStrUInt();
            Value valueOfProperty = pv.readPropertyValue(objectArg.getObjectLabels(), property);
            JSArray.setUnknownEntries(array, valueOfProperty, c);
        }
        if (properties.isDefaultNonArrayMaybePresent()) {
            Value property = Value.makeAnyStrNotUInt();
            Value valueOfProperty = pv.readPropertyValue(objectArg.getObjectLabels(), property);
            JSArray.setUnknownEntries(array, valueOfProperty, c);
        }

        return Value.makeObject(array);
    }

    /**
     * Transfer for Object.assign
     */
    public static Value assign(CallInfo call, Solver.SolverInterface c) {
        // 1. prepare target
        Value targetValue = Conversion.toObject(call.getSourceNode(), FunctionCalls.readParameter(call, c.getState(), 0), c);
        Set<ObjectLabel> targetObjects = targetValue.getObjectLabels();

        // 2. prepare sources
        List<Value> sources;
        if (call.isUnknownNumberOfArgs()) {
            sources = singletonList(call.getUnknownArg());
        } else {
            sources = newList();
            for (int i = 1; i < call.getNumberOfArgs(); i++) {
                sources.add(FunctionCalls.readParameter(call, c.getState(), i));
            }
        }
        Set<ObjectLabel> sourceObjects = sources.stream()
                .map(v -> UnknownValueResolver.getRealValue(v, c.getState()))
                .map(Value::restrictToNotNullNotUndef)
                .map(v -> Conversion.toObject(call.getSourceNode(), v, null))
                .map(Value::getObjectLabels)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        // 3. copy individual properties
        Set<Pair<ObjectLabel, ObjProperties>> propertiesToCopy = sourceObjects.stream()
                .map(l -> Pair.make(l, c.getState().getProperties(singleton(l), ObjProperties.PropertyQuery.makeQuery().onlyEnumerable())))
                .collect(Collectors.toSet());
        ParallelTransfer pt = new ParallelTransfer(c);
        propertiesToCopy.forEach(pair -> pt.add(() -> {
            // NB: minor unsoundness: since iteration order is unknown, the parallel transfer ought to be pushed down below the name iteration.
            Obj sourceObj = c.getState().getObject(pair.getFirst(), false);  // avoid prototypes: we can only copy properties from the object itself

            sourceObj.getProperties().forEach((name, value) -> {
                if (value.isUnknown()) {
                    value = UnknownValueResolver.getProperty(pair.getFirst(), name, c.getState(), true);
                }
                performSingleAssign(targetObjects, pair.getFirst(), name.toValue(), value, !pair.getSecond().isDefinite(), c);
            });
            if (pair.getSecond().isDefaultArrayMaybePresent()) {
                performSingleAssign(targetObjects, pair.getFirst(), Value.makeAnyStrUInt(), UnknownValueResolver.getDefaultArrayProperty(pair.getFirst(), c.getState()), true, c);
            }
            if (pair.getSecond().isDefaultNonArrayMaybePresent()) {
                performSingleAssign(targetObjects, pair.getFirst(), Value.makeAnyStrNotUInt(), UnknownValueResolver.getDefaultNonArrayProperty(pair.getFirst(), c.getState()), true, c);
            }
        }));
        if (sourceObjects.isEmpty() || sources.stream().anyMatch(v -> v.isMaybeNull() || v.isMaybeUndef())) {
            // the noop
            pt.add(() -> {
            });
        }
        pt.complete();
        return Value.makeObject(targetObjects);
    }

    /**
     * Utility function for {@link #assign(CallInfo, Solver.SolverInterface)}
     */
    private static void performSingleAssign(Set<ObjectLabel> targetObjects, ObjectLabel source, Value propName, Value value, boolean forceWeak, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        if (value.isMaybePresentAccessor() && UnknownValueResolver.getRealValue(value, c.getState()).isMaybeGetter()) {
            value = pv.readPropertyValue(singleton(source), propName); // trigger getters (also oin prototypes)
        }
        value = value.restrictToNotGetterSetter().removeAttributes();
        if (!value.isMaybePresent()) {
            return;
        }
        pv.writeProperty(targetObjects, propName, value, forceWeak);
    }

    public static Value evaluateToString(Value value, Solver.SolverInterface c) {
        return NativeObjectToString.evaluate(value, c);
    }
}
