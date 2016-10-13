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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.style.ClientBoundingRect;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The Element interface represents an element in an HTML or XML document.
 */
public class DOMElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = new ObjectLabel(DOMObjects.ELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.ELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.ELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "Element", Value.makeObject(CONSTRUCTOR));

        // Prototype
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(DOMNode.PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        // DOM Level 1
        createDOMProperty(INSTANCES, "tagName", Value.makeAnyStr(), c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // Unknown
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_GET_BOUNDING_CLIENT_RECT, "getBoundingClientRect", 0, c);

        // DOM Level 1
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_GET_ATTRIBUTE, "getAttribute", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_SET_ATTRIBUTE, "setAttribute", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_REMOVE_ATTRIBUTE, "removeAttribute", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_GET_ATTRIBUTE_NODE, "getAttributeNode", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_SET_ATTRIBUTE_NODE, "setAttributeNode", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_REMOVE_ATTRIBUTE_NODE, "removeAttributeNode", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_GET_ELEMENTS_BY_TAGNAME, "getElementsByTagName", 1, c);

        // DOM Level 2
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_GET_ATTRIBUTE_NS, "getAttributeNS", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_SET_ATTRIBUTE_NS, "setAttributeNS", 3, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_REMOVE_ATTRIBUTE_NS, "removeAttributeNS", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_GET_ATTRIBUTE_NODE_NS, "getAttributeNodeNS", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_SET_ATTRIBUTE_NODE_NS, "setAttributeNodeNS", 3, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_GET_ELEMENTS_BY_TAGNAME_NS, "getElementsByTagNameNS", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_HAS_ATTRIBUTE, "hasAttribute", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_HAS_ATTRIBUTE_NS, "hasAttributeNS", 2, c);

        // DOM Level 3
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_SET_ID_ATTRIBUTE, "setIdAttribute", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_SET_ID_ATTRIBUTE_NS, "setIdAttributeNS", 3, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_SET_ID_ATTRIBUTE_NODE, "setIdAttributeNode", 2, c);

        // DOM Level 2
        createDOMProperty(DOMAttr.INSTANCES, "ownerElement", Value.makeObject(INSTANCES).setReadOnly(), c);

        // semistandard
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_QUERY_SELECTOR_ALL, "querySelectorAll", 1, c);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case ELEMENT_GET_ATTRIBUTE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeAnyStr();
            }
            case ELEMENT_SET_ATTRIBUTE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value value =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case ELEMENT_REMOVE_ATTRIBUTE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case ELEMENT_GET_ATTRIBUTE_NS: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value namespace =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeAnyStr();
            }
            case ELEMENT_GET_ATTRIBUTE_NODE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeObject(DOMAttr.INSTANCES);
            }
            case ELEMENT_GET_ATTRIBUTE_NODE_NS: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value namespace =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeObject(DOMAttr.INSTANCES);
            }
            case ELEMENT_GET_BOUNDING_CLIENT_RECT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeObject(ClientBoundingRect.INSTANCES);
            }
            case ELEMENT_GET_ELEMENTS_BY_TAGNAME: {
                // TODO: needs precision, but cannot do like document.getElementsByTagName() bc. State is for everything
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeObject(DOMNodeList.INSTANCES);
            }
            case ELEMENT_GET_ELEMENTS_BY_TAGNAME_NS: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value namespace =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeObject(DOMNodeList.INSTANCES);
            }
            case ELEMENT_HAS_ATTRIBUTE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeAnyBool();
            }
            case ELEMENT_HAS_ATTRIBUTE_NS: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value namespace =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeAnyBool();
            }
            case ELEMENT_REMOVE_ATTRIBUTE_NS: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value namespaceURI =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value localName =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case ELEMENT_REMOVE_ATTRIBUTE_NODE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                return DOMConversion.toAttr(NativeFunctions.readParameter(call, s, 0), c);
            }
            case ELEMENT_SET_ATTRIBUTE_NS: {
                NativeFunctions.expectParameters(nativeObject, call, c, 3, 3);
                /* Value namespace =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c).joinNull();
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                /* Value value =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 2), c);
                return Value.makeUndef();
            }
            case ELEMENT_SET_ATTRIBUTE_NODE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value newAttr =*/
                DOMConversion.toAttr(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeObject(DOMAttr.INSTANCES).joinNull();
            }
            case ELEMENT_SET_ATTRIBUTE_NODE_NS: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value newAttr =*/
                DOMConversion.toAttr(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeObject(DOMAttr.INSTANCES).joinNull();
            }
            case ELEMENT_SET_ID_ATTRIBUTE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value isId =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 1));
                return Value.makeUndef();
            }
            case ELEMENT_SET_ID_ATTRIBUTE_NS: {
                NativeFunctions.expectParameters(nativeObject, call, c, 3, 3);
                /* Value namespaceURI =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value localName =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                /* Value isId =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 2));
                return Value.makeUndef();
            }
            case ELEMENT_SET_ID_ATTRIBUTE_NODE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value idAttr =*/
                DOMConversion.toAttr(NativeFunctions.readParameter(call, s, 0), c);
                /* Value isId =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 1));
                return Value.makeUndef();
            }
            case ELEMENT_QUERY_SELECTOR_ALL: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                return Value.makeObject(DOMNodeList.INSTANCES);
            }
            default: {
                throw new AnalysisException("Unknown Native Object: " + nativeObject);
            }
        }
    }
}
