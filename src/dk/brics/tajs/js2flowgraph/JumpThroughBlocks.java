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

package dk.brics.tajs.js2flowgraph;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.util.Pair;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;

import static dk.brics.tajs.util.Collections.newList;

/**
 * Basic blocks used for modeling flow through some blocks on jumps from continue, break or return. (Exceptional jumps are handled elsewhere.)
 * Each such jump should propagate flow to the entry and propagate flow from the exit to the actual jump target.
 * Knowledge of all blocks to jump through is required because of the need for cloning.
 */
class JumpThroughBlocks {

    private final BasicBlock entry;

    private final BasicBlock exit;

    private final List<BasicBlock> allBlocks;

    /**
     * Convenience constructor for singleton jump-throughs.
     */
    JumpThroughBlocks(BasicBlock singleBlock, FunctionAndBlockManager functionAndBlockManager) {
        this(singleBlock, singleBlock, Collections.singletonList(singleBlock), functionAndBlockManager);
    }

    /**
     * Constructs jump-through blocks as cloned of the given basic blocks.
     */
    JumpThroughBlocks(BasicBlock entry, BasicBlock exit, List<BasicBlock> allBlocks, FunctionAndBlockManager functionAndBlockManager) {
        assert entry != null;
        assert exit != null;
        assert allBlocks != null;
        assert allBlocks.contains(entry);
        assert allBlocks.contains(exit);

        Pair<Pair<BasicBlock, BasicBlock>, List<BasicBlock>> clones = cloneBlocks(entry, exit, allBlocks, functionAndBlockManager);
        this.entry = clones.getFirst().getFirst();
        this.exit = clones.getFirst().getSecond();
        this.allBlocks = clones.getSecond();
    }

    /**
     * Clones the given basic blocks.
     *
     * @return a tuple ((cloned-entry, cloned-exit), cloned-all)
     */
    private static Pair<Pair<BasicBlock, BasicBlock>, List<BasicBlock>> cloneBlocks(BasicBlock entry, BasicBlock exit, List<BasicBlock> all, FunctionAndBlockManager functionAndBlockManager) {
        IdentityHashMap<BasicBlock, BasicBlock> old2new = FunctionBuilderHelper.cloneBlocksAndNodes(all, functionAndBlockManager);
        final List<BasicBlock> cloned = newList();
        for (BasicBlock orig : all) {
            cloned.add(old2new.get(orig));
        }
        return Pair.make(Pair.make(old2new.get(entry), old2new.get(exit)), cloned);
    }

    /**
     * Returns a shallow clone of this object.
     */
    JumpThroughBlocks copy(FunctionAndBlockManager functionAndBlockManager) {
        return new JumpThroughBlocks(entry, exit, allBlocks, functionAndBlockManager);
    }

    /**
     * Returns all the (cloned) blocks.
     */
    List<BasicBlock> getAllBlocks() {
        return allBlocks;
    }

    /**
     * Returns the (cloned) entry block.
     */
    BasicBlock getEntry() {
        return entry;
    }

    /**
     * Returns the (cloned) exit block.
     */
    BasicBlock getExit() {
        return exit;
    }
}
