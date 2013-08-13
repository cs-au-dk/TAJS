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
import dk.brics.tajs.flowgraph.ICallNode;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;

/**
 * Call to toString or valueOf conversion.
 * <p>
 * Must be the only node in its block. The block must have precisely one successor.
 */
public class CallConversionNode extends LoadNode implements ICallNode { // TODO: implement on-the-fly toString/valueOf insertion
	
	public enum Kind {
		TOSTRING,
		VALUEOF
	}

    public enum Sequence {
        FIRST,
        SECOND
    }
	
	private Kind kind;

    private Sequence sequence_number;
	
	private int base_reg;
	
    /**
     * Constructs a new conversion call node.
     * @param kind The conversion kind.
     * @param result_reg The register for the result.
     * @param base_reg The base object register.
     * @param location The source location.
     */
    public CallConversionNode(Kind kind, Sequence sequence_number, int result_reg, int base_reg, SourceLocation location) {
        super(result_reg, location);
        this.kind = kind;
        this.base_reg = base_reg;
        this.sequence_number = sequence_number;
    }

    /**
     * Returns the kind.
     */
	public Kind getKind() {
    	return kind;
	}

    /**
     * Returns the sequence number.
     */
    public Sequence getSequenceNumber() {
        return sequence_number;
    }

    /**
     * Returns the base register.
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

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("conversion");
		switch (kind) {
		case TOSTRING:
			b.append("-tostring");
			break;
		case VALUEOF:
			b.append("-valueof");
			break;
		}
		b.append('[').append(sequence_number == Sequence.FIRST ? 1 : 2).append(",v").append(base_reg).append(",").append("v").append(getResultRegister()).append("]");
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
        if (base_reg == NO_VALUE)
            throw new AnalysisException("No base register for call conversion node: " + toString());
        if (getResultRegister() == NO_VALUE)
            throw new AnalysisException("No result register for call conversion node: " + toString());
        if (b.getSuccessors().size() > 1)
            throw new AnalysisException("More than one successor for call conversion node block: " + b.toString());
	}

	@Override
	public boolean isConstructorCall() {
		return false;
	}
}
