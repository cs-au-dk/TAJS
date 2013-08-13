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
 * Begin for-in node.
 * <p>
 * for (<i>v</i><sub><i>property</i></sub> in <i>v</i><sub><i>object</i></sub>) { ... }
 * <p>
 * The block must have precisely one successor.
 */
public class BeginForInNode extends Node {

	private int object_reg;

	private int propertylist_reg;
	
	/**
     * End node for this block.
     */
    private EndForInNode end_node;

	/**
	 * Constructs a new begin for-in node.
	 */
	public BeginForInNode(int object_reg, int propertylist_reg, SourceLocation location) {
		super(location);
		this.object_reg = object_reg;
		this.propertylist_reg = propertylist_reg;
	}
	
	/**
	 * Sets the end node for this block.
	 */
	public void setEndNode(EndForInNode end_node) {
		this.end_node = end_node;
	}
	
	/**
	 * Returns the end node for this block.
	 */
	public EndForInNode getEndNode() {
		return end_node;
	}

	/**
	 * Returns the object register.
	 */
	public int getObjectRegister() {
		return object_reg;
	}

    /**
     * Sets the object register.
     */
    public void setObjectRegister(int object_reg) {
        this.object_reg = object_reg;
    }
	
	/**
	 * Returns the property list register.
	 */
	public int getPropertyListRegister() {
		return propertylist_reg;
	}
	
	@Override
	public String toString() {
		return "begin-for-in[v" + object_reg + ",v" + propertylist_reg + "]";
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
        if (b.getSuccessors().size() > 1)
            throw new AnalysisException("More than one successor for begin-for-in node block: " + b.toString());
        if (object_reg == NO_VALUE)
            throw new AnalysisException("Invalid object register: " + toString());
    }
}
