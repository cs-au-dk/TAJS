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

package dk.brics.tajs.analysis.nativeobjects.concrete;

import java.util.Optional;

/**
 * Result of a native call with an (optional) mapping to the concrete receiver and arguments.
 */
public class MappedNativeResult<T> {

    private final Optional<ConcreteApplyMapping> mapped;

    private final NativeResult<T> result;

    public MappedNativeResult(Optional<ConcreteApplyMapping> mapped, NativeResult<T> result) {
        this.mapped = mapped;
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MappedNativeResult<?> that = (MappedNativeResult<?>) o;

        if (mapped != null ? !mapped.equals(that.mapped) : that.mapped != null) return false;
        return result != null ? result.equals(that.result) : that.result == null;
    }

    @Override
    public int hashCode() {
        int result1 = mapped != null ? mapped.hashCode() : 0;
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        return result1;
    }

    public Optional<ConcreteApplyMapping> getMapped() {
        return mapped;
    }

    public NativeResult<T> getResult() {
        return result;
    }
}
