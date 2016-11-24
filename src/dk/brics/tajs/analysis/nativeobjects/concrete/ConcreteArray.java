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

import java.util.List;
import java.util.Map;

public class ConcreteArray implements ConcreteValue {

    private final List<ConcreteValue> array;

    private final Map<String, ConcreteValue> extraProperties;

    /**
     * Creates an array of concrete values, with some extra properties.
     * NB: the extra properties are *not* represented in the toSourceCode result!
     */
    public ConcreteArray(List<ConcreteValue> array, Map<String, ConcreteValue> extraProperties) {
        this.array = array;
        this.extraProperties = extraProperties;
        for (int i = 0; i < array.size(); i++) {
            extraProperties.remove(i + ""); // kill the numeric properties, to avoid double-representation
        }
    }

    public Map<String, ConcreteValue> getExtraProperties() {
        return extraProperties;
    }

    @Override
    public String toSourceCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (ConcreteValue argument : array) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(argument.toSourceCode());
        }
        sb.append("]");
        return sb.toString();
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
}
