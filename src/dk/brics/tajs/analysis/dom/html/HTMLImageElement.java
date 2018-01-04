/*
 * Copyright 2009-2018 Aarhus University
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

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class HTMLImageElement {

    public static ObjectLabel INSTANCES;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel CONSTRUCTOR;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.HTMLIMAGEELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        INSTANCES = ObjectLabel.make(DOMObjects.HTMLIMAGEELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);
        PROTOTYPE = ObjectLabel.make(DOMObjects.HTMLIMAGEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "HTMLImageElement", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        // DOM LEVEL 0
        createDOMProperty(INSTANCES, "complete", Value.makeAnyBool(), c);

        // DOM Level 1
        createDOMProperty(INSTANCES, "lowSrc", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "align", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "alt", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "border", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "isMap", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "longDesc", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "src", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "useMap", Value.makeAnyStr(), c);

        // DOM Level 2
        createDOMProperty(INSTANCES, "height", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "hspace", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "vspace", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "width", Value.makeAnyNum(), c);

        // HTML5
        createDOMProperty(INSTANCES, "naturalWidth", Value.makeAnyNumUInt().setReadOnly(), c);
        createDOMProperty(INSTANCES, "naturalHeight", Value.makeAnyNumUInt().setReadOnly(), c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        // DOM LEVEL 0
        createDOMProperty(DOMWindow.WINDOW, "Image", Value.makeObject(CONSTRUCTOR), c);

        /*
         * Functions.
         */
        // No functions.
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        switch (nativeObject) {
            case HTMLIMAGEELEMENT_CONSTRUCTOR: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeObject(INSTANCES);
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
