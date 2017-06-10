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
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisLimitationException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;

/**
 * The DOMConfiguration interface represents the configuration of a document and
 * maintains a table of recognized parameters.
 * <p>
 * Introduced in DOM Level 3.
 */
public class DOMConfiguration {

    public static ObjectLabel INSTANCES;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel CONSTRUCTOR;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.CONFIGURATION_CONSTRUCTOR, Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.CONFIGURATION_PROTOTYPE, Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.CONFIGURATION_INSTANCES, Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "Configuration", Value.makeObject(CONSTRUCTOR));

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object.
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // DOM Level 3
        createDOMFunction(PROTOTYPE, DOMObjects.CONFIGURATION_SET_PARAMETER, "setParameter", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.CONFIGURATION_GET_PARAMETER, "getParameter", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.CONFIGURATION_CAN_SET_PARAMETER, "canSetParameter", 2, c);
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case CONFIGURATION_CAN_SET_PARAMETER: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyBool();
            }
            case CONFIGURATION_GET_PARAMETER: {
                throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getJSSourceNode().getSourceLocation() + ": CONFIGURATION_GET_PARAMETER not supported");
            }
            case CONFIGURATION_SET_PARAMETER: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case CONFIGURATION_CONSTRUCTOR:
                throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getJSSourceNode().getSourceLocation() + ": Unimplemented native function: " + nativeObject);
            default: {
                throw new UnsupportedOperationException("Unsupported Native Object " + nativeObject);
            }
        }
    }
}
