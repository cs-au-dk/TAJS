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

package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class HTMLSelectElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLSELECTELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.HTMLSELECTELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.HTMLSELECTELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "HTMLSelectElement", Value.makeObject(CONSTRUCTOR));

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
        createDOMProperty(INSTANCES, "type", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(INSTANCES, "selectedIndex", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "value", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "form", Value.makeObject(HTMLFormElement.INSTANCES).setReadOnly(), c);
        createDOMProperty(INSTANCES, "disabled", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "multiple", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "name", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "size", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "tabIndex", Value.makeAnyNum(), c);

        // DOM Level 2
        createDOMProperty(INSTANCES, "length", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "options", Value.makeObject(HTMLOptionsCollection.INSTANCES).setReadOnly(), c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // DOM Level 1
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLSELECTELEMENT_ADD, "add", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLSELECTELEMENT_REMOVE, "remove", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLSELECTELEMENT_BLUR, "blur", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLSELECTELEMENT_FOCUS, "focus", 0, c);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case HTMLSELECTELEMENT_ADD: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value element =*/
                DOMConversion.toHTMLElement(NativeFunctions.readParameter(call, s, 0), c);
                /* Value before =*/
                DOMConversion.toHTMLElement(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case HTMLSELECTELEMENT_REMOVE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value index =*/
                Conversion.toInteger(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case HTMLSELECTELEMENT_BLUR: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case HTMLSELECTELEMENT_FOCUS: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
