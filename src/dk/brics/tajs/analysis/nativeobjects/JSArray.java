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

package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.ParallelTransfer;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.js.UserFunctionCalls;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
import dk.brics.tajs.lattice.Bool;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.PKey.StringPKey;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Summarized;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;
import dk.brics.tajs.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * 15.4 native Array functions.
 */
public class JSArray {

    /**
     * TODO: Since Array.prototype.join can call itself without using the worklist, a cyclic array can cause a StackOverflowError. This stack is used to guard against that error.
     */
    private static final Stack<Set<ObjectLabel>> cyclicJoinGuard = new Stack<>();

    private JSArray() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, Solver.SolverInterface c) {
        final State state = c.getState();
        final PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        switch (nativeobject) {

            case ARRAY: { // 15.4, no difference between function and constructor
                // 15.4.1.1, 15.4.2.1 paragraph 2 and 3, 15.4.2.2 paragraph 1.
                ObjectLabel objlabel = makeArray(call.getSourceNode(), c);

                Value length = Value.makeAnyNumUInt();

                boolean isArrayLiteral = call.getSourceNode() instanceof CallNode && ((CallNode) call.getSourceNode()).getLiteralConstructorKind() == CallNode.LiteralConstructorKinds.ARRAY;
                if (call.isUnknownNumberOfArgs())
                    pv.writeProperty(Collections.singleton(objlabel), Value.makeAnyStrUInt(), call.getUnknownArg().summarize(new Summarized(objlabel)));
                else {
                    int numArgs = call.getNumberOfArgs();
                    if (numArgs == 1 && !isArrayLiteral) { // 15.4.2.2, paragraph 2.
                        Value lenarg = FunctionCalls.readParameter(call, state, 0).summarize(new Summarized(objlabel));
                        Status s;
                        if (lenarg.isMaybeSingleNum()) {
                            double d = lenarg.getNum();
                            if (d >= 0 && d < Math.pow(2, 32) && Math.floor(d) == d) {
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
                            Exceptions.throwRangeError(c, s == Status.MAYBE);
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
                                Value parameter = FunctionCalls.readParameter(call, state, i).summarize(new Summarized(objlabel));
                                pv.writeProperty(objlabel, Integer.toString(i), parameter);
                            }
                        }
                    }
                }
                writeLength(objlabel, length, c);
                return Value.makeObject(objlabel);
            }

            case ARRAY_ISARRAY: { // 15.4.3.2
                Value arg = FunctionCalls.readParameter(call, state, 0);
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

            case ARRAY_TOSTRING: { // 15.4.4.2
                return evaluateToString(state.readThis(), c);
            }

            case ARRAY_TOLOCALESTRING: // 15.4.4.3
            case ARRAY_JOIN: { // 15.4.4.5
                boolean is_toLocaleString = nativeobject == ECMAScriptObjects.ARRAY_TOLOCALESTRING;
                Set<ObjectLabel> objlabels = state.readThisObjects();
                Value sepArg = is_toLocaleString? Value.makeStr(","): FunctionCalls.readParameter(call, state, 0);
                AbstractNode node = call.getJSSourceNode();
                return evaluateJoinOrToLocaleString(node, objlabels, sepArg, is_toLocaleString && !c.getAnalysis().getUnsoundness().mayAssumeFixedLocale(call.getSourceNode()), c);
            }

            case ARRAY_CONCAT: { // 15.4.4.4
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
                            element = FunctionCalls.readParameter(call, state, i).summarize(new Summarized(resultLabel));
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
                            expectedLengthArrayLength = arrayLength; // The arrays need to be of same length to use precise unfoldings
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
                    unfoldedElements.add(call.getUnknownArg().summarize(new Summarized(resultLabel)));
                    isPreciseUnfolding = false;
                }

                if (isPreciseUnfolding) {
                    for (int i = 0; i < unfoldedElements.size(); i++) {
                        Value unfoldedElement = unfoldedElements.get(i);
                        boolean maybeAbsent = unfoldedElement.isMaybeAbsent();
                        unfoldedElement = unfoldedElement.restrictToNotAbsent();
                        if (!unfoldedElement.isNone()) {
                            pv.writeProperty(resultLabelAsSet, Value.makeTemporaryStr(i + ""), unfoldedElement, maybeAbsent);
                        }
                    }
                    Value length = Value.makeNum(unfoldedElements.size());
                    writeLength(resultLabel, length, c);
                } else {
                    Value v = UnknownValueResolver.join(unfoldedElements,state);
                    pv.writeProperty(Collections.singleton(resultLabel), Value.makeAnyStrUInt(), v);

                    Value receiverLength = readLength(state.readThisObjects(), c);
                    if (receiverLength.isMaybeSingleNum()) {
                        // recover precision for the prefix of the result array if the receivers length is precise
                        for (int i = 0; i < receiverLength.getNum(); i++) {
                            Value propertyName = Value.makeStr(i + "");
                            pv.writeProperty(Collections.singleton(resultLabel), propertyName, pv.readPropertyValue(state.readThisObjects(), propertyName));
                        }
                    }

                    writeLength(resultLabel, Value.makeAnyNumUInt(), c);
                }
                return resultArray;
            }

            case ARRAY_POP: { // 15.4.4.6
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
                    res = pv.readPropertyValue(thisobj, index);
                    c.getMonitoring().visitPropertyRead(call.getJSSourceNode(), state.readThisObjects(), Value.makeTemporaryStr(index), state, false);
                    pv.deleteProperty(thisobj, Value.makeStr(index), false);
                    new_len = Value.makeNum(length - 1);
                } else {
                    res = pv.readPropertyValue(thisobj, Value.makeAnyStrUInt());
                    c.getMonitoring().visitPropertyRead(call.getJSSourceNode(), state.readThisObjects(), Value.makeAnyStrUInt(), state, false);
                    pv.deleteProperty(thisobj, Value.makeAnyStrUInt(), false);
                    new_len = Value.makeAnyNumUInt();
                }
                writeLength(thisobj, new_len, c);
                return res;
            }

            case ARRAY_PUSH: { // 15.4.4.7
                Set<ObjectLabel> arr = state.readThisObjects();
                Value new_len;
                if (call.isUnknownNumberOfArgs()) {
                    new_len = Value.makeAnyNumUInt();
                    pv.writeProperty(arr, Value.makeAnyStrUInt(), call.getUnknownArg());
                } else {
                    Value old_len = UnknownValueResolver.getRealValue(readLength(arr, c), state);
                    if (call.getNumberOfArgs() == 0) {
                        new_len = old_len;
                    } else {
                        new_len = Value.makeAnyNumUInt();
                    }
                    if (arr.size() == 1 && arr.iterator().next().isSingleton()) {
                        Double length_prop = old_len.getNum();
                        long length = length_prop != null ? Conversion.toUInt32(length_prop) : -1;
                        int i;
                        for (i = 0; i < call.getNumberOfArgs(); i++) {
                            Value v = FunctionCalls.readParameter(call, state, i);
                            if (length > -1)
                                pv.writeProperty(arr, Value.makeTemporaryStr(String.valueOf(i + length)), v);
                            else {
                                pv.writeProperty(arr, Value.makeAnyStrUInt(), v);
                                break;
                            }
                        }
                        if (length > -1)
                            new_len = Value.makeNum(i + length);
                    } else {
                        for (int i = 0; i < call.getNumberOfArgs(); i++)
                            pv.writeProperty(arr, Value.makeAnyStrUInt(), FunctionCalls.readParameter(call, state, i));
                    }
                }
                writeLength(arr, new_len, c);
                return new_len;
            }

            case ARRAY_REVERSE: { // 15.4.4.8
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
                            pv.writePropertyWithAttributes(thisobj, StringPKey.make(s2), near_start);
                        if (near_end.isNotPresent())
                            pv.deleteProperty(thisobj, Value.makeStr(s1), false);
                        else if (near_end.isMaybePresent())
                            pv.writePropertyWithAttributes(thisobj, StringPKey.make(s1), near_end);
                    }
                } else {
                    Value v = pv.readPropertyWithAttributes(thisobj, Value.makeAnyStrUInt());
                    if (v.isMaybePresent())
                        pv.writeProperty(thisobj, Value.makeAnyStrUInt(), v);
                }
                return Value.makeObject(thisobj);
            }

            case ARRAY_SHIFT: {
                // perform the operation on a per-array basis to avoid exchange of values among arrays
                Set<ObjectLabel> thisObjects = newSet(state.readThisObjects());
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
                            Bool is_def = pv.hasProperty(thisObj, Value.makeTemporaryStr(s));
                            if (is_def.isMaybeTrue()) {
                                Value elem = pv.readPropertyWithAttributes(thisObj, s);
                                pv.writeProperty(thisObj, Value.makeTemporaryStr(Integer.toString(i - 1)), elem, moreThanOneArray);
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
                        Value defaultArrayProperty = UnknownValueResolver.getDefaultNumericProperty(current, state);
                        if (!defaultArrayProperty.restrictToNotAbsent().isNone()) {
                            pv.writeProperty(thisObj, Value.makeAnyStrUInt(), defaultArrayProperty, moreThanOneArray);
                        }
                        pv.deleteProperty(thisObj, Value.makeAnyStrUInt(), moreThanOneArray);
                    }
                    writeLength(thisObj, new_length, c);
                }
                return firstElement;
            }

            case ARRAY_SLICE: {
                ObjectLabel resultArray = makeArray(call.getSourceNode(), Value.makeNum(0), state.readThis(), c);

                // resolve the fromIndex, a value of null means that it is not coercible to a single, precise number
                Value fromIndexValue = FunctionCalls.readParameter(call, state, 0).summarize(new Summarized(resultArray));
                if (fromIndexValue.isMaybeUndef()) {
                    fromIndexValue = fromIndexValue.restrictToNotUndef().joinNum(0);
                }
                Double fromIndexNum = Conversion.toNumber(fromIndexValue, c).getNum();
                Long fromIndex = fromIndexNum != null ? Conversion.toUInt32(fromIndexNum) : null;

                // resolve the toIndex, a value of null means that it is not coercible to a single, precise number
                Value toIndexValue = FunctionCalls.readParameter(call, state, 1).summarize(new Summarized(resultArray));
                Long toIndex;
                if (toIndexValue.isMaybeUndef() && !toIndexValue.isMaybeOtherThanUndef()) {
                    toIndex =  Math.round(Math.pow(2, 32)-1); // default value: array length, resolved later
                } else {
                    Double toIndexNum = Conversion.toNumber(toIndexValue, c).getNum();
                    toIndex = toIndexNum != null ? (long) Conversion.toInt32(toIndexNum) : null; //FIXME: Add to conversion a conversion from double to signed integers (long) (github #468)
                }

                ParallelTransfer.process(state.readThisObjects(), thisObject -> {
                    Value thisLength = readLength(thisObject, c);
                    if (thisLength.isMaybeSingleNum() && fromIndex != null && toIndex != null && toIndex >= 0) {
                        // precise case: copy individual properties
                        long resolvedToIndex = Math.min(Conversion.toUInt32(thisLength.getNum()), toIndex);
                        long resolvedLength = Math.max(0, resolvedToIndex - fromIndex);
                        for (long i = 0; i < resolvedLength; i++) {
                            Value read;
                            read = pv.readPropertyValue(singleton(thisObject), i + fromIndex + "");
                            pv.writeProperty(resultArray, i + "", read);
                        }
                        writeLength(resultArray, Value.makeNum(resolvedLength), c);
                    } else {
                        // imprecise case: merge all properties (could be special cased for a little extra precision (e.g. known length of output, precise prefix of output...)
                        Value read = pv.readPropertyValue(singleton(thisObject), Value.makeAnyStrUInt());
                        if (read.isNone()) {
                            c.getState().setToBottom();
                        } else {
                            pv.writeProperty(singleton(resultArray), Value.makeAnyStrUInt(), read);
                            writeLength(resultArray, Value.makeAnyNumUInt(), c);
                        }
                    }
                }, c);
                return Value.makeObject(resultArray);
            }

            case ARRAY_SORT: {
                Value comparefn = FunctionCalls.readParameter(call, state, 0);
                Set<ObjectLabel> thisobj = state.readThisObjects();
                Value length = UnknownValueResolver.getRealValue(pv.readPropertyValue(thisobj, "length"), state);
                if (length.isMaybeSingleNum() && (length.getNum() == 0 || length.getNum() == 1)) {
                    return Value.makeObject(thisobj); // fast case: nothing to sort
                }

                // Minor unsoundness: we ignore the cases where the comparison function mutate the array directly

                for (int i = 0; i < 2; i++) { // 2 enough, we just need the feedback loop
                    if (!comparefn.isNone()) {
                        if (Conversion.isMaybeNonCallable(comparefn.restrictToNotUndef())) {
                            Exceptions.throwTypeError(c);
                            c.getMonitoring().addMessage(call.getSourceNode(), Severity.HIGH,
                                    "TypeError, invalid argument to Array.prototype.sort");
                        }
                        if (!comparefn.isMaybeUndef() && comparefn.getObjectLabels().stream().noneMatch(l -> l.getKind() == Kind.FUNCTION)) {
                            return Value.makeNone(); // definitely invalid comparefn;
                        }
                        shuffleArrayWeakly(thisobj, c);
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
                                    c.getAnalysis().getUnsoundness().addMessage(c.getNode(), "Ignoring host object comparefn in Array.prototype.sort");
                                } else {
                                    implicitAfterCall = UserFunctionCalls.implicitUserFunctionCall(obj, new FunctionCalls.DefaultImplicitCallInfo(c) {
                                        @Override
                                        public Value getArg(int i) {
                                            return pv.readPropertyValue(thisobj, 0 + "" /* the shuffle merged all property values here (and other places) */);
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
                    if (comparefn.isNone()) {
                        break;
                    }
                }
                return Value.makeObject(thisobj);
            }

            case ARRAY_SPLICE: {
                // TODO: improve precision?
                // construct return value
                ObjectLabel resultArray = makeArray(call.getSourceNode(), c);
                Value arrayValues = pv.readPropertyValue(state.readThisObjects(), Value.makeAnyStrUInt());
                if (arrayValues.isNone()) {
                    return Value.makeNone();
                }
                pv.writeProperty(Collections.singleton(resultArray), Value.makeAnyStrUInt(), arrayValues);
                writeLength(resultArray, Value.makeAnyNumUInt(), c);

                // mutate the input
                Set<ObjectLabel> thisObjects = state.readThisObjects();
                readLength(thisObjects, c); // force read-side-effects
                Value parameters = Value.makeNone();
                if (call.isUnknownNumberOfArgs())
                    parameters = UnknownValueResolver.join(parameters, call.getUnknownArg().summarize(new Summarized(resultArray)), state);
                else
                    for (int i = 2; i < call.getNumberOfArgs(); i++)
                        parameters = UnknownValueResolver.join(parameters, FunctionCalls.readParameter(call, state, i).summarize(new Summarized(resultArray)), state);
                pv.deleteProperty(thisObjects, Value.makeAnyStrUInt(), true);
                pv.writeProperty(thisObjects, Value.makeAnyStrUInt(), parameters.join(arrayValues).removeAttributes(), true);
                writeLength(thisObjects, Value.makeAnyNumUInt(), c);

                return Value.makeObject(resultArray);
            }

            case ARRAY_UNSHIFT: { // 15.4.4.13
                // TODO merge implementation with ARRAY_SHIFT?
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
                            Bool is_def = pv.hasProperty(thisObj, Value.makeTemporaryStr(s));
                            if (is_def.isMaybeTrue()) {
                                Value elem = pv.readPropertyWithAttributes(thisObj, s);
                                pv.writeProperty(thisObj, Value.makeTemporaryStr(Long.toString(i + 1)), elem, moreThanOneArray);
                            } else
                                pv.deleteProperty(thisObj, Value.makeTemporaryStr(Long.toString(i + 1)), moreThanOneArray);
                        }
                        new_length = Value.makeNum(length + 1);
                    } else {
                        // imprecise case: length is not known --> mix all array properties
                        new_length = Value.makeAnyNumUInt();
                        pv.writeProperty(thisObj, Value.makeAnyStrUInt(), pv.readPropertyValue(thisObj, Value.makeAnyStrUInt()).removeAttributes(), moreThanOneArray);
                        pv.deleteProperty(thisObj, Value.makeAnyStrUInt(), moreThanOneArray);
                    }
                    writeLength(thisObj, new_length, c);
                    sharedNewLength = sharedNewLength.join(new_length);
                }
                pv.writeProperty(thisObjects, Value.makeTemporaryStr("0"), FunctionCalls.readParameter(call, state, 0).removeAttributes(), moreThanOneArray);
                return sharedNewLength;
            }

            case ARRAY_INDEXOF: { // 15.4.4.14
                c.getMonitoring().visitPropertyRead(call.getJSSourceNode(), state.readThisObjects(), Value.makeAnyStrUInt(), state, false);
            /* Value searchElement =*/
                FunctionCalls.readParameter(call, state, 0);
                Value fromIndex = call.getNumberOfArgs() > 1 ? Conversion.toInteger(FunctionCalls.readParameter(call, state, 1), c) : Value.makeNum(0); // TODO: sometimes certain?
                Double fromindex_num = UnknownValueResolver.getRealValue(fromIndex, state).getNum();
                int n = fromindex_num == null ? -1 : fromindex_num.intValue();
                Set<ObjectLabel> thisobj = state.readThisObjects();
                Value length_val = readLength(thisobj, c);
                Double length_prop_num = UnknownValueResolver.getRealValue(length_val, state).getNum();
                long length = length_prop_num != null ? Conversion.toUInt32(length_prop_num) : -1;
                if (length == 0 || (n > length && length > 0)) // 15.4.4.14 item 4 and item 6
                    return Value.makeNum(-1);
                return Value.makeAnyNumNotNaNInf();
            }

            default:
                return null;
        }
    }

    private static void shuffleArrayWeakly(Set<ObjectLabel> array, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        Value length = UnknownValueResolver.getRealValue(pv.readPropertyValue(array, "length"), c.getState());
        if (length.isMaybeSingleNum()) {
            Set<Value> propertyValues = newSet();
            for (int i = 0; i < length.getNum(); i++) {
                propertyValues.add(pv.readPropertyValue(array, i + "")); // minor unsoundness: the reads are not weak, getters are definitely invoked
                c.getMonitoring().visitPropertyRead(c.getNode(), array, Value.makeStr(i + ""), c.getState(), false);
            }
            Value anyPropertyValue = UnknownValueResolver.join(propertyValues, c.getState());
            for (int i = 0; i < length.getNum(); i++) {
                pv.writeProperty(array, Value.makeStr(i + ""), anyPropertyValue, true);
                c.getMonitoring().visitPropertyWrite((Node) c.getNode(), array, Value.makeStr(i + ""));
            }
        } else {
            Value anyPropertyValue = pv.readPropertyValue(array, Value.makeAnyStrUInt());
            pv.writeProperty(array, Value.makeAnyStrUInt(), anyPropertyValue);
            c.getMonitoring().visitPropertyRead(c.getNode(), array, Value.makeAnyStrUInt(), c.getState(), false);
            c.getMonitoring().visitPropertyWrite((Node) c.getNode(), array, Value.makeAnyStrUInt());
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

    private static Value readLength(ObjectLabel thisobj, Solver.SolverInterface c) {
        return readLength(singleton(thisobj), c);
    }

    private static void writeLength(ObjectLabel thisObj, Value length, Solver.SolverInterface c) {
        writeLength(singleton(thisObj), length, c);
    }

    private static void writeLength(Set<ObjectLabel> thisObj, Value length, Solver.SolverInterface c) {
        if(length.isMaybeSingleNum() && length.getNum() < 0){
            throw new AnalysisException("Trying to write negative array length (might be a real JavaScript error, but more like a TAJS-programmer error)!");
        }
        c.getAnalysis().getPropVarOperations().writeProperty(thisObj, Value.makeTemporaryStr("length"), length);
    }

    public static ObjectLabel makeArray(AbstractNode allocationNode, Solver.SolverInterface c) {
        return makeArray(allocationNode, Value.makeNum(0), c);
    }

    public static ObjectLabel makeArray(AbstractNode allocationNode, Value length, Solver.SolverInterface c) {
        return makeArray(allocationNode, length, (Context)null, c);
    }

    /**
     * Makes an array where the heapContext of the thisObj is used as the heapContext of the resulting array.
     * This helps if two different arrays are created at the same callsite, but with 2 different this-objects.
     * This scenario happens if two different functions created using Function.prototype.bind are called form the same callsite.
     */
    public static ObjectLabel makeArray(AbstractNode allocationNode, Value length, Value thisObj, Solver.SolverInterface c) {
        if (thisObj != null && thisObj.isMaybeSingleObjectLabel()) {
            return makeArray(allocationNode, length, thisObj.getAllObjectLabels().iterator().next().getHeapContext(), c);
        }
        return makeArray(allocationNode, length, (Context)null, c);
    }

    public static ObjectLabel makeArray(AbstractNode allocationNode, Value length, Context heapContext, Solver.SolverInterface c) {
        ObjectLabel array = ObjectLabel.make(allocationNode, Kind.ARRAY, heapContext);
        c.getState().newObject(array);
        c.getState().writeInternalPrototype(array, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));
        writeLength(array, length, c);
        return array;
    }

    public static void setEntries(ObjectLabel array, List<Value> content, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        for (int i = 0; i < content.size(); i++) {
            pv.writeProperty(array, Integer.toString(i), content.get(i));
        }
        writeLength(array, Value.makeNum(content.size()), c);
    }

    public static void setUnknownEntries(ObjectLabel array, Value content, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        pv.writeProperty(singleton(array), Value.makeAnyStrUInt(), content);
        writeLength(array, Value.makeAnyNumUInt(), c);
    }

    public static Value evaluateToString(Value thisval, Solver.SolverInterface c) {
        List<Value> strs = newList();
        for (ObjectLabel thisObj : Conversion.toObjectLabels(c.getNode(), thisval, c)) {
            // 15.4.4.2 Array.prototype.toString ( ) - defined in terms of this.join() or Object.prototype.toString
            Value join = c.getAnalysis().getPropVarOperations().readPropertyValue(singleton(thisObj), "join");
            c.getMonitoring().visitPropertyRead(c.getNode(), singleton(thisObj), Value.makeStr("join"), c.getState(), false);
            join = UnknownValueResolver.getRealValue(join, c.getState());
            boolean hasNonCallable = false;
            boolean hasArrayJoin = false;
            boolean hasOtherCallable = false;
            if (join.isMaybePrimitiveOrSymbol()) {
                hasNonCallable = true;
            }
            for (ObjectLabel joinObj : join.getObjectLabels()) {
                if (joinObj.isHostObject() && joinObj.getHostObject() == ECMAScriptObjects.ARRAY_JOIN) {
                    hasArrayJoin = true;
                } else if (joinObj.getKind() != Kind.FUNCTION) {
                    hasNonCallable = true;
                } else {
                    hasOtherCallable = true;
                }
            }
            if (hasArrayJoin) {
                // common case: take a fast path
                strs.add(evaluateJoinOrToLocaleString(c.getNode(), singleton(thisObj), Value.makeUndef(), false, c));
            }
            if (hasNonCallable) {
                strs.add(JSObject.evaluateToString(Value.makeObject(thisObj), c));
            }
            if (hasOtherCallable) {
                // FIXME: make the implicit calls (GitHub #353)
                throw new AnalysisLimitationException.AnalysisModelLimitationException(c.getNode().getSourceLocation() + ": Trying to call toString for Array with redefined join-property.");
            }
        }
        return Value.join(strs);
    }

    public static Value evaluateJoinOrToLocaleString(AbstractNode node, Set<ObjectLabel> objlabels, Value separatorValue, boolean is_toLocaleString, Solver.SolverInterface c) {
            State state = c.getState();
            PropVarOperations pv = c.getAnalysis().getPropVarOperations();
            c.getMonitoring().visitPropertyRead(node, objlabels, Value.makeAnyStrUInt(), state, false);
            Value length_val = readLength(objlabels, c);
            Double length_prop = UnknownValueResolver.getRealValue(length_val, state).getNum();
            if (length_prop == null) {
                return Value.makeAnyStr();
            }
            long length = Conversion.toUInt32(length_prop);
            if (length == 0)
                return Value.makeStr("");
            if (separatorValue.isMaybeUndef() && !is_toLocaleString) {
                separatorValue = separatorValue.restrictToNotUndef().joinStr(",");
            }
            Value v = Conversion.toString(separatorValue, c);
            String separator = v.isMaybeSingleStr() ? v.getStr() : null;
            if ((length != 1 && separator == null /* = sep is a fuzzy string */))
                return Value.makeAnyStr();

            List<String> strings = newList();
            for (int i = 0; i < length; i++) {
                Value prop = pv.readPropertyValue(objlabels, i + "");
                prop = UnknownValueResolver.getRealValue(prop, state);
                if(isMaybeCyclicJoin(prop)){
                    // NB: both v8 and firefox returns the empty string for the cyclic element
                    return Value.makeAnyStr();
                }
                boolean isMaybeNullUndef = prop.isMaybeUndef() || prop.isMaybeNull();
                boolean isMaybeNotNullUndef = !prop.restrictToNotNullNotUndef().isNone();
                final String string;
                if (isMaybeNullUndef && !isMaybeNotNullUndef) {
                    string = "";
                } else if (!isMaybeNullUndef && isMaybeNotNullUndef) {
                    if (is_toLocaleString) {
                        return Value.makeAnyStr(); // TODO make a call to toLocaleString and use that
                    }
                    try {
                        cyclicJoinGuard.push(objlabels);
                        Value v2 = Conversion.toString(prop.restrictToNotNullNotUndef(), c);
                        string = v2.isMaybeSingleStr() ? v2.getStr() : null;
                    } finally {
                        cyclicJoinGuard.pop();
                    }
                } else {
                    string = null;
                }
                if (string == null) {
                    return Value.makeAnyStr();
                }
                strings.add(string);
            }
            if (strings.size() == 1) { // separator might be null, if strings.size() == 1
                return Value.makeStr(strings.iterator().next());
            }
            return Value.makeStr(String.join(separator, strings));
    }

    private static boolean isMaybeCyclicJoin(Value prop) {
        return prop.getObjectLabels().stream().anyMatch(l -> cyclicJoinGuard.stream().anyMatch(gs -> gs.contains(l)));
    }
}