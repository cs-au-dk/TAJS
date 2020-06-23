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

package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.AbstractNode;

/**
 * Pair of an abstract node and a context.
 */
public final class NodeAndContext<ContextType extends IContext<?>> {

    private AbstractNode n;

    private ContextType c;

    /**
     * Constructs a new pair.
     */
    public NodeAndContext(AbstractNode n, ContextType c) {
        this.n = n;
        this.c = c;
    }

    /**
     * Returns the node.
     */
    public AbstractNode getNode() {
        return n;
    }

    /**
     * Returns the context.
     */
    public ContextType getContext() {
        return c;
    }

    /**
     * Checks whether this object and the given object are equal.
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NodeAndContext))
            return false;
        NodeAndContext<ContextType> ncp = (NodeAndContext<ContextType>) obj;
        return ncp.n == n && ncp.c.equals(c);
    }

    /**
     * Computes the hash code for this object.
     */
    @Override
    public int hashCode() {
        return n.getIndex() * 13 + c.hashCode() * 3;
    }

    @Override
    public String toString() {
        return "node " + n.getIndex() + ", context " + c;
    }
}
