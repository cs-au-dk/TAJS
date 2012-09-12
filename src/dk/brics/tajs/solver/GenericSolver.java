/*
 * Copyright 2012 Aarhus University
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

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.ExceptionalReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.ReturnNode;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

/**
 * Generic fixpoint solver for flow graphs.
 */
public class GenericSolver<BlockStateType extends IBlockState<BlockStateType, CallContextType, CallEdgeType>, 
CallContextType extends ICallContext<CallContextType>, 
CallEdgeType extends ICallEdge<BlockStateType>,
MonitoringType extends IMonitoring<BlockStateType,CallContextType>,
AnalysisType extends IAnalysis<BlockStateType, CallContextType, CallEdgeType, MonitoringType, AnalysisType>> {

	private static Logger logger = Logger.getLogger(GenericSolver.class); 

	private final AnalysisType analysis;

    private final SolverSynchronizer sync;

    private FlowGraph flowgraph;

    private BasicBlock global_entry_block;

    private IAnalysisLatticeElement<BlockStateType,CallContextType,CallEdgeType> the_analysis_lattice_element;

    private WorkList<CallContextType> worklist;
    
    private CallDependencies<CallContextType> deps;

    private AbstractNode current_node;

    private BlockStateType current_state;

    private CallContextType current_context;

    /**
     * Messages are disabled during fixpoint iteration and enabled in the subsequent scan phase.
     */
    private boolean messages_enabled;

    private SolverInterface c;

	/**
     * Interface to solver used while evaluating transfer functions. 
     * Provides callbacks from transfer functions to solver state.
     */
    public class SolverInterface {

        private SolverInterface() {}

        /**
         * Returns the node currently being visited.
         */
        public AbstractNode getCurrentNode() {
            if (current_node == null)
                throw new AnalysisException("Unexpected call to getCurrentNode");
            return current_node;
        }

        /**
         * Returns the current calling context.
         */
        public CallContextType getCurrentContext() {
            if (current_context == null)
                throw new AnalysisException("Unexpected call to getCurrentContext");
            return current_context;
        }

        /**
         * Returns the current abstract state.
         */
        public BlockStateType getCurrentState() {
            if (current_state == null)
                throw new AnalysisException("Unexpected call to getCurrentState");
            return current_state;
        }

        /**
         * Sets the current abstract state.
         */
        public void setCurrentState(BlockStateType state) {
            current_state = state;
        }

        /**
         * Returns the flow graph.
         */
        public FlowGraph getFlowGraph() {
            return flowgraph;
        }
        
        /**
         * Returns the analysis object.
         */
        public AnalysisType getAnalysis() {
        	return analysis;
        }

        /**
         * Returns the analysis lattice element.
         */
        public IAnalysisLatticeElement<BlockStateType, CallContextType, CallEdgeType> getAnalysisLatticeElement() {
            return the_analysis_lattice_element;
        }

        /**
         * Returns the monitoring object of the analysis.
         */
        public MonitoringType getMonitoring() {
            return analysis.getMonitoring();
        }

        /**
         * Returns true if in message scanning phase.
         */
        public boolean isScanning() {
            return messages_enabled;
        }

        /**
         * Merges <code>state</code> into the entry state of <code>block</code> in call context <code>context</code> 
         * and updates the work list accordingly. 
         * The given state may be modified by this operation. 
         * Ignored if in scan phase.
         */
        public void propagateToBasicBlock(BlockStateType state, BasicBlock block, CallContextType context) {
            if (messages_enabled)
                return;
            propagate(state, block, context, false, false);
        }

        /**
         * Replaces the entry state of <code>block</code> in call context <code>context</code> with <code>state</code> 
         * and updates the work list accordingly.
         * The given state may be modified by this operation.
         * Ignored if in scan phase.
         */
        public void propagateToBasicBlockDestructively(BlockStateType state, BasicBlock block, CallContextType context) {
            if (messages_enabled)
                return;
            propagate(state, block, context, false, true);
        }
        
        /**
         * Propagates dataflow.
         * @return true if the worklist is changed
         */
        private boolean propagate(BlockStateType state, BasicBlock block, CallContextType context, boolean trim, boolean overwrite) {
            IAnalysisLatticeElement.MergeResult res = the_analysis_lattice_element.propagate(state, block, context, trim, overwrite);
            if (res != null) {
                if (worklist.add(worklist.new Entry(block, context)))
                	deps.incrementFunctionActivityLevel(block.getFunction(), context.toEntry(block).getContext());
                if (sync != null)
                    sync.markPendingBlock(block);
                analysis.getMonitoring().visitNewFlow(block, context, the_analysis_lattice_element.getState(block, context), res.getDiff(), "CALL");
                if (logger.isDebugEnabled()) 
                	logger.debug("New flow at block " + block.getIndex() + " node "
                			+ block.getFirstNode().getIndex() + ", context " + context
                			+ (res.getDiff() != null ? ", diff:" + res.getDiff() : ""));
                return true;
            }
           	return false;
        }
        
        /**
         * Merges the edge state into the entry state of the callee in the given call context and updates the work list accordingly. 
         * Also updates the call graph and triggers reevaluation of ordinary/exceptional return flow. 
         * The given state may be modified by this operation. 
         * Ignored if in scan phase.
         */
        public void propagateToFunctionEntry(CallEdgeType edge, AbstractNode call_node, CallContextType caller_context, Function callee, CallContextType callee_context) {
            if (messages_enabled)
                return;
            BlockStateType callee_entry_state = the_analysis_lattice_element.getState(callee.getEntry(), callee_context);
            CallGraph<BlockStateType, CallContextType, CallEdgeType> cg = the_analysis_lattice_element.getCallGraph();
			BlockStateType edge_state_diff = cg.addTarget(call_node, caller_context, callee.getEntry(), callee_context, edge, sync);
            if (!edge_state_diff.isNone()) {
            	// new flow at call edge, propagate transformed and reduced state into function entry
                edge_state_diff.transform(edge, callee_entry_state, callee, callee_context);
                boolean changed = propagate(edge_state_diff, callee.getEntry(), callee_context, true, false);
                if (changed) {
    				// new flow along call edge, so charge the call edge  
                	deps.chargeCallEdge(call_node, caller_context, callee, callee_context);
                }
                // process existing ordinary/exceptional return flow
                CallContextType stored_context = current_context;
                BlockStateType stored_state = current_state;
                AbstractNode stored_node = current_node;
                current_context = callee_context;
                current_state = null;
                current_node = null;
                analysis.getNodeTransferFunctions().transferReturn(call_node, callee, caller_context, callee_context);
                current_context = stored_context;
                current_state = stored_state;
                current_node = stored_node;
            }
        }
        
        /**
         * Transforms the given state inversely according to the call edge.
         */
        public void returnFromFunctionExit(BlockStateType return_state, AbstractNode call_node, CallContextType caller_context, Function callee, CallContextType callee_context) {
        	return_state.transformInverse(the_analysis_lattice_element.getCallGraph().getCallEdge(call_node, caller_context, callee.getEntry(), callee_context), 
        			the_analysis_lattice_element.getState(callee.getEntry(), callee_context));
        }
        
        /**
         * Checks whether the given edge is charged.
         * Always returns true if charged edges are disabled.
         */
        public boolean isCallEdgeCharged(AbstractNode call_node, CallContextType caller_context, Function callee, CallContextType callee_context) {
        	return deps.isCallEdgeCharged(call_node, caller_context, callee, callee_context);
        }
    }

    /**
     * Constructs a new solver.
     */
    public GenericSolver(AnalysisType analysis, SolverSynchronizer sync) {
        this.analysis = analysis;
        this.sync = sync;
        Locale.setDefault(Locale.US); // there be dragons if this is not set...
    }

    /**
     * Initializes the solver for the given flow graph and HTML document.
     */
    public void init(FlowGraph fg, Document document) {
        if (the_analysis_lattice_element != null)
            throw new IllegalStateException("solve() called repeatedly");

        this.flowgraph = fg;
        this.global_entry_block = fg.getEntryBlock();
        this.the_analysis_lattice_element = analysis.makeAnalysisLattice(fg);
        c = new SolverInterface();
        analysis.setSolverInterface(c);

        // initialize worklist
        worklist = new WorkList<>(analysis.getWorklistStrategy());
        deps = new CallDependencies<>();
        current_node = global_entry_block.getFirstNode();
        analysis.getInitialStateBuilder().addInitialState(global_entry_block, c, document);
    }

    /**
     * Runs the solver.
     */
    public void solve() {
    	// iterate until fixpoint
    	block_loop: while (!worklist.isEmpty()) {    		
    		if (sync != null) {
    			if (sync.isSingleStep())
    				if (logger.isDebugEnabled()) 
    					logger.debug("Worklist: " + worklist);
    			sync.waitIfSingleStep();
    		}
    		// pick a pending entry
    		WorkList<CallContextType>.Entry p = worklist.removeNext();
    		if (p == null)
    			continue; // entry may have been removed
    		BasicBlock block = p.getBlock();
    		if (sync != null) 
    			sync.markActiveBlock(block);
    		current_context = p.getContext();
    		CallContextType entry_context = current_context.toEntry(block).getContext();
			deps.decrementFunctionActivityLevel(block.getFunction(), entry_context);
    		BlockStateType state = the_analysis_lattice_element.getState(block, p.getContext());
    		if (logger.isDebugEnabled()) {
    			logger.debug("Selecting worklist entry for block " + block.getIndex() + " at " + block.getSourceLocation());
    			logger.debug("Worklist: " + worklist);
    			logger.debug("Visiting " + block);
//    			logger.debug("Number of abstract states at this block: " + the_analysis_lattice_element.getSize(block));
    			logger.debug("Context: " + current_context);
    		} else if (!Options.isQuietEnabled() && !Options.isTestEnabled() && logger.isInfoEnabled()) {
//    			if (block.isEntry())
//    				logger.debug("Entering " + block.getFunction() + " at " + block.getFunction().getSourceLocation());
//    			if (block.isOrdinaryExit())
//    				logger.debug("Returning from " + block.getFunction() + " at " + block.getFunction().getSourceLocation());
//    			if (block.isExceptionalExit())
//    				logger.debug("Exception from " + block.getFunction() + " at " + block.getFunction().getSourceLocation());
    			logger.info("block " + block.getIndex() + " at " + block.getSourceLocation() + 
    					", transfers: " + (analysis.getMonitoring().getTotalNumberOfNodeTransfers() + 1) + 
//    					" (avg/node: " + ((float) ((analysis.getMonitoring().getTotalNumberOfNodeTransfers() + 1) * 1000 / flowgraph.getNumberOfNodes())) / 1000 + ")" + 
    					", pending: " + (worklist.size() + 1) + 
    					", contexts: " + the_analysis_lattice_element.getSize(block));
    		}
    		// basic block transfer
    		analysis.getMonitoring().visitBlockTransfer();
    		current_state = state.clone();
    		if (global_entry_block == block)
    			current_state.trim(null); // use *trimmed* initial state
    		if (Options.isIntermediateStatesEnabled())
    			logger.debug("Before block transfer: " + current_state.toString());
    		for (AbstractNode n : block.getNodes()) {
    			current_node = n;
    			if (logger.isDebugEnabled()) 
    				logger.debug("Visiting node " + current_node.getIndex() + ": "
    						+ current_node + " at " + current_node.getSourceLocation());
    			if (n.isDead()) {
        			if (logger.isDebugEnabled())
        				logger.debug("Skipping dead node");
    					continue;
    			}
    			analysis.getNodeTransferFunctions().transfer(current_node, current_state);
    			analysis.getMonitoring().visitNodeTransfer(current_node);
    			if (current_state.isNone()) {
    				logger.debug("No non-exceptional flow");
    				continue block_loop;
    			}
   				current_state.check();
    			if (Options.isIntermediateStatesEnabled() && !(n instanceof CallNode))
    				logger.debug("After node transfer: " + current_state.toStringBrief());
    		}
    		AbstractNode first = block.getFirstNode();
    		if (!(first instanceof CallNode) && !(first instanceof ReturnNode)
    				&& !(first instanceof EventDispatcherNode)
    				&& !(first instanceof ExceptionalReturnNode)) {
    			analysis.getBlockTransferFunction().transfer(block, current_state);
    			// edge transfer
    			for (Iterator<BasicBlock> i = block.getSuccessors().iterator(); i.hasNext();) {
    				BasicBlock succ = i.next();
    				if (analysis.getEdgeTransferFunctions().transfer(block, succ, current_state))
    					c.propagateToBasicBlock(i.hasNext() ? current_state.clone() : current_state, succ, current_context);
    			}
    		}
    		if (!deps.isFunctionActive(block.getFunction(), entry_context))
    			for (NodeAndContext<CallContextType> nc : the_analysis_lattice_element.getCallGraph().getSources(block.getFunction().getEntry(), entry_context)) {
    				// callee has become inactive, so discharge the call edge 
    				deps.dischargeCallEdge(nc.getNode(), nc.getContext(), block.getFunction(), entry_context);
    			}
    	}
    	deps.assertEmpty();
    	messages_enabled = true;
    }

    /**
     * Scans for messages. Takes one round through all nodes and all contexts without invoking <code>propagate</code>. 
     * {@link #solve()} must be called first.
     */
    public void scan() {
    	if (the_analysis_lattice_element == null)
    		throw new IllegalStateException("scan() called before solve()");
    	analysis.getMonitoring().beginScanPhase(flowgraph);
    	// visit each block
        for (Function function : flowgraph.getFunctions()) {
			if (logger.isDebugEnabled()) 
				logger.debug("Scanning " + function + " at " + function.getSourceLocation());
			analysis.getMonitoring().visitFunction(function, the_analysis_lattice_element.getStates(function.getEntry()).values());
			for (BasicBlock block : function.getBlocks()) {
				if (logger.isDebugEnabled()) 
					logger.debug("Scanning " + block + " at " + block.getSourceLocation());
				block_loop: for (Map.Entry<CallContextType, BlockStateType> me : the_analysis_lattice_element.getStates(block).entrySet()) {
					current_state = me.getValue().clone();
					current_context = me.getKey();
					if (global_entry_block == block)
						current_state.trim(null); // use *trimmed* initial state
					if (logger.isDebugEnabled()) 
						logger.debug("Call context: " + current_context);
					if (Options.isIntermediateStatesEnabled())
						logger.debug("Before block transfer: " + current_state);
					for (AbstractNode node : block.getNodes()) {
						current_node = node;
						if (logger.isDebugEnabled()) 
							logger.debug("node " + current_node.getIndex() + ": " + current_node);
		    			if (node.isDead())
		    				continue;
						if (current_state.isNone())
							continue block_loop; // unreachable, so skip the rest of the block
						analysis.getMonitoring().visitReachableNode(node);
						analysis.getNodeTransferFunctions().transfer(node, current_state);
					}
				}
			}
        }
        for (Message m : analysis.getMonitoring().endScanPhase(flowgraph)) {
        	System.out.println(m);
    	}
    }

    /**
     * Returns the analysis lattice element.
     * {@link #solve()} must be called first. 
     */
    public IAnalysisLatticeElement<BlockStateType, CallContextType, CallEdgeType> getAnalysisLatticeElement() {
        if (the_analysis_lattice_element == null)
            throw new IllegalStateException("getAnalysisLatticeElement() called before solve()");
        return the_analysis_lattice_element;
    }

    /**
     * Returns the flow graph.
     */
    public FlowGraph getFlowGraph() {
        return flowgraph;
    }
}
