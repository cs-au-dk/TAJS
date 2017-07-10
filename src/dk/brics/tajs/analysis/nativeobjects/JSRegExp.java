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
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.nativeobjects.concrete.SingleGamma;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.util.Collectors;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * 15.10 native RegExp functions.
 */
public class JSRegExp {

    private static Value V_THIS; // TODO see https://dev.opera.com/articles/javascript-for-hackers/

    private JSRegExp() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, final CallInfo call, Solver.SolverInterface c) {
        State state = c.getState();
        switch (nativeobject) {

            case REGEXP: {
                // TODO: Needs code review.
                boolean ctor = call.isConstructorCall();
                Value pattern = call.getNumberOfArgs() > 0 ? FunctionCalls.readParameter(call, state, 0) : Value.makeStr(""); // TODO: handle unknown number of args
                Value flags = call.getNumberOfArgs() > 1 ? FunctionCalls.readParameter(call, state, 1) : Value.makeUndef();
                Value result = Value.makeNone();
                // Use arg as our working copy of pattern.
                Value arg = pattern;
                // 15.10.3.1 function call; If the pattern is a RegExp object and flags are undefined, return the pattern.
                if (!flags.isNotUndef()) {
                    boolean regexp_ol = true;
                    for (ObjectLabel ol : pattern.getObjectLabels()) {
                        if (ol.getKind() != Kind.REGEXP)
                            regexp_ol = false;
                    }
                    if (regexp_ol && !ctor && !pattern.getObjectLabels().isEmpty())
                        return pattern;
                }

                // Otherwise call the RegExp constructor as per 15.10.4.1.
                if (flags.isMaybeUndef()) {
                    for (ObjectLabel ol : pattern.getObjectLabels())
                        if (ol.getKind() == Kind.REGEXP) {
                            result = result.joinObject(ol);
                        } else {
                            arg = arg.joinObject(ol);
                        }
                }
                if (flags.isMaybeOtherThanUndef())
                    for (ObjectLabel ol : pattern.getObjectLabels())
                        arg = arg.joinObject(ol);
                if (ctor && !flags.isMaybeUndef() && !result.getObjectLabels().isEmpty()) {
                    // TODO: Throw a TypeError exception as per 15.10.4.1 if we are certain?
                    c.getMonitoring().addMessage(c.getNode(), Severity.HIGH, "TypeError, internal RegExp property with flags not undefined");
                }
                if (!arg.isNotPresent()) {
                    AbstractNode sourceNode = call.getSourceNode();
                    ObjectLabel label = allocateUninitializedRegExp(sourceNode, null, state);

                    boolean isRegExpLiteral = sourceNode instanceof CallNode && ((CallNode) sourceNode).getLiteralConstructorKind() == CallNode.LiteralConstructorKinds.REGEXP;
                    boolean threwException = mutateRegExp(Collections.singleton(label), arg, flags, isRegExpLiteral, !isRegExpLiteral, state, c);
                    if (threwException) {
                        return Value.makeNone();
                    }
                    result = result.joinObject(label);
                }
                return result;
            }
            case REGEXP_COMPILE: {
                Value pattern = call.getNumberOfArgs() > 0 ? FunctionCalls.readParameter(call, state, 0) : Value.makeStr(""); // TODO: handle unknown number of args
                Value flags = call.getNumberOfArgs() > 1 ? FunctionCalls.readParameter(call, state, 1) : Value.makeUndef();
                pattern = UnknownValueResolver.getRealValue(pattern, state);
                if (pattern.isMaybeObject()) {
                    // TODO proper support for regexp argument (currently unsound: missing flags transfer & checking for no double declaration of flags)
                    pattern = pattern.restrictToNotObject().join(UnknownValueResolver.getRealValue(c.getAnalysis().getPropVarOperations().readPropertyValue(pattern.getObjectLabels(), "source"), state));
                }
                boolean threwException = mutateRegExp(state.readThisObjects(), pattern, flags, false, false, state, c);
                if (threwException) {
                    return Value.makeNone();
                }
                return Value.makeUndef();
            }
            case REGEXP_EXEC: { // 15.10.6.2 (see STRING_MATCH)
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "RegExp.prototype.exec", 1, call, c, () -> handleUnknownRegexMatchResult(call.getSourceNode(), state.readThis(), c));
            }

            case REGEXP_TEST: { // 15.10.6.3
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "RegExp.prototype.test", 1, call, c, () -> {
                    makeFuzzyLastIndexOfAnyGlobalRegexes(state.readThis(), c);
                    return Value.makeAnyBool();
                });
            }

            case REGEXP_TOSTRING: { // 15.10.6.4
                return evaluateToString(state.readThis(), c);
            }

            default:
                return null;
        }
    }

    private static ObjectLabel allocateUninitializedRegExp(AbstractNode sourceNode, HeapContext heapContext, State state) {
        ObjectLabel label = ObjectLabel.make(sourceNode, Kind.REGEXP, heapContext);
        state.newObject(label);
        state.writeInternalPrototype(label, Value.makeObject(InitialStateBuilder.REGEXP_PROTOTYPE));
        return label;
    }

    /**
     * Some shared functionality between the RegExp constructor and RegExp.compile.
     * Will return 'true' if a definite exception has occured.
     */
    private static boolean mutateRegExp(Set<ObjectLabel> regexp, Value pattern, Value flags, boolean isRegExpLiteral, boolean isConstructorCall, State state, Solver.SolverInterface c) {
        final Value pGlobal;
        final Value pIgnoreCase;
        final Value pMultiline;
        if (!flags.isMaybeUndef()) {
            Value sflags = Conversion.toString(flags, c);
            if (sflags.isMaybeSingleStr()) {
                // flags are known
                String strflags = sflags.getStr();
                pGlobal = Value.makeBool(strflags.contains("g"));
                pIgnoreCase = Value.makeBool(strflags.contains("i"));
                pMultiline = Value.makeBool(strflags.contains("m"));
                strflags = strflags.replaceFirst("g", "").replaceFirst("i", "").replaceFirst("m", "");
                if ((!strflags.isEmpty())) {
                    Exceptions.throwSyntaxError(c);
                    c.getMonitoring().addMessage(c.getNode(), Severity.HIGH, "SyntaxError, invalid flags at RegExp constructor");
                    return true;
                }
            } else {
                // flags are unknown
                pGlobal = Value.makeAnyBool();
                pIgnoreCase = Value.makeAnyBool();
                pMultiline = Value.makeAnyBool();
            }
        } else {
            // flags are undefined
            pGlobal = Value.makeBool(false);
            pIgnoreCase = Value.makeBool(false);
            pMultiline = Value.makeBool(false);
        }

        Set<ObjectLabel> regExpArgument = pattern.getObjectLabels().stream().filter(l -> l.getKind() == Kind.REGEXP).collect(Collectors.toSet());
        Value nonRegExpArgument = pattern.removeObjects(regExpArgument);
        if (isConstructorCall && nonRegExpArgument.isMaybeUndef()) {
            nonRegExpArgument = nonRegExpArgument.restrictToNotUndef().joinStr("");
        }
        nonRegExpArgument = Conversion.toString(nonRegExpArgument, c);

        Value internalRegExpArgumentValue = UnknownValueResolver.getRealValue(state.readInternalValue(regExpArgument), state);
        Value patternToUse = Value.join(nonRegExpArgument, internalRegExpArgumentValue);
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        if (!isRegExpLiteral && SingleGamma.isConcreteString(patternToUse, c) && (patternToUse.getStr().isEmpty() || patternToUse.getStr().contains("/"))) {
            // 15.10.4.1:
            // The characters / occurring in the pattern shall be escaped in S as necessary to ensure
            // that the String value formed by concatenating the Strings "/", S, "/", and F can be
            // parsed (in an appropriate lexical context) as a RegularExpressionLiteral that behaves
            // identically to the constructed regular expression ...
            // ... If P is the empty String, this specification can be met by letting S be "(?:)".

            // let the concrete semantic handle this mess...
            if (patternToUse.getStr().contains("/")) {
                // Nashorn does not do the *first* part of 15.10.4.1.
                // TODO remove this branch once Nashorn has improved (create bug report?) (see GitHub #194)
                // Proper escaping is done in firefox, but not in Chrome: https://code.google.com/p/chromium/issues/detail?id=515897
                patternToUse = Value.makeAnyStr();
            } else {
                Value concreteResult = TAJSConcreteSemantics.convertTAJSCallExplicit(Value.makeUndef(), "RegExp", Collections.singletonList(patternToUse), c);
                patternToUse = pv.readPropertyValue(concreteResult.getObjectLabels(), "source");
            }
        }
        writeRegExpProperties(regexp, state, patternToUse, pGlobal, pIgnoreCase, pMultiline, Value.makeNum(0), pv);
        return false;
    }

    private static void writeRegExpProperties(Set<ObjectLabel> regexp, State state, Value pattern, Value global, Value ignoreCase, Value multiline, Value lastIndex, PropVarOperations pv) {
        state.writeInternalValue(regexp, pattern);
        pv.writePropertyWithAttributes(regexp, "source", pattern.setAttributes(true, true, true));
        pv.writePropertyWithAttributes(regexp, "lastIndex", lastIndex.setAttributes(true, true, false));
        pv.writePropertyWithAttributes(regexp, "global", global.setAttributes(true, true, true));
        pv.writePropertyWithAttributes(regexp, "ignoreCase", ignoreCase.setAttributes(true, true, true));
        pv.writePropertyWithAttributes(regexp, "multiline", multiline.setAttributes(true, true, true));
    }

    public static Value handleUnknownRegexMatchResult(AbstractNode sourceNode, Value regex, Solver.SolverInterface c) {
        ObjectLabel objlabel = JSArray.makeArray(sourceNode, c);
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        Value res = Value.makeObject(objlabel);
        pv.writeProperty(Collections.singleton(objlabel), Value.makeAnyStrUInt(), Value.makeAnyStr().joinAbsent());
        pv.writePropertyWithAttributes(objlabel, "length", Value.makeAnyNumUInt().setAttributes(true, true, false));
        pv.writeProperty(objlabel, "index", Value.makeAnyNumUInt());
        pv.writeProperty(objlabel, "input", c.getState().readThis()); // TODO: this should be a string??!

        makeFuzzyLastIndexOfAnyGlobalRegexes(regex, c);

        return res.joinNull();
    }

    public static void makeFuzzyLastIndexOfAnyGlobalRegexes(Value value, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        State state = c.getState();
        UnknownValueResolver.getRealValue(value, state).getObjectLabels().stream()
                .filter(l -> l.getKind() == Kind.REGEXP)
                .filter(l -> UnknownValueResolver.getRealValue(pv.readPropertyValue(singleton(l), "global"), state).isMaybeTrue())
                .forEach(l -> pv.writeProperty(singleton(l), Value.makeStr("lastIndex"), Value.makeAnyNumUInt(), true));
    }

    public static Value evaluateToString(Value thisval, Solver.SolverInterface c) {
        List<Value> strs = newList();
        boolean is_maybe_typeerror = thisval.isMaybePrimitive();
        for (ObjectLabel thisObj : thisval.getObjectLabels()) {
            if (thisObj.getKind() == Kind.REGEXP) {
                strs.add(TAJSConcreteSemantics.convertTAJSCallExplicit(Value.makeObject(thisObj), "RegExp.prototype.toString", newList(), c, Value::makeAnyStr));
            } else {
                is_maybe_typeerror = true;
            }
        }
        if (is_maybe_typeerror) {
            Exceptions.throwTypeError(c);
        }
        return Value.join(strs);
    }

    public static ObjectLabel makeRegExp(AbstractNode sourceNode, String source, boolean global, boolean ignoreCase, boolean multiline, double lastIndex, HeapContext heapContext, Solver.SolverInterface c) {
        ObjectLabel label = allocateUninitializedRegExp(sourceNode, heapContext, c.getState());
        writeRegExpProperties(singleton(label), c.getState(), Value.makeStr(source), Value.makeBool(global), Value.makeBool(ignoreCase), Value.makeBool(multiline), Value.makeNum(lastIndex), c.getAnalysis().getPropVarOperations());
        return label;
    }
}
