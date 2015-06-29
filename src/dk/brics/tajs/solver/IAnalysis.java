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

import dk.brics.tajs.flowgraph.FlowGraph;

/**
 * Interface for analyses on flow graphs.
 */
public interface IAnalysis<BlockStateType extends IBlockState<BlockStateType, ContextType, CallEdgeType>,
        ContextType extends IContext<ContextType>,
        CallEdgeType extends ICallEdge<BlockStateType>,
        MonitoringType extends IMonitoring<BlockStateType, ContextType>,
        SpecialVarsType,
        AnalysisType extends IAnalysis<BlockStateType, ContextType, CallEdgeType, MonitoringType, SpecialVarsType, AnalysisType>> {

    /**
     * Returns a new global analysis lattice element.
     */
    IAnalysisLatticeElement<BlockStateType, ContextType, CallEdgeType, SpecialVarsType> makeAnalysisLattice(FlowGraph fg);

    /**
     * Returns the initial state builder.
     */
    IInitialStateBuilder<BlockStateType, ContextType, CallEdgeType> getInitialStateBuilder();

    /**
     * Returns the node transfer functions.
     */
    INodeTransfer<BlockStateType, ContextType> getNodeTransferFunctions();

    /**
     * Returns the edge transfer functions.
     */
    IEdgeTransfer<BlockStateType, ContextType> getEdgeTransferFunctions();

    /**
     * Returns the work list strategy.
     */
    IWorkListStrategy<ContextType> getWorklistStrategy();

    /**
     * Returns the monitoring object.
     */
    MonitoringType getMonitoring();

    /**
     * Sets the current solver interface.
     */
    void setSolverInterface(GenericSolver<BlockStateType, ContextType, CallEdgeType, MonitoringType, SpecialVarsType, AnalysisType>.SolverInterface c);

    /**
     * Constructs a new call edge for the given abstract state.
     */
    CallEdgeType makeCallEdge(BlockStateType edge_state);

    /**
     * Called before each fixpoint iteration, returns false if iteration should be aborted.
     */
    boolean allowNextIteration();
}
