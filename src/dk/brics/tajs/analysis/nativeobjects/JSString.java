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
import dk.brics.tajs.analysis.nativeobjects.JSRegExp.RegExpExecHandler;
import dk.brics.tajs.analysis.nativeobjects.concrete.Alpha;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteArray;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteBoolean;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteNull;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteNumber;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteRegularExpression;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteString;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteUndefined;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteValue;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteValueVisitor;
import dk.brics.tajs.analysis.nativeobjects.concrete.InvocationResult;
import dk.brics.tajs.analysis.nativeobjects.concrete.NashornConcreteSemantics;
import dk.brics.tajs.analysis.nativeobjects.concrete.SingleGamma;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.util.AnalysisException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * 15.5 native String functions.
 */
public class JSString {

    private JSString() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, final CallInfo call, final Solver.SolverInterface c) {
        if (nativeobject != ECMAScriptObjects.STRING)
            if (NativeFunctions.throwTypeErrorIfConstructor(call, c))
                return Value.makeNone();

        State state = c.getState();
        switch (nativeobject) {

            case STRING: { // 15.5.1/2
                Value s;
                if (call.isUnknownNumberOfArgs())
                    s = Conversion.toString(NativeFunctions.readParameter(call, state, 0), c).joinStr("");
                else
                    s = call.getNumberOfArgs() >= 1 ? Conversion.toString(NativeFunctions.readParameter(call, state, 0), c) : Value.makeStr("");

                if (s.isNone()) {
                    // we might be waiting for implicit toString calls
                    return Value.makeNone();
                }

                if (call.isConstructorCall()) { // 15.5.2
                    ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.STRING);
                    state.newObject(objlabel);
                    state.writeInternalValue(objlabel, s);
                    state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.STRING_PROTOTYPE));
                    Value len = s.isMaybeSingleStr() ? Value.makeNum(s.getStr().length()) : Value.makeAnyNumUInt();
                    c.getAnalysis().getPropVarOperations().writePropertyWithAttributes(objlabel, "length", len.setAttributes(true, true, true));
                    return Value.makeObject(objlabel);
                } else // 15.5.1
                    return s;
            }

            case STRING_FROMCHARCODE: { // 15.5.3.2
                return TAJSConcreteSemantics.convertTAJSCall(Value.makeUndef(), "String.fromCharCode", -1, call, c, Value.makeAnyStr());
            }

            case STRING_TOSTRING: // 15.5.4.2
            case STRING_VALUEOF: { // 15.5.4.3
                return state.readThisObjectsCoerced((l) -> evaluateToString(l, c));
            }

            case STRING_CHARAT: {// 15.5.4.4
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.charAt", 1, call, c, Value.makeAnyStr());
            }
            case STRING_CHARCODEAT: { // 15.5.4.5
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.charCodeAt", 1, call, c, Value.makeAnyNumUInt().joinNumNaN());
            }

            case STRING_CONCAT: { // 15.5.4.6
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.concat", -1, call, c, Value.makeAnyStr());
            }

            case STRING_INDEXOF: {// 15.5.4.7
                Value receiver = Value.makeObject(state.readThisObjects());
                Value haystack = Conversion.toString(receiver, c);
                Value needle = Conversion.toString(NativeFunctions.readParameter(call, state, 0), c);
                if (!haystack.isStrMayContainSubstring(needle)) {
                    return Value.makeNum(-1);
                }
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.indexOf", 2, call, c, Value.makeAnyNumNotNaNInf());
            }

            case STRING_LASTINDEXOF: { // 15.5.4.8
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.lastIndexOf", 2, call, c, Value.makeAnyNumNotNaNInf());
            }

            case STRING_LOCALECOMPARE: { // 15.5.4.9
                Value defaultValue = Value.makeAnyNumNotNaNInf();
                if (Options.get().isUnsoundEnabled()) {
                    return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.localeCompare", 1, call, c, defaultValue);
                }
                return defaultValue;
            }

            case STRING_MATCH: { // 15.5.4.10 (see REGEXP_EXEC)
                return RegExpExecHandler.handle(TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.match", 1, call, c), call, c);
            }

            case STRING_REPLACE: { // 15.5.4.11
                // TODO: support regex-version of String.replace

                final Value toReplaceWith;
                final Value toReplace;
                if (call.isUnknownNumberOfArgs()) {
                    toReplace = NativeFunctions.readParameter(call, state, 0);
                    toReplaceWith = call.getUnknownArg().joinUndef();
                } else {
                    toReplace = NativeFunctions.readParameter(call, state, 0);
                    toReplaceWith = NativeFunctions.readParameter(call, state, 1);
                }

                if (SingleGamma.isConcreteValue(state.readThis(), c) && SingleGamma.isConcreteValue(toReplace, c)) {
                    // sound "optimization": if a function is given as second argument, then it is only a problem if it could be invoked..
                    InvocationResult<ConcreteNumber> concreteResult = NashornConcreteSemantics.get().apply("String.prototype.search", SingleGamma.toConcreteValue(state.readThis(), c), Collections.singletonList(escapeAnyStringForRegExp(SingleGamma.toConcreteValue(toReplace, c))));
                    if (concreteResult.kind != InvocationResult.Kind.VALUE) {
                        handleFunctionCallbacks(toReplaceWith, call, c);
                        return Value.makeAnyStr();
                    }

                    boolean hasAnyMatches = (int) (concreteResult.getValue()).getNumber() != -1;
                    if (hasAnyMatches) {
                        boolean anyCallbacks = handleFunctionCallbacks(toReplaceWith, call, c);
                        if (anyCallbacks) {
                            return Value.makeAnyStr();
                        }
                    }
                    return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.replace", hasAnyMatches ? 2 : 1, call, c, Value.makeAnyStr());
                }

                invokeCallback(toReplaceWith, c);
                return Value.makeAnyStr();
            }

            case STRING_SEARCH: { // 15.5.4.12
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.search", 1, call, c, Value.makeAnyNumNotNaNInf());
            }

            case STRING_SLICE: {  // 15.5.4.13
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.slice", 2, call, c, Value.makeAnyStr());
            }
            case STRING_SUBSTRING: {  // 15.5.4.15
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.substring", 2, call, c, Value.makeAnyStr());
            }
            case STRING_SUBSTR: { // B.2.3
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.substr", 2, call, c, Value.makeAnyStr());
            }

            case STRING_SPLIT: { // 15.5.4.14
                return splitString(nativeobject, call, c);
            }

            case STRING_TOLOWERCASE: { // 15.5.4.16
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.toLowerCase", 0, call, c, Value.makeAnyStr());
            }
            case STRING_TOUPPERCASE: { // 15.5.4.18
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.toUpperCase", 0, call, c, Value.makeAnyStr());
            }

            case STRING_TOLOCALELOWERCASE: { // 15.5.4.17
                Value defaultValue = Value.makeAnyStr();
                if (Options.get().isUnsoundEnabled()) {
                    return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.toLocaleLowerCase", 0, call, c, defaultValue);
                }
                return defaultValue;
            }
            case STRING_TOLOCALEUPPERCASE: { // 15.5.4.19
                Value defaultValue = Value.makeAnyStr();
                if (Options.get().isUnsoundEnabled()) {
                    return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.toLocaleUpperCase", 0, call, c, defaultValue);
                }
                return defaultValue;
            }

            case STRING_TRIM: { // 15.5.4.20
                Value s = Conversion.toString(Value.makeObject(state.readThisObjects()), c);
                if (s.isMaybeSingleStr()) {
                    return Value.makeStr(s.getStr().trim());
                } else if (s.isMaybeStrUInt()
                        || s.isMaybeStrOtherNum()
                        || s.isMaybeStrJSON()
                        || s.isMaybeStrIdentifier()
                        || s.isMaybeStrIdentifierParts()) {
                    return s;
                } else if (s.isMaybeStrPrefixedIdentifierParts()) {
                    return Value.makeNone().joinPrefixedIdentifierParts(s.getPrefix().trim());
                } else {
                    return Value.makeAnyStr();
                }
            }

            default:
                return null;
        }
    }

    private static void invokeCallback(Value callback, Solver.SolverInterface c) {
        for (int i = 0; i < 2; i++) { // 2 enough, we just need the feedback loop
            List<Value> result = newList();
            BasicBlock implicitAfterCall = null;
            for (ObjectLabel obj : callback.getObjectLabels()) {
                if (obj.getKind() == Kind.FUNCTION) {
                    if (obj.isHostObject()) { // weird, but possible
                        // TODO: callback is a host object, should invoke it (but unlikely worthwhile to implement...)
                        c.getMonitoring().addMessage(c.getNode(), Message.Severity.HIGH, "Ignoring host object callback in String.prototype.replace");
                    } else {
                        implicitAfterCall = UserFunctionCalls.implicitUserFunctionCall(obj, new FunctionCalls.DefaultImplicitCallInfo(c) {
                            @Override
                            public Value getArg(int i1) {
                                return getUnknownArg();
                            }

                            @Override
                            public int getNumberOfArgs() {
                                throw new AnalysisException("Number of arguments is unknown!");
                            }

                            @Override
                            public Value getUnknownArg() {
                                return Value.makeAnyStr().joinAnyNumUInt().joinUndef();
                            }

                            @Override
                            public boolean isUnknownNumberOfArgs() {
                                return true;
                            }
                        }, c);
                    }
                }
                UserFunctionCalls.implicitUserFunctionReturn(result, true, implicitAfterCall, c);
            }
        }
    }

    /**
     * Escapes characters in a string such that it can be used as a regular expression for that exact string. Non-strings are not effected.
     */
    private static ConcreteValue escapeAnyStringForRegExp(ConcreteValue concreteValue) {
        return concreteValue.accept(new ConcreteValueVisitor<ConcreteValue>() {
            @Override
            public ConcreteValue visit(ConcreteNumber v) {
                return v;
            }

            @Override
            public ConcreteValue visit(ConcreteString v) {
                // http://stackoverflow.com/questions/3446170/escape-string-for-use-in-javascript-regex
                /*
                function escapeRegExp(str) {
                    return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
                }
                */
                // NB more escapings for using the regular expression as source code in concrete semantics is done in ConcreteString
                ConcreteRegularExpression toReplace = new ConcreteRegularExpression(new ConcreteString("[\\-\\[\\]\\/\\{\\}\\(\\)\\*\\+\\?\\.\\\\\\^\\$\\|]"), new ConcreteBoolean(true), new ConcreteBoolean(false), new ConcreteBoolean(false));
                ConcreteString toReplaceWith = new ConcreteString("\\$&");
                InvocationResult<ConcreteValue> concreteResult = NashornConcreteSemantics.get().apply("String.prototype.replace", v, Arrays.asList(toReplace, toReplaceWith));
                if (concreteResult.kind != InvocationResult.Kind.VALUE) {
                    new AnalysisException("Unable to escape string to RegExp?!?");
                }
                return concreteResult.getValue();
            }

            @Override
            public ConcreteValue visit(ConcreteArray v) {
                return v;
            }

            @Override
            public ConcreteValue visit(ConcreteUndefined v) {
                return v;
            }

            @Override
            public ConcreteValue visit(ConcreteRegularExpression v) {
                return v;
            }

            @Override
            public ConcreteValue visit(ConcreteNull v) {
                return v;
            }

            @Override
            public ConcreteValue visit(ConcreteBoolean v) {
                return v;
            }
        });
    }

    private static Value splitString(ECMAScriptObjects nativeobject, final CallInfo call, final Solver.SolverInterface c) {
        State state = c.getState();
        InvocationResult<ConcreteArray> concreteResult = TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.split", 2, call, c);
        switch (concreteResult.kind) {
            case VALUE:
                return Alpha.createNewArrayValue(concreteResult.getValue(), call.getSourceNode(), c);
            case BOTTOM:
                return Value.makeNone();
            case EXCEPTION:
                Exceptions.throwTypeError(c);  // assuming type-errors are the right thing to throw
                return Value.makeNone();
            case NON_CONCRETE:
                break;
            default:
                throw new AnalysisException("Unhandled switch case: " + concreteResult.kind);
        }
        Value separator = NativeFunctions.readParameter(call, state, 0);
        Value origLimit = NativeFunctions.readParameter(call, state, 1);
        boolean isUnlimited = !origLimit.isNotUndef();
        Value limit = isUnlimited ? Value.makeNum(Math.pow(2, 32) - 1) /* limit by spec! */ : Conversion.toNumber(origLimit, c);

        ObjectLabel resultArray = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);

        state.newObject(resultArray);
        state.writeInternalPrototype(resultArray, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));
        final Value thisStringValue = Conversion.toString(state.readThis(), c);

        if (thisStringValue.isNone()) {
            // we might be waiting for implicit toString calls
            return Value.makeNone();
        }

        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        if (limit.equals(Value.makeNum(0))) {
            // CASE: a definite limit of zero
            pv.writePropertyWithAttributes(resultArray, "length", limit.setAttributes(true, true, false));
            return Value.makeObject(resultArray);
        }

        if (separator.isMaybeUndef()) {
            // CASE: undefined separator
            pv.writeProperty(Collections.singleton(resultArray), Value.makeStr("0"), thisStringValue);
            pv.writePropertyWithAttributes(resultArray, "length", Value.makeNum(1).setAttributes(true, true, false));
            return Value.makeObject(resultArray);
        }

        if (separator.isMaybeSingleStr() && thisStringValue.isMaybeSingleStr() && separator.equals(Value.makeStr(separator.getStr())) && thisStringValue.equals(Value.makeStr(thisStringValue.getStr()))) {
            // precise case: both this and input are definite single strings
            final Map<String, Value> argsMap = newMap();
            argsMap.put("<base/this>", Value.makeStr(thisStringValue.getStr()));
            argsMap.put("<arg/separator>", Value.makeStr(separator.getStr()));
            // we are precise, so allocate a unique array
            resultArray = new ObjectLabel(call.getSourceNode(), Kind.ARRAY, HeapContext.make(null, argsMap));
            state.newObject(resultArray);
            state.writeInternalPrototype(resultArray, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));
            final List<Value> splitValues = new ArrayList<>();
            final String separatorString = separator.getStr();
            final String thisString = thisStringValue.getStr();
            Value resultArrayLength;
            if ((limit.isMaybeSingleNum() && limit.equals(Value.makeNum(limit.getNum()))) || isUnlimited) {
                // CASE: knows both string values and the limit

                // strategy write the exact values and the exact length
                String[] splitString = thisString.split(separatorString);
                int limitNum = limit.getNum().intValue();
                int matches = 0;
                while (matches < limitNum && splitString.length > matches) {
                    splitValues.add(Value.makeStr(splitString[matches]));
                    matches++;
                }
                resultArrayLength = Value.makeNum(splitValues.size());
            } else {
                // CASE: knows both string values, but does not know the limit

                // strategy: maybe write the values, set an unknown length, join values with undefined
                String[] splitString = thisString.split(separatorString);
                for (String s : splitString) {
                    splitValues.add(Value.makeStr(s).joinUndef());
                }
                resultArrayLength = Value.makeAnyNumUInt();
            }
            pv.writePropertyWithAttributes(resultArray, "length", resultArrayLength.setAttributes(true, true, false));
            Set<ObjectLabel> toWrite = Collections.singleton(resultArray);
            for (int i = 0; i < splitValues.size(); i++) {
                final Value index = Value.makeStr(String.valueOf(i));
                final Value value = splitValues.get(i);
                pv.writeProperty(toWrite, index, value);
            }
            return Value.makeObject(resultArray);
        } else {
            // CASE imprecise string information
            pv.writeProperty(Collections.singleton(resultArray), Value.makeAnyStrUInt(), Value.makeAnyStr(), true);
            pv.writePropertyWithAttributes(resultArray, "length", Value.makeAnyNumUInt().setAttributes(true, true, false));
            return Value.makeObject(resultArray);
        }
    }

    private static boolean handleFunctionCallbacks(Value toReplaceWith, CallInfo call, Solver.SolverInterface c) {
        boolean anyCallbacks = false;
        if (toReplaceWith.isMaybeObject()) {
            for (ObjectLabel objectLabel : toReplaceWith.getObjectLabels()) {
                if (objectLabel.getKind() == Kind.FUNCTION) {
                    if (Options.get().isUnsoundEnabled()) {
                        c.getMonitoring().addMessage(call.getSourceNode(), Message.Severity.HIGH, "Ignoring String.replace(..., function(){...})");
                    } else {
                        anyCallbacks = true;
                    }
                }
            }
        }
        if (anyCallbacks) {
            invokeCallback(toReplaceWith, c);
        }
        return anyCallbacks;
    }

    public static Value evaluateToString(ObjectLabel thiss, Solver.SolverInterface c) {
        // 15.5.4.2 String.prototype.toString ( )
        // Returns this string value. (Note that, for a String object, the toString method happens to return the same thing as
        // the valueOf method.)
        if (thiss.getKind() != Kind.STRING) {
            Exceptions.throwTypeError(c);
            return Value.makeNone();
        }
        return c.getState().readInternalValue(singleton(thiss));
    }

    public static Set<Value> getEnumerableStringPropertyNames(Value v) {
        Set<Value> propertyNames = newSet();
        if (v.isMaybeSingleStr()) {
            for (int i = 0; i < v.getStr().length(); i++) {
                propertyNames.add(Value.makeStr(Integer.toString(i)));
            }
        } else if (v.isMaybeFuzzyStr()) {
            propertyNames.add(Value.makeAnyStrUInt());
        }
        return propertyNames;
    }
}
