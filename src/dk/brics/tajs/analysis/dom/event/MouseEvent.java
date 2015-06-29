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
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMRegistry;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The MouseEvent interface provides specific contextual information associated
 * with Mouse events.
 */
public class MouseEvent {

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(State s) {
        PROTOTYPE = new ObjectLabel(DOMObjects.MOUSE_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.MOUSE_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(UIEvent.PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(s, INSTANCES, "screenX", Value.makeAnyNum().setReadOnly());
        createDOMProperty(s, INSTANCES, "screenY", Value.makeAnyNum().setReadOnly());
        createDOMProperty(s, INSTANCES, "clientX", Value.makeAnyNum().setReadOnly());
        createDOMProperty(s, INSTANCES, "clientY", Value.makeAnyNum().setReadOnly());
        createDOMProperty(s, INSTANCES, "ctrlKey", Value.makeAnyBool().setReadOnly());
        createDOMProperty(s, INSTANCES, "shiftKey", Value.makeAnyBool().setReadOnly());
        createDOMProperty(s, INSTANCES, "altKey", Value.makeAnyBool().setReadOnly());
        createDOMProperty(s, INSTANCES, "metaKey", Value.makeAnyBool().setReadOnly());
        createDOMProperty(s, INSTANCES, "button", Value.makeAnyNumUInt().setReadOnly());
        createDOMProperty(s, INSTANCES, "relatedTarget", DOMFunctions.makeAnyHTMLElement().setReadOnly());

        // DOM Level 0
        createDOMProperty(s, INSTANCES, "offsetX", Value.makeAnyNum().setReadOnly());
        createDOMProperty(s, INSTANCES, "offsetY", Value.makeAnyNum().setReadOnly());
        createDOMProperty(s, INSTANCES, "layerX", Value.makeAnyNum().setReadOnly());
        createDOMProperty(s, INSTANCES, "layerY", Value.makeAnyNum().setReadOnly());

        /*
        * Functions.
        */
        createDOMFunction(s, PROTOTYPE, DOMObjects.MOUSE_EVENT_INIT_MOUSE_EVENT, "initMouseEvent", 15);

        // Multiplied Object
        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        // DOM Registry
        DOMRegistry.registerMouseEventLabel(INSTANCES);
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case MOUSE_EVENT_INIT_MOUSE_EVENT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 15, 15);
                /* Value typeArg =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value canBubbleArg =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 1));
                /* Value cancelableArg =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 2));
                // View arg not checked
                /* Value detailArg =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 4), c);
                /* Value screenXArg =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 5), c);
                /* Value screenYArg =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 6), c);
                /* Value clientXArg =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 7), c);
                /* Value clientYArg =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 8), c);
                /* Value ctrlKeyArg =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 9));
                /* Value altKeyArg =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 10));
                /* Value shiftKeyArg =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 11));
                /* Value metaKeyArg =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 12));
                /* Value buttonArg =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 13), c);
                /* Value relatedTargetArg =*/
                DOMConversion.toEventTarget(NativeFunctions.readParameter(call, s, 14), c);
                return Value.makeUndef();
            }
            default:
                throw new UnsupportedOperationException("Unsupported Native Object: "
                        + nativeObject);
        }
    }
}
