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

import dk.brics.tajs.analysis.Context;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.EvalCache;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.uneval.NormalForm;
import dk.brics.tajs.analysis.uneval.UnevalTools;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.FlowGraphFragment;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
import dk.brics.tajs.js2flowgraph.FlowGraphMutator;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.unevalizer.Unevalizer;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Set;

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
    public static Value evaluate(ECMAScriptObjects nativeobject, final CallInfo call, final State state, final Solver.SolverInterface c) {
        if (nativeobject != ECMAScriptObjects.FUNCTION && nativeobject != ECMAScriptObjects.FUNCTION_PROTOTYPE)
            if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
                return Value.makeNone();

        switch (nativeobject) {

            case FUNCTION: { // 15.3.1 / 15.3.2 (no difference between function and constructor)
                if (c.isScanning())
                    return Value.makeNone();

                if (Options.get().isUnevalizerEnabled()) {
                    FlowGraph currentFg = c.getFlowGraph();

                    //First parse the argument string
                    if (call.isUnknownNumberOfArgs())
                        throw new AnalysisException("Unable to handle unknown args to Function"); // FIXME: uneval, unknown arguments

                    CallNode callNode = (CallNode) call.getSourceNode();

                    int nrArgs = call.getNumberOfArgs();

                    String stringArgs = "";
                    if (nrArgs > 1) { // if only one arg: no parameters!
                        for (int i = 0; i < nrArgs - 1; i++) {
                            if (!stringArgs.isEmpty()) {
                                stringArgs += ",";
                            }
                            Value v = Conversion.toString(call.getArg(i), c);
                            if (v.getStr() == null)
                                throw new AnalysisException("Unable to handle unknown arguments to Function"); // FIXME: uneval, unknown arguments
                            stringArgs += v.getStr();
                        }
                    }

                    final Value vBody;
                    if (nrArgs > 0) {
                        vBody = Conversion.toString(call.getArg(nrArgs - 1), c);
                    } else {
                        vBody = Value.makeStr("");
                    }

                    String body = Strings.escapeSource(vBody.getStr());
                    if (body == null)
                        throw new AnalysisException("Unable to handle non-constant code in Function at " + callNode.getBlock().getSourceLocation()); // FIXME: uneval, unknown arguments

                    String var = callNode.getResultRegister() == AbstractNode.NO_VALUE ? null : UnevalTools.gensym();
                    String complete_function = (var == null ? "\"" : "\"" + var + " = ") + "(function (" + stringArgs + ") {" + body + "})\"";
                    if (nrArgs == 0) {
                        // UnevalTools.rebuildNormalForm will crash due to missing argument-register ...
                        throw new AnalysisException("Unevalizer can not handle `new Function()` at " + callNode.getBlock().getSourceLocation() + "..."); // See GitHub #147
                    }
                    NormalForm input = UnevalTools.rebuildNormalForm(currentFg, callNode, state, c);
                    String unevaled = new Unevalizer().uneval(UnevalTools.unevalizerCallback(currentFg, state, callNode, input), complete_function, false, null);
                    String unevaledSubst = var == null ? unevaled : unevaled.replace(var, UnevalTools.VAR_PLACEHOLDER); // to avoid the random string in the cache

                    if (unevaled == null)
                        throw new AnalysisException("Unevalable eval: " + UnevalTools.rebuildFullExpression(currentFg, callNode, callNode.getArgRegister(0)));
                    if (log.isDebugEnabled())
                        log.debug("Unevalized: " + unevaled);

                    EvalCache evalCache = c.getAnalysis().getEvalCache();
                    NodeAndContext<Context> cc = new NodeAndContext<>(callNode, state.getContext());
                    FlowGraphFragment e = evalCache.getCode(cc);

                    if (e == null || !e.getKey().equals(unevaledSubst)) {
                        e = FlowGraphMutator.extendFlowGraph(currentFg, unevaled, unevaledSubst, e, callNode, false, var);
                    }

                    evalCache.setCode(cc, e);
                    c.propagateToBasicBlock(state.clone(), e.getEntryBlock(), state.getContext());
                    return Value.makeNone();
                }
                throw new AnalysisException("Don't know how to handle call to 'Function' - unevalizer isn't enabled");
            }

            case FUNCTION_PROTOTYPE: { // 15.3.4
                return Value.makeUndef();
            }

            case FUNCTION_TOSTRING: { // 15.3.4.2
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.FUNCTION))
                    return Value.makeNone();
                return Value.makeAnyStr();
            }

            case FUNCTION_APPLY: { // 15.3.4.3
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 2);
                Value argarray = NativeFunctions.readParameter(call, state, 1);
                final boolean maybe_empty = argarray.isMaybeNull() || argarray.isMaybeUndef();
                boolean maybe_typeerror = !argarray.isNotBool() || !argarray.isNotNum() || !argarray.isNotStr();
                boolean maybe_ok = false;
                boolean unknown_length = false;
                int fixed_length = -1;
                if (maybe_empty) {
                    fixed_length = 0;
                    maybe_ok = true;
                }
                final Set<ObjectLabel> argarrays = newSet();
                for (ObjectLabel objlabel : argarray.getObjectLabels())
                    if (objlabel.getKind() == Kind.ARRAY || objlabel.getKind() == Kind.ARGUMENTS) {
                        argarrays.add(objlabel);
                        Value lengthval = state.readPropertyValue(Collections.singleton(objlabel), "length");
                        lengthval = UnknownValueResolver.getRealValue(lengthval, state);
                        if (lengthval.isMaybeSingleNum()) {
                            int len = lengthval.getNum().intValue();
                            if (fixed_length == -1)
                                fixed_length = len;
                            else if (len != fixed_length)
                                unknown_length = true;
                        } else
                            unknown_length = true;
                        maybe_ok = true;
                    } else
                        maybe_typeerror = true;
                if (maybe_typeerror) {
                    Exceptions.throwTypeError(state, c);
                    c.getMonitoring().addMessage(c.getCurrentNode(),
                            Severity.HIGH, "TypeError, invalid arguments to 'apply'");
                }
                if (!maybe_ok)
                    return Value.makeNone();
                final boolean unknown_length__final = unknown_length;
                final int fixed_length__final = fixed_length;
                FunctionCalls.callFunction(new CallInfo() { // TODO: possible infinite recursion of callFunction with apply/call? (see test109.js)

                    @Override
                    public AbstractNode getSourceNode() {
                        return call.getSourceNode();
                    }

                    @Override
                    public Node getJSSourceNode() {
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
                        if (unknown_length__final)
                            return getUnknownArg();
                        else if (i < fixed_length__final) {
                            Value v = state.readPropertyValue(argarrays, Integer.toString(i));
                            if (maybe_empty)
                                v = v.joinUndef();
                            return v;
                        } else
                            return Value.makeUndef();
                    }

                    @Override
                    public int getNumberOfArgs() {
                        if (unknown_length__final)
                            return -1;
                        else
                            return fixed_length__final;
                    }

                    @Override
                    public Value getUnknownArg() {
                        return state.readPropertyValue(argarrays, Value.makeAnyStrUInt());
                    }

                    @Override
                    public boolean isUnknownNumberOfArgs() {
                        return unknown_length__final;
                    }

                    @Override
                    public int getResultRegister() {
                        return call.getResultRegister();
                    }

                    @Override
                    public ExecutionContext getExecutionContext() {
                        return call.getExecutionContext();
                    }
                }, state, c);
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
                    public Node getJSSourceNode() {
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
                }, state, c);
                return Value.makeNone(); // no direct flow to the successor
            }

            default:
                return null;
        }
    }

    private static Set<ObjectLabel> prepareThis(CallInfo call, State callee_state, Solver.SolverInterface c) {
        Value thisval = NativeFunctions.readParameter(call, callee_state, 0);
        // 15.3.4.3/4
        boolean maybe_null_or_undef = thisval.isMaybeNull() || thisval.isMaybeUndef();
        thisval = thisval.restrictToNotNullNotUndef();
        Set<ObjectLabel> this_objs = newSet(Conversion.toObjectLabels(callee_state, call.getSourceNode(), thisval, c)); // TODO: disable messages? (but not side-effects!)
        if (maybe_null_or_undef)
            this_objs.add(InitialStateBuilder.GLOBAL);
        return this_objs;
    }
}
