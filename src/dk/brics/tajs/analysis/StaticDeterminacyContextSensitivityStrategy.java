/*
 * Copyright 2009-2019 Aarhus University
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

import dk.brics.tajs.analysis.nativeobjects.concrete.SingleGamma;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.flowgraph.syntaticinfo.SyntacticQueries;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collectors;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;

/**
 * Implementation of the heuristics for call- and heap-contexts according to "Determinacy in Static Analysis of jQuery", OOPSLA 2014.
 */
public class StaticDeterminacyContextSensitivityStrategy extends BasicContextSensitivityStrategy {

    private final SyntacticQueries syntacticInformation;

    private final Map<Context, Map<String, Value>> freeVariableValuesAtAllocation = newMap();

    public StaticDeterminacyContextSensitivityStrategy(SyntacticQueries syntacticInformation) {
        this.syntacticInformation = syntacticInformation;
    }

    /**
     * From OOPSLA 2014 page 11:
     * <p>
     * For every object literal {p:v, ...} that is in a for-in loop or
     * syntactically contains a function parameter that is present in the
     * parameter sensitivity component of the current context, the abstract
     * address of the object will be context sensitive in the same way as
     * wrapper objects.
     * <p>
     */
    @Override
    public Context makeObjectLiteralHeapContext(AbstractNode node, State state, Solver.SolverInterface c) {
        Context functionContext = state.getContext();
        if (!shouldLiteralBeHeapSensitive(node, functionContext)) {
            return makeHeapContext(node, null, c);
        }
        return makeHeapContext(node, functionContext, c);
    }

    private boolean shouldLiteralBeHeapSensitive(AbstractNode node, Context arguments) {
        if (syntacticInformation.isCorrelatedAccessFunction(node.getBlock().getFunction())) {
            return true;
        }
        if (arguments == null || arguments.isUnknown()) {
            return false;
        }
        if (arguments.hasArguments()) {
            List<String> parameterNames = node.getBlock().getFunction().getParameterNames();
            int argumentNumber = 0;
            for (Value value : arguments.getArguments()) {
                boolean isSensitive = value != null;
                boolean hasParameterNameForArgument = parameterNames.size() > argumentNumber;
                if (isSensitive && hasParameterNameForArgument) {
                    String parameterName = parameterNames.get(argumentNumber);
                    if (syntacticInformation.doesLiteralReferenceParameter(node, parameterName)) {
                        return true;
                    }
                }
                argumentNumber++;
            }
        }
        return false;
    }

    /**
     * From OOPSLA 2014 page 10:
     * <p>
     * When a call or construct instruction creates a new activation object
     * and a new arguments object, the abstract addresses of these objects
     * obtain their context sensitivity valuation directly from the newly
     * created callee context.
     */
    @Override
    public Context makeActivationAndArgumentsHeapContext(State state, ObjectLabel function, FunctionCalls.CallInfo callInfo, Solver.SolverInterface c) {
        // Due to implementation details, the callee context is created *after* the activation and argument objects.
        // So the callee-context is computed here (using the same algorithm) as well
        return makeHeapContext(callInfo.getJSSourceNode(), decideCallContextArguments(function, callInfo, state, c), c);
    }

    @Override
    public Context makeConstructorHeapContext(State state, ObjectLabel function, FunctionCalls.CallInfo callInfo, Solver.SolverInterface c) {
        return makeHeapContext(callInfo.getJSSourceNode(), decideCallContextArguments(function, callInfo, state, c), c);
    }

    /**
     * From OOPSLA 2014 page 7:
     * <p>
     * When a transfer function creates a new context (t, a) at some call
     * site, we select those parameters for inclusion in a whose abstract
     * value is a concrete string (i.e., a known string in the String
     * lattice) or a single object address (i.e., exactly one object
     * allocation site).
     */
    @Override
    public Context makeFunctionEntryContext(State state, ObjectLabel function, FunctionCalls.CallInfo callInfo, Solver.SolverInterface c) {
        assert (function.getKind() == ObjectLabel.Kind.FUNCTION);
        Value thisval = null;
        if (!Options.get().isObjectSensitivityDisabled()) {
            if (c.getFlowGraph().getSyntacticInformation().isFunctionWithThisReference(function.getFunction())) {
                thisval = state.readThis();
            }
        }
        Context functionContext = decideCallContextArguments(function, callInfo, state, c);
        return Context.make(thisval, null, null, null, null, functionContext.getUnknownArg(), functionContext.getParameterNames(), functionContext.getArguments(), functionContext.getFreeVariables());
    }

    @Override
    public Context makeNextLoopUnrollingContext(Context currentContext, BeginLoopNode node) {
        if (node.isNested()) {
            return currentContext;
        }
        return super.makeNextLoopUnrollingContext(currentContext, node);
    }

    @Override
    public void requestContextSensitiveParameter(Function function, String parameter) {
        // ignore
    }

    private Context decideCallContextArguments(ObjectLabel obj_f, FunctionCalls.CallInfo call, State edge_state, Solver.SolverInterface c) {
        Map<String, Value> freeVariables = null;
        if (freeVariableValuesAtAllocation.containsKey(obj_f.getHeapContext())) {
            // Values of closure variables used as extra arguments (due to special-var removal).
            // We reuse the closure variables captured by decideCallContextArguments
            // NB: a proper implementation should be able to read the *current* value of the closure variables
            // .. but TAJS does not currently allow that, since the context is decided at the call-site
            freeVariables = freeVariableValuesAtAllocation.get(obj_f.getHeapContext());
        }
        List<Value> arguments = newList();
        if (!call.isUnknownNumberOfArgs()) {
            for (int i = 0; i < call.getNumberOfArgs(); i++) {
                arguments.add(FunctionCalls.readParameter(call, edge_state, i));
            }
        }
        List<Value> selectedArguments = newList();
        for (Value argument : arguments) {
            // avoid directly recursive context sensitivity components
            argument = argument.removeObjects(
                    argument.getObjectLabels().stream()
                            .filter(l -> c.getNode().equals(l.getNode()) || isRecursiveHeapContext(l))
                            .collect(Collectors.toSet()));

            if (isPrecise(argument, c)) {
                selectedArguments.add(argument);
            } else {
                selectedArguments.add(null);
            }
        }
        return Context.make(null, obj_f.getFunction().getParameterNames(), selectedArguments, freeVariables);
    }

    private boolean isRecursiveHeapContext(ObjectLabel l) {
        if (l.getNode() == null) {
            return false;
        }
        Set<ObjectLabel> topLevelObjectLabels = Context.extractTopLevelObjectLabels(l.getHeapContext());
        Set<ObjectLabel> recursives = topLevelObjectLabels.stream()
                .filter(innerLabel -> l.getNode().equals(innerLabel.getNode()))
                .collect(Collectors.toSet());
        boolean isRecursive = !recursives.isEmpty();
//        if (isRecursive) {
//            System.out.printf("Object %s is recursive by: %s%n", l, recursives);
//        }
        return isRecursive;
    }

    /**
     * From OOPSLA 2014 page 10:
     * <p>
     * When a declare-function instruction with source location n in some
     * context (t, a, b, d) \in C creates a new function object, its abstract
     * address is selected as (n, a, b, d) \in L; that is, the context
     * sensitivity valuations a, b, and d are taken directly from the current
     * context.
     * <p>
     * Reminder:
     * `a` is parameters, `b` is the loop unrolling variables, `d` is the for-in unrolling registers
     * <p>
     * <b>Major simplification (due to upcoming special-var removal): the function is allocated wrt. the current values of closure-variables.</b>
     * The closure variables must be "determinate" (string / single allocation site) or a valid loop variable.
     * <p>
     * NB; The capture of closure variables at allocation time might seem silly, since JavaScript semantics does not capture these values at allocation time.
     * But it turns out that the values caught in this way keeps functions separate regardless.
     */
    @Override
    public Context makeFunctionHeapContext(Function function, Solver.SolverInterface c) {
        final Map<String, Value> map = newMap();
        Set<String> closureVariableNames = c.getFlowGraph().getSyntacticInformation().getClosureVariableNames(function);
        if (closureVariableNames != null) {
            for (String closureVariableName : closureVariableNames) {
                Value value = UnknownValueResolver.getRealValue(c.getAnalysis().getPropVarOperations().readVariable(closureVariableName, null), c.getState());
                boolean isLoopVariable = syntacticInformation.isLoopVariable(function.getOuterFunction(), closureVariableName)
                        && value.isMaybeSingleNum();
                boolean isValidClosureVariables = isLoopVariable || isPrecise(value, c);
                boolean isRecursive = value.toString().split(closureVariableName + "=").length > 2; //TODO: Less hacky way of detecting recursion
                if (isValidClosureVariables && !isRecursive) {
                    map.put(closureVariableName, value);
                }
            }
        }
        Context heapContext = makeHeapContext(c.getNode(), Context.makeFreeVars(map), c);
        freeVariableValuesAtAllocation.put(heapContext, map);
        return heapContext;
    }

    public Context makeHeapContext(AbstractNode location, Context arguments, Solver.SolverInterface c) {
        boolean recursive = Context.extractTopLevelObjectLabels(arguments).stream().anyMatch(l -> {
            SourceLocation thisAllocationSite = location.getSourceLocation();
            SourceLocation otherAllocationSite = l.getSourceLocation();
            return thisAllocationSite.equals(otherAllocationSite);
        });
        if (recursive) {
            // System.out.printf("Avoiding creation of recursive objectlabel at %s: %s%n", location, arguments);
            return Context.makeEmpty();
        }
        return arguments != null ? Context.make(arguments.getUnknownArg(), arguments.getParameterNames(), arguments.getArguments(), arguments.getFreeVariables()) : Context.makeEmpty();
    }

    /**
     * A value is precise ("determinate") if it is a single string value or a bounded (ideally a single) number of abstract objects, or a single number (addition made to better support require).
     */
    public static boolean isPrecise(Value value, Solver.SolverInterface c) {
        if (value.typeSize() == 1) {
            if (SingleGamma.isConcreteString(value, c)) {
                return true;
            }
            if (value.isNullOrUndef()) {
                return true;
            }
            if (value.isMaybeObjectOrSymbol()) {
                return value.getObjectLabels().size() == 1;
            }
            if (value.isMaybeSingleNum() && value.getNum() != null) {
                return true;
            }
            if (!value.isNotBool() && (value.isMaybeTrueButNotFalse() || value.isMaybeFalseButNotTrue())) {
                return true;
            }
        }
        return false;
    }
}


