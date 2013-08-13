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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.lattice.ObjectLabel.Kind;

/**
 * 15.5 native String functions.
 */
public class JSString {

	private JSString() {}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (nativeobject != ECMAScriptObjects.STRING)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeNone();

		switch (nativeobject) {

		case STRING: { // 15.5.1/2
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
			Value s;
			if (call.isUnknownNumberOfArgs())
				s = Conversion.toString(NativeFunctions.readParameter(call, state, 0), c).joinStr("");
			else
				s = call.getNumberOfArgs() >= 1 ? Conversion.toString(NativeFunctions.readParameter(call, state, 0), c) : Value.makeStr("");
			if (call.isConstructorCall()) { // 15.5.2
				ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.STRING);
				state.newObject(objlabel);
				state.writeInternalValue(objlabel, s);
				state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.STRING_PROTOTYPE));
				Value len = s.isMaybeSingleStr() ? Value.makeNum(s.getStr().length()) : Value.makeAnyNumUInt();
				state.writePropertyWithAttributes(objlabel, "length", len.setAttributes(true, true, true));
				return Value.makeObject(objlabel);
			} else // 15.5.1
				return s;
		}

		case STRING_FROMCHARCODE: { // 15.5.3.2
			if (call.isUnknownNumberOfArgs())
				return Value.makeAnyStr();
			StringBuilder b = new StringBuilder(call.getNumberOfArgs());
			for (int i = 0; i < call.getNumberOfArgs(); i++) {
				Value v = Conversion.toNumber(NativeFunctions.readParameter(call, state, i), c);
				if (v.isMaybeSingleNum()) {
					long codepoint = Conversion.toUInt16(v.getNum());
					b.append((char)codepoint);
				} else
					return Value.makeAnyStr();
			}
			return Value.makeStr(b.toString());
		}
					
		case STRING_TOSTRING: // 15.5.4.2
		case STRING_VALUEOF: { // 15.5.4.3
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.STRING))
				return Value.makeNone();
			return state.readInternalValue(state.readThisObjects());
		}

		case STRING_CHARAT: // 15.5.4.4
		case STRING_CHARCODEAT: { // 15.5.4.5
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Value receiver = Value.makeObject(state.readThisObjects());
			Value str = Conversion.toString(receiver, c);
			Value pos = Conversion.toInteger(NativeFunctions.readParameter(call, state, 0), c);
			if (str.isMaybeSingleStr() && pos.isMaybeSingleNum()) { // FIXME: may also be maybe non-single!
				String s = str.getStr();
				double p = pos.getNum();
				try {
					char ch = s.charAt((int)Math.round(p));
					if (nativeobject == ECMAScriptObjects.STRING_CHARAT)
						return Value.makeStr(new String(new char[]{ch}));
					else
						return Value.makeNum(ch);
				} catch (IndexOutOfBoundsException e) {
					if (nativeobject == ECMAScriptObjects.STRING_CHARAT)
						return Value.makeStr("");
					else
						return Value.makeNum(Double.NaN);
				}
			} else
				return Value.makeAnyStr();
		}
			
		case STRING_CONCAT: { // 15.5.4.6
			return Value.makeAnyStr(); // TODO: improve precision?
//			if (call.isUnknownNumberOfArgs())
//				return Value.makeAnyStr();
//			String r = "";
//			for (int i = 0; i < call.getNumberOfArgs(); i++) {
//				Value argi = Conversion.toString(NativeFunctions.readParameter(call, s, i),c);
//				if (argi.isMaybeAnyStr())
//					return Value.makeAnyStr();
//				if (argi.isNoValue()) 
//					return Value.makeBottom();
//				r = r + argi.getStr();
//			}
//			return Value.makeStr(r);
		}
					
		case STRING_INDEXOF: // 15.5.4.7
		case STRING_LASTINDEXOF: { // 15.5.4.8
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
      /* Value word = */ NativeFunctions.readParameter(call, state, 0);
			return Value.makeAnyNumNotNaNInf(); // TODO: improve precision?
//			Value vthis = Conversion.toString(Value.makeObject(call.getThis(state)),c);
//			if (vthis.isMaybeAnyStr())
//				return Value.makeAnyNumNotNaNInf();
//			String thisstr = vthis.getStr();
//			Value vSearchString;// = Conversion.toString(params <= 2 ? Value.makeUndef() : NativeFunctions.readParameter(call, s, first_param));
//			Value vPosition;
//			if (call.isUnknownNumberOfArgs()) {
//				vSearchString = call.getUnknownArg().joinUndef();
//				vPosition = call.getUnknownArg().joinUndef();;
//			}
//			else if (call.getNumberOfArgs() == 0) {
//				vSearchString = Value.makeUndef();
//				vPosition = Value.makeUndef();
//			}
//			else if (call.getNumberOfArgs() == 1) {
//				vSearchString = NativeFunctions.readParameter(call, s, 0);
//				vPosition = Value.makeUndef();
//			}
//			else {
//				vSearchString = NativeFunctions.readParameter(call, s, 0);
//				vPosition = NativeFunctions.readParameter(call, s, 1);
//			}
//			vSearchString = Conversion.toString(vSearchString, c);
//			if (vSearchString.isMaybeAnyStr())
//				return Value.makeAnyNumNotNaNInf();
//			boolean isIndexOf = nativeobject.equals(NativeObjects.STRING_INDEXOF);
//			int defaultPosition = isIndexOf ? 0 : thisstr.length();
//			vPosition = (vPosition.isMaybeUndef() ? Value.makeNum(defaultPosition) : Value.makeBottom()).join(vPosition);
//			vPosition= Conversion.toInteger(vPosition,c);
//			if (vPosition.isMaybeFuzzyNum())
//				return Value.makeAnyNumNotNaNInf();
//			String searchString = vSearchString.getStr();
//			int position = vPosition.getNum().intValue();
//			int result = isIndexOf ? thisstr.indexOf(searchString, position) : thisstr.lastIndexOf(searchString, position);
//			return Value.makeNum(result);
		}
		
		case STRING_LOCALECOMPARE: { // 15.5.4.9
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
      /* Value param1 = */ NativeFunctions.readParameter(call, state, 0);
			return Value.makeAnyNum(); // TODO: improve precision?
		}
					
		case STRING_MATCH: { // 15.5.4.10 (see REGEXP_EXEC)
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
      /* Value match_string = */ NativeFunctions.readParameter(call, state, 0);
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
					
		case STRING_REPLACE: { // 15.5.4.11
			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
      /* Value param1 = */ NativeFunctions.readParameter(call, state, 0);
			// FIXME: if second param to 'replace' is a function, it may be called, even several times!
			return Value.makeAnyStr(); // complicated regexp stuff
		}

		case STRING_SEARCH: { // 15.5.4.12
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
      /* Value search_word = */ NativeFunctions.readParameter(call, state, 0);
			return Value.makeAnyNumNotNaNInf(); // TODO: improve precision?
		}
		
		case STRING_SLICE: { // 15.5.4.13
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
      /* Value slice_pos = */ NativeFunctions.readParameter(call, state, 0);
			return Value.makeAnyStr(); // TODO: improve precision?
//			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
//			Value vthis = Conversion.toString(Value.makeObject(call.getThis(state)),c);
//			if (vthis.isMaybeAnyStr() || call.isUnknownNumberOfArgs())
//				return Value.makeAnyStr();
//			String strthis = vthis.getStr();
//			int sourcelength = strthis.length();
//			Value vstart = NativeFunctions.readParameter(call, s, 0);
//			Value vend = NativeFunctions.readParameter(call, s, 1);
//			if (vend.isMaybeUndef())
//				vend = vend.join(Value.makeNum(sourcelength));
//			vstart = Conversion.toInteger(vstart,c);
//			vend = Conversion.toInteger(vend,c);
//			if (vstart.isMaybeFuzzyNum() || vend.isMaybeFuzzyNum())
//				return Value.makeAnyStr();
//			int istart = vstart.getNum().intValue();
//			int iend = vend.getNum().intValue();
//			int result5 = istart < 0 ? Math.max(0, sourcelength + istart) : Math.min(sourcelength, istart);
//			int result6 = iend < 0   ? Math.max(0, sourcelength + iend) : Math.min(sourcelength, iend);
//			int result7 = Math.max(0, result6 - result5);
//			return Value.makeStr(strthis.substring(result5, result5 + result7));
		}

		case STRING_SPLIT: { // 15.5.4.14
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
      /* Value split_char = */ NativeFunctions.readParameter(call, state, 0);
			ObjectLabel aobj = new ObjectLabel(call.getSourceNode(), Kind.ARRAY);
			state.newObject(aobj);
			state.writeInternalPrototype(aobj, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));
			state.writeProperty(Collections.singleton(aobj), Value.makeAnyStrUInt(), Value.makeAnyStr(), true, false);
			state.writePropertyWithAttributes(aobj, "length", Value.makeAnyNumUInt().setAttributes(true, true, false));
			return Value.makeObject(aobj); // TODO: improve precision?		
		}

		case STRING_SUBSTRING: { // 15.5.4.15
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
      /* Value split_pos = */ NativeFunctions.readParameter(call, state, 0);
			return Value.makeAnyStr(); // TODO: improve precision?
//			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2);
//			Value vthisStr = Conversion.toString(Value.makeObject(call.getThis(state)),c);
//			if (vthisStr.isMaybeAnyStr() || call.isUnknownNumberOfArgs())
//				return Value.makeAnyStr();
//			String sthis = vthisStr.getStr();
//			int result2 = sthis.length();
//			Value vstart = NativeFunctions.readParameter(call, s, 0);
//			Value result3 = Conversion.toInteger(vstart,c);
//			Value vend = NativeFunctions.readParameter(call, s, 1);
//			Value result4 = Conversion.toInteger(vend,c);
//			if (result3.isMaybeAnyNum() || result4.isMaybeAnyNum())
//				return Value.makeAnyNumNotNaNInf();
//			int iresult3 = result3.getNum().intValue();
//			int iresult4 = result4.getNum().intValue();
//			int iresult5 = Math.min(Math.max(iresult3, 0), result2);
//			int iresult6 = Math.min(Math.max(iresult4, 0), result2);
//			int iresult7 = Math.min(iresult5, iresult6);
//			int iresult8 = Math.max(iresult5, iresult6);
//			return Value.makeStr(sthis.substring(iresult7, iresult8));
		}

		case STRING_TOLOWERCASE: // 15.5.4.16
		case STRING_TOUPPERCASE: { // 15.5.4.18
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			Value vthisstring = Conversion.toString(Value.makeObject(state.readThisObjects()),c);
			if (vthisstring.isMaybeSingleStr()) {
				String sthis = vthisstring.getStr();
				if (nativeobject.equals(ECMAScriptObjects.STRING_TOLOWERCASE)
						|| nativeobject.equals(ECMAScriptObjects.STRING_TOLOCALELOWERCASE))
					return Value.makeStr(sthis.toLowerCase());
				else
					return Value.makeStr(sthis.toUpperCase());
			} else
				return Value.makeAnyStr();
		}

		case STRING_TOLOCALELOWERCASE: // 15.5.4.17
		case STRING_TOLOCALEUPPERCASE: { // 15.5.4.19
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			return Value.makeAnyStr();
		}
		
		case STRING_SUBSTR: { // B.2.3
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 2);
      /* Value split_pos = */ NativeFunctions.readParameter(call, state, 0);
			return Value.makeAnyStr(); // TODO: improve precision? (see test/micro/test184.js)
		}

        case STRING_TRIM: { // 15.5.4.20
            NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
            return Value.makeAnyStr();
        }
		
		default:
			return null;
		}
	}
}
