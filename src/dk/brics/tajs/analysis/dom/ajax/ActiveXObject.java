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

package dk.brics.tajs.analysis.dom.ajax;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class ActiveXObject {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel INSTANCES;

    public static ObjectLabel PROTOTYPE;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.ACTIVE_X_OBJECT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.ACTIVE_X_OBJECT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.ACTIVE_X_OBJECT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // TODO modelling choice: ActiveXObject is IE only..
        // pv.writeProperty(DOMWindow.WINDOW, "ActiveXObject", Value.makeObject(CONSTRUCTOR));

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Constants.
         */
        createDOMProperty(PROTOTYPE, "UNSENT", Value.makeNum(0), c);
        createDOMProperty(PROTOTYPE, "OPENED", Value.makeNum(1), c);
        createDOMProperty(PROTOTYPE, "HEADERS_RECEIVED", Value.makeNum(2), c);
        createDOMProperty(PROTOTYPE, "LOADING", Value.makeNum(3), c);
        createDOMProperty(PROTOTYPE, "DONE", Value.makeNum(4), c);

        /*
         * Properties.
         */
        createDOMProperty(INSTANCES, "readyState", Value.makeAnyNumUInt().setReadOnly(), c);
        createDOMProperty(INSTANCES, "status", Value.makeAnyNumUInt().setReadOnly(), c);
        createDOMProperty(INSTANCES, "statusText", Value.makeAnyStr().setReadOnly(), c);

        if (Options.get().isReturnJSON()) {
            createDOMProperty(INSTANCES, "responseText", Value.makeJSONStr(), c);
        } else {
            createDOMProperty(INSTANCES, "responseText", Value.makeAnyStr(), c);
        }
        // TODO createDOMProperty(INSTANCES, "responseXML", Value.makeObject(DOMDocument.DOCUMENT).setReadOnly(), DOMSpec.LEVEL_2);

        /*
         * Functions.
         */
        createDOMFunction(PROTOTYPE, DOMObjects.ACTIVE_X_OBJECT_OPEN, "open", 5, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ACTIVE_X_OBJECT_SEND, "send", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ACTIVE_X_OBJECT_SET_REQUEST_HEADER, "setRequestHeader", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ACTIVE_X_OBJECT_ABORT, "abort", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ACTIVE_X_OBJECT_GET_RESPONSE_HEADER, "getResponseHeader", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ACTIVE_X_OBJECT_GET_ALL_RESPONSE_HEADERS, "getAllResponseHeaders", 0, c);

        // Multiply object
        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();
    }

    /*
     * Transfer functions
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case ACTIVE_X_OBJECT_OPEN: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 5);
                /* Value method =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value url =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }

            case ACTIVE_X_OBJECT_SET_REQUEST_HEADER: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value header =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value value =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }

            case ACTIVE_X_OBJECT_SEND: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 1);
                return Value.makeUndef();
            }

            case ACTIVE_X_OBJECT_ABORT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }

            case ACTIVE_X_OBJECT_GET_RESPONSE_HEADER: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value header =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyStr();
            }

            case ACTIVE_X_OBJECT_GET_ALL_RESPONSE_HEADERS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeAnyStr();
            }

            case ACTIVE_X_OBJECT_CONSTRUCTOR: {
                // TODO: Check if this is sound.
                return Value.makeObject(INSTANCES).joinUndef();
            }

            default: {
                throw new AnalysisException(call.getJSSourceNode().getSourceLocation() + ": Unknown Native Object: " + nativeObject);
            }
        }
    }
}
