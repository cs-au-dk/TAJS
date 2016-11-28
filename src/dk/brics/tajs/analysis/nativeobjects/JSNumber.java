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
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message.Severity;

import static dk.brics.tajs.util.Collections.singleton;
import static dk.brics.tajs.util.Collections.singletonList;

/**
 * 15.7 native Number functions.
 */
public class JSNumber {

    private JSNumber() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, Solver.SolverInterface c) {
        State state = c.getState();
        switch (nativeobject) {

            case NUMBER: {
                Value v;
                if (call.isUnknownNumberOfArgs())
                    v = Conversion.toNumber(NativeFunctions.readParameter(call, state, 0), c).joinNum(+0.0d);
                else if (call.getNumberOfArgs() >= 1)
                    v = Conversion.toNumber(NativeFunctions.readParameter(call, state, 0), c);
                else
                    v = Value.makeNum(+0.0d);
                if (call.isConstructorCall()) { // 15.7.2
                    ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.NUMBER);
                    state.newObject(objlabel);
                    state.writeInternalValue(objlabel, v);
                    state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.NUMBER_PROTOTYPE));
                    return Value.makeObject(objlabel);
                } else // 15.7.1
                    return v;
            }

            case NUMBER_ISFINITE: {
                Value n = NativeFunctions.readParameter(call, state, 0);
                Value result = Value.makeNone();

                if (n.isMaybeOtherThanNum()) {
                    result = result.joinBool(false);
                }
                Value num = n.restrictToNum();
                if (!num.isNone()) {
                    if (num.isMaybeNaN() || num.isMaybeInf()) {
                        result = result.joinBool(false);
                    }
                    if (num.isMaybeAnyNum() || num.isMaybeNumUInt() || num.isMaybeSingleNum() || num.isMaybeNumOther()) {
                        result = result.joinBool(true);
                    }
                }
                return result;
            }

            case NUMBER_TOFIXED: // 15.7.4.5
            case NUMBER_TOEXPONENTIAL: // 15.7.4.6
            case NUMBER_TOPRECISION: { // 15.7.4.7
                Value f = NativeFunctions.readParameter(call, state, 0);
                if (f.isMaybeUndef()) {
                    f = Conversion.toInteger(f.restrictToNotUndef(), c);
                    if (nativeobject == ECMAScriptObjects.NUMBER_TOFIXED || nativeobject == ECMAScriptObjects.NUMBER_TOEXPONENTIAL) {
                        f = f.joinNum(0);
                    } else {
                        // .toPrecision will resort to ToString(Number) in this case, the ConcreteSemantics call later should take care of that case
                    }
                } else {
                    f = Conversion.toInteger(f, c);
                }

                Value base = state.readInternalValue(state.readThisObjects());
                base = UnknownValueResolver.getRealValue(base, c.getState());

                boolean definitely_rangeerror = false;
                boolean maybe_rangeerror = false;
                if (f.isMaybeSingleNum()) {
                    int f_int = f.getNum().intValue();
                    if (f_int < 0 || f_int > 20)
                        definitely_rangeerror = true;
                } else {
                    maybe_rangeerror = true;
                }
                if (maybe_rangeerror || definitely_rangeerror) {
                    Exceptions.throwRangeError(c);
                    c.getMonitoring().addMessage(call.getSourceNode(), Severity.HIGH, "RangeError in Number function");
                }
                if (definitely_rangeerror)
                    return Value.makeNone();

                return TAJSConcreteSemantics.convertTAJSCall(base, nativeobject.toString(), 1, call, c, Value.makeAnyStr());
            }

            case NUMBER_TOLOCALESTRING: // 15.7.4.3
            case NUMBER_TOSTRING: { // 15.7.4.2
                Value radix = NativeFunctions.readParameter(call, c.getState(), 0);
                Value v = state.readThisObjectsCoerced((l) -> evaluateToString(l, radix, c));
                if (nativeobject == ECMAScriptObjects.NUMBER_TOLOCALESTRING && !Options.get().isUnsoundEnabled()) {
                    return Value.makeAnyStr();
                }
                return v;
            }

            case NUMBER_VALUEOF: { // 15.7.4.4
                return state.readInternalValue(state.readThisObjects());
            }

            default:
                return null;
        }
    }

    public static Value evaluateToString(ObjectLabel thiss, Value radix, Solver.SolverInterface c) {
        // 15.7.4.2 Number.prototype.toString (radix)
        // If radix is the number 10 or undefined, then this number value is given as an argument to the ToString operator;
        // the resulting string value is returned.
        // If radix is an integer from 2 to 36, but not 10, the result is a string, the choice of which is implementation-dependent.
        if (thiss.getKind() != Kind.NUMBER) {
            Exceptions.throwTypeError(c);
            return Value.makeNone();
        }
        State state = c.getState();
        Value v = state.readInternalValue(singleton(thiss));
        v = UnknownValueResolver.getRealValue(v, state);
        radix = UnknownValueResolver.getRealValue(radix, c.getState());
        if (radix.isMaybeUndef()) {
            radix = radix.restrictToNotUndef().join(Value.makeNum(10));
        }
        radix = Conversion.toInteger(radix, c);
        if (radix.getNum() == null || radix.getNum() != 10) {
            return TAJSConcreteSemantics.convertTAJSCallExplicit(v, "Number.prototype.toString", singletonList(radix), c, Value.makeAnyStr());
        }
        return Conversion.toString(v, c);
    }
}
