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

package dk.brics.tajs.monitoring.inspector;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.monitoring.AnalysisTimeLimiter;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.inspector.datacollection.InspectorFactory;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

/**
 * Quick-and-dirty tester method for {@link dk.brics.tajs.monitoring.inspector.datacollection.monitors.InspectorMonitor}.
 */
public class QuickShow {

    private static void TestInit() {
        Main.initLogging();
        Main.reset();

        // test in complex setting
        Options.get().enablePolyfillMDN();
        Options.get().enableUnevalizer();
        Options.get().enableDeterminacy();

//        Options.get().getSoundnessTesterOptions().setTest(false);
//        Options.get().getSoundnessTesterOptions().setGenerate(false);
        Options.get().enableTest();
    }

    public static void main(String[] args) throws Exception {
        TestInit();
        IAnalysisMonitoring inspector = InspectorFactory.createInspectorMonitor();
        String target = "test/google/richards.js";
        Options.get().getArguments().add(target);
        Analysis a = Main.init(Options.get(), new CompositeMonitoring(inspector, new AnalysisTimeLimiter(30)), null);
        if (a == null)
            throw new AnalysisException("Error during initialization");
        Main.run(a);
    }
}
