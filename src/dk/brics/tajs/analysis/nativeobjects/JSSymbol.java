/*
 * Copyright 2009-2020 Aarhus University
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
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import java.util.List;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * EC6 Symbol support.
 */
public class JSSymbol {

    // Symbols that are created, for example, using Symbol.for().

    private JSSymbol() {
    }

    /**
     * Evaluates the given native function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, final CallInfo call, Solver.SolverInterface c) {
        State state = c.getState();
        switch (nativeobject) {
            case SYMBOL: {
                if (call.isConstructorCall()) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                if (call.getNumberOfArgs() > 0)
                    Conversion.toString(FunctionCalls.readParameter(call, state, 0), c); // TODO: currently abstracting away the symbol names (possible solution: extra string field in (sub-class of) ObjectLabel?) - github #513
                ObjectLabel no = ObjectLabel.make(call.getSourceNode(), Kind.SYMBOL); //TODO: Object label with arbitrary allocation site to also account for Symbol.for(key)? - github #511
                state.newObject(no);
                state.writeInternalPrototype(no, Value.makeObject(InitialStateBuilder.SYMBOL_PROTOTYPE));
                return Value.makeSymbol(no);
            }
            case SYMBOL_FOR: {
                if (call.getNumberOfArgs() > 0)
                    Conversion.toString(FunctionCalls.readParameter(call, state, 0), c);
                return Value.makeSymbol(InitialStateBuilder.UNKNOWN_SYMBOL_INSTANCES); // TODO: github #511
            }
            case SYMBOL_VALUEOF: {
                return evaluateToSymbol(state.readThis(), c);
            }
            case SYMBOL_TOSTRING:
                return Value.makeAnyStr();
            case SYMBOL_KEYFOR:
                throw new AnalysisException("Model for Symbol.keyFor not implemented"); //TODO
            case SYMBOL_TOSOURCE:
                throw new AnalysisException("Model for Symbol.toSource not implemented"); //TODO
            default:
                return null;
        }
    }

    public static Value evaluateToSymbol(Value thisval, Solver.SolverInterface c) {
        List<Value> symbols = newList();
        boolean is_maybe_typeerror = thisval.isMaybePrimitive();
        symbols.add(thisval.restrictToSymbol());
        for (ObjectLabel thisObj : thisval.getObjectLabels()) {
            if (thisObj.getKind() == Kind.SYMBOL) {
                symbols.add(c.getState().readInternalValue(singleton(thisObj)));
            } else {
                is_maybe_typeerror = true;
            }
        }
        if (is_maybe_typeerror) {
            Exceptions.throwTypeError(c);
        }
        return Value.join(symbols);
    }
}
