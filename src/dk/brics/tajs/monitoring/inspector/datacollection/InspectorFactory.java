/*
 * Copyright 2009-2020 Aarhus University
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

import dk.brics.tajs.monitoring.AnalysisMonitor;
import dk.brics.tajs.monitoring.CompositeMonitor;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.PhaseMonitoring;
import dk.brics.tajs.monitoring.TogglableMonitor;
import dk.brics.tajs.monitoring.TypeCollector;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.ContextRegistrationMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.EventHandlerRegistrationMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.InspectorMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.LazyPropagationMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.NodeTransferTimeMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.NonLazyTypeCollectorMonitoring;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.ObjectCollectionMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.PropagationMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.StateCollectorMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.monitors.VisitationMonitoring;
import dk.brics.tajs.monitoring.inspector.gutters.DefaultGutterDataProvider;
import dk.brics.tajs.monitoring.inspector.gutters.DefaultGutters;
import dk.brics.tajs.monitoring.inspector.gutters.GutterProvider;
import dk.brics.tajs.util.Pair;

import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Utility class for instantiating {@link InspectorMonitor} properly.
 */
public class InspectorFactory {

    public static IAnalysisMonitoring createInspectorMonitor() {
        return createInspectorMonitor(newSet());
    }

    public static IAnalysisMonitoring createInspectorMonitor(Set<GutterProvider> extraGutterProviders) {
        Pair<IAnalysisMonitoring, Pair<InspectorDataProvider, DefaultGutterDataProvider>> creatorSetup = makeInspectorCreatorSetup();
        TogglableMonitor togglableMonitor = new TogglableMonitor(creatorSetup.getFirst()); // NB: other monitors (e.g. the extra monitors added by Main.init) can not be controlled by this monitor
        Set<GutterProvider> gutterProviders = newSet();
        gutterProviders.addAll(extraGutterProviders);
        gutterProviders.add(new DefaultGutters(creatorSetup.getSecond().getSecond()));
        InspectorMonitor inspector = new InspectorMonitor(togglableMonitor.getController(), creatorSetup.getSecond().getFirst(), gutterProviders);
        return CompositeMonitor.make(togglableMonitor, inspector);
    }

    private static Pair<IAnalysisMonitoring, Pair<InspectorDataProvider, DefaultGutterDataProvider>> makeInspectorCreatorSetup() {
        final AnalysisMonitor monitoring = new AnalysisMonitor();
        final VisitationMonitoring visitation = new VisitationMonitoring();

        final TypeCollector typeCollector = new TypeCollector();
        final NonLazyTypeCollectorMonitoring nonLazyTypeCollectorMonitoring = new NonLazyTypeCollectorMonitoring(typeCollector);
        final StateCollectorMonitor stateCollector = new StateCollectorMonitor();
        final NodeTransferTimeMonitor timeMonitor = new NodeTransferTimeMonitor();
        final LazyPropagationMonitor lazyPropagationMonitor = new LazyPropagationMonitor();
        final EventHandlerRegistrationMonitor eventHandlerRegistrationMonitor = new EventHandlerRegistrationMonitor();
        final PropagationMonitor propagationMonitor = new PropagationMonitor();
        final ContextRegistrationMonitor contextRegistrationMonitor = new ContextRegistrationMonitor();
        final ObjectCollectionMonitor allocationCollectingMonitor = new ObjectCollectionMonitor();

        IAnalysisMonitoring m1 = CompositeMonitor.make(timeMonitor, lazyPropagationMonitor, monitoring, visitation, propagationMonitor, contextRegistrationMonitor);
        IAnalysisMonitoring m2 = CompositeMonitor.make(monitoring, stateCollector, nonLazyTypeCollectorMonitoring, eventHandlerRegistrationMonitor, allocationCollectingMonitor);
        PhaseMonitoring<IAnalysisMonitoring,IAnalysisMonitoring> collectionMonitors = new PhaseMonitoring<>(m1, m2);
        DefaultGutterDataProvider gutterDataProvider = new DefaultGutterDataProvider(eventHandlerRegistrationMonitor, propagationMonitor, contextRegistrationMonitor, allocationCollectingMonitor, timeMonitor, monitoring::getMessages, visitation, typeCollector, lazyPropagationMonitor, stateCollector);
        InspectorDataProvider inspectorDataProvider = new InspectorDataProvider(eventHandlerRegistrationMonitor, propagationMonitor, contextRegistrationMonitor, allocationCollectingMonitor);
        return Pair.make(collectionMonitors, Pair.make(inspectorDataProvider, gutterDataProvider));
    }
}