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

package dk.brics.tajs.analysis.dom.ajax;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMRegistry;
import dk.brics.tajs.analysis.dom.event.Event;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * To dispatch a readystatechange event means that an event with the name readystatechange, which does not bubble and is
 * not cancelable, and which uses the Event interface, is to be dispatched at the XMLHttpRequest object.
 */
public class ReadystateEvent {

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PROTOTYPE = ObjectLabel.make(DOMObjects.READY_STATE_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.READY_STATE_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(Event.PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(INSTANCES, "cancelable", Value.makeBool(false).setReadOnly(), c);
        createDOMProperty(INSTANCES, "bubbles", Value.makeBool(false).setReadOnly(), c);
        createDOMProperty(INSTANCES, "target", Value.makeObject(XmlHttpRequest.INSTANCES).setReadOnly(), c); // TODO: bound to a specific XMLHttpRequest object...
        createDOMProperty(INSTANCES, "type", Value.makeStr("readystatechange").setReadOnly(), c);

        /*
         *  ResponseText + ResponseXML (TODO)
         */
//TODO:        createDOMProperty(INSTANCES, "responseText", Value.makeJSONStr(), DOMSpec.LEVEL_0);

        // DOM Registry
        DOMRegistry.registerAjaxEventLabel(INSTANCES);
    }
}
