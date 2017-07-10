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

package dk.brics.tajs.monitoring.inspector.datacollection.monitors;

import dk.brics.inspector.InspectorSetup;
import dk.brics.inspector.api.InspectorAPI;
import dk.brics.inspector.server.InspectorServer;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.monitoring.AnalysisPhase;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.TogglableMonitor.Toggler;
import dk.brics.tajs.monitoring.inspector.api.SynchronizedMonitoringStoppingAPI;
import dk.brics.tajs.monitoring.inspector.api.TAJSInspectorAPI;
import dk.brics.tajs.monitoring.inspector.datacollection.InspectorDataProvider;
import dk.brics.tajs.monitoring.inspector.dataprocessing.IDManager;
import dk.brics.tajs.monitoring.inspector.gutters.GutterProvider;

import java.util.Set;

/**
 * Main monitor of the inspector implementation: starts an {@link InspectorServer} at the end of the scan phase.
 */
public class InspectorMonitor extends DefaultAnalysisMonitoring {

    private final Toggler monitoringToggler;

    private final InspectorDataProvider inspectorDataProvider;

    private final Set<GutterProvider> gutters;

    private Solver.SolverInterface c;

    /**
     * @see dk.brics.tajs.monitoring.inspector.datacollection.InspectorFactory for how to instantiate
     */
    public InspectorMonitor(Toggler monitoringToggler, InspectorDataProvider inspectorDataProvider, Set<GutterProvider> gutters) {
        this.monitoringToggler = monitoringToggler;
        this.inspectorDataProvider = inspectorDataProvider;
        this.gutters = gutters;
    }

    @Override
    public void visitPhasePost(AnalysisPhase phase) {
        if (phase == AnalysisPhase.SCAN) {
            IDManager idManager = new IDManager();
            InspectorAPI tajsAPI = new TAJSInspectorAPI(inspectorDataProvider.get(), gutters, idManager, c);
            InspectorAPI threadSafeAPI = new SynchronizedMonitoringStoppingAPI(tajsAPI, monitoringToggler);
            InspectorSetup.simpleStart(threadSafeAPI);
        }
    }

    @Override
    public void setSolverInterface(Solver.SolverInterface c) {
        this.c = c;
    }
}
