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

import java.util.List;

/**
 * Call/construct node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = [new] [<i>v</i><sub><i>base</i></sub>.] <i>v</i><sub><i>function</i></sub>(<i>v</i><sub>0</sub>,...,<i>v</i><sub><i>n</i></sub>)
 * <p>
 * Note that <i>v</i><sub><i>function</i></sub> represents the function value (not the property name),
 * and <i>v</i><sub><i>base</i></sub> represents the base object (or {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} if absent).
 * <p>
 * The function may be given as a property name (fixed or computed) instead of a register.
 * <p>
 * Must be the only node in its block. The block must have precisely one successor.
 */
public class CallNode extends LoadNode {

    private boolean constructor;

    private boolean arraylit;

    private int base_reg; // NO_VALUE if absent (i.e. implicitly the global object, used for arraylits and regular expressions)

    private int function_reg = NO_VALUE; // NO_VALUE if property_reg or property_str is used instead

    private int property_reg = NO_VALUE;

    private String property_str; // null if not used

    private int[] arg_regs;

    /**
     * Constructs a new call/construct node.
     *
     * @param constructor  Is this a constructor.
     * @param arraylit     Does this call node come from an array literal
     * @param result_reg   The register for the result.
     * @param base_reg     The base object register.
     * @param function_reg The function register.
     * @param arg_regs     The argument registers as a list.
     * @param location     The source location.
     */
    public CallNode(boolean constructor, boolean arraylit, int result_reg, int base_reg, int function_reg, List<Integer> arg_regs, SourceLocation location) {
        super(result_reg, location);
        this.constructor = constructor;
        this.arraylit = arraylit;
        this.base_reg = base_reg;
        this.function_reg = function_reg;
        this.arg_regs = new int[arg_regs.size()];
        for (int i = 0; i < this.arg_regs.length; i++)
            this.arg_regs[i] = arg_regs.get(i);
    }

    /**
     * Constructs a new call/construct node with a property read.
     *
     * @param constructor  Is this a constructor.
     * @param result_reg   The register for the result.
     * @param base_reg     The base object register.
     * @param property_reg The property register, if not fixed property name.
     * @param property_str The property, if fixed-string property name
     * @param arg_regs     The argument registers as a list.
     * @param location     The source location.
     */
    public CallNode(boolean constructor, int result_reg, int base_reg, int property_reg, String property_str, List<Integer> arg_regs, SourceLocation location) {
        super(result_reg, location);
        this.constructor = constructor;
        this.base_reg = base_reg;
        this.property_reg = property_reg;
        this.property_str = property_str;
        this.arg_regs = new int[arg_regs.size()];
        for (int i = 0; i < this.arg_regs.length; i++)
            this.arg_regs[i] = arg_regs.get(i);
    }

    /**
     * Checks whether this is a constructor call or an ordinary call.
     */
    public boolean isConstructorCall() {
        return constructor;
    }

    /**
     * Returns true if this call node comes from an array literal.
     * This allows us to distinguish between [4] and Array(4) for the case where Array gets precisely one numeric argument.
     */
    public boolean isArrayLiteral() {
        return arraylit;
    }

    /**
     * Returns the base register.
     * Variable number {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} represents absent value (implicitly the global object).
     */
    public int getBaseRegister() {
        return base_reg;
    }

    /**
     * Returns the function register, or {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} if not applicable.
     */
    public int getFunctionRegister() {
        return function_reg;
    }

    /**
     * Returns the property register, or {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} if not applicable.
     */
    public int getPropertyRegister() {
        return property_reg;
    }

    /**
     * Returns the property string, or null if not fixed.
     */
    public String getPropertyString() {
        return property_str;
    }

    /**
     * Returns true if the property is a fixed string.
     */
    public boolean isPropertyFixed() {
        return property_str != null;
    }

    /**
     * Returns the given argument register.
     * Counts from 0.
     */
    public int getArgRegister(int i) {
        return arg_regs[i];
    }

    /**
     * Returns the number of arguments.
     */
    public int getNumberOfArgs() {
        return arg_regs.length;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (constructor)
            b.append("construct");
        else
            b.append("call");
        b.append('[');
        if (base_reg == NO_VALUE)
            b.append('-');
        else
            b.append('v').append(base_reg);
        if (property_str != null)
            b.append(",'").append(property_str).append("'");
        else if (property_reg != NO_VALUE)
            b.append(",[v").append(property_reg).append("]");
        else
            b.append(",v").append(function_reg);
        for (int v : arg_regs)
            b.append(",v").append(v);
        b.append(",");
        int resultReg = getResultRegister();
        if (resultReg == NO_VALUE)
            b.append("-");
        else
            b.append("v").append(resultReg);
        b.append("]");
        return b.toString();
    }

    @Override
    public <ArgType> void visitBy(NodeVisitor<ArgType> v, ArgType a) {
        v.visit(this, a);
    }

    @Override
    public boolean canThrowExceptions() {
        return true;
    }

    @Override
    public void check(BasicBlock b) {
        if (b.getNodes().size() != 1)
            throw new AnalysisException("Node should have its own basic block: " + toString());
        if (b.getSuccessors().size() > 1)
            throw new AnalysisException("More than one successor for call node block: " + b);
    }
}
