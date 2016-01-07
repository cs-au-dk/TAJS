package dk.brics.tajs.analysis;

import dk.brics.tajs.analysis.nativeobjects.concrete.Gamma;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.EndLoopNode;
import dk.brics.tajs.js2flowgraph.ASTInfo;
import dk.brics.tajs.js2flowgraph.AstEnv;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ContextArguments;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Implementation of the heuristics for call- and heap-contexts according to "Determinacy in Static Analysis of jQuery", OOPSLA 2014.
 */
public class StaticDeterminacyContextSensitivityStrategy implements IContextSensitivityStrategy {

    private final SyntacticHints syntacticHints;

    private final PreciseInterestingValuePredicate determinateInterestingValue = new PreciseInterestingValuePredicate(1);

    private final Map<HeapContext, Map<String, Value>> closureVariableValuesAtAllocation = newMap();

    private final BasicContextSensitivityStrategy basic = new BasicContextSensitivityStrategy();

    public StaticDeterminacyContextSensitivityStrategy(SyntacticHints syntacticHints) {
        this.syntacticHints = syntacticHints;
    }

    /**
     * The heuristic for which values that are allowed in contexts is now insufficient in a complex case:
     * <p>
     * <tt>Recursive calls in $.extend which includes $.isPlainObject</tt>
     * <p>
     * The definition of a precise value is loosened slightly for these two functions.
     */
    private static boolean isPreciseSpecialCaseOnExtendOrIsPlainObject(Value argument, Function function, State state, SourceLocation sourceLocation, Solver.SolverInterface c) {
        PreciseInterestingValuePredicate precisionPredicateLarge = new PreciseInterestingValuePredicate(4);
        if (!precisionPredicateLarge.isPrecise(argument, c)) {
            return false;
        }

        boolean DEBUG = false;

        boolean isPrecise;
        Value jQuery = UnknownValueResolver.getRealValue(c.getAnalysis().getPropVarOperations().readVariable("jQuery", null), state);
        boolean isJQueryExtend = false;
        boolean isJQueryIsPlainObject = false;
        if (jQuery.getObjectLabels().size() == 1) {
            Value extend = UnknownValueResolver.getProperty(jQuery.getObjectLabels().iterator().next(), "extend", state, false);
            if (extend.getObjectLabels().size() == 1) {
                isJQueryExtend = extend.getObjectLabels().iterator().next().getFunction() == function;
            }
            Value isPlainObject = UnknownValueResolver.getProperty(jQuery.getObjectLabels().iterator().next(), "isPlainObject", state, false);
            if (isPlainObject.getObjectLabels().size() == 1) {
                isJQueryIsPlainObject = isPlainObject.getObjectLabels().iterator().next().getFunction() == function;
            }
        }
        isPrecise = isJQueryExtend || isJQueryIsPlainObject;
        if (DEBUG) {
            if (!isPrecise) {
                Set<SourceLocation> objectSourceLocations = argument.getObjectSourceLocations();
                List<Integer> lineNumbers = newList();
                objectSourceLocations.forEach(l -> lineNumbers.add(l.getLineNumber()));
                Collections.sort(lineNumbers);
                System.out.println(String.format("small/large discrepancy at line %s for arg with objects from lines: %s", sourceLocation.getLineNumber(), lineNumbers));
            }
        }
        return isPrecise;
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
    public HeapContext makeObjectLiteralHeapContext(AbstractNode node, State state) {
        ContextArguments funArgs = state.getContext().getFunArgs();
        if (!shouldLiteralBeHeapSensitive(node, funArgs)) {
            return null;
        }
        return HeapContext.make(funArgs, null);
    }

    @Override
    public Context makeInitialContext() {
        return basic.makeInitialContext();
    }

    private boolean shouldLiteralBeHeapSensitive(AbstractNode node, ContextArguments arguments) {
        if (arguments == null || arguments.isUnknown()) {
            return false;
        }
        if (syntacticHints.isIsInForIn(node)) {
            return true;
        }
        List<String> parameterNames = node.getBlock().getFunction().getParameterNames();
        int argumentNumber = 0;
        for (Value value : arguments.getArguments()) {
            boolean isSensitive = value != null;
            boolean hasParameterNameForArgument = parameterNames.size() > argumentNumber;
            if (isSensitive && hasParameterNameForArgument) {
                String parameterName = parameterNames.get(argumentNumber);
                if (syntacticHints.doesLiteralReferenceParameter(node, parameterName)) {
                    return true;
                }
            }
            argumentNumber++;
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
    public HeapContext makeActivationAndArgumentsHeapContext(State state, ObjectLabel function, FunctionCalls.CallInfo callInfo, Solver.SolverInterface c) {
        // Due to implementation details, the callee context is created *after* the activation and argument objects.
        // So the callee-context is computed here (using the same algorithm) as well
        return HeapContext.make(decideCallContextArguments(function, callInfo, state, c), null);
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
        // set thisval for object sensitivity (unlike traditional object sensitivity we allow sets of object labels)
        Set<ObjectLabel> thisval = null;
        if (!Options.get().isObjectSensitivityDisabled()) {
            if (function.getFunction().isUsesThis()) {
                thisval = newSet(state.readThisObjects());
            }
        }

        ContextArguments funArgs = decideCallContextArguments(function, callInfo, state, c);

        return new Context(thisval, funArgs, null, null, null);
    }

    @Override
    public Context makeForInEntryContext(Context currentContext, BeginForInNode n, Value v) {
        return basic.makeForInEntryContext(currentContext, n, v);
    }

    @Override
    public Context makeNextLoopUnrollingContext(Context currentContext, BeginLoopNode node) {
        return basic.makeNextLoopUnrollingContext(currentContext, node);
    }

    @Override
    public Context makeLoopExitContext(Context currentContext, EndLoopNode node) {
        return basic.makeLoopExitContext(currentContext, node);
    }

    @Override
    public void requestContextSensitiveParameter(Function function, String parameter) {
        // ignore
    }

    private ContextArguments decideCallContextArguments(ObjectLabel obj_f, FunctionCalls.CallInfo call, State edge_state, Solver.SolverInterface c) {
        Map<String, Value> closureVariables = null;
        if (closureVariableValuesAtAllocation.containsKey(obj_f.getHeapContext())) {
            // Values of closure variables used as extra arguments (due to special-var removal).
            // We reuse the closure variables captured by decideCallContextArguments

            // NB: a proper implementation should be able to read the *current* value of the closure variables
            // .. but TAJS does not currently allow that, since the context is decided at the call-site

            closureVariables = closureVariableValuesAtAllocation.get(obj_f.getHeapContext());
        }

        List<Value> arguments = newList();
        for (int i = 0; i < call.getNumberOfArgs(); i++) {
            Value argument = UnknownValueResolver.getRealValue(call.getArg(i), edge_state);
            boolean isPrecise = determinateInterestingValue.isPrecise(argument, c);
            if (!isPrecise) {
                isPrecise = isPreciseSpecialCaseOnExtendOrIsPlainObject(argument, obj_f.getFunction(), edge_state, call.getJSSourceNode().getSourceLocation(), c);
            }
            if (isPrecise) {
                arguments.add(argument);
            } else {
                arguments.add(null);
            }
        }

        return new ContextArguments(obj_f.getFunction().getParameterNames(), arguments, closureVariables);
    }

    /**
     * From OOPSLA 2014 page 10:
     * <p>
     * When a declare-function instruction with source location n in some
     * context (t, a, b, d) ∈ C creates a new function object, its abstract
     * address is selected as (n, a, b, d) ∈ L; that is, the context
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
    public HeapContext makeFunctionHeapContext(Function function, Solver.SolverInterface c) {
        final Map<String, Value> map = newMap();

        Set<String> closureVariableNames = function.getClosureVariableNames();
        if (closureVariableNames != null) {
            for (String closureVariableName : closureVariableNames) {
                Value value = UnknownValueResolver.getRealValue(c.getAnalysis().getPropVarOperations().readVariable(closureVariableName, null), c.getState());
                boolean isLoopVariable = syntacticHints.isLoopVariable(function.getOuterFunction(), closureVariableName)
                        && value.isMaybeSingleNum();
                boolean isValidClosureVariables = isLoopVariable || determinateInterestingValue.isPrecise(value, c);
                if (isValidClosureVariables) {
                    map.put(closureVariableName, value);
                }
            }
        }
        HeapContext heapContext = HeapContext.make(new ContextArguments(null, map), null);
        closureVariableValuesAtAllocation.put(heapContext, map);
        return heapContext;
    }

    /**
     * A value is precise ("determinate") if it is a single string value or a bounded number of abstract objects/functions/arrays.
     */
    private static class PreciseInterestingValuePredicate {

        private final int objectLimit;

        public PreciseInterestingValuePredicate(int objectLimit) {
            this.objectLimit = objectLimit;
        }

        public boolean isPrecise(Value value, Solver.SolverInterface c) {
            if (value.typeSize() == 1) {
                if (Gamma.isConcreteString(value, c)) {
                    return true;
                }
                if (value.isMaybeObject()) {
                    for (ObjectLabel objectLabel : value.getObjectLabels()) {
                        switch (objectLabel.getKind()) {
                            case OBJECT:
                                return value.getObjectSourceLocations().size() <= objectLimit;
                            case FUNCTION:
                            case ARGUMENTS:
                            case ARRAY:
                                return value.getObjectLabels().size() == 1;
                        }
                    }
                }
            }
            return false;
        }
    }

    /**
     * Various syntactic information that guides the heuristics.
     */
    public static class SyntacticHints {

        private static SyntacticHints instance;

        private final Map<AbstractNode, Set<String>> variableDependencies = newMap();

        private final Set<AbstractNode> inForIn = newSet();

        private final Map<Function, Set<String>> loopVariables = newMap();

        private SyntacticHints() {
        }

        public static SyntacticHints get() {
            if (instance == null) {
                instance = new SyntacticHints();
            }
            return instance;
        }

        public static void reset() {
            instance = null;
        }

        public void registerLiteral(AbstractNode literalNode, ASTInfo.LiteralTree literalTree, ASTInfo astInfo) {
            if (astInfo.getLiteralsInForIn().contains(literalTree)) {
                inForIn.add(literalNode);
            }
            if (astInfo.getVariableReadsInLiterals().containsKey(literalTree)) {
                Set<String> variableReads = astInfo.getVariableReadsInLiterals().get(literalTree);
                for (String variableRead : variableReads) {
                    addToMapSet(variableDependencies, literalNode, variableRead);
                }
            }
        }

        /**
         * From OOPSLA 2014 page 8:
         * <p>
         * "However,we include only variables that appear syntactically in
         * the condition and in the body of a non-nested for loop in the
         * current function, and are involved in a dynamic property read
         * operation (i.e. as a sub-expression of e2 in e1[e2])"
         */
        public boolean isLoopVariable(Function function, String variable) {
            return loopVariables.containsKey(function) && loopVariables.get(function).contains(variable);
        }

        public boolean doesLiteralReferenceParameter(AbstractNode literalAllocationNode, String parameterName) {
            return variableDependencies.containsKey(literalAllocationNode) && variableDependencies.get(literalAllocationNode).contains(parameterName);
        }

        public void registerLoop(ASTInfo.LoopTree loopTree, AstEnv env, ASTInfo astInfo) {
            // avoid nested loops
            if (astInfo.getNestedLoops().contains(loopTree)) {
                return;
            }
            Set<String> conditionReadVariables = newSet();
            Set<String> nonInitializerWriteVariables = newSet();
            Set<String> dpaReadVariables = newSet();
            if (astInfo.getLoopConditionVariableReads().containsKey(loopTree)) {
                conditionReadVariables.addAll(astInfo.getLoopConditionVariableReads().get(loopTree));
            }
            if (astInfo.getLoopNonInitializerVariableWrites().containsKey(loopTree)) {
                nonInitializerWriteVariables.addAll(astInfo.getLoopNonInitializerVariableWrites().get(loopTree));
            }
            if (astInfo.getLoopNonInitializerDynamicPropertyVariableReads().containsKey(loopTree)) {
                dpaReadVariables.addAll(astInfo.getLoopNonInitializerDynamicPropertyVariableReads().get(loopTree));
            }

            // intersect the three sets: a conjunction
            Set<String> candidateVariables = newSet(conditionReadVariables);
            candidateVariables.retainAll(nonInitializerWriteVariables);
            candidateVariables.retainAll(dpaReadVariables);

            for (String candidateVariable : candidateVariables) {
                addToMapSet(loopVariables, env.getFunction(), candidateVariable);
            }
        }

        public boolean isIsInForIn(AbstractNode node) {
            return inForIn.contains(node);
        }
    }
}


