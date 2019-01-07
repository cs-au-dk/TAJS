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
import dk.brics.tajs.util.AnalysisException;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.util.Collections.newSet;

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

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.EVENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "Event", Value.makeObject(CONSTRUCTOR));

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        Set<String> eventTypes = newSet();
        eventTypes.add("unload");
        eventTypes.add("click");
        eventTypes.add("dblclick");
        eventTypes.add("focus");
        eventTypes.add("focusin");
        eventTypes.add("focusout");
        eventTypes.add("blur");
        eventTypes.add("submit");
        eventTypes.add("reset");
        eventTypes.add("select");
        eventTypes.add("selectstart");
        eventTypes.add("selectend");
        eventTypes.add("change");
        eventTypes.add("resize");
        eventTypes.add("orientationchange");

        // TODO these belong on the event instances, and not on the prototype...
        createDOMProperty(PROTOTYPE, "type", Value.makeAnyStrIdent().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "eventPhase", Value.makeAnyNumUInt().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "bubbles", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "cancelable", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "timeStamp", Value.makeAnyNumUInt().joinAnyNumOther().setReadOnly(), c);

        // DOM LEVEL 0
        createDOMProperty(PROTOTYPE, "pageX", Value.makeAnyNumUInt().joinAbsent(), c);
        createDOMProperty(PROTOTYPE, "pageY", Value.makeAnyNumUInt().joinAbsent(), c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Constants (PhaseType).
         */
        createDOMProperty(PROTOTYPE, "CAPTURING_PHASE", Value.makeNum(1), c);
        createDOMProperty(PROTOTYPE, "AT_TARGET", Value.makeNum(2), c);
        createDOMProperty(PROTOTYPE, "BUBBLING_PHASE", Value.makeNum(3), c);

        /*
         * Functions.
         */
        createDOMFunction(PROTOTYPE, DOMObjects.EVENT_STOP_IMMEDIATE_PROPAGATION, "stopImmediatePropagation", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.EVENT_STOP_PROPAGATION, "stopPropagation", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.EVENT_PREVENT_DEFAULT, "preventDefault", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.EVENT_INIT_EVENT, "initEvent", 3, c);
    }

    /*
     * Transfer function.
     */

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case EVENT_CONSTRUCTOR: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Value event_name = FunctionCalls.readParameter(call, s, 0);
            /* Value useCapture =*/
                return Value.makeObject(INSTANCES);
            }
            case EVENT_INIT_EVENT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 3, 3);
                /* Value eventType =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value canBubble =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 0));
                /* Value cancelable =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 0));
                return Value.makeUndef();
            }
            case EVENT_PREVENT_DEFAULT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case EVENT_STOP_IMMEDIATE_PROPAGATION: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case EVENT_STOP_PROPAGATION: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
