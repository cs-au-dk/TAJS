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

package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.lattice.AnalysisLatticeElement;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.IAnalysis;
import dk.brics.tajs.solver.IEdgeTransfer;
import dk.brics.tajs.solver.IWorkListStrategy;
import dk.brics.tajs.solver.SolverSynchronizer;

/**
 * Encapsulation of the analysis using {@link State}, {@link Context},
 * {@link Solver}, {@link InitialStateBuilder}, {@link Transfer},
 * {@link WorkListStrategy}, {@link IContextSensitivityStrategy}, and {@link IAnalysisMonitoring}.
 */
public final class Analysis implements IAnalysis<State, Context, CallEdge, IAnalysisMonitoring, Analysis> {

    private final Solver solver;

    private final InitialStateBuilder initial_state_builder;

    private final Transfer transfer;

    private final WorkListStrategy worklist_strategy;

    private final IAnalysisMonitoring monitoring;

    private final EvalCache eval_cache;

    private IContextSensitivityStrategy context_sensitivity_strategy;

    private final PropVarOperations state_util;

    /**
     * Constructs a new analysis object.
     */
    public Analysis(IAnalysisMonitoring monitoring, SolverSynchronizer sync) {
        this.monitoring = monitoring;
        initial_state_builder = new InitialStateBuilder();
        transfer = new Transfer();
        worklist_strategy = new WorkListStrategy();
        eval_cache = new EvalCache();
        solver = new Solver(this, sync);
        state_util = new PropVarOperations();
    }

    @Override
    public AnalysisLatticeElement makeAnalysisLattice(FlowGraph fg) {
        return new AnalysisLatticeElement(fg);
    }

    @Override
    public void initContextSensitivity(FlowGraph fg) {
        if (Options.get().isDeterminacyEnabled()) {
            context_sensitivity_strategy = new StaticDeterminacyContextSensitivityStrategy(fg.getSyntacticHints());
        } else {
            context_sensitivity_strategy = new BasicContextSensitivityStrategy();
        }
    }

    @Override
    public InitialStateBuilder getInitialStateBuilder() {
        return initial_state_builder;
    }

    @Override
    public Transfer getNodeTransferFunctions() {
        return transfer;
    }

    @Override
    public IEdgeTransfer<State, Context> getEdgeTransferFunctions() {
        return transfer;
    }

    @Override
    public IWorkListStrategy<Context> getWorklistStrategy() {
        return worklist_strategy;
    }

    @Override
    public IAnalysisMonitoring getMonitoring() {
        return monitoring;
    }

    @Override
    public void setSolverInterface(Solver.SolverInterface c) {
        transfer.setSolverInterface(c);
        state_util.setSolverInterface(c);
        worklist_strategy.setCallGraph(c.getAnalysisLatticeElement().getCallGraph());
    }

    /**
     * Returns the solver.
     */
    public Solver getSolver() {
        return solver;
    }

    /**
     * Returns the eval cache.
     */
    public EvalCache getEvalCache() {
        return eval_cache;
    }

    @Override
    public CallEdge makeCallEdge(State edge_state) {
        return new CallEdge(edge_state);
    }

    /**
     * Returns the context sensitivity strategy.
     */
    public IContextSensitivityStrategy getContextSensitivityStrategy() {
        return context_sensitivity_strategy;
    }

    /**
     * Returns the properties/variables operations object.
     */
    public PropVarOperations getPropVarOperations() {
        return state_util;
    }
}
