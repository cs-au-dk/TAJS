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

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class HTMLAudioElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = new ObjectLabel(DOMObjects.HTMLAUDIOELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = new ObjectLabel(DOMObjects.HTMLAUDIOELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.HTMLAUDIOELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "Audio", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(HTMLMediaElement.PROTOTYPE));

        // Instances Object
        s.newObject(INSTANCES);
        createDOMProperty(INSTANCES, "autoplay", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "autobuffer", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "buffered", Value.makeObject(TimeRanges.INSTANCES), c);
        createDOMProperty(INSTANCES, "controls", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "loop", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "mozCurrentSampleOffset", Value.makeAnyNum(), c);
        createDOMProperty(INSTANCES, "muted", Value.makeAnyBool(), c);
        createDOMProperty(INSTANCES, "played", Value.makeObject(TimeRanges.INSTANCES), c);
        createDOMProperty(INSTANCES, "preload", Value.makeAnyStrNotUInt(), c);
        createDOMProperty(INSTANCES, "src", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "volume", Value.makeAnyNum(), c);

        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));
        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        switch (nativeObject) {
            case HTMLAUDIOELEMENT_CONSTRUCTOR: {
                return Value.makeObject(INSTANCES);
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
