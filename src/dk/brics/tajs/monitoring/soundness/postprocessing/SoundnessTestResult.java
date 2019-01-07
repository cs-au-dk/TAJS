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

package dk.brics.tajs.monitoring.soundness.postprocessing;

import dk.brics.tajs.analysis.KnownUnsoundnesses;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.monitoring.soundness.testing.SoundnessCheck;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.PathAndURLUtils;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * The final result of a soundness test: a success-bit and an explanatory string.
 */
public class SoundnessTestResult {

    private static final Logger log = Logger.getLogger(SoundnessTestResult.class);

    public final boolean success;

    public final String message;

    private SoundnessTestResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    private static Comparator<SoundnessCheck> getFailureComparison() {
        return Comparator.comparing((SoundnessCheck o) -> !o.hasDataFlow())
                .thenComparing(o -> o.getFailureKind() == SoundnessCheck.FailureKind.MISSING_NATIVE_PROPERTY)
                .thenComparing(SoundnessCheck::getSourceLocation, new SourceLocation.Comparator())
                .thenComparing(SoundnessCheck::getMessage);
    }

    /**
     * Constructs a (single) {@link SoundnessTestResult} based on the results of (many) soundness checks.
     */
    public static SoundnessTestResult make(CategorizedSoundnessCheckResults categorized, boolean reachabilityFailure, Path mainFile) {
        boolean isManuallyIgnoredMainFile = KnownUnsoundnesses.isUninspectedUnsoundFile(mainFile);
        SoundnessCheckCounts counts = categorized.getRawCounts();
        if (isManuallyIgnoredMainFile) {
            // the following conversion seems like overkill, but it is the TAJS-idiomatic way to print paths in a platform independent way
            String sourceLocationString = PathAndURLUtils.toPortableString(mainFile);
            log.warn(String.format("Disabling soundness testing for %s entirely (%d uninspected soundness errors).", sourceLocationString, counts.expectedFailureCount));
            if (counts.failureCount < 3 && !reachabilityFailure) {
                StringBuilder failureRendering = new StringBuilder();
                List<SoundnessCheck> failureList = newList(categorized.failures);
                failureList.sort(Comparator.comparing(SoundnessCheck::getSourceLocation, new SourceLocation.Comparator()));
                failureList.forEach(f -> failureRendering.append(String.format("%n\t%s: %s", f.getSourceLocation().toString(), f.getMessage())));
                return new SoundnessTestResult(false, String.format("Expected a lot more soundness tests to fail, but most (%d) succeeded unexpectedly (that is a good thing).%n\tPlease mark the following %d locations as failing *instead* of the entire file:%s", counts.successCount, counts.failureCount, failureRendering.toString()));
            }
        } else if (counts.unexpectedFailureCount > 0) {
            List<String> failureExplanationLines = newList();
            int maxFailuresToDisplay = 25;
            Set<SoundnessCheck> liveFailures = newSet(categorized.unexpectedFailures);
            List<SoundnessCheck> failuresToDisplay = liveFailures.stream()
                    .sorted(getFailureComparison())
                    .limit(maxFailuresToDisplay)
                    .collect(Collectors.toList());
            Set<SourceLocation> deadFailureLocations = categorized.unexpectedFailures.stream()
                    .filter(c -> !c.hasDataFlow())
                    .map(SoundnessCheck::getSourceLocation)
                    .collect(Collectors.toSet());

            failuresToDisplay
                    .forEach(f -> failureExplanationLines.add(String.format("\t%s: %s", f.getSourceLocation().toString(), f.getMessage())));
            if (liveFailures.size() > maxFailuresToDisplay) {
                failureExplanationLines.add(String.format("\t... %d more ...", liveFailures.size() - maxFailuresToDisplay));
            }
            if (!deadFailureLocations.isEmpty()) {
                failureExplanationLines.add("Lines with dead expressions that should be live:");
                failureExplanationLines.addAll(IntervalComputations.makePrettyFileLineIntervalLines(deadFailureLocations));
            }
            String failureExplanation = String.join(String.format("%n"), failureExplanationLines);
            String fullMessage = String.format("SoundnessTesting failed (%d unexpected failures / %d checks (%d expected failures)):%n%s", counts.unexpectedFailureCount, counts.checkCount, counts.expectedFailureCount, failureExplanation);
            return new SoundnessTestResult(false, fullMessage);
        } else if (!categorized.expectedFailureLocationsThatDidNotHappen.isEmpty()) {
            StringBuilder failureRendering = new StringBuilder();
            List<SourceLocation> failureList = newList(categorized.expectedFailureLocationsThatDidNotHappen);
            failureList.sort(new SourceLocation.Comparator());
            failureList.forEach(f -> failureRendering.append(String.format("%n\t\t%s", f.toString())));
            return new SoundnessTestResult(false, String.format("Expected more soundness tests to fail, but some succeeded unexpectedly (that is a good thing).%n\tPlease unmark the following locations as failing:%s", failureRendering.toString()));
        }
        return new SoundnessTestResult(true, String.format("Soundness testing succeeded for %d checks (with %d expected failures)", categorized.checks.size(), counts.expectedFailureCount));
    }

    /**
     * Computations related to line-intervals.
     */
    private static class IntervalComputations {

        private static List<String> makePrettyFileLineIntervalLines(Set<SourceLocation> sourceLocations) {
            return sourceLocations.stream()
                    .collect(Collectors.groupingBy(l -> l.toUserFriendlyString(false)))
                    .entrySet().stream()
                    .map(entry -> {
                        List<Pair<Integer, Integer>> intervals = makeLineIntervalsList(entry.getValue());
                        String intervalString = makePrettyLineIntervals(intervals);
                        return String.format("\t%s: %s", entry.getKey(), intervalString);
                    })
                    .collect(Collectors.toList());
        }

        private static String makePrettyLineIntervals(List<Pair<Integer, Integer>> intervals) {
            List<String> intervalStrings = intervals.stream()
                    .map(p -> Objects.equals(p.getFirst(), p.getSecond()) ?
                            String.format("%d", p.getFirst()) :
                            String.format("%d-%d", p.getFirst(), p.getSecond())
                    )
                    .collect(Collectors.toList());
            return String.join(", ", intervalStrings);
        }

        private static List<Pair<Integer, Integer>> makeLineIntervalsList(Collection<SourceLocation> locations) {
            List<Integer> orderedLineNumbers = locations.stream()
                    .map(SourceLocation::getLineNumber)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            if (orderedLineNumbers.isEmpty()) {
                return newList();
            }
            Iterator<Integer> iterator = orderedLineNumbers.iterator();
            int currentStart = iterator.next();
            int previous = currentStart;
            List<Pair<Integer, Integer>> intervals = newList();
            while (iterator.hasNext()) {
                int current = iterator.next();
                if (previous + 1 != current) {
                    intervals.add(Pair.make(currentStart, previous));
                    currentStart = current;
                }
                previous = current;
            }
            intervals.add(Pair.make(currentStart, previous));
            return intervals;
        }
    }
}
