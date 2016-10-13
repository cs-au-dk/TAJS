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

package dk.brics.tajs.lattice;

import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;

/**
 * An object property.
 * Immutable.
 */
public class Property {

    private String propertyname;

    private int hashcode;

    /**
     * Kind of property.
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

    private static Property theDefaultArrayProperty =  new Property(Kind.DEFAULT_ARRAY, null);

    private static Property theDefaultNonArrayProperty =  new Property(Kind.DEFAULT_NONARRAY, null);

    private static Property theInternalValueProperty =  new Property(Kind.INTERNAL_VALUE, null);

    private static Property theInternalPrototypeProperty =  new Property(Kind.INTERNAL_PROTOTYPE, null);

    private static Property theInternalScopeProperty =  new Property(Kind.INTERNAL_SCOPE, null);

    private Property(Kind kind, String propertyname) {
        this.kind = kind;
        this.propertyname = propertyname;
        hashcode = kind.hashCode() * 5 + (propertyname != null ? propertyname.hashCode() * 31 : 0);
    }

    /**
     * Constructs an ordinary property.
     */
    public static Property makeOrdinaryProperty(String propertyname) {
        return new Property(Kind.ORDINARY, propertyname);
    }

    /**
     * Constructs a default-array property.
     */
    public static Property makeDefaultArrayProperty() {
        return theDefaultArrayProperty;
    }

    /**
     * Constructs a default-nonarray property.
     */
    public static Property makeDefaultNonArrayProperty() {
        return theDefaultNonArrayProperty;
    }

    /**
     * Constructs an internal value property.
     */
    public static Property makeInternalValueProperty() {
        return theInternalValueProperty;
    }

    /**
     * Constructs an internal prototype property.
     */
    public static Property makeInternalPrototypeProperty() {
        return theInternalPrototypeProperty;
    }

    /**
     * Constructs an internal scope property.
     */
    public static Property makeInternalScopeProperty() {
        return theInternalScopeProperty;
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
        Property other = (Property) obj;
        if (kind != other.kind)
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
     * Checks whether the property refers to default-array/nonarray.
     * Should not be invoked with an internal property reference.
     */
    public boolean isFuzzy() {
        switch (kind) {
            case ORDINARY:
                return false;
            case DEFAULT_ARRAY:
            case DEFAULT_NONARRAY:
                return false;
            default:
                throw new AnalysisException(kind + " not expected");
        }
    }

    /**
     * Returns the property name (for ordinary properties).
     */
    public String getPropertyName() {
        return propertyname;
    }

    /**
     * Converts this property to a {@link Str}.
     */
    public Str toStr() {
        switch (kind) {
            case ORDINARY:
                return Value.makeStr(propertyname);
            case DEFAULT_ARRAY:
                return Value.makeAnyStrUInt();
            case DEFAULT_NONARRAY:
                return Value.makeAnyStrNotUInt();
            default:
                throw new AnalysisException(kind + " not expected");
        }
    }

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        switch (kind) {
            case ORDINARY:
                return Strings.escape(propertyname);
            case DEFAULT_ARRAY:
                return "[[default-array]]";
            case DEFAULT_NONARRAY:
                return "[[default-nonarray]]";
            case INTERNAL_VALUE:
                return "[[Value]]";
            case INTERNAL_PROTOTYPE:
                return "[[Prototype]]";
            case INTERNAL_SCOPE:
                return "[[Scope]]";
        }
        throw new AnalysisException(kind + " not expected");
    }
}
