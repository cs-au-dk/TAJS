/*
 * Copyright 2009-2019 Aarhus University
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

package dk.brics.tajs.monitoring;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.CatchNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.ReturnNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;

import java.util.Collection;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Monitor for reachability information.
 * <p>
 * Implementation note: the collected information is context sensitive, but the exposed API is currently context insensitive.
 */
public class ReachabilityMonitor extends DefaultAnalysisMonitoring {

    private Set<Pair<AbstractNode, Context>> reachable = newSet();

    private Set<Function> functions = newSet();

    @Override
    public void visitFunction(Function f, Collection<State> entry_states) {
        functions.add(f);
    }

    public Set<AbstractNode> getReachableNodes() {
        return reachable.stream()
                .map(Pair::getFirst)
                .collect(Collectors.toSet());
    }

    public Set<Function> getReachableFunctions() {
        return reachable.stream()
                .map(n -> n.getFirst().getBlock().getFunction())
                .collect(Collectors.toSet());
    }

    public Set<Function> getUnreachableFunctions() {
        Collection<Function> all = functions;
        Set<Function> reachable = getReachableFunctions();
        Set<Function> unreachable = newSet(all);
        unreachable.removeAll(reachable);
        return unreachable;
    }

    @Override
    public void visitNodeTransferPre(AbstractNode n, State s) {
        n = n.getDuplicateOf() != null ? n.getDuplicateOf() : n;
        reachable.add(Pair.make(n, s.getContext()));
    }

    /**
     * Finds the unreachable nodes in a function that are not dominated by other unreahable nodes.
     */
    public Set<AbstractNode> getUndominatedUnreachableNodes(Function f, boolean ignoreIfNodeSuccessors) {
        Set<AbstractNode> reachableNodes = getReachableNodes();
        Set<AbstractNode> firstUnreachableNodes = newSet();
        Set<BasicBlock> successors = newSet();
        Set<BasicBlock> successors_of_blocks_where_last_non_artificial_node_is_reachable = newSet();

        // block relations
        for (BasicBlock b : f.getBlocks()) {
            successors.addAll(b.getSuccessors());
            if (b.getExceptionHandler() != null)
                successors.add(b.getExceptionHandler());
            AbstractNode lastNonArtificialNode = null;
            for (AbstractNode node : b.getNodes()) {
                if (!node.isArtificial()) {
                    lastNonArtificialNode = node;
                }
            }
            if (((lastNonArtificialNode == null && successors_of_blocks_where_last_non_artificial_node_is_reachable.contains(b)) || reachableNodes.contains(lastNonArtificialNode)) &&
                    !(ignoreIfNodeSuccessors && b.getLastNode() instanceof IfNode)) // optionally exclude if node successors (those are reported separately if always true/false in monitoring)
                successors_of_blocks_where_last_non_artificial_node_is_reachable.addAll(b.getSuccessors());
        }

        // node relations
        for (BasicBlock b : f.getBlocks()) {
            boolean predecessorIsReachable = false;
            boolean isFirstNonArtificialNodeInBlock = true;
            for (AbstractNode n : b.getNodes()) {
                if (n.getDuplicateOf() == null && !n.isArtificial() && !(n instanceof ReturnNode)) { // ignore duplicate nodes, artificial nodes, and return nodes
                    if (!reachableNodes.contains(n)) {
                        final boolean shouldUse;
                        if (isFirstNonArtificialNodeInBlock) {
                            shouldUse = (n instanceof CatchNode || // unreachable catch block
                                    successors_of_blocks_where_last_non_artificial_node_is_reachable.contains(b) || // unreachable first node but predecessor block is reachable
                                    (!b.isEntry() && !successors.contains(b))); // block is neither function entry nor successor of another block
                        } else {
                            shouldUse = predecessorIsReachable;
                        }

                        if (shouldUse) { // previous node in the block killed the flow
                            firstUnreachableNodes.add(n);
                            break; // skip remaining nodes for this block
                        }
                    }

                    predecessorIsReachable = reachableNodes.contains(n);
                }
                if (!n.isArtificial()) {
                    isFirstNonArtificialNodeInBlock = false;
                }
            }
        }
        return firstUnreachableNodes;
    }
}
