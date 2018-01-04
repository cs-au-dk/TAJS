/*
 * Copyright 2009-2018 Aarhus University
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

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Keeps track of call edges that await return flow.
 * <p>
 * A call edge is charged when new flow appears on the edge.
 * It is discharged when the callee has no blocks in the worklist, nor any outgoing charged call edges.
 * Return flow can safely ignore call edges that are not charged.
 */
class CallDependencies<ContextType extends IContext<ContextType>> {

    private static Logger log = Logger.getLogger(CallDependencies.class);

//	static { org.apache.log4j.LogManager.getLogger(CallDependencies.class).setLevel(org.apache.log4j.Level.DEBUG); }

    private final class Edge {

        private BasicBlock caller;

        private ContextType caller_context;

        private ContextType edge_context;

        private BasicBlock callee;

        private ContextType callee_context;

        public Edge(BasicBlock caller, ContextType caller_context, ContextType edge_context, BasicBlock callee, ContextType callee_context) {
            this.caller = caller;
            this.caller_context = caller_context;
            this.edge_context = edge_context;
            this.callee = callee;
            this.callee_context = callee_context;
        }

        public BlockAndContext<ContextType> getCallee() {
            return new BlockAndContext<>(callee, callee_context);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = callee.getIndex();
            result = prime * result + callee_context.hashCode();
            result = prime * result + caller.getIndex();
            result = prime * result + caller_context.hashCode();
            result = prime * result + edge_context.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            @SuppressWarnings("unchecked")
            Edge other = (Edge) obj;
            if (!callee.equals(other.callee))
                return false;
            if (!callee_context.equals(other.callee_context))
                return false;
            if (!caller.equals(other.caller))
                return false;
            if (!caller_context.equals(other.caller_context))
                return false;
            return edge_context.equals(other.edge_context);
        }

        @Override
        public String toString() {
            return "(" + caller + " " + caller.getSourceLocation() + ", " + caller_context + ", " + edge_context + ", " + callee + ", " + callee_context + ")";
        }
    }

    /**
     * Charged call edges.
     */
    private Set<Edge> charged_call_edges;

    /**
     * Map from function entry to its outgoing charged call edges.
     */
    private Map<BlockAndContext<ContextType>, Set<Edge>> charged_call_edges_forward_map;

    /**
     * Map from function entry to its incoming charged call edges.
     */
    private Map<BlockAndContext<ContextType>, Set<Edge>> charged_call_edges_backward_map;

    /**
     * Number of items in the worklist for the given function entry.
     */
    private Map<BlockAndContext<ContextType>, Integer> function_activity_level;

    public CallDependencies() {
        if (!Options.get().isChargedCallsDisabled()) {
            charged_call_edges = newSet();
            charged_call_edges_forward_map = newMap();
            charged_call_edges_backward_map = newMap();
            function_activity_level = newMap();
        }
    }

    /**
     * Records a call edge that awaits return flow.
     * Has no effect if charged edges are disabled.
     */
    public void chargeCallEdge(BasicBlock caller, ContextType caller_context, ContextType edge_context, BasicBlock callee, ContextType callee_context) {
        if (Options.get().isChargedCallsDisabled())
            return;
        Edge e = new Edge(caller, caller_context, edge_context, callee, callee_context);
        if (charged_call_edges.add(e)) {
            BlockAndContext<ContextType> caller_entry = BlockAndContext.makeEntry(caller, caller_context);
            BlockAndContext<ContextType> callee_entry = BlockAndContext.makeEntry(callee, callee_context);
            addToMapSet(charged_call_edges_forward_map, caller_entry, e);
            addToMapSet(charged_call_edges_backward_map, callee_entry, e);
            if (log.isDebugEnabled())
                log.debug("charging call edge " + e);
        }
    }

    private void dischargeCallEdge(Edge e) {
        if (charged_call_edges.remove(e)) {
            BlockAndContext<ContextType> caller_entry = BlockAndContext.makeEntry(e.caller, e.caller_context);
            BlockAndContext<ContextType> callee_entry = BlockAndContext.makeEntry(e.callee, e.callee_context);
            Set<Edge> sf = charged_call_edges_forward_map.get(caller_entry);
            if (sf == null)
                throw new AnalysisException("unexpected null set");
            sf.remove(e);
            if (sf.isEmpty()) {
                charged_call_edges_forward_map.remove(caller_entry);
            }
            Set<Edge> sb = charged_call_edges_backward_map.get(callee_entry);
            if (sb == null)
                throw new AnalysisException("unexpected null set");
            sb.remove(e);
            if (sb.isEmpty()) {
                charged_call_edges_backward_map.remove(callee_entry);
            }
            if (log.isDebugEnabled())
                log.debug("discharging call edge " + e);
            dischargeIfInactive(caller_entry);
        }
    }

    /**
     * Discharges the incoming call edges if the function is inactive.
     * Has no effect if charged edges are disabled.
     */
    public void dischargeIfInactive(BlockAndContext<ContextType> entry) {
        if (Options.get().isChargedCallsDisabled())
            return;
        if (!isFunctionActive(entry)) {
            Set<Edge> es = charged_call_edges_backward_map.get(entry);
            if (es != null) {
                for (Edge f : newList(es)) {
                    dischargeCallEdge(f);
                }
            }
        }
    }

    /**
     * Checks whether the given edge is charged.
     * Always returns true if charged edges are disabled.
     */
    public boolean isCallEdgeCharged(BasicBlock caller, ContextType caller_context, ContextType edge_context, BasicBlock callee, ContextType callee_context) {
        if (Options.get().isChargedCallsDisabled())
            return true;
        return charged_call_edges.contains(new Edge(caller, caller_context, edge_context, callee, callee_context));
    }

    private void addToFunctionActivityLevel(BlockAndContext<ContextType> bc, int value) {
        Integer i = function_activity_level.get(bc);
        if (i == null)
            i = 0;
        i += value;
        if (i != 0) {
            if (i < 0)
                throw new AnalysisException("negative function activity level for " + bc);
            function_activity_level.put(bc, i);
        } else
            function_activity_level.remove(bc);
        if (log.isDebugEnabled())
            log.debug("function activity level for " + bc + ": " + i);
    }

    /**
     * Increments the function activity level for the given function and context.
     */
    public void incrementFunctionActivityLevel(BlockAndContext<ContextType> bc) {
        if (Options.get().isChargedCallsDisabled())
            return;
        addToFunctionActivityLevel(bc, 1);
    }

    /**
     * Decrements the function activity level for the given function and context.
     */
    public void decrementFunctionActivityLevel(BlockAndContext<ContextType> bc) {
        if (Options.get().isChargedCallsDisabled())
            return;
        addToFunctionActivityLevel(bc, -1);
    }

    /**
     * Checks whether the given function and context pair is active,
     * i.e. if a function is reachable along charged edges and the function contains a location that is in the worklist.
     * Always returns true if charged calls are disabled.
     */
    public boolean isFunctionActive(BlockAndContext<ContextType> bc) {
        if (Options.get().isChargedCallsDisabled())
            return true;
        return isFunctionActive(bc, Collections.newSet());
    }

    private boolean isFunctionActive(BlockAndContext<ContextType> bc, Set<BlockAndContext<ContextType>> visited) {
        if (visited.contains(bc))
            return false;
        visited.add(bc);
        // 1) the function is active if its worklist activity is positive
        if (function_activity_level.containsKey(bc))
            return true;
        // 2) the function is active if it contains a charged outgoing edge to an active function (inductively)
        Set<Edge> edges = charged_call_edges_forward_map.get(bc);
        if (edges != null)
            for (Edge e : edges)
                if (isFunctionActive(e.getCallee(), visited))
                    return true;
        // 3) otherwise it is inactive
        return false;
    }

    /**
     * Checks whether all functions are inactive.
     *
     * @throws AnalysisException if not all functions are not inactive
     */
    public void assertEmpty() {
        if (Options.get().isChargedCallsDisabled())
            return;
        if (!charged_call_edges.isEmpty())
            throw new AnalysisException("unexpected charged call edges: " + charged_call_edges);
        if (!function_activity_level.isEmpty())
            throw new AnalysisException("unexpected active functions: " + function_activity_level);
    }
}
