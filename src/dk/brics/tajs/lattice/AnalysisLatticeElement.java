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

package dk.brics.tajs.lattice;

import static dk.brics.tajs.util.Collections.newMap;

import java.util.Map;

import org.apache.log4j.Logger;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.IAnalysisLatticeElement;
import dk.brics.tajs.solver.ICallContext;

/**
 * Global analysis lattice element.
 */
public class AnalysisLatticeElement<
		BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
		CallContextType extends ICallContext<CallContextType>,
		CallEdgeType extends CallEdge<BlockStateType>> implements
                                    IAnalysisLatticeElement<BlockStateType,CallContextType,CallEdgeType> {

	private static Logger logger = Logger.getLogger(AnalysisLatticeElement.class); 

	/**
	 * Abstract block states.
	 * Stores an abstract state for each basic block entry and call context. 
	 * Default is none.
	 */
	private Map<BasicBlock,Map<CallContextType,BlockStateType>> block_entry_states;
	
	/**
	 * Call graph.
	 */
	private CallGraph<BlockStateType,CallContextType,CallEdgeType> call_graph;
	
	/**
	 * Constructs a new global analysis lattice element.
	 */
	public AnalysisLatticeElement(FlowGraph fg) {
		block_entry_states = newMap();
		for (Function ff : fg.getFunctions()) {
			for (BasicBlock bb : ff.getBlocks()) {
				Map<CallContextType,BlockStateType> m = newMap();
				block_entry_states.put(bb,m);				
			}
		}
		call_graph = new CallGraph<>();
	}
	
	@Override
	public CallGraph<BlockStateType,CallContextType,CallEdgeType> getCallGraph() {
		return call_graph;
	}
	
	@Override
	public void check() {
		for (Map<CallContextType,BlockStateType> m : block_entry_states.values()) {
			for (BlockStateType b : m.values()) {
				b.check();
			}
		}
	}
	
	@Override
	public BlockStateType getState(BasicBlock block, CallContextType context) {
		Map<CallContextType, BlockStateType> bs = block_entry_states.get(block);
		BlockStateType b;
		if (bs == null) {
			Map<CallContextType,BlockStateType> m = newMap();
			block_entry_states.put(block, m);
			b = null;			
		}
		else {
			b = bs.get(context);
		}
		if (b != null) {
			b.checkOwner(block, context);
		}
		return b;
	}

	@Override
	public Map<CallContextType,BlockStateType> getStates(BasicBlock block) {
		Map<CallContextType,BlockStateType> m = block_entry_states.get(block);
		if (m == null) {
			m = newMap();
			block_entry_states.put(block, m);
		}
		return m;
	}
	
	@Override
	public int getSize(BasicBlock block) {
		return block_entry_states.get(block).size();
	}
	
	@Override
	public MergeResult propagate(BlockStateType s, BasicBlock b, CallContextType c, boolean localize) {
		logger.debug("propagating state to block " + b.getIndex() + " at " + b.getSourceLocation());
		s.check();
		if (Options.isIntermediateStatesEnabled() && localize) {
			logger.debug("before localization: " + s.toString());
		}
		boolean add;
		String diff = null;
		Map<CallContextType,BlockStateType> m = getStates(b);
		BlockStateType state_current = m.get(c);
		if (state_current == null) {
			add = true;
			if (localize) {
				s.localize(null);
			}
			s.setBasicBlock(b);
			s.setContext(c);
			m.put(c, s);
			state_current = s;
		} else {
			if (Options.isIntermediateStatesEnabled()) {
				logger.debug("existing block entry state: " + state_current.toString());
			}
			BlockStateType state_old = null;
			if (Options.isNewFlowEnabled()) {
				state_old = state_current.clone();
			}
			state_current.checkOwner(b, c);
//			if (Options.isIntermediateStatesEnabled() && localize) {
//				logger.debug("before localization: " + s.toString());
//			}
			if (localize) {
				s.localize(state_current);
			}
			if (Options.isIntermediateStatesEnabled() && localize) {
				logger.debug("after localization, before join: " + s.toString());
			}
			add = state_current.propagate(s, localize);
			s.getSolverInterface().getMonitoring().visitJoin();
			if (Options.isNewFlowEnabled()) {
				diff = state_current.diff(state_old);
			}
		}
		if (add) {
			state_current.check();
			if (Options.isIntermediateStatesEnabled()) {
				logger.debug("Added block entry state at block " + b.getIndex()+ ": " + state_current);
			}
			return new MergeResult(diff);
		} else
			return null;
	}
}
