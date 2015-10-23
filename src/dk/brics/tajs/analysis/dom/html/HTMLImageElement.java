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

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
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

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLIMAGEELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        INSTANCES = new ObjectLabel(DOMObjects.HTMLIMAGEELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);
        PROTOTYPE = new ObjectLabel(DOMObjects.HTMLIMAGEELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "HTMLImageElement", Value.makeObject(CONSTRUCTOR));

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
        createDOMProperty(s, INSTANCES, "complete", Value.makeAnyBool());

        // DOM Level 1
        createDOMProperty(s, INSTANCES, "lowSrc", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "name", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "align", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "alt", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "border", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "isMap", Value.makeAnyBool());
        createDOMProperty(s, INSTANCES, "longDesc", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "src", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "useMap", Value.makeAnyStr());

        // DOM Level 2
        createDOMProperty(s, INSTANCES, "height", Value.makeAnyNum());
        createDOMProperty(s, INSTANCES, "hspace", Value.makeAnyNum());
        createDOMProperty(s, INSTANCES, "vspace", Value.makeAnyNum());
        createDOMProperty(s, INSTANCES, "width", Value.makeAnyNum());

        // HTML5
        createDOMProperty(s, INSTANCES, "naturalWidth", Value.makeAnyNumUInt().setReadOnly());
        createDOMProperty(s, INSTANCES, "naturalHeight", Value.makeAnyNumUInt().setReadOnly());

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        // DOM LEVEL 0
        createDOMProperty(s, DOMWindow.WINDOW, "Image", Value.makeObject(CONSTRUCTOR));

        /*
         * Functions.
         */
        // No functions.
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case HTMLIMAGEELEMENT_CONSTRUCTOR: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeObject(INSTANCES);
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
