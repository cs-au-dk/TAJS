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
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The THEAD, TFOOT, and TBODY elements.
 */
public class HTMLTableSectionElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLTABLESECTIONELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.HTMLTABLESECTIONELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.HTMLTABLESECTIONELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "HTMLTableSectionElement", Value.makeObject(CONSTRUCTOR));

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
        createDOMProperty(s, INSTANCES, "align", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "ch", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "chOff", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "vAlign", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "rows", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly());

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // DOM Level 2
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTABLESECTIONELEMENT_INSERTROW, "insertRow", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLTABLESECTIONELEMENT_DELETEROW, "deleteRow", 1);
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case HTMLTABLESECTIONELEMENT_INSERTROW: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value index =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE);
            }
            case HTMLTABLESECTIONELEMENT_DELETEROW: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value index =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
