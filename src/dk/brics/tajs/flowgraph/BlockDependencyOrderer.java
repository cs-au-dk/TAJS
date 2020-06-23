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

package dk.brics.tajs.flowgraph;

import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.util.AnalysisException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Orders blocks according to their dependencies.
 *
 * @see #produceDependencyOrder(Collection, Set, List, boolean)
 */
public class BlockDependencyOrderer {

    private final Set<BasicBlock> ignored;

    private final Set<BasicBlock> temporarilyMarked;

    private final Set<BasicBlock> notPermanentlyMarked;

    private final List<BasicBlock> sorted;

    private Stack<Task> worklist;

    private boolean delayFirstBlockInLoopBody;

    private BlockDependencyOrderer(Collection<BasicBlock> blocks, Set<BasicBlock> ignored, List<BasicBlock> rootOrder, boolean delayFirstBlockInLoopBody) {
        // TODO consider the amount of reversals in this implementation, some of them might cancel each other out!
        this.ignored = ignored;
        this.notPermanentlyMarked = newSet(blocks);
        this.temporarilyMarked = newSet();
        this.sorted = newList();
        this.worklist = new Stack<>();
        this.delayFirstBlockInLoopBody = delayFirstBlockInLoopBody;
        // Implementation choice in topological sort: process roots only, and in reverse order
        List<BasicBlock> reverseRootOrder = newList(rootOrder);
        Collections.reverse(reverseRootOrder);
        for (BasicBlock root : reverseRootOrder) {
            worklist.push(new ProcessBlock(root));
            while (!worklist.isEmpty()) {
                worklist.pop().process();
            }
        }
        if (!notPermanentlyMarked.isEmpty()) {
            // sanity check that it is ok to iterate roots instead of "notPermanentlyMarked"
            throw new AnalysisException("Bad topological sort implementation");
        }
        Collections.reverse(sorted); // the list has been built backwards...
    }

    /**
     * Produces a topological sorting of blocks with a depth-first search that ignores cycles.
     * Algorithm: wikipedia on topological sorting with depth-first (Cormen2001/Tarjan1976).
     * - slightly modified to produce prettier orders
     * - rewritten to iterative form (with callbacks) to avoid blowing the call stack
     */
    public static List<BasicBlock> produceDependencyOrder(Collection<BasicBlock> blocks, Set<BasicBlock> ignored, List<BasicBlock> rootOrder, boolean delayFirstBlockInLoopBody) {
        return new BlockDependencyOrderer(blocks, ignored, rootOrder, delayFirstBlockInLoopBody).sorted;
    }

    private interface Task {

        void process();
    }

    private class ProcessBlock implements Task {

        private final BasicBlock block;

        public ProcessBlock(BasicBlock block) {
            this.block = block;
        }

        @Override
        public void process() {
            if (temporarilyMarked.contains(block)) {
                return;
            }
            if (notPermanentlyMarked.contains(block)) {
                temporarilyMarked.add(block);
                List<BasicBlock> successors = newList(block.getSuccessors());

                // Implementation choice in topological sort: disambiguate the following ambiguous successors in specific order:
                // - true-branches before false-branches: ensures loop bodies are processed completely before exiting the loop
                // - ordinary flow, then exceptional (no good reason right now)

                if (successors.size() > 1 && block.getLastNode() instanceof IfNode) {
                    IfNode ifNode = (IfNode) block.getLastNode();
                    successors.sort((succ1, succ2) -> {
                        if (succ1 == ifNode.getSuccTrue() && succ2 == ifNode.getSuccFalse()) {
                            return -1;
                        }
                        if (succ2 == ifNode.getSuccTrue() && succ1 == ifNode.getSuccFalse()) {
                            return -1;
                        }
                        return 0;
                    });
                }

                if (block.getExceptionHandler() != null) {
                    successors.add(block.getExceptionHandler());
                }
                successors.removeAll(ignored);
                boolean delay = delayFirstBlockInLoopBody && block.getFirstNode().isLoopEntryNode();
                if (!delay) {
                    worklist.push(new PostProcessBlock(block)); // post-process needs to be below the processing of the current block's successors - except for the first basic block in a loop if delayFirstBlockInLoopBody is set
                }
                Collections.reverse(successors); // reverse to get the right stack order
                for (BasicBlock m : successors) {
                    worklist.push(new ProcessBlock(m));
                }
                if (delay) { // if delayFirstBlockInLoopBody is set, the first basic block in a loop should be processed (immediately) after the rest of the loop body
                    worklist.push(new PostProcessBlock(block));
                }
            }
        }
    }

    private class PostProcessBlock implements Task {

        private final BasicBlock block;

        public PostProcessBlock(BasicBlock block) {
            this.block = block;
        }

        @Override
        public void process() {
            notPermanentlyMarked.remove(block);
            temporarilyMarked.remove(block);
            sorted.add(block);
        }
    }
}
