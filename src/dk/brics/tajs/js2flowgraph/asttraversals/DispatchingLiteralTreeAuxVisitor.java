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

package dk.brics.tajs.js2flowgraph.asttraversals;

import com.google.javascript.jscomp.parsing.parser.trees.LiteralExpressionTree;

public abstract class DispatchingLiteralTreeAuxVisitor<T, V> implements LiteralTreeAuxVisitor<T, V> {

    /**
     * From {@link com.google.javascript.jscomp.parsing.IRFactory.TransformDispatcher#processLiteralExpression(LiteralExpressionTree)}
     */
    public T process(LiteralExpressionTree tree, V aux) {
        switch (tree.literalToken.type) {
            case NUMBER:
                return processNumberLiteral(tree, aux);
            case STRING:
                return processStringLiteral(tree, aux);
            case FALSE:
            case TRUE:
                return processBooleanLiteral(tree, aux);
            case NULL:
                return processNullLiteral(tree, aux);
            case REGULAR_EXPRESSION:
                return processRegExpLiteral(tree, aux);
            default:
                throw new IllegalStateException("Unexpected literal type: "
                        + tree.literalToken.getClass() + " type: "
                        + tree.literalToken.type);
        }
    }
}
