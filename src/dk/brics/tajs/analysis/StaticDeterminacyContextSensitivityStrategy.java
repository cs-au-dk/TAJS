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
import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.flowgraph.syntaticinfo.SyntacticQueries;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.PartitionedValue;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.GenericSolver;
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

    public StaticDeterminacyContextSensitivityStrategy(SyntacticQueries syntacticInformation) {
        this.syntacticInformation = syntacticInformation;
    }

    /**
     * From OOPSLA 2014 page 11:
     * <p>
     * For every object literal {p:v, ...} that is in a for-in loop or syntactically contains a function parameter that
     * is present in the parameter sensitivity component of the current context, the abstract address of the object
     * will be context sensitive in the same way as wrapper objects.
     * <p>
     */
    @Override
    public Context makeObjectLiteralHeapContext(AbstractNode node, State state, Solver.SolverInterface c) {
        Context functionContext = state.getContext();
        if (shouldLiteralBeHeapSensitive(node, functionContext)) {
            return makeHeapContext(node, functionContext);
        } else {
            return null;
        }
    }

    /**
     * Decides whether an object literal should use heap context sensitivity.
     */
    private boolean shouldLiteralBeHeapSensitive(AbstractNode node, Context arguments) {
        // if the current function contains variables that are used as both property read and write names, then yes
        if (syntacticInformation.isCorrelatedAccessFunction(node.getBlock().getFunction())) {
            return true;
        }
        // if the literal contains a function parameter that is selected for parameter sensitivity, then yes
        if (arguments != null && !arguments.isUnknownArgs() && arguments.hasArguments()) {
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
        return makeHeapContext(callInfo.getJSSourceNode(), decideCallContextArguments(function, callInfo, state, c));
    }

    /**
     * Same behavior as {@link #makeActivationAndArgumentsHeapContext(State, ObjectLabel, FunctionCalls.CallInfo, GenericSolver.SolverInterface)}.
     */
    @Override
    public Context makeConstructorHeapContext(State state, ObjectLabel function, FunctionCalls.CallInfo callInfo, Solver.SolverInterface c) {
        return makeHeapContext(callInfo.getJSSourceNode(), decideCallContextArguments(function, callInfo, state, c));
    }

    /**
     * From OOPSLA 2014 page 7:
     * <p>
     * When a transfer function creates a new context (t, a) at some call site, we select those parameters for inclusion in a whose abstract value
     * is a concrete string (i.e., a known string in the String lattice) or a single object address (i.e., exactly one object allocation site).
     * <p>
     * Unless -no-object-sensitivity is enabled, the context also uses object sensitivity.
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
        return Context.make(thisval, null, null, null, null, functionContext.getUnknownArg(), functionContext.getParameterNames(), functionContext.getArguments(), functionContext.getFreeVariables(), callInfo.getFunctionPartitions(function));
    }

    @Override
    public Context makeNextLoopUnrollingContext(Context currentContext, BeginLoopNode node) {
        if (node.isNested() && !node.getSourceLocation().toString().startsWith("HOST(")) { // TODO: better way to recognize host code?
            return currentContext; // disabling loop unrolling for inner loops (except for host locations)
        }
        return super.makeNextLoopUnrollingContext(currentContext, node);
    }

//    /**
//     * Parameter sensitivity hints from TAJS_addContextSensitivity and Unevalizer are ignored!.
//     */
//    @Override
//    public void requestContextSensitiveParameter(Function function, String parameter) {
//        // ignore
//    }

    /**
     * Creates a context based on determinate arguments and free variables.
     */
    private Context decideCallContextArguments(ObjectLabel obj_f, FunctionCalls.CallInfo call, State edge_state, Solver.SolverInterface c) {
        List<Value> selectedArguments = newList();
        if (!call.isUnknownNumberOfArgs()) {
            for (int i = 0; i < call.getNumberOfArgs(); i++) {
                Value argument = FunctionCalls.readParameter(call, edge_state, i);
                // avoid directly recursive context sensitivity components
                argument = argument.removeObjects(
                        argument.getObjectLabels().stream()
                                .filter(l -> c.getNode().equals(l.getNode()) || isRecursiveHeapContext(l))
                                .collect(Collectors.toSet()));

                if (isPrecise(argument, c)) {
                    selectedArguments.add(argument.setFunctionPartitions(null));
                } else {
                    selectedArguments.add(null);
                }
            }
        }
        return Context.make(null, obj_f.getFunction().getParameterNames(), selectedArguments, obj_f.getHeapContext().getFreeVariables());
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
     * (`a` is parameters, `b` is the loop unrolling variables, `d` is the for-in unrolling registers)
     */
    @Override
    public Context makeFunctionHeapContext(Function function, Solver.SolverInterface c) {
        // find values of free variables (only including variables that are loop variables or are determinate)
        final Map<String, Value> map = newMap();
        Set<String> freeVariableNames = c.getFlowGraph().getSyntacticInformation().getClosureVariableNames(function);
        if (freeVariableNames != null) {
            for (String freeVariableName : freeVariableNames) {
                Value value = UnknownValueResolver.getRealValue(c.getAnalysis().getPropVarOperations().readVariable(freeVariableName, null), c.getState());
                boolean isLoopVariable = syntacticInformation.isLoopVariable(function.getOuterFunction(), freeVariableName)
                        && value.isMaybeSingleNum();
                boolean isValidFreeVariables = isLoopVariable || isPrecise(value, c);
                boolean isRecursive = value.toString().split(freeVariableName + "=").length > 2; //TODO: Less hacky way of detecting recursion in contexts (see isRecursiveHeapContext and Context.extractTopLevelObjectLabels?)
                if (isValidFreeVariables && !isRecursive) {
                    map.put(freeVariableName, value);
                }
            }
        }
        return makeHeapContext(c.getNode(), Context.makeFreeVars(map));
    }

    /**
     * Creates a heap context from the given context. A heap context only contains information for parameter sensitivity.
     */
    private Context makeHeapContext(AbstractNode location, Context arguments) {
        boolean recursive = Context.extractTopLevelObjectLabels(arguments).stream().anyMatch(l -> location.getSourceLocation().equals(l.getSourceLocation()));
        if (recursive) {
            // System.out.printf("Avoiding creation of recursive objectlabel at %s: %s%n", location, arguments);
            return Context.makeEmpty();
        }
        return arguments != null ? Context.make(arguments.getUnknownArg(), arguments.getParameterNames(), arguments.getArguments(), arguments.getFreeVariables()) : Context.makeEmpty();
    }

    /**
     * A value is precise ("determinate") if it is a single string value or a bounded (ideally a single) number of abstract objects, or a single number (addition made to better support 'require').
     * A partitioned value is precise if at least one partition has a precise value.
     */
    private static boolean isPrecise(Value value, Solver.SolverInterface c) {
        if (value instanceof PartitionedValue) {
            PartitionedValue pv = (PartitionedValue) value;
            return pv.getPartitionValues().stream().anyMatch(v -> isPrecise(v, c));
        }
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
            if (value.isMaybeSingleNum()) {
                return true;
            }
            if (!value.isNotBool() && (value.isMaybeTrueButNotFalse() || value.isMaybeFalseButNotTrue())) {
                return true;
            }
        }
        return false;
    }
}


