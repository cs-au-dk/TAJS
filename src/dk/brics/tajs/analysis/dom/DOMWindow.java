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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.html5.MediaQueryList;
import dk.brics.tajs.analysis.dom.style.CSSStyleDeclaration;
import dk.brics.tajs.flowgraph.EventType;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.unevalizer.SimpleUnevalizerAPI;
import dk.brics.tajs.unevalizer.UnevalizerAPI;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.apache.log4j.Logger;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * DOM Window.
 */
public class DOMWindow {

    private static Logger log = Logger.getLogger(DOMWindow.class);

    public static ObjectLabel WINDOW;

    public static ObjectLabel WINDOW_CONSTRUCTOR;

    public static ObjectLabel WINDOW_PROTOTYPE;

    public static ObjectLabel CHROME;

    public static ObjectLabel HISTORY;

    public static ObjectLabel LOCATION;

    public static ObjectLabel NAVIGATOR;

    public static ObjectLabel PERFORMANCE;

    public static ObjectLabel SCREEN;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();

        WINDOW_CONSTRUCTOR = ObjectLabel.make(DOMObjects.WINDOW_CONSTRUCTOR, Kind.FUNCTION);
        WINDOW_PROTOTYPE = ObjectLabel.make(DOMObjects.WINDOW_PROTOTYPE, Kind.OBJECT);
        CHROME = ObjectLabel.make(DOMObjects.WINDOW_CHROME, Kind.OBJECT);
        HISTORY = ObjectLabel.make(DOMObjects.WINDOW_HISTORY, Kind.OBJECT);
        LOCATION = ObjectLabel.make(DOMObjects.WINDOW_LOCATION, Kind.OBJECT);
        NAVIGATOR = ObjectLabel.make(DOMObjects.WINDOW_NAVIGATOR, Kind.OBJECT);
        PERFORMANCE = ObjectLabel.make(DOMObjects.WINDOW_PERFORMANCE, Kind.OBJECT);
        SCREEN = ObjectLabel.make(DOMObjects.WINDOW_SCREEN, Kind.OBJECT);

        // NB: The WINDOW object has already been instantiated.

        // Window constructor Object
        s.newObject(WINDOW_CONSTRUCTOR);
        pv.writePropertyWithAttributes(WINDOW_CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(WINDOW_CONSTRUCTOR, "prototype", Value.makeObject(WINDOW_PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(WINDOW_CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        createDOMProperty(WINDOW, "Window", Value.makeObject(WINDOW_CONSTRUCTOR), c);

        /*
         * Properties.
         */
        // DOM LEVEL 0
        createDOMProperty(WINDOW, "innerHeight", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "innerWidth", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "length", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "name", Value.makeAnyStr(), c);
        createDOMProperty(WINDOW, "opener", Value.makeNull(), c);
        createDOMProperty(WINDOW, "outerHeight", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "outerWidth", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "pageXOffset", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "pageYOffset", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "parent", Value.makeObject(WINDOW), c);
        createDOMProperty(WINDOW, "self", Value.makeObject(WINDOW), c);
        createDOMProperty(WINDOW, "scrollMaxX", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "scrollMaxY", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "scrollX", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "scrollY", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "screenX", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "screenY", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "status", Value.makeAnyStr(), c);
        createDOMProperty(WINDOW, "top", Value.makeObject(WINDOW), c);
        createDOMProperty(WINDOW, "window", Value.makeObject(WINDOW), c);

        createDOMProperty(WINDOW, "onfocus", Value.makeNull(), c);
        createDOMProperty(WINDOW, "ontransitionend", Value.makeNull(), c);
        createDOMProperty(WINDOW, "onwebkittransitionend", Value.makeNull(), c);
        createDOMProperty(WINDOW, "onanimationend", Value.makeNull(), c);
        createDOMProperty(WINDOW, "onwebkitanimationend", Value.makeNull(), c);

        /*
         * Functions.
         */
        // DOM LEVEL 0
        createDOMFunction(WINDOW, DOMObjects.WINDOW_ALERT, "alert", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_ATOB, "atob", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_BACK, "back", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_BLUR, "blur", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_BTOA, "btoa", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_CLEAR_INTERVAL, "clearInterval", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_CLEAR_TIMEOUT, "clearTimeout", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_CLOSE, "close", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_CONFIRM, "confirm", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_DISPATCHEVENT, "dispatchEvent", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_ESCAPE, "escape", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_FOCUS, "focus", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_FORWARD, "forward", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_HOME, "home", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_MAXIMIZE, "maximize", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_MINIMIZE, "minimize", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_MOVEBY, "moveBy", 2, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_MOVETO, "moveTo", 2, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_OPEN, "open", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_PRINT, "print", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_PROMPT, "prompt", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_RESIZEBY, "resizeBy", 2, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_RESIZETO, "resizeTo", 2, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_SCROLL, "scroll", 2, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_SCROLLBY, "scrollBy", 2, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_SCROLLBYLINES, "scrollByLines", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_SCROLLBYPAGES, "scrollByPages", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_SCROLLTO, "scrollTo", 2, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_SET_INTERVAL, "setInterval", 2, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_SET_TIMEOUT, "setTimeout", 2, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_STOP, "stop", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_UNESCAPE, "unescape", 1, c);

        // DOM LEVEL 2
        createDOMFunction(WINDOW, DOMObjects.WINDOW_GET_COMPUTED_STYLE, "getComputedStyle", 0, c);

        createDOMFunction(WINDOW, DOMObjects.WINDOW_CANCEL_ANIM_FRAME, "cancelAnimFrame", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_CANCEL_ANIMATION_FRAME, "cancelAnimationFrame", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_REQUEST_ANIMATION_FRAME, "requestAnimationFrame", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_WEBKIT_REQUEST_ANIMATION_FRAME, "webkitRequestAnimationFrame", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_MATCH_MEDIA, "matchMedia", 1, c);

        /*
         * WINDOW CHROME object
         */
        s.newObject(CHROME);
        s.writeInternalPrototype(CHROME, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Properties
        createDOMProperty(WINDOW, "chrome", Value.makeObject(CHROME), c);

        // Functions
        createDOMFunction(CHROME, DOMObjects.WINDOW_CHROME_LOAD_TIMES, "loadTimes", 0, c);

        /*
         * WINDOW HISTORY object
         */
        s.newObject(HISTORY);
        s.writeInternalPrototype(HISTORY, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        // Properties.
        createDOMProperty(HISTORY, "length", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "history", Value.makeObject(HISTORY), c);
        // Functions.
        createDOMFunction(HISTORY, DOMObjects.WINDOW_HISTORY_BACK, "back", 0, c);
        createDOMFunction(HISTORY, DOMObjects.WINDOW_HISTORY_FORWARD, "forward", 0, c);
        createDOMFunction(HISTORY, DOMObjects.WINDOW_HISTORY_GO, "go", 1, c);
        createDOMFunction(HISTORY, DOMObjects.WINDOW_HISTORY_PUSH_STATE, "pushState", 3, c);
        createDOMFunction(HISTORY, DOMObjects.WINDOW_HISTORY_REPLACE_STATE, "replaceState", 3, c);

        /*
         * WINDOW LOCATION object
         */
        s.newObject(LOCATION);
        s.writeInternalPrototype(LOCATION, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        createDOMProperty(WINDOW, "location", Value.makeObject(LOCATION), c);

        // Properties.
        createDOMProperty(LOCATION, "hash", Value.makeAnyStr(), c);
        createDOMProperty(LOCATION, "host", Value.makeAnyStr(), c);
        createDOMProperty(LOCATION, "hostname", Value.makeAnyStr(), c);
        createDOMProperty(LOCATION, "href", Value.makeAnyStr(), c);
        createDOMProperty(LOCATION, "origin", Value.makeAnyStr(), c);
        createDOMProperty(LOCATION, "pathname", Value.makeAnyStr(), c);
        createDOMProperty(LOCATION, "port", Value.makeAnyStr(), c);
        createDOMProperty(LOCATION, "protocol", Value.makeAnyStr(), c);
        createDOMProperty(LOCATION, "search", Value.makeAnyStr(), c);
        createDOMProperty(LOCATION, "hash", Value.makeAnyStr(), c);
        // Functions.
        createDOMFunction(LOCATION, DOMObjects.WINDOW_LOCATION_ASSIGN, "assign", 1, c);
        createDOMFunction(LOCATION, DOMObjects.WINDOW_LOCATION_RELOAD, "reload", 1, c);
        createDOMFunction(LOCATION, DOMObjects.WINDOW_LOCATION_REPLACE, "replace", 1, c);
        createDOMFunction(LOCATION, DOMObjects.WINDOW_LOCATION_TOSTRING, "toString", 0, c);

        /*
         * WINDOW NAVIGATOR object
         */
        s.newObject(NAVIGATOR);
        s.writeInternalPrototype(NAVIGATOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        createDOMProperty(WINDOW, "navigator", Value.makeObject(NAVIGATOR), c);

        // Properties.
        createDOMProperty(NAVIGATOR, "appName", Value.makeAnyStr(), c);
        createDOMProperty(NAVIGATOR, "appVersion", Value.makeAnyStr(), c);
        createDOMProperty(NAVIGATOR, "language", Value.makeAnyStr(), c);
        createDOMProperty(NAVIGATOR, "platform", Value.makeAnyStr(), c);
        createDOMProperty(NAVIGATOR, "product", Value.makeAnyStr(), c);
        createDOMProperty(NAVIGATOR, "userAgent", Value.makeAnyStr(), c);
        createDOMProperty(NAVIGATOR, "vendor", Value.makeAnyStr(), c);

        /*
         * WINDOW PERFORMANCE object
         */
        s.newObject(PERFORMANCE);
        s.writeInternalPrototype(PERFORMANCE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        createDOMProperty(WINDOW, "performance", Value.makeObject(PERFORMANCE), c);

        // Functions.
        createDOMFunction(PERFORMANCE, DOMObjects.WINDOW_PERFORMANCE_NOW, "now", 0, c);

        /*
         * WINDOW PERFORMANCE object
         */
        s.newObject(PERFORMANCE);
        s.writeInternalPrototype(PERFORMANCE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        createDOMProperty(WINDOW, "performance", Value.makeObject(PERFORMANCE), c);

        // Functions.
        createDOMFunction(PERFORMANCE, DOMObjects.WINDOW_PERFORMANCE_NOW, "now", 0, c);

        /*
         * WINDOW SCREEN object
         */
        s.newObject(SCREEN);
        s.writeInternalPrototype(SCREEN, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        createDOMProperty(WINDOW, "screen", Value.makeObject(SCREEN), c);
        // Properties.
        createDOMProperty(SCREEN, "availTop", Value.makeAnyNumUInt(), c);
        createDOMProperty(SCREEN, "availLeft", Value.makeAnyNumUInt(), c);
        createDOMProperty(SCREEN, "availHeight", Value.makeAnyNumUInt(), c);
        createDOMProperty(SCREEN, "availWidth", Value.makeAnyNumUInt(), c);
        createDOMProperty(SCREEN, "colorDepth", Value.makeAnyNumUInt(), c);
        createDOMProperty(SCREEN, "height", Value.makeAnyNumUInt(), c);
        createDOMProperty(SCREEN, "left", Value.makeAnyNumUInt(), c);
        createDOMProperty(SCREEN, "pixelDepth", Value.makeAnyNumUInt(), c);
        createDOMProperty(SCREEN, "top", Value.makeAnyNumUInt(), c);
        createDOMProperty(SCREEN, "width", Value.makeAnyNumUInt(), c);

    }

    public static Value evaluate(DOMObjects nativeObject, final CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        // TODO: check that parameters are numbers? (same for many other DOM functions...)
        switch (nativeObject) {
            case WINDOW_ALERT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case WINDOW_ATOB: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyStr();
            }
            case WINDOW_BACK: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_BLUR: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_BTOA: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyStr();
            }
            case WINDOW_CLOSE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_CLEAR_INTERVAL: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toInteger(FunctionCalls.readParameter(call, s, 0), c);
                // TODO: Fix Later: Event Handlers cannot be removed.
                return Value.makeUndef();
            }
            case WINDOW_CLEAR_TIMEOUT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toInteger(FunctionCalls.readParameter(call, s, 0), c);
                // TODO: Fix Later: Event Handlers cannot be removed.
                return Value.makeUndef();
            }
            case WINDOW_CONFIRM: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyBool();
            }
            case WINDOW_DISPATCHEVENT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                return Value.makeAnyBool();
            }
            case WINDOW_ESCAPE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyStr();
            }
            case WINDOW_FOCUS: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_FORWARD: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_HISTORY_BACK: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_HISTORY_FORWARD: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_HISTORY_GO: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
            /* Value v =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case WINDOW_HISTORY_PUSH_STATE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 3);
                return Value.makeUndef();
            }
            case WINDOW_HISTORY_REPLACE_STATE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 3);
                return Value.makeUndef();
            }
            case WINDOW_HOME: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_LOCATION_ASSIGN: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
            /* Value url =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case WINDOW_LOCATION_RELOAD: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
            /* Value url =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 0));
                return Value.makeUndef();
            }
            case WINDOW_LOCATION_REPLACE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
            /* Value url =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case WINDOW_LOCATION_TOSTRING: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeAnyStr();
            }
            case WINDOW_MAXIMIZE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_MINIMIZE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_MOVEBY: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_MOVETO: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_OPEN: {
                if (Options.get().isDebugEnabled()) {
                    log.warn("WINDOW_OPEN not supported");
                }
                return Value.makeObject(WINDOW);
            }
            case WINDOW_PERFORMANCE_NOW: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeAnyNum();
            }
            case WINDOW_PRINT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_PROMPT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 2);
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeAnyStr();
            }
            case WINDOW_RESIZEBY: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_RESIZETO: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_SCROLL: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_SCROLLBY: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_SCROLLBYLINES: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case WINDOW_SCROLLBYPAGES: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case WINDOW_SCROLLTO: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_STOP: {
                s.setToBottom();
                return Value.makeNone();
            }
            case WINDOW_SET_INTERVAL:
            case WINDOW_SET_TIMEOUT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() == 0) {
                    return Value.makeUndef();
                }
                Value callbackParameter = FunctionCalls.readParameter(call, s, 0);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);

                // split into two cases: functions and non-functions (to be unevaled)
                Set<ObjectLabel> functionLabels = newSet();
                for (ObjectLabel objectLabel : callbackParameter.getObjectLabels()) {
                    if (objectLabel.getKind() == Kind.FUNCTION) {
                        functionLabels.add(objectLabel);
                    }
                }
                if (callbackParameter.isMaybeUndef() || callbackParameter.isMaybeNull()) {
                    Exceptions.throwTypeError(c);
                }
                Value callbackSourceCode = Conversion.toString(callbackParameter.removeObjects(functionLabels).restrictToNotNullNotUndef(), c);
                Value allCallbacks = Value.makeObject(functionLabels);

                if (!c.isScanning()) {
                    if (!callbackSourceCode.isNone()) {
                        if (callbackSourceCode.isMaybeSingleStr()) {
                            allCallbacks = allCallbacks.join(SimpleUnevalizerAPI.createSetTimeOutOrSetIntervalFunction(call.getSourceNode(), callbackSourceCode.getStr(), c));
                        } else if (Options.get().isUnevalizerEnabled()) {
                            if (call.getSourceNode() instanceof CallNode) {
                                CallNode callNode = (CallNode) call.getSourceNode();
                                Value vCallbackUnevaled = UnevalizerAPI.evaluateSetTimeoutSetIntervalStringCall(call, c, s, callbackSourceCode, callNode);
                                allCallbacks = allCallbacks.join(vCallbackUnevaled);
                            } else {
                                if (!c.getAnalysis().getUnsoundness().mayIgnoreEvalCallAtNonCallNode(call.getSourceNode())) {
                                    throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getSourceNode().getSourceLocation() + ": Invoking setTimeout/setInterval from non-CallNode - unevalizer can't handle that");
                                }
                            }
                        } else
                            throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getJSSourceNode().getSourceLocation() + ": Can't handle arbitrary strings in setInterval/setTimeout. Try with -uneval");
                    }
                    DOMEvents.addEventHandler(allCallbacks, EventType.TIMEOUT, c);
                }
                return Value.makeAnyNumUInt();
            }
            case WINDOW_UNESCAPE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeAnyStr();
            }
            case WINDOW_GET_COMPUTED_STYLE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 2);
                return Value.makeObject(CSSStyleDeclaration.INSTANCES);
            }
            case WINDOW_CANCEL_ANIM_FRAME: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                return Value.makeUndef();
            }
            case WINDOW_CANCEL_ANIMATION_FRAME: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                return Value.makeUndef();
            }
            case WINDOW_REQUEST_ANIMATION_FRAME:
            case WINDOW_WEBKIT_REQUEST_ANIMATION_FRAME: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                return Value.makeUndef();
            }
            case WINDOW_MATCH_MEDIA: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(MediaQueryList.INSTANCES);
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
