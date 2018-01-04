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
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.inspector.util.OccurenceCountingMap;

import java.util.Map;

import static dk.brics.tajs.util.Collections.newMap;

public class LazyPropagationMonitor extends DefaultAnalysisMonitoring {

    /**
     * Number of calls to the UnknownValueResolver where a value is recovered partially in non-scanning mode at each node.
     */
    private OccurenceCountingMap<AbstractNode> unknown_value_resolve_analyzing_partial = new OccurenceCountingMap<>();

    /**
     * Number of calls to the UnknownValueResolver where a value is recovered fully in non-scanning mode at each node.
     */
    private OccurenceCountingMap<AbstractNode> unknown_value_resolve_analyzing_full = new OccurenceCountingMap<>();

    /**
     * Maximal size of property recovery graphs at each node.
     */
    private Map<AbstractNode, Integer> max_recovery_graph_sizes = newMap();

    public Map<AbstractNode, Integer> getUnknown_value_resolve_analyzing_partial() {
        return unknown_value_resolve_analyzing_partial.getMapView();
    }

    public Map<AbstractNode, Integer> getUnknown_value_resolve_analyzing_full() {
        return unknown_value_resolve_analyzing_full.getMapView();
    }

    public Map<AbstractNode, Integer> getMax_recovery_graph_sizes() {
        return max_recovery_graph_sizes;
    }

    @Override
    public void visitUnknownValueResolve(AbstractNode node, boolean partial, boolean scanning) {
        if (partial) {
            unknown_value_resolve_analyzing_partial.count(node);
        } else {
            unknown_value_resolve_analyzing_full.count(node);
        }
    }

    @Override
    public void visitRecoveryGraph(AbstractNode node, int size) {
        if (!max_recovery_graph_sizes.containsKey(node)) {
            max_recovery_graph_sizes.put(node, size);
        }
        max_recovery_graph_sizes.put(node, Math.max(max_recovery_graph_sizes.get(node), size));
    }

    public LazyPropagationData getData() {
        return new LazyPropagationData(unknown_value_resolve_analyzing_full.getMapView(), unknown_value_resolve_analyzing_partial.getMapView(), max_recovery_graph_sizes);
    }

    public static class LazyPropagationData {

        public final Map<AbstractNode, Integer> fullRecovers;

        public final Map<AbstractNode, Integer> partialRecovers;

        public final Map<AbstractNode, Integer> maxRecoveryGraphSize;

        public LazyPropagationData(Map<AbstractNode, Integer> fullRecovers, Map<AbstractNode, Integer> partialRecovers, Map<AbstractNode, Integer> maxRecoveryGraphSize) {
            this.fullRecovers = fullRecovers;
            this.partialRecovers = partialRecovers;
            this.maxRecoveryGraphSize = maxRecoveryGraphSize;
        }
    }
}
