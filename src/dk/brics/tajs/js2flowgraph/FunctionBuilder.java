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
import com.google.javascript.jscomp.parsing.parser.LiteralToken;
import com.google.javascript.jscomp.parsing.parser.Token;
import com.google.javascript.jscomp.parsing.parser.TokenType;
import com.google.javascript.jscomp.parsing.parser.trees.ArgumentListTree;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.BlockTree;
import com.google.javascript.jscomp.parsing.parser.trees.BreakStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.CallExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.CaseClauseTree;
import com.google.javascript.jscomp.parsing.parser.trees.CatchTree;
import com.google.javascript.jscomp.parsing.parser.trees.CommaExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ConditionalExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ContinueStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.DebuggerStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.DefaultClauseTree;
import com.google.javascript.jscomp.parsing.parser.trees.DoWhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.EmptyStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExpressionStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FinallyTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForInStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FormalParameterListTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.GetAccessorTree;
import com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.IfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.LabelledStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.LiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberLookupExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NewExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NullTree;
import com.google.javascript.jscomp.parsing.parser.trees.ObjectLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParenExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import com.google.javascript.jscomp.parsing.parser.trees.PropertyNameAssignmentTree;
import com.google.javascript.jscomp.parsing.parser.trees.ReturnStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.SetAccessorTree;
import com.google.javascript.jscomp.parsing.parser.trees.SwitchStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThisExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThrowStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TryStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.UnaryExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.UpdateExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationListTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WithStatementTree;
import com.google.javascript.jscomp.parsing.parser.util.SourceRange;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.SourceLocation.SourceLocationMaker;
import dk.brics.tajs.flowgraph.TAJSFunctionName;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginWithNode;
import dk.brics.tajs.flowgraph.jsnodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode.LiteralConstructorKinds;
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
import dk.brics.tajs.flowgraph.jsnodes.UnaryOperatorNode.Op;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode.Kind;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.flowgraph.syntaticinfo.DynamicProperty;
import dk.brics.tajs.flowgraph.syntaticinfo.StaticProperty;
import dk.brics.tajs.flowgraph.syntaticinfo.SyntacticReference;
import dk.brics.tajs.flowgraph.syntaticinfo.Variable;
import dk.brics.tajs.js2flowgraph.ASTInfo.LiteralTree;
import dk.brics.tajs.js2flowgraph.ASTInfo.LoopTree;
import dk.brics.tajs.js2flowgraph.FunctionAndBlockManager.SessionKey;
import dk.brics.tajs.js2flowgraph.asttraversals.DefaultDispatchingParseTreeAuxVisitor;
import dk.brics.tajs.js2flowgraph.asttraversals.DispatchingLiteralTreeAuxVisitor;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException.AnalysisModelLimitationException;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.ParseError;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.addNodeToBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.getFlowGraphBinaryNonAssignmentOp;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.getFlowGraphBinaryOperationFromCompoundAssignment;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.getFlowGraphUnaryNonAssignmentOp;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.getPrefixPostfixOp;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.getSource;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeBasicBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeCatchBasicBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeJoinBasicBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeJumpThroughBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeSuccessorBasicBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.parseRegExpLiteral;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.requiresOwnBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.setDuplicateBlocks;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.setupFunction;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.stripParens;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.wireAndRegisterJumpThroughBlocks;
import static dk.brics.tajs.util.AnalysisLimitationException.SyntacticSupportNotImplemented;
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
public class FunctionBuilder extends DefaultDispatchingParseTreeAuxVisitor<TranslationResult, AstEnv> {

    // TODO: enable this to mark lines with `function` or `var` as covered even though they do not have effects.
    private static final boolean USE_REPRESENTATION_NODES = false;

    private final ASTInfo astInfo;

    private final FunctionAndBlockManager functionAndBlocksManager;

    private final SourceLocationMaker sourceLocationMaker;

    private final LiteralBuilder literalBuilder;

    private final SyntacticAnalysis syntacticInformationCollector;

    /**
     * Constructs a new function builder.
     */
    FunctionBuilder(ASTInfo astInfo, FunctionAndBlockManager functionAndBlocksManager, SourceLocationMaker sourceLocationMaker,SyntacticAnalysis syntacticInformationCollector) {
        this.literalBuilder = new LiteralBuilder();
        this.astInfo = astInfo;
        this.functionAndBlocksManager = functionAndBlocksManager;
        this.sourceLocationMaker = sourceLocationMaker;
        this.syntacticInformationCollector = syntacticInformationCollector;
    }

    /**
     * Registers a value access (read/write).
     */
    private BasicBlock access(AbstractNode accessNode, SyntacticReference target, AstEnv env) {
        if (target.type == SyntacticReference.Type.StaticProperty || target.type == SyntacticReference.Type.DynamicProperty) {
            syntacticInformationCollector.registerPropertyAccess(accessNode, target.asProperty());
        }

        // add the access
        addNodeToBlock(accessNode, env.getAppendBlock(), env);
        BasicBlock appendBlock;
        if (requiresOwnBlock(accessNode)) {
            appendBlock = makeSuccessorBasicBlock(env.getAppendBlock(), functionAndBlocksManager);
        } else {
            appendBlock = env.getAppendBlock();
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
        BasicBlock exceptionBlock = makeCatchBasicBlock(env.getAppendBlock(), functionAndBlocksManager);
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
        appendBlock = requiresOwnBlock(lastBodyNode) || requiresOwnBlock(endNode) ? makeSuccessorBasicBlock(appendBlock, functionAndBlocksManager) : appendBlock;
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
        return isReferenceASTType(access.type);
    }

    /**
     * Creates conditional flow with two branches, as for if-statements and ?-:-expressions.
     */
    private TranslationResult makeConditional(ParseTree tree, ParseTree condition, ParseTree left, ParseTree right, SourceLocation sourceLocation, AstEnv env) {

        BasicBlock trueBranch = makeBasicBlock(env.getAppendBlock().getExceptionHandler(), functionAndBlocksManager);
        BasicBlock falseBranch = makeBasicBlock(env.getAppendBlock().getExceptionHandler(), functionAndBlocksManager);

        int conditionResultRegister = nextRegister(env);
        IfNode ifNode = new IfNode(conditionResultRegister, sourceLocation);
        ifNode.setSuccessors(trueBranch, falseBranch);

        AstEnv conditionEnv = env.makeResultRegister(conditionResultRegister).makeStatementLevel(false).makeEnclosingIfNode(condition, ifNode);
        TranslationResult processedCondition = process(condition, conditionEnv);

        processedCondition.getAppendBlock().addSuccessor(trueBranch);
        processedCondition.getAppendBlock().addSuccessor(falseBranch);
        addNodeToBlock(ifNode, processedCondition.getAppendBlock(), env);
        syntacticInformationCollector.registerIfNode(ifNode, tree, astInfo);
        syntacticInformationCollector.registerIfNodeCondition(ifNode, condition);

        IfNode enclosingIfNode = env.getEnclosingIfNode(tree);
        AstEnv branchEnv = env;
        if (enclosingIfNode != null) {
            branchEnv = branchEnv.makeEnclosingIfNode(left, enclosingIfNode);
            if (right != null) {
                branchEnv = branchEnv.makeEnclosingIfNode(right, enclosingIfNode);
            }
        }
        int currentRegister = env.getRegisterManager().getRegister(); /* mutual exclusive branches, so let them share registers */
        TranslationResult processedLeft = process(left, branchEnv.makeRegisterManager(new RegisterManager(currentRegister)).makeAppendBlock(trueBranch));
        TranslationResult processedRight = right == null ? TranslationResult.makeAppendBlock(falseBranch) : process(right, branchEnv.makeRegisterManager(new RegisterManager(currentRegister)).makeAppendBlock(falseBranch));

        BasicBlock joinBlock = makeJoinBasicBlock(processedLeft.getAppendBlock(), processedRight.getAppendBlock(), functionAndBlocksManager);
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
     * @param updatedValueIsResult whether the value of the processed expression is the value of the target (coerced!) value before or after update
     * @param coerceToNumber       whether the target value should be coerced to a number
     * @param op                   the operator to apply to the target value and the operand
     * @param target               the target value, is read and written
     * @param operand              the operand
     * @param env                  the environment
     * @param sourceLocation       the source location of the operation
     */
    private TranslationResult processCompoundAssignmentOperation(boolean updatedValueIsResult, boolean coerceToNumber, BinaryOperatorNode.Op op, ParseTree target, ParseTree operand, AstEnv env, SourceLocation sourceLocation) {
        TranslationResult processedTarget = processAccessPartly(target, env.makeStatementLevel(false));
        final SyntacticReference targetReference = processedTarget.getResultReference();

        int readRegister = updatedValueIsResult ? nextRegister(env) : (getUsableResultRegister(env));
        AstEnv readEnv = env.makeResultRegister(readRegister).makeStatementLevel(false).makeAppendBlock(processedTarget.getAppendBlock());
        BasicBlock appendBlock = read(targetReference, readEnv);

        // convert to number
        AstEnv coercionEnv;
        if (coerceToNumber) {
            int coerctionResultRegister = updatedValueIsResult? nextRegister(env): getUsableResultRegister(env);
            coercionEnv = env.makeResultRegister(coerctionResultRegister).makeStatementLevel(false);
            addNodeToBlock(new UnaryOperatorNode(Op.PLUS, readEnv.getResultRegister(), coercionEnv.getResultRegister(), sourceLocation), appendBlock, coercionEnv);
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

        syntacticInformationCollector.registerCompoundAssignmentOperation(operand, processedTarget.getResultReference().location, sourceLocationMaker);

        appendBlock = write(targetReference, operationEnv.getResultRegister(), env.makeAppendBlock(appendBlock));

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
                return TranslationResult.makeResultReference(new Variable("this", makeSourceLocation(access)), env.getAppendBlock());
            }
            case IDENTIFIER_EXPRESSION: {
                SyntacticReference reference = new Variable(access.asIdentifierExpression().identifierToken.value, makeSourceLocation(access));
                return TranslationResult.makeResultReference(reference, env.getAppendBlock());
            }
            case MEMBER_EXPRESSION: {
                MemberExpressionTree memberExpression = access.asMemberExpression();
                final AstEnv baseEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
                TranslationResult processedBase = process(memberExpression.operand, baseEnv);
                SyntacticReference reference = new StaticProperty(processedBase.getResultReference(), baseEnv.getResultRegister(), memberExpression.memberName.value, makeSourceLocation(access));
                return TranslationResult.makeResultReference(reference, processedBase.getAppendBlock());
            }
            case MEMBER_LOOKUP_EXPRESSION:
                MemberLookupExpressionTree memberLookupExpression = access.asMemberLookupExpression();
                final AstEnv baseEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
                TranslationResult processedBase = process(memberLookupExpression.operand, baseEnv);
                final AstEnv memberEnv = env.makeResultRegister(nextRegister(env)).makeAppendBlock(processedBase.getAppendBlock()).makeStatementLevel(false);
                TranslationResult processedMember = process(memberLookupExpression.memberExpression, memberEnv);
                SyntacticReference baseReference = processedBase.getResultReference();
                SyntacticReference reference = new DynamicProperty(baseReference, baseEnv.getResultRegister(), processedMember.getResultReference(), memberEnv.getResultRegister(), makeSourceLocation(access));
                return TranslationResult.makeResultReference(reference, processedMember.getAppendBlock());
            default:
                throw new RuntimeException("Unhandled reference type: " + access.type);
        }
    }

    /**
     * Produces a WriteVariableNode or a WritePropertyNode.
     */
    private BasicBlock write(SyntacticReference target, int valueRegister, AstEnv env) {
        return writeWithCustomLocation(target, valueRegister, env, target.location);
    }

    /**
     * Produces a WriteVariableNode or a WritePropertyNode, using the given source location.
     */
    private BasicBlock writeWithCustomLocation(SyntacticReference target, int valueRegister, AstEnv env, SourceLocation location) {
        final AbstractNode write;
        switch (target.type) {
            case Variable:
                write = new WriteVariableNode(valueRegister, target.asVariable().name, location);
                break;
            case StaticProperty:
                StaticProperty staticProperty = target.asStaticProperty();
                write = new WritePropertyNode(staticProperty.baseRegister, staticProperty.propertyName, valueRegister, Kind.ORDINARY, false, location);
                break;
            case DynamicProperty:
                DynamicProperty dynamicProperty = target.asDynamicProperty();
                write = new WritePropertyNode(dynamicProperty.baseRegister, dynamicProperty.propertyRegister, valueRegister, false, location);
                break;
            default:
                throw new RuntimeException("Unhandled write type: " + target.type);
        }
        return access(write, target, env);
    }

    /**
     * Produces a ReadVariableNode or a ReadPropertyNode.
     */
    private BasicBlock read(SyntacticReference target, AstEnv env) {
        final AbstractNode read;
        switch (target.type) {
            case Variable:
                read = new ReadVariableNode(target.asVariable().name, env.getResultRegister(), env.getBaseRegister(), target.location);
                break;
            case StaticProperty:
                StaticProperty staticProperty = target.asStaticProperty();
                read = new ReadPropertyNode(staticProperty.baseRegister, staticProperty.propertyName, env.getResultRegister(), target.location);
                break;
            case DynamicProperty:
                DynamicProperty dynamicProperty = target.asDynamicProperty();
                read = new ReadPropertyNode(dynamicProperty.baseRegister, dynamicProperty.propertyRegister, env.getResultRegister(), target.location);
                break;
            default:
                throw new RuntimeException("Unhandled read type: " + target.type);
        }
        return access(read, target, env.makeAppendBlock(env.getAppendBlock()));
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
        SyntacticReference resultReference;

        // TAJS_-functions can only be accessed as simple function call expressions.
        final TAJSFunctionName tajsFunctionName;
        if (isAccess(operand) && operand.type == ParseTreeType.IDENTIFIER_EXPRESSION
                && operand.asIdentifierExpression().identifierToken.value.startsWith("TAJS_")) {
            String name = operand.asIdentifierExpression().identifierToken.value;
            Optional<TAJSFunctionName> tajsFunctionNameOptional = Arrays.stream(TAJSFunctionName.values()).filter(o -> o.toString().equals(name)).findAny();
            if (tajsFunctionNameOptional.isPresent()) {
                tajsFunctionName = tajsFunctionNameOptional.get();
            } else {
                throw new AnalysisException("Bad use of TAJS_. No such function: " + name);
            }
        } else {
            tajsFunctionName = null;
        }
        if (tajsFunctionName != null) {
            processed = TranslationResult.makeAppendBlock(funEnv.getAppendBlock());
            propertyAccessCall = false;
            resultReference = null;
        } else if (isAccess(operand)) {
            processed = processAccessPartly(operand, funEnv);
            resultReference = processed.getResultReference();
            AstEnv referenceEnv = funEnv.makeAppendBlock(processed.getAppendBlock());
            switch (resultReference.type) {
                case Variable:
                    propertyAccessCall = false;
                    processed = TranslationResult.makeAppendBlock(read(resultReference, referenceEnv));
                    break;
                case StaticProperty:
                case DynamicProperty:
                    propertyAccessCall = true;
                    if (resultReference.type == SyntacticReference.Type.StaticProperty) {
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
            resultReference = null;
        }

        List<Integer> argumentRegisters = newList();
        if (arguments != null) {
            for (ParseTree argument : arguments.arguments) {
                int argumentRegister = nextRegister(env);
                argumentRegisters.add(argumentRegister);
                processed = process(argument, env.makeResultRegister(argumentRegister).makeStatementLevel(false).makeAppendBlock(processed.getAppendBlock()));
            }
        }

        final CallNode callNode;
        if (tajsFunctionName != null) {
            callNode = new CallNode(env.getResultRegister(), tajsFunctionName, argumentRegisters, location);
        } else if (propertyAccessCall) {
            callNode = new CallNode(constructor, env.getResultRegister(), propertyObjectRegister, propertyNameRegister, propertyNameString, argumentRegisters, location);
        } else {
            callNode = new CallNode(constructor, env.getResultRegister(), baseRegister, funEnv.getResultRegister(), argumentRegisters, location);
        }
        syntacticInformationCollector.registerFunctionCall(callNode, operand, arguments, resultReference);

        BasicBlock callBlock = makeSuccessorBasicBlock(processed.getAppendBlock(), functionAndBlocksManager);
        addNodeToBlock(callNode, callBlock, env);

        BasicBlock postCallBlock = makeSuccessorBasicBlock(callBlock, functionAndBlocksManager);
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

        BasicBlock unreachableBlock = makeBasicBlock(appendBlock.getExceptionHandler(), functionAndBlocksManager);
        functionAndBlocksManager.registerUnreachableSyntacticSuccessor(appendBlock, unreachableBlock);
        return TranslationResult.makeAppendBlock(unreachableBlock);
    }

    /**
     * Processes the non-trivial control flow of short-circuit '&amp;&amp;' and '||'.
     *
     * @param tree a tree of either '&amp;&amp;' or '||'
     */
    private TranslationResult processShortCircuitOperators(BinaryOperatorTree tree, AstEnv env) {
        // prepare block structure
        IfNode enclosingIfNode = env.getEnclosingIfNode(tree);
        BasicBlock lastBlockForLHS = makeBasicBlock(env.getAppendBlock().getExceptionHandler(), functionAndBlocksManager);
        BasicBlock firstBlockForRHS = makeBasicBlock(env.getAppendBlock().getExceptionHandler(), functionAndBlocksManager);
        BasicBlock abortBlock;
        if (enclosingIfNode == null) {
            abortBlock = makeSuccessorBasicBlock(lastBlockForLHS, functionAndBlocksManager);
        } else {
            abortBlock = tree.operator.type == TokenType.AND ? enclosingIfNode.getSuccFalse() : enclosingIfNode.getSuccTrue();
            lastBlockForLHS.addSuccessor(abortBlock);
        }
        lastBlockForLHS.addSuccessor(firstBlockForRHS);
        lastBlockForLHS.addSuccessor(abortBlock);

        // prepare if-node
        int register = getUsableResultRegister(env);
        IfNode ifNode = new IfNode(register, makeSourceLocation(tree));
        if (tree.operator.type == TokenType.AND) {
            ifNode.setSuccessors(firstBlockForRHS, abortBlock);
        } else {
            ifNode.setSuccessors(abortBlock, firstBlockForRHS);
        }

        // lhs
        AstEnv expressionEnv = env.makeResultRegister(register).makeStatementLevel(false).makeEnclosingIfNode(tree.left, ifNode);
        BasicBlock leftAppendBlock = process(tree.left, expressionEnv).getAppendBlock();
        leftAppendBlock.addSuccessor(lastBlockForLHS);

        syntacticInformationCollector.registerIfNodeCondition(ifNode, tree.left);
        addNodeToBlock(ifNode, lastBlockForLHS, expressionEnv);

        // rhs
        TranslationResult rightAppendBlock = process(tree.right, env.makeAppendBlock(firstBlockForRHS).makeStatementLevel(false));
        if (enclosingIfNode != null) {
            syntacticInformationCollector.registerIfNodeCondition(enclosingIfNode, tree.right);
        }
        BasicBlock appendBlock;
        if (enclosingIfNode == null) {
            appendBlock = makeJoinBasicBlock(abortBlock, rightAppendBlock.getAppendBlock(), functionAndBlocksManager);
        } else {
            appendBlock = rightAppendBlock.getAppendBlock();
        }

        return TranslationResult.makeAppendBlock(appendBlock);
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
     * Builds a loop of the form [init] [condFirst] [body] [inc] [condLast].
     */
    private TranslationResult processLoop(ParseTree tree, ParseTree initializer, ParseTree condition, ParseTree increment, ParseTree body, boolean defaultConditionIsTrue, SourceLocation conditionLocation /* condition might be null */, boolean isNested, AstEnv env, boolean conditionIsLast) {

        // create loop structural nodes
        final BasicBlock exceptionHandler = env.getAppendBlock().getExceptionHandler();
        final BasicBlock firstInitializerBlock = makeBasicBlock(exceptionHandler, functionAndBlocksManager);
        final BasicBlock lastInitializerBlock = makeBasicBlock(exceptionHandler, functionAndBlocksManager);
        final BasicBlock firstConditionBlock = makeBasicBlock(exceptionHandler, functionAndBlocksManager);
        final BasicBlock lastConditionBlock = makeBasicBlock(exceptionHandler, functionAndBlocksManager);
        final BasicBlock firstIncrementBlock = makeBasicBlock(exceptionHandler, functionAndBlocksManager);
        final BasicBlock lastIncrementBlock = makeBasicBlock(exceptionHandler, functionAndBlocksManager);
        final BasicBlock firstBodyBlock = makeBasicBlock(exceptionHandler, functionAndBlocksManager);
        final BasicBlock lastBodyBlock = makeBasicBlock(exceptionHandler, functionAndBlocksManager);
        final BasicBlock firstPostLoopBlock = makeBasicBlock(exceptionHandler, functionAndBlocksManager);

        // link loop structural nodes
        env.getAppendBlock().addSuccessor(firstInitializerBlock);
        lastConditionBlock.addSuccessor(firstBodyBlock);
        lastBodyBlock.addSuccessor(firstIncrementBlock);
        if (conditionIsLast) {
            lastInitializerBlock.addSuccessor(firstBodyBlock);
            lastIncrementBlock.addSuccessor(firstConditionBlock);
        } else {
            lastInitializerBlock.addSuccessor(firstConditionBlock);
            lastIncrementBlock.addSuccessor(firstConditionBlock);
        }
        lastConditionBlock.addSuccessor(firstBodyBlock);
        lastConditionBlock.addSuccessor(firstPostLoopBlock);

        // process initializer
        if (initializer != null) {
            AstEnv initializerEnv = env.makeResultRegister(AbstractNode.NO_VALUE).makeAppendBlock(firstInitializerBlock).makeStatementLevel(true);
            process(initializer, initializerEnv).getAppendBlock().addSuccessor(lastInitializerBlock);
        } else {
            firstInitializerBlock.addSuccessor(lastInitializerBlock);
        }

        // prepare loop unrolling environment
        int conditionalRegister = nextRegister(env);
        IfNode ifNode = new IfNode(conditionalRegister, conditionLocation);
        final BeginLoopNode beginLoopNode = new BeginLoopNode(ifNode, isNested, makeSourceLocation(body));
        SourceLocation loopEndLocation = conditionLocation;
        endNodeScopeExceptionally(new EndLoopNode(beginLoopNode, loopEndLocation), env.makeStatementLevel(true).makeAppendBlock(firstConditionBlock));
        BasicBlock bodyExceptionHandler = firstConditionBlock.getExceptionHandler();
        Stream.of(firstConditionBlock, lastConditionBlock, firstBodyBlock, lastBodyBlock, firstIncrementBlock, lastIncrementBlock)
                .forEach(b -> b.setExceptionHandler(bodyExceptionHandler));

        // process condition
        BasicBlock conditionAppendBlock;
        if (defaultConditionIsTrue && condition == null) {
            assert !conditionIsLast;
            AbstractNode trueNode = ConstantNode.makeBoolean(true, conditionalRegister, conditionLocation);
            trueNode.setArtificial();
            addNodeToBlock(trueNode, firstConditionBlock, env.makeStatementLevel(false));
            conditionAppendBlock = firstConditionBlock;
        } else {
            AstEnv conditionEnv = env.makeAppendBlock(firstConditionBlock).makeResultRegister(conditionalRegister).makeStatementLevel(false);
            conditionAppendBlock = process(condition, conditionEnv).getAppendBlock();
        }
        syntacticInformationCollector.registerIfNodeCondition(ifNode, condition);
        addNodeToBlock(beginLoopNode, conditionAppendBlock, env.makeStatementLevel(false));
        conditionAppendBlock = makeSuccessorBasicBlock(conditionAppendBlock, functionAndBlocksManager);
        conditionAppendBlock.addSuccessor(lastConditionBlock);
        addNodeToBlock(ifNode, lastConditionBlock, env.makeStatementLevel(true));
        ifNode.setSuccessors(firstBodyBlock, firstPostLoopBlock);

        // process body
        BasicBlock jumpThroughBlock = makeJumpThroughBlock(bodyExceptionHandler);
        addNodeToBlock(new EndLoopNode(beginLoopNode, loopEndLocation), jumpThroughBlock, env.makeStatementLevel(true));

        AstEnv bodyEnv = env
                .makeAppendBlock(firstBodyBlock)
                .makeJumpThroughBlock(new JumpThroughBlocks(jumpThroughBlock, functionAndBlocksManager))
                .makeUnlabelledContinueAndBreak(firstIncrementBlock, firstPostLoopBlock)
                .makeStatementLevel(true);
        if (env.hasLoopLabel(tree)) {
            bodyEnv = bodyEnv.makeLabelledContinue(env.getLoopLabelName(tree), firstIncrementBlock);
        }
        process(body, bodyEnv).getAppendBlock().addSuccessor(lastBodyBlock);
        if (!firstBodyBlock.getNodes().isEmpty()) {
            firstBodyBlock.getFirstNode().setIsLoopEntryNode(true);
        }

        // process increment
        if (increment != null) {
            AstEnv incrementEnv = bodyEnv.makeAppendBlock(firstIncrementBlock).makeStatementLevel(true).makeResultRegister(AbstractNode.NO_VALUE);
            process(increment, incrementEnv).getAppendBlock().addSuccessor(lastIncrementBlock);
        } else {
            firstIncrementBlock.addSuccessor(lastIncrementBlock);
        }

        // end loop unrolling environment
        EndLoopNode ordinaryEndLoopNode = new EndLoopNode(beginLoopNode, loopEndLocation);
        BasicBlock postLoopAppendBlock = endNodeScopeOrdinarily(ordinaryEndLoopNode, bodyExceptionHandler, env.makeAppendBlock(firstPostLoopBlock));
        postLoopAppendBlock = makeSuccessorBasicBlock(postLoopAppendBlock, functionAndBlocksManager);

        return TranslationResult.makeAppendBlock(postLoopAppendBlock);
    }

    /**
     * Processes a function declaration.
     */
    Function processFunctionDeclaration(FunctionDeclarationTree.Kind kind, String name, FormalParameterListTree parameters, ParseTree body, AstEnv env, SourceLocation location, String source) {
        // 1. prepare function object
        List<String> parameterNames = newList();
        for (ParseTree parameter : parameters.parameters) {
            if (parameter.type != ParseTreeType.IDENTIFIER_EXPRESSION) {
                unsupportedLanguageFeature(parameter, "Non-identifier parameter name");
            }
            parameterNames.add(parameter.asIdentifierExpression().identifierToken.value);
        }

        boolean isStrict = !Options.get().isNoStrictEnabled() && (env.getFunction().isStrict() || FunctionBuilderHelper.startsWithUseStrictDirective(body));
        Function function = new Function(name, parameterNames, env.getFunction(), isStrict, location, source);

        AstEnv freshRegistersAndScopeEnv = env.makeRegisterManager(new RegisterManager(AbstractNode.FIRST_ORDINARY_REG));
        boolean expressionContext = kind == FunctionDeclarationTree.Kind.EXPRESSION || (env.getUnevalExpressionResult() != null && env.getUnevalExpressionResult().resultRegister == env.getResultRegister());
        int withScopeRegister = expressionContext ? nextRegister(freshRegistersAndScopeEnv) : AbstractNode.NO_VALUE;
        freshRegistersAndScopeEnv = freshRegistersAndScopeEnv.makeBaseRegister(withScopeRegister);

        AstEnv functionEnv = setupFunction(function, freshRegistersAndScopeEnv, functionAndBlocksManager);

        // 2. register function
        if (expressionContext) {
            DeclareFunctionNode node = new DeclareFunctionNode(function, true, env.getResultRegister(), location);
            function.setNode(node);
            addNodeToBlock(node, env.getAppendBlock(), env.makeStatementLevel(false));
        } else {
            DeclareFunctionNode declarationNode = new DeclareFunctionNode(function, false, AbstractNode.NO_VALUE, location);
            function.setNode(declarationNode);
            addNodeToBlock(declarationNode, env.getDeclarationBlock(), env);
            if (USE_REPRESENTATION_NODES) {
                addNodeToBlock(new NopNode("function " + name, declarationNode.getSourceLocation()), env.getAppendBlock(), env.makeStatementLevel(false));
            }
        }

        // 3. wire the function body
        TranslationResult processedBody = process(body, functionEnv);
        processedBody.getAppendBlock().addSuccessor(function.getOrdinaryExit());

        function.setMaxRegister(functionEnv.getRegisterManager().getRegister()); // assumes monotonically increasing register implementation

        return function;
    }

    @Override
    public TranslationResult process(ArrayLiteralExpressionTree tree, AstEnv env) {
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
        BasicBlock callBlock = makeSuccessorBasicBlock(processed.getAppendBlock(), functionAndBlocksManager);
        AbstractNode callNode = new CallNode(LiteralConstructorKinds.ARRAY, env.getResultRegister(), AbstractNode.NO_VALUE, variableEnv.getResultRegister(), elementRegisters, location);
        syntacticInformationCollector.registerLiteral(callNode, new LiteralTree(tree), astInfo);
        addNodeToBlock(callNode, callBlock, env);
        BasicBlock postCallBlock = makeSuccessorBasicBlock(callBlock, functionAndBlocksManager);
        return TranslationResult.makeAppendBlock(postCallBlock);
    }

    @Override
    public TranslationResult process(ProgramTree tree, AstEnv env) {
        return processList(tree.sourceElements, env.makeStatementLevel(true));
    }

    @Override
    public TranslationResult process(BlockTree tree, AstEnv env) {
        AstEnv blockEnv;
        ImmutableList<ParseTree> statements = tree.statements;
        blockEnv = env;
        BasicBlock appendBlock = processList(statements, blockEnv.makeStatementLevel(true)).getAppendBlock();
        return TranslationResult.makeAppendBlock(appendBlock);
    }

    @Override
    public TranslationResult process(BreakStatementTree tree, AstEnv env) {
        return processJump(tree.type, tree.getLabel(), makeSourceLocation(tree), env.makeStatementLevel(true));
    }

    @Override
    public TranslationResult process(CatchTree tree, AstEnv env) {
        // 1. extract information
        SourceLocation location = makeSourceLocation(tree);

        final int catchRegister;
        if (tree.exception != null) {
            catchRegister = nextRegister(env);
            addNodeToBlock(new CatchNode(tree.exception.asIdentifierExpression().identifierToken.value, catchRegister, location), env.getAppendBlock(), env.makeStatementLevel(false));
        } else {
            catchRegister = env.getResultRegister();
        }

        // 2. create blocks
        BasicBlock appendBlock = makeSuccessorBasicBlock(env.getAppendBlock(), functionAndBlocksManager);

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
        BasicBlock jumpThroughBlock = makeJumpThroughBlock(appendBlock.getExceptionHandler());
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
    public TranslationResult process(ConditionalExpressionTree tree, AstEnv env) {
        return makeConditional(tree, tree.condition, tree.left, tree.right, makeSourceLocation(tree), env.makeStatementLevel(false));
    }

    @Override
    public TranslationResult process(ContinueStatementTree tree, AstEnv env) {
        return processJump(tree.type, tree.getLabel(), makeSourceLocation(tree), env.makeStatementLevel(true));
    }

    @Override
    public TranslationResult process(DoWhileStatementTree tree, AstEnv env) {
        LoopTree loopTree = new LoopTree(tree);
        syntacticInformationCollector.registerLoop(loopTree, env, astInfo);
        return processLoop(tree, null, tree.condition, null, tree.body, false, makeSourceLocation(tree.condition), astInfo.getNestedLoops().contains(loopTree), env, true);
    }

    @Override
    public TranslationResult process(MemberLookupExpressionTree tree, AstEnv env) {
        TranslationResult processed = processAccessPartly(tree, env);
        syntacticInformationCollector.registerSimpleRead(tree, processed.getResultReference());

        BasicBlock appendBlock = read(processed.getResultReference(), env.makeAppendBlock(processed.getAppendBlock()));
        processed = TranslationResult.makeResultReference(processed.getResultReference(), appendBlock);
        return processed;
    }

    @Override
    public TranslationResult process(EmptyStatementTree tree, AstEnv env) {
        return TranslationResult.makeAppendBlock(env.getAppendBlock());
    }

    @Override
    public TranslationResult process(ExpressionStatementTree tree, AstEnv env) {
        return process(tree.expression, env.makeStatementLevel(true).makeResultRegister(AbstractNode.NO_VALUE));
    }

    @Override
    public TranslationResult process(ForInStatementTree tree, AstEnv env) {
        // 1. prepare some values
        final SourceLocation loopLocation = makeSourceLocation(tree);

        int objectRegister = nextRegister(env);
        int propertyListRegister = nextRegister(env);
        int conditionRegister = nextRegister(env);
        int propertyRegister = nextRegister(env);

        BasicBlock appendBlock = env.getAppendBlock();

        // 2. process the loop-header (this is *only* the rhs of the 'lhs in rhs'!)
        AstEnv collectionEnv = env.makeAppendBlock(appendBlock).makeResultRegister(objectRegister).makeStatementLevel(false);
        TranslationResult processedCollection = process(tree.collection, collectionEnv);
        appendBlock = processedCollection.getAppendBlock();
        appendBlock = makeSuccessorBasicBlock(appendBlock, functionAndBlocksManager);

        // 3. start the for-in
        BeginForInNode beginForIn = new BeginForInNode(objectRegister, propertyListRegister, loopLocation);
        addNodeToBlock(beginForIn, appendBlock, env.makeStatementLevel(false));
        BasicBlock beginForInBlock = appendBlock;

        // 4. create conditional (has-next-property) for the loop
        BasicBlock conditionBlock = makeSuccessorBasicBlock(appendBlock, functionAndBlocksManager);

        EndForInNode exceptionalEndForIn = new EndForInNode(beginForIn, loopLocation);
        endNodeScopeExceptionally(exceptionalEndForIn, env.makeAppendBlock(conditionBlock).makeStatementLevel(true));

        addNodeToBlock(new HasNextPropertyNode(propertyListRegister, conditionRegister, loopLocation), conditionBlock, env.makeStatementLevel(false));

        BasicBlock trueBranch = makeSuccessorBasicBlock(conditionBlock, functionAndBlocksManager);
        BasicBlock falseBranch = makeSuccessorBasicBlock(conditionBlock, functionAndBlocksManager);

        // 5. make the end of the loop to account for syntactic BasicBlock.entryBlock computation
        // (another End is inserted at the end of the loop-body, and the nodetransfer implementation only needs that End node in practice)
        falseBranch = endNodeScopeOrdinarily(new EndForInNode(beginForIn, loopLocation), falseBranch.getExceptionHandler(), env.makeStatementLevel(true).makeAppendBlock(falseBranch));

        IfNode ifNode = new IfNode(conditionRegister, loopLocation);
        addNodeToBlock(ifNode, conditionBlock, env.makeStatementLevel(false));
        ifNode.setSuccessors(trueBranch, falseBranch);
        ifNode.setArtificial();

        falseBranch = makeSuccessorBasicBlock(falseBranch, functionAndBlocksManager);

        // 6. create iterator (next-property), and procsses the lhs of the 'lhs in rhs'
        final AstEnv bodyEnv = env.makeAppendBlock(trueBranch);

        appendBlock = bodyEnv.getAppendBlock();

        AstEnv loopVariableEnv = bodyEnv.makeAppendBlock(appendBlock).makeResultRegister(AbstractNode.NO_VALUE).makeStatementLevel(false);
        TranslationResult processedLoopVariable = processAccessPartly(desugarForInLoopVariable(tree.initializer, loopVariableEnv), loopVariableEnv);
        appendBlock = processedLoopVariable.getAppendBlock();

        SourceLocation lhsLocation;
        if (tree.initializer.type == ParseTreeType.VARIABLE_DECLARATION_LIST) {
            lhsLocation = makeSourceLocation(tree.initializer.asVariableDeclarationList().declarations.get(0).lvalue);
        } else {
            lhsLocation = makeSourceLocation(tree.initializer);
        }
        NextPropertyNode nextPropertyNode = new NextPropertyNode(propertyListRegister, propertyRegister, lhsLocation);

        addNodeToBlock(nextPropertyNode, appendBlock, bodyEnv.makeStatementLevel(false));

        syntacticInformationCollector.registerForIn(tree, lhsLocation, sourceLocationMaker);

        writeWithCustomLocation(processedLoopVariable.getResultReference(), propertyRegister, bodyEnv.makeAppendBlock(appendBlock).makeStatementLevel(true), lhsLocation);

        // 7. create body
        BasicBlock jumpThroughBlock = makeJumpThroughBlock(env.getAppendBlock().getExceptionHandler());
        addNodeToBlock(new EndForInNode(beginForIn, loopLocation), jumpThroughBlock, bodyEnv.makeStatementLevel(true));
        AstEnv syntacticBodyEnv = bodyEnv.makeUnlabelledContinueAndBreak(beginForInBlock, falseBranch).makeJumpThroughBlock(new JumpThroughBlocks(jumpThroughBlock, functionAndBlocksManager)).makeAppendBlock(appendBlock).makeStatementLevel(true);
        if (env.hasLoopLabel(tree)) {
            syntacticBodyEnv = syntacticBodyEnv.makeLabelledContinue(env.getLoopLabelName(tree), beginForInBlock);
        }
        TranslationResult processedBody = process(tree.body, syntacticBodyEnv);
        BasicBlock endOfBody = processedBody.getAppendBlock();

        endOfBody = endNodeScopeOrdinarily(new EndForInNode(beginForIn, loopLocation), endOfBody.getExceptionHandler(), env.makeStatementLevel(true).makeAppendBlock(endOfBody));

        endOfBody.addSuccessor(beginForInBlock);

        // falseBranch.setExceptionHandler(env.getAppendBlock().getExceptionHandler());

        return TranslationResult.makeAppendBlock(falseBranch);
    }

    @Override
    public TranslationResult process(ForStatementTree tree, AstEnv env) {
        LoopTree loopTree = new LoopTree(tree);
        syntacticInformationCollector.registerLoop(loopTree, env, astInfo);
        ParseTree condition = tree.condition.type == ParseTreeType.NULL ? null : tree.condition;
        return processLoop(tree, tree.initializer, condition, tree.increment, tree.body, true, makeSourceLocation(tree.condition), astInfo.getNestedLoops().contains(loopTree), env, false);
    }

    @Override
    public TranslationResult process(CallExpressionTree tree, AstEnv env) {
        return processInvocation(tree.operand, tree.arguments, false, makeSourceLocation(tree), env);
    }

    @Override
    public TranslationResult process(FunctionDeclarationTree tree, AstEnv env) {
        if (tree.kind == FunctionDeclarationTree.Kind.ARROW) {
            unsupportedLanguageFeature(tree, "ES6 arrow functions");
        }
        String name = tree.name == null ? null : tree.name.value;
        Function function = processFunctionDeclaration(tree.kind, name, tree.formalParameterList, tree.functionBody, env, makeSourceLocation(tree), getSource(tree));
        syntacticInformationCollector.registerFunction(function, tree, astInfo);
        return TranslationResult.makeAppendBlock(env.getAppendBlock());
    }

    @Override
    public TranslationResult process(IfStatementTree tree, AstEnv env) {
        return makeConditional(tree, tree.condition, tree.ifClause, tree.elseClause, makeSourceLocation(tree), env.makeStatementLevel(true));
    }

    @Override
    public TranslationResult process(BinaryOperatorTree tree, AstEnv env) {
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
                if (Options.get().isUnevalizerEnabled() && processedLhs.getResultReference().type == SyntacticReference.Type.Variable) {
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
                    boolean lhsIsVariable = processedLhs.getResultReference().type ==
                            SyntacticReference.Type.Variable;
                    SourceLocation location = processedLhs.getResultReference().location;

                    if (lhsIsVariable) {
                        syntacticInformationCollector.registerWriteVariable(tree, location, sourceLocationMaker);
                    }

                    BasicBlock writeAppendBlock = writeWithCustomLocation(processedLhs.getResultReference(), rhsEnv.getResultRegister(), env.makeAppendBlock(processed.getAppendBlock()), location);
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
    public TranslationResult process(LabelledStatementTree tree, AstEnv env) {
        ParseTree statement = tree.statement;

        if (statement.type == ParseTreeType.LABELLED_STATEMENT) {
            // technically valid, but it is messy for us to dig down in the AST to find the real target
            throw new SyntacticSupportNotImplemented(makeSourceLocation(tree) + ": No support for labelled label-statements.");
        }

        String name = tree.name.value;
        BasicBlock breakBlock = makeBasicBlock(env.getAppendBlock().getExceptionHandler(), functionAndBlocksManager);
        // note: it is a syntax error to try to continue to a non-iteration-statement label.
        AstEnv labelledEnv = env.makeLabelledBreak(name, breakBlock).makeStatementLevel(true);

        boolean isLoopStatement = FunctionBuilderHelper.isLoopStatement(tree.statement);
        if (isLoopStatement) {
            labelledEnv = labelledEnv.makeLoopLabelName(statement, name);
        }
        TranslationResult processed = process(statement, labelledEnv);

        processed.getAppendBlock().addSuccessor(breakBlock);
        return TranslationResult.makeAppendBlock(breakBlock);
    }

    @Override
    public TranslationResult process(IdentifierExpressionTree tree, AstEnv env) {
        TranslationResult processed = processAccessPartly(tree, env);
        syntacticInformationCollector.registerSimpleRead(tree, processed.getResultReference());

        BasicBlock append = read(processed.getResultReference(), env);
        processed = TranslationResult.makeResultReference(processed.getResultReference(), append);
        return processed;
    }

    @Override
    public TranslationResult process(NewExpressionTree tree, AstEnv env) {
        return processInvocation(tree.operand, tree.arguments, true, makeSourceLocation(tree), env);
    }

    @Override
    public TranslationResult process(ObjectLiteralExpressionTree tree, AstEnv env) {
        final int thisRegister = getUsableResultRegister(env);
        NewObjectNode node = new NewObjectNode(thisRegister, makeSourceLocation(tree));
        addNodeToBlock(node, env.getAppendBlock(), env);
        syntacticInformationCollector.registerLiteral(node, new LiteralTree(tree), astInfo);
        final AstEnv propertyEnv = env.makeThisRegister(thisRegister).makeStatementLevel(false);
        return processList(tree.propertyNameAndValues, propertyEnv);
    }

    @Override
    public TranslationResult process(ParenExpressionTree tree, AstEnv env) {
        return process(tree.expression, env.makeStatementLevel(false));
    }

    @Override
    public TranslationResult process(MemberExpressionTree tree, AstEnv env) {
        TranslationResult processed = processAccessPartly(tree, env);
        syntacticInformationCollector.registerSimpleRead(tree, processed.getResultReference());

        BasicBlock appendBlock = read(processed.getResultReference(), env.makeAppendBlock(processed.getAppendBlock()));
        processed = TranslationResult.makeResultReference(processed.getResultReference(), appendBlock);
        return processed;
    }

    @Override
    public TranslationResult process(ReturnStatementTree tree, AstEnv env) {
        if (env.getFunction().getOuterFunction() == null) { // parser doesn't warn about this
            throw new ParseError(makeSourceLocation(tree) + ": Syntax Error: Top level returns are not allowed.");
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
    public TranslationResult process(CaseClauseTree tree, AstEnv env) {
        throw new AnalysisException("Top level case detected, weird.");
    }

    @Override
    public TranslationResult process(SwitchStatementTree tree, AstEnv env) {
        SourceLocation location = makeSourceLocation(tree);
        // 1. resolve the operand
        int operandRegister = nextRegister(env);
        TranslationResult processedSequence = process(tree.expression, env.makeResultRegister(operandRegister).makeStatementLevel(false));

        // 2. create "return" block
        BasicBlock returnBlock = makeBasicBlock(processedSequence.getAppendBlock().getExceptionHandler(), functionAndBlocksManager);

        // 3. process cases (save default case for last)
        DefaultClauseTree defaultCase = null;
        BasicBlock trueBranch = null;
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
            BasicBlock oldTrueBranch = trueBranch;

            BasicBlock equalityCheckAppendBlock = processedSequence.getAppendBlock();
            trueBranch = makeSuccessorBasicBlock(equalityCheckAppendBlock, functionAndBlocksManager);
            falseBranch = makeSuccessorBasicBlock(equalityCheckAppendBlock, functionAndBlocksManager);

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
                throw new SyntacticSupportNotImplemented(makeSourceLocation(tree) + ": No support for default-case in non-last position");
            }
            // 5. handle default case
            // 5.1 : propagate from either case
            BasicBlock defaultBlock = makeSuccessorBasicBlock(processedSequence.getAppendBlock(), functionAndBlocksManager);
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
    public TranslationResult process(ThrowStatementTree tree, AstEnv env) {
        AstEnv throwableEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
        TranslationResult processedThrowable = process(tree.value, throwableEnv);
        addNodeToBlock(new ThrowNode(throwableEnv.getResultRegister(), makeSourceLocation(tree)), processedThrowable.getAppendBlock(), env.makeStatementLevel(true));
        return processedThrowable;
    }

    @Override
    public TranslationResult process(TryStatementTree tree, AstEnv env) {
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
        final BasicBlock tryStartBlock = makeSuccessorBasicBlock(originalAppendBlock, functionAndBlocksManager);
        final BasicBlock catchStartBlock = catchTree != null ? makeCatchBasicBlock(tryStartBlock, functionAndBlocksManager) : null;
        final BasicBlock exceptionalStartFinallyBlock = finallyTree != null ? makeBasicBlock(originalExceptionBlock, functionAndBlocksManager) : null;
        final BasicBlock ordinaryFinallyStartBlock = makeBasicBlock(originalExceptionBlock, functionAndBlocksManager);

        if (exceptionalStartFinallyBlock != null) {
            if (catchStartBlock != null) {
                catchStartBlock.setExceptionHandler(exceptionalStartFinallyBlock);
            } else {
                tryStartBlock.setExceptionHandler(exceptionalStartFinallyBlock);
            }
        }

        // 3. translate finally first - it is needed for jumpThroughBlocks during the try and catch
        final TranslationResult ordinarilyProcessedFinally;
        final JumpThroughBlocks finallyJumpThroughBlocks;
        if (exceptionalStartFinallyBlock != null) {
            // 1. create the exceptional finally blocks
            int tryRegister = nextRegister(env);

            // (note: for the empty finally block, a non-required [(catch v42), (throw v42)] will be created)
            CatchNode finallyCatchNode = new CatchNode(tryRegister, sourceLocation);
            finallyCatchNode.setArtificial();
            addNodeToBlock(finallyCatchNode, exceptionalStartFinallyBlock, env.makeStatementLevel(false));

            SessionKey exceptionalSessionKey = functionAndBlocksManager.startSession();
            final TranslationResult exceptionalProcessedFinally = process(finallyTree, env.makeAppendBlock(exceptionalStartFinallyBlock).makeStatementLevel(true));
            functionAndBlocksManager.endSession(exceptionalSessionKey);

            final List<BasicBlock> exceptionalFinallyBlocks = newList();
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
            SessionKey ordinarySessionKey = functionAndBlocksManager.startSession();
            ordinarilyProcessedFinally = process(finallyTree, env.makeAppendBlock(ordinaryFinallyStartBlock).makeStatementLevel(true));
            functionAndBlocksManager.endSession(ordinarySessionKey);

            final List<BasicBlock> ordinaryFinallyBlocks = newList();
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
    public TranslationResult process(UnaryExpressionTree tree, AstEnv env) {
        ParseTree operand = stripParens(tree.operand);
        if (operand.type == ParseTreeType.LITERAL_EXPRESSION && operand.asLiteralExpression().literalToken.type == TokenType.NUMBER && tree.operator.type == TokenType.MINUS) {
            // special case: primitive constant-folding to match the old flowgraph builder
            double number = ClosureASTUtil.normalizeNumber(operand.asLiteralExpression().literalToken.asLiteral());
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
                    syntacticInformationCollector.registerSimpleRead(operand.asIdentifierExpression(), new Variable(operand.asIdentifierExpression().identifierToken.value, null));
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
                if (!isAccess(tree.operand)) {
                    // case: `delete true; delete 42;`
                    // If we need to support it: NodeTransfer should return `true`
                    throw new AnalysisModelLimitationException(makeSourceLocation(tree) + ": delete of non-references are not supported.");
                }
                processedSub = processAccessPartly(tree.operand, subEnv);
                SyntacticReference reference = processedSub.getResultReference();
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

    private static boolean isReferenceASTType(ParseTreeType type) {
        switch (type) {
            case THIS_EXPRESSION:
            case IDENTIFIER_EXPRESSION:
            case MEMBER_EXPRESSION:
            case MEMBER_LOOKUP_EXPRESSION:
                return true;
            default:
                return false;
        }
    }

    @Override
    public TranslationResult process(VariableStatementTree tree, AstEnv env) {
        return process(tree.declarations, env.makeStatementLevel(true));
    }

    @Override
    public TranslationResult process(VariableDeclarationListTree tree, AstEnv env) {
        if (tree.declarationType != TokenType.VAR && tree.declarationType != TokenType.CONST /* TODO: unsound to treat as var, but unlikely to be an issue in practice (GitHub #182) */) {
            throw new SyntacticSupportNotImplemented(makeSourceLocation(tree) + ": Only var/const declarations supported, " + tree.declarationType + " is not supported");
        }
        return processList(tree.declarations, env.makeStatementLevel(true));
    }

    @Override
    public TranslationResult process(VariableDeclarationTree declaration, AstEnv env) {
        final String variableName;
        SourceLocation variableLocation = makeSourceLocation(declaration.lvalue);
        if (declaration.lvalue.type == ParseTreeType.IDENTIFIER_EXPRESSION) {
            variableName = declaration.lvalue.asIdentifierExpression().identifierToken.value;
        } else {
            throw new SyntacticSupportNotImplemented(variableLocation + ": Only identifier-var declarations supported");
        }
        if (!env.getFunction().getVariableNames().contains(variableName)) {
            DeclareVariableNode declarationNode = new DeclareVariableNode(variableName, variableLocation);
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

            syntacticInformationCollector.registerVariableDeclaration(declaration, variableLocation, sourceLocationMaker);

            addNodeToBlock(new WriteVariableNode(rhsEnv.getResultRegister(), variableName, variableLocation), processed.getAppendBlock(), env.makeStatementLevel(true));
        } else {
            processed = TranslationResult.makeAppendBlock(env.getAppendBlock());
        }
        return processed;
    }

    @Override
    public TranslationResult process(WhileStatementTree tree, AstEnv env) {
        LoopTree loopTree = new LoopTree(tree);
        syntacticInformationCollector.registerLoop(loopTree, env, astInfo);
        return processLoop(tree, null, tree.condition, null, tree.body, false, makeSourceLocation(tree.condition), astInfo.getNestedLoops().contains(loopTree), env, false);
    }

    @Override
    public TranslationResult process(WithStatementTree tree, AstEnv env) {
        SourceLocation location = makeSourceLocation(tree);
        int withRegister = nextRegister(env);

        // 1. read the with-expression
        final AstEnv expressionEnv = env.makeResultRegister(withRegister).makeStatementLevel(false);
        TranslationResult processedExpression = process(tree.expression, expressionEnv);
        BasicBlock appendBlock = processedExpression.getAppendBlock();

        // 2. begin the with-environment (and end it exceptionally)
        addNodeToBlock(new BeginWithNode(withRegister, location), appendBlock, env.makeStatementLevel(true));
        appendBlock = makeSuccessorBasicBlock(appendBlock, functionAndBlocksManager);

        endNodeScopeExceptionally(new EndWithNode(location), env.makeAppendBlock(appendBlock).makeStatementLevel(true));

        // 3. process the body (and end it ordinarily)
        BasicBlock jumpThroughBlock = makeJumpThroughBlock(env.getAppendBlock().getExceptionHandler());
        addNodeToBlock(new EndWithNode(location), jumpThroughBlock, env);
        AstEnv bodyEnv = env.makeBaseRegister(nextRegister(env)).makeAppendBlock(appendBlock).makeJumpThroughBlock(new JumpThroughBlocks(jumpThroughBlock, functionAndBlocksManager)).makeStatementLevel(true);
        TranslationResult processedBody = process(tree.body, bodyEnv);
        appendBlock = processedBody.getAppendBlock();

        appendBlock = endNodeScopeOrdinarily(new EndWithNode(location), appendBlock.getExceptionHandler(), env.makeAppendBlock(appendBlock));

        return TranslationResult.makeAppendBlock(appendBlock);
    }

    @Override
    public TranslationResult process(ThisExpressionTree tree, AstEnv env) {
        TranslationResult processedAstInfo = processAccessPartly(tree, env);
        syntacticInformationCollector.registerSimpleRead(tree, processedAstInfo.getResultReference());
        processedAstInfo = TranslationResult.makeAppendBlock(read(processedAstInfo.getResultReference(), env.makeBaseRegister(AbstractNode.NO_VALUE)));
        return processedAstInfo;
    }

    @Override
    public TranslationResult process(NullTree literalNode, AstEnv env) {
        return TranslationResult.makeAppendBlock(env.getAppendBlock());
    }

    @Override
    public TranslationResult process(UpdateExpressionTree tree, AstEnv env) {
        return processCompoundAssignmentOperation(tree.operatorPosition == UpdateExpressionTree.OperatorPosition.PREFIX, true, getPrefixPostfixOp(tree.operator.type), tree.operand, makeConstant(1, tree.location), env, makeSourceLocation(tree));
    }

    @Override
    public TranslationResult process(CommaExpressionTree tree, AstEnv env) {
        return processList(tree.expressions, env.makeStatementLevel(false));
    }

    @Override
    public TranslationResult process(FinallyTree tree, AstEnv env) {
        return process(tree.block, env); // finally wiring is being done in the try-handler
    }

    @Override
    public TranslationResult process(GetAccessorTree tree, AstEnv env) {
        FormalParameterListTree parameters = new FormalParameterListTree(tree.location, ImmutableList.of());
        String source = String.format("function %s()%s", tree.propertyName.asIdentifier().value, getSource(tree.body));
        return processAccessor(tree, tree.propertyName, parameters, tree.body, Kind.GETTER, env, makeSourceLocation(tree), source);
    }

    @Override
    public TranslationResult process(SetAccessorTree tree, AstEnv env) {
        IdentifierExpressionTree parameterName = new IdentifierExpressionTree(tree.parameter.location, tree.parameter);
        FormalParameterListTree parameters = new FormalParameterListTree(tree.location, ImmutableList.of(parameterName));
        String source = String.format("function %s(%s)%s", tree.propertyName.asIdentifier().value,  tree.parameter.value, getSource(tree.body));
        return processAccessor(tree, tree.propertyName, parameters, tree.body, Kind.SETTER, env, makeSourceLocation(tree), source);
    }

    private TranslationResult processAccessor(ParseTree accessorTree, Token propertyName, FormalParameterListTree parameters, ParseTree body, Kind propertyKind, AstEnv env, SourceLocation location, String prettySource) {
        syntacticInformationCollector.registerDeclaredAccessor(accessorTree, location, sourceLocationMaker);

        int functionRegister = env.getRegisterManager().nextRegister();
        processFunctionDeclaration(FunctionDeclarationTree.Kind.EXPRESSION, null, parameters, body, env.makeResultRegister(functionRegister), location, prettySource);
        Integer base = env.getThisRegister();
        WritePropertyNode write = makeWriteFixedPropertyNode(base, propertyName, functionRegister, propertyKind, location);
        addNodeToBlock(write, env.getAppendBlock(), env);
        return TranslationResult.makeAppendBlock(env.getAppendBlock());
    }

    @Override
    public TranslationResult process(PropertyNameAssignmentTree tree, AstEnv env) {
        AstEnv rhsEnv = env.makeResultRegister(nextRegister(env)).makeStatementLevel(false);
        TranslationResult processedRhs;
        if (tree.value == null) {
            // Implicit property value assignment ie.
            // var b = {a};
            SourceLocation location = makeSourceLocation(tree);
            processedRhs = TranslationResult.makeAppendBlock(read(new Variable(tree.name.asIdentifier().value, location), rhsEnv));
        } else {
            processedRhs = process(tree.value, rhsEnv);
        }

        Integer base = env.getThisRegister();
        int value = rhsEnv.getResultRegister();
        WritePropertyNode write = makeWriteFixedPropertyNode(base, tree.name, value, Kind.ORDINARY, makeSourceLocation(tree));
        addNodeToBlock(write, processedRhs.getAppendBlock(), env);
        return processedRhs;
    }

    WritePropertyNode makeWriteFixedPropertyNode(int baseRegister, Token propertyName, int rhsRegister, Kind propertyKind, SourceLocation location) {
        switch (propertyName.type) {
            case IDENTIFIER: {
                return new WritePropertyNode(baseRegister, propertyName.asIdentifier().value, rhsRegister, propertyKind, true, location);
            }
            case STRING: {
                return new WritePropertyNode(baseRegister, ClosureASTUtil.normalizeString(propertyName.asLiteral()), rhsRegister, propertyKind, true, location);
            }
            case NUMBER: {
                return new WritePropertyNode(baseRegister, propertyName.asLiteral().value, rhsRegister, propertyKind, true, location);
            }
            default:
                throw new RuntimeException("Unhandled property name type: " + propertyName.type);
        }
    }

    @Override
    public TranslationResult process(LiteralExpressionTree tree, AstEnv env) {
        return literalBuilder.process(tree, env);
    }

    private class LiteralBuilder extends DispatchingLiteralTreeAuxVisitor<TranslationResult, AstEnv> {


        @Override
        public TranslationResult processStringLiteral(LiteralExpressionTree tree, AstEnv env) {
            String string = ClosureASTUtil.normalizeString(tree.literalToken.asLiteral());
            ConstantNode node = ConstantNode.makeString(string, env.getResultRegister(), makeSourceLocation(tree));
            BasicBlock block = env.getAppendBlock();
            addNodeToBlock(node, block, env);
            return TranslationResult.makeAppendBlock(env.getAppendBlock());        }

        @Override
        public TranslationResult processNumberLiteral(LiteralExpressionTree tree, AstEnv env) {
            double number = ClosureASTUtil.normalizeNumber(tree.literalToken.asLiteral());
            ConstantNode node = ConstantNode.makeNumber(number, env.getResultRegister(), makeSourceLocation(tree));
            BasicBlock block = env.getAppendBlock();
            addNodeToBlock(node, block, env);
            return TranslationResult.makeAppendBlock(env.getAppendBlock());        }

        @Override
        public TranslationResult processBooleanLiteral(LiteralExpressionTree tree, AstEnv env) {
            addNodeToBlock(ConstantNode.makeBoolean(tree.literalToken.type == TokenType.TRUE, env.getResultRegister(), makeSourceLocation(tree)), env.getAppendBlock(), env);
            return TranslationResult.makeAppendBlock(env.getAppendBlock());        }

        @Override
        public TranslationResult processNullLiteral(LiteralExpressionTree tree, AstEnv env) {
            addNodeToBlock(ConstantNode.makeNull(env.getResultRegister(), makeSourceLocation(tree)), env.getAppendBlock(), env);
            return TranslationResult.makeAppendBlock(env.getAppendBlock());        }

        @Override
        public TranslationResult processRegExpLiteral(LiteralExpressionTree tree, AstEnv env) {
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
            BasicBlock callBlock = makeSuccessorBasicBlock(env.getAppendBlock(), functionAndBlocksManager);
            addNodeToBlock(new CallNode(LiteralConstructorKinds.REGEXP, env.getResultRegister(), AbstractNode.NO_VALUE, variableEnv.getResultRegister(), args, location), callBlock, env);
            return TranslationResult.makeAppendBlock(makeSuccessorBasicBlock(callBlock, functionAndBlocksManager));
        }
    }

    private SourceLocation makeSourceLocation(ParseTree tree) {
        return FunctionBuilderHelper.makeSourceLocation(tree, sourceLocationMaker);
    }

    @Override
    public TranslationResult unsupportedLanguageFeature(ParseTree tree, String feature) {
        throw new SyntacticSupportNotImplemented(FunctionBuilderHelper.makeSourceLocation(tree, sourceLocationMaker) + ": " + feature + " not yet supported");
    }

    @Override
    public TranslationResult ignoredByClosureCompiler(ParseTree tree) {
        return null;
    }

    @Override
    public TranslationResult process(DebuggerStatementTree tree, AstEnv env) {
        // NOOP
        return TranslationResult.makeAppendBlock(env.getAppendBlock());
    }

    public TranslationResult process(ParseTree tree, AstEnv env) {
        if (!env.isStatementLevel()) {
            // probably correct, not sure if isStatementLevel always is enabled when it should be
            // minor consequence: some statements will also seem to make use of the register
            syntacticInformationCollector.registerExpressionRegister(tree, env.getResultRegister());
        }
        return super.process(tree, env);
    }
}
