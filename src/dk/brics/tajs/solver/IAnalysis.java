/*
 * Copyright 2009-2013 Aarhus University
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
					       MonitoringType extends IMonitoring<BlockStateType,ContextType>,
					       AnalysisType extends IAnalysis<BlockStateType, ContextType, CallEdgeType, MonitoringType, AnalysisType>> {

	/**
	 * Returns a new global analysis lattice element.
	 */
	public IAnalysisLatticeElement<BlockStateType,ContextType,CallEdgeType> makeAnalysisLattice(FlowGraph fg);

	/**
	 * Returns the initial state builder.
	 */
	public IInitialStateBuilder<BlockStateType,ContextType,CallEdgeType> getInitialStateBuilder();

	/**
	 * Returns the node transfer functions.
	 */
	public INodeTransfer<BlockStateType,ContextType> getNodeTransferFunctions();

	/**
	 * Returns the end-of-block transfer function.
	 */
	public IBlockTransfer<BlockStateType,ContextType> getBlockTransferFunction();

	/**
	 * Returns the edge transfer functions.
	 */
	public IEdgeTransfer<BlockStateType,ContextType> getEdgeTransferFunctions();
	
	/**
	 * Returns the work list strategy.
	 */
	public IWorkListStrategy<ContextType> getWorklistStrategy();
	
	/**
	 * Returns the monitoring object.
	 */
	public MonitoringType getMonitoring();
	
	/**
	 * Sets the current solver interface.
	 */
	public void setSolverInterface(GenericSolver<BlockStateType,ContextType,CallEdgeType,MonitoringType,AnalysisType>.SolverInterface c);
	
	/**
	 * Constructs a new call edge for the given abstract state.
	 */
	public CallEdgeType makeCallEdge(BlockStateType edge_state);
}
