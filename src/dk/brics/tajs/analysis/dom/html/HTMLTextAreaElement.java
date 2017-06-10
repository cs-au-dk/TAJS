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

public class HTMLTextAreaElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.HTMLTEXTAREAELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.HTMLTEXTAREAELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.HTMLTEXTAREAELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "HTMLTextAreaElement", Value.makeObject(CONSTRUCTOR));

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
        createDOMProperty(INSTANCES, "form", Value.makeObject(HTMLFormElement.INSTANCES).setReadOnly(), c);
        createDOMProperty(INSTANCES, "accessKey", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "cols", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "disabled", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "readOnly", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "rows", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "tabIndex", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "type", Value.makeStr("textarea").setReadOnly(), c);
        createDOMProperty(INSTANCES, "value", Value.makeAnyStr(), c);

        // DOM Level 2
        createDOMProperty(INSTANCES, "defaultValue", Value.makeAnyStr(), c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // DOM Level 1
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLTEXTAREAELEMENT_BLUR, "blur", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLTEXTAREAELEMENT_FOCUS, "focus", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLTEXTAREAELEMENT_SELECT, "select", 0, c);
    }

    /**
     * Transfer functions
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        switch (nativeObject) {
            case HTMLTEXTAREAELEMENT_SELECT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case HTMLTEXTAREAELEMENT_BLUR: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case HTMLTEXTAREAELEMENT_FOCUS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
