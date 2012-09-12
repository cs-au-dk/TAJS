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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.lattice.ObjectLabel.Kind;

/**
 * 15.11 native Error functions.
 */
public class JSError {

	private JSError() {}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (nativeobject == ECMAScriptObjects.ERROR_TOSTRING)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeNone();

		switch (nativeobject) {

		case ERROR: { // 15.11.1 / 15.11.2 (function calls act like constructors here, also below)
			return createErrorObject(InitialStateBuilder.ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case EVAL_ERROR: { // 15.11.6.1
			return createErrorObject(InitialStateBuilder.EVAL_ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case RANGE_ERROR: { // 15.11.6.2
			return createErrorObject(InitialStateBuilder.RANGE_ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case REFERENCE_ERROR: { // 15.11.6.3
			return createErrorObject(InitialStateBuilder.REFERENCE_ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case SYNTAX_ERROR: { // 15.11.6.4
			return createErrorObject(InitialStateBuilder.SYNTAX_ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case TYPE_ERROR: { // 15.11.6.5
			return createErrorObject(InitialStateBuilder.TYPE_ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case URI_ERROR: { // 15.11.6.6
			return createErrorObject(InitialStateBuilder.URI_ERROR_PROTOTYPE, nativeobject, call, state, c);
		}

		case ERROR_TOSTRING: { // 15.11.4.4
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			return Value.makeAnyStr();
		}
			
		default:
			return null;
		}
	}
	
	/**
	 * Creates a new error object according to 15.11.1.1 / 15.11.2.1 / 15.11.7.2 / 15.11.7.4.
	 */
	private static Value createErrorObject(ObjectLabel proto, ECMAScriptObjects nativeobject, CallInfo call, 
			State state, Solver.SolverInterface c) {
		NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
		ObjectLabel obj = new ObjectLabel(call.getSourceNode(), Kind.ERROR);
		state.newObject(obj);
		state.writeInternalPrototype(obj, Value.makeObject(proto));
		Value message = NativeFunctions.readParameter(call, state, 0);
		if (message.isMaybeOtherThanUndef())
			state.writeProperty(obj, "message", Conversion.toString(message.restrictToNotUndef(), c).removeAttributes());
		return Value.makeObject(obj);
	}
}
