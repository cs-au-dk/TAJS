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

package dk.brics.tajs.monitoring.inspector.datacollection;

import dk.brics.tajs.monitoring.inspector.datacollection.monitors.ContextRegistrationMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.EventHandlerRegistrationMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.ObjectCollectionMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.PropagationMonitor;

/**
 * Utility class for constructing {@link InspectorData}
 */
public class InspectorDataProvider {

    private final EventHandlerRegistrationMonitor eventHandlerRegistrationMonitor;

    private final PropagationMonitor propagationMonitor;

    private final ContextRegistrationMonitor contextRegistrationMonitor;

    private final ObjectCollectionMonitor allocationCollectingMonitor;

    public InspectorDataProvider(EventHandlerRegistrationMonitor eventHandlerRegistrationMonitor,
                                 PropagationMonitor propagationMonitor,
                                 ContextRegistrationMonitor contextRegistrationMonitor,
                                 ObjectCollectionMonitor allocationCollectingMonitor) {
        this.eventHandlerRegistrationMonitor = eventHandlerRegistrationMonitor;
        this.propagationMonitor = propagationMonitor;
        this.contextRegistrationMonitor = contextRegistrationMonitor;
        this.allocationCollectingMonitor = allocationCollectingMonitor;
    }

    public InspectorData get() {
        return new InspectorData(
                eventHandlerRegistrationMonitor.getMap(),
                propagationMonitor.getData(),
                contextRegistrationMonitor.getMap(),
                allocationCollectingMonitor.getByAllocationSite()
        );
    }
}
