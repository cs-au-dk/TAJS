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

import java.util.Set;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.lattice.ObjectLabel.Kind;

/**
 * 15.2 native Object functions.
 */
public class JSObject {

	private JSObject() {}

	/**
	 * Evaluates the given native function.
	 */
	public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, State state, Solver.SolverInterface c) {
		if (nativeobject != ECMAScriptObjects.OBJECT)
			if (NativeFunctions.throwTypeErrorIfConstructor(call, state, c))
				return Value.makeNone();

		switch (nativeobject) {

		case OBJECT: { // 15.2.1 and 15.2.2
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 1);
			Value arg = NativeFunctions.readParameter(call, state, 0);
            Value arg2 = arg.restrictToNotNullNotUndef().restrictToNotObject();
            boolean arg_maybe_other = arg2.isMaybeOtherThanUndef() && arg2.isMaybeOtherThanNull();
            // 15.2.1.1 step 2 for objects and 15.2.2.1 step 3 in one swoop. Slightly cheating, but toObject(Obj) = Obj.
			Value res = arg.restrictToObject();
            // 15.2.1.1 step 2 for non-objects and 15.2.2.1 step 5-7.
            res = arg_maybe_other ? res.join(Conversion.toObject(state, call.getSourceNode(), arg2.restrictToStrBoolNum(), c)) : res;
			if (arg.isMaybeNull() || arg.isMaybeUndef()) {
				// 15.2.1.1 step 1 and 15.2.2.1 step 8
				ObjectLabel obj = new ObjectLabel(call.getSourceNode(), Kind.OBJECT);
				state.newObject(obj);
				state.writeInternalPrototype(obj, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
				res = res.joinObject(obj);
			}
			return res;
		}
		case OBJECT_DEFINE_PROPERTY: { // 15.2.3.6
			NativeFunctions.expectParameters(nativeobject, call, c, 2, 2); // TODO allow 1-3 arguments
        	Value o = NativeFunctions.readParameter(call, state, 0);
        	Value nameString = Conversion.toString(NativeFunctions.readParameter(call, state, 1), c);
        	state.writeProperty(o.getObjectLabels(), nameString, Value.makeUndef(), true, false); // FIXME: unsound (?)
			return NativeFunctions.readParameter(call, state, 0);
		}

		case OBJECT_TOSTRING: // 15.2.4.2
		case OBJECT_TOLOCALESTRING: { // 15.2.4.3
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			Set<ObjectLabel> thisobj = state.readThisObjects();
			if (thisobj.size() == 1)
				return Value.makeStr("[object " + thisobj.iterator().next().getKind() + "]");
			else
				return Value.makeAnyStr(); // TODO: better approximation of string sets?
		}
		
		case OBJECT_VALUEOF: { // 15.2.4.4
			NativeFunctions.expectParameters(nativeobject, call, c, 0, 0);
			return Value.makeObject(state.readThisObjects());
		}

		case OBJECT_HASOWNPROPERTY: { // 15.2.4.5
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Set<ObjectLabel> thisobj = state.readThisObjects();
            Value v = NativeFunctions.readParameter(call, state, 0);
            // Result is only defined when called with a parameter. Return false if not, just like Safari.
            if (!v.isMaybeOtherThanUndef())
                return Value.makeBool(false);
			Value propval = Conversion.toString(v, c);
			if (propval.isNotStr())
				return Value.makeNone();
			else if (propval.isMaybeSingleStr()) {
				String propname = propval.getStr();
				Value val = state.readPropertyDirect(thisobj, propname);
				Value res = Value.makeNone();
				if (val.isMaybeAbsent() || val.isNotPresent())
					res = res.joinBool(false);
				if (val.isMaybePresent())
					res = res.joinBool(true);
				return res;
			}
            for (ObjectLabel ol : thisobj) {
                Obj o = state.getObject(ol, false);
                for (String propname : o.getProperties().keySet()) {
                	if (UnknownValueResolver.getProperty(ol, propname, state, true).isMaybePresent())
                        return Value.makeAnyBool();
                }
                if (UnknownValueResolver.getDefaultArrayProperty(ol, state).isMaybePresent() ||
                		UnknownValueResolver.getDefaultNonArrayProperty(ol, state).isMaybePresent())
                    return Value.makeAnyBool();
            }
            return Value.makeBool(false);
		}

		case OBJECT_ISPROTOTYPEOF: { // 15.2.4.6
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
            /* Value arg = */ NativeFunctions.readParameter(call, state, 0);
			return Value.makeAnyBool(); // TODO: improve precision for OBJECT_ISPROTOTYPEOF
		}

		case OBJECT_PROPERTYISENUMERABLE: { // 15.2.4.7
			NativeFunctions.expectParameters(nativeobject, call, c, 1, 1);
			Set<ObjectLabel> thisobj = state.readThisObjects();
			Value propval = Conversion.toString(NativeFunctions.readParameter(call, state, 0), c);
			if (propval.isNotStr())
				return Value.makeNone();
			else if (propval.isMaybeSingleStr()) {
				String propname = propval.getStr();
				Value val = state.readPropertyDirect(thisobj, propname);
				Value res = Value.makeNone();
				if (val.isMaybeAbsent() || val.isMaybeDontEnum() || val.isNotPresent())
					res = res.joinBool(false);
				if (val.isMaybePresent() && val.isMaybeNotDontEnum())
					res = res.joinBool(true);
				return res;
			}
            for (ObjectLabel ol : thisobj) {
                Obj o = state.getObject(ol, false);
                for (String propname : o.getProperties().keySet()) {
                	if (UnknownValueResolver.getProperty(ol, propname, state, true).isMaybeNotDontEnum())
                        return Value.makeAnyBool();
                }
                if (UnknownValueResolver.getDefaultArrayProperty(ol, state).isMaybeNotDontEnum() ||
                		UnknownValueResolver.getDefaultNonArrayProperty(ol, state).isMaybeNotDontEnum())
                    return Value.makeAnyBool();
            }
            return Value.makeBool(false);
        }

		default:
			return null;
		}
	}
}
