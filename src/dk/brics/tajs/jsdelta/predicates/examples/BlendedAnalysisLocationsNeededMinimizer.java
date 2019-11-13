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

package dk.brics.tajs.jsdelta.predicates.examples;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.blendedanalysis.BlendedAnalysisOptionValues;
import dk.brics.tajs.blendedanalysis.BlendedAnalysisOptions;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.jsdelta.predicates.AbstractLocationMinimizer;
import dk.brics.tajs.monitoring.AnalysisMonitor;
import dk.brics.tajs.options.Options;

import java.nio.file.Paths;
import java.util.Set;

/**
 * Finds the minimal set of locations that need blended analysis, in order to be analyzable in the chosen time limit.
 */
public class BlendedAnalysisLocationsNeededMinimizer extends AbstractLocationMinimizer {

    private static String fileToAnalyze = "benchmarks/tajs/src/underscore/libraries/underscore-1.8.3.js";

    /**
     * The analysis time limit. Should be at least the time it takes to analyze the file with blended analysis allowed in all locations.
     */
    private static int allowedAnalysisTime = 30;

    public static void main(String[] args) {
        Main.initLogging();
        AbstractLocationMinimizer.reduce(Paths.get(fileToAnalyze));
    }

    public void run(Set<SourceLocation> locations) {
        Main.initLogging();
        Main.reset();
        Options.get().enableIncludeDom();
        Options.get().enableTest();
        Options.get().enableNoMessages();
        Options.get().enableUnevalizer();
        Options.get().enablePolyfillMDN();
        Options.get().enableConsoleModel();
        Options.get().enablePolyfillTypedArrays();
        Options.get().enablePolyfillES6Collections();
        Options.get().enableDeterminacy();
        Options.get().enableBlendedAnalysis();
        Options.get().setAnalysisTimeLimit(allowedAnalysisTime);

        Options.get().getSoundnessTesterOptions().setTest(false);
        Options.get().getSoundnessTesterOptions().setGenerateOnlyIncludeAutomatically(true);

        BlendedAnalysisOptionValues refOptions = new BlendedAnalysisOptionValues();
        refOptions.setAllowedBlendedAnalysisSourceLocations(locations);
        BlendedAnalysisOptions.set(refOptions);

        Options.get().getArguments().add(Paths.get(fileToAnalyze));
        Analysis a = Main.init(Options.get(), new AnalysisMonitor(), null);
        Main.run(a);
    }

    @Override
    protected boolean test(Set<SourceLocation> sourceLocations) {
        try {
            run(sourceLocations);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}