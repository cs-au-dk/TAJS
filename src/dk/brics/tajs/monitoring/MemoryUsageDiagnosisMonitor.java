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

package dk.brics.tajs.monitoring;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.MustReachingDefs;
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.StateExtras;
import dk.brics.tajs.lattice.Summarized;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singletonList;

/**
 * Measures different parts of the analysis state in order to diagnose memory usage.
 * <p>
 * NB-1: performs measurements for the entire analysis state at the fix point (i.e. during scan phase)
 * NB-2: could easily be perform similar measurements on a per-state basis.
 */
public class MemoryUsageDiagnosisMonitor extends DefaultAnalysisMonitoring {

    private static final Logger log = Logger.getLogger(MemoryUsageDiagnosisMonitor.class);

    private final Set<State> states;

    private final Set<ObjectLabel> labels;

    private final Set<Context> contexts;

    private final Set<ExecutionContext> executionContexts;

    private final Set<ScopeChain> scopeChains;

    private final Set<Obj> objs;

    private final Set<StateExtras> extras;

    private final Set<MustReachingDefs> mustReachingDefs;

    private final Set<Summarized> summarized;

    private FlowGraph flowGraph;

    private CallGraph<State, Context, CallEdge> callGraph;

    private boolean recordingEnabled = false;

    public MemoryUsageDiagnosisMonitor() {
        contexts = makeIdentitySet();
        labels = makeIdentitySet();
        executionContexts = makeIdentitySet();
        scopeChains = makeIdentitySet();
        objs = makeIdentitySet();
        extras = makeIdentitySet();
        mustReachingDefs = makeIdentitySet();
        states = makeIdentitySet();
        summarized = makeIdentitySet();
    }

    private <T> Set<T> makeIdentitySet() {
        return Collections.newSetFromMap(new IdentityHashMap<>());
    }

    private <T> Set<T> makeIdentitySet(Collection<T> collection) {
        Set<T> set = makeIdentitySet();
        set.addAll(collection);
        return set;
    }

    @Override
    public void setSolverInterface(Solver.SolverInterface c) {
        this.callGraph = c.getAnalysisLatticeElement().getCallGraph();
        this.flowGraph = c.getFlowGraph();
    }

    @Override
    public void visitPhasePre(AnalysisPhase phase) {
        recordingEnabled = phase == AnalysisPhase.SCAN;
    }

    @Override
    public void visitBlockTransferPost(BasicBlock b, State state) {
        if (recordingEnabled) {
            record(state);
        }
    }

    private void record(State state) {
        labels.addAll(state.getStore().keySet());
        contexts.add(state.getContext());
        executionContexts.add(state.getExecutionContext());
        if (state.getScopeChain() != null) {
            scopeChains.add(state.getScopeChain());
        }
        objs.addAll(state.getStore().values());
        extras.add(state.getExtras());
        mustReachingDefs.add(state.getMustReachingDefs());
        summarized.add(state.getSummarized());
        states.add(state); // could delay all the other recording, but the collection states might be removed later
    }

    @Override
    public void visitPhasePost(AnalysisPhase phase) {
        if (phase == AnalysisPhase.SCAN) {
            show(measure());
        }
    }

    /**
     * Prints a description of the measurements.
     */
    public void show(Measurements measurements) {
        List<String> lines = newList();
        lines.add("Memory usage diagnostics at scan phase:");
        lines.addAll(measurements.format("  "));
        log.info(String.join(String.format("%n"), lines));
    }

    private Measurements measure() {
        Measurements measurements = new Measurements();
        Set<Context> heapContexts = makeIdentitySet(
                labels.stream()
                        .map(ObjectLabel::getHeapContext)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );

        measurements.measureIdentitySetDuplication("State", states);
        measurements.measureIdentitySetDuplication("ObjectLabel", labels);
        measurements.measureIdentitySetDuplication("Context", contexts);
        measurements.measureIdentitySetDuplication("Context(heap)", heapContexts);
        measurements.measureIdentitySetDuplication("ExecutionContext", executionContexts);
        measurements.measureIdentitySetDuplication("ScopeChain", scopeChains);
        measurements.measureIdentitySetDuplication("Obj", objs);
        measurements.measureIdentitySetDuplication("Summarized", summarized);
        measurements.measureIdentitySetDuplication("Extras", extras);
        measurements.measureIdentitySetDuplication("MustReachingDefs", mustReachingDefs);

        measurements.recordPlainNumber("Block", flowGraph.getNumberOfBlocks());
        measurements.recordPlainNumber("Node", flowGraph.getNumberOfNodes());
        measurements.recordPlainNumber("Function", flowGraph.getFunctions().size());
        measurements.recordPlainNumber("Callgraph: out", callGraph.getCallEdgeInfo().size());
        measurements.recordPlainNumber("Callgraph: edge", callGraph.getCallEdgeInfo().values().stream().mapToInt(Map::size).sum());
        measurements.recordPlainNumber("Callgraph: in", callGraph.getCallSources().size());

        specializeMeasurement(measurements, "ObjectLabel", labels, "Kind", ObjectLabel::getKind);
        specializeMeasurement(measurements, "ObjectLabel", labels, "singleton", ObjectLabel::isSingleton);
        specializeMeasurement(measurements, "ObjectLabel", labels, "host", ObjectLabel::isHostObject);
        specializeMeasurement(measurements, "ObjectLabel", labels, "heapCtx", l -> l.getHeapContext() != null);

        specializeMeasurement(measurements, "Context", contexts, "loopUnrolling", c -> c.getLoopUnrolling() != null && !c.getLoopUnrolling().isEmpty());
        specializeMeasurement(measurements, "Context", contexts, "specialRegs", c -> c.getSpecialRegisters() != null && !c.getSpecialRegisters().isEmpty());
        specializeMeasurement(measurements, "Context", contexts, "thisVal", c -> c.getThisVal() != null && !c.getThisVal().isNone());

        specializeMeasurement(measurements, "Obj", this.objs, "writable", Obj::isWritable);
        specializeMeasurement(measurements, "Obj", this.objs, "writableProperties", Obj::isWritableProperties);

        measurements.recordPlainNumber("Sum(|Obj.properties|)",
                objs.stream()
                        .mapToInt(obj ->
                                obj.getProperties().size()
                                        + (!obj.getDefaultNumericProperty().isUnknown() && obj.getDefaultNumericProperty().isMaybePresent() ? 1 : 0)
                                        + (!obj.getDefaultOtherProperty().isUnknown() && obj.getDefaultOtherProperty().isMaybePresent() ? 1 : 0))
                        .sum());
        measurements.recordPlainNumber("Sum(|Obj.properties|) (unique Obj)",
                newSet(objs).stream()
                        .mapToInt(obj ->
                                obj.getProperties().size()
                                        + (!obj.getDefaultNumericProperty().isUnknown() && obj.getDefaultNumericProperty().isMaybePresent() ? 1 : 0)
                                        + (!obj.getDefaultOtherProperty().isUnknown() && obj.getDefaultOtherProperty().isMaybePresent() ? 1 : 0))
                        .sum());

        measurements.measureIdentitySetDuplication("SourceLocation",
                makeIdentitySet(flowGraph.getFunctions().stream()
                        .flatMap(f -> f.getBlocks().stream())
                        .flatMap(b -> b.getNodes().stream())
                        .map(AbstractNode::getSourceLocation)
                        .collect(Collectors.toList())));

        return measurements;
    }

    /**
     * Utility method for verbosely displaying canonicalization potentials.
     */
    @SuppressWarnings("unused")
    private void printCanonicalizationPotentials(Collection<?> elements) {
        elements.stream()
                .collect(Collectors.groupingBy(o -> o, java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(e -> Pair.make(e.getKey().toString(), e.getValue()))
                .sorted(Comparator.comparing(Pair::getSecond))
                .forEach(System.out::println);
    }

    /**
     * Specializes the measurements for a collection.
     */
    private <T> void specializeMeasurement(Measurements measurements, String prefixDescription, Set<T> collection, String specialization, Function<T, ?> specializer) {
        collection.stream()
                .collect(Collectors.groupingBy(specializer))
                .forEach((specializedBy, specialized) -> {
                    String description = String.format("%s:%s:%s", prefixDescription, specialization, specializedBy);
                    measurements.measureIdentitySetDuplication(description, makeIdentitySet(specialized));
                });
    }

    public static class Measurements {

        private Map<String, Pair<Number, List<String>>> measurements = newMap();

        private void recordPlainNumber(String description, int number) {
            record(description, number, newList());
        }

        private void record(String description, Number number, List<String> additionalInformation) {
            measurements.put(description, Pair.make(number, additionalInformation));
        }

        private void measureDuplication(String description, int totalCount, int uniqueCount) {
            String additionalInformation = String.format("%d%% unique", ((uniqueCount * 100) / totalCount));
            record(description, totalCount, singletonList(additionalInformation));
        }

        private void measureIdentitySetDuplication(String description, Set<?> elements) {
            measureDuplication(description, elements.size(), newSet(elements).size());
        }

        /**
         * For pretty printing.
         */
        public List<String> format(String indentation) {
            int maxDescriptionLength = measurements.keySet().stream().mapToInt(String::length).max().getAsInt();
            int maxNumberLength = measurements.values().stream().mapToInt(number -> (number.getFirst() + "").length()).max().getAsInt();
            return measurements.entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(e -> {
                        String keySpace = String.join("", Collections.nCopies(maxDescriptionLength - e.getKey().length(), " "));
                        String spaceValue = String.join("", Collections.nCopies(maxNumberLength - (e.getValue().getFirst() + "").length(), " "));
                        return String.format("%s%s%s : %s%s%s",
                                indentation,
                                e.getKey(),
                                keySpace,
                                spaceValue,
                                e.getValue().getFirst(),
                                e.getValue().getSecond().isEmpty() ?
                                        "" : String.format("(%s)",
                                        String.join(",", e.getValue().getSecond())
                                ));
                    })
                    .collect(Collectors.toList());
        }

        public Map<String, Pair<Number, List<String>>> getMeasurements() {
            return measurements;
        }
    }
}
