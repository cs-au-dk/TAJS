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
import com.google.javascript.jscomp.parsing.parser.trees.UpdateExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationListTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WhileStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.WithStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.YieldExpressionTree;

public abstract class DefaultDispatchingParseTreeAuxVisitor<V, Aux> extends DispatchingParseTreeAuxVisitor<V, Aux> {

    @Override
    public V process(AmbientDeclarationTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ArrayLiteralExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ArrayPatternTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ArrayTypeTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(AssignmentRestElementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ProgramTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(AwaitExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(BinaryOperatorTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(BlockTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(BreakStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(CallSignatureTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(CatchTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ClassDeclarationTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(CommaExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ComprehensionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ComprehensionForTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ComprehensionIfTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ComputedPropertyDefinitionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ComputedPropertyGetterTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ComputedPropertyMemberVariableTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ComputedPropertyMethodTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ComputedPropertySetterTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ConditionalExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ContinueStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(DebuggerStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(DefaultParameterTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(DoWhileStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(MemberLookupExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(EmptyStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(EnumDeclarationTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ExportDeclarationTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ExportSpecifierTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ExpressionStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(FinallyTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ForInStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ForStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ForOfStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(FormalParameterListTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(FunctionDeclarationTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(CallExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(FunctionTypeTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(GenericTypeListTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(GetAccessorTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(IfStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ImportDeclarationTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ImportSpecifierTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(IndexSignatureTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(InterfaceDeclarationTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(LabelledStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(MemberVariableTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(MissingPrimaryExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(IdentifierExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(NamespaceDeclarationTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(NewExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(NewTargetExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(NullTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ObjectLiteralExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ObjectPatternTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(OptionalParameterTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ParameterizedTypeTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ParenExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(UpdateExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(MemberExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(PropertyNameAssignmentTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(RecordTypeTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(LiteralExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(RestParameterTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ReturnStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(SetAccessorTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(SpreadExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(SuperExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(CaseClauseTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(DefaultClauseTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(SwitchStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(TemplateLiteralExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(TemplateLiteralPortionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(TemplateSubstitutionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ThisExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(ThrowStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(TryStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(TypeAliasTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(TypeNameTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(TypeQueryTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(TypedParameterTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(UnaryExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(UnionTypeTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(VariableDeclarationTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(VariableDeclarationListTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(VariableStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(WhileStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(WithStatementTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

    @Override
    public V process(YieldExpressionTree tree, Aux aux) {
        return unsupportedLanguageFeature(tree, tree.getClass().getSimpleName());
    }

}
