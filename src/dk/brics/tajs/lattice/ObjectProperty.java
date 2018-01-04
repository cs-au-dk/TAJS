/*
 * Copyright 2009-2018 Aarhus University
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

/**
 * Pair of an {@link ObjectLabel} and a {@link Property}.
 * Immutable.
 */
public class ObjectProperty {

    private ObjectLabel objlabel;

    private Property property;

    private int hashcode;

    /**
     * Constructs an ObjectProperty.
     */
    public ObjectProperty(ObjectLabel objlabel, Property property) {
        this.objlabel = objlabel;
        this.property = property;
        hashcode = (objlabel != null ? objlabel.hashCode() * 13 : 0) + (property != null ? property.hashCode() * 31 : 0);
    }

    /**
     * Constructs an ObjectProperty for an ordinary property.
     */
    public static ObjectProperty makeOrdinary(ObjectLabel objlabel, PKey propertyname) {
        return new ObjectProperty(objlabel, Property.makeOrdinaryProperty(propertyname));
    }

    /**
     * Constructs an ObjectProperty for a default-array property.
     */
    public static ObjectProperty makeDefaultArray(ObjectLabel objlabel) {
        return new ObjectProperty(objlabel, Property.makeDefaultArrayProperty());
    }

    /**
     * Constructs an ObjectProperty for a default-nonarray property.
     */
    public static ObjectProperty makeDefaultNonArray(ObjectLabel objlabel) {
        return new ObjectProperty(objlabel, Property.makeDefaultNonArrayProperty());
    }

    /**
     * Constructs an ObjectProperty for an internal value property.
     */
    public static ObjectProperty makeInternalValue(ObjectLabel objlabel) {
        return new ObjectProperty(objlabel, Property.makeInternalValueProperty());
    }

    /**
     * Constructs an ObjectProperty for an internal prototype property.
     */
    public static ObjectProperty makeInternalPrototype(ObjectLabel objlabel) {
        return new ObjectProperty(objlabel, Property.makeInternalPrototypeProperty());
    }

    /**
     * Constructs an ObjectProperty for an internal scope property.
     */
    public static ObjectProperty makeInternalScope(ObjectLabel objlabel) {
        return new ObjectProperty(objlabel, Property.makeInternalScopeProperty());
    }

    /**
     * Constructs a copy of this ObjectProperty but with a singleton object label instead of a summary object label.
     */
    public ObjectProperty makeSingleton() {
        if (objlabel.isSingleton())
            throw new AnalysisException("unexpected object label " + objlabel);
        return new ObjectProperty(objlabel.makeSingleton(), property);
    }

    /**
     * Constructs a copy of this ObjectProperty but with another object label.
     */
    public ObjectProperty makeRenamed(ObjectLabel new_objlabel) {
        return new ObjectProperty(new_objlabel, property);
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
        ObjectProperty other = (ObjectProperty) obj;
        if (objlabel == null) {
            if (other.objlabel != null)
                return false;
        } else if (!objlabel.equals(other.objlabel))
            return false;
        if (property == null) {
            if (other.property != null)
                return false;
        } else if (!property.equals(other.property))
            return false;
        return true;
    }

    /**
     * Returns the property.
     */
    public Property getProperty() {
        return property;
    }

    /**
     * Returns the kind.
     */
    public Property.Kind getKind() { // FIXME: inline all calls?
        return property.getKind();
    }

    /**
     * Returns the object label.
     */
    public ObjectLabel getObjectLabel() {
        return objlabel;
    }

    /**
     * Returns the property key (for ordinary properties).
     */
    public PKey getPropertyName() {
        return property.getPropertyName();
    }

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return objlabel + "." + property;
    }
}
