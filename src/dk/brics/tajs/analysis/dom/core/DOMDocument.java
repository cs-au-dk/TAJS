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
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMRegistry;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.html.HTMLCollection;
import dk.brics.tajs.analysis.dom.xpath.XPathResult;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.solver.Message.Severity;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The Document interface represents the entire HTML or XML document.
 * Conceptually, it is the root of the document tree, and provides the primary
 * access to the document's data.
 */
public class DOMDocument {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.DOCUMENT_CONSTRUCTOR, Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.DOCUMENT_PROTOTYPE, Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.DOCUMENT_INSTANCES, Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "Document", Value.makeObject(CONSTRUCTOR));

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(DOMNode.PROTOTYPE));

        // Multiplied object.
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(INSTANCES, "onfocus", Value.makeNull(), c);

        // DOM LEVEL 1
        createDOMProperty(INSTANCES, "doctype", Value.makeObject(DOMDocumentType.INSTANCES), c);
        // NB: The documentElement property is written by HTMLBuilder (due to cyclic dependency).

        // DOM LEVEL 2
        // various properties from the NODE Interface:
        createDOMProperty(INSTANCES, "nodeName", Value.makeStr("#document").setReadOnly(), c);
        createDOMProperty(INSTANCES, "nodeValue", Value.makeNull().setReadOnly(), c);
        createDOMProperty(INSTANCES, "nodeType", Value.makeNum(9).setReadOnly(), c);

        // DOM LEVEL 3
        createDOMProperty(INSTANCES, "inputEncoding", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(INSTANCES, "xmlEncoding", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(INSTANCES, "xmlStandalone", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "xmlVersion", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "strictErrorChecking", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "documentURI", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "domConfig", Value.makeObject(DOMConfiguration.INSTANCES), c);
        createDOMProperty(INSTANCES, "compatMode", Value.makeAnyStr(), c);

        // Summarize:
        // NB: The objectlabel is summarized in HTMLBuilder, because a property is added to it there.

        /*
         * Properties from DOMWindow
         */
        createDOMProperty(INSTANCES, "location", Value.makeObject(DOMWindow.LOCATION), c);
        createDOMProperty(INSTANCES, "readyState", Value.makeAnyStrNotUInt().setReadOnly(), c);

        /*
         * Functions.
         */
        // DOM LEVEL 1
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ELEMENT, "createElement", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_CREATE_DOCUMENTFRAGMENT, "createDocumentFragment", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_CREATE_TEXTNODE, "createTextNode", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_CREATE_COMMENT, "createComment", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_CREATE_CDATASECTION, "createCDATASection", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_CREATEPROCESSINGINSTRUCTION, "createProcessingInstruction", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ATTRIBUTE, "createAttribute", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ENTITYREFERENCE, "createEntityReference", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_GET_ELEMENTS_BY_TAGNAME, "getElementsByTagName", 1, c);

        // DOM LEVEL 2
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_IMPORT_NODE, "importNode", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ELEMENT_NS, "createElementNS", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ATTRIBUTE_NS, "createAttributeNS", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_GET_ELEMENTS_BY_TAGNAME_NS, "getElementsByTagNameNS", 2, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_GET_ELEMENT_BY_ID, "getElementById", 1, c);

        // DOM LEVEL 3
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_ADOPT_NODE, "adoptNode", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_NORMALIZEDOCUMENT, "normalizeDocument", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_RENAME_NODE, "renameNode", 3, c);

        // semistandard
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_EVALUATE, "evaluate", 5, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_EXECCOMMAND, "execCommand", 3, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_QUERY_SELECTOR_ALL, "querySelectorAll", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.DOCUMENT_QUERY_SELECTOR, "querySelector", 1, c);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeobject, CallInfo call, Solver.SolverInterface c, Set<ObjectLabel> html_object_labels) {
        State s = c.getState();
        switch (nativeobject) {
            case DOCUMENT_ADOPT_NODE: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() < 1) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                return Value.makeObject(DOMElement.INSTANCES);
            }
            case DOCUMENT_CREATE_ATTRIBUTE: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() < 1) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(DOMAttr.INSTANCES);
            }

            case DOCUMENT_CREATE_CDATASECTION: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() < 1) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                ObjectLabel label = ObjectLabel.make(DOMObjects.CDATASECTION_INSTANCES, Kind.OBJECT);
                s.newObject(label);
                return Value.makeObject(label);
            }
            case DOCUMENT_CREATE_COMMENT: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() < 1) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                ObjectLabel label = ObjectLabel.make(DOMObjects.COMMENT_INSTANCES, Kind.OBJECT);
                s.newObject(label);
                return Value.makeObject(label);
            }
            case DOCUMENT_CREATE_ELEMENT: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() < 1) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                Value tagname = Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                if (tagname.isMaybeSingleStr()) {
                    String t = tagname.getStr();
                    if (t.length() > 0 && t.charAt(0) == '<') {
                        // Extended createElement syntax is being used
                        DOMException.throwException(call.getSourceNode(), c);
                        return Value.makeNone();
                    }
                    return Value.makeObject(DOMFunctions.getHTMLObjectLabel(t));
                } else if (tagname.isMaybeFuzzyStr()) { // TODO: could be more precise...
                    return Value.makeObject(html_object_labels);
                } else {
                    return DOMFunctions.makeAnyHTMLElement();
                }
            }
            case DOCUMENT_CREATE_ELEMENT_NS: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 2);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() < 1) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                Value tagname = Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                if (tagname.isMaybeSingleStr()) {
                    String t = tagname.getStr();
                    if (t.length() > 0 && t.charAt(0) == '<') {
                        // Extended createElement syntax is being used
                        DOMException.throwException(call.getSourceNode(), c);
                        return Value.makeNone();
                    }
                    return Value.makeObject(DOMFunctions.getHTMLObjectLabel(t));
                } else if (tagname.isMaybeFuzzyStr()) { // TODO: could be more precise...
                    return Value.makeObject(html_object_labels);
                } else {
                    return DOMFunctions.makeAnyHTMLElement();
                }
            }
            case DOCUMENT_CREATE_ENTITYREFERENCE: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() < 1) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                /* Value name =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(DOMEntityReference.INSTANCES);
            }
            case DOCUMENT_CREATEPROCESSINGINSTRUCTION: {
                DOMFunctions.expectParameters(nativeobject, call, c, 2, 2);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() < 2) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                /* Value target =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value data =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeObject(DOMProcessingInstruction.INSTANCES);
            }
            case DOCUMENT_CREATE_TEXTNODE: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() < 1) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                /* Value data =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(DOMText.INSTANCES);
            }
            case DOCUMENT_CREATE_DOCUMENTFRAGMENT: {
                DOMFunctions.expectParameters(nativeobject, call, c, 0, 0);
                return Value.makeObject(DOMDocumentFragment.INSTANCES);
            }
            case DOCUMENT_EVALUATE: {
                DOMFunctions.expectParameters(nativeobject, call, c, 5, 5);
                return Value.makeObject(XPathResult.INSTANCES);
            }
            case DOCUMENT_EXECCOMMAND: {
                DOMFunctions.expectParameters(nativeobject, call, c, 3, 3);
                c.getMonitoring().addMessage(call.getJSSourceNode(), Message.Severity.TAJS_ERROR, "Warning: Calling document.execCommand, but no side-effects have been implemented for it...");
                return Value.makeAnyBool();
            }
            case DOCUMENT_GET_ELEMENT_BY_ID: {
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() < 1) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value id = Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                Set<ObjectLabel> labels;
                if (id.isMaybeSingleStr()) {
                    labels = s.getExtras().getFromMayMap(DOMRegistry.MayMaps.ELEMENTS_BY_ID.name(), id.getStr());
                    if (labels.isEmpty()) {
                        c.getMonitoring().addMessage(call.getSourceNode(), Severity.HIGH, String.format("<%s>(%s) did not match any elements", nativeobject, id));
                    }
                } else {
                    labels = s.getExtras().getValuesFromMayMap(DOMRegistry.MayMaps.ELEMENTS_BY_ID.name());
                }
                return Value.makeObject(labels).joinNull();
            }
            case DOCUMENT_GET_ELEMENTS_BY_TAGNAME: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(HTMLCollection.INSTANCES);
            }
            case DOCUMENT_RENAME_NODE: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 3);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() < 1) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                return DOMFunctions.makeAnyHTMLElement();
            }
            case DOCUMENT_QUERY_SELECTOR_ALL: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() < 1) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                return DOMNodeList.makeNaiveInstance();
            }
            case DOCUMENT_QUERY_SELECTOR: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value selector = Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return DOMFunctions.makeAnyHTMLElement().joinNull();
            }
            case DOCUMENT_CREATE_ATTRIBUTE_NS:
            case DOCUMENT_GET_ELEMENTS_BY_TAGNAME_NS:
            case DOCUMENT_IMPORT_NODE:
            case DOCUMENT_NORMALIZEDOCUMENT:
            default: {
                c.getMonitoring().addMessage(call.getSourceNode(), Severity.TAJS_ERROR, "Unsupported operation: " + nativeobject);
                return Value.makeNone();
            }
        }
    }
}
