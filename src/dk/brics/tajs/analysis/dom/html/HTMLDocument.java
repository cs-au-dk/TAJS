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

package dk.brics.tajs.analysis.dom.html;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

import java.util.Collections;
import java.util.Set;

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.*;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

/**
 * An HTMLDocument is the root of the HTML hierarchy and holds the entire
 * content. Besides providing access to the hierarchy, it also provides some
 * convenience methods for accessing certain sets of information from the
 * document.
 */
public class HTMLDocument {

    public static ObjectLabel CONSTRUCTOR;
    public static ObjectLabel PROTOTYPE;
    public static ObjectLabel INSTANCES;

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLDOCUMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.HTMLDOCUMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.HTMLDOCUMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "HTMLDocument", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(DOMDocument.INSTANCES));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        // DOM Level 1
        createDOMProperty(s, INSTANCES, "title", Value.makeAnyStr());
        createDOMProperty(s, INSTANCES, "referrer", Value.makeAnyStr().setReadOnly());
        createDOMProperty(s, INSTANCES, "domain", Value.makeAnyStr().setReadOnly());
        createDOMProperty(s, INSTANCES, "URL", Value.makeAnyStr().setReadOnly());
        createDOMProperty(s, INSTANCES, "images", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly());
        createDOMProperty(s, INSTANCES, "applets", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly());
        createDOMProperty(s, INSTANCES, "links", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly());
        createDOMProperty(s, INSTANCES, "forms", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly());
        createDOMProperty(s, INSTANCES, "anchors", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly());
        createDOMProperty(s, INSTANCES, "cookie", Value.makeAnyStr());

        // DOM LEVEL 0 / UNKNOWN
        createDOMProperty(s, INSTANCES, "width", Value.makeAnyNumUInt());
        createDOMProperty(s, INSTANCES, "height", Value.makeAnyNumUInt());

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // DOM Level 1
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLDOCUMENT_OPEN, "open", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLDOCUMENT_CLOSE, "close", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLDOCUMENT_WRITE, "write", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLDOCUMENT_WRITELN, "writeln", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLDOCUMENT_GET_ELEMENTS_BY_NAME, "getElementsByName", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLDOCUMENT_GET_ELEMENTS_BY_CLASS_NAME, "getElementsByClassName", 1);
        
        createDOMProperty(s, DOMWindow.WINDOW, "document", Value.makeObject(HTMLDocument.INSTANCES));
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case HTMLDOCUMENT_OPEN: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case HTMLDOCUMENT_CLOSE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case HTMLDOCUMENT_WRITE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case HTMLDOCUMENT_WRITELN: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case HTMLDOCUMENT_GET_ELEMENTS_BY_NAME: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Value name = Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                if (name.isMaybeSingleStr()) {
                    Set<ObjectLabel> labels = s.getExtras().getFromMayMap(DOMRegistry.MayMaps.ELEMENTS_BY_NAME.name(), name.getStr());
                    Value v = Value.makeObject(labels);
                    ObjectLabel nodeList = DOMFunctions.makeEmptyNodeList();
                    if (labels.size() > 0) {
                        s.writeProperty(Collections.singleton(nodeList), Value.makeAnyStrUInt(), v, true, false);
                    }
                    return Value.makeObject(nodeList);
                }
                return DOMFunctions.makeAnyHTMLNodeList(s);
            }
            case HTMLDOCUMENT_GET_ELEMENTS_BY_CLASS_NAME: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value className =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return DOMFunctions.makeAnyHTMLNodeList(s);
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }

}
