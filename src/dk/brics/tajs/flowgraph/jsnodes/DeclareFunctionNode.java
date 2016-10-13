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

package dk.brics.tajs.flowgraph.jsnodes;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;

/**
 * Function declaration/expression node.
 * <p>
 * <i>v</i> = function [ <i>name</i> ] ( <i>args</i> ) { <i>body</i> )
 */
public class DeclareFunctionNode extends LoadNode {

    private Function f;

    private boolean expression;

    /**
     * Constructs a new function declaration node.
     *
     * @param f          The function being declared.
     * @param expression True if this an expression, false if it is a declaration.
     * @param result_reg The result register.
     * @param location   The source location.
     */
    public DeclareFunctionNode(Function f, boolean expression, int result_reg, SourceLocation location) {
        super(result_reg, location);
        this.f = f;
        this.expression = expression;
    }

    /**
     * Returns the function being declared.
     */
    public Function getFunction() {
        return f;
    }

    /**
     * Returns true if this is an expression, false if it is a declaration.
     */
    public boolean isExpression() {
        return expression;
    }

    @Override
    public String toString() {
        int resultReg = getResultRegister();
        return "function-" + (expression ? "expr" : "decl") + "[" + f + "," + (resultReg == NO_VALUE ? "-" : ("v" + resultReg)) + "]";
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean canThrowExceptions() {
        return false;
    }

    @Override
    public void check(BasicBlock b) {
        if (f == null)
            throw new AnalysisException("Declare function node with null function: " + toString());
        if (!expression && getResultRegister() != NO_VALUE)
            throw new AnalysisException("Declare function node with nonsense result register: " + toString());
    }
}
