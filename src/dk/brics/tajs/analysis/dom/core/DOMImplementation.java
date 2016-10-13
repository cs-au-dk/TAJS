/*
 * Copyright 2009-2016 Aarhus University
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
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The DOMImplementation interface provides a number of methods for performing
 * operations that are independent of any particular instance of the document
 * object model.
 */
public class DOMImplementation {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = new ObjectLabel(DOMObjects.DOMIMPLEMENTATION_CONSTRUCTOR, Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.DOMIMPLEMENTATION_PROTOTYPE, Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.DOMIMPLEMENTATION_INSTANCES, Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "DOMImplementation", Value.makeObject(CONSTRUCTOR));

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object.
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));
        // s.writeProperty(DOMDocument.INSTANCES, "implementation", Value.makeObject(INSTANCES)); // TODO: should be here??
        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Properties.
         */
        // None

        /*
         * Functions.
         */
        // DOM Level 1
        createDOMFunction(PROTOTYPE, DOMObjects.DOMIMPLEMENTATION_HASFEATURE, "hasFeature", 2, c);

        // DOM Level 2
        createDOMFunction(PROTOTYPE, DOMObjects.DOMIMPLEMENTATION_CREATEDOCUMENTTYPE, "createDocumentType", 3, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOMIMPLEMENTATION_CREATEDOCUMENT, "createDocument", 3, c);

        createDOMProperty(DOMDocument.INSTANCES, "implementation", Value.makeObject(INSTANCES), c);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeobject) {
            case DOMIMPLEMENTATION_HASFEATURE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
                /* Value feature =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value version =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeAnyBool();
            }
            case DOMIMPLEMENTATION_CREATEDOCUMENTTYPE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 3, 3);
                /* Value qualifiedName =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value publicId =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                /* Value systemId =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 2), c);
                return Value.makeObject(DOMDocumentType.INSTANCES);
            }
            case DOMIMPLEMENTATION_CREATEDOCUMENT: {
                NativeFunctions.expectParameters(nativeobject, call, c, 3, 3);
                /* Value namespaceURI =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value qualifiedName =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                /* Value docType =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 2), c);
                return Value.makeObject(DOMDocument.INSTANCES);
            }
            default:
                throw new AnalysisException("Unknown Native Object: " + nativeobject);
        }
    }
}
