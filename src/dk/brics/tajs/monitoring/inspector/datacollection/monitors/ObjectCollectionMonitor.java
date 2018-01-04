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

package dk.brics.tajs.monitoring.inspector.datacollection.monitors;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newMap;

public class ObjectCollectionMonitor extends DefaultAnalysisMonitoring {

    private final Map<Pair<AbstractNode, Context>, Set<ObjectLabel>> objects;

    public ObjectCollectionMonitor() {
        objects = newMap();
    }

    @Override
    public void visitNewObject(AbstractNode node, ObjectLabel label, State state) {
        addToMapSet(objects, Pair.make(node, state.getContext()), label);
    }

    @Override
    public void visitRenameObject(AbstractNode node, ObjectLabel from, ObjectLabel to, State state) {
        addToMapSet(objects, Pair.make(node, state.getContext()), to);
    }

    public Set<ObjectLabel> getObjects() {
        return objects.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
    }

    public Map<AbstractNode, Set<ObjectLabel>> getByAllocationSite() {
        return objects.entrySet().stream()
                .collect(
                        Collectors.groupingBy(e -> e.getKey().getFirst(),
                                java.util.stream.Collectors.mapping(Map.Entry::getValue,
                                        Collector.of(
                                                Collections::<ObjectLabel>newSet,
                                                Set::addAll /* only difference from TAJSCollectors.toSet */, (acc1, acc2) -> {
                                                    acc1.addAll(acc2);
                                                    return acc1;
                                                }))));
    }
}
