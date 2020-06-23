/*
 * Copyright 2009-2020 Aarhus University
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

import com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree;
import com.google.javascript.jscomp.parsing.parser.trees.CommaExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ForInStatementTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParenExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType;
import com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.ValueLogLocationInformation;
import dk.brics.tajs.util.Collections;

import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.stripParens;

/**
 * Helper class for populating {@link ValueLogLocationInformation}.
 * <p>
 * All the register*-methods should be called when appropriate when building the flowgraph.
 */
public class ValueLogLocationRemapping {

    private final ValueLogLocationInformation state;

    public ValueLogLocationRemapping(ValueLogLocationInformation state) {
        this.state = state;
    }

    /**
     * @see #addRawMapping(SourceLocation, SourceLocation, boolean)
     */
    private void addMapping(SourceLocation tajsLocation, ParseTree tree, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        addMapping_noStrip(tajsLocation, stripParens(tree), sourceLocationMaker);
    }

    /**
     * @see #addRawMapping(SourceLocation, SourceLocation, boolean)
     */
    private void addMapping_noStrip(SourceLocation tajsLocation, ParseTree tree, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        addRawMapping(tajsLocation, FunctionBuilderHelper.makeSourceLocation(tree, sourceLocationMaker));
    }

    /**
     * @see #addRawMapping(SourceLocation, SourceLocation, boolean)
     */
    private void addRawMapping(SourceLocation tajsLocation, SourceLocation jalangiLocation) {
        addRawMapping(tajsLocation, jalangiLocation, false);
    }

    /**
     * Registers the first given location as an alias of the second given location.
     */
    private void addRawMapping(SourceLocation tajsLocation, SourceLocation jalangiLocation, boolean debug) {
        if (debug) {
            System.out.printf("Adding alias: (%d:%d) -> (%d:%d)%n", tajsLocation.getLineNumber(), tajsLocation.getColumnNumber(), jalangiLocation.getLineNumber(), jalangiLocation.getColumnNumber());
        }
        Collections.addToMapSet(state.getTajsLocation2jalangiLocation(), tajsLocation, jalangiLocation);
    }

    public void registerWriteVariable(BinaryOperatorTree tree, SourceLocation tajsLocation, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        // This if is done in order to match the SoundnessTester and Jalangi sourcelocations.
        // It is kind of weird, variable-writes use the rhs position, property-writes use the lhs position?!
        ParseTree rhs = tree.right;
        boolean rhsIsSequence = stripParens(rhs).type == ParseTreeType.COMMA_EXPRESSION;
        if (rhsIsSequence) {
            // another special case: comma expression use the *first* expressions source location.. ..but do not strip parentheses around that!
            ParseTree firstCommaExpression = ((CommaExpressionTree) stripParens(rhs)).expressions.get(0);
            addMapping_noStrip(tajsLocation, firstCommaExpression, sourceLocationMaker);
        } else {
            addMapping(tajsLocation, rhs, sourceLocationMaker);
        }
    }

    public void registerForIn(ForInStatementTree tree, SourceLocation tajsLocation, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        addMapping(tajsLocation, tree, sourceLocationMaker);
        addRawMapping(tajsLocation, tajsLocation); // Tests occurs for two accesses to o2 in: `for(o2.p in o1){}`, we need an alias!
    }

    /**
     * Examples:
     * ```
     * v++
     * ++o.p
     * v += x
     * v.p += x.p
     * ```
     */
    public void registerCompoundAssignmentOperation(ParseTree operand, SourceLocation sourceLocation, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        addMapping(sourceLocation, operand, sourceLocationMaker);
        addRawMapping(sourceLocation, sourceLocation); // Tests occurs for b on both rhs and lhs: `x.b += y`, we need an alias!
    }

    public void registerVariableDeclaration(VariableDeclarationTree tree, SourceLocation variableLocation, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        addMapping(variableLocation, tree.initializer, sourceLocationMaker);
    }

    public void registerDeclaredAccessor(ParseTree accessorTree, SourceLocation sourceLocation, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        int parenthesisOffset = FunctionBuilderHelper.getSource(accessorTree).indexOf('('); // this assumes the header accessor is defined on a single same line
        SourceLocation jalangiLocation = sourceLocationMaker.make(sourceLocation.getLineNumber(), sourceLocation.getColumnNumber() + parenthesisOffset, sourceLocation.getEndLineNumber(), sourceLocation.getEndColumnNumber() + parenthesisOffset);
        addRawMapping(sourceLocation, jalangiLocation); // for matching function-entries
        state.getDeclaredAccessorAllocationSites().add(sourceLocation); // for matching function-values
    }

    public void registerParenExpression(ParenExpressionTree tree, SourceLocation.SourceLocationMaker sourceLocationMaker) {
        addRawMapping(FunctionBuilderHelper.makeSourceLocation(tree, sourceLocationMaker), FunctionBuilderHelper.makeSourceLocation(tree.expression, sourceLocationMaker));
    }
}
