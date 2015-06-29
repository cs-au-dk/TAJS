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

package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The Event interface is used to provide contextual information about an event
 * to the handler processing the event. An object which implements the Event
 * interface is generally passed as the first parameter to an event handler.
 * More specific context information is passed to event handlers by deriving
 * additional interfaces from Event which contain information directly relating
 * to the type of event they accompany. These derived interfaces are also
 * implemented by the object passed to the event listener.
 */
public class Event {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.EVENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "Event", Value.makeObject(CONSTRUCTOR));

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(s, PROTOTYPE, "type", Value.makeAnyStr().setReadOnly());
        createDOMProperty(s, PROTOTYPE, "target", DOMFunctions.makeAnyHTMLElement().setReadOnly());
        createDOMProperty(s, PROTOTYPE, "currentTarget", DOMFunctions.makeAnyHTMLElement().setReadOnly());
        createDOMProperty(s, PROTOTYPE, "eventPhase", Value.makeAnyNumUInt().setReadOnly());
        createDOMProperty(s, PROTOTYPE, "bubbles", Value.makeAnyBool().setReadOnly());
        createDOMProperty(s, PROTOTYPE, "cancelable", Value.makeAnyBool().setReadOnly());
        createDOMProperty(s, PROTOTYPE, "timeStamp", Value.makeAnyNumUInt().setReadOnly());

        // DOM LEVEL 0
        createDOMProperty(s, PROTOTYPE, "pageX", Value.makeAnyNumUInt());
        createDOMProperty(s, PROTOTYPE, "pageY", Value.makeAnyNumUInt());

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Constants (PhaseType).
         */
        createDOMProperty(s, PROTOTYPE, "CAPTURING_PHASE", Value.makeNum(1));
        createDOMProperty(s, PROTOTYPE, "AT_TARGET", Value.makeNum(2));
        createDOMProperty(s, PROTOTYPE, "BUBBLING_PHASE", Value.makeNum(3));

        /*
         * Functions.
         */
        createDOMFunction(s, PROTOTYPE, DOMObjects.EVENT_STOP_PROPAGATION, "stopPropagation", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.EVENT_PREVENT_DEFAULT, "preventDefault", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.EVENT_INIT_EVENT, "initEvent", 3);
    }

    /*
     * Transfer function.
     */

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case EVENT_INIT_EVENT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 3, 3);
                /* Value eventType =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value canBubble =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 0));
                /* Value cancelable =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 0));
                return Value.makeUndef();
            }
            case EVENT_PREVENT_DEFAULT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case EVENT_STOP_PROPAGATION: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
