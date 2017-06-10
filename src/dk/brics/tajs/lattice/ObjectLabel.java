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

package dk.brics.tajs.lattice;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.options.OptionValues;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.DeepImmutable;

/**
 * Label of abstract object.
 * Immutable.
 */
public final class ObjectLabel implements Comparable<ObjectLabel>, DeepImmutable {

    /**
     * Source location used for host functions.
     */
    private static final SourceLocation initial_source = new SourceLocation.SyntheticLocationMaker("<initial state>").makeUnspecifiedPosition();

    /**
     * Special object label for absent getter/setter.
     */
    public static final ObjectLabel absent_accessor_function = make(null, null, null, Kind.FUNCTION, null, false);

    /**
     * Object kinds.
     */
    public enum Kind {

        OBJECT("Object"),
        FUNCTION("Function"),
        ARRAY("Array"),
        REGEXP("RegExp"),
        DATE("Date"),
        STRING("String"),
        NUMBER("Number"),
        BOOLEAN("Boolean"),
        ERROR("Error"),
        MATH("Math"),
        ACTIVATION("activation"),
        ARGUMENTS("arguments");

        private String name;

        Kind(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * If set, this abstract object represents a single concrete object.
     * (If not set, it can represent any number of concrete objects.)
     */
    private final boolean singleton;

    private final Kind kind; // [[Class]]

    private final AbstractNode node; // non-null for user defined non-Function objects

    private final HostObject hostobject; // non-null for host objects

    private final Function function; // non-null for user defined functions

    private final HeapContext heapContext; // for context sensitivity

    /**
     * Cached hashcode for immutable instance.
     */
    private final int hashcode;

    private ObjectLabel(HostObject hostobject, AbstractNode node, Function function, Kind kind, HeapContext heapContext, boolean singleton) {
        this.hostobject = hostobject;
        this.node = node;
        this.function = function;
        this.kind = kind;
        if (Options.get().isRecencyDisabled()) {
            this.singleton = false;
        } else {
            this.singleton = singleton;
        }
        if (heapContext == null) {
            this.heapContext = HeapContext.make(null, null);
        } else {
            this.heapContext = heapContext;
        }
        this.hashcode = (this.hostobject != null ? this.hostobject.toString().hashCode() : 0) +
                (this.function != null ? this.function.hashCode() : 0) +
                (this.node != null ? this.node.getIndex() : 0) +
                (this.singleton ? 123 : 0) +
                this.heapContext.hashCode() +
                this.kind.ordinal() * 117; // avoids using enum hashcodes
    }

    public static ObjectLabel make(HostObject hostobject, AbstractNode node, Function function, Kind kind, HeapContext heapContext, boolean singleton){
        return Canonicalizer.get().canonicalize(new ObjectLabel(hostobject, node, function, kind, heapContext, singleton));
    }
    /**
     * Constructs a new object label for a user defined non-function object.
     */
    public static ObjectLabel make(AbstractNode n, Kind kind) {
        return make(null, n, null, kind, null, true);
    }

    /**
     * Constructs a new object label for a user defined non-function object.
     * If {@link OptionValues#isRecencyDisabled()} is disabled, the object label
     * represents a single concrete object (otherwise, it may represent any
     * number of concrete objects).
     */
    public static ObjectLabel make(AbstractNode n, Kind kind, HeapContext heapContext) {
        return make(null, n, null, kind, heapContext, true);
    }

    /**
     * Constructs a new object label for a user defined function object.
     */
    public static ObjectLabel make(Function f) {
        return make(null, null, f, Kind.FUNCTION, null, true);
    }

    /**
     * Constructs a new object label for a user defined function object.
     * If {@link OptionValues#isRecencyDisabled()} is disabled, the object label
     * represents a single concrete object (otherwise, it may represent any
     * number of concrete objects).
     */
    public static ObjectLabel make(Function f, HeapContext heapContext) {
        return make(null, null, f, Kind.FUNCTION, heapContext, true);
    }

    /**
     * Constructs a new object label for a host object.
     * If {@link OptionValues#isRecencyDisabled()} is disabled, the object label
     * represents a single concrete object (otherwise, it may represent any
     * number of concrete objects).
     */
    public static ObjectLabel make(HostObject hostobject, Kind kind) {
        return make(hostobject, null, null, kind, null, true);
    }

    /**
     * Returns the object label kind.
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * Returns the source location.
     */
    public SourceLocation getSourceLocation() {
        if (node != null)
            return node.getSourceLocation();
        else if (function != null)
            return function.getSourceLocation();
        else
            return initial_source;
    }

    /**
     * Returns the node of this non-function object label.
     * Fails if this is a user-defined function.
     */
    public AbstractNode getNode() {
        return node;
    }

    /**
     * Returns true if this object label represents a host object.
     */
    public boolean isHostObject() {
        return hostobject != null;
    }

    /**
     * Returns the descriptor for this object label.
     * It is assumed that this object label represents a host function.
     */
    public HostObject getHostObject() {
        return hostobject;
    }

    /**
     * Returns true if this object label definitely represents a single concrete object.
     */
    public boolean isSingleton() {
        return singleton;
    }

    /**
     * Returns the function of this function object label.
     * Fails if this is not a user-defined function.
     */
    public Function getFunction() {
        if (function == null)
            throw new IllegalArgumentException("Non-Function object label: " + this);
        return function;
    }

    /**
     * Returns the heap context.
     */
    public HeapContext getHeapContext() {
        return heapContext;
    }

    /**
     * Returns the summary object label associated with this singleton object label.
     */
    public ObjectLabel makeSummary() {
        if (!singleton && !Options.get().isRecencyDisabled())
            throw new AnalysisException("Attempt to obtain summary of non-singleton");
        return make(hostobject, node, function, kind, heapContext, false);
    }

    /**
     * Returns the singleton object label associated with this object label, or this object if that is singleton.
     */
    public ObjectLabel makeSingleton() {
        if (singleton)
            return this;
        return make(hostobject, node, function, kind, heapContext, true);
    }

    /**
     * Produces a string representation of this object label.
     */
    @Override
    public String toString() {
        if (this == absent_accessor_function)
            return "<absent getter/setter>";
        StringBuilder b = new StringBuilder();
        if (singleton)
            b.append('@');
        else
            b.append('*');
        if (function != null) {
            String f = function.getName();
            if (f == null)
                f = "<anonymous>";
            b.append(f).append("#fun").append(function.getIndex());
        } else if (hostobject != null)
            b.append(hostobject).append('[').append(hostobject.getAPI().getShortName()).append(']');
        else if (node != null) {
            b.append(kind).append("#node").append(node.getIndex());
        }
        b.append(heapContext);
        return b.toString();
    }

    /**
     * Checks whether the given object label is equal to this one.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof ObjectLabel))
            return false;
        ObjectLabel x = (ObjectLabel) obj;
        if (this.hashcode != x.hashcode) {
            return false;
        }
        if ((hostobject == null) != (x.hostobject == null))
            return false;
        if (!heapContext.equals(x.heapContext)) // using collection equality
            return false;
        return (hostobject == null || hostobject.equals(x.hostobject)) &&
                function == x.function && node == x.node &&
                singleton == x.singleton && kind == x.kind;
    }

    /**
     * Returns the hash code for this object label.
     */
    @Override
    public int hashCode() {
        return hashcode;
    }

    /**
     * Compares this and the given object label.
     * The source location is used as primary key, and toString is used as secondary key.
     */
    @Override
    public int compareTo(ObjectLabel objlabel) {
        int c = getSourceLocation().compareTo(objlabel.getSourceLocation());
        if (c != 0)
            return c;
        if (equals(objlabel))
            return 0;
        return toString().compareTo(objlabel.toString());
    }
}
