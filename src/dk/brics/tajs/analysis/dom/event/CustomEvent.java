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

package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
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
public class CustomEvent {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();

        CONSTRUCTOR = ObjectLabel.make(DOMObjects.CUSTOM_EVENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.CUSTOM_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.CUSTOM_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "CustomEvent", Value.makeObject(CONSTRUCTOR));

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(Event.PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties
         */
        createDOMProperty(PROTOTYPE, "detail", Value.makeNull().setReadOnly(), c);

        /*
         * Functions
         */
        createDOMFunction(PROTOTYPE, DOMObjects.CUSTOM_EVENT_INIT_CUSTOM_EVENT, "initCustomEvent", 5, c);
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case CUSTOM_EVENT_CONSTRUCTOR: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 2);

                /* Value typeArg */
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);

                /* Value customEventInitArg */
                // ???

                return Value.makeObject(INSTANCES);
            }
            case CUSTOM_EVENT_INIT_CUSTOM_EVENT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 4, 4);
                /* Value typeArg =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value canBubble =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 1));
                /* Value cancelableArg =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 2));
                /* Value detailArg =*/
                // ???
                return Value.makeUndef();
            }
            default:
                throw new UnsupportedOperationException("Unsupported Native Object: "
                        + nativeObject);
        }
    }
}
