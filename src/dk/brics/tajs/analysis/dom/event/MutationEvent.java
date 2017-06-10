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

package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMRegistry;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The MutationEvent interface provides specific contextual information
 * associated with Mutation events.
 */
public class MutationEvent {

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        PROTOTYPE = ObjectLabel.make(DOMObjects.MUTATION_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.MUTATION_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(Event.PROTOTYPE));

        // Multiplied object.
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "MutationEvent", Value.makeObject(INSTANCES));

        /*
         * Properties.
         */
        createDOMProperty(INSTANCES, "relatedNode", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(INSTANCES, "prevValue", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(INSTANCES, "newValue", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(INSTANCES, "attrName", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(INSTANCES, "attrChange", Value.makeAnyNumUInt().setReadOnly(), c);

        /*
         * Constants (attrChangeType).
         */
        createDOMProperty(PROTOTYPE, "MODIFICATION", Value.makeNum(1), c);
        createDOMProperty(PROTOTYPE, "ADDITION", Value.makeNum(2), c);
        createDOMProperty(PROTOTYPE, "REMOVAL", Value.makeNum(3), c);

        /*
         * Functions.
         */
        createDOMFunction(PROTOTYPE, DOMObjects.MUTATION_EVENT_INIT_MUTATION_EVENT, "initMutationEvent", 8, c);

        // Multiply Object
        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        // DOM Registry
        DOMRegistry.registerMutationEventLabel(INSTANCES);
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case MUTATION_EVENT_INIT_MUTATION_EVENT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 8, 8);
                /* Value typeArg =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value canBubble =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 1));
                /* Value cancelable =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 2));
                /* Value relatedNode =*/
                DOMConversion.toNode(FunctionCalls.readParameter(call, s, 3), c);
                /* Value prevValue =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 4), c);
                /* Value newValue =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 5), c);
                /* Value attrName =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 6), c);
                /* Value attrChange =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 7), c);
                return Value.makeUndef();
            }
            default:
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
        }
    }
}
