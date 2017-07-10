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

package dk.brics.tajs.flowgraph.syntaticinfo;

import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.util.AnalysisException;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Syntactic information that eventually is used in {@link SyntacticQueries}.
 */
public class RawSyntacticInformation {

    private final Map<AbstractNode, Set<String>> variableDependencies;

    private final Set<Function> correlatedAccessFunctions;

    private final Set<AbstractNode> inForIn;

    private final Map<Function, Set<String>> loopVariables;

    private final Set<CallNode> tajsCallsWithLiteralFalseAsFirstOrFourthArgument;

    private final Map<IfNode, Set<ConditionPattern>> conditionPatterns;

    private final Map<AbstractNode, SyntacticReference> nodeWithBaseReferences;

    private final Map<ParseTree, SyntacticReference> simpleReads;

    private final Map<ParseTree, Integer> expressionRegisters;

    private final Map<IfNode, Set<String>> conditionRefined1ArgumentVariables;

    private final Map<IfNode, Set<String>> conditionRefinedArgumentVariables;

    private final Map<Function, Set<String>> stackVariables;

    private final Map<Function, Set<String>> functionClosureVariables;

    private final Set<Function> functionsWithThisReference;

    public RawSyntacticInformation() {
        this.variableDependencies = newMap();
        this.correlatedAccessFunctions = newSet();
        this.inForIn = newSet();
        this.loopVariables = newMap();
        this.tajsCallsWithLiteralFalseAsFirstOrFourthArgument = newSet();
        this.conditionPatterns = newMap();
        this.nodeWithBaseReferences = newMap();
        this.simpleReads = newMap();
        this.expressionRegisters = newMap();
        this.conditionRefined1ArgumentVariables = newMap();
        this.conditionRefinedArgumentVariables = newMap();
        this.stackVariables = newMap();
        this.functionClosureVariables = newMap();
        this.functionsWithThisReference = newSet();
    }

    public Map<AbstractNode, Set<String>> getVariableDependencies() {
        return variableDependencies;
    }

    public Set<Function> getCorrelatedAccessFunctions() {
        return correlatedAccessFunctions;
    }

    public Set<AbstractNode> getInForIn() {
        return inForIn;
    }

    public Map<Function, Set<String>> getLoopVariables() {
        return loopVariables;
    }

    public Set<CallNode> getTajsCallsWithLiteralFalseAsFirstOrFourthArgument() {
        return tajsCallsWithLiteralFalseAsFirstOrFourthArgument;
    }

    public Map<IfNode, Set<ConditionPattern>> getConditionPatterns() {
        return conditionPatterns;
    }

    public Map<AbstractNode, SyntacticReference> getNodeWithBaseReferences() {
        return nodeWithBaseReferences;
    }

    public Map<ParseTree, SyntacticReference> getSimpleReads() {
        return simpleReads;
    }

    public Map<ParseTree, Integer> getExpressionRegisters() {
        return expressionRegisters;
    }

    public Map<IfNode, Set<String>> getConditionRefined1ArgumentVariables() {
        return conditionRefined1ArgumentVariables;
    }

    public Map<IfNode, Set<String>> getConditionRefinedArgumentVariables() {
        return conditionRefinedArgumentVariables;
    }

    public Map<Function, Set<String>> getStackVariables() {
        return stackVariables;
    }

    public Map<Function, Set<String>> getFunctionClosureVariables() {
        return functionClosureVariables;
    }

    public Set<Function> getFunctionsWithThisReference() {
        return functionsWithThisReference;
    }

    public SyntacticQueries getQueryView() {
        return new QueryView();
    }

    /**
     * A query view of the raw information.
     */
    private class QueryView implements SyntacticQueries {

        @Override
        public boolean isLoopVariable(Function function, String variable) {
            return loopVariables.containsKey(function) && loopVariables.get(function).contains(variable);
        }

        @Override
        public boolean doesLiteralReferenceParameter(AbstractNode literalAllocationNode, String parameterName) {
            return variableDependencies.containsKey(literalAllocationNode) && variableDependencies.get(literalAllocationNode).contains(parameterName);
        }

        @Override
        public boolean isInForIn(AbstractNode node) {
            return inForIn.contains(node);
        }

        @Override
        public boolean isCorrelatedAccessFunction(Function function) {
            return correlatedAccessFunctions.contains(function);
        }

        @Override
        public Set<CallNode> getTajsCallsWithLiteralFalseAsFirstOrFourthArgument() {
            return tajsCallsWithLiteralFalseAsFirstOrFourthArgument;
        }

        @Override
        public Set<ConditionPattern> getConditionPattern(IfNode ifNode) {
            if (conditionPatterns.containsKey(ifNode)) {
                return conditionPatterns.get(ifNode);
            }
            return newSet();
        }

        @Override
        public Optional<SyntacticReference> getBaseReference(AbstractNode node) {
            if (nodeWithBaseReferences.containsKey(node)) {
                return Optional.of(nodeWithBaseReferences.get(node));
            }
            return Optional.empty();
        }

        @Override
        public boolean isSimpleRead(ParseTree tree) {
            return simpleReads.containsKey(tree);
        }

        @Override
        public SyntacticReference getSimpleRead(ParseTree tree) {
            return simpleReads.get(tree);
        }

        @Override
        public int getExpressionRegister(ParseTree tree) {
            if (!expressionRegisters.containsKey(tree)) {
                throw new AnalysisException("No register associated with tree: " + tree + "?!?");
            }
            return expressionRegisters.get(tree);
        }

        @Override
        public Set<String> getConditionRefined1ArgumentVariables(IfNode ifNode) {
            return conditionRefined1ArgumentVariables.getOrDefault(ifNode, newSet());
        }

        @Override
        public Set<String> getConditionRefinedArgumentVariables(IfNode ifNode) {
            return conditionRefinedArgumentVariables.getOrDefault(ifNode, newSet());
        }

        @Override
        public boolean isStackVariable(Function function, String variableName) {
            return stackVariables.getOrDefault(function, newSet()).contains(variableName);
        }

        @Override
        public Set<String> getClosureVariableNames(Function function) {
            return functionClosureVariables.get(function);
        }

        @Override
        public boolean isFunctionWithThisReference(Function function) {
            return functionsWithThisReference.contains(function);
        }
    }
}
