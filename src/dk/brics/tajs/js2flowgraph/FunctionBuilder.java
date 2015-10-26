package dk.brics.tajs.js2flowgraph;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.parsing.parser.IdentifierToken;
import com.google.javascript.jscomp.parsing.parser.LiteralToken;
import com.google.javascript.jscomp.parsing.parser.TokenType;
import com.google.javascript.jscomp.parsing.parser.trees.ArgumentListTree;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.BlockTree;
import com.google.javascript.jscomp.parsing.parser.trees.BreakStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.CallExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.CaseClauseTree;
import com.google.javascript.jscomp.parsing.parser.trees.CatchTree;
import com.google.javascript.jscomp.parsing.parser.trees.ClassDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.CommaExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyAssignmentTree;
import com.google.javascript.jscomp.parsing.parser.trees.ConditionalExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ContinueStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.DebuggerStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.DefaultClauseTree;
import com.google.javascript.jscomp.parsing.parser.trees.DefaultParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.DoWhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.EmptyStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExportDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExportSpecifierTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExpressionStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FinallyTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForInStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForOfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FormalParameterListTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.GetAccessorTree;
import com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.IfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportSpecifierTree;
import com.google.javascript.jscomp.parsing.parser.trees.LabelledStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.LiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberLookupExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MissingPrimaryExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ModuleImportTree;
import com.google.javascript.jscomp.parsing.parser.trees.NewExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NullTree;
import com.google.javascript.jscomp.parsing.parser.trees.ObjectLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParenExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType;
import com.google.javascript.jscomp.parsing.parser.trees.PostfixExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import com.google.javascript.jscomp.parsing.parser.trees.PropertyNameAssignmentTree;
import com.google.javascript.jscomp.parsing.parser.trees.RestParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.ReturnStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.SetAccessorTree;
import com.google.javascript.jscomp.parsing.parser.trees.SpreadExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.SuperExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.SwitchStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThisExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThrowStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TryStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.UnaryExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationListTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WithStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.YieldExpressionTree;
import com.google.javascript.jscomp.parsing.parser.util.SourceRange;
import dk.brics.tajs.analysis.StaticDeterminacyContextSensitivityStrategy;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.AssumeNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginWithNode;
import dk.brics.tajs.flowgraph.jsnodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.CatchNode;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.DeletePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.EndForInNode;
import dk.brics.tajs.flowgraph.jsnodes.EndLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.EndWithNode;
import dk.brics.tajs.flowgraph.jsnodes.HasNextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.NewObjectNode;
import dk.brics.tajs.flowgraph.jsnodes.NextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.NopNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.ThrowNode;
import dk.brics.tajs.flowgraph.jsnodes.TypeofNode;
import dk.brics.tajs.flowgraph.jsnodes.UnaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.NotImplemented;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.addNodeToBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.getFlowGraphBinaryNonAssignmentOp;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.getFlowGraphBinaryOperationFromCompoundAssignment;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.getFlowGraphUnaryNonAssignmentOp;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.getPrefixPostfixOp;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeAssumeNonNullUndef;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeBasicBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeCatchBasicBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeDirectiveNode;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeJoinBasicBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeSourceLocation;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeSourceLocationEnd;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeSuccessorBasicBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.parseRegExpLiteral;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.requiresOwnBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.setDuplicateBlocks;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.setupFunction;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.stripParens;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.wireAndRegisterJumpThroughBlocks;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * AST visitor that builds functions.
 * <p>
 * Pattern matches on the AST node type and calls itself recursively.
 * <p>
 * Information is propagated downwards in the tree using {@link AstEnv}
 * and upwards using {@link TranslationResult}.
 * The class is a very large visitor. Utility methods are kept elsewhere, e.g. in {@link FunctionBuilderHelper}.
 */
public class FunctionBuilder extends ParseTreeAuxVisitor<TranslationResult, AstEnv> {

    // TODO: enable this to mark lines with `function` or `var` as covered even though they do not have effects.
    private static final boolean USE_REPRESENTATION_NODES = false;

    private final ClosureASTUtil closureUtil;

    private final ASTInfo astInfo;

    private final FunctionAndBlockManager functionAndBlocksManager;

    /**
     * Constructs a new function builder.
     */
    FunctionBuilder(ClosureASTUtil closureUtil, ASTInfo astInfo, FunctionAndBlockManager functionAndBlocksManager) {
        this.closureUtil = closureUtil;
        this.astInfo = astInfo;
        this.functionAndBlocksManager = functionAndBlocksManager;
    }

    /**
     * Registers a value access (read/write), potentially adding relevant assume nodes.
     */
    private BasicBlock access(AbstractNode accessNode, Reference target, AstEnv env) {
        // try to make an assume node
        final AssumeNode assumeNode;
        if (target.type == Reference.Type.StaticProperty || target.type == Reference.Type.DynamicProperty) {
            assumeNode = makeAssumeNonNullUndef(target.asProperty().base);
        } else {
            assumeNode = null;
        }

        // add the access
        addNodeToBlock(accessNode, env.getAppendBlock(), assumeNode == null ? env : env.makeStatementLevel(false));
        BasicBlock appendBlock;
        if (requiresOwnBlock(accessNode)) {
            appendBlock = makeSuccessorBasicBlock(env.getFunction(), env.getAppendBlock(), functionAndBlocksManager);
        } else {
            appendBlock = env.getAppendBlock();
        }

        // add the assume node
        if (assumeNode != null) {
            addNodeToBlock(assumeNode, appendBlock, env);
        }

        return appendBlock;
    }

    /**
     * Desugars for-in loops variable declarations to just the variable.
     */
    private ParseTree desugarForInLoopVariable(ParseTree tree, AstEnv env) {
        switch (tree.type) {
            case VARIABLE_DECLARATION_LIST:
                ImmutableList<VariableDeclarationTree> declarations = tree.asVariableDeclarationList().declarations;
                if (declarations.size() != 1) {
                    throw new AnalysisException(makeSourceLocation(tree) + ": Multiple variable declarations in for-in initializer?!?");
                }
                VariableDeclarationTree declaration = declarations.get(0);
                process(declaration, env);
                if (declaration.lvalue.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
                    return declaration.lvalue;
                } else {
                    throw new AnalysisException(makeSourceLocation(declaration.lvalue) + ": Only identifier-var declarations supported");
                }
            default:
                return tree;
        }
    }

    /**
     * Inserts a 'end-scope-type-node' in an extra exception handler which propagates to the original exception handler.
     * Useful for {@link EndWithNode} and {@link EndForInNode}
     */
    private void endNodeScopeExceptionally(AbstractNode endNode, AstEnv env) {
        BasicBlock originalExceptionHandler = env.getAppendBlock().getExceptionHandler();
        BasicBlock exceptionBlock = makeCatchBasicBlock(env.getFunction(), env.getAppendBlock(), functionAndBlocksManager);
        addNodeToBlock(endNode, exceptionBlock, env);
        exceptionBlock.addSuccessor(originalExceptionHandler);
        exceptionBlock.setExceptionHandler(null);
    }

    /**
     * Inserts a 'end-scope-type-node'.
     * Useful for {@link EndWithNode} and {@link EndForInNode}
     */
    private BasicBlock endNodeScopeOrdinarily(AbstractNode endNode, BasicBlock scopeExceptionHandler, AstEnv env) {
        BasicBlock appendBlock = env.getAppendBlock();
        AbstractNode lastBodyNode = appendBlock.isEmpty() ? null : appendBlock.getLastNode();
        appendBlock = requiresOwnBlock(lastBodyNode) || requiresOwnBlock(endNode) ? makeSuccessorBasicBlock(env.getFunction(), appendBlock, functionAndBlocksManager) : appendBlock;
        addNodeToBlock(endNode, appendBlock, env);
        appendBlock.setExceptionHandler(scopeExceptionHandler.getSingleSuccessor() /* the first exception handler block is already used for ending the node scope exceptionally */);
        return appendBlock;
    }

    /**
     * Produces a usable result register.
     * If the current result register in NO_VALUE, the next free register is returned.
     */
    private static int getUsableResultRegister(AstEnv env) {
        return env.getResultRegister() == AbstractNode.NO_VALUE ? nextRegister(env) : env.getResultRegister();
    }

    /**
     * Checks whether the given AST node is an access operation ('this', variable, or property access).
     */
    private static boolean isAccess(ParseTree access) {
        access = stripParens(access);
        switch (access.type) {
            case THIS_EXPRESSION:
            case IDENTIFIER_EXPRESSION:
            case MEMBER_EXPRESSION:
            case MEMBER_LOOKUP_EXPRESSION:
                return true;
            default:
                return false;
        }
    }

    /**
     * Creates conditional flow with two branches, as for if-statements and ?-:-expressions.
     */
    private TranslationResult makeConditional(ParseTree condition, ParseTree left, ParseTree right, SourceLocation sourceLocation, AstEnv env) {
        AstEnv conditionEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
        TranslationResult processedCondition = process(condition, conditionEnv);

        BasicBlock trueBranch = makeSuccessorBasicBlock(env.getFunction(), processedCondition.getAppendBlock(), functionAndBlocksManager);
        BasicBlock falseBranch = makeSuccessorBasicBlock(env.getFunction(), processedCondition.getAppendBlock(), functionAndBlocksManager);

        IfNode ifNode = new IfNode(conditionEnv.getResultRegister(), sourceLocation);
        ifNode.setSuccessors(trueBranch, falseBranch);
        addNodeToBlock(ifNode, processedCondition.getAppendBlock(), env);

        int currentRegister = env.getRegisterManager().getRegister(); /* mutual exclusive branches, so let them share registers */
        TranslationResult processedLeft = process(left, env.makeRegisterManager(new RegisterManager(currentRegister)).makeAppendBlock(trueBranch));
        TranslationResult processedRight = right == null ? TranslationResult.makeAppendBlock(falseBranch) : process(right, env.makeRegisterManager(new RegisterManager(currentRegister)).makeAppendBlock(falseBranch));

        BasicBlock joinBlock = makeJoinBasicBlock(env, processedLeft.getAppendBlock(), processedRight.getAppendBlock(), functionAndBlocksManager);
        return TranslationResult.makeAppendBlock(joinBlock);
    }

    /**
     * Creates a literal from the given integer.
     */
    private static LiteralExpressionTree makeConstant(int number, SourceRange location) {
        LiteralToken literalToken = new LiteralToken(TokenType.NUMBER, Integer.toString(number), location);
        return new LiteralExpressionTree(location, literalToken);
    }

    /**
     * Returns the next free register.
     */
    private static int nextRegister(AstEnv env) {
        return env.getRegisterManager().nextRegister();
    }

    /**
     * Simplifies expressions of the form o['xyz'] to o.xyz.
     *
     * @return a simplified expression or the non-simplifiable argument expression
     */
    private static ParseTree potentiallySimplifyMemberLookupExpression(ParseTree potentialSimplifiableExpression) {
        if (potentialSimplifiableExpression.type == ParseTreeType.MEMBER_LOOKUP_EXPRESSION &&
                potentialSimplifiableExpression.asMemberLookupExpression().memberExpression.type == ParseTreeType.LITERAL_EXPRESSION &&
                potentialSimplifiableExpression.asMemberLookupExpression().memberExpression.asLiteralExpression().literalToken.type == TokenType.STRING) {
            MemberLookupExpressionTree memberLookupExpression = potentialSimplifiableExpression.asMemberLookupExpression();
            LiteralExpressionTree complexMember = memberLookupExpression.memberExpression.asLiteralExpression();
            LiteralToken complexLiteral = complexMember.literalToken.asLiteral();
            IdentifierToken simpleMember = new IdentifierToken(complexMember.location, ClosureASTUtil.normalizeString(complexLiteral));
            return new MemberExpressionTree(memberLookupExpression.location, memberLookupExpression.operand, simpleMember);
        }
        return potentialSimplifiableExpression;
    }

    /**
     * Handles compound assingments where a target value is updated by some operator.
     * Useful for unaries: ++x, postfixes: x++, and regular compound assignments: x+=1
     *
     * @param updatedValueIsResult whether the value of the processed expression is the value of the target value before or after update
     * @param coerceToNumber       whether the target value should be coerced to a number
     * @param op                   the operator to apply to the target value and the operand
     * @param target               the target value, is read and written
     * @param operand              the operand
     * @param env                  the environment
     * @param sourceLocation       the source location of the operation
     */
    private TranslationResult processCompoundAssignmentOperation(boolean updatedValueIsResult, boolean coerceToNumber, BinaryOperatorNode.Op op, ParseTree target, ParseTree operand, AstEnv env, SourceLocation sourceLocation) {
        TranslationResult processedTarget = processAccessPartly(target, env.makeStatementLevel(false));
        final Reference targetReference = processedTarget.getResultReference();

        int readRegister = updatedValueIsResult ? nextRegister(env) : (getUsableResultRegister(env));
        AstEnv readEnv = env.makeResultRegister(readRegister).makeStatementLevel(false).makeAppendBlock(processedTarget.getAppendBlock());
        BasicBlock appendBlock = read(targetReference, readEnv);

        // convert to number
        AstEnv coercionEnv;
        if (coerceToNumber) {
            coercionEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
            addNodeToBlock(new UnaryOperatorNode(UnaryOperatorNode.Op.PLUS, readEnv.getResultRegister(), coercionEnv.getResultRegister(), sourceLocation), appendBlock, coercionEnv);
        } else {
            coercionEnv = env.makeResultRegister(readEnv.getResultRegister());
        }

        AstEnv operandEnv = env.makeResultRegister(nextRegister(env)).makeAppendBlock(appendBlock).makeStatementLevel(false);
        TranslationResult processedOperand = process(operand, operandEnv);
        appendBlock = processedOperand.getAppendBlock();

        // apply the operation
        int operationResultRegister = updatedValueIsResult ? getUsableResultRegister(env) : nextRegister(env);
        AstEnv operationEnv = env.makeResultRegister(operationResultRegister).makeStatementLevel(false);
        addNodeToBlock(new BinaryOperatorNode(op, coercionEnv.getResultRegister(), operandEnv.getResultRegister(), operationEnv.getResultRegister(), sourceLocation), appendBlock, operationEnv);

        appendBlock = write(targetReference.changeSourceLocation(sourceLocation), operationEnv.getResultRegister(), env.makeAppendBlock(appendBlock));

        return TranslationResult.makeAppendBlock(appendBlock);
    }

    /**
     * Processes the subexpressions of an access.
     * Adds nodes for the subexpressions to the appendBlock.
     *
     * @return The return-values {@link TranslationResult#getResultReference()} should be consulted.
     */
    private TranslationResult processAccessPartly(ParseTree access, AstEnv env) {
        access = stripParens(access);
        access = potentiallySimplifyMemberLookupExpression(access);
        switch (access.type) {
            case THIS_EXPRESSION: {
                return TranslationResult.makeResultReference(new Reference.Variable("this", makeSourceLocation(access)), env.getAppendBlock());
            }
            case IDENTIFIER_EXPRESSION: {
                Reference reference = new Reference.Variable(access.asIdentifierExpression().identifierToken.value, makeSourceLocation(access));
                return TranslationResult.makeResultReference(reference, env.getAppendBlock());
            }
            case MEMBER_EXPRESSION: {
                MemberExpressionTree memberExpression = access.asMemberExpression();
                final AstEnv baseEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
                TranslationResult processedBase = process(memberExpression.operand, baseEnv);
                Reference reference = new Reference.StaticProperty(processedBase.getResultReference(), baseEnv.getResultRegister(), memberExpression.memberName.value, makeSourceLocation(access));
                return TranslationResult.makeResultReference(reference, processedBase.getAppendBlock());
            }
            case MEMBER_LOOKUP_EXPRESSION:
                MemberLookupExpressionTree memberLookupExpression = access.asMemberLookupExpression();
                final AstEnv baseEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
                TranslationResult processedBase = process(memberLookupExpression.operand, baseEnv);
                final AstEnv memberEnv = env.makeResultRegister(nextRegister(env)).makeAppendBlock(processedBase.getAppendBlock()).makeStatementLevel(false);
                TranslationResult processedMember = process(memberLookupExpression.memberExpression, memberEnv);
                Reference baseReference = processedBase.getResultReference();
                Reference reference = new Reference.DynamicProperty(baseReference, baseEnv.getResultRegister(), processedMember.getResultReference(), memberEnv.getResultRegister(), makeSourceLocation(access));
                return TranslationResult.makeResultReference(reference, processedMember.getAppendBlock());
            default:
                throw new RuntimeException("Unhandled reference type: " + access.type);
        }
    }

    /**
     * Produces a WriteVariableNode or a WritePropertyNode.
     */
    private BasicBlock write(Reference target, int valueRegister, AstEnv env) {
        return writeWithCustomLocation(target, valueRegister, env, target.location);
    }

    /**
     * Produces a WriteVariableNode or a WritePropertyNode, using the given source location.
     */
    private BasicBlock writeWithCustomLocation(Reference target, int valueRegister, AstEnv env, SourceLocation location) {
        final AbstractNode write;
        switch (target.type) {
            case Variable:
                write = new WriteVariableNode(valueRegister, target.asVariable().name, location);
                break;
            case StaticProperty:
                Reference.StaticProperty staticProperty = target.asStaticProperty();
                write = new WritePropertyNode(staticProperty.baseRegister, staticProperty.propertyName, valueRegister, location);
                break;
            case DynamicProperty:
                Reference.DynamicProperty dynamicProperty = target.asDynamicProperty();
                write = new WritePropertyNode(dynamicProperty.baseRegister, dynamicProperty.propertyRegister, valueRegister, location);
                break;
            default:
                throw new RuntimeException("Unhandled write type: " + target.type);
        }
        return access(write, target, env);
    }

    /**
     * Produces a ReadVariableNode or a ReadPropertyNode.
     */
    private BasicBlock read(Reference target, AstEnv env) {
        final AbstractNode read;
        switch (target.type) {
            case Variable:
                read = new ReadVariableNode(target.asVariable().name, env.getResultRegister(), env.getBaseRegister(), target.location);
                break;
            case StaticProperty:
                Reference.StaticProperty staticProperty = target.asStaticProperty();
                read = new ReadPropertyNode(staticProperty.baseRegister, staticProperty.propertyName, env.getResultRegister(), target.location);
                break;
            case DynamicProperty:
                Reference.DynamicProperty dynamicProperty = target.asDynamicProperty();
                read = new ReadPropertyNode(dynamicProperty.baseRegister, dynamicProperty.propertyRegister, env.getResultRegister(), target.location);
                break;
            default:
                throw new RuntimeException("Unhandled read type: " + target.type);
        }
        return access(read, target, env.makeAppendBlock(env.getAppendBlock()));
    }

    /**
     * Unrolls a loop one-and-a-half times by copying the condition to the end of the loop body.
     */
    private void unrollLoopOneAndAHalf(ParseTree condition, int conditionalRegister, BasicBlock conditionStartBlock, BasicBlock trueBlock, BasicBlock trueBranch, BasicBlock falseBranch, SourceLocation conditionLocation, AstEnv env) {
        BasicBlock unrollStartBlock = makeSuccessorBasicBlock(env.getFunction(), trueBlock, functionAndBlocksManager);

        AstEnv conditionEnv = env.makeAppendBlock(unrollStartBlock).makeResultRegister(conditionalRegister).makeStatementLevel(false);
        FunctionAndBlockManager.SessionKey blocksKey = functionAndBlocksManager.startSession();
        TranslationResult unrollProcessedCondition = process(condition, conditionEnv);
        functionAndBlocksManager.endSession(blocksKey);

        Set<BasicBlock> unrolledBlocks = newSet();
        unrolledBlocks.add(unrollStartBlock);
        unrolledBlocks.addAll(functionAndBlocksManager.getSessionBlocks(blocksKey));

        trueBlock = unrollProcessedCondition.getAppendBlock();

        IfNode ifNode = new IfNode(conditionalRegister, conditionLocation);
        ifNode.setSuccessors(trueBranch, falseBranch);

        addNodeToBlock(ifNode, trueBlock, env.makeStatementLevel(false));
        trueBlock.addSuccessor(trueBranch);
        trueBlock.addSuccessor(falseBranch);

        setDuplicateBlocks(unrolledBlocks, newSet(), newSet(), unrollStartBlock, conditionStartBlock);
    }

    /**
     * Processes a function or constructor invocation.
     */
    private TranslationResult processInvocation(ParseTree operand, ArgumentListTree arguments, boolean constructor, SourceLocation location, AstEnv env) {
        final int baseRegister;
        if (constructor) {
            baseRegister = AbstractNode.NO_VALUE;
        } else if (operand.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
            baseRegister = nextRegister(env);
        } else {
            baseRegister = AbstractNode.NO_VALUE;
        }
        AstEnv funEnv = env.makeBaseRegister(baseRegister).makeResultRegister(nextRegister(env)).makeStatementLevel(false);

        final boolean propertyAccessCall;
        int propertyObjectRegister = AbstractNode.NO_VALUE;
        String propertyNameString = null;
        int propertyNameRegister = AbstractNode.NO_VALUE;
        TranslationResult processed;
        if (isAccess(operand)) {
            processed = processAccessPartly(operand, funEnv);
            Reference resultReference = processed.getResultReference();
            AstEnv referenceEnv = funEnv.makeAppendBlock(processed.getAppendBlock());
            switch (resultReference.type) {
                case Variable:
                    propertyAccessCall = false;
                    processed = TranslationResult.makeAppendBlock(read(resultReference, referenceEnv));
                    break;
                case StaticProperty:
                case DynamicProperty:
                    propertyAccessCall = true;
                    if (resultReference.type == Reference.Type.StaticProperty) {
                        propertyObjectRegister = resultReference.asStaticProperty().baseRegister;
                        propertyNameString = resultReference.asStaticProperty().propertyName;
                    } else {
                        propertyObjectRegister = resultReference.asDynamicProperty().baseRegister;
                        propertyNameRegister = resultReference.asDynamicProperty().propertyRegister;
                    }
                    processed = TranslationResult.makeAppendBlock(read(resultReference, referenceEnv.makeResultRegister(AbstractNode.NO_VALUE) /* the property is resolved twice: once here, and once during the callnode transfer ...*/));
                    break;
                default:
                    throw new RuntimeException("Unhandled reference type: " + resultReference.type);
            }
        } else {
            propertyAccessCall = false;
            processed = process(operand, funEnv);
        }

        List<Integer> argumentRegisters = newList();
        if (arguments != null) {
            for (ParseTree argument : arguments.arguments) {
                int argumentRegister = nextRegister(env);
                argumentRegisters.add(argumentRegister);
                processed = process(argument, env.makeResultRegister(argumentRegister).makeStatementLevel(false).makeAppendBlock(processed.getAppendBlock()));
            }
        }

        final AbstractNode callNode;
        if (propertyAccessCall) {
            callNode = new CallNode(constructor, env.getResultRegister(), propertyObjectRegister, propertyNameRegister, propertyNameString, argumentRegisters, location);
        } else {
            callNode = new CallNode(constructor, env.getResultRegister(), baseRegister, funEnv.getResultRegister(), argumentRegisters, location);
        }

        BasicBlock callBlock = makeSuccessorBasicBlock(env.getFunction(), processed.getAppendBlock(), functionAndBlocksManager);
        addNodeToBlock(callNode, callBlock, env);

        BasicBlock postCallBlock = makeSuccessorBasicBlock(env.getFunction(), callBlock, functionAndBlocksManager);
        return TranslationResult.makeAppendBlock(postCallBlock);
    }

    /**
     * Processes jumps from return, break and continue statements by ensuring proper flow through finally and end-node-environment-blocks.
     *
     * @param type  either BREAK or CONTINUE
     * @param label the name of the label, or null in the case of a jump to the default label
     */
    private TranslationResult processJump(ParseTreeType type, String label, SourceLocation location, AstEnv env) {
        final BasicBlock jumpTarget;
        final AstEnv targetEnv;
        String prettyJumpName;
        switch (type) {
            case RETURN_STATEMENT: {
                assert (label == null);
                targetEnv = env.getFunctionEnv();
                jumpTarget = env.getFunction().getOrdinaryExit();
                prettyJumpName = "return";
                break;
            }
            case BREAK_STATEMENT: {
                if (label != null) {
                    targetEnv = env.getLabelledBreakEnv(label);
                    jumpTarget = targetEnv.getLabelledBreak(label);
                } else {
                    targetEnv = env.getUnlabelledBreakEnv();
                    jumpTarget = targetEnv.getUnlabelledBreak();
                }
                prettyJumpName = "break";
                break;
            }
            case CONTINUE_STATEMENT: {
                if (label != null) {
                    targetEnv = env.getLabelledContinueEnv(label);
                    jumpTarget = targetEnv.getLabelledContinue(label);
                } else {
                    targetEnv = env.getUnlabelledContinueEnv();
                    jumpTarget = targetEnv.getUnlabelledContinue();
                }
                prettyJumpName = "continue";
                break;
            }
            default: {
                throw new AnalysisException("Unhandled jump type:" + type);
            }
        }

        if (label != null) {
            prettyJumpName += " " + label;
        } else {
            prettyJumpName += " <DEFAULT>";
        }

        List<JumpThroughBlocks> jumpThroughBlocks = env.getJumpThroughBlocks(targetEnv);
        final BasicBlock target;
        if (jumpThroughBlocks.isEmpty()) {
            // nothing to jump through, just jump directly
            target = jumpTarget;
        } else {
            Pair<BasicBlock, BasicBlock> jumpThroughPair = wireAndRegisterJumpThroughBlocks(jumpThroughBlocks, functionAndBlocksManager);

            target = jumpThroughPair.getFirst();
            jumpThroughPair.getSecond().addSuccessor(jumpTarget);
        }

        BasicBlock appendBlock = env.getAppendBlock();

        // cannot have an empty block here (see dk.brics.tajs.test.TestFlowgraphBuilder.flowgraphbuilder_0202())
        if (type != ParseTreeType.RETURN_STATEMENT && appendBlock.isEmpty()) {
            NopNode nop = new NopNode(prettyJumpName, location);
            nop.setArtificial();
            addNodeToBlock(nop, appendBlock, env);
        }
        if (!appendBlock.isEmpty()) {
            // a jump will enable clearing the registers
            appendBlock.getLastNode().setRegistersDone(true);
        }
        appendBlock.addSuccessor(target);

        BasicBlock unreachableBlock = makeBasicBlock(env.getFunction(), appendBlock.getExceptionHandler(), functionAndBlocksManager);
        functionAndBlocksManager.registerUnreachableSyntacticSuccessor(appendBlock, unreachableBlock);
        return TranslationResult.makeAppendBlock(unreachableBlock);
    }

    /**
     * Processes the non-trivial control flow of short-circuit '&amp;&amp;' and '||'.
     *
     * @param tree a tree of either '&amp;&amp;' or '||'
     */
    private TranslationResult processShortCircuitOperators(BinaryOperatorTree tree, AstEnv env) {
        AstEnv expressionEnv = env.makeResultRegister(getUsableResultRegister(env)).makeStatementLevel(false);
        IfNode ifNode = new IfNode(expressionEnv.getResultRegister(), makeSourceLocation(tree));

        TranslationResult processedLeft = process(tree.left, expressionEnv);
        BasicBlock trueBranch = makeSuccessorBasicBlock(env.getFunction(), processedLeft.getAppendBlock(), functionAndBlocksManager);
        BasicBlock falseBranch = makeSuccessorBasicBlock(env.getFunction(), processedLeft.getAppendBlock(), functionAndBlocksManager);

        if (tree.operator.type == TokenType.AND) {
            ifNode.setSuccessors(falseBranch, trueBranch);
        } else {
            ifNode.setSuccessors(trueBranch, falseBranch);
        }

        addNodeToBlock(ifNode, processedLeft.getAppendBlock(), expressionEnv);

        TranslationResult processedRight = process(tree.right, env.makeAppendBlock(falseBranch).makeStatementLevel(false));

        BasicBlock joinBlock = makeJoinBasicBlock(env, trueBranch, processedRight.getAppendBlock(), functionAndBlocksManager);
        return TranslationResult.makeAppendBlock(joinBlock);
    }

    /**
     * Processes a sequence of statements.
     */
    private TranslationResult processList(List<? extends ParseTree> trees, AstEnv env) {
        TranslationResult lastProcessed = TranslationResult.makeAppendBlock(env.getAppendBlock());
        int i = 0;
        for (ParseTree tree : trees) {
            i++;
            final boolean isLast = i == trees.size();
            lastProcessed = process(tree, env.makeAppendBlock(lastProcessed.getAppendBlock()).makeResultRegister(isLast ? env.getResultRegister() : AbstractNode.NO_VALUE));
        }
        return lastProcessed;
    }

    /**
     * Builds a loop of the form ([init];[cond];[inc]){body}.
     */
    private TranslationResult processLoop(ParseTree tree, ParseTree initializer, ParseTree condition, ParseTree increment, ParseTree body, boolean defaultConditionIsTrue, SourceLocation conditionLocation /* condition might be null */, AstEnv env) {
        SourceLocation loopEndLocation = makeSourceLocation(tree); // TODO improve
        int conditionalRegister = nextRegister(env);

        // 1. process initializer
        final TranslationResult processedInitializer;
        if (initializer != null) {
            AstEnv initializerEnv = env.makeResultRegister(AbstractNode.NO_VALUE).makeAppendBlock(env.getAppendBlock()).makeStatementLevel(true);
            processedInitializer = process(initializer, initializerEnv);
        } else {
            processedInitializer = TranslationResult.makeAppendBlock(env.getAppendBlock());
        }

        IfNode ifNode = new IfNode(conditionalRegister, conditionLocation);
        final BeginLoopNode beginLoopNode;
        if (Options.get().isLoopUnrollingEnabled()) {
            beginLoopNode = new BeginLoopNode(ifNode, makeSourceLocation(body));
        } else {
            beginLoopNode = null;
        }

        // 2. process condition
        BasicBlock firstConditionBlock = makeSuccessorBasicBlock(env.getFunction(), processedInitializer.getAppendBlock(), functionAndBlocksManager);

        if (Options.get().isLoopUnrollingEnabled()) {
            endNodeScopeExceptionally(new EndLoopNode(beginLoopNode, loopEndLocation), env.makeStatementLevel(true).makeAppendBlock(firstConditionBlock));
        }

        final TranslationResult processedCondition;
        if (defaultConditionIsTrue && condition == null) {
            AbstractNode trueNode = ConstantNode.makeBoolean(true, conditionalRegister, conditionLocation);
            trueNode.setArtificial();
            addNodeToBlock(trueNode, firstConditionBlock, env.makeStatementLevel(false));
            processedCondition = TranslationResult.makeAppendBlock(makeSuccessorBasicBlock(env.getFunction(), firstConditionBlock, functionAndBlocksManager));
        } else {
            AstEnv conditionEnv = env.makeAppendBlock(firstConditionBlock).makeResultRegister(conditionalRegister).makeStatementLevel(false);
            processedCondition = process(condition, conditionEnv);
        }

        BasicBlock conditionAppendBlock = processedCondition.getAppendBlock();

        if (Options.get().isLoopUnrollingEnabled()) {
            BasicBlock beginLoopBlock = makeSuccessorBasicBlock(env.getFunction(), conditionAppendBlock, functionAndBlocksManager);
            addNodeToBlock(beginLoopNode, beginLoopBlock, env.makeStatementLevel(false));
            conditionAppendBlock = makeSuccessorBasicBlock(env.getFunction(), beginLoopBlock, functionAndBlocksManager);
        }

        addNodeToBlock(ifNode, conditionAppendBlock, env.makeStatementLevel(false /* ought to be true! */));

        BasicBlock firstBodyBlock = makeSuccessorBasicBlock(env.getFunction(), conditionAppendBlock, functionAndBlocksManager);
        BasicBlock firstPostLoopBlock = makeSuccessorBasicBlock(env.getFunction(), conditionAppendBlock, functionAndBlocksManager);
        ifNode.setSuccessors(firstBodyBlock, firstPostLoopBlock);

        // 3. process increment (before body, we might need to do a `continue` to the increment)
        AstEnv bodyEnv = env.makeAppendBlock(firstBodyBlock).makeStatementLevel(true);
        if (Options.get().isLoopUnrollingEnabled()) {
            BasicBlock jumpThroughBlock = new BasicBlock(env.getFunction());
            jumpThroughBlock.setExceptionHandler(env.getAppendBlock().getExceptionHandler());
            addNodeToBlock(new EndLoopNode(beginLoopNode, loopEndLocation), jumpThroughBlock, env.makeStatementLevel(true));

            bodyEnv = bodyEnv.makeJumpThroughBlock(new JumpThroughBlocks(jumpThroughBlock, functionAndBlocksManager));
        }

        final BasicBlock firstIncrementBlock;
        final BasicBlock lastIncrementBlock;
        final BasicBlock firstContinueBlock;
        if (increment != null) {
            firstIncrementBlock = makeBasicBlock(env.getFunction(), firstBodyBlock.getExceptionHandler(), functionAndBlocksManager);
            AstEnv incrementEnv = bodyEnv.makeAppendBlock(firstIncrementBlock).makeStatementLevel(true).makeResultRegister(AbstractNode.NO_VALUE);
            lastIncrementBlock = process(increment, incrementEnv).getAppendBlock();
            firstContinueBlock = firstIncrementBlock;
        } else {
            firstIncrementBlock = null;
            lastIncrementBlock = null;
            firstContinueBlock = firstConditionBlock;
        }

        // 4. process body
        bodyEnv = bodyEnv.makeUnlabelledContinueAndBreak(firstContinueBlock, firstPostLoopBlock).makeAppendBlock(firstBodyBlock).makeStatementLevel(true);
        if (env.hasLoopLabel(tree)) {
            bodyEnv = bodyEnv.makeLabelledContinue(env.getLoopLabelName(tree), firstContinueBlock);
        }
        TranslationResult processedBody = process(body, bodyEnv);
        BasicBlock lastBodyBlock = processedBody.getAppendBlock();

        // 5. insert increment
        if (firstIncrementBlock != null) {
            lastBodyBlock.addSuccessor(firstIncrementBlock);
            lastBodyBlock = lastIncrementBlock;
        }

        if (Options.get().isLoopUnrollingEnabled()) {
            EndLoopNode ordinaryEndLoopNode = new EndLoopNode(beginLoopNode, loopEndLocation);
            firstPostLoopBlock = endNodeScopeOrdinarily(ordinaryEndLoopNode, conditionAppendBlock.getExceptionHandler(), env.makeAppendBlock(firstPostLoopBlock));
            firstPostLoopBlock = makeSuccessorBasicBlock(env.getFunction(), firstPostLoopBlock, functionAndBlocksManager);
        }

        // 6. make back edge
        if (condition != null && Options.get().isUnrollOneAndAHalfEnabled()) {
            unrollLoopOneAndAHalf(condition, conditionalRegister, firstConditionBlock, lastBodyBlock, firstBodyBlock, firstPostLoopBlock, conditionLocation, env);
        } else {
            lastBodyBlock.addSuccessor(firstConditionBlock);
        }

        return TranslationResult.makeAppendBlock(firstPostLoopBlock);
    }

    /**
     * Processes a function declaration.
     */
    Function processFunctionDeclaration(FunctionDeclarationTree.Kind kind, String name, FormalParameterListTree parameters, ParseTree body, AstEnv env, SourceLocation location) {
        // 1. prepare function object
        List<String> parameterNames = newList();
        if (parameters.hasRestParameter()) {
            throw new AnalysisException("Unsupported parameter style: &rest");
        }
        for (ParseTree parameter : parameters.parameters) {
            parameterNames.add(parameter.asIdentifierExpression().identifierToken.value);
        }

        Function function = new Function(name, parameterNames, env.getFunction(), location);

        AstEnv freshRegistersAndScopeEnv = env.makeRegisterManager(new RegisterManager(AbstractNode.FIRST_ORDINARY_REG));
        boolean expressionContext = kind == FunctionDeclarationTree.Kind.EXPRESSION || (env.getUnevalExpressionResult() != null && env.getUnevalExpressionResult().resultRegister == env.getResultRegister());
        int withScopeRegister = expressionContext ? nextRegister(freshRegistersAndScopeEnv) : AbstractNode.NO_VALUE;
        freshRegistersAndScopeEnv = freshRegistersAndScopeEnv.makeBaseRegister(withScopeRegister);

        AstEnv functionEnv = setupFunction(function, freshRegistersAndScopeEnv, functionAndBlocksManager);

        // 2. register function
        if (expressionContext) {
            addNodeToBlock(new DeclareFunctionNode(function, true, env.getResultRegister(), location), env.getAppendBlock(), env.makeStatementLevel(false));
        } else {
            DeclareFunctionNode declarationNode = new DeclareFunctionNode(function, false, AbstractNode.NO_VALUE, location);
            addNodeToBlock(declarationNode, env.getDeclarationBlock(), env.makeStatementLevel(false /* FIXME, this should be true. But setInterval strings reach this case... */));
            if (USE_REPRESENTATION_NODES) {
                addNodeToBlock(new NopNode("function " + name, declarationNode.getSourceLocation()), env.getAppendBlock(), env.makeStatementLevel(false));
            }
        }

        // 3. wire the function body
        TranslationResult processedBody = process(body, functionEnv);
        processedBody.getAppendBlock().addSuccessor(function.getOrdinaryExit());

        function.setMaxRegister(functionEnv.getRegisterManager().getRegister()); // assumes monotonically increasing register implementation

        // patch source locations
        SourceLocation functionEnd = makeSourceLocationEnd(body);
        function.setSourceLocation(location);
        function.getOrdinaryExit().getFirstNode().setSourceLocation(functionEnd);
        function.getExceptionalExit().getFirstNode().setSourceLocation(functionEnd);

        return function;
    }

    @Override
    TranslationResult process(ArrayLiteralExpressionTree tree, AstEnv env) {
        List<Integer> elementRegisters = newList();
        AstEnv variableEnv = env.makeResultRegister(nextRegister(env)).makeBaseRegister(AbstractNode.NO_VALUE).makeStatementLevel(false);
        SourceLocation location = makeSourceLocation(tree);
        addNodeToBlock(new ReadVariableNode("Array", variableEnv.getResultRegister(), variableEnv.getBaseRegister(), location), env.getAppendBlock(), variableEnv);
        TranslationResult processed = TranslationResult.makeAppendBlock(env.getAppendBlock());
        if (tree.elements != null) {
            for (ParseTree element : tree.elements) {
                if (element.type == ParseTreeType.NULL) {
                    elementRegisters.add(AbstractNode.NO_VALUE); // syntactic case: [42, "x", , , true]
                } else {
                    int elementRegister = nextRegister(env);
                    elementRegisters.add(elementRegister);
                    processed = process(element, env.makeResultRegister(elementRegister).makeStatementLevel(false).makeAppendBlock(processed.getAppendBlock()));
                }
            }
        }
        BasicBlock callBlock = makeSuccessorBasicBlock(env.getFunction(), processed.getAppendBlock(), functionAndBlocksManager);
        AbstractNode callNode = new CallNode(CallNode.LiteralConstructorKinds.ARRAY, env.getResultRegister(), AbstractNode.NO_VALUE, variableEnv.getResultRegister(), elementRegisters, location);
        if (Options.get().isDeterminacyEnabled()) {
            StaticDeterminacyContextSensitivityStrategy.SyntacticHints.get().registerLiteral(callNode, new ASTInfo.LiteralTree(tree), astInfo);
        }
        addNodeToBlock(callNode, callBlock, env);
        BasicBlock postCallBlock = makeSuccessorBasicBlock(env.getFunction(), callBlock, functionAndBlocksManager);
        return TranslationResult.makeAppendBlock(postCallBlock);
    }

    @Override
    TranslationResult process(ProgramTree tree, AstEnv env) {
        return processList(tree.sourceElements, env.makeStatementLevel(true));
    }

    @Override
    TranslationResult process(BlockTree tree, AstEnv env) {
        return processList(tree.statements, env.makeStatementLevel(true));
    }

    @Override
    TranslationResult process(BreakStatementTree tree, AstEnv env) {
        return processJump(tree.type, tree.getLabel(), makeSourceLocation(tree), env.makeStatementLevel(true));
    }

    @Override
    TranslationResult process(CatchTree tree, AstEnv env) {
        // 1. extract information
        SourceLocation location = makeSourceLocation(tree);
        final Function function = env.getFunction();

        final int catchRegister;
        if (tree.exceptionName != null) {
            catchRegister = nextRegister(env);
            addNodeToBlock(new CatchNode(tree.exceptionName.value, catchRegister, location), env.getAppendBlock(), env.makeStatementLevel(false));
        } else {
            catchRegister = env.getResultRegister();
        }

        // 2. create blocks
        BasicBlock appendBlock = makeSuccessorBasicBlock(function, env.getAppendBlock(), functionAndBlocksManager);

        // 3. setup scope
        BeginWithNode beginWithNode = new BeginWithNode(catchRegister, location);
        beginWithNode.setArtificial();
        addNodeToBlock(beginWithNode, env.getAppendBlock(), env.makeStatementLevel(true));

        // 4. exception scope end
        EndWithNode endWithNodeExceptional = new EndWithNode(location);
        endWithNodeExceptional.setArtificial();
        endNodeScopeExceptionally(endWithNodeExceptional, env.makeAppendBlock(appendBlock).makeStatementLevel(true));

        // 5. catch body
        int childRegister = nextRegister(env);
        BasicBlock jumpThroughBlock = new BasicBlock(env.getFunction());
        jumpThroughBlock.setExceptionHandler(appendBlock.getExceptionHandler());
        EndWithNode envWithNodeJump = new EndWithNode(location);
        envWithNodeJump.setArtificial();
        addNodeToBlock(envWithNodeJump, jumpThroughBlock, env);
        AstEnv catchBodyEnv = env.makeBaseRegister(childRegister).makeAppendBlock(appendBlock).makeJumpThroughBlock(new JumpThroughBlocks(jumpThroughBlock, functionAndBlocksManager)).makeStatementLevel(true);
        TranslationResult processedCatchBody = process(tree.catchBody, catchBodyEnv);

        // 6. ordinary scope end
        EndWithNode endWithNodeOrdinary = new EndWithNode(location);
        endWithNodeOrdinary.setArtificial();
        BasicBlock lastBlock = endNodeScopeOrdinarily(endWithNodeOrdinary, processedCatchBody.getAppendBlock().getExceptionHandler(), env.makeStatementLevel(true).makeAppendBlock(processedCatchBody.getAppendBlock()));

        return TranslationResult.makeAppendBlock(lastBlock);
    }

    @Override
    TranslationResult process(ConditionalExpressionTree tree, AstEnv env) {
        return makeConditional(tree.condition, tree.left, tree.right, makeSourceLocation(tree), env.makeStatementLevel(false));
    }

    @Override
    TranslationResult process(ContinueStatementTree tree, AstEnv env) {
        return processJump(tree.type, tree.getLabel(), makeSourceLocation(tree), env.makeStatementLevel(true));
    }

    @Override
    TranslationResult process(DoWhileStatementTree tree, AstEnv env) {
        SourceLocation location = makeSourceLocation(tree);

        // 1. make blocks for break & continue
        BasicBlock exceptionHandler = env.getAppendBlock().getExceptionHandler();
        BasicBlock firstBodyBlock = makeBasicBlock(env.getFunction(), exceptionHandler, functionAndBlocksManager);
        BasicBlock firstPostLoopBlock = makeBasicBlock(env.getFunction(), exceptionHandler, functionAndBlocksManager);
        BasicBlock firstConditionBlock = makeBasicBlock(env.getFunction(), exceptionHandler, functionAndBlocksManager);

        env.getAppendBlock().addSuccessor(firstBodyBlock);

        // 2. make body
        AstEnv loopEnv = env.makeUnlabelledContinueAndBreak(firstConditionBlock, firstPostLoopBlock).makeAppendBlock(firstBodyBlock).makeStatementLevel(true);
        if (env.hasLoopLabel(tree)) {
            loopEnv = loopEnv.makeLabelledContinue(env.getLoopLabelName(tree), firstConditionBlock);
        }
        TranslationResult processedBody = process(tree.body, loopEnv);
        BasicBlock lastBodyBlock = processedBody.getAppendBlock();

        // 3. make condition
        lastBodyBlock.addSuccessor(firstConditionBlock);
        int conditionalRegister = nextRegister(env);

        AstEnv conditionEnv = env.makeResultRegister(conditionalRegister).makeAppendBlock(firstConditionBlock).makeStatementLevel(false);
        TranslationResult processedCondition = process(tree.condition, conditionEnv);
        BasicBlock lastConditionBlock = processedCondition.getAppendBlock();

        // 4. make branch
        IfNode ifNode = new IfNode(conditionalRegister, location);
        ifNode.setSuccessors(firstBodyBlock, firstPostLoopBlock);
        addNodeToBlock(ifNode, lastConditionBlock, env.makeStatementLevel(false));
        lastConditionBlock.addSuccessor(firstBodyBlock);
        lastConditionBlock.addSuccessor(firstPostLoopBlock);

        return TranslationResult.makeAppendBlock(firstPostLoopBlock);
    }

    @Override
    TranslationResult process(MemberLookupExpressionTree tree, AstEnv env) {
        TranslationResult processed = processAccessPartly(tree, env);

        BasicBlock appendBlock = read(processed.getResultReference(), env.makeAppendBlock(processed.getAppendBlock()));
        processed = TranslationResult.makeResultReference(processed.getResultReference(), appendBlock);
        return processed;
    }

    @Override
    TranslationResult process(EmptyStatementTree tree, AstEnv env) {
        return TranslationResult.makeAppendBlock(env.getAppendBlock());
    }

    @Override
    TranslationResult process(ExpressionStatementTree tree, AstEnv env) {
        if (tree.expression.type == ParseTreeType.LITERAL_EXPRESSION && tree.expression.asLiteralExpression().literalToken.type == TokenType.STRING) {
            // check whether this is a TAJS directive
            final LiteralToken text = tree.expression.asLiteralExpression().literalToken.asLiteral();
            SourceLocation location = makeSourceLocation(tree);
            final AbstractNode directiveNode = makeDirectiveNode(ClosureASTUtil.normalizeString(text), location);
            if (directiveNode != null) {
                addNodeToBlock(directiveNode, env.getAppendBlock(), env.makeStatementLevel(true));
                return TranslationResult.makeAppendBlock(env.getAppendBlock());
            }
        }
        return process(tree.expression, env.makeStatementLevel(true).makeResultRegister(AbstractNode.NO_VALUE));
    }

    @Override
    TranslationResult process(ForInStatementTree tree, AstEnv env) {
        // 1. prepare some values
        final SourceLocation loopStartLocation = makeSourceLocation(tree);
        final SourceLocation loopEndLocation = makeSourceLocationEnd(tree);

        int objectRegister = nextRegister(env);
        int propertyListRegister = nextRegister(env);
        int conditionRegister = nextRegister(env);
        int propertyRegister = nextRegister(env);

        BasicBlock appendBlock = env.getAppendBlock();

        // 2. process the loop-header (this is *only* the rhs of the 'lhs in rhs'!)
        AstEnv collectionEnv = env.makeAppendBlock(appendBlock).makeResultRegister(objectRegister).makeStatementLevel(false);
        TranslationResult processedCollection = process(tree.collection, collectionEnv);
        appendBlock = processedCollection.getAppendBlock();
        appendBlock = makeSuccessorBasicBlock(env.getFunction(), appendBlock, functionAndBlocksManager);

        // 3. start the for-in
        BeginForInNode beginForIn = new BeginForInNode(objectRegister, propertyListRegister, loopStartLocation);
        addNodeToBlock(beginForIn, appendBlock, env.makeStatementLevel(false));
        BasicBlock beginForInBlock = appendBlock;

        // 4. create conditional (has-next-property) for the loop
        BasicBlock conditionBlock = makeSuccessorBasicBlock(env.getFunction(), appendBlock, functionAndBlocksManager);

        EndForInNode exceptionalEndForIn = new EndForInNode(beginForIn, loopEndLocation);
        endNodeScopeExceptionally(exceptionalEndForIn, env.makeAppendBlock(conditionBlock).makeStatementLevel(true));

        addNodeToBlock(new HasNextPropertyNode(propertyListRegister, conditionRegister, loopStartLocation), conditionBlock, env.makeStatementLevel(false));

        BasicBlock trueBranch = makeSuccessorBasicBlock(env.getFunction(), conditionBlock, functionAndBlocksManager);
        BasicBlock falseBranch = makeSuccessorBasicBlock(env.getFunction(), conditionBlock, functionAndBlocksManager);

        // 5. make the end of the loop to account for syntactic BasicBlock.entryBlock computation
        // (another End is inserted at the end of the loop-body, and the nodetransfer implementation only needs that End node in practice)
        falseBranch = endNodeScopeOrdinarily(new EndForInNode(beginForIn, loopEndLocation), falseBranch.getExceptionHandler(), env.makeStatementLevel(true).makeAppendBlock(falseBranch));

        IfNode ifNode = new IfNode(conditionRegister, loopStartLocation);
        addNodeToBlock(ifNode, conditionBlock, env.makeStatementLevel(false));
        ifNode.setSuccessors(trueBranch, falseBranch);
        ifNode.setArtificial();

        falseBranch = makeSuccessorBasicBlock(env.getFunction(), falseBranch, functionAndBlocksManager);

        // 6. create iterator (next-property), and procsses the lhs of the 'lhs in rhs'
        final AstEnv bodyEnv = env.makeAppendBlock(trueBranch);

        appendBlock = bodyEnv.getAppendBlock();

        AstEnv loopVariableEnv = bodyEnv.makeAppendBlock(appendBlock).makeResultRegister(AbstractNode.NO_VALUE).makeStatementLevel(false);
        TranslationResult processedLoopVariable = processAccessPartly(desugarForInLoopVariable(tree.initializer, loopVariableEnv), loopVariableEnv);
        appendBlock = processedLoopVariable.getAppendBlock();
        NextPropertyNode nextPropertyNode = new NextPropertyNode(propertyListRegister, propertyRegister, makeSourceLocation(tree.initializer));

        addNodeToBlock(nextPropertyNode, appendBlock, bodyEnv.makeStatementLevel(false));
        writeWithCustomLocation(processedLoopVariable.getResultReference(), propertyRegister, bodyEnv.makeAppendBlock(appendBlock).makeStatementLevel(true), makeSourceLocation(tree.initializer));

        // 7. create body
        BasicBlock jumpThroughBlock = new BasicBlock(bodyEnv.getFunction());
        jumpThroughBlock.setExceptionHandler(env.getAppendBlock().getExceptionHandler());
        addNodeToBlock(new EndForInNode(beginForIn, loopEndLocation), jumpThroughBlock, bodyEnv.makeStatementLevel(true));
        AstEnv syntacticBodyEnv = bodyEnv.makeUnlabelledContinueAndBreak(beginForInBlock, falseBranch).makeJumpThroughBlock(new JumpThroughBlocks(jumpThroughBlock, functionAndBlocksManager)).makeAppendBlock(appendBlock).makeStatementLevel(true);
        if (env.hasLoopLabel(tree)) {
            syntacticBodyEnv = syntacticBodyEnv.makeLabelledContinue(env.getLoopLabelName(tree), beginForInBlock);
        }
        TranslationResult processedBody = process(tree.body, syntacticBodyEnv);
        BasicBlock endOfBody = processedBody.getAppendBlock();

        endOfBody = endNodeScopeOrdinarily(new EndForInNode(beginForIn, loopEndLocation), endOfBody.getExceptionHandler(), env.makeStatementLevel(true).makeAppendBlock(endOfBody));

        endOfBody.addSuccessor(beginForInBlock);

        // falseBranch.setExceptionHandler(env.getAppendBlock().getExceptionHandler());

        return TranslationResult.makeAppendBlock(falseBranch);
    }

    @Override
    TranslationResult process(ForStatementTree tree, AstEnv env) {
        if (Options.get().isDeterminacyEnabled()) {
            StaticDeterminacyContextSensitivityStrategy.SyntacticHints.get().registerLoop(new ASTInfo.LoopTree(tree), env, astInfo);
        }
        ParseTree condition = tree.condition.type == ParseTreeType.NULL ? null : tree.condition;
        return processLoop(tree, tree.initializer, condition, tree.increment, tree.body, true, makeSourceLocation(tree.condition), env);
    }

    @Override
    TranslationResult process(CallExpressionTree tree, AstEnv env) {
        return processInvocation(tree.operand, tree.arguments, false, makeSourceLocation(tree), env);
    }

    @Override
    TranslationResult process(FunctionDeclarationTree tree, AstEnv env) {
        String name = tree.name == null ? null : tree.name.value;
        Function function = processFunctionDeclaration(tree.kind, name, tree.formalParameterList, tree.functionBody, env, makeSourceLocation(tree));
        Set<String> closureVariables = astInfo.getFunctionClosureVariables().get(tree);
        function.getClosureVariableNames().addAll(closureVariables == null ? newSet() : closureVariables);
        return TranslationResult.makeAppendBlock(env.getAppendBlock());
    }

    @Override
    TranslationResult process(IfStatementTree tree, AstEnv env) {
        return makeConditional(tree.condition, tree.ifClause, tree.elseClause, makeSourceLocation(tree), env.makeStatementLevel(true));
    }

    @Override
    TranslationResult process(BinaryOperatorTree tree, AstEnv env) {
        SourceLocation sourceLocation = makeSourceLocation(tree);
        ParseTree lhs = tree.left;
        ParseTree rhs = tree.right;
        if (ClosureASTUtil.isAssignment(tree)) {
            // assignments: either plain '=' or compound: '@='
            if (tree.operator.type == TokenType.EQUAL) {
                // decide result register now, in case the assignment is used as an expression
                int rhsResultRegister = getUsableResultRegister(env);

                TranslationResult processedLhs = processAccessPartly(lhs, env);

                UnevalExpressionResult unevalHack = null;
                if (Options.get().isUnevalizerEnabled() && processedLhs.getResultReference().type == Reference.Type.Variable) {
                    UnevalExpressionResult maybeHack = env.getUnevalExpressionResult();
                    if (maybeHack != null) {
                        String variableName = processedLhs.getResultReference().asVariable().name;
                        if (variableName.equals(maybeHack.specialVariableName)) {
                            unevalHack = maybeHack;
                        }
                    }
                }

                AstEnv rhsEnv = env.makeResultRegister(rhsResultRegister).makeAppendBlock(processedLhs.getAppendBlock()).makeStatementLevel(false);
                if (unevalHack != null) {
                    rhsEnv = rhsEnv.makeResultRegister(unevalHack.resultRegister);
                }

                TranslationResult processed = process(rhs, rhsEnv);

                if (unevalHack == null) {
                    BasicBlock writeAppendBlock = write(processedLhs.getResultReference(), rhsEnv.getResultRegister(), env.makeAppendBlock(processed.getAppendBlock()));
                    processed = TranslationResult.makeAppendBlock(writeAppendBlock);
                }

                return processed;
            } else {
                return processCompoundAssignmentOperation(true, false, getFlowGraphBinaryOperationFromCompoundAssignment(tree), lhs, rhs, env, sourceLocation);
            }
        } else if (tree.operator.type == TokenType.AND || tree.operator.type == TokenType.OR) {
            // short-circuit && and || are special binary operations wrt. control flow
            return processShortCircuitOperators(tree, env);
        } else {
            /* From ES5, Annex D:
             ECMAScript generally uses a left to right evaluation order, however the Edition 3 specification
             language for the > and <= operators resulted in a partial right to left order. The specification
             has been corrected for these operators such that it now specifies a full left to right evaluation
             order. However, this change of order is potentially observable if side-effects occur during the
             evaluation process.
             */
            // Implementation: evaluation order is left to right

            // regular binary operations
            AstEnv leftEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
            int rhsResultRegister = nextRegister(env); /* compute result register now for test-comparibility with legacy flowgraph builder */
            TranslationResult processedLeft = process(lhs, leftEnv);
            AstEnv rightEnv = env.makeResultRegister(rhsResultRegister).makeAppendBlock(processedLeft.getAppendBlock()).makeStatementLevel(false);
            TranslationResult processedRight = process(rhs, rightEnv);
            BinaryOperatorNode operatorNode = new BinaryOperatorNode(getFlowGraphBinaryNonAssignmentOp(tree.operator.type), leftEnv.getResultRegister(), rightEnv.getResultRegister(), env.getResultRegister(), sourceLocation);
            addNodeToBlock(operatorNode, processedRight.getAppendBlock(), env);
            return processedRight;
        }
    }

    @Override
    TranslationResult process(LabelledStatementTree tree, AstEnv env) {
        ParseTree statement = tree.statement;

        String name = tree.name.value;
        BasicBlock breakBlock = makeBasicBlock(env.getFunction(), env.getAppendBlock().getExceptionHandler(), functionAndBlocksManager);
        // note: it is a syntax error to try to continue to a non-iteration-statement label.
        AstEnv labelledEnv = env.makeLabelledBreak(name, breakBlock).makeStatementLevel(true);

        boolean isLoopStatement = statement instanceof ForStatementTree ||
                statement instanceof WhileStatementTree ||
                statement instanceof ForInStatementTree ||
                statement instanceof DoWhileStatementTree;
        if (isLoopStatement) {
            labelledEnv = labelledEnv.makeLoopLabelName(statement, name);
        }
        TranslationResult processed = process(statement, labelledEnv);

        processed.getAppendBlock().addSuccessor(breakBlock);
        return TranslationResult.makeAppendBlock(breakBlock);
    }

    @Override
    TranslationResult process(IdentifierExpressionTree tree, AstEnv env) {
        TranslationResult processed = processAccessPartly(tree, env);
        BasicBlock append = read(processed.getResultReference(), env);
        processed = TranslationResult.makeResultReference(processed.getResultReference(), append);
        return processed;
    }

    @Override
    TranslationResult process(NewExpressionTree tree, AstEnv env) {
        return processInvocation(tree.operand, tree.arguments, true, makeSourceLocation(tree), env);
    }

    @Override
    TranslationResult process(ObjectLiteralExpressionTree tree, AstEnv env) {
        final int thisRegister = getUsableResultRegister(env);
        NewObjectNode node = new NewObjectNode(thisRegister, makeSourceLocation(tree));
        addNodeToBlock(node, env.getAppendBlock(), env);
        if (Options.get().isDeterminacyEnabled()) {
            StaticDeterminacyContextSensitivityStrategy.SyntacticHints.get().registerLiteral(node, new ASTInfo.LiteralTree(tree), astInfo);
        }
        final AstEnv propertyEnv = env.makeThisRegister(thisRegister).makeStatementLevel(false);
        return processList(tree.propertyNameAndValues, propertyEnv);
    }

    @Override
    TranslationResult process(ParenExpressionTree tree, AstEnv env) {
        return process(tree.expression, env.makeStatementLevel(false));
    }

    @Override
    TranslationResult process(MemberExpressionTree tree, AstEnv env) {
        TranslationResult processed = processAccessPartly(tree, env);
        BasicBlock appendBlock = read(processed.getResultReference(), env.makeAppendBlock(processed.getAppendBlock()));
        processed = TranslationResult.makeResultReference(processed.getResultReference(), appendBlock);
        return processed;
    }

    @Override
    TranslationResult process(ReturnStatementTree tree, AstEnv env) {
        if (env.getFunction().getOuterFunction() == null) { // parser doesn't warn about this
            throw new AnalysisException(makeSourceLocation(tree) + ": Syntax Error: Top level returns are not allowed.");
        }
        final TranslationResult processedReturnValue;
        if (tree.expression == null) {
            // implicit return of 'undefined'
            processedReturnValue = TranslationResult.makeAppendBlock(env.getAppendBlock());
            addNodeToBlock(ConstantNode.makeUndefined(AbstractNode.RETURN_REG, makeSourceLocation(tree)), processedReturnValue.getAppendBlock(), env);
        } else {
            processedReturnValue = process(tree.expression, env.makeResultRegister(AbstractNode.RETURN_REG).makeStatementLevel(false));
        }
        AstEnv jumpEnv = env.makeAppendBlock(processedReturnValue.getAppendBlock());
        return processJump(tree.type, null, makeSourceLocation(tree), jumpEnv.makeStatementLevel(true));
    }

    @Override
    TranslationResult process(CaseClauseTree tree, AstEnv env) {
        throw new AnalysisException("Top level case detected, weird.");
    }

    @Override
    TranslationResult process(SwitchStatementTree tree, AstEnv env) {
        SourceLocation location = makeSourceLocation(tree);
        // 1. resolve the operand
        int operandRegister = nextRegister(env);
        TranslationResult processedSequence = process(tree.expression, env.makeResultRegister(operandRegister).makeStatementLevel(false));

        // 2. create "return" block
        BasicBlock returnBlock = makeBasicBlock(env.getFunction(), processedSequence.getAppendBlock().getExceptionHandler(), functionAndBlocksManager);

        // 3. process cases (save default case for last)
        DefaultClauseTree defaultCase = null;
        BasicBlock trueBranch = null;
        BasicBlock oldTrueBranch;
        BasicBlock falseBranch = null;
        int caseRegister = tree.caseClauses.isEmpty() ? AbstractNode.NO_VALUE : nextRegister(env);
        int comparisonRegister = tree.caseClauses.isEmpty() ? AbstractNode.NO_VALUE : nextRegister(env);
        final AstEnv caseEnv = env.makeUnlabelledBreak(returnBlock);
        for (ParseTree caseTree : tree.caseClauses) {
            // 3.1. handle syntactic cases
            final ParseTree expression;
            final ImmutableList<ParseTree> body;
            switch (caseTree.type) {
                case DEFAULT_CLAUSE:
                    defaultCase = caseTree.asDefaultClause();
                    continue;
                case CASE_CLAUSE:
                    CaseClauseTree caseClause = caseTree.asCaseClause();
                    expression = caseClause.expression;
                    body = caseClause.statements;
                    break;
                default:
                    throw new AnalysisException("Unhandled child type of switch:" + caseTree.type);
            }

            // 3.2. create comparison
            processedSequence = process(expression, caseEnv.makeResultRegister(caseRegister).makeAppendBlock(processedSequence.getAppendBlock()).makeStatementLevel(false));

            BinaryOperatorNode equalityNode = new BinaryOperatorNode(BinaryOperatorNode.Op.SEQ, operandRegister, caseRegister, comparisonRegister, location);
            addNodeToBlock(equalityNode, processedSequence.getAppendBlock(), caseEnv.makeStatementLevel(false));

            // 3.3. dispatch on comparison result
            oldTrueBranch = trueBranch;

            BasicBlock equalityCheckAppendBlock = processedSequence.getAppendBlock();
            trueBranch = makeSuccessorBasicBlock(env.getFunction(), equalityCheckAppendBlock, functionAndBlocksManager);
            falseBranch = makeSuccessorBasicBlock(env.getFunction(), equalityCheckAppendBlock, functionAndBlocksManager);

            if (oldTrueBranch != null && !oldTrueBranch.getSuccessors().contains(returnBlock)) {
                oldTrueBranch.addSuccessor(trueBranch);
            }

            IfNode ifNode = new IfNode(comparisonRegister, location);
            AstEnv ifEnv = caseEnv.makeAppendBlock(equalityCheckAppendBlock).makeStatementLevel(false);
            addNodeToBlock(ifNode, ifEnv.getAppendBlock(), ifEnv);
            ifNode.setSuccessors(trueBranch, falseBranch);

            // 3.4. handle body
            TranslationResult processedBody = processList(body, caseEnv.makeAppendBlock(trueBranch));
            trueBranch = processedBody.getAppendBlock();
            processedSequence = TranslationResult.makeAppendBlock(falseBranch);
        }

        if (defaultCase == null) {
            // 4. handle the no-default case
            if (trueBranch != null) {
                // let the last branch-end propagate naturally to outside the switch
                trueBranch.addSuccessor(returnBlock);
            }
        } else {
            boolean lastCaseIsDefaultCase = tree.caseClauses.reverse().get(0).type == ParseTreeType.DEFAULT_CLAUSE;
            if (!lastCaseIsDefaultCase) {
                // Actual switch behaviour: if no branches are matched, the default branch is taken - with fallthrough to the succeeding branches!
                // We consider it a weird programming style, and choose not to make a complex flowgraph to handle that case...
                throw new AnalysisException(makeSourceLocation(tree) + ": No support for default-case in non-last position");
            }
            // 5. handle default case
            // 5.1 : propagate from either case
            BasicBlock defaultBlock = makeSuccessorBasicBlock(env.getFunction(), processedSequence.getAppendBlock(), functionAndBlocksManager);
            if (falseBranch != null && !falseBranch.getSuccessors().contains(returnBlock)) {
                falseBranch.addSuccessor(defaultBlock);
            }
            if (trueBranch != null && !trueBranch.getSuccessors().contains(returnBlock)) {
                trueBranch.addSuccessor(defaultBlock);
            }
            // 5.2 handle body
            processedSequence = processList(defaultCase.statements, caseEnv.makeAppendBlock(defaultBlock));
        }

        // 6. propagate again
        processedSequence.getAppendBlock().addSuccessor(returnBlock);
        return TranslationResult.makeAppendBlock(returnBlock);
    }

    @Override
    TranslationResult process(ThrowStatementTree tree, AstEnv env) {
        AstEnv throwableEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
        TranslationResult processedThrowable = process(tree.value, throwableEnv);
        addNodeToBlock(new ThrowNode(throwableEnv.getResultRegister(), makeSourceLocation(tree)), processedThrowable.getAppendBlock(), env.makeStatementLevel(true));
        return processedThrowable;
    }

    @Override
    TranslationResult process(TryStatementTree tree, AstEnv env) {
        // 1. extract and simplify relevant information
        SourceLocation sourceLocation = makeSourceLocation(tree);
        ParseTree tryTree = tree.body;
        CatchTree catchTree = tree.catchBlock == null || tree.catchBlock.type == ParseTreeType.NULL ? null : tree.catchBlock.asCatch().asCatch();
        ParseTree finallyTree = tree.finallyBlock;

        // don't bother with translating finally-nodes without children
        finallyTree = finallyTree != null && finallyTree.asFinally().block.type == ParseTreeType.BLOCK && finallyTree.asFinally().block.asBlock().statements.isEmpty() ? null : finallyTree;

        final BasicBlock originalAppendBlock = env.getAppendBlock();
        final BasicBlock originalExceptionBlock = originalAppendBlock.getExceptionHandler();
        final Function function = originalAppendBlock.getFunction();

        // 2. set up the blocks
        final BasicBlock tryStartBlock = makeSuccessorBasicBlock(function, originalAppendBlock, functionAndBlocksManager);
        final BasicBlock catchStartBlock = catchTree != null ? makeCatchBasicBlock(function, tryStartBlock, functionAndBlocksManager) : null;
        final BasicBlock exceptionalStartFinallyBlock = finallyTree != null ? makeBasicBlock(function, originalExceptionBlock, functionAndBlocksManager) : null;
        final BasicBlock ordinaryFinallyStartBlock = makeBasicBlock(function, originalExceptionBlock, functionAndBlocksManager);

        if (exceptionalStartFinallyBlock != null) {
            if (catchStartBlock != null) {
                catchStartBlock.setExceptionHandler(exceptionalStartFinallyBlock);
            } else {
                tryStartBlock.setExceptionHandler(exceptionalStartFinallyBlock);
            }
        }

        // 3. translate finally first - it is needed for jumpThroughBlocks during the try and catch
        final TranslationResult exceptionalProcessedFinally;
        final TranslationResult ordinarilyProcessedFinally;
        final List<BasicBlock> ordinaryFinallyBlocks;
        final List<BasicBlock> exceptionalFinallyBlocks;
        final JumpThroughBlocks finallyJumpThroughBlocks;
        if (exceptionalStartFinallyBlock != null) {
            // 1. create the exceptional finally blocks
            int tryRegister = nextRegister(env);

            // (note: for the empty finally block, a non-required [(catch v42), (throw v42)] will be created)
            CatchNode finallyCatchNode = new CatchNode(tryRegister, sourceLocation);
            finallyCatchNode.setArtificial();
            addNodeToBlock(finallyCatchNode, exceptionalStartFinallyBlock, env.makeStatementLevel(false));

            FunctionAndBlockManager.SessionKey exceptionalSessionKey = functionAndBlocksManager.startSession();
            exceptionalProcessedFinally = process(finallyTree, env.makeAppendBlock(exceptionalStartFinallyBlock).makeStatementLevel(true));
            functionAndBlocksManager.endSession(exceptionalSessionKey);

            exceptionalFinallyBlocks = newList();
            exceptionalFinallyBlocks.add(exceptionalStartFinallyBlock);
            exceptionalFinallyBlocks.addAll(functionAndBlocksManager.getSessionBlocks(exceptionalSessionKey));

            // the exception "caught" by finally needs to be rethrown
            ThrowNode finallyRethrowNode = new ThrowNode(tryRegister, sourceLocation);
            finallyRethrowNode.setArtificial();
            BasicBlock lastExceptionalFinallyBlock = exceptionalProcessedFinally.getAppendBlock();
            addNodeToBlock(finallyRethrowNode, lastExceptionalFinallyBlock, env.makeStatementLevel(true));
            lastExceptionalFinallyBlock.addSuccessor(originalExceptionBlock);

            // 2. create the ordinary finally blocks
            // copy the finally body to keep exceptional and ordinary flow separate.
            FunctionAndBlockManager.SessionKey ordinarySessionKey = functionAndBlocksManager.startSession();
            ordinarilyProcessedFinally = process(finallyTree, env.makeAppendBlock(ordinaryFinallyStartBlock).makeStatementLevel(true));
            functionAndBlocksManager.endSession(ordinarySessionKey);

            ordinaryFinallyBlocks = newList();
            ordinaryFinallyBlocks.add(ordinaryFinallyStartBlock);
            ordinaryFinallyBlocks.addAll(functionAndBlocksManager.getSessionBlocks(ordinarySessionKey));

            // mark the nodes for the exceptional flow as duplicates
            setDuplicateBlocks(newSet(exceptionalFinallyBlocks), newSet(), newSet(Arrays.asList(finallyCatchNode, finallyRethrowNode)), exceptionalStartFinallyBlock, ordinaryFinallyStartBlock);

            finallyJumpThroughBlocks = new JumpThroughBlocks(ordinaryFinallyStartBlock, ordinarilyProcessedFinally.getAppendBlock(), ordinaryFinallyBlocks, functionAndBlocksManager);
        } else {
            finallyJumpThroughBlocks = null;
            ordinarilyProcessedFinally = TranslationResult.makeAppendBlock(ordinaryFinallyStartBlock);
        }

        // 4. translate try
        AstEnv tryEnv = env.makeAppendBlock(tryStartBlock).makeStatementLevel(true);
        if (finallyJumpThroughBlocks != null) {
            tryEnv = tryEnv.makeJumpThroughBlock(finallyJumpThroughBlocks);
        }
        TranslationResult processedTry = process(tryTree, tryEnv);

        // the try is followed by the finallly
        processedTry.getAppendBlock().addSuccessor(ordinaryFinallyStartBlock);

        // 5. translate catch
        if (catchStartBlock != null) {
            AstEnv catchEnv = env.makeAppendBlock(catchStartBlock).makeStatementLevel(true);
            if (finallyJumpThroughBlocks != null) {
                catchEnv = catchEnv.makeJumpThroughBlock(finallyJumpThroughBlocks);
            }
            TranslationResult processedCatch = process(catchTree, catchEnv);

            BasicBlock lastCatchBlock = processedCatch.getAppendBlock();

            // the non-exceptional catch is followed by the non-exceptional finallly
            lastCatchBlock.addSuccessor(ordinaryFinallyStartBlock);
            // (the exceptional catch is followed by the exceptional finallly - but this is handled through the jumpThroughBlocks)
        }

        return ordinarilyProcessedFinally;
    }

    @Override
    TranslationResult process(UnaryExpressionTree tree, AstEnv env) {
        ParseTree operand = stripParens(tree.operand);
        if (operand.type == ParseTreeType.LITERAL_EXPRESSION && operand.asLiteralExpression().literalToken.type == TokenType.NUMBER && tree.operator.type == TokenType.MINUS) {
            // special case: primitive constant-folding to match the old flowgraph builder
            double number = closureUtil.normalizeNumber(operand.asLiteralExpression().literalToken.asLiteral());
            addNodeToBlock(ConstantNode.makeNumber(-1 * number, env.getResultRegister(), makeSourceLocation(tree)), env.getAppendBlock(), env);
            return TranslationResult.makeAppendBlock(env.getAppendBlock());
        }
        TranslationResult processedSub;
        final AbstractNode node;
        SourceLocation sourceLocation = makeSourceLocation(tree);
        switch (tree.operator.type) {
            case TYPEOF: {
                // typeof is a special unary operator, with two variants
                if (operand.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
                    node = new TypeofNode(operand.asIdentifierExpression().identifierToken.value, env.getResultRegister(), sourceLocation, makeSourceLocation(operand));
                    processedSub = TranslationResult.makeAppendBlock(env.getAppendBlock());
                } else {
                    AstEnv subEnv = env.makeResultRegister(nextRegister(env));
                    processedSub = process(operand, subEnv.makeStatementLevel(false));
                    node = new TypeofNode(subEnv.getResultRegister(), env.getResultRegister(), sourceLocation, makeSourceLocation(operand));
                }
                break;
            }
            case VOID: {
                // void x produces 'undefined'
                processedSub = process(tree.operand, env.makeResultRegister(AbstractNode.NO_VALUE).makeStatementLevel(false));
                node = ConstantNode.makeUndefined(env.getResultRegister(), sourceLocation);
                node.setArtificial();
                break;
            }
            case DELETE: {
                AstEnv subEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
                processedSub = processAccessPartly(tree.operand, subEnv);
                Reference reference = processedSub.getResultReference();
                if (reference == null) {
                    // case: `delete 42` - will be caugth as a syntax error in the parser.
                    // If we need to support it: NodeTransfer should return `true`
                    throw new AnalysisException("Unexpected delete");
                }
                switch (reference.type) {
                    case Variable:
                        node = new DeletePropertyNode(reference.asVariable().name, env.getResultRegister(), sourceLocation);
                        break;
                    case StaticProperty:
                        node = new DeletePropertyNode(reference.asStaticProperty().baseRegister, reference.asStaticProperty().propertyName, env.getResultRegister(), sourceLocation);
                        break;
                    case DynamicProperty:
                        node = new DeletePropertyNode(reference.asDynamicProperty().baseRegister, reference.asDynamicProperty().propertyRegister, env.getResultRegister(), sourceLocation);
                        break;
                    default:
                        throw new RuntimeException("Unhandled reference type: " + reference.type);
                }
                break;
            }
            case PLUS_PLUS:
            case MINUS_MINUS: {
                // ++x and --x
                return processCompoundAssignmentOperation(true, true, getPrefixPostfixOp(tree.operator.type), tree.operand, makeConstant(1, tree.location), env, makeSourceLocation(tree));
            }
            default: {
                // regular unaries
                AstEnv subEnv = env.makeResultRegister(nextRegister(env));
                processedSub = process(operand, subEnv.makeStatementLevel(false));
                node = new UnaryOperatorNode(getFlowGraphUnaryNonAssignmentOp(tree.operator.type), subEnv.getResultRegister(), env.getResultRegister(), sourceLocation);
                break;
            }
        }
        addNodeToBlock(node, processedSub.getAppendBlock(), env);
        return processedSub;
    }

    @Override
    TranslationResult process(VariableStatementTree tree, AstEnv env) {
        return process(tree.declarations, env.makeStatementLevel(true));
    }

    @Override
    TranslationResult process(VariableDeclarationListTree tree, AstEnv env) {
        if (tree.declarationType != TokenType.VAR) {
            throw new AnalysisException(makeSourceLocation(tree) + ": Only var declarations supported, " + tree.declarationType + " is not supported");
        }
        return processList(tree.declarations, env.makeStatementLevel(true));
    }

    @Override
    TranslationResult process(VariableDeclarationTree declaration, AstEnv env) {
        final String variableName;
        if (declaration.lvalue.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
            variableName = declaration.lvalue.asIdentifierExpression().identifierToken.value;
        } else {
            throw new AnalysisException(makeSourceLocation(declaration.lvalue) + ": Only identifier-var declarations supported");
        }
        if (!env.getFunction().getVariableNames().contains(variableName)) {
            DeclareVariableNode declarationNode = new DeclareVariableNode(variableName, makeSourceLocation(declaration.lvalue));
            addNodeToBlock(declarationNode, env.getDeclarationBlock(), env.makeStatementLevel(true));
            if (USE_REPRESENTATION_NODES) {
                addNodeToBlock(new NopNode("var " + variableName, declarationNode.getSourceLocation()), env.getAppendBlock(), env.makeStatementLevel(true));
            }
        }
        env.getFunction().addVariableName(variableName);
        final TranslationResult processed;
        if (declaration.initializer != null) {
            AstEnv rhsEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
            processed = process(declaration.initializer, rhsEnv);
            addNodeToBlock(new WriteVariableNode(rhsEnv.getResultRegister(), variableName, makeSourceLocation(declaration.initializer)), processed.getAppendBlock(), env.makeStatementLevel(true));
        } else {
            processed = TranslationResult.makeAppendBlock(env.getAppendBlock());
        }
        return processed;
    }

    @Override
    TranslationResult process(WhileStatementTree tree, AstEnv env) {
        if (Options.get().isDeterminacyEnabled()) {
            StaticDeterminacyContextSensitivityStrategy.SyntacticHints.get().registerLoop(new ASTInfo.LoopTree(tree), env, astInfo);
        }
        return processLoop(tree, null, tree.condition, null, tree.body, false, makeSourceLocation(tree.condition), env);
    }

    @Override
    TranslationResult process(WithStatementTree tree, AstEnv env) {
        SourceLocation location = makeSourceLocation(tree);
        int withRegister = nextRegister(env);

        // 1. read the with-expression
        final AstEnv expressionEnv = env.makeResultRegister(withRegister).makeStatementLevel(false);
        TranslationResult processedExpression = process(tree.expression, expressionEnv);
        BasicBlock appendBlock = processedExpression.getAppendBlock();

        // 2. begin the with-environment (and end it exceptionally)
        addNodeToBlock(new BeginWithNode(withRegister, location), appendBlock, env.makeStatementLevel(true));
        appendBlock = makeSuccessorBasicBlock(env.getFunction(), appendBlock, functionAndBlocksManager);

        endNodeScopeExceptionally(new EndWithNode(location), env.makeAppendBlock(appendBlock).makeStatementLevel(true));

        // 3. process the body (and end it ordinarily)
        BasicBlock jumpThroughBlock = new BasicBlock(env.getFunction());
        jumpThroughBlock.setExceptionHandler(env.getAppendBlock().getExceptionHandler());
        addNodeToBlock(new EndWithNode(location), jumpThroughBlock, env);
        AstEnv bodyEnv = env.makeBaseRegister(nextRegister(env)).makeAppendBlock(appendBlock).makeJumpThroughBlock(new JumpThroughBlocks(jumpThroughBlock, functionAndBlocksManager)).makeStatementLevel(true);
        TranslationResult processedBody = process(tree.body, bodyEnv);
        appendBlock = processedBody.getAppendBlock();

        appendBlock = endNodeScopeOrdinarily(new EndWithNode(location), appendBlock.getExceptionHandler(), env.makeAppendBlock(appendBlock));

        return TranslationResult.makeAppendBlock(appendBlock);
    }

    @Override
    TranslationResult process(ThisExpressionTree tree, AstEnv env) {
        TranslationResult processedAstInfo = processAccessPartly(tree, env);
        env.getFunction().setUsesThis(true);
        processedAstInfo = TranslationResult.makeAppendBlock(read(processedAstInfo.getResultReference(), env.makeBaseRegister(AbstractNode.NO_VALUE)));
        return processedAstInfo;
    }

    @Override
    TranslationResult process(NullTree literalNode, AstEnv env) {
        return TranslationResult.makeAppendBlock(env.getAppendBlock());
    }

    @Override
    TranslationResult process(PostfixExpressionTree tree, AstEnv env) {
        return processCompoundAssignmentOperation(false, true, getPrefixPostfixOp(tree.operator.type), tree.operand, makeConstant(1, tree.location), env, makeSourceLocation(tree));
    }

    @Override
    TranslationResult process(CommaExpressionTree tree, AstEnv env) {
        return processList(tree.expressions, env.makeStatementLevel(false));
    }

    @Override
    TranslationResult process(FinallyTree tree, AstEnv env) {
        return process(tree.block, env); // finally wiring is being done in the try-handler
    }

    @Override
    TranslationResult process(PropertyNameAssignmentTree tree, AstEnv env) {
        AstEnv rhsEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
        TranslationResult processedRhs = process(tree.value, rhsEnv);
        final WritePropertyNode node;
        switch (tree.name.type) {
            case IDENTIFIER: {
                node = new WritePropertyNode(env.getThisRegister(), tree.name.asIdentifier().value, rhsEnv.getResultRegister(), makeSourceLocation(tree));
                break;
            }
            case STRING: {
                node = new WritePropertyNode(env.getThisRegister(), ClosureASTUtil.normalizeString(tree.name.asLiteral()), rhsEnv.getResultRegister(), makeSourceLocation(tree));
                break;
            }
            case NUMBER: {
                node = new WritePropertyNode(env.getThisRegister(), tree.name.asLiteral().value, rhsEnv.getResultRegister(), makeSourceLocation(tree));
                break;
            }
            default:
                throw new RuntimeException("Unhandled property name type: " + tree.name.type);
        }
        addNodeToBlock(node, processedRhs.getAppendBlock(), env);
        return processedRhs;
    }

    @Override
    TranslationResult processBooleanLiteral(LiteralExpressionTree tree, AstEnv env) {
        addNodeToBlock(ConstantNode.makeBoolean(tree.literalToken.type == TokenType.TRUE, env.getResultRegister(), makeSourceLocation(tree)), env.getAppendBlock(), env);
        return TranslationResult.makeAppendBlock(env.getAppendBlock());
    }

    @Override
    TranslationResult processNullLiteral(LiteralExpressionTree tree, AstEnv env) {
        addNodeToBlock(ConstantNode.makeNull(env.getResultRegister(), makeSourceLocation(tree)), env.getAppendBlock(), env);
        return TranslationResult.makeAppendBlock(env.getAppendBlock());
    }

    @Override
    TranslationResult processNumberLiteral(LiteralExpressionTree tree, AstEnv env) {
        double number = closureUtil.normalizeNumber(tree.literalToken.asLiteral());
        ConstantNode node = ConstantNode.makeNumber(number, env.getResultRegister(), makeSourceLocation(tree));
        BasicBlock block = env.getAppendBlock();
        addNodeToBlock(node, block, env);
        return TranslationResult.makeAppendBlock(env.getAppendBlock());
    }

    @Override
    TranslationResult processRegExpLiteral(LiteralExpressionTree tree, AstEnv env) {
        SourceLocation location = makeSourceLocation(tree);

        // 1. prepare the constructor call
        AstEnv variableEnv = env.makeResultRegister(nextRegister(env)).makeBaseRegister(AbstractNode.NO_VALUE).makeStatementLevel(false);
        addNodeToBlock(new ReadVariableNode("RegExp", variableEnv.getResultRegister(), variableEnv.getBaseRegister(), location), env.getAppendBlock(), variableEnv);

        // 2. prepare the arguments
        Pair<String, String> parsed = parseRegExpLiteral(tree.literalToken.asLiteral());
        int patternRegister = nextRegister(env);
        int flagsRegister = parsed.getSecond().isEmpty() ? AbstractNode.NO_VALUE : nextRegister(env);
        List<Integer> args = newList();

        addNodeToBlock(ConstantNode.makeString(parsed.getFirst(), patternRegister, location), env.getAppendBlock(), env.makeStatementLevel(false));
        args.add(patternRegister);

        if (flagsRegister != AbstractNode.NO_VALUE) {
            addNodeToBlock(ConstantNode.makeString(parsed.getSecond(), flagsRegister, location), env.getAppendBlock(), env.makeStatementLevel(false));
            args.add(flagsRegister);
        }

        // 3. call
        BasicBlock callBlock = makeSuccessorBasicBlock(env.getFunction(), env.getAppendBlock(), functionAndBlocksManager);
        addNodeToBlock(new CallNode(CallNode.LiteralConstructorKinds.REGEXP, env.getResultRegister(), AbstractNode.NO_VALUE, variableEnv.getResultRegister(), args, location), callBlock, env);
        return TranslationResult.makeAppendBlock(makeSuccessorBasicBlock(env.getFunction(), callBlock, functionAndBlocksManager));
    }

    @Override
    TranslationResult processStringLiteral(LiteralExpressionTree tree, AstEnv env) {
        String string = ClosureASTUtil.normalizeString(tree.literalToken.asLiteral());
        ConstantNode node = ConstantNode.makeString(string, env.getResultRegister(), makeSourceLocation(tree));
        BasicBlock block = env.getAppendBlock();
        addNodeToBlock(node, block, env);
        return TranslationResult.makeAppendBlock(env.getAppendBlock());
    }

    @Override
    TranslationResult process(DebuggerStatementTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(DefaultClauseTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(GetAccessorTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(SetAccessorTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(FormalParameterListTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(DefaultParameterTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(RestParameterTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(SpreadExpressionTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(ClassDeclarationTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(SuperExpressionTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(YieldExpressionTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(ForOfStatementTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(ExportDeclarationTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(ExportSpecifierTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(ImportDeclarationTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(ImportSpecifierTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(ModuleImportTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(MissingPrimaryExpressionTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult processIllegalToken(ParseTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult process(ComputedPropertyAssignmentTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult processTemplateString(LiteralExpressionTree tree, AstEnv env) {
        throw new NotImplemented(tree, tree.type.toString());
    }

    @Override
    TranslationResult unsupportedLanguageFeature(ParseTree node, String feature, AstEnv env) {
        throw new NotImplemented(node, node.type.toString());
    }
}
