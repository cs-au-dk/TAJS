/*
 * Copyright 2009-2017 Aarhus University
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
import dk.brics.tajs.flowgraph.AbstractNodeVisitor;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Abstract base class for all ordinary flow graph nodes.
 */
public abstract class Node extends AbstractNode {

    /**
     * Constructs a new node.
     */
    public Node(SourceLocation location) {
        super(location);
    }

    /**
     * Visits this node with the given visitor.
     */
    public abstract void visitBy(NodeVisitor v);

    @Override
    public void visitBy(AbstractNodeVisitor v) {
        v.visit(this);
    }
}
