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

/**
 * Read property node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = <i>v</i><sub><i>base</i></sub>.<i>property</i></sub><br>
 * or<br>
 * <i>v</i><sub><i>result</i></sub> = <i>v</i><sub><i>base</i></sub>[<i>v</i><sub><i>property</i></sub>]<br>
 * <p>
 * Note that reading a property may overwrite the base register due to ToObject coercion.
 */
public class ReadPropertyNode extends LoadNode implements IPropertyNode {

	private int base_reg;

	private int property_reg = NO_VALUE;
	
	private String property_str;
	
	/**
	 * Constructs a new read property node with variable property name.
	 */
	public ReadPropertyNode(int base_reg, int property_reg, int result_reg, SourceLocation location) {
		super(result_reg, location);
		this.base_reg = base_reg;
		this.property_reg = property_reg;
	}

	/**
	 * Constructs a new read property node with fixed property name.
	 */
	public ReadPropertyNode(int base_reg, String property_str, int result_reg, SourceLocation location) {
		super(result_reg, location);
		this.base_reg = base_reg;
		this.property_str = property_str;
	}

	@Override
    public int getBaseRegister() {
		return base_reg;
	}

    @Override
    public void setBaseRegister(int base_reg) {
        this.base_reg = base_reg;
    }

	@Override
    public int getPropertyRegister() {
		return property_reg;
	}

    @Override
    public void setPropertyRegister(int property_reg) {
        this.property_reg = property_reg;
    }
	
	@Override
    public String getPropertyString() {
		return property_str;
	}
	
	@Override
    public void setPropertyString(String property_str) {
        this.property_str = property_str;
    }
	
	@Override
    public boolean isPropertyFixed() {
		return property_str != null;
	}

	@Override
	public String toString() {
        return "read-property[v" + base_reg 
		+ (property_str != null ? ",'" + property_str + "'" : ",v" + property_reg) 
		+ "," + (getResultRegister() == NO_VALUE ? "-" : ("v" + getResultRegister())) + "]";
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
        if (base_reg == NO_VALUE)
            throw new AnalysisException("Base register is NO_VALUE:" + toString());
        if (property_reg == NO_VALUE && property_str == null)
            throw new AnalysisException("Both property register and property string are undefined: " + toString());
    }
}
