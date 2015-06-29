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

package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The object used to represent the TH and TD elements. See the TD element
 * definition in HTML 4.01.
 */
public class HTMLTableCellElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLTABLECELLELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.HTMLTABLECELLELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.HTMLTABLECELLELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "HTMLTableCellElement", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        // DOM Level 1
        createDOMProperty(s, INSTANCES, "cellIndex", Value.makeAnyNum());
        createDOMProperty(s, INSTANCES, "abbr", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "align", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "axis", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "bgColor", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "ch", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "chOff", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "colSpan", Value.makeAnyNum());
        createDOMProperty(s, INSTANCES, "headers", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "height", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "noWrap", Value.makeAnyBool());
        createDOMProperty(s, INSTANCES, "rowSpan", Value.makeAnyNum());
        createDOMProperty(s, INSTANCES, "scope", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "vAlign", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "width", Value.makeAnyStr());

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // No functions
    }
}
