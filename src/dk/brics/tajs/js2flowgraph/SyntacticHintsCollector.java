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

package dk.brics.tajs.js2flowgraph;

import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SyntacticHints;

import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Registration of syntactic hints to be used for context sensitivty.
 */
public class SyntacticHintsCollector {

    private SyntacticHints syntacticHints;

    public SyntacticHintsCollector() {
        this.syntacticHints = new SyntacticHints();
    }

    public SyntacticHints getSyntacticHints() {
        return syntacticHints;
    }

    public void registerLiteral(AbstractNode literalNode, ASTInfo.LiteralTree literalTree, ASTInfo astInfo) {
        if (astInfo.getLiteralsInForIn().contains(literalTree)) {
            syntacticHints.registerInForIn(literalNode);
        }
        if (astInfo.getVariableReadsInLiterals().containsKey(literalTree)) {
            Set<String> variableReads = astInfo.getVariableReadsInLiterals().get(literalTree);
            for (String variableRead : variableReads) {
                syntacticHints.registerVariableDependency(literalNode, variableRead);
            }
        }
    }

    public void registerLoop(ASTInfo.LoopTree loopTree, AstEnv env, ASTInfo astInfo) {
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
            syntacticHints.registerLoopVariable(env.getFunction(), candidateVariable);
        }
    }

    public void registerFunction(Function function, FunctionDeclarationTree functionTree, ASTInfo astInfo) {
        Map<FunctionDeclarationTree, Set<String>> candidates = astInfo.getFunctionsWithVariableCorrelatedPropertyAccesses();
        if (candidates.containsKey(functionTree)) {
            syntacticHints.registerCorrelatedAccessFunction(function);
        }
    }
}
