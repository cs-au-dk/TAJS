/*
 * Copyright 2009-2019 Aarhus University
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
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;

/**
 * End-for-in node.
 * <p>
 * Must be the only node in its block.
 */
public class EndForInNode extends Node {

    /**
     * Begin node for this block.
     */
    private BeginForInNode begin_node;

    /**
     * Constructs a new end for-in node.
     */
    public EndForInNode(BeginForInNode begin_node, SourceLocation location) {
        super(location);
        this.begin_node = begin_node;
    }

    @Override
    public boolean canThrowExceptions() {
        return false;
    }

    @Override
    public void check(BasicBlock b) {
        if (b.getNodes().size() != 1)
            throw new AnalysisException("Node should have its own basic block: " + toString());
        if (b.getSuccessors().size() > 1)
            throw new AnalysisException("More than one successor for EndForInNode-block: " + b);
    }

    /**
     * Get the begin node for this block.
     */
    public BeginForInNode getBeginNode() {
        return begin_node;
    }

    @Override
    public boolean isArtificial() {
        return true;
    }

    @Override
    public String toString() {
        return "end-for-in(begin-node:" + begin_node.getIndex() + ")";
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }
}
