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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.util.AnalysisException;

/**
 * 15.7 native Number functions.
 */
public class JSNumber {

	private JSNumber() {}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (nativeobject != ECMAScriptObjects.NUMBER)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeNone();

		switch (nativeobject) {

		case NUMBER: {
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
			Value v;
			if (call.isUnknownNumberOfArgs())
				v =  Conversion.toNumber(NativeFunctions.readParameter(call, state, 0), c).joinNum(+0.0d);
			else if (call.getNumberOfArgs() >= 1)
				v = Conversion.toNumber(NativeFunctions.readParameter(call, state, 0), c);
			else
				v = Value.makeNum(+0.0d);
			if (call.isConstructorCall()) { // 15.7.2
				ObjectLabel objlabel = new ObjectLabel(call.getSourceNode(), Kind.NUMBER);
				state.newObject(objlabel);
				state.writeInternalValue(objlabel, v);
				state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.NUMBER_PROTOTYPE));
				return Value.makeObject(objlabel);
			} else // 15.7.1
				return v;
		}

		case NUMBER_TOFIXED: // 15.7.4.5
		case NUMBER_TOEXPONENTIAL: // 15.7.4.6
		case NUMBER_TOPRECISION: { // 15.7.4.7

			NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.NUMBER))
				return Value.makeNone();
			//return Value.makeAnyStr();
			Value f;
            int args = call.getNumberOfArgs();
			if (call.isUnknownNumberOfArgs())
				f = Conversion.toInteger(NativeFunctions.readParameter(call, state, 0), c).joinNum(0);
			else if (args >= 1)
				f = Conversion.toInteger(NativeFunctions.readParameter(call, state, 0), c);
			else
				f = Value.makeNum(0);
			Value x = state.readInternalValue(state.readThisObjects());
			boolean definitely_rangeerror = false;
			boolean maybe_rangeerror = false;
			int f_int = 0;
			if (f.isMaybeSingleNum()) {
				f_int = f.getNum().intValue();
				if (f_int < 0 || f_int > 20)
					definitely_rangeerror = true;
			} else
				maybe_rangeerror = true;
			if (maybe_rangeerror || definitely_rangeerror) {
				Exceptions.throwRangeError(state, c);
				c.getMonitoring().addMessage(call.getSourceNode(), Severity.HIGH, "RangeError in Number function");
			}
			if (definitely_rangeerror)
				return Value.makeNone();
			if (x.isMaybeNaN())
				return Value.makeStr("NaN");
			if (x.isMaybeFuzzyNum() || f.isMaybeFuzzyNum())
				return Value.makeAnyStr();
			double x_num = x.getNum();
			if (Double.isInfinite(x_num)) {
				StringBuilder sb = new StringBuilder();
				if (x_num < 0) {
					x_num = -x_num; // FIXME: unused!?
					sb.append('-');
				}
				sb.append("Infinity");
				return Value.makeStr(sb.toString());
			}
			// rough approximation, best effort with Java formatting
			// TODO: improve NUMBER_TOEXPONENTIAL, TO_FIXED, TO_PRECISION?
			// probably an implementation of the specification is required
			// alternative: steal from org.mozilla.javascript
			// however, the needed classes are not public! Eg. DToA.java
			String flag;
			switch (nativeobject) {
			case NUMBER_TOFIXED:
                // This method rounds the value when called with 0 arguments.
                if (args == 0)
                    flag = "%.0f";
                else
                    flag = String.format("%%1.%df", f_int);
				break;
			case NUMBER_TOEXPONENTIAL:
                // "The number of digits necessary to represent the number uniquely"
                // TODO: Improve precision
                if (args == 0)
                    flag = "%e";
                else
                    flag = String.format("%%1.%de", f_int);
				break;
			case NUMBER_TOPRECISION:
                // This methods behaves like toString() when called with 0 arguments.
                // Special case when x_num is 0.0 to avoid crashing in String.format("%1.0g", x_num).
                if (args == 0)
                    return Conversion.toString(x, c);
                else if (Double.compare(x_num, 0.0) == 0)
                    flag = "0";
                else
                    flag = String.format("%%1.%dg", f_int);
				break;
			default:
				throw new AnalysisException();
			}
			String result = String.format(flag, x_num);
			return Value.makeStr(result);
		}		
		
		case NUMBER_TOLOCALESTRING: // 15.7.4.3 
		case NUMBER_TOSTRING: { // 15.7.4.2
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.NUMBER))
				return Value.makeNone();
			Value val = state.readInternalValue(state.readThisObjects());
			if (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() == 0)
				return Conversion.toString(val, c);
			else
				return Value.makeAnyStr();
			// TODO: assuming that toLocaleString methods behave as toString (also other objects) - OK?
		}
		
		case NUMBER_VALUEOF: { // 15.7.4.4
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			if (NativeFunctions.throwTypeErrorIfWrongKindOfThis(nativeobject, call, state, c, Kind.NUMBER))
				return Value.makeNone();
			return state.readInternalValue(state.readThisObjects());
		}
			
		default:
			return null;
		}
	}
}
