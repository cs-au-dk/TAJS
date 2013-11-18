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

import java.util.Collections;
import java.util.Set;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.lattice.Bool;
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;
import dk.brics.tajs.util.Strings;

/**
 * 15.4 native Array functions.
 */
public class JSArray {

	private JSArray() {}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (nativeobject != ECMAScriptObjects.ARRAY)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeNone();
		
		switch (nativeobject) {
		
		case ARRAY: { // 15.4, no difference between function and constructor
            NativeFunctions.expectParameters(nativeobject, call, c, 0, -1);
            // 15.4.1.1, 15.4.2.1 paragraph 2 and 3, 15.4.2.2 paragraph 1.
			ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);
			state.newObject(objlabel);
            state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));

            Value length = Value.makeAnyNumUInt();
            int numArgs = call.getNumberOfArgs();
			if (call.isUnknownNumberOfArgs())
				state.writeProperty(Collections.singleton(objlabel), Value.makeAnyStrUInt(), call.getUnknownArg(), true, false);
			else if (numArgs == 1 && !((CallNode) call.getSourceNode()).isArrayLiteral()) { // 15.4.2.2, paragraph 2.
				Value lenarg = NativeFunctions.readParameter(call, state, 0);
				Status s;
				if (lenarg.isMaybeSingleNum()) {
					double d = lenarg.getNum();
					if (d >= 0 && d < 2147483647d && Math.floor(d) == d) {
						s = Status.NONE;
						length = Value.makeNum(d);
					} else
						s = Status.CERTAIN;
                } else if (lenarg.isMaybeNumUInt() && !lenarg.isMaybeNumOther() && !lenarg.isMaybeInf() && !lenarg.isMaybeNaN())
					s = Status.NONE; // We're good: unknown UInt and nothing else.
				else if (!lenarg.isMaybeNumUInt() && (lenarg.isMaybeNumOther() || lenarg.isMaybeInf() || lenarg.isMaybeNaN()))
					s = Status.CERTAIN; // We're not good: definitely not UInt but something else.
				else if (lenarg.isMaybeFuzzyNum())
					s = Status.MAYBE; // Who knows: might be an UInt.
				else {
					s = Status.NONE; // Definitely not a number. See writing of zeroprop below.
					length = Value.makeNone();
				}
				if (s == Status.CERTAIN && lenarg.isMaybeOtherThanNum())
					s = Status.MAYBE;
				if (s != Status.NONE) {
					Exceptions.throwRangeError(state, c);
					c.getMonitoring().addMessage(call.getSourceNode(), Severity.HIGH, "RangeError, invalid value of array length");
				}
				if (s == Status.CERTAIN)
					return Value.makeNone();
				if (lenarg.isMaybeOtherThanNum()) { // 15.4.2.2, paragraph 3.
					length = length.joinNum(1);
					Value zeroprop = lenarg.restrictToNotNum();
					if (!lenarg.isNotNum())
						zeroprop = zeroprop.joinAbsent();
					state.writeProperty(objlabel, "0", zeroprop);
				} 
			} else { // 15.4.2.1
                length = Value.makeNum(numArgs);
				for (int i = 0; i < numArgs; i++)
					state.writeProperty(objlabel, Integer.toString(i), NativeFunctions.readParameter(call, state, i));
			}
            state.writePropertyWithAttributes(objlabel, "length", length.setAttributes(true, true, false));
			return Value.makeObject(objlabel);
		}

        case ARRAY_ISARRAY: { // 15.4.3.2
            NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
            Value arg = NativeFunctions.readParameter(call, state, 0);
            Value result = Value.makeNone();
            if (arg.isMaybePrimitive())
                result = result.joinBool(false);
            for (ObjectLabel l : arg.getObjectLabels()) {
                if (l.getKind() == Kind.ARRAY)
                    result = result.joinBool(true);
                else
                    result = result.joinBool(false);
            }
            return result;
        }
		
		case ARRAY_TOSTRING: // 15.4.4.2
		case ARRAY_TOLOCALESTRING: // 15.4.4.3
		case ARRAY_JOIN: { // 15.4.4.5
            boolean is_join = nativeobject == ECMAScriptObjects.ARRAY_JOIN;
            boolean is_toString = nativeobject == ECMAScriptObjects.ARRAY_TOSTRING;
            boolean is_toLocaleString = nativeobject == ECMAScriptObjects.ARRAY_TOLOCALESTRING;
			if (!is_join)
				if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.ARRAY))
					return Value.makeNone();
            NativeFunctions.expectParameters(nativeobject, call, c, 0, is_join ? 1 : 0);
            Set<ObjectLabel> objlabels = state.readThisObjects();
            Value length_val = state.readPropertyValue(objlabels, "length");
            Double length_prop = UnknownValueResolver.getRealValue(length_val, state).getNum();
            long length = length_prop == null ? -1 : Conversion.toUInt32(length_prop);
            Value sepArg = NativeFunctions.readParameter(call, state, 0);
            if (length_prop == null || (is_join && (call.isUnknownNumberOfArgs() || sepArg.isMaybeOtherThanStr())))
                return Value.makeAnyStr();
			String sep = is_toString || call.getNumberOfArgs() < 1 ? "," : Conversion.toString(sepArg, c).getStr();
            if (length == 0)
                return Value.makeStr("");
            if (is_toLocaleString)
                return Value.makeAnyStr(); // TODO: Reuse the function body by calling toObject(readProperty(i)).toLocaleString() instead of toString(readProperty(i)).
            Value zeroArg = state.readPropertyValue(objlabels, "0");
            zeroArg = UnknownValueResolver.getRealValue(zeroArg, state);
            String resString = "";
            zeroArg = zeroArg.restrictToNotNullNotUndef();
            resString += zeroArg.isMaybeOtherThanUndef() ? Conversion.toString(zeroArg, c).getStr() + sep : "";
			for (int i = 1; i < length; i++) {
				Value prop = state.readPropertyValue(objlabels, Integer.toString(i));
				prop = UnknownValueResolver.getRealValue(prop, state);
                Value tmpStr = Conversion.toString(prop.restrictToNotNullNotUndef(), c);
                if (!tmpStr.isMaybeSingleStr() && !(tmpStr.isNone() && (prop.isMaybeUndef() || prop.isMaybeNull())))
                    return Value.makeAnyStr();
                String vs = tmpStr.getStr();
                if (prop.isMaybeNull() || prop.isMaybeUndef())
					resString = resString + sep;
				else 
					resString = resString + vs + sep;
			}
			return Value.makeStr(resString.substring(0, resString.length()-sep.length()));
		}
		
		case ARRAY_CONCAT: { // 15.4.4.4
			ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);
			state.newObject(objlabel);
			Value res = Value.makeObject(objlabel);
			state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));
			Value v = state.readPropertyValue(state.readThisObjects(), Value.makeAnyStrUInt());
			if (call.isUnknownNumberOfArgs())
				v = UnknownValueResolver.join(v, call.getUnknownArg(), state);
			else
				for (int i = 0; i < call.getNumberOfArgs(); i++)
					v = UnknownValueResolver.join(v, NativeFunctions.readParameter(call, state, i), state);
			state.writeProperty(Collections.singleton(objlabel), Value.makeAnyStrUInt(), v, true, false);
			state.writePropertyWithAttributes(objlabel, "length", Value.makeAnyNumUInt().setAttributes(true, true, false));
			return res; // TODO: improve precision?
//			Value res = Value.makeBottom();
//			ObjectLabel conc = new ObjectLabel(node.getFunction().getIndex(), call.getProgramCounter(), Kind.ARRAY, node.getSourceLocation());
//			after.newObject(conc);
//			res = res.add(conc);
//			Value finalLength = Value.makeNum(0);
//			int n = 0,k = 0;
//			boolean exactIndexKnown = true;
//			while (n < params-1) {
//				Value arr = NativeFunctions.readParameter(call, state, first_param+n-1);
//				Value length = Conversion.toNumber(before.readProperty(arr.getObjectLabels(), "length"));
//				boolean isMaybeNotArray = false;
//				for (ObjectLabel ol : arr.getObjectLabels())
//					if (ol.getKind() != Kind.ARRAY) 
//						isMaybeNotArray = true;
//				if (!arr.isNotBool() || !arr.isNotNull() || !arr.isNotNum() || !arr.isNotStr() || !arr.isNotUndef()) isMaybeNotArray = true;
//				if (isMaybeNotArray && arr.getObjectLabels().size() > 1)
//					exactIndexKnown = false;
//				if (length.isFuzzyNum() || !exactIndexKnown) {
//					exactIndexKnown = false;
//					finalLength = Value.makeMaybeNumNotNaN(); 
//					solver.checkUnknownArrayIndexPresent(arr.getObjectLabels());
//					after.writeUnknownArrayIndex(conc, after.readUnknownArrayIndex(arr.getObjectLabels()));
//				}
//				else {
//					
//					double len = length.getNumValue() == null ? 0 : length.getNumValue();
//					finalLength = Value.makeNum(finalLength.getNumValue() + len);
//					Value kth = isMaybeNotArray ? arr : Value.makeBottom();
//					if (len == 0 && isMaybeNotArray) {
//						after.writeProperty(conc, Integer.toString(k++), kth);
//						finalLength = Value.makeNum(finalLength.getNumValue() + 1);
//					}
//					else
//						for (int i = 0; i < len; i++) {
//							String s = Integer.toString(i);
//							solver.checkPropertyPresent(arr.getObjectLabels(), s, false, false);
//							Value ith = before.readProperty(arr.getObjectLabels(), s);
//							if (isMaybeNotArray && i > 0) ith = ith.joinMaybeAbsent();
//							after.writeProperty(conc, Integer.toString(k++), i == 0 ? ith.join(kth) : ith);
//						}
//				}
//				n++;
//			}
//			after.writeSpecialProperty(conc, "length", finalLength, true, true, false);
//			return res;
		}

        case ARRAY_FOREACH: {
            NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
            @SuppressWarnings("unused") Value callbackfn = NativeFunctions.readParameter(call, state, 0);
            @SuppressWarnings("unused") Value this_arg =  call.getNumberOfArgs() >= 1 ? NativeFunctions.readParameter(call, state, 1) : Value.makeNone();
            return Value.makeUndef();
        }
		
		case ARRAY_POP: { // 15.4.4.6
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			Set<ObjectLabel> thisobj = state.readThisObjects();
            Value length_val = state.readPropertyValue(thisobj, "length");
            Double length_prop_num = UnknownValueResolver.getRealValue(length_val, state).getNum();
            long length = length_prop_num != null ? Conversion.toUInt32(length_prop_num) : -1;
            if (length == 0) // 15.4.4.6 item 3 and item 5.
                return Value.makeUndef();
            Value res;
            Value new_len;
            if (length > 0) {
                res = UnknownValueResolver.getRealValue(state.readPropertyValue(thisobj, String.valueOf(length - 1)), state);
                state.deleteProperty(thisobj, Value.makeStr(String.valueOf(length - 1)));
                new_len = Value.makeNum(length - 1);
            } else {
			    res = UnknownValueResolver.getRealValue(state.readPropertyValue(thisobj, Value.makeAnyStrUInt()), state);
			    state.deleteProperty(thisobj, Value.makeAnyStrUInt());
                new_len = Value.makeAnyNumUInt();
            }
			state.writePropertyWithAttributes(thisobj, "length", new_len.setAttributes(true, true, false));
			return res;
		}
		
		case ARRAY_PUSH: { // 15.4.4.7
			Set<ObjectLabel> arr = state.readThisObjects();
            Value new_len = Value.makeAnyNumUInt();
			if (call.isUnknownNumberOfArgs()) {
                state.writeProperty(arr, Value.makeAnyStrUInt(), call.getUnknownArg(), true, false);
            } else if (arr.size() == 1) {
                Value len_val = UnknownValueResolver.getRealValue(state.readPropertyValue(arr, "length"), state);
                Double length_prop = len_val.getNum();
                long length = length_prop != null ? Conversion.toUInt32(length_prop) : -1;
                int i;
                for (i = 0; i < call.getNumberOfArgs(); i++) {
					Value v = NativeFunctions.readParameter(call, state, i);
                    if (length > -1)
						state.writeProperty(arr, Value.makeTemporaryStr(String.valueOf(i + length)), v, true, false);
					else {
						state.writeProperty(arr, Value.makeAnyStrUInt(), v, true, false);
						break;
					}
				}
                if (length > -1)
                    new_len = Value.makeNum(i + length);
			} else
                for (int i = 0; i < call.getNumberOfArgs(); i++)
					state.writeProperty(arr, Value.makeAnyStrUInt(), NativeFunctions.readParameter(call, state, i), true, false);
			state.writePropertyWithAttributes(arr, "length", new_len.setAttributes(true, true, false));
			return new_len;
		}
		
		case ARRAY_REVERSE: { // 15.4.4.8
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
            Set<ObjectLabel> thisobj = state.readThisObjects();
            Value length_val = state.readPropertyValue(thisobj, "length");
            Double length_prop_num = UnknownValueResolver.getRealValue(length_val, state).getNum();
            long length = length_prop_num != null ? Conversion.toUInt32(length_prop_num) : -1;
            if (length > -1 && length <= 1) // 15.4.4.8 item 5
                return Value.makeObject(thisobj);
            if (length > 0) {
                for (int k = 0; k < Math.floor(length / 2); k++) {
                    String s1 = Integer.toString(k), s2 = Integer.toString(Long.valueOf(length).intValue() - k - 1);
                    Value near_start = state.readPropertyWithAttributes(thisobj, s1);
                    Value near_end = state.readPropertyWithAttributes(thisobj, s2);
                    if (near_start.isNotPresent())
                        state.deleteProperty(thisobj, Value.makeStr(s2));
                    else if (near_start.isMaybePresent())
                        state.writePropertyWithAttributes(thisobj, s2, near_start);
                    if (near_end.isNotPresent())
                        state.deleteProperty(thisobj, Value.makeStr(s1));
                    else if (near_end.isMaybePresent())
                        state.writePropertyWithAttributes(thisobj, s1, near_end);
                }
            } else {
            	Value v = state.readPropertyWithAttributes(thisobj, Value.makeAnyStrUInt());
            	if (v.isMaybePresent())
            		state.writeProperty(thisobj, Value.makeAnyStrUInt(), v, true, false);
            }
			return Value.makeObject(thisobj);
		}
		
		case ARRAY_SHIFT: {
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			Set<ObjectLabel> thisobj = state.readThisObjects();
            Value new_len = Value.makeAnyNumUInt();
            Value length_val = state.readPropertyValue(thisobj, "length");
            Double length_prop_num = UnknownValueResolver.getRealValue(length_val, state).getNum();
            long length = length_prop_num != null ? Conversion.toUInt32(length_prop_num) : -1;
            if (length == 0) // 15.4.4.9 item 5
                return Value.makeUndef();
            Value zero_elem = UnknownValueResolver.getRealValue(state.readPropertyValue(thisobj, "0"), state);
            int max_index = -1;
            if (length < 0) { // Shift all properties by one even if we don't know the length.
                for (ObjectLabel ll : thisobj) {
                    for (String prop : UnknownValueResolver.getProperties(ll, state).keySet()) {
                        if (!Strings.isArrayIndex(prop))
                            continue;
                        max_index = Math.max(max_index, Integer.valueOf(prop));
                    }
                }
            }
            int loopmax = length > 0 ? Long.valueOf(length).intValue() : max_index;
            for (int i = 1; i <= loopmax; i++) { // TODO: this may be bad if the array length is high
                String s = Integer.toString(i);
                Bool is_def = state.hasProperty(thisobj, s);
                if (is_def.isMaybeTrue()) {
                    Value elem = state.readPropertyWithAttributes(thisobj, s);
                    state.writeProperty(thisobj, Value.makeTemporaryStr(Integer.toString(i - 1)), elem, false, false);
                } else
                    state.deleteProperty(thisobj, Value.makeTemporaryStr(Integer.toString(i - 1)));
            }
            state.deleteProperty(thisobj, Value.makeTemporaryStr(Integer.toString(loopmax)));
            if (length > 0)
                new_len = Value.makeNum(length - 1);
			state.writePropertyWithAttributes(thisobj, "length", new_len.setAttributes(true, true, false));
			return zero_elem;
		}
		
		case ARRAY_SLICE: {
			if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() == 0) {
				// sometimes used to clone arrays (or to create real arrays from array-like objects)
				Set<ObjectLabel> thisobj = state.readThisObjects();
				ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);
				state.newObject(objlabel);
				Value res = Value.makeObject(objlabel);
				state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));
	            Value length_val = state.readPropertyValue(thisobj, "length");
				state.writePropertyWithAttributes(objlabel, "length", length_val.setAttributes(true, true, false));
                for (ObjectLabel ll : thisobj) {
                	Set<String> from_props = UnknownValueResolver.getProperties(ll, state).keySet();
                	for (String prop : from_props) {
                    	if (Strings.isArrayIndex(prop)) {
                    		Value v = state.readPropertyDirect(Collections.singleton(ll), prop);
                    		if (v.isMaybePresent()) {
                    			state.writeProperty(objlabel, prop, v);
                    		}
                    	}
                    }
                    Obj obj_to = state.getObject(objlabel, true);
                    Obj obj_from = state.getObject(ll, false);
					Value default_to = UnknownValueResolver.getDefaultArrayProperty(objlabel, state);
                    Value default_from = UnknownValueResolver.getDefaultArrayProperty(ll, state);
					obj_to.setDefaultArrayProperty(default_from.join(default_to));
                	for (String prop : UnknownValueResolver.getProperties(objlabel, state).keySet()) {
                    	if (!from_props.contains(prop) && Strings.isArrayIndex(prop)) {
                    		Value v = obj_from.getDefaultArrayProperty();
                    		if (v.isMaybePresent()) {
                    			state.writeProperty(objlabel, prop, v);
                    		}
                    	}
                    }
                }
				return res;
			} else {
				NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
				ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);
				state.newObject(objlabel);
				Value res = Value.makeObject(objlabel);
				state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));
				Value v = state.readPropertyValue(state.readThisObjects(), Value.makeAnyStrUInt());
				state.writeProperty(Collections.singleton(objlabel), Value.makeAnyStrUInt(), v, true, false);
				state.writePropertyWithAttributes(objlabel, "length", Value.makeAnyNumUInt().setAttributes(true, true, false));
				return res; // TODO: improve precision?
			}
//			NativeFunctions.expectTwoParameters(solver, node, params, first_param);
//			Value arr = NativeFunctions.readParameter(call, state, first_param-1);
//			Value res = Value.makeBottom();
//			ObjectLabel slice = new ObjectLabel(node.getFunction().getIndex(), call.getProgramCounter(), Kind.ARRAY, node.getSourceLocation());
//			res.add(slice);
//			after.newObject(slice); // TODO: set internal prototype?
//			after.writeUnknownArrayIndex(slice, before.readUnknownArrayIndex(arr.getObjectLabels()));
//			after.writeSpecialProperty(slice, "length", Value.makeMaybeNum(), true, true, false);
//			return arr;
		}

        case ARRAY_SOME: {
            NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
            @SuppressWarnings("unused") Value callback = NativeFunctions.readParameter(call, state, 0);
            @SuppressWarnings("unused") Value thisfn = call.getNumberOfArgs() >= 1 ? NativeFunctions.readParameter(call, state, 1) : Value.makeNone();
            return Value.makeAnyBool();
        }

		case ARRAY_SORT: {
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
			@SuppressWarnings("unused") Value comparefn = call.getNumberOfArgs() >= 1 ? NativeFunctions.readParameter(call, state, 0) : Value.makeNone();
			Set<ObjectLabel> thisobj = state.readThisObjects();
			state.writeProperty(thisobj, Value.makeAnyStrUInt(), state.readPropertyValue(thisobj, Value.makeAnyStrUInt()), true, false);
			return Value.makeObject(thisobj); // TODO: improve precision?
		}
		
		case ARRAY_SPLICE: {
			NativeFunctions.expectParameters(nativeobject, call, c, 2, -1);
			ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);
			state.newObject(objlabel);
			Value res = Value.makeObject(objlabel);
			state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));
			Value v = state.readPropertyValue(state.readThisObjects(), Value.makeAnyStrUInt());
			if (call.isUnknownNumberOfArgs())
				v = UnknownValueResolver.join(v, call.getUnknownArg(), state);
			else
				for (int i = 0; i < call.getNumberOfArgs(); i++)
					v = UnknownValueResolver.join(v, NativeFunctions.readParameter(call, state, i), state);
			state.writeProperty(Collections.singleton(objlabel), Value.makeAnyStrUInt(), v, true, false);
			state.writePropertyWithAttributes(objlabel, "length", Value.makeAnyNumUInt().setAttributes(true, true, false));
			return res; // TODO: improve precision?
		}
		
		case ARRAY_UNSHIFT: { // 15.4.4.13
			Set<ObjectLabel> arr = state.readThisObjects();
			if (call.isUnknownNumberOfArgs()) {
				Value v = call.getUnknownArg();
				state.writeProperty(arr, Value.makeAnyStrUInt(), v, true, false);
			} else { // TODO: improve precision?
				for (int i = 0; i < call.getNumberOfArgs(); i++) {
					Value v = NativeFunctions.readParameter(call, state, i);
					state.writeProperty(arr, Value.makeAnyStrUInt(), v, true, false);
				}
			}
			state.writePropertyWithAttributes(arr, "length", Value.makeAnyNumUInt().setAttributes(true, true, false));
			return Value.makeAnyNumUInt();
		}
		
		case ARRAY_INDEXOF: { // 15.4.4.14
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
            /* Value searchElement =*/ NativeFunctions.readParameter(call, state, 0);
            Value fromIndex = call.getNumberOfArgs() > 1 ? Conversion.toInteger(NativeFunctions.readParameter(call, state, 1), c) : Value.makeNum(0); // TODO: sometimes certain?
            Double fromindex_num = UnknownValueResolver.getRealValue(fromIndex, state).getNum();
            int n = fromindex_num == null ? -1 : fromindex_num.intValue();
            Set<ObjectLabel> thisobj = state.readThisObjects();
            Value length_val = state.readPropertyValue(thisobj, "length");
            Double length_prop_num = UnknownValueResolver.getRealValue(length_val, state).getNum();
            long length = length_prop_num != null ? Conversion.toUInt32(length_prop_num) : -1;
            if (length == 0 || (n > length && length > 0)) // 15.4.4.14 item 4 and item 6
                return Value.makeNum(-1);
            return Value.makeAnyNumOther();
		}
		
		default:
			return null;
		}
	}
}
