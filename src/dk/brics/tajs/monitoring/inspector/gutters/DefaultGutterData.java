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

package dk.brics.tajs.monitoring.inspector.gutters;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.monitoring.TypeCollector;
import dk.brics.tajs.monitoring.inspector.datacollection.SourceLine;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.LazyPropagationMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.PropagationMonitor.PropagationData;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.VisitationMonitoring;
import dk.brics.tajs.solver.Message;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Simple Java-bean with lots of different data to be used by the {@link DefaultGutters}
 */
public class DefaultGutterData {

    public final PropagationData propagationData;

    public final Map<BasicBlock, Set<Context>> blockContexts;

    public final Map<AbstractNode, Set<ObjectLabel>> allocationSiteMap;

    public final Map<Optional<AbstractNode>, Long> timesForNodes;

    public final Set<Message> messages;

    public final Map<SourceLine, Integer> contextsPerLine;

    public final TypeCollector typeCollector;

    public final LazyPropagationMonitor.LazyPropagationData lazyPropagationData;

    public final Map<AbstractNode, Integer> maxStateSize;

    public final VisitationMonitoring.Info flowgraphInfo;

    public DefaultGutterData(
            PropagationData propagationData,
            Map<BasicBlock, Set<Context>> blockContexts,
            Map<AbstractNode, Set<ObjectLabel>> allocationSiteMap,
            Map<Optional<AbstractNode>, Long> timesForNodes,
            Set<Message> messages,
            Map<SourceLine, Integer> contextsPerLine,
            TypeCollector typeCollector,
            LazyPropagationMonitor.LazyPropagationData lazyPropagationData,
            Map<AbstractNode, Integer> maxStateSize,
            VisitationMonitoring.Info flowgraphInfo) {
        this.propagationData = propagationData;
        this.blockContexts = blockContexts;
        this.allocationSiteMap = allocationSiteMap;
        this.timesForNodes = timesForNodes;
        this.messages = messages;
        this.contextsPerLine = contextsPerLine;
        this.typeCollector = typeCollector;
        this.lazyPropagationData = lazyPropagationData;
        this.maxStateSize = maxStateSize;
        this.flowgraphInfo = flowgraphInfo;
    }
}
