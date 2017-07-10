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

package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.core.DOMElement;
import dk.brics.tajs.analysis.dom.core.DOMException;
import dk.brics.tajs.analysis.dom.core.DOMNamedNodeMap;
import dk.brics.tajs.analysis.dom.core.DOMStringMap;
import dk.brics.tajs.analysis.dom.style.CSSStyleDeclaration;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class HTMLElement {

    public static ObjectLabel ELEMENT;

    public static ObjectLabel ELEMENT_PROTOTYPE;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        ELEMENT = ObjectLabel.make(DOMObjects.HTMLELEMENT, ObjectLabel.Kind.OBJECT);
        ELEMENT_PROTOTYPE = ObjectLabel.make(DOMObjects.HTMLELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

        // Prototype Object
        s.newObject(ELEMENT_PROTOTYPE);
        s.writeInternalPrototype(ELEMENT_PROTOTYPE, Value.makeObject(DOMElement.PROTOTYPE));

        // Multiplied Object
        s.newObject(ELEMENT);
        s.writeInternalPrototype(ELEMENT, Value.makeObject(ELEMENT_PROTOTYPE));
        pv.writePropertyWithAttributes(ELEMENT, "length", Value.makeNum(0).setAttributes(true, true, true)); // FIXME: ? (GitHub #407)
        pv.writePropertyWithAttributes(ELEMENT, "prototype", Value.makeObject(ELEMENT_PROTOTYPE).setAttributes(true, true, true));
        pv.writeProperty(DOMWindow.WINDOW, "HTMLElement", Value.makeObject(ELEMENT));

        /*
         * Properties.
         */
        // DOM Level 1
        // Note: id attribute not set here.
        createDOMProperty(ELEMENT_PROTOTYPE, "title", Value.makeAnyStr(), c);
        createDOMProperty(ELEMENT_PROTOTYPE, "lang", Value.makeAnyStr(), c);
        createDOMProperty(ELEMENT_PROTOTYPE, "dir", Value.makeAnyStr(), c);
        createDOMProperty(ELEMENT_PROTOTYPE, "className", Value.makeAnyStr(), c);

        // DOM LEVEL 0
        createDOMProperty(ELEMENT_PROTOTYPE, "clientHeight", Value.makeAnyNumUInt(), c);
        createDOMProperty(ELEMENT_PROTOTYPE, "clientWidth", Value.makeAnyNumUInt(), c);

        // MSIE
        createDOMProperty(ELEMENT_PROTOTYPE, "offsetLeft", Value.makeAnyNum(), c);
        createDOMProperty(ELEMENT_PROTOTYPE, "offsetTop", Value.makeAnyNum(), c);
        createDOMProperty(ELEMENT_PROTOTYPE, "offsetHeight", Value.makeAnyNum(), c);
        createDOMProperty(ELEMENT_PROTOTYPE, "offsetWidth", Value.makeAnyNum(), c);

        // DOM LEVEL 0
        createDOMProperty(ELEMENT_PROTOTYPE, "attributes", Value.makeObject(DOMNamedNodeMap.INSTANCES), c);

        // Style
        createDOMProperty(ELEMENT_PROTOTYPE, "style", Value.makeObject(CSSStyleDeclaration.INSTANCES), c);

        s.multiplyObject(ELEMENT);
        ELEMENT = ELEMENT.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        createDOMFunction(ELEMENT_PROTOTYPE, DOMObjects.HTMLELEMENT_GET_ELEMENTS_BY_CLASS_NAME, "getElementsByClassName", 1, c);
        createDOMFunction(ELEMENT_PROTOTYPE, DOMObjects.HTMLELEMENT_BLUR, "blur", 1, c);
        createDOMFunction(ELEMENT_PROTOTYPE, DOMObjects.HTMLELEMENT_FOCUS, "focus", 1, c);
        createDOMFunction(ELEMENT_PROTOTYPE, DOMObjects.HTMLELEMENT_MATCHES, "matches", 1, c);


        // semistandard
        // NB: webkit version!
        createDOMFunction(ELEMENT_PROTOTYPE, DOMObjects.HTMLELEMENT_MATCHES_SELECTOR, "webkitMatchesSelector", 1, c);


        // Living version
        createDOMProperty(ELEMENT_PROTOTYPE, "dataset", Value.makeObject(DOMStringMap.INSTANCES), c);


        createDOMProperty(ELEMENT_PROTOTYPE, "tagName", Value.makeAnyStr(), c);
        createDOMProperty(ELEMENT_PROTOTYPE, "nodeName", Value.makeAnyStr(), c);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        switch (nativeObject) {
            case HTMLELEMENT_GET_ELEMENTS_BY_CLASS_NAME: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value className =*/
                Conversion.toString(FunctionCalls.readParameter(call, c.getState(), 0), c);
                return Value.makeObject(HTMLCollection.INSTANCES);
            }
            case HTMLELEMENT_MATCHES_SELECTOR: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                // may throw on bad syntax
                DOMException.throwException(call.getSourceNode(), c);
                return Value.makeAnyBool();
            }
            case HTMLELEMENT_MATCHES: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(FunctionCalls.readParameter(call, c.getState(), 0), c);
                return Value.makeAnyBool();
            }
            case HTMLELEMENT_BLUR: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case HTMLELEMENT_FOCUS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
