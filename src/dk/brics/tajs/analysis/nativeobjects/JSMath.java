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
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import java.util.Random;

/**
 * 15.8 native Math functions.
 */
public class JSMath {

    private JSMath() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, Solver.SolverInterface c) {
        State state = c.getState();
        switch (nativeobject) {

            case MATH_ABS: // 15.8.2.1
            case MATH_ASIN: // 15.8.2.3
            case MATH_ACOS: // 15.8.2.2
            case MATH_ATAN:  // 15.8.2.4
            case MATH_CEIL: // 15.8.2.6
            case MATH_COS: // 15.8.2.7
            case MATH_EXP: // 15.8.2.8
            case MATH_FLOOR: // 15.8.2.9
            case MATH_LOG: // 15.8.2.10
            case MATH_ROUND: // 15.8.2.15
            case MATH_SIN: // 15.8.2.16
            case MATH_SQRT: // 15.8.2.17
            case MATH_TAN: // 15.8.2.18
            case MATH_SIGN:
            case MATH_LOG10: {
                Value num = Conversion.toNumber(FunctionCalls.readParameter(call, state, 0), c);
                if (num.isMaybeSingleNum()) {
                    double d = num.getNum();
                    double res;
                    switch (nativeobject) {
                        case MATH_ABS:
                            res = Math.abs(d);
                            break;
                        case MATH_ASIN:
                            res = Math.asin(d);
                            break;
                        case MATH_ACOS:
                            res = Math.acos(d);
                            break;
                        case MATH_ATAN:
                            res = Math.atan(d);
                            break;
                        case MATH_CEIL:
                            res = Math.ceil(d);
                            break;
                        case MATH_COS:
                            res = Math.cos(d);
                            break;
                        case MATH_EXP:
                            res = Math.exp(d);
                            break;
                        case MATH_FLOOR:
                            res = Math.floor(d);
                            break;
                        case MATH_LOG:
                            res = Math.log(d);
                            break;
                        case MATH_ROUND:
                            res = Math.round(d);
                            break;
                        case MATH_SIN:
                            res = Math.sin(d);
                            break;
                        case MATH_SQRT:
                            res = Math.sqrt(d);
                            break;
                        case MATH_TAN:
                            res = Math.tan(d);
                            break;
                        case MATH_SIGN:
                            res = Math.signum(d);
                            break;
                        case MATH_LOG10:
                            res = Math.log10(d);
                            break;
                        default:
                            throw new AnalysisException();
                    }
                    return Value.makeNum(res);
                } else if (!num.isNotNum())
                    return Value.makeAnyNum();
                else
                    return Value.makeNone();
            }

            case MATH_ATAN2: // 15.8.2.5
            case MATH_POW: { // 15.8.2.13
                Value num1 = Conversion.toNumber(FunctionCalls.readParameter(call, state, 0), c);
                Value num2 = Conversion.toNumber(FunctionCalls.readParameter(call, state, 1), c);
                if (num1.isMaybeSingleNum() && num2.isMaybeSingleNum()) {
                    double d1 = num1.getNum();
                    double d2 = num2.getNum();
                    double res;
                    switch (nativeobject) {
                        case MATH_ATAN2:
                            res = Math.atan2(d1, d2);
                            break;
                        case MATH_POW:
                            res = Math.pow(d1, d2);
                            break;
                        default:
                            throw new AnalysisException();
                    }
                    return Value.makeNum(res);
                } else if (!num1.isNotNum() && !num2.isNotNum())
                    return Value.makeAnyNum();
                else
                    return Value.makeNone();
            }

            case MATH_MAX: { // 15.8.2.11
                double res = Double.NEGATIVE_INFINITY;
                if (call.isUnknownNumberOfArgs()) {
                    Value num = Conversion.toNumber(FunctionCalls.readUnknownParameter(call), c);
                    if (num.isMaybeSingleNum())
                        res = num.getNum();
                    else if (!num.isNotNum())
                        return Value.makeAnyNum();
                    else
                        return Value.makeNone();
                } else
                    for (int i = 0; i < call.getNumberOfArgs(); i++) {
                        Value num = Conversion.toNumber(FunctionCalls.readParameter(call, state, i), c);
                        if (num.isMaybeSingleNum())
                            res = Math.max(res, num.getNum());
                        else if (!num.isNotNum())
                            return Value.makeAnyNum();
                        else
                            return Value.makeNone();
                    }
                return Value.makeNum(res);
            }

            case MATH_MIN: { // 15.8.2.12
                double res = Double.POSITIVE_INFINITY;
                if (call.isUnknownNumberOfArgs()) {
                    Value num = Conversion.toNumber(FunctionCalls.readUnknownParameter(call), c);
                    if (num.isMaybeSingleNum())
                        res = num.getNum();
                    else if (!num.isNotNum())
                        return Value.makeAnyNum();
                    else
                        return Value.makeNone();
                } else
                    for (int i = 0; i < call.getNumberOfArgs(); i++) {
                        Value num = Conversion.toNumber(FunctionCalls.readParameter(call, state, i), c);
                        if (num.isMaybeSingleNum())
                            res = Math.min(res, num.getNum());
                        else if (!num.isNotNum())
                            return Value.makeAnyNum();
                        else
                            return Value.makeNone();
                    }
                return Value.makeNum(res);
            }

            case MATH_RANDOM: { // 15.8.2.14
                if (c.getAnalysis().getUnsoundness().mayUseFixedMathRandom(c.getNode()))
                    return Value.makeNum(new Random(c.getNode().getIndex()).nextDouble()); // pick a pseudo-random value deterministically from the node index
                else
                    return Value.makeAnyNumNotNaNInf();
            }

            case MATH_IMUL: {
                return Value.makeAnyNumUInt();
            }

            case MATH_TRUNC:
            case MATH_TANH:
            case MATH_ASINH:
            case MATH_ACOSH:
            case MATH_ATANH:
            case MATH_HYPOT:
            case MATH_FROUND:
            case MATH_CLZ32:
            case MATH_CBRT:
            case MATH_SINH:
            case MATH_COSH:
            case MATH_LOG2:
            case MATH_LOG1P:
            case MATH_EXPM1: {
                return Value.makeAnyNum();
            }

            default:
                return null;
        }
    }
}
