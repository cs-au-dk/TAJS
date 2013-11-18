/*
 * Copyright 2009-2013 Aarhus University
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

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.core.DOMElement;
import dk.brics.tajs.analysis.dom.style.CSSStyleDeclaration;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;

public class HTMLElement {

    public static ObjectLabel ELEMENT;
    public static ObjectLabel ELEMENT_PROTOTYPE;
    public static ObjectLabel ELEMENT_ATTRIBUTES;

    public static void build(State s) {
        ELEMENT = new ObjectLabel(DOMObjects.HTMLELEMENT, ObjectLabel.Kind.OBJECT);
        ELEMENT_PROTOTYPE = new ObjectLabel(DOMObjects.HTMLELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        ELEMENT_ATTRIBUTES = new ObjectLabel(DOMObjects.HTMLELEMENT_ATTRIBUTES, ObjectLabel.Kind.OBJECT);

        // Prototype Object
        s.newObject(ELEMENT_PROTOTYPE);
        s.writeInternalPrototype(ELEMENT_PROTOTYPE, Value.makeObject(DOMElement.PROTOTYPE));

        // Multiplied Object
        s.newObject(ELEMENT);
        s.writeInternalPrototype(ELEMENT, Value.makeObject(ELEMENT_PROTOTYPE));
        s.writePropertyWithAttributes(ELEMENT, "length", Value.makeNum(0).setAttributes(true, true, true)); // FIXME: ?
        s.writePropertyWithAttributes(ELEMENT, "prototype", Value.makeObject(ELEMENT_PROTOTYPE).setAttributes(true, true, true));
        s.writeProperty(DOMWindow.WINDOW, "HTMLElement", Value.makeObject(ELEMENT));

        /*
         * Properties.
         */
        // DOM Level 1
        // Note: id attribute not set here.
        createDOMProperty(s, ELEMENT_PROTOTYPE, "title", Value.makeAnyStr());
        createDOMProperty(s, ELEMENT_PROTOTYPE, "lang", Value.makeAnyStr());
        createDOMProperty(s, ELEMENT_PROTOTYPE, "dir", Value.makeAnyStr());
        createDOMProperty(s, ELEMENT_PROTOTYPE, "className", Value.makeAnyStr());

        // DOM LEVEL 0
        createDOMProperty(s, ELEMENT_PROTOTYPE, "clientHeight", Value.makeAnyNumUInt());
        createDOMProperty(s, ELEMENT_PROTOTYPE, "clientWidth", Value.makeAnyNumUInt());

        // MSIE
        createDOMProperty(s, ELEMENT_PROTOTYPE, "offsetLeft", Value.makeAnyNumUInt());
        createDOMProperty(s, ELEMENT_PROTOTYPE, "offsetTop", Value.makeAnyNumUInt());
        createDOMProperty(s, ELEMENT_PROTOTYPE, "offsetParent", Value.makeAnyNumUInt());
        createDOMProperty(s, ELEMENT_PROTOTYPE, "offsetHeight", Value.makeAnyNumUInt());
        createDOMProperty(s, ELEMENT_PROTOTYPE, "offsetWidth", Value.makeAnyNumUInt());

        // DOM LEVEL 0
        s.newObject(ELEMENT_ATTRIBUTES);
        s.writeProperty(Collections.singleton(ELEMENT_ATTRIBUTES), Value.makeAnyStr(), Value.makeAnyStr(), true, false);
        s.writeInternalPrototype(ELEMENT_ATTRIBUTES, Value.makeNull()); // FIXME: null, really?
        s.multiplyObject(ELEMENT_ATTRIBUTES);
        ELEMENT_ATTRIBUTES = ELEMENT_ATTRIBUTES.makeSingleton().makeSummary();
        createDOMProperty(s, ELEMENT_PROTOTYPE, "attributes", Value.makeObject(ELEMENT_ATTRIBUTES));

        // Style
        createDOMProperty(s, ELEMENT_PROTOTYPE, "style", Value.makeObject(CSSStyleDeclaration.STYLEDECLARATION));

        s.multiplyObject(ELEMENT);
        ELEMENT = ELEMENT.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        createDOMFunction(s, ELEMENT_PROTOTYPE, DOMObjects.HTMLELEMENT_GET_ELEMENTS_BY_CLASS_NAME, "getElementsByClassName", 1);
        
        // semistandard
        // NB: webkit version!
        createDOMFunction(s, ELEMENT_PROTOTYPE, DOMObjects.HTMLELEMENT_MATCHES_SELECTOR, "webkitMatchesSelector", 1);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case HTMLELEMENT_GET_ELEMENTS_BY_CLASS_NAME: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value className =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return DOMFunctions.makeAnyHTMLNodeList(s);
            }
			case HTMLELEMENT_MATCHES_SELECTOR: {
				NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
				return Value.makeAnyBool();
			}
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }

}
