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

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.PropertyReference.Kind;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.Strings;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Resolves 'unknown' and polymorphic properties.
 * Contains wrappers for certain methods in {@link Obj}.
 * Each method reads the appropriate property. If unknown/polymorphic, it is recovered via the function entry state in the call graph.
 *
 * @author Anders M&oslash;ller &lt;<a href="mailto:amoeller@cs.au.dk">amoeller@cs.au.dk</a>&gt;
 */
public final class UnknownValueResolver {

    public static Logger log = Logger.getLogger(UnknownValueResolver.class);

    static {
        LogManager.getLogger(UnknownValueResolver.class).setLevel(Level.INFO);
    } // set to Level.DEBUG to force debug output or Level.INFO to disable

    /**
     * A recovery graph node is a triple of a flow graph node, a context, and a property reference.
     * The flow graph node and the context identify a location in the program.
     * The property reference denotes the desired property <i>at the function entry</i> relative to the node and context location.
     * (Note that "function entries" may be for-in body entries.)
     */
    private static class RGNode {

        private AbstractNode n;

        private Context c;

        private PropertyReference p;

        public RGNode(AbstractNode n, Context c, PropertyReference p) {
            this.n = n;
            this.c = c;
            this.p = p;
        }

        public RGNode(NodeAndContext<Context> nc, PropertyReference p) {
            this(nc.getNode(), nc.getContext(), p);
        }

        public AbstractNode getNode() {
            return n;
        }

        public Context getContext() {
            return c;
        }

        public PropertyReference getPropertyReference() {
            return p;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof RGNode))
                return false;
            RGNode ncp = (RGNode) obj;
            return ncp.n == n && ncp.c.equals(c) && ncp.p.equals(p);
        }

        @Override
        public int hashCode() {
            return n.getIndex() * 13 + c.hashCode() * 3 + p.hashCode() * 5;
        }

        @Override
        public String toString() {
            return "{n=" + n.getIndex() + ", c=" + c + ", p=" + p + '}';
        }
    }

    /**
     * Recovery graph.
     * <p>
     * Each node represents a property value to be recovered at some location.
     * Each root is a node where (a part of) the desired value is available without further traversal.
     * Each edge corresponds to relevant flow of the 'unknown' values.
     */
    private static class RecoveryGraph {

        /**
         * The internal representation groups the callee nodes with same function entry.
         */
        private Map<RGNode, // target or call node
                Map<Pair<Context, RGNode>, // edge context and callee function entry (groups callee nodes with same function entry)
                        Set<RGNode>>> graph; // callee node

        private Set<RGNode> roots;

        private LinkedList<RGNode> pending;

        private Map<RGNode, Value> original_polymorphic_value;

        /**
         * Constructs a new empty recovery graph.
         */
        public RecoveryGraph() {
        }

        private void prepare() {
            if (graph == null) {
                graph = newMap();
                roots = newSet();
                pending = new LinkedList<>();
                original_polymorphic_value = newMap();
            }
        }

        /**
         * Marks the given node as a root.
         */
        public void addRoot(RGNode n) {
            if (roots.add(n))
                if (log.isDebugEnabled())
                    log.debug("adding root " + n);
        }

        /**
         * Returns the roots.
         * (Modifications of the returned set will affect subsequent calls to the method.)
         */
        public Set<RGNode> getRoots() {
            return roots != null ? roots : java.util.Collections.<RGNode>emptySet();
        }

        /**
         * Adds a node (caller) and updates the pending list accordingly.
         */
        public void addNode(RGNode n) {
            prepare();
            graph.put(n, dk.brics.tajs.util.Collections.<Pair<Context, RGNode>, Set<RGNode>>newMap());
            pending.add(n);
            if (log.isDebugEnabled())
                log.debug("adding node " + n);
        }

        /**
         * Checks whether the pending list is empty.
         */
        public boolean pendingIsEmpty() {
            return pending == null || pending.isEmpty();
        }

        /**
         * Removes and returns the next pending node.
         */
        public RGNode getNextPending() {
            return pending.remove();
        }

        /**
         * Returns the set of nodes.
         */
        public Set<RGNode> getNodes() {
            return graph != null ? graph.keySet() : java.util.Collections.<RGNode>emptySet();
        }

        /**
         * Returns the number of nodes.
         */
        public int getNumberOfNodes() {
            return graph != null ? graph.size() : 0;
        }

        /**
         * Adds a node (caller) and an edge (from caller to callee) and updates the pending list accordingly.
         */
        public void addNodeAndEdge(
                AbstractNode call_node,
                Context caller_context,
                PropertyReference caller_prop,
                Context edge_context,
                RGNode callee) {
            prepare();
            RGNode caller_n = new RGNode(call_node, caller_context, caller_prop);
            Map<Pair<Context, RGNode>, Set<RGNode>> t2 = graph.get(caller_n);
            if (t2 == null) {
                t2 = newMap();
                graph.put(caller_n, t2);
                pending.add(caller_n); // haven't seen caller_n before so visit it too
                if (log.isDebugEnabled())
                    log.debug("adding caller node " + caller_n);
            }
            BlockAndContext<Context> ce = BlockAndContext.makeEntry(callee.getNode().getBlock(), callee.getContext());
            RGNode callee_functionentry_n = new RGNode(ce.getBlock().getFirstNode(), ce.getContext(), callee.getPropertyReference());
            Collections.addToMapSet(t2, Pair.make(edge_context, callee_functionentry_n), callee);
            if (log.isDebugEnabled())
                log.debug("adding edge from node " + caller_n + " to node " + callee);
        }

        /**
         * Returns the callees, grouped by function entry location.
         */
        public Set<Entry<Pair<Context, RGNode>, Set<RGNode>>> getCallees(RGNode n) {
            return graph != null ? graph.get(n).entrySet() :
                    java.util.Collections.<Entry<Pair<Context, RGNode>, Set<RGNode>>>emptySet();
        }

        /**
         * Records the given value if non-null and polymorphic.
         */
        public void setPolymorphic(BlockAndContext<Context> bc, PropertyReference p, Value v) {
            if (v != null && v.isPolymorphic())
                original_polymorphic_value.put(new RGNode(bc.getBlock().getFirstNode(), bc.getContext(), p), v);
        }

        /**
         * Returns the original value for the given location, or null if not polymorphic.
         */
        public Value getPolymorphic(AbstractNode n, Context c, PropertyReference p) {
            return getPolymorphic(new RGNode(n, c, p));
        }

        /**
         * Returns the original value for the given location, or null if not polymorphic.
         */
        public Value getPolymorphic(RGNode n) {
            return original_polymorphic_value.get(n);
        }
    }

    /**
     * One or two property references.
     */
    private static class PropertyReferencePair {

        PropertyReference prop1;

        PropertyReference prop2; // null if absent
    }

    private UnknownValueResolver() {
    }

    /**
     * Generic function for recovering 'unknown' properties.
     */
    private static Obj recover(State s, PropertyReference prop, boolean partial) {
        Value value_at_s = getValue(s, prop);
        if (!partial && value_at_s != null && value_at_s.isPolymorphic() && value_at_s.isMaybeAbsent() && !value_at_s.isMaybePresent()) { // optimization only
            Obj res = s.getObject(prop.getObjectLabel(), true);
            res.setValue(prop, Value.makeAbsent());
            return res;
        }
        PropertyReferencePair entry_prop = toEntry(s, prop);
        if (partial && entry_prop.prop1 != null && entry_prop.prop2 != null) {
            if (log.isDebugEnabled())
                log.debug("switching from partial to full recover");
            return recover(s, prop, false);
        }
        if (log.isDebugEnabled())
            log.debug((partial ? "partially" : "fully") + " recovering " + prop + " at block " + s.getBasicBlock().getIndex() + " context " + s.getContext());
        GenericSolver<State, Context, CallEdge, ?, ?>.SolverInterface c = s.getSolverInterface();
        c.getMonitoring().visitUnknownValueResolve(partial, c.isScanning());
        // build recovery graph
        RecoveryGraph g = new RecoveryGraph();
        State entry_state = getEntryState(s);
        if (entry_prop.prop1 != null && !isOK(entry_state, entry_prop.prop1, partial))
            g.addNode(new RGNode(s.getBasicBlock().getFirstNode(), s.getContext(), entry_prop.prop1));
        if (entry_prop.prop2 != null && !isOK(entry_state, entry_prop.prop2, partial))
            g.addNode(new RGNode(s.getBasicBlock().getFirstNode(), s.getContext(), entry_prop.prop2));
        while (!g.pendingIsEmpty()) {
            RGNode n = g.getNextPending();
            BlockAndContext<Context> n_entry = BlockAndContext.makeEntry(n.getNode().getBlock(), n.getContext());
            // remember whether the value is polymorphic at the entry location
            State callee_functionentry_state = getEntryState(c, n.getContext(), n.getNode());
            if (!partial)
                g.setPolymorphic(n_entry, n.getPropertyReference(), getValue(callee_functionentry_state, n.getPropertyReference()));
            // iterate through incoming call edges
            for (CallGraph.ReverseEdge<Context> cs : c.getAnalysisLatticeElement().getCallGraph().getSources(n_entry)) {
                CallEdge call_edge = c.getAnalysisLatticeElement().getCallGraph().getCallEdge(cs.getCallNode(), cs.getCallerContext(), n_entry.getBlock(), cs.getEdgeContext());
                State call_edge_state = call_edge.getState();
                PropertyReference call_edge_prop = n.getPropertyReference();
                if (isOK(call_edge_state, call_edge_prop, partial)) // value is available at the call edge
                    g.addRoot(n);
                else { // need to go to the function entry of the caller
                    State caller_functionentry_state = getEntryState(c, cs.getCallerContext(), cs.getCallNode());
                    PropertyReferencePair caller_functionentry_prop = toEntry(call_edge_state, call_edge_prop);
                    if (partial && caller_functionentry_prop.prop1 != null && caller_functionentry_prop.prop2 != null) {
                        if (log.isDebugEnabled())
                            log.debug("switching from partial to full recover");
                        return recover(s, prop, false);
                    }
                    if (caller_functionentry_prop.prop1 != null)
                        addRootOrPredecessors(n, cs.getCallNode(), cs.getCallerContext(), cs.getEdgeContext(), caller_functionentry_state, caller_functionentry_prop.prop1, g, partial);
                    if (caller_functionentry_prop.prop2 != null)
                        addRootOrPredecessors(n, cs.getCallNode(), cs.getCallerContext(), cs.getEdgeContext(), caller_functionentry_state, caller_functionentry_prop.prop2, g, partial);
                }
            }
        }
        if (Options.get().isStatisticsEnabled())
            c.getMonitoring().visitRecoveryGraph(g.getNumberOfNodes());
        // recover at roots
        for (RGNode n : g.getRoots()) { // TODO: recover at roots as soon as we mark them as roots instead of having a separate phase?
            State callee_functionentry_state = getEntryState(c, n.getContext(), n.getNode());
            boolean changed = false;
            BlockAndContext<Context> n_entry = BlockAndContext.makeEntry(n.getNode().getBlock(), n.getContext());
            for (CallGraph.ReverseEdge<Context> cs : c.getAnalysisLatticeElement().getCallGraph().getSources(n_entry)) {
                CallEdge call_edge = c.getAnalysisLatticeElement().getCallGraph().getCallEdge(cs.getCallNode(), cs.getCallerContext(), n_entry.getBlock(), cs.getEdgeContext());
                State call_edge_state = call_edge.getState();
                PropertyReference call_edge_prop = n.getPropertyReference();
                if (isOK(call_edge_state, call_edge_prop, partial)) { // recover from call edge
                    Value poly_at_n_entry = partial ? null : g.getPolymorphic(n_entry.getBlock().getFirstNode(), n_entry.getContext(), n.getPropertyReference());
                    changed |= propagate(call_edge_state, call_edge_prop, callee_functionentry_state, n.getPropertyReference(), null, partial, true, poly_at_n_entry); // no summarization
                } else { // recover from the function entry of the caller
                    State caller_functionentry_state = getEntryState(c, cs.getCallerContext(), cs.getCallNode());
                    PropertyReferencePair caller_functionentry_prop = toEntry(call_edge_state, call_edge_prop);
                    if (caller_functionentry_prop.prop1 != null)
                        changed |= recoverAtRootFromCallerFunctionEntry(n, caller_functionentry_state, caller_functionentry_prop.prop1, call_edge_state, call_edge_prop, callee_functionentry_state, partial, g);
                    if (caller_functionentry_prop.prop2 != null)
                        changed |= recoverAtRootFromCallerFunctionEntry(n, caller_functionentry_state, caller_functionentry_prop.prop2, call_edge_state, call_edge_prop, callee_functionentry_state, partial, g);
                }
            }
            if (changed) {
                s.getSolverInterface().getMonitoring().visitNewFlow(n_entry.getBlock(), n_entry.getContext(), callee_functionentry_state, null, "recover");
                if (log.isDebugEnabled())
                    log.debug("recovered value at root " + n);
            }
        }
        // propagate throughout the graph
        Set<RGNode> pending2 = g.getRoots();
        LinkedList<RGNode> pending_list2 = new LinkedList<>(pending2);
        while (!pending2.isEmpty()) {
            RGNode n = pending_list2.remove();
            pending2.remove(n);
            State n_functionentry_state = getEntryState(c, n.getContext(), n.getNode());
            for (Entry<Pair<Context, RGNode>,
                    Set<RGNode>> me : g.getCallees(n)) {
                RGNode callee_functionentry_n = me.getKey().getSecond();
                Context edge_context = me.getKey().getFirst();
                CallEdge call_edge = c.getAnalysisLatticeElement().getCallGraph().getCallEdge(n.getNode(), n.getContext(),
                        callee_functionentry_n.getNode().getBlock(), edge_context);
                State call_edge_state = call_edge.getState();
                State callee_functionentry_state = c.getAnalysisLatticeElement().getState(callee_functionentry_n.getNode().getBlock(), callee_functionentry_n.getContext());
                PropertyReference call_edge_prop = callee_functionentry_n.getPropertyReference();
                Value value_at_call_edge = partial ? null : getValue(call_edge_state, call_edge_prop);
                propagate(n_functionentry_state, n.getPropertyReference(), call_edge_state, call_edge_prop, call_edge_state.getSummarized(), partial, false, value_at_call_edge);
                Value poly_at_callee_functionentry = partial ? null : g.getPolymorphic(callee_functionentry_n);
                boolean changed = propagate(call_edge_state, call_edge_prop, callee_functionentry_state, callee_functionentry_n.getPropertyReference(), null, partial, true, poly_at_callee_functionentry);
                if (changed) {
                    for (RGNode callee_n : me.getValue()) {
                        if (!pending2.contains(callee_n)) { // propagate further if changed
                            pending2.add(callee_n);
                            pending_list2.add(callee_n);
                        }
                    }
                    s.getSolverInterface().getMonitoring().visitNewFlow(callee_functionentry_n.getNode().getBlock(), callee_functionentry_n.getContext(), callee_functionentry_state, null, "recover");
                    if (log.isDebugEnabled())
                        log.debug("recovered value at node " + n);
                }
            }
        }
        if (Options.get().isDebugOrTestEnabled()) {
            for (RGNode n : g.getNodes()) {
                State n_entry_state = getEntryState(c, n.getContext(), n.getNode());
                if (!isOK(n_entry_state, n.getPropertyReference(), partial))
                    throw new AnalysisException("Unexpected value at " + n.getPropertyReference() + ", should have been recovered!?");
            }
        }
        // propagate to the current state (necessary for materializing all properties and for abstract gc)
        if (entry_prop.prop1 != null)
            propagate(entry_state, entry_prop.prop1, s, prop, s.getSummarized(), partial, false, value_at_s);
        if (entry_prop.prop2 != null)
            propagate(entry_state, entry_prop.prop2, s, prop, s.getSummarized(), partial, false, value_at_s);
        return s.getObject(prop.getObjectLabel(), false);
    }

    /**
     * Adds n as root to the recovery graph or proceeds backward through the call graph to add more nodes and edges.
     *
     * @param n                          recovery graph node being visited
     * @param call_node                  an incoming function call
     * @param caller_context             the context at the call node
     * @param edge_context               the context at the edge
     * @param caller_functionentry_state abstract state at the function entry of the caller
     * @param caller_functionentry_prop  the relevant property at the function entry of the caller
     */
    private static void addRootOrPredecessors(RGNode n,
                                              AbstractNode call_node,
                                              Context caller_context,
                                              Context edge_context,
                                              State caller_functionentry_state,
                                              PropertyReference caller_functionentry_prop,
                                              RecoveryGraph g,
                                              boolean partial) {
        if (isOK(caller_functionentry_state, caller_functionentry_prop, partial)) // value is available at the function entry of the caller
            g.addRoot(n);
        else
            g.addNodeAndEdge(call_node, caller_context, caller_functionentry_prop, edge_context, n); // proceed backward through call graph
    }

    /**
     * Recovers the value to the function entry of n from the caller via the call edge.
     *
     * @param n the recovery graph node
     * @return true if the value changed at n
     */
    private static boolean recoverAtRootFromCallerFunctionEntry(RGNode n,
                                                                State caller_functionentry_state,
                                                                PropertyReference caller_functionentry_prop,
                                                                State call_edge_state,
                                                                PropertyReference call_edge_prop,
                                                                State callee_functionentry_state,
                                                                boolean partial,
                                                                RecoveryGraph g) {
        if (isOK(caller_functionentry_state, caller_functionentry_prop, partial)) { // recover from entry of caller function
            Value value_at_call_edge = partial ? null : getValue(call_edge_state, call_edge_prop);
            propagate(caller_functionentry_state, caller_functionentry_prop, call_edge_state, call_edge_prop, call_edge_state.getSummarized(), partial, false, value_at_call_edge);
            BlockAndContext<Context> ce = BlockAndContext.makeEntry(n.getNode().getBlock(), n.getContext());
            Value poly_at_callee_functionentry = partial ? null : g.getPolymorphic(ce.getBlock().getFirstNode(), ce.getContext(), n.getPropertyReference());
            return propagate(call_edge_state, call_edge_prop, callee_functionentry_state, n.getPropertyReference(), null, partial, true, poly_at_callee_functionentry);
        }
        return false;
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
    private static PropertyReferencePair toEntry(State s, PropertyReference prop) {
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
     * Returns the designated value, or null if internal scope.
     */
    private static Value getValue(State s, PropertyReference prop) {
        switch (prop.getKind()) {
            case INTERNAL_SCOPE:
                return null;
            default:
                Obj obj = s.getObject(prop.getObjectLabel(), false);
                return obj.getValue(prop);
        }
    }

    /**
     * Checks whether the property is OK
     * (that is, non-'unknown' for partial recovery and non-'unknown'-nor-polymorphic for full recovery).
     */
    private static boolean isOK(State s, PropertyReference prop, boolean partial) {
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
     *
     * @param summarized summarization sets, null if no summarization necessary
     * @param to_entry   propagation to function entry if true, propagating from function entry if false
     * @param orig_dst_v original value at dst, if polymorphic
     * @return true if dst is changed
     */
    private static boolean propagate(State src_s, PropertyReference src_prop, State dst_s, PropertyReference dst_prop,
                                     Summarized summarized, boolean partial, boolean to_entry, Value orig_dst_v) {
        if (src_s == dst_s && src_prop == dst_prop)
            return false; // joining to itself, nothing changes
        Obj src_obj = src_s.getObject(src_prop.getObjectLabel(), false);
        if (Options.get().isDebugOrTestEnabled() && !isOK(src_s, src_prop, partial))
            throw new AnalysisException("Unexpected value");
        Kind r = dst_prop.getKind();
        if (!(r == Kind.ORDINARY || r == Kind.INTERNAL_VALUE || r == Kind.INTERNAL_PROTOTYPE) && src_prop.getKind() != r)
            throw new AnalysisException("Unexpected property reference kind");
        boolean changed = false;
        Obj dst_obj = dst_s.getObject(dst_prop.getObjectLabel(), false);
        if (r != Kind.INTERNAL_SCOPE) {
            Value old_src_v = src_obj.getValue(src_prop);
            Value old_dst_v = dst_obj.getValue(dst_prop);
            Value src_v = to_entry ?
                    old_src_v.restrictToNotModified() : // to entry: remove modified flags
                    (partial ? old_src_v : old_src_v.summarize(summarized)); // to non-entry and full: summarize
            Value dst_v = old_dst_v;
            if (partial) {
                if (to_entry)
                    src_v = src_v.makePolymorphic(dst_prop);
                else
                    src_v = src_v.makePolymorphic(src_prop);
            } else {
                if (orig_dst_v != null && orig_dst_v.isPolymorphic()) {
                    if (orig_dst_v.isMaybePolymorphicPresent())
                        src_v = src_v.restrictToNonAttributes(); // recover only the non-attributes part
                    else
                        src_v = Value.makeNone(); // not expecting to recover anything from the polymorphic part (will propagate later)
                }
                dst_v = dst_v.makeNonPolymorphic();
            }
            Value new_dst_v = src_v.join(dst_v);
            if (new_dst_v != old_dst_v) {
                if (!dst_obj.isWritable())
                    dst_obj = dst_s.getObject(dst_prop.getObjectLabel(), true);
                dst_obj.setValue(dst_prop, new_dst_v);
                if (log.isDebugEnabled())
                    log.debug("propagating " + old_src_v + " (" + src_prop + " at " + src_s.getBasicBlock().getFirstNode().getSourceLocation() +
                            ") into " + old_dst_v + " (" + dst_prop + " at " + dst_s.getBasicBlock().getFirstNode().getSourceLocation() +
                            " block " + dst_s.getBasicBlock().getIndex() +
                            ") resulting in " + new_dst_v);
                if (r == Kind.DEFAULT_ARRAY || r == Kind.DEFAULT_NONARRAY)
                    for (String p : src_obj.getPropertyNames())
                        if (Strings.isArrayIndex(p) == (r == Kind.DEFAULT_ARRAY) && !dst_obj.getProperties().containsKey(p)) {
                            if (log.isDebugEnabled())
                                log.debug("materialized property " + p);
                            dst_obj.setProperty(p, Value.makeUnknown());
                        }
                changed = true;
            }
        } else {
            ScopeChain src_v = src_obj.getScopeChain();
            if (src_v != null && !to_entry) {
                src_v = ScopeChain.summarize(src_v, summarized);
            }
            if (!dst_obj.isWritable())
                dst_obj = dst_s.getObject(dst_prop.getObjectLabel(), true);
            if (dst_obj.isScopeChainUnknown()) {
                dst_obj.setScopeChain(src_v);
                changed = true;
            } else {
                changed = dst_obj.addToScopeChain(src_v);
            }
        }
        return changed;
    }

    /**
     * Returns the enclosing entry state for the location of the given state.
     * The enclosing entry is the nearest for-in body entry or function entry.
     */
    private static State getEntryState(State s) {
        return s.getSolverInterface().getAnalysisLatticeElement().getState(BlockAndContext.makeEntry(s.getBasicBlock(), s.getContext()));
    }

    /**
     * Returns the enclosing entry state for the given node and context.
     */
    private static State getEntryState(GenericSolver<State, Context, CallEdge, ?, ?>.SolverInterface c, Context context, AbstractNode node) {
        return c.getAnalysisLatticeElement().getState(BlockAndContext.makeEntry(node.getBlock(), context));
    }

    /**
     * Wrapper for {@link Obj#getProperty(String)}.
     * Never returns 'unknown'.
     */
    public static Value getProperty(ObjectLabel objlabel, String propertyname, State s, boolean partial) {
        Value res = s.getObject(objlabel, false).getProperty(propertyname);
        if (!isValueOK(res, partial)) {
            res = recover(s, PropertyReference.makeOrdinaryPropertyReference(objlabel, propertyname),
                    partial && !Options.get().isPolymorphicDisabled()).getProperty(propertyname);
            if (log.isDebugEnabled())
                log.debug("getProperty(" + objlabel + "," + propertyname + ") = " + res);
        }
        return res;
    }

    /**
     * Wrapper for {@link Obj#getDefaultArrayProperty}.
     * Never returns 'unknown' or polymorphic value.
     * As a side-effect, ordinary properties may be materialized.
     */
    public static Value getDefaultArrayProperty(ObjectLabel objlabel, State s) { // TODO: extend polymorphism to the default array property?
        Value res = s.getObject(objlabel, false).getDefaultArrayProperty();
        if (!isValueOK(res, false)) {
            res = recover(s, PropertyReference.makeDefaultArrayPropertyReference(objlabel), false).getDefaultArrayProperty();
            if (log.isDebugEnabled())
                log.debug("getDefaultArrayProperty(" + objlabel + ") = " + res);
        }
        return res;
    }

    /**
     * Wrapper for {@link Obj#getDefaultNonArrayProperty}.
     * Never returns 'unknown' or polymorphic value.
     * As a side-effect, ordinary properties may be materialized.
     */
    public static Value getDefaultNonArrayProperty(ObjectLabel objlabel, State s) { // TODO: extend polymorphism to the default non-array property?
        Value res = s.getObject(objlabel, false).getDefaultNonArrayProperty();
        if (!isValueOK(res, false)) {
            res = recover(s, PropertyReference.makeDefaultNonArrayPropertyReference(objlabel), false).getDefaultNonArrayProperty();
            if (log.isDebugEnabled())
                log.debug("getDefaultNonArrayProperty(" + objlabel + ") = " + res);
        }
        return res;
    }

    /**
     * Wrapper for {@link Obj#getInternalValue()}.
     * Never returns 'unknown'.
     */
    public static Value getInternalValue(ObjectLabel objlabel, State s, boolean partial) {
        Value res = s.getObject(objlabel, false).getInternalValue();
        if (!isValueOK(res, false)) {
            res = recover(s, PropertyReference.makeInternalValuePropertyReference(objlabel),
                    partial && !Options.get().isPolymorphicDisabled()).getInternalValue();
            if (log.isDebugEnabled())
                log.debug("getInternalValue(" + objlabel + ") = " + res);
        }
        return res;
    }

    /**
     * Wrapper for {@link Obj#getInternalPrototype()}.
     * Never returns 'unknown'.
     */
    public static Value getInternalPrototype(ObjectLabel objlabel, State s, boolean partial) {
        Value res = s.getObject(objlabel, false).getInternalPrototype();
        if (!isValueOK(res, false)) {
            res = recover(s, PropertyReference.makeInternalPrototypePropertyReference(objlabel),
                    partial && !Options.get().isPolymorphicDisabled()).getInternalPrototype();
            if (log.isDebugEnabled())
                log.debug("getInternalPrototype(" + objlabel + ") = " + res);
        }
        return res;
    }

    /**
     * Wrapper for {@link Obj#getScopeChain()}.
     * Never returns 'unknown'.
     */
    public static ScopeChain getScopeChain(ObjectLabel objlabel, State s) { // TODO: extend polymorphism to the scope chain property?
        Obj obj = s.getObject(objlabel, false);
        ScopeChain res;
        if (isScopeChainOK(obj, false))
            res = obj.getScopeChain();
        else {
            res = recover(s, PropertyReference.makeInternalScopePropertyReference(objlabel), false).getScopeChain();
            if (log.isDebugEnabled())
                log.debug("getScopeChain(" + objlabel + ") = " + (res != null ? res : "[]"));
        }
        return res;
    }

    /**
     * Wrapper for {@link Obj#getProperties()}.
     * The resulting map may contain 'unknown' and polymorphic values, but all property names will be present.
     * The map is not writable.
     */
    public static Map<String, Value> getProperties(ObjectLabel objlabel, State s) {
        Obj obj = s.getObject(objlabel, false);
        if (obj.getDefaultArrayProperty().isUnknown() || obj.getDefaultNonArrayProperty().isUnknown()) {
            if (obj.getDefaultArrayProperty().isUnknown())
                recover(s, PropertyReference.makeDefaultArrayPropertyReference(objlabel), false);
            if (obj.getDefaultNonArrayProperty().isUnknown())
                recover(s, PropertyReference.makeDefaultNonArrayPropertyReference(objlabel), false);
            obj = s.getObject(objlabel, false); // now all properties have been materialized from the defaults if unknown
            if (log.isDebugEnabled())
                log.debug("getProperties(" + objlabel + ")");
        }
        return obj.getProperties();
    }

    /**
     * Fully recovers the given value if polymorphic.
     */
    public static Value getRealValue(Value v, State s) {
        if (!v.isPolymorphic())
            return v;
        if (v.isMaybeAbsent() && !v.isMaybePresent())
            return Value.makeAbsent();
        State entry_state = getEntryState(s);
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
        res = v.replaceValue(res.summarize(s.getSummarized()));
        if (v.isMaybePolymorphicPresent() && res.isNotPresent())
            throw new AnalysisException(v + " recovered to " + res);
        return res;
    }

    /**
     * Checks whether the given two values can be joined.
     * Assumes that both values are non-unknown.
     *
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
     *
     * @return true if all values are non-polymorphic or all are polymorphic and of same name
     */
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
                var = v.getPropertyReference();
            } else
                some_nonpoly = true;
        }
        return !(some_poly && some_nonpoly);
    }

    /**
     * Joins the given values, performing full recovery for polymorphic values if necessary.
     * Assumes that both values are non-unknown.
     */
    public static Value join(Value v1, Value v2, State s) {
        return join(v1, s, v2, s);
    }

    /**
     * Joins the given values, performing full recovery for polymorphic values if necessary.
     * Assumes that both values are non-unknown.
     */
    public static Value join(Value v1, State s1, Value v2, State s2) {
        if (!canJoin(v1, v2)) {
            v1 = getRealValue(v1, s1);
            v2 = getRealValue(v2, s2);
        }
        return v1.join(v2);
    }

    /**
     * Joins the given values, performing full recovery for polymorphic values if necessary.
     * Assumes that the values are non-unknown.
     */
    public static Value join(Iterable<Value> values, State s) {
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

    /**
     * Localizes a value for function entry propagation.
     *
     * @param v      value to be localized
     * @param other  existing value at destination
     * @param s      current state containing v
     * @param v_prop store location of v
     */
    public static Value localize(Value v, Value other, State s, PropertyReference v_prop) {
        Value res;
        if (other.isUnknown()) {
            // reduce to unknown
            res = Value.makeUnknown();
        } else if (other.isPolymorphic()) {
            // reduce or recover to polymorphic
            if (v.isUnknown()) {
                v = s.readProperty(v_prop, true);
            }
            res = v.makePolymorphic(other.getPropertyReference());
        } else { // other is fully known value
            if (v.isUnknown()) {
                v = s.readProperty(v_prop, false);
            } else if (v.isPolymorphic()) {
                v = getRealValue(v, s);
            }
            res = v.restrictToNotModified();
        }
        return res;
    }
}
