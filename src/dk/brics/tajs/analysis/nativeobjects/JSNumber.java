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
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteString;
import dk.brics.tajs.analysis.nativeobjects.concrete.Gamma;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;

/**
 * 15.7 native Number functions.
 */
public class JSNumber {

    private JSNumber() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
        if (nativeobject != ECMAScriptObjects.NUMBER)
            if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
                return Value.makeNone();

        switch (nativeobject) {

            case NUMBER: {
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
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

            case NUMBER_TOFIXED: // 15.7.4.5
            case NUMBER_TOEXPONENTIAL: // 15.7.4.6
            case NUMBER_TOPRECISION: { // 15.7.4.7

                NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
                if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.NUMBER))
                    return Value.makeNone();

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
                base = UnknownValueResolver.getRealValue(base, c.getCurrentState());

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
                    Exceptions.throwRangeError(state, c);
                    c.getMonitoring().addMessage(call.getSourceNode(), Severity.HIGH, "RangeError in Number function");
                }
                if (definitely_rangeerror)
                    return Value.makeNone();

                if (nativeobject == ECMAScriptObjects.NUMBER_TOFIXED && Gamma.isConcreteNumber(base, c) && Gamma.isConcreteNumber(f, c)) {
                    boolean fIsZero = Double.valueOf(Gamma.toConcreteNumber(f, c).getNumber()).equals(0.0);
                    double concreteBaseNumber = Gamma.toConcreteNumber(base, c).getNumber();
                    boolean baseIsHalfy = Double.valueOf(Math.abs(concreteBaseNumber)).equals(0.5);
                    boolean triggersConcreteSemanticsBug = baseIsHalfy && fIsZero;
                    // The Nashorn engine for Concrete semantics has a bug, where it returns 0 on:
                    // > 0.5.toFixed()
                    // > -0.5.toFixed()
                    // it should return 1 and -1 respectively...
                    if (triggersConcreteSemanticsBug) {
                        return Value.makeStr(concreteBaseNumber > 0 ? "1" : "-1");
                    }
                }
                return TAJSConcreteSemantics.convertTAJSCall(base, nativeobject.toString(), 1, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }

            case NUMBER_TOLOCALESTRING: // 15.7.4.3
            case NUMBER_TOSTRING: { // 15.7.4.2
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
                if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.NUMBER))
                    return Value.makeNone();
                Value val = state.readInternalValue(state.readThisObjects());
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() == 0)
                    return Conversion.toString(val, c);
                else
                    return Value.makeAnyStr();
                // TODO: assuming that toLocaleString methods behave as toString (also other objects) - OK?
            }

            case NUMBER_VALUEOF: { // 15.7.4.4
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.NUMBER))
                    return Value.makeNone();
                return state.readInternalValue(state.readThisObjects());
            }

            default:
                return null;
        }
    }
}
