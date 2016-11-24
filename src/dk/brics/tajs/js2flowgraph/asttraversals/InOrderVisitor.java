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

package dk.brics.tajs.js2flowgraph.asttraversals;

import com.google.javascript.jscomp.parsing.parser.trees.AmbientDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayPatternTree;
import com.google.javascript.jscomp.parsing.parser.trees.ArrayTypeTree;
import com.google.javascript.jscomp.parsing.parser.trees.AssignmentRestElementTree;
import com.google.javascript.jscomp.parsing.parser.trees.AwaitExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.BlockTree;
import com.google.javascript.jscomp.parsing.parser.trees.BreakStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.CallExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.CallSignatureTree;
import com.google.javascript.jscomp.parsing.parser.trees.CaseClauseTree;
import com.google.javascript.jscomp.parsing.parser.trees.CatchTree;
import com.google.javascript.jscomp.parsing.parser.trees.ClassDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.CommaExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComprehensionForTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComprehensionIfTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComprehensionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyDefinitionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyGetterTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyMemberVariableTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertyMethodTree;
import com.google.javascript.jscomp.parsing.parser.trees.ComputedPropertySetterTree;
import com.google.javascript.jscomp.parsing.parser.trees.ConditionalExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ContinueStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.DebuggerStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.DefaultClauseTree;
import com.google.javascript.jscomp.parsing.parser.trees.DefaultParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.DoWhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.EmptyStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.EnumDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExportDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExportSpecifierTree;
import com.google.javascript.jscomp.parsing.parser.trees.ExpressionStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FinallyTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForInStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForOfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FormalParameterListTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionTypeTree;
import com.google.javascript.jscomp.parsing.parser.trees.GenericTypeListTree;
import com.google.javascript.jscomp.parsing.parser.trees.GetAccessorTree;
import com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.IfStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.ImportSpecifierTree;
import com.google.javascript.jscomp.parsing.parser.trees.IndexSignatureTree;
import com.google.javascript.jscomp.parsing.parser.trees.InterfaceDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.LabelledStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.LiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberLookupExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberVariableTree;
import com.google.javascript.jscomp.parsing.parser.trees.MissingPrimaryExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NamespaceDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.NewExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NewTargetExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.NullTree;
import com.google.javascript.jscomp.parsing.parser.trees.ObjectLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ObjectPatternTree;
import com.google.javascript.jscomp.parsing.parser.trees.OptionalParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParameterizedTypeTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParenExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.PostfixExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import com.google.javascript.jscomp.parsing.parser.trees.PropertyNameAssignmentTree;
import com.google.javascript.jscomp.parsing.parser.trees.RecordTypeTree;
import com.google.javascript.jscomp.parsing.parser.trees.RestParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.ReturnStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.SetAccessorTree;
import com.google.javascript.jscomp.parsing.parser.trees.SpreadExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.SuperExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.SwitchStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TemplateLiteralExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.TemplateLiteralPortionTree;
import com.google.javascript.jscomp.parsing.parser.trees.TemplateSubstitutionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThisExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ThrowStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TryStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.TypeAliasTree;
import com.google.javascript.jscomp.parsing.parser.trees.TypeNameTree;
import com.google.javascript.jscomp.parsing.parser.trees.TypeQueryTree;
import com.google.javascript.jscomp.parsing.parser.trees.TypedParameterTree;
import com.google.javascript.jscomp.parsing.parser.trees.UnaryExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.UnionTypeTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationListTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WithStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.YieldExpressionTree;
import dk.brics.tajs.js2flowgraph.FunctionBuilderHelper;
import dk.brics.tajs.util.AnalysisLimitationException;

import java.util.List;

public abstract class InOrderVisitor extends DispatchingParseTreeVisitor<Void> {

    public void in(ParseTree tree) {
    }

    public void in(AmbientDeclarationTree tree) {
    }

    public void in(ArrayLiteralExpressionTree tree) {
    }

    public void in(ArrayPatternTree tree) {
    }

    public void in(ArrayTypeTree tree) {
    }

    public void in(AssignmentRestElementTree tree) {
    }

    public void in(ProgramTree tree) {
    }

    public void in(AwaitExpressionTree tree) {
    }

    public void in(BinaryOperatorTree tree) {
    }

    public void in(BlockTree tree) {
    }

    public void in(BreakStatementTree tree) {
    }

    public void in(CallSignatureTree tree) {
    }

    public void in(CatchTree tree) {
    }

    public void in(ClassDeclarationTree tree) {
    }

    public void in(CommaExpressionTree tree) {
    }

    public void in(ComprehensionTree tree) {
    }

    public void in(ComprehensionForTree tree) {
    }

    public void in(ComprehensionIfTree tree) {
    }

    public void in(ComputedPropertyDefinitionTree tree) {
    }

    public void in(ComputedPropertyGetterTree tree) {
    }

    public void in(ComputedPropertyMemberVariableTree tree) {
    }

    public void in(ComputedPropertyMethodTree tree) {
    }

    public void in(ComputedPropertySetterTree tree) {
    }

    public void in(ConditionalExpressionTree tree) {
    }

    public void in(ContinueStatementTree tree) {
    }

    public void in(DebuggerStatementTree tree) {
    }

    public void in(DefaultParameterTree tree) {
    }

    public void in(DoWhileStatementTree tree) {
    }

    public void in(MemberLookupExpressionTree tree) {
    }

    public void in(EmptyStatementTree tree) {
    }

    public void in(EnumDeclarationTree tree) {
    }

    public void in(ExportDeclarationTree tree) {
    }

    public void in(ExportSpecifierTree tree) {
    }

    public void in(ExpressionStatementTree tree) {
    }

    public void in(FinallyTree tree) {
    }

    public void in(ForInStatementTree tree) {
    }

    public void in(ForStatementTree tree) {
    }

    public void in(ForOfStatementTree tree) {
    }

    public void in(FormalParameterListTree tree) {
    }

    public void in(FunctionDeclarationTree tree) {
    }

    public void in(CallExpressionTree tree) {
    }

    public void in(FunctionTypeTree tree) {
    }

    public void in(GenericTypeListTree tree) {
    }

    public void in(GetAccessorTree tree) {
    }

    public void in(IfStatementTree tree) {
    }

    public void in(ImportDeclarationTree tree) {
    }

    public void in(ImportSpecifierTree tree) {
    }

    public void in(IndexSignatureTree tree) {
    }

    public void in(InterfaceDeclarationTree tree) {
    }

    public void in(LabelledStatementTree tree) {
    }

    public void in(MemberVariableTree tree) {
    }

    public void in(MissingPrimaryExpressionTree tree) {
    }

    public void in(IdentifierExpressionTree tree) {
    }

    public void in(NamespaceDeclarationTree tree) {
    }

    public void in(NewExpressionTree tree) {
    }

    public void in(NewTargetExpressionTree tree) {
    }

    public void in(NullTree tree) {
    }

    public void in(ObjectLiteralExpressionTree tree) {
    }

    public void in(ObjectPatternTree tree) {
    }

    public void in(OptionalParameterTree tree) {
    }

    public void in(ParameterizedTypeTree tree) {
    }

    public void in(ParenExpressionTree tree) {
    }

    public void in(PostfixExpressionTree tree) {
    }

    public void in(MemberExpressionTree tree) {
    }

    public void in(PropertyNameAssignmentTree tree) {
    }

    public void in(RecordTypeTree tree) {
    }

    public void in(RestParameterTree tree) {
    }

    public void in(ReturnStatementTree tree) {
    }

    public void in(SetAccessorTree tree) {
    }

    public void in(SpreadExpressionTree tree) {
    }

    public void in(SuperExpressionTree tree) {
    }

    public void in(CaseClauseTree tree) {
    }

    public void in(DefaultClauseTree tree) {
    }

    public void in(SwitchStatementTree tree) {
    }

    public void in(TemplateLiteralExpressionTree tree) {
    }

    public void in(TemplateLiteralPortionTree tree) {
    }

    public void in(LiteralExpressionTree tree) {
    }

    public void in(TemplateSubstitutionTree tree) {
    }

    public void in(ThisExpressionTree tree) {
    }

    public void in(ThrowStatementTree tree) {
    }

    public void in(TryStatementTree tree) {
    }

    public void in(TypeAliasTree tree) {
    }

    public void in(TypeNameTree tree) {
    }

    public void in(TypeQueryTree tree) {
    }

    public void in(TypedParameterTree tree) {
    }

    public void in(UnaryExpressionTree tree) {
    }

    public void in(UnionTypeTree tree) {
    }

    public void in(VariableDeclarationTree tree) {
    }

    public void in(VariableDeclarationListTree tree) {
    }

    public void in(VariableStatementTree tree) {
    }

    public void in(WhileStatementTree tree) {
    }

    public void in(WithStatementTree tree) {
    }

    public void in(YieldExpressionTree tree) {
    }

    public void out(AmbientDeclarationTree tree) {
    }

    public void out(ArrayLiteralExpressionTree tree) {
    }

    public void out(ArrayPatternTree tree) {
    }

    public void out(ArrayTypeTree tree) {
    }

    public void out(AssignmentRestElementTree tree) {
    }

    public void out(ProgramTree tree) {
    }

    public void out(AwaitExpressionTree tree) {
    }

    public void out(BinaryOperatorTree tree) {
    }

    public void out(BlockTree tree) {
    }

    public void out(BreakStatementTree tree) {
    }

    public void out(CallSignatureTree tree) {
    }

    public void out(CatchTree tree) {
    }

    public void out(ClassDeclarationTree tree) {
    }

    public void out(CommaExpressionTree tree) {
    }

    public void out(ComprehensionTree tree) {
    }

    public void out(ComprehensionForTree tree) {
    }

    public void out(ComprehensionIfTree tree) {
    }

    public void out(ComputedPropertyDefinitionTree tree) {
    }

    public void out(ComputedPropertyGetterTree tree) {
    }

    public void out(ComputedPropertyMemberVariableTree tree) {
    }

    public void out(ComputedPropertyMethodTree tree) {
    }

    public void out(ComputedPropertySetterTree tree) {
    }

    public void out(ConditionalExpressionTree tree) {
    }

    public void out(ContinueStatementTree tree) {
    }

    public void out(DebuggerStatementTree tree) {
    }

    public void out(DefaultParameterTree tree) {
    }

    public void out(DoWhileStatementTree tree) {
    }

    public void out(MemberLookupExpressionTree tree) {
    }

    public void out(EmptyStatementTree tree) {
    }

    public void out(EnumDeclarationTree tree) {
    }

    public void out(ExportDeclarationTree tree) {
    }

    public void out(ExportSpecifierTree tree) {
    }

    public void out(ExpressionStatementTree tree) {
    }

    public void out(FinallyTree tree) {
    }

    public void out(ForInStatementTree tree) {
    }

    public void out(ForStatementTree tree) {
    }

    public void out(ForOfStatementTree tree) {
    }

    public void out(FormalParameterListTree tree) {
    }

    public void out(FunctionDeclarationTree tree) {
    }

    public void out(CallExpressionTree tree) {
    }

    public void out(FunctionTypeTree tree) {
    }

    public void out(GenericTypeListTree tree) {
    }

    public void out(GetAccessorTree tree) {
    }

    public void out(IfStatementTree tree) {
    }

    public void out(ParseTree tree) {
    }

    public void out(ImportDeclarationTree tree) {
    }

    public void out(ImportSpecifierTree tree) {
    }

    public void out(IndexSignatureTree tree) {
    }

    public void out(InterfaceDeclarationTree tree) {
    }

    public void out(LabelledStatementTree tree) {
    }

    public void out(MemberVariableTree tree) {
    }

    public void out(MissingPrimaryExpressionTree tree) {
    }

    public void out(IdentifierExpressionTree tree) {
    }

    public void out(NamespaceDeclarationTree tree) {
    }

    public void out(NewExpressionTree tree) {
    }

    public void out(NewTargetExpressionTree tree) {
    }

    public void out(NullTree tree) {
    }

    public void out(ObjectLiteralExpressionTree tree) {
    }

    public void out(ObjectPatternTree tree) {
    }

    public void out(OptionalParameterTree tree) {
    }

    public void out(ParameterizedTypeTree tree) {
    }

    public void out(ParenExpressionTree tree) {
    }

    public void out(PostfixExpressionTree tree) {
    }

    public void out(MemberExpressionTree tree) {
    }

    public void out(PropertyNameAssignmentTree tree) {
    }

    public void out(RecordTypeTree tree) {
    }

    public void out(RestParameterTree tree) {
    }

    public void out(ReturnStatementTree tree) {
    }

    public void out(SetAccessorTree tree) {
    }

    public void out(SpreadExpressionTree tree) {
    }

    public void out(SuperExpressionTree tree) {
    }

    public void out(CaseClauseTree tree) {
    }

    public void out(DefaultClauseTree tree) {
    }

    public void out(SwitchStatementTree tree) {
    }

    public void out(TemplateLiteralExpressionTree tree) {
    }

    public void out(TemplateLiteralPortionTree tree) {
    }

    public void out(LiteralExpressionTree tree) {
    }

    public void out(TemplateSubstitutionTree tree) {
    }

    public void out(ThisExpressionTree tree) {
    }

    public void out(ThrowStatementTree tree) {
    }

    public void out(TryStatementTree tree) {
    }

    public void out(TypeAliasTree tree) {
    }

    public void out(TypeNameTree tree) {
    }

    public void out(TypeQueryTree tree) {
    }

    public void out(TypedParameterTree tree) {
    }

    public void out(UnaryExpressionTree tree) {
    }

    public void out(UnionTypeTree tree) {
    }

    public void out(VariableDeclarationTree tree) {
    }

    public void out(VariableDeclarationListTree tree) {
    }

    public void out(VariableStatementTree tree) {
    }

    public void out(WhileStatementTree tree) {
    }

    public void out(WithStatementTree tree) {
    }

    public void out(YieldExpressionTree tree) {
    }

    private Void process(List<? extends ParseTree> trees) {
        for (ParseTree tree : trees) {
            process(tree);
        }
        return null;
    }

    @Override
    public Void process(ArrayLiteralExpressionTree tree) {
        in(tree);
        process(tree.elements);
        out(tree);
        return null;
    }

    @Override
    public Void process(ArrayPatternTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ArrayTypeTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(AssignmentRestElementTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ProgramTree tree) {
        in(tree);
        process(tree.sourceElements);
        out(tree);
        return null;
    }

    @Override
    public Void process(AwaitExpressionTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(BinaryOperatorTree tree) {
        in(tree);
        process(tree.left);
        process(tree.right);
        out(tree);
        return null;
    }

    @Override
    public Void process(BlockTree tree) {
        in(tree);
        process(tree.statements);
        out(tree);
        return null;
    }

    @Override
    public Void process(LiteralExpressionTree tree) {
        in(tree);
        out(tree);
        return null;
    }

    @Override
    public Void process(BreakStatementTree breakStatementTree) {
        in(breakStatementTree);
        out(breakStatementTree);
        return null;
    }

    @Override
    public Void process(CallSignatureTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(CatchTree tree) {
        in(tree);
        process(tree.catchBody);
        out(tree);
        return null;
    }

    @Override
    public Void process(ClassDeclarationTree tree) {
        in(tree);
        process(tree.superClass);
        process(tree.elements);
        out(tree);
        return null;
    }

    @Override
    public Void process(CommaExpressionTree tree) {
        in(tree);
        process(tree.expressions);
        out(tree);
        return null;
    }

    @Override
    public Void process(ComprehensionTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ComprehensionForTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ComprehensionIfTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ComputedPropertyDefinitionTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ComputedPropertyGetterTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ComputedPropertyMemberVariableTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ComputedPropertyMethodTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ComputedPropertySetterTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ConditionalExpressionTree tree) {
        in(tree);
        process(tree.condition);
        process(tree.left);
        process(tree.right);
        out(tree);
        return null;
    }

    @Override
    public Void process(ContinueStatementTree tree) {
        in(tree);
        out(tree);
        return null;
    }

    @Override
    public Void process(DebuggerStatementTree tree) {
        in(tree);
        out(tree);
        return null;
    }

    @Override
    public Void process(DefaultParameterTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(DoWhileStatementTree tree) {
        in(tree);
        process(tree.body);
        process(tree.condition);
        out(tree);
        return null;
    }

    @Override
    public Void process(MemberLookupExpressionTree tree) {
        in(tree);
        process(tree.operand);
        process(tree.memberExpression);
        out(tree);
        return null;
    }

    @Override
    public Void process(EmptyStatementTree tree) {
        in(tree);
        out(tree);
        return null;
    }

    @Override
    public Void process(EnumDeclarationTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ExportDeclarationTree tree) {
        in(tree);
        process(tree.declaration);
        process(tree.exportSpecifierList);
        out(tree);
        return null;
    }

    @Override
    public Void process(ExportSpecifierTree tree) {
        in(tree);
        out(tree);
        return null;
    }

    @Override
    public Void process(ExpressionStatementTree tree) {
        in(tree);
        process(tree.expression);
        out(tree);
        return null;
    }

    @Override
    public Void process(FinallyTree tree) {
        in(tree);
        process(tree.block);
        out(tree);
        return null;
    }

    @Override
    public Void process(ForInStatementTree tree) {
        in(tree);
        process(tree.initializer);
        process(tree.collection);
        process(tree.body);
        out(tree);
        return null;
    }

    @Override
    public Void process(ForStatementTree tree) {
        in(tree);
        process(tree.initializer);
        process(tree.condition);
        process(tree.increment);
        process(tree.body);
        out(tree);
        return null;
    }

    @Override
    public Void process(ForOfStatementTree tree) {
        in(tree);
        process(tree.initializer);
        process(tree.collection);
        process(tree.body);
        out(tree);
        return null;
    }

    @Override
    public Void process(FormalParameterListTree tree) {
        in(tree);
        process(tree.parameters);
        out(tree);
        return null;
    }

    @Override
    public Void process(FunctionDeclarationTree tree) {
        in(tree);
        process(tree.formalParameterList);
        process(tree.functionBody);
        out(tree);
        return null;
    }

    @Override
    public Void process(CallExpressionTree tree) {
        in(tree);
        process(tree.operand);
        process(tree.arguments.arguments);
        out(tree);
        return null;
    }

    @Override
    public Void process(FunctionTypeTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(GenericTypeListTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(GetAccessorTree tree) {
        in(tree);
        process(tree.body);
        out(tree);
        return null;
    }

    @Override
    public Void process(IfStatementTree tree) {
        in(tree);
        process(tree.condition);
        process(tree.ifClause);
        process(tree.elseClause);
        out(tree);
        return null;
    }

    @Override
    public Void process(ImportDeclarationTree tree) {
        in(tree);
        process(tree.importSpecifierList);
        out(tree);
        return null;
    }

    @Override
    public Void process(ImportSpecifierTree tree) {
        in(tree);
        out(tree);
        return null;
    }

    @Override
    public Void process(IndexSignatureTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(InterfaceDeclarationTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(LabelledStatementTree tree) {
        in(tree);
        process(tree.statement);
        out(tree);
        return null;
    }

    @Override
    public Void process(MemberVariableTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(MissingPrimaryExpressionTree tree) {
        in(tree);
        out(tree);
        return null;
    }

    @Override
    public Void process(IdentifierExpressionTree tree) {
        in(tree);
        out(tree);
        return null;
    }

    @Override
    public Void process(NamespaceDeclarationTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(NewExpressionTree tree) {
        in(tree);
        process(tree.operand);
        process(tree.arguments);
        out(tree);
        return null;
    }

    @Override
    public Void process(NewTargetExpressionTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(NullTree literalNode) {
        in(literalNode);
        out(literalNode);
        return null;
    }

    @Override
    public Void process(ObjectLiteralExpressionTree tree) {
        in(tree);
        process(tree.propertyNameAndValues);
        out(tree);
        return null;
    }

    @Override
    public Void process(ObjectPatternTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(OptionalParameterTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ParameterizedTypeTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ParenExpressionTree tree) {
        in(tree);
        process(tree.expression);
        out(tree);
        return null;
    }

    @Override
    public Void process(PostfixExpressionTree tree) {
        in(tree);
        process(tree.operand);
        out(tree);
        return null;
    }

    @Override
    public Void process(MemberExpressionTree tree) {
        in(tree);
        process(tree.operand);
        out(tree);
        return null;
    }

    @Override
    public Void process(PropertyNameAssignmentTree tree) {
        in(tree);
        process(tree.value);
        out(tree);
        return null;
    }

    @Override
    public Void process(RecordTypeTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(RestParameterTree tree) {
        in(tree);
        out(tree);
        return null;
    }

    @Override
    public Void process(ReturnStatementTree tree) {
        in(tree);
        process(tree.expression);
        out(tree);
        return null;
    }

    @Override
    public Void process(SetAccessorTree tree) {
        in(tree);
        process(tree.body);
        out(tree);
        return null;
    }

    @Override
    public Void process(SpreadExpressionTree tree) {
        in(tree);
        process(tree.expression);
        out(tree);
        return null;
    }

    @Override
    public Void process(SuperExpressionTree tree) {
        in(tree);
        out(tree);
        return null;
    }

    @Override
    public Void process(CaseClauseTree tree) {
        in(tree);
        process(tree.expression);
        process(tree.statements);
        out(tree);
        return null;
    }

    @Override
    public Void process(DefaultClauseTree tree) {
        in(tree);
        process(tree.statements);
        out(tree);
        return null;
    }

    @Override
    public Void process(SwitchStatementTree tree) {
        in(tree);
        process(tree.expression);
        process(tree.caseClauses);
        out(tree);
        return null;
    }

    @Override
    public Void process(TemplateLiteralExpressionTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(TemplateLiteralPortionTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(TemplateSubstitutionTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(ThisExpressionTree tree) {
        in(tree);
        out(tree);
        return null;
    }

    @Override
    public Void process(ThrowStatementTree tree) {
        in(tree);
        process(tree.value);
        out(tree);
        return null;
    }

    @Override
    public Void process(TryStatementTree tree) {
        in(tree);
        process(tree.body);
        process(tree.catchBlock);
        process(tree.finallyBlock);
        out(tree);
        return null;
    }

    @Override
    public Void process(TypeAliasTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(TypeNameTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(TypeQueryTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(TypedParameterTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(UnaryExpressionTree tree) {
        in(tree);
        process(tree.operand);
        out(tree);
        return null;
    }

    @Override
    public Void process(UnionTypeTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public Void process(VariableDeclarationTree decl) {
        in(decl);
        process(decl.lvalue);
        process(decl.initializer);
        out(decl);
        return null;
    }

    @Override
    public Void process(VariableDeclarationListTree tree) {
        in(tree);
        process(tree.declarations.asList());
        out(tree);
        return null;
    }

    @Override
    public Void process(VariableStatementTree tree) {
        in(tree);
        process(tree.declarations);
        out(tree);
        return null;
    }

    @Override
    public Void process(WhileStatementTree tree) {
        in(tree);
        process(tree.condition);
        process(tree.body);
        out(tree);
        return null;
    }

    @Override
    public Void process(WithStatementTree tree) {
        in(tree);
        process(tree.expression);
        process(tree.body);
        out(tree);
        return null;
    }

    @Override
    public Void process(YieldExpressionTree tree) {
        in(tree);
        process(tree.expression);
        out(tree);
        return null;
    }

    @Override
    public Void unsupportedLanguageFeature(ParseTree tree, String feature) {
        throw new AnalysisLimitationException.SyntacticSupportNotImplemented(FunctionBuilderHelper.makeSourceLocation(tree, null) + ": " + feature + " not yet supported");
    }

    @Override
    public Void ignoredByClosureCompiler(ParseTree node) {
        return null;
    }

    public Void process(ParseTree node) {
        if (node != null) {
            super.process(node);
        }
        return null;
    }

    @Override
    public Void process(AmbientDeclarationTree tree) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }
}
