/*
 * Copyright 2009-2019 Aarhus University
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
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;

import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * 15.6 native Boolean functions.
 */
public class JSBoolean {

    private JSBoolean() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, Solver.SolverInterface c) {
        State state = c.getState();
        switch (nativeobject) {

            case BOOLEAN: {
                Value b = Conversion.toBoolean(FunctionCalls.readParameter(call, state, 0));
                if (call.isConstructorCall()) { // 15.6.2
                    return Conversion.toObject(call.getSourceNode(), b, false, c);
                } else // 15.6.1
                    return b;
            }

            case BOOLEAN_TOSTRING: { // 15.6.4.2
                return evaluateToString(state.readThis(), c);
            }

            case BOOLEAN_VALUEOF: { // 15.6.4.3
                Set<ObjectLabel> booleanObjects = newSet(state.readThisObjects());
                booleanObjects.removeIf(l -> l.getKind() != Kind.BOOLEAN);  // exceptions handled by NativeFunctionSignatureChecker
                return state.readInternalValue(booleanObjects);
            }

            default:
                return null;
        }
    }

    public static Value evaluateToString(Value thisval, Solver.SolverInterface c) {
        boolean is_maybe_true = thisval.isMaybeTrue();
        boolean is_maybe_false = thisval.isMaybeFalse();
        boolean is_maybe_typeerror = !thisval.isNotStr() || !thisval.isNotNum() || thisval.isNullOrUndef();
        for (ObjectLabel thisObj : thisval.getObjectLabels()) {
            if (thisObj.getKind() != Kind.BOOLEAN) {
                is_maybe_typeerror = true;
            } else {
                Value v = c.getState().readInternalValue(singleton(thisObj));
                v = UnknownValueResolver.getRealValue(v, c.getState());
                if (v.isMaybeTrue()) {
                    is_maybe_true = true;
                }
                if (v.isMaybeFalse()) {
                    is_maybe_false = true;
                }
            }
        }
        if (is_maybe_typeerror) {
            Exceptions.throwTypeError(c);
        }
        if (is_maybe_true) {
            if (is_maybe_false) {
                return Value.makeAnyStr();
            } else {
                return Value.makeStr("true");
            }
        } else {
            if (is_maybe_false) {
                return Value.makeStr("false");
            } else {
                return Value.makeNone();
            }
        }
    }
}
