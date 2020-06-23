/*
 * Copyright 2009-2020 Aarhus University
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
public interface NodeVisitor {

    /**
     * Visits a BinaryOperatorNode.
     */
    void visit(BinaryOperatorNode n);

    /**
     * Visits a CallNode.
     */
    void visit(CallNode n);

    /**
     * Visits a CatchNode.
     */
    void visit(CatchNode n);

    /**
     * Visits a ConstantNode.
     */
    void visit(ConstantNode n);

    /**
     * Visits a DeletePropertyNode.
     */
    void visit(DeletePropertyNode n);

    /**
     * Visits a BeginWithNode.
     */
    void visit(BeginWithNode n);

    /**
     * Visits an ExceptionalReturnNode.
     */
    void visit(ExceptionalReturnNode n);

    /**
     * Visits a DeclareFunctionNode.
     */
    void visit(DeclareFunctionNode n);

    /**
     * Visits a BeginForInNode.
     */
    void visit(BeginForInNode n);

    /**
     * Visits an IfNode.
     */
    void visit(IfNode n);

    /**
     * Visits an EndWithNode.
     */
    void visit(EndWithNode n);

    /**
     * Visits a NewObjectNode.
     */
    void visit(NewObjectNode n);

    /**
     * Visits a NextPropertyNode.
     */
    void visit(NextPropertyNode n);

    /**
     * Visits a HasNextPropertyNode.
     */
    void visit(HasNextPropertyNode n);

    /**
     * Visits a NopNode.
     */
    void visit(NopNode n);

    /**
     * Visits a ReadPropertyNode.
     */
    void visit(ReadPropertyNode n);

    /**
     * Visits a ReadVariableNode.
     */
    void visit(ReadVariableNode n);

    /**
     * Visits a ReturnNode.
     */
    void visit(ReturnNode n);

    /**
     * Visits a ThrowNode.
     */
    void visit(ThrowNode n);

    /**
     * Visits a UnaryOperatorNode.
     */
    void visit(UnaryOperatorNode n);

    /**
     * Visits a DeclareVariableNode.
     */
    void visit(DeclareVariableNode n);

    /**
     * Visits a WritePropertyNode.
     */
    void visit(WritePropertyNode n);

    /**
     * Visits a WriteVariableNode.
     */
    void visit(WriteVariableNode n);

    /**
     * Visits an EventDispatcherNode.
     */
    void visit(EventDispatcherNode n);

    /**
     * Visits an EndForInNode.
     */
    void visit(EndForInNode n);

    /**
     * Visits a BeginLoopNode.
     */
    void visit(BeginLoopNode n);

    /**
     * Visits an EndLoopNode.
     */
    void visit(EndLoopNode n);
}
