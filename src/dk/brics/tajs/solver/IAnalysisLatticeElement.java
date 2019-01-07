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

package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.BasicBlock;

import java.util.Map;

/**
 * Interface for global analysis lattice elements.
 */
public interface IAnalysisLatticeElement<StateType extends IState<StateType, ContextType, CallEdgeType>,
        ContextType extends IContext<ContextType>,
        CallEdgeType extends ICallEdge<StateType>> {

    /**
     * Result from {@link IAnalysisLatticeElement#propagate(IState, BlockAndContext, boolean)}.
     */
    class MergeResult {

        private String diff;

        /**
         * Constructs a new merge result.
         */
        public MergeResult(String diff) {
            this.diff = diff;
        }

        /**
         * Returns a description of the abstract state difference,
         * or null if not available.
         */
        public String getDiff() {
            return diff;
        }
    }

    /**
     * Returns the abstract state for entry of the given basic block and context,
     * where null represents none.
     */
    StateType getState(BasicBlock block, ContextType context);

    /**
     * Returns the abstract state for entry of the given basic block and context,
     * where null represents none.
     */
    StateType getState(BlockAndContext<ContextType> bc);

    /**
     * Returns the contexts and abstract states for the entry of the given basic block.
     */
    Map<ContextType, StateType> getStates(BasicBlock block);

    /**
     * Returns the call graph.
     */
    CallGraph<StateType, ContextType, CallEdgeType> getCallGraph();

//    /**
//     * Returns the number of states stored for the given basic block.
//     */
//    int getSize(BasicBlock block); // (currently unused)

    /**
     * Propagates s into the entry state at the given location.
     * The given state may be modified by the operation.
     *
     * @param localize if set, localize the state while joining
     * @return a merge result, or null if no new flow added.
     */
    MergeResult propagate(StateType s, BlockAndContext<ContextType> bc, boolean localize);

    /**
     * Returns the total number of (non-bottom) abstract states.
     */
    int getNumberOfStates();
}