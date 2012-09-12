/*
 * Copyright 2012 Aarhus University
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

package dk.brics.tajs.analysis.dom.event;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;

import org.apache.log4j.Logger;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMEvents;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.analysis.dom.core.DOMNode;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

/**
 * The EventTarget interface is implemented by all Nodes in an implementation
 * which supports the DOM Event Model. Therefore, this interface can be obtained
 * by using binding-specific casting methods on an instance of the Node
 * interface. The interface allows registration and removal of EventListeners on
 * an EventTarget and dispatch of events to that EventTarget.
 */
public class EventTarget {

	private static Logger logger = Logger.getLogger(EventTarget.class); 

	public static void build(State s) {
        // Event target has no native object... see class comment.

        /*
         * Properties.
         */
        // No properties.

        /*
         * Functions.
         */
        createDOMFunction(s, DOMNode.PROTOTYPE, DOMObjects.EVENT_TARGET_ADD_EVENT_LISTENER, "addEventListener", 3);
        createDOMFunction(s, DOMNode.PROTOTYPE, DOMObjects.EVENT_TARGET_REMOVE_EVENT_LISTENER, "removeEventListener", 3);
        createDOMFunction(s, DOMNode.PROTOTYPE, DOMObjects.EVENT_TARGET_DISPATCH_EVENT, "dispatchEvent", 1);

        // DOM LEVEL 0
        createDOMFunction(s, DOMWindow.WINDOW, DOMObjects.WINDOW_ADD_EVENT_LISTENER, "addEventListener", 3);
        createDOMFunction(s, DOMWindow.WINDOW, DOMObjects.WINDOW_REMOVE_EVENT_LISTENER, "removeEventListener", 3);
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            /*
             * Events added with useCapture = true must be removed
             * separately from events added with useCapture = false. Model this?
             */
            case EVENT_TARGET_ADD_EVENT_LISTENER:
            case WINDOW_ADD_EVENT_LISTENER: {
                NativeFunctions.expectParameters(nativeObject, call, c, 3, 3);
                Value type = Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                Value function = NativeFunctions.readParameter(call, s, 1);
                /* Value useCapture =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 2));
                if (type.isMaybeSingleStr()) {
                    DOMEvents.addEventHandler(s, type.getStr(), function);
                } else {
                    DOMEvents.addUnknownEventHandler(s, function.getObjectLabels());
                }
                return Value.makeUndef();
            }
            case EVENT_TARGET_REMOVE_EVENT_LISTENER:
            case WINDOW_REMOVE_EVENT_LISTENER: {
                NativeFunctions.expectParameters(nativeObject, call, c, 3, 3);
                Value type = Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                Value function = DOMConversion.toEventHandler(NativeFunctions.readParameter(call, s, 1), c);
                /* Value useCapture =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 2));
                // FIXME: testUneval29 triggers this message.

                logger.warn("WARN: EVENT_TARGET_REMOVE_EVENT_LISTENER not implemented.");

                return Value.makeUndef();
            }
            case EVENT_TARGET_DISPATCH_EVENT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                logger.warn("WARN: EVENT_TARGET_DISPATCH_EVENT not implemented.");
                return Value.makeUndef();
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }

}
