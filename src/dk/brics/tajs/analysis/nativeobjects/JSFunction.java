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
import dk.brics.tajs.analysis.ParallelTransfer;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.unevalizer.SimpleUnevalizerAPI;
import dk.brics.tajs.unevalizer.UnevalizerLimitations;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Strings;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * 15.3 native Function functions.
 */
public class JSFunction {

    private static Logger log = Logger.getLogger(JSFunction.class);

    private JSFunction() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, final CallInfo call, final Solver.SolverInterface c) {
        State state = c.getState();
        switch (nativeobject) {

            case FUNCTION: { // 15.3.1 / 15.3.2 (no difference between function and constructor)
                if (c.isScanning())
                    return Value.makeNone();

                //First parse the argument string
                if (call.isUnknownNumberOfArgs()) {
                    return UnevalizerLimitations.handle("Unable to handle unknown args to Function", call.getSourceNode(), c);
                }

                if (!(call.getSourceNode() instanceof CallNode)) {
                    if (c.getAnalysis().getUnsoundness().mayIgnoreEvalCallAtNonCallNode(call.getSourceNode())) {
                        return Value.makeUndef();
                    }
                    throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getSourceNode().getSourceLocation() + ": Invoking Function from non-CallNode - unevalizer can't handle that"); // TODO: generalize unevalizer to handle calls from EventDispatcherNode and implicit calls?
                }
                CallNode callNode = (CallNode) call.getSourceNode();
                int nrArgs = call.getNumberOfArgs();

                List<Value> vParameterNames = newList();
                if (nrArgs > 1) { // if only one arg: no parameters!
                    for (int i = 0; i < nrArgs - 1; i++) {
                        Value parameterName = Conversion.toString(FunctionCalls.readParameter(call, state, i), c);
                        vParameterNames.add(parameterName);
                    }
                }

                Value vBody;
                if (nrArgs > 0) {
                    vBody = Conversion.toString(FunctionCalls.readParameter(call, state, nrArgs - 1), c);
                } else {
                    vBody = Value.makeStr("");
                }

                Set<Value> toStringedArguments = newSet();
                toStringedArguments.add(vBody);
                toStringedArguments.addAll(vParameterNames);
                if (toStringedArguments.stream().anyMatch(Value::isNone)) {
                    return Value.makeNone();
                }
                if (toStringedArguments.stream().anyMatch(Value::isMaybeFuzzyStr)) {
                    if (c.getAnalysis().getUnsoundness().maySimplifyImpreciseFunctionConstructor(callNode)) {
                        vParameterNames.clear();
                        vBody = Value.makeStr("");
                    } else {
                        throw new AnalysisLimitationException.AnalysisPrecisionLimitationException(call.getJSSourceNode().getSourceLocation() + ": Too imprecise calls to Function");
                    }
                }

                // TODO: no escaping of parameters?
                String body = Strings.escapeSource(vBody.getStr());
                List<String> parameterNames = vParameterNames.stream()
                        .flatMap(v -> Arrays.stream(v.getStr().split(",")))
                        .map(String::trim)
                        .collect(Collectors.toList());

                return SimpleUnevalizerAPI.evaluateFunctionCall(call.getSourceNode(), parameterNames, body, c);
            }

            case FUNCTION_PROTOTYPE: { // 15.3.4
                return Value.makeUndef();
            }

            case FUNCTION_TOSTRING: { // 15.3.4.2
                return evaluateToString(state.readThis(), c);
            }

            case FUNCTION_APPLY: { // 15.3.4.3
                final PropVarOperations pv = c.getAnalysis().getPropVarOperations();
                Value argarray = FunctionCalls.readParameter(call, state, 1);

                // handle bad arguments
                boolean hasBadPrimitives = !argarray.restrictToNotObject().restrictToNotNull().restrictToNotUndef().isNone();
                boolean hasOnlyBadPrimitives = !argarray.isMaybeObject() && !argarray.isMaybeNull() && !argarray.isMaybeUndef();
                if (hasBadPrimitives) {
                    Exceptions.throwTypeError(c);
                    c.getMonitoring().addMessage(c.getNode(), Severity.HIGH, "TypeError, invalid arguments to 'apply'");
                    if (hasOnlyBadPrimitives) {
                        return Value.makeNone();
                    }
                }

                // group the array-like arguments by their lengths. This will reduce the number of calls with an unknown number of arguments.
                Map<Value, List<ObjectLabel>> lengths = argarray.getObjectLabels().stream().collect(Collectors.groupingBy(l -> getLengthAsArrayIndex(l, c)));
                if (lengths.containsKey(Value.makeNone())) {
                    return Value.makeNone(); // waiting for coercion to finish (of the length property)
                }
                // special case: null and undefined count as an empty array
                boolean maybeEmpty = argarray.isNullOrUndef();
                if (maybeEmpty) {
                    Value key = Value.makeNum(0);
                    if (!lengths.containsKey(key)) {
                        lengths.put(key, newList());
                    }
                }

                Value functionValue = state.readThis();
                ParallelTransfer.process(lengths.entrySet(), entry -> {
                    Value lengthValue = entry.getKey();
                    List<ObjectLabel> argumentObjectsForLength = entry.getValue();
                    c.getMonitoring().visitPropertyRead(call.getSourceNode(), newSet(argumentObjectsForLength), lengthValue, state, false);
                    FunctionCalls.callFunction(new CallInfo() { // TODO: possible infinite recursion of callFunction with apply/call? (see test109.js)

                        @Override
                        public AbstractNode getSourceNode() {
                            return call.getSourceNode();
                        }

                        @Override
                        public AbstractNode getJSSourceNode() {
                            return call.getJSSourceNode();
                        }

                        @Override
                        public boolean isConstructorCall() {
                            return false;
                        }

                        @Override
                        public Value getFunctionValue() {
                            return functionValue;
                        }

                        @Override
                        public Value getThis() {
                            return FunctionCalls.readParameter(call, c.getState(), 0);
                        }

                        @Override
                        public Value getArg(int i) {
                            if (!isUnknownNumberOfArgs() && lengthValue.getNum() <= i) {
                                return Value.makeAbsent(); // asking out of bounds
                            }
                            Value result = c.withState(state, () -> pv.readPropertyValue(argumentObjectsForLength, Integer.toString(i)));
                            if (maybeEmpty && lengthValue.getNum() == 0) {
                                result = result.joinAbsent(); // special case with null and undef acting as an empty array
                            }
                            return result;
                        }

                        @Override
                        public int getNumberOfArgs() {
                            if (isUnknownNumberOfArgs()) {
                                throw new AnalysisException("Number of arguments is unknown!");
                            }
                            return lengthValue.getNum().intValue(); // coercions have made this safe
                        }

                        @Override
                        public Value getUnknownArg() {
                            return c.withState(state, () -> pv.readPropertyValue(argumentObjectsForLength, Value.makeAnyStrUInt()));
                        }

                        @Override
                        public boolean isUnknownNumberOfArgs() {
                            return !lengthValue.isMaybeSingleNum();
                        }

                        @Override
                        public int getResultRegister() {
                            return call.getResultRegister();
                        }

                        @Override
                        public ExecutionContext getExecutionContext() {
                            return call.getExecutionContext();
                        }
                    }, c);
                }, c);
                return Value.makeNone();
            }

            case FUNCTION_CALL: { // 15.3.4.4
                FunctionCalls.callFunction(new CallInfo() {

                    @Override
                    public AbstractNode getSourceNode() {
                        return call.getSourceNode();
                    }

                    @Override
                    public AbstractNode getJSSourceNode() {
                        return call.getJSSourceNode();
                    }

                    @Override
                    public boolean isConstructorCall() {
                        return false;
                    }

                    @Override
                    public Value getFunctionValue() {
                        return state.readThis();
                    }

                    @Override
                    public Value getThis() {
                        return FunctionCalls.readParameter(call, c.getState(), 0);
                    }

                    @Override
                    public Value getArg(int i) {
                        return call.getArg(i + 1);
                    }

                    @Override
                    public int getNumberOfArgs() {
                        int n = call.getNumberOfArgs();
                        return n > 0 ? n - 1 : 0;
                    }

                    @Override
                    public Value getUnknownArg() {
                        return call.getUnknownArg();
                    }

                    @Override
                    public boolean isUnknownNumberOfArgs() {
                        return call.isUnknownNumberOfArgs();
                    }

                    @Override
                    public int getResultRegister() {
                        return call.getResultRegister();
                    }

                    @Override
                    public ExecutionContext getExecutionContext() {
                        return call.getExecutionContext();
                    }
                }, c);
                return Value.makeNone(); // no direct flow to the successor
            }

            default:
                return null;
        }
    }

    private static Value getLengthAsArrayIndex(ObjectLabel l, Solver.SolverInterface c) {
        Value v = c.getAnalysis().getPropVarOperations().readPropertyValue(Collections.singleton(l), "length");
        v = UnknownValueResolver.getRealValue(v, c.getState());
        Value n = Conversion.toNumber(v, c);
        if (n.isMaybeFuzzyNum() || n.isNone()) {
            return n;
        }
        if (n.isMaybeSingleNum()) {
            return Value.makeNum(Conversion.toInt32(n.getNum()));
        }
        if (n.isMaybeNaN() || n.isMaybeInf()) {
            return Value.makeNum(0);
        }
        throw new AnalysisException("Unhandled coerced-number case: " + n);
    }

    public static Value evaluateToString(Value thisval, Solver.SolverInterface c) {
        List<Value> strs = newList();
        boolean is_maybe_typeerror = thisval.isMaybePrimitive();
        for (ObjectLabel thisObj : thisval.getObjectLabels()) {
            if (thisObj.getKind() != Kind.FUNCTION) {
                is_maybe_typeerror = true;
            } else {
                Optional<String> toString = c.getAnalysis().getUnsoundness().evaluate_FunctionToString(c.getNode(), thisObj);
                if (toString.isPresent()) {
                    strs.add(Value.makeStr(toString.get()));
                } else {
                    strs.add(Value.makeAnyStr());
                }
            }
        }
        if (is_maybe_typeerror) {
            Exceptions.throwTypeError(c);
        }
        return Value.join(strs);
    }
}
