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

package dk.brics.tajs.monitoring.inspector.gutters;

import dk.brics.inspector.api.model.lines.Gutter;
import dk.brics.inspector.api.model.lines.GutterKind;
import dk.brics.inspector.api.model.lines.LineMap;
import dk.brics.inspector.api.model.lines.LineMessage;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.monitoring.TypeCollector;
import dk.brics.tajs.monitoring.inspector.datacollection.InspectorFactory;
import dk.brics.tajs.monitoring.inspector.datacollection.SourceLine;
import dk.brics.tajs.monitoring.inspector.dataprocessing.DomainMapper;
import dk.brics.tajs.monitoring.inspector.dataprocessing.IDManager;
import dk.brics.tajs.monitoring.inspector.util.OccurenceCountingMap;
import dk.brics.tajs.monitoring.soundness.LogFileHelper;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.PathAndURLUtils;
import org.apache.log4j.Logger;

import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.PathAndURLUtils.normalizeFileURL;

/**
 * The default gutters provided by the {@link dk.brics.tajs.monitoring.inspector.datacollection.monitors.InspectorMonitor}.
 * Additional gutters can be provided by implenting {@link GutterProvider} and providing them to {@link InspectorFactory#createInspectorMonitor(java.util.Set)}
 */
public class DefaultGutters implements GutterProvider {

    private static final Logger log = Logger.getLogger(DefaultGutters.class);

    private final DefaultGutterDataProvider dataCreator;

    public DefaultGutters(DefaultGutterDataProvider dataCreator) {
        this.dataCreator = dataCreator;
    }

    private LineMap<Long> convertOccurrenceCountningMap(URL url, OccurenceCountingMap<SourceLine> toConvert) {
        Map<Integer, Long> map = toConvert.getResults().stream()
                .filter(r -> url.equals(r.getElement().getLocation()))
                .collect(Collectors.groupingBy(r -> r.getElement().getLine(), java.util.stream.Collectors.summingLong(OccurenceCountingMap.CountingResult::getOccurences)));
        return new LineMap<>(map);
    }

    private LineMap<Double> convertNodeMapToSummedLineMap(URL url, Map<AbstractNode, ? extends Number> map) {
        return convertNodeMapToSummedLineMap(url, map, Number::doubleValue);
    }

    private LineMap<Double> convertNodeMapToMaxedLineMap(URL url, Map<AbstractNode, ? extends Number> map) {
        return convertNodeMapToMaxedLineMap(url, map, Number::doubleValue);
    }

    private <T> LineMap<Double> convertNodeMapToSummedLineMap(URL url, Map<AbstractNode, T> map, Function<T, Double> mapper) {
        Map<Integer, Double> data = map.entrySet().stream()
                .filter(e -> e.getKey().getSourceLocation().getLocation() != null)
                .filter(e -> url.equals(e.getKey().getSourceLocation().getLocation()))
                .collect(Collectors.groupingBy(
                        e -> e.getKey().getSourceLocation().getLineNumber(),
                        java.util.stream.Collectors.mapping(e -> mapper.apply(e.getValue()), java.util.stream.Collectors.summingDouble(e -> e))));
        return new LineMap<>(data);
    }

    private <T> LineMap<Double> convertNodeMapToMaxedLineMap(URL url, Map<AbstractNode, T> map, Function<T, Double> mapper) {
        Map<Integer, Double> data = map.entrySet().stream()
                .filter(e -> e.getKey().getSourceLocation().getLocation() != null)
                .filter(e -> url.equals(e.getKey().getSourceLocation().getLocation()))
                .collect(Collectors.groupingBy(
                        e -> e.getKey().getSourceLocation().getLineNumber(),
                        java.util.stream.Collectors.mapping(e -> mapper.apply(e.getValue()), java.util.stream.Collectors.maxBy(Comparator.comparing(e -> e)))))
                .entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
        return new LineMap<>(data);
    }

    private <T> LineMap<Double> convertBlockAndContextMapToSummedLineMap(URL url, Map<BlockAndContext<Context>, T> map, Function<T, Double> mapper) {
        Map<Integer, Double> data = map.entrySet().stream()
                .filter(e -> e.getKey().getBlock().getSourceLocation().getLocation() != null)
                .filter(e -> url.equals(e.getKey().getBlock().getSourceLocation().getLocation()))
                .flatMap(e -> e.getKey().getBlock().getNodes().stream() // count all lines for a block equally
                        .map(n -> n.getSourceLocation().getLineNumber())
                        .distinct()
                        .map(l -> Pair.make(l, e.getValue())))
                .collect(Collectors.groupingBy(
                        Pair::getFirst,
                        java.util.stream.Collectors.mapping(e -> mapper.apply(e.getSecond()), java.util.stream.Collectors.summingDouble(e -> e))));
        return new LineMap<>(data);
    }

    private <N1 extends Number, N2 extends Number> LineMap<Double> normalize(LineMap<N1> toNormalize, LineMap<N2> toNormalizeBy) {
        Map<Integer, Double> data = toNormalize.data.entrySet().stream()
                .filter(e -> {
                    // XXX avoid this: the division map should contain the same keys!
                    boolean use = toNormalizeBy.data.containsKey(e.getKey()) && toNormalizeBy.data.get(e.getKey()).doubleValue() != 0;
                    if (!use) {
                        // System.err.println("FIXME Skipping normalization of line: " + e.getKey());
                    }
                    return use;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (e.getValue().doubleValue()) / toNormalizeBy.data.get(e.getKey()).doubleValue()));
        return new LineMap<>(data);
    }

    private LineMap<Integer> makeSuspiciousnessLevelMap(URL url, TypeCollector typeCollector) {
        Map<Integer, Optional<Integer>> line2maxSuspiciousness = typeCollector.getTypeInformation().entrySet().stream()
                .filter(e -> url.equals(e.getKey().getVariableLocation().getLocation()))
                .map(e -> Pair.make(e.getKey().getVariableLocation().getLineNumber(), new SuspiciousnessLevel(e.getValue()).getNumericLevel()))
                .collect(Collectors.groupingBy(Pair::getFirst, java.util.stream.Collectors.mapping(Pair::getSecond, java.util.stream.Collectors.maxBy(Comparator.comparing(Integer::doubleValue)))));
        Map<Integer, Integer> data = line2maxSuspiciousness
                .entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
        return new LineMap<>(data);
    }

    private LineMap<Set<LineMessage>> makeMessages(URL url, DefaultGutterData data) {
        DomainMapper domainMapper = new DomainMapper(new IDManager() /* these ids are not used elsewhere */);
        Map<Integer, Set<LineMessage>> map = data.messages.stream()
                .filter(m -> m.getNode().getSourceLocation().getLocation() != null)
                .filter(m -> url.equals(m.getNode().getSourceLocation().getLocation()))
                .filter(m -> m.getStatus() != Status.NONE)
                .filter(m -> m.getSeverity() != Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE || m.getStatus() == Status.CERTAIN)
                .collect(Collectors.groupingBy(
                        m -> m.getNode().getSourceLocation().getLineNumber(),
                        java.util.stream.Collectors.mapping((message) -> {
                            Severity severity = message.getSeverity();
                            Severity uncounditionalSeverity = severity == Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE ? Severity.MEDIUM : severity; // non-certain cases has been eliminated already
                            SourceLocation sourceLocation = message.getNode().getSourceLocation();
                            return new LineMessage(domainMapper.makeFromSourceLocation(sourceLocation), message.getMessage(), domainMapper.makeMessageLevel(uncounditionalSeverity), domainMapper.makeMessageSource(uncounditionalSeverity), domainMapper.makeMessageCertainty(message.getStatus()));
                        }, Collectors.toSet())));

        return new LineMap<>(map);
    }

    @Override
    public Set<Gutter<?>> create(URL url) {
        // TODO refactor elsewhere to avoid having so many different conversions
        DefaultGutterData data = dataCreator.create();

        // TODO unify the normalization implementations
        Set<Gutter<?>> gutters = newSet();
        LineMap<Long> blockPerLineMap = convertOccurrenceCountningMap(url, data.flowgraphInfo.getBlocksPerLine());
        LineMap<Long> nodesPerLineMap = convertOccurrenceCountningMap(url, data.flowgraphInfo.getNodesPerLine());
        gutters.add(new Gutter<>(GutterKind.NUMBER, "State sizes", "states", convertNodeMapToMaxedLineMap(url, data.maxStateSize)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Blocks", "blocks", blockPerLineMap));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Nodes", "nodes", nodesPerLineMap));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Context counts", "contexts", convertLineMap(url, data.contextsPerLine)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Allocation counts", "allocations", convertNodeMapToSummedLineMap(url, data.allocationSiteMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size())))));
        LineMap<Long> blockVisitsPerLine = convertOccurrenceCountningMap(url, data.flowgraphInfo.getBlockVisitCountsPerLine());
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Block visit counts", "times visited", blockVisitsPerLine));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Normalized block visit counts", "times visited (normalized)", normalize(blockVisitsPerLine, blockPerLineMap)));
        LineMap<Double> timeForLines = convertNodeMapToSummedLineMap(url, data.timesForNodes.entrySet().stream()
                .filter(e1 -> e1.getKey().isPresent())
                .map(e1 -> Pair.make(e1.getKey().get(), e1.getValue()))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Durations", "node transfer time (ns) spent", timeForLines));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Normalized durations", "normalized node transfer time (ns) spent ", normalize(timeForLines, nodesPerLineMap)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Max suspiciousness", "max suspiciousness level", makeSuspiciousnessLevelMap(url, data.typeCollector)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Full unknown value resolve", "full unknown value recoveries", convertNodeMapToSummedLineMap(url, data.lazyPropagationData.fullRecovers)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Partial unknown value resolve", "partial unknown value recoveries", convertNodeMapToSummedLineMap(url, data.lazyPropagationData.partialRecovers)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Recovery graph size (max)", "largest recovery graph size", convertNodeMapToMaxedLineMap(url, data.lazyPropagationData.maxRecoveryGraphSize)));

        LineMap<Double> progatorCounts = convertBlockAndContextMapToSummedLineMap(url, data.propagationData.getNumbers(), numbers -> (double) numbers.getPropagator());
        LineMap<Double> propagateeCounts = convertBlockAndContextMapToSummedLineMap(url, data.propagationData.getNumbers(), numbers -> (double) numbers.getPropagatee());
        LineMap<Double> changerCounts = convertBlockAndContextMapToSummedLineMap(url, data.propagationData.getNumbers(), numbers -> (double) numbers.getChanger());
        LineMap<Double> changeeCounts = convertBlockAndContextMapToSummedLineMap(url, data.propagationData.getNumbers(), numbers -> (double) numbers.getChangee());
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Propagator counts", "times acting as propagator", progatorCounts));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Propagatee counts", "times acting as propagatee", propagateeCounts));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Changer counts", "times acting as changer", changerCounts));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Changee counts", "times acting as changee", changeeCounts));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Normalized propagator counts", "times acting as propagator (block-normalized)", normalize(progatorCounts, blockPerLineMap)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Normalized propagatee counts", "times acting as propagatee (block-normalized)", normalize(propagateeCounts, blockPerLineMap)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Normalized changer counts", "times acting as changer (block-normalized)", normalize(changerCounts, blockPerLineMap)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Normalized changee counts", "times acting as changee (block-normalized)", normalize(changeeCounts, blockPerLineMap)));

        LineMap<Double> propagatorTimes = convertBlockAndContextMapToSummedLineMap(url, data.propagationData.getNumbers(), numbers -> (double) numbers.getPropagatorWatch().getElapsedMicro());
        LineMap<Double> propagateeTimes = convertBlockAndContextMapToSummedLineMap(url, data.propagationData.getNumbers(), numbers -> (double) numbers.getPropagateeWatch().getElapsedMicro());
        LineMap<Double> changerTimes = convertBlockAndContextMapToSummedLineMap(url, data.propagationData.getNumbers(), numbers -> (double) numbers.getChangerWatch().getElapsedMicro());
        LineMap<Double> changeeTimes = convertBlockAndContextMapToSummedLineMap(url, data.propagationData.getNumbers(), numbers -> (double) numbers.getPropagateeWatch().getElapsedMicro());
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Propagator time", "time (ms) acting as propagator", propagatorTimes));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Propagatee time", "time (ms) acting as propagatee", propagateeTimes));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Changer time", "time (ms) acting as changer", changerTimes));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Changee time", "time (ms) acting as changee", changeeTimes));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Block normalized propagator time", "time (ms) acting as propagator (block-normalized)", normalize(propagatorTimes, blockPerLineMap)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Block normalized propagatee time", "time (ms) acting as propagatee (block-normalized)", normalize(propagateeTimes, blockPerLineMap)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Block normalized changer time", "time (ms) acting as changer (block-normalized)", normalize(changerTimes, blockPerLineMap)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Block normalized changee time", "time (ms) acting as changee (block-normalized)", normalize(changeeTimes, blockPerLineMap)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Count normalized propagator time", "time (ms) acting as propagator (count-normalized)", normalize(propagatorTimes, progatorCounts)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Count normalized propagatee time", "time (ms) acting as propagatee (count-normalized)", normalize(propagateeTimes, propagateeCounts)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Count normalized changer time", "time (ms) acting as changer (count-normalized)", normalize(changerTimes, changerCounts)));
        gutters.add(new Gutter<>(GutterKind.NUMBER, "Count normalized changee time", "time (ms) acting as changee (count-normalized)", normalize(changeeTimes, changeeCounts)));

        gutters.add(new Gutter<>(GutterKind.NUMBER, "Max durations", "max node transfer time (ns) spent per node on the line",
                convertMap(data.timesForNodes,
                        e -> e.isPresent() && url.equals(e.get().getSourceLocation().getLocation()),
                        k -> k.get().getSourceLocation().getLineNumber(),
                        es -> es.stream()
                                .map(Pair::getSecond).max(Long::compare).get()
                ))
        );

        gutters.addAll(addValueLogGutters(url, data));

        gutters.add(new Gutter<>(GutterKind.STRING, "Messages", "messages", makeMessages(url, data)));
        return gutters;
    }

    private <V> LineMap<V> convertLineMap(URL url, Map<SourceLine, V> map) {
        Map<Integer, V> data = map.entrySet().stream()
                .filter(e -> url.equals(e.getKey().getLocation()))
                .collect(Collectors.toMap(e -> e.getKey().getLine(), Map.Entry::getValue));
        return new LineMap<>(data);
    }

    private <K, V, R> LineMap<R> convertMap(Map<K, V> map, Predicate<K> filter, Function<K, Integer> lineMapper, Function<Collection<Pair<K, V>>, R> valueMapper) {
        Map<Integer, R> data = map.entrySet().stream()
                .filter(e -> filter.test(e.getKey()))
                .collect(Collectors.groupingBy(e -> lineMapper.apply(e.getKey()), java.util.stream.Collectors.mapping(e -> Pair.make(e.getKey(), e.getValue()), Collectors.toList())))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> valueMapper.apply(e.getValue())));
        return new LineMap<>(data);
    }

    private Set<Gutter<?>> addValueLogGutters(URL url, DefaultGutterData data) {
        Set<Gutter<?>> gutters = newSet();
        URL logFile;
        try {
            logFile = new LogFileHelper().getLogFile();
        } catch (Exception e) {
            log.warn("Could not create value logger gutters due to lack of value logger log file (using the -log-file option will fix this)" );
            return newSet();
        }
        if (logFile != null) {
            Path main = Options.get().getArguments().get(Options.get().getArguments().size() - 1);
            Path mainDir = main.getParent();
            Set<Integer> concreteLiveLines = LogFileHelper.makeLogParser(logFile).getEntries().stream()
                    .map(e -> Pair.make(normalizeFileURL(PathAndURLUtils.toURL(mainDir.resolve(e.getSourceLocation().getFileName()))), e.getSourceLocation().getLineNumber()))
                    .filter(l -> url.equals(l.getFirst()))
                    .map(Pair::getSecond)
                    .collect(Collectors.toSet());

            Set<Integer> abstractLiveLines = data.flowgraphInfo.getAbstractLiveLines().get(url);
            Set<Integer> spuriousLiveLines = newSet(abstractLiveLines);
            Set<Integer> spuriousDeadLines = newSet(concreteLiveLines);
            spuriousLiveLines.removeAll(concreteLiveLines);
            spuriousDeadLines.removeAll(abstractLiveLines);

            gutters.add(new Gutter<>(GutterKind.BOOLEAN, "Observed concrete", "source code line is observed in concrete execution", convertToBooleanLikeLineMap(concreteLiveLines)));
            gutters.add(new Gutter<>(GutterKind.BOOLEAN, "Non-observed concrete, reachable abstract", "source code line is not observed in concrete execution, but reachable according to analysis", convertToBooleanLikeLineMap(spuriousDeadLines)));
            gutters.add(new Gutter<>(GutterKind.BOOLEAN, "Observed concrete, unreachable abstract", "source code line is observed in concrete execution, but unreachable according to analysis (unsound!)", convertToBooleanLikeLineMap(spuriousLiveLines)));
        }
        return gutters;
    }

    private LineMap<Integer> convertToBooleanLikeLineMap(Set<Integer> liveLines) {
        return new LineMap<>(liveLines.stream().collect(Collectors.toMap(l -> l, l -> 1)));
    }
}
