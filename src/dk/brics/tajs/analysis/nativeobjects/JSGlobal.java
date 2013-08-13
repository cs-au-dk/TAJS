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

package dk.brics.tajs.analysis.nativeobjects;

import java.io.File;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import dk.brics.tajs.analysis.CallContext;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.EvalCache;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.ajax.ReadystateEvent;
import dk.brics.tajs.analysis.dom.event.EventListener;
import dk.brics.tajs.analysis.dom.event.KeyboardEvent;
import dk.brics.tajs.analysis.dom.event.MouseEvent;
import dk.brics.tajs.analysis.dom.event.UIEvent;
import dk.brics.tajs.analysis.dom.event.WheelEvent;
import dk.brics.tajs.analysis.uneval.NormalForm;
import dk.brics.tajs.analysis.uneval.UnevalTools;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.FlowGraphFragment;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.js2flowgraph.RhinoASTToFlowgraph;
import dk.brics.tajs.lattice.BlockState;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.unevalizer.Unevalizer;
import dk.brics.tajs.util.AnalysisException;

/**
 * 15.1 and B.2 native global functions.
 */
public class JSGlobal {

	private static Logger logger = Logger.getLogger(JSGlobal.class); 

	private JSGlobal() {}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, final State state, Solver.SolverInterface c) {
		if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
			return Value.makeNone();

		switch (nativeobject) {

		case EVAL: { // 15.1.2.1
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value evalValue = NativeFunctions.readParameter(call, state, 0);
			if (evalValue.isMaybeOtherThanStr()) {
				if (!evalValue.isNotStr())
                    throw new AnalysisException("Parameter to eval is maybe a string and maybe a non-string, we currently can't handle that, sorry"); // TODO: improve?
				// TODO: issue warning if calling eval with non-string value
				return evalValue;
			}
            if (Options.isEvalStatistics()) 
            	c.getMonitoring().visitEvalCall(call.getSourceNode(), NativeFunctions.readParameter(call, state, 0));
			if (c.isScanning())
				return Value.makeNone();
			if (evalValue.isStrJSON()) { 
				return DOMFunctions.makeAnyJSONObject(state).join(evalValue.restrictToNotStr());
			} else if (Options.isUnevalEnabled()) {
                CallNode evalCall = (CallNode) call.getSourceNode();
                FlowGraph currentFg = c.getFlowGraph();
                boolean ignoreResult = evalCall.getResultRegister() == AbstractNode.NO_VALUE;
                String var = ignoreResult ? null : UnevalTools.gensym(); // Do we need the value of the eval call after?
                NormalForm input = UnevalTools.rebuildNormalForm(currentFg, evalCall, state, c);

                // Collect special args that should be analyzed context sensitively
                c.getAnalysis().getSpecialArgs().addContextSensitivity(evalCall.getBlock().getFunction(), input.getArgumentsInUse(), state, c);

                // What we should use as key for the eval cache is the entire tuple from the Uneval paper. Since that
                // might contain infinite sets and other large things we just call the Unevalizer and compare the output
                // of the Unevalizer to the key in the cache. This makes us Uneval more things, but we save the work
                // of re-extending the flow graph every time.
                boolean aliased_call = !"eval".equals(UnevalTools.get_call_name(currentFg, evalCall)); // TODO: aliased_call should also affect the execution context?
                String unevaled = new Unevalizer().uneval(UnevalTools.unevalizerCallback(currentFg, state, evalCall, input), input.getNormalForm(), aliased_call, var);

                if (unevaled == null)
                	throw new AnalysisException("Unevalable eval: " + UnevalTools.rebuildFullExpression(currentFg, evalCall, evalCall.getArgRegister(0)));
    			if (logger.isDebugEnabled()) 
    				logger.debug("Unevalized: " + unevaled);

               	unevaled = UnevalTools.rebuildFullFromMapping(currentFg, unevaled, input.getMapping(), evalCall);

                String unevaledSubst = ignoreResult ? unevaled : unevaled.replace(var, UnevalTools.VAR_PLACEHOLDER); // to avoid the random string in the cache
                EvalCache evalCache = c.getAnalysis().getEvalCache();
                NodeAndContext<CallContext> cc = new NodeAndContext<>(evalCall, c.getCurrentContext());
                FlowGraphFragment e = evalCache.getCode(cc);

                // Cache miss.
                if (e == null || !e.getKey().equals(unevaledSubst))
                    e = (new RhinoASTToFlowgraph()).extendFlowgraph(currentFg, unevaled, unevaledSubst, evalCall, e, var);

                evalCache.setCode(cc, e);
                c.propagateToBasicBlock(state.clone(), e.getEntryBlock(), c.getCurrentContext());
                if (Options.isFlowGraphEnabled()) {
                	try (PrintWriter pw = new PrintWriter(new File("out" + File.separator + "flowgraphs" + File.separator + "uneval-" + 
                			evalCall.getIndex() + "-" + Integer.toHexString(c.getCurrentContext().hashCode()) + ".dot"))) {
                		currentFg.toDot(pw);
                        pw.flush();
                    } catch (Exception ee) {
                        throw new AnalysisException(ee);
                    }
                }
                return Value.makeNone();
			} else {
				throw new AnalysisException("eval of non JSONStr not supported, and unevalizer is not enabled");
			}
		}
		case PARSEINT: { // 15.1.2.2
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
			Value str = Conversion.toString(NativeFunctions.readParameter(call, state, 0), c);
			Value basis;
			if (call.isUnknownNumberOfArgs()) {
				basis = NativeFunctions.readParameter(call, state, 1).joinNum(0);
			} else {
				basis = call.getNumberOfArgs() >= 2 ? Conversion.toNumber(NativeFunctions.readParameter(call, state, 1), c) : Value.makeNum(0);
			}
			if (str.isMaybeSingleStr() && basis.isMaybeSingleNum()) {
				String s = str.getStr().trim();
				double sign = 1;
				if (s.startsWith("-"))
					sign = -1;
				if (s.startsWith("-") || s.startsWith("+"))
					s = s.substring(1);
				int radix = Conversion.toInt32(basis.getNum());
				if (radix == 0) {
					radix = 10;
					if (s.length() > 1 && s.startsWith("0")) {
						radix = 8;
						if (s.length() > 2 && Character.toLowerCase(s.charAt(1)) == 'x') {
							radix = 16;
							s = s.substring(2);
						}
					}
				}
				if (radix < 2 || radix > 36)
					return Value.makeNum(Double.NaN);
				else {
					int i;
					String z = s;
					for (i = 0; i < s.length(); i++)
						if (Character.digit(s.charAt(i), radix) < 0) {
							z = s.substring(0, i);
							break;
						}
					if (z.isEmpty())
						return Value.makeNum(Double.NaN);
					else
						return Value.makeNum(sign * Integer.parseInt(z, radix));
				}
			} else
				return Value.makeAnyNum();
		}

		case PARSEFLOAT: { // 15.1.2.3
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value str = Conversion.toString(NativeFunctions.readParameter(call, state, 0), c);
			if (str.isMaybeSingleStr()) {
				String s = str.getStr().trim();
				Pattern p = Pattern.compile("[+-]?(Infinity|([0-9]+\\.[0-9]*|\\.[0-9]+|[0-9]+)([eE][+-]?[0-9]+)?)");
				Matcher m = p.matcher(s);
				if (m.lookingAt())
					return Value.makeNum(Double.parseDouble(m.group(0)));
				else
					return Value.makeNum(Double.NaN);
			} else
				return Value.makeAnyNum();
		}

		case ISNAN: { // 15.1.2.4
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value num = Conversion.toNumber(NativeFunctions.readParameter(call, state, 0), c);
			Value res = Value.makeNone();
			if (num.isMaybeNaN())
				res = res.joinBool(true);
			if (num.isMaybeSingleNum() || num.isMaybeInf() || num.isMaybeNumUInt() || num.isMaybeNumOther())
				res = res.joinBool(false);
			return res; 
		}

		case ISFINITE: { // 15.1.2.5
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value num = Conversion.toNumber(NativeFunctions.readParameter(call, state, 0), c);
			if (num.isMaybeSingleNum())
				return Value.makeBool(!num.getNum().isInfinite());
			Value res = Value.makeNone();
			if (num.isMaybeNaN() || num.isMaybeInf())
				res = res.joinBool(false);
			if (num.isMaybeNumUInt() || num.isMaybeNumOther())
				res = res.joinBool(true);
			return res; 
		}

		case PRINT:  // in Rhino, expects any number of parameters; returns undefined
		case ALERT: {
			return Value.makeUndef();
		}

		case DECODEURI: // 15.1.3.1
		case DECODEURICOMPONENT: // 15.1.3.2
		case ENCODEURI: // 15.1.3.3
		case ENCODEURICOMPONENT: // 15.1.3.4
		case ESCAPE: // B.2.1
		case UNESCAPE: { // B.2.2
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
            /* Value arg = */ NativeFunctions.readParameter(call, state, 0);
			return Value.makeAnyStr(); // TODO: could improve precision for constant strings
		}

		case ASSERT: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value x = Conversion.toBoolean(NativeFunctions.readParameter(call, state, 0));
			if (x.isMaybeFalse())
				c.getMonitoring().addMessage(c.getCurrentNode(), Severity.HIGH, "Assertion fails");
			return Value.makeUndef();
		}

		case DUMPVALUE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value x = NativeFunctions.readParameter(call, state, 0); // to avoid recover: call.getArg(0);
			c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Abstract value: " + x.restrictToNotModified() /*+ " (context: " + c.getCurrentContext() + ")"*/);
			return Value.makeUndef();
		}

		case DUMPPROTOTYPE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value x = NativeFunctions.readParameter(call, state, 0);

			StringBuilder sb = new StringBuilder();
			Value p = state.readInternalPrototype(x.getObjectLabels());
			p = UnknownValueResolver.getRealValue(p, state);
			while (p.isMaybeObject()) {
				sb.append(p.toString());
				p = state.readInternalPrototype(p.getObjectLabels());
				p = UnknownValueResolver.getRealValue(p, state);
				if (!p.isNullOrUndef()) {
					sb.append(" -> ");
				}
			}

			c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Prototype: " + sb);
			return Value.makeUndef();
		}

		case DUMPOBJECT: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value x = NativeFunctions.readParameter(call, state, 0);
			c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Abstract object: " + state.printObject(x) /*+ " (context: " + c.getCurrentContext() + ")"*/);
			return Value.makeUndef();
		}

		case DUMPSTATE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Abstract state:\n" + state /*+ " (context: " + c.getCurrentContext() + ")"*/);
			/*
			try {
                File outdir = new File("out");
                if (!outdir.exists()) {
                    outdir.mkdir();
                }
				FileWriter fw = new FileWriter("out" + File.separator + "state.dot");
				fw.write(state.toDot());
				fw.close();			
			} catch (IOException e) {
				throw new AnalysisException(e);
			}
			*/
			return Value.makeUndef();
		}

		case DUMPMODIFIEDSTATE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Abstract state (modified parts):" /*+ " (context: " + c.getCurrentContext() + ")"*/ + state.toStringModified());
			return Value.makeUndef();
		}

		case DUMPATTRIBUTES: {
			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
			Value x = NativeFunctions.readParameter(call, state, 0);
			Value p = Conversion.toString(NativeFunctions.readParameter(call, state, 1), c);
			if (!p.isMaybeSingleStr())
				c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Calling dumpAttributes with non-constant property name");
			else {
				String propertyname = p.getStr();
				Value v = state.readPropertyDirect(x.getObjectLabels(), propertyname);
				c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Property attributes: " + v.printAttributes() /*+ " (context: " + c.getCurrentContext() + ")"*/);
			}
			return Value.makeUndef();
		}

		case DUMPOBJECTORIGIN: { // TODO: remove dumpObjectOrigin? (use dumpObject instead)
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value x = NativeFunctions.readParameter(call, state, 0);
			c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Origin of objects: " + BlockState.printObjectOrigins(x) /*+ " (context: " + c.getCurrentContext() + ")"*/);
			return Value.makeUndef();
		}

		case DUMPEXPRESSION: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			CallNode cn =(CallNode) call.getSourceNode();
			String s = UnevalTools.rebuildFullExpression(c.getFlowGraph(), call.getSourceNode(), cn.getArgRegister(0));
			c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Exp: " + s);
			return Value.makeUndef();
		}

		case DUMPNF: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			CallNode cn =(CallNode) call.getSourceNode();
			NormalForm s = UnevalTools.rebuildNormalForm(c.getFlowGraph(), cn, state, c);
			c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "NF: " + s.toString());
			return Value.makeUndef();

		}

        case ASSERT_SINGLE_NUM: {
            NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
            Value arg = NativeFunctions.readParameter(call, state, 0);
            if(!arg.isMaybeSingleNum()) {
                throw new AnalysisException("Assertion failed:  Not a SingleNum: " + arg.toString());
            }
            return Value.makeUndef();
        }

        case ASSERT_ONE_OBJ: {
            NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
            Value arg = NativeFunctions.readParameter(call, state, 0);
            if (arg.getObjectLabels().size() != 1) {
                throw new AnalysisException("Assertion failed:  Not a single object label: " + arg.toString());
            }
            return Value.makeUndef();
        }

        case ASSERT_ABSENT: {
            // TODO: Not supported
            return Value.makeUndef();
        }

        case ASSERT_MOST_RECENT_OBJ: {
            NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
            Value arg = NativeFunctions.readParameter(call, state, 0);
            if (arg.getObjectLabels().size() != 1) {
                throw new AnalysisException("Assertion failed:  Not a single object label: " + arg.toString());
            }
            if (!arg.getObjectLabels().iterator().next().isSingleton()) {
                throw new AnalysisException("Assertion failed:  Not the most recent object: " + arg.toString());
            }
            return Value.makeUndef();
        }

        case ASSERT_SUMMARY_OBJ: {
            NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
            Value arg = NativeFunctions.readParameter(call, state, 0);
            if (arg.getObjectLabels().size() != 1) {
                throw new AnalysisException("Assertion failed:  Not a single object label: " + arg.toString());
            }
            if (arg.getObjectLabels().iterator().next().isSingleton()) {
                throw new AnalysisException("Assertion failed:  Not the summary object: " + arg.toString());
            }
            return Value.makeUndef();
        }

		case CONVERSION_TO_PRIMITIVE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
			Value varg = NativeFunctions.readParameter(call, state, 0);
			Value vhint;
			if (call.isUnknownNumberOfArgs())
				vhint = NativeFunctions.readParameter(call, state, 1).joinStr("NONE");
			else
				vhint = call.getNumberOfArgs() >= 2 ? NativeFunctions.readParameter(call, state, 1) : Value.makeStr("NONE");
				if (!vhint.isMaybeSingleStr()) {
					c.getMonitoring().addMessageInfo(c.getCurrentNode(), Severity.HIGH, "Calling conversionToPrimitive with non-constant hint string");
					return Value.makeUndef();
				} else {
					String shint = vhint.getStr();
					return Conversion.toPrimitive(varg, shint.equals("NONE") ? Conversion.Hint.NONE : shint.equals("NUM") ? Conversion.Hint.NUM : Conversion.Hint.STR, c);
				}
		}

		case ADD_CONTEXT_SENSITIVITY:{
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			if (call.isUnknownNumberOfArgs()) {
				throw new AnalysisException("Calling TAJS_addContextSensitivity with unknown number of arguments");
			} else {
				Value paramName = NativeFunctions.readParameter(call, state, 0);
				if (paramName.isStrIdentifier()){
					final Function enclosingFunction = call.getSourceNode().getBlock().getFunction();
					final String concreteParamName = paramName.getStr();
					boolean hasParam = enclosingFunction.getParameterNames().contains(concreteParamName);
					if (hasParam) {
						c.getAnalysis().getSpecialArgs().addToSpecialArgs(enclosingFunction, concreteParamName);
					} else {
						throw new AnalysisException(String.format("Calling TAJS_addContextSensitivity with non-existing parameter-name: '%s'. Function has: '%s'",
								concreteParamName, enclosingFunction.getParameterNames()));
					}
				} else {
					throw new AnalysisException("Calling TAJS_addContextSensitivity with non-identifier parameter-name: " + paramName);
				}
			}
			return Value.makeUndef();
		}

		case TAJS_GET_UI_EVENT: {
			return Value.makeObject(UIEvent.INSTANCES);
		}

		case TAJS_GET_MOUSE_EVENT: {
			return Value.makeObject(MouseEvent.INSTANCES);
		}

		case TAJS_GET_AJAX_EVENT: {
			return Value.makeObject(ReadystateEvent.INSTANCES);
		}

		case TAJS_GET_KEYBOARD_EVENT: {
			return Value.makeObject(KeyboardEvent.INSTANCES);
		}

		case TAJS_GET_EVENT_LISTENER: {
			return Value.makeObject(EventListener.INSTANCES);
		}

		case TAJS_GET_WHEEL_EVENT: {
			return Value.makeObject(WheelEvent.INSTANCES);
		}

		default:
			return null;
		}
	}
}
