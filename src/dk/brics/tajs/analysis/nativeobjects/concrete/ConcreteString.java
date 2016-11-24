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

public class ConcreteString implements PrimitiveConcreteValue {

    private final String string;

    public ConcreteString(String string) {
        this.string = string;
    }

    private String escapeDoubleQuotesAndNewLines(String string) {
        return string.replace("\"", "\\\"").replaceAll("(\\r|\\n|\\r\\n|\u2028|\u2029)+", "\\\\n");
    }

    private String escapeBackslashes(String string) {
        return string.replace("\\", "\\\\");
    }

    @Override
    public String toSourceCode() {
        return String.format("\"%s\"", escapeDoubleQuotesAndNewLines(escapeBackslashes(string)));
    }

    public String toRegExpSourceCodeComponent() {
        return escapeDoubleQuotesAndNewLines(string);
    }

    @Override
    public <T> T accept(ConcreteValueVisitor<T> v) {
        return v.visit(this);
    }

    public String getString() {
        return string;
    }
}
