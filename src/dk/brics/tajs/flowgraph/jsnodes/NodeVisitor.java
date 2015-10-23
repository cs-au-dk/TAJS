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

/**
 * Visitor for nodes.
 */
public interface NodeVisitor<ArgType> {

    /**
     * Visits an AssumeNode.
     */
    void visit(AssumeNode n, ArgType a);

    /**
     * Visits a BinaryOperatorNode.
     */
    void visit(BinaryOperatorNode n, ArgType a);

    /**
     * Visits a CallNode.
     */
    void visit(CallNode n, ArgType a);

    /**
     * Visits a CatchNode.
     */
    void visit(CatchNode n, ArgType a);

    /**
     * Visits a ConstantNode.
     */
    void visit(ConstantNode n, ArgType a);

    /**
     * Visits a DeletePropertyNode.
     */
    void visit(DeletePropertyNode n, ArgType a);

    /**
     * Visits a BeginWithNode.
     */
    void visit(BeginWithNode n, ArgType a);

    /**
     * Visits an ExceptionalReturnNode.
     */
    void visit(ExceptionalReturnNode n, ArgType a);

    /**
     * Visits a DeclareFunctionNode.
     */
    void visit(DeclareFunctionNode n, ArgType a);

    /**
     * Visits a BeginForInNode.
     */
    void visit(BeginForInNode n, ArgType a);

    /**
     * Visits an IfNode.
     */
    void visit(IfNode n, ArgType a);

    /**
     * Visits an EndWithNode.
     */
    void visit(EndWithNode n, ArgType a);

    /**
     * Visits a NewObjectNode.
     */
    void visit(NewObjectNode n, ArgType a);

    /**
     * Visits a NextPropertyNode.
     */
    void visit(NextPropertyNode n, ArgType a);

    /**
     * Visits a HasNextPropertyNode.
     */
    void visit(HasNextPropertyNode n, ArgType a);

    /**
     * Visits a NopNode.
     */
    void visit(NopNode n, ArgType a);

    /**
     * Visits a ReadPropertyNode.
     */
    void visit(ReadPropertyNode n, ArgType a);

    /**
     * Visits a ReadVariableNode.
     */
    void visit(ReadVariableNode n, ArgType a);

    /**
     * Visits a ReturnNode.
     */
    void visit(ReturnNode n, ArgType a);

    /**
     * Visits a ThrowNode.
     */
    void visit(ThrowNode n, ArgType a);

    /**
     * Visits a TypeofNode.
     */
    void visit(TypeofNode n, ArgType a);

    /**
     * Visits a UnaryOperatorNode.
     */
    void visit(UnaryOperatorNode n, ArgType a);

    /**
     * Visits a DeclareVariableNode.
     */
    void visit(DeclareVariableNode n, ArgType a);

    /**
     * Visits a WritePropertyNode.
     */
    void visit(WritePropertyNode n, ArgType a);

    /**
     * Visits a WriteVariableNode.
     */
    void visit(WriteVariableNode n, ArgType a);

    /**
     * Visits an EventDispatcherNode.
     */
    void visit(EventDispatcherNode n, ArgType a);

    /**
     * Visits an EndForInNode.
     */
    void visit(EndForInNode n, ArgType a);

    /**
     * Visits a BeginLoopNode.
     */
    void visit(BeginLoopNode n, ArgType a);

    /**
     * Visits an EndLoopNode.
     */
    void visit(EndLoopNode n, ArgType a);
}
