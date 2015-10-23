/*
 * Copyright 2009-2015 Aarhus University
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
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.nativeobjects.JSRegExp.RegExpExecHandler;
import dk.brics.tajs.analysis.nativeobjects.concrete.Alpha;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteArray;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteBoolean;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteNull;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteNumber;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteRegularExpression;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteSemantics;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteString;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteUndefined;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteValue;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteValueVisitor;
import dk.brics.tajs.analysis.nativeobjects.concrete.Gamma;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.None;
import dk.brics.tajs.util.OptionalObjectVisitor;
import dk.brics.tajs.util.Some;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * 15.5 native String functions.
 */
public class JSString {

    private JSString() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, final CallInfo call, final State state, final Solver.SolverInterface c) {
        if (nativeobject != ECMAScriptObjects.STRING)
            if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
                return Value.makeNone();

        switch (nativeobject) {

            case STRING: { // 15.5.1/2
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
                Value s;
                if (call.isUnknownNumberOfArgs())
                    s = Conversion.toString(NativeFunctions.readParameter(call, state, 0), c).joinStr("");
                else
                    s = call.getNumberOfArgs() >= 1 ? Conversion.toString(NativeFunctions.readParameter(call, state, 0), c) : Value.makeStr("");
                if (call.isConstructorCall()) { // 15.5.2
                    ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.STRING);
                    state.newObject(objlabel);
                    state.writeInternalValue(objlabel, s);
                    state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.STRING_PROTOTYPE));
                    Value len = s.isMaybeSingleStr() ? Value.makeNum(s.getStr().length()) : Value.makeAnyNumUInt();
                    state.writePropertyWithAttributes(objlabel, "length", len.setAttributes(true, true, true));
                    return Value.makeObject(objlabel);
                } else // 15.5.1
                    return s;
            }

            case STRING_FROMCHARCODE: { // 15.5.3.2
                NativeFunctions.expectParameters(nativeobject, call, c, 0, -1);
                if (call.isUnknownNumberOfArgs())
                    Conversion.toNumber(call.getUnknownArg().joinUndef(), c);
                for (int i = 0; i < call.getNumberOfArgs(); i++) {
                    Conversion.toNumber(NativeFunctions.readParameter(call, state, i), c);
                }
                return TAJSConcreteSemantics.convertTAJSCall(Value.makeUndef(), "String.fromCharCode", -1, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }

            case STRING_TOSTRING: // 15.5.4.2
            case STRING_VALUEOF: { // 15.5.4.3
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.STRING))
                    return Value.makeNone();
                return state.readInternalValue(state.readThisObjects());
            }

            case STRING_CHARAT: {// 15.5.4.4
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value receiver = Value.makeObject(state.readThisObjects());
                Conversion.toString(receiver, c);
                Conversion.toInteger(NativeFunctions.readParameter(call, state, 0), c);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.charAt", 1, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }
            case STRING_CHARCODEAT: { // 15.5.4.5
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value receiver = Value.makeObject(state.readThisObjects());
                Conversion.toString(receiver, c);
                Conversion.toInteger(NativeFunctions.readParameter(call, state, 0), c);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.charCodeAt", 1, ConcreteNumber.class, state, call, c, Value.makeAnyNumUInt().joinNumNaN());
            }

            case STRING_CONCAT: { // 15.5.4.6
                NativeFunctions.expectParameters(nativeobject, call, c, 0, -1);
                Value receiver = Value.makeObject(state.readThisObjects());
                Conversion.toString(receiver, c);
                if (call.isUnknownNumberOfArgs())
                    Conversion.toNumber(call.getUnknownArg().joinUndef(), c);
                for (int i = 0; i < call.getNumberOfArgs(); i++) {
                    Conversion.toNumber(NativeFunctions.readParameter(call, state, i), c);
                }
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.concat", -1, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }

            case STRING_INDEXOF: {// 15.5.4.7
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
                Value receiver = Value.makeObject(state.readThisObjects());
                Conversion.toString(receiver, c);
                Conversion.toString(NativeFunctions.readParameter(call, state, 0), c);
                Conversion.toInteger(NativeFunctions.readParameter(call, state, 1), c);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.indexOf", 2, ConcreteNumber.class, state, call, c, Value.makeAnyNumNotNaNInf());
            }
            case STRING_LASTINDEXOF: { // 15.5.4.8
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
                Value receiver = Value.makeObject(state.readThisObjects());
                Conversion.toString(receiver, c);
                Conversion.toString(NativeFunctions.readParameter(call, state, 0), c);
                Conversion.toInteger(NativeFunctions.readParameter(call, state, 1), c);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.lastIndexOf", 2, ConcreteNumber.class, state, call, c, Value.makeAnyNumNotNaNInf());
            }

            case STRING_LOCALECOMPARE: { // 15.5.4.9
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value receiver = Value.makeObject(state.readThisObjects());
                Conversion.toString(receiver, c);
                Conversion.toString(NativeFunctions.readParameter(call, state, 0), c);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.localeCompare", 1, ConcreteNumber.class, state, call, c, Value.makeAnyNumNotNaNInf());
            }

            case STRING_MATCH: { // 15.5.4.10 (see REGEXP_EXEC)
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value receiver = Value.makeObject(state.readThisObjects());
                Conversion.toString(receiver, c);
                // NB: a argument string might be coerced to a regular expression...
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.match", 1, ConcreteValue.class, state, call, c).apply(new RegExpExecHandler(call, state));
            }

            case STRING_REPLACE: { // 15.5.4.11
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
                Value receiver = Value.makeObject(state.readThisObjects());
                Conversion.toString(receiver, c);
                // NB: a argument#0 string might be coerced to a regular expression...
                Conversion.toString(NativeFunctions.readParameter(call, state, 1), c);

                final Value toReplaceWith;
                final Value toReplace;
                if (call.isUnknownNumberOfArgs()) {
                    toReplace = NativeFunctions.readParameter(call, state, 0);
                    toReplaceWith = call.getUnknownArg().joinUndef();
                } else {
                    toReplace = NativeFunctions.readParameter(call, state, 0);
                    toReplaceWith = NativeFunctions.readParameter(call, state, 1);
                }
                if (Gamma.isConcreteValue(state.readThis(), c) && Gamma.isConcreteValue(toReplace, c)) {
                    // sound "optimization": if a function is given as second argument, then it is only a problem if it could be invoked..
                    return ConcreteSemantics.get().<ConcreteNumber>apply("String.prototype.search", Gamma.toConcreteValue(state.readThis(), c), Collections.singletonList(escapeAnyStringForRegExp(Gamma.toConcreteValue(toReplace, c)))).apply(new OptionalObjectVisitor<Value, ConcreteNumber>() {
                        private void checkFunction() {
                            if (toReplaceWith.isMaybeObject()) {
                                for (ObjectLabel objectLabel : toReplaceWith.getObjectLabels()) {
                                    if (objectLabel.getKind() == Kind.FUNCTION) {
                                        if (Options.get().isUnsoundEnabled()) {
                                            c.getMonitoring().addMessage(call.getSourceNode(), Severity.HIGH, "Ignoring String.replace(..., function(){...})");
                                        } else {
                                            // FIXME unsoundly skipping the side effects of the callback
                                            // throw new AnalysisException("Cannot handle String.replace(..., function(){...})");
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public Value visit(None<ConcreteNumber> o) {
                            checkFunction();
                            return Value.makeAnyStr();
                        }

                        @Override
                        public Value visit(Some<ConcreteNumber> o) {
                            boolean hasAnyMatches = (int) o.get().getNumber() != -1;
                            if (hasAnyMatches) {
                                checkFunction();
                            }
                            return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.replace", hasAnyMatches ? 2 : 1, ConcreteString.class, state, call, c, Value.makeAnyStr());
                        }
                    });
                }
                return Value.makeAnyStr();
            }

            case STRING_SEARCH: { // 15.5.4.12
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value receiver = Value.makeObject(state.readThisObjects());
                Conversion.toString(receiver, c);
                // NB: a argument#0 string might be coerced to a regular expression...
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.search", 1, ConcreteNumber.class, state, call, c, Value.makeAnyNumNotNaNInf());
            }

            case STRING_SLICE: {  // 15.5.4.13
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 2);
                Value receiver = Value.makeObject(state.readThisObjects());
                Conversion.toString(receiver, c);
                Conversion.toNumber(NativeFunctions.readParameter(call, state, 0), c);
                // only done if param != undefined
                Conversion.toNumber(NativeFunctions.readParameter(call, state, 1), c);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.slice", 2, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }
            case STRING_SUBSTRING: {  // 15.5.4.15
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 2);
                Value receiver = Value.makeObject(state.readThisObjects());
                Conversion.toString(receiver, c);
                Conversion.toNumber(NativeFunctions.readParameter(call, state, 0), c);
                // only done if param != undefined
                Conversion.toNumber(NativeFunctions.readParameter(call, state, 1), c);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.substring", 2, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }
            case STRING_SUBSTR: { // B.2.3
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 2);
                Value receiver = Value.makeObject(state.readThisObjects());
                Conversion.toString(receiver, c);
                Conversion.toNumber(NativeFunctions.readParameter(call, state, 0), c);
                // only done if param != undefined
                Conversion.toNumber(NativeFunctions.readParameter(call, state, 1), c);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.substr", 2, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }

            case STRING_SPLIT: { // 15.5.4.14
                return splitString(nativeobject, call, state, c);
            }

            case STRING_TOLOWERCASE: { // 15.5.4.16
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                Conversion.toString(Value.makeObject(state.readThisObjects()), c);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.toLowerCase", 0, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }
            case STRING_TOUPPERCASE: { // 15.5.4.18
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                Conversion.toString(Value.makeObject(state.readThisObjects()), c);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.toUpperCase", 0, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }

            case STRING_TOLOCALELOWERCASE: { // 15.5.4.17
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                Conversion.toString(Value.makeObject(state.readThisObjects()), c);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.toLocaleLowerCase", 0, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }
            case STRING_TOLOCALEUPPERCASE: { // 15.5.4.19
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                Conversion.toString(Value.makeObject(state.readThisObjects()), c);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.toLocaleUpperCase", 0, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }

            case STRING_TRIM: { // 15.5.4.20
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                Value vthisstring = Conversion.toString(Value.makeObject(state.readThisObjects()), c);
                if (vthisstring.isMaybeSingleStr()) {
                    String sthis = vthisstring.getStr();
                    return Value.makeStr(sthis.trim());
                }
                return Value.makeAnyStr();
            }

            default:
                return null;
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
                return ConcreteSemantics.get().apply("String.prototype.replace", v, Arrays.asList(toReplace, toReplaceWith)).apply(new OptionalObjectVisitor<ConcreteValue, ConcreteValue>() {
                    @Override
                    public ConcreteValue visit(None<ConcreteValue> obj) {
                        throw new AnalysisException("Unable to escape string to RegExp?!?");
                    }

                    @Override
                    public ConcreteValue visit(Some<ConcreteValue> obj) {
                        return obj.get();
                    }
                });
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

    private static Value splitString(ECMAScriptObjects nativeobject, final CallInfo call, final State state, final Solver.SolverInterface c) {
        NativeFunctions.expectParameters(nativeobject, call, c, 0, 2);
        return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "String.prototype.split", 2, ConcreteArray.class, state, call, c).apply(new OptionalObjectVisitor<Value, ConcreteArray>() {
            @Override
            public Value visit(Some<ConcreteArray> o) {
                return Alpha.createNewArrayValue(o.get(), state, call.getSourceNode());
            }

            @Override
            public Value visit(None<ConcreteArray> o) {
                Value separator = NativeFunctions.readParameter(call, state, 0);
                Value origLimit = NativeFunctions.readParameter(call, state, 1);
                boolean isUnlimited = !origLimit.isNotUndef();
                Value limit = isUnlimited ? Value.makeNum(Math.pow(2, 32) - 1) /* limit by spec! */ : Conversion.toNumber(origLimit, c);

                ObjectLabel resultArray = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);

                state.newObject(resultArray);
                state.writeInternalPrototype(resultArray, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));
                final Value thisStringValue = Conversion.toString(state.readThis(), c);
                if (limit.equals(Value.makeNum(0))) {
                    // CASE: a definite limit of zero
                    state.writePropertyWithAttributes(resultArray, "length", limit.setAttributes(true, true, false));
                    return Value.makeObject(resultArray);
                }

                if (separator.isMaybeUndef()) {
                    // CASE: undefined separator
                    state.writeProperty(Collections.singleton(resultArray), Value.makeStr("0"), thisStringValue, true, false);
                    state.writePropertyWithAttributes(resultArray, "length", Value.makeNum(1).setAttributes(true, true, false));
                    return Value.makeObject(resultArray);
                }

                if (separator.isMaybeSingleStr() && thisStringValue.isMaybeSingleStr() && separator.equals(Value.makeStr(separator.getStr())) && thisStringValue.equals(Value.makeStr(thisStringValue.getStr()))) {
                    // precise case: both this and input are definite single strings
                    final Map<String, Value> argsMap = newMap();
                    argsMap.put("<base/this>", Value.makeStr(thisStringValue.getStr()));
                    argsMap.put("<arg/separator>", Value.makeStr(separator.getStr()));
                    // we are precise, so allocate a unique array
                    resultArray = new ObjectLabel(call.getSourceNode(), Kind.ARRAY, new HeapContext(null, argsMap));
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
                    state.writePropertyWithAttributes(resultArray, "length", resultArrayLength.setAttributes(true, true, false));
                    Set<ObjectLabel> toWrite = Collections.singleton(resultArray);
                    for (int i = 0; i < splitValues.size(); i++) {
                        final Value index = Value.makeStr(String.valueOf(i));
                        final Value value = splitValues.get(i);
                        state.writeProperty(toWrite, index, value, true, false);
                    }
                    return Value.makeObject(resultArray);
                } else {
                    // CASE imprecise string information
                    state.writeProperty(Collections.singleton(resultArray), Value.makeAnyStrUInt(), Value.makeAnyStr(), true, true);
                    state.writePropertyWithAttributes(resultArray, "length", Value.makeAnyNumUInt().setAttributes(true, true, false));
                    return Value.makeObject(resultArray);
                }
            }
        });
    }
}
