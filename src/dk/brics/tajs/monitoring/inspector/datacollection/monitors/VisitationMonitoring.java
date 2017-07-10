/*
 * Copyright 2009-2017 Aarhus University
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

package dk.brics.tajs.monitoring.inspector.datacollection.monitors;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.AssumeNode;
import dk.brics.tajs.flowgraph.jsnodes.CatchNode;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.inspector.datacollection.SourceLine;
import dk.brics.tajs.monitoring.inspector.util.OccurenceCountingMap;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import static dk.brics.tajs.flowgraph.jsnodes.AssumeNode.Kind.UNREACHABLE;
import static dk.brics.tajs.util.Collections.newSet;

public class VisitationMonitoring extends DefaultAnalysisMonitoring {

    private final Set<AbstractNode> visitableNodes;

    private final Set<AbstractNode> reachableNodes;

    private final OccurenceCountingMap<SourceLine> nodesPerLineMap;

    private final OccurenceCountingMap<SourceLine> blocksPerLineMap;

    private final OccurenceCountingMap<SourceLine> visitCountMapByBlocks;

    private final Set<Function> seenFunctions = newSet();

    public VisitationMonitoring() {
        this.visitCountMapByBlocks = new OccurenceCountingMap<>();
        this.visitableNodes = new HashSet<>();
        this.reachableNodes = new HashSet<>();
        this.nodesPerLineMap = new OccurenceCountingMap<>();
        this.blocksPerLineMap = new OccurenceCountingMap<>();
    }

    private static OccurenceCountingMap<SourceLine> makeBlocksPerLineMap(Function f) {
        OccurenceCountingMap<SourceLine> map = new OccurenceCountingMap<>();
        for (BasicBlock b : f.getBlocks()) {
            Set<Integer> lines = new HashSet<>();
            for (AbstractNode n : b.getNodes()) {
                if (!n.isArtificial()) {
                    final int lineNumber = n.getSourceLocation().getLineNumber();
                    if (!lines.contains(lineNumber)) {
                        // count each line in each block exactly once, regardless of how many nodes each block has
                        final SourceLine lineSourceLocation = makeWithUnspecifiedColumn(n.getSourceLocation());
                        if (lineSourceLocation != null) {
                            map.count(lineSourceLocation);
                        }
                        lines.add(lineNumber);
                    }
                }
            }
        }
        return map;
    }

    private static OccurenceCountingMap<SourceLine> makeNodesPerLineMap(Function f) {
        OccurenceCountingMap<SourceLine> map = new OccurenceCountingMap<>();
        for (BasicBlock b : f.getBlocks()) {
            for (AbstractNode n : b.getNodes()) {
                final SourceLine lineSourceLocation = makeWithUnspecifiedColumn(n.getSourceLocation());
                if (lineSourceLocation != null) {
                    map.count(lineSourceLocation);
                }
            }
        }
        return map;
    }

    private static SourceLine makeWithUnspecifiedColumn(SourceLocation sourceLocation) {
        URL location = sourceLocation.getLocation();
        if (location != null) {
            return new SourceLine(location, sourceLocation.getLineNumber());
        }
        return null;
    }

    public Info createLineVisitingInfo() {
        return new Info(visitCountMapByBlocks, visitableNodes, reachableNodes, blocksPerLineMap, nodesPerLineMap);
    }

    private void addFunction(Function f) {
        if (seenFunctions.contains(f)) {
            return;
        }
        seenFunctions.add(f);
        visitableNodes.addAll(VisitableNodesAnalyzer.analyze(f, false));
        reachableNodes.addAll(VisitableNodesAnalyzer.analyze(f, true));
        nodesPerLineMap.countAll(makeNodesPerLineMap(f));
        blocksPerLineMap.countAll(makeBlocksPerLineMap(f));
    }

    @Override
    public void visitBlockTransferPost(BasicBlock block, State state) {
        addFunction(block.getFunction());
        Set<SourceLine> lines = new HashSet<>();
        for (AbstractNode node : block.getNodes()) {
            if (!visitableNodes.contains(node)) {
                continue;
            }
            if (node.getSourceLocation().getLocation() == null)
                continue; // node js artificial nodes doesn't have a corresponding file location, ReportMaker reads it.
            if (Options.get().isIgnoreUnreachableEnabled() && !reachableNodes.contains(node)) {
                continue;
            }

            final SourceLocation sourceLocation = node.getSourceLocation();
            // only count each line once per transfer
            final SourceLine lineSourceLocation = makeWithUnspecifiedColumn(sourceLocation);
            if (!lines.contains(lineSourceLocation)) { // only count each line once per block transfer
                // System.out.format("Visiting line %d with node of type %s (column: %s)%n", lineNumber, node.getClass().getSimpleName(), sourceLocation.getColumnNumber());
                visitCountMapByBlocks.count(lineSourceLocation);
                lines.add(lineSourceLocation);
            }
        }
    }

    public static class Info {

        private final Set<AbstractNode> visitableNodes;

        private final Set<AbstractNode> reachableNodes;

        private final OccurenceCountingMap<SourceLine> blockVisitCountsPerLine;

        private final OccurenceCountingMap<SourceLine> blocksPerLine;

        private final OccurenceCountingMap<SourceLine> nodesPerLine;

        public Info(OccurenceCountingMap<SourceLine> blockVisitCountsPerLine, Set<AbstractNode> visitableNodes, Set<AbstractNode> reachableNodes, OccurenceCountingMap<SourceLine> blocksPerLine, OccurenceCountingMap<SourceLine> nodesPerLine) {
            this.blockVisitCountsPerLine = blockVisitCountsPerLine;
            this.visitableNodes = visitableNodes;
            this.reachableNodes = reachableNodes;
            this.blocksPerLine = blocksPerLine;
            this.nodesPerLine = nodesPerLine;
        }

        public OccurenceCountingMap<SourceLine> getBlocksPerLine() {
            return blocksPerLine;
        }

        public OccurenceCountingMap<SourceLine> getNodesPerLine() {
            return nodesPerLine;
        }

        public Set<AbstractNode> getReachableNodes() {
            return reachableNodes;
        }

        public OccurenceCountingMap<SourceLine> getBlockVisitCountsPerLine() {
            return blockVisitCountsPerLine;
        }

        public Set<AbstractNode> getVisitableNodesPerLine() {
            return visitableNodes;
        }

        public Map<URL, Set<Integer>> getVisitableLinesPerFile() {
            return convertNodeSetToURLLineSetMap(visitableNodes);
        }

        public Map<URL, Set<Integer>> getReachableLinesPerFile() {
            return convertNodeSetToURLLineSetMap(reachableNodes);
        }

        private Map<URL, Set<Integer>> convertNodeSetToURLLineSetMap(Set<AbstractNode> nodes) {
            return nodes.stream()
                    .filter(n -> n.getSourceLocation().getLocation() != null)
                    .collect(Collectors.groupingBy(n -> n.getSourceLocation().getLocation(), java.util.stream.Collectors.mapping(n -> n.getSourceLocation().getLineNumber(), Collectors.toSet())));
        }
    }

    private static class VisitableNodesAnalyzer {
        private static final boolean DEBUG_LOCAL = false;

        public static Set<AbstractNode> analyze(Function f, boolean useReachabilityInformation) {
            Set<AbstractNode> visitableNodes = new HashSet<>();
            Set<BasicBlock> visitedBlocks = new HashSet<>();
            Map<BasicBlock, SourceLocation> unreachableDirectiveLocationsInBlocks = new HashMap<>();
            Stack<BasicBlock> workList = new Stack<>();

            workList.add(f.getEntry());

            // reachability analysis for the blocks
            while (!workList.isEmpty()) {
                BasicBlock block = workList.pop();
                if (visitedBlocks.contains(block) || f != block.getFunction()) {
                    continue;
                }
                visitedBlocks.add(block);
                List<AbstractNode> nodes = block.getNodes();
                boolean canFlowThrough = true;
                for (AbstractNode node : nodes) {
                    if (useReachabilityInformation && node instanceof AssumeNode && ((AssumeNode) node).getKind() == UNREACHABLE) {
                        canFlowThrough = false;
                        // theoretic problem: multiple NO_FLOW nodes in same block, but the last of them is visited first.
                        unreachableDirectiveLocationsInBlocks.put(block, node.getSourceLocation());
                        break;
                    }
                }
                if (canFlowThrough) {
                    final BasicBlock exceptionHandler = block.getExceptionHandler();
                    if (exceptionHandler != null)
                        workList.add(exceptionHandler);
                    workList.addAll(block.getSuccessors());
                }
            }

            // ignore the first block of each function: it is pure de
            visitedBlocks.remove(f.getEntry());


            // for all the reachable blocks, register the nodes which are located prior to NO_FLOW nodes
            for (BasicBlock block : visitedBlocks) {
                for (AbstractNode node : block.getNodes()) {
                    // the node is reachable if it is in a block without a NO_FLOW node, or if it is located prior to the location of said node.
                    final boolean reachable;
                    if (unreachableDirectiveLocationsInBlocks.containsKey(block)) {
                        reachable = isNodeBefore(node, unreachableDirectiveLocationsInBlocks.get(block));
                    } else {
                        reachable = true;
                    }

                    if (DEBUG_LOCAL) {
                        if (!reachable) {
                            System.out.format("NOT reachable: %s(line: %d)%n", node, node.getSourceLocation().getLineNumber());
                        } else {
                            System.out.format("Reachable: %s(line: %d)%n", node, node.getSourceLocation().getLineNumber());
                        }
                    }
                    final boolean visitable = reachable && !node.isArtificial() && !(node instanceof CatchNode);
                    if (visitable) {
                        visitableNodes.add(node);
                    }

                    if (DEBUG_LOCAL) {
                        if (!visitable) {
                            System.out.format("NOT visitable: %s(line: %d)%n", node, node.getSourceLocation().getLineNumber());
                        } else {
                            System.out.format("Visitable: %s(line: %d)%n", node, node.getSourceLocation().getLineNumber());
                        }
                    }
                }
            }

            return visitableNodes;
        }

        private static boolean isNodeBefore(AbstractNode node, SourceLocation sourceLocation) {
            SourceLocation nodeLocation = node.getSourceLocation();
            if (!nodeLocation.getLocation().equals(sourceLocation.getLocation())) {
                // If multiple files are included, they should get their own initializers?
                throw new AnalysisException("Nodes from different files in same block?");
            }
            if (nodeLocation.getLineNumber() == sourceLocation.getLineNumber()) {
                return nodeLocation.getColumnNumber() <= sourceLocation.getColumnNumber();
            }
            return nodeLocation.getLineNumber() <= sourceLocation.getLineNumber();
        }
    }
}
