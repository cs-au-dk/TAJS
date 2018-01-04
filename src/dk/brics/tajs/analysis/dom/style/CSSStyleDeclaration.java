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

package dk.brics.tajs.analysis.dom.style;

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
import dk.brics.tajs.solver.Message;

import java.util.Collections;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;

public class CSSStyleDeclaration {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();

        CONSTRUCTOR = ObjectLabel.make(DOMObjects.CSSSTYLEDECLARATION_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.CSSSTYLEDECLARATION_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.CSSSTYLEDECLARATION_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "CSSStyleDeclaration", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties
         */
        pv.writeProperty(Collections.singleton(INSTANCES), Value.makeAnyStrNotUInt(), Value.makeAnyStr(), false, true);

        /*
         * Functions.
         */
        createDOMFunction(PROTOTYPE, DOMObjects.CSSSTYLEDECLARATION_GETPROPERTYVALUE, "getPropertyValue", 1, c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        switch (nativeobject) {
            case CSSSTYLEDECLARATION_GETPROPERTYVALUE: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                return Value.makeAnyStr();
            }
            default: {
                c.getMonitoring().addMessage(call.getSourceNode(), Message.Severity.TAJS_ERROR, "Unsupported operation: " + nativeobject);
                return Value.makeNone();
            }
        }
    }
}
