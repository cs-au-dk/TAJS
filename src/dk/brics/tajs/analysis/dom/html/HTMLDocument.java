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

package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMRegistry;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.analysis.dom.core.DOMNodeList;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.util.AnalysisException;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

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

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.HTMLDOCUMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.HTMLDOCUMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.HTMLDOCUMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "HTMLDocument", Value.makeObject(CONSTRUCTOR));

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
        createDOMProperty(INSTANCES, "title", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "referrer", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(INSTANCES, "domain", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(INSTANCES, "URL", Value.makeAnyStr().setReadOnly(), c);
        createDOMProperty(INSTANCES, "images", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly(), c);
        createDOMProperty(INSTANCES, "applets", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly(), c);
        createDOMProperty(INSTANCES, "links", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly(), c);
        createDOMProperty(INSTANCES, "forms", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly(), c);
        createDOMProperty(INSTANCES, "anchors", Value.makeObject(HTMLCollection.INSTANCES).setReadOnly(), c);
        createDOMProperty(INSTANCES, "cookie", Value.makeAnyStr(), c);

        // DOM LEVEL 0 / UNKNOWN
        createDOMProperty(INSTANCES, "width", Value.makeAnyNumUInt(), c);
        createDOMProperty(INSTANCES, "height", Value.makeAnyNumUInt(), c);

        // various properties from the NODE Interface:
        createDOMProperty(INSTANCES, "nodeName", Value.makeStr("#document").setReadOnly(), c);
        createDOMProperty(INSTANCES, "nodeValue", Value.makeNull().setReadOnly(), c);
        createDOMProperty(INSTANCES, "nodeType", Value.makeNum(9).setReadOnly(), c);

        /*
         * Functions.
         */
        // DOM Level 1
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLDOCUMENT_OPEN, "open", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLDOCUMENT_CLOSE, "close", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLDOCUMENT_WRITE, "write", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLDOCUMENT_WRITELN, "writeln", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLDOCUMENT_GET_ELEMENTS_BY_NAME, "getElementsByName", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLDOCUMENT_GET_ELEMENTS_BY_CLASS_NAME, "getElementsByClassName", 1, c);

        createDOMProperty(DOMWindow.WINDOW, "document", Value.makeObject(INSTANCES), c);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case HTMLDOCUMENT_OPEN: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case HTMLDOCUMENT_CLOSE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case HTMLDOCUMENT_WRITE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case HTMLDOCUMENT_WRITELN: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case HTMLDOCUMENT_GET_ELEMENTS_BY_NAME: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Value name = Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                if (name.isMaybeSingleStr()) {
                    Set<ObjectLabel> labels = s.getExtras().getFromMayMap(DOMRegistry.MayMaps.ELEMENTS_BY_NAME.name(), name.getStr());
                    if (labels.isEmpty()) {
                        c.getMonitoring().addMessage(call.getSourceNode(), Message.Severity.HIGH, String.format("<%s>(%s) did not match any elements", nativeObject, name.getStr()));
                    }
                    ObjectLabel nodeList = DOMNodeList.makeAllocationSiteInstanceWithUnorderedContent(call.getSourceNode(), labels, c);
                    return Value.makeObject(nodeList);
                }
                return DOMNodeList.makeNaiveInstance();
            }
            case HTMLDOCUMENT_GET_ELEMENTS_BY_CLASS_NAME: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value className =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(HTMLCollection.INSTANCES);
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
