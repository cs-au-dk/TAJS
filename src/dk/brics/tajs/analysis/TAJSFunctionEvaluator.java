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

package dk.brics.tajs.analysis;

import dk.brics.tajs.analysis.Conversion.Hint;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.dom.ajax.ReadystateEvent;
import dk.brics.tajs.analysis.dom.event.EventListener;
import dk.brics.tajs.analysis.dom.event.KeyboardEvent;
import dk.brics.tajs.analysis.dom.event.MouseEvent;
import dk.brics.tajs.analysis.dom.event.UIEvent;
import dk.brics.tajs.analysis.dom.event.WheelEvent;
import dk.brics.tajs.analysis.nativeobjects.FunctionFileLoader;
import dk.brics.tajs.analysis.nativeobjects.JSRegExp;
import dk.brics.tajs.analysis.nativeobjects.TAJSFunction;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.analysis.uneval.NormalForm;
import dk.brics.tajs.analysis.uneval.UnevalTools;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.lattice.ContextArguments;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException.AnalysisModelLimitationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Evaluates the TAJS_* functions ({@link TAJSFunction}).
 */
public class TAJSFunctionEvaluator {

    public static Value evaluate(TAJSFunction nativeobject, CallInfo call, Solver.SolverInterface c) {
        State state = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        switch (nativeobject) {
            case TAJS_DUMPVALUE: {
                Value x = FunctionCalls.readParameter(call, state, 0); // to avoid recover: call.getArg(0);
                c.getMonitoring().addMessageInfo(c.getNode(), Severity.HIGH, "Abstract value: " + x.restrictToNotModified() /*+ " (context: " + c.getState().getContext() + ")"*/);
                return Value.makeUndef();
            }

            case TAJS_DUMPPROTOTYPE: {
                Value x = FunctionCalls.readParameter(call, state, 0);
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
                c.getMonitoring().addMessageInfo(c.getNode(), Severity.HIGH, "Prototype: " + sb);
                return Value.makeUndef();
            }

            case TAJS_DUMPOBJECT: {
                Value x = FunctionCalls.readParameter(call, state, 0);
                c.getMonitoring().addMessageInfo(c.getNode(), Severity.HIGH, "Abstract object: " + state.printObject(x) /*+ " (context: " + c.getState().getContext() + ")"*/);
                return Value.makeUndef();
            }

            case TAJS_DUMPSTATE: {
                c.getMonitoring().addMessageInfo(c.getNode(), Severity.HIGH, "Abstract state:\n" +  /*"Context: " + c.getState().getContext() + "\n" + */ state);
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
                c.getMonitoring().addMessageInfo(c.getNode(), Severity.HIGH, "Abstract state (modified parts):" /*+ " (context: " + c.getState().getContext() + ")"*/ + state.toStringModified());
                return Value.makeUndef();
            }

            case TAJS_DUMPATTRIBUTES: {
                Value x = FunctionCalls.readParameter(call, state, 0);
                Value p = Conversion.toString(FunctionCalls.readParameter(call, state, 1), c);
                if (!p.isMaybeSingleStr())
                    c.getMonitoring().addMessageInfo(c.getNode(), Severity.HIGH, "Calling dumpAttributes with non-constant property name");
                else {
                    String propertyname = p.getStr();
                    Value v = c.getAnalysis().getPropVarOperations().readPropertyDirect(x.getObjectLabels(), propertyname);
                    c.getMonitoring().addMessageInfo(c.getNode(), Severity.HIGH, "Property attributes: " + v.printAttributes() /*+ " (context: " + c.getState().getContext() + ")"*/);
                }
                return Value.makeUndef();
            }

            case TAJS_DUMPEXPRESSION: {
                CallNode cn = (CallNode) call.getSourceNode();
                String s = UnevalTools.rebuildFullExpression(c.getFlowGraph(), call.getSourceNode(), cn.getArgRegister(0));
                c.getMonitoring().addMessageInfo(c.getNode(), Severity.HIGH, "Exp: " + s);
                return Value.makeUndef();
            }

            case TAJS_DUMPNF: {
                CallNode cn = (CallNode) call.getSourceNode();
                NormalForm s = UnevalTools.rebuildNormalForm(c.getFlowGraph(), cn, state, c);
                c.getMonitoring().addMessageInfo(c.getNode(), Severity.HIGH, "NF: " + s);
                return Value.makeUndef();
            }

            case TAJS_CONVERSION_TO_PRIMITIVE: {
                Value varg = FunctionCalls.readParameter(call, state, 0);
                Value vhint;
                if (call.isUnknownNumberOfArgs())
                    vhint = FunctionCalls.readParameter(call, state, 1).joinStr("NONE");
                else
                    vhint = call.getNumberOfArgs() >= 2 ? FunctionCalls.readParameter(call, state, 1) : Value.makeStr("NONE");

                if (!vhint.isMaybeSingleStr()) {
                    c.getMonitoring().addMessageInfo(c.getNode(), Severity.HIGH, "Calling conversionToPrimitive with non-constant hint string");
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
                    param = FunctionCalls.readParameter(call, state, 0);
                    function = call.getSourceNode().getBlock().getFunction();
                } else if (call.getNumberOfArgs() == 2) { // function given as first parameter
                    Value funval = FunctionCalls.readParameter(call, state, 0);
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
                    param = FunctionCalls.readParameter(call, state, 1);
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

            case TAJS_MAKE_CONTEXT_SENSITIVE: {
                Value calleeArg = FunctionCalls.readParameter(call, state, 0);
                int argumentPositionArg = FunctionCalls.readParameter(call, state, 1).getNum().intValue();
                Value guardArg = FunctionCalls.readParameter(call, state, 2);
                Value caller = UnknownValueResolver.getRealValue(pv.readPropertyValue(guardArg.getObjectLabels(), "caller"), c.getState());
                calleeArg.getObjectLabels().forEach(calleeLabel -> {
                    if (!calleeLabel.isHostObject() && calleeLabel.getKind() == Kind.FUNCTION) {
                        if (caller.isMaybeUndef() || caller.isNone()) {
                            c.getAnalysis().getContextSensitivityStrategy().makeSensitive(calleeLabel.getFunction(), argumentPositionArg);
                        } else {
                            caller.getObjectLabels().forEach(callerLabel -> {
                                if (!callerLabel.isHostObject() && callerLabel.getKind() == Kind.FUNCTION) {
                                    c.getAnalysis().getContextSensitivityStrategy().makeSensitiveFromCaller(calleeLabel.getFunction(), argumentPositionArg, callerLabel.getFunction());
                                }
                            });
                        }
                    }
                });
                return Value.makeUndef();
            }

            case TAJS_NEW_OBJECT: {
                return newInstanceForContext(Kind.OBJECT, InitialStateBuilder.OBJECT_PROTOTYPE, state, call);
            }

            case TAJS_NEW_ARRAY: {
                return newInstanceForContext(Kind.ARRAY, InitialStateBuilder.ARRAY_PROTOTYPE, state, call);
            }

            case TAJS_ASYNC_LISTEN: {
                AsyncEvents.listen(call.getSourceNode(), FunctionCalls.readParameter(call, state, 0), c);
                return Value.makeUndef();
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
                if (!c.isScanning()) {
                    return Value.makeUndef();
                }
                return tajsValueAssert(nativeobject, call, state, c);
            }

            case TAJS_MAKE: {
                return tajsMake(nativeobject, call, state, c);
            }

            case TAJS_MAKE_PARTIAL: {
                String kind = FunctionCalls.readParameter(call, state, 0).getStr();
                Kind allocationKind;
                ObjectLabel prototype;
                switch (kind) {
                    case "FUNCTION":
                        allocationKind = Kind.FUNCTION;
                        prototype = InitialStateBuilder.FUNCTION_PROTOTYPE;
                        break;
                    case "OBJECT":
                        allocationKind = Kind.OBJECT;
                        prototype = InitialStateBuilder.OBJECT_PROTOTYPE;
                        break;
                    case "ARRAY":
                        allocationKind = Kind.ARRAY;
                        prototype = InitialStateBuilder.ARRAY_PROTOTYPE;
                        break;
                    default:
                        throw new AnalysisException("Unhandled case: " + kind);
                }
                String identifier = FunctionCalls.readParameter(call, state, 1).getStr();
                ObjectLabel objlabel = ObjectLabel.make(new PartialHostModels(identifier), allocationKind);
                state.newObject(objlabel);
                if (allocationKind == Kind.FUNCTION) {
                    ObjectLabel externalPrototype = ObjectLabel.make(new PartialHostModels(String.format("%s.prototype", identifier)), Kind.OBJECT);
                    state.newObject(externalPrototype);
                    state.writeInternalPrototype(externalPrototype, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
                    pv.writePropertyWithAttributes(objlabel, "prototype", Value.makeObject(externalPrototype).setAttributes(true, true, false));
                }
                state.writeInternalPrototype(objlabel, Value.makeObject(prototype));
                return Value.makeObject(objlabel);
            }

            case TAJS_JOIN: {
                final Set<Value> values = newSet();
                for (int i = 0; i < call.getNumberOfArgs(); i++) {
                    values.add(FunctionCalls.readParameter(call, state, i));
                }
                return Value.join(values);
            }

            case TAJS_ASSERT_EQUALS: {
                if (!c.isScanning()) {
                    return Value.makeUndef();
                }
                Value expected = FunctionCalls.readParameter(call, state, 0);
                Value actual = FunctionCalls.readParameter(call, state, 1);
                if (call.isUnknownNumberOfArgs() || call.getNumberOfArgs() < 2 || call.getNumberOfArgs() > 3) {
                    throw new AnalysisException(call.getJSSourceNode().getSourceLocation() + ": " + String.format("Unexpected number of arguments to %s", nativeobject));
                }
                boolean expectedResult;
                if (call.getNumberOfArgs() == 3) {
                    Value expectedResultValue = FunctionCalls.readParameter(call, state, 2);
                    if (expectedResultValue.isMaybeOtherThanBool() || expectedResultValue.isMaybeAnyBool()) {
                        throw new AnalysisException(call.getJSSourceNode().getSourceLocation() + ": " + String.format("Invalid expectedResult-argument: `%s` to %s", expectedResultValue.toString(), nativeobject));
                    }
                    expectedResult = expectedResultValue.isMaybeTrueButNotFalse();
                } else {
                    expectedResult = true;
                }
                if (expected.equals(actual) != expectedResult) {
                    String reason;
                    if (expectedResult) {
                        reason = String.format("Expected=%s, Actual=%s", expected, actual);
                    } else {
                        reason = String.format("Not-expected=%s, Actual=%s", expected, actual);
                    }
                    throw new AssertionError(String.format("Assertion failed. %s (at %s)", reason, call.getJSSourceNode().getSourceLocation()));
                }
                return Value.makeUndef();
            }

            /*
             * Loads a JavaScript file (arg0) as a function with the chosen parameter names (arg2...), maybe marks the function as host-environment function (arg1)
             */
            case TAJS_LOAD: {
                Value target = FunctionCalls.readParameter(call, state, 0);
                Value isHostEnvironment = FunctionCalls.readParameter(call, state, 1);
                if (target.isMaybeFuzzyStr() || target.isNotStr()) {
                    throw new AnalysisException("Only constant-string TAJS_loads supported: " + target);
                }
                if (isHostEnvironment.isMaybeAnyBool() || isHostEnvironment.isMaybeOtherThanBool()) {
                    throw new AnalysisException("Only definite isHostEnvironment TAJS_loads supported: " + isHostEnvironment);
                }
                if (call.isUnknownNumberOfArgs()) {
                    throw new AnalysisException("Only known number of arguments supported");
                }
                List<String> parameterNames = newList();
                for (int i = 2; i < call.getNumberOfArgs(); i++) {
                    Value argument = FunctionCalls.readParameter(call, state, i);
                    if (!argument.isMaybeSingleStr()) {
                        throw new AnalysisException("Only constant-string parameter-names for TAJS_load supported: " + argument);
                    }
                    parameterNames.add(argument.getStr());
                }

                return FunctionFileLoader.loadFunction(target.getStr(), isHostEnvironment.isMaybeTrueButNotFalse(), parameterNames, c);
            }

            case TAJS_FIRST_ORDER_STRING_REPLACE: {
                Value receiver = FunctionCalls.readParameter(call, state, 0);
                Value toReplace = FunctionCalls.readParameter(call, state, 1);
                Value toReplaceWith = FunctionCalls.readParameter(call, state, 2);
                if (toReplaceWith.getObjectLabels().stream().anyMatch(l -> l.getKind() == Kind.FUNCTION)) {
                    throw new AnalysisModelLimitationException(c.getNode().getSourceLocation() + ": Replacer function callbacks are not supported: " + toReplaceWith);
                }
                return TAJSConcreteSemantics.convertTAJSCallExplicit(receiver, "String.prototype.replace", Arrays.asList(toReplace, toReplaceWith), c, () -> {
                    JSRegExp.makeFuzzyLastIndexOfAnyGlobalRegexes(FunctionCalls.readParameter(call, c.getState(), 0), c);
                    return Value.makeAnyStr();
                });
            }

            default:
                return null;
        }
    }

    private static Value tajsMake(TAJSFunction nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
        Value methodNameSuffix = FunctionCalls.readParameter(call, state, 0);
        AbstractNode callNode = call.getJSSourceNode();
        if (!methodNameSuffix.isMaybeSingleStr()) {
            throw new AnalysisException(callNode.getSourceLocation() + ": " + String.format("Call to %s failed. Method name suffix '%s' is not a single string!", nativeobject, methodNameSuffix));
        }
        String methodName = "make" + methodNameSuffix.getStr();
        try {
            Method method = Value.class.getMethod(methodName);
            return (Value) method.invoke(null);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new AnalysisException(callNode.getSourceLocation() + ": " + String.format("Call to %s failed (%s). Method '%s' is not a zero-argument static method on the Value class!", nativeobject.toString(), e.getClass().getSimpleName(), methodName));
        }
    }

    /**
     * Instantiates a new object which inherits (parts of) the current calling context.
     */
    private static Value newInstanceForContext(Kind allocationKind, ObjectLabel prototype, State state, CallInfo call) {
        ContextArguments funArgs = state.getContext().getFunArgs();
        // crude guard against infinite allocations due to recursion:
        // if the current receiver is using this allocation site already, then we do not nest the context further
        // (infinite recursive allocations should be guarded elsewhere)
        Set<ObjectLabel> callContextReceivers = newSet();
        if (funArgs != null && funArgs.getSelectedClosureVariables() != null) {
            callContextReceivers.addAll(funArgs.getSelectedClosureVariables().getOrDefault("this", Value.makeNone()).getObjectLabels());
        }
        if (state.getContext().getThisVal() != null) {
            callContextReceivers.addAll(state.getContext().getThisVal());
        }
        Set<SourceLocation> callContextReceiverAllocationSites = callContextReceivers.stream().map(ObjectLabel::getSourceLocation).collect(Collectors.toSet());
        ObjectLabel objlabel;
        SourceLocation allocationSite = call.getSourceNode().getSourceLocation();
        if (callContextReceiverAllocationSites.contains(allocationSite)) {
            Map<String, Value> recursiveTagger = newMap();
            recursiveTagger.put("isRecursiveAllocation", Value.makeBool(true)); // TODO: "isRecursiveAllocation"??? (see comment at the beginning of this method)
            objlabel = ObjectLabel.make(call.getSourceNode(), allocationKind, HeapContext.make(null, recursiveTagger));
        } else {
            objlabel = ObjectLabel.make(call.getSourceNode(), allocationKind, HeapContext.make(funArgs, null));
        }
        state.newObject(objlabel);
        state.writeInternalPrototype(objlabel, Value.makeObject(prototype));
        return Value.makeObject(objlabel);
    }

    /**
     * Generic value assertion. Could replace all other asserts.
     * <p>
     * Signature:
     * <p>
     * <pre>
     * TAJS_assert(value, predicate?, expectedResult?, mustBeReachable?)
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
    private static Value tajsValueAssert(HostObject nativeObject, CallInfo call, final State state, Solver.SolverInterface c) {
        Value value = FunctionCalls.readParameter(call, state, 0);
        final Value predicate;
        if (call.getNumberOfArgs() < 2) {
            predicate = Value.makeStr("isMaybeTrueButNotFalse");
        } else {
            predicate = FunctionCalls.readParameter(call, state, 1);
        }
        final Value expectedResult;
        if (call.getNumberOfArgs() < 3) {
            expectedResult = Value.makeBool(true);
        } else {
            expectedResult = FunctionCalls.readParameter(call, state, 2);
        }
        SourceLocation sourceLocation = call.getSourceNode().getSourceLocation();
        reflectiveAssert(nativeObject, value, predicate, expectedResult, sourceLocation);
        return Value.makeUndef();
    }

    private static void reflectiveAssert(HostObject nativeObject, Value value, Value predicate, Value expectedResult, SourceLocation sourceLocation) {
        if (!predicate.isMaybeSingleStr()) {
            throw new AnalysisException(String.format("Call to %s failed. Predicate '%s' is not a single string!", nativeObject.toString(), predicate));
        }
        if (!(expectedResult.isMaybeTrueButNotFalse() || expectedResult.isMaybeFalseButNotTrue())) {
            throw new AnalysisException(String.format("Call to %s failed. Expected result '%s' is not a single boolean!", nativeObject.toString(), expectedResult));
        }
        final String predicateString = predicate.getStr().replace(" ", "");
        boolean result = false;
        for (String methodName : predicateString.split("\\|\\|")) {
            try {
                Method method = Value.class.getMethod(methodName.trim());
                result = result || (boolean) method.invoke(value);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new AnalysisException(String.format("Call to %s failed (%s). Predicate '%s' is not a zero-argument predicate on the Value class!", nativeObject.toString(), e.getClass().getSimpleName(), methodName));
            }
        }
        if ((expectedResult.isMaybeTrueButNotFalse() && !result) || (expectedResult.isMaybeFalseButNotTrue() && result)) {
            throw new AssertionError(String.format("Assertion failed:  <%s>(%s) != %s (%s)", predicate, value, expectedResult, sourceLocation));
        }
    }
}
