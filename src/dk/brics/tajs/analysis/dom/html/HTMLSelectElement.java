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
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class HTMLSelectElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLSELECTELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.HTMLSELECTELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.HTMLSELECTELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "HTMLSelectElement", Value.makeObject(CONSTRUCTOR));

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
        createDOMProperty(s, INSTANCES, "type", Value.makeAnyStr().setReadOnly());
        createDOMProperty(s, INSTANCES, "selectedIndex", Value.makeAnyNum());
        createDOMProperty(s, INSTANCES, "value", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "form", Value.makeObject(HTMLFormElement.INSTANCES).setReadOnly());
        createDOMProperty(s, INSTANCES, "disabled", Value.makeAnyBool());
        createDOMProperty(s, INSTANCES, "multiple", Value.makeAnyBool());
        createDOMProperty(s, INSTANCES, "name", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "size", Value.makeAnyNum());
        createDOMProperty(s, INSTANCES, "tabIndex", Value.makeAnyNum());

        // DOM Level 2
        createDOMProperty(s, INSTANCES, "length", Value.makeAnyNum());
        createDOMProperty(s, INSTANCES, "options", Value.makeObject(HTMLOptionsCollection.INSTANCES).setReadOnly());

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // DOM Level 1
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLSELECTELEMENT_ADD, "add", 2);
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLSELECTELEMENT_REMOVE, "remove", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLSELECTELEMENT_BLUR, "blur", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLSELECTELEMENT_FOCUS, "focus", 0);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
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
