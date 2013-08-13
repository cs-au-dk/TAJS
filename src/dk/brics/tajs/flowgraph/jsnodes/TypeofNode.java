/*
 * Copyright 2009-2013 Aarhus University
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
import dk.brics.tajs.util.Strings;

/**
 * Typeof node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = typeof <i>v</i> (for syntactic property references)<br>
 * or<br>
 * <i>v</i><sub><i>result</i></sub> = typeof <i>x</i> (for variables)
 */
public class TypeofNode extends LoadNode {

	private int arg_reg = NO_VALUE;
	
	private String varname;

	/**
	 * Constructs a new typeof node for a property reference.
	 */
	public TypeofNode(int arg_reg, int result_reg, SourceLocation location) {
		super(result_reg, location);
		this.arg_reg = arg_reg;
	}

	/**
	 * Constructs a new typeof node for a variable. 
	 */
	public TypeofNode(String varname, int result_reg, SourceLocation location) {
		super(result_reg, location);
		this.varname = varname;
	}

	/**
	 * Returns the argument register, or {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} if not applicable.
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
	 * Returns the source variable name, or null if not a variable.
	 */
	public String getVariableName() {
		return varname;
	}

	/**
	 * Returns true if the argument is a variable, false if it is a property reference.
	 */
	public boolean isVariable() {
		return varname != null;
	}

	@Override
	public String toString() {
        int resultReg = getResultRegister();
		return "typeof[" 
		+ (varname != null ? "'" + Strings.escape(varname) + "'" : "v" + arg_reg ) 
		+ "," + (resultReg == NO_VALUE ? "-" : ("v" + resultReg)) + "]";
	}

	@Override
	public <ArgType> void visitBy(NodeVisitor<ArgType> v, ArgType a) {
		v.visit(this, a);
	}

	@Override
	public boolean canThrowExceptions() {
		return false;
	}

    @Override
    public void check(BasicBlock b) {
        if (varname == null && arg_reg == NO_VALUE)
            throw new AnalysisException("No variable name and no argument register: " + toString());
    }
}
