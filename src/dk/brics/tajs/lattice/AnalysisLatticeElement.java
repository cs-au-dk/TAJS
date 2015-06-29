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

package dk.brics.tajs.lattice;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.IAnalysisLatticeElement;
import dk.brics.tajs.solver.IContext;
import org.apache.log4j.Logger;

import java.util.Map;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * Global analysis lattice element.
 */
public class AnalysisLatticeElement<
        BlockStateType extends BlockState<BlockStateType, ContextType, CallEdgeType>,
        ContextType extends IContext<ContextType>,
        CallEdgeType extends CallEdge<BlockStateType>> implements
        IAnalysisLatticeElement<BlockStateType, ContextType, CallEdgeType, SpecialVars> {

    private static Logger log = Logger.getLogger(AnalysisLatticeElement.class);

    /**
     * Abstract block states.
     * Stores an abstract state for each basic block entry and context.
     * Default is none.
     */
    private Map<BasicBlock, Map<ContextType, BlockStateType>> block_entry_states;

    /**
     * Call graph.
     */
    private CallGraph<BlockStateType, ContextType, CallEdgeType> call_graph;

    /**
     * Special variables.
     */
    private SpecialVars specialvars;

    /**
     * Constructs a new global analysis lattice element.
     */
    public AnalysisLatticeElement(FlowGraph fg) {
        block_entry_states = newMap();
        for (Function ff : fg.getFunctions()) {
            for (BasicBlock bb : ff.getBlocks()) {
                Map<ContextType, BlockStateType> m = newMap();
                block_entry_states.put(bb, m);
            }
        }
        call_graph = new CallGraph<>();
        specialvars = new SpecialVars();
    }

    @Override
    public CallGraph<BlockStateType, ContextType, CallEdgeType> getCallGraph() {
        return call_graph;
    }

    @Override
    public SpecialVars getSpecialVars() {
        return specialvars;
    }

    @Override
    public BlockStateType getState(BasicBlock block, ContextType context) {
        Map<ContextType, BlockStateType> bs = block_entry_states.get(block);
        BlockStateType b;
        if (bs == null) {
            Map<ContextType, BlockStateType> m = newMap();
            block_entry_states.put(block, m);
            b = null;
        } else {
            b = bs.get(context);
        }
        if (b != null) {
            b.checkOwner(block, context);
        }
        return b;
    }

    @Override
    public BlockStateType getState(BlockAndContext<ContextType> bc) {
        return getState(bc.getBlock(), bc.getContext());
    }

    @Override
    public Map<ContextType, BlockStateType> getStates(BasicBlock block) {
        Map<ContextType, BlockStateType> m = block_entry_states.get(block);
        if (m == null) {
            m = newMap();
            block_entry_states.put(block, m);
        }
        return m;
    }

//    @Override
//    public int getSize(BasicBlock block) {
//        return block_entry_states.get(block).size();
//    }

    @Override
    public MergeResult propagate(BlockStateType s, BasicBlock b, ContextType c, boolean localize) {
        if (log.isDebugEnabled()) {
            log.debug("propagating state to block " + b.getIndex() + " at " + b.getSourceLocation());
            if (Options.get().isIntermediateStatesEnabled() && localize) {
                log.debug("before localization: " + s);
            }
        }
        boolean add;
        String diff = null;
        Map<ContextType, BlockStateType> m = getStates(b);
        BlockStateType state_current = m.get(c);
        if (state_current == null) { // existing state at (b,c) is implicitly bottom, so just store s
            add = true;
            if (localize) {
                s.localize(null);
            }
            s.setBasicBlock(b);
            s.setContext(c);
            m.put(c, s);
            state_current = s;
        } else { // a nontrivial state already exists at (b,c), so join s into it
            if (Options.get().isIntermediateStatesEnabled()) {
                if (log.isDebugEnabled())
                    log.debug("existing block entry state: " + state_current);
            }
            BlockStateType state_old = null;
            if (Options.get().isNewFlowEnabled()) {
                state_old = state_current.clone();
            }
            state_current.checkOwner(b, c);
//			if (Options.get().isIntermediateStatesEnabled() && localize) {
//				if (log.isDebugEnabled())
//                log.debug("before localization: " + s.toString());
//			}
            if (localize) {
                s.localize(state_current);
            }
            if (Options.get().isIntermediateStatesEnabled() && localize) {
                if (log.isDebugEnabled())
                    log.debug("after localization, before join: " + s);
            }
            add = state_current.propagate(s, localize);
            s.getSolverInterface().getMonitoring().visitJoin();
            if (Options.get().isNewFlowEnabled()) {
                diff = state_current.diff(state_old);
            }
        }
        if (add) {
            if (Options.get().isIntermediateStatesEnabled()) {
                if (log.isDebugEnabled())
                    log.debug("Added block entry state at block " + b.getIndex() + ": " + state_current);
            }
            return new MergeResult(diff);
        } else
            return null;
    }
}
