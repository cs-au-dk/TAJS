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

package dk.brics.tajs.analysis;

import dk.brics.tajs.blendedanalysis.solver.BlendedAnalysisManager;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.lattice.AnalysisLatticeElement;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.IAnalysis;
import dk.brics.tajs.solver.IEdgeTransfer;
import dk.brics.tajs.solver.SolverSynchronizer;
import dk.brics.tajs.typetesting.ITypeTester;

/**
 * Encapsulation of the analysis using {@link State}, {@link Context},
 * {@link Solver}, {@link InitialStateBuilder}, {@link Transfer},
 * {@link IContextSensitivityStrategy}, and {@link IAnalysisMonitoring}.
 */
public final class Analysis implements IAnalysis<State, Context, CallEdge, IAnalysisMonitoring, Analysis> {

    private final Solver solver;

    private final InitialStateBuilder initial_state_builder;

    private final Transfer transfer;

    private final IAnalysisMonitoring monitoring;

    private final EvalCache eval_cache;

    private final Unsoundness unsoundness;

    private CustomContextSensitivityStrategy context_sensitivity_strategy;

    private final PropVarOperations state_util;

    private BlendedAnalysisManager blended_analysis_manager;

    private ITypeTester<Context> ttr;

    /**
     * Constructs a new analysis object.
     */
    public Analysis(IAnalysisMonitoring monitoring, SolverSynchronizer sync, Transfer transfer, ITypeTester<Context> ttr) {
        unsoundness = new Unsoundness(Options.get().getUnsoundness(), monitoring::addMessageInfo);
        this.monitoring = monitoring;
        initial_state_builder = new InitialStateBuilder();
        this.transfer = transfer;
        this.ttr = ttr;
        eval_cache = new EvalCache();
        solver = new Solver(this, sync);
        state_util = new PropVarOperations(unsoundness);
        if (Options.get().isBlendedAnalysisEnabled() || Options.get().isIgnoreUnreachedEnabled())
            blended_analysis_manager = new BlendedAnalysisManager();
    }

    /**
     * Constructs a new analysis object with the default transfer.
     */
    public Analysis(IAnalysisMonitoring monitoring, SolverSynchronizer sync) {
        this(monitoring, sync, new Transfer(), null);
    }

    @Override
    public AnalysisLatticeElement makeAnalysisLattice(FlowGraph fg) {
        return new AnalysisLatticeElement(fg);
    }

    /**
     * Initializes the context-sensitivity strategy.
     * If a type-tester is available, then that is used;
     * otherwise, if determinacy is enabled then {@link StaticDeterminacyContextSensitivityStrategy} is used;
     * otherwise {@link BasicContextSensitivityStrategy} is used.
     */
    @Override
    public void initContextSensitivity(FlowGraph fg) {
        IContextSensitivityStrategy s;
        if (ttr != null) {
            s = ttr.getCustomContextSensitivityStrategy(fg);
        } else if (Options.get().isDeterminacyEnabled()) {
            s = new StaticDeterminacyContextSensitivityStrategy(fg.getSyntacticInformation());
        } else {
            s = new BasicContextSensitivityStrategy();
        }
        context_sensitivity_strategy = new CustomContextSensitivityStrategy(s);
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
    public IEdgeTransfer<Context> getEdgeTransferFunctions() {
        return transfer;
    }

    @Override
    public IAnalysisMonitoring getMonitoring() {
        return monitoring;
    }

    @Override
    public BlendedAnalysisManager getBlendedAnalysis() {
        return blended_analysis_manager;
    }

    @Override
    public void setSolverInterface(Solver.SolverInterface c) {
        transfer.setSolverInterface(c);
        state_util.setSolverInterface(c);
        monitoring.setSolverInterface(c);
        if (Options.get().isBlendedAnalysisEnabled() || Options.get().isIgnoreUnreachedEnabled())
            blended_analysis_manager.setSolverInterface(c);
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
    public CustomContextSensitivityStrategy getContextSensitivityStrategy() {
        return context_sensitivity_strategy;
    }

    /**
     * Returns the properties/variables operations object.
     */
    public PropVarOperations getPropVarOperations() {
        return state_util;
    }

    public Unsoundness getUnsoundness() {
        return unsoundness;
    }

    @Override
    public ITypeTester<Context> getTypeTester() {
        return ttr;
    }
}
