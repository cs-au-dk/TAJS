/*
 * Copyright 2012 Aarhus University
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

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMRegistry;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class WheelEvent {

    public static ObjectLabel PROTOTYPE;
    public static ObjectLabel INSTANCES;

    public static void build(State s) {
        PROTOTYPE = new ObjectLabel(DOMObjects.WHEEL_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.WHEEL_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(MouseEvent.PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Constants.
         */
        createDOMProperty(s, PROTOTYPE, "DOM_DELTA_PIXEL", Value.makeNum(0));
        createDOMProperty(s, PROTOTYPE, "DOM_DELTA_LINE", Value.makeNum(1));
        createDOMProperty(s, PROTOTYPE, "DOM_DELTA_PAGE", Value.makeNum(2));

        /*
         * Properties.
         */
        createDOMProperty(s, INSTANCES, "deltaX", Value.makeAnyNum().setReadOnly());
        createDOMProperty(s, INSTANCES, "deltaY", Value.makeAnyNum().setReadOnly());
        createDOMProperty(s, INSTANCES, "deltaZ", Value.makeAnyNum().setReadOnly());
        createDOMProperty(s, INSTANCES, "deltaMode", Value.makeAnyNumUInt().setReadOnly());

        /*
         * Functions.
         */
        createDOMFunction(s, PROTOTYPE, DOMObjects.WHEEL_EVENT_INIT_WHEEL_EVENT, "initWheelEvent", 16);
        createDOMFunction(s, PROTOTYPE, DOMObjects.WHEEL_EVENT_INIT_WHEEL_EVENT_NS, "initWheelEventNS", 17);

        // Multiplied object
        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        // DOM Registry
        DOMRegistry.registerWheelEventLabel(INSTANCES);
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case WHEEL_EVENT_INIT_WHEEL_EVENT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 16, 16);
                /* Value typeArg =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value canBubbleArg =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 1));
                /* Value cancelableArg =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 2));
                // viewArg not checked
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
                /* Value buttonArg =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 9), c);
                /* Value relatedTargetArg =*/
                DOMConversion.toEventTarget(NativeFunctions.readParameter(call, s, 10), c);
                /* Value modifiersListArg =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 11), c);
                /* Value deltaXArg =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 12), c);
                /* Value deltaYArg =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 13), c);
                /* Value deltaZArg =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 14), c);
                /* Value deltaMode =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 15), c);
                return Value.makeUndef();
            }
            default: {
                throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
            }
        }
    }

}
