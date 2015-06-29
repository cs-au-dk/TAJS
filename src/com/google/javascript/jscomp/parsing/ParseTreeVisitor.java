package com.google.javascript.jscomp.parsing;

import com.google.javascript.jscomp.parsing.parser.trees.*;

/**
 * Gives access to com.google.javascript.jscomp.parsing.NewTypeSafeDispatcher.
 */
public abstract class ParseTreeVisitor<T> extends NewTypeSafeDispatcher<T> {

    @Override
    public abstract T processArrayLiteral(ArrayLiteralExpressionTree tree);

    @Override
    public abstract T processAstRoot(ProgramTree tree);

    @Override
    public abstract T processBlock(BlockTree tree);

    @Override
    public abstract T processBreakStatement(BreakStatementTree breakStatementTree);

    @Override
    public abstract T processCatchClause(CatchTree tree);

    @Override
    public abstract T processComputedProperty(ComputedPropertyAssignmentTree tree);

    @Override
    public abstract T processConditionalExpression(ConditionalExpressionTree tree);

    @Override
    public abstract T processContinueStatement(ContinueStatementTree tree);

    @Override
    public abstract T processDoLoop(DoWhileStatementTree tree);

    @Override
    public abstract T processElementGet(MemberLookupExpressionTree tree);

    @Override
    public abstract T processEmptyStatement(EmptyStatementTree tree);

    @Override
    public abstract T processExpressionStatement(ExpressionStatementTree tree);

    @Override
    public abstract T processForInLoop(ForInStatementTree tree);

    @Override
    public abstract T processForLoop(ForStatementTree tree);

    @Override
    public abstract T processFunctionCall(CallExpressionTree tree);

    @Override
    public abstract T processFunction(FunctionDeclarationTree tree);

    @Override
    public abstract T processIfStatement(IfStatementTree tree);

    @Override
    public abstract T processBinaryExpression(BinaryOperatorTree tree);

    @Override
    public abstract T processLabeledStatement(LabelledStatementTree tree);

    @Override
    public abstract T processName(IdentifierExpressionTree tree);

    @Override
    public abstract T processNewExpression(NewExpressionTree tree);

    @Override
    public abstract T processNumberLiteral(LiteralExpressionTree tree);

    @Override
    public abstract T processObjectLiteral(ObjectLiteralExpressionTree tree);

    @Override
    public abstract T processParenthesizedExpression(ParenExpressionTree tree);

    @Override
    public abstract T processPropertyGet(MemberExpressionTree tree);

    @Override
    public abstract T processRegExpLiteral(LiteralExpressionTree tree);

    @Override
    public abstract T processReturnStatement(ReturnStatementTree tree);

    @Override
    public abstract T processStringLiteral(LiteralExpressionTree tree);

    @Override
    public abstract T processSwitchCase(CaseClauseTree tree);

    @Override
    public abstract T processSwitchStatement(SwitchStatementTree tree);

    @Override
    public abstract T processThrowStatement(ThrowStatementTree tree);

    @Override
    public abstract T processTemplateString(LiteralExpressionTree tree);

    @Override
    public abstract T processTryStatement(TryStatementTree tree);

    @Override
    public abstract T processUnaryExpression(UnaryExpressionTree tree);

    @Override
    public abstract T processVariableStatement(VariableStatementTree tree);

    @Override
    public abstract T processVariableDeclarationList(VariableDeclarationListTree tree);

    @Override
    public abstract T processVariableDeclaration(VariableDeclarationTree decl);

    @Override
    public abstract T processWhileLoop(WhileStatementTree tree);

    @Override
    public abstract T processWithStatement(WithStatementTree tree);

    @Override
    public abstract T processDebuggerStatement(DebuggerStatementTree tree);

    @Override
    public abstract T processThisExpression(ThisExpressionTree tree);

    @Override
    public abstract T processSwitchDefault(DefaultClauseTree tree);

    @Override
    public abstract T processBooleanLiteral(LiteralExpressionTree tree);

    @Override
    public abstract T processNullLiteral(LiteralExpressionTree tree);

    @Override
    public abstract T processNull(NullTree literalNode);

    @Override
    public abstract T processPostfixExpression(PostfixExpressionTree tree);

    @Override
    public abstract T processCommaExpression(CommaExpressionTree tree);

    @Override
    public abstract T processFinally(FinallyTree tree);

    @Override
    public abstract T processGetAccessor(GetAccessorTree tree);

    @Override
    public abstract T processSetAccessor(SetAccessorTree tree);

    @Override
    public abstract T processPropertyNameAssignment(PropertyNameAssignmentTree tree);

    @Override
    public abstract T processFormalParameterList(FormalParameterListTree tree);

    @Override
    public abstract T processDefaultParameter(DefaultParameterTree tree);

    @Override
    public abstract T processRestParameter(RestParameterTree tree);

    @Override
    public abstract T processSpreadExpression(SpreadExpressionTree tree);

    @Override
    public abstract T processClassDeclaration(ClassDeclarationTree tree);

    @Override
    public abstract T processSuper(SuperExpressionTree tree);

    @Override
    public abstract T processYield(YieldExpressionTree tree);

    @Override
    public abstract T processForOf(ForOfStatementTree tree);

    @Override
    public abstract T processExportDecl(ExportDeclarationTree tree);

    @Override
    public abstract T processExportSpec(ExportSpecifierTree tree);

    @Override
    public abstract T processImportDecl(ImportDeclarationTree tree);

    @Override
    public abstract T processImportSpec(ImportSpecifierTree tree);

    @Override
    public abstract T processModuleImport(ModuleImportTree tree);

    @Override
    public abstract T processMissingExpression(MissingPrimaryExpressionTree tree);

    @Override
    public abstract T processIllegalToken(ParseTree node);

    @Override
    public abstract T unsupportedLanguageFeature(ParseTree node, String feature);
}
