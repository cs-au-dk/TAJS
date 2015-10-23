/*
 * Copyright 2009-2015 Aarhus University
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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.html.HTMLElement;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

public class HTMLCanvasElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(State s) {
        CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLCANVASELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.HTMLCANVASELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.HTMLCANVASELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "HTMLCanvasElement", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLCANVASELEMENT_GET_CONTEXT, "getContext", 2);
        createDOMFunction(s, PROTOTYPE, DOMObjects.HTMLCANVASELEMENT_TO_DATA_URL, "toDataURL", 1);
        createDOMProperty(s, CanvasRenderingContext2D.CONTEXT2D_PROTOTYPE, "canvas", Value.makeObject(HTMLCanvasElement.CONSTRUCTOR));
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE));

        // Instances Object
        s.newObject(INSTANCES);
        createDOMProperty(s, INSTANCES, "height", Value.makeAnyNumUInt());
        createDOMProperty(s, INSTANCES, "width", Value.makeAnyNumUInt());
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));
        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();
    }

    public static Value evaluate (DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case HTMLCANVASELEMENT_GET_CONTEXT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 2);

                Value arg;
                if (call.isUnknownNumberOfArgs()) {
                    arg = NativeFunctions.readUnknownParameter(call);
                } else {
                    arg = NativeFunctions.readParameter(call, s, 0);
                    /* unused for now */
                    NativeFunctions.readParameter(call, s, 1);
                }

                List<Value> results = newList();

                arg = Conversion.toString(arg, c);

                Set<String> contextNames = newSet(Arrays.asList("2d", "webgl", "experimental-webgl"));
                if (arg.isMaybeStr("2d")) {
                    results.add(Value.makeObject(CanvasRenderingContext2D.CONTEXT2D));
                }

                if (arg.isMaybeStr("webgl") || arg.isMaybeStr("experimental-webgl")) {
                    results.add(Value.makeObject(WebGLRenderingContext.INSTANCES));
                    // not always supported
                    results.add(Value.makeNull());
                }

                if(!arg.isMaybeSingleStr() || !contextNames.contains(arg.getStr())){
                    results.add(Value.makeNull());
                }

                return Value.join(results);
            }
            case HTMLCANVASELEMENT_TO_DATA_URL: {
                return Value.makeAnyStr();
            }
            case HTMLCANVASELEMENT_CONSTRUCTOR: {
                return Value.makeObject(INSTANCES);
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
