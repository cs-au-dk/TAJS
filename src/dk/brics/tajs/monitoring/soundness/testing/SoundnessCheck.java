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

import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * A soundness check. If {@link #isFailure()} is true, then the static analysis is provably unsound at {@link #getSourceLocation()}
 */
public interface SoundnessCheck {

    SourceLocation getSourceLocation();

    String getMessage();

    boolean isFailure();

    boolean hasDataFlow();

    FailureKind getFailureKind();

    enum FailureKind {
        MISSING_NATIVE_PROPERTY,
        WRONG_VALUE,
        MISSING_CALL,
        UNREACHABLE,
        MISSING_DATAFLOW
    }
}
