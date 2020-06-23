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

package dk.brics.tajs.monitoring.soundness.testing;

import dk.au.cs.casa.jer.LogParser;
import dk.au.cs.casa.jer.entries.IEntry;
import dk.au.cs.casa.jer.entries.ModuleExportsEntry;
import dk.brics.tajs.analysis.KnownUnsoundnesses;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.ValueLogLocationInformation;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.TypeCollector.VariableSummary;
import dk.brics.tajs.monitoring.soundness.LogFileHelper;
import dk.brics.tajs.monitoring.soundness.ValueLogSourceLocationEqualityDecider;
import dk.brics.tajs.monitoring.soundness.postprocessing.CategorizedSoundnessCheckResults;
import dk.brics.tajs.monitoring.soundness.postprocessing.SoundnessTestResult;
import dk.brics.tajs.monitoring.soundness.postprocessing.SoundnessTesterPerformance;
import dk.brics.tajs.monitoring.soundness.postprocessing.SoundnessTesterStatistics;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;
import org.apache.log4j.Logger;

import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Tests the soundness of a static analysis result by comparing it with results from a dynamic analysis.
 */
public class SoundnessTester {

    private static final Logger log = Logger.getLogger(SoundnessTester.class);

    private final Path mainFile;

    private final Map<VariableSummary, Value> type_map;

    private final ValueLogLocationInformation valueLogLocationInformation;

    private final Solver.SolverInterface c;

    private final Set<SourceLocation> domObjectAllocationSites;

    /**
     * Constructor.
     */
    public SoundnessTester(Map<VariableSummary, Value> type_map, Set<SourceLocation> domObjectAllocationSites, Solver.SolverInterface c) {
        this.domObjectAllocationSites = domObjectAllocationSites;
        this.mainFile = Options.get().getArguments().get(Options.get().getArguments().size() - 1);
        this.type_map = type_map;
        this.valueLogLocationInformation = c.getFlowGraph().getValueLogLocationInformation();
        this.c = c;
    }

    /**
     * Tests the soundness of the static analysis result by comparing it with the content of the given value log.
     */
    public SoundnessTestResult test(URL logFile) {
        if (!Options.get().isQuietEnabled())
            log.info("Testing soundness...");

        // setup
        SoundnessTesterPerformance soundnessTesterPerformance = new SoundnessTesterPerformance(mainFile, type_map, c.getFlowGraph());
        soundnessTesterPerformance.beginSetup();
        FlowGraph flowGraph = c.getAnalysis().getSolver().getFlowGraph();
        Map<Pair<SourceLocation, String>, Set<Value>> resolvedTypeMap = resolveTypeMap(type_map);
        Map<Class<? extends AbstractNode>, Map<SourceLocation, Set<AbstractNode>>> loc2nodes = buildLoc2Nodes(flowGraph);
        LogParser logParser = LogFileHelper.makeLogParser(logFile);
        String runResult = logParser.getMetadata().getResult();
        Set<IEntry> entries = getEntries(logParser);
        Set<SoundnessCheck> checks = newSet();
        ProgramExitReachabilitySoundnessTester programExitReachabilitySoundnessTester = new ProgramExitReachabilitySoundnessTester(checks, c);
        ValueLogSourceLocationEqualityDecider equalityDecider = new ValueLogSourceLocationEqualityDecider(valueLogLocationInformation.getTajsLocation2jalangiLocation(), flowGraph);
        LogEntrySoundnessTester logEntrySoundnessTester = new LogEntrySoundnessTester(resolvedTypeMap, loc2nodes, checks, equalityDecider, valueLogLocationInformation, domObjectAllocationSites, c);
        soundnessTesterPerformance.endSetupStartTest();

        // test
        boolean reachabilityFailure = programExitReachabilitySoundnessTester.test(runResult);
        logEntrySoundnessTester.test(entries);
        soundnessTesterPerformance.endTest();
        c.getMonitoring().visitSoundnessTestingDone(checks.size());

        // report
        CategorizedSoundnessCheckResults categorized = new CategorizedSoundnessCheckResults(checks, mainFile);
        boolean measure_performance = false;
        if (measure_performance) {
            soundnessTesterPerformance.reportPerformance(equalityDecider.getEqualities(), categorized, categorized.getRawCounts());
        }
        if (SoundnessTesterStatistics.EasyPersistence.isEnabled()) {
            SoundnessTesterStatistics.TestResult result = new SoundnessTesterStatistics.TestResult(categorized.getRawCounts(), categorized.getLocationCounts(), KnownUnsoundnesses.isUninspectedUnsoundFile(mainFile));
            SoundnessTesterStatistics.EasyPersistence.update(mainFile, result);
        }
        return SoundnessTestResult.make(categorized, reachabilityFailure, mainFile);
    }

    private Set<IEntry> getEntries(LogParser logParser) {
        return logParser.getEntries().stream()
                .filter(e -> !(e instanceof ModuleExportsEntry))
                .filter(e -> !(e.getSourceLocation().getColumnNumber() == -1))
                .filter(e -> !(e.getSourceLocation().getFileName().matches(".*js-url-\\d+.js"))) // https://github.com/cs-au-dk/jalangilogger/issues/6
                .filter(e -> !(e.getSourceLocation().getFileName().contains("node_modules")))
                .collect(Collectors.toSet());
    }

    /**
     * Converts observations of variables and fields during the analysis to a simpler form. The conversion also maps the information according to the aliases discoved by {@link ValueLogLocationInformation}.
     */
    private Map<Pair<SourceLocation, String>, Set<Value>> resolveTypeMap(Map<VariableSummary, Value> type_map) {
        Map<Pair<SourceLocation, String>, Set<Value>> map = newMap();
        type_map.forEach((k, v) -> {
            Set<SourceLocation> aliases = valueLogLocationInformation.getJalangiLocations(k.getVariableLocation());
            aliases.forEach(alias -> addToMapSet(map, Pair.make(alias, k.getVariableName()), v));
            addToMapSet(map, Pair.make(k.getVariableLocation(), k.getVariableName()), v);
        });
        return map;
    }

    /**
     * Constructs a map for efficiently finding nodes at a specific location.
     */
    private Map<Class<? extends AbstractNode>, Map<SourceLocation, Set<AbstractNode>>> buildLoc2Nodes(FlowGraph flowGraph) {
        Set<AbstractNode> nodes = flowGraph.getFunctions().stream()
                .flatMap(f -> f.getBlocks().stream()
                        .flatMap(b -> b.getNodes().stream()))
                .collect(Collectors.toSet());
        Map<Class<? extends AbstractNode>, Map<SourceLocation, Set<AbstractNode>>> loc2Node = newMap();
        nodes.forEach(n -> {
            if (!loc2Node.containsKey(n.getClass())) {
                loc2Node.put(n.getClass(), newMap());
            }
            Map<SourceLocation, Set<AbstractNode>> currentGroup = loc2Node.get(n.getClass());
            Set<SourceLocation> aliases = valueLogLocationInformation.getJalangiLocations(n.getSourceLocation());
            aliases.forEach(alias -> addToMapSet(currentGroup, alias, n));
        });
        return loc2Node;
    }
}
