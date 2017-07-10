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

package dk.brics.tajs.monitoring.soundness.postprocessing;

import dk.brics.tajs.analysis.KnownUnsoundnesses;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.monitoring.soundness.testing.SoundnessCheck;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;

import java.nio.file.Path;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * A categorization of {@link SoundnessCheck}s.
 */
public class CategorizedSoundnessCheckResults {

    final Set<SoundnessCheck> failures;

    final Set<SoundnessCheck> unexpectedFailures;

    final Set<SourceLocation> expectedFailureLocationsThatDidNotHappen;

    final Set<SoundnessCheck> uninspectedFailures;

    final Set<SoundnessCheck> knownUnsoundnessFailures;

    final Set<SoundnessCheck> knownJalangiFailures;

    final Set<SoundnessCheck> checks;

    public CategorizedSoundnessCheckResults(Set<SoundnessCheck> checks, Path mainFile) {
        this.checks = checks;
        this.failures = newSet(checks.stream()
                .filter(SoundnessCheck::isFailure)
                .collect(Collectors.toList()));

        boolean isManuallyIgnoredMainFile = KnownUnsoundnesses.isUninspectedUnsoundFile(mainFile);
        this.unexpectedFailures = failures.stream()
                .filter(check -> !isManuallyIgnoredMainFile && !KnownUnsoundnesses.isUnsoundLocation(check.getSourceLocation()))
                .collect(Collectors.toSet());

        // Since multiple entries can use the same source location, we remove all entries for a location if a single entry for that location failed
        // This would not be needed if dk.brics.tajs.options.SoundnessTesterOptions.isExpectedFailureLocation was more precise
        Set<SourceLocation> locationsOfFailures = failures.stream()
                .map(SoundnessCheck::getSourceLocation)
                .collect(Collectors.toSet());
        this.expectedFailureLocationsThatDidNotHappen = checks.stream()
                .map(SoundnessCheck::getSourceLocation)
                .filter(KnownUnsoundnesses::isUnsoundLocation)
                .collect(Collectors.toSet());
        expectedFailureLocationsThatDidNotHappen.removeAll(locationsOfFailures);
        // Report results
        this.uninspectedFailures = failures.stream().filter(check -> isManuallyIgnoredMainFile || KnownUnsoundnesses.isUninspectedUnsoundLocation(check.getSourceLocation())).collect(Collectors.toSet());
        this.knownUnsoundnessFailures = failures.stream().filter(check -> KnownUnsoundnesses.isTAJSUnsoundLocation(check.getSourceLocation())).collect(Collectors.toSet());
        this.knownJalangiFailures = failures.stream().filter(check -> KnownUnsoundnesses.isJalangiUnsoundLocation(check.getSourceLocation())).collect(Collectors.toSet());
    }

    public SoundnessCheckCounts getLocationCounts() {
        Set<SourceLocation> checks_locations = extractLocations(checks);
        Set<SourceLocation> failures_locations = extractLocations(failures);
        Set<SourceLocation> unexpectedFailures_locations = extractLocations(unexpectedFailures);
        Set<SourceLocation> expectedFailures_locations = newSet(failures_locations);
        expectedFailures_locations.removeAll(unexpectedFailures_locations);
        Set<SourceLocation> uninspectedFailures_locations = extractLocations(uninspectedFailures);
        Set<SourceLocation> knownUnsoundnessFailures_locations = extractLocations(knownUnsoundnessFailures);
        Set<SourceLocation> knownJalangiFailures_locations = extractLocations(knownJalangiFailures);

        Set<SourceLocation> successCount_locations = newSet(checks_locations);
        successCount_locations.removeAll(failures_locations);
        SoundnessCheckCounts locationCounts = new SoundnessCheckCounts(
                checks_locations.size(),
                failures_locations.size(),
                successCount_locations.size(),
                unexpectedFailures_locations.size(),
                expectedFailures_locations.size(),
                uninspectedFailures_locations.size(),
                knownUnsoundnessFailures_locations.size(),
                knownJalangiFailures_locations.size());
        return locationCounts;
    }

    private Set<SourceLocation> extractLocations(Set<SoundnessCheck> checks) {
        return checks.stream().map(SoundnessCheck::getSourceLocation).collect(Collectors.toSet());
    }

    public SoundnessCheckCounts getRawCounts() {
        SoundnessCheckCounts rawCounts = new SoundnessCheckCounts(
                checks.size(),
                failures.size(),
                checks.size() - failures.size(),
                unexpectedFailures.size(),
                failures.size() - unexpectedFailures.size(),
                uninspectedFailures.size(),
                knownUnsoundnessFailures.size(),
                knownJalangiFailures.size());
        int checkSum = rawCounts.uninspectedFailureCount + rawCounts.knownUnsoundnessFailureCount + rawCounts.knownJalangiFailureCount;
        if (checkSum != rawCounts.expectedFailureCount) {
            throw new AnalysisException("Inconsistent failure rawCounts.");
        }
        return rawCounts;
    }
}
