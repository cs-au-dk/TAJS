/*
 * Copyright 2009-2016 Aarhus University
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

package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collections;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.singleton;

public class DOMEventLoop {

    private static Logger log = Logger.getLogger(DOMEventLoop.class);

    private static DOMEventLoop instance;

    private final Value loadEvent;

    private final Value keyboardEvent;

    private final Value mouseEvent;

    private final Value ajaxEvent;

    private final Value anyEvent;

    private DOMEventLoop() {
        anyEvent = DOMEvents.createAnyEvent();

        loadEvent = DOMEvents.createAnyLoadEvent();

        if (Options.get().isSingleEventHandlerType()) {
            keyboardEvent = anyEvent;
        } else {
            keyboardEvent = DOMEvents.createAnyKeyboardEvent();
        }

        if (Options.get().isSingleEventHandlerType()) {
            mouseEvent = anyEvent;
        } else {
            mouseEvent = DOMEvents.createAnyMouseEvent();
        }

        if (Options.get().isSingleEventHandlerType()) {
            ajaxEvent = anyEvent;
        } else {
            ajaxEvent = DOMEvents.createAnyAjaxEvent();
        }
    }

    public static DOMEventLoop get() {

        if (instance == null) {
            instance = new DOMEventLoop();
        }
        return instance;
    }

    private static void triggerEventHandler(EventDispatcherNode currentNode, State currentState, DOMRegistry.MaySets eventhandlerKind, Value event, boolean requiresStateCloning, Solver.SolverInterface c) {
        Set<ObjectLabel> handlers = currentState.getExtras().getFromMaySet(eventhandlerKind.name());
        if (handlers.isEmpty()) {
            return;
        }
        State callState = requiresStateCloning ? currentState.clone() : currentState;
        c.setState(callState);
        for (ObjectLabel l : handlers) {
            log.debug("Triggering eventHandlers <" + eventhandlerKind + ">: " + l);
        }

        if (event != null) {
            // Support the unofficial window.event property that is set by the browser
            PropVarOperations pv = c.getAnalysis().getPropVarOperations();
            pv.writeProperty(DOMWindow.WINDOW, "event", event); // strong write to override old value
            pv.deleteProperty(singleton(DOMWindow.WINDOW), Value.makeTemporaryStr("event"), true); // weak delete to emulate unofficial
        }

        Set<ObjectLabel> thisTargets;
        if (eventhandlerKind == DOMRegistry.MaySets.TIMEOUT_EVENT_HANDLERS) {
            thisTargets = singleton(InitialStateBuilder.GLOBAL);
        } else {
            thisTargets = DOMBuilder.getAllDOMEventTargets();
        }
        List<Value> args = event == null ? newList() : Collections.singletonList(event);
        FunctionCalls.callFunction(new FunctionCalls.EventHandlerCall(currentNode, Value.makeObject(handlers), args, thisTargets, callState), c);
        c.setState(currentState);
    }

    public void multipleNondeterministicEventLoops(EventDispatcherNode n, Solver.SolverInterface c) {
        State state = c.getState();
        if (n.getType() == EventDispatcherNode.Type.DOM_LOAD) {
            triggerEventHandler(n, state, DOMRegistry.MaySets.LOAD_EVENT_HANDLER, loadEvent, false, c);
        }

        if (n.getType() == EventDispatcherNode.Type.DOM_UNLOAD) {
            triggerEventHandler(n, state, DOMRegistry.MaySets.UNLOAD_EVENT_HANDLERS, null, false, c);
        }

        if (n.getType() == EventDispatcherNode.Type.DOM_OTHER) {
            triggerEventHandler(n, state, DOMRegistry.MaySets.KEYBOARD_EVENT_HANDLER, keyboardEvent, true, c);
            triggerEventHandler(n, state, DOMRegistry.MaySets.MOUSE_EVENT_HANDLER, mouseEvent, true, c);
            triggerEventHandler(n, state, DOMRegistry.MaySets.AJAX_EVENT_HANDLER, ajaxEvent, true, c);
            // not adding precise support for touch events, they are represented in the anyEvent
            triggerEventHandler(n, state, DOMRegistry.MaySets.UNKNOWN_EVENT_HANDLERS, anyEvent, true, c);
            triggerEventHandler(n, state, DOMRegistry.MaySets.TIMEOUT_EVENT_HANDLERS, null, true, c);
        }
    }
}
