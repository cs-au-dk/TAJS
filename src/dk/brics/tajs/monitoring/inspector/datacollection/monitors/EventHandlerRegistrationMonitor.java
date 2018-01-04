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
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.util.Pair;

import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newMap;

public class EventHandlerRegistrationMonitor extends DefaultAnalysisMonitoring {

    private final Map<Pair<AbstractNode, Context>, Set<Value>> map = newMap();

    public Map<Pair<AbstractNode, Context>, Set<Value>> getMap() {
        return map;
    }

    @Override
    public void visitEventHandlerRegistration(AbstractNode node, Context context, Value handler) {
        addToMapSet(map, Pair.make(node, context), handler);
    }
}
