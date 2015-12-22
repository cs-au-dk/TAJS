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

import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;

/**
 * Constant node.
 * <p>
 * <i>v</i> = <i>constant</i>
 */
public class ConstantNode extends LoadNode {

    /**
     * The different kinds of constant values.
     */
    public enum Type {

        /**
         * Number.
         */
        NUMBER,

        /**
         * String.
         */
        STRING,

        /**
         * Boolean.
         */
        BOOLEAN,

        /**
         * Undefined.
         */
        UNDEFINED,

        /**
         * Null.
         */
        NULL
    }

    private Type type;

    private double number;

    private String string;

    private boolean bool;

    private ConstantNode(Type type, double number, String string, boolean bool, int result_reg, SourceLocation location) {
        super(result_reg, location);
        this.type = type;
        this.number = number;
        this.string = string;
        this.bool = bool;
    }

    /**
     * Constructs a new constant number node.
     *
     * @param number     The number.
     * @param result_reg The register to store the number in.
     * @param location   The source location.
     * @return Returns a new node representing the number.
     */
    public static ConstantNode makeNumber(double number, int result_reg, SourceLocation location) {
        return new ConstantNode(Type.NUMBER, number, null, false, result_reg, location);
    }

    /**
     * Constructs a new constant string node.
     *
     * @param string     The string.
     * @param result_reg The register to store the string in.
     * @param location   The source location.
     * @return Returns a new node representing the string.
     */
    public static ConstantNode makeString(String string, int result_reg, SourceLocation location) {
        return new ConstantNode(Type.STRING, 0.0d, string, false, result_reg, location);
    }

    /**
     * Constructs a new constant boolean node.
     *
     * @param bool       The boolean.
     * @param result_reg The register to store the boolean in.
     * @param location   The source location.
     * @return Returns a new node representing the boolean.
     */
    public static ConstantNode makeBoolean(boolean bool, int result_reg, SourceLocation location) {
        return new ConstantNode(Type.BOOLEAN, 0.0d, null, bool, result_reg, location);
    }

    /**
     * Constructs a new constant 'null' node.
     *
     * @param result_reg The register to store the 'null' in.
     * @param location   The source location.
     * @return Returns a new node representing the null.
     */
    public static ConstantNode makeNull(int result_reg, SourceLocation location) {
        return new ConstantNode(Type.NULL, 0.0d, null, false, result_reg, location);
    }

    /**
     * Constructs a new constant 'undefined' node.
     *
     * @param result_reg The register to store the undefined in.
     * @param location   The source location.
     * @return Returns a new node representing the undefined.
     */
    public static ConstantNode makeUndefined(int result_reg, SourceLocation location) {
        return new ConstantNode(Type.UNDEFINED, 0.0d, null, false, result_reg, location);
    }

    /**
     * Returns the type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the number (for Type.NUMBER only).
     */
    public double getNumber() {
        return number;
    }

    /**
     * Returns the string (for Type.STRING only).
     */
    public String getString() {
        return string;
    }

    /**
     * Returns the boolean (for Type.BOOLEAN only).
     */
    public boolean getBoolean() {
        return bool;
    }

    /**
     * Converts the data to a human readable form.
     */
    private String valueToString() {
        switch (type) {
            case NUMBER:
                return Double.toString(number);
            case STRING:
                return "\"" + Strings.escape(string) + "\"";
            case BOOLEAN:
                return Boolean.toString(bool);
            case NULL:
                return "null";
            case UNDEFINED:
                return "undefined";
            default:
                throw new AnalysisException("Unexpected type: " + type);
        }
    }

    @Override
    public String toString() {
        int resultReg = getResultRegister();
        return "constant[" + valueToString() + "," + (resultReg == NO_VALUE ? "-" : ("v" + resultReg)) + "]";
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean canThrowExceptions() {
        return false;
    }
}
