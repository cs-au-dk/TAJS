/*
 * Copyright 2009-2020 Aarhus University
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
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.html.HTMLCollection;
import dk.brics.tajs.analysis.dom.style.ClientBoundingRect;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.util.Collections.singleton;

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
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.ELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.ELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.ELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(DOMNode.CONSTRUCTOR));
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
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_MATCHES, "matches", 1, c);

        // semistandard
        // NB: webkit version!
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_MATCHES_SELECTOR, "webkitMatchesSelector", 1, c);

        // semistandard
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_REMOVE, "remove", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_QUERY_SELECTOR_ALL, "querySelectorAll", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.ELEMENT_QUERY_SELECTOR, "querySelector", 1, c);

        createDOMProperty(PROTOTYPE, "classList", Value.makeObject(DOMTokenList.INSTANCES), c);
    }

    public static Value setAttributeModel(Set<ObjectLabel> receivers, Value attributeName, Value attributeValue, Solver.SolverInterface c) {
        if (attributeName.isMaybeSingleStr() && attributeName.getStr().equals("name")) {
            PropVarOperations pv = c.getAnalysis().getPropVarOperations();
            pv.writeProperty(singleton(HTMLCollection.INSTANCES), Conversion.toString(attributeValue, c), Value.makeObject(receivers));
        }
        return Value.makeUndef();
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case ELEMENT_GET_ATTRIBUTE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyStr().joinNull();
            }
            case ELEMENT_SET_ATTRIBUTE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value name =*/
                Value attributeName = Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value value =*/
                Value attributeValue = Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);

                if (attributeName.isMaybeSingleStr() && attributeName.isMaybeStr("class")) {
                    PropVarOperations pv = c.getAnalysis().getPropVarOperations();
                    pv.writeProperty(call.getThis().getObjectLabels(), Value.makeStr("className"), attributeValue);
                }
                return setAttributeModel(s.readThisObjects(), attributeName, attributeValue, c);
            }
            case ELEMENT_REMOVE_ATTRIBUTE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case ELEMENT_GET_ATTRIBUTE_NS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value namespace =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeAnyStr().joinNull();
            }
            case ELEMENT_GET_ATTRIBUTE_NODE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(DOMAttr.INSTANCES);
            }
            case ELEMENT_GET_ATTRIBUTE_NODE_NS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value namespace =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeObject(DOMAttr.INSTANCES);
            }
            case ELEMENT_GET_BOUNDING_CLIENT_RECT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeObject(ClientBoundingRect.INSTANCES);
            }
            case ELEMENT_GET_ELEMENTS_BY_TAGNAME: {
                // TODO: needs precision, but cannot do like document.getElementsByTagName() bc. State is for everything
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(HTMLCollection.INSTANCES);
            }
            case ELEMENT_GET_ELEMENTS_BY_TAGNAME_NS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value namespace =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeObject(HTMLCollection.INSTANCES);
            }
            case ELEMENT_HAS_ATTRIBUTE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyBool();
            }
            case ELEMENT_HAS_ATTRIBUTE_NS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value namespace =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeAnyBool();
            }
            case ELEMENT_REMOVE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case ELEMENT_REMOVE_ATTRIBUTE_NS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value namespaceURI =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value localName =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case ELEMENT_REMOVE_ATTRIBUTE_NODE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                return DOMConversion.toAttr(FunctionCalls.readParameter(call, s, 0), c);
            }
            case ELEMENT_SET_ATTRIBUTE_NS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 3, 3);
                /* Value namespace =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c).joinNull();
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                /* Value value =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 2), c);
                return Value.makeUndef();
            }
            case ELEMENT_SET_ATTRIBUTE_NODE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value newAttr =*/
                DOMConversion.toAttr(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(DOMAttr.INSTANCES).joinNull();
            }
            case ELEMENT_SET_ATTRIBUTE_NODE_NS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value newAttr =*/
                DOMConversion.toAttr(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(DOMAttr.INSTANCES).joinNull();
            }
            case ELEMENT_SET_ID_ATTRIBUTE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value isId =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 1));
                return Value.makeUndef();
            }
            case ELEMENT_SET_ID_ATTRIBUTE_NS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 3, 3);
                /* Value namespaceURI =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value localName =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                /* Value isId =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 2));
                return Value.makeUndef();
            }
            case ELEMENT_SET_ID_ATTRIBUTE_NODE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value idAttr =*/
                DOMConversion.toAttr(FunctionCalls.readParameter(call, s, 0), c);
                /* Value isId =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 1));
                return Value.makeUndef();
            }
            case ELEMENT_QUERY_SELECTOR_ALL: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                return DOMNodeList.makeNaiveInstance();
            }
            case ELEMENT_QUERY_SELECTOR: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Value selector = Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return DOMFunctions.makeAnyHTMLElement().joinNull();
            }
            case ELEMENT_MATCHES_SELECTOR: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                // may throw on bad syntax
                Exceptions.throwTypeError(c); // FIXME: should be a DOM-Exception
                return Value.makeAnyBool();
            }
            case ELEMENT_MATCHES: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(FunctionCalls.readParameter(call, c.getState(), 0), c);
                return Value.makeAnyBool();
            }
            default: {
                throw new AnalysisException(call.getJSSourceNode().getSourceLocation() + ": Unknown Native Object: " + nativeObject);
            }
        }
    }
}
