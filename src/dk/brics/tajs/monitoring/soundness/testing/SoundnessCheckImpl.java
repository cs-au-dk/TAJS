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

import java.util.Objects;

import static dk.brics.tajs.monitoring.soundness.ValueLoggerSourceLocationMapper.makeTAJSSourceLocation;

/**
 * Base class for {@link SoundnessCheck} implementations.
 */
public abstract class SoundnessCheckImpl implements SoundnessCheck {

    private final SourceLocation sourceLocation;

    private final String message;

    private final boolean failure;

    public SoundnessCheckImpl(SourceLocation sourceLocation, String message, boolean failure) {
        this.sourceLocation = sourceLocation;
        this.message = message;
        this.failure = failure;
    }

    public SoundnessCheckImpl(dk.au.cs.casa.jer.entries.SourceLocation sourceLocation, String message, boolean failure) {
        this(makeTAJSSourceLocation(sourceLocation), message, failure);
    }

    @Override
    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean isFailure() {
        return failure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoundnessCheckImpl that = (SoundnessCheckImpl) o;

        if (failure != that.failure) return false;
        if (!Objects.equals(sourceLocation, that.sourceLocation))
            return false;
        if (!Objects.equals(message, that.message)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sourceLocation != null ? sourceLocation.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (failure ? 1 : 0);
        return result;
    }
}
