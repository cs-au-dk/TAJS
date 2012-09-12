/*
 * Copyright 2012 Aarhus University
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

import java.util.List;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.ICallNode;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;

/**
 * Call/construct node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = [new] [<i>v</i><sub><i>base</i></sub>.] <i>v</i><sub><i>function</i></sub>(<i>v</i><sub>0</sub>,...,<i>v</i><sub><i>n</i></sub>)
 * <p>
 * Note that <i>v</i><sub><i>function</i></sub> represents the function value (not the property name),
 * and <i>v</i><sub><i>base</i></sub> represents the base object (or {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} if absent).
 * <p>
 * Must be the only node in its block. The block must have precisely one successor.
 */
public class CallNode extends LoadNode implements ICallNode {
	
	private boolean constructor;

    private boolean arraylit;

	private int base_reg; // NO_VALUE if absent (i.e. implicitly the global object, used for arraylits and regular expressions)
	
	private int function_reg;

	private int[] arg_regs;
	
    /**
     * Constructs a new call/construct node.
     *
     * @param constructor Is this a constructor.
     * @param arraylit Does this call node come from an array literal
     * @param result_reg The register for the result.
     * @param base_reg The base object register.
     * @param function_reg The function register.
     * @param arg_regs The argument registers as a list.
     * @param location The source location.
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

    @Override
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
     * Sets the base register.
     */
    public void setBaseRegister(int base_var) {
        this.base_reg = base_var;
    }

    /**
     * Returns the function register.
     */
	public int getFunctionRegister() {
		return function_reg;
	}

    /**
     * Sets the function register.
     */
    public void setFunctionRegister(int function_reg) {
        this.function_reg = function_reg;
    }

    /**
     * Returns the given argument register.
     * Counts from 0.
     */
	public int getArgRegister(int i) {
		return arg_regs[i];
	}

    /**
     * Sets the i'th argument to the given argument register.
     * Counts from 0.
     */
    public void setArgRegister(int i, int var) {
        arg_regs[i] = var;
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
        if (function_reg == NO_VALUE)
            throw new AnalysisException("No function register for call node: " + toString());
        if (b.getSuccessors().size() > 1)
            throw new AnalysisException("More than one successor for call node block: " + b.toString());
	}
}
