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

package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.BasicBlock;

/**
 * Pair of a basic block and a context.
 */
public final class BlockAndContext<ContextType extends IContext<?>> {

    private BasicBlock b;

    private ContextType c;

    /**
     * Constructs a new pair.
     */
    public BlockAndContext(BasicBlock b, ContextType c) {
        this.b = b;
        this.c = c;
    }

    /**
     * Returns the block.
     */
    public BasicBlock getBlock() {
        return b;
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
        if (!(obj instanceof BlockAndContext))
            return false;
        BlockAndContext<ContextType> fcp = (BlockAndContext<ContextType>) obj;
        return fcp.b == b && fcp.c.equals(c);
    }

    /**
     * Computes the hash code for this object.
     */
    @Override
    public int hashCode() {
        return b.getIndex() * 13 + c.hashCode() * 3;
    }

    @Override
    public String toString() {
        return "block " + b.getIndex() + ", context " + c;
    }
}
