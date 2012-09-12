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

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;

/**
 * Keeps track of call edges that await return flow.
 * 
 * A call edge is charged when new flow appears on the edge. 
 * It is discharged when the callee has no blocks in the worklist, nor any outgoing charged call edges. 
 * Return flow can safely ignore call edges that are not charged.
 */
public class CallDependencies<CallContextType extends ICallContext<CallContextType>> {
	
	private static Logger logger = Logger.getLogger(CallDependencies.class); 

//	static { org.apache.log4j.LogManager.getLogger(CallDependencies.class).setLevel(org.apache.log4j.Level.DEBUG); }

	private final class Edge {

		private AbstractNode caller;
		
		private CallContextType caller_context;
		
		private Function callee;
		
		private CallContextType callee_context;
		
		public Edge(AbstractNode caller, CallContextType caller_context, Function callee, CallContextType callee_context) {
			this.caller = caller;
			this.caller_context = caller_context;
			this.callee = callee;
			this.callee_context = callee_context;
		}

		public Function getCallee() {
			return callee;
		}

		public CallContextType getCalleeContext() {
			return callee_context;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = callee.getIndex();
			result = prime * result + callee_context.hashCode();
			result = prime * result + caller.getIndex();
			result = prime * result + caller_context.hashCode();
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
			return true;
		}

		@Override
		public String toString() {
			return "(" + caller + " " + caller.getSourceLocation() + ", " + caller_context + ", " + callee + ", " + callee_context + ")";
		}		
	}

	/**
	 * Charged call edges.
	 */
	private Set<Edge> charged_call_edges;
	
	/**
	 * Map from function entry to its outgoing charged call edges.
	 */
	private Map<BlockAndContext<CallContextType>,Set<Edge>> charged_call_edges_map;
	
	/**
	 * Number of items in the worklist for the given function entry.
	 */
	private Map<BlockAndContext<CallContextType>,Integer> function_activity_level;

	public CallDependencies() {
		if (!Options.isChargedCallsDisabled()) {
			charged_call_edges = newSet();
			charged_call_edges_map = newMap();
			function_activity_level = newMap();
		}		
	}

    /**
     * Records a call edge that awaits return flow.
     * Has no effect if charged edges are disabled.
     */
    public void chargeCallEdge(AbstractNode caller, CallContextType caller_context, Function callee, CallContextType callee_context) {
		if (Options.isChargedCallsDisabled())
			return;
		Edge e = new Edge(caller, caller_context, callee, callee_context);
		if (charged_call_edges.add(e)) {
			BlockAndContext<CallContextType> caller_entry = new BlockAndContext<>(caller.getBlock().getFunction().getEntry(), caller_context.toEntry(caller.getBlock()).getContext());
			Set<Edge> s = charged_call_edges_map.get(caller_entry);
			if (s == null) {
				s = newSet();
				charged_call_edges_map.put(caller_entry, s);
			}
			s.add(e);
			logger.debug("charging call edge " + e);
		}
    }
	
    /**
     * Discharges return flow for a call edge.
     * Has no effect if charged edges are disabled.
     */
    public void dischargeCallEdge(AbstractNode caller, CallContextType caller_context, Function callee, CallContextType callee_context) {
		if (Options.isChargedCallsDisabled())
			return;
		Edge e = new Edge(caller, caller_context, callee, callee_context);
		if (charged_call_edges.remove(e)) {
			BlockAndContext<CallContextType> caller_entry = new BlockAndContext<>(caller.getBlock().getFunction().getEntry(), caller_context.toEntry(caller.getBlock()).getContext());
			Set<Edge> s = charged_call_edges_map.get(caller_entry);
			if (s == null)
				throw new AnalysisException("unexpected null set");
			s.remove(e);
			if (s.isEmpty())
				charged_call_edges_map.remove(caller_entry);
			logger.debug("discharging call edge " + e);
		}
    }
    
    /**
     * Checks whether the given edge is charged.
     * Always returns true if charged edges are disabled.
     */
    public boolean isCallEdgeCharged(AbstractNode caller, CallContextType caller_context, Function callee, CallContextType callee_context) {
		if (Options.isChargedCallsDisabled())
			return true;
		return charged_call_edges.contains(new Edge(caller, caller_context, callee, callee_context));
    }
    
    private void addToFunctionActivityLevel(Function f, CallContextType c, int value) {
    	BlockAndContext<CallContextType> bc = new BlockAndContext<>(f.getEntry(), c);
    	Integer i = function_activity_level.get(bc);
    	if (i == null)
    		i = 0;
    	i += value;
    	if (i != 0) {
    		if (i < 0)
    			throw new AnalysisException("negative function activity level for " + f + " " + f.getSourceLocation() + ", " + c);
    		function_activity_level.put(bc, i);
    	} else
    		function_activity_level.remove(bc);
    	logger.debug("function activity level for " + f + " " + f.getSourceLocation() + ", " + c + ": " + i);
    }

    /**
     * Increments the function activity level for the given function and context.
     */
    public void incrementFunctionActivityLevel(Function f, CallContextType c) {
		if (Options.isChargedCallsDisabled())
			return;
    	addToFunctionActivityLevel(f, c, 1);
    }
    
    /**
     * Decrements the function activity level for the given function and context.
     */
    public void decrementFunctionActivityLevel(Function f, CallContextType c) {
		if (Options.isChargedCallsDisabled())
			return;
    	addToFunctionActivityLevel(f, c, -1);
    }
    
    /**
     * Checks whether the given function and context pair is active, 
     * i.e. if a function is reachable along charged edges and the function contains a location that is in the worklist.
     * Always returns true if charged calls are disabled.
     */
    public boolean isFunctionActive(Function f, CallContextType c) {
		if (Options.isChargedCallsDisabled())
			return true;
		return isFunctionActive(f, c, Collections.<BlockAndContext<CallContextType>>newSet());
    }
    
    private boolean isFunctionActive(Function f, CallContextType c, Set<BlockAndContext<CallContextType>> visited) {
    	BlockAndContext<CallContextType> b = new BlockAndContext<>(f.getEntry(), c);
    	if (visited.contains(b))
    		return false;
    	visited.add(b);
    	// 1) the function is active if its worklist activity is positive
    	if (function_activity_level.containsKey(b))
    		return true;
    	// 2) the function is active if it contains a charged outgoing edge to an active function (inductively)
    	Set<Edge> edges = charged_call_edges_map.get(b);
    	if (edges != null)
    		for (Edge e : edges)
    			if (isFunctionActive(e.getCallee(), e.getCalleeContext(), visited))
    				return true;
    	// 3) otherwise it is inactive
    	return false;
    }

    /**
     * Checks whether all functions are inactive.
     * @throws AnalysisException if not all functions are not inactive
     */
    public void assertEmpty() {
		if (Options.isChargedCallsDisabled())
			return;
		if (!charged_call_edges.isEmpty()) // there may be charged call edges when analysis has completed (due to unfortunate worklist order)
			logger.debug("remaining charged call edges: " + charged_call_edges);
		if (!function_activity_level.isEmpty())
			throw new AnalysisException("unexpected active functions: " + function_activity_level);
    }
}
