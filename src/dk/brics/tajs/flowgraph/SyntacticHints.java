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

package dk.brics.tajs.flowgraph;

import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Syntactic hints to be used for context sensitivity.
 */
public class SyntacticHints {

    private final Map<AbstractNode, Set<String>> variableDependencies;

    private final Set<Function> correlatedAccessFunctions;

    private final Set<AbstractNode> inForIn;

    private final Map<Function, Set<String>> loopVariables;

    public SyntacticHints() {
        this.variableDependencies = newMap();
        this.correlatedAccessFunctions = newSet();
        this.inForIn = newSet();
        this.loopVariables = newMap();
    }

    public void registerInForIn(AbstractNode literalNode) {
        inForIn.add(literalNode);
    }

    public void registerVariableDependency(AbstractNode literalNode, String variableRead) {
        addToMapSet(variableDependencies, literalNode, variableRead);
    }

    public void registerLoopVariable(Function function, String candidateVariable) {
        addToMapSet(loopVariables, function, candidateVariable);
    }

    public void registerCorrelatedAccessFunction(Function function) {
        correlatedAccessFunctions.add(function);
    }

    /**
     * Checks whether the given variable appears syntactically in the condition of a non-nested loop
     * and in a dynamic property read operation in the given function.
     */
    public boolean isLoopVariable(Function function, String variable) {
        return loopVariables.containsKey(function) && loopVariables.get(function).contains(variable);
    }

    /**
     * Checks whether the given parameter is used in the given literal constructor.
     */
    public boolean doesLiteralReferenceParameter(AbstractNode literalAllocationNode, String parameterName) {
        return variableDependencies.containsKey(literalAllocationNode) && variableDependencies.get(literalAllocationNode).contains(parameterName);
    }

    public boolean isInForIn(AbstractNode node) { // TODO: unused? javadoc?
        return inForIn.contains(node);
    }

    /**
     * Checks whether the given function contains variables that are used as both property read and write names.
     */
    public boolean isCorrelatedAccessFunction(Function function) {
        return correlatedAccessFunctions.contains(function);
    }
}
