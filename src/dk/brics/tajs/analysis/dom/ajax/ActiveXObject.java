/*
 * Copyright 2009-2013 Aarhus University
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

package dk.brics.tajs.analysis.dom.ajax;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
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
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

public class ActiveXObject {

    public static ObjectLabel CONSTRUCTOR;
    public static ObjectLabel INSTANCES;
    public static ObjectLabel PROTOTYPE;

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.ACTIVE_X_OBJECT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.ACTIVE_X_OBJECT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.ACTIVE_X_OBJECT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // TODO modelling choice: ActiveXObject is IE only..
        // s.writeProperty(DOMWindow.WINDOW, "ActiveXObject", Value.makeObject(CONSTRUCTOR)); 

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Constants.
         */
        createDOMProperty(s, PROTOTYPE, "UNSENT", Value.makeNum(0));
        createDOMProperty(s, PROTOTYPE, "OPENED", Value.makeNum(1));
        createDOMProperty(s, PROTOTYPE, "HEADERS_RECEIVED", Value.makeNum(2));
        createDOMProperty(s, PROTOTYPE, "LOADING", Value.makeNum(3));
        createDOMProperty(s, PROTOTYPE, "DONE", Value.makeNum(4));

        /*
         * Properties.
         */
        createDOMProperty(s, INSTANCES, "readyState", Value.makeAnyNumUInt().setReadOnly());
        createDOMProperty(s, INSTANCES, "status", Value.makeAnyNumUInt().setReadOnly());
        createDOMProperty(s, INSTANCES, "statusText", Value.makeAnyStr().setReadOnly());

        if (Options.isReturnJSON()) {
            createDOMProperty(s, INSTANCES, "responseText", Value.makeJSONStr());
        } else {
            createDOMProperty(s, INSTANCES, "responseText", Value.makeAnyStr());
        }
        // TODO createDOMProperty(s, INSTANCES, "responseXML", Value.makeObject(DOMDocument.DOCUMENT).setReadOnly(), DOMSpec.LEVEL_2);

        /*
         * Functions.
         */
        createDOMFunction(s, PROTOTYPE, DOMObjects.ACTIVE_X_OBJECT_OPEN, "open", 5);
        createDOMFunction(s, PROTOTYPE, DOMObjects.ACTIVE_X_OBJECT_SEND, "send", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.ACTIVE_X_OBJECT_SET_REQUEST_HEADER, "setRequestHeader", 2);
        createDOMFunction(s, PROTOTYPE, DOMObjects.ACTIVE_X_OBJECT_ABORT, "abort", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.ACTIVE_X_OBJECT_GET_RESPONSE_HEADER, "getResponseHeader", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.ACTIVE_X_OBJECT_GET_ALL_RESPONSE_HEADERS, "getAllResponseHeaders", 0);

        // Multiply object
        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();
    }

    /*
     * Transfer functions
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case ACTIVE_X_OBJECT_OPEN: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 5);
                /* Value method =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value url =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }

            case ACTIVE_X_OBJECT_SET_REQUEST_HEADER: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value header =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value value =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }

            case ACTIVE_X_OBJECT_SEND: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 1);
                return Value.makeUndef();
            }

            case ACTIVE_X_OBJECT_ABORT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }

            case ACTIVE_X_OBJECT_GET_RESPONSE_HEADER: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value header =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeAnyStr();
            }

            case ACTIVE_X_OBJECT_GET_ALL_RESPONSE_HEADERS: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeAnyStr();
            }

            case ACTIVE_X_OBJECT_CONSTRUCTOR: {
                // TODO: Check if this is sound.
                return Value.makeObject(INSTANCES).joinUndef();
            }

            default: {
                throw new AnalysisException("Unknown Native Object: " + nativeObject);
            }
        }
    }
}
