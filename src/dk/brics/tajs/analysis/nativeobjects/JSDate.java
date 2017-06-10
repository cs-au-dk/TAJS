/*
 * Copyright 2009-2017 Aarhus University
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

import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteNumber;
import dk.brics.tajs.analysis.nativeobjects.concrete.SingleGamma;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;

/**
 * 15.9 and B.2 native Date functions.
 */
public class JSDate {

    private JSDate() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, Solver.SolverInterface c) {
        // TODO: warn about year 2000 problem for getYear/setYear?

        State state = c.getState();
        switch (nativeobject) {

            case DATE: { // 15.9.3
                if (call.isConstructorCall()) {
                    Value internal;
                    if(!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() == 0){
                        internal = getNow(call, c);
                    }else{
                        internal = Value.makeAnyNum();
                    }
                    return createDateObject(call.getSourceNode(), internal, state);
                } else // 15.9.2
                    return Value.makeAnyStr();
            }

            case DATE_GETFULLYEAR: // 15.9.5.10
            case DATE_GETUTCFULLYEAR: // 15.9.5.11
            case DATE_GETMONTH: // 15.9.5.12
            case DATE_GETUTCMONTH: // 15.9.5.13
            case DATE_GETDATE: // 15.9.5.14
            case DATE_GETUTCDATE: // 15.9.5.15
            case DATE_GETDAY: // 15.9.5.16
            case DATE_GETUTCDAY: // 15.9.5.17
            case DATE_GETHOURS: // 15.9.5.18
            case DATE_GETUTCHOURS: // 15.9.5.19
            case DATE_GETMINUTES: // 15.9.5.20
            case DATE_GETUTCMINUTES: // 15.9.5.21
            case DATE_GETSECONDS: // 15.9.5.22
            case DATE_GETUTCSECONDS: // 15.9.5.23
            case DATE_GETMILLISECONDS: // 15.9.5.24
            case DATE_GETUTCMILLISECONDS: // 15.9.5.25
            case DATE_GETTIMEZONEOFFSET: // 15.9.5.26
            case DATE_GETYEAR: { // B.2.4
                return concrete(nativeobject.toString(), Value.makeAnyNum(), c);
            }

            case DATE_GETTIME: { // 15.9.5.9
                return concrete(nativeobject.toString(), Value.makeAnyNum(), c);
            }

            case DATE_PARSE: // 15.9.4.2
            case DATE_SETDATE: // 15.9.5.36
            case DATE_SETUTCDATE: // 15.9.5.37
            case DATE_SETYEAR: // B.2.5
            case DATE_SETHOURS: // 15.9.5.35
            case DATE_SETUTCHOURS: // 15.9.5.36
            case DATE_SETMILLISECONDS: // 15.9.5.28
            case DATE_SETUTCMILLISECONDS: // 15.9.5.29
            case DATE_SETMINUTES: // 15.9.5.33
            case DATE_SETUTCMINUTES: // 15.9.5.34
            case DATE_SETFULLYEAR: // 15.9.5.40
            case DATE_SETUTCFULLYEAR: // 15.9.5.41
            case DATE_SETSECONDS: // 15.9.5.30
            case DATE_SETUTCSECONDS: // 15.9.5.31
            case DATE_SETMONTH: // 15.9.5.38
            case DATE_SETUTCMONTH: // 15.9.5.39
            case DATE_SETTIME: { // 15.9.5.27
                // TODO perform side-effects on the date object (currently unsound)
                return Value.makeAnyNum();
            }

            case DATE_TOSTRING: // 15.9.5.2
            case DATE_TODATESTRING: // 15.9.5.3
            case DATE_TOTIMESTRING: // 15.9.5.4
            case DATE_TOLOCALESTRING: // 15.9.5.5
            case DATE_TOLOCALEDATESTRING: // 15.9.5.6
            case DATE_TOLOCALETIMESTRING: { // 15.9.5.7
                return evaluateToString(state.readThis(), c);
            }

            case DATE_TOUTCSTRING: // 15.9.5.42
            case DATE_TOGMTSTRING: { // B.2.6
                return Value.makeAnyStr();
            }

            case DATE_UTC: { // 15.9.4.3
                return c.getState().readInternalValue(createDateObject(call.getSourceNode(), Value.makeAnyNum(), state).getObjectLabels());
            }

            case DATE_VALUEOF: { // 15.9.5.8
                return concrete(nativeobject.toString(), c.getState().readInternalValue(c.getState().readThisObjects()), c);
            }

            case DATE_NOW: { // 15.9.4.4
                return getNow(call, c);
            }

            default:
                return null;
        }
    }

    private static Value concrete(String functionName, Value defaultValue, Solver.SolverInterface c) {
        // TODO support Date in ConcreteSemantics?
        Value internal = c.getState().readInternalValue(c.getState().readThisObjects());
        internal = UnknownValueResolver.getRealValue(internal, c.getState());
        if (!SingleGamma.isConcreteNumber(internal, c)) {
            return defaultValue;
        }
        ConcreteNumber concreteInternal = SingleGamma.toConcreteNumber(internal, c);
        String code = String.format("%s.call(new Date(%d))", functionName, Double.valueOf(concreteInternal.getNumber()).longValue());
        return TAJSConcreteSemantics.eval(code);
    }

    private static Value getNow(CallInfo call, Solver.SolverInterface c) {
        return Value.makeAnyNum();
    }

    /**
     * Creates a new Date object.
     */
    private static Value createDateObject(AbstractNode n, Value internal, State state) {
        ObjectLabel objlabel = ObjectLabel.make(n, Kind.DATE);
        state.newObject(objlabel);
        state.writeInternalValue(objlabel, internal);
        state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.DATE_PROTOTYPE));
        return Value.makeObject(objlabel);
    }

    public static Value evaluateToString(Value thisval, Solver.SolverInterface c) {
        boolean is_maybe_typeerror = thisval.isMaybePrimitive();
        boolean is_maybe_anystring = false;
        for (ObjectLabel thisObj : thisval.getObjectLabels()) {
            if (thisObj.getKind() == Kind.DATE) {
                is_maybe_anystring = true;
            } else {
                is_maybe_typeerror = true;
            }
        }
        if (is_maybe_typeerror) {
            Exceptions.throwTypeError(c);
        }
        if (is_maybe_anystring) {
            return Value.makeAnyStr();
        } else {
            return Value.makeNone();
        }
    }
}
