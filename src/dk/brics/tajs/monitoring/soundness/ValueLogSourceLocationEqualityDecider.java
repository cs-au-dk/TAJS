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

package dk.brics.tajs.monitoring.soundness;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;

import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Decision procedure for deciding if a TAJS and log-entry source location are equal.
 */
public class ValueLogSourceLocationEqualityDecider {

    private final Set<Pair<dk.au.cs.casa.jer.entries.SourceLocation, SourceLocation>> sourceLocationEqualities;

    public ValueLogSourceLocationEqualityDecider(Map<SourceLocation, Set<SourceLocation>> tajsLocation2jalangiLocation, FlowGraph flowGraph) {
        this.sourceLocationEqualities = makeSourceLocationEqualities(tajsLocation2jalangiLocation, flowGraph);
    }

    private Set<SourceLocation> getAllSourceLocations(FlowGraph flowGraph) {
        return flowGraph.getFunctions().stream()
                .flatMap(f -> f.getBlocks().stream()
                        .flatMap(b -> b.getNodes().stream()))
                .filter(n -> n.getSourceLocation().getLocation() != null)
                .map(AbstractNode::getSourceLocation)
                .collect(Collectors.toSet());
    }

    private Set<Pair<dk.au.cs.casa.jer.entries.SourceLocation, SourceLocation>> makeSourceLocationEqualities(Map<SourceLocation, Set<SourceLocation>> tajsLocation2jalangiLocation, FlowGraph flowGraph) {
        Set<Pair<dk.au.cs.casa.jer.entries.SourceLocation, SourceLocation>> equalities = newSet();
        getAllSourceLocations(flowGraph).stream()
                .filter(l -> !flowGraph.isHostEnvironmentSource(l))
                .forEach(n -> {
                    Set<SourceLocation> aliases = tajsLocation2jalangiLocation.getOrDefault(n, singleton(n));
                    aliases.forEach(alias -> equalities.add(Pair.make(ValueLoggerSourceLocationMapper.makeLoggerSourceLocation(alias), alias)));
                });
        return equalities;
    }

    public boolean areEqual(dk.au.cs.casa.jer.entries.SourceLocation jalangiLocation, SourceLocation realTajslocation) {
        if (jalangiLocation.getLineNumber() != realTajslocation.getLineNumber()) {
            return false; // trivially different
        }
        Pair<dk.au.cs.casa.jer.entries.SourceLocation, SourceLocation> key = Pair.make(jalangiLocation, realTajslocation);
        return sourceLocationEqualities.contains(key);
    }

    public Set<Pair<dk.au.cs.casa.jer.entries.SourceLocation, SourceLocation>> getEqualities() {
        return sourceLocationEqualities;
    }
}
