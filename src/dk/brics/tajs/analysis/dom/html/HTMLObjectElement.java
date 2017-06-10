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

package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * Generic embedded object.
 * <p>
 * Note: In principle, all properties on the object element are read-write but
 * in some environments some properties may be read-only once the underlying
 * object is instantiated. See the OBJECT element definition in [HTML 4.01].
 */
public class HTMLObjectElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.HTMLOBJECTELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.HTMLOBJECTELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.HTMLOBJECTELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "HTMLObjectElement", Value.makeObject(CONSTRUCTOR));

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
        createDOMProperty(INSTANCES, "form", Value.makeObject(HTMLFormElement.INSTANCES).setReadOnly(), c);
        createDOMProperty(INSTANCES, "code", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "align", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "archive", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "border", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "codeBase", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "codeType", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "data", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "declare", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "height", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "hspace", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "standby", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "tabIndex", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "type", Value.makeAnyStr().restrictToNotStrIdentifierParts() /* mime-type */, c);
        createDOMProperty(INSTANCES, "useMap", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "vspace", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "width", Value.makeAnyStr(), c);

        // DOM Level 2
        createDOMProperty(INSTANCES, "contentDocument", Value.makeObject(DOMDocument.INSTANCES).setReadOnly(), c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // No functions.
    }
}
