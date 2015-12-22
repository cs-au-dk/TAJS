/*
 * Copyright 2009-2015 Aarhus University
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

import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;

/**
 * Reference to an object property.
 * Immutable.
 */
public class PropertyReference {

    private ObjectLabel objlabel;

    private String propertyname;

    private int hashcode;

    /**
     * Kind of property being referenced.
     */
    public enum Kind {
        ORDINARY,
        DEFAULT_ARRAY,
        DEFAULT_NONARRAY,
        INTERNAL_VALUE,
        INTERNAL_PROTOTYPE,
        INTERNAL_SCOPE
    }

    private Kind kind;

    private PropertyReference(Kind kind, ObjectLabel objlabel, String propertyname) {
        this.kind = kind;
        this.objlabel = objlabel;
        this.propertyname = propertyname;
        hashcode = kind.hashCode() * 5 + (objlabel != null ? objlabel.hashCode() * 13 : 0) + (propertyname != null ? propertyname.hashCode() * 31 : 0);
    }

    /**
     * Constructs a reference to ordinary property.
     */
    public static PropertyReference makeOrdinaryPropertyReference(ObjectLabel objlabel, String propertyname) {
        return new PropertyReference(Kind.ORDINARY, objlabel, propertyname);
    }

    /**
     * Constructs a reference to a default-array property.
     */
    public static PropertyReference makeDefaultArrayPropertyReference(ObjectLabel objlabel) {
        return new PropertyReference(Kind.DEFAULT_ARRAY, objlabel, null);
    }

    /**
     * Constructs a reference to a default-nonarray property.
     */
    public static PropertyReference makeDefaultNonArrayPropertyReference(ObjectLabel objlabel) {
        return new PropertyReference(Kind.DEFAULT_NONARRAY, objlabel, null);
    }

    /**
     * Constructs a reference to an internal value property.
     */
    public static PropertyReference makeInternalValuePropertyReference(ObjectLabel objlabel) {
        return new PropertyReference(Kind.INTERNAL_VALUE, objlabel, null);
    }

    /**
     * Constructs a reference to an internal prototype property.
     */
    public static PropertyReference makeInternalPrototypePropertyReference(ObjectLabel objlabel) {
        return new PropertyReference(Kind.INTERNAL_PROTOTYPE, objlabel, null);
    }

    /**
     * Constructs a reference to an internal scope property.
     */
    public static PropertyReference makeInternalScopePropertyReference(ObjectLabel objlabel) {
        return new PropertyReference(Kind.INTERNAL_SCOPE, objlabel, null);
    }

    /**
     * Constructs a copy of this property reference but with a singleton object label instead of a summary object label.
     */
    public PropertyReference makeSingleton() {
        if (objlabel.isSingleton())
            throw new AnalysisException("unexpected object label " + objlabel);
        return new PropertyReference(kind, objlabel.makeSingleton(), propertyname);
    }

    /**
     * Constructs a copy of this property reference but with another object label.
     */
    public PropertyReference makeRenamed(ObjectLabel new_objlabel) {
        return new PropertyReference(kind, new_objlabel, propertyname);
    }

    /**
     * Returns the hash code for this object.
     */
    @Override
    public int hashCode() {
        return hashcode;
    }

    /**
     * Indicates whether some other object is equal to this one.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PropertyReference other = (PropertyReference) obj;
        if (kind != other.kind)
            return false;
        if (objlabel == null) {
            if (other.objlabel != null)
                return false;
        } else if (!objlabel.equals(other.objlabel))
            return false;
        if (propertyname == null) {
            if (other.propertyname != null)
                return false;
        } else if (!propertyname.equals(other.propertyname))
            return false;
        return true;
    }

    /**
     * Returns the kind.
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * Returns the object label.
     */
    public ObjectLabel getObjectLabel() {
        return objlabel;
    }

    /**
     * Returns the property name (for ordinary properties).
     */
    public String getPropertyName() {
        return propertyname;
    }

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(objlabel).append('.');
        switch (kind) {
            case ORDINARY:
                b.append(Strings.escape(propertyname));
                break;
            case DEFAULT_ARRAY:
                b.append("[[default-array]]");
                break;
            case DEFAULT_NONARRAY:
                b.append("[[default-nonarray]]");
                break;
            case INTERNAL_VALUE:
                b.append("[[Value]]");
                break;
            case INTERNAL_PROTOTYPE:
                b.append("[[Prototype]]");
                break;
            case INTERNAL_SCOPE:
                b.append("[[Scope]]");
                break;
        }
        return b.toString();
    }
}
