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

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;

/**
 * Assume node.
 */
public class AssumeNode extends Node { // TODO: split different kinds into separate subclasses?
	
	/**
	 * The different kinds of assume node.
	 */
	public enum Kind {
		
		/**
		 * Variable value is not null/undefined.
		 */
		VARIABLE_NON_NULL_UNDEF,
		
		/**
		 * Property value is not null/undefined.
		 */
		PROPERTY_NON_NULL_UNDEF,
		
		/**
		 * No flow reaches this location
		 */ 
		UNREACHABLE, 
		
		/**
		 * A variable has a known to-boolean value.
		 */
		VARIABLE_BOOLY, 

		/**
		 * A property has a known to-boolean value.
		 */
		PROPERTY_BOOLY
		// TODO: other assumption kinds?
	}
	
	private Kind kind;
	
	private String varname;
	
	private int base_reg = NO_VALUE;

	private int property_reg = NO_VALUE;
	
	private String property_str;
	
	private boolean booly_value; 
	
	private ReadPropertyNode access_node;
	
	private AssumeNode(Kind kind, SourceLocation location) {
		super(location);
		this.kind = kind;
		setArtificial();
	}

    /**
     * Constructs a new assume node for "variable value is not null/undefined".
     *
     * @param varname The name of the variable.
     * @param location The source location for the variable.
     * @return A new assume node for varname at location.
     */
	public static AssumeNode makeVariableNonNullUndef(String varname, SourceLocation location) {
		AssumeNode n = new AssumeNode(Kind.VARIABLE_NON_NULL_UNDEF, location);
		n.varname = varname;
		return n;
	}

    /**
     * Constructs a new assume node for "property value is not null/undefined" with variable property.
     *
     * @param access_node The access node.
     * @return A new assume node for property value not null/undefined.
     */
	public static AssumeNode makePropertyNonNullUndef(ReadPropertyNode access_node) {
        // Doesn't make sense to insert property non null undef unless access_node is a read or write property node.
		AssumeNode an = new AssumeNode(Kind.PROPERTY_NON_NULL_UNDEF, access_node.getSourceLocation());
		an.base_reg = access_node.getBaseRegister();
        if (access_node.isPropertyFixed())
            an.property_str = access_node.getPropertyString();
        else 
		    an.property_reg = access_node.getPropertyRegister();
		an.access_node = access_node;
		return an;
	}

	/**
	 * Returns the assume node kind.
	 */
	public Kind getKind() {
		return kind;
	}

	/**
	 * Returns the source variable name.
	 */
	public String getVariableName() {
		return varname;
	}

	/**
	 * Returns the base register, or {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} if not applicable.
	 */
	public int getBaseRegister() {
		return base_reg;
	}

    /**
     * Sets the base register.
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
	 * Returns the property string, or null if not fixed.
	 */
	public String getPropertyString() {
		return property_str;
	}
	
	/**
	 * Returns the node where the property access occurred.
	 */
	public AbstractNode getAccessNode() {
		return access_node;
	}
	
	/**
	 * Returns true if the property is a fixed string.
	 */
	public boolean isPropertyFixed() {
		return property_str != null;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		switch (kind) {
		case VARIABLE_NON_NULL_UNDEF: {
				b.append("<variable-non-null-undef>['").append(Strings.escape(varname)).append("']");
				break;
		}
		case PROPERTY_NON_NULL_UNDEF: {
				b.append("<property-non-null-undef>[v").append(base_reg);
				if (property_str != null)
					b.append(",\'").append(Strings.escape(property_str)).append('\'');
				else
					b.append(",v").append(property_reg);
				b.append(']');
				break;
		}
		case UNREACHABLE: {
			b.append("<unreachable>");
			break;
		}
		case VARIABLE_BOOLY:{
			b.append("<variable-"+booly_value+">['").append(Strings.escape(varname)).append("']");
			break;
		}
		case PROPERTY_BOOLY:{
			b.append("<property-" + booly_value + ">[v").append(base_reg);
			if (property_str != null)
				b.append(",\'").append(Strings.escape(property_str)).append('\'');
			else
				b.append(",v").append(property_reg);
			b.append(']');
			break;
		}		

		default:
			throw new UnsupportedOperationException("Unhandled enum: " + kind);
		}
		return b.toString();
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
        if (kind  == Kind.VARIABLE_NON_NULL_UNDEF && varname == null)
            throw new AnalysisException("Variable non null undef with null variable name: " + toString());
        if (kind  == Kind.PROPERTY_NON_NULL_UNDEF && base_reg == NO_VALUE)
            throw new AnalysisException("Property non null undef with no base object: " + toString());
        if (kind  == Kind.PROPERTY_NON_NULL_UNDEF && property_reg == NO_VALUE && property_str == null)
            throw new AnalysisException("Both property register and property string are undefined: " + toString());
        if (kind  == Kind.VARIABLE_BOOLY && varname == null)
            throw new AnalysisException("Variable with null variable name: " + toString());
        if (kind  == Kind.PROPERTY_BOOLY && base_reg == NO_VALUE)
            throw new AnalysisException("Property with no base object: " + toString());
        if (kind  == Kind.PROPERTY_BOOLY && property_reg == NO_VALUE && property_str == null)
            throw new AnalysisException("Both property register and property string are undefined: " + toString());
    }

	public static AssumeNode makeUnreachable(SourceLocation srcLoc){
		return new AssumeNode(Kind.UNREACHABLE, srcLoc);	
	}

	public static AbstractNode makeVariableBooly(SourceLocation srcLoc, String variableName, boolean booly) {
		final AssumeNode assumeNode = new AssumeNode(Kind.VARIABLE_BOOLY, srcLoc);
		assumeNode.varname = variableName;
		assumeNode.booly_value = booly;
		return assumeNode;
	}
	
	public boolean getBoolyValue(){
		return booly_value;
	}

	public static AbstractNode makePropertyBooly(ReadPropertyNode node, boolean booly) {
		AssumeNode assume = new AssumeNode(Kind.PROPERTY_BOOLY, node.getSourceLocation());
		assume.booly_value = booly;
		assume.base_reg = node.getBaseRegister();
        if (node.isPropertyFixed())
            assume.property_str = node.getPropertyString();
        else 
		    assume.property_reg = node.getPropertyRegister();
		assume.access_node = node;
		return assume;
    }
}
