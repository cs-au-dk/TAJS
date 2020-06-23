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

package dk.brics.tajs.analysis.nativeobjects.concrete;

import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.PKey.StringPKey;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConcreteArray implements ConcreteValue {

    private final List<ConcreteValue> array;

    private final Map<PKey, ConcreteValue> extraProperties;

    /**
     * Creates an array of concrete values, with some extra properties.
     * NB: the extra properties are *not* represented in the toSourceCode result!
     */
    public ConcreteArray(List<ConcreteValue> array, Map<PKey, ConcreteValue> extraProperties) {
        this.array = array;
        this.extraProperties = extraProperties;
        for (int i = 0; i < array.size(); i++) {
            extraProperties.remove(StringPKey.make(i + "")); // kill the numeric properties, to avoid double-representation
        }
    }

    public Map<PKey, ConcreteValue> getExtraProperties() {
        return extraProperties;
    }

    @Override
    public String toSourceCode() {
        return array.stream()
                .map(ConcreteValue::toSourceCode)
                .collect(java.util.stream.Collectors.joining(",", "[", "]"));
    }

    @Override
    public String toString() {
        return toSourceCode();
    }

    @Override
    public <T> T accept(ConcreteValueVisitor<T> v) {
        return v.visit(this);
    }

    public int getLength() {
        return array.size();
    }

    public ConcreteValue get(int i) {
        return array.get(i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConcreteArray that = (ConcreteArray) o;

        if (!Objects.equals(array, that.array)) return false;
        return Objects.equals(extraProperties, that.extraProperties);

    }

    @Override
    public int hashCode() {
        int result = array != null ? array.hashCode() : 0;
        result = 31 * result + (extraProperties != null ? extraProperties.hashCode() : 0);
        return result;
    }
}
