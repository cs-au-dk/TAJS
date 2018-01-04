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

package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.EventType;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

public class DOMEvents {

    private static final Logger log = Logger.getLogger(DOMEvents.class);

    private static final Value domContentLoadedEvent;

    private static final Value loadEvent;

    private static final Value keyboardEvent;

    private static final Value mouseEvent;

    private static final Value ajaxEvent;

    private static final Value anyEvent;

    private static final Value timeoutEvent;

    private static final Value unloadEvent;

    static {
        anyEvent = createAnyEvent();
        domContentLoadedEvent = createAnyDOMContentLoadedEvent();
        loadEvent = createAnyLoadEvent();
        timeoutEvent = Value.makeNone();
        unloadEvent = Value.makeNone();
        if (Options.get().isSingleEventHandlerType()) {
            keyboardEvent = anyEvent;
        } else {
            keyboardEvent = createAnyKeyboardEvent();
        }
        if (Options.get().isSingleEventHandlerType()) {
            mouseEvent = anyEvent;
        } else {
            mouseEvent = createAnyMouseEvent();
        }
        if (Options.get().isSingleEventHandlerType()) {
            ajaxEvent = anyEvent;
        } else {
            ajaxEvent = createAnyAjaxEvent();
        }
    }

    /**
     * Create generic Keyboard Event.
     */
    private static Value createAnyKeyboardEvent() {
        return Value.makeObject(DOMRegistry.getKeyboardEventLabel());
    }

    /**
     * Create generic Mouse Event.
     */
    private static Value createAnyMouseEvent() {
        return Value.makeObject(DOMRegistry.getMouseEventLabel());
    }

    /**
     * Creates a generic AJAX Event.
     */
    private static Value createAnyAjaxEvent() {
        return Value.makeObject(DOMRegistry.getAjaxEventLabel());
    }

    /**
     * Creates a generic DOMContentLoaded Event
     */
    private static Value createAnyDOMContentLoadedEvent() {
        return Value.makeObject(DOMRegistry.getDOMContentLoadedEventLabel());
    }

    /**
     * Creates a generic load Event
     */
    private static Value createAnyLoadEvent() {
        return Value.makeObject(DOMRegistry.getLoadEventLabel());
    }

    /**
     * Create a generic non-mouse, non-keyboard Event.
     */
    private static Value createAnyEvent() {
        Set<ObjectLabel> labels = Collections.newSet();
        labels.add(DOMRegistry.getHashChangeEventLabel()); // NB: this event does not have its own field in this class. That is deliberate since there currently is no EventType.HASH_CHANGE event type.
        labels.add(DOMRegistry.getKeyboardEventLabel());
        labels.add(DOMRegistry.getMouseEventLabel());
        labels.add(DOMRegistry.getMutationEventLabel());
        labels.add(DOMRegistry.getWheelEventLabel());
        labels.add(DOMRegistry.getTouchEventLabel());
        return Value.makeObject(labels);
    }

    /**
     * Add Event Handler. (NOT Timeout Event Handlers.)
     */
    public static void addEventHandler(Value handler, EventType kind, Solver.SolverInterface c) {
        Collection<ObjectLabel> labels = toEventHandler(handler, c);
        DOMRegistry.MaySets key = mapEventTypeToMaySetKey(kind);
        c.getMonitoring().visitEventHandlerRegistration(c.getNode(), c.getState().getContext(), Value.makeObject(newSet(labels)));
        c.getState().getExtras().addToMaySet(key.name(), labels);
    }

    private static DOMRegistry.MaySets mapEventTypeToMaySetKey(EventType kind) {
        switch (kind) {
            case DOM_CONTENT_LOADED:
                return DOMRegistry.MaySets.DOM_CONTENT_LOADED_EVENT_HANDLER;
            case LOAD:
                return DOMRegistry.MaySets.LOAD_EVENT_HANDLER;
            case UNLOAD:
                return DOMRegistry.MaySets.UNLOAD_EVENT_HANDLERS;
            case KEYBOARD:
                return DOMRegistry.MaySets.KEYBOARD_EVENT_HANDLER;
            case MOUSE:
                return DOMRegistry.MaySets.MOUSE_EVENT_HANDLER;
            case AJAX:
                return DOMRegistry.MaySets.AJAX_EVENT_HANDLER;
            case TIMEOUT:
                return DOMRegistry.MaySets.TIMEOUT_EVENT_HANDLERS;
            case OTHER: // treat as unknown
            case UNKNOWN:
                return DOMRegistry.MaySets.UNKNOWN_EVENT_HANDLERS;
            default:
                throw new AnalysisException("Unknown event handler type");
        }
    }

    /**
     * Converts the given value to an EventHandler value.
     */
    private static Set<ObjectLabel> toEventHandler(Value value, Solver.SolverInterface c) {
        value = UnknownValueResolver.getRealValue(value, c.getState());
        Set<ObjectLabel> handlers = Collections.newSet();
        boolean maybeNonFunction = value.isMaybePrimitive();
        for (ObjectLabel objectLabel : value.getObjectLabels()) {
            if (objectLabel.getKind() == ObjectLabel.Kind.FUNCTION) {
                handlers.add(objectLabel);
            } else {
                maybeNonFunction = true;
            }
        }
        if (maybeNonFunction) {
            c.getMonitoring().addMessage(c.getNode(), Message.Severity.HIGH, "TypeError, non-function event handler");
        }
        return handlers;
    }

    private static void triggerEventHandler(AbstractNode currentNode, State currentState, EventType type, boolean requiresStateCloning, Solver.SolverInterface c, Set<ObjectLabel> handlers) {
        Value event = getEvent(type);
        if (Options.get().isUserEventsDisabled() && type.isUserEvent()) {
            return;
        }
        if (handlers.isEmpty()) {
            return;
        }
        State callState = requiresStateCloning ? currentState.clone() : currentState;
        c.withState(callState, () -> {
            for (ObjectLabel l : handlers) {
                log.debug("Triggering eventHandlers <" + type + ">: " + l);
            }
            if (!event.isNone()) {
                // Support the unofficial window.event property that is set by the browser
                PropVarOperations pv = c.getAnalysis().getPropVarOperations();
                pv.writeProperty(DOMWindow.WINDOW, "event", event); // strong write to override old value
                pv.deleteProperty(singleton(DOMWindow.WINDOW), Value.makeTemporaryStr("event"), true); // weak delete to emulate unofficial
            }
            Set<ObjectLabel> thisTargets;
            if (type == EventType.TIMEOUT) {
                thisTargets = singleton(InitialStateBuilder.GLOBAL);
            } else {
                thisTargets = DOMBuilder.getAllDOMEventTargets();
            }
            List<Value> args = event.isNone() ? newList() : Collections.singletonList(event);
            FunctionCalls.callFunction(new FunctionCalls.EventHandlerCall(currentNode, Value.makeObject(handlers), args, thisTargets, callState), c);
        });
    }

    public static Value getEvent(EventType type) {
        switch (type) {
            case DOM_CONTENT_LOADED:
                return domContentLoadedEvent;
            case LOAD:
                return loadEvent;
            case UNLOAD:
                return unloadEvent;
            case KEYBOARD:
                return keyboardEvent;
            case MOUSE:
                return mouseEvent;
            case AJAX:
                return ajaxEvent;
            case TIMEOUT:
                return timeoutEvent;
            case OTHER:
            case UNKNOWN:
                return anyEvent;
            default:
                throw new AnalysisException("Unhandleded case: " + type);
        }
    }

    private static void triggerEventHandler(EventType type, Solver.SolverInterface c) {
        boolean requiresStateCloning = type == EventType.DOM_CONTENT_LOADED || type == EventType.LOAD || type == EventType.UNLOAD;
        DOMRegistry.MaySets maysetKey = convertEventTypeToHandlerKey(type);
        triggerEventHandler(c.getNode(), c.getState(), type, requiresStateCloning, c, c.getState().getExtras().getFromMaySet(maysetKey.name()));
    }

    private static DOMRegistry.MaySets convertEventTypeToHandlerKey(EventType type) {
        switch (type) {
            case DOM_CONTENT_LOADED:
                return DOMRegistry.MaySets.DOM_CONTENT_LOADED_EVENT_HANDLER;
            case LOAD:
                return DOMRegistry.MaySets.LOAD_EVENT_HANDLER;
            case UNLOAD:
                return DOMRegistry.MaySets.UNLOAD_EVENT_HANDLERS;
            case KEYBOARD:
                return DOMRegistry.MaySets.KEYBOARD_EVENT_HANDLER;
            case MOUSE:
                return DOMRegistry.MaySets.MOUSE_EVENT_HANDLER;
            case UNKNOWN:
                return DOMRegistry.MaySets.UNKNOWN_EVENT_HANDLERS;
            case OTHER:
                return DOMRegistry.MaySets.UNKNOWN_EVENT_HANDLERS; // TODO improve precision
            case AJAX:
                return DOMRegistry.MaySets.AJAX_EVENT_HANDLER;
            case TIMEOUT:
                return DOMRegistry.MaySets.TIMEOUT_EVENT_HANDLERS;
            default:
                throw new AnalysisException("Unhandled case: " + type);
        }
    }

    public static void emit(EventDispatcherNode n, Solver.SolverInterface c) {
        if (n.getType() == EventDispatcherNode.Type.DOM_CONTENT_LOADED) {
            triggerEventHandler(EventType.DOM_CONTENT_LOADED, c);
        }
        if (n.getType() == EventDispatcherNode.Type.DOM_LOAD) {
            triggerEventHandler(EventType.LOAD, c);
        }
        if (n.getType() == EventDispatcherNode.Type.DOM_UNLOAD) {
            triggerEventHandler(EventType.UNLOAD, c);
        }
        if (n.getType() == EventDispatcherNode.Type.DOM_OTHER) {
            triggerEventHandler(EventType.KEYBOARD, c);
            triggerEventHandler(EventType.MOUSE, c);
            triggerEventHandler(EventType.AJAX, c);
            triggerEventHandler(EventType.UNKNOWN, c);
            triggerEventHandler(EventType.OTHER, c);
            triggerEventHandler(EventType.TIMEOUT, c);
        }
    }
}
