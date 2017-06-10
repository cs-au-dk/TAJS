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

public abstract class DispatchingParseTreeVisitor<T> implements ParseTreeVisitor<T> {

    private Indirector indirector = new Indirector();

    public T process(ParseTree node) {
        return indirector.process(node, null);
    }

    class Indirector extends DispatchingParseTreeAuxVisitor<T, Void> {

        @Override
        public T process(AmbientDeclarationTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ArrayLiteralExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ArrayPatternTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ArrayTypeTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(AssignmentRestElementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ProgramTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(AwaitExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(BinaryOperatorTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(BlockTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(BreakStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(CallSignatureTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(CatchTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ClassDeclarationTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(CommaExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ComprehensionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ComprehensionForTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ComprehensionIfTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ComputedPropertyDefinitionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ComputedPropertyGetterTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ComputedPropertyMemberVariableTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ComputedPropertyMethodTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ComputedPropertySetterTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ConditionalExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ContinueStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(DebuggerStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(DefaultParameterTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(DoWhileStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(MemberLookupExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(EmptyStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(EnumDeclarationTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ExportDeclarationTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ExportSpecifierTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ExpressionStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(FinallyTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ForInStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ForStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ForOfStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(FormalParameterListTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(FunctionDeclarationTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(CallExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(FunctionTypeTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(GenericTypeListTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(GetAccessorTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(IfStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ImportDeclarationTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ImportSpecifierTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(IndexSignatureTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(InterfaceDeclarationTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(LabelledStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(MemberVariableTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(MissingPrimaryExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(IdentifierExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(NamespaceDeclarationTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(NewExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(NewTargetExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(NullTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ObjectLiteralExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ObjectPatternTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(OptionalParameterTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ParameterizedTypeTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ParenExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(PostfixExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(MemberExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(PropertyNameAssignmentTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(RecordTypeTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(LiteralExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(RestParameterTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ReturnStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(SetAccessorTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(SpreadExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(SuperExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(CaseClauseTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(DefaultClauseTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(SwitchStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(TemplateLiteralExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(TemplateLiteralPortionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(TemplateSubstitutionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ThisExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(ThrowStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(TryStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(TypeAliasTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(TypeNameTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(TypeQueryTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(TypedParameterTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(UnaryExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(UnionTypeTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(VariableDeclarationTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(VariableDeclarationListTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(VariableStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(WhileStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(WithStatementTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T process(YieldExpressionTree tree, Void aux) {
            return DispatchingParseTreeVisitor.this.process(tree);
        }

        @Override
        public T unsupportedLanguageFeature(ParseTree tree, String feature) {
            return DispatchingParseTreeVisitor.this.unsupportedLanguageFeature(tree, feature);
        }

        @Override
        public T ignoredByClosureCompiler(ParseTree tree) {
            return DispatchingParseTreeVisitor.this.ignoredByClosureCompiler(tree);
        }
    }
}
