package dk.brics.tajs.analysis.nativeobjects.concrete;

import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.analysis.Context;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.SpecialVars;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.util.None;
import dk.brics.tajs.util.Optional;
import dk.brics.tajs.util.OptionalObjectVisitor;
import dk.brics.tajs.util.Some;
import org.apache.log4j.Logger;

import java.util.List;

import static dk.brics.tajs.util.Collections.newList;

/**
 * A bridge between TAJS and concrete semantics.
 */
public class TAJSConcreteSemantics {

    private static final Logger log = Logger.getLogger(TAJSConcreteSemantics.class);

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
     * @param returnType   as the class of the expected concrete return type
     * @param state        as the state the call is performed in
     * @param call         as the call which is converted
     * @param c            as the solverinterface
     * @param <T>          as the expected concrete return type
     * @return Some concrete value if the call is guaranteed to be concrete and the call succeeds with the expected return type. Otherwire None.
     */
    public static <T extends ConcreteValue> Optional<T> convertTAJSCall(Value vThis, String functionName, int maxArguments, final Class<T> returnType, State state, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        if (Options.get().isConcreteNativeDisabled() || !Gamma.isConcreteValue(vThis, c)) {
            return None.<T>make();
        }

        // four cases for the number of arguments to read:
        final int numberOfArgumentsToRead;
        if (call.isUnknownNumberOfArgs()) {
            if (maxArguments < 0) {
                // unknown number + read all -> abort
                return None.<T>make();
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
        final List<ConcreteValue> arguments = newList();
        for (int argumentIndex = 0; argumentIndex < numberOfArgumentsToRead; argumentIndex++) {
            final Value argument;
            if (call.isUnknownNumberOfArgs()) {
                argument = call.getUnknownArg().joinUndef();
            } else {
                argument = NativeFunctions.readParameter(call, state, argumentIndex);
            }
            if (!Gamma.isConcreteValue(argument, c)) {
                return None.<T>make();
            }
            arguments.add(Gamma.toConcreteValue(argument, c));
        }
        return performCall(vThis, functionName, returnType, c, arguments);
    }

    private static <T extends ConcreteValue> Optional<T> performCall(Value vThis, String functionName, final Class<T> returnType, GenericSolver<State, Context, CallEdge<State>, IAnalysisMonitoring<State, Context, CallEdge<State>>, SpecialVars, Analysis>.SolverInterface c, List<ConcreteValue> arguments) {
        // perform the call with the arguments
        return ConcreteSemantics.get().apply(functionName, Gamma.toConcreteValue(vThis, c), arguments).apply(new OptionalObjectVisitor<Optional<T>, ConcreteValue>() {
            @SuppressWarnings("unchecked")
            @Override
            public Optional<T> visit(None<ConcreteValue> o) {
                return (Optional<T>) o;
            }

            @SuppressWarnings("unchecked")
            @Override
            public Optional<T> visit(Some<ConcreteValue> o) {
                if (!returnType.isAssignableFrom(o.get().getClass())) {
                    log.error(String.format("Unexpected return type from native call. Expected %s. Got %s", returnType.getSimpleName(), o.get().getClass().getSimpleName()));
                    return None.<T>make();
                }
                return (Optional<T>) o;
            }
        });
    }

    /**
     * Utility wrapper for convertTAJSCall, in case of non-concrete state or call-failure, the supplied default value is returned.
     *
     * @see #convertTAJSCall(Value, String, int, Class, State, FunctionCalls.CallInfo, dk.brics.tajs.solver.GenericSolver.SolverInterface)
     */
    public static <T extends PrimitiveConcreteValue> Value convertTAJSCall(Value vThis, String functionName, int maxArguments, final Class<T> returnType, State state, FunctionCalls.CallInfo call, Solver.SolverInterface c, final Value defaultValue) {
        Value result = convertTAJSCall(vThis, functionName, maxArguments, returnType, state, call, c).apply(
                new OptionalObjectVisitor<Value, T>() {
                    @Override
                    public Value visit(None<T> o) {
                        return defaultValue;
                    }

                    @Override
                    public Value visit(Some<T> o) {
                        return Alpha.toValue(o.get());
                    }
                }
        );
        return result;
    }


    /**
     * A version of {@link dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics#convertTAJSCall(dk.brics.tajs.lattice.Value, String, int, Class, dk.brics.tajs.analysis.State, dk.brics.tajs.analysis.FunctionCalls.CallInfo, dk.brics.tajs.solver.GenericSolver.SolverInterface)} with an explicit list of arguments.
     */
    public static <T extends PrimitiveConcreteValue> Optional<T> convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments, final Class<T> returnType, Solver.SolverInterface c) {
        final List<ConcreteValue> concreteArguments = newList();
        for (Value argument : arguments) {
            if (!Gamma.isConcreteValue(vThis, c)) {
                return None.make();
            } else {
                concreteArguments.add(Gamma.toConcreteValue(argument, c));
            }
        }
        return performCall(vThis, functionName, returnType, c, concreteArguments);
    }

}
