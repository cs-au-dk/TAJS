/*
 * Copyright 2009-2020 Aarhus University
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

package dk.brics.tajs.analysis.dom.html;

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
 * The create* and delete* methods on the table allow authors to construct and
 * modify tables. [HTML 4.01] specifies that only one of each of the CAPTION,
 * THEAD, and TFOOT elements may exist in a table. Therefore, if one exists, and
 * the createTHead() or createTFoot() method is called, the method returns the
 * existing THead or TFoot element. See the TABLE element definition in HTML
 * 4.01.
 */
public class HTMLTableElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.HTMLTABLEELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.HTMLTABLEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.HTMLTABLEELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "HTMLTableElement", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        // DOM Level 1
        createDOMProperty(INSTANCES, "rows", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly(), c);
        createDOMProperty(INSTANCES, "tBodies", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly(), c);
        createDOMProperty(INSTANCES, "align", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "bgColor", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "border", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "cellPadding", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "cellSpacing", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "frame", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "rules", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "summary", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "width", Value.makeAnyStr(), c);

        // DOM Level 2
        createDOMProperty(INSTANCES, "caption", Value.makeObject(HTMLTableCaptionElement.INSTANCES), c);
        createDOMProperty(INSTANCES, "tHead", Value.makeObject(HTMLTableSectionElement.INSTANCES), c);
        createDOMProperty(INSTANCES, "tFoot", Value.makeObject(HTMLTableSectionElement.INSTANCES), c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // DOM Level 1
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_CREATETHEAD, "createTHead", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_DELETETHEAD, "deleteTHead", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_CREATETFOOT, "createTFoot", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_DELETETFOOT, "deleteTFoot", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_CREATECAPTION, "createCaption", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_DELETECAPTION, "deleteCaption", 0, c);

        // DOM Level 2
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_INSERTROW, "insertRow", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLTABLEELEMENT_DELETEROW, "deleteRow", 1, c);
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case HTMLTABLEELEMENT_CREATETHEAD:
            case HTMLTABLEELEMENT_CREATETFOOT:
            case HTMLTABLEELEMENT_CREATECAPTION: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE);
            }
            case HTMLTABLEELEMENT_DELETETHEAD:
            case HTMLTABLEELEMENT_DELETETFOOT:
            case HTMLTABLEELEMENT_DELETECAPTION: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case HTMLTABLEELEMENT_INSERTROW: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value index =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE);
            }
            case HTMLTABLEELEMENT_DELETEROW: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value index =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object " + nativeObject);
            }
        }
    }
}
