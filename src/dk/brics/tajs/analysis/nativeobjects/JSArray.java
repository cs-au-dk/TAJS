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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.js.UserFunctionCalls;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.lattice.Bool;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * 15.4 native Array functions.
 */
public class JSArray {

    private JSArray() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, Solver.SolverInterface c) {
        if (nativeobject != ECMAScriptObjects.ARRAY)
            if (NativeFunctions.throwTypeErrorIfConstructor(call, c))
                return Value.makeNone();

        final State state = c.getState();
        final PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        switch (nativeobject) {

            case ARRAY: { // 15.4, no difference between function and constructor
                NativeFunctions.expectParameters(nativeobject, call, c, 0, -1);
                // 15.4.1.1, 15.4.2.1 paragraph 2 and 3, 15.4.2.2 paragraph 1.
                ObjectLabel objlabel = makeArray(call.getSourceNode(), c);

                Value length = Value.makeAnyNumUInt();
                int numArgs = call.getNumberOfArgs();
                boolean isArrayLiteral = call.getSourceNode() instanceof CallNode && ((CallNode) call.getSourceNode()).getLiteralConstructorKind() == CallNode.LiteralConstructorKinds.ARRAY;
                if (call.isUnknownNumberOfArgs())
                    pv.writeProperty(Collections.singleton(objlabel), Value.makeAnyStrUInt(), call.getUnknownArg(), false, true);
                else if (numArgs == 1 && !isArrayLiteral) { // 15.4.2.2, paragraph 2.
                    Value lenarg = NativeFunctions.readParameter(call, state, 0);
                    Status s;
                    if (lenarg.isMaybeSingleNum()) {
                        double d = lenarg.getNum();
                        if (d >= 0 && d < 2147483647d && Math.floor(d) == d) {
                            s = Status.NONE;
                            length = Value.makeNum(d);
                        } else
                            s = Status.CERTAIN;
                    } else if (lenarg.isMaybeNumUInt() && !lenarg.isMaybeNumOther() && !lenarg.isMaybeInf() && !lenarg.isMaybeNaN())
                        s = Status.NONE; // We're good: unknown UInt and nothing else.
                    else if (!lenarg.isMaybeNumUInt() && (lenarg.isMaybeNumOther() || lenarg.isMaybeInf() || lenarg.isMaybeNaN()))
                        s = Status.CERTAIN; // We're not good: definitely not UInt but something else.
                    else if (lenarg.isMaybeFuzzyNum())
                        s = Status.MAYBE; // Who knows: might be an UInt.
                    else {
                        s = Status.NONE; // Definitely not a number. See writing of zeroprop below.
                        length = Value.makeNone();
                    }
                    if (s == Status.CERTAIN && lenarg.isMaybeOtherThanNum())
                        s = Status.MAYBE;
                    if (s != Status.NONE) {
                        Exceptions.throwRangeError(c);
                        c.getMonitoring().addMessage(call.getSourceNode(), Severity.HIGH, "RangeError, invalid value of array length");
                    }
                    if (s == Status.CERTAIN)
                        return Value.makeNone();
                    if (lenarg.isMaybeOtherThanNum()) { // 15.4.2.2, paragraph 3.
                        length = length.joinNum(1);
                        Value zeroprop = lenarg.restrictToNotNum();
                        if (!lenarg.isNotNum())
                            zeroprop = zeroprop.joinAbsent();
                        pv.writeProperty(objlabel, "0", zeroprop);
                    }
                } else { // 15.4.2.1
                    length = Value.makeNum(numArgs);
                    for (int i = 0; i < numArgs; i++) {
                        boolean isAbsent = isArrayLiteral && ((CallNode) call.getSourceNode()).getArgRegister(i) == AbstractNode.NO_VALUE;
                        // support for the array literal syntax with omitted values: ['foo',,,,'bar']
                        if (!isAbsent) {
                            Value parameter = NativeFunctions.readParameter(call, state, i);
                            pv.writeProperty(objlabel, Integer.toString(i), parameter);
                        }
                    }
                }
                writeLength(objlabel, length, c);
                return Value.makeObject(objlabel);
            }

            case ARRAY_ISARRAY: { // 15.4.3.2
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value arg = NativeFunctions.readParameter(call, state, 0);
                Value result = Value.makeNone();
                if (arg.isMaybePrimitive())
                    result = result.joinBool(false);
                for (ObjectLabel l : arg.getObjectLabels()) {
                    if (l.getKind() == Kind.ARRAY)
                        result = result.joinBool(true);
                    else
                        result = result.joinBool(false);
                }
                return result;
            }

            case ARRAY_TOSTRING: // 15.4.4.2
            case ARRAY_TOLOCALESTRING: // 15.4.4.3
            case ARRAY_JOIN: { // 15.4.4.5
                boolean is_join = nativeobject == ECMAScriptObjects.ARRAY_JOIN;
                boolean is_toString = nativeobject == ECMAScriptObjects.ARRAY_TOSTRING;
                boolean is_toLocaleString = nativeobject == ECMAScriptObjects.ARRAY_TOLOCALESTRING;
                if (!is_join)
                    if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.ARRAY))
                        return Value.makeNone();
                NativeFunctions.expectParameters(nativeobject, call, c, 0, is_join ? 1 : 0);
                Set<ObjectLabel> objlabels = state.readThisObjects();
                c.getMonitoring().visitPropertyRead(call.getJSSourceNode(), objlabels, Value.makeAnyStrUInt(), state, false);
                Value length_val = readLength(objlabels, c);
                Double length_prop = UnknownValueResolver.getRealValue(length_val, state).getNum();
                long length = length_prop == null ? -1 : Conversion.toUInt32(length_prop);
                Value sepArg = NativeFunctions.readParameter(call, state, 0);
                boolean sepArgIsUndef = sepArg.restrictToNotUndef().isNone();
                if (length_prop == null || (is_join && (call.isUnknownNumberOfArgs() || ((!sepArgIsUndef) && sepArg.isMaybeOtherThanStr()))))
                    return Value.makeAnyStr();
                String sep = is_toString || call.getNumberOfArgs() < 1 || sepArgIsUndef ? "," : Conversion.toString(sepArg, c).getStr();
                if (length == 0)
                    return Value.makeStr("");
                if (sep == null /* = sep is a fuzzy string */ || is_toLocaleString)
                    return Value.makeAnyStr(); // TODO: Reuse the function body by calling toObject(readProperty(i)).toLocaleString() instead of toString(readProperty(i)).
                Value zeroArg = pv.readPropertyValue(objlabels, "0");
                zeroArg = UnknownValueResolver.getRealValue(zeroArg, state);
                String resString = "";
                zeroArg = zeroArg.restrictToNotNullNotUndef();
                resString += zeroArg.isMaybeOtherThanUndef() ? Conversion.toString(zeroArg, c).getStr() + sep : "";
                for (int i = 1; i < length; i++) {
                    Value prop = pv.readPropertyValue(objlabels, Integer.toString(i));
                    prop = UnknownValueResolver.getRealValue(prop, state);
                    Value tmpStr = Conversion.toString(prop.restrictToNotNullNotUndef(), c);
                    if (!tmpStr.isMaybeSingleStr() && !(tmpStr.isNone() && (prop.isMaybeUndef() || prop.isMaybeNull())))
                        return Value.makeAnyStr();
                    String vs = tmpStr.getStr();
                    if (prop.isMaybeNull() || prop.isMaybeUndef())
                        resString += sep;
                    else
                        resString = resString + vs + sep;
                }
                return Value.makeStr(resString.substring(0, resString.length() - sep.length()));
            }

            case ARRAY_CONCAT: { // 15.4.4.4
                NativeFunctions.expectParameters(nativeobject, call, c, 0, -1);

                ObjectLabel resultLabel = makeArray(call.getSourceNode(), c);

                Set<ObjectLabel> resultLabelAsSet = Collections.singleton(resultLabel);
                Value resultArray = Value.makeObject(resultLabel);
                // the spec essentially says that the this-object is the very first argument, and that arrays should be unfolded

                // all the unfolded elements in order. The order of the elements is insignificant if `isPreciseUnfolding == false`
                List<Value> unfoldedElements = newList();
                boolean isPreciseUnfolding = true;
                if (!call.isUnknownNumberOfArgs()) {
                    for (int i = -1; i < call.getNumberOfArgs(); i++) {
                        List<Value> unfoldedElement = newList();
                        final Value element;
                        if (i == -1) {
                            element = state.readThis();
                        } else {
                            element = call.getArg(i);
                        }

                        Pair<Set<ObjectLabel>, Value> separatedArrayValues = separateArrayValues(element, state);

                        // if the element represents an array, it should be unfolded to multiple elements
                        // if the element represents multiple arrays, they should have the same length - if we are to be precise
                        // if the element represents arrays and non-array, the arrays should have length one - if we are to be precise

                        // the required array length for high precision. null means that the array length can be any length
                        Long expectedLengthArrayLength = null;

                        if (!separatedArrayValues.getSecond().isNone()) {
                            expectedLengthArrayLength = 1L;
                            unfoldedElement.add(separatedArrayValues.getSecond());
                        }
                        for (ObjectLabel arrayLabel : separatedArrayValues.getFirst()) {
                            // resolve the length, a value of null means that it is not coercible to a single, precise number
                            Value lengthValue = readLength(singleton(arrayLabel), c);
                            Double lengthNum = Conversion.toNumber(UnknownValueResolver.getRealValue(lengthValue, state), c).getNum();
                            Long arrayLength = lengthNum != null ? Conversion.toUInt32(lengthNum) : null;

                            if (arrayLength == null || (expectedLengthArrayLength != null && !Objects.equals(expectedLengthArrayLength, arrayLength))) {
                                isPreciseUnfolding = false;
                            }

                            // unfold the array
                            if (arrayLength == null) {
                                // imprecise case: just add the element
                                unfoldedElement.add(pv.readPropertyValue(singleton(arrayLabel), Value.makeAnyStrUInt()));
                            } else {
                                for (int j = 0; j < arrayLength; j++) {
                                    final Value arrayEntry = pv.readPropertyWithAttributes(singleton(arrayLabel), Value.makeTemporaryStr(j + ""));
                                    if (unfoldedElement.size() <= j) {
                                        unfoldedElement.add(arrayEntry);
                                    } else {
                                        Value joined = UnknownValueResolver.join(unfoldedElement.get(j), arrayEntry, state);
                                        unfoldedElement.set(j, joined);
                                    }
                                }
                            }
                        }

                        unfoldedElements.addAll(unfoldedElement);
                    }
                } else {
                    unfoldedElements.add(call.getUnknownArg());
                    isPreciseUnfolding = false;
                }

                if (isPreciseUnfolding) {
                    for (int i = 0; i < unfoldedElements.size(); i++) {
                        Value unfoldedElement = unfoldedElements.get(i);
                        boolean maybeAbsent = unfoldedElement.isMaybeAbsent();
                        unfoldedElement = unfoldedElement.restrictToNotAbsent();
                        if (!unfoldedElement.isNone()) {
                            pv.writeProperty(resultLabelAsSet, Value.makeTemporaryStr(i + ""), unfoldedElement, maybeAbsent, true);
                        }
                    }
                    Value length = Value.makeNum(unfoldedElements.size()).setAttributes(true, true, false);
                    writeLength(resultLabel, length, c);
                } else {
                    Value v = UnknownValueResolver.join(unfoldedElements,state);
                    pv.writeProperty(Collections.singleton(resultLabel), Value.makeAnyStrUInt(), v, false, true);
                    writeLength(resultLabel, Value.makeAnyNumUInt().setAttributes(true, true, false), c);
                }
                return resultArray;
            }

            case ARRAY_POP: { // 15.4.4.6
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                Set<ObjectLabel> thisobj = state.readThisObjects();
                Value length_val = readLength(thisobj, c);
                Double length_prop_num = UnknownValueResolver.getRealValue(length_val, state).getNum();
                long length = length_prop_num != null ? Conversion.toUInt32(length_prop_num) : -1;
                if (length == 0) // 15.4.4.6 item 3 and item 5.
                    return Value.makeUndef();
                Value res;
                Value new_len;
                if (length > 0) {
                    String index = String.valueOf(length - 1);
                    res = UnknownValueResolver.getRealValue(pv.readPropertyValue(thisobj, index), state);
                    c.getMonitoring().visitPropertyRead(call.getJSSourceNode(), state.readThisObjects(), Value.makeTemporaryStr(index), state, false);
                    pv.deleteProperty(thisobj, Value.makeStr(index), false);
                    new_len = Value.makeNum(length - 1);
                } else {
                    res = UnknownValueResolver.getRealValue(pv.readPropertyValue(thisobj, Value.makeAnyStrUInt()), state);
                    c.getMonitoring().visitPropertyRead(call.getJSSourceNode(), state.readThisObjects(), Value.makeAnyStrUInt(), state, false);
                    pv.deleteProperty(thisobj, Value.makeAnyStrUInt(), false);
                    new_len = Value.makeAnyNumUInt();
                }
                writeLength(thisobj, new_len, c);
                return res;
            }

            case ARRAY_PUSH: { // 15.4.4.7
                Set<ObjectLabel> arr = state.readThisObjects();
                Value new_len = Value.makeAnyNumUInt();
                if (call.isUnknownNumberOfArgs()) {
                    pv.writeProperty(arr, Value.makeAnyStrUInt(), call.getUnknownArg(), false, true);
                } else if (arr.size() == 1) {
                    Value len_val = UnknownValueResolver.getRealValue(readLength(arr, c), state);
                    Double length_prop = len_val.getNum();
                    long length = length_prop != null ? Conversion.toUInt32(length_prop) : -1;
                    int i;
                    for (i = 0; i < call.getNumberOfArgs(); i++) {
                        Value v = NativeFunctions.readParameter(call, state, i);
                        if (length > -1)
                            pv.writeProperty(arr, Value.makeTemporaryStr(String.valueOf(i + length)), v, false, true);
                        else {
                            pv.writeProperty(arr, Value.makeAnyStrUInt(), v, false, true);
                            break;
                        }
                    }
                    if (length > -1)
                        new_len = Value.makeNum(i + length);
                } else
                    for (int i = 0; i < call.getNumberOfArgs(); i++)
                        pv.writeProperty(arr, Value.makeAnyStrUInt(), NativeFunctions.readParameter(call, state, i), false, true);
                writeLength(arr, new_len, c);
                return new_len;
            }

            case ARRAY_REVERSE: { // 15.4.4.8
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                Set<ObjectLabel> thisobj = state.readThisObjects();
                Value length_val = readLength(thisobj, c);
                Double length_prop_num = UnknownValueResolver.getRealValue(length_val, state).getNum();
                long length = length_prop_num != null ? Conversion.toUInt32(length_prop_num) : -1;
                if (length > -1 && length <= 1) // 15.4.4.8 item 5
                    return Value.makeObject(thisobj);

                c.getMonitoring().visitPropertyRead(call.getJSSourceNode(), thisobj, Value.makeAnyStrUInt(), state, false);
                if (length > 0) {
                    for (int k = 0; k < Math.floor(length / 2); k++) {
                        String s1 = Integer.toString(k), s2 = Integer.toString(Long.valueOf(length).intValue() - k - 1);
                        Value near_start = pv.readPropertyWithAttributes(thisobj, s1);
                        Value near_end = pv.readPropertyWithAttributes(thisobj, s2);
                        if (near_start.isNotPresent())
                            pv.deleteProperty(thisobj, Value.makeStr(s2), false);
                        else if (near_start.isMaybePresent())
                            pv.writePropertyWithAttributes(thisobj, s2, near_start);
                        if (near_end.isNotPresent())
                            pv.deleteProperty(thisobj, Value.makeStr(s1), false);
                        else if (near_end.isMaybePresent())
                            pv.writePropertyWithAttributes(thisobj, s1, near_end);
                    }
                } else {
                    Value v = pv.readPropertyWithAttributes(thisobj, Value.makeAnyStrUInt());
                    if (v.isMaybePresent())
                        pv.writeProperty(thisobj, Value.makeAnyStrUInt(), v, false, true);
                }
                return Value.makeObject(thisobj);
            }

            case ARRAY_SHIFT: {
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                // perform the operation on a per-array basis to avoid exchange of values among arrays
                Set<ObjectLabel> thisObjects = state.readThisObjects();
                Value firstElement = UnknownValueResolver.getRealValue(pv.readPropertyValue(thisObjects, "0"), state);
                boolean moreThanOneArray = thisObjects.size() > 1;
                for (ObjectLabel current : thisObjects) {
                    Set<ObjectLabel> thisObj = Collections.singleton(current);
                    Value length_val = readLength(thisObj, c);
                    Double length_prop_num = UnknownValueResolver.getRealValue(length_val, state).getNum();
                    long length = length_prop_num != null ? Conversion.toUInt32(length_prop_num) : -1;
                    final Value new_length;
                    if (length != -1) {
                        // precise case: length is known
                        for (int i = 1; i < length; i++) { // TODO: this may be bad if the array length is high
                            String s = Integer.toString(i);
                            Bool is_def = pv.hasProperty(thisObj, s);
                            if (is_def.isMaybeTrue()) {
                                Value elem = pv.readPropertyWithAttributes(thisObj, s);
                                pv.writeProperty(thisObj, Value.makeTemporaryStr(Integer.toString(i - 1)), elem, moreThanOneArray, true);
                            } else
                                pv.deleteProperty(thisObj, Value.makeTemporaryStr(Integer.toString(i - 1)), moreThanOneArray);
                        }
                        pv.deleteProperty(thisObj, Value.makeTemporaryStr(Long.toString(length - 1)), moreThanOneArray);
                        if (length == 0) {
                            new_length = Value.makeNum(0);
                        } else {
                            new_length = Value.makeNum(length - 1);
                        }
                    } else {
                        // imprecise case: length is not known --> mix all array properties
                        new_length = Value.makeAnyNumUInt();
                        Value defaultArrayProperty = UnknownValueResolver.getDefaultArrayProperty(current, state);
                        if (!defaultArrayProperty.restrictToNotAbsent().isNone()) {
                            pv.writeProperty(thisObj, Value.makeAnyStrUInt(), defaultArrayProperty, moreThanOneArray, true);
                        }
                        pv.deleteProperty(thisObj, Value.makeAnyStrUInt(), moreThanOneArray);
                    }
                    writeLength(thisObj, new_length, c);
                }
                return firstElement;
            }

            case ARRAY_SLICE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 2);

                Set<ObjectLabel> thisObjects = state.readThisObjects();

                ObjectLabel resultLabel = makeArray(call.getSourceNode(), c);

                Set<ObjectLabel> resultLabelAsSet = Collections.singleton(resultLabel);
                Value resultArray = Value.makeObject(resultLabel);
                boolean isKnownArgs = !call.isUnknownNumberOfArgs();

                if (isKnownArgs) {
                    // resolve the fromIndex, a value of null means that it is not coercible to a single, precise number
                    Value fromIndexValue = UnknownValueResolver.getRealValue(call.getArg(0), state);
                    if (fromIndexValue.isMaybeUndef()) {
                        fromIndexValue = fromIndexValue.restrictToNotUndef().joinNum(0);
                    }
                    Double fromIndexNum = Conversion.toNumber(fromIndexValue, c).getNum();
                    Long fromIndex = fromIndexNum != null ? Conversion.toUInt32(fromIndexNum) : null;

                    // resolve the toIndex, a value of null means that it is not coercible to a single, precise number
                    Value toIndexValue = UnknownValueResolver.getRealValue(call.getArg(1), state);
                    Long toIndex;
                    if (toIndexValue.isMaybeUndef() && !toIndexValue.isMaybeOtherThanUndef()) {
                        toIndex = Long.MAX_VALUE; // default value: array length, resolved later
                    } else {
                        Double toIndexNum = Conversion.toNumber(toIndexValue, c).getNum();
                        toIndex = toIndexNum != null ? Conversion.toUInt32(toIndexNum) : null;
                    }

                    for (ObjectLabel thisObject : thisObjects) {
                        Set<ObjectLabel> thisObjectAsSet = Collections.singleton(thisObject);
                        // resolve the length, a value of null means that it is not coercible to a single, precise number
                        Value thisLengthValue = readLength(thisObjectAsSet, c);
                        Double thisLengthNum = Conversion.toNumber(UnknownValueResolver.getRealValue(thisLengthValue, state), c).getNum();
                        Long thisLength = thisLengthNum != null ? Conversion.toUInt32(thisLengthNum) : null;

                        // rectify negative index as offset from the end
                        if (fromIndex != null && fromIndex < 0) {
                            if (thisLength == null) {
                                fromIndex = null;
                            } else {
                                fromIndex = Math.max(thisLength + fromIndex, 0);
                            }
                        }
                        // rectify negative index as offset from the end
                        if (toIndex != null && toIndex < 0) {
                            if (thisLength == null) {
                                toIndex = null;
                            } else {
                                toIndex = Math.max(thisLength + toIndex, 0);
                            }
                        }

                        // rectify overly large end index
                        if (toIndex != null) {
                            if (thisLength == null) {
                                toIndex = null;
                            } else {
                                toIndex = Math.min(thisLength, toIndex);
                            }
                        }

                        // we are precise if we know fromIndex and toIndex exactly, even if we do not know the exact length of the array!
                        boolean isPrecise = fromIndex != null && toIndex != null;
                        if (isPrecise) {
                            long length = Math.max(toIndex - fromIndex, 0);
                            writeLength(resultLabel, Value.makeNum(length).setAttributes(true, true, false), c);

                            // shallow copy each index value
                            for (long offset = 0; offset < length; offset++) {
                                Value toMove = pv.readPropertyWithAttributes(thisObjectAsSet, Value.makeTemporaryStr((fromIndex + offset) + ""));
                                if (toMove.restrictToNotAbsent().isNone()) {
                                    continue;
                                }
                                if (toMove.isMaybeAbsent()) {
                                    toMove = toMove.joinUndef().restrictToNotAbsent();
                                }
                                pv.writeProperty(resultLabelAsSet, Value.makeTemporaryStr(offset + ""), toMove, thisObjects.size() != 1, true);
                            }
                            return resultArray;
                        } else {
                            // force reads
                            pv.readPropertyValue(thisObjects, Value.makeAnyStrUInt());
                        }
                    }
                } else {
                    // force reads
                    Conversion.toInteger(call.getUnknownArg(), c);
                }

                // fallback if no more precise branches has returned
                Value v = pv.readPropertyValue(state.readThisObjects(), Value.makeAnyStrUInt());
                pv.writeProperty(Collections.singleton(resultLabel), Value.makeAnyStrUInt(), v, false, true);
                writeLength(resultLabel, Value.makeAnyNumUInt().setAttributes(true, true, false), c);
                return resultArray;
            }

            case ARRAY_SOME: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
                c.getMonitoring().visitPropertyRead(call.getJSSourceNode(), state.readThisObjects(), Value.makeAnyStrUInt(), state, false);
                @SuppressWarnings("unused") Value callback = NativeFunctions.readParameter(call, state, 0);
                @SuppressWarnings("unused") Value thisfn = call.getNumberOfArgs() >= 1 ? NativeFunctions.readParameter(call, state, 1) : Value.makeNone();
                return Value.makeAnyBool();
            }

            case ARRAY_SORT: {
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
                c.getMonitoring().visitPropertyRead(call.getJSSourceNode(), state.readThisObjects(), Value.makeAnyStrUInt(), state, false);
                Value comparefn = NativeFunctions.readParameter(call, state, 0);
                Set<ObjectLabel> thisobj = state.readThisObjects();

                for (int i = 0; i < 2; i++) { // 2 enough, we just need the feedback loop
                    if (!comparefn.isNone()) {
                        if (Conversion.isMaybeNonCallable(comparefn.restrictToNotUndef())) {
                            Exceptions.throwTypeError(c);
                            c.getMonitoring().addMessage(call.getSourceNode(), Severity.HIGH,
                                    "TypeError, invalid argument to Array.prototype.sort");
                        }
                        List<Value> result = newList();
                        boolean anyHostFunctions = false;
                        if (comparefn.isMaybeUndef()) {
                            anyHostFunctions = true; // 'undefined' is like a special comparefn
                            result.add(Value.makeAnyNum()); // the actual value doesn't matter here
                        }
                        BasicBlock implicitAfterCall = null;
                        for (ObjectLabel obj : comparefn.getObjectLabels()) {
                            if (obj.getKind() == Kind.FUNCTION) {
                                if (obj.isHostObject()) { // weird, but possible
                                    anyHostFunctions = true;
                                    // TODO: comparefn is a host object, should invoke it (but unlikely worthwhile to implement...), see test/micro/arraysort2.js
                                    c.getMonitoring().addMessage(c.getNode(), Message.Severity.HIGH, "Ignoring host object comparefn in Array.prototype.sort");
                                } else {
                                    implicitAfterCall = UserFunctionCalls.implicitUserFunctionCall(obj, new FunctionCalls.DefaultImplicitCallInfo(c) {
                                        @Override
                                        public Value getArg(int i) {
                                            return pv.readPropertyValue(thisobj, Value.makeAnyStrUInt());
                                        }

                                        @Override
                                        public int getNumberOfArgs() {
                                            return 2;
                                        }

                                        @Override
                                        public Value getUnknownArg() {
                                            throw new AnalysisException("Should not be called. Arguments are not unknown.");
                                        }

                                        @Override
                                        public boolean isUnknownNumberOfArgs() {
                                            return false;
                                        }
                                    }, c);
                                }
                            }
                        }
                        if (UserFunctionCalls.implicitUserFunctionReturn(result, anyHostFunctions, implicitAfterCall, c).isNone())
                            return Value.makeNone();
                    }
                    pv.writeProperty(thisobj, Value.makeAnyStrUInt(), pv.readPropertyValue(thisobj, Value.makeAnyStrUInt()), false, true);
                    if (comparefn.isNone()) {
                        break;
                    }
                }
                return Value.makeObject(thisobj);
            }

            case ARRAY_SPLICE: {
                // TODO: improve precision?
                NativeFunctions.expectParameters(nativeobject, call, c, 2, -1);

                // construct return value
                ObjectLabel resultArray = makeArray(call.getSourceNode(), c);
                Value arrayValues = pv.readPropertyValue(state.readThisObjects(), Value.makeAnyStrUInt());
                pv.writeProperty(Collections.singleton(resultArray), Value.makeAnyStrUInt(), arrayValues, false, true);
                writeLength(resultArray, Value.makeAnyNumUInt().setAttributes(true, true, false), c);

                // mutate the input
                Set<ObjectLabel> thisObjects = state.readThisObjects();
                readLength(thisObjects, c); // force read-side-effects
                Value parameters = Value.makeNone();
                if (call.isUnknownNumberOfArgs())
                    parameters = UnknownValueResolver.join(parameters, call.getUnknownArg(), state);
                else
                    for (int i = 2; i < call.getNumberOfArgs(); i++)
                        parameters = UnknownValueResolver.join(parameters, NativeFunctions.readParameter(call, state, i), state);
                pv.deleteProperty(thisObjects, Value.makeAnyStrUInt(), true);
                pv.writeProperty(thisObjects, Value.makeAnyStrUInt(), parameters.join(arrayValues), false, false);
                writeLength(thisObjects, Value.makeAnyNumUInt(), c);

                return Value.makeObject(resultArray);
            }

            case ARRAY_UNSHIFT: { // 15.4.4.13
                // TODO merge implementation with ARRAY_SHIFT?
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
                // perform the operation on a per-array basis to avoid exchange of values among arrays
                Set<ObjectLabel> thisObjects = state.readThisObjects();
                boolean moreThanOneArray = thisObjects.size() > 1;
                Value sharedNewLength = Value.makeNone();
                for (ObjectLabel current : thisObjects) {
                    Set<ObjectLabel> thisObj = Collections.singleton(current);
                    Value length_val = readLength(thisObj, c);
                    Double length_prop_num = UnknownValueResolver.getRealValue(length_val, state).getNum();
                    long length = length_prop_num != null ? Conversion.toUInt32(length_prop_num) : -1;
                    final Value new_length;
                    if (length != -1) {
                        // precise case: length is known
                        for (long i = length - 1; i >= 0; i--) { // TODO: this may be bad if the array length is high
                            String s = Long.toString(i);
                            Bool is_def = pv.hasProperty(thisObj, s);
                            if (is_def.isMaybeTrue()) {
                                Value elem = pv.readPropertyWithAttributes(thisObj, s);
                                pv.writeProperty(thisObj, Value.makeTemporaryStr(Long.toString(i + 1)), elem, moreThanOneArray, true);
                            } else
                                pv.deleteProperty(thisObj, Value.makeTemporaryStr(Long.toString(i + 1)), moreThanOneArray);
                        }
                        new_length = Value.makeNum(length + 1);
                    } else {
                        // imprecise case: length is not known --> mix all array properties
                        new_length = Value.makeAnyNumUInt();
                        pv.writeProperty(thisObj, Value.makeAnyStrUInt(), pv.readPropertyValue(thisObj, Value.makeAnyStrUInt()).removeAttributes(), moreThanOneArray, true);
                        pv.deleteProperty(thisObj, Value.makeAnyStrUInt(), moreThanOneArray);
                    }
                    writeLength(thisObj, new_length, c);
                    sharedNewLength = sharedNewLength.join(new_length);
                }
                pv.writeProperty(thisObjects, Value.makeTemporaryStr("0"), NativeFunctions.readParameter(call, state, 0).removeAttributes(), moreThanOneArray, true);
                return sharedNewLength;
            }

            case ARRAY_INDEXOF: { // 15.4.4.14
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
                c.getMonitoring().visitPropertyRead(call.getJSSourceNode(), state.readThisObjects(), Value.makeAnyStrUInt(), state, false);
            /* Value searchElement =*/
                NativeFunctions.readParameter(call, state, 0);
                Value fromIndex = call.getNumberOfArgs() > 1 ? Conversion.toInteger(NativeFunctions.readParameter(call, state, 1), c) : Value.makeNum(0); // TODO: sometimes certain?
                Double fromindex_num = UnknownValueResolver.getRealValue(fromIndex, state).getNum();
                int n = fromindex_num == null ? -1 : fromindex_num.intValue();
                Set<ObjectLabel> thisobj = state.readThisObjects();
                Value length_val = readLength(thisobj, c);
                Double length_prop_num = UnknownValueResolver.getRealValue(length_val, state).getNum();
                long length = length_prop_num != null ? Conversion.toUInt32(length_prop_num) : -1;
                if (length == 0 || (n > length && length > 0)) // 15.4.4.14 item 4 and item 6
                    return Value.makeNum(-1);
                return Value.makeAnyNumOther();
            }

            default:
                return null;
        }
    }

    /**
     * Separates a value into a part with Array-objectLabels and a part without.
     * Will resolve unknown values.
     */
    private static Pair<Set<ObjectLabel>, Value> separateArrayValues(Value value, State state) {
        final Set<ObjectLabel> arrays = newSet();
        value = UnknownValueResolver.getRealValue(value, state);

        for (ObjectLabel objectLabel : value.getObjectLabels()) {
            if (objectLabel.getKind() == Kind.ARRAY) {
                arrays.add(objectLabel);
            }
        }
        final Value nonArrays = value.removeObjects(arrays);
        return Pair.make(arrays, nonArrays);
    }

    /*
     * Methods for reading and writing length of an array.
     * Removes spurious NaNs (could remove more), but that should not be needed once Array.length is implemented with getter and setter
     */
    private static Value readLength(Set<ObjectLabel> thisobj, Solver.SolverInterface c) {
        return UnknownValueResolver.getRealValue(c.getAnalysis().getPropVarOperations().readPropertyValue(thisobj, "length"), c.getState()).restrictToNotNaN();
    }

    private static void writeLength(ObjectLabel resultLabel, Value length, Solver.SolverInterface c) {
        writeLength(singleton(resultLabel), length, c);
    }

    private static void writeLength(Set<ObjectLabel> thisObj, Value length, Solver.SolverInterface c) {
        c.getAnalysis().getPropVarOperations().writeProperty(thisObj, Value.makeTemporaryStr("length"), length, false, false);
    }

    public static ObjectLabel makeArray(AbstractNode allocationNode, Solver.SolverInterface c) {
        return makeArray(allocationNode, null, c);
    }

    public static ObjectLabel makeArray(AbstractNode allocationNode, HeapContext heapContext, Solver.SolverInterface c) {
        ObjectLabel array = new ObjectLabel(allocationNode, Kind.ARRAY, heapContext);
        c.getState().newObject(array);
        c.getState().writeInternalPrototype(array, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));
        writeLength(array, Value.makeNum(0), c);
        return array;
    }

    public static void setEntries(ObjectLabel array, List<Value> content, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        for (int i = 0; i < content.size(); i++) {
            pv.writeProperty(array, "" + i, content.get(i));
        }
        writeLength(array, Value.makeNum(content.size()), c);
    }

    public static void setUnknownEntries(ObjectLabel array, Value content, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        pv.writeProperty(singleton(array), Value.makeAnyStrUInt(), content, false, true);
        writeLength(array, Value.makeAnyNumUInt(), c);
    }
}