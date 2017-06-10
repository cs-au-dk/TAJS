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

package dk.brics.tajs.js2flowgraph;

import com.google.javascript.jscomp.parsing.parser.TokenType;
import com.google.javascript.jscomp.parsing.parser.trees.ArgumentListTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberLookupExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType;
import com.google.javascript.jscomp.parsing.parser.trees.ThisExpressionTree;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.WritableSyntacticInformation;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.js2flowgraph.ASTInfo.ConditionTree;
import dk.brics.tajs.js2flowgraph.ASTInfo.LiteralTree;
import dk.brics.tajs.js2flowgraph.ASTInfo.LoopTree;
import dk.brics.tajs.js2flowgraph.ConditionPatternMatcher.ConditionPattern;
import dk.brics.tajs.js2flowgraph.Reference.Property;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * @see dk.brics.tajs.flowgraph.WritableSyntacticInformation.SyntacticInformation
 */
public class SyntacticHintsCollector {

    private final WritableSyntacticInformation syntacticInformation;

    public SyntacticHintsCollector() {
        this.syntacticInformation = new WritableSyntacticInformation();
    }

    public WritableSyntacticInformation getSyntacticHints() {
        return syntacticInformation;
    }

    public void registerLiteral(AbstractNode literalNode, LiteralTree literalTree, ASTInfo astInfo) {
        if (astInfo.getLiteralsInForIn().contains(literalTree)) {
            syntacticInformation.registerInForIn(literalNode);
        }
        if (astInfo.getVariableReadsInLiterals().containsKey(literalTree)) {
            Set<String> variableReads = astInfo.getVariableReadsInLiterals().get(literalTree);
            for (String variableRead : variableReads) {
                syntacticInformation.registerVariableDependency(literalNode, variableRead);
            }
        }
    }

    public void registerLoop(LoopTree loopTree, AstEnv env, ASTInfo astInfo) {
        // avoid nested loops
        if (astInfo.getNestedLoops().contains(loopTree)) {
            return;
        }
        Set<String> conditionReadVariables = newSet();
        Set<String> nonInitializerWriteVariables = newSet();
        Set<String> dpaReadVariables = newSet();
        if (astInfo.getLoopConditionVariableReads().containsKey(loopTree)) {
            conditionReadVariables.addAll(astInfo.getLoopConditionVariableReads().get(loopTree));
        }
        if (astInfo.getLoopNonInitializerVariableWrites().containsKey(loopTree)) {
            nonInitializerWriteVariables.addAll(astInfo.getLoopNonInitializerVariableWrites().get(loopTree));
        }
        if (astInfo.getLoopNonInitializerDynamicPropertyVariableReads().containsKey(loopTree)) {
            dpaReadVariables.addAll(astInfo.getLoopNonInitializerDynamicPropertyVariableReads().get(loopTree));
        }

        // intersect the three sets: a conjunction
        Set<String> candidateVariables = newSet(conditionReadVariables);
        candidateVariables.retainAll(nonInitializerWriteVariables);
        candidateVariables.retainAll(dpaReadVariables);

        for (String candidateVariable : candidateVariables) {
            syntacticInformation.registerLoopVariable(env.getFunction(), candidateVariable);
        }
    }

    public void registerFunction(Function function, FunctionDeclarationTree functionTree, ASTInfo astInfo) {
        Map<FunctionDeclarationTree, Set<String>> candidates = astInfo.getFunctionsWithVariableCorrelatedPropertyAccesses();
        if (candidates.containsKey(functionTree)) {
            syntacticInformation.registerCorrelatedAccessFunction(function);
        }
        Set<String> stackVariables = newSet();
        stackVariables.addAll(function.getVariableNames());
        stackVariables.addAll(function.getParameterNames());
        stackVariables.removeAll(astInfo.getNonStackVariables().getOrDefault(functionTree, newSet()));
        syntacticInformation.registerStackVariables(function, stackVariables);
        syntacticInformation.registerFunctionClosureVariables(function, astInfo.getFunctionClosureVariables().get(functionTree));
        if (astInfo.getFunctionsWithThisReference().contains(functionTree)) {
            syntacticInformation.registerFunctionWithThisReference(function);
        }
    }

    public void registerFunctionCall(CallNode callNode, ParseTree operand, ArgumentListTree arguments, Reference reference) {
        if (callNode.getTajsFunction() != null) {
            boolean isFirstArgumentLiteralFalse = arguments.arguments.size() == 1
                    && arguments.arguments.get(0).type == ParseTreeType.LITERAL_EXPRESSION
                    && arguments.arguments.get(0).asLiteralExpression().literalToken.type == TokenType.FALSE;
            boolean isFourthArgumentLiteralFalse = arguments.arguments.size() == 4
                    && arguments.arguments.get(3).type == ParseTreeType.LITERAL_EXPRESSION
                    && arguments.arguments.get(3).asLiteralExpression().literalToken.type == TokenType.FALSE;
            if (isFirstArgumentLiteralFalse || isFourthArgumentLiteralFalse) {
                syntacticInformation.registerTAJSCallWithLiteralFalseAsFirstOrFourthArgument(callNode);
            }
        }
        if (reference != null) {
            syntacticInformation.registerAssociatedBaseReference(callNode, reference);
        }
    }

    public void registerIfNodeCondition(IfNode ifNode, ParseTree condition) {
        if (condition == null /* condition might be implicit */) {
            return;
        }
        Set<ConditionPattern> patterns = new ConditionPatternMatcher(syntacticInformation.asReadOnly()).match(condition);
        if (!patterns.isEmpty()) {
            syntacticInformation.registerIfConditionPatterns(ifNode, patterns);
        }
    }

    public void registerPropertyAccess(AbstractNode accessNode, Property property) {
        Reference base = property.base;
        if (base != null) {
            syntacticInformation.registerAssociatedBaseReference(accessNode, base);
        }
    }

    public void registerSimpleRead(IdentifierExpressionTree tree, Reference target) {
        syntacticInformation.registerSimpleRead(tree, target);
    }

    public void registerSimpleRead(MemberExpressionTree tree, Reference target) {
        syntacticInformation.registerSimpleRead(tree, target);
    }

    public void registerSimpleRead(MemberLookupExpressionTree tree, Reference target) {
        syntacticInformation.registerSimpleRead(tree, target);
    }

    public void registerSimpleRead(ThisExpressionTree tree, Reference target) {
        syntacticInformation.registerSimpleRead(tree, target);
    }

    public void registerExpressionRegister(ParseTree expressionTree, int resultRegister) {
        syntacticInformation.registerExpressionRegister(expressionTree, resultRegister);
    }

    public void registerIfNode(IfNode ifNode, ParseTree tree, ASTInfo astInfo) {
        ConditionTree key = ConditionTree.makeUnsafe(tree);
        if (astInfo.getConditionRefined1ArgumentVariables().containsKey(key)) {
            Set<String> variables = astInfo.getConditionRefined1ArgumentVariables().get(key);
            syntacticInformation.registerConditionRefined1ArgumentVariables(ifNode, variables);
        }
        if (astInfo.getConditionRefinedArgumentVariables().containsKey(key)) {
            Set<String> variables = astInfo.getConditionRefinedArgumentVariables().get(key).stream().flatMap(List::stream).collect(Collectors.toSet());
            syntacticInformation.registerConditionRefinedArgumentVariables(ifNode, variables);
        }
    }
}
