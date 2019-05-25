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
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.js.UserFunctionCalls;
import dk.brics.tajs.analysis.nativeobjects.concrete.Alpha;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteBoolean;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteNumber;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteRegularExpression;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteString;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteValue;
import dk.brics.tajs.analysis.nativeobjects.concrete.MappedNativeResult;
import dk.brics.tajs.analysis.nativeobjects.concrete.NativeResult;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.util.AnalysisException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TRIM;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TRIMLEFT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TRIMRIGHT;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
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
        State state = c.getState();
        switch (nativeobject) {

            case STRING: { // 15.5.1/2
                Value s;
                Value paramValue = FunctionCalls.readParameter(call, state, 0);
                Set<ObjectLabel> symbols = paramValue.getSymbols();
                paramValue = paramValue.restrictToNotSymbol();
                if (call.isUnknownNumberOfArgs())
                    s = Conversion.toString(paramValue, c).joinStr("");
                else
                    s = call.getNumberOfArgs() >= 1 ? Conversion.toString(paramValue, c) : Value.makeStr("");

                if (!symbols.isEmpty()) { // TODO: could improve precision here if we didn't abstract away the symbol names (see JSSymbol.SYMBOL) - github #513
                    s = s.joinAnyStr();
                }

                if (s.isNone()) {
                    // we might be waiting for implicit toString calls
                    return Value.makeNone();
                }

                if (call.isConstructorCall()) { // 15.5.2
                    return Conversion.toObject(call.getSourceNode(), s, false, c);
                } else // 15.5.1
                    return s;
            }

            case STRING_FROMCHARCODE: { // 15.5.3.2
                return TAJSConcreteSemantics.convertTAJSCall(Value.makeUndef(), "String.fromCharCode", -1, call, c, Value::makeAnyStr);
            }

            case STRING_TOSTRING: // 15.5.4.2
            case STRING_VALUEOF: { // 15.5.4.3
                return evaluateToString(state.readThis(), c);
            }

            case STRING_CHARAT: {// 15.5.4.4
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.charAt", 1, call, c, Value::makeAnyStr);
            }
            case STRING_CHARCODEAT: { // 15.5.4.5
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.charCodeAt", 1, call, c, () -> Value.makeAnyNumUInt().joinNumNaN());
            }

            case STRING_CONCAT: { // 15.5.4.6
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.concat", -1, call, c, Value::makeAnyStr);
            }

            case STRING_INDEXOF: {// 15.5.4.7
                Value receiver = state.readThis();
                Value haystack = Conversion.toString(receiver, c);
                Value needle = Conversion.toString(FunctionCalls.readParameter(call, state, 0), c);
                if (!haystack.isStrMayContainSubstring(needle)) {
                    return Value.makeNum(-1);
                }
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.indexOf", 2, call, c, Value::makeAnyNumNotNaNInf);
            }

            case STRING_LASTINDEXOF: { // 15.5.4.8
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.lastIndexOf", 2, call, c, Value::makeAnyNumNotNaNInf);
            }

            case STRING_LOCALECOMPARE: { // 15.5.4.9
                Value defaultValue = Value.makeAnyNumNotNaNInf();
                if (c.getAnalysis().getUnsoundness().mayAssumeFixedLocale(call.getSourceNode())) {
                    return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.localeCompare", 1, call, c, () -> defaultValue);
                }
                return defaultValue;
            }

            case STRING_MATCH: { // 15.5.4.10 (see REGEXP_EXEC)
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.match", 1, call, c, () -> JSRegExp.handleUnknownRegexMatchResult(call.getSourceNode(), FunctionCalls.readParameter(call, c.getState(), 0), c));
            }

            case STRING_SEARCH: { // 15.5.4.12
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.search", 1, call, c, Value::makeAnyNumNotNaNInf);
            }

            case STRING_SLICE: {  // 15.5.4.13
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.slice", 2, call, c, Value::makeAnyStr);
            }
            case STRING_SUBSTRING: {  // 15.5.4.15
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.substring", 2, call, c, Value::makeAnyStr);
            }
            case STRING_SUBSTR: { // B.2.3
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.substr", 2, call, c, Value::makeAnyStr);
            }

            case STRING_SPLIT: { // 15.5.4.14
                return splitString(nativeobject, call, c);
            }

            case STRING_TOLOWERCASE: { // 15.5.4.16
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.toLowerCase", 0, call, c, Value::makeAnyStr);
            }

            case STRING_TOUPPERCASE: { // 15.5.4.18
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.toUpperCase", 0, call, c, Value::makeAnyStr);
            }

            case STRING_TOLOCALELOWERCASE: { // 15.5.4.17
                Value defaultValue = Value.makeAnyStr();
                if (c.getAnalysis().getUnsoundness().mayAssumeFixedLocale(call.getSourceNode())) {
                    return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.toLocaleLowerCase", 0, call, c, () -> defaultValue);
                }
                return defaultValue;
            }
            case STRING_TOLOCALEUPPERCASE: { // 15.5.4.19
                Value defaultValue = Value.makeAnyStr();
                if (c.getAnalysis().getUnsoundness().mayAssumeFixedLocale(call.getSourceNode())) {
                    return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.toLocaleUpperCase", 0, call, c, () -> defaultValue);
                }
                return defaultValue;
            }

            case STRING_TRIM: // 15.5.4.20
            case STRING_TRIMLEFT:
            case STRING_TRIMRIGHT: {
                Value thisString = Conversion.toString(state.readThis(), c);
                if (thisString.isMaybeAnyStr()) {
                    return Value.makeAnyStr();
                }
                Value trimmedString;
                if (thisString.isMaybeSingleStr()) {
                    String m = null;
                    if (nativeobject == STRING_TRIM)
                        m = "String.prototype.trim";
                    else if (nativeobject == STRING_TRIMRIGHT)
                        m = "String.prototype.trimRight";
                    else if (nativeobject == STRING_TRIMLEFT)
                        m = "String.prototype.trimLeft";
                    return TAJSConcreteSemantics.convertTAJSCall(thisString, m, 0, call, c, Value::makeAnyStr);
                } else if (thisString.isMaybeStrUInt()
                        || thisString.isMaybeStrOtherNum()
                        || thisString.isMaybeStrJSON()
                        || thisString.isMaybeStrIdentifier()
                        || thisString.isMaybeStrOtherIdentifierParts()) {
                    trimmedString = thisString;
                } else if (thisString.isMaybeStrPrefix() && (nativeobject == STRING_TRIM || nativeobject == STRING_TRIMLEFT)) {
                    Pattern LTRIM = Pattern.compile("^[\\s\\uFEFF\\xA0]+");
                    String trimmedPrefixString = thisString.getPrefix().replaceAll(LTRIM.toString(), "");
                    trimmedString = !trimmedPrefixString.isEmpty() ? Value.makeNone().joinPrefix(trimmedPrefixString) : Value.makeAnyStr();
                } else {
                    trimmedString = Value.makeAnyStr();
                }
                return trimmedString;
            }

            case STRING_STARTSWITH: {
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.startsWith", 2, call, c, Value::makeAnyBool);
            }

            case STRING_ENDSWITH: {
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.endsWith", 2, call, c, Value::makeAnyBool);
            }

            case STRING_FROMCODEPOINT: {
                return TAJSConcreteSemantics.convertTAJSCall(Value.makeUndef(), "String.fromCodePoint", -1, call, c, Value::makeAnyStr);
            }

            case STRING_INCLUDES: {
                return Value.makeAnyBool();
            }

            case STRING_CODEPOINTAT: {
                return Value.makeAnyNumUInt().joinUndef();
            }

            default:
                return null;
        }
    }

    // TODO unused, remove?
    private static void invokeCallback(Value callback, Solver.SolverInterface c, boolean withFeedbackLoop) {
        int iterations = withFeedbackLoop ? 2 /* 2 enough, we just need the feedback loop */ : 1;
        for (int i = 0; i < iterations; i++) {
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
     * Escapes characters in a string such that it can be used as a regular expression for that exact string.
     */
    // TODO unused (really???), remove?
    private static String escapeRegExp(String v, Solver.SolverInterface c) {
        // http://stackoverflow.com/questions/3446170/escape-string-for-use-in-javascript-regex
                /*
                function escapeRegExp(str) {
                    return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
                }
                */
        // NB more escapings for using the regular expression as source code in concrete semantics is done in ConcreteString
        ConcreteRegularExpression toReplace = new ConcreteRegularExpression(new ConcreteString("[\\-\\[\\]\\/\\{\\}\\(\\)\\*\\+\\?\\.\\\\\\^\\$\\|]"), new ConcreteBoolean(true), new ConcreteBoolean(false), new ConcreteBoolean(false), new ConcreteNumber(0.0));
        ConcreteString toReplaceWith = new ConcreteString("\\$&");
        MappedNativeResult<ConcreteValue> concreteResult = TAJSConcreteSemantics.getNative().apply("String.prototype.replace", new ConcreteString(v), Arrays.asList(toReplace, toReplaceWith));
        if (concreteResult.getResult().kind != NativeResult.Kind.VALUE) {
            throw new AnalysisException("Unable to escape string to RegExp?!?");
        }
        return Alpha.toValue(concreteResult.getResult().getValue(), c).getStr();
    }

    private static Context.Qualifier baseThisQualifier = new Context.Qualifier() {
        @Override
        public String toString() {
            return "<base/this>";
        }
    };

    private static Context.Qualifier argSeparatorQualifier = new Context.Qualifier() {
        @Override
        public String toString() {
            return "<arg/separator>";
        }
    };

    private static Value splitString(ECMAScriptObjects nativeobject, final CallInfo call, final Solver.SolverInterface c) {
        State state = c.getState();
        Value sentinel = Value.makeNull();
        Value concreteResult = TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.split", 2, call, c, () -> sentinel);
        if (!concreteResult.equals(sentinel)) { // alternative: wrap the remining statements of this method in the default-behavior callback
            return concreteResult;
        }
        Value separator = FunctionCalls.readParameter(call, state, 0);
        Value origLimit = FunctionCalls.readParameter(call, state, 1);
        boolean isUnlimited = !origLimit.isNotUndef();
        Value limit = isUnlimited ? Value.makeNum(Math.pow(2, 32) - 1) /* limit by spec! */ : Conversion.toNumber(origLimit, c);

        ObjectLabel resultArray = ObjectLabel.make(call.getSourceNode(), Kind.ARRAY);

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
            final Map<Context.Qualifier, Value> argsMap = newMap();
            argsMap.put(baseThisQualifier, Value.makeStr(thisStringValue.getStr()));
            argsMap.put(argSeparatorQualifier, Value.makeStr(separator.getStr()));
            // we are precise, so allocate a unique array
            resultArray = ObjectLabel.make(call.getSourceNode(), Kind.ARRAY, Context.makeQualifiers(argsMap));
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

    private static boolean handleFunctionCallbacks(Value toReplaceWith, CallInfo call, Solver.SolverInterface c, boolean withFeedbackLoop) {
        boolean anyCallbacks = false;
        if (toReplaceWith.isMaybeObject()) {
            for (ObjectLabel objectLabel : toReplaceWith.getObjectLabels()) {
                if (objectLabel.getKind() == Kind.FUNCTION) {
                    anyCallbacks = true;
                }
            }
        }
        if (anyCallbacks) {
            invokeCallback(toReplaceWith, c, withFeedbackLoop);
        }
        return anyCallbacks;
    }

    public static Value evaluateToString(Value thisval, Solver.SolverInterface c) {
        List<Value> strs = newList();
        boolean is_maybe_typeerror = thisval.isMaybePrimitive();
        strs.add(thisval.restrictToStr());
        for (ObjectLabel thisObj : thisval.getObjectLabels()) {
            if (thisObj.getKind() == Kind.STRING) {
                strs.add(c.getState().readInternalValue(singleton(thisObj)));
            } else {
                is_maybe_typeerror = true;
            }
        }
        if (is_maybe_typeerror) {
            Exceptions.throwTypeError(c);
        }
        return Value.join(strs);
    }
}
