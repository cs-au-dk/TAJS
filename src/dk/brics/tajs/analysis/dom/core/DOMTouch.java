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

package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The Touch interface represents a single contact point on a touch-sensitive device.
 */
public class DOMTouch {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.TOUCH_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.TOUCH_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.TOUCH_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "Touch", Value.makeObject(CONSTRUCTOR));

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

         /*
         * Properties.
         */
        createDOMProperty(PROTOTYPE, "identifier", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "screenX", Value.makeAnyNum().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "screenY", Value.makeAnyNum().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "clientX", Value.makeAnyNum().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "clientY", Value.makeAnyNum().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "pageX", Value.makeAnyNum().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "pageY", Value.makeAnyNum().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "radiusX", Value.makeAnyNum().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "radiusY", Value.makeAnyNum().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "rotationAngle", Value.makeAnyNum().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "force", Value.makeAnyNum().setReadOnly(), c);
        
        INSTANCES = INSTANCES.makeSingleton().makeSummary();
    }

}
