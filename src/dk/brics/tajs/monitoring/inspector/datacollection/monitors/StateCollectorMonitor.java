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
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;

import java.util.Comparator;
import java.util.Map;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * A monitor that collects all the post-transfer states in the analysis. Only the most recent state is kept for each block and context pair.
 */
public class StateCollectorMonitor extends DefaultAnalysisMonitoring {

    private final Map<BlockAndContext<Context>, State> states;

    public StateCollectorMonitor() {
        states = newMap();
    }

    @Override
    public void visitBlockTransferPost(BasicBlock b, State state) {
        states.put(new BlockAndContext<>(b, state.getContext()), state);
    }

    public Map<BlockAndContext<Context>, State> getStates() {
        return states;
    }

    public Map<AbstractNode, Integer> getMaxStateSizes() {
        return states.entrySet().stream()
                // compute max state size per block
                .collect(Collectors.groupingBy(e -> e.getKey().getBlock(),
                        java.util.stream.Collectors.mapping(e -> e.getValue().getStore().values().stream()
                                        .flatMap(o -> o.getProperties().values().stream())
                                        .mapToInt(p -> p.getAllObjectLabels().size())
                                        .sum(),
                                java.util.stream.Collectors.maxBy(Comparator.comparing(Integer::doubleValue))
                        ))).entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                // spread out to each node
                .flatMap(e -> e.getKey().getNodes().stream().map(n -> Pair.make(n, e.getValue())))
                .collect(Collectors.toMap(Pair::getFirst, e -> e.getSecond().get()));
    }
}
