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

package dk.brics.tajs.lattice;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.DeepImmutable;

import java.util.Objects;

/**
 * Token for differentiating different partitions in a PartitionedValue.
 */
public abstract class PartitionToken implements DeepImmutable {

    /**
     * The node where the partitioning is introduced.
     */
    private final AbstractNode n;

    PartitionToken(AbstractNode n) {
        this.n = n;
    }

    public AbstractNode getNode() {
        return n;
    }

    public abstract String toString();

    public static class PropertyNamePartitionToken extends PartitionToken {

        /**
         * Property name used for distinguish partitions introduced at the same location.
         */
        private final Property prop;

        private final int hashcode;

        /**
         * Creates the token introduced for properties described by the given PKey.
         */
        private PropertyNamePartitionToken(AbstractNode n, Property prop) {
            super(n);
            this.prop = prop;
            this.hashcode = Objects.hash(prop, n);
        }

        public static PropertyNamePartitionToken make(AbstractNode n, Property prop) {
            return Canonicalizer.get().canonicalize(new PropertyNamePartitionToken(n, prop));
        }

        @Override
        public boolean equals(Object o) {
            if (!Canonicalizer.get().isCanonicalizing())
                return this == o;
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PropertyNamePartitionToken that = (PropertyNamePartitionToken) o;
            return Objects.equals(prop, that.prop) &&
                    Objects.equals(getNode(), that.getNode());
        }

        @Override
        public int hashCode() {
            return hashcode;
        }

        @Override
        public String toString() {
            return "(node" + getNode().getIndex() + "," + prop + ")";
        }

        public Property getProperty() {
            return prop;
        }
    }

    public static class TypePartitionToken extends PartitionToken {

        private final Type type;

        private final int hashcode;

        /**
         * Creates the token introduced for the given type.
         */
        private TypePartitionToken(AbstractNode n, Type type) {
            super(n);
            this.type = type;
            this.hashcode = Objects.hash(type, n);
        }

        public static TypePartitionToken make(AbstractNode n, Type type) {
            return Canonicalizer.get().canonicalize(new TypePartitionToken(n, type));
        }

        @Override
        public String toString() {
            return "(node" + getNode().getIndex() + "," + type + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TypePartitionToken that = (TypePartitionToken) o;
            return hashcode == that.hashcode &&
                    type == that.type;
        }

        @Override
        public int hashCode() {
            return this.hashcode;
        }

        /**
         * Types
         */
        public enum Type {

            STRING("String"),
            NUMBER("Number"),
            BOOLEAN("Boolean"),
            UNDEF("Undef"),
            NULL("Null"),

            OBJ_OBJECT("OBJ_Object"),
            OBJ_FUNCTION("OBJ_Function"),
            OBJ_SYMBOL("OBJ_Symbol"),
            OBJ_ARRAY("OBJ_Array"),
            OBJ_REGEXP("OBJ_RegExp"),
            OBJ_DATE("OBJ_Date"),
            OBJ_STRING("OBJ_String"),
            OBJ_NUMBER("OBJ_Number"),
            OBJ_BOOLEAN("OBJ_Boolean"),
            OBJ_ERROR("OBJ_Error"),
            OBJ_MATH("OBJ_Math"),
            OBJ_ACTIVATION("OBJ_activation"),
            OBJ_ARGUMENTS("OBJ_arguments");

            private String name;

            Type(String name) {
                this.name = name;
            }

            public static Type getObjectType(ObjectLabel.Kind kind) {
                switch (kind) {
                    case OBJECT: return OBJ_OBJECT;
                    case FUNCTION: return OBJ_FUNCTION;
                    case SYMBOL: return OBJ_SYMBOL;
                    case ARRAY: return OBJ_ARRAY;
                    case REGEXP: return OBJ_REGEXP;
                    case DATE: return OBJ_DATE;
                    case STRING: return OBJ_STRING;
                    case NUMBER: return OBJ_NUMBER;
                    case BOOLEAN: return OBJ_BOOLEAN;
                    case ERROR: return OBJ_ERROR;
                    case MATH: return OBJ_MATH;
                    case ACTIVATION: return OBJ_ACTIVATION;
                    case ARGUMENTS: return OBJ_ARGUMENTS;
                    default: throw new AnalysisException("Unhandled ObjectLabel kind");
                }
            }

            @Override
            public String toString() {
                return name;
            }
        }
    }

    public static class FunctionPartitionToken extends PartitionToken {

        private final Function function;

        private final Context c;

        private final PartitionToken q;

        private final int hashcode;

        private FunctionPartitionToken(AbstractNode n, Context c, Function function, PartitionToken q) {
            super(n);
            this.c = c;
            this.q = q;
            this.function = function;
            this.hashcode = Objects.hash(n, c, function, q);
        }

        public static FunctionPartitionToken make(AbstractNode n, Context c, Function function, PartitionToken q) {
            return Canonicalizer.get().canonicalize(new FunctionPartitionToken(n, c, function, q));
        }

        public Function getFunction() {
            return function;
        }

        @Override
        public String toString() {
            return "(node" + getNode().getIndex() + ",function" + function.getIndex() + ",context=" + c + ",token=" + q + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (!Canonicalizer.get().isCanonicalizing())
                return this == o;
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FunctionPartitionToken that = (FunctionPartitionToken) o;
            return hashcode == that.hashcode &&
                    Objects.equals(q, that.q) &&
                    Objects.equals(function, that.function) &&
                    Objects.equals(c, that.c);
        }

        @Override
        public int hashCode() {
            return hashcode;
        }
    }
}
