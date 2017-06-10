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

package dk.brics.tajs.analysis.dom.core;

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
 * The CharacterData interface extends Node with a set of attributes and methods
 * for accessing character data in the DOM. For clarity this set is defined here
 * rather than on each object that uses these attributes and methods. No DOM
 * objects correspond directly to CharacterData, though Text and others do
 * inherit the interface from it. All offsets in this interface start from 0.
 */
public class DOMCharacterData {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.CHARACTERDATA_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.CHARACTERDATA_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.CHARACTERDATA_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "CharacterData", Value.makeObject(CONSTRUCTOR));

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(DOMNode.PROTOTYPE));

        // Multiplied object.
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        // DOM Level 1
        createDOMProperty(INSTANCES, "data", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "length", Value.makeAnyNumUInt(), c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // DOM Level 1
        createDOMFunction(PROTOTYPE, DOMObjects.CHARACTERDATA_SUBSTRINGDATA, "substringData", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.CHARACTERDATA_APPENDDATA, "appendData", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.CHARACTERDATA_INSERTDATA, "insertData", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.CHARACTERDATA_DELETEDATA, "deleteData", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.CHARACTERDATA_REPLACEDATA, "replaceData", 3, c);
    }

    public static Value evaluate(DOMObjects nativeobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeobject) {
            case CHARACTERDATA_APPENDDATA: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                /* Value arg =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case CHARACTERDATA_DELETEDATA: {
                DOMFunctions.expectParameters(nativeobject, call, c, 2, 2);
                /* Value offset =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value count =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case CHARACTERDATA_REPLACEDATA: {
                DOMFunctions.expectParameters(nativeobject, call, c, 3, 3);
                /* Value offset =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value count =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value arg =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 2), c);
                return Value.makeUndef();
            }
            case CHARACTERDATA_SUBSTRINGDATA: {
                DOMFunctions.expectParameters(nativeobject, call, c, 2, 2);
                /* Value offset =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value count =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeAnyStr();
            }
            case CHARACTERDATA_INSERTDATA: {
                DOMFunctions.expectParameters(nativeobject, call, c, 2, 2);
                /* Value offset =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value arg =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            default:
                throw new AnalysisException("Unsupported Native Object: " + nativeobject);
        }
    }
}
