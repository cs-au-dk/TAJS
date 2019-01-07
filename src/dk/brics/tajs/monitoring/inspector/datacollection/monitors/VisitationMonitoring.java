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

package dk.brics.tajs.monitoring.inspector.datacollection.monitors;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.inspector.datacollection.SourceLine;
import dk.brics.tajs.monitoring.inspector.util.OccurenceCountingMap;
import dk.brics.tajs.util.Collectors;

import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

public class VisitationMonitoring extends DefaultAnalysisMonitoring {

    /**
     * Contains all nodes that are analyzed
     */
    private final Set<AbstractNode> visitedNodes;

    /**
     * A counter for how many nodes there are on each line
     */
    private final OccurenceCountingMap<SourceLine> nodesPerLineMap;

    /**
     * A counter for how many blocks there are on each line
     */
    private final OccurenceCountingMap<SourceLine> blocksPerLineMap;

    /**
     * A counter for how many times blocks on a line have been analyzed
     */
    private final OccurenceCountingMap<SourceLine> visitCountMapByBlocks;

    /**
     * Contains all the functions that have been analyzed
     */
    private final Set<Function> seenFunctions = newSet();

    public VisitationMonitoring() {
        this.visitCountMapByBlocks = new OccurenceCountingMap<>();
        this.visitedNodes = new HashSet<>();
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
        return new Info(visitCountMapByBlocks, visitedNodes, blocksPerLineMap, nodesPerLineMap);
    }

    private void addFunction(Function f) {
        if (seenFunctions.contains(f)) {
            return;
        }
        seenFunctions.add(f);
        nodesPerLineMap.countAll(makeNodesPerLineMap(f));
        blocksPerLineMap.countAll(makeBlocksPerLineMap(f));
    }

    @Override
    public void visitNodeTransferPre(AbstractNode n, State state) {
        visitedNodes.add(n);
    }

    @Override
    public void visitBlockTransferPost(BasicBlock block, State state) {
        addFunction(block.getFunction());
        Set<SourceLine> lines = new HashSet<>();
        for (AbstractNode node : block.getNodes()) {
            if (node.getSourceLocation().getLocation() == null)
                continue; // node js artificial nodes doesn't have a corresponding file location, ReportMaker reads it.

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

        private final Set<AbstractNode> visitedNodes;

        private final OccurenceCountingMap<SourceLine> blockVisitCountsPerLine;

        private final OccurenceCountingMap<SourceLine> blocksPerLine;

        private final OccurenceCountingMap<SourceLine> nodesPerLine;

        public Info(OccurenceCountingMap<SourceLine> blockVisitCountsPerLine, Set<AbstractNode> visitedNodes, OccurenceCountingMap<SourceLine> blocksPerLine, OccurenceCountingMap<SourceLine> nodesPerLine) {
            this.blockVisitCountsPerLine = blockVisitCountsPerLine;
            this.visitedNodes = visitedNodes;
            this.blocksPerLine = blocksPerLine;
            this.nodesPerLine = nodesPerLine;
        }

        public OccurenceCountingMap<SourceLine> getBlocksPerLine() {
            return blocksPerLine;
        }

        public OccurenceCountingMap<SourceLine> getNodesPerLine() {
            return nodesPerLine;
        }

        public OccurenceCountingMap<SourceLine> getBlockVisitCountsPerLine() {
            return blockVisitCountsPerLine;
        }

        public Map<URL, Set<Integer>> getAbstractLiveLines() {
            return convertNodeSetToURLLineSetMap(visitedNodes);
        }

        private Map<URL, Set<Integer>> convertNodeSetToURLLineSetMap(Set<AbstractNode> nodes) {
            return nodes.stream()
                    .filter(n -> n.getSourceLocation().getLocation() != null)
                    .collect(Collectors.groupingBy(n -> n.getSourceLocation().getLocation(), java.util.stream.Collectors.mapping(n -> n.getSourceLocation().getLineNumber(), Collectors.toSet())));
        }
    }
}
