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

interface ParseTreeVisitor<T> {

    T process(AmbientDeclarationTree tree);

    T process(ArrayLiteralExpressionTree tree);

    T process(ArrayPatternTree tree);

    T process(ArrayTypeTree tree);

    T process(AssignmentRestElementTree tree);

    T process(ProgramTree tree);

    T process(AwaitExpressionTree tree);

    T process(BinaryOperatorTree tree);

    T process(BlockTree tree);

    T process(BreakStatementTree tree);

    T process(CallSignatureTree tree);

    T process(CatchTree tree);

    T process(ClassDeclarationTree tree);

    T process(CommaExpressionTree tree);

    T process(ComprehensionTree tree);

    T process(ComprehensionForTree tree);

    T process(ComprehensionIfTree tree);

    T process(ComputedPropertyDefinitionTree tree);

    T process(ComputedPropertyGetterTree tree);

    T process(ComputedPropertyMemberVariableTree tree);

    T process(ComputedPropertyMethodTree tree);

    T process(ComputedPropertySetterTree tree);

    T process(ConditionalExpressionTree tree);

    T process(ContinueStatementTree tree);

    T process(DebuggerStatementTree tree);

    T process(DefaultParameterTree tree);

    T process(DoWhileStatementTree tree);

    T process(MemberLookupExpressionTree tree);

    T process(EmptyStatementTree tree);

    T process(EnumDeclarationTree tree);

    T process(ExportDeclarationTree tree);

    T process(ExportSpecifierTree tree);

    T process(ExpressionStatementTree tree);

    T process(FinallyTree tree);

    T process(ForInStatementTree tree);

    T process(ForStatementTree tree);

    T process(ForOfStatementTree tree);

    T process(FormalParameterListTree tree);

    T process(FunctionDeclarationTree tree);

    T process(CallExpressionTree tree);

    T process(FunctionTypeTree tree);

    T process(GenericTypeListTree tree);

    T process(GetAccessorTree tree);

    T process(IfStatementTree tree);

    T process(ImportDeclarationTree tree);

    T process(ImportSpecifierTree tree);

    T process(IndexSignatureTree tree);

    T process(InterfaceDeclarationTree tree);

    T process(LabelledStatementTree tree);

    T process(MemberVariableTree tree);

    T process(MissingPrimaryExpressionTree tree);

    T process(IdentifierExpressionTree tree);

    T process(NamespaceDeclarationTree tree);

    T process(NewExpressionTree tree);

    T process(NewTargetExpressionTree tree);

    T process(NullTree tree);

    T process(ObjectLiteralExpressionTree tree);

    T process(ObjectPatternTree tree);

    T process(OptionalParameterTree tree);

    T process(ParameterizedTypeTree tree);

    T process(ParenExpressionTree tree);

    T process(PostfixExpressionTree tree);

    T process(MemberExpressionTree tree);

    T process(PropertyNameAssignmentTree tree);

    T process(RecordTypeTree tree);

    T process(RestParameterTree tree);

    T process(ReturnStatementTree tree);

    T process(SetAccessorTree tree);

    T process(SpreadExpressionTree tree);

    T process(SuperExpressionTree tree);

    T process(CaseClauseTree tree);

    T process(DefaultClauseTree tree);

    T process(SwitchStatementTree tree);

    T process(TemplateLiteralExpressionTree tree);

    T process(TemplateLiteralPortionTree tree);

    T process(LiteralExpressionTree tree);

    T process(TemplateSubstitutionTree tree);

    T process(ThisExpressionTree tree);

    T process(ThrowStatementTree tree);

    T process(TryStatementTree tree);

    T process(TypeAliasTree tree);

    T process(TypeNameTree tree);

    T process(TypeQueryTree tree);

    T process(TypedParameterTree tree);

    T process(UnaryExpressionTree tree);

    T process(UnionTypeTree tree);

    T process(VariableDeclarationTree tree);

    T process(VariableDeclarationListTree tree);

    T process(VariableStatementTree tree);

    T process(WhileStatementTree tree);

    T process(WithStatementTree tree);

    T process(YieldExpressionTree tree);

    T unsupportedLanguageFeature(ParseTree tree, String feature);

    T ignoredByClosureCompiler(ParseTree tree);
}
