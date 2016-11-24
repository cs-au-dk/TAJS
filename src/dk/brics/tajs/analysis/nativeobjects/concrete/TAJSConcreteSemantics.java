/*
 * Copyright 2009-2016 Aarhus University
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

package dk.brics.tajs.analysis.nativeobjects.concrete;

import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.util.AnalysisException;

import java.util.List;
import java.util.Set;

/**
 * A bridge between TAJS and a concrete semantics.
 *
 * @see TAJSSplitConcreteSemantics for more general and precise version that returns sets of values
 */
public class TAJSConcreteSemantics {

    private TAJSConcreteSemantics() {}

    private static <T> InvocationResult<T> pick(Set<InvocationResult<T>> obj) {
        if (obj.size() != 1) {
            throw new AnalysisException("Not producing exactly one result: " + obj);
        }
        return obj.iterator().next();
    }

    public static <T extends PrimitiveConcreteValue> Value convertTAJSCall(Value vThis, String functionName, int maxArguments, FunctionCalls.CallInfo call, GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, Analysis>.SolverInterface c, Value defaultValue) {
        Set<Value> splitResult = TAJSSplitConcreteSemantics.convertTAJSCall(vThis, functionName, maxArguments, call, c, defaultValue);
        return Value.join(splitResult);
    }

    public static Value convertFunctionToString(ObjectLabel functionLabel) {
        return TAJSSplitConcreteSemantics.convertFunctionToString(functionLabel);
    }

    public static <T extends PrimitiveConcreteValue> Value convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments, GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, Analysis>.SolverInterface c, Value defaultValue) {
        Set<Value> splitResult = TAJSSplitConcreteSemantics.convertTAJSCallExplicit(vThis, functionName, arguments, c, defaultValue);
        return Value.join(splitResult);
    }

    public static <T extends ConcreteValue> InvocationResult<T> convertTAJSCall(Value vThis, String functionName, int maxArguments, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        Set<InvocationResult<T>> splitResult = TAJSSplitConcreteSemantics.convertTAJSCall(vThis, functionName, maxArguments, call, c);
        return pick(splitResult);
    }

    public static <T extends ConcreteValue> InvocationResult<T> convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments, Solver.SolverInterface c) {
        Set<InvocationResult<T>> splitResult = TAJSSplitConcreteSemantics.convertTAJSCallExplicit(vThis, functionName, arguments, c);
        return pick(splitResult);
    }
}
