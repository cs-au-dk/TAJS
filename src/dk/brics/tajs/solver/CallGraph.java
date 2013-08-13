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

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.Strings;

/**
 * Call graph.
 */
public class CallGraph<BlockStateType extends IBlockState<BlockStateType, ?, CallEdgeType>, 
	CallContextType extends ICallContext<?>, 
	CallEdgeType extends ICallEdge<BlockStateType>> {
	
	private static Logger logger = Logger.getLogger(CallGraph.class); 

	/**
	 * Map from (callee entry, callee context) to set of ((caller node, caller context), edge context).
	 */
	private Map<BlockAndContext<CallContextType>,Set<Pair<NodeAndContext<CallContextType>,CallContextType>>> call_sources; // default is empty maps
	
	/**
	 * Map from (caller node, caller context) to (callee entry, edge context) to call edge info.
	 * Note that this map uses edge contexts, not callee contexts.
	 */
	private Map<NodeAndContext<CallContextType>,Map<BlockAndContext<CallContextType>,CallEdgeType>> call_edge_info; // default is empty maps
	
	/**
	 * Map from basic block and context to occurrence order.
	 */
	private Map<BlockAndContext<CallContextType>,Integer> block_context_order;
	
	private int next_block_context_order;
	
	/**
	 * Constructs a new initially empty call graph.
	 */
	public CallGraph() {
		call_sources = newMap();
		call_edge_info = newMap();
		block_context_order = newMap();
	}
	
	/**
	 * Adds an edge from the given call node to the given function.
	 * @return the edge state delta
	 */
	public BlockStateType addTarget(AbstractNode caller, CallContextType caller_context, BasicBlock callee, CallContextType edge_context, 
			 BlockStateType edge_state, SolverSynchronizer sync, IAnalysis<BlockStateType, ?, CallEdgeType, ?, ?> analysis) {
        BlockStateType edge_state_diff = edge_state.clone();
		NodeAndContext<CallContextType> nc = new NodeAndContext<>(caller, caller_context);
		Map<BlockAndContext<CallContextType>,CallEdgeType> mb = call_edge_info.get(nc);
		if (mb == null) {
			mb = newMap();
			call_edge_info.put(nc, mb);
		}
		BlockAndContext<CallContextType> fc = new BlockAndContext<>(callee, edge_context);
		CallEdgeType call_edge = mb.get(fc); // old call edge state must be subsumed by the new edge state *modulo recovery operations*
		if (call_edge == null) {
			// new edge
			mb.put(fc, analysis.makeCallEdge(edge_state)); 
			if (sync != null && isOrdinaryCallEdge(callee))
				sync.callEdgeAdded(caller.getBlock().getFunction(), callee.getFunction());
		} else {
			// edge already exists, find the diff and overwrite the edge state
			edge_state_diff.remove(call_edge.getState()); 
			call_edge.setState(edge_state);
		}
		if (logger.isDebugEnabled()) 
			logger.debug((call_edge == null ? "adding" : "updating") + " call edge from node " + caller.getIndex() + " to " +
					(isOrdinaryCallEdge(callee) ? "function " : "for-in body ") + callee.getIndex() + " context " + edge_context);
		return edge_state_diff; // TODO: optionally use edge_state instead of edge_state_diff?
	}
	
	/**
	 * Adds a reverse edge.
	 * Also registers the callee and callee_context.
	 */
	public void addSource(AbstractNode caller, CallContextType caller_context, BasicBlock callee, CallContextType callee_context,
			CallContextType edge_context) {
		registerBlockContext(callee, callee_context);
		NodeAndContext<CallContextType> nc = new NodeAndContext<>(caller, caller_context);
		BlockAndContext<CallContextType> fc = new BlockAndContext<>(callee, callee_context);
		addToMapSet(call_sources, fc, Pair.make(nc, edge_context));
	}
	
	/**
	 * Checks whether the given callee belongs to an ordinary call edge (rather than a pseudo-edge from for-in loops). 
	 */
	private static boolean isOrdinaryCallEdge(BasicBlock callee) {
		return callee.isEntry();
	}

	/**
	 * Assigns an order to the given (basic block,context).
	 */
	public void registerBlockContext(BasicBlock b, CallContextType context) { // TODO: use proper topological order? (worklist order must be stable)
		BlockAndContext<CallContextType> fc = new BlockAndContext<>(b, context);
		if (!block_context_order.containsKey(fc))
			block_context_order.put(fc, next_block_context_order++);
	}
	
	/**
	 * Returns the occurrence order of the given (basic block,context).
	 */
	public int getBlockContextOrder(BasicBlock b, CallContextType context) {
		Integer order = block_context_order.get(new BlockAndContext<>(b, context));
		if (order == null)
			throw new AnalysisException("Unexpected basic block and context: block " + b.getIndex() + " at " + b.getSourceLocation() + ", " + context);
		return order;
	}
	
	/**
	 * Returns the call nodes, caller contexts, and edge contexts that have the given basic block as target for a given callee context.
	 */
	public Set<Pair<NodeAndContext<CallContextType>,CallContextType>> getSources(BasicBlock b, CallContextType callee_context) {
		Set<Pair<NodeAndContext<CallContextType>,CallContextType>> s = call_sources.get(new BlockAndContext<>(b, callee_context));
		if (s == null)
			return Collections.emptySet();
		return s;
	}

	/**
	 * Returns the specified call edge info.
	 */
	public CallEdgeType getCallEdge(AbstractNode caller, CallContextType caller_context, BasicBlock callee, CallContextType edge_context) {
		Map<BlockAndContext<CallContextType>, CallEdgeType> mb = getCallEdges(caller, caller_context);
		CallEdgeType b = mb.get(new BlockAndContext<>(callee, edge_context));
		if (b == null)
			throw new AnalysisException("No such edge!?");
		return b;
	}

	/**
	 * Returns the specified map from (callee entry, edge context) to call edge info.
	 */
	public Map<BlockAndContext<CallContextType>, CallEdgeType> getCallEdges(AbstractNode caller, CallContextType caller_context) {
		Map<BlockAndContext<CallContextType>,CallEdgeType> mb = call_edge_info.get(new NodeAndContext<>(caller, caller_context));
		if (mb == null)
			throw new AnalysisException("No such edge!?");
		return mb;
	}
	
	/**
	 * Returns a textual description of this call graph.
	 * Contexts and pseudo-call-edges are disregarded in the output.
	 */
	@Override
	public String toString() { // TODO: sort the output
		StringBuilder b = new StringBuilder();
		for (Map.Entry<Function,Set<AbstractNode>> me : getReverseEdgesIgnoreContexts().entrySet()) {
			Function f = me.getKey();
			Set<AbstractNode> ns = me.getValue();
			b.append(f).append(" at ").append(f.getSourceLocation()).append(" may be called from:\n");
			for (AbstractNode n : ns)
				b.append("  ").append(n.getSourceLocation()).append("\n");
		}
		return b.toString();
	}
	
	private Map<Function,Set<AbstractNode>> getReverseEdgesIgnoreContexts() {
		Map<Function,Set<AbstractNode>> m = newMap();
		for (Map.Entry<BlockAndContext<CallContextType>,Set<Pair<NodeAndContext<CallContextType>,CallContextType>>> me : call_sources.entrySet()) {
			BasicBlock b = me.getKey().getBlock();
			if (isOrdinaryCallEdge(b)) {
				Function f = b.getFunction();
				Set<AbstractNode> s = m.get(f);
				if (s == null) {
					s = newSet();
					m.put(f, s);
				}
				for (Pair<NodeAndContext<CallContextType>,CallContextType> nc : me.getValue())
					s.add(nc.getFirst().getNode());
			}
		}
		return m;
	}
	
	private Map<AbstractNode,Set<Function>> getForwardEdgesIgnoreContexts() {
		Map<AbstractNode,Set<Function>> m = newMap();
		for (Map.Entry<NodeAndContext<CallContextType>, Map<BlockAndContext<CallContextType>, CallEdgeType>> me : call_edge_info.entrySet()) {
			AbstractNode n = me.getKey().getNode();
			Set<Function> s = m.get(n);
			if (s == null) {
				s = newSet();
				m.put(n, s);
			}
			for (BlockAndContext<CallContextType> fc : me.getValue().keySet()) {
				BasicBlock b = fc.getBlock();
				if (isOrdinaryCallEdge(b))
					s.add(b.getFunction());			
			}
		}
		return m;
	}
	
	/**
	 * Produces a Graphviz dot representation of this call graph.
	 * Contexts are disregarded in the output.
	 * Each function in the call graph is annotated with its source location.
	 */
	public void toDot(PrintWriter out) {
		toDot(out, true);
	}
	
	/**
	 * Produces a Graphviz dot representation of this call graph.
	 * Contexts and pseudo-call-edges are disregarded in the output.
	 * @param show_source_locations if true, each function will be annotated with its source location
	 */
	public void toDot(PrintWriter out, boolean show_source_locations) {
		out.println("digraph {");
		for (Map.Entry<Function,Set<AbstractNode>> me : getReverseEdgesIgnoreContexts().entrySet()) {
			Function f = me.getKey();
			Set<AbstractNode> ns = me.getValue();
			out.println("  f" + f.getIndex() + " [shape=box label=\"" + dotLabel(f, show_source_locations) + "\"]");
			Set<Function> fs = newSet();
			for (AbstractNode n : ns)
				fs.add(n.getBlock().getFunction());
			for (Function t : fs)
				out.println("  f" + t.getIndex() + " -> f" + f.getIndex());
		}
        out.println("  f0 [shape=box label=\"<main>\"]}");
	}
	
	private static String dotLabel(Function f, boolean show_source_location) {
		if (f.isMain())
			return "<main>";
		StringBuilder sb = new StringBuilder();
		sb.append(f.getName() != null ? Strings.escape(f.getName()) : "function");
		sb.append('(');
		boolean first = true;
		for (String p : f.getParameterNames()) {
			if (first)
				first = false;
			else
				sb.append(',');
			sb.append(Strings.escape(p));
		}
		sb.append(')');
		if (show_source_location)
			sb.append("\\n" + f.getSourceLocation());
		return sb.toString();
	}

	/**
	 * Returns the total number of method invocation nodes.
	 * @param single_target_only if true, only count invocations with unique target
	 */
	private int getNumberOfInvocations(FlowGraph fg, boolean single_target_only) { // FIXME: ok to ignore calls to host functions?
		Map<AbstractNode,Set<Function>> m = getForwardEdgesIgnoreContexts();
		int res = 0;
		for (Function f : fg.getFunctions())
			for (BasicBlock b : f.getBlocks())
				for (AbstractNode n : b.getNodes())
					if (n instanceof CallNode && ((CallNode) n).getBaseRegister() != AbstractNode.NO_VALUE)  { // FIXME: Is NO_VALUE for arrays and regexps, but not function calls in general (trying to identify method vs. function call)
						Set<Function> fs = m.get(n);
						if (fs != null && (single_target_only ? fs.size() == 1 : fs.size() >= 1))
							res++; // FIXME: was:  res +=  call_targets.get(n) != null ? call_targets.get(n).size() : 0;   (and not excluding those with 0 targets)
					}
		return res; // FIXME: impl. vs. javadoc?
	}

    /**
     * Return call graph statistics on the number of invocations in human readable form.
     */
    public String getCallGraphStatistics(FlowGraph fg) {
        int total_inv = getNumberOfInvocations(fg, false);
        int single_inv = getNumberOfInvocations(fg, true);
        StringBuilder sb = new StringBuilder();
        sb.append("Total invocations: ").append(total_inv).append("\n");
        sb.append("Total invocations with single target: ").append(single_inv).append("\n");
        sb.append("==> % single target invocations: ").append((100 * ((float) single_inv) / total_inv)).append("%\n");
        return sb.toString();
    }
    
	// TODO: CallGraph: make variants of toString and toDot that make a node for each (function,context) 
    
    /**
     * Visits all edges by the given visitor.
     */
    public void visitAllEdges(ICallEdge.Visitor<CallEdgeType> visitor) {
    	for (Map.Entry<NodeAndContext<CallContextType>,Map<BlockAndContext<CallContextType>,CallEdgeType>> me1 : call_edge_info.entrySet())
    		for (Map.Entry<BlockAndContext<CallContextType>,CallEdgeType> me2 : me1.getValue().entrySet()) 
    			visitor.visit(me1.getKey(), me2.getValue(), me2.getKey());
    }
}
