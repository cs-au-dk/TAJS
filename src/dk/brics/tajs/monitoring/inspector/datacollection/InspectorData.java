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

package dk.brics.tajs.monitoring.inspector.datacollection;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.PropagationMonitor;
import dk.brics.tajs.util.Pair;

import java.util.Map;
import java.util.Set;

/**
 * Simple Java bean with lots of different data to be used by the {@link dk.brics.tajs.monitoring.inspector.api.TAJSInspectorAPI}
 */
public class InspectorData {

    public final Map<Pair<AbstractNode, Context>, Set<Value>> eventHandlerRegistrationLocations;

    public final PropagationMonitor.PropagationData propagationData;

    public final Map<BasicBlock, Set<Context>> blockContexts;

    public final Map<AbstractNode, Set<ObjectLabel>> allocationSiteMap;

    public InspectorData(Map<Pair<AbstractNode, Context>, Set<Value>> eventHandlerRegistrationLocations,
                         PropagationMonitor.PropagationData propagationData,
                         Map<BasicBlock, Set<Context>> blockContexts,
                         Map<AbstractNode, Set<ObjectLabel>> allocationSiteMap) {
        this.eventHandlerRegistrationLocations = eventHandlerRegistrationLocations;
        this.propagationData = propagationData;
        this.blockContexts = blockContexts;
        this.allocationSiteMap = allocationSiteMap;
    }
}
