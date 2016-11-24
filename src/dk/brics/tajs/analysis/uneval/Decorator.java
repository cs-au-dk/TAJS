/*
 * Copyright 2009-2016 Aarhus University
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

package dk.brics.tajs.analysis.uneval;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.util.Collections;

import java.util.Map;
import java.util.Set;

public class Decorator {

    private Map<BasicBlock, Set<BasicBlock>> predecessorBlocks = Collections.newMap();

    public Decorator(Function function) {
        for (BasicBlock predecessor : function.getBlocks()) {
            for (BasicBlock successor : predecessor.getSuccessors()) {
                    /* OLD IMPLEMENTATION, by ???:
                    predecessorBlocks.get(successor).add(predecessor);
                     */
                    /* NEW IMPLEMENTATION, by @esbena:
                        @esbena: the unevalizer is treated as a black box due to its code quality,
                        This fix makes some sense, but I am not sure exactly why it is needed.
                         (the fix prevents a StackOverflowError)
                     */
                // the flowgraph block order forms a topological ordering: loop back edges can be identified easily
                boolean isLoopBackEdge = predecessor.getOrder() > successor.getOrder();
                // the unevalizer mechanisms does not need to consider loop back edges
                if (!isLoopBackEdge) {
                    Collections.addToMapSet(predecessorBlocks, successor, predecessor);
                }
            }
        }
    }

    /**
     * Returns the set of basic blocks occurring immediately before the given basic block.
     */
    @SuppressWarnings("unchecked")
    public Set<BasicBlock> getPredecessorBlocks(BasicBlock basicBlock) {
        return predecessorBlocks.getOrDefault(basicBlock, java.util.Collections.EMPTY_SET);
    }
}
