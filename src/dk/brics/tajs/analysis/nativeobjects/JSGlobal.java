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

import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.unevalizer.UnevalizerAPI;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.apache.log4j.Logger;

/**
 * 15.1 and B.2 native global functions.
 */
public class JSGlobal {

    private static Logger log = Logger.getLogger(JSGlobal.class);

    private JSGlobal() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, Solver.SolverInterface c) {
        State state = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        switch (nativeobject) {

            case EVAL: { // 15.1.2.1
                Value arg = FunctionCalls.readParameter(call, state, 0);
                Value evalString = arg.restrictToStr();
                Value evaled = evalString.isNone()? Value.makeNone(): evaluateEvalStringArgument(evalString, call, state, c);
                return evaled.join(arg.restrictToNotStr());
            }
            case PARSEINT: { // 15.1.2.2
                return TAJSConcreteSemantics.convertTAJSCall(Value.makeUndef(), nativeobject.toString(), 2, call, c, () -> Value.makeAnyNumUInt().joinNumNaN());
            }

            case PARSEFLOAT: { // 15.1.2.3
                return TAJSConcreteSemantics.convertTAJSCall(Value.makeUndef(), nativeobject.toString(), 1, call, c, Value::makeAnyNum);
            }

            case ISNAN: { // 15.1.2.4
                Value num = Conversion.toNumber(FunctionCalls.readParameter(call, state, 0), c);
                Value res = Value.makeNone();
                if (num.isMaybeNaN())
                    res = res.joinBool(true);
                if (num.isMaybeSingleNum() || num.isMaybeInf() || num.isMaybeNumUInt() || num.isMaybeNumOther())
                    res = res.joinBool(false);
                return res;
            }

            case ISFINITE: { // 15.1.2.5
                Value num = Conversion.toNumber(FunctionCalls.readParameter(call, state, 0), c);
                if (num.isMaybeSingleNum())
                    return Value.makeBool(!num.getNum().isInfinite());
                Value res = Value.makeNone();
                if (num.isMaybeNaN() || num.isMaybeInf())
                    res = res.joinBool(false);
                if (num.isMaybeNumUInt() || num.isMaybeNumOther())
                    res = res.joinBool(true);
                return res;
            }

            case PRINT:  // in Rhino, expects any number of parameters; returns undefined
            case ALERT: {
                return Value.makeUndef();
            }

            case DECODEURI: // 15.1.3.1
            case DECODEURICOMPONENT: // 15.1.3.2
            case ENCODEURI: // 15.1.3.3
            case ENCODEURICOMPONENT: // 15.1.3.4
            case ESCAPE: // B.2.1
            case UNESCAPE: { // B.2.2
                return TAJSConcreteSemantics.convertTAJSCall(Value.makeUndef(), nativeobject.toString(), 1, call, c, Value::makeAnyStr);
            }

            default:
                return null;
        }
    }

    private static Value evaluateEvalStringArgument(Value evalString, CallInfo call, State state, GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, Analysis>.SolverInterface c) {
        if (Options.get().isEvalStatistics())
            c.getMonitoring().visitEvalCall(call.getSourceNode(), FunctionCalls.readParameter(call, state, 0));
        if (evalString.isStrJSON()) {
            return JSJson.makeAnyJSONObject(c).join(evalString.restrictToNotStr());
        }
        if (c.isScanning())
            return Value.makeNone();
        if (!(call.getSourceNode() instanceof CallNode)) {
            if (c.getAnalysis().getUnsoundness().mayIgnoreEvalCallAtNonCallNode(call.getSourceNode())) {
                return Value.makeUndef();
            }
            throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getSourceNode().getSourceLocation() + ": Invoking eval from non-CallNode - unevalizer can't handle that"); // TODO: 'eval' invoked via implicit toString/valueOf coercion? in that case, just return 'undefined'
        }
        CallNode callNode = (CallNode) call.getSourceNode();
        if (Options.get().isUnevalizerEnabled()) {
            return UnevalizerAPI.evaluateEvalCall(call, c, state, callNode);
        } else {
            throw new AnalysisLimitationException.AnalysisPrecisionLimitationException(call.getJSSourceNode().getSourceLocation() + ": eval of non JSONStr not supported, and unevalizer is not enabled");
        }
    }
}
