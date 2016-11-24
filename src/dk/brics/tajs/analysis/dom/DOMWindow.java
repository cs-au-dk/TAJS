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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.EvalCache;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.style.CSSStyleDeclaration;
import dk.brics.tajs.analysis.uneval.NormalForm;
import dk.brics.tajs.analysis.uneval.UnevalTools;
import dk.brics.tajs.flowgraph.EventType;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.FlowGraphFragment;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.js2flowgraph.FlowGraphMutator;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.unevalizer.Unevalizer;
import dk.brics.tajs.unevalizer.UnevalizerLimitations;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;
import dk.brics.tajs.util.Strings;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
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

    public static ObjectLabel HISTORY;

    public static ObjectLabel LOCATION;

    public static ObjectLabel NAVIGATOR;

    public static ObjectLabel SCREEN;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        HISTORY = new ObjectLabel(DOMObjects.WINDOW_HISTORY, Kind.OBJECT);
        LOCATION = new ObjectLabel(DOMObjects.WINDOW_LOCATION, Kind.OBJECT);
        NAVIGATOR = new ObjectLabel(DOMObjects.WINDOW_NAVIGATOR, Kind.OBJECT);
        SCREEN = new ObjectLabel(DOMObjects.WINDOW_SCREEN, Kind.OBJECT);

        // NB: The WINDOW object has already been instantiated.

        /*
         * Properties.
         */
        // DOM LEVEL 0
        createDOMProperty(WINDOW, "innerHeight", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "innerWidth", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "length", Value.makeAnyNumUInt(), c);
        createDOMProperty(WINDOW, "name", Value.makeAnyStr(), c);
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
        createDOMFunction(WINDOW, DOMObjects.WINDOW_SET_TIMEOUT /* FIXME implement proper function for this */, "requestAnimationFrame", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_SET_TIMEOUT /* FIXME implement proper function for this */, "webkitRequestAnimationFrame", 1, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_STOP, "stop", 0, c);
        createDOMFunction(WINDOW, DOMObjects.WINDOW_UNESCAPE, "unescape", 1, c);

        // DOM LEVEL 2
        createDOMFunction(WINDOW, DOMObjects.WINDOW_GET_COMPUTED_STYLE, "getComputedStyle", 0, c);

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
        createDOMProperty(NAVIGATOR, "product", Value.makeAnyStr(), c);
        createDOMProperty(NAVIGATOR, "appName", Value.makeAnyStr(), c);
        createDOMProperty(NAVIGATOR, "appVersion", Value.makeAnyStr(), c);
        createDOMProperty(NAVIGATOR, "userAgent", Value.makeAnyStr(), c);

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
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case WINDOW_ATOB: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeAnyStr();
            }
            case WINDOW_BACK: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_BLUR: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_BTOA: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeAnyStr();
            }
            case WINDOW_CLOSE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_CLEAR_INTERVAL: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toInteger(NativeFunctions.readParameter(call, s, 0), c);
                // TODO: Fix Later: Event Handlers cannot be removed.
                return Value.makeUndef();
            }
            case WINDOW_CLEAR_TIMEOUT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toInteger(NativeFunctions.readParameter(call, s, 0), c);
                // TODO: Fix Later: Event Handlers cannot be removed.
                return Value.makeUndef();
            }
            case WINDOW_CONFIRM: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeAnyBool();
            }
            case WINDOW_ESCAPE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeAnyStr();
            }
            case WINDOW_FOCUS: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_FORWARD: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_HISTORY_BACK: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_HISTORY_FORWARD: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_HISTORY_GO: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
            /* Value v =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case WINDOW_HOME: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_LOCATION_ASSIGN: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
            /* Value url =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case WINDOW_LOCATION_RELOAD: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
            /* Value url =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 0));
                return Value.makeUndef();
            }
            case WINDOW_LOCATION_REPLACE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
            /* Value url =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case WINDOW_LOCATION_TOSTRING: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeAnyStr();
            }
            case WINDOW_MAXIMIZE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_MINIMIZE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_MOVEBY: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_MOVETO: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_OPEN: {
                if (Options.get().isDebugEnabled()) {
                    log.warn("WINDOW_OPEN not supported");
                }
                return Value.makeObject(WINDOW);
            }
            case WINDOW_PRINT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case WINDOW_PROMPT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 2);
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeAnyStr();
            }
            case WINDOW_RESIZEBY: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_RESIZETO: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_SCROLL: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_SCROLLBY: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_SCROLLBYLINES: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case WINDOW_SCROLLBYPAGES: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case WINDOW_SCROLLTO: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case WINDOW_STOP: {
                s.setToNone();
                return Value.makeNone();
            }
            case WINDOW_SET_INTERVAL:
            case WINDOW_SET_TIMEOUT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() == 0) {
                    return Value.makeUndef();
                }
                Value callbackParameter = NativeFunctions.readParameter(call, s, 0);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);

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
                    if (!callbackSourceCode.isNotStr()) {
                        if (Options.get().isUnevalizerEnabled()) {
                            if(call.getSourceNode() instanceof CallNode) {
                                CallNode callNode = (CallNode) call.getSourceNode();
                                FlowGraph currFg = c.getFlowGraph();
                                NormalForm nf = UnevalTools.rebuildNormalForm(currFg, callNode, s, c);

                                String uneval_input = callbackSourceCode.getStr() != null ? "\"" + Strings.escapeSource(callbackSourceCode.getStr()) + "\"" : nf.getNormalForm();
                                String unevaled = new Unevalizer().uneval(UnevalTools.unevalizerCallback(currFg, c, callNode, nf, false), uneval_input, false, null, call.getSourceNode(), c);
                                if (unevaled == null) {
                                    return UnevalizerLimitations.handle("Could not uneval setTimeout/setInterval string (you should use higher-order functions instead): " + uneval_input, call.getSourceNode(), Value.makeAnyNumUInt(), c);
                                }
                                log.debug("Unevalized:" + unevaled);

                                if (callbackSourceCode.getStr() == null) // Called with non-constant.
                                    unevaled = UnevalTools.rebuildFullFromMapping(currFg, unevaled, nf.getMapping(), callNode);

                                EvalCache evalCache = c.getAnalysis().getEvalCache(); // TODO: refactor to avoid duplicated code (see JSFunction.FUNCTION and JSGlobal.EVAL)
                                NodeAndContext<Context> cc = new NodeAndContext<>(callNode, s.getContext());
                                FlowGraphFragment e = evalCache.getCode(cc);

                                // Cache miss.
                                if (e == null || !e.getKey().equals(unevaled)) {
                                    e = FlowGraphMutator.extendFlowGraph(currFg, unevaled, unevaled, e, callNode, true, null);
                                }

                                ObjectLabel callbackUnevaled = new ObjectLabel(e.getEntryFunction());
                                allCallbacks = allCallbacks.join(Value.makeObject(callbackUnevaled));
                                evalCache.setCode(cc, e);
                                if (Options.get().isFlowGraphEnabled()) {
                                    try (PrintWriter pw = new PrintWriter(new File("out" + File.separator + "flowgraphs" + File.separator + "uneval-" +
                                            callNode.getIndex() + "-" + Integer.toHexString(s.getContext().hashCode()) + ".dot"))) {
                                        currFg.toDot(pw);
                                        pw.flush();
                                    } catch (Exception ee) {
                                        throw new AnalysisException(ee);
                                    }
                                }
                            }else{
                                if (Options.get().isUnsoundEnabled() && call.getSourceNode() instanceof EventDispatcherNode) {
                                    return Value.makeUndef();
                                }
                                throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getSourceNode().getSourceLocation() + ": Invoking setTimeout/setInterval from non-CallNode - unevalizer can't handle that");
                            }
                        } else
                            throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getJSSourceNode().getSourceLocation() + ": Can't handle arbitrary strings in setInterval/setTimeout. Try with -uneval");
                    }
                    DOMEvents.addEventHandler(allCallbacks, EventType.TIMEOUT, c);
                }
                return Value.makeAnyNumUInt();
            }
            case WINDOW_UNESCAPE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeAnyStr();
            }
            case WINDOW_GET_COMPUTED_STYLE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 2);
                return Value.makeObject(CSSStyleDeclaration.STYLEDECLARATION);
            }
            default: {
                throw new AnalysisException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
