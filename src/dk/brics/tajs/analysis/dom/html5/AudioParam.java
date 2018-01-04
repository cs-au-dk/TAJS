/*
 * Copyright 2009-2018 Aarhus University
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

import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisLimitationException;

import java.util.Arrays;
import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.util.Collections.newSet;

public class AudioParam {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();

        // Scraped from google chrome:
        Set<String> scrapedFunctionPropertyNames = newSet(Arrays.asList("setValueAtTime", "linearRampToValueAtTime", "exponentialRampToValueAtTime", "setTargetAtTime", "setValueCurveAtTime", "cancelScheduledValues"));

        CONSTRUCTOR = ObjectLabel.make(DOMObjects.AUDIOPARAM_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.AUDIOPARAM_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.AUDIOPARAM_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "AudioParam", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(INSTANCES, "value", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "defaultValue", Value.makeAnyNumUInt().setReadOnly(), c);

        s.multiplyObject(INSTANCES);
        // FIXME AudioParam is an interface, GainNode is an example of an (yet unmodelled) instance (GitHub #408)
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */

        ObjectLabel dummyFunction = ObjectLabel.make(DOMObjects.AUDIOPARAM_TAJS_UNSUPPORTED_FUNCTION, ObjectLabel.Kind.FUNCTION);
        for (String propertyName : scrapedFunctionPropertyNames) {
            // explicit implementation of: createDOMFunction(PROTOTYPE, DOMObjects.WEBGLRENDERINGCONTEXT_SOME_FUNCTION, propertyName, ***Value.makeAnyNum()***);
            s.newObject(dummyFunction);
            pv.writePropertyWithAttributes(PROTOTYPE, propertyName, Value.makeObject(dummyFunction).setAttributes(true, false, false));
            pv.writePropertyWithAttributes(dummyFunction, "length", Value.makeAnyNum().setAttributes(true, true, true));
            s.writeInternalPrototype(dummyFunction, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        }
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case AUDIOPARAM_CONSTRUCTOR:
                Exceptions.throwTypeError(c);
                s.setToBottom();
            case AUDIOPARAM_TAJS_UNSUPPORTED_FUNCTION:
                throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getJSSourceNode().getSourceLocation() + ": This function from AudioParam is not yet supported: " + call.getJSSourceNode().getSourceLocation());
            default: {
                throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
