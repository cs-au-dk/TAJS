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

package dk.brics.tajs.analysis.dom.html5;

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

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * Each Storage object provides access to a list of key/value pairs,
 * which are sometimes called items. Keys and values are strings.
 * Any string (including the empty string) is a valid key.
 */
public class StorageElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.STORAGE_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.STORAGE_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.STORAGE_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "Storage", Value.makeObject(CONSTRUCTOR));

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(INSTANCES, "length", Value.makeAnyNumUInt().setReadOnly(), c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();
        
        /*
         * Functions.
         */
        createDOMFunction(PROTOTYPE, DOMObjects.STORAGE_KEY, "key", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.STORAGE_GET_ITEM, "getItem", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.STORAGE_SET_ITEM, "setItem", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.STORAGE_REMOVE_ITEM, "removeItem", 1, c);
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case STORAGE_KEY: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
             /* Value index =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyStr();
            }
            case STORAGE_GET_ITEM: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
             /* Value key =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyStr().joinNull();
            }
            case STORAGE_SET_ITEM: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
             /* Value key =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
             /* Value data =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case STORAGE_REMOVE_ITEM: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
             /* Value key =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
