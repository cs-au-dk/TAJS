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
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;

import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;
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
                    v = Conversion.toNumber(FunctionCalls.readParameter(call, state, 0), c).joinNum(+0.0d);
                else if (call.getNumberOfArgs() >= 1)
                    v = Conversion.toNumber(FunctionCalls.readParameter(call, state, 0), c);
                else
                    v = Value.makeNum(+0.0d);
                if (call.isConstructorCall()) { // 15.7.2
                    return Conversion.toObject(call.getSourceNode(), v, false, c);
                } else // 15.7.1
                    return v;
            }

            case NUMBER_ISFINITE: {
                Value n = FunctionCalls.readParameter(call, state, 0);
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
                Value f = FunctionCalls.readParameter(call, state, 0);
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
                    Exceptions.throwRangeError(c, maybe_rangeerror);
                    c.getMonitoring().addMessage(call.getSourceNode(), Severity.HIGH, "RangeError in Number function");
                }
                if (definitely_rangeerror)
                    return Value.makeNone();

                return TAJSConcreteSemantics.convertTAJSCall(base, nativeobject.toString(), 1, call, c, Value::makeAnyStr);
            }

            case NUMBER_TOLOCALESTRING: // 15.7.4.3
            case NUMBER_TOSTRING: { // 15.7.4.2
                Value radix = FunctionCalls.readParameter(call, c.getState(), 0);
                Value v = evaluateToString(state.readThis(), radix, c);
                if (nativeobject == ECMAScriptObjects.NUMBER_TOLOCALESTRING && !c.getAnalysis().getUnsoundness().mayAssumeFixedLocale(call.getSourceNode())) {
                    return Value.makeAnyStr();
                }
                return v;
            }

            case NUMBER_VALUEOF: { // 15.7.4.4
                Set<ObjectLabel> numberObjects = newSet(state.readThisObjects());
                numberObjects.removeIf(l -> l.getKind() != Kind.NUMBER);  // exceptions handled by NativeFunctionSignatureChecker
                return state.readInternalValue(numberObjects);
            }

            case NUMBER_ISSAFEINTEGER:
            case NUMBER_ISINTEGER:
            case NUMBER_ISNAN: {
                return Value.makeAnyBool();
            }

            default:
                return null;
        }
    }

    public static Value evaluateToString(Value thisval, Value radix, Solver.SolverInterface c) {
        List<Value> numvals = newList();
        boolean is_maybe_typeerror = thisval.isMaybePrimitive();
        Value numval = thisval.restrictToStr();
        if (!numval.isNotNum()) {
            numvals.add(numval);
        }
        for (ObjectLabel thisObj : thisval.getObjectLabels()) {
            if (thisObj.getKind() != Kind.NUMBER) {
                is_maybe_typeerror = true;
            }
            Value v = c.getState().readInternalValue(singleton(thisObj));
            v = UnknownValueResolver.getRealValue(v, c.getState());
            numvals.add(v);

        }
        if (is_maybe_typeerror) {
            Exceptions.throwTypeError(c);
        }
        radix = UnknownValueResolver.getRealValue(radix, c.getState());
        if (radix.isMaybeUndef()) {
            radix = radix.restrictToNotUndef().join(Value.makeNum(10));
        }
        radix = Conversion.toInteger(radix, c);
        List<Value> strs = newList();
        for (Value v : numvals) {
            Value str;
            if (radix.getNum() == null || radix.getNum() != 10) {
                str = TAJSConcreteSemantics.convertTAJSCallExplicit(v, "Number.prototype.toString", singletonList(radix), c, Value::makeAnyStr);
            } else {
                str = Conversion.toString(v, c);
            }
            strs.add(str);
        }
        return Value.join(strs);
    }
}
