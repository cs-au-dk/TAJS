/*
 * Copyright 2009-2018 Aarhus University
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

package dk.brics.tajs.analysis.dom.xpath;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.event.Event;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * A base interface that all views shall derive from.
 */
public class XPathResult {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();

        CONSTRUCTOR = ObjectLabel.make(DOMObjects.XPATH_RESULT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.XPATH_RESULT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.XPATH_RESULT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "XPathResult", Value.makeObject(CONSTRUCTOR));

        createDOMProperty(CONSTRUCTOR, "ANY_TYPE", Value.makeNum(0).setReadOnly(), c);
        createDOMProperty(CONSTRUCTOR, "NUMBER_TYPE", Value.makeNum(1).setReadOnly(), c);
        createDOMProperty(CONSTRUCTOR, "STRING_TYPE", Value.makeNum(2).setReadOnly(), c);
        createDOMProperty(CONSTRUCTOR, "BOOLEAN_TYPE", Value.makeNum(3).setReadOnly(), c);
        createDOMProperty(CONSTRUCTOR, "UNORDERED_NODE_ITERATOR_TYPE", Value.makeNum(4).setReadOnly(), c);
        createDOMProperty(CONSTRUCTOR, "ORDERED_NODE_ITERATOR_TYPE", Value.makeNum(5).setReadOnly(), c);
        createDOMProperty(CONSTRUCTOR, "UNORDERED_NODE_SNAPSHOT_TYPE", Value.makeNum(6).setReadOnly(), c);
        createDOMProperty(CONSTRUCTOR, "ORDERED_NODE_SNAPSHOT_TYPE", Value.makeNum(7).setReadOnly(), c);
        createDOMProperty(CONSTRUCTOR, "ANY_UNORDERED_NODE_TYPE", Value.makeNum(8).setReadOnly(), c);
        createDOMProperty(CONSTRUCTOR, "FIRST_ORDERED_NODE_TYPE", Value.makeNum(9).setReadOnly(), c);

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(Event.PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties
         */
        createDOMProperty(PROTOTYPE, "booleanValue", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "invalidIteratorState", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "numberValue", Value.makeNull().makeAnyNum(), c); // FIXME: supposed to be join? (github #502)
        createDOMProperty(PROTOTYPE, "resultType", Value.makeNull().makeAnyNumUInt(), c); // FIXME: supposed to be join? (github #502)
        createDOMProperty(PROTOTYPE, "singleNodeValue", Value.makeNull().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "snapshotLength", Value.makeAnyNumUInt().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "stringValue", Value.makeAnyStr().setReadOnly(), c);

        /*
         * Functions
         */
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            default:
                throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
        }
    }
}
