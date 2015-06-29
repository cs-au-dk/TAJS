package dk.brics.tajs.js2flowgraph;

import com.google.javascript.jscomp.parsing.ParseTreeVisitor;
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

/**
 * Visitor implementation with aux argument.
 */
abstract class ParseTreeAuxVisitor<T, V> {

    abstract T process(ArrayLiteralExpressionTree tree, V aux);

    abstract T process(ProgramTree tree, V aux);

    abstract T process(BlockTree tree, V aux);

    abstract T process(BreakStatementTree breakStatementTree, V aux);

    abstract T process(CatchTree tree, V aux);

    abstract T process(ComputedPropertyAssignmentTree tree, V aux);

    abstract T process(ConditionalExpressionTree tree, V aux);

    abstract T process(ContinueStatementTree tree, V aux);

    abstract T process(DoWhileStatementTree tree, V aux);

    abstract T process(MemberLookupExpressionTree tree, V aux);

    abstract T process(EmptyStatementTree tree, V aux);

    abstract T process(ExpressionStatementTree tree, V aux);

    abstract T process(ForInStatementTree tree, V aux);

    abstract T process(ForStatementTree tree, V aux);

    abstract T process(CallExpressionTree tree, V aux);

    abstract T process(FunctionDeclarationTree tree, V aux);

    abstract T process(IfStatementTree tree, V aux);

    abstract T process(BinaryOperatorTree tree, V aux);

    abstract T process(LabelledStatementTree tree, V aux);

    abstract T process(IdentifierExpressionTree tree, V aux);

    abstract T process(NewExpressionTree tree, V aux);

    abstract T process(ObjectLiteralExpressionTree tree, V aux);

    abstract T process(ParenExpressionTree tree, V aux);

    abstract T process(MemberExpressionTree tree, V aux);

    abstract T process(ReturnStatementTree tree, V aux);

    abstract T process(CaseClauseTree tree, V aux);

    abstract T process(SwitchStatementTree tree, V aux);

    abstract T process(ThrowStatementTree tree, V aux);

    abstract T process(TryStatementTree tree, V aux);

    abstract T process(UnaryExpressionTree tree, V aux);

    abstract T process(VariableStatementTree tree, V aux);

    abstract T process(VariableDeclarationListTree tree, V aux);

    abstract T process(VariableDeclarationTree decl, V aux);

    abstract T process(WhileStatementTree tree, V aux);

    abstract T process(WithStatementTree tree, V aux);

    abstract T process(DebuggerStatementTree tree, V aux);

    abstract T process(ThisExpressionTree tree, V aux);

    abstract T process(DefaultClauseTree tree, V aux);

    abstract T processNumberLiteral(LiteralExpressionTree tree, V aux);

    abstract T processStringLiteral(LiteralExpressionTree tree, V aux);

    abstract T processTemplateString(LiteralExpressionTree tree, V aux);

    abstract T processBooleanLiteral(LiteralExpressionTree tree, V aux);

    abstract T processNullLiteral(LiteralExpressionTree tree, V aux);

    abstract T processRegExpLiteral(LiteralExpressionTree tree, V aux);

    abstract T process(NullTree literalNode, V aux);

    abstract T process(PostfixExpressionTree tree, V aux);

    abstract T process(CommaExpressionTree tree, V aux);

    abstract T process(FinallyTree tree, V aux);

    abstract T process(GetAccessorTree tree, V aux);

    abstract T process(SetAccessorTree tree, V aux);

    abstract T process(PropertyNameAssignmentTree tree, V aux);

    abstract T process(FormalParameterListTree tree, V aux);

    abstract T process(DefaultParameterTree tree, V aux);

    abstract T process(RestParameterTree tree, V aux);

    abstract T process(SpreadExpressionTree tree, V aux);

    abstract T process(ClassDeclarationTree tree, V aux);

    abstract T process(SuperExpressionTree tree, V aux);

    abstract T process(YieldExpressionTree tree, V aux);

    abstract T process(ForOfStatementTree tree, V aux);

    abstract T process(ExportDeclarationTree tree, V aux);

    abstract T process(ExportSpecifierTree tree, V aux);

    abstract T process(ImportDeclarationTree tree, V aux);

    abstract T process(ImportSpecifierTree tree, V aux);

    abstract T process(ModuleImportTree tree, V aux);

    abstract T process(MissingPrimaryExpressionTree tree, V aux);

    abstract T processIllegalToken(ParseTree tree, V aux);

    abstract T unsupportedLanguageFeature(ParseTree node, String feature, V aux);

    final T process(ParseTree node, final V aux) {
        final ParseTreeAuxVisitor<T, V> outer = this;
        final ParseTreeVisitor<T> visitor = new ParseTreeVisitor<T>() {

            @Override
            public T processArrayLiteral(ArrayLiteralExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processAstRoot(ProgramTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processBlock(BlockTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processBreakStatement(BreakStatementTree breakStatementTree) {
                return outer.process(breakStatementTree, aux);
            }

            @Override
            public T processCatchClause(CatchTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processComputedProperty(ComputedPropertyAssignmentTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processConditionalExpression(ConditionalExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processContinueStatement(ContinueStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processDoLoop(DoWhileStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processElementGet(MemberLookupExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processEmptyStatement(EmptyStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processExpressionStatement(ExpressionStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processForInLoop(ForInStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processForLoop(ForStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processFunctionCall(CallExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processFunction(FunctionDeclarationTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processIfStatement(IfStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processBinaryExpression(BinaryOperatorTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processLabeledStatement(LabelledStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processName(IdentifierExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processNewExpression(NewExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processNumberLiteral(LiteralExpressionTree tree) {
                return outer.processNumberLiteral(tree, aux);
            }

            @Override
            public T processObjectLiteral(ObjectLiteralExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processParenthesizedExpression(ParenExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processPropertyGet(MemberExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processRegExpLiteral(LiteralExpressionTree tree) {
                return outer.processRegExpLiteral(tree, aux);
            }

            @Override
            public T processReturnStatement(ReturnStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processStringLiteral(LiteralExpressionTree tree) {
                return outer.processStringLiteral(tree, aux);
            }

            @Override
            public T processSwitchCase(CaseClauseTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processSwitchStatement(SwitchStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processThrowStatement(ThrowStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processTemplateString(LiteralExpressionTree tree) {
                return outer.processTemplateString(tree, aux);
            }

            @Override
            public T processTryStatement(TryStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processUnaryExpression(UnaryExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processVariableStatement(VariableStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processVariableDeclarationList(VariableDeclarationListTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processVariableDeclaration(VariableDeclarationTree decl) {
                return outer.process(decl, aux);
            }

            @Override
            public T processWhileLoop(WhileStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processWithStatement(WithStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processDebuggerStatement(DebuggerStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processThisExpression(ThisExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processSwitchDefault(DefaultClauseTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processBooleanLiteral(LiteralExpressionTree tree) {
                return outer.processBooleanLiteral(tree, aux);
            }

            @Override
            public T processNullLiteral(LiteralExpressionTree tree) {
                return outer.processNullLiteral(tree, aux);
            }

            @Override
            public T processNull(NullTree literalNode) {
                return outer.process(literalNode, aux);
            }

            @Override
            public T processPostfixExpression(PostfixExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processCommaExpression(CommaExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processFinally(FinallyTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processGetAccessor(GetAccessorTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processSetAccessor(SetAccessorTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processPropertyNameAssignment(PropertyNameAssignmentTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processFormalParameterList(FormalParameterListTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processDefaultParameter(DefaultParameterTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processRestParameter(RestParameterTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processSpreadExpression(SpreadExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processClassDeclaration(ClassDeclarationTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processSuper(SuperExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processYield(YieldExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processForOf(ForOfStatementTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processExportDecl(ExportDeclarationTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processExportSpec(ExportSpecifierTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processImportDecl(ImportDeclarationTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processImportSpec(ImportSpecifierTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processModuleImport(ModuleImportTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processMissingExpression(MissingPrimaryExpressionTree tree) {
                return outer.process(tree, aux);
            }

            @Override
            public T processIllegalToken(ParseTree node2) {
                return outer.processIllegalToken(node2, aux);
            }

            @Override
            public T unsupportedLanguageFeature(ParseTree node2, String feature) {
                return outer.unsupportedLanguageFeature(node2, feature, aux);
            }
        };
        return visitor.process(node);
    }
}
