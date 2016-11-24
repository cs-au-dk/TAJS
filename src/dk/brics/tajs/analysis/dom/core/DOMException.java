/*
 * Copyright 2009-2016 Aarhus University
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

package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * DOM operations only raise exceptions in "exceptional" circumstances, i.e.,
 * when an operation is impossible to perform (either for logical reasons,
 * because data is lost, or because the implementation has become unstable). In
 * general, DOM methods return specific error values in ordinary processing
 * situations, such as out-of-bound errors when using NodeList.
 * <p>
 * http://www.w3.org/TR/DOM-Level-2-Core/core.html#ID-17189187
 */
public class DOMException {

    private static ObjectLabel DOMEXCEPTION;

    private static ObjectLabel DOMEXCEPTION_PROTOTYPE;

    /**
     * Creates a DOMException.
     */
    public static Value newDOMException(int code, Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        s.newObject(DOMEXCEPTION);
        s.writeInternalPrototype(DOMEXCEPTION, Value.makeObject(DOMEXCEPTION_PROTOTYPE));
        pv.writeProperty(DOMEXCEPTION, "code", Value.makeNum(code));
        return Value.makeObject(DOMEXCEPTION);
    }

    public static void build(Solver.SolverInterface c) {
        DOMEXCEPTION = new ObjectLabel(DOMObjects.DOMEXCEPTION, Kind.OBJECT);
        DOMEXCEPTION_PROTOTYPE = new ObjectLabel(DOMObjects.DOMEXCEPTION_PROTOTYPE, Kind.OBJECT);

        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        s.newObject(DOMEXCEPTION_PROTOTYPE);
        s.writeInternalPrototype(DOMEXCEPTION_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "DOMException", Value.makeObject(DOMEXCEPTION_PROTOTYPE));

        /*
         * Properties.
         */
        // DOM Level 1
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "INDEX_SIZE_ERR", Value.makeNum(1), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "DOMSTRING_SIZE_ERR", Value.makeNum(2), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "HIERARCHY_REQUEST_ERR", Value.makeNum(3), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "WRONG_DOCUMENT_ERR", Value.makeNum(4), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "INVALID_CHARACTER_ERR", Value.makeNum(5), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "NO_DATA_ALLOWED_ERR", Value.makeNum(6), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "NO_MODIFICATION_ALLOWED_ERR", Value.makeNum(7), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "NOT_FOUND_ERR", Value.makeNum(8), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "NOT_SUPPORTED_ERR", Value.makeNum(9), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "INUSE_ATTRIBUTE_ERR", Value.makeNum(10), c);

        // DOM Level 2
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "INVALID_STATE_ERR", Value.makeNum(11), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "SYNTAX_ERR", Value.makeNum(12), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "INVALID_MODIFICATION_ERR", Value.makeNum(13), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "NAMESPACE_ERR", Value.makeNum(14), c);
        createDOMProperty(DOMEXCEPTION_PROTOTYPE, "INVALID_ACCESS_ERR", Value.makeNum(15), c);
    }
}
