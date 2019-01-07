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

public class ConcreteRegularExpression implements ConcreteValue {

    private final ConcreteString source;

    private final ConcreteBoolean global;

    private final ConcreteBoolean ignoreCase;

    private final ConcreteBoolean multiline;

    private final ConcreteNumber lastIndex;

    public ConcreteRegularExpression(ConcreteString source, ConcreteBoolean global, ConcreteBoolean ignoreCase, ConcreteBoolean multiline, ConcreteNumber lastIndex) {
        this.source = source;
        this.global = global;
        this.ignoreCase = ignoreCase;
        this.multiline = multiline;
        this.lastIndex = lastIndex;
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

    public ConcreteNumber getLastIndex() {
        return lastIndex;
    }

    @Override
    public String toSourceCode() {
        return String.format("(function(){ var r = /%s/%s%s%s; r.lastIndex = %s; return r;})()", source.toRegExpSourceCodeComponent(), global.getBooleanValue() ? "g" : "", ignoreCase.getBooleanValue() ? "i" : "", multiline.getBooleanValue() ? "m" : "", lastIndex.toSourceCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConcreteRegularExpression that = (ConcreteRegularExpression) o;

        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (global != null ? !global.equals(that.global) : that.global != null) return false;
        if (ignoreCase != null ? !ignoreCase.equals(that.ignoreCase) : that.ignoreCase != null) return false;
        if (multiline != null ? !multiline.equals(that.multiline) : that.multiline != null) return false;
        return lastIndex != null ? lastIndex.equals(that.lastIndex) : that.lastIndex == null;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (global != null ? global.hashCode() : 0);
        result = 31 * result + (ignoreCase != null ? ignoreCase.hashCode() : 0);
        result = 31 * result + (multiline != null ? multiline.hashCode() : 0);
        result = 31 * result + (lastIndex != null ? lastIndex.hashCode() : 0);
        return result;
    }
}
