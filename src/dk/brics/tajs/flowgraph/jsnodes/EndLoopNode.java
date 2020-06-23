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

import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * End-loop node.
 * <p>
 * Placed at every exit of the loop (for example, immediately after the false branch).
 */
public class EndLoopNode extends Node {

    private final BeginLoopNode beginNode;

    /**
     * Constructs a new end-loop node.
     */
    public EndLoopNode(BeginLoopNode beginNode, SourceLocation location) {
        super(location);
        this.beginNode = beginNode;
        setArtificial();
    }

    @Override
    public String toString() {
        return "end-loop[v" + beginNode.getIfNode().getConditionRegister() + "]";
    }

    @Override
    public boolean canThrowExceptions() {
        return false;
    }

    /**
     * Returns the associated begin-loop node.
     */
    public BeginLoopNode getBeginNode() {
        return beginNode;
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }
}
