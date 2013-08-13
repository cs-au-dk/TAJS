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

package dk.brics.tajs.analysis;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

import dk.brics.tajs.analysis.nativeobjects.ECMAScriptFunctions;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.Bool;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.util.AnalysisException;

/**
 * Type conversions for abstract values (Chapter 9).
 * 
 * Messages produced here are always of kind MAYBE (never CERTAIN) because conversion generally may occur conditionally.
 */
public class Conversion {

	/**
	 * Preferred type for conversion.
	 */
	public enum Hint {
		NONE, NUM, STR;
	}

	/**
	 * Strings that are valid StrDecimalLiteral according to 9.3.1.
	 */
	static private final Pattern STR_DECIMAL_LITERAL = 
		Pattern.compile("[-+]?(Infinity|([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([eE][-+]?[0-9]+)?)");
	
	/**
	 * Strings that are valid HexIntegerLiteral according to 9.3.1.
	 */
	static private final Pattern HEX_INTEGER_LITERAL = 
		Pattern.compile("0[xX][0-9a-fA-F]+");
	
	private Conversion() {}
	
	/**
	 * 8.6.2.6 [[DefaultValue]].
	 * Can only return primitive values.
	 */
	private static Value defaultValue(ObjectLabel obj, int register, Hint hint, Solver.SolverInterface c) {
        // When the [[DefaultValue]] method of O is called with no hint, then it behaves as if the hint were Number, 
		// unless O is a Date object (section 15.9), in which case it behaves as if the hint were String.
		if (hint == Hint.NONE)
			hint = obj.getKind() == Kind.DATE ? Hint.STR : Hint.NUM;
		State s = c.getCurrentState();
		Value result;
		boolean maybe_typeerror = false;
		if (hint == Hint.STR) {
			// When the [[DefaultValue]] method of O is called with hint String, the following steps are taken:
			// 1. Call the [[Get]] method of object O with argument "toString".
			// 2. If Result(1) is not an object, go to step 5.
			// 3. Call the [[Call]] method of Result(1), with O as the this value and an empty argument list.
			// 4. If Result(3) is a primitive value, return Result(3).
			// 5. Call the [[Get]] method of object O with argument "valueOf".
			// 6. If Result(5) is not an object, go to step 9.
			// 7. Call the [[Call]] method of Result(5), with O as the this value and an empty argument list.
			// 8. If Result(7) is a primitive value, return Result(7).
			// 9. Throw a TypeError exception.
			Value tostring = s.readPropertyWithAttributes(Collections.singleton(obj), "toString");
			tostring = UnknownValueResolver.getRealValue(tostring, s);
			result = ECMAScriptFunctions.internalToString(obj, tostring.getObjectLabels(), c);
			result = UnknownValueResolver.getRealValue(result, s);
			if (tostring.isMaybePrimitive() || result.isMaybeObject()) {
				Value valueof = s.readPropertyWithAttributes(Collections.singleton(obj), "valueOf");
				valueof = UnknownValueResolver.getRealValue(valueof, s);
				result = result.restrictToNotObject().join(ECMAScriptFunctions.internalValueOf(obj, valueof.getObjectLabels(), register, c));
				result = UnknownValueResolver.getRealValue(result, s);
				if (valueof.isMaybePrimitive() || result.isMaybeObject())
					maybe_typeerror = true;
			}
		} else if (hint == Hint.NUM) {
			// When the [[DefaultValue]] method of O is called with hint Number, the following steps are taken:
			// 1. Call the [[Get]] method of object O with argument "valueOf".
			// 2. If Result(1) is not an object, go to step 5.
			// 3. Call the [[Call]] method of Result(1), with O as the this value and an empty argument list.
			// 4. If Result(3) is a primitive value, return Result(3).
			// 5. Call the [[Get]] method of object O with argument "toString".
			// 6. If Result(5) is not an object, go to step 9.
			// 7. Call the [[Call]] method of Result(5), with O as the this value and an empty argument list.
			// 8. If Result(7) is a primitive value, return Result(7).
			// 9. Throw a TypeError exception.
			Value valueof = s.readPropertyWithAttributes(Collections.singleton(obj), "valueOf");
			valueof = UnknownValueResolver.getRealValue(valueof, s);
			result = ECMAScriptFunctions.internalValueOf(obj, valueof.getObjectLabels(), register, c);
			result = UnknownValueResolver.getRealValue(result, s);
			if (valueof.isMaybePrimitive() || result.isMaybeObject()) {
				Value tostring = s.readPropertyWithAttributes(Collections.singleton(obj), "toString");
				tostring = UnknownValueResolver.getRealValue(tostring, s);
				result = result.restrictToNotObject().join(ECMAScriptFunctions.internalToString(obj, tostring.getObjectLabels(), c));
				result = UnknownValueResolver.getRealValue(result, s);
				if (tostring.isMaybePrimitive() || result.isMaybeObject())
					maybe_typeerror = true;
			}
		} else
			throw new AnalysisException();
		if (maybe_typeerror) {
			c.getMonitoring().addMessage(c.getCurrentNode(), Severity.HIGH, "TypeError when computing default value for object"); 
			Exceptions.throwTypeError(s, c); // no ordinary flow (may be called in a loop, so don't set s to none)
		} 
        return result.restrictToNotObject();
	}

    /**
     * 9.1 ToPrimitive.
     * Converts a value to a primitive value according to the hint.
     * Has no effect on primitive types but transforms wrapper objects to their wrapped values.
     */
    public static Value toPrimitive(Value v, Hint hint, Solver.SolverInterface c) {
        return toPrimitive(v, AbstractNode.NO_VALUE, hint, c);
    }
        /**
       * 9.1 ToPrimitive.
       * Converts a value to a primitive value according to the hint.
       * Has no effect on primitive types but transforms wrapper objects to their wrapped values.
       */
	public static Value toPrimitive(Value v, int register, Hint hint, Solver.SolverInterface c) {
        v = UnknownValueResolver.getRealValue(v, c.getCurrentState());
		Collection<Value> vs = newList();
		Value nonobj = v.restrictToNotObject(); // The result equals the input argument (no conversion).
		if (!nonobj.isNotPresent())
			vs.add(nonobj);
		for (ObjectLabel ol : v.getObjectLabels()) {
			// Return a default value for the Object. The default value of an object is
			// retrieved by calling the internal [[DefaultValue]] method of the object,
			// passing the optional hint PreferredType.
			vs.add(defaultValue(ol, register, hint, c));
		}
        return UnknownValueResolver.join(vs, c.getCurrentState());
	}
	
	/**
	 * 9.2 ToBoolean.
	 */
	public static Value toBoolean(Value v) {
		Value result = v.restrictToBool(); // The result equals the input argument (no conversion).
		if (v.isMaybeUndef()) {
			// false
			result = result.joinBool(false);
		}
		if (v.isMaybeNull()) {
			// false
			result = result.joinBool(false);
		}
		if (!v.isNotNum()) {
			// The result is false if the argument is +0, -0, or NaN; otherwise the result is true.
			if (v.isMaybeNaN())
				result = result.joinBool(false);
			if (v.isMaybeSingleNum()) {
				if (Math.abs(v.getNum()) == 0.0)
					result = result.joinBool(false);
				else
					result = result.joinBool(true);
			} else
				result = result.joinAnyBool();
		}
		if (!v.isNotStr()) {
			// The result is false if the argument is the empty string (its length is zero); otherwise the result is true.
			if (v.isMaybeSingleStr()) {
				if (v.getStr().equals(""))
					result = result.joinBool(false);
				else
					result = result.joinBool(true);
			} else if (v.isMaybeStrOther() || v.isMaybeStrIdentifierParts() || v.isMaybeStrJSON())
				result = result.joinAnyBool();
			else
				result = result.joinBool(true);
		}
		if (v.isMaybeObject()) {
			// true
			result = result.joinBool(true);
		}
		return result;
	}

    /**
     * 9.3 ToNumber.
     */
    public static Value toNumber(Value v, Solver.SolverInterface c) {
        return toNumber(v, AbstractNode.NO_VALUE, c);
    }
        /**
       * 9.3 ToNumber.
       */
	public static Value toNumber(Value v, int register, Solver.SolverInterface c) {
		if (v.isMaybeObject()) {
			// Call ToPrimitive(input argument, hint Number).
			v = toPrimitive(v, register, Hint.NUM, c);
		}
		Value result = v.restrictToNum(); // The result equals the input argument (no conversion).
		if (v.isMaybeUndef()) {
			// NaN
			result = result.joinNumNaN();
			c.getMonitoring().addMessage(c.getCurrentNode(), Severity.LOW, "Conversion to number yields NaN");
		}
		if (v.isMaybeNull()) {
			// +0
			result = result.joinNum(0.0);
		}
		result = Value.join(result, fromBooltoNum(v), fromStrtoNum(v, c));
		return result;
	}
	
	/**
	 * 9.3 Boolean to Number.
	 */
	public static Value fromBooltoNum(Bool b) {
		// The result is 1 if the argument is true. The result is +0 if the argument is	false.
		if (b.isNotBool())
			return Value.makeNone();
		else if (b.isMaybeAnyBool())
			return Value.makeAnyNumUInt(); // join of 1 and +0
		else if (b.isMaybeFalseButNotTrue())
			return Value.makeNum(0.0);
		else
			return Value.makeNum(1.0);
	}

	/**
	 * 9.3.1 String to Number.
	 */
	public static Value fromStrtoNum(Str str, Solver.SolverInterface c) {
		Value v;
		if (str.isMaybeSingleStr()) {
			String s = str.getStr();
			if (s.equals(""))
				return Value.makeNum(0.0);
			else {
				s = s.trim();
				if (STR_DECIMAL_LITERAL.matcher(s).matches())
					v = Value.makeNum(new Double(s));
				else if (HEX_INTEGER_LITERAL.matcher(s).matches())
					v = Value.makeNum(Long.parseLong(s.substring(2), 16));
				else
					v = Value.makeNumNaN();
			}
		} else {
			if (str.isMaybeStrIdentifierParts() || str.isMaybeStrPrefixedIdentifierParts() || str.isMaybeStrJSON()
					|| (str.isMaybeStrUInt() && str.isMaybeStrOtherNum()))
				v = Value.makeAnyNum(); // TODO: could be more precise for STR_PREFIX if the prefix string is not a UInt
			else if (str.isMaybeStrUInt()) {
				v = Value.makeAnyNumUInt();
				if (str.isMaybeStrIdentifier())
					v = v.joinNumNaN().joinNumInf();
			} else if (str.isMaybeStrOtherNum())
				v = Value.makeAnyNumOther().joinNumNaN().joinNumInf();
			else if (str.isMaybeStrIdentifier())
				v = Value.makeNumNaN().joinNumInf();
			else
				v = Value.makeNone();
		}
		if (v.isMaybeNaN())
			c.getMonitoring().addMessage(c.getCurrentNode(), Severity.LOW, "Conversion from string to number yields NaN");
		return v;
	}

    /**
     * 9.4 ToInteger.
     */
    public static Value toInteger(Value v, Solver.SolverInterface c) {
        return toInteger(v, AbstractNode.NO_VALUE, c);
    }

        /**
       * 9.4 ToInteger.
       */
	public static Value toInteger(Value v, int register, Solver.SolverInterface c) {
		Value num = toNumber(v, register, c);
		if (num.isNotNum())
			return num;
		if (num.isNaN())
			return Value.makeNum(0);
		if (num.isMaybeSingleNum()) {
			Double d = num.getNum();
			if (d == 0.0 || d == -0.0 || Double.isInfinite(d))
				return num;
			else
				return Value.makeNum(Math.signum(d) * Math.floor(Math.abs(d)));			
		} else {
			Value r = Value.makeNone();
			if (num.isMaybeNaN())
				r = r.joinNum(0);
			if (num.isMaybeInf())
				r = r.joinNumInf();
			if (num.isMaybeNumUInt()) 
				r = r.joinAnyNumUInt();
			if (num.isMaybeNumOther()) 
				r = r.joinAnyNumUInt().joinAnyNumOther(); // may overflow UInt32
			return r;
		}
	}

	/**
	 * 9.5 ToInt32.
	 */
	public static int toInt32(double nm) {
		if (Double.isNaN(nm) || Double.isInfinite(nm))
			return 0;
		double w = Math.signum(nm) * Math.floor(Math.abs(nm));
		Double v = w % 4294967296L;
		if (v < 0)
			v += 4294967296L;
		if (v > 2147483648L) 
			v -= 4294967296L;
		return v.intValue();
	}

	/**
	 * 9.6 ToUInt32.
	 */
	public static long toUInt32(double nm) {
		if (Double.isNaN(nm) || Double.isInfinite(nm)) 
			return 0L;
		double w = Math.signum(nm) * Math.floor(Math.abs(nm));
		Double v = w % 4294967296L;
		if (v < 0)
			v += 4294967296L;
		return v.longValue();
	}
	
	/**
	 * 9.7 ToUInt16.
	 */
	public static long toUInt16(double nm) {
		if (Double.isNaN(nm) || Double.isInfinite(nm) || nm == -0.0)
			return 0L;
		double w = Math.signum(nm) * Math.floor(Math.abs(nm));
		Double v = w % 65536L;
		if (v < 0)
			v += 65536L;
		return v.longValue();
	}

    /**
     * 9.8 ToString.
     */
    public static Value toString(Value v, Solver.SolverInterface c) {
        return toString(v, AbstractNode.NO_VALUE, c);
    }

	/**
	 * 9.8 ToString.
	 */
	public static Value toString(Value v, int register, Solver.SolverInterface c) {
		v = UnknownValueResolver.getRealValue(v, c.getCurrentState());
        // object to string
		if (v.isMaybeObject()) {
			// Call ToPrimitive(input argument, hint String).
			v = toPrimitive(v, register, Hint.STR, c);
	        c.getMonitoring().addMessage(c.getCurrentNode(), Severity.LOW, "Converting object to string");		
		}
		// string
		Value result = v.restrictToStr(); // Return the input argument (no conversion)
		// boolean to string
		if (!v.isNotBool()) {
			// If the argument is true, then the result is "true".
			// If the argument is false, then the result is "false".
			if (v.isMaybeAnyBool())
				result = result.joinAnyStrIdentifierParts(); // join of "true" and "false"
			else if (v.isMaybeTrueButNotFalse())
				result = result.joinStr("true");
			else
				result = result.joinStr("false");
	        c.getMonitoring().addMessage(c.getCurrentNode(), Severity.LOW, "Converting boolean to string");		
		}
		// number to string
		if (!v.isNotNum()) {
			// 9.8.1 ToString Applied to the Number Type
			if (v.isMaybeSingleNum()) {
				// single number to string
				String s;
				double dbl = v.getNum();
				if (Double.isInfinite(dbl))
					s = Double.toString(dbl);
				else if (Math.floor(dbl) == dbl)
					s = Long.toString((long)dbl);
				else
					s = Double.toString(dbl);
				result = result.joinStr(s);
			} else {
				// NaN to string
				if (v.isMaybeNaN())
					result = result.joinStr(Double.toString(Double.NaN));
				// uint/other/inf to string
				if (v.isMaybeNumUInt())
					result = result.joinAnyStrUInt();
				if (v.isMaybeNumOther())
					result = result.joinAnyStrOtherNum();
				if (v.isMaybeInf())
					result = result.joinAnyStrOther();
			}
			// TODO: warn about number-to-string conversion? (presumably rarely indicating a bug)
	        // c.getMonitoring().addMessage(c.getCurrentNode(), Severity.LOW, "Converting number to string");
		}
		// null to string
		if (v.isMaybeNull()) {
			// "null"
			result = result.joinStr("null");
	        c.getMonitoring().addMessage(c.getCurrentNode(), Severity.LOW, "Converting null to string");
		}
        // undefined to string
		if (v.isMaybeUndef()) {
			// "undefined"
			result = result.joinStr("undefined");
	        c.getMonitoring().addMessage(c.getCurrentNode(), Severity.LOW, "Converting undefined to string");
		}
		return result;
	}

	/**
	 * 9.9 ToObject, returning a Value.
	 * Note that this may have side-effects on the state!
	 * However, if the solver interface is null, no side-effects or messages are produced (but all object labels are still returned).
	 */
	public static Value toObject(State state, AbstractNode node, Value v, Solver.SolverInterface c) {
		return Value.makeObject(toObjectLabels(state, node, v, c));
	}
	
	/**
	 * 9.9 ToObject, returning a set of object labels.
	 * Note that this may have side-effects on the state!
	 * However, if the solver interface is null, no side-effects or messages are produced (but all object labels are still returned).
	 */
	public static Set<ObjectLabel> toObjectLabels(State state, AbstractNode node, Value v, Solver.SolverInterface c) {
		Set<ObjectLabel> result = newSet();
		// Object: The result is the input argument (no conversion).
		result.addAll(v.getObjectLabels());
        // primitive number to object
		if (!v.isNotNum()) {
			// Create a new Number object whose [[value]] property is set to the value of the number.
			ObjectLabel lNum = new ObjectLabel(node, Kind.NUMBER);
			if (c != null) {
				state.newObject(lNum);
				state.writeInternalPrototype(lNum, Value.makeObject(InitialStateBuilder.NUMBER_PROTOTYPE));
				state.writeInternalValue(lNum, v.restrictToNum());
	            c.getMonitoring().addMessage(c.getCurrentNode(), Severity.LOW, "Converting primitive number to object");
			}
			result.add(lNum);
		}
        // primitive boolean to object
		if (!v.isNotBool()) {
			// Create a new Boolean object whose [[value]] property is set to the value of the boolean.
			ObjectLabel lBool = new ObjectLabel(node, Kind.BOOLEAN);
			if (c != null) {
				state.newObject(lBool);
				state.writeInternalPrototype(lBool, Value.makeObject(InitialStateBuilder.BOOLEAN_PROTOTYPE));
				state.writeInternalValue(lBool, v.restrictToBool());
	            c.getMonitoring().addMessage(c.getCurrentNode(), Severity.LOW, "Converting primitive boolean to object");
			}
			result.add(lBool);
		}
        // primitive string to object
		if (!v.isNotStr()) {
			// Create a new String object whose [[value]] property is set to the value of the string.
			Value vstring = v.restrictToStr();
			Value vlength = vstring.isMaybeSingleStr() ? Value.makeNum(vstring.getStr().length()) : Value.makeAnyNumUInt();
			ObjectLabel lString = new ObjectLabel(node, Kind.STRING);
			if (c != null) {
				state.newObject(lString);
				state.writeInternalPrototype(lString, Value.makeObject(InitialStateBuilder.STRING_PROTOTYPE));
				state.writeInternalValue(lString, vstring);
				state.writePropertyWithAttributes(lString, "length", vlength.setAttributes(true, true, true));
	            c.getMonitoring().addMessage(c.getCurrentNode(), Severity.LOW, "Converting primitive string to object");
			}
			result.add(lString);
		}
		// null to object
		if (!v.isNotNull()) {
			// Throw a TypeError exception.
			if (c != null) {
				Exceptions.throwTypeError(state, c);
				// TODO: warn about null-to-object conversion? (we already have Monitoring.visitPropertyAccess)
				// c.getMonitoring().addMessage(c.getCurrentNode(), Severity.HIGH, "TypeError, attempt to convert null to object");
			}
		}
		// undefined to object
		if (!v.isNotUndef()) {
			// Throw a TypeError exception.
			if (c != null) {
				Exceptions.throwTypeError(state, c);
				// TODO: warn about undefined-to-object conversion? (we already have Monitoring.visitPropertyAccess)
				// c.getMonitoring().addMessage(c.getCurrentNode(), Severity.HIGH, "TypeError, attempt to convert undefined to object");
			}
		}
		return result;
	}
}
