/*
 * Copyright 2012 Aarhus University
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

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.dom.*;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import java.util.Collections;
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

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.DOCUMENT_CONSTRUCTOR, Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.DOCUMENT_PROTOTYPE, Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.DOCUMENT_INSTANCES, Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "Document", Value.makeObject(CONSTRUCTOR));

        // Prototype object.
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(DOMNode.PROTOTYPE));

        // Multiplied object.
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /**
         * Properties.
         */
        // DOM LEVEL 1
        createDOMProperty(s, INSTANCES, "doctype", Value.makeObject(DOMDocumentType.INSTANCES));
        // NB: The documentElement property is written by HTMLBuilder (due to cyclic dependency).

        // DOM LEVEL 2
        // None.

        // DOM LEVEL 3
        createDOMProperty(s, INSTANCES, "inputEncoding", Value.makeAnyStr().setReadOnly());
        createDOMProperty(s, INSTANCES, "xmlEncoding", Value.makeAnyStr().setReadOnly());
        createDOMProperty(s, INSTANCES, "xmlStandalone", Value.makeAnyBool());
        createDOMProperty(s, INSTANCES, "xmlVersion", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "strictErrorChecking", Value.makeAnyBool());
        createDOMProperty(s, INSTANCES, "documentURI", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "domConfig", Value.makeObject(DOMConfiguration.INSTANCES));

        // Summarize:
        // NB: The objectlabel is summarized in HTMLBuilder, because a property is added to it there.

        /**
         * Properties from DOMWindow
         */
        createDOMProperty(s, INSTANCES, "location", Value.makeObject(DOMWindow.LOCATION));

        /**
         * Functions.
         */
        // DOM LEVEL 1
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ELEMENT, "createElement", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_DOCUMENTFRAGMENT, "createDocumentFragment", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_TEXTNODE, "createTextNode", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_COMMENT, "createComment", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_CDATASECTION, "createCDATASection", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATEPROCESSINGINSTRUCTION, "createProcessingInstruction", 2);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ATTRIBUTE, "createAttribute", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ENTITYREFERENCE, "createEntityReference", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_GET_ELEMENTS_BY_TAGNAME, "getElementsByTagName", 1);

        // DOM LEVEL 2
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_IMPORT_NODE, "importNode", 2);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ELEMENT_NS, "createElementNS", 2);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_CREATE_ATTRIBUTE_NS, "createAttributeNS", 2);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_GET_ELEMENTS_BY_TAGNAME_NS, "getElementsByTagNameNS", 2);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_GET_ELEMENT_BY_ID, "getElementById", 1);

        // DOM LEVEL 3
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_ADOPT_NODE, "adoptNode", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_NORMALIZEDOCUMENT, "normalizeDocument", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.DOCUMENT_RENAME_NODE, "renameNode", 3);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeobject, CallInfo call, State s, Solver.SolverInterface c, Set<ObjectLabel> html_object_labels) {
        switch (nativeobject) {
            case DOCUMENT_ADOPT_NODE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                //throw new UnsupportedOperationException("DOCUMENT_ADOPT_NODE not supported.");
                return Value.makeObject(DOMElement.INSTANCES);
            }
            case DOCUMENT_CREATE_ATTRIBUTE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeObject(DOMAttr.INSTANCES);
            }
            case DOCUMENT_CREATE_ATTRIBUTE_NS: {
                throw new UnsupportedOperationException("DOCUMENT_CREATE_ATTRIBUTE_NS not supported.");
            }
            case DOCUMENT_CREATE_CDATASECTION: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                ObjectLabel label = new ObjectLabel(DOMObjects.CDATASECTION_INSTANCES, Kind.OBJECT);
                s.newObject(label);
                return Value.makeObject(label);
            }
            case DOCUMENT_CREATE_COMMENT: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                ObjectLabel label = new ObjectLabel(DOMObjects.COMMENT_INSTANCES, Kind.OBJECT);
                s.newObject(label);
                return Value.makeObject(label);
            }
            case DOCUMENT_CREATE_ELEMENT: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value tagname = Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                if (tagname.isMaybeSingleStr()) {
                    String t = tagname.getStr();
                    return Value.makeObject(DOMFunctions.getHTMLObjectLabel(t));
                } else if (tagname.isMaybeFuzzyStr()) { // TODO: could be more precise...
                    return Value.makeObject(html_object_labels);
                } else {
                    return DOMFunctions.makeAnyHTMLElement();
                }
            }
            case DOCUMENT_CREATE_ELEMENT_NS: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
                Value tagname = Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                if (tagname.isMaybeSingleStr()) {
                    String t = tagname.getStr();
                    return Value.makeObject(DOMFunctions.getHTMLObjectLabel(t));
                } else if (tagname.isMaybeFuzzyStr()) { // TODO: could be more precise...
                    return Value.makeObject(html_object_labels);
                } else {
                    return DOMFunctions.makeAnyHTMLElement();
                }
            }
            case DOCUMENT_CREATE_ENTITYREFERENCE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                /* Value name =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeObject(DOMEntityReference.INSTANCES);
            }
            case DOCUMENT_CREATEPROCESSINGINSTRUCTION: {
                NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
                /* Value target =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value data =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeObject(DOMProcessingInstruction.INSTANCES);
            }
            case DOCUMENT_CREATE_TEXTNODE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                /* Value data =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeObject(DOMText.INSTANCES);
            }
            case DOCUMENT_CREATE_DOCUMENTFRAGMENT: {
                NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
                return Value.makeObject(DOMDocumentFragment.INSTANCES);
            }
            case DOCUMENT_GET_ELEMENT_BY_ID: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value id = Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                if (id.isMaybeSingleStr()) {
                    Set<ObjectLabel> labels = s.getExtras().getFromMayMap(DOMRegistry.MayMaps.ELEMENTS_BY_ID.name(), id.getStr());
                    if (labels.size() > 0) {
                        return Value.makeObject(labels);
                    }
                    return DOMFunctions.makeAnyHTMLElement().joinNull();
                }
                return DOMFunctions.makeAnyHTMLElement().joinNull();
            }
            case DOCUMENT_GET_ELEMENTS_BY_TAGNAME: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
                Value tagname = Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                if (tagname.isMaybeSingleStr()) {
                    Set<ObjectLabel> labels = s.getExtras().getFromMayMap(DOMRegistry.MayMaps.ELEMENTS_BY_TAGNAME.name(), tagname.getStr());
                    Value v = Value.makeObject(labels);
                    ObjectLabel nodeList = DOMFunctions.makeEmptyNodeList();
                    if (labels.size() > 0) {
                        s.writeProperty(Collections.singleton(nodeList), Value.makeAnyStrUInt(), v, true, false);
                    }
                    return Value.makeObject(nodeList);
                }
                return DOMFunctions.makeAnyHTMLNodeList(s);
            }
            case DOCUMENT_GET_ELEMENTS_BY_TAGNAME_NS: {
                throw new UnsupportedOperationException("DOCUMENT_GET_ELEMENTS_BY_TAGNAME_NS not supported.");
            }
            case DOCUMENT_IMPORT_NODE: {
                throw new UnsupportedOperationException("DOCUMENT_IMPORT_NODE not supported.");
            }
            case DOCUMENT_NORMALIZEDOCUMENT: {
                throw new UnsupportedOperationException("DOCUMENT_NORMALIZEDOCUMENT not supported.");
            }
            case DOCUMENT_RENAME_NODE: {
                NativeFunctions.expectParameters(nativeobject, call, c, 1, 3);
                return DOMFunctions.makeAnyHTMLElement();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeobject);
            }
        }
    }
}
