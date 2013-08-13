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

import dk.brics.tajs.analysis.*;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * The Text interface inherits from CharacterData and represents the textual
 * content (termed character data in XML) of an Element or Attr. If there is no
 * markup inside an element's content, the text is contained in a single object
 * implementing the Text interface that is the only child of the element. If
 * there is markup, it is parsed into the information items (elements, comments,
 * etc.) and Text nodes that form the list of children of the element.
 */
public class DOMText {

    public static ObjectLabel CONSTRUCTOR;
    public static ObjectLabel PROTOTYPE;
    public static ObjectLabel INSTANCES;

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.TEXT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.TEXT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.TEXT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "Text", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(DOMCharacterData.PROTOTYPE));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(s, INSTANCES, "isElementContentWhitespace", Value.makeAnyBool().setReadOnly());
        createDOMProperty(s, INSTANCES, "wholeText", Value.makeAnyStr().setReadOnly());

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        createDOMFunction(s, PROTOTYPE, DOMObjects.TEXT_SPLIT_TEXT, "splitText", 1);
        createDOMFunction(s, PROTOTYPE, DOMObjects.TEXT_REPLACE_WHOLE_TEXT, "replaceWholeText", 1);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeObjects, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObjects) {
            case TEXT_SPLIT_TEXT: {
                NativeFunctions.expectParameters(nativeObjects, call, c, 1, 1);
                /* Value offset =*/ Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeObject(PROTOTYPE);
            }
            case TEXT_REPLACE_WHOLE_TEXT: {
                NativeFunctions.expectParameters(nativeObjects, call, c, 1, 1);
                /* Value content =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeObject(PROTOTYPE).joinNull();
            }
            default: {
                throw new AnalysisException("Unknown Native Object: " + nativeObjects);
            }
        }
    }

}
