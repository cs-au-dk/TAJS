/*
 * Copyright 2009-2013 Aarhus University
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

import static dk.brics.tajs.util.Collections.newSet;

import java.util.Set;

import org.apache.log4j.Logger;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;

public class DOMEventLoop {

	private static Logger logger = Logger.getLogger(DOMEventLoop.class); 

	// NB: IF you modify something here, you should most likely change it in multipleNondeterministicEventLoops too!
    public static void singleNondeterministicEventLoop(EventDispatcherNode n, State state, Solver.SolverInterface c) {
        if (n.getType() == EventDispatcherNode.Type.SINGLE) {
            // States
            final State loadState = state.clone();
            final State unloadState = state.clone();
            final State keyboardState = state.clone();
            final State mouseState = state.clone();
            final State ajaxState = state.clone();
            final State unknownState = state.clone();
            final State timeoutState = state.clone();

            Set<ObjectLabel> labels = newSet();
            labels.addAll(loadState.getExtras().getFromMaySet(DOMRegistry.MaySets.LOAD_EVENT_HANDLER.name()));
            labels.addAll(loadState.getExtras().getFromMustSet(DOMRegistry.MustSets.LOAD_EVENT_HANDLER.name()));

            // Load Events
            c.setCurrentState(loadState); // TODO: all these calls to setCurrentState shouldn't be necessary (see also multipleNondeterministicEventLoops)
            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(labels), null),
                    loadState, c);

            // Unload Events
            c.setCurrentState(unloadState);
            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(unloadState.getExtras().getFromMaySet(DOMRegistry.MaySets.UNLOAD_EVENT_HANDLERS.name())), null),
                    unloadState, c);

            // Keyboard Events
            Value keyboardEvent;
            if (Options.isSingleEventHandlerType()) {
                keyboardEvent = DOMEvents.createAnyEvent();
            } else {
                keyboardEvent = DOMEvents.createAnyKeyboardEvent();
            }
            keyboardState.writeProperty(DOMWindow.WINDOW, "event", keyboardEvent);
            c.setCurrentState(keyboardState);
            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(keyboardState.getExtras().getFromMaySet(DOMRegistry.MaySets.KEYBOARD_EVENT_HANDLER.name())), keyboardEvent),
                    keyboardState, c);

            // Mouse Events
            Value mouseEvent;
            if (Options.isSingleEventHandlerType()) {
                mouseEvent = DOMEvents.createAnyEvent();
            } else {
                mouseEvent = DOMEvents.createAnyMouseEvent();
            }
            mouseState.writeProperty(DOMWindow.WINDOW, "event", mouseEvent);
            c.setCurrentState(mouseState);
            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(mouseState.getExtras().getFromMaySet(DOMRegistry.MaySets.MOUSE_EVENT_HANDLER.name())), mouseEvent),
                    mouseState, c);

            // AJAX Events
            Value ajaxEvent;
            if (Options.isSingleEventHandlerType()) {
                ajaxEvent = DOMEvents.createAnyEvent();
            } else {
                ajaxEvent = DOMEvents.createAnyAjaxEvent();
            }
            ajaxState.writeProperty(DOMWindow.WINDOW, "event", ajaxEvent);
            c.setCurrentState(ajaxState);
            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(ajaxState.getExtras().getFromMaySet(DOMRegistry.MaySets.AJAX_EVENT_HANDLER.name())), ajaxEvent),
                    ajaxState, c);

            // Unknown Event Handlers
            Value anyEvent = DOMEvents.createAnyEvent();
            unknownState.writeProperty(DOMWindow.WINDOW, "event", anyEvent);

            c.setCurrentState(unknownState);
            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(unknownState.getExtras().getFromMaySet(DOMRegistry.MaySets.UNKNOWN_EVENT_HANDLERS.name())), anyEvent),
                    unknownState, c);

            // Timeout
            c.setCurrentState(timeoutState);
            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(timeoutState.getExtras().getFromMaySet(DOMRegistry.MaySets.TIMEOUT_EVENT_HANDLERS.name())), null),
                    timeoutState, c);

            // Return state to its original
            c.setCurrentState(state);

            // Join states
//            state.propagate(loadState);
//            state.propagate(unloadState);
//            state.propagate(keyboardState);
//            state.propagate(mouseState);
//            state.propagate(ajaxState);
//            state.propagate(unknownState);
//            state.propagate(timeoutState);
        }
    }

    public static void multipleNondeterministicEventLoops(EventDispatcherNode n, State state, Solver.SolverInterface c) {
        if (n.getType() == EventDispatcherNode.Type.SINGLE) {
            throw new IllegalStateException();
        }

        // Load Event Handlers
        if (n.getType() == EventDispatcherNode.Type.LOAD) {
            // Debugging

            Set<ObjectLabel> labels = newSet();
            labels.addAll(state.getExtras().getFromMaySet(DOMRegistry.MaySets.LOAD_EVENT_HANDLER.name()));
            labels.addAll(state.getExtras().getFromMustSet(DOMRegistry.MustSets.LOAD_EVENT_HANDLER.name()));

            if (Options.isDebugEnabled()) {
                for (ObjectLabel l : labels) {
                    logger.debug("LoadEventHandler: " + l);
                }
            }

            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(labels), null),
                    state, c);
        }

        // Unload Event Handlers
        if (n.getType() == EventDispatcherNode.Type.UNLOAD) {
            // Debugging
            if (Options.isDebugEnabled()) {
                for (ObjectLabel l : state.getExtras().getFromMaySet(DOMRegistry.MaySets.UNLOAD_EVENT_HANDLERS.name())) {
                	logger.debug("UnloadEventHandler: " + l);
                }
            }

            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(state.getExtras().getFromMaySet(DOMRegistry.MaySets.UNLOAD_EVENT_HANDLERS.name())), null),
                    state, c);
        }

        // Other Event Handlers
        if (n.getType() == EventDispatcherNode.Type.OTHER) {
            // Debugging
            if (Options.isDebugEnabled()) {
                for (ObjectLabel l : state.getExtras().getFromMaySet(DOMRegistry.MaySets.KEYBOARD_EVENT_HANDLER.name())) {
                	logger.debug("KeyboardEventHandler: " + l);
                }
                for (ObjectLabel l : state.getExtras().getFromMaySet(DOMRegistry.MaySets.MOUSE_EVENT_HANDLER.name())) {
                	logger.debug("MouseEventHandler: " + l);
                }
                for (ObjectLabel l : state.getExtras().getFromMaySet(DOMRegistry.MaySets.AJAX_EVENT_HANDLER.name())) {
                	logger.debug("AjaxEventHandlers: " + l);
                }
                for (ObjectLabel l : state.getExtras().getFromMaySet(DOMRegistry.MaySets.UNKNOWN_EVENT_HANDLERS.name())) {
                	logger.debug("UnknownEventHandler: " + l);
                }
                for (ObjectLabel l : state.getExtras().getFromMaySet(DOMRegistry.MaySets.TIMEOUT_EVENT_HANDLERS.name())) {
                	logger.debug("TimeoutEventHandler: " + l);
                }
            }

            // States
            final State keyboardState = state.clone();
            final State mouseState = state.clone();
            final State ajaxState = state.clone();
            final State unknownState = state.clone();
            final State timeoutState = state.clone(); // TODO: cloning states is expensive - only do it if the states are actually needed (similar for the other state manipulation below) - see also singleNondeterministicEventLoop

            // Keyboard Events
            Value keyboardEvent;
            if (Options.isSingleEventHandlerType()) {
                keyboardEvent = DOMEvents.createAnyEvent();
            } else {
                keyboardEvent = DOMEvents.createAnyKeyboardEvent();
            }
            keyboardState.writeProperty(DOMWindow.WINDOW, "event", keyboardEvent);
            c.setCurrentState(keyboardState);
            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(keyboardState.getExtras().getFromMaySet(DOMRegistry.MaySets.KEYBOARD_EVENT_HANDLER.name())), keyboardEvent),
                    keyboardState, c);

            // Mouse Events
            Value mouseEvent;
            if (Options.isSingleEventHandlerType()) {
                mouseEvent = DOMEvents.createAnyEvent();
            } else {
                mouseEvent = DOMEvents.createAnyMouseEvent();
            }
            mouseState.writeProperty(DOMWindow.WINDOW, "event", mouseEvent);
            c.setCurrentState(mouseState);
            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(mouseState.getExtras().getFromMaySet(DOMRegistry.MaySets.MOUSE_EVENT_HANDLER.name())), mouseEvent),
                    mouseState, c);

            // AJAX Events
            Value ajaxEvent;
            if (Options.isSingleEventHandlerType()) {
                ajaxEvent = DOMEvents.createAnyEvent();
            } else {
                ajaxEvent = DOMEvents.createAnyAjaxEvent();
            }
            ajaxState.writeProperty(DOMWindow.WINDOW, "event", ajaxEvent);
            c.setCurrentState(ajaxState);
            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(ajaxState.getExtras().getFromMaySet(DOMRegistry.MaySets.AJAX_EVENT_HANDLER.name())), ajaxEvent),
                    ajaxState, c);

            // Unknown Event Handlers
            Value anyEvent = DOMEvents.createAnyEvent();
            unknownState.writeProperty(DOMWindow.WINDOW, "event", anyEvent);
            c.setCurrentState(unknownState);
            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(unknownState.getExtras().getFromMaySet(DOMRegistry.MaySets.UNKNOWN_EVENT_HANDLERS.name())), anyEvent),
                    unknownState, c);

            // Timeout
            c.setCurrentState(timeoutState);
            FunctionCalls.callFunction(
                    new FunctionCalls.EventHandlerCall(n, Value.makeObject(timeoutState.getExtras().getFromMaySet(DOMRegistry.MaySets.TIMEOUT_EVENT_HANDLERS.name())), null),
                    timeoutState, c);

            // Return state to its original
            c.setCurrentState(state);

            // Join states
// TODO: why join the states into 'state'? (commented out - also in singleNondeterministicEventLoop)
//            state.propagate(keyboardState);
//            state.propagate(mouseState);
//            state.propagate(ajaxState);
//            state.propagate(unknownState);
//            state.propagate(timeoutState);
        }
    }

}
