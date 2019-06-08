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

package dk.brics.tajs.flowgraph.syntaticinfo;

import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;

import java.util.Set;

/**
 * Syntactic information in an easily queriable form.
 */
@SuppressWarnings("unused")
// The use of this information varies: future heuristics might find the information useful, even though it is not used at the moment.
public interface SyntacticQueries {

    /**
     * True iff the given variable appears syntactically in the condition of a non-nested loop
     * and in a dynamic property read operation in the given function.
     */
    boolean isLoopVariable(Function function, String variable);

    /**
     * True iff the given parameter is used in the given literal constructor.
     */
    boolean doesLiteralReferenceParameter(AbstractNode literalAllocationNode, String parameterName);

    /**
     * True iff the given node is inside a for-in loop.
     */
    boolean isInForIn(AbstractNode node); // TODO: currently unused

    /**
     * True iff the given function contains variables that are used as both property read and write names.
     */
    boolean isCorrelatedAccessFunction(Function function);

    /**
     * The set of CallNodes to TAJS_* function with 'false' as first or fourth argument.
     */
    Set<CallNode> getTajsCallsWithLiteralFalseAsFirstOrFourthArgument();

    /**
     * True iff the parse tree is a simple read.
     */
    boolean isSimpleRead(ParseTree tree);

    /**
     * The associated reference for a simple read.
     */
    SyntacticReference getSimpleRead(ParseTree tree);

    /**
     * The register used for storing the result of an expression.
     */
    int getExpressionRegister(ParseTree tree);

    /**
     * True iff the variable could be allocated on the stack (i.e. not referenced by any closures).
     */
    boolean isStackVariable(Function function, String variableName); // TODO: currently unused

    /**
     * The set of free variables of a function that are bound in an outer function (closure variables) - includes only variables used directly in function.
     */
    Set<String> getClosureVariableNames(Function function);

    /**
     * True iff the function has an occurence of 'this'.
     */
    boolean isFunctionWithThisReference(Function function);

    /**
     * The set of free variables of a function that are bound in an outer function (closure variables) - includes variables used in functions defined inside fun.
     */
    Set<String> getClosureVariableNamesTransitively(Function fun);
}
