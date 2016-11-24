/*
 * Copyright 2009-2016 Aarhus University
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

public class InvocationResult<T> {

    public final Kind kind;

    private final T value;

    private InvocationResult(Kind kind, T value) {
        this.kind = kind;
        this.value = value;
    }

    public static <T> InvocationResult<T> makeValue(T value) {
        return new InvocationResult<>(Kind.VALUE, value);
    }

    public static <T> InvocationResult<T> makeException() {
        return new InvocationResult<>(Kind.EXCEPTION, null);
    }

    public static <T> InvocationResult<T> makeBottom() {
        return new InvocationResult<>(Kind.BOTTOM, null);
    }

    public static <T> InvocationResult<T> makeNonConcrete() {
        return new InvocationResult<>(Kind.NON_CONCRETE, null);
    }

    public T getValue() {
        if (kind != Kind.VALUE)
            throw new AnalysisException("Can not request value from InvocationResult of kind: " + kind + "!");
        return value;
    }

    public enum Kind {
        VALUE,
        BOTTOM,
        EXCEPTION,
        NON_CONCRETE
    }
}
