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

package dk.brics.tajs.lattice;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.PropertyReference.Kind;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.solver.ICallContext;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Strings;

/**
 * Resolves 'unknown' and polymorphic properties.
 * Contains wrappers for certain methods in {@link Obj}.
 * Each method reads the appropriate property. If unknown/polymorphic, it is recovered via the function entry state in the call graph.
 */
public final class UnknownValueResolver {

	public static Logger logger = Logger.getLogger(UnknownValueResolver.class); 

	static { LogManager.getLogger(UnknownValueResolver.class).setLevel(Level.INFO); } // set to Level.DEBUG to enable debug output

	/**
	 * A recovery graph node is a triple of a basic block, a call context, and a property reference.
	 * The basic block and the call context identify a location in the program.
	 * The property reference denotes the desired property <i>at the function entry</i> relative to the basic block and call context location.
	 * (Note that "function entries" may be for-in body entries.)
	 */
	private static class Node<CallContextType extends ICallContext<CallContextType>> {

		private BasicBlock b;
		
		private CallContextType c;
		
		private PropertyReference p;
		
		public Node(BasicBlock b, CallContextType c, PropertyReference p) {
			this.b = b;
			this.c = c;
			this.p = p;
		}

		public BasicBlock getBlock() {
			return b;
		}
		
		public CallContextType getContext() {
			return c;
		}
		
		public PropertyReference getPropertyReference() {
			return p;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Node))
				return false;
			Node<CallContextType> ncp = (Node<CallContextType>) obj;
			return ncp.b == b && ncp.c.equals(c) && ncp.p.equals(p);
		}

		@Override
		public int hashCode() {
			return b.getIndex() * 13 + c.hashCode() * 3 + p.hashCode() * 5;
		}

		@Override
		public String toString() {
			return "{b=" + b.getIndex() + ", c=" + c + ", p=" + p + '}';
		}
	}

	/**
	 * Recovery graph.
	 * 
	 * Each node represents a property value to be recovered at some location.
	 * Each root is a node where (a part of) the desired value is available without further traversal.
	 * Each edge corresponds to relevant flow of the 'unknown' values.
	 */
	private static class RecoveryGraph<CallContextType extends ICallContext<CallContextType>> {

		/**
		 * The internal representation groups the callee nodes with same function entry.
		 */
		private Map<Node<CallContextType>, // target or call node
		Map<Node<CallContextType>, // callee function entry (groups callee nodes with same function entry)
		Set<Node<CallContextType>>>> graph; // callee node

		private Set<Node<CallContextType>> roots;

		private LinkedList<Node<CallContextType>> pending;
		
		/**
		 * Constructs a new empty recovery graph.
		 */
		public RecoveryGraph() {
			 graph = newMap();
			 roots = newSet();
			 pending = new LinkedList<>();
		}
		
		/**
		 * Marks the given node as a root.
		 */
		public void addRoot(Node<CallContextType> n) {
			roots.add(n);
			if (logger.isDebugEnabled()) 
				logger.debug("adding root " + n);
		}
		
		/**
		 * Returns the roots.
		 * (Modifications of the returned set will affect subsequent calls to the method.)
		 */
		public Set<Node<CallContextType>> getRoots() {
			return roots;
		}
		
		/**
		 * Adds a node (caller) and updates the pending list accordingly.
		 */
		public void addNode(Node<CallContextType> n) {
			graph.put(n, dk.brics.tajs.util.Collections.<Node<CallContextType>,Set<Node<CallContextType>>>newMap());
			pending.add(n);
			if (logger.isDebugEnabled()) 
				logger.debug("adding node " + n);
		}
		
		/**
		 * Checks whether the pending list is empty.
		 */
		public boolean pendingIsEmpty() {
			return pending.isEmpty();
		}

		/**
		 * Removes and returns the next pending node.
		 */
		public Node<CallContextType> getNextPending() {
			return pending.remove();
		}
		
		/**
		 * Returns the set of nodes.
		 */
		public Set<Node<CallContextType>> getNodes() {
			return graph.keySet();
		}
		
		/**
		 * Returns the number of nodes.
		 */
		public int getNumberOfNodes() {
			return graph.size();
		}
		
		/**
		 * Adds a node (caller) and an edge (from caller to callee) and updates the pending list accordingly.
		 */
		public void addNodeAndEdge(
				Node<CallContextType> callee,
				NodeAndContext<CallContextType> caller,
				PropertyReference caller_prop) {
			Node<CallContextType> caller_n = 
				new Node<>(caller.getNode().getBlock(), caller.getContext(), caller_prop);
			Map<Node<CallContextType>,Set<Node<CallContextType>>> t2 = graph.get(caller_n);
			if (t2 == null) {
				t2 = newMap();
				graph.put(caller_n, t2);
				pending.add(caller_n); // haven't seen caller_n before so visit it too
				if (logger.isDebugEnabled()) 
					logger.debug("adding caller node " + caller_n);
			}
			BlockAndContext<CallContextType> entry = callee.getContext().toEntry(callee.getBlock());
			Node<CallContextType> callee_functionentry_n = 
				new Node<>(entry.getBlock(), entry.getContext(), callee.getPropertyReference());
			Collections.addToMapSet(t2, callee_functionentry_n, callee);
			if (logger.isDebugEnabled()) 
				logger.debug("adding edge from node " + caller_n + " to node " + callee);
		}
		
		/**
		 * Returns the callees, grouped by function entry location.
		 */
		public Set<Map.Entry<Node<CallContextType>,Set<Node<CallContextType>>>> 
		getCallees(Node<CallContextType> n) {
			return graph.get(n).entrySet();
		}
	}

	/**
	 * One or two property references.
	 */
	private static class PropertyReferencePair {

		PropertyReference prop1;
		
		PropertyReference prop2; // null if absent
	}
	
	private UnknownValueResolver() {}

	/**
	 * Generic function for recovering 'unknown' properties.
	 */
	private static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>> 
	Obj recover(
			BlockState<BlockStateType,CallContextType,CallEdgeType> s,
			PropertyReference prop, 
			boolean partial) { // TODO: make fast track for recoveries that only need to look at the function entry state
		if (logger.isDebugEnabled()) 
			logger.debug((partial ? "partially" : "fully") + " recovering " + prop  + " at block " + s.getBasicBlock().getIndex() + " context " + s.getContext());
		GenericSolver<BlockStateType,CallContextType,CallEdgeType,?,?>.SolverInterface c = s.getSolverInterface();
		c.getMonitoring().visitUnknownValueResolve();
		// build recovery graph
		RecoveryGraph<CallContextType> g = new RecoveryGraph<>();
		PropertyReferencePair entry_prop = toEntry(s, prop);
		g.addNode(new Node<>(s.getBasicBlock(), s.getContext(), entry_prop.prop1));
		if (entry_prop.prop2 != null)
			g.addNode(new Node<>(s.getBasicBlock(), s.getContext(), entry_prop.prop2));
		while (!g.pendingIsEmpty()) {
			Node<CallContextType> n = g.getNextPending();
			// iterate through incoming call edges
			BlockAndContext<CallContextType> n_entry = n.getContext().toEntry(n.getBlock());
			for (NodeAndContext<CallContextType> cs : c.getAnalysisLatticeElement().getCallGraph().getSources(n_entry.getBlock(), n_entry.getContext())) {
				CallEdgeType call_edge = c.getAnalysisLatticeElement().getCallGraph().getCallEdge(cs.getNode(), cs.getContext(), n_entry.getBlock(), n_entry.getContext());
				BlockStateType call_edge_state = call_edge.getState();
				PropertyReference call_edge_prop = n.getPropertyReference();
				if (isOK(call_edge_state, call_edge_prop, partial)) // value is available at the call edge
					g.addRoot(n);
				else {
					BlockStateType caller_functionentry_state = getEntryState(c, cs.getNode().getBlock(), cs.getContext());
					PropertyReferencePair caller_functionentry_prop = toEntry(call_edge_state, call_edge_prop);
					if (isOK(caller_functionentry_state, caller_functionentry_prop.prop1, partial)) // value is available at the function entry of the caller (primary property reference)
						g.addRoot(n);
					else 
						g.addNodeAndEdge(n, cs, caller_functionentry_prop.prop1); // proceed backward through call graph
					if (caller_functionentry_prop.prop2 != null) {
						if (isOK(caller_functionentry_state, caller_functionentry_prop.prop2, partial)) // value is available at the function entry of the caller (secondary property reference)
							g.addRoot(n);
						else 
							g.addNodeAndEdge(n, cs, caller_functionentry_prop.prop2); // proceed backward through call graph
					}
				}
			}
		}
		if (Options.isStatisticsEnabled())
			c.getMonitoring().visitRecoveryGraph(g.getNumberOfNodes());
		// recover at roots
		for (Node<CallContextType> n : g.getRoots()) {
			BlockStateType callee_functionentry_state = getEntryState(c, n.getBlock(), n.getContext());
			boolean changed = false;
			BlockAndContext<CallContextType> n_entry = n.getContext().toEntry(n.getBlock());
			for (NodeAndContext<CallContextType> cs : c.getAnalysisLatticeElement().getCallGraph().getSources(n_entry.getBlock(), n_entry.getContext())) {
				CallEdgeType call_edge = c.getAnalysisLatticeElement().getCallGraph().getCallEdge(cs.getNode(), cs.getContext(), n_entry.getBlock(), n_entry.getContext());
				BlockStateType call_edge_state = call_edge.getState();
				PropertyReference call_edge_prop =n.getPropertyReference();
				if (isOK(call_edge_state, call_edge_prop, partial)) { // recover from call edge
					changed |= propagate(callee_functionentry_state, n.getPropertyReference(), call_edge_state, call_edge_prop, null, partial); // no summarization
				} else {
					BlockStateType caller_functionentry_state = getEntryState(c, cs.getNode().getBlock(), cs.getContext());
					PropertyReferencePair caller_functionentry_prop = toEntry(call_edge_state, call_edge_prop);
					if (isOK(caller_functionentry_state, caller_functionentry_prop.prop1, partial)) { // recover from entry of caller function
						propagate(call_edge_state, call_edge_prop, caller_functionentry_state, caller_functionentry_prop.prop1, call_edge_state.getSummarized(), partial);
						changed |= propagate(callee_functionentry_state, n.getPropertyReference(), call_edge_state, call_edge_prop, null, partial);
					}
					if (caller_functionentry_prop.prop2 != null) {
						if (isOK(caller_functionentry_state, caller_functionentry_prop.prop2, partial)) { // recover from entry of caller function
							propagate(call_edge_state, call_edge_prop, caller_functionentry_state, caller_functionentry_prop.prop2, call_edge_state.getSummarized(), partial);
							changed |= propagate(callee_functionentry_state, n.getPropertyReference(), call_edge_state, call_edge_prop, null, partial);
						}
					}
				}
			}
			if (changed) {
				s.getSolverInterface().getMonitoring().visitNewFlow(n_entry.getBlock(), n_entry.getContext(), callee_functionentry_state, null, "recover");
				if (logger.isDebugEnabled()) 
					logger.debug("recovered value at root " + n);
			}
		}
		// propagate throughout the graph
		Set<Node<CallContextType>> pending2 = g.getRoots();
		LinkedList<Node<CallContextType>> pending_list2 = new LinkedList<>(pending2);
		while (!pending2.isEmpty()) {
			Node<CallContextType> n = pending_list2.remove();
			pending2.remove(n);
			BlockStateType n_functionentry_state = getEntryState(c, n.getBlock(), n.getContext());
			for (Map.Entry<Node<CallContextType>, 
					Set<Node<CallContextType>>> me : g.getCallees(n)) {
				Node<CallContextType> callee_functionentry_n = me.getKey();
				CallEdgeType call_edge = c.getAnalysisLatticeElement().getCallGraph().getCallEdge(n.getBlock().getSingleNode(), n.getContext(), 
						callee_functionentry_n.getBlock(), callee_functionentry_n.getContext());
				BlockStateType call_edge_state = call_edge.getState();
				BlockStateType callee_functionentry_state = c.getAnalysisLatticeElement().getState(callee_functionentry_n.getBlock(), callee_functionentry_n.getContext());
				PropertyReference call_edge_prop = callee_functionentry_n.getPropertyReference();
				propagate(call_edge_state, callee_functionentry_n.getPropertyReference(), n_functionentry_state, n.getPropertyReference(), call_edge_state.getSummarized(), partial);
				boolean changed = propagate(callee_functionentry_state, callee_functionentry_n.getPropertyReference(), call_edge_state, call_edge_prop, null, partial);
				if (changed) {
					for (Node<CallContextType> callee_n : me.getValue()) {
						if (!pending2.contains(callee_n)) { // propagate further if changed
							pending2.add(callee_n);
							pending_list2.add(callee_n);
						}
					}
					s.getSolverInterface().getMonitoring().visitNewFlow(callee_functionentry_n.getBlock(), callee_functionentry_n.getContext(), callee_functionentry_state, null, "recover");
					if (logger.isDebugEnabled()) 
						logger.debug("recovered value at node " + n);
				}
			}
		}
		if (Options.isDebugOrTestEnabled()) {
			for (Node<CallContextType> n : g.getNodes()) {
				BlockStateType n_state = getEntryState(c, n.getBlock(), n.getContext());
				if (!isOK(n_state, n.getPropertyReference(), partial))
					throw new AnalysisException("Unexpected value at " + n.getPropertyReference() + ", should have been recovered!?");
			}
		}
		// propagate to the current state (necessary for materializing all properties and for abstract gc)
		Obj dst_obj = s.getObject(prop.getObjectLabel(), true); 
		// TODO: skip if already at function entry state?
		switch (prop.getKind()) {
		case ORDINARY:
			dst_obj.setProperty(prop.getPropertyName(), getValueFromFunctionEntry(s, entry_prop, null, partial));
			break;
		case INTERNAL_VALUE:
			dst_obj.setInternalValue(getValueFromFunctionEntry(s, entry_prop, null, partial));
			break;
		case INTERNAL_PROTOTYPE:
			dst_obj.setInternalPrototype(getValueFromFunctionEntry(s, entry_prop, null, partial));
			break;
		case DEFAULT_ARRAY:
			Collection<String> src_array_props = newList();
			dst_obj.setDefaultArrayProperty(getValueFromFunctionEntry(s, entry_prop, src_array_props, partial));
			materializeProperties(dst_obj, src_array_props);
			break;
		case DEFAULT_NONARRAY:
			Collection<String> src_nonarray_props = newList();
			dst_obj.setDefaultNonArrayProperty(getValueFromFunctionEntry(s, entry_prop, src_nonarray_props, partial));
			materializeProperties(dst_obj, src_nonarray_props);
			break;
		case INTERNAL_SCOPE:
			dst_obj.setScopeChain(getInternalScopeFromFunctionEntry(s, entry_prop));
			break;
		default:
			throw new AnalysisException("Unexpected property reference kind");
		}
		return dst_obj;
	}

	/**
	 * Materializes the given explicit properties as 'unknown' if not already present.
	 */
	private static void materializeProperties(Obj dst_obj, Collection<String> materialize) {
		for (String p : materialize)
			if (!dst_obj.getProperties().containsKey(p)) {
				if (logger.isDebugEnabled()) 
					logger.debug("materialized property " + p);
				dst_obj.setProperty(p, Value.makeUnknown());
			}
	}
	
	/**
	 * If the given property reference refers to a polymorphic value, then a 
	 * reference to the corresponding property at function entry is returned,
	 * otherwise the given property reference is returned. However, if 
	 * 1) the given property reference refers to an 'unknown' value, 
	 * 2) the object label is a summary, and 
	 * 3) the object label has (maybe) been summarized since entry, 
	 * then both the given property reference and its singleton variant are returned.
	 */
	private static <BlockStateType extends BlockState<BlockStateType,CallContextType,?>,
	CallContextType extends ICallContext<CallContextType>>
	PropertyReferencePair toEntry(
			BlockState<BlockStateType,CallContextType,?> s, 
			PropertyReference prop) {
		PropertyReferencePair p = new PropertyReferencePair();
		Obj obj = s.getObject(prop.getObjectLabel(), false);
		Value v = null;
		switch (prop.getKind()) {
		case ORDINARY:
			v = obj.getProperty(prop.getPropertyName());
			break;
		case INTERNAL_VALUE:
			v = obj.getInternalValue();
			break;
		case INTERNAL_PROTOTYPE:
			v = obj.getInternalPrototype();
			break;
		default:
			break; // other kinds aren't polymorphic (after call to isOK)
		}
		if (v != null && v.isPolymorphic())
			p.prop1 = v.getPropertyReference();
		else {
			p.prop1 = prop;
			if ((v == null || v.isUnknown()) && !prop.getObjectLabel().isSingleton() && s.getSummarized().isMaybeSummarized(prop.getObjectLabel().makeSingleton()))
				p.prop2 = prop.makeSingleton();
		}
		return p;
	}

	/**
	 * Checks whether the property is OK 
	 * (that is, non-'unknown' for partial recovery and non-'unknown'-nor-polymorphic for full recovery).
	 */
	private static <BlockStateType extends BlockState<BlockStateType,CallContextType,?>,
	CallContextType extends ICallContext<CallContextType>>
	boolean isOK(
			BlockState<BlockStateType,CallContextType,?> s, 
			PropertyReference prop,
			boolean partial) {
		Obj obj = s.getObject(prop.getObjectLabel(), false);
		switch (prop.getKind()) {
		case INTERNAL_SCOPE:
			return isScopeChainOK(obj, partial);
		default:
			return isValueOK(obj.getValue(prop), partial);
		}		
	}
	
	/**
	 * Checks whether the value is OK. 
	 * (that is, non-'unknown' for partial recovery and non-'unknown'-nor-polymorphic for full recovery).
	 */
	private static boolean isValueOK(Value v, boolean partial) {
		return !v.isUnknown() && (partial || !v.isPolymorphic());
	}
	
	/**
	 * Checks whether the scope chain is OK. 
	 * (that is, non-'unknown' - partial recovery is not supported for scope chain values).
	 */
	private static boolean isScopeChainOK(Obj obj, boolean partial) {
		if (partial)
			throw new AnalysisException("Unexpected property reference kind");
		return !obj.isScopeChainUnknown();
	}
	
	/**
	 * Propagates a property from src into dst.
	 * Must pass isOK for the src arguments.
	 * @param summarized summarization sets, null if no summarization necessary
	 * @param renaming renaming, null if no renaming necessary
	 * @return true if dst is changed
	 */
	private static <BlockStateType extends BlockState<BlockStateType,CallContextType,?>,
	CallContextType extends ICallContext<CallContextType>>
	boolean propagate(
			BlockState<BlockStateType,CallContextType,?> dst_s, PropertyReference dst_prop,
			BlockState<BlockStateType,CallContextType,?> src_s, PropertyReference src_prop,
			Summarized summarized,
			boolean partial) {
		if (src_s == dst_s && src_prop == dst_prop)
			return false; // joining to itself, nothing changes
		Obj src_obj = src_s.getObject(src_prop.getObjectLabel(), false);
		if (Options.isDebugOrTestEnabled() && !isOK(src_s, src_prop, partial))
			throw new AnalysisException("Unexpected value");
		Kind r = dst_prop.getKind();
		if (!(r == Kind.ORDINARY || r == Kind.INTERNAL_VALUE || r == Kind.INTERNAL_PROTOTYPE) && src_prop.getKind() != dst_prop.getKind())
			throw new AnalysisException("Unexpected property reference kind");
		Obj dst_obj = dst_s.getObject(dst_prop.getObjectLabel(), true);
		switch (dst_prop.getKind()) {
		case INTERNAL_SCOPE:
			return propagateInternalScope(dst_obj, src_obj.getScopeChain(), summarized);
		case DEFAULT_ARRAY:
			return propagateDefaultArray(dst_obj, src_obj, summarized);
		case DEFAULT_NONARRAY:
			return propagateDefaultNonArray(dst_obj, src_obj, summarized);
		case ORDINARY:
			return propagateProperty(dst_obj, dst_prop.getObjectLabel(), dst_prop.getPropertyName(), src_obj.getValue(src_prop), summarized, partial);
		case INTERNAL_VALUE:
			return propagateInternalValue(dst_obj, dst_prop.getObjectLabel(), src_obj.getValue(src_prop), summarized, partial);
		case INTERNAL_PROTOTYPE:
			return propagateInternalPrototype(dst_obj, dst_prop.getObjectLabel(), src_obj.getValue(src_prop), summarized, partial);
		default:
			throw new AnalysisException("Unexpected property reference kind");
		}
	}
	
	/**
	 * Propagates the given value into the internal prototype property of the destination object.
	 * Restricts to not-modified and summarizes the value, and introduces polymorphic value if necessary.
	 */
	private static boolean propagateInternalPrototype(Obj dst_obj, ObjectLabel dst_objlabel, Value src_v, Summarized s, boolean partial) {
		src_v = src_v.restrictToNotModified();
		Value dst_v = dst_obj.getInternalPrototype();
		Value new_dst_v;
		if (partial)
			new_dst_v = Value.makePolymorphic(PropertyReference.makeInternalPrototypePropertyReference(dst_objlabel), src_v);
		else {
			new_dst_v = src_v.summarize(s);
		}
		new_dst_v = new_dst_v.join(dst_v);
		dst_obj.setInternalPrototype(new_dst_v);
		return !new_dst_v.equals(dst_v);
	}
	
	/**
	 * Propagates the given value into the internal value property of the destination object.
	 * Restricts to not-modified and summarizes the value, and introduces polymorphic value if necessary.
	 */
	private static boolean propagateInternalValue(Obj dst_obj, ObjectLabel dst_objlabel, Value src_v, Summarized s, boolean partial) {
		src_v = src_v.restrictToNotModified();
		Value dst_v = dst_obj.getInternalValue();
		Value new_dst_v;
		if (partial)
			new_dst_v = Value.makePolymorphic(PropertyReference.makeInternalValuePropertyReference(dst_objlabel), src_v);
		else {
			new_dst_v = src_v.summarize(s);
		}
		new_dst_v = new_dst_v.join(dst_v);
		dst_obj.setInternalValue(new_dst_v);
		return !new_dst_v.equals(dst_v);
	}
	
	/**
	 * Propagates the given value into the given ordinary property of the destination object.
	 * Restricts to not-modified and summarizes the value, and introduces polymorphic value if necessary.
	 */
	private static boolean propagateProperty(Obj dst_obj, ObjectLabel dst_objlabel, String dst_propertyname, Value src_v, Summarized s, boolean partial) {
		src_v = src_v.restrictToNotModified();
		Value dst_v = dst_obj.getProperty(dst_propertyname);
		Value new_dst_v;
		if (partial)
			new_dst_v = Value.makePolymorphic(PropertyReference.makeOrdinaryPropertyReference(dst_objlabel, dst_propertyname), src_v);
		else {
			new_dst_v = src_v.summarize(s);
		}
		new_dst_v = new_dst_v.join(dst_v);
		dst_obj.setProperty(dst_propertyname, new_dst_v);
		return !new_dst_v.equals(dst_v);
	}
	
	/**
	 * Propagates the default array property value.
	 * Restricts to not-modified and summarizes the value.
	 * As a side-effect, ordinary properties may be materialized (to 'unknown').
	 */
	private static boolean propagateDefaultArray(Obj dst_obj, Obj src_obj, Summarized s) {
		Value src_v = src_obj.getDefaultArrayProperty().restrictToNotModified().summarize(s);
		Value dst_v = dst_obj.getDefaultArrayProperty();
		Value new_dst_v = dst_v.join(src_v);
		dst_obj.setDefaultArrayProperty(new_dst_v);
		for (String p : src_obj.getPropertyNames())
			if (Strings.isArrayIndex(p) && !dst_obj.getProperties().containsKey(p)) {
				if (logger.isDebugEnabled()) 
					logger.debug("materialized property " + p);
				dst_obj.setProperty(p, Value.makeUnknown());
			}
		return !new_dst_v.equals(dst_v);
	}
	
	/**
	 * Propagates the default non-array property value.
	 * Restricts to not-modified and summarizes the value.
	 * As a side-effect, ordinary properties may be materialized (to 'unknown').
	 */
	private static boolean propagateDefaultNonArray(Obj dst_obj, Obj src_obj, Summarized s) {
		Value src_v = src_obj.getDefaultNonArrayProperty().restrictToNotModified().summarize(s);
		Value dst_v = dst_obj.getDefaultNonArrayProperty();
		Value new_dst_v = dst_v.join(src_v);
		dst_obj.setDefaultNonArrayProperty(new_dst_v);
		for (String p : src_obj.getPropertyNames())
			if (!Strings.isArrayIndex(p) && !dst_obj.getProperties().containsKey(p)) {
				if (logger.isDebugEnabled()) 
					logger.debug("materialized property " + p);
				dst_obj.setProperty(p, Value.makeUnknown());
			}
		return !new_dst_v.equals(dst_v);
	}
	
	/**
	 * Propagates the given value into the internal scope property of the destination object.
	 * Summarizes the value.
	 */
	private static boolean propagateInternalScope(Obj dst_obj, ScopeChain src_v, Summarized s) {
		boolean changed;
		if (src_v != null) {
			src_v = ScopeChain.summarize(src_v, s);
		}
		if (dst_obj.isScopeChainUnknown()) {
			dst_obj.setScopeChain(src_v);
			changed = true;
		} else 
			changed = dst_obj.addToScopeChain(src_v);
		return changed;
	}
	
	/**
	 * Gets the given property value from the function entry state.
	 * Summarizes the value.
	 * Also collects the explicit properties in the source objects if getting a default property.
	 */
	private static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>> 
	Value getValueFromFunctionEntry(
			BlockState<BlockStateType, CallContextType, CallEdgeType> s,
			PropertyReferencePair entry_prop,
			Collection<String> src_props,
			boolean partial) {
		BlockStateType entry_state = getEntryState(s);
		Obj obj1 = entry_state.getObject(entry_prop.prop1.getObjectLabel(), false);
		switch (entry_prop.prop1.getKind()) {
		case DEFAULT_ARRAY:
			addProperties(obj1, src_props, true);
			break;
		case DEFAULT_NONARRAY:
			addProperties(obj1, src_props, false);
			break;
		default:
			// do nothing
			break;
		}
		Value res = obj1.getValue(entry_prop.prop1).summarize(s.getSummarized());
		if (entry_prop.prop2 != null) {
			Obj obj2 = entry_state.getObject(entry_prop.prop2.getObjectLabel(), false);
			switch (entry_prop.prop2.getKind()) {
			case DEFAULT_ARRAY:
				addProperties(obj2, src_props, true);
				break;
			case DEFAULT_NONARRAY:
				addProperties(obj2, src_props, false);
				break;
			default:
				// do nothing
				break;
			}
			res = join(res, obj2.getValue(entry_prop.prop2).summarize(s.getSummarized()), s);
		}
		if (Options.isDebugOrTestEnabled() && !isValueOK(res, partial))
			throw new AnalysisException("Unexpected value " + res);
		return res;
	}

	/**
	 * Collects the explicit properties of the given kind.
	 */
	private static void addProperties(Obj obj, Collection<String> src_props, boolean array) { 
		for (String p : obj.getPropertyNames())
			if (array == Strings.isArrayIndex(p))
				src_props.add(p);
	}

	/**
	 * Gets the given internal scope property value from the function entry state.
	 * Summarizes the value.
	 */
	private static <BlockStateType extends BlockState<BlockStateType,CallContextType,?>,
	CallContextType extends ICallContext<CallContextType>> 
	ScopeChain getInternalScopeFromFunctionEntry(
			BlockState<BlockStateType, CallContextType, ?> s,
			PropertyReferencePair entry_prop) {
		BlockStateType entry_state = getEntryState(s);
		ScopeChain res = ScopeChain.summarize(entry_state.getObject(entry_prop.prop1.getObjectLabel(), false).getScopeChain(), s.getSummarized());
		if (entry_prop.prop2 != null)
			res = ScopeChain.add(res, ScopeChain.summarize(entry_state.getObject(entry_prop.prop2.getObjectLabel(), false).getScopeChain(), s.getSummarized()));
		return res;
	}

	/**
	 * Returns the enclosing entry state for the location of the given state.
	 * The enclosing entry is the nearest for-in body entry or function entry.
	 */
	private static <BlockStateType extends BlockState<BlockStateType,CallContextType,?>,
	CallContextType extends ICallContext<CallContextType>> 
	BlockStateType getEntryState(BlockState<BlockStateType, CallContextType, ?> s) {
		return getEntryState(s.getSolverInterface(), s.getBasicBlock(), s.getContext());
	}

	/**
	 * Returns the enclosing entry state for the given block and context.
	 */
	private static <BlockStateType extends BlockState<BlockStateType,CallContextType,?>,
	CallContextType extends ICallContext<CallContextType>> 
	BlockStateType getEntryState(
			GenericSolver<BlockStateType,CallContextType,?,?,?>.SolverInterface c,
			BasicBlock block,
			CallContextType context) {
		BlockAndContext<CallContextType> enclosing = context.toEntry(block); 
		return c.getAnalysisLatticeElement().getState(enclosing.getBlock(), enclosing.getContext());
	}

	/**
	 * Wrapper for {@link Obj#getProperty(String)}.
	 * Never returns 'unknown'.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>>
	Value getProperty(
			ObjectLabel objlabel, String propertyname, 
			BlockState<BlockStateType,CallContextType,CallEdgeType> s,
			boolean partial) {
		Value res = s.getObject(objlabel, false).getProperty(propertyname);
		if (!isValueOK(res, partial)) {
			res = recover(s, PropertyReference.makeOrdinaryPropertyReference(objlabel, propertyname), 
					partial && Options.isPolymorphicEnabled()).getProperty(propertyname);
			if (logger.isDebugEnabled()) 
				logger.debug("getProperty(" + objlabel  + "," + propertyname + ") = " + res);
		}
		return res;
	}

	/**
	 * Wrapper for {@link Obj#getDefaultArrayProperty}.
	 * Never returns 'unknown'.
	 * As a side-effect, ordinary properties may be materialized (to 'unknown').
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>> 
	Value getDefaultArrayProperty(
			ObjectLabel objlabel, 
			BlockState<BlockStateType,CallContextType,CallEdgeType> s) { // TODO: extend polymorphism to the default array property?
		Value res = s.getObject(objlabel, false).getDefaultArrayProperty();
		if (!isValueOK(res, false)) {
			res = recover(s, PropertyReference.makeDefaultArrayPropertyReference(objlabel), false).getDefaultArrayProperty();
			if (logger.isDebugEnabled()) 
				logger.debug("getDefaultArrayProperty(" + objlabel  + ") = " + res);
		}
		return res;
	}

	/**
	 * Wrapper for {@link Obj#getDefaultNonArrayProperty}.
	 * Never returns 'unknown'.
	 * As a side-effect, ordinary properties may be materialized (to 'unknown').
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>>
	Value getDefaultNonArrayProperty(
			ObjectLabel objlabel, 
			BlockState<BlockStateType,CallContextType,CallEdgeType> s) { // TODO: extend polymorphism to the default non-array property?
		Value res = s.getObject(objlabel, false).getDefaultNonArrayProperty();
		if (!isValueOK(res, false)) {
			res = recover(s, PropertyReference.makeDefaultNonArrayPropertyReference(objlabel), false).getDefaultNonArrayProperty();
			if (logger.isDebugEnabled()) 
				logger.debug("getDefaultNonArrayProperty(" + objlabel  + ") = " + res);
		}
		return res;
	}

	/**
	 * Wrapper for {@link Obj#getInternalValue()}.
	 * Never returns 'unknown'.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>>
	Value getInternalValue(
			ObjectLabel objlabel, 
			BlockState<BlockStateType,CallContextType,CallEdgeType> s,
			boolean partial) {
		Value res = s.getObject(objlabel, false).getInternalValue();
		if (!isValueOK(res, false)) {
			res = recover(s, PropertyReference.makeInternalValuePropertyReference(objlabel), 
					partial && Options.isPolymorphicEnabled()).getInternalValue();
			if (logger.isDebugEnabled()) 
				logger.debug("getInternalValue(" + objlabel  + ") = " + res);
		}
		return res;
	}

	/**
	 * Wrapper for {@link Obj#getInternalPrototype()}.
	 * Never returns 'unknown'.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>>
	Value getInternalPrototype(
			ObjectLabel objlabel, 
			BlockState<BlockStateType,CallContextType,CallEdgeType> s,
			boolean partial) {
		Value res = s.getObject(objlabel, false).getInternalPrototype();
		if (!isValueOK(res, false)) {
			res = recover(s, PropertyReference.makeInternalPrototypePropertyReference(objlabel), 
					partial && Options.isPolymorphicEnabled()).getInternalPrototype();
			if (logger.isDebugEnabled()) 
				logger.debug("getInternalPrototype(" + objlabel  + ") = " + res);
		}
		return res;
	}

	/**
	 * Wrapper for {@link Obj#getScopeChain()}.
	 * Never returns 'unknown'.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>>
	ScopeChain getScopeChain(
			ObjectLabel objlabel, 
			BlockState<BlockStateType,CallContextType,CallEdgeType> s) { // TODO: extend polymorphism to the scope chain property?
		Obj obj = s.getObject(objlabel, false);
		ScopeChain res;
		if (isScopeChainOK(obj, false))
		  res = obj.getScopeChain();
		else {
			res = recover(s, PropertyReference.makeInternalScopePropertyReference(objlabel), false).getScopeChain();
			if (logger.isDebugEnabled()) 
				logger.debug("getScopeChain(" + objlabel  + ") = " + (res != null ? res : "[]"));
		}
		return res;
	} 

	/**
	 * Wrapper for {@link Obj#getProperties()}.
	 * The resulting map may contain 'unknown' and polymorphic values, but all property names will be present.
	 * The map is not writable.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>>
	Map<String,Value> getProperties(
			ObjectLabel objlabel, 
			BlockState<BlockStateType,CallContextType,CallEdgeType> s) {
		Obj obj = s.getObject(objlabel, false);
		if (obj.getDefaultArrayProperty().isUnknown() || obj.getDefaultNonArrayProperty().isUnknown()) {
			if (obj.getDefaultArrayProperty().isUnknown())
				recover(s, PropertyReference.makeDefaultArrayPropertyReference(objlabel), false);
			if (obj.getDefaultNonArrayProperty().isUnknown())
				recover(s, PropertyReference.makeDefaultNonArrayPropertyReference(objlabel), false);
			obj = s.getObject(objlabel, false); // now all properties have been materialized from the defaults if unknown
			if (logger.isDebugEnabled()) 
				logger.debug("getProperties(" + objlabel + ")");
		}
		return obj.getProperties();
	}

	/**
	 * Fully recovers the given value if polymorphic.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>>
	Value getRealValue(
			Value v, 
			BlockState<BlockStateType,CallContextType,CallEdgeType> s) {
		if (!v.isPolymorphic())
			return v;
		BlockState<BlockStateType,CallContextType,CallEdgeType> entry_state = getEntryState(s);
		PropertyReference var = v.getPropertyReference();
		Value res;
		switch (var.getKind()) {
		case ORDINARY:
			res = getProperty(var.getObjectLabel(), var.getPropertyName(), entry_state, false);
			break;
		case DEFAULT_ARRAY:
			res = getDefaultArrayProperty(var.getObjectLabel(), entry_state);
			break;
		case DEFAULT_NONARRAY:
			res = getDefaultNonArrayProperty(var.getObjectLabel(), entry_state);
			break;
		case INTERNAL_VALUE:
			res = getInternalValue(var.getObjectLabel(), entry_state, false);
			break;
		case INTERNAL_PROTOTYPE:
			res = getInternalPrototype(var.getObjectLabel(), entry_state, false);
			break;
		default:
			throw new AnalysisException("Unexpected property reference kind");
		}
		if (v.isMaybeModified()) // inherit the modified status from v
			res = res.joinModified();
		else
			res = res.restrictToNotModified();
		return res.summarize(s.getSummarized());
	}

	/**
	 * Checks whether the given two values can be joined.
	 * Assumes that both values are non-unknown.
	 * @return true if both values are non-polymorphic or both are polymorphic and of the same name
	 */
	private static boolean canJoin(Value v1, Value v2) {
		if (v1.isUnknown() || v2.isUnknown())
			throw new AnalysisException("Unexpected value");
		return v1.isPolymorphic() == v2.isPolymorphic()
		&& (!v1.isPolymorphic() || v1.getPropertyReference().equals(v2.getPropertyReference()));
	}

	/**
	 * Checks whether the given values can be joined.
	 * Assumes that the values are non-unknown.
	 * @return true if all values are non-polymorphic or all are polymorphic and of same name
	 */
	@SuppressWarnings("null")
	private static boolean canJoin(Iterable<Value> values) {
		boolean some_poly = false, some_nonpoly = false;
		PropertyReference var = null;
		for (Value v : values) {
			if (v.isUnknown())
				throw new AnalysisException("Unexpected value");
			if (v.isPolymorphic()) {
				if (some_poly && !var.equals(v.getPropertyReference()))
					return false;
				some_poly = true;
				var = v.getPropertyReference();;
			} else
				some_nonpoly = true;
		}
		if (some_poly && some_nonpoly)
			return false;
		return true;
	}

	/**
	 * Joins the given values, performing full recovery for polymorphic values if necessary.
	 * Assumes that both values are non-unknown.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>>
	Value join(
			Value v1, 
			Value v2, 
			BlockState<BlockStateType,CallContextType,CallEdgeType> s) {
		if (!canJoin(v1, v2)) {
			v1 = getRealValue(v1, s);
			v2 = getRealValue(v2, s);
		}
		return v1.join(v2);
	}

	/**
	 * Joins the given values, performing full recovery for polymorphic values if necessary.
	 * Marks the resulting value as maybe modified if different from v1.
	 * Assumes that both values are non-unknown.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>>
	Value joinWithModified(
			Value v1, 
			Value v2, 
			BlockState<BlockStateType,CallContextType,CallEdgeType> s) {
		if (!canJoin(v1, v2)) {
			v1 = getRealValue(v1, s);
			v2 = getRealValue(v2, s);
		}
		return v1.joinWithModified(v2);
	}

	/**
	 * Joins the given values, performing full recovery for polymorphic values if necessary.
	 * Assumes that the values are non-unknown.
	 */
	public static <BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
	CallContextType extends ICallContext<CallContextType>,
	CallEdgeType extends CallEdge<BlockStateType>>
	Value join(
			Iterable<Value> values, 
			BlockState<BlockStateType,CallContextType,CallEdgeType> s) {
		if (canJoin(values))
			return Value.join(values);
		Collection<Value> vs = new ArrayList<>();
		for (Value v : values) {
			if (v.isPolymorphic())
				v = getRealValue(v, s);
			vs.add(v);
		}
		return Value.join(vs);
	}
}
