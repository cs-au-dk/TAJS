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

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.inspector.datacollection.SourceLine;
import dk.brics.tajs.monitoring.inspector.util.OccurenceCountingMap;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

public class ContextRegistrationMonitor extends DefaultAnalysisMonitoring {

    private final Map<BasicBlock, Set<Context>> map = newMap();

    private static SourceLine makeWithUnspecifiedColumn(SourceLocation sourceLocation) {
        URL location = sourceLocation.getLocation();
        if (location != null) {
            return new SourceLine(location, sourceLocation.getLineNumber());
        }
        return null;
    }

    @Override
    public void visitPropagationPost(BlockAndContext<Context> from, BlockAndContext<Context> to, boolean changed) {
        addToMapSet(map, from.getBlock(), from.getContext());
        addToMapSet(map, to.getBlock(), to.getContext());
    }

    public Map<BasicBlock, Set<Context>> getMap() {
        return map;
    }

    public Map<SourceLine, Integer> getContextsPerLine() {
        OccurenceCountingMap<SourceLine> map = new OccurenceCountingMap<>();
        this.map.entrySet().stream()
                .flatMap(e1 -> e1.getKey().getNodes().stream().map(n -> Pair.make(n, e1.getValue())))
                .filter(e -> e.getFirst().getSourceLocation().getLocation() != null)
                .collect(Collectors.groupingBy(e1 -> makeWithUnspecifiedColumn(e1.getFirst().getSourceLocation()), java.util.stream.Collectors.mapping(Pair::getSecond, Collectors.toSet()))).entrySet().stream()
                .map(e -> Pair.make(e.getKey(), newSet(e.getValue().stream().flatMap(Collection::stream).collect(Collectors.toSet()))))
                .forEach(e -> map.count(e.getFirst(), e.getSecond().size()));
        return map.getMapView();
    }
}
