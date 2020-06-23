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

import dk.brics.tajs.flowgraph.AbstractNodeVisitor;

/**
 * Implementation of {#link NodeVisitor} with empty visitor methods.
 */
public class DefaultNodeVisitor implements NodeVisitor, AbstractNodeVisitor {

    @Override
    public void visit(Node n) {
        n.visitBy((NodeVisitor) this);
    }

    @Override
    public void visit(BinaryOperatorNode n) {
        // empty
    }

    @Override
    public void visit(CallNode n) {
        // empty
    }

    @Override
    public void visit(CatchNode n) {
        // empty
    }

    @Override
    public void visit(ConstantNode n) {
        // empty
    }

    @Override
    public void visit(DeletePropertyNode n) {
        // empty
    }

    @Override
    public void visit(BeginWithNode n) {
        // empty
    }

    @Override
    public void visit(ExceptionalReturnNode n) {
        // empty
    }

    @Override
    public void visit(DeclareFunctionNode n) {
        // empty
    }

    @Override
    public void visit(BeginForInNode n) {
        // empty
    }

    @Override
    public void visit(IfNode n) {
        // empty
    }

    @Override
    public void visit(EndWithNode n) {
        // empty
    }

    @Override
    public void visit(NewObjectNode n) {
        // empty
    }

    @Override
    public void visit(NextPropertyNode n) {
        // empty
    }

    @Override
    public void visit(HasNextPropertyNode n) {
        // empty
    }

    @Override
    public void visit(NopNode n) {
        // empty
    }

    @Override
    public void visit(ReadPropertyNode n) {
        // empty
    }

    @Override
    public void visit(ReadVariableNode n) {
        // empty
    }

    @Override
    public void visit(ReturnNode n) {
        // empty
    }

    @Override
    public void visit(ThrowNode n) {
        // empty
    }

    @Override
    public void visit(UnaryOperatorNode n) {
        // empty
    }

    @Override
    public void visit(DeclareVariableNode n) {
        // empty
    }

    @Override
    public void visit(WritePropertyNode n) {
        // empty
    }

    @Override
    public void visit(WriteVariableNode n) {
        // empty
    }

    @Override
    public void visit(EventDispatcherNode n) {
        // empty
    }

    @Override
    public void visit(EndForInNode n) {
        // empty
    }

    @Override
    public void visit(BeginLoopNode n) {
        // empty
    }

    @Override
    public void visit(EndLoopNode n) {
        // empty
    }
}
