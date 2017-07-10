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
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.monitoring.AnalysisPhase;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

import static dk.brics.tajs.util.Collections.newMap;

public class NodeTransferTimeMonitor extends DefaultAnalysisMonitoring {

    private Map<Optional<AbstractNode>, Long> timesForNodes = newMap();

    private long lastStart;

    private Optional<AbstractNode> currentLocation_sanity;

    @Override
    public void visitPhasePre(AnalysisPhase phase) {
        if (phase == AnalysisPhase.ANALYSIS) {
            startNonNode();
        }
        if (phase == AnalysisPhase.SCAN) {
            stopNonNode();
        }
    }

    private void stop(Optional<AbstractNode> location) {
        sanityCheckStop(location);
        long stop = System.nanoTime();
        final long delta = stop - lastStart + 1;
        timesForNodes.putIfAbsent(location, 0L);
        timesForNodes.put(location, timesForNodes.get(location) + delta);
    }

    private void sanityCheckStop(Optional<AbstractNode> location) {
        if (!currentLocation_sanity.equals(location)) {
            throw new IllegalStateException(String.format("Expected location to stop for to be %s, but got %s", currentLocation_sanity, location));
        }
    }

    private void start(Optional<AbstractNode> location) {
        lastStart = System.nanoTime();
        currentLocation_sanity = location;
    }

    @Override
    public void visitNodeTransferPost(AbstractNode n, State s) {
        stopNode(n);
        startNonNode();
    }

    @Override
    public void visitNodeTransferPre(AbstractNode n, State s) {
        stopNonNode();
        startNode(n);
    }

    private void startNonNode() {
        start(Optional.empty());
    }

    private void stopNonNode() {
        stop(Optional.empty());
    }

    private void startNode(AbstractNode n) {
        start(Optional.of(n));
    }

    private void stopNode(AbstractNode n) {
        stop(Optional.of(n));
    }

    public Map<Optional<AbstractNode>, Long> getTimesForNodes() {
        return timesForNodes;
    }

    public Map<Optional<Pair<URL, Integer>>, Long> getTimes() {
        Map<Optional<Pair<URL, Integer>>, Long> byUrlAndLine = timesForNodes.entrySet().stream()
                .collect(Collectors.groupingBy(e -> {
                    Optional<AbstractNode> nodeKey = e.getKey();
                    Optional<Pair<URL, Integer>> urlLineKey;
                    if (!nodeKey.isPresent()) {
                        urlLineKey = Optional.empty();
                    } else {
                        SourceLocation location = nodeKey.get().getSourceLocation();
                        if (location.getLocation() == null) {
                            urlLineKey = Optional.empty();
                        } else {
                            urlLineKey = Optional.of(Pair.make(location.getLocation(), location.getLineNumber()));
                        }
                    }
                    return urlLineKey;
                }, java.util.stream.Collectors.summingLong(Map.Entry::getValue)));
        return byUrlAndLine;
    }
}
