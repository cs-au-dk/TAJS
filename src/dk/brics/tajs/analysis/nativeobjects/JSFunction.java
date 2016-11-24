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
import dk.brics.tajs.analysis.EvalCache;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.ParallelTransfer;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.analysis.uneval.NormalForm;
import dk.brics.tajs.analysis.uneval.UnevalTools;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.FlowGraphFragment;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.js2flowgraph.FlowGraphMutator;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.unevalizer.Unevalizer;
import dk.brics.tajs.unevalizer.UnevalizerLimitations;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;
import dk.brics.tajs.util.Strings;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (nativeobject != ECMAScriptObjects.FUNCTION && nativeobject != ECMAScriptObjects.FUNCTION_PROTOTYPE)
            if (NativeFunctions.throwTypeErrorIfConstructor(call, c))
                return Value.makeNone();

        State state = c.getState();
        switch (nativeobject) {

            case FUNCTION: { // 15.3.1 / 15.3.2 (no difference between function and constructor)
                if (c.isScanning())
                    return Value.makeNone();

                if (Options.get().isUnevalizerEnabled()) {
                    FlowGraph currentFg = c.getFlowGraph();

                    //First parse the argument string
                    if (call.isUnknownNumberOfArgs()) {
                        return UnevalizerLimitations.handle("Unable to handle unknown args to Function", call.getSourceNode(), c);
                    }

                    if (call.getSourceNode() instanceof CallNode) {
                        CallNode callNode = (CallNode) call.getSourceNode();

                        int nrArgs = call.getNumberOfArgs();

                        String stringArgs = "";
                        boolean tooImprecise = false;
                        if (nrArgs > 1) { // if only one arg: no parameters!
                            for (int i = 0; i < nrArgs - 1; i++) {
                                if (!stringArgs.isEmpty()) {
                                    stringArgs += ",";
                                }
                                Value arg = UnknownValueResolver.getRealValue(call.getArg(i), state);
                                if (arg.isNotStr()) {
                                    tooImprecise |= true; // very likely a spurious call/argument, might as well give up now
                                }
                                Value v = Conversion.toString(arg, c);
                                if (v.getStr() == null) {
                                    tooImprecise |= true;
                                } else {
                                    stringArgs += v.getStr();
                                }
                            }
                        }

                        final Value vBody;
                        if (nrArgs > 0) {
                            Value arg = UnknownValueResolver.getRealValue(call.getArg(nrArgs - 1), state);
                            if (arg.isNotStr()) {
                                tooImprecise |= true; // very likely a spurious call/argument, might as well give up now
                            }
                            vBody = Conversion.toString(arg, c);
                        } else {
                            vBody = Value.makeStr("");
                        }

                        String body = Strings.escapeSource(vBody.getStr());
                        if (body == null)
                            tooImprecise |= true;

                        if (tooImprecise) {
                            if (Options.get().isUnsoundEnabled()) {
                                stringArgs = "";
                                body = "";
                            } else {
                                throw new AnalysisLimitationException.AnalysisPrecisionLimitationException(call.getJSSourceNode().getSourceLocation() + ": Too imprecise calls to Function");
                            }
                        }

                        String var = call.getResultRegister() == AbstractNode.NO_VALUE ? null : UnevalTools.gensym();
                        String complete_function = (var == null ? "\"" : "\"" + var + " = ") + "(function (" + stringArgs + ") {" + body + "})\"";

                        NormalForm input = UnevalTools.rebuildNormalForm(currentFg, callNode, state, c);
                        String unevaled = new Unevalizer().uneval(UnevalTools.unevalizerCallback(currentFg, c, callNode, input, false), complete_function, false, null, call.getSourceNode(), c);

                        if (unevaled == null)
                            return UnevalizerLimitations.handle("Unevalable eval: " + UnevalTools.rebuildFullExpression(currentFg, callNode, callNode.getArgRegister(0)), call.getSourceNode(), c);

                        String unevaledSubst = var == null ? unevaled : unevaled.replace(var, UnevalTools.VAR_PLACEHOLDER); // to avoid the random string in the cache

                        if (log.isDebugEnabled())
                            log.debug("Unevalized: " + unevaled);

                        EvalCache evalCache = c.getAnalysis().getEvalCache();
                        NodeAndContext<Context> cc = new NodeAndContext<>(call.getSourceNode(), state.getContext());
                        FlowGraphFragment e = evalCache.getCode(cc);

                        if (e == null || !e.getKey().equals(unevaledSubst)) {
                            e = FlowGraphMutator.extendFlowGraph(currentFg, unevaled, unevaledSubst, e, callNode, false, var);
                        }

                        evalCache.setCode(cc, e);
                        c.propagateToBasicBlock(state.clone(), e.getEntryBlock(), state.getContext());
                        return Value.makeNone();
                    } else {
                        if (Options.get().isUnsoundEnabled() && call.getSourceNode() instanceof EventDispatcherNode) {
                            return Value.makeUndef();
                        }
                        throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getSourceNode().getSourceLocation() + ": Invoking Function from non-CallNode - unevalizer can't handle that"); // TODO: generalize unevalizer to handle calls from EventDispatcherNode and implicit calls?
                    }
                }
                throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getJSSourceNode().getSourceLocation() + ": Don't know how to handle call to 'Function' - unevalizer isn't enabled");
            }

            case FUNCTION_PROTOTYPE: { // 15.3.4
                return Value.makeUndef();
            }

            case FUNCTION_TOSTRING: { // 15.3.4.2
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                return state.readThisObjectsCoerced((l) -> evaluateToString(l, c));
            }

            case FUNCTION_APPLY: { // 15.3.4.3
                final PropVarOperations pv = c.getAnalysis().getPropVarOperations();
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 2);
                Value argarray = NativeFunctions.readParameter(call, state, 1);

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
                        public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
                            return JSFunction.prepareThis(call, callee_state, c);
                        }

                        @Override
                        public Value getArg(int i) {
                            if (!isUnknownNumberOfArgs() && lengthValue.getNum() <= i) {
                                return Value.makeUndef(); // asking out of bounds
                            }
                            Value result = c.withState(state, () -> pv.readPropertyValue(argumentObjectsForLength, Integer.toString(i)));
                            if (maybeEmpty && lengthValue.getNum() == 0) {
                                result = result.joinUndef(); // special case with null and undef acting as an empty array
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
                NativeFunctions.expectParameters(nativeobject, call, c, 1, -1);
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
                    public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
                        return JSFunction.prepareThis(call, callee_state, c);
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

    private static Set<ObjectLabel> prepareThis(CallInfo call, State callee_state, Solver.SolverInterface c) {
        Value thisval = NativeFunctions.readParameter(call, callee_state, 0);
        // 15.3.4.3/4
        boolean maybe_null_or_undef = thisval.isMaybeNull() || thisval.isMaybeUndef();
        Value thisvalFinal = thisval.restrictToNotNullNotUndef();
        Set<ObjectLabel> this_objs = c.withState(callee_state, () -> newSet(Conversion.toObjectLabels(call.getSourceNode(), thisvalFinal, c))); // TODO: disable messages? (but not side-effects!)
        if (maybe_null_or_undef)
            this_objs.add(InitialStateBuilder.GLOBAL);
        return this_objs;
    }

    public static Value evaluateToString(ObjectLabel thiss, Solver.SolverInterface c) {
        // 15.3.4.2 Function.prototype.toString ( )
        // An implementation-dependent representation of the function is returned.
        if (thiss.getKind() != Kind.FUNCTION) {
            Exceptions.throwTypeError(c);
            return Value.makeNone();
        }
        if (Options.get().isUnsoundEnabled()) {
            return TAJSConcreteSemantics.convertFunctionToString(thiss);
        }
        return Value.makeAnyStr();
    }
}
