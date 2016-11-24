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

package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.IAnalysisLatticeElement.MergeResult;
import dk.brics.tajs.util.AnalysisException;
import net.htmlparser.jericho.Source;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.function.Supplier;

/**
 * Generic fixpoint solver for flow graphs.
 */
public class GenericSolver<StateType extends IState<StateType, ContextType, CallEdgeType>,
        ContextType extends IContext<ContextType>,
        CallEdgeType extends ICallEdge<StateType>,
        MonitoringType extends ISolverMonitoring<StateType, ContextType>,
        AnalysisType extends IAnalysis<StateType, ContextType, CallEdgeType, MonitoringType, AnalysisType>> {

    private static Logger log = Logger.getLogger(GenericSolver.class);

    private final AnalysisType analysis;

    private final SolverSynchronizer sync;

    private FlowGraph flowgraph;

    private BasicBlock global_entry_block;

    private IAnalysisLatticeElement<StateType, ContextType, CallEdgeType> the_analysis_lattice_element;

    private WorkList<ContextType> worklist;

    private CallDependencies<ContextType> deps;

    private AbstractNode current_node;

    private StateType current_state;

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

        private SolverInterface() {
        }

        /**
         * Returns the node currently being visited.
         */
        public AbstractNode getNode() {
            if (current_node == null)
                throw new AnalysisException("Unexpected call to getNode");
            return current_node;
        }

        /**
         * Returns the current abstract state.
         */
        public StateType getState() {
            return current_state;
        }

        /**
         * Sets the current abstract state.
         */
        public void setState(StateType state) {
            current_state = state;
        }

        /**
         * Runs the given supplier function with the given state set to current.
         */
        public <T> T withState(StateType state, Supplier<T> fun) {
            StateType old = current_state;
            current_state = state;
            T res = fun.get();
            current_state = old;
            return res;
        }

        /**
         * Runs the given function with the given state set to current.
         */
        public void withState(StateType state, Runnable fun) {
            withState(state, () -> {
                fun.run();
                return null;
            });
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
        public IAnalysisLatticeElement<StateType, ContextType, CallEdgeType> getAnalysisLatticeElement() {
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
        public void propagateToBasicBlock(StateType state, BasicBlock block, ContextType context) {
            if (messages_enabled)
                return;
            propagate(state, block, context, false);
        }

        /**
         * Propagates dataflow.
         */
        private void propagate(StateType state, BasicBlock block, ContextType context, boolean localize) {
            MergeResult res = the_analysis_lattice_element.propagate(state, block, context, localize);
            the_analysis_lattice_element.getCallGraph().registerBlockContext(block, context);
            if (res != null) {
                addToWorklist(block, context);
                if (sync != null)
                    sync.markPendingBlock(block);
                analysis.getMonitoring().visitNewFlow(block, context, the_analysis_lattice_element.getState(block, context), res.getDiff(), "CALL");
                if (log.isDebugEnabled())
                    log.debug("New flow at block " + block.getIndex() + " node "
                            + block.getFirstNode().getIndex() + ", context " + context
                            + (res.getDiff() != null ? ", diff:" + res.getDiff() : ""));
            }
        }

        /**
         * Adds the given location to the worklist.
         */
        public void addToWorklist(BasicBlock block, ContextType context) {
            if (worklist.add(worklist.new Entry(block, context)))
                deps.incrementFunctionActivityLevel(BlockAndContext.makeEntry(block, context));
        }

        /**
         * Merges the edge state into the entry state of the callee in the given context and updates the work list accordingly.
         * Also updates the call graph and triggers reevaluation of ordinary/exceptional return flow.
         * The given state may be modified by this operation.
         * Ignored if in scan phase.
         */
        public void propagateToFunctionEntry(AbstractNode call_node, ContextType caller_context, StateType edge_state,
                                             ContextType edge_context, BasicBlock callee_entry, boolean implicit) {
            if (messages_enabled)
                return;
            CallGraph<StateType, ContextType, CallEdgeType> cg = the_analysis_lattice_element.getCallGraph();
            // add to existing call edge
            if (cg.addTarget(call_node, caller_context, callee_entry, edge_context, edge_state, sync, analysis)) {
                // new flow at call edge, transform it relative to the function entry states and contexts
                ContextType callee_context = edge_state.transform(cg.getCallEdge(call_node, caller_context, callee_entry, edge_context),
                        edge_context, the_analysis_lattice_element.getStates(callee_entry), callee_entry);
                cg.addSource(call_node, caller_context, callee_entry, callee_context, edge_context, implicit);
                // propagate transformed state into function entry
                propagate(edge_state, callee_entry, callee_context, true);
                // charge the call edge  
                deps.chargeCallEdge(call_node.getBlock(), caller_context, edge_context, callee_entry, callee_context);
                // process existing ordinary/exceptional return flow
                StateType stored_state = current_state;
                AbstractNode stored_node = current_node;
                current_state = null;
                current_node = null;
                analysis.getNodeTransferFunctions().transferReturn(call_node, callee_entry, caller_context, callee_context, edge_context, implicit);
                current_state = stored_state;
                current_node = stored_node;
            }
        }

        /**
         * Transforms the given state inversely according to the call edge.
         */
        public void returnFromFunctionExit(StateType return_state, AbstractNode call_node, ContextType caller_context,
                                           BasicBlock callee_entry, ContextType edge_context, boolean implicit) {
            CallEdgeType edge = the_analysis_lattice_element.getCallGraph().getCallEdge(call_node, caller_context, callee_entry, edge_context);
            if (return_state.transformInverse(edge, callee_entry, return_state.getContext())) {
                // need to re-process the incoming flow at function entry
                propagateToFunctionEntry(call_node, caller_context, edge.getState(), edge_context, callee_entry, implicit);
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
    public void init(FlowGraph fg, Source document) {
        if (the_analysis_lattice_element != null)
            throw new IllegalStateException("init() called repeatedly");

        flowgraph = fg;
        global_entry_block = fg.getEntryBlock();
        the_analysis_lattice_element = analysis.makeAnalysisLattice(fg);
        analysis.initContextSensitivity(fg);
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
        int nodeTransfers = 0;
        boolean terminatedEarly = false;
        // iterate until fixpoint
        block_loop:
        while (!worklist.isEmpty()) {
            if (!analysis.getMonitoring().allowNextIteration()) {
                if (!Options.get().isQuietEnabled()) {
                    log.warn("Terminating fixpoint solver early and unsoundly");
                }
                terminatedEarly = true;
                break;
            }
            if (sync != null) {
                if (sync.isSingleStep())
                    if (log.isDebugEnabled())
                        log.debug("Worklist: " + worklist);
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
            deps.decrementFunctionActivityLevel(BlockAndContext.makeEntry(block, context));
            StateType state = the_analysis_lattice_element.getState(block, p.getContext());
            if (state == null)
                throw new AnalysisException();
            if (log.isDebugEnabled()) {
                log.debug("Selecting worklist entry for block " + block.getIndex() + " at " + block.getSourceLocation());
                log.debug("Worklist: " + worklist);
                log.debug("Visiting " + block);
//    			log.debug("Number of abstract states at this block: " + the_analysis_lattice_element.getSize(block));
                log.debug("Context: " + context);
            } else if (!Options.get().isQuietEnabled() && !Options.get().isTestEnabled() && log.isInfoEnabled()) {
//    			if (block.isEntry())
//    				log.debug("Entering " + block.getFunction() + " at " + block.getFunction().getSourceLocation());
//    			if (block.isOrdinaryExit())
//    				log.debug("Returning from " + block.getFunction() + " at " + block.getFunction().getSourceLocation());
//    			if (block.isExceptionalExit())
//    				log.debug("Exception from " + block.getFunction() + " at " + block.getFunction().getSourceLocation());
                log.info(//"block " + block.getIndex() + " at " +
                        block.getSourceLocation() +
//    					", context " + context +
                                " (node transfers: " + (nodeTransfers + 1) +
//    					" (avg/node: " + ((float) ((analysis.getMonitoring().getTotalNumberOfNodeTransfers() + 1) * 1000 / flowgraph.getNumberOfNodes())) / 1000 + ")" + 
                                ", worklist size: " + (worklist.size() + 1) +
//    					", contexts: " + the_analysis_lattice_element.getSize(block) +
                                ")");
            }
            // basic block transfer
            analysis.getMonitoring().visitBlockTransfer(block, state);
            current_state = state.clone();
            if (global_entry_block == block)
                current_state.localize(null); // use *localized* initial state
            if (Options.get().isIntermediateStatesEnabled())
                if (log.isDebugEnabled())
                    log.debug("Before block transfer: " + current_state);
            for (AbstractNode n : block.getNodes()) {
                nodeTransfers++;
                current_node = n;
                if (log.isDebugEnabled())
                    log.debug("Visiting node " + current_node.getIndex() + ": "
                            + current_node + " at " + current_node.getSourceLocation());
                analysis.getNodeTransferFunctions().transfer(current_node);
                analysis.getMonitoring().visitNodeTransfer(current_node);
                if (current_state.isNone()) {
                    log.debug("No non-exceptional flow");
                    continue block_loop;
                }
                if (Options.get().isIntermediateStatesEnabled())
                    if (log.isDebugEnabled())
                        log.debug("After node transfer: " + current_state.toStringBrief());
            }
            analysis.getMonitoring().visitPostBlockTransfer(block, current_state);
            // edge transfer
            for (Iterator<BasicBlock> i = block.getSuccessors().iterator(); i.hasNext(); ) {
                BasicBlock succ = i.next();
                StateType s = i.hasNext() ? current_state.clone() : current_state;
                ContextType new_context = analysis.getEdgeTransferFunctions().transfer(block, succ, s);
                if (new_context != null) {
                    c.propagateToBasicBlock(s, succ, new_context);
                }
            }
            if (!deps.isFunctionActive(BlockAndContext.makeEntry(block, context)))
                for (CallGraph.ReverseEdge<ContextType> re : the_analysis_lattice_element.getCallGraph().getSources(BlockAndContext.makeEntry(block, context))) {
                    // callee has become inactive, so discharge the call edge
                    deps.dischargeCallEdge(re.getCallNode().getBlock(), re.getCallerContext(), re.getEdgeContext(), BlockAndContext.makeEntry(block, context));
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
        // visit each block
        for (Function function : flowgraph.getFunctions()) {
            if (log.isDebugEnabled())
                log.debug("Scanning " + function + " at " + function.getSourceLocation());
            analysis.getMonitoring().visitFunction(function, the_analysis_lattice_element.getStates(function.getEntry()).values());
            for (BasicBlock block : function.getBlocks()) {
                if (log.isDebugEnabled())
                    log.debug("Scanning " + block + " at " + block.getSourceLocation());
                block_loop:
                for (Entry<ContextType, StateType> me : the_analysis_lattice_element.getStates(block).entrySet()) {
                    current_state = me.getValue().clone();
                    ContextType context = me.getKey();
                    if (global_entry_block == block)
                        current_state.localize(null); // use *localized* initial state
                    if (log.isDebugEnabled()) {
                        log.debug("Context: " + context);
                        if (Options.get().isIntermediateStatesEnabled())
                            log.debug("Before block transfer: " + current_state);
                    }
                    for (AbstractNode node : block.getNodes()) {
                        current_node = node;
                        if (log.isDebugEnabled())
                            log.debug("node " + current_node.getIndex() + ": " + current_node);
                        if (current_state.isNone())
                            continue block_loop; // unreachable, so skip the rest of the block
                        analysis.getMonitoring().visitReachableNode(node);
                        analysis.getNodeTransferFunctions().transfer(node);
                    }
                    analysis.getMonitoring().visitPostBlockTransfer(block, current_state);
                }
            }
        }
    }

    /**
     * Returns the analysis lattice element.
     * {@link #solve()} must be called first.
     */
    public IAnalysisLatticeElement<StateType, ContextType, CallEdgeType> getAnalysisLatticeElement() {
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
