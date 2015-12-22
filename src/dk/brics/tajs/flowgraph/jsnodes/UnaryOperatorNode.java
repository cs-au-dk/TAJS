/*
 * Copyright 2009-2015 Aarhus University
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
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;

/**
 * Unary operator node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = &lt;<i>op</i>&gt;(<i>v</i>)
 */
public class UnaryOperatorNode extends LoadNode {

    /**
     * The different kinds of unary operators.
     */
    public enum Op {

        /**
         * ~
         */
        COMPLEMENT,

        /**
         * !
         */
        NOT,

        /**
         * -
         */
        MINUS,

        /**
         * +
         */
        PLUS
    }

    private int arg_reg;

    private Op op;

    /**
     * Constructs a new unary operator node.
     *
     * @param op         The unary operator.
     * @param arg_reg    The register for the argument.
     * @param result_reg The register for the result.
     * @param location   The source location.
     */
    public UnaryOperatorNode(Op op, int arg_reg, int result_reg, SourceLocation location) {
        super(result_reg, location);
        this.arg_reg = arg_reg;
        this.op = op;
    }

    /**
     * Returns the argument register.
     */
    public int getArgRegister() {
        return arg_reg;
    }

    /**
     * Sets the argument register.
     */
    public void setArgRegister(int arg_reg) {
        this.arg_reg = arg_reg;
    }

    /**
     * Returns the operator.
     */
    public Op getOperator() {
        return op;
    }

    /**
     * Returns the operator in human readable form.
     */
    private String operatorToString() {
        switch (op) {
            case COMPLEMENT:
                return "~";
            case NOT:
                return "!";
            case MINUS:
                return "-";
            case PLUS:
                return "+";
            default:
                throw new AnalysisException("Unexpected operator");
        }
    }

    @Override
    public String toString() {
        int resultReg = getResultRegister();
        return operatorToString() + "[v" + arg_reg + "," + (resultReg == NO_VALUE ? "-" : ("v" + resultReg)) + "]";
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean canThrowExceptions() {
        return true;
    }

    @Override
    public void check(BasicBlock b) {
        if (arg_reg == NO_VALUE)
            throw new AnalysisException("Invalid argument register: " + toString());
    }
}
