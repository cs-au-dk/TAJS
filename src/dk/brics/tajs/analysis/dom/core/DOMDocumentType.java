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

package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * Each Document has a doctype attribute whose value is either null or a
 * DocumentType object. The DocumentType interface in the DOM Core provides an
 * interface to the list of entities that are defined for the document, and
 * little else because the effect of namespaces and the various XML schema
 * efforts on DTD representation are not clearly understood as of this writing.
 */
public class DOMDocumentType {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.DOCUMENTTYPE_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.DOCUMENTTYPE_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.DOCUMENTTYPE_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "DocumentType", Value.makeObject(CONSTRUCTOR));

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(DOMNode.PROTOTYPE));

        // Multiplied object.
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        // DOM Level 1
        createDOMProperty(s, INSTANCES, "name", Value.makeAnyStr().setReadOnly());
        createDOMProperty(s, INSTANCES, "entities", Value.makeObject(DOMNamedNodeMap.INSTANCES).setReadOnly());
        createDOMProperty(s, INSTANCES, "notations", Value.makeObject(DOMNamedNodeMap.INSTANCES).setReadOnly());

        // DOM Level 2
        createDOMProperty(s, INSTANCES, "publicId", Value.makeAnyStr().setReadOnly());
        createDOMProperty(s, INSTANCES, "systemId", Value.makeAnyStr().setReadOnly());
        createDOMProperty(s, INSTANCES, "internalSubset", Value.makeAnyStr().setReadOnly());

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions
         */
        // No functions.
    }
}
