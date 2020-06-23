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

package dk.brics.tajs.analysis.dom.html5;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import java.util.Arrays;
import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.util.Collections.newSet;

public class AudioContext {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();

        // Scraped from google chrome (remove once they are supported):
        Set<String> scrapedFunctionPropertyNames = newSet(Arrays.asList("createBuffer", "decodeAudioData", "createBufferSource", "createMediaElementSource", "createMediaStreamSource", "createMediaStreamDestination", "createGain", "createDelay", "createBiquadFilter", "createWaveShaper", "createPanner", "createConvolver", "createDynamicsCompressor", "createPeriodicWave", "createChannelSplitter", "createChannelMerger", "startRendering"));

        CONSTRUCTOR = ObjectLabel.make(DOMObjects.AUDIOCONTEXT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.AUDIOCONTEXT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.AUDIOCONTEXT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "AudioContext", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(INSTANCES, "currentTime", Value.makeAnyNum().setReadOnly(), c);
        createDOMProperty(INSTANCES, "destination", Value.makeObject(AudioDestinationNode.INSTANCES).setReadOnly(), c);
        // TODO support AudioListeners: createDOMProperty(INSTANCES, "listener", Value.makeObject(AudioListener.INSTANCES).setReadOnly(), c);
        createDOMProperty(INSTANCES, "sampleRate", Value.makeAnyNum().setReadOnly(), c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        createDOMFunction(PROTOTYPE, DOMObjects.AUDIOCONTEXT_CREATE_ANALYSER, "createAnalyser", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.AUDIOCONTEXT_CREATE_OSCILLATOR, "createOscillator", 0, c);
        createDOMFunction(PROTOTYPE, DOMObjects.AUDIOCONTEXT_CREATE_SCRIPT_PROCESSOR, "createScriptProcessor", 3, c);
        ObjectLabel dummyFunction = ObjectLabel.make(DOMObjects.AUDIOCONTEXT_TAJS_UNSUPPORTED_FUNCTION, ObjectLabel.Kind.FUNCTION);
        for (String propertyName : scrapedFunctionPropertyNames) {
            s.newObject(dummyFunction);
            pv.writePropertyWithAttributes(PROTOTYPE, propertyName, Value.makeObject(dummyFunction).setAttributes(true, false, false));
            pv.writePropertyWithAttributes(dummyFunction, "length", Value.makeAnyNum().setAttributes(true, true, true));
            s.writeInternalPrototype(dummyFunction, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        }
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        switch (nativeObject) {
            case AUDIOCONTEXT_CONSTRUCTOR:
                return Value.makeObject(INSTANCES);
            case AUDIOCONTEXT_CREATE_OSCILLATOR:
                return Value.makeObject(OscillatorNode.INSTANCES);
            case AUDIOCONTEXT_CREATE_ANALYSER:
                throw new AnalysisException("This function (AudioContext.createAnalyser) is not yet supported: " + call.getJSSourceNode().getSourceLocation());
            case AUDIOCONTEXT_CREATE_SCRIPT_PROCESSOR:
                return Value.makeObject(ScriptProcessorNode.INSTANCES);
            case AUDIOCONTEXT_TAJS_UNSUPPORTED_FUNCTION:
                throw new AnalysisException("This function from AudioContext is not yet supported: " + call.getJSSourceNode().getSourceLocation());
            default: {
                throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
