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

package dk.brics.tajs.analysis;

import static dk.brics.tajs.util.Collections.newSet;

import java.util.Set;

import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.util.Strings;

/**
 * Dispatch evaluation of native functions and common functionality used by the native functions.
 */
public class NativeFunctions { // TODO: to be replaced by the host model system

	private NativeFunctions() {
	}

	public static void evaluateGetter(HostObject nativeobject, ObjectLabel objlabel, String propertyname, Solver.SolverInterface c) {
		DOMFunctions.evaluateGetter(nativeobject, objlabel, propertyname, c);
	}

	public static void evaluateSetter(HostObject nativeobject, ObjectLabel objlabel, String propertyname, Value v, Solver.SolverInterface c) {
		DOMFunctions.evaluateSetter(nativeobject, objlabel, propertyname, v, c);
	}

	/**
	 * Issues a warning if the number of parameters is not in the given interval. max is ignored if -1.
	 */
	public static void expectParameters(HostObject hostobject, CallInfo call, Solver.SolverInterface c, int min, int max) {
		c.getMonitoring().visitNativeFunctionCall(call.getSourceNode(), hostobject, call.isUnknownNumberOfArgs(), call.getNumberOfArgs(), min, max);
		// TODO: implementations *may* throw TypeError if too many parameters to functions (p.76)
	}

	/**
	 * Reads the value of a call parameter. Returns 'undefined' if too few parameters. The first parameter has number 0.
	 */
	public static Value readParameter(CallInfo call, State state, int param) {
		int num_actuals = call.getNumberOfArgs();
		boolean num_actuals_unknown = call.isUnknownNumberOfArgs();
		if (num_actuals_unknown || param < num_actuals)
			return UnknownValueResolver.getRealValue(call.getArg(param), state);
		else
			return Value.makeUndef();
	}

	/**
	 * Reads the value of a call parameter. Only to be called if the number of arguments is unknown.
	 */
	public static Value readUnknownParameter(CallInfo call) {
		return call.getUnknownArg().joinUndef();
	}

	/**
	 * Updates the length property of any arrays among the given objects in accordance with 15.4.5.1. Also models truncation of the array if the
	 * 'length' property is being set. Sets the state to none if an exception is definitely thrown.
	 * This method only updates the 'length' property by writes to array indices, not by explicit writes to 'length'.
	 */
	public static void updateArrayLength(AbstractNode node, State state, Set<ObjectLabel> objlabels, Str propertystr, Value value,
                                         int register, Solver.SolverInterface c) {
		Set<ObjectLabel> arrays = newSet();
		for (ObjectLabel ol : objlabels)
			if (ol.getKind() == Kind.ARRAY)
				arrays.add(ol);
		if (arrays.isEmpty())
			return;
		// step 12-14 assignment to 'length', need to check for RangeError exceptions and array truncation
		boolean definitely_length = propertystr.isMaybeSingleStr() && propertystr.getStr().equals("length");
		boolean maybe_length = propertystr.isMaybeStrIdentifier() || propertystr.isMaybeStrIdentifierParts() || propertystr.isMaybeStrJSON() 
			|| (propertystr.isMaybeStrPrefixedIdentifierParts() && "length".startsWith(propertystr.getPrefix()));
        Double old_length = UnknownValueResolver.getRealValue(state.readPropertyValue(arrays, "length"), state).getNum();
		if (definitely_length || maybe_length) {
			value = UnknownValueResolver.getRealValue(value, state);
			Value numvalue = Conversion.toNumber(value, register, c); // XXX: Implicit call to valueOf/toString. See TestMicro183.
			// throw RangeError exception if illegal value
			boolean invalid = false;
			if (numvalue.isMaybeSingleNum()) {
				long uintvalue = Conversion.toUInt32(numvalue.getNum());
				if (uintvalue != numvalue.getNum())
					invalid = true;
			} else if (numvalue.isMaybeOtherThanNumUInt())
				invalid = true;
			if (invalid) {
				Exceptions.throwRangeError(state, c);
				c.getMonitoring().addMessage(node, Severity.HIGH, "RangeError, assigning invalid value to array 'length' property");			
			}
			// truncate 
			Double num = numvalue.getNum();
			if (definitely_length && num != null && old_length != null && old_length - num < 25) { // note: bound to avoid too many iterations 
                for (int i = num.intValue(); i < old_length.intValue(); i++) {
                    state.deleteProperty(arrays, Value.makeStr(Integer.toString(i)));
                }
            } else
			    state.deleteProperty(arrays, Value.makeAnyStrUInt()); 
		}
		// step 9-10 assignment to array index, need to magically update 'length'
		boolean definitely_index = propertystr.isMaybeSingleStr() && Strings.isArrayIndex(propertystr.getStr());
		boolean maybe_index = propertystr.isMaybeStrSomeUInt();
		if (definitely_index || maybe_index) {
			Value v;
            if ((definitely_index && old_length != null))
                v = Value.makeNum(Math.max(old_length, Double.valueOf(propertystr.getStr()) + 1));
            else
			    v = Value.makeAnyNumUInt();
		    state.writePropertyWithAttributes(arrays, "length", v.setAttributes(true, true, false));
		}
	}

	/**
	 * Throws a type error exception and issues a warning if the given call is a constructor call. Don't forget to set the ordinary state to none if
	 * the exception will definitely occur.
	 * 
	 * @return true if the exception is definitely thrown
	 */
	public static boolean throwTypeErrorIfConstructor(CallInfo call, State state, Solver.SolverInterface c) {
		if (call.isConstructorCall()) {
			Exceptions.throwTypeError(state, c);
			c.getMonitoring().addMessage(call.getSourceNode(), Severity.HIGH,
				"TypeError, constructor call to object that cannot be used as constructor");
			return true;
		} else
			return false;
	}

	/**
	 * Throws a type error exception and issues a warning if the kind of the 'this' object is not as expected. Don't forget to set the ordinary state
	 * to none if the exception will definitely occur.
	 * 
	 * @return true if the exception is definitely thrown
	 */
	public static boolean throwTypeErrorIfWrongKindOfThis(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c,
			ObjectLabel.Kind kind) {
		Set<ObjectLabel> this_obj = state.readThis().getObjectLabels();
		boolean some_bad = false;
		boolean some_good = false;
		for (ObjectLabel objlabel : this_obj)
			if (objlabel.getKind() != kind)
				some_bad = true;
			else
				some_good = true;
		if (some_bad) {
			Exceptions.throwTypeError(state, c);
			c.getMonitoring().addMessage(call.getSourceNode(), Severity.HIGH,
				"TypeError, native function " + nativeobject + " called on invalid object kind, expected " + kind);
		}
		return some_bad && !some_good;
	}
}
