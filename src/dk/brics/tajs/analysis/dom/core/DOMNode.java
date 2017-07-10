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

package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMBuilder;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;

import java.util.Arrays;
import java.util.List;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The Node interface is the primary datatype for the entire Document Object
 * Model. It represents a single node in the document tree.
 */
public class DOMNode {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.NODE_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.NODE_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.NODE_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "Node", Value.makeObject(CONSTRUCTOR));

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Constants.
         */
        createDOMProperty(PROTOTYPE, "ELEMENT_NODE", Value.makeNum(1).setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "ATTRIBUTE_NODE", Value.makeNum(2).setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "TEXT_NODE", Value.makeNum(3).setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "CDATA_SECTION_NODE", Value.makeNum(4).setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "ENTITY_REFERENCE_NODE", Value.makeNum(5).setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "ENTITY_NODE", Value.makeNum(6).setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "PROCESSING_INSTRUCTION_NODE", Value.makeNum(7).setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "COMMENT_NODE", Value.makeNum(8).setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "DOCUMENT_NODE", Value.makeNum(9).setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "DOCUMENT_TYPE_NODE", Value.makeNum(10).setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "DOCUMENT_FRAGMENT_NODE", Value.makeNum(11).setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "NOTATION_NODE", Value.makeNum(12).setReadOnly(), c);

        /*
         * Properties.
         */
        // DOM LEVEL 1
        createDOMProperty(PROTOTYPE, "nodeName", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "nodeValue", Value.makeAnyStr().setReadOnly(), c);
        List<Integer> nodeTypes = Arrays.asList(/*NOT ZERO!*/ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12); // See 'Constants' above
        createDOMProperty(PROTOTYPE, "nodeType", Value.join(nodeTypes.stream().map(Value::makeNum).collect(Collectors.toList())).setReadOnly(), c);
        createDOMProperty(PROTOTYPE, "childNodes", DOMNodeList.makeNaiveInstance().setReadOnly(), c);
        // NB: 'attributes' and 'ownerDocument' are set in CoreBuilder due to circularity

        // DOM LEVEL 2
        createDOMProperty(PROTOTYPE, "prefix", Value.makeAnyStr(), c);
        createDOMProperty(PROTOTYPE, "localName", Value.makeAnyStr(), c);
        createDOMProperty(PROTOTYPE, "namespaceURI", Value.makeAnyStr(), c);

        /*
         * Functions.
         */
        // DOM LEVEL 1
        createDOMFunction(PROTOTYPE, DOMObjects.NODE_APPEND_CHILD, "appendChild", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.NODE_CLONE_NODE, "cloneNode", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.NODE_HAS_CHILD_NODES, "hasChildNodes", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.NODE_INSERT_BEFORE, "insertBefore", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.NODE_REMOVE_CHILD, "removeChild", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.NODE_REPLACE_CHILD, "replaceChild", 2, c);

        // DOM LEVEL 2
        createDOMFunction(PROTOTYPE, DOMObjects.NODE_IS_SUPPORTED, "isSupported", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.NODE_HAS_ATTRIBUTES, "hasAttributes", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.NODE_NORMALIZE, "normalize", 0, c);

        // DOM LEVEL 3
        createDOMFunction(PROTOTYPE, DOMObjects.NODE_COMPARE_DOCUMENT_POSITION, "compareDocumentPosition", 1, c);

        // DOM LEVEL 4
        createDOMFunction(PROTOTYPE, DOMObjects.NODE_CONTAINS, "contains", 1, c);

        INSTANCES = INSTANCES.makeSingleton().makeSummary();
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case NODE_APPEND_CHILD: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                return FunctionCalls.readParameter(call, s, 0);
            }
            case NODE_CLONE_NODE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value deep =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 0));
                return Value.makeObject(DOMBuilder.getAllHtmlObjectLabels());
            }
            case NODE_HAS_CHILD_NODES: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeAnyBool();
            }
            case NODE_INSERT_BEFORE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value newChild =*/
                DOMConversion.toNode(FunctionCalls.readParameter(call, s, 0), c);
                /* Value refChild =*/
                DOMConversion.toNode(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeObject(DOMBuilder.getAllHtmlObjectLabels());
            }
            case NODE_REMOVE_CHILD: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /*Value oldChild =*/
                DOMConversion.toNode(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(DOMBuilder.getAllHtmlObjectLabels());
            }
            case NODE_REPLACE_CHILD: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value newChild =*/
                DOMConversion.toNode(FunctionCalls.readParameter(call, s, 0), c);
                /* Value oldChild =*/
                DOMConversion.toNode(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeObject(DOMBuilder.getAllHtmlObjectLabels());
            }
            case NODE_IS_SUPPORTED: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value feature =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value version =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeAnyBool();
            }
            case NODE_HAS_ATTRIBUTES: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeAnyBool();
            }
            case NODE_NORMALIZE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case NODE_COMPARE_DOCUMENT_POSITION: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                return Value.makeAnyNumUInt();
            }
            case NODE_CONTAINS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                return Value.makeAnyBool();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
