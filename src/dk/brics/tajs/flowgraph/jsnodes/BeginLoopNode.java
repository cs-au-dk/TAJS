/*
 * Copyright 2009-2018 Aarhus University
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
 * Begin-loop node.
 * <p>
 * Placed just before the loop branch.
 */
public class BeginLoopNode extends Node {

    private final IfNode ifNode;

    /**
     * True iff the loop is nested inside another loop in the same function body.
     */
    private final boolean nested;

    /**
     * Constructs a new begin-loop node.
     */
    public BeginLoopNode(IfNode ifNode, boolean nested, SourceLocation sourceLocation) {
        super(sourceLocation);
        this.ifNode = ifNode;
        this.nested = nested;
        setArtificial();
    }

    /**
     * Returns the loop branch node.
     */
    public IfNode getIfNode() {
        return ifNode;
    }

    @Override
    public String toString() {
        return "begin-loop[v" + ifNode.getConditionRegister() + "]";
    }

    @Override
    public boolean canThrowExceptions() {
        return false;
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }

    /**
     * Returns true iff the loop is nested inside another loop in the same function body.
     */
    public boolean isNested() {
        return nested;
    }
}
