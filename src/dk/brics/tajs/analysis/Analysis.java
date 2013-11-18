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

package dk.brics.tajs.analysis;

import static dk.brics.tajs.util.Collections.newMap;

import java.util.List;
import java.util.Map;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.lattice.AnalysisLatticeElement;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.solver.IAnalysis;
import dk.brics.tajs.solver.IBlockTransfer;
import dk.brics.tajs.solver.IEdgeTransfer;
import dk.brics.tajs.solver.IWorkListStrategy;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.solver.SolverSynchronizer;

/**
 * Encapsulation of the analysis using {@link State}, {@link Context}, {@link Monitoring}, 
 * {@link Solver}, {@link InitialStateBuilder}, {@link Transfer},  
 * {@link WorkListStrategy}, {@link Monitoring}, and {@link NativeFunctions}.
 */
public final class Analysis implements IAnalysis<State,Context,CallEdge<State>,Monitoring<State,Context,CallEdge<State>>,Analysis> {
	
	private Solver solver;
	
	private InitialStateBuilder initial_state_builder;
	
	private Transfer transfer;
	
	private WorkListStrategy worklist_strategy;
	
	private Monitoring<State,Context,CallEdge<State>> monitoring;
	
	private EvalCache eval_cache;
	
	private SpecialVars specialvars; // TODO: move this into AnalysisLatticeElement ?

	/**
	 * For-in specializations.
	 * For each BeginForInNode and non-specialized context, gives a list of specialized contexts.
	 */
	private Map<NodeAndContext<Context>,List<Context>> for_in_specializations;  // TODO: move this into AnalysisLatticeElement ?

	/**
	 * Constructs a new analysis object.
	 */
	public Analysis(SolverSynchronizer sync) {
		initial_state_builder = new InitialStateBuilder();
		transfer = new Transfer();
		worklist_strategy = new WorkListStrategy();
		monitoring = new Monitoring<>();
		eval_cache = new EvalCache();
		specialvars = new SpecialVars();
		for_in_specializations = newMap();
		solver = new Solver(this, sync);
	}
	
	@Override
	public AnalysisLatticeElement<State,Context,CallEdge<State>> makeAnalysisLattice(FlowGraph fg) {
		return new AnalysisLatticeElement<>(fg);
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
	public IBlockTransfer<State,Context> getBlockTransferFunction() {
		return transfer;
	}

	@Override
	public IEdgeTransfer<State,Context> getEdgeTransferFunctions() {
		return transfer;
	}

	@Override
	public IWorkListStrategy<Context> getWorklistStrategy() {
		return worklist_strategy;
	}

	@Override
	public Monitoring<State,Context,CallEdge<State>> getMonitoring() {
		return monitoring;
	}

	@Override
	public void setSolverInterface(Solver.SolverInterface c) {
		transfer.setSolverInterface(c);
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
	
	/**
	 * Returns the special variables info.
	 */
	public SpecialVars getSpecialVars() {
		return specialvars;
	}

	@Override
	public CallEdge<State> makeCallEdge(State edge_state) {
		return new CallEdge<>(edge_state);
	}
	
	public void setForInSpecializations(AbstractNode begin, Context nonspec, List<Context> spec) {
		for_in_specializations.put(new NodeAndContext<>(begin, nonspec), spec);
	}
	
	public List<Context> getForInSpecializations(AbstractNode begin, Context nonspec) {
		return for_in_specializations.get(new NodeAndContext<>(begin, nonspec));
	}
}
