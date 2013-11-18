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
import dk.brics.tajs.util.Pair;

/**
 * Generic fixpoint solver for flow graphs.
 */
public class GenericSolver<BlockStateType extends IBlockState<BlockStateType, ContextType, CallEdgeType>, 
ContextType extends IContext<ContextType>, 
CallEdgeType extends ICallEdge<BlockStateType>,
MonitoringType extends IMonitoring<BlockStateType,ContextType>,
AnalysisType extends IAnalysis<BlockStateType, ContextType, CallEdgeType, MonitoringType, AnalysisType>> {

	private static Logger logger = Logger.getLogger(GenericSolver.class); 

	private final AnalysisType analysis;

    private final SolverSynchronizer sync;

    private FlowGraph flowgraph;

    private BasicBlock global_entry_block;

    private IAnalysisLatticeElement<BlockStateType,ContextType,CallEdgeType> the_analysis_lattice_element;

    private WorkList<ContextType> worklist;
    
    private CallDependencies<ContextType> deps;

    private AbstractNode current_node;

    private BlockStateType current_state;

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
        public IAnalysisLatticeElement<BlockStateType, ContextType, CallEdgeType> getAnalysisLatticeElement() {
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
         * Merges <code>state</code> into the entry state of <code>block</code> in context <code>context</code> 
         * and updates the work list accordingly. 
         * The given state may be modified by this operation. 
         * Ignored if in scan phase.
         */
        public void propagateToBasicBlock(BlockStateType state, BasicBlock block, ContextType context) {
            if (messages_enabled)
                return;
            propagate(state, block, context, false);
        }

        /**
         * Propagates dataflow.
         */
        private void propagate(BlockStateType state, BasicBlock block, ContextType context, boolean localize) {
            IAnalysisLatticeElement.MergeResult res = the_analysis_lattice_element.propagate(state, block, context, localize);
            the_analysis_lattice_element.getCallGraph().registerBlockContext(block, context);
            if (res != null) {
                addToWorklist(block, context);
                if (sync != null)
                    sync.markPendingBlock(block);
                analysis.getMonitoring().visitNewFlow(block, context, the_analysis_lattice_element.getState(block, context), res.getDiff(), "CALL");
                if (logger.isDebugEnabled()) 
                	logger.debug("New flow at block " + block.getIndex() + " node "
                			+ block.getFirstNode().getIndex() + ", context " + context
                			+ (res.getDiff() != null ? ", diff:" + res.getDiff() : ""));
            }
        }

        /**
         * Adds the given location to the worklist.
         */
		public void addToWorklist(BasicBlock block, ContextType context) {
			if (worklist.add(worklist.new Entry(block, context)))
            	deps.incrementFunctionActivityLevel(context.getEntryBlockAndContext());
		}
		
        /**
         * Merges the edge state into the entry state of the callee in the given context and updates the work list accordingly. 
         * Also updates the call graph and triggers reevaluation of ordinary/exceptional return flow. 
         * The given state may be modified by this operation. 
         * Ignored if in scan phase.
         */
        public void propagateToFunctionEntry(AbstractNode call_node, ContextType caller_context, BlockStateType edge_state, 
        		ContextType edge_context, BasicBlock callee_entry) {
            if (messages_enabled)
                return;
            CallGraph<BlockStateType, ContextType, CallEdgeType> cg = the_analysis_lattice_element.getCallGraph();
            // add to existing call edge, compute edge_state_diff
			BlockStateType edge_state_diff = cg.addTarget(call_node, caller_context, callee_entry, edge_context, edge_state, sync, analysis);
            if (!edge_state_diff.isNone()) {
            	// new flow at call edge, transform it relative to the function entry states and contexts
                ContextType callee_context = edge_state_diff.transform(cg.getCallEdge(call_node, caller_context, callee_entry, edge_context), 
                		edge_context, the_analysis_lattice_element.getStates(callee_entry), callee_entry);
                cg.addSource(call_node, caller_context, callee_entry, callee_context, edge_context);
            	// propagate transformed state into function entry
                propagate(edge_state_diff, callee_entry, callee_context, true);
                // charge the call edge  
                deps.chargeCallEdge(call_node.getBlock(), caller_context, edge_context, callee_entry, callee_context);
                // process existing ordinary/exceptional return flow
                BlockStateType stored_state = current_state;
                AbstractNode stored_node = current_node;
                current_state = null;
                current_node = null;
                analysis.getNodeTransferFunctions().transferReturn(call_node, callee_entry, caller_context, callee_context, edge_context);
                current_state = stored_state;
                current_node = stored_node;
            }
        }
        
        /**
         * Transforms the given state inversely according to the call edge.
         */
        public void returnFromFunctionExit(BlockStateType return_state, AbstractNode call_node, ContextType caller_context, 
        		BasicBlock callee_entry, ContextType edge_context) {
        	CallEdgeType edge = the_analysis_lattice_element.getCallGraph().getCallEdge(call_node, caller_context, callee_entry, edge_context);
        	if (return_state.transformInverse(edge, callee_entry, return_state.getContext())) {
        		// need to re-process the incoming flow at function entry
        		propagateToFunctionEntry(call_node, caller_context, edge.getState(), edge_context, callee_entry);
        	}
        }
        
        /**
         * Checks whether the given edge is charged.
         * Always returns true if charged edges are disabled.
         */
        public boolean isCallEdgeCharged(BasicBlock caller, ContextType caller_context, ContextType edge_context, 
        		BlockAndContext<ContextType> callee_entry) {
        	return deps.isCallEdgeCharged(caller, caller_context, edge_context, callee_entry.getBlock(), callee_entry.getContext());
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
		boolean terminatedEarly = false;
    	// iterate until fixpoint
    	block_loop: while (!worklist.isEmpty()) {  
    		if (Options.isBoundedIterationsEnabled() && analysis.getMonitoring().getTotalNumberOfNodeTransfers() > Options.getIterationBound()) {
				logger.warn("Iteration bound reached. Terminating solver loop early and unsoundly.");
				terminatedEarly = true;
				break;
			}
    		if (sync != null) {
    			if (sync.isSingleStep())
    				if (logger.isDebugEnabled()) 
    					logger.debug("Worklist: " + worklist);
    			sync.waitIfSingleStep();
    		}
    		// pick a pending entry
    		WorkList<ContextType>.Entry p = worklist.removeNext();
    		if (p == null)
    			continue; // entry may have been removed
    		BasicBlock block = p.getBlock();
    		ContextType context = p.getContext();
    		if (sync != null) 
    			sync.markActiveBlock(block);
			deps.decrementFunctionActivityLevel(context.getEntryBlockAndContext());
    		BlockStateType state = the_analysis_lattice_element.getState(block, p.getContext());
    		if (state == null)
    			throw new AnalysisException();
    		if (logger.isDebugEnabled()) {
    			logger.debug("Selecting worklist entry for block " + block.getIndex() + " at " + block.getSourceLocation());
    			logger.debug("Worklist: " + worklist);
    			logger.debug("Visiting " + block);
//    			logger.debug("Number of abstract states at this block: " + the_analysis_lattice_element.getSize(block));
    			logger.debug("Context: " + context);
    		} else if (!Options.isQuietEnabled() && !Options.isTestEnabled() && logger.isInfoEnabled()) {
//    			if (block.isEntry())
//    				logger.debug("Entering " + block.getFunction() + " at " + block.getFunction().getSourceLocation());
//    			if (block.isOrdinaryExit())
//    				logger.debug("Returning from " + block.getFunction() + " at " + block.getFunction().getSourceLocation());
//    			if (block.isExceptionalExit())
//    				logger.debug("Exception from " + block.getFunction() + " at " + block.getFunction().getSourceLocation());
    			logger.info("block " + block.getIndex() + " at " + block.getSourceLocation() + 
    					", context " + context +
    					", transfers: " + (analysis.getMonitoring().getTotalNumberOfNodeTransfers() + 1) + 
//    					" (avg/node: " + ((float) ((analysis.getMonitoring().getTotalNumberOfNodeTransfers() + 1) * 1000 / flowgraph.getNumberOfNodes())) / 1000 + ")" + 
    					", pending: " + (worklist.size() + 1) + 
    					", contexts: " + the_analysis_lattice_element.getSize(block));
    		}
    		// basic block transfer
    		analysis.getMonitoring().visitBlockTransfer();
    		current_state = state.clone();
    		if (global_entry_block == block)
    			current_state.localize(null); // use *localized* initial state
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
    			if (Options.isIntermediateStatesEnabled() && !(n instanceof CallNode))
    				logger.debug("After node transfer: " + current_state.toStringBrief());
    		}
    		AbstractNode first = block.getFirstNode();
    		if (!(first instanceof CallNode) && !(first instanceof ReturnNode) // TODO: shouldn't refer to dk.brics.tajs.flowgraph.jsnodes from this package, check for empty successors instead?
    				&& !(first instanceof EventDispatcherNode)
    				&& !(first instanceof ExceptionalReturnNode)) {
    			analysis.getBlockTransferFunction().transfer(block, current_state);
    			// edge transfer
    			for (Iterator<BasicBlock> i = block.getSuccessors().iterator(); i.hasNext();) {
    				BasicBlock succ = i.next();
        			BlockStateType s = i.hasNext() ? current_state.clone() : current_state;
    				ContextType new_context = analysis.getEdgeTransferFunctions().transfer(block, succ, s);
    				if (new_context != null) {
    					c.propagateToBasicBlock(s, succ, new_context);
    				}
    			}
    		}
    		if (!deps.isFunctionActive(context.getEntryBlockAndContext()))
    			for (Pair<NodeAndContext<ContextType>,ContextType> nc : the_analysis_lattice_element.getCallGraph().getSources(context.getEntryBlockAndContext())) {
    				// callee has become inactive, so discharge the call edge 
    				deps.dischargeCallEdge(nc.getFirst().getNode().getBlock(), nc.getFirst().getContext(), nc.getSecond(), context.getEntryBlockAndContext());
    			}
    	}
		if (!terminatedEarly)
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
				block_loop: for (Map.Entry<ContextType, BlockStateType> me : the_analysis_lattice_element.getStates(block).entrySet()) {
					current_state = me.getValue().clone();
					ContextType context = me.getKey();
					if (global_entry_block == block)
						current_state.localize(null); // use *localized* initial state
					if (logger.isDebugEnabled()) 
						logger.debug("Context: " + context);
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
    public IAnalysisLatticeElement<BlockStateType, ContextType, CallEdgeType> getAnalysisLatticeElement() {
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
