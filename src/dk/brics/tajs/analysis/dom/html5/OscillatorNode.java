package dk.brics.tajs.analysis.dom.html5;

import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class OscillatorNode {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(State s) {

        CONSTRUCTOR = new ObjectLabel(DOMObjects.OSCILLATORNODE_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.OSCILLATORNODE_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.OSCILLATORNODE_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "OscillatorNode", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(AudioNode.PROTOTYPE));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(s, INSTANCES, "frequency", Value.makeObject(AudioParam.INSTANCES));
        createDOMProperty(s, INSTANCES, "detune", Value.makeObject(AudioParam.INSTANCES));
        createDOMProperty(s, INSTANCES, "type", Value.makeAnyStr(/* sine, square, sawtooth, triangle, custom */));

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        createDOMFunction(s, PROTOTYPE, DOMObjects.OSCILLATORNODE_START, "start", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.OSCILLATORNODE_STOP, "stop", 0);
        createDOMFunction(s, PROTOTYPE, DOMObjects.OSCILLATORNODE_SET_PERIODIC_WAVE, "setPeriodicWave", 1);
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            case OSCILLATORNODE_CONSTRUCTOR:
                Exceptions.throwTypeError(s, c);
                s.setToNone();
            case OSCILLATORNODE_START:
            case OSCILLATORNODE_STOP:
                return Value.makeUndef();
            case OSCILLATORNODE_SET_PERIODIC_WAVE:
                throw new AnalysisException("This function from OscillatorNode is not yet supported: " + call.getJSSourceNode().getSourceLocation());
            default: {
                throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
