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

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;

/**
 * Delete property node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = delete <i>v</i><sub><i>base</i></sub>[<i>v</i><sub><i>property</i></sub>]<br>
 * or<br>
 * <i>v</i><sub><i>result</i></sub> = delete <i>v</i><sub><i>base</i></sub>.<i>property</i><br>
 * or<br>
 * <i>v</i><sub><i>result</i></sub> = delete <i>x</i> (for variables)
 */
public class DeletePropertyNode extends LoadNode {

	private int base_reg = NO_VALUE;

	private int property_reg = NO_VALUE;
	
	private String property_str;
	
	private String varname;

	/**
	 * Constructs a new delete property node for a reference with variable property name.
	 */
	public DeletePropertyNode(int base_reg, int property_reg, int result_reg, SourceLocation location) {
		super(result_reg, location);
		this.base_reg = base_reg;
		this.property_reg = property_reg;
	}

	/**
	 * Constructs a new delete property node for a reference with fixed property name.
	 */
	public DeletePropertyNode(int base_reg, String property_str, int result_reg, SourceLocation location) { 
		super(result_reg, location);
		this.base_reg = base_reg;
		this.property_str = property_str;
	}

	/**
	 * Constructs a new delete property node for a variable.
	 */
	public DeletePropertyNode(String varname, int result_reg, SourceLocation location) {
		super(result_reg, location);
		this.varname = varname;
	}

	/**
	 * Returns the base register, or {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} if not applicable.
	 */
	public int getBaseRegister() {
		return base_reg;
	}

    /**
     * Sets the base register
     */
    public void setBaseRegister(int base_reg) {
        this.base_reg = base_reg;
    }

	/**
	 * Returns the property register, or {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} if not applicable.
	 */
	public int getPropertyRegister() {
		return property_reg;
	}

    /**
     * Sets the property register.
     */
    public void setPropertyRegister(int property_reg) {
        this.property_reg = property_reg;
    }
	
	/**
	 * Returns the property string, or null if not fixed or not a reference.
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
	 * Returns the source variable name, or null if not a variable.
	 */
	public String getVariableName() {
		return varname;
	}
	
	/**
	 * Returns true if the argument is a variable, false if it is an ordinary reference.
	 */
	public boolean isVariable() {
		return varname != null;
	}

	@Override
	public String toString() {
        int resultReg = getResultRegister();
        return "delete-property["
                + (varname != null ? "'" + Strings.escape(varname) + "'" :
                (base_reg == NO_VALUE ? "-" : ("v" + base_reg))
                        + (property_str != null ? ",'" + property_str + "'" : ",v" + property_reg))
                + "," + (resultReg == NO_VALUE ? "-" : ("v" + resultReg)) + "]";
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
        if (property_reg == NO_VALUE && property_str == null && (varname == null || varname.isEmpty()))
            throw new AnalysisException("Property register, property string and variable name are all undefined: " + toString());
    }
}
