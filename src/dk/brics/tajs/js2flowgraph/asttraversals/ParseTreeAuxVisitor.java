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

public interface ParseTreeAuxVisitor<T, V> {

    T process(ParseTree tree, V aux);

    T process(AmbientDeclarationTree tree, V aux);

    T process(ArrayLiteralExpressionTree tree, V aux);

    T process(ArrayPatternTree tree, V aux);

    T process(ArrayTypeTree tree, V aux);

    T process(AssignmentRestElementTree tree, V aux);

    T process(ProgramTree tree, V aux);

    T process(AwaitExpressionTree tree, V aux);

    T process(BinaryOperatorTree tree, V aux);

    T process(BlockTree tree, V aux);

    T process(BreakStatementTree tree, V aux);

    T process(CallSignatureTree tree, V aux);

    T process(CatchTree tree, V aux);

    T process(ClassDeclarationTree tree, V aux);

    T process(CommaExpressionTree tree, V aux);

    T process(ComprehensionTree tree, V aux);

    T process(ComprehensionForTree tree, V aux);

    T process(ComprehensionIfTree tree, V aux);

    T process(ComputedPropertyDefinitionTree tree, V aux);

    T process(ComputedPropertyGetterTree tree, V aux);

    T process(ComputedPropertyMemberVariableTree tree, V aux);

    T process(ComputedPropertyMethodTree tree, V aux);

    T process(ComputedPropertySetterTree tree, V aux);

    T process(ConditionalExpressionTree tree, V aux);

    T process(ContinueStatementTree tree, V aux);

    T process(DebuggerStatementTree tree, V aux);

    T process(DefaultParameterTree tree, V aux);

    T process(DoWhileStatementTree tree, V aux);

    T process(MemberLookupExpressionTree tree, V aux);

    T process(EmptyStatementTree tree, V aux);

    T process(EnumDeclarationTree tree, V aux);

    T process(ExportDeclarationTree tree, V aux);

    T process(ExportSpecifierTree tree, V aux);

    T process(ExpressionStatementTree tree, V aux);

    T process(FinallyTree tree, V aux);

    T process(ForInStatementTree tree, V aux);

    T process(ForStatementTree tree, V aux);

    T process(ForOfStatementTree tree, V aux);

    T process(FormalParameterListTree tree, V aux);

    T process(FunctionDeclarationTree tree, V aux);

    T process(CallExpressionTree tree, V aux);

    T process(FunctionTypeTree tree, V aux);

    T process(GenericTypeListTree tree, V aux);

    T process(GetAccessorTree tree, V aux);

    T process(IfStatementTree tree, V aux);

    T process(ImportDeclarationTree tree, V aux);

    T process(ImportSpecifierTree tree, V aux);

    T process(IndexSignatureTree tree, V aux);

    T process(InterfaceDeclarationTree tree, V aux);

    T process(LabelledStatementTree tree, V aux);

    T process(MemberVariableTree tree, V aux);

    T process(MissingPrimaryExpressionTree tree, V aux);

    T process(IdentifierExpressionTree tree, V aux);

    T process(NamespaceDeclarationTree tree, V aux);

    T process(NewExpressionTree tree, V aux);

    T process(NewTargetExpressionTree tree, V aux);

    T process(NullTree tree, V aux);

    T process(ObjectLiteralExpressionTree tree, V aux);

    T process(ObjectPatternTree tree, V aux);

    T process(OptionalParameterTree tree, V aux);

    T process(ParameterizedTypeTree tree, V aux);

    T process(ParenExpressionTree tree, V aux);

    T process(UpdateExpressionTree tree, V aux);

    T process(MemberExpressionTree tree, V aux);

    T process(PropertyNameAssignmentTree tree, V aux);

    T process(RecordTypeTree tree, V aux);

    T process(LiteralExpressionTree tree, V aux);

    T process(RestParameterTree tree, V aux);

    T process(ReturnStatementTree tree, V aux);

    T process(SetAccessorTree tree, V aux);

    T process(SpreadExpressionTree tree, V aux);

    T process(SuperExpressionTree tree, V aux);

    T process(CaseClauseTree tree, V aux);

    T process(DefaultClauseTree tree, V aux);

    T process(SwitchStatementTree tree, V aux);

    T process(TemplateLiteralExpressionTree tree, V aux);

    T process(TemplateLiteralPortionTree tree, V aux);

    T process(TemplateSubstitutionTree tree, V aux);

    T process(ThisExpressionTree tree, V aux);

    T process(ThrowStatementTree tree, V aux);

    T process(TryStatementTree tree, V aux);

    T process(TypeAliasTree tree, V aux);

    T process(TypeNameTree tree, V aux);

    T process(TypeQueryTree tree, V aux);

    T process(TypedParameterTree tree, V aux);

    T process(UnaryExpressionTree tree, V aux);

    T process(UnionTypeTree tree, V aux);

    T process(VariableDeclarationTree tree, V aux);

    T process(VariableDeclarationListTree tree, V aux);

    T process(VariableStatementTree tree, V aux);

    T process(WhileStatementTree tree, V aux);

    T process(WithStatementTree tree, V aux);

    T process(YieldExpressionTree tree, V aux);

    T unsupportedLanguageFeature(ParseTree tree, String feature);

    T ignoredByClosureCompiler(ParseTree tree);
}
