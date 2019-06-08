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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * Decorator that caches the results of its delegate.
 */
public class CachingNativeConcreteSemantics implements NativeConcreteSemantics {

    private final NativeConcreteSemantics delegate;

    private final Map<Signature, MappedNativeResult<ConcreteValue>> signatureCache;

    private final Map<String, NativeResult<ConcreteValue>> evalCache;

    public CachingNativeConcreteSemantics(NativeConcreteSemantics delegate) {
        this.delegate = delegate;
        signatureCache = new WeakHashMap<>();
        evalCache = new WeakHashMap<>();
    }

    @Override
    public MappedNativeResult<ConcreteValue> apply(String functionName, ConcreteValue base, List<ConcreteValue> arguments) {
        Signature key = new Signature(functionName, base, arguments);
        if (!signatureCache.containsKey(key)) {
            signatureCache.put(key, delegate.apply(functionName, base, arguments));
        }
        return signatureCache.get(key);
    }

    @Override
    public NativeResult<ConcreteValue> eval(String script) {
        if (!evalCache.containsKey(script)) {
            evalCache.put(script, delegate.eval(script));
        }
        return evalCache.get(script);
    }

    private class Signature {

        private final String functionName;

        private final ConcreteValue base;

        private final List<ConcreteValue> arguments;

        private final int hashCode;

        public Signature(String functionName, ConcreteValue base, List<ConcreteValue> arguments) {
            this.functionName = functionName;
            this.base = base;
            this.arguments = arguments;
            int hashCode = functionName != null ? functionName.hashCode() : 0;
            hashCode = 31 * hashCode + (base != null ? base.hashCode() : 0);
            hashCode = 31 * hashCode + (arguments != null ? arguments.hashCode() : 0);
            this.hashCode = hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Signature signature = (Signature) o;

            if (!Objects.equals(functionName, signature.functionName)) return false;
            if (!Objects.equals(base, signature.base)) return false;
            return Objects.equals(arguments, signature.arguments);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }
}
