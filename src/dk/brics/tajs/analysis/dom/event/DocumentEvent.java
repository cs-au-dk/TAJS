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

package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;

/**
 * The DocumentEvent interface provides a mechanism by which the user can create
 * an Event of a type supported by the implementation.
 * <p>
 * Simply augments the DOMDocument object.
 */
public class DocumentEvent {

    public static void build(State s) {

        /*
         * Properties.
         */
        // No properties.

        /*
         * Functions.
         */
        createDOMFunction(s, DOMDocument.PROTOTYPE, DOMObjects.DOCUMENT_EVENT_CREATE_EVENT, "createEvent", 1);
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case DOCUMENT_EVENT_CREATE_EVENT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value eventType =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeObject(Event.INSTANCES);
            }
            default:
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
        }
    }
}
