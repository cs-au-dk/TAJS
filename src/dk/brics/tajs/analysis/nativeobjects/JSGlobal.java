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
import dk.brics.tajs.analysis.Conversion.Hint;
import dk.brics.tajs.analysis.EvalCache;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.ajax.ReadystateEvent;
import dk.brics.tajs.analysis.dom.event.EventListener;
import dk.brics.tajs.analysis.dom.event.KeyboardEvent;
import dk.brics.tajs.analysis.dom.event.MouseEvent;
import dk.brics.tajs.analysis.dom.event.UIEvent;
import dk.brics.tajs.analysis.dom.event.WheelEvent;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteNumber;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteString;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.analysis.uneval.NormalForm;
import dk.brics.tajs.analysis.uneval.UnevalTools;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.FlowGraphFragment;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.js2flowgraph.FlowGraphMutator;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.unevalizer.Unevalizer;
import dk.brics.tajs.unevalizer.UnevalizerLimitations;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Pair;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * 15.1 and B.2 native global functions.
 */
public class JSGlobal {

    private static Logger log = Logger.getLogger(JSGlobal.class);

    private static final Set<ECMAScriptObjects> TAJS_HOOK_FUNCTIONS = newSet();

    static {
        TAJS_HOOK_FUNCTIONS.addAll(Arrays.asList(ECMAScriptObjects.TAJS_DUMPVALUE, ECMAScriptObjects.TAJS_DUMPPROTOTYPE, ECMAScriptObjects.TAJS_DUMPOBJECT, ECMAScriptObjects.TAJS_DUMPSTATE, ECMAScriptObjects.TAJS_DUMPMODIFIEDSTATE, ECMAScriptObjects.TAJS_DUMPATTRIBUTES, ECMAScriptObjects.TAJS_DUMPEXPRESSION, ECMAScriptObjects.TAJS_DUMPNF, ECMAScriptObjects.TAJS_ASSERT, ECMAScriptObjects.TAJS_CONVERSION_TO_PRIMITIVE, ECMAScriptObjects.TAJS_ADD_CONTEXT_SENSITIVITY, ECMAScriptObjects.TAJS_NEW_OBJECT));
    }

    public static Value removeTAJSSpecificFunctions(Value v) {
        if (v.isPolymorphicOrUnknown())
            return v;
        Set<ObjectLabel> objs = newSet();
        for (ObjectLabel label : v.getObjectLabels()) {
            //noinspection SuspiciousMethodCalls
            if (label.isHostObject() && TAJS_HOOK_FUNCTIONS.contains(label.getHostObject())) {
                objs.add(label);
            }
        }
        return v.removeObjects(objs);
    }

    private JSGlobal() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, final State state, Solver.SolverInterface c) {
        if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
            return Value.makeNone();

        switch (nativeobject) {

            case EVAL: { // 15.1.2.1
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value evalValue = NativeFunctions.readParameter(call, state, 0);
                if (evalValue.isMaybeOtherThanStr()) {
                    if (!evalValue.isNotStr())
                        return UnevalizerLimitations.handle("Parameter to eval is maybe a string and maybe a non-string, we currently can't handle that, sorry", call.getSourceNode(), c); // TODO: improve?
                    // TODO: issue warning if calling eval with non-string value
                    return evalValue;
                }
                if (Options.get().isEvalStatistics())
                    c.getMonitoring().visitEvalCall(call.getSourceNode(), NativeFunctions.readParameter(call, state, 0));
                if (c.isScanning())
                    return Value.makeNone();
                if (evalValue.isStrJSON()) {
                    return DOMFunctions.makeAnyJSONObject(state).join(evalValue.restrictToNotStr());
                } else if (Options.get().isUnevalizerEnabled()) {
                    CallNode evalCall = (CallNode) call.getSourceNode();
                    FlowGraph currentFg = c.getFlowGraph();
                    boolean ignoreResult = evalCall.getResultRegister() == AbstractNode.NO_VALUE;
                    String var = ignoreResult ? null : UnevalTools.gensym(); // Do we need the value of the eval call after?
                    NormalForm input = UnevalTools.rebuildNormalForm(currentFg, evalCall, state, c);

                    // Collect special args that should be analyzed context sensitively

                    Function f = evalCall.getBlock().getFunction();
                    Set<String> importantParameters = input.getArgumentsInUse().stream().filter(arg -> f.getParameterNames().contains(arg)).collect(Collectors.toSet());
                    addContextSensitivity(f, importantParameters, state, c);

                    // What we should use as key for the eval cache is the entire tuple from the Uneval paper. Since that
                    // might contain infinite sets and other large things we just call the Unevalizer and compare the output
                    // of the Unevalizer to the key in the cache. This makes us Uneval more things, but we save the work
                    // of re-extending the flow graph every time.
                    boolean aliased_call = !"eval".equals(UnevalTools.get_call_name(currentFg, evalCall)); // TODO: aliased_call should also affect the execution context?
                    String unevaled = new Unevalizer().uneval(UnevalTools.unevalizerCallback(currentFg, state, evalCall, input), input.getNormalForm(), aliased_call, var);

                    if (unevaled == null)
                        return UnevalizerLimitations.handle("Unevalable eval: " + UnevalTools.rebuildFullExpression(currentFg, evalCall, evalCall.getArgRegister(0)), call.getSourceNode(), c);
                    if (log.isDebugEnabled())
                        log.debug("Unevalized: " + unevaled);

                    unevaled = UnevalTools.rebuildFullFromMapping(currentFg, unevaled, input.getMapping(), evalCall);

                    String unevaledSubst = ignoreResult ? unevaled : unevaled.replace(var, UnevalTools.VAR_PLACEHOLDER); // to avoid the random string in the cache
                    EvalCache evalCache = c.getAnalysis().getEvalCache();
                    NodeAndContext<Context> cc = new NodeAndContext<>(evalCall, state.getContext());
                    FlowGraphFragment e = evalCache.getCode(cc);

                    // Cache miss.
                    if (e == null || !e.getKey().equals(unevaledSubst)) {
                        e = FlowGraphMutator.extendFlowGraph(currentFg, unevaled, unevaledSubst, e, evalCall, false, var);
                    }

                    evalCache.setCode(cc, e);
                    c.propagateToBasicBlock(state.clone(), e.getEntryBlock(), state.getContext());
                    if (Options.get().isFlowGraphEnabled()) {
                        try (PrintWriter pw = new PrintWriter(new File("out" + File.separator + "flowgraphs" + File.separator + "uneval-" +
                                evalCall.getIndex() + "-" + Integer.toHexString(state.getContext().hashCode()) + ".dot"))) {
                            currentFg.toDot(pw);
                            pw.flush();
                        } catch (Exception ee) {
                            throw new AnalysisException(ee);
                        }
                    }
                    return Value.makeNone();
                } else {
                    throw new AnalysisException("eval of non JSONStr not supported, and unevalizer is not enabled");
                }
            }
            case PARSEINT: { // 15.1.2.2
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
                Conversion.toString(NativeFunctions.readParameter(call, state, 0), c);
                Conversion.toString(NativeFunctions.readParameter(call, state, 1).restrictToNotUndef() /* implementation coercion of undefined -> 0 */, c);
                return TAJSConcreteSemantics.convertTAJSCall(Value.makeUndef(), nativeobject.toString(), 2, ConcreteNumber.class, state, call, c, Value.makeAnyNumUInt().joinNumNaN());
            }

            case PARSEFLOAT: { // 15.1.2.3
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, state, 0), c);
                return TAJSConcreteSemantics.convertTAJSCall(Value.makeUndef(), nativeobject.toString(), 1, ConcreteNumber.class, state, call, c, Value.makeAnyNum());
            }

            case ISNAN: { // 15.1.2.4
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value num = Conversion.toNumber(NativeFunctions.readParameter(call, state, 0), c);
                Value res = Value.makeNone();
                if (num.isMaybeNaN())
                    res = res.joinBool(true);
                if (num.isMaybeSingleNum() || num.isMaybeInf() || num.isMaybeNumUInt() || num.isMaybeNumOther())
                    res = res.joinBool(false);
                return res;
            }

            case ISFINITE: { // 15.1.2.5
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value num = Conversion.toNumber(NativeFunctions.readParameter(call, state, 0), c);
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
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, state, 0), c);
                return TAJSConcreteSemantics.convertTAJSCall(Value.makeUndef(), nativeobject.toString(), 1, ConcreteString.class, state, call, c, Value.makeAnyStr());
            }

            case TAJS_DUMPVALUE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value x = NativeFunctions.readParameter(call, state, 0); // to avoid recover: call.getArg(0);
                c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Abstract value: " + x.restrictToNotModified() /*+ " (context: " + c.getCurrentContext() + ")"*/);
                return Value.makeUndef();
            }

            case TAJS_DUMPPROTOTYPE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value x = NativeFunctions.readParameter(call, state, 0);

                StringBuilder sb = new StringBuilder();
                Value p = state.readInternalPrototype(x.getObjectLabels());
                p = UnknownValueResolver.getRealValue(p, state);
                while (p.isMaybeObject()) {
                    sb.append(p);
                    p = state.readInternalPrototype(p.getObjectLabels());
                    p = UnknownValueResolver.getRealValue(p, state);
                    if (!p.isNullOrUndef()) {
                        sb.append(" -> ");
                    }
                }

                c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Prototype: " + sb);
                return Value.makeUndef();
            }

            case TAJS_DUMPOBJECT: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value x = NativeFunctions.readParameter(call, state, 0);
                c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Abstract object: " + state.printObject(x) /*+ " (context: " + c.getCurrentContext() + ")"*/);
                return Value.makeUndef();
            }

            case TAJS_DUMPSTATE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Abstract state:\n" + state /*+ " (context: " + c.getCurrentContext() + ")"*/);
            /*
            try {
                File outdir = new File("out");
                if (!outdir.exists()) {
                    outdir.mkdir();
                }
                FileWriter fw = new FileWriter("out" + File.separator + "state.dot");
                fw.write(state.toDot());
                fw.close();            
            } catch (IOException e) {
                throw new AnalysisException(e);
            }
            */
                return Value.makeUndef();
            }

            case TAJS_DUMPMODIFIEDSTATE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Abstract state (modified parts):" /*+ " (context: " + c.getCurrentContext() + ")"*/ + state.toStringModified());
                return Value.makeUndef();
            }

            case TAJS_DUMPATTRIBUTES: {
                NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
                Value x = NativeFunctions.readParameter(call, state, 0);
                Value p = Conversion.toString(NativeFunctions.readParameter(call, state, 1), c);
                if (!p.isMaybeSingleStr())
                    c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Calling dumpAttributes with non-constant property name");
                else {
                    String propertyname = p.getStr();
                    Value v = state.readPropertyDirect(x.getObjectLabels(), propertyname);
                    c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Property attributes: " + v.printAttributes() /*+ " (context: " + c.getCurrentContext() + ")"*/);
                }
                return Value.makeUndef();
            }

            case TAJS_DUMPEXPRESSION: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                CallNode cn = (CallNode) call.getSourceNode();
                String s = UnevalTools.rebuildFullExpression(c.getFlowGraph(), call.getSourceNode(), cn.getArgRegister(0));
                c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Exp: " + s);
                return Value.makeUndef();
            }

            case TAJS_DUMPNF: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                CallNode cn = (CallNode) call.getSourceNode();
                NormalForm s = UnevalTools.rebuildNormalForm(c.getFlowGraph(), cn, state, c);
                c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "NF: " + s);
                return Value.makeUndef();
            }

            case TAJS_CONVERSION_TO_PRIMITIVE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
                Value varg = NativeFunctions.readParameter(call, state, 0);
                Value vhint;
                if (call.isUnknownNumberOfArgs())
                    vhint = NativeFunctions.readParameter(call, state, 1).joinStr("NONE");
                else
                    vhint = call.getNumberOfArgs() >= 2 ? NativeFunctions.readParameter(call, state, 1) : Value.makeStr("NONE");

                if (!vhint.isMaybeSingleStr()) {
                    c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Calling conversionToPrimitive with non-constant hint string");
                    return Value.makeUndef();
                } else {
                    String shint = vhint.getStr();
                    return Conversion.toPrimitive(varg, shint.equals("NONE") ? Hint.NONE : shint.equals("NUM") ? Hint.NUM : Hint.STR, c);
                }
            }

            case TAJS_ADD_CONTEXT_SENSITIVITY: {
                if (call.isUnknownNumberOfArgs())
                    throw new AnalysisException("Calling TAJS_addContextSensitivity with unknown number of arguments");
                Value param;
                Function function;
                if (call.getNumberOfArgs() == 1) { // enclosing function
                    param = NativeFunctions.readParameter(call, state, 0);
                    function = call.getSourceNode().getBlock().getFunction();
                } else if (call.getNumberOfArgs() == 2) { // function given as first parameter
                    Value funval = NativeFunctions.readParameter(call, state, 0);
                    Set<ObjectLabel> objlabels = funval.getObjectLabels();
                    if (funval.isMaybeOtherThanObject())
                        throw new AnalysisException("Calling TAJS_addContextSensitivity with non-fixed argument: " + funval);
                    function = null;
                    for (ObjectLabel objlabel : objlabels) {
                        if (objlabel.getKind() != Kind.FUNCTION || objlabel.isHostObject())
                            throw new AnalysisException("Calling TAJS_addContextSensitivity with non-user-function argument: " + objlabel);
                        if (function == null)
                            function = objlabel.getFunction();
                        else if (function != objlabel.getFunction()) // in case of multiple function object labels, they must agree on the function
                            throw new AnalysisException("Calling TAJS_addContextSensitivity with non-fixed argument: " + funval);
                    }
                    if (function == null)
                        throw new AnalysisException("Calling TAJS_addContextSensitivity with non-user-function argument: " + funval);
                    param = NativeFunctions.readParameter(call, state, 1);
                } else
                    throw new AnalysisException("Calling TAJS_addContextSensitivity with unexpected number of arguments");
                String var;
                if (param.isMaybeSingleStr()) {
                    if (param.isMaybeOtherThanStr())
                        throw new AnalysisException("Calling TAJS_addContextSensitivity with non-fixed-string argument: " + param);
                    var = param.getStr();
                } else if (param.isMaybeSingleNum()) {
                    if (param.isMaybeOtherThanNumUInt())
                        throw new AnalysisException("Calling TAJS_addContextSensitivity with non-fixed-index argument: " + param);
                    var = function.getParameterNames().get(param.getNum().intValue());
                } else {
                    throw new AnalysisException("Calling TAJS_addContextSensitivity with unexpected argument: " + param);
                }
                if (!function.getParameterNames().contains(var)) {
                    throw new AnalysisException("Bad use of manual TAJS_addContextSensitivity. No such parameter name: " + var);
                }
                c.getAnalysis().getContextSensitivityStrategy().requestContextSensitiveParameter(function, var);
                return Value.makeUndef();
            }

            case TAJS_NEW_OBJECT: {
                ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.OBJECT,
                        new HeapContext(state.getContext().getFunArgs(), null));
                state.newObject(objlabel);
                state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
                return Value.makeObject(objlabel);
            }

            case TAJS_GET_UI_EVENT: {
                return Value.makeObject(UIEvent.INSTANCES);
            }

            case TAJS_GET_MOUSE_EVENT: {
                return Value.makeObject(MouseEvent.INSTANCES);
            }

            case TAJS_GET_AJAX_EVENT: {
                return Value.makeObject(ReadystateEvent.INSTANCES);
            }

            case TAJS_GET_KEYBOARD_EVENT: {
                return Value.makeObject(KeyboardEvent.INSTANCES);
            }

            case TAJS_GET_EVENT_LISTENER: {
                return Value.makeObject(EventListener.INSTANCES);
            }

            case TAJS_GET_WHEEL_EVENT: {
                return Value.makeObject(WheelEvent.INSTANCES);
            }

            case TAJS_ASSERT: {
                return tajsValueAssert(nativeobject, call, state, c);
            }
            default:
                return null;
        }
    }

    /**
     * Generic value assertion. Could replace all other asserts.
     * <p>
     * Signature:
     * <p>
     * <pre>
     * TAJS_assert(value, predicate?, expectedResult?)
     * </pre>
     * Optional arguments default to ~true. Thus TAJS_assert(x) is equivalent to the usual assert(x)
     * <p>
     * The predicate is an expression of predicate method names on {@link Value}, the method names can be combined with the logical connector '||', whitespace is ignored.
     * <p>
     * Examples:
     * <pre>
     * TAJS_assert(value, isMaybeAnyNum, expectedResult)
     * TAJS_assert(value, isMaybeAnyNum || isMaybeAnyString, expectedResult)
     * TAJS_assert(value, isMaybeAnyNum || isMaybeAnyString || isMaybeObject, expectedResult)
     * </pre>
     * <p>
     * where:
     * <ul>
     * <li><b>value</b> is the JavaScript value to assert something about</li>
     * <li><b>predicate</b> is the predicates to evaluate <b>value</b> with</li>
     * <li><b>expectedResult</b> is the single boolean expected return value of the predicate invocation. E.g. true</li>
     * </ul>
     */
    private static Value tajsValueAssert(ECMAScriptObjects nativeobject, CallInfo call, final State state, Solver.SolverInterface c) {
        if (c.isScanning()) {
            NativeFunctions.expectParameters(nativeobject, call, c, 1, 3);
            Value value = NativeFunctions.readParameter(call, state, 0);
            final Value predicate;
            if (call.getNumberOfArgs() < 2) {
                predicate = Value.makeStr("isMaybeTrueButNotFalse");
            } else {
                predicate = NativeFunctions.readParameter(call, state, 1);
            }
            final Value expectedResult;
            if (call.getNumberOfArgs() < 3) {
                expectedResult = Value.makeBool(true);
            } else {
                expectedResult = NativeFunctions.readParameter(call, state, 2);
            }
            SourceLocation sourceLocation = call.getSourceNode().getSourceLocation();

            reflectiveAssert(value, predicate, expectedResult, sourceLocation);
        }
        return Value.makeUndef();
    }

    private static void reflectiveAssert(Value value, Value predicate, Value expectedResult, SourceLocation sourceLocation) {
        if (!predicate.isMaybeSingleStr()) {
            throw new AnalysisException(String.format("Call to %s failed. Predicate '%s' is not a single string!", ECMAScriptObjects.TAJS_ASSERT.toString(), predicate));
        }
        if (!(expectedResult.isMaybeTrueButNotFalse() || expectedResult.isMaybeFalseButNotTrue())) {
            throw new AnalysisException(String.format("Call to %s failed. Expected result '%s' is not a single boolean!", ECMAScriptObjects.TAJS_ASSERT.toString(), expectedResult));
        }
        final String predicateString = predicate.getStr().replace(" ", "");
        boolean result = false;
        for (String methodName : predicateString.split("\\|\\|")) {
            try {
                Method method = Value.class.getMethod(methodName.trim());
                result = result || (boolean) method.invoke(value);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new AnalysisException(String.format("Call to %s failed (%s). Predicate '%s' is not a zero-argument predicate on the Value class!", ECMAScriptObjects.TAJS_ASSERT.toString(), e.getClass().getSimpleName(), methodName));
            }
        }
        if ((expectedResult.isMaybeTrueButNotFalse() && !result) || (expectedResult.isMaybeFalseButNotTrue() && result)) {
            throw new AssertionError(String.format("Assertion failed:  <%s>(%s) != %s (%s)", predicate, value, expectedResult, sourceLocation));
        }
    }

    /**
     * Makes the given function context sensitive on selected parameters, and recursively backward through the call graph.
     *
     * @param f    the function
     * @param args the parameter names
     */
    private static void addContextSensitivity(Function f, Collection<String> args, State s, Solver.SolverInterface c) {
        // TODO: explain in the javadoc how this works (note that it currently assumes that the parameters are passed unchanged between the functions) (#167)
        if (args.isEmpty())
            return;
        boolean added = false;
        for (String param : f.getParameterNames()) {
            if (args.contains(param)) {
                c.getAnalysis().getContextSensitivityStrategy().requestContextSensitiveParameter(f, param);
                added = true;
            }
        }
        if (added) { // TODO: explain the interaction with the unevalizer... (#167)
            CallGraph<?, Context, ?> cg = c.getAnalysisLatticeElement().getCallGraph();
            Set<Pair<NodeAndContext<Context>, Context>> callers = cg.getSources(new BlockAndContext<>(f.getEntry(), s.getContext())); // TODO: use getEntryBlockAndContext? (#167)
            for (Pair<NodeAndContext<Context>, Context> caller : callers) { // propagate recursively backward through the call graph
                NormalForm nf = UnevalTools.rebuildNormalForm(c.getFlowGraph(), (CallNode) caller.getFirst().getNode(), s, c);
                addContextSensitivity(caller.getFirst().getNode().getBlock().getFunction(), nf.getArgumentsInUse(), s, c);
            }
        } else {
            throw new AnalysisException("Bad use of addContextSensitivity. No such parameter names: " + args);
        }
    }
}
