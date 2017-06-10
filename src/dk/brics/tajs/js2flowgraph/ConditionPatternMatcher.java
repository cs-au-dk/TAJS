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
import dk.brics.tajs.flowgraph.WritableSyntacticInformation.SyntacticInformation;

import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Syntactic pattern matching for various conditional expressions.
 */
public class ConditionPatternMatcher {

    private final SyntacticInformation syntacticInformation;

    public ConditionPatternMatcher(SyntacticInformation syntacticInformation) {
        this.syntacticInformation = syntacticInformation;
    }

    private boolean isSomeStrictEquality(Token token) {
        return token.type == TokenType.EQUAL_EQUAL_EQUAL
                || token.type == TokenType.NOT_EQUAL_EQUAL;
    }

    private Reference.Variable getVariable(ParseTree tree) {
        return new Reference.Variable(tree.asIdentifierExpression().identifierToken.value, null);
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

    public interface ConditionPatternVisitor<T> {

        T visit(Truthiness p);

        T visit(TypeofEquality p);

        T visit(OrdinaryEquality p);
    }

    public interface ConditionPattern {

        <T> T accept(ConditionPatternVisitor<T> v);

        Reference getReference();
    }

    public class TypeofEquality extends AbstractEquality {

        public TypeofEquality(Reference reference, int comparateeRegister, boolean isStrict, boolean negated) {
            super(reference, comparateeRegister, isStrict, negated);
        }

        public <T> T accept(ConditionPatternVisitor<T> v) {
            return v.visit(this);
        }
    }

    public class OrdinaryEquality extends AbstractEquality {

        public OrdinaryEquality(Reference reference, int comparateeRegister, boolean isStrict, boolean negated) {
            super(reference, comparateeRegister, isStrict, negated);
        }

        public <T> T accept(ConditionPatternVisitor<T> v) {
            return v.visit(this);
        }
    }

    public abstract class AbstractEquality extends AbstractConditionPattern {

        public final boolean isStrict;

        private final int comparateeRegister;

        public AbstractEquality(Reference reference, int comparateeRegister, boolean isStrict, boolean negated) {
            super(reference, negated);
            this.comparateeRegister = comparateeRegister;
            this.isStrict = isStrict;
        }

        public boolean isStrict() {
            return isStrict;
        }

        public int getComparateeRegister() {
            return comparateeRegister;
        }
    }

    public class Truthiness extends AbstractConditionPattern {

        public Truthiness(Reference reference, boolean negated) {
            super(reference, negated);
        }

        public <T> T accept(ConditionPatternVisitor<T> v) {
            return v.visit(this);
        }
    }

    private abstract class AbstractConditionPattern implements ConditionPattern {

        private final Reference reference;

        private final boolean negated;

        public AbstractConditionPattern(Reference reference, boolean negated) {
            this.reference = reference;
            this.negated = negated;
        }

        public Reference getReference() {
            return reference;
        }

        public boolean isNegated() {
            return negated;
        }
    }
}
