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

import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.util.AnalysisLimitationException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

public class TAJSSplitConcreteSemantics { // XXX: splitting...

    private static final Bridge concreteSemantics = new TAJSSingleConcreteSemanticsBridge();

    private TAJSSplitConcreteSemantics() {}

    public static <T> Set<List<T>> makePowerSetOfListsFromListsOfSets(List<Set<T>> sources) {
        return makePowerSetOfListsFromListsOfSets(sources, singleton(Collections.emptyList()));
    }

    private static <T> Set<List<T>> makePowerSetOfListsFromListsOfSets(List<Set<T>> sources, Set<List<T>> bases) {
        if (sources.isEmpty()) {
            return bases;
        }
        sources = new LinkedList<>(sources);
        Set<T> headSources = sources.remove(0);
        List<Set<T>> tailSources = sources;
        Set<List<T>> extendedBases = newSet();
        for (T head : headSources) {
            // extend all entries with the elements
            for (List<T> base : bases) {
                List<T> extended = newList(base);
                extended.add(head);
                extendedBases.add(extended);
            }
        }
        return makePowerSetOfListsFromListsOfSets(tailSources, extendedBases);
    }

    /**
     * Converts a call from inside TAJS to a concrete call, if possible.
     * <p>
     * Can always be called. Will return None if something goes wrong or is non-concrete.
     * <p>
     * It is the responsibility of the caller to have *registered* reads and coercions of the arguments, but the arguments should not be mutated.
     * <p>
     * If one of the read parameters is a boxed primitive, then a coercion is done to its primitive counterpart (once more).
     *
     * @param <T>          as the expected concrete return type
     * @param vThis        as the value to use as the 'this' object. Some functions do not use the this object, a concrete (undefined) value should be passed in that case.
     * @param functionName as the full name of the function to call, e.g. String.prototype.slice
     * @param maxArguments as the maximal number of arguments to read from the argumentslist
     * @param call         as the call which is converted
     * @param c            as the solverinterface
     * @return Some concrete value if the call is guaranteed to be concrete and the call succeeds with the expected return type. Otherwire None.
     */
    public static <T extends ConcreteValue> Set<InvocationResult<T>> convertTAJSCall(Value vThis, String functionName, int maxArguments, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        if (Options.get().isConcreteNativeDisabled()) {
            return singleton(InvocationResult.makeNonConcrete());
        }

        // four cases for the number of arguments to read:
        final int numberOfArgumentsToRead;
        if (call.isUnknownNumberOfArgs()) {
            if (maxArguments < 0) {
                // unknown number + read all -> abort
                return singleton(InvocationResult.makeNonConcrete());
            }
            // unknown number + read some -> read some
            numberOfArgumentsToRead = maxArguments;
        } else {
            if (maxArguments < 0) {
                // known number + read all -> read known
                numberOfArgumentsToRead = call.getNumberOfArgs();
            } else {
                // known number + read some -> read the minimum of the two values (native functions applies semantics to the number of arguments given, not just undefined/not-undefined!!)
                numberOfArgumentsToRead = Math.min(maxArguments, call.getNumberOfArgs());
            }
        }

        // read the arguments
        final List<Value> arguments = newList();
        for (int argumentIndex = 0; argumentIndex < numberOfArgumentsToRead; argumentIndex++) {
            final Value argument;
            if (call.isUnknownNumberOfArgs()) {
                argument = NativeFunctions.readUnknownParameter(call);
            } else {
                argument = NativeFunctions.readParameter(call, c.getState(), argumentIndex);
            }
            arguments.add(argument);
        }
        return convertTAJSCallExplicit(vThis, functionName, arguments, c);
    }

    /**
     * Implements Function.prototype.toString
     */
    public static Value convertFunctionToString(ObjectLabel functionLabel) {
        if (functionLabel.isHostObject()) {
            String enumString = functionLabel.getHostObject().toString();
            String functionName = enumString.substring(enumString.lastIndexOf(".") + 1) /* abusing convention of host objects names pointing to their definition */;
            String source = String.format("function %s() { [native code] }", functionName);
            return Value.makeStr(source);
        }
        // In google chrome, the toString of a function is ignoring inline comments in certain places:
        // ```
        // $ (function /**/foo/**/(/**/bar/**/)/**/{/**/baz;/**/}).toString()
        // > "function foo(/**/bar/**/)/**/{/**/baz;/**/}"
        // ```
        // So we can not simply return the source of the function object
        // (but nashorn does return the entire source of the function declaration, so the call to concrete semantics is actually moot currently)
        String toStringCallCode = String.format("(%s).toString()", functionLabel.getFunction().getSource());
        InvocationResult<ConcreteString> result = NashornConcreteSemantics.get().eval(toStringCallCode);
        if (result.kind != InvocationResult.Kind.VALUE) {
            throw new AnalysisLimitationException.AnalysisModelLimitationException("Implementation only supports value-results here: supply a valid, non-crashing program!");
        }
        return Alpha.toValue(result.getValue());
    }

    /**
     * Attempts to perform a concrete call.
     * <p>
     * Can always be called. Will return None if something goes wrong or is non-concrete.
     */
    public static <T extends ConcreteValue> Set<InvocationResult<T>> convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments, Solver.SolverInterface c) {
        vThis = UnknownValueResolver.getRealValue(vThis, c.getState());
        arguments = arguments.stream().map(arg -> UnknownValueResolver.getRealValue(arg, c.getState())).collect(Collectors.toList());
        return concreteSemantics.convertTAJSCallExplicit(vThis, functionName, arguments, c);
    }

    /**
     * Utility wrapper for {@link #convertTAJSCall(Value, String, int, FunctionCalls.CallInfo, GenericSolver.SolverInterface)}, in case of non-concrete state or call-failure, the supplied default value is returned.
     */
    public static <T extends PrimitiveConcreteValue> Set<Value> convertTAJSCall(Value vThis, String functionName, int maxArguments, FunctionCalls.CallInfo call, Solver.SolverInterface c, Value defaultValue) {
        Set<InvocationResult<T>> concreteResult = convertTAJSCall(vThis, functionName, maxArguments, call, c);
        return handleResult(concreteResult, defaultValue, c);
    }

    private static <T extends PrimitiveConcreteValue> Set<Value> handleResult(Set<InvocationResult<T>> concreteResult, Value defaultValue, Solver.SolverInterface c) {
        Map<InvocationResult.Kind, List<InvocationResult<T>>> groupedResults = concreteResult.stream().collect(Collectors.groupingBy(r -> r.kind));

        boolean hasExceptions = !groupedResults.getOrDefault(InvocationResult.Kind.EXCEPTION, newList()).isEmpty();
        if (hasExceptions) {
            Exceptions.throwTypeError(c); // XXX assuming type-errors are the right thing to throw
        }

        boolean hasNonConcrete = !groupedResults.getOrDefault(InvocationResult.Kind.NON_CONCRETE, newList()).isEmpty();
        if (hasNonConcrete) {
            return singleton(defaultValue);
        }

        Stream<T> values = groupedResults.getOrDefault(InvocationResult.Kind.VALUE, newList()).stream().map(InvocationResult::getValue);
        return values.map(Alpha::toValue).collect(Collectors.toSet());
    }

    /**
     * Utility wrapper for {@link #convertTAJSCallExplicit(Value, String, List, GenericSolver.SolverInterface)}, in case of non-concrete state or call-failure, the supplied default value is returned.
     */
    public static <T extends PrimitiveConcreteValue> Set<Value> convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments, Solver.SolverInterface c, Value defaultValue) {
        Set<InvocationResult<T>> concreteResult = convertTAJSCallExplicit(vThis, functionName, arguments, c);
        return handleResult(concreteResult, defaultValue, c);
    }

    public static Value eval(String code) {
        InvocationResult<PrimitiveConcreteValue> result = NashornConcreteSemantics.get().eval(code);
        if (result.kind != InvocationResult.Kind.VALUE) {
            throw new AnalysisLimitationException.AnalysisModelLimitationException("Implementation only supports value-results here: supply a valid, non-crashing program!");
        }
        return Alpha.toValue(result.getValue());
    }

    private interface Bridge {

        <T extends ConcreteValue> Set<InvocationResult<T>> convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments, Solver.SolverInterface c);
    }

    private static class TAJSSingleConcreteSemanticsBridge implements Bridge {

        @Override
        public <T extends ConcreteValue> Set<InvocationResult<T>> convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments, Solver.SolverInterface c) {
            final List<ConcreteValue> concreteArguments = newList();
            if (!SingleGamma.isConcreteValue(vThis, c)) {
                return singleton(InvocationResult.makeNonConcrete());
            }
            ConcreteValue concreteReceiver = SingleGamma.toConcreteValue(vThis, c);
            for (Value argument : arguments) {
                if (!SingleGamma.isConcreteValue(argument, c)) {
                    return singleton(InvocationResult.makeNonConcrete());
                } else {
                    concreteArguments.add(SingleGamma.toConcreteValue(argument, c));
                }
            }
            InvocationResult<T> result = NashornConcreteSemantics.get().apply(functionName, concreteReceiver, concreteArguments);
            return singleton(result);
        }
    }
}
