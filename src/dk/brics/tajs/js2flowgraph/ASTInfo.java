package dk.brics.tajs.js2flowgraph;

import com.google.javascript.jscomp.parsing.parser.IdentifierToken;
import com.google.javascript.jscomp.parsing.parser.TokenType;
import com.google.javascript.jscomp.parsing.parser.trees.*;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

import static dk.brics.tajs.util.Collections.*;

/**
 * Misc. information about the AST
 */
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
     * The function hierarchy, where each function maps to its outer function
     */
    private Map<FunctionDeclarationTree, FunctionDeclarationTree> functionHierarchy = newMap();
    /**
     * Function declarations
     */
    private Set<FunctionDeclarationTree> functions = newSet();

    private void findClosureVariables(FunctionDeclarationTree function) {
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
            }
            closureVariablesNotDefinedInOuterFunctions.removeAll(outerDeclarations);
        }
        closureVariables.removeAll(closureVariablesNotDefinedInOuterFunctions);

        functionClosureVariables.put(function, closureVariables.isEmpty() ? null : closureVariables);
    }

    public Map<FunctionDeclarationTree, Set<String>> getFunctionClosureVariables() {
        return functionClosureVariables;
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

    /**
     * Does a single pass of the tree, extracting relevant information.
     */
    public void updateWith(ProgramTree tree) {
        Set<FunctionDeclarationTree> oldFunctions = newSet(functions);
        new InfoVisitor().process(tree);
        Set<FunctionDeclarationTree> newFunctions = newSet(functions);
        newFunctions.removeAll(oldFunctions);
        for (FunctionDeclarationTree newFunction : newFunctions) {
            findClosureVariables(newFunction);
        }
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final LoopTree loopTree = (LoopTree) o;

            if (!tree.equals(loopTree.tree)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return tree.hashCode();
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

            final LiteralTree literalTree = (LiteralTree) o;

            if (!tree.equals(literalTree.tree)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return tree.hashCode();
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
        private final Set<MemberLookupExpressionTree> dynamicPropertyWrites = newSet();


        public InfoVisitor() {
            FunctionDeclarationTree dummyGlobal = new FunctionDeclarationTree(null, new IdentifierToken(null, "DUMMY_GLOBAL"), false, false, FunctionDeclarationTree.Kind.DECLARATION, null, null);
            functionNesting.push(dummyGlobal);
            functionOrLoopNesting.push(new FunctionOrLoopTree(dummyGlobal));
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
        public void in(PostfixExpressionTree tree) {
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
            if (!nonForInLoopNesting.isEmpty()) {
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
        public Void processElementGet(MemberLookupExpressionTree tree) {
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
        public Void processForInLoop(ForInStatementTree tree) {
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
        public Void processForLoop(ForStatementTree tree) {
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
        public Void processWhileLoop(WhileStatementTree tree) {
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
        }

        private void registerDynamicPropertyRead(MemberLookupExpressionTree tree) {
            functionsOrLoopsWithDynamicPropertyReads.add(functionOrLoopNesting.peek());
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
