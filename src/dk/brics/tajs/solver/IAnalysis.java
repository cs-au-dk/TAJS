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

import dk.brics.tajs.blendedanalysis.solver.BlendedAnalysisManager;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.typetesting.ITypeTester;

/**
 * Interface for analyses on flow graphs.
 */
public interface IAnalysis<StateType extends IState<StateType, ContextType, CallEdgeType>,
        ContextType extends IContext<ContextType>,
        CallEdgeType extends ICallEdge<StateType>,
        MonitoringType extends ISolverMonitoring<StateType, ContextType>,
        AnalysisType extends IAnalysis<StateType, ContextType, CallEdgeType, MonitoringType, AnalysisType>> {

    /**
     * Returns a new global analysis lattice element.
     */
    IAnalysisLatticeElement<StateType, ContextType, CallEdgeType> makeAnalysisLattice(FlowGraph fg);

    /**
     * Initializes the context sensitivity heuristics.
     */
    void initContextSensitivity(FlowGraph fg);

    /**
     * Returns the initial state builder.
     */
    IInitialStateBuilder<StateType, ContextType, CallEdgeType, MonitoringType, AnalysisType> getInitialStateBuilder();

    /**
     * Returns the node transfer functions.
     */
    INodeTransfer<StateType, ContextType> getNodeTransferFunctions();

    /**
     * Returns the edge transfer functions.
     */
    IEdgeTransfer<ContextType> getEdgeTransferFunctions();

    /**
     * Returns the blended analysis component.
     */
    BlendedAnalysisManager getBlendedAnalysis();

    /**
     * Returns the monitoring object.
     */
    MonitoringType getMonitoring();

    /**
     * Sets the current solver interface.
     */
    void setSolverInterface(GenericSolver<StateType, ContextType, CallEdgeType, MonitoringType, AnalysisType>.SolverInterface c);

    /**
     * Creates a copy of this edge with a cloned abstract state.
     */
    CallEdgeType cloneCallEdge(CallEdgeType edge);

    /**
     * Returns the type tester, or null if not available.
     */
    ITypeTester<ContextType> getTypeTester();
}
