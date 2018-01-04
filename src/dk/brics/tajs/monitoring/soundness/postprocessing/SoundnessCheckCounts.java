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

/**
 * Counters for {@link dk.brics.tajs.monitoring.soundness.testing.SoundnessCheck}s.
 */
class SoundnessCheckCounts {

    public final int checkCount;

    public final int failureCount;

    public final int successCount;

    public final int unexpectedFailureCount;

    public final int expectedFailureCount;

    public final int uninspectedFailureCount;

    public final int knownUnsoundnessFailureCount;

    public final int knownJalangiFailureCount;

    public SoundnessCheckCounts(int checkCount, int failureCount, int successCount, int unexpectedFailureCount, int expectedFailureCount, int uninspectedFailureCount, int knownUnsoundnessFailureCount, int knownJalangiFailureCount) {
        this.checkCount = checkCount;
        this.failureCount = failureCount;
        this.successCount = successCount;
        this.unexpectedFailureCount = unexpectedFailureCount;
        this.expectedFailureCount = expectedFailureCount;
        this.uninspectedFailureCount = uninspectedFailureCount;
        this.knownUnsoundnessFailureCount = knownUnsoundnessFailureCount;
        this.knownJalangiFailureCount = knownJalangiFailureCount;
    }
}
