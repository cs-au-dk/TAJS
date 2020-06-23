/*
 * Copyright 2009-2020 Aarhus University
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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The UIEvent interface provides specific contextual information associated
 * with User Interface events.
 */
public class UIEvent {

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PROTOTYPE = ObjectLabel.make(DOMObjects.UI_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.UI_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(Event.PROTOTYPE));

        // Multiplied object.
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(PROTOTYPE, "detail", Value.makeAnyNum().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "view", Value.makeObject(DOMWindow.WINDOW).setReadOnly(), c);

        /*
         * Functions.
         */
        createDOMFunction(PROTOTYPE, DOMObjects.UI_EVENT_INIT_UI_EVENT, "initUIEvent", 5, c);
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case UI_EVENT_INIT_UI_EVENT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 5, 5);
                /* Value typeArg =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value canBubble =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 1));
                /* Value cancelableArg =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 2));
                // View arg not modelled...
                /* Value detailArg =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 4), c);
                return Value.makeUndef();
            }
            default:
                throw new UnsupportedOperationException("Unsupported Native Object: "
                        + nativeObject);
        }
    }
}
