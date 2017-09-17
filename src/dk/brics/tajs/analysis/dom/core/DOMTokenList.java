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
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
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

public class DOMTokenList {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.TOKENLIST_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.TOKENLIST_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.TOKENLIST_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "DOMTokenList", Value.makeObject(CONSTRUCTOR));

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
        createDOMFunction(PROTOTYPE, DOMObjects.TOKENLIST_ITEM, "item", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.TOKENLIST_CONTAINS, "contains", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.TOKENLIST_ADD, "add", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.TOKENLIST_REMOVE, "remove", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.TOKENLIST_REPLACE, "replace", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.TOKENLIST_SUPPORTS, "supports", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.TOKENLIST_TOGGLE, "toggle", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.TOKENLIST_ENTRIES, "entries", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.TOKENLIST_FOREACH, "forEach", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.TOKENLIST_KEYS, "keys", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.TOKENLIST_VALUES, "values", 1, c);

    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeobject, CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeobject) {
            case TOKENLIST_CONSTRUCTOR:
                Exceptions.throwTypeError(c);
                return Value.makeNone();
            case TOKENLIST_CONTAINS:
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                /* Value content =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyBool();
            case TOKENLIST_ADD:
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                /* Value content =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            case TOKENLIST_REMOVE:
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                /* Value content =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            case TOKENLIST_REPLACE:
                DOMFunctions.expectParameters(nativeobject, call, c, 2, 2);
                /* Value content =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            case TOKENLIST_SUPPORTS:
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                /* Value content =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyBool();
            case TOKENLIST_TOGGLE:
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                /* Value content =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyBool();
            /*
            case TOKENLIST_ENTRIES:
            case TOKENLIST_FOREACH:
            case TOKENLIST_KEYS:
            case TOKENLIST_VALUES:
            */
            case TOKENLIST_ITEM: {
                return DOMFunctions.makeAnyHTMLElement();
                //    throw new AnalysisLimitationException(call.getJSSourceNode(), "TOKENLIST_ITEM not supported: "
                //           + nativeobject);
            }
            default: {
                throw new AnalysisException("Unknown Native Object: " + nativeobject);
            }
        }
    }
}
