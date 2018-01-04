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

package dk.brics.tajs.monitoring.soundness.postprocessing;

import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.TypeCollector;
import dk.brics.tajs.monitoring.soundness.testing.SoundnessCheck;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * Numbers for reporting on the performance of the soundness tester.
 */
public class SoundnessTesterPerformance {

    private static final Logger log = Logger.getLogger(SoundnessTesterPerformance.class);

    private final Path mainFile;

    private final Map<TypeCollector.VariableSummary, Value> type_map;

    private final FlowGraph flowgraph;

    private long startPreparationTime;

    private long startTestTime;

    private long setupDuration;

    private long testDuration;

    public SoundnessTesterPerformance(Path mainFile, Map<TypeCollector.VariableSummary, Value> type_map, FlowGraph flowgraph) {
        this.mainFile = mainFile;
        this.type_map = type_map;
        this.flowgraph = flowgraph;
    }

    public void reportPerformance(Set<Pair<dk.au.cs.casa.jer.entries.SourceLocation, dk.brics.tajs.flowgraph.SourceLocation>> sourceLocationEqualities, CategorizedSoundnessCheckResults categorized, SoundnessCheckCounts rawCounts) {
        Map<Class<? extends SoundnessCheck>, Long> checkCounts = categorized.checks.stream().collect(Collectors.groupingBy(SoundnessCheck::getClass, java.util.stream.Collectors.counting()));
        long duration = setupDuration + testDuration;
        log.info(String.format("Soundness testing performed on %s:"
                        + "%n\tIt involved %d checks, %d variables, ~%d objects, %d sourcelocation aliases, for %d TAJS-locations and %d Jalangi-locations."
                        + "%n\tIt took %d ms. (%d ms on preparation (%.2f ms/check, %.2f ms/node), %d ms on checking (%.2f ms/check, %.2f ms/node))"
                        + "%n\tCheckCounts: %s",
                mainFile.toString(),
                rawCounts.checkCount, type_map.size(), type_map.values().stream().flatMap(v -> v.getAllObjectLabels().stream()).distinct().count(), sourceLocationEqualities.size(), sourceLocationEqualities.stream().map(Pair::getFirst).distinct().count(), sourceLocationEqualities.stream().map(Pair::getSecond).distinct().count(),
                duration,
                setupDuration, setupDuration / ((double) rawCounts.checkCount), setupDuration / ((double) flowgraph.getNumberOfNodes()),
                testDuration, testDuration / ((double) rawCounts.checkCount), testDuration / ((double) flowgraph.getNumberOfNodes()),
                checkCounts.entrySet().stream().sorted(Comparator.comparing(e -> -e.getValue())).map(e -> String.format("%n\t\t%s: %d", e.getKey().getSimpleName(), e.getValue())).collect(java.util.stream.Collectors.joining())
        ));
    }

    public void beginSetup() {
        startPreparationTime = System.currentTimeMillis();
    }

    public void endSetupStartTest() {
        setupDuration = System.currentTimeMillis() - startPreparationTime;
        startTestTime = System.currentTimeMillis();
    }

    public void endTest() {
        testDuration = System.currentTimeMillis() - startTestTime;
    }
}
