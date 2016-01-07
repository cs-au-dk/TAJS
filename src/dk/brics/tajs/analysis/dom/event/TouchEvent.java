package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMRegistry;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class TouchEvent {
	public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PROTOTYPE = new ObjectLabel(DOMObjects.TOUCH_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.TOUCH_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(UIEvent.PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(INSTANCES, "altKey", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(INSTANCES, "ctrlKey", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(INSTANCES, "metaKey", Value.makeAnyBool().setReadOnly(), c);
        createDOMProperty(INSTANCES, "shiftKey", Value.makeAnyBool().setReadOnly(), c);
        
        // Multiplied Object
        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        // DOM Registry
        DOMRegistry.registerTouchEventLabel(INSTANCES);
    }
}
