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

package dk.brics.tajs.analysis.dom.core;

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

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The DOMStringList interface provides the abstraction of an ordered collection
 * of DOMString values, without defining or constraining how this collection is
 * implemented.
 * <p>
 * Introduced in DOM Level 3.
 */
public class DOMStringList {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.STRINGLIST_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.STRINGLIST_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.STRINGLIST_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "StringList", Value.makeObject(CONSTRUCTOR));

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object.
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        // DOM Level 3
        createDOMProperty(s, INSTANCES, "length", Value.makeAnyNumUInt().setReadOnly());

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // DOM Level 3
        createDOMFunction(s, PROTOTYPE, DOMObjects.STRINGLIST_ITEM, "item", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.STRINGLIST_CONTAINS, "contains", 1);

        // DOM Level 3
        createDOMProperty(s, DOMConfiguration.INSTANCES, "parameterNames", Value.makeObject(PROTOTYPE));
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case STRINGLIST_ITEM: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value index =*/
                Conversion.toInteger(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeAnyStr().joinNull();
            }
            case STRINGLIST_CONTAINS: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value str =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeAnyBool();
            }
            default: {
                throw new UnsupportedOperationException("Unsupported Native Object " + nativeObject);
            }
        }
    }
}
