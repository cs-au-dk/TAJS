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

package dk.brics.tajs.flowgraph;

import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.js2flowgraph.ConditionPatternMatcher.ConditionPattern;
import dk.brics.tajs.js2flowgraph.Reference;
import dk.brics.tajs.util.AnalysisException;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addAllToMapSet;
import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * @see SyntacticInformation
 */
public class WritableSyntacticInformation {

    private final Map<AbstractNode, Set<String>> variableDependencies;

    private final Set<Function> correlatedAccessFunctions;

    private final Set<AbstractNode> inForIn;

    private final Map<Function, Set<String>> loopVariables;

    private final Set<CallNode> tajsCallsWithLiteralFalseAsFirstOrFourthArgument;

    private final Map<IfNode, Set<ConditionPattern>> conditionPatterns;

    private final Map<AbstractNode, Reference> nodeWithBaseReferences;

    private final Map<ParseTree, Reference> simpleReads;

    private final Map<ParseTree, Integer> expressionRegisters;

    private final Map<IfNode, Set<String>> conditionRefined1ArgumentVariables;

    private final Map<IfNode, Set<String>> conditionRefinedArgumentVariables;

    private final Map<Function, Set<String>> stackVariables;

    private final Map<Function, Set<String>> functionClosureVariables;

    private final Set<Function> functionsWithThisReference;

    private final SyntacticInformation readOnly;

    public WritableSyntacticInformation() {
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
        this.readOnly = new SyntacticInformation();
    }

    // remember to keep this up to date!!!
    public void add(WritableSyntacticInformation other) {
        variableDependencies.putAll(other.variableDependencies);
        correlatedAccessFunctions.addAll(other.correlatedAccessFunctions);
        inForIn.addAll(other.inForIn);
        loopVariables.putAll(other.loopVariables);
        tajsCallsWithLiteralFalseAsFirstOrFourthArgument.addAll(other.tajsCallsWithLiteralFalseAsFirstOrFourthArgument);
        conditionPatterns.putAll(other.conditionPatterns);
        nodeWithBaseReferences.putAll(other.nodeWithBaseReferences);
        simpleReads.putAll(other.simpleReads);
        expressionRegisters.putAll(other.expressionRegisters);
        conditionRefined1ArgumentVariables.putAll(other.conditionRefined1ArgumentVariables);
        conditionRefinedArgumentVariables.putAll(other.conditionRefinedArgumentVariables);
        stackVariables.putAll(other.stackVariables);
        functionClosureVariables.putAll(other.functionClosureVariables);
        functionsWithThisReference.addAll(other.functionsWithThisReference);
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

    public void registerTAJSCallWithLiteralFalseAsFirstOrFourthArgument(CallNode callNode) {
        tajsCallsWithLiteralFalseAsFirstOrFourthArgument.add(callNode);
    }

    public void registerIfConditionPatterns(IfNode ifNode, Set<ConditionPattern> patterns) {
        addAllToMapSet(this.conditionPatterns, ifNode, patterns);
    }

    public void registerAssociatedBaseReference(AbstractNode node, Reference base) {
        this.nodeWithBaseReferences.put(node, base);
    }

    public void registerSimpleRead(ParseTree tree, Reference target) {
        this.simpleReads.put(tree, target);
    }

    public void registerExpressionRegister(ParseTree expressionTree, int resultRegister) {
        this.expressionRegisters.put(expressionTree, resultRegister);
    }

    public void registerConditionRefined1ArgumentVariables(IfNode ifNode, Set<String> variables) {
        this.conditionRefined1ArgumentVariables.put(ifNode, variables);
    }

    public void registerConditionRefinedArgumentVariables(IfNode ifNode, Set<String> variables) {
        this.conditionRefinedArgumentVariables.put(ifNode, variables);
    }

    public void registerStackVariables(Function function, Set<String> stackVariables) {
        this.stackVariables.put(function, stackVariables);
    }

    public void registerFunctionClosureVariables(Function function, Set<String> variables) {
        functionClosureVariables.put(function, variables);
    }

    public void registerFunctionWithThisReference(Function function) {
        functionsWithThisReference.add(function);
    }

    public SyntacticInformation asReadOnly() {
        return readOnly;
    }

    /**
     * Extra syntatic information.
     */
    @SuppressWarnings("unused") // The use of this information varies: future heuristics might find the information useful, even though it is not used at the moment.
    public class SyntacticInformation {

        /**
         * True iff the given variable appears syntactically in the condition of a non-nested loop
         * and in a dynamic property read operation in the given function.
         */
        public boolean isLoopVariable(Function function, String variable) {
            return loopVariables.containsKey(function) && loopVariables.get(function).contains(variable);
        }

        /**
         * True iff the given parameter is used in the given literal constructor.
         */
        public boolean doesLiteralReferenceParameter(AbstractNode literalAllocationNode, String parameterName) {
            return variableDependencies.containsKey(literalAllocationNode) && variableDependencies.get(literalAllocationNode).contains(parameterName);
        }

        /**
         * True iff the given node is inside a for-in loop.
         */
        public boolean isInForIn(AbstractNode node) {
            return inForIn.contains(node);
        }

        /**
         * True iff the given function contains variables that are used as both property read and write names.
         */
        public boolean isCorrelatedAccessFunction(Function function) {
            return correlatedAccessFunctions.contains(function);
        }

        /**
         * The set of CallNodes to TAJS_* function with 'false' as first or fourth argument.
         */
        public Set<CallNode> getTajsCallsWithLiteralFalseAsFirstOrFourthArgument() {
            return tajsCallsWithLiteralFalseAsFirstOrFourthArgument;
        }

        /**
         * The set of pattern-matched conditions used in the condition of an if-node.
         */
        public Set<ConditionPattern> getConditionPattern(IfNode ifNode) {
            if (conditionPatterns.containsKey(ifNode)) {
                return conditionPatterns.get(ifNode);
            }
            return newSet();
        }

        /**
         * The base-reference of a property access.
         */
        public Optional<Reference> getBaseReference(AbstractNode node) {
            if (nodeWithBaseReferences.containsKey(node)) {
                return Optional.of(nodeWithBaseReferences.get(node));
            }
            return Optional.empty();
        }

        /**
         * True iff the parse tree is a simple read.
         */
        public boolean isSimpleRead(ParseTree tree) {
            return simpleReads.containsKey(tree);
        }

        /**
         * The associated reference for a simple read.
         */
        public Reference getSimpleRead(ParseTree tree) {
            return simpleReads.get(tree);
        }

        /**
         * The register used for storing the result of an expression.
         */
        public int getExpressionRegister(ParseTree tree) {
            if (!expressionRegisters.containsKey(tree)) {
                throw new AnalysisException("No register associated with tree: " + tree + "?!?");
            }
            return expressionRegisters.get(tree);
        }

        /**
         * The variables that are used as the first argument to a function call in a condition, where the variable is also used in he body of the condition.
         */
        public Set<String> getConditionRefined1ArgumentVariables(IfNode ifNode) {
            return conditionRefined1ArgumentVariables.getOrDefault(ifNode, newSet());
        }

        /**
         * The variables that are used as arguments to a function call in a condition, where the variable is also used in he body of the condition.
         */
        public Set<String> getConditionRefinedArgumentVariables(IfNode ifNode) {
            return conditionRefinedArgumentVariables.getOrDefault(ifNode, newSet());
        }

        /**
         * True iff the variable could be allocated on the stack (i.e. not referenced by any closures).
         */
        public boolean isStackVariable(Function function, String variableName) {
            return stackVariables.getOrDefault(function, newSet()).contains(variableName);
        }

        /**
         * The set of free variables of a function that are bound in an outer function (closure variables).
         */
        public Set<String> getClosureVariableNames(Function function) {
            return functionClosureVariables.get(function);
        }

        /**
         * True iff the function has an occurence of 'this'.
         */
        public boolean isFunctionWithThisReference(Function function) {
            return functionsWithThisReference.contains(function);
        }
    }
}
