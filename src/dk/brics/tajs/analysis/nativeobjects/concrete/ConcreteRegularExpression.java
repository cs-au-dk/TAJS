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

public class ConcreteRegularExpression implements ConcreteValue {

    private final ConcreteString source;

    private final ConcreteBoolean global;

    private final ConcreteBoolean ignoreCase;

    private final ConcreteBoolean multiline;

    public ConcreteRegularExpression(ConcreteString source, ConcreteBoolean global, ConcreteBoolean ignoreCase, ConcreteBoolean multiline) {
        this.source = source;
        this.global = global;
        this.ignoreCase = ignoreCase;
        this.multiline = multiline;
    }

    @Override
    public <T> T accept(ConcreteValueVisitor<T> v) {
        return v.visit(this);
    }

    public ConcreteBoolean getGlobal() {
        return global;
    }

    public ConcreteBoolean getIgnoreCase() {
        return ignoreCase;
    }

    public ConcreteBoolean getMultiline() {
        return multiline;
    }

    public ConcreteString getSource() {
        return source;
    }

    @Override
    public String toSourceCode() {
        return String.format("/%s/%s%s%s", source.toRegExpSourceCodeComponent(), global.getBooleanValue() ? "g" : "", ignoreCase.getBooleanValue() ? "i" : "", multiline.getBooleanValue() ? "m" : "");
    }
}
