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

import com.google.javascript.jscomp.parsing.parser.Token;
import com.google.javascript.jscomp.parsing.parser.TokenType;
import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType;
import dk.brics.tajs.flowgraph.syntaticinfo.ConditionPattern;
import dk.brics.tajs.flowgraph.syntaticinfo.OrdinaryEquality;
import dk.brics.tajs.flowgraph.syntaticinfo.SyntacticQueries;
import dk.brics.tajs.flowgraph.syntaticinfo.Truthiness;
import dk.brics.tajs.flowgraph.syntaticinfo.TypeofEquality;
import dk.brics.tajs.flowgraph.syntaticinfo.Variable;

import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Syntactic pattern matching for various conditional expressions.
 */
public class ConditionPatternMatcher {

    private final SyntacticQueries syntacticInformation;

    public ConditionPatternMatcher(SyntacticQueries syntacticInformation) {
        this.syntacticInformation = syntacticInformation;
    }

    private boolean isSomeStrictEquality(Token token) {
        return token.type == TokenType.EQUAL_EQUAL_EQUAL
                || token.type == TokenType.NOT_EQUAL_EQUAL;
    }

    private Variable getVariable(ParseTree tree) {
        return new Variable(tree.asIdentifierExpression().identifierToken.value, null);
    }

    private boolean isTypeof(ParseTree tree) {
        return tree.type == ParseTreeType.UNARY_EXPRESSION
                && tree.asUnaryExpression().operator.type == TokenType.TYPEOF;
    }

    private boolean isSomeEquality(Token token) {
        return isEquality(token) || isInEquality(token);
    }

    private boolean isEquality(Token token) {
        return token.type == TokenType.EQUAL_EQUAL
                || token.type == TokenType.EQUAL_EQUAL_EQUAL;
    }

    private boolean isInEquality(Token token) {
        return token.type == TokenType.NOT_EQUAL
                || token.type == TokenType.NOT_EQUAL_EQUAL;
    }

    public Set<ConditionPattern> match(ParseTree condition) {
        // TODO support parenthesis and negation normalizations
        Set<ConditionPattern> patterns = newSet();
        switch (condition.type) {
            case BINARY_OPERATOR:
                BinaryOperatorTree opTree = condition.asBinaryOperator();
                ParseTree left = opTree.left;
                Token operator = opTree.operator;
                ParseTree right = opTree.right;

                Optional<ConditionPattern> origPattern = matchBinary(left, operator, right, false);
                if (origPattern.isPresent()) {
                    patterns.add(origPattern.get());
                }
                Optional<ConditionPattern> flippedPattern = matchBinary(right, operator, left, true); // flip and retry
                if (flippedPattern.isPresent()) {
                    patterns.add(flippedPattern.get());
                }
                // TODO support some numeric comparisons?
                // TODO support instanceof
                break;
            case IDENTIFIER_EXPRESSION:
                patterns.add(new Truthiness(syntacticInformation.getSimpleRead(condition), false));
                break;
            case UNARY_EXPRESSION:
                if (condition.asUnaryExpression().operator.type == TokenType.BANG && syntacticInformation.isSimpleRead(condition.asUnaryExpression().operand)) {
                    patterns.add(new Truthiness(syntacticInformation.getSimpleRead(condition.asUnaryExpression().operand), true));
                }
                break;
            case MEMBER_EXPRESSION:
            case MEMBER_LOOKUP_EXPRESSION:
                patterns.add(new Truthiness(syntacticInformation.getSimpleRead(condition), false));
                break;
        }
        return patterns;
    }

    private Optional<ConditionPattern> matchBinary(ParseTree left, Token operator, ParseTree right, boolean requireAssociative) {
        // TODO make use of `requireAssociative` (in particular: a function call on the rhs could in principle change the lhs value, making refinements of the (now old) lhs unsound)
        boolean isSomeEquality = isSomeEquality(operator);
        boolean isSomeStrictEquality = isSomeStrictEquality(operator);
        boolean isSomeInequality = isInEquality(operator);

        if (isSomeEquality && syntacticInformation.isSimpleRead(left)) {
            return Optional.of(new OrdinaryEquality(syntacticInformation.getSimpleRead(left), syntacticInformation.getExpressionRegister(right), isSomeStrictEquality, isSomeInequality));
        }
        if (isSomeEquality && isTypeof(left) && syntacticInformation.isSimpleRead(left.asUnaryExpression().operand)) {
            return Optional.of(new TypeofEquality(syntacticInformation.getSimpleRead(left.asUnaryExpression().operand), syntacticInformation.getExpressionRegister(right), isSomeStrictEquality, isSomeInequality));
        }
        return Optional.empty();
    }
}
