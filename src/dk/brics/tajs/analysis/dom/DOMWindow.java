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

package dk.brics.tajs.analysis.dom;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

import java.io.File;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import dk.brics.tajs.analysis.CallContext;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.EvalCache;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.uneval.NormalForm;
import dk.brics.tajs.analysis.uneval.UnevalTools;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.FlowGraphFragment;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.js2flowgraph.RhinoASTToFlowgraph;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.unevalizer.Unevalizer;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Strings;

/**
 * DOM Window.
 */
public class DOMWindow {

	private static Logger logger = Logger.getLogger(DOMWindow.class); 

	public static ObjectLabel WINDOW;

	public static ObjectLabel HISTORY;
	public static ObjectLabel LOCATION;
	public static ObjectLabel NAVIGATOR;
	public static ObjectLabel SCREEN;
	public static ObjectLabel JSON;

	public static void build(final State s) {
		HISTORY = new ObjectLabel(DOMObjects.WINDOW_HISTORY, Kind.OBJECT);
		LOCATION = new ObjectLabel(DOMObjects.WINDOW_LOCATION, Kind.OBJECT);
		NAVIGATOR = new ObjectLabel(DOMObjects.WINDOW_NAVIGATOR, Kind.OBJECT);
		SCREEN = new ObjectLabel(DOMObjects.WINDOW_SCREEN, Kind.OBJECT);
		JSON = new ObjectLabel(DOMObjects.WINDOW_JSON, Kind.OBJECT);

		// NB: The WINDOW object has already been instantiated.

		/*
		 * Properties.
		 */
		// DOM LEVEL 0
		createDOMProperty(s, WINDOW, "innerHeight", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "innerWidth", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "length", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "name", Value.makeAnyStr());
		createDOMProperty(s, WINDOW, "outerHeight", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "outerWidth", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "pageXOffset", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "pageYOffset", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "parent", Value.makeObject(WINDOW));
		createDOMProperty(s, WINDOW, "self", Value.makeObject(WINDOW));
		createDOMProperty(s, WINDOW, "scrollMaxX", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "scrollMaxY", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "scrollX", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "scrollY", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "screenX", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "screenY", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "status", Value.makeAnyStr());
		createDOMProperty(s, WINDOW, "top", Value.makeObject(WINDOW));
		createDOMProperty(s, WINDOW, "window", Value.makeObject(WINDOW));

		/*
		 * Functions.
		 */
		// DOM LEVEL 0
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_ALERT, "alert", 1);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_ATOB, "atob", 1);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_BACK, "back", 0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_BLUR, "blur", 0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_BTOA, "btoa", 1);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_CLEAR_INTERVAL, "clearInterval", 0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_CLEAR_TIMEOUT, "clearTimeout", 0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_CLOSE, "close", 0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_CONFIRM, "confirm", 1);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_ESCAPE, "escape", 1);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_FOCUS, "focus", 0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_FORWARD, "forward", 0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_HOME, "home", 0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_MAXIMIZE, "maximize", 0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_MINIMIZE, "minimize", 0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_MOVEBY, "moveBy", 2);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_MOVETO, "moveTo", 2);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_OPEN, "open", 1);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_PRINT, "print", 0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_PROMPT, "prompt", 1);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_RESIZEBY, "resizeBy", 2);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_RESIZETO, "resizeTo", 2);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SCROLL, "scroll", 2);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SCROLLBY, "scrollBy", 2);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SCROLLBYLINES, "scrollByLines", 1);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SCROLLBYPAGES, "scrollByPages", 1);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SCROLLTO, "scrollTo", 2);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SET_INTERVAL, "setInterval", 2);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_SET_TIMEOUT, "setTimeout", 2);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_STOP, "stop", 0);
		createDOMFunction(s, WINDOW, DOMObjects.WINDOW_UNESCAPE, "unescape", 1);

		/**
		 * WINDOW HISTORY object
		 */
		s.newObject(HISTORY);
		s.writeInternalPrototype(HISTORY, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
		// Properties.
		createDOMProperty(s, HISTORY, "length", Value.makeAnyNumUInt());
		createDOMProperty(s, WINDOW, "history", Value.makeObject(HISTORY));
		// Functions.
		createDOMFunction(s, HISTORY, DOMObjects.WINDOW_HISTORY_BACK, "back", 0);
		createDOMFunction(s, HISTORY, DOMObjects.WINDOW_HISTORY_FORWARD, "forward", 0);
		createDOMFunction(s, HISTORY, DOMObjects.WINDOW_HISTORY_GO, "go", 1);

		/*
		 * WINDOW LOCATION object
		 */
		s.newObject(LOCATION);
		s.writeInternalPrototype(LOCATION, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
		createDOMProperty(s, WINDOW, "location", Value.makeObject(LOCATION));

		// Properties.
		createDOMProperty(s, LOCATION, "hash", Value.makeAnyStr());
		createDOMProperty(s, LOCATION, "host", Value.makeAnyStr());
		createDOMProperty(s, LOCATION, "hostname", Value.makeAnyStr());
		createDOMProperty(s, LOCATION, "href", Value.makeAnyStr());
		createDOMProperty(s, LOCATION, "pathname", Value.makeAnyStr());
		createDOMProperty(s, LOCATION, "port", Value.makeAnyStr());
		createDOMProperty(s, LOCATION, "protocol", Value.makeAnyStr());
		createDOMProperty(s, LOCATION, "search", Value.makeAnyStr());
		createDOMProperty(s, LOCATION, "hash", Value.makeAnyStr());
		// Functions.
		createDOMFunction(s, LOCATION, DOMObjects.WINDOW_LOCATION_ASSIGN, "assign", 1);
		createDOMFunction(s, LOCATION, DOMObjects.WINDOW_LOCATION_RELOAD, "reload", 1);
		createDOMFunction(s, LOCATION, DOMObjects.WINDOW_LOCATION_REPLACE, "replace", 1);
		createDOMFunction(s, LOCATION, DOMObjects.WINDOW_LOCATION_TOSTRING, "toString", 0);

		/*
		 * WINDOW NAVIGATOR object
		 */
		s.newObject(NAVIGATOR);
		s.writeInternalPrototype(NAVIGATOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
		createDOMProperty(s, WINDOW, "navigator", Value.makeObject(NAVIGATOR));
		// Properties.
		createDOMProperty(s, NAVIGATOR, "product", Value.makeAnyStr());
		createDOMProperty(s, NAVIGATOR, "appName", Value.makeAnyStr());
		createDOMProperty(s, NAVIGATOR, "appVersion", Value.makeAnyStr());
		createDOMProperty(s, NAVIGATOR, "userAgent", Value.makeAnyStr());

		/*
		 * WINDOW SCREEN object
		 */
		s.newObject(SCREEN);
		s.writeInternalPrototype(SCREEN, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
		createDOMProperty(s, WINDOW, "screen", Value.makeObject(SCREEN));
		// Properties.
		createDOMProperty(s, SCREEN, "availTop", Value.makeAnyNumUInt());
		createDOMProperty(s, SCREEN, "availLeft", Value.makeAnyNumUInt());
		createDOMProperty(s, SCREEN, "availHeight", Value.makeAnyNumUInt());
		createDOMProperty(s, SCREEN, "availWidth", Value.makeAnyNumUInt());
		createDOMProperty(s, SCREEN, "colorDepth", Value.makeAnyNumUInt());
		createDOMProperty(s, SCREEN, "height", Value.makeAnyNumUInt());
		createDOMProperty(s, SCREEN, "left", Value.makeAnyNumUInt());
		createDOMProperty(s, SCREEN, "pixelDepth", Value.makeAnyNumUInt());
		createDOMProperty(s, SCREEN, "top", Value.makeAnyNumUInt());
		createDOMProperty(s, SCREEN, "width", Value.makeAnyNumUInt());

		/*
		 * WINDOW JSON object
		 */
		s.newObject(JSON);
		s.writeInternalPrototype(JSON, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
		createDOMProperty(s, WINDOW, "JSON", Value.makeObject(JSON)); // XXX: DOMSpec.LEVEL_0?
		createDOMFunction(s, JSON, DOMObjects.WINDOW_JSON_PARSE, "parse", 1); // XXX: DOMSpec.LEVEL_0?
	}

	public static Value evaluate(DOMObjects nativeObject, final CallInfo call, final State s, Solver.SolverInterface c) {
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
			/* Value v =*/ Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
			return Value.makeUndef();
		}
		case WINDOW_HOME: {
			NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
			return Value.makeUndef();
		}
		case WINDOW_LOCATION_ASSIGN: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value url =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
			return Value.makeUndef();
		}
		case WINDOW_JSON_PARSE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value data =*/ NativeFunctions.readParameter(call, s, 0);
			return DOMFunctions.makeAnyJSONObject(s);
		}
		case WINDOW_LOCATION_RELOAD: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value url =*/ Conversion.toBoolean(NativeFunctions.readParameter(call, s, 0));
			return Value.makeUndef();
		}
		case WINDOW_LOCATION_REPLACE: {
			NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			/* Value url =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
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
			if (Options.isDebugEnabled()) {
				logger.warn("WINDOW_OPEN not supported");
			}
			return Value.makeObject(DOMWindow.WINDOW);
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
			Value functionOrCode = NativeFunctions.readParameter(call, s, 0);
            Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
			if (!c.isScanning()) {
				if (!functionOrCode.isNotStr()) {
					if (Options.isUnevalEnabled()) {
						CallNode callNode = (CallNode) call.getSourceNode();
						FlowGraph currFg = c.getFlowGraph();
						NormalForm nf = UnevalTools.rebuildNormalForm(currFg, callNode, s, c);

						String uneval_input = functionOrCode.getStr() != null ? "\""+Strings.escapeSource(functionOrCode.getStr()) +"\""
								: nf.getNormalForm();
						String unevaled = new Unevalizer().uneval(UnevalTools.unevalizerCallback(currFg, s, callNode, nf), uneval_input, false, null);
						if (unevaled == null) {
							throw new AnalysisException("Could not uneval setTimeout string (you should use higher-order functions instead): " + uneval_input);
						}
						logger.debug("Unevalized:" + unevaled);

						if (functionOrCode.getStr() == null) // Called with non-constant.
							unevaled = UnevalTools.rebuildFullFromMapping(currFg, unevaled, nf.getMapping(), callNode);

						EvalCache evalCache = c.getAnalysis().getEvalCache();
                        NodeAndContext<CallContext> cc = new NodeAndContext<>(callNode, c.getCurrentContext());
                        FlowGraphFragment e = evalCache.getCode(cc);

                        // Cache miss.
                        if (e == null || !e.getKey().equals(unevaled))
                            e = (new RhinoASTToFlowgraph()).extendFlowGraphWithEventHandler(currFg, unevaled, e, callNode);

                        evalCache.setCode(cc, e);
                        if (Options.isFlowGraphEnabled()) {
                        	try (PrintWriter pw = new PrintWriter(new File("out" + File.separator + "flowgraphs" + File.separator + "uneval-" + 
                        			callNode.getIndex() + "-" + Integer.toHexString(c.getCurrentContext().hashCode()) + ".dot"))) {
                        		currFg.toDot(pw);
                                pw.flush();
                            } catch (Exception ee) {
                                throw new AnalysisException(ee);
                            }
                        }
                        State state = s.clone();
                        state.getExtras().addToMaySet(DOMRegistry.MaySets.TIMEOUT_EVENT_HANDLERS.name(), Collections.singleton(new ObjectLabel(e.getEntryFunction())));
                        c.propagateToBasicBlock(state, e.getEntryBlock(), c.getCurrentContext());
					} else 
						throw new UnsupportedOperationException("Can't handle arbitrary strings in setInterval/setTimeout. Try with -uneval");
				}
				if (functionOrCode.isMaybeObject()) {
					// TODO: check that the objects are functions
					DOMEvents.addTimeoutEventHandler(s, functionOrCode.getObjectLabels());
				} else
                    s.setToNone(); // abort the ordinary flow
			}
			return Value.makeAnyNumUInt();
		}
	case WINDOW_UNESCAPE: {
		NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
		Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
		return Value.makeAnyStr();
	}
	default: {
		throw new AnalysisException("Unsupported Native Object: " + nativeObject);
	}
	}
}
}
