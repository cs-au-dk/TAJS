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

package dk.brics.tajs.analysis.nativeobjects.concrete;

import dk.brics.tajs.util.AnalysisException;

import java.util.Objects;

public class NativeResult<T> {

    public final Kind kind;

    private final T value;

    private NativeResult(Kind kind, T value) {
        this.kind = kind;
        this.value = value;
    }

    public static <T> NativeResult<T> makeValue(T value) {
        return new NativeResult<>(Kind.VALUE, value);
    }

    public static <T> NativeResult<T> makeException() {
        return new NativeResult<>(Kind.EXCEPTION, null);
    }

    public static <T> NativeResult<T> makeBottom() {
        return new NativeResult<>(Kind.BOTTOM, null);
    }

    public static <T> NativeResult<T> makeNonConcrete() {
        return new NativeResult<>(Kind.NON_CONCRETE, null);
    }

    public T getValue() {
        if (kind != Kind.VALUE)
            throw new AnalysisException("Can not request value from InvocationResult of kind: " + kind + "!");
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NativeResult<?> that = (NativeResult<?>) o;

        if (kind != that.kind) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = kind != null ? kind.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "InvocationResult(kind: " + kind + ", value: " + value + ")";
    }

    public enum Kind {
        VALUE,
        BOTTOM,
        EXCEPTION,
        NON_CONCRETE
    }
}
