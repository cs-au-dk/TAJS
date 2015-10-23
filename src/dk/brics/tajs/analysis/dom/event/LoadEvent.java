package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMRegistry;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class LoadEvent {

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(State s) {
        PROTOTYPE = new ObjectLabel(DOMObjects.LOAD_EVENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.LOAD_EVENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(Event.PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(s, INSTANCES, "type", Value.makeAnyStr().setReadOnly());
        createDOMProperty(s, INSTANCES, "bubbles", Value.makeAnyBool().setReadOnly());
        createDOMProperty(s, INSTANCES, "cancelable", Value.makeAnyBool().setReadOnly());
        createDOMProperty(s, INSTANCES, "detail", Value.makeAnyNum().setReadOnly());
        createDOMProperty(s, INSTANCES, "view", Value.makeObject(DOMWindow.WINDOW).setReadOnly());

        // DOM Registry
        DOMRegistry.registerLoadEventLabel(INSTANCES);
    }
}
