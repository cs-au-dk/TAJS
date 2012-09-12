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

package dk.brics.tajs.analysis.nativeobjects;

import java.util.Collections;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;

/**
 * 15.10 native RegExp functions.
 */
public class JSRegExp { // TODO: see http://dev.opera.com/articles/view/opera-javascript-for-hackers-1/

	private JSRegExp() {}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (nativeobject != ECMAScriptObjects.REGEXP)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeNone();

		switch (nativeobject) {

		case REGEXP: {
            // TODO: Needs code review.
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
			boolean ctor = call.isConstructorCall();
			Value pattern = call.getNumberOfArgs() > 0 ? NativeFunctions.readParameter(call, state, 0) : Value.makeStr(""); // TODO: handle unknown number of args
			Value flags = call.getNumberOfArgs() > 1 ? NativeFunctions.readParameter(call, state, 1) : Value.makeUndef();
			Value result = Value.makeNone();
            // Use arg as our working copy of pattern.
            Value arg = pattern;
			// 15.10.3.1 function call; If the pattern is a RegExp object and flags are undefined, return the pattern.
            if (!flags.isNotUndef()) {
                boolean regexp_ol = true;
                for (ObjectLabel ol : pattern.getObjectLabels()) {
                    if (!ol.getKind().equals(Kind.REGEXP))
                        regexp_ol = false;
                }
                if (regexp_ol && !ctor && !pattern.getObjectLabels().isEmpty())
                    return pattern;
            }

            // Otherwise call the RegExp constructor as per 15.10.4.1.
			if (flags.isMaybeUndef()) {
				for (ObjectLabel ol : pattern.getObjectLabels())
					if (ol.getKind().equals(Kind.REGEXP)) {
						result = result.joinObject(ol);
                    } else {
						arg = arg.joinObject(ol);
                    }
			}
			if (flags.isMaybeOtherThanUndef())
				for (ObjectLabel ol : pattern.getObjectLabels())
					arg = arg.joinObject(ol);
			if (ctor && !flags.isMaybeUndef() && !result.getObjectLabels().isEmpty()) {
	            // TODO: Throw a TypeError exception as per 15.10.4.1 if we are certain?
				c.getMonitoring().addMessage(c.getCurrentNode(), Severity.HIGH, "TypeError, internal RegExp property with flags not undefined");
			}
			if (!arg.isNotPresent()) {
				Value pGlobal = Value.makeAnyBool();
				Value pIgnoreCase = Value.makeAnyBool();
				Value pMultiline = Value.makeAnyBool();
				if (!flags.isMaybeUndef()) {
					Value sflags = Conversion.toString(flags,c);
					if (sflags.isMaybeSingleStr()) {
						String strflags = sflags.getStr();
						pGlobal = Value.makeBool(strflags.contains("g"));
						pIgnoreCase = Value.makeBool(strflags.contains("i"));
						pMultiline = Value.makeBool(strflags.contains("m"));
						strflags = strflags.replaceFirst("g", "").replaceFirst("i", "").replaceFirst("m", "");
						if ((!strflags.isEmpty())) { 
							Exceptions.throwSyntaxError(state, c);
							c.getMonitoring().addMessage(c.getCurrentNode(), Severity.HIGH, "SyntaxError, invalid flags at RegExp constructor");
							return Value.makeNone();
						}
					}
				}
                if (!arg.isNotStr()) {
				    ObjectLabel no = new ObjectLabel(ECMAScriptObjects.REGEXP, Kind.REGEXP);
				    state.newObject(no);
				    state.writeInternalPrototype(no, Value.makeObject(InitialStateBuilder.REGEXP_PROTOTYPE));
				    Value p = UnknownValueResolver.join(Conversion.toString(arg.restrictToStr(), c), state.readInternalValue(arg.restrictToStr().getObjectLabels()), state);
				    state.writeInternalValue(no, p);
				    state.writePropertyWithAttributes(no, "source", p.setAttributes(true, true, true));
				    state.writePropertyWithAttributes(no, "lastIndex", Value.makeNum(0).setAttributes(true, true, false));
				    state.writePropertyWithAttributes(no, "global", pGlobal.setAttributes(true, true, true));
				    state.writePropertyWithAttributes(no, "ignoreCase", pIgnoreCase.setAttributes(true, true, true));
				    state.writePropertyWithAttributes(no, "multiline", pMultiline.setAttributes(true, true, true));
				    result = result.joinObject(no);
                }
			}
			return result;
		}
		
		case REGEXP_EXEC: { // 15.10.6.2 (see STRING_MATCH)
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
            /* Value arg = */  NativeFunctions.readParameter(call, state, 0);
			ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);
			state.newObject(objlabel);
			Value res = Value.makeObject(objlabel);
			state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));
			state.writeProperty(Collections.singleton(objlabel), Value.makeAnyStrUInt(), Value.makeAnyStr().joinAbsent(), true, false);
			state.writePropertyWithAttributes(objlabel, "length", Value.makeAnyNumUInt().setAttributes(true, true, false));
			state.writeProperty(objlabel, "index", Value.makeAnyNumUInt());
			state.writeProperty(objlabel, "input", Value.makeAnyStr()); // TODO: improve precision?
			return res.joinNull();
		}
		
		case REGEXP_TEST: { // 15.10.6.3
            NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
            /* Value arg = */  NativeFunctions.readParameter(call, state, 0);
			return Value.makeAnyBool(); //TODO: More precision.
			//			NativeFunctions.expectOneParameter(solver, node, params, first_param);
//			Value receiver = NativeFunctions.readParameter(call, s, first_param-1);
//			Set<ObjectLabel> objlabels = 
//				NativeFunctions.filterByKind(solver, node, receiver.getObjectLabels(), Kind.REGEXP, "RegExp.prototype.test must be applied to RegExp objects only");
//			return objlabels.isEmpty() ? Value.makeBottom() : Value.makeMaybeBool();
		}
		
		case REGEXP_TOSTRING: { // 15.10.6.4
            NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			return Value.makeAnyStr(); 
		}
			
		default:
			return null;
		}
	}
}
