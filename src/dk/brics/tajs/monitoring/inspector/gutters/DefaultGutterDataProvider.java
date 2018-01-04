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

package dk.brics.tajs.monitoring.inspector.gutters;

import dk.brics.tajs.monitoring.TypeCollector;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.ContextRegistrationMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.EventHandlerRegistrationMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.LazyPropagationMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.NodeTransferTimeMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.ObjectCollectionMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.PropagationMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.StateCollectorMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.VisitationMonitoring;
import dk.brics.tajs.solver.Message;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Utility class for constructing {@link DefaultGutterData}
 */
public class DefaultGutterDataProvider {

    public final EventHandlerRegistrationMonitor eventHandlerRegistrationMonitor;

    private final PropagationMonitor propagationMonitor;

    private final ContextRegistrationMonitor contextInsensitivityMapperMonitor;

    private final ObjectCollectionMonitor allocationCollectingMonitor;

    private final NodeTransferTimeMonitor timeMonitor;

    private final Supplier<Set<Message>> messageMonitor;

    private final VisitationMonitoring visitationMonitoringIAnalysis;

    private final TypeCollector typeCollector;

    private final LazyPropagationMonitor lazyPropagationMonitor;

    private final StateCollectorMonitor stateCollectorMonitor;

    public DefaultGutterDataProvider(EventHandlerRegistrationMonitor eventHandlerRegistrationMonitor,
                                     PropagationMonitor propagationMonitor,
                                     ContextRegistrationMonitor contextRegistrationMonitor,
                                     ObjectCollectionMonitor allocationCollectingMonitor,
                                     NodeTransferTimeMonitor timeMonitor,
                                     Supplier<Set<Message>> messageMonitor,
                                     VisitationMonitoring visitationMonitoringIAnalysis,
                                     TypeCollector typeCollector,
                                     LazyPropagationMonitor lazyPropagationMonitor,
                                     StateCollectorMonitor stateCollectorMonitor
    ) {
        this.eventHandlerRegistrationMonitor = eventHandlerRegistrationMonitor;
        this.propagationMonitor = propagationMonitor;
        this.contextInsensitivityMapperMonitor = contextRegistrationMonitor;
        this.allocationCollectingMonitor = allocationCollectingMonitor;
        this.timeMonitor = timeMonitor;
        this.messageMonitor = messageMonitor;
        this.visitationMonitoringIAnalysis = visitationMonitoringIAnalysis;
        this.typeCollector = typeCollector;
        this.lazyPropagationMonitor = lazyPropagationMonitor;
        this.stateCollectorMonitor = stateCollectorMonitor;
    }

    public DefaultGutterData create() {
        return new DefaultGutterData(
                propagationMonitor.getData(),
                contextInsensitivityMapperMonitor.getMap(),
                allocationCollectingMonitor.getByAllocationSite(),
                timeMonitor.getTimesForNodes(),
                messageMonitor.get(),
                contextInsensitivityMapperMonitor.getContextsPerLine(),
                typeCollector,
                lazyPropagationMonitor.getData(),
                stateCollectorMonitor.getMaxStateSizes(),
                visitationMonitoringIAnalysis.createLineVisitingInfo()
        );
    }
}
