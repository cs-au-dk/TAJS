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
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
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
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.None;
import dk.brics.tajs.util.OptionalObjectVisitor;
import dk.brics.tajs.util.Some;

import java.util.Collections;
import java.util.Set;

/**
 * 15.10 native RegExp functions.
 */
public class JSRegExp { // TODO see https://dev.opera.com/articles/javascript-for-hackers/

    private JSRegExp() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, final CallInfo call, Solver.SolverInterface c) {
        if (nativeobject != ECMAScriptObjects.REGEXP)
            if (NativeFunctions.throwTypeErrorIfConstructor(call, c))
                return Value.makeNone();

        State state = c.getState();
        switch (nativeobject) {

            case REGEXP: {
                // TODO: Needs code review.
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
                boolean ctor = call.isConstructorCall();
                Value pattern = call.getNumberOfArgs() > 0 ? NativeFunctions.readParameter(call, state, 0) : Value.makeStr(""); // TODO: handle unknown number of args
                Value flags = call.getNumberOfArgs() > 1 ? NativeFunctions.readParameter(call, state, 1) : Value.makeUndef();
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
                    ObjectLabel no = new ObjectLabel(call.getSourceNode(), Kind.REGEXP);
                    state.newObject(no);
                    state.writeInternalPrototype(no, Value.makeObject(InitialStateBuilder.REGEXP_PROTOTYPE));

                    boolean threwException = mutateRegExp(Collections.singleton(no), arg, flags, call.getSourceNode() instanceof CallNode && ((CallNode) call.getSourceNode()).getLiteralConstructorKind() == CallNode.LiteralConstructorKinds.REGEXP, state, c);
                    if(threwException){
                        return Value.makeNone();
                    }
                    result = result.joinObject(no);
                }
                return result;
            }
            case REGEXP_COMPILE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 2);
                Value pattern = call.getNumberOfArgs() > 0 ? NativeFunctions.readParameter(call, state, 0) : Value.makeStr(""); // TODO: handle unknown number of args
                Value flags = call.getNumberOfArgs() > 1 ? NativeFunctions.readParameter(call, state, 1) : Value.makeUndef();
                pattern = UnknownValueResolver.getRealValue(pattern, state);
                if(pattern.isMaybeObject()){
                    // TODO proper support for regexp argument (currently unsound: missing flags transfer & checking for no double declaration of flags)
                    pattern = pattern.restrictToNotObject().join(UnknownValueResolver.getRealValue(c.getAnalysis().getPropVarOperations().readPropertyValue(pattern.getObjectLabels(), "source"), state));
                }
                boolean threwException = mutateRegExp(state.readThisObjects(), pattern, flags, false, state, c);
                if(threwException){
                    return Value.makeNone();
                }
                return Value.makeUndef();
            }
            case REGEXP_EXEC: { // 15.10.6.2 (see STRING_MATCH)
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, state, 0), c);
                Value result = TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "RegExp.prototype.exec", 1, ConcreteValue.class, state, call, c).apply(new RegExpExecHandler(call, c));
                if (result.isMaybeObject()) {
                    updateRegExpLastIndex(state.readThis(), c);
                }
                return result;
            }

            case REGEXP_TEST: { // 15.10.6.3
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                final Value vThis = state.readThis();
                final Value vArg = Conversion.toString(NativeFunctions.readParameter(call, state, 0), c);

                if (Gamma.isConcreteValues(c, vThis, vArg)) {
                    Value execCallResult = ConcreteSemantics.get().apply("RegExp.prototype.exec", Gamma.toConcreteValue(state.readThis(), c), Collections.singletonList(Gamma.toConcreteValue(vArg, c))).apply(new RegExpExecHandler(call, c));
                    if (execCallResult.isMaybeObject()) {
                        updateRegExpLastIndex(state.readThis(), c);
                    }
                    return Value.makeBool(execCallResult.isMaybeObject() /* spec: true if not null, and concrete semantics guarantees a single value */);
                }
                return Value.makeAnyBool();
            }

            case REGEXP_TOSTRING: { // 15.10.6.4
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                return TAJSConcreteSemantics.convertTAJSCall(state.readThis(), "RegExp.prototype.toString", 0, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }

            default:
                return null;
        }
    }

    /**
     * Some shared functionality between the RegExp constructor and RegExp.compile.
     * Will return 'true' if a definite exception has occured.
     */
    private static boolean mutateRegExp(Set<ObjectLabel> regexp, Value pattern, Value flags, boolean isRegExpLiteral, State state, Solver.SolverInterface c) {
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
        if (!pattern.isNotStr()) {

            Value p = UnknownValueResolver.join(Conversion.toString(pattern.restrictToStr(), c), state.readInternalValue(pattern.restrictToStr().getObjectLabels()), state);
            final Value origP = p;
            if (!isRegExpLiteral && Gamma.isConcreteString(p, c) && (p.getStr().isEmpty() || p.getStr().contains("/"))) {
                // 15.10.4.1:
                // The characters / occurring in the pattern shall be escaped in S as necessary to ensure
                // that the String value formed by concatenating the Strings "/", S, "/", and F can be
                // parsed (in an appropriate lexical context) as a RegularExpressionLiteral that behaves
                // identically to the constructed regular expression ...
                // ... If P is the empty String, this specification can be met by letting S be "(?:)".

                // let the concrete semantic handle this mess...
                if (origP.getStr().contains("/")) {
                    // Nashorn does not do the *first* part of 15.10.4.1.
                    // TODO remove this branch once Nashorn has improved (create bug report?) (see GitHub #194)
                    // Proper escaping is done in firefox, but not in Chrome: https://code.google.com/p/chromium/issues/detail?id=515897
                    p = Value.makeAnyStr();
                } else {
                    p = TAJSConcreteSemantics.convertTAJSCallExplicit(Value.makeUndef(), "RegExp", Collections.singletonList(origP), ConcreteRegularExpression.class, c).apply(new OptionalObjectVisitor<Value, ConcreteRegularExpression>() {
                        @Override
                        public Value visit(None<ConcreteRegularExpression> obj) {
                            throw new AnalysisException("Could not do proper special casing of concrete string argument to RegExp(" + origP + ")");
                        }

                        @Override
                        public Value visit(Some<ConcreteRegularExpression> obj) {
                            Value source = Alpha.toValue(obj.get().getSource());
                            return source;
                        }
                    });
                }
            }
            state.writeInternalValue(regexp, p);
            PropVarOperations pv = c.getAnalysis().getPropVarOperations();
            pv.writePropertyWithAttributes(regexp, "source", p.setAttributes(true, true, true));
            pv.writePropertyWithAttributes(regexp, "lastIndex", Value.makeNum(0).setAttributes(true, true, false));
            pv.writePropertyWithAttributes(regexp, "global", pGlobal.setAttributes(true, true, true));
            pv.writePropertyWithAttributes(regexp, "ignoreCase", pIgnoreCase.setAttributes(true, true, true));
            pv.writePropertyWithAttributes(regexp, "multiline", pMultiline.setAttributes(true, true, true));
        }
        return false;
    }

    private static void updateRegExpLastIndex(Value value, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        if (UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "global"), c.getState()).isMaybeTrue()) {
            pv.writeProperty(value.getObjectLabels(), Value.makeTemporaryStr("lastIndex"), Value.makeAnyNumUInt(), false, true);
        }
    }

    /**
     * Handles the return value of a concrete call to RegExp.exec and String.match
     */
    public static class RegExpExecHandler implements OptionalObjectVisitor<Value, ConcreteValue> {

        private final CallInfo call;

        private final Solver.SolverInterface c;

        public RegExpExecHandler(CallInfo call, Solver.SolverInterface c) {
            this.call = call;
            this.c = c;
        }

        @Override
        public Value visit(None<ConcreteValue> o) {
            ObjectLabel objlabel = JSArray.makeArray(call.getSourceNode(), c);
            PropVarOperations pv = c.getAnalysis().getPropVarOperations();
            Value res = Value.makeObject(objlabel);
            pv.writeProperty(Collections.singleton(objlabel), Value.makeAnyStrUInt(), Value.makeAnyStr().joinAbsent(), false, true);
            pv.writePropertyWithAttributes(objlabel, "length", Value.makeAnyNumUInt().setAttributes(true, true, false));
            pv.writeProperty(objlabel, "index", Value.makeAnyNumUInt());
            pv.writeProperty(objlabel, "input", c.getState().readThis());
            return res.joinNull();
        }

        @Override
        public Value visit(Some<ConcreteValue> o) {
            return o.get().accept(new ConcreteValueVisitor<Value>() {
                @Override
                public Value visit(ConcreteNumber v) {
                    throw impossible();
                }

                private RuntimeException impossible() {
                    return new AnalysisException("Unexpected type");
                }

                @Override
                public Value visit(ConcreteString v) {
                    throw impossible();
                }

                @Override
                public Value visit(ConcreteArray v) {
                    return Alpha.createNewArrayValue(v, call.getSourceNode(), c);
                }

                @Override
                public Value visit(ConcreteUndefined v) {
                    throw impossible();
                }

                @Override
                public Value visit(ConcreteRegularExpression v) {
                    throw impossible();
                }

                @Override
                public Value visit(ConcreteNull v) {
                    return Value.makeNull();
                }

                @Override
                public Value visit(ConcreteBoolean v) {
                    throw impossible();
                }
            });
        }
    }
}
