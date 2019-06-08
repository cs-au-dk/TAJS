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

package dk.brics.tajs.flowgraph.jsnodes;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;

/**
 * Binary operator node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = &lt;<i>op</i>&gt;(<i>v</i><sub>1</sub>,<i>v</i><sub>1</sub>)
 */
public class BinaryOperatorNode extends LoadNode {

    /**
     * The different kinds of binary operators.
     */
    public enum Op {

        /**
         * +
         */
        ADD,

        /**
         * -
         */
        SUB,

        /**
         * *
         */
        MUL,

        /**
         * /
         */
        DIV,

        /**
         * %
         */
        REM,

        /**
         * &amp;
         */
        AND,

        /**
         * |
         */
        OR,

        /**
         * ^
         */
        XOR,

        /**
         * ==
         */
        EQ,

        /**
         * !=
         */
        NE,

        /**
         * &lt;
         */
        LT,

        /**
         * &gt;=
         */
        GE,

        /**
         * &lt;=
         */
        LE,

        /**
         * &gt;
         */
        GT,

        /**
         * &lt;&lt;
         */
        SHL,

        /**
         * &gt;&gt;
         */
        SHR,

        /**
         * &gt;&gt;&gt;
         */
        USHR,

        /**
         * ===
         */
        SEQ,

        /**
         * !==
         */
        SNE,

        /**
         * in
         */
        IN,

        /**
         * instanceof
         */
        INSTANCEOF
    }

    private int arg1_reg;

    private int arg2_reg;

    private Op op;

    /**
     * Constructs a new binary operator node.
     *
     * @param op         The operator
     * @param arg1_reg   The register for the first argument.
     * @param arg2_reg   The register for the second argument.
     * @param result_reg The register for the result.
     * @param location   The source location.
     */
    public BinaryOperatorNode(Op op, int arg1_reg, int arg2_reg, int result_reg, SourceLocation location) {
        super(result_reg, location);
        this.arg1_reg = arg1_reg;
        this.arg2_reg = arg2_reg;
        this.op = op;
    }

    /**
     * Returns the first argument register.
     */
    public int getArg1Register() {
        return arg1_reg;
    }

    /**
     * Returns the second argument register.
     */
    public int getArg2Register() {
        return arg2_reg;
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
    public String operatorToString() {
        switch (op) {
            case ADD:
                return "+";
            case SUB:
                return "-";
            case MUL:
                return "*";
            case DIV:
                return "/";
            case REM:
                return "%";
            case AND:
                return "&";
            case OR:
                return "|";
            case XOR:
                return "^";
            case EQ:
                return "==";
            case NE:
                return "!=";
            case LT:
                return "<";
            case GE:
                return ">=";
            case LE:
                return "<=";
            case GT:
                return ">";
            case SHL:
                return "<<";
            case SHR:
                return ">>";
            case USHR:
                return ">>>";
            case SEQ:
                return "===";
            case SNE:
                return "!==";
            case IN:
                return "in";
            case INSTANCEOF:
                return "instanceof";
            default:
                throw new AnalysisException("Unexpected operator");
        }
    }

    @Override
    public String toString() {
        int resultReg = getResultRegister();
        return operatorToString() + "[v" + arg1_reg + ",v" + arg2_reg + "," + (resultReg == NO_VALUE ? "-" : ("v" + resultReg)) + "]";
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
        if (arg1_reg == NO_VALUE)
            throw new AnalysisException("Invalid left argument: " + this);
        if (arg2_reg == NO_VALUE)
            throw new AnalysisException("Invalid right argument: " + this);
    }
}
