package dk.brics.tajs.analysis.dom.html5;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
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

    public static void build(State s) {

        // Scraped from google chrome (remove once they are supported):
        Set<String> scrapedFunctionPropertyNames = newSet(Arrays.asList("createBuffer", "decodeAudioData", "createBufferSource", "createMediaElementSource", "createMediaStreamSource", "createMediaStreamDestination", "createGain", "createDelay", "createBiquadFilter", "createWaveShaper", "createPanner", "createConvolver", "createDynamicsCompressor", "createPeriodicWave", "createChannelSplitter", "createChannelMerger", "startRendering"));

        CONSTRUCTOR = new ObjectLabel(DOMObjects.AUDIOCONTEXT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.AUDIOCONTEXT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.AUDIOCONTEXT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "AudioContext", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(s, INSTANCES, "currentTime", Value.makeAnyNum().setReadOnly());
        createDOMProperty(s, INSTANCES, "destination", Value.makeObject(AudioDestinationNode.INSTANCES).setReadOnly());
        // TODO support AudioListeners: createDOMProperty(s, INSTANCES, "listener", Value.makeObject(AudioListener.INSTANCES).setReadOnly());
        createDOMProperty(s, INSTANCES, "sampleRate", Value.makeAnyNum().setReadOnly());

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        createDOMFunction(s, PROTOTYPE, DOMObjects.AUDIOCONTEXT_CREATE_ANALYSER, "createAnalyser", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.AUDIOCONTEXT_CREATE_OSCILLATOR, "createOscillator", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.AUDIOCONTEXT_CREATE_SCRIPT_PROCESSOR, "createScriptProcessor", 3);
        ObjectLabel dummyFunction = new ObjectLabel(DOMObjects.AUDIOCONTEXT_TAJS_UNSUPPORTED_FUNCTION, ObjectLabel.Kind.FUNCTION);
        for (String propertyName : scrapedFunctionPropertyNames) {
            s.newObject(dummyFunction);
            s.writePropertyWithAttributes(PROTOTYPE, propertyName, Value.makeObject(dummyFunction).setAttributes(true, false, false));
            s.writePropertyWithAttributes(dummyFunction, "length", Value.makeAnyNum().setAttributes(true, true, true));
            s.writeInternalPrototype(dummyFunction, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        }
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
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
