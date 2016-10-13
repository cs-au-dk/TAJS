/*
 * Copyright 2009-2016 Aarhus University
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
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMRegistry;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The KeyboardEvent interface provides specific contextual information
 * associated with keyboard devices. Each keyboard event references a key using
 * an identifier. Keyboard events are commonly directed at the element that has
 * the focus.
 */
public class KeyboardEvent {

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PROTOTYPE = new ObjectLabel(DOMObjects.KEYBOARD_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.KEYBOARD_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(UIEvent.PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Constants (KeyLocation code)
         */
        createDOMProperty(PROTOTYPE, "DOM_KEY_LOCATION_STANDARD", Value.makeNum(0), c);
        createDOMProperty(PROTOTYPE, "DOM_KEY_LOCATION_LEFT", Value.makeNum(1), c);
        createDOMProperty(PROTOTYPE, "DOM_KEY_LOCATION_RIGHT", Value.makeNum(2), c);
        createDOMProperty(PROTOTYPE, "DOM_KEY_LOCATION_NUMPAD", Value.makeNum(3), c);
        createDOMProperty(PROTOTYPE, "DOM_KEY_LOCATION_MOBILE", Value.makeNum(4), c);
        createDOMProperty(PROTOTYPE, "DOM_KEY_LOCATION_JOYSTICK", Value.makeNum(5), c);

        /*
         * Properties
         */
        createDOMProperty(INSTANCES, "keyIdentifier", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(INSTANCES, "keyLocation", Value.makeAnyNumUInt().setReadOnly(), c);
        createDOMProperty(INSTANCES, "ctrlKey", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(INSTANCES, "shiftKey", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(INSTANCES, "altKey", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(INSTANCES, "metaKey", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(INSTANCES, "repeat", Value.makeAnyBool().setReadOnly(), c);

        // DOM LEVEL 0:
        createDOMProperty(PROTOTYPE, "charCode", Value.makeAnyNumUInt(), c);
        createDOMProperty(PROTOTYPE, "key", Value.makeAnyNumUInt(), c);
        createDOMProperty(PROTOTYPE, "keyCode", Value.makeAnyNumUInt(), c);

        /*
         * Functions
         */

        // DOM Level 3:
        createDOMFunction(PROTOTYPE, DOMObjects.KEYBOARD_EVENT_GET_MODIFIER_STATE, "getModifierState", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.KEYBOARD_EVENT_INIT_KEYBOARD_EVENT, "initKeyboardEvent", 7, c);
        createDOMFunction(PROTOTYPE, DOMObjects.KEYBOARD_EVENT_INIT_KEYBOARD_EVENT_NS, "initKeyboardEventNS", 8, c);

        // Multiplied object
        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        // DOM Registry
        DOMRegistry.registerKeyboardEventLabel(INSTANCES);
    }

    /*
     * Transfer functions
     */

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case KEYBOARD_EVENT_GET_MODIFIER_STATE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value keyIdentifierArg =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeAnyBool();
            }
            case KEYBOARD_EVENT_INIT_KEYBOARD_EVENT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 7, 7);
                /* Value typeArg =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value canBubbleArg =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 1));
                /* Value cancelableArg =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 2));
                // viewArg not checked...
                /* Value keyIdentifierArg =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 4), c);
                /* Value keyLocationArg =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 5), c);
                /* Value modifiersListArg =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 6), c);
                return Value.makeUndef();
            }
            case KEYBOARD_EVENT_INIT_KEYBOARD_EVENT_NS: {
                throw new UnsupportedOperationException("KeyboardEvent.initKeyboardEventNS not supported!");
            }
            default: {
                throw new UnsupportedOperationException("Unsupported Native Object");
            }
        }
    }
}
