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

package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMRegistry;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.util.Collections.newSet;

public class LoadEvent {

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PROTOTYPE = ObjectLabel.make(DOMObjects.LOAD_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.LOAD_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(Event.PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        Set<String> loadEventTypes = newSet();
        loadEventTypes.add("DOMContentLoaded");
        loadEventTypes.add("load");

        createDOMProperty(INSTANCES, "type", Value.makeStrings(loadEventTypes).setReadOnly(), c);
        createDOMProperty(INSTANCES, "bubbles", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(INSTANCES, "cancelable", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(INSTANCES, "detail", Value.makeAnyNum().setReadOnly(), c);
        createDOMProperty(INSTANCES, "view", Value.makeObject(DOMWindow.WINDOW).setReadOnly().joinAbsent(), c);

        // DOM Registry
        DOMRegistry.registerDOMContentLoadedEventLabel(INSTANCES);
        DOMRegistry.registerLoadEventLabel(INSTANCES);
    }
}
