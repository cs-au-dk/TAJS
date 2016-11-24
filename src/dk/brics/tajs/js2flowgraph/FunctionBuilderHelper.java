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

package dk.brics.tajs.js2flowgraph;

import com.google.javascript.jscomp.parsing.parser.LiteralToken;
import com.google.javascript.jscomp.parsing.parser.SourceFile;
import com.google.javascript.jscomp.parsing.parser.TokenType;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.BlockTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType;
import com.google.javascript.jscomp.parsing.parser.util.SourcePosition;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.AssumeNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginWithNode;
import dk.brics.tajs.flowgraph.jsnodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.EndForInNode;
import dk.brics.tajs.flowgraph.jsnodes.EndLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.EndWithNode;
import dk.brics.tajs.flowgraph.jsnodes.ExceptionalReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.ReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.ThrowNode;
import dk.brics.tajs.flowgraph.jsnodes.UnaryOperatorNode;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Pair;

import java.net.URL;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dk.brics.tajs.util.Collections.newList;

/**
 * Miscellaneous helper functions for {@link FunctionBuilder}.
 */
public class FunctionBuilderHelper {

    /**
     * Adds a node to a basic block.
     *
     * @param node  the node
     * @param block the basic block
     * @param env   the current environment
     */
    public static void addNodeToBlock(AbstractNode node, BasicBlock block, AstEnv env) {
        block.addNode(node);
        node.setRegistersDone(env.isStatementLevel());
    }

    /**
     * Clones blocks, preserving their internal successor relationships.
     * The duplicate reference will be set on the new nodes (unless set already)
     * External successors remain unchanged.
     *
     * @param origs the basic blocks to copy
     * @return the mapping from original blocks to their clones
     */
    public static IdentityHashMap<BasicBlock, BasicBlock> cloneBlocksAndNodes(Collection<BasicBlock> origs, FunctionAndBlockManager functionAndBlockManager) {
        // create new empty basic blocks
        IdentityHashMap<BasicBlock, BasicBlock> translationMap = new IdentityHashMap<>();
        for (BasicBlock orig : origs) {
            BasicBlock clone = new BasicBlock(orig.getFunction());
            translationMap.put(orig, clone);
            if (functionAndBlockManager.isUnreachable(orig)) {
                BasicBlock predecessor = functionAndBlockManager.getUnreachableSyntacticSuccessorPredecessor(orig);
                functionAndBlockManager.registerUnreachableSyntacticSuccessor(predecessor, clone);
            }
        }

        // clone the nodes
        for (Map.Entry<BasicBlock, BasicBlock> origAndClone : translationMap.entrySet()) {
            BasicBlock orig = origAndClone.getKey();
            BasicBlock clone = origAndClone.getValue();
            for (AbstractNode origNode : orig.getNodes()) {
                AbstractNode clonedNode = origNode.clone();
                if (clonedNode instanceof IfNode) {    // the only node kind with BasicBlock fields
                    IfNode origIfNode = (IfNode) origNode;
                    ((IfNode) clonedNode).setSuccessors(translationMap.get(origIfNode.getSuccTrue()), translationMap.get(origIfNode.getSuccFalse()));
                }
                if (clonedNode.getDuplicateOf() == null) {
                    if (clonedNode instanceof EndForInNode || clonedNode instanceof EndWithNode) {
                        // FIXME remove this special case. But somehow avoid doing it such that the original node has index = -1!
                    } else {
                        clonedNode.setDuplicateOf(origNode);
                    }
                }
                clone.addNode(clonedNode);
            }
            BasicBlock translatedExceptionHandler = translationMap.get(orig.getExceptionHandler());
            clone.setExceptionHandler(translatedExceptionHandler == null ? orig.getExceptionHandler() : translatedExceptionHandler);
            for (BasicBlock origSuccessor : orig.getSuccessors()) {
                BasicBlock translatedSuccessor = translationMap.get(origSuccessor);
                clone.addSuccessor(translatedSuccessor == null ? origSuccessor : translatedSuccessor);
            }
        }
        return translationMap;
    }

    /**
     * Creates node for a directive, or null if the directive is not recognized.
     */
    public static AbstractNode makeDirectiveNode(String text, SourceLocation location) {
        Directive directive = null;
        for (Directive d : Directive.values()) {
            if (text.equals(d.getName())) {
                directive = d;
                break;
            }
        }
        if (directive == null) {
            return null;
        }
        final AbstractNode directiveNode;
        switch (directive) {
            case NO_FLOW: {
                directiveNode = AssumeNode.makeUnreachable(location);
                break;
            }
            default:
                throw new AnalysisException("Unexpected directive: " + directive);
        }
        return directiveNode;
    }

    /**
     * Links together copies of the basic blocks in the given JumpThroughBlocks.
     * The resulting block structure is a single-entry-single-exit connected graph of basic blocks.
     * The basic block copies are also added to the FunctionAndBlockManager.
     *
     * @param jumpThroughBlocks as the blocks to operate on
     * @return the first and last block of the produced graph
     */
    public static Pair<BasicBlock, BasicBlock> wireAndRegisterJumpThroughBlocks(List<JumpThroughBlocks> jumpThroughBlocks, FunctionAndBlockManager functionAndBlocksManager) {
        assert !jumpThroughBlocks.isEmpty();
        LinkedList<JumpThroughBlocks> linkedJumpThroughBlocks = new LinkedList<>();
        for (JumpThroughBlocks blocks : jumpThroughBlocks) {
            JumpThroughBlocks copy = blocks.copy(functionAndBlocksManager);
            functionAndBlocksManager.add(copy.getAllBlocks()); // the blocks are registered to be used in the flowgraph
            linkedJumpThroughBlocks.add(copy); // copy in case of multiple usages of the blocks
        }
        BasicBlock firstBlock = linkedJumpThroughBlocks.getFirst().getEntry();
        BasicBlock lastBlock = linkedJumpThroughBlocks.getLast().getExit();
        assert lastBlock.getSuccessors().isEmpty();
        Iterator<JumpThroughBlocks> currentIterator = linkedJumpThroughBlocks.iterator();
        Iterator<JumpThroughBlocks> nextIterator = linkedJumpThroughBlocks.iterator();
        nextIterator.next();
        while (nextIterator.hasNext()) { // nextIterator is all the time one step ahead of currentIterator
            JumpThroughBlocks currentBlocks = currentIterator.next();
            JumpThroughBlocks nextBlocks = nextIterator.next();
            assert currentBlocks.getExit().getSuccessors().isEmpty();
            currentBlocks.getExit().addSuccessor(nextBlocks.getEntry());
        }
        return Pair.make(firstBlock, lastBlock);
    }

    /**
     * Closure compiler binary operator -&gt; TAJS flow graph operator.
     */
    public static BinaryOperatorNode.Op getFlowGraphBinaryNonAssignmentOp(TokenType type) {
        switch (type) {
            case SLASH:
                return BinaryOperatorNode.Op.DIV;
            case LEFT_SHIFT:
                return BinaryOperatorNode.Op.SHL;
            case PERCENT:
                return BinaryOperatorNode.Op.REM;
            case STAR:
                return BinaryOperatorNode.Op.MUL;
            case RIGHT_SHIFT:
                return BinaryOperatorNode.Op.SHR;
            case MINUS:
                return BinaryOperatorNode.Op.SUB;
            case UNSIGNED_RIGHT_SHIFT:
                return BinaryOperatorNode.Op.USHR;
            case GREATER_EQUAL:
                return BinaryOperatorNode.Op.GE;
            case CLOSE_ANGLE:
                return BinaryOperatorNode.Op.GT;
            case LESS_EQUAL:
                return BinaryOperatorNode.Op.LE;
            case OPEN_ANGLE:
                return BinaryOperatorNode.Op.LT;
            case NOT_EQUAL:
                return BinaryOperatorNode.Op.NE;
            case NOT_EQUAL_EQUAL:
                return BinaryOperatorNode.Op.SNE;
            case PLUS:
                return BinaryOperatorNode.Op.ADD;
            case EQUAL_EQUAL_EQUAL:
                return BinaryOperatorNode.Op.SEQ;
            case EQUAL_EQUAL:
                return BinaryOperatorNode.Op.EQ;
            case BAR:
                return BinaryOperatorNode.Op.OR;
            case AMPERSAND:
                return BinaryOperatorNode.Op.AND;
            case CARET:
                return BinaryOperatorNode.Op.XOR;
            case INSTANCEOF:
                return BinaryOperatorNode.Op.INSTANCEOF;
            case IN:
                return BinaryOperatorNode.Op.IN;
            case COMMA:
            default:
                throw new AnalysisException("Unhandled binary operator: " + type);
        }
    }

    /**
     * Closure compiler  compound assignments binary operator -&gt; TAJS flow graph operator.
     */
    public static BinaryOperatorNode.Op getFlowGraphBinaryOperationFromCompoundAssignment(BinaryOperatorTree tree) {
        final TokenType newOperation;
        switch (tree.operator.type) {
            case PLUS_EQUAL:
                newOperation = TokenType.PLUS;
                break;
            case BAR_EQUAL:
                newOperation = TokenType.BAR;
                break;
            case CARET_EQUAL:
                newOperation = TokenType.CARET;
                break;
            case AMPERSAND_EQUAL:
                newOperation = TokenType.AMPERSAND;
                break;
            case LEFT_SHIFT_EQUAL:
                newOperation = TokenType.LEFT_SHIFT;
                break;
            case RIGHT_SHIFT_EQUAL:
                newOperation = TokenType.RIGHT_SHIFT;
                break;
            case UNSIGNED_RIGHT_SHIFT_EQUAL:
                newOperation = TokenType.UNSIGNED_RIGHT_SHIFT;
                break;
            case MINUS_EQUAL:
                newOperation = TokenType.MINUS;
                break;
            case STAR_EQUAL:
                newOperation = TokenType.STAR;
                break;
            case SLASH_EQUAL:
                newOperation = TokenType.SLASH;
                break;
            case PERCENT_EQUAL:
                newOperation = TokenType.PERCENT;
                break;
            default: {
                throw new AnalysisException("Unhandled compound binary operator: " + tree.operator.type);
            }
        }
        return getFlowGraphBinaryNonAssignmentOp(newOperation);
    }

    /**
     * Closure compiler unary operator -&gt; TAJS flow graph operator.
     */
    public static UnaryOperatorNode.Op getFlowGraphUnaryNonAssignmentOp(TokenType type) {
        switch (type) {
            case BANG:
                return UnaryOperatorNode.Op.NOT;
            case TILDE:
                return UnaryOperatorNode.Op.COMPLEMENT;
            case PLUS:
                return UnaryOperatorNode.Op.PLUS;
            case MINUS:
                return UnaryOperatorNode.Op.MINUS;
            case DELETE:
            default:
                throw new AnalysisException("Unhandled unary type: " + type);
        }
    }

    /**
     * Closure compiler prefix/postfix operator -&gt; TAJS flow graph operator.
     */
    public static BinaryOperatorNode.Op getPrefixPostfixOp(TokenType token) {
        switch (token) {
            case PLUS_PLUS:
                return BinaryOperatorNode.Op.ADD;
            case MINUS_MINUS:
                return BinaryOperatorNode.Op.SUB;
            default:
                throw new AnalysisException("Unhandled binary type: " + token);
        }
    }

    /**
     * Creates an assume node for the given reference being non-null/undefined.
     */
    public static AssumeNode makeAssumeNonNullUndef(Reference base) {
        if (base == null)
            return null;
        switch (base.type) {
            case Variable: {
                String name = base.asVariable().name;
                if ("this".equals(name)) {
                    return null;
                }
                return AssumeNode.makeVariableNonNullUndef(name, base.location);
            }
            case StaticProperty: {
                Reference.StaticProperty property = base.asStaticProperty();
                return AssumeNode.makePropertyNonNullUndef(property.baseRegister, property.propertyName, base.location);
            }
            case DynamicProperty: {
                Reference.DynamicProperty property = base.asDynamicProperty();
                return AssumeNode.makePropertyNonNullUndef(property.baseRegister, property.propertyRegister, base.location);
            }
            default:
                throw new AnalysisException("Unexpected reference type: " + base.type);
        }
    }

    /**
     * Creates a TAJS source location from the start position of given AST node.
     */
    public static SourceLocation makeSourceLocation(ParseTree tree, URL location) {
        return new SourceLocation(tree.location.start.line + 1, tree.location.start.column + 1, tree.location.start.source.name, location);
    }

    /**
     * Finds the source code of a tree.
     */
    public static String getSource(ParseTree tree) {
        SourcePosition start = tree.location.start;
        SourcePosition end = tree.location.end;
        SourceFile sourceFile = start.source;
        return sourceFile.contents.substring(start.offset, end.offset);
    }

    /**
     * Creates a TAJS source location from the end position of the given AST node.
     */
    public static SourceLocation makeSourceLocationEnd(ParseTree tree, URL location) {
        return new SourceLocation(tree.location.end.line + 1, tree.location.end.column, tree.location.end.source.name, location);
    }

    /**
     * Creates a new basic block, for a function with some exception handler.
     */
    public static BasicBlock makeBasicBlock(Function fun, BasicBlock exceptionHandler, FunctionAndBlockManager functionAndBlocksManager) {
        BasicBlock newBlock = new BasicBlock(fun);
        functionAndBlocksManager.add(newBlock);
        newBlock.setExceptionHandler(exceptionHandler);
        return newBlock;
    }

    /**
     * Creates a new basic block that becomes the exception handler for the given basic block.
     */
    public static BasicBlock makeCatchBasicBlock(Function fun, BasicBlock thrower, FunctionAndBlockManager functionAndBlocksManager) {
        BasicBlock catchBlock = makeBasicBlock(fun, thrower.getExceptionHandler(), functionAndBlocksManager);
        thrower.setExceptionHandler(catchBlock);
        return catchBlock;
    }

    /**
     * Creates a new basic block that joins trueBlock and falseBlock.
     */
    public static BasicBlock makeJoinBasicBlock(AstEnv env, BasicBlock trueBlock, BasicBlock falseBlock, FunctionAndBlockManager functionAndBlocksManager) {
        BasicBlock joinBlock = makeBasicBlock(env.getFunction(), trueBlock.getExceptionHandler(), functionAndBlocksManager);
        trueBlock.addSuccessor(joinBlock);
        falseBlock.addSuccessor(joinBlock);
        return joinBlock;
    }

    /**
     * Creates a new basic block as a successor of the given basic block.
     */
    public static BasicBlock makeSuccessorBasicBlock(Function fun, BasicBlock predecessor, FunctionAndBlockManager functionAndBlocksManager) {
        BasicBlock successor = makeBasicBlock(fun, predecessor.getExceptionHandler(), functionAndBlocksManager);
        predecessor.addSuccessor(successor);
        return successor;
    }

    /**
     * Parses the given regexp literal into a pattern and flags.
     *
     * @return a pair of the pattern and the flags
     */
    public static Pair<String, String> parseRegExpLiteral(LiteralToken token) {
        String rawRegex = token.value;
        int lastSlash = rawRegex.lastIndexOf('/');
        String pattern = rawRegex.substring(1, lastSlash);
        final String flags;
        if (lastSlash < rawRegex.length()) {
            flags = rawRegex.substring(lastSlash + 1);
        } else {
            flags = "";
        }
        return Pair.make(pattern, flags);
    }

    /**
     * Checks whether the given node is of a kind that requires its own basic block.
     */
    public static boolean requiresOwnBlock(AbstractNode n) {
        return (n instanceof ThrowNode
                || n instanceof CallNode
                || n instanceof ExceptionalReturnNode
                || n instanceof ReturnNode
                || n instanceof BeginWithNode
                || n instanceof EndWithNode
                || n instanceof EndLoopNode
                || n instanceof EndForInNode);
    }

    /**
     * Traverses the children of the blocks and marks the duplicates
     * by calling {@link AbstractNode#setDuplicateOf(AbstractNode)}.
     *
     * @param copyBlocks    collection of all the copy blocks
     * @param seenBlocks    blocks already visited in the traversal (initially empty)
     * @param nodesToIgnore nodes that should be ignored
     * @param copyBlock     head of the copy blocks
     * @param origBlock     head of the original blocks
     */
    public static void setDuplicateBlocks(Set<BasicBlock> copyBlocks, Set<BasicBlock> seenBlocks, Set<AbstractNode> nodesToIgnore, BasicBlock copyBlock, BasicBlock origBlock) {
        if (!copyBlocks.contains(copyBlock) || seenBlocks.contains(copyBlock))
            return;
        setDuplicateNodes(copyBlock, origBlock, nodesToIgnore);
        seenBlocks.add(copyBlock);
        Iterator<BasicBlock> oi = origBlock.getSuccessors().iterator();
        Iterator<BasicBlock> ci = copyBlock.getSuccessors().iterator();
        while (ci.hasNext() && oi.hasNext()) {
            setDuplicateBlocks(copyBlocks, seenBlocks, nodesToIgnore, ci.next(), oi.next());
        }
    }

    /**
     * Traverses the nodes of the blocks and marks the duplicates.
     * Used by {@link #setDuplicateBlocks(Set, Set, Set, BasicBlock, BasicBlock)}.
     */
    private static void setDuplicateNodes(BasicBlock copyBlock, BasicBlock origBlock, Set<AbstractNode> nodesToIgnore) {
        List<AbstractNode> copyNodes = newList(copyBlock.getNodes());
        copyNodes.removeAll(nodesToIgnore);
        List<AbstractNode> originalNodes = newList(origBlock.getNodes());
        originalNodes.removeAll(nodesToIgnore);

        Iterator<AbstractNode> ci = copyNodes.iterator();
        Iterator<AbstractNode> oi = originalNodes.iterator();
        while (oi.hasNext()) {
            AbstractNode on = oi.next();
            if (!ci.hasNext())
                break;
            AbstractNode cn = ci.next();
            if (!on.getSourceLocation().equals(cn.getSourceLocation()) && oi.hasNext()) // origBlock may have additional nodes not in copyBlock
                on = oi.next();
            cn.setDuplicateOf(on);
        }
    }

    /**
     * Creates the initial basic blocks and nodes for a function.
     */
    public static AstEnv setupFunction(Function fun, AstEnv env, FunctionAndBlockManager functionAndBlocksManager) {
        BasicBlock entry = makeBasicBlock(fun, null, functionAndBlocksManager);
        BasicBlock body = makeSuccessorBasicBlock(fun, entry, functionAndBlocksManager);
        BasicBlock retBB = makeBasicBlock(fun, null, functionAndBlocksManager);
        BasicBlock exceptionretBB = makeBasicBlock(fun, null, functionAndBlocksManager);
        body.setExceptionHandler(exceptionretBB);
        fun.setEntry(entry);
        fun.setExceptionalExit(exceptionretBB);
        fun.setOrdinaryExit(retBB);

        AstEnv funEnv = env.makeEnclosingFunction(fun).makeStatementLevel(true);
        Function function = funEnv.getFunction();

        ConstantNode returnUndefinedByDefault = ConstantNode.makeUndefined(AbstractNode.RETURN_REG, function.getSourceLocation());
        returnUndefinedByDefault.setArtificial();
        AstEnv artificialEnv = funEnv.makeStatementLevel(false);
        addNodeToBlock(returnUndefinedByDefault, function.getEntry(), artificialEnv);

        ReturnNode returnNode = new ReturnNode(AbstractNode.RETURN_REG, function.getSourceLocation());
        returnNode.setArtificial();
        addNodeToBlock(returnNode, function.getOrdinaryExit(), artificialEnv);

        ExceptionalReturnNode expeptionalReturn = new ExceptionalReturnNode(function.getSourceLocation());
        expeptionalReturn.setArtificial();
        addNodeToBlock(expeptionalReturn, function.getExceptionalExit(), artificialEnv);

        functionAndBlocksManager.add(fun);
        return funEnv;
    }

    /**
     * Skips parenthesis expressions.
     */
    public static ParseTree stripParens(ParseTree tree) {
        if (tree.type == ParseTreeType.PAREN_EXPRESSION) {
            return stripParens(tree.asParenExpression().expression);
        }
        return tree;
    }

    public static boolean isLoopStatement(ParseTree statement) {
        switch (statement.type) {
            case FOR_IN_STATEMENT:
            case FOR_OF_STATEMENT:
            case FOR_STATEMENT:
            case WHILE_STATEMENT:
            case DO_WHILE_STATEMENT:
                return true;
            default:
                return false;
        }
    }

    /**
     * Special TAJS directives.
     * Directives are constant strings that appear as expression statements in the JavaScript code.
     */
    public enum Directive {

        NO_FLOW("dk.brics.tajs.directives.unreachable");

        private final String name;

        Directive(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        /**
         * Returns the name of the directive.
         */
        String getName() {
            return name;
        }
    }
}
