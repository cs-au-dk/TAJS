/*
 * Copyright 2009-2018 Aarhus University
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
import dk.brics.tajs.flowgraph.jsnodes.IfNode;

import java.util.Optional;
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
    boolean isInForIn(AbstractNode node);

    /**
     * True iff the given function contains variables that are used as both property read and write names.
     */
    boolean isCorrelatedAccessFunction(Function function);

    /**
     * The set of CallNodes to TAJS_* function with 'false' as first or fourth argument.
     */
    Set<CallNode> getTajsCallsWithLiteralFalseAsFirstOrFourthArgument();

    /**
     * The set of pattern-matched conditions used in the condition of an if-node.
     */
    Set<ConditionPattern> getConditionPattern(IfNode ifNode);

    /**
     * The base-reference of a property access.
     */
    Optional<SyntacticReference> getBaseReference(AbstractNode node);

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
     * The variables that are used as the first argument to a function call in a condition, where the variable is also used in he body of the condition.
     */
    Set<String> getConditionRefined1ArgumentVariables(IfNode ifNode);

    /**
     * The variables that are used as arguments to a function call in a condition, where the variable is also used in he body of the condition.
     */
    Set<String> getConditionRefinedArgumentVariables(IfNode ifNode);

    /**
     * True iff the variable could be allocated on the stack (i.e. not referenced by any closures).
     */
    boolean isStackVariable(Function function, String variableName);

    /**
     * The set of free variables of a function that are bound in an outer function (closure variables).
     */
    Set<String> getClosureVariableNames(Function function);

    /**
     * True iff the function has an occurence of 'this'.
     */
    boolean isFunctionWithThisReference(Function function);
}
