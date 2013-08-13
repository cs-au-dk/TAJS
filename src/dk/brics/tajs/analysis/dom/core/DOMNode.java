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

package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.*;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import java.util.Set;

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

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.NODE_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.NODE_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.NODE_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "Node", Value.makeObject(CONSTRUCTOR));

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Constants.
         */
        createDOMProperty(s, PROTOTYPE, "ELEMENT_NODE", Value.makeNum(1).setReadOnly());
        createDOMProperty(s, PROTOTYPE, "ATTRIBUTE_NODE", Value.makeNum(2).setReadOnly());
        createDOMProperty(s, PROTOTYPE, "TEXT_NODE", Value.makeNum(3).setReadOnly());
        createDOMProperty(s, PROTOTYPE, "CDATA_SECTION_NODE", Value.makeNum(4).setReadOnly());
        createDOMProperty(s, PROTOTYPE, "ENTITY_REFERENCE_NODE", Value.makeNum(5).setReadOnly());
        createDOMProperty(s, PROTOTYPE, "ENTITY_NODE", Value.makeNum(6).setReadOnly());
        createDOMProperty(s, PROTOTYPE, "PROCESSING_INSTRUCTION_NODE", Value.makeNum(7).setReadOnly());
        createDOMProperty(s, PROTOTYPE, "COMMENT_NODE", Value.makeNum(8).setReadOnly());
        createDOMProperty(s, PROTOTYPE, "DOCUMENT_NODE", Value.makeNum(9).setReadOnly());
        createDOMProperty(s, PROTOTYPE, "DOCUMENT_TYPE_NODE", Value.makeNum(10).setReadOnly());
        createDOMProperty(s, PROTOTYPE, "DOCUMENT_FRAGMENT_NODE", Value.makeNum(11).setReadOnly());
        createDOMProperty(s, PROTOTYPE, "NOTATION_NODE", Value.makeNum(12).setReadOnly());

        /*
         * Properties.
         */
        // DOM LEVEL 1
        createDOMProperty(s, PROTOTYPE, "nodeName", Value.makeAnyStr());
        createDOMProperty(s, PROTOTYPE, "nodeValue", Value.makeAnyStr());
        createDOMProperty(s, PROTOTYPE, "nodeType", Value.makeAnyNum());
        createDOMProperty(s, PROTOTYPE, "parentNode", Value.makeObject(INSTANCES));
        createDOMProperty(s, PROTOTYPE, "childNodes", Value.makeObject(DOMNodeList.INSTANCES));
        createDOMProperty(s, PROTOTYPE, "firstChild", Value.makeObject(INSTANCES));
        createDOMProperty(s, PROTOTYPE, "lastChild", Value.makeObject(INSTANCES));
        createDOMProperty(s, PROTOTYPE, "previousSibling", Value.makeObject(INSTANCES));
        createDOMProperty(s, PROTOTYPE, "nextSibling", Value.makeObject(INSTANCES));
        // NB: 'attributes' and 'ownerDocument' are set in CoreBuilder due to circularity

        // DOM LEVEL 2
        createDOMProperty(s, PROTOTYPE, "prefix", Value.makeAnyStr());
        createDOMProperty(s, PROTOTYPE, "localName", Value.makeAnyStr());
        createDOMProperty(s, PROTOTYPE, "namespaceURI", Value.makeAnyStr());



        /*
         * Functions.
         */
        // DOM LEVEL 1
        createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_APPEND_CHILD, "appendChild", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_CLONE_NODE, "cloneNode", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_HAS_CHILD_NODES, "hasChildNodes", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_INSERT_BEFORE, "insertBefore", 2);
        createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_REMOVE_CHILD, "removeChild", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_REPLACE_CHILD, "replaceChild", 2);

        // DOM LEVEL 2
        createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_IS_SUPPORTED, "isSupported", 2);
        createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_HAS_ATTRIBUTES, "hasAttributes", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.NODE_NORMALIZE, "normalize", 0);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case NODE_APPEND_CHILD: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                return NativeFunctions.readParameter(call, s, 0);
            }
            case NODE_CLONE_NODE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value deep =*/ Conversion.toBoolean(NativeFunctions.readParameter(call, s, 0));
                int baseReg = call.getBaseRegister();
                Set<ObjectLabel> cloneLabels = s.readRegister(baseReg).getObjectLabels();
                return Value.makeObject(cloneLabels);
            }
            case NODE_HAS_CHILD_NODES: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeAnyBool();
            }
            case NODE_INSERT_BEFORE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Value newChild = DOMConversion.toNode(NativeFunctions.readParameter(call, s, 0), c);
                /* Value refChild =*/ DOMConversion.toNode(NativeFunctions.readParameter(call, s, 1), c);
                return newChild;
            }
            case NODE_REMOVE_CHILD: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Value oldChild = DOMConversion.toNode(NativeFunctions.readParameter(call, s, 0), c);
                return oldChild;
            }
            case NODE_REPLACE_CHILD: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value newChild =*/ DOMConversion.toNode(NativeFunctions.readParameter(call, s, 0), c);
                Value oldChild = DOMConversion.toNode(NativeFunctions.readParameter(call, s, 1), c);
                return oldChild;
            }
            case NODE_IS_SUPPORTED: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value feature =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value version =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeAnyBool();
            }
            case NODE_HAS_ATTRIBUTES: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeAnyBool();
            }
            case NODE_NORMALIZE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }

}
