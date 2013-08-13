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

/**
 * Visitor for nodes.
 */
public interface NodeVisitor<ArgType> {

	/**
	 * Visits an AssumeNode.
	 */
	public void visit(AssumeNode n, ArgType a);

	/**
	 * Visits a BinaryOperatorNode.
	 */
	public void visit(BinaryOperatorNode n, ArgType a);

	/**
	 * Visits a CallNode.
	 */
	public void visit(CallNode n, ArgType a);

	/**
	 * Visits a CallNode.
	 */
	public void visit(CallConversionNode n, ArgType a);

	/**
	 * Visits a CatchNode.
	 */
	public void visit(CatchNode n, ArgType a);

	/**
	 * Visits a ConstantNode.
	 */
	public void visit(ConstantNode n, ArgType a);

	/**
	 * Visits a DeletePropertyNode.
	 */
	public void visit(DeletePropertyNode n, ArgType a);

	/**
	 * Visits an BeginWithNode.
	 */
	public void visit(BeginWithNode n, ArgType a);

	/**
	 * Visits an ExceptionalReturnNode.
	 */
	public void visit(ExceptionalReturnNode n, ArgType a);

	/**
	 * Visits a DeclareFunctionNode.
	 */
	public void visit(DeclareFunctionNode n, ArgType a);

	/**
	 * Visits a BeginForInNode.
	 */
	public void visit(BeginForInNode n, ArgType a);

	/**
	 * Visits an IfNode.
	 */
	public void visit(IfNode n, ArgType a);

	/**
	 * Visits a EndWithNode.
	 */
	public void visit(EndWithNode n, ArgType a);

	/**
	 * Visits a NewObjectNode.
	 */
	public void visit(NewObjectNode n, ArgType a);

	/**
	 * Visits a NextPropertyNode.
	 */
	public void visit(NextPropertyNode n, ArgType a);
	
	/**
	 * Visits a HasNextPropertyNode.
	 */
	public void visit(HasNextPropertyNode n, ArgType a);

	/**
	 * Visits a NopNode.
	 */
	public void visit(NopNode n, ArgType a);

	/**
	 * Visits a ReadPropertyNode.
	 */
	public void visit(ReadPropertyNode n, ArgType a);

	/**
	 * Visits a ReadVariableNode.
	 */
	public void visit(ReadVariableNode n, ArgType a);

	/**
	 * Visits a ReturnNode.
	 */
	public void visit(ReturnNode n, ArgType a);

	/**
	 * Visits a ThrowNode.
	 */
	public void visit(ThrowNode n, ArgType a);

	/**
	 * Visits a TypeofNode.
	 */
	public void visit(TypeofNode n, ArgType a);

	/**
	 * Visits a UnaryOperatorNode.
	 */
	public void visit(UnaryOperatorNode n, ArgType a);

	/**
	 * Visits a DeclareVariableNode.
	 */
	public void visit(DeclareVariableNode n, ArgType a);

	/**
	 * Visits a WritePropertyNode.
	 */
	public void visit(WritePropertyNode n, ArgType a);

	/**
	 * Visits a WriteVariableNode.
	 */
	public void visit(WriteVariableNode n, ArgType a);

	/**
	 * Visits an EventEntryNode.
	 */
	public void visit(EventEntryNode n, ArgType a);

	/**
	 * Visits an EventDispatcherNode.
	 */
	public void visit(EventDispatcherNode n, ArgType a);

    /**
     * Visits a EndForInNode.
     */
    public void visit(EndForInNode n, ArgType a);
}
