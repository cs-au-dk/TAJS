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
 * The anchor element. See the A element definition in HTML 4.01
 */
public class HTMLAnchorElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.HTMLANCHORELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        INSTANCES = ObjectLabel.make(DOMObjects.HTMLANCHORELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);
        PROTOTYPE = ObjectLabel.make(DOMObjects.HTMLANCHORELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "HTMLAnchorElement", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        // DOM LEVEL 1
        createDOMProperty(INSTANCES, "accessKey", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "charset", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "coords", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "hash", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "host", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "hostname", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "href", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "hreflang", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "rel", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "rev", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "shape", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "tabIndex", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "target", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "pathname", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "port", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "protocol", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "search", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "type", Value.makeAnyStr().restrictToNotStrIdentifierParts() /* mime-type */, c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // DOM LEVEL 1
        // TODO: Remove, not present on this object, but on HTMLElement
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLANCHORELEMENT_BLUR, "blur", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLANCHORELEMENT_FOCUS, "focus", 0, c);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        switch (nativeObject) {
            case HTMLANCHORELEMENT_BLUR: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case HTMLANCHORELEMENT_FOCUS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
