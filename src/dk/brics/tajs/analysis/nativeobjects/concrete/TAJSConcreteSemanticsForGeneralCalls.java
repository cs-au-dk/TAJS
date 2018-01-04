/*
 * Copyright 2009-2018 Aarhus University
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
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.nativeobjects.concrete.NativeResult.Kind;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collectors;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Helper class for {@link TAJSConcreteSemantics} which does the conversions between an abstract call and a concrete call.
 */
class TAJSConcreteSemanticsForGeneralCalls {

    private final DomainMapperForGeneralCalls converter;

    private final Solver.SolverInterface c;

    public TAJSConcreteSemanticsForGeneralCalls(Solver.SolverInterface c) {
        this.c = c;
        this.converter = new DeterminateMapper();
    }

    /**
     * Applies side effects to objects by based on how their shape when the concrete semantics have been applied.
     */
    void makeSideEffects(Value base, List<Value> arguments, Optional<ConcreteApplyMapping> mapped) {
        if (!mapped.isPresent()) {
            return;
        }
        makeSideEffects(base, mapped.get().getBase());
        for (int i = 0; i < arguments.size(); i++) {
            makeSideEffects(arguments.get(i), mapped.get().getArguments().get(i));
        }
    }

    /**
     * Applies side effects to objects by based on how their shape when the concrete semantics have been applied.
     */
    void makeSideEffects(Value abstractBefore, ConcreteValue concreteAfter) {
        concreteAfter.accept(new ConcreteValueVisitor<Void>() {
            @Override
            public Void visit(ConcreteNumber v) {
                return null;
            }

            @Override
            public Void visit(ConcreteString v) {
                return null;
            }

            @Override
            public Void visit(ConcreteArray v) {
                return null; // TODO support side effects on arrays
            }

            @Override
            public Void visit(ConcreteUndefined v) {
                return null;
            }

            @Override
            public Void visit(ConcreteRegularExpression v) {
                v.getLastIndex();
                ObjectLabel regexpLabel = abstractBefore.getObjectLabels().iterator().next(); // by construction of the concrete value, this is a single regexp label
                c.getAnalysis().getPropVarOperations().writeProperty(regexpLabel, "lastIndex", Alpha.toValue(v.getLastIndex(), c).setAttributes(true, true, false));
                return null;
            }

            @Override
            public Void visit(ConcreteNull v) {
                return null;
            }

            @Override
            public Void visit(ConcreteNullOrUndefined v) {
                return null;
            }

            @Override
            public Void visit(ConcreteBoolean v) {
                return null;
            }
        });
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
     * @param vThis        as the value to use as the 'this' object. Some functions do not use the this object, a concrete (undefined) value should be passed in that case.
     * @param functionName as the full name of the function to call, e.g. String.prototype.slice
     * @param maxArguments as the maximal number of arguments to read from the argumentslist
     * @param call         as the call which is converted
     * @return Some concrete value if the call is guaranteed to be concrete and the call succeeds with the expected return type. Otherwire None.
     */
    public Set<NativeResult<ConcreteValue>> convertTAJSCall(Value vThis, String functionName, int maxArguments, CallInfo call) { // TODO: always get vThis from c.getState?
        if (Options.get().isConcreteNativeDisabled()) {
            return singleton(NativeResult.makeNonConcrete());
        }

        // four cases for the number of arguments to read:
        final int numberOfArgumentsToRead;
        if (call.isUnknownNumberOfArgs()) {
            if (maxArguments < 0) {
                // unknown number + read all -> abort
                return singleton(NativeResult.makeNonConcrete());
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
                argument = FunctionCalls.readUnknownParameter(call);
            } else {
                argument = FunctionCalls.readParameter(call, c.getState(), argumentIndex);
            }
            arguments.add(argument);
        }
        return convertTAJSCallExplicit(vThis, functionName, arguments);
    }

    /**
     * Attempts to perform a native call.
     *
     * @param vThis        the receiver of the call
     * @param functionName the fully qualified name of the native function
     * @param arguments    the list of arguments
     * @return results
     */
    public Set<NativeResult<ConcreteValue>> convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments) {
        vThis = UnknownValueResolver.getRealValue(vThis, c.getState());
        arguments = arguments.stream().map(arg -> UnknownValueResolver.getRealValue(arg, c.getState())).collect(Collectors.toList());
        Set<NativeResult<ConcreteValue>> result = converter.convertTAJSCallExplicit(vThis, functionName, arguments);
        return result;
    }

    /**
     * Utility wrapper for {@link #convertTAJSCall(Value, String, int, dk.brics.tajs.analysis.FunctionCalls.CallInfo)}, in case of non-concrete state or call-failure, the supplied default behavior produces the return value.
     */
    public Set<Value> convertTAJSCall(Value vThis, String functionName, int maxArguments, CallInfo call, Supplier<Value> defaultBehavior) {
        Set<NativeResult<ConcreteValue>> concreteResult = convertTAJSCall(vThis, functionName, maxArguments, call);
        return handleResult(concreteResult, defaultBehavior);
    }

    /**
     * Converts a result from the native semantics, throwing JavaScript exceptions if needed, might fall back to using a supplied default behavior.
     */
    private Set<Value> handleResult(Set<NativeResult<ConcreteValue>> concreteResult, Supplier<Value> defaultBehavior) {
        Map<Kind, List<NativeResult<ConcreteValue>>> groupedResults = concreteResult.stream().collect(Collectors.groupingBy(r -> r.kind));

        boolean hasExceptions = !groupedResults.getOrDefault(Kind.EXCEPTION, newList()).isEmpty();
        if (hasExceptions) {
            Exceptions.throwTypeError(c); // TODO: assuming type-errors are the right thing to throw
        }

        boolean hasNonConcrete = !groupedResults.getOrDefault(Kind.NON_CONCRETE, newList()).isEmpty();
        if (hasNonConcrete) {
            return singleton(defaultBehavior.get());
        }

        Stream<ConcreteValue> values = groupedResults.getOrDefault(Kind.VALUE, newList()).stream().map(NativeResult::getValue);
        return values.map(v -> Alpha.toValue(v, c)).collect(Collectors.toSet());
    }

    /**
     * Utility wrapper for {@link #convertTAJSCallExplicit(Value, String, List)}, in case of non-concrete state or call-failure, the supplied default value is returned.
     */
    public Set<Value> convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments, Supplier<Value> defaultBehavior) {
        Set<NativeResult<ConcreteValue>> concreteResult = convertTAJSCallExplicit(vThis, functionName, arguments);
        return handleResult(concreteResult, defaultBehavior);
    }

    /**
     * Maps between an abstract call and a concrete call.
     */
    private interface DomainMapperForGeneralCalls {

        /**
         * Invokes a native function on a receiver with some arguments.
         */
        Set<NativeResult<ConcreteValue>> convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments);

        /**
         * Determines if a value is considered precise enough to be used as a concrete values by this mapper.
         */
        boolean isConcrete(Value value);
    }

    /**
     * Supports the trivial mapping of determinate abstract values to their concrete counterparts.
     */
    private class DeterminateMapper implements DomainMapperForGeneralCalls {

        @Override
        public Set<NativeResult<ConcreteValue>> convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments) {
            final List<ConcreteValue> concreteArguments = newList();
            if (!SingleGamma.isConcreteValue(vThis, c)) {
                return singleton(NativeResult.makeNonConcrete());
            }
            ConcreteValue concreteReceiver = SingleGamma.toConcreteValue(vThis, c);
            for (Value argument : arguments) {
                if (!SingleGamma.isConcreteValue(argument, c)) {
                    return singleton(NativeResult.makeNonConcrete());
                } else {
                    concreteArguments.add(SingleGamma.toConcreteValue(argument, c));
                }
            }
            MappedNativeResult<ConcreteValue> result = TAJSConcreteSemantics.getNative().apply(functionName, concreteReceiver, concreteArguments);

            makeSideEffects(vThis, arguments, result.getMapped());

            return singleton(result.getResult());
        }

        @Override
        public boolean isConcrete(Value value) {
            return SingleGamma.isConcreteValue(value, c);
        }
    }
}
