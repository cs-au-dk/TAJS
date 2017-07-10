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

package dk.brics.tajs.analysis.dom.html5;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.html.HTMLElement;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;

public class HTMLMediaElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.HTMLMEDIAELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.HTMLMEDIAELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.HTMLMEDIAELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "HTMLMediaElement", Value.makeObject(CONSTRUCTOR));

        // FIXME: not all media element prototype properties implemented (GitHub #409)

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE));
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLMEDIAELEMENT_CAN_PLAY_TYPE, "canPlayType", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLMEDIAELEMENT_FAST_SEEK, "fastSeek", 1, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLMEDIAELEMENT_LOAD, "load", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLMEDIAELEMENT_PAUSE, "pause", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.HTMLMEDIAELEMENT_PLAY, "play", 0, c);

        // Instances Object
        s.newObject(INSTANCES);

        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));
        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        switch (nativeObject) {
            case HTMLMEDIAELEMENT_CONSTRUCTOR: {
                return Value.makeObject(INSTANCES);
            }
            case HTMLMEDIAELEMENT_CAN_PLAY_TYPE:
                return Value.makeAnyStr();
            case HTMLMEDIAELEMENT_FAST_SEEK:
                return Value.makeUndef();
            case HTMLMEDIAELEMENT_LOAD:
                return Value.makeUndef();
            case HTMLMEDIAELEMENT_PAUSE:
                return Value.makeUndef();
            case HTMLMEDIAELEMENT_PLAY:
                return Value.makeUndef();
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
