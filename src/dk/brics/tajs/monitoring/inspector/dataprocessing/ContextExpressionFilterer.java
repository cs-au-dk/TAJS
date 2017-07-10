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

package dk.brics.tajs.monitoring.inspector.dataprocessing;

import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.util.Collectors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filters contexts based on a tiny domain specific language.
 */
public class ContextExpressionFilterer {

    private final GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, Analysis>.SolverInterface c;

    public ContextExpressionFilterer(Solver.SolverInterface c) {
        this.c = c;
    }

    /**
     * Filters contexts based on a tiny domain specific language.
     * <p>
     * Uses predicate-methods of Value reflectively.
     */
    public Set<Context> filter(AbstractNode node, Set<Context> contexts, String expressionString) {
        // TODO handle errors better

        // EXAMPLES:
        // isMaybeAnyStr(x) -> picks the contexts where the variable x is the unknown string
        // !isMaybeAnyStr(x) -> picks the contexts where the variable x is NOT the unknown string
        Pattern predicateVariablePattern = Pattern.compile("(!)?(\\w+)\\((\\w+)\\)");
        Matcher matcher = predicateVariablePattern.matcher(expressionString);
        if (!matcher.matches()) {
            return null;
        }
        boolean negated = matcher.group(1) == null;
        String predicateName = matcher.group(2);
        String variableName = matcher.group(3);
        Method method = getPredicate(predicateName); // XXX security risk

        IntermediaryStateComputer intermediaryStateComputer = new IntermediaryStateComputer(c);
        return contexts.stream()
                .filter(context -> {
                    Value variableValue = intermediaryStateComputer.withTempState(node, intermediaryStateComputer.makeIntermediaryPostState(node, context),
                            () -> UnknownValueResolver.getRealValue(c.getAnalysis().getPropVarOperations().readVariable(variableName, null, true), c.getState()));
                    try {
                        if (!variableValue.isMaybePresent()) {
                            throw new IllegalArgumentException("Variable is not in scope: " + variableName);
                        }
                        Boolean result = (Boolean) method.invoke(variableValue);
                        return negated ? !result : result;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toSet());
    }

    private Method getPredicate(String predicateName) {
        try {
            return Value.class.getMethod(predicateName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
