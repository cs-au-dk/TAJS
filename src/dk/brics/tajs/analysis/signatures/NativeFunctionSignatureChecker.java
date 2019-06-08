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

package dk.brics.tajs.analysis.signatures;

import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.HostAPIs;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.signatures.types.Coercion;
import dk.brics.tajs.analysis.signatures.types.Parameter;
import dk.brics.tajs.analysis.signatures.types.Requirement;
import dk.brics.tajs.analysis.signatures.types.Signature;
import dk.brics.tajs.analysis.signatures.types.ValueDescription;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.FreeVariablePartitioning;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Checks native function calls for maybe/definite type errors based on their signatures.
 * <p>
 * Also triggers coercions.
 * <p>
 * Also registers type error messages.
 */
public class NativeFunctionSignatureChecker {

    private static NativeFunctionSignatureChecker instance;

    /**
     * The supported signatures.
     */
    private final Map<HostObject, Signature> signatures;

    private NativeFunctionSignatureChecker() {
        this.signatures = new NativeFunctionSignatureBuilder().getSignatures();
    }

    public static NativeFunctionSignatureChecker get() {
        if (instance == null) {
            instance = new NativeFunctionSignatureChecker(); // TODO avoid singleton pattern. Initialize this during initial state building
        }
        return instance;
    }

    /**
     * Checks if a value satisfies a requirement.
     * Will propagate a type-error if the requirement is maybe not satisfied.
     *
     * @return true iff the requirement is definitely not satisfied
     */
    private static boolean isRequirementDefinetelyFailing(Value value, Requirement requirement, Optional<String> failureMessage, Solver.SolverInterface c) {
        if (requirement.maybeNotSatisfied(value)) {
            Exceptions.throwTypeError(c);
            if (failureMessage.isPresent()) {
                c.getMonitoring().addMessage(c.getNode(), Message.Severity.HIGH, failureMessage.get());
            }
        }
        return !requirement.maybeSatisfied(value);
    }

    /**
     * @see Signature#shouldStopPropagation(HostObject, FunctionCalls.CallInfo, GenericSolver.SolverInterface)
     */
    public boolean shouldStopPropagation(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        if (signatures.containsKey(hostobject)) {
            return signatures.get(hostobject).shouldStopPropagation(hostobject, call, c);
        }
        if (hostobject.getAPI() == HostAPIs.ECMASCRIPT_NATIVE && Options.get().isDebugOrTestEnabled()) {
            // expect all of ECMAScript to have nice signatures
            throw new AnalysisException("No signature defined for " + hostobject);
        }
        return false;
    }

    /**
     * Signature with a fixed number of (maybe optional) arguments.
     */
    public static class SimpleSignature implements Signature {

        private final ValueDescription base;

        private final List<Parameter> parameters;

        private final int minArguments;

        private final int maxArguments;

        private final boolean isConstructor;

        public SimpleSignature(boolean isConstructor, ValueDescription base, Parameter... parameters) {
            this(isConstructor, base, Arrays.asList(parameters));
        }

        public SimpleSignature(boolean isConstructor, ValueDescription base, List<Parameter> parameters) {
            this.isConstructor = isConstructor;
            this.base = base;
            this.parameters = parameters;
            maxArguments = parameters.size();
            int firstOptional = 0;
            for (; firstOptional < parameters.size(); firstOptional++) {
                if (!parameters.get(firstOptional).isMandatory()) {
                    break;
                }
            }
            this.minArguments = firstOptional;
            for (int lastMandatory = 0; lastMandatory < parameters.size(); lastMandatory++) {
                if (lastMandatory > firstOptional && parameters.get(lastMandatory).isMandatory()) {
                    throw new AnalysisException("Mandatory parameters after optional parameters: " + parameters);
                }
            }
        }

        @Override
        public boolean shouldStopPropagation(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
            if (call.isConstructorCall() && !isConstructor) { // ECMAScript behaviour: Constructors are the only ones that are allowed to be invoked as constructors
                Exceptions.throwTypeError(c);
                c.getMonitoring().addMessage(call.getSourceNode(), Message.Severity.HIGH, "TypeError, " + hostobject + " can not be as a constructor");
                return true;
            }
            warnAboutArgumentCounts(hostobject, call, c);
            List<Value> parameterValues = newList();
            for (int i = 0; i < parameters.size(); i++) {
                parameterValues.add(FunctionCalls.readParameter(call, c.getState(), i));
            }

            Set<Pair<Value, Pair<Requirement, Optional<String>>>> requirements = newSet();
            Set<Pair<Value, Coercion>> coercions = newSet();

            pairValueAndDescription(c.getState().readThis(), this.base, coercions, requirements, Optional.of("TypeError, native function " + hostobject + " called on invalid object kind"), c);
            for (int i = 0; i < parameters.size(); i++) {
                Parameter parameter = parameters.get(i);
                boolean absentButOptional = (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() <= i) && !parameter.isMandatory();
                if(absentButOptional) continue;  // Do not generate requirements for absent optional arguments
                pairValueAndDescription(parameterValues.get(i), parameter.getValueDescription(), coercions, requirements, Optional.empty(), c);
            }

            boolean definitelyFailingRequirement = requirements.stream()
                    .anyMatch(r -> isRequirementDefinetelyFailing(r.getFirst(), r.getSecond().getFirst(), r.getSecond().getSecond(), c));
            boolean delayedCoercion = coercions.stream()
                    .anyMatch(pair -> pair.getSecond().coerce(pair.getFirst(), c));

            boolean stopPropagation = definitelyFailingRequirement || delayedCoercion;
            return stopPropagation;
        }

        @Override
        public int getParametersLength() {
            return maxArguments;
        }

        private void pairValueAndDescription(Value value, ValueDescription description, Set<Pair<Value, Coercion>> coercions, Set<Pair<Value, Pair<Requirement, Optional<String>>>> requirements, Optional<String> failureMessage, Solver.SolverInterface c) {
            if (description.getRequirement().isPresent()) {
                requirements.add(Pair.make(value, Pair.make(description.getRequirement().get(), failureMessage)));
            }

            if (description.getCoercion().isPresent()) {
                coercions.add(Pair.make(value, description.getCoercion().get()));
            }
        }

        private void warnAboutArgumentCounts(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
            if (c.isScanning()) {
                c.getMonitoring().visitNativeFunctionCall(call.getSourceNode(), hostobject, call.isUnknownNumberOfArgs(), call.isUnknownNumberOfArgs() ? -1 : call.getNumberOfArgs(), minArguments, maxArguments);
            }
        }
    }

    /**
     * Generalization of other signatures. Capable of picking the appropriate signatures based on the number of arguments at the call site.
     */
    static class ArityOverloadedSignature implements Signature {

        private final Map<Pair<Integer, Integer>, Signature> signatures;

        public ArityOverloadedSignature(SimpleSignature... simpleSignatures) {
            signatures = newMap();
            Arrays.asList(simpleSignatures).forEach(sig -> signatures.put(Pair.make(sig.minArguments, sig.maxArguments), sig));

            if (signatures.size() != simpleSignatures.length) {
                throw new AnalysisException("Ambiguous overloading: " + Arrays.toString(simpleSignatures));
            }
        }

        @Override
        public boolean shouldStopPropagation(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
            Collection<Signature> targets = pickTargetsByArity(call);
            return targets.stream().anyMatch(s -> s.shouldStopPropagation(hostobject, call, c));
        }

        @Override
        public int getParametersLength() {
            return signatures.keySet().stream().mapToInt(Pair::getSecond).max().getAsInt();
        }

        private Collection<Signature> pickTargetsByArity(FunctionCalls.CallInfo call) {
            Collection<Signature> targets;
            if (call.isUnknownNumberOfArgs()) {
                targets = signatures.values();
            } else {
                int numberOfArgs = call.getNumberOfArgs();
                targets = signatures.entrySet().stream()
                        .filter(e -> e.getKey().getFirst() <= numberOfArgs && numberOfArgs <= e.getKey().getSecond())
                        .map(Map.Entry::getValue)
                        .collect(Collectors.toSet());
                if (targets.isEmpty()) {
                    targets = singleton(signatures.entrySet().stream().max((e1, e2) -> {
                        Integer max1 = e1.getKey().getSecond();
                        Integer max2 = e2.getKey().getSecond();
                        return max1.compareTo(max2);
                    }).get().getValue());
                }
            }
            return targets;
        }
    }

    /**
     * Variadic signature with a {@link SimpleSignature} prefix, and a last argument which can be repeated unboundedly.
     */
    public static class VarSignature implements Signature {

        private final Parameter varParameter;

        private final SimpleSignature simpleSignature;

        private final int fixedParameterCount;

        public VarSignature(boolean isConstructor, ValueDescription base, Parameter... parameters) {
            this(isConstructor, base, Arrays.asList(parameters));
        }

        public VarSignature(boolean isConstructor, ValueDescription base, List<Parameter> parameters) {
            List<Parameter> fixedParameters = parameters.subList(0, parameters.size() - 1);
            this.fixedParameterCount = fixedParameters.size();
            this.simpleSignature = new SimpleSignature(isConstructor, base, fixedParameters);
            this.varParameter = parameters.get(parameters.size() - 1);
            if (this.varParameter.isMandatory()) {
                throw new AnalysisException("The last VarSig parameter should not be mandatory.");
            }
        }

        @Override
        public boolean shouldStopPropagation(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
            boolean shouldStopAccordingToSimpleSig = shouldStopAccordingToSimpleSig(hostobject, call, c);
            ValueDescription description = varParameter.getValueDescription();
            Optional<Coercion> coercion = description.getCoercion();
            Optional<Requirement> requirement = description.getRequirement();
            boolean shouldStopAccordingToCoercion = false;
            boolean shouldStopAccordingToRequirement = false;
            if (call.isUnknownNumberOfArgs()) {
                Value unknownArg = FunctionCalls.readUnknownParameter(call);
                if (coercion.isPresent()) {
                    shouldStopAccordingToCoercion = coercion.get().coerce(unknownArg, c);
                }
                if (requirement.isPresent()) {
                    shouldStopAccordingToRequirement = isRequirementDefinetelyFailing(unknownArg, requirement.get(), Optional.empty(), c);
                }
            } else {
                for (int i = fixedParameterCount; i < call.getNumberOfArgs(); i++) {
                    Value arg = FunctionCalls.readParameter(call, c.getState(), i);
                    if (arg.isNone()) {
                        return true;
                    }
                    if (coercion.isPresent()) {
                        shouldStopAccordingToCoercion |= coercion.get().coerce(arg, c);
                    }
                    if (requirement.isPresent()) {
                        shouldStopAccordingToRequirement |= isRequirementDefinetelyFailing(arg, requirement.get(), Optional.empty(), c);
                    }
                }
            }
            boolean shouldStopAccordingToVarParameter = shouldStopAccordingToCoercion || shouldStopAccordingToRequirement;
            return shouldStopAccordingToSimpleSig || shouldStopAccordingToVarParameter;
        }

        @Override
        public int getParametersLength() {
            return fixedParameterCount;
        }

        private boolean shouldStopAccordingToSimpleSig(HostObject hostobject, final FunctionCalls.CallInfo call, Solver.SolverInterface c) {
            return simpleSignature.shouldStopPropagation(hostobject, new FunctionCalls.CallInfo() {
                @Override
                public AbstractNode getSourceNode() {
                    return call.getSourceNode();
                }

                @Override
                public AbstractNode getJSSourceNode() {
                    return call.getJSSourceNode();
                }

                @Override
                public boolean isConstructorCall() {
                    return call.isConstructorCall();
                }

                @Override
                public Value getFunctionValue() {
                    return call.getFunctionValue();
                }

                @Override
                public Value getThis() {
                    return c.getState().readThis();
                }

                @Override
                public Value getArg(int i) {
                    return call.getArg(i);
                }

                @Override
                public int getNumberOfArgs() {
                    return Math.min(call.getNumberOfArgs(), fixedParameterCount); // hide the varparams
                }

                @Override
                public Value getUnknownArg() {
                    return call.getUnknownArg();
                }

                @Override
                public boolean isUnknownNumberOfArgs() {
                    return call.isUnknownNumberOfArgs();
                }

                @Override
                public int getResultRegister() {
                    return call.getResultRegister();
                }

                @Override
                public ExecutionContext getExecutionContext() {
                    return call.getExecutionContext();
                }

                @Override
                public boolean assumeFunction() {
                    return false;
                }

                @Override
                public FreeVariablePartitioning getFreeVariablePartitioning() {
                    return null;
                }
            }, c);
        }
    }
}
