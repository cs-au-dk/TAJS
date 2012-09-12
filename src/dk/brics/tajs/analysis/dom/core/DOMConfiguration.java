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

package dk.brics.tajs.analysis.dom.core;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.lattice.ObjectLabel.Kind;

/**
 * The DOMConfiguration interface represents the configuration of a document and
 * maintains a table of recognized parameters.
 * <p/>
 * Introduced in DOM Level 3.
 */
public class DOMConfiguration {

    public static ObjectLabel INSTANCES;
    public static ObjectLabel PROTOTYPE;
    public static ObjectLabel CONSTRUCTOR;

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.CONFIGURATION_CONSTRUCTOR, Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.CONFIGURATION_PROTOTYPE, Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.CONFIGURATION_INSTANCES, Kind.FUNCTION);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "Configuration", Value.makeObject(CONSTRUCTOR));

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object.
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // DOM Level 3
        createDOMFunction(s, PROTOTYPE, DOMObjects.CONFIGURATION_SET_PARAMETER, "setParameter", 2);
        createDOMFunction(s, PROTOTYPE, DOMObjects.CONFIGURATION_GET_PARAMETER, "getParameter", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.CONFIGURATION_CAN_SET_PARAMETER, "canSetParameter", 2);

    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case CONFIGURATION_CAN_SET_PARAMETER: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value name =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeAnyBool();
            }
            case CONFIGURATION_GET_PARAMETER: {
                throw new UnsupportedOperationException("CONFIGURATION_GET_PARAMETER not supported");
            }
            case CONFIGURATION_SET_PARAMETER: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value name =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            default: {
                throw new UnsupportedOperationException("Unsupported Native Object " + nativeObject);
            }
        }
    }
}
