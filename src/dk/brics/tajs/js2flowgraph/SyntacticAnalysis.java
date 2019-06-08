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

import com.google.javascript.jscomp.parsing.parser.TokenType;
import com.google.javascript.jscomp.parsing.parser.trees.ArgumentListTree;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForInStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberLookupExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType;
import com.google.javascript.jscomp.parsing.parser.trees.ThisExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.syntaticinfo.Property;
import dk.brics.tajs.flowgraph.syntaticinfo.RawSyntacticInformation;
import dk.brics.tajs.flowgraph.syntaticinfo.SyntacticQueries;
import dk.brics.tajs.flowgraph.syntaticinfo.SyntacticReference;
import dk.brics.tajs.js2flowgraph.ASTInfo.ConditionTree;
import dk.brics.tajs.js2flowgraph.ASTInfo.LiteralTree;
import dk.brics.tajs.js2flowgraph.ASTInfo.LoopTree;
import dk.brics.tajs.util.Collectors;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Syntactic analysis for obtaining information that eventually is used in {@link SyntacticQueries}.
 */
public class SyntacticAnalysis {

    private final RawSyntacticInformation rawSyntacticInformation;

    private final ValueLogLocationRemapping valueLogLocationRemapping;

    public SyntacticAnalysis(RawSyntacticInformation rawSyntacticInformation, ValueLogLocationRemapping valueLogLocationRemapping) {
        this.rawSyntacticInformation = rawSyntacticInformation;
        this.valueLogLocationRemapping = valueLogLocationRemapping;
    }

    public void registerLiteral(AbstractNode literalNode, LiteralTree literalTree, ASTInfo astInfo) {
        if (astInfo.getLiteralsInForIn().contains(literalTree)) {
            rawSyntacticInformation.getInForIn().add(literalNode);
        }
        if (astInfo.getVariableReadsInLiterals().containsKey(literalTree)) {
            Set<String> variableReads = astInfo.getVariableReadsInLiterals().get(literalTree);
            for (String variableRead : variableReads) {
                addToMapSet(rawSyntacticInformation.getVariableDependencies(), literalNode, variableRead);
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
            addToMapSet(rawSyntacticInformation.getLoopVariables(), env.getFunction(), candidateVariable);
        }
    }

    public void registerFunction(Function function, FunctionDeclarationTree functionTree, ASTInfo astInfo) {
        Map<FunctionDeclarationTree, Set<String>> candidates = astInfo.getFunctionsWithVariableCorrelatedPropertyAccesses();
        if (candidates.containsKey(functionTree)) {
            rawSyntacticInformation.getCorrelatedAccessFunctions().add(function);
        }
        Set<String> stackVariables = newSet();
        stackVariables.addAll(function.getVariableNames());
        stackVariables.addAll(function.getParameterNames());
        stackVariables.removeAll(astInfo.getNonStackVariables().getOrDefault(functionTree, newSet()));
        rawSyntacticInformation.getStackVariables().put(function, stackVariables);
        rawSyntacticInformation.registerFunctionClosureVariables(function, astInfo.getFunctionClosureVariables().get(functionTree));
        if (astInfo.getFunctionsWithThisReference().contains(functionTree)) {
            rawSyntacticInformation.getFunctionsWithThisReference().add(function);
        }
    }

    public void registerFunctionCall(CallNode callNode, ParseTree operand, ArgumentListTree arguments, SyntacticReference reference) {
        if (callNode.getTajsFunctionName() != null) {
            boolean isFirstArgumentLiteralFalse = arguments.arguments.size() == 1
                    && arguments.arguments.get(0).type == ParseTreeType.LITERAL_EXPRESSION
                    && arguments.arguments.get(0).asLiteralExpression().literalToken.type == TokenType.FALSE;
            boolean isFourthArgumentLiteralFalse = arguments.arguments.size() == 4
                    && arguments.arguments.get(3).type == ParseTreeType.LITERAL_EXPRESSION
                    && arguments.arguments.get(3).asLiteralExpression().literalToken.type == TokenType.FALSE;
            if (isFirstArgumentLiteralFalse || isFourthArgumentLiteralFalse) {
                rawSyntacticInformation.getTajsCallsWithLiteralFalseAsFirstOrFourthArgument().add(callNode);
            }
        }
        if (reference != null) {
            rawSyntacticInformation.getNodeWithBaseReferences().put(callNode, reference);
        }
    }

    public void registerIfNodeCondition(IfNode ifNode, ParseTree condition) {
    }

    public void registerPropertyAccess(AbstractNode accessNode, Property property) {
        SyntacticReference base = property.base;
        if (base != null) {
            rawSyntacticInformation.getNodeWithBaseReferences().put(accessNode, base);
        }
    }

    public void registerSimpleRead(IdentifierExpressionTree tree, SyntacticReference target) {
        rawSyntacticInformation.getSimpleReads().put(tree, target);
    }

    public void registerSimpleRead(MemberExpressionTree tree, SyntacticReference target) {
        rawSyntacticInformation.getSimpleReads().put(tree, target);
    }

    public void registerSimpleRead(MemberLookupExpressionTree tree, SyntacticReference target) {
        rawSyntacticInformation.getSimpleReads().put(tree, target);
    }

    public void registerSimpleRead(ThisExpressionTree tree, SyntacticReference target) {
        rawSyntacticInformation.getSimpleReads().put(tree, target);
    }

    public void registerExpressionRegister(ParseTree expressionTree, int resultRegister) {
        rawSyntacticInformation.getExpressionRegisters().put(expressionTree, resultRegister);
    }

    public void registerIfNode(IfNode ifNode, ParseTree tree, ASTInfo astInfo) {
        ConditionTree key = ConditionTree.makeUnsafe(tree);
        if (astInfo.getConditionRefined1ArgumentVariables().containsKey(key)) {
            Set<String> variables = astInfo.getConditionRefined1ArgumentVariables().get(key);
            rawSyntacticInformation.getConditionRefined1ArgumentVariables().put(ifNode, variables);
        }
        if (astInfo.getConditionRefinedArgumentVariables().containsKey(key)) {
            Set<String> variables = astInfo.getConditionRefinedArgumentVariables().get(key).stream()
                    .flatMap(List::stream)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            rawSyntacticInformation.getConditionRefinedArgumentVariables().put(ifNode, variables);
        }
    }

    public void registerWriteVariable(BinaryOperatorTree tree, SourceLocation tajsLocation, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        valueLogLocationRemapping.registerWriteVariable(tree, tajsLocation, sourceLocationMaker);
    }

    public void registerForIn(ForInStatementTree tree, SourceLocation tajsLocation, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        valueLogLocationRemapping.registerForIn(tree, tajsLocation, sourceLocationMaker);
    }

    public void registerCompoundAssignmentOperation(ParseTree operand, SourceLocation sourceLocation, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        valueLogLocationRemapping.registerCompoundAssignmentOperation(operand, sourceLocation, sourceLocationMaker);
    }

    public void registerVariableDeclaration(VariableDeclarationTree tree, SourceLocation variableLocation, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        valueLogLocationRemapping.registerVariableDeclaration(tree, variableLocation, sourceLocationMaker);
    }

    public void registerDeclaredAccessor(ParseTree accessorTree, SourceLocation sourceLocation, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        valueLogLocationRemapping.registerDeclaredAccessor(accessorTree, sourceLocation, sourceLocationMaker);
    }
}
