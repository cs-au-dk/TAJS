/*
 * Copyright 2009-2017 Aarhus University
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
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.State.Properties;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;
import dk.brics.tajs.util.Strings;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.singleton;

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
                if (prototype.restrictToNotNull().isMaybeOtherThanObject()) {
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

                return stopPropagation? Value.makeNone(): Value.makeObject(obj);
            }

            case OBJECT_DEFINE_PROPERTY: { // 15.2.3.6
                Value o = FunctionCalls.readParameter(call, state, 0);
                Value propertyName = Conversion.toString(FunctionCalls.readParameter(call, state, 1), c);
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
                Value propertyName = Conversion.toString(FunctionCalls.readParameter(call, state1, 0), c);
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
                Value propval = Conversion.toString(v, c);
                if (propval.isNotStr())
                    return Value.makeNone();

                State.Properties properties = state.getProperties(thisobj, false, false);
                if (propval.isMaybeSingleStr()) {
                    String str = propval.getStr();
                    if (properties.getDefinitely().contains(str)) {
                        return Value.makeBool(true);
                    }
                    if (properties.getMaybe().contains(str)) {
                        return Value.makeAnyBool();
                    }
                    if (Strings.isArrayIndex(str) && properties.isArray()) {
                        return Value.makeAnyBool();
                    }
                    if (!Strings.isArrayIndex(str) && properties.isNonArray()) {
                        return Value.makeAnyBool();
                    }
                } else {
                    if (propval.isMaybeStrSomeNonUInt() && properties.isNonArray()) {
                        return Value.makeAnyBool();
                    }
                    if (propval.isMaybeStrUInt() && properties.isArray()) {
                        return Value.makeAnyBool();
                    }
                    if (properties.getMaybe().stream().anyMatch(p -> propval.isMaybeStr(p))) {
                        return Value.makeAnyBool();
                    }
                }
                return Value.makeBool(false);
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
                    Value val = pv.readPropertyDirect(thisobj, propname);
                    Value res = Value.makeNone();
                    if (val.isMaybeAbsent() || val.isMaybeDontEnum() || val.isNotPresent())
                        res = res.joinBool(false);
                    if (val.isMaybePresent() && val.isMaybeNotDontEnum())
                        res = res.joinBool(true);
                    return res;
                }
                for (ObjectLabel ol : thisobj) {
                    Obj o = state.getObject(ol, false);
                    for (String propname : o.getProperties().keySet()) {
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
                Value objectArg = UnknownValueResolver.getRealValue(FunctionCalls.readParameter(call, state, 0), state).restrictToObject();
                if (objectArg.isNone()) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                ObjectLabel array = JSArray.makeArray(call.getSourceNode(), c);
                State.Properties properties = state.getProperties(objectArg.getObjectLabels(), true, false);
                if (!properties.getMaybe().isEmpty()) {
                    if ((properties.getMaybe().size() < 2 || c.getAnalysis().getUnsoundness().mayUseSortedObjectKeys(call.getSourceNode())) && properties.isDefinite()) {
                        // we know the *order* of property names
                        List<String> sortedNames = newList(properties.getDefinitely());
                        Collections.sort(sortedNames);
                        JSArray.setEntries(array, sortedNames.stream().map(Value::makeStr).collect(Collectors.toList()), c);
                    } else {
                        // Order of properties is the same as for-in: unspecified.
                        List<Value> propertyNames = properties.getMaybe().stream().map(Value::makeStr).collect(Collectors.toList());
                        Value joinedPropertyNames = Value.join(propertyNames);
                        if (properties.isDefinite()) {
                            // we know the *number* of property names
                            List<Value> joinedPropertyNamesArray = propertyNames.stream().map(x -> joinedPropertyNames).collect(Collectors.toList());
                            JSArray.setEntries(array, joinedPropertyNamesArray, c);
                        } else {
                            // we know nothing
                            JSArray.setUnknownEntries(array, joinedPropertyNames, c);
                        }
                    }
                }
                if (properties.isArray()) {
                    JSArray.setUnknownEntries(array, Value.makeAnyStrUInt(), c);
                }
                if (properties.isNonArray()) {
                    JSArray.setUnknownEntries(array, Value.makeAnyStrNotUInt(), c);
                }
                return Value.makeObject(array);
            case OBJECT_FREEZE:
            case OBJECT_PREVENTEXTENSIONS:
            case OBJECT_SEAL:
                if (c.getAnalysis().getUnsoundness().maySkipMissingModelOfNativeFunction(call.getSourceNode(), nativeobject)) {
                    c.getMonitoring().addMessage(call.getJSSourceNode(), Message.Severity.TAJS_ERROR, "Warning: Calling " + nativeobject + ", but no side-effects have been implemented for it...");
                    return FunctionCalls.readParameter(call, state, 0);
                }
                // TODO: GitHub #249
                throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getSourceNode().getSourceLocation() + ": No transfer function for native function " + nativeobject);

            case OBJECT_GETOWNPROPERTYDESCRIPTOR: // FIXME: handle toObject/missing-object-typeerror cases
                Set<ObjectLabel> receivers = Conversion.toObjectLabels(c.getNode(), FunctionCalls.readParameter(call, state, 0), c);
                Value name = FunctionCalls.readParameter(call, state, 1);
                Str nameStr = Conversion.toString(name, c);
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

            case OBJECT_SETPROTOTYPEOF: {
                Set<ObjectLabel> rec = FunctionCalls.readParameter(call, state, 0).getObjectLabels();
                Value proto = FunctionCalls.readParameter(call, state, 1);
                boolean notNullOrObject = proto.restrictToNotObject().isMaybeOtherThanNull() || proto.restrictToNotNull().isMaybeOtherThanObject();
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
            Properties properties = state.getProperties(singleton(props), true, false);
            properties.getMaybe().forEach(prop -> {
                boolean forceWeak = !properties.getDefinitely().contains(prop);
                stopPropagation[0] |= definePropertyFromDescriptorsObjectProperty(target, props, Value.makeStr(prop), forceWeak, c).isNone();
            });
            if (properties.isArray()) {
                stopPropagation[0] |= definePropertyFromDescriptorsObjectProperty(target, props, Value.makeAnyStrUInt(), true, c).isNone();
            }
            if (properties.isNonArray()) {
                stopPropagation[0] |= definePropertyFromDescriptorsObjectProperty(target, props, Value.makeAnyStrNotUInt(), true, c).isNone();
            }
        }, c);
        return stopPropagation[0];
    }

    private static Value definePropertyFromDescriptorsObjectProperty(Value target, ObjectLabel propertyDescriptorsObject, Value propertyName, boolean forceWeak, Solver.SolverInterface c) {
        c.getMonitoring().visitPropertyRead(c.getNode(), singleton(propertyDescriptorsObject), propertyName, c.getState(), true);
        Value propertyDescriptorObject = c.getAnalysis().getPropVarOperations().readPropertyValue(singleton(propertyDescriptorsObject), propertyName);
        return defineProperty(target, propertyName, propertyDescriptorObject, forceWeak, c);
    }

    private static Value defineProperty(Value o, Value propertyName, Value propertyDescriptorObject, boolean forceWeak, Solver.SolverInterface c) {
        PropertyDescriptor desc = PropertyDescriptor.toDefinePropertyPropertyDescriptor(propertyDescriptorObject, c);
        Value value = desc.makePropertyWithAttributes();
        if (value.isNone())
            return Value.makeNone();
        c.getAnalysis().getPropVarOperations().writeProperty(o.getObjectLabels(), propertyName, value, true, true, forceWeak, true); // FIXME: trigger TypeError exception if attempt to overwrite non-writable (#291)
        return o;
    }

    public static Value evaluateToString(Value value, Solver.SolverInterface c) {
        return NativeObjectToString.evaluate(value, c);
    }
}
