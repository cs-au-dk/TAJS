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

package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.Strings;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Call graph.
 */
public class CallGraph<BlockStateType extends IBlockState<BlockStateType, ?, CallEdgeType>,
        ContextType extends IContext<?>,
        CallEdgeType extends ICallEdge<BlockStateType>> {

    private static Logger log = Logger.getLogger(CallGraph.class);

    /**
     * Map from (callee entry, callee context) to set of ((caller node, caller context), edge context).
     */
    private Map<BlockAndContext<ContextType>, Set<Pair<NodeAndContext<ContextType>, ContextType>>> call_sources; // default is empty maps

    /**
     * Map from (caller node, caller context) to (callee entry, edge context) to call edge info.
     * Note that this map uses edge contexts, not callee contexts.
     */
    private Map<NodeAndContext<ContextType>, Map<BlockAndContext<ContextType>, CallEdgeType>> call_edge_info; // default is empty maps

    /**
     * Map from basic block and context to occurrence order.
     */
    private Map<BlockAndContext<ContextType>, Integer> block_context_order;

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
     *
     * @return true if the call edge changed as result of this operation
     */
    public boolean addTarget(AbstractNode caller, ContextType caller_context, BasicBlock callee, ContextType edge_context,
                             BlockStateType edge_state, SolverSynchronizer sync, IAnalysis<BlockStateType, ?, CallEdgeType, ?, ?, ?> analysis) {
        boolean changed;
        NodeAndContext<ContextType> nc = new NodeAndContext<>(caller, caller_context);
        Map<BlockAndContext<ContextType>, CallEdgeType> mb = call_edge_info.get(nc);
        if (mb == null) {
            mb = newMap();
            call_edge_info.put(nc, mb);
        }
        BlockAndContext<ContextType> fc = new BlockAndContext<>(callee, edge_context);
        CallEdgeType call_edge = mb.get(fc); // old call edge state must be subsumed by the new edge state *modulo recovery operations*
        if (call_edge == null) {
            // new edge
            mb.put(fc, analysis.makeCallEdge(edge_state.clone()));
            if (sync != null && isOrdinaryCallEdge(callee))
                sync.callEdgeAdded(caller.getBlock().getFunction(), callee.getFunction());
            changed = true;
        } else {
            // propagate into existing edge
            changed = call_edge.getState().propagate(edge_state, true);
        }
        if (log.isDebugEnabled())
            log.debug((call_edge == null ? "adding" : "updating") + " call edge from node " + caller.getIndex() + " to " +
                    (isOrdinaryCallEdge(callee) ? "function " : "for-in body ") + callee.getIndex() + " context " + edge_context);
        return changed;
    }

    /**
     * Adds a reverse edge.
     */
    public void addSource(AbstractNode caller, ContextType caller_context, BasicBlock callee, ContextType callee_context,
                          ContextType edge_context) {
        NodeAndContext<ContextType> nc = new NodeAndContext<>(caller, caller_context);
        BlockAndContext<ContextType> fc = new BlockAndContext<>(callee, callee_context);
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
    public void registerBlockContext(BasicBlock b, ContextType context) {
        BlockAndContext<ContextType> fc = new BlockAndContext<>(b, context);
        if (!block_context_order.containsKey(fc))
            block_context_order.put(fc, next_block_context_order++);
    }

    /**
     * Returns the occurrence order of the given (basic block,context).
     */
    public int getBlockContextOrder(BlockAndContext<ContextType> bc) {
        Integer order = block_context_order.get(bc);
        if (order == null)
            throw new AnalysisException("Unexpected basic block and context: " + bc);
        return order;
    }

    /**
     * Returns the call nodes, caller contexts, and edge contexts that have the given basic block as target for a given callee context.
     */
    public Set<Pair<NodeAndContext<ContextType>, ContextType>> getSources(BlockAndContext<ContextType> bc) {
        Set<Pair<NodeAndContext<ContextType>, ContextType>> s = call_sources.get(bc);
        if (s == null)
            return Collections.emptySet();
        return s;
    }

    /**
     * Returns the specified call edge info.
     */
    public CallEdgeType getCallEdge(AbstractNode caller, ContextType caller_context, BasicBlock callee, ContextType edge_context) {
        Map<BlockAndContext<ContextType>, CallEdgeType> mb = getCallEdges(caller, caller_context);
        CallEdgeType b = mb.get(new BlockAndContext<>(callee, edge_context));
        if (b == null)
            throw new AnalysisException("No such edge!?");
        return b;
    }

    /**
     * Returns the specified map from (callee entry, edge context) to call edge info.
     */
    public Map<BlockAndContext<ContextType>, CallEdgeType> getCallEdges(AbstractNode caller, ContextType caller_context) {
        Map<BlockAndContext<ContextType>, CallEdgeType> mb = call_edge_info.get(new NodeAndContext<>(caller, caller_context));
        if (mb == null)
            throw new AnalysisException("No such edge!?");
        return mb;
    }

    /**
     * Returns a textual description of this call graph.
     * Contexts and pseudo-call-edges are disregarded in the output.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<Function, List<AbstractNode>> me : sort(getReverseEdgesIgnoreContexts().entrySet())) {
            Function f = me.getKey();
            List<AbstractNode> ns = me.getValue();
            b.append(f).append(" at ").append(f.getSourceLocation()).append(" may be called from:\n");
            for (AbstractNode n : ns)
                b.append("  ").append(n.getSourceLocation()).append("\n");
        }
        return b.toString();
    }

    private Map<Function, Set<AbstractNode>> getReverseEdgesIgnoreContexts() {
        Map<Function, Set<AbstractNode>> m = newMap();
        for (Map.Entry<BlockAndContext<ContextType>, Set<Pair<NodeAndContext<ContextType>, ContextType>>> me : call_sources.entrySet()) {
            BasicBlock b = me.getKey().getBlock();
            if (isOrdinaryCallEdge(b)) {
                Function f = b.getFunction();
                Set<AbstractNode> s = m.get(f);
                if (s == null) {
                    s = newSet();
                    m.put(f, s);
                }
                for (Pair<NodeAndContext<ContextType>, ContextType> nc : me.getValue())
                    s.add(nc.getFirst().getNode());
            }
        }
        return m;
    }

    private static List<Map.Entry<Function, List<AbstractNode>>> sort(Set<Map.Entry<Function, Set<AbstractNode>>> s) {
        List<Map.Entry<Function, List<AbstractNode>>> res = newList();
        for (Map.Entry<Function, Set<AbstractNode>> me : s) {
            List<AbstractNode> ns = newList(me.getValue());
            ns.sort(new Comparator<AbstractNode>() {
                @Override
                public int compare(AbstractNode n1, AbstractNode n2) {
                    return n1.getSourceLocation().compareTo(n2.getSourceLocation());
                }
            });
            res.add(new AbstractMap.SimpleEntry<>(me.getKey(), ns));
        }
        res.sort(new Comparator<Map.Entry<Function, List<AbstractNode>>>() {
            @Override
            public int compare(Entry<Function, List<AbstractNode>> o1, Entry<Function, List<AbstractNode>> o2) {
                return o1.getKey().getSourceLocation().compareTo(o2.getKey().getSourceLocation());
            }
        });
        return res;
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
     *
     * @param show_source_locations if true, each function will be annotated with its source location
     */
    public void toDot(PrintWriter out, boolean show_source_locations) {
        out.println("digraph {");
        for (Map.Entry<Function, Set<AbstractNode>> me : getReverseEdgesIgnoreContexts().entrySet()) {
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
     * Returns the total number of call nodes with reachable contexts.
     * Each call node is counted once for each reachable context.
     *
     * @param max_targets if nonzero, only include calls with at most max_targets target functions
     */
    public int getNumberOfInvocationsInDifferentContexts(int max_targets) {
        int c = 0;
        for (Map.Entry<NodeAndContext<ContextType>, Map<BlockAndContext<ContextType>, CallEdgeType>> me : call_edge_info.entrySet()) {
            AbstractNode n = me.getKey().getNode();
            if (n instanceof CallNode
                    && ((CallNode) n).getBaseRegister() != AbstractNode.NO_VALUE) { // skip array/regexp literals
                if (max_targets == 0) {
                    c++;
                } else {
                    Set<BasicBlock> targets = newSet();
                    for (BlockAndContext<ContextType> bc : me.getValue().keySet()) {
                        targets.add(bc.getBlock());
                    }
                    if (targets.size() <= max_targets)
                        c++;
                }
            }
        }
        return c;
    }

    /**
     * Return call graph statistics on the number of invocations in human readable form.
     */
    public String getCallGraphStatistics() {
        StringBuilder sb = new StringBuilder();
        int total = getNumberOfInvocationsInDifferentContexts(0);
        int single = getNumberOfInvocationsInDifferentContexts(1);
        sb.append("Total invocations: ").append(total).append("\n");
        sb.append("Total invocations with single target: ").append(single).append("\n");
        sb.append("==> % single target invocations: ").append((100 * ((float) single) / total)).append("%\n");
        return sb.toString();
    }

}
