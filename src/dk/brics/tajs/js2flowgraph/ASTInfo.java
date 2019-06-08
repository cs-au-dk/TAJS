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

package dk.brics.tajs.js2flowgraph;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.parsing.parser.IdentifierToken;
import com.google.javascript.jscomp.parsing.parser.SourceFile;
import com.google.javascript.jscomp.parsing.parser.TokenType;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.CallExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ConditionalExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.DoWhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.EmptyStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForInStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FormalParameterListTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree.Builder;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree.Kind;
import com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.IfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberLookupExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ObjectLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThisExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.UnaryExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.UpdateExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.WhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.util.SourcePosition;
import com.google.javascript.jscomp.parsing.parser.util.SourceRange;
import dk.brics.tajs.js2flowgraph.asttraversals.InOrderVisitor;
import dk.brics.tajs.util.Collectors;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

import static dk.brics.tajs.util.Collections.addAllToMapSet;
import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

public class ASTInfo {

    // very ad-hoc information gathered during a single pass of multiple ASTs
    // could be made more general+expensive if facts about subtrees are stored at each node in the AST.

    /**
     * The loops that occur inside other loops.
     */
    private final Set<LoopTree> nestedLoops = newSet();

    /**
     * The variables read in the condition of a loop
     */
    private final Map<LoopTree, Set<String>> loopConditionVariableReads = newMap();

    /**
     * The variables written anywhere in a loop, except for the initializer.
     */
    private final Map<LoopTree, Set<String>> loopNonInitializerVariableWrites = newMap();

    /**
     * The variables read in the property part of a dynamic property access in a loop, except for in the initializer.
     */
    private final Map<LoopTree, Set<String>> loopNonInitializerDynamicPropertyVariableReads = newMap();

    /**
     * The literal instantiations ( {}, [] ) that occur in for-in loops
     */
    private final Set<LiteralTree> literalsInForIn = newSet();

    /**
     * The functions or loops that have dynamic property read in them
     * NB: If the read occurs in an inner function or loop, it is *not* registered for the outer function or loop!
     */
    private final Set<FunctionOrLoopTree> functionsOrLoopsWithDynamicPropertyReads = newSet();

    private final Set<MemberLookupExpressionTree> dynamicPropertyWrites = newSet();

    /**
     * The variables read in a literal instantiation ( {}, [] )
     */
    private Map<LiteralTree, Set<String>> variableReadsInLiterals = newMap();

    /**
     * The variables read by a function
     */
    private Map<FunctionDeclarationTree, Set<String>> functionVariableReads = newMap();

    /**
     * The variables declared by a function, including parameters
     */
    private Map<FunctionDeclarationTree, Set<String>> functionVariableDeclarations = newMap();

    /**
     * The variables read by a function that are defined in an outer function
     */
    private Map<FunctionDeclarationTree, Set<String>> functionClosureVariables = newMap();

    /**
     * The variables defined by a function that are used by inner functions (potential closures)
     */
    private Map<FunctionDeclarationTree, Set<String>> nonStackVariables = newMap();

    /**
     * The function hierarchy, where each function maps to its outer function
     */
    private Map<FunctionDeclarationTree, FunctionDeclarationTree> functionHierarchy = newMap();

    /**
     * Function declarations
     */
    private Set<FunctionDeclarationTree> functions = newSet();

    /**
     * Function declarations with `this` references, e.g. `this`.
     */
    private Set<FunctionDeclarationTree> functionsWithThisReference = newSet();
    /**
     * Functions with variables that are used as property read names, e.g. `o[p]` or `o[p] = x` -> o[p] is in the map
     */
    private Map<FunctionDeclarationTree, Set<MemberLookupExpressionTree>> functionsWithVariablesAsPropertyAccessName = newMap();

    /**
     * Functions with variables that are used as both property read and write names, e.g. `o[p]` and `o[p] = x` -> {'p'} is in the map
     */
    private Map<FunctionDeclarationTree, Set<String>> functionsWithVariableCorrelatedPropertyAccesses = newMap();

    /**
     * Conditional structures with variable reads in the condition-part, e.g. `if(x){}` -> 'x' is in the map
     */
    private Map<ConditionTree, Set<String>> conditionsWithVariableReadsInTheCondition = newMap();

    /**
     * Conditional structures with variable reads as arguments in the condition-part, e.g. `if(f(x, y)){}` -> {'[x, y]'} is in the map
     */
    private Map<ConditionTree, Set<List<String>>> conditionsWithVariableReadsAsArgumentsInTheCondition = newMap();

    /**
     * Conditional structures with variable reads in the body-parts, e.g. `if(_){x} else if{y} else {z}` -> {'x', 'y', 'z'} is in the map
     */
    private Map<ConditionTree, Set<String>> conditionsWithVariableReadsInTheBodies = newMap();

    private void updateClosureVariables(FunctionDeclarationTree function) {
        Set<String> closureVariables = newSet();
        if (functionVariableReads.containsKey(function)) {
            closureVariables.addAll(functionVariableReads.get(function));
        }
        Set<String> declarations = newSet();
        if (functionVariableDeclarations.containsKey(function)) {
            declarations.addAll(functionVariableDeclarations.get(function));
        }
        closureVariables.removeAll(declarations);

        Set<String> closureVariablesNotDefinedInOuterFunctions = newSet(closureVariables);
        FunctionDeclarationTree outer = function;
        while (null != (outer = functionHierarchy.get(outer)) && !closureVariablesNotDefinedInOuterFunctions.isEmpty()) {
            Set<String> outerDeclarations = newSet();
            if (functionVariableDeclarations.containsKey(outer)) {
                outerDeclarations.addAll(functionVariableDeclarations.get(outer));

                Set<String> closureVariablesOfOuterFunction = newSet(closureVariablesNotDefinedInOuterFunctions);
                closureVariablesOfOuterFunction.retainAll(functionVariableDeclarations.get(outer));
                addAllToMapSet(nonStackVariables, outer, closureVariablesOfOuterFunction);
            }
            closureVariablesNotDefinedInOuterFunctions.removeAll(outerDeclarations);
        }
        closureVariables.removeAll(closureVariablesNotDefinedInOuterFunctions);

        functionClosureVariables.put(function, closureVariables);
    }

    public Map<FunctionDeclarationTree, Set<String>> getFunctionClosureVariables() {
        return functionClosureVariables;
    }

    public Set<FunctionDeclarationTree> getFunctionsWithThisReference() {
        return functionsWithThisReference;
    }

    public Map<FunctionDeclarationTree, Set<String>> getNonStackVariables() {
        return nonStackVariables;
    }

    public Set<LiteralTree> getLiteralsInForIn() {
        return literalsInForIn;
    }

    public Map<LoopTree, Set<String>> getLoopConditionVariableReads() {
        return loopConditionVariableReads;
    }

    public Map<LoopTree, Set<String>> getLoopNonInitializerDynamicPropertyVariableReads() {
        return loopNonInitializerDynamicPropertyVariableReads;
    }

    public Map<LoopTree, Set<String>> getLoopNonInitializerVariableWrites() {
        return loopNonInitializerVariableWrites;
    }

    public Set<LoopTree> getNestedLoops() {
        return nestedLoops;
    }

    public Map<LiteralTree, Set<String>> getVariableReadsInLiterals() {
        return variableReadsInLiterals;
    }

    public Set<FunctionOrLoopTree> getFunctionsOrLoopsWithDynamicPropertyReads() {
        return functionsOrLoopsWithDynamicPropertyReads;
    }

    public Map<ConditionTree, Set<String>> getConditionsWithVariableReadsInTheCondition() {
        return conditionsWithVariableReadsInTheCondition;
    }

    public Map<ConditionTree, Set<List<String>>> getConditionsWithVariableReadsAsArgumentsInTheCondition() {
        return conditionsWithVariableReadsAsArgumentsInTheCondition;
    }

    public Map<ConditionTree, Set<String>> getConditionsWithVariableReadsInTheBodies() {
        return conditionsWithVariableReadsInTheBodies;
    }

    /**
     * Does a single pass of the tree, extracting relevant information.
     */
    public void updateWith(ProgramTree tree) {
        Set<FunctionDeclarationTree> oldFunctions = newSet(functions);
        new InfoVisitor().process(tree);
        Set<FunctionDeclarationTree> newFunctions = newSet(functions);
        newFunctions.removeAll(oldFunctions);
        newFunctions.forEach(this::updateClosureVariables);
        newFunctions.forEach(this::updateFunctionsWithVariableCorrelatedPropertyAccesses);
    }

    public Map<ConditionTree, Set<String>> getConditionRefinedVariables() {
        Map<ConditionTree, Set<String>> map = newMap();
        conditionsWithVariableReadsInTheCondition.forEach((tree, conditionVariables) -> {
            Set<String> bodyVariables = conditionsWithVariableReadsInTheBodies.getOrDefault(tree, newSet());
            Set<String> refinedVariables = newSet(conditionVariables);
            refinedVariables.retainAll(bodyVariables);
            if (!refinedVariables.isEmpty()) {
                map.put(tree, refinedVariables);
            }
        });
        return map;
    }

    public Map<ConditionTree, Set<List<Optional<String>>>> getConditionRefinedArgumentVariables() {
        Map<ConditionTree, Set<List<Optional<String>>>> map = newMap();
        conditionsWithVariableReadsAsArgumentsInTheCondition.forEach((tree, conditionArgumentVariablesLists) -> {
            Set<String> bodyVariables = conditionsWithVariableReadsInTheBodies.getOrDefault(tree, newSet());
            conditionArgumentVariablesLists.forEach(conditionArgumentVariables -> {
                List<Optional<String>> refinedVariables = conditionArgumentVariables.stream()
                        .map(conditionArgumentVariable -> bodyVariables.contains(conditionArgumentVariable) ? Optional.of(conditionArgumentVariable) : Optional.<String>empty())
                        .collect(Collectors.toList());
                if (refinedVariables.stream().anyMatch(Optional::isPresent)) {
                    addToMapSet(map, tree, refinedVariables);
                }
            });
        });
        return map;
    }

    public Map<ConditionTree, Set<String>> getConditionRefined1ArgumentVariables() {
        Map<ConditionTree, Set<String>> map = newMap();
        getConditionRefinedArgumentVariables().forEach((tree, argumentLists) -> {
            Set<String> variables = argumentLists.stream()
                    .filter(list -> list.size() == 1)
                    .map(list -> list.get(0))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            if (!variables.isEmpty()) {
                addAllToMapSet(map, tree, variables);
            }
        });
        return map;
    }

    private void updateFunctionsWithVariableCorrelatedPropertyAccesses(FunctionDeclarationTree fun) {
        if (!functionsWithVariablesAsPropertyAccessName.containsKey(fun)) {
            return;
        }
        Set<MemberLookupExpressionTree> es = functionsWithVariablesAsPropertyAccessName.get(fun);
        Set<MemberLookupExpressionTree> readTrees = es.stream()
                .filter(dynamicPropertyWrites::contains)
                .collect(Collectors.toSet());
        Set<MemberLookupExpressionTree> writeTrees = newSet(es);
        writeTrees.removeAll(readTrees);
        Function<MemberLookupExpressionTree, String> getVariableName = e -> e.memberExpression.asIdentifierExpression().identifierToken.value;
        Set<String> reads = readTrees.stream()
                .map(getVariableName)
                .collect(Collectors.toSet());
        Set<String> writes = writeTrees.stream()
                .map(getVariableName)
                .collect(Collectors.toSet());
        Set<String> readsAndWrites = newSet(reads);
        readsAndWrites.retainAll(writes);
        if (!readsAndWrites.isEmpty()) {
            functionsWithVariableCorrelatedPropertyAccesses.put(fun, readsAndWrites);
        }
    }

    public Map<FunctionDeclarationTree, Set<MemberLookupExpressionTree>> getFunctionsWithVariablesAsPropertyAccessName() {
        return functionsWithVariablesAsPropertyAccessName;
    }

    public Map<FunctionDeclarationTree, Set<String>> getFunctionsWithVariableCorrelatedPropertyAccesses() {
        return functionsWithVariableCorrelatedPropertyAccesses;
    }

    /**
     * Type safe wrapper structure for different loop ASTs
     */
    public static class LoopTree {

        private final ParseTree tree;

        public LoopTree(WhileStatementTree tree) {
            this.tree = tree;
        }

        public LoopTree(ForStatementTree tree) {
            this.tree = tree;
        }

        public LoopTree(DoWhileStatementTree tree) {
            this.tree = tree;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LoopTree loopTree = (LoopTree) o;

            return Objects.equals(tree, loopTree.tree);
        }

        @Override
        public int hashCode() {
            return tree != null ? tree.hashCode() : 0;
        }
    }

    /**
     * Type safe wrapper structure for different literal constructor ASTs
     */
    public static class LiteralTree {

        private final ParseTree tree;

        public LiteralTree(ObjectLiteralExpressionTree tree) {
            this.tree = tree;
        }

        public LiteralTree(ArrayLiteralExpressionTree tree) {
            this.tree = tree;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LiteralTree that = (LiteralTree) o;

            return Objects.equals(tree, that.tree);
        }

        @Override
        public int hashCode() {
            return tree != null ? tree.hashCode() : 0;
        }
    }

    public static class ConditionTree {

        public final ParseTree tree;

        public ConditionTree(ParseTree tree) {
            this.tree = tree;
        }

        public ConditionTree(IfStatementTree tree) {
            this((ParseTree) tree);
        }

        public ConditionTree(ConditionalExpressionTree tree) {
            this((ParseTree) tree);
        }

        public ConditionTree(BinaryOperatorTree tree) {
            this((ParseTree) tree);
        }

        public static ConditionTree makeUnsafe(ParseTree tree) {
            return new ConditionTree(tree);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ConditionTree that = (ConditionTree) o;

            return Objects.equals(tree, that.tree);
        }

        @Override
        public int hashCode() {
            return tree != null ? tree.hashCode() : 0;
        }
    }

    public static class FunctionOrLoopTree {

        private final ParseTree tree;

        private FunctionOrLoopTree(ParseTree tree) {
            this.tree = tree;
        }

        public FunctionOrLoopTree(FunctionDeclarationTree tree) {
            this((ParseTree) tree);
        }

        public FunctionOrLoopTree(LoopTree tree) {
            this(tree.tree);
        }

        public boolean isFunction() {
            return tree instanceof FunctionDeclarationTree;
        }
    }

    /**
     * Actual tree inorder-visitor. Stores tree information during descend using many stacks, writing relevant facts to {@link dk.brics.tajs.js2flowgraph.ASTInfo} when possible
     */
    private class InfoVisitor extends InOrderVisitor {

        private final Stack<FunctionOrLoopTree> functionOrLoopNesting = new Stack<>();

        private final Stack<FunctionDeclarationTree> functionNesting = new Stack<>();

        private final Stack<LoopTree> nonForInLoopNesting = new Stack<>();

        private final Stack<LiteralTree> literalNesting = new Stack<>();

        private final Stack<MemberLookupExpressionTree> dynamicPropertyAccessNesting = new Stack<>();

        private final Stack<LoopTree> loopConditionNesting = new Stack<>();

        private final Stack<LoopTree> loopNonInitializerNesting = new Stack<>();

        private final Stack<ForInStatementTree> forInLoopNesting = new Stack<>();

        private final Set<IdentifierExpressionTree> variableWrites = newSet();

        private final Stack<ConditionTree> conditionConditionNesting = new Stack<>();

        private final Stack<ConditionTree> conditionBodyNesting = new Stack<>();

        public InfoVisitor() {
            Builder builder = FunctionDeclarationTree.builder(Kind.DECLARATION);
            builder.setName(new IdentifierToken(null, "DUMMY_GLOBAL"));
            SourcePosition dummyPos = new SourcePosition(new SourceFile("DUMMY_GLOBAL_FILE", ""), 0, 0, 0);
            SourceRange dummyRange = new SourceRange(dummyPos, dummyPos);
            builder.setFormalParameterList(new FormalParameterListTree(dummyRange, ImmutableList.of()));
            builder.setFunctionBody(new EmptyStatementTree(dummyRange));
            FunctionDeclarationTree dummyGlobal = builder.build(dummyRange);
            functionNesting.push(dummyGlobal);
            functionOrLoopNesting.push(new FunctionOrLoopTree(dummyGlobal));
        }


        @Override
        public void in(ThisExpressionTree tree) {
            functionsWithThisReference.add(functionNesting.peek());
        }


        @Override
        public void in(ForInStatementTree tree) {
            forInLoopNesting.push(tree);
            functionOrLoopNesting.push(new FunctionOrLoopTree(tree));
        }

        @Override
        public void in(IdentifierExpressionTree tree) {
            if (!variableWrites.contains(tree)) {
                registerVariableRead(tree);
            }
        }

        @Override
        public void in(MemberLookupExpressionTree tree) {
            if (!dynamicPropertyWrites.contains(tree)) {
                registerDynamicPropertyRead(tree);
            }
        }

        @Override
        public void in(UpdateExpressionTree tree) {
            if (tree.operand.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
                registerVariableRead(tree.operand.asIdentifierExpression());
                registerVariableWrite(tree.operand.asIdentifierExpression());
            }

            if (tree.operand.type == ParseTreeType.MEMBER_LOOKUP_EXPRESSION) {
                registerDynamicPropertyRead(tree.operand.asMemberLookupExpression());
                registerDynamicPropertyWrite(tree.operand.asMemberLookupExpression());
            }
        }

        @Override
        public void in(FunctionDeclarationTree tree) {
            functionHierarchy.put(tree, functionNesting.peek());
            functionNesting.push(tree);
            functionOrLoopNesting.push(new FunctionOrLoopTree(tree));
            functions.add(tree);
        }

        @Override
        public void in(UnaryExpressionTree tree) {
            switch (tree.operator.type) {
                case PLUS_PLUS:
                case MINUS_MINUS: {
                    if (tree.operand.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
                        registerVariableRead(tree.operand.asIdentifierExpression());
                        registerVariableWrite(tree.operand.asIdentifierExpression());
                    }
                    if (tree.operand.type == ParseTreeType.MEMBER_LOOKUP_EXPRESSION) {
                        registerDynamicPropertyRead(tree.operand.asMemberLookupExpression());
                        registerDynamicPropertyWrite(tree.operand.asMemberLookupExpression());
                    }
                }
                default:
                    // ignore
            }
        }

        @Override
        public void in(BinaryOperatorTree tree) {
            if (ClosureASTUtil.isAssignment(tree)) {
                if (tree.operator.type != TokenType.EQUAL) {
                    // compound assignment: a read is also performed
                    if (tree.left.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
                        registerVariableRead(tree.left.asIdentifierExpression());
                    }
                    if (tree.left.type == ParseTreeType.MEMBER_LOOKUP_EXPRESSION) {
                        registerDynamicPropertyRead(tree.left.asMemberLookupExpression());
                    }
                }
                if (tree.left.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
                    registerVariableWrite(tree.left.asIdentifierExpression());
                }
                if (tree.left.type == ParseTreeType.MEMBER_LOOKUP_EXPRESSION) {
                    registerDynamicPropertyWrite(tree.left.asMemberLookupExpression());
                }
            }
        }

        @Override
        public void in(WhileStatementTree tree) {
            inNonForInLoop(new LoopTree(tree));
        }

        @Override
        public void in(ForStatementTree tree) {
            inNonForInLoop(new LoopTree(tree));
        }

        @Override
        public void in(DoWhileStatementTree tree) {
            inNonForInLoop(new LoopTree(tree));
        }

        @Override
        public void in(ArrayLiteralExpressionTree tree) {
            LiteralTree literalTree = new LiteralTree(tree);
            literalNesting.push(literalTree);
            if (!forInLoopNesting.isEmpty()) {
                literalsInForIn.add(new LiteralTree(tree));
            }
        }

        @Override
        public void in(ObjectLiteralExpressionTree tree) {
            LiteralTree literalTree = new LiteralTree(tree);
            literalNesting.push(literalTree);
            if (!forInLoopNesting.isEmpty()) {
                literalsInForIn.add(literalTree);
            }
        }

        @Override
        public void in(VariableDeclarationTree tree) {
            if (tree.lvalue.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
                String variableName = tree.lvalue.asIdentifierExpression().identifierToken.value;
                addToMapSet(functionVariableDeclarations, functionNesting.peek(), variableName);
            }
        }

        @Override
        public void in(FormalParameterListTree tree) {
            for (ParseTree parameter : tree.parameters) {
                if (parameter.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
                    final String parameterName = parameter.asIdentifierExpression().identifierToken.value;
                    addToMapSet(functionVariableDeclarations, functionNesting.peek(), parameterName);
                }
            }
        }

        private void inNonForInLoop(LoopTree loopTree) {
            if (!nonForInLoopNesting.isEmpty() && !functionOrLoopNesting.peek().isFunction()) {
                nestedLoops.add(loopTree);
            }
            nonForInLoopNesting.push(loopTree);
            functionOrLoopNesting.push(new FunctionOrLoopTree(loopTree));
        }

        @Override
        public void out(FunctionDeclarationTree tree) {
            functionNesting.pop();
            functionOrLoopNesting.pop();
        }

        @Override
        public void out(ArrayLiteralExpressionTree tree) {
            literalNesting.pop();
        }

        @Override
        public void out(ObjectLiteralExpressionTree tree) {
            literalNesting.pop();
        }

        @Override
        public void out(ForInStatementTree tree) {
            forInLoopNesting.pop();
            functionOrLoopNesting.pop();
        }

        @Override
        public void out(WhileStatementTree tree) {
            outNonForInLoop();
        }

        @Override
        public void out(ForStatementTree tree) {
            outNonForInLoop();
        }

        private void outNonForInLoop() {
            nonForInLoopNesting.pop();
            functionOrLoopNesting.pop();
        }

        @Override
        public Void process(MemberLookupExpressionTree tree) {
            in(tree);
            process(tree.operand);
            dynamicPropertyAccessNesting.push(tree);
            {
                process(tree.memberExpression);
            }
            dynamicPropertyAccessNesting.pop();
            out(tree);
            return null;
        }

        @Override
        public Void process(IfStatementTree tree) {
            in(tree);
            conditionConditionNesting.push(new ConditionTree(tree));
            {
                process(tree.condition);
            }
            conditionConditionNesting.pop();
            conditionBodyNesting.push(new ConditionTree(tree));
            {
                process(tree.ifClause);
                process(tree.elseClause);
            }
            conditionBodyNesting.pop();
            out(tree);
            return null;
        }

        @Override
        public void out(CallExpressionTree tree) {
            if (!conditionConditionNesting.isEmpty()) {
                List<String> argumentVariables =
                        tree.arguments.arguments.stream()
                                .filter(t -> t.type == ParseTreeType.IDENTIFIER_EXPRESSION)
                                .map(t -> t.asIdentifierExpression().identifierToken.value)
                                .collect(Collectors.toList());
                addToMapSet(conditionsWithVariableReadsAsArgumentsInTheCondition, conditionConditionNesting.peek(), argumentVariables);
            }
        }

        @Override
        public Void process(ForInStatementTree tree) {
            in(tree);
            if (tree.initializer.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
                registerVariableWrite(tree.initializer.asIdentifierExpression());
            }
            if (tree.initializer.type == ParseTreeType.MEMBER_LOOKUP_EXPRESSION) {
                registerDynamicPropertyWrite(tree.initializer.asMemberLookupExpression());
            }
            {
                process(tree.initializer);
                process(tree.collection);
                process(tree.body);
            }
            out(tree);
            return null;
        }

        @Override
        public Void process(ForStatementTree tree) {
            in(tree);
            process(tree.initializer);
            {
                loopNonInitializerNesting.push(new LoopTree(tree));
                {
                    loopConditionNesting.push(new LoopTree(tree));
                    process(tree.condition);
                    loopConditionNesting.pop();
                }
                process(tree.increment);
                process(tree.body);
                loopNonInitializerNesting.pop();
            }
            out(tree);
            return null;
        }

        @Override
        public Void process(WhileStatementTree tree) {
            in(tree);
            {
                loopNonInitializerNesting.push(new LoopTree(tree));
                {
                    loopConditionNesting.push(new LoopTree(tree));
                    process(tree.condition);
                    loopConditionNesting.pop();
                }
                process(tree.body);
                loopNonInitializerNesting.pop();
            }
            out(tree);
            return null;
        }

        private void registerDynamicPropertyWrite(MemberLookupExpressionTree tree) {
            dynamicPropertyWrites.add(tree);
            if (tree.memberExpression.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
                addToMapSet(functionsWithVariablesAsPropertyAccessName, functionNesting.peek(), tree);
            }
        }

        private void registerDynamicPropertyRead(MemberLookupExpressionTree tree) {
            functionsOrLoopsWithDynamicPropertyReads.add(functionOrLoopNesting.peek());
            if (tree.memberExpression.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
                addToMapSet(functionsWithVariablesAsPropertyAccessName, functionNesting.peek(), tree);
            }
        }

        private void registerVariableRead(IdentifierExpressionTree tree) {
            String variableName = tree.identifierToken.value;
            for (LoopTree loopTree : loopConditionNesting) {
                addToMapSet(loopConditionVariableReads, loopTree, variableName);
            }
            for (LoopTree loopTree : loopNonInitializerNesting) {
                if (!dynamicPropertyAccessNesting.isEmpty()) {
                    addToMapSet(loopNonInitializerDynamicPropertyVariableReads, loopTree, variableName);
                }
            }
            for (LiteralTree literalTree : literalNesting) {
                addToMapSet(variableReadsInLiterals, literalTree, variableName);
            }
            for (ConditionTree conditionTree : conditionConditionNesting) {
                addToMapSet(conditionsWithVariableReadsInTheCondition, conditionTree, variableName);
            }
            for (ConditionTree conditionTree : conditionBodyNesting) {
                addToMapSet(conditionsWithVariableReadsInTheBodies, conditionTree, variableName);
            }

            addToMapSet(functionVariableReads, functionNesting.peek(), variableName);
        }

        private void registerVariableWrite(IdentifierExpressionTree tree) {
            variableWrites.add(tree);
            for (LoopTree loopTree : loopNonInitializerNesting) {
                addToMapSet(loopNonInitializerVariableWrites, loopTree, tree.identifierToken.value);
            }
        }
    }
}
