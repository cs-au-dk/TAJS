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

package dk.brics.tajs.analysis;

import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.BOOLEAN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.BOOLEAN_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.BOOLEAN_VALUEOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ERROR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ERROR_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.FUNCTION;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.JSON_PARSE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.JSON_STRINGIFY;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_ISFINITE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_TOEXPONENTIAL;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_TOFIXED;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_TOLOCALESTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_TOPRECISION;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_VALUEOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.REGEXP;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.REGEXP_COMPILE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.REGEXP_EXEC;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.REGEXP_TEST;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.REGEXP_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_CHARAT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_CHARCODEAT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_CONCAT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_ENDSWITH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_FROMCHARCODE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_INDEXOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_LASTINDEXOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_LOCALECOMPARE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_MATCH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_REPLACE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_SEARCH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_SLICE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_SPLIT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_STARTSWITH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_SUBSTR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_SUBSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TOLOCALELOWERCASE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TOLOCALEUPPERCASE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TOLOWERCASE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TOUPPERCASE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TRIM;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_VALUEOF;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

public class NativeFunctionSignatureChecker { // TODO: review + javadoc + inspect remaining calls to expectParameter

    // TODO: replace checks with coerced signature-cases, and "precise" invocations of real transfers (as lambdas even?)

    private static NativeFunctionSignatureChecker instance;

    private final Map<HostObject, Signature> signatures;

    private NativeFunctionSignatureChecker() {
        this.signatures = makeSignatures();
    }

    public static NativeFunctionSignatureChecker get() {
        if (instance == null) {
            instance = new NativeFunctionSignatureChecker();
        }
        return instance;
    }

    private static void addStaticSig(Map<HostObject, Signature> signatures, ECMAScriptObjects hostObject, boolean isConstructor, Parameter... parameters) {
        ValueDescription none = new ValueDescription(Optional.empty(), Optional.empty());
        addSig(signatures, hostObject, new SimpleSignature(isConstructor, none, parameters));
    }

    private static void addStaticVarSig(Map<HostObject, Signature> signatures, ECMAScriptObjects hostObject, boolean isConstructor, Parameter... parameters) {
        ValueDescription none = new ValueDescription(Optional.empty(), Optional.empty());
        addSig(signatures, hostObject, new VarSignature(isConstructor, none, parameters));
    }

    private static void addVarSig(Map<HostObject, Signature> signatures, ECMAScriptObjects hostObject, boolean isConstructor, ValueDescription base, Parameter... parameters) {
        addSig(signatures, hostObject, new VarSignature(isConstructor, base, parameters));
    }

    private static void addSig(Map<HostObject, Signature> signatures, ECMAScriptObjects hostObject, ValueDescription base, Parameter... parameters) {
        addSig(signatures, hostObject, new SimpleSignature(false, base, parameters));
    }

    private static void addSig(Map<HostObject, Signature> signatures, ECMAScriptObjects hostObject, Signature signature) {
        if (signatures.containsKey(hostObject)) {
            throw new AnalysisException("Attempting to redefine signature: " + hostObject);
        }
        signatures.put(hostObject, signature);
    }

    private static boolean isRequirementDefinetelyFailing(Value value, Requirement requirement, Solver.SolverInterface c) {
        if (requirement.maybeNotSatisfied(value))
            Exceptions.throwTypeError(c);
        return !requirement.maybeSatisfied(value);
    }

    /**
     * Checks native function calls for maybe/definite type errors based on their signatures.
     * <p>
     * Also performs required coercions.
     * <p>
     * Also registers type errors.
     *
     * @return true iff the native function should be evaluated.
     */
    public boolean shouldStopPropagation(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        if (signatures.containsKey(hostobject)) {
            return signatures.get(hostobject).shouldStopPropagation(hostobject, call, c);
        }
        return false;
    }

    private Map<HostObject, Signature> makeSignatures() {
        Map<HostObject, Signature> signatures = newMap();
        Requirement isNotNullUndefined = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                return !v.restrictToNotNullNotUndef().isNone();
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.isNullOrUndef();
            }
        };
        Requirement isString = Requirements.isString;
        Requirement isNumber = Requirements.isNumber;
        Requirement isRegExp = Requirements.isRegExp;
        Requirement isBoolean = Requirements.isBoolean;

        ValueDescription none = new ValueDescription(Optional.empty(), Optional.empty());

        // TODO coercions are done based on ObjectLabel kinds, but prototypes would be more correct to use?

        // mandatory parameters
        Must mpNumber = new Must(new ValueDescription(Optional.empty(), Optional.of(DelayedCoercions::number)));
        Must mpBoolean = new Must(new ValueDescription(Optional.empty(), Optional.of(DelayedCoercions::bool)));
        Must mpInteger = new Must(new ValueDescription(Optional.empty(), Optional.of(DelayedCoercions::integer)));
        Must mpString = new Must(new ValueDescription(Optional.empty(), Optional.of(DelayedCoercions::string)));
        Must mpRegExp = new Must(new ValueDescription(Optional.empty(), Optional.of(DelayedCoercions::regExp)));
        Must mpStringIfNotRegExp = new Must(new ValueDescription(Optional.empty(), Optional.of(DelayedCoercions::stringIfNotRegExp)));
        Must mpStringIfNotFunction = new Must(new ValueDescription(Optional.empty(), Optional.of(DelayedCoercions::stringIfNotFunction)));
        Must mpStringIfNotRegExpOrUndefined = new Must(new ValueDescription(Optional.empty(), Optional.of(DelayedCoercions::stringIfNotRegExpOrUndefined)));
        Must mpStringIfNotUndefined = new Must(new ValueDescription(Optional.empty(), Optional.of(DelayedCoercions::stringIfNotUndefined)));
        Must mpObjectIfNotUndefined = new Must(new ValueDescription(Optional.empty(), Optional.of(DelayedCoercions::objectIfNotNullUndefined)));
        Must mpNumberIfNotString = new Must(new ValueDescription(Optional.empty(), Optional.of(DelayedCoercions::numberIfNotString)));
        Must mpDontCare = new Must(new ValueDescription(Optional.empty(), Optional.empty()));

        // optional parameters
        Maybe pNumber = mpNumber.toMaybe();
        Maybe pBoolean = mpBoolean.toMaybe();
        Maybe pInteger = mpInteger.toMaybe();
        Maybe pString = mpString.toMaybe();
        Maybe pStringIfNotRegExp = mpStringIfNotRegExp.toMaybe();
        Maybe pStringIfNotRegExpOrUndefined = mpStringIfNotRegExpOrUndefined.toMaybe();
        Maybe pStringIfNotUndefined = mpStringIfNotUndefined.toMaybe();
        Maybe pObjectIfNotUndefined = mpObjectIfNotUndefined.toMaybe();

        // receivers
        ValueDescription rString = new ValueDescription(Optional.of(isString), Optional.empty());
        ValueDescription rNumber = new ValueDescription(Optional.of(isNumber), Optional.empty());
        ValueDescription rRegExp = new ValueDescription(Optional.of(isRegExp), Optional.empty());
        ValueDescription rBoolean = new ValueDescription(Optional.of(isBoolean), Optional.empty());
        ValueDescription rCoerceString = new ValueDescription(Optional.of(isNotNullUndefined), Optional.of(DelayedCoercions::string));

        // CONSTRUCTORS
        addConstructorSig(signatures, STRING, pString);
        addConstructorSig(signatures, NUMBER, pNumber);
        addConstructorSig(signatures, REGEXP, pStringIfNotRegExpOrUndefined, pStringIfNotUndefined);
        addConstructorVarSig(signatures, ARRAY, new Must(none).toMaybe());
        addConstructorSig(signatures, OBJECT, pObjectIfNotUndefined);
        addConstructorVarSig(signatures, FUNCTION, pString);
        addConstructorSig(signatures, BOOLEAN, pBoolean);
        addSig(signatures, DATE,
                new ArityOverloadedSignature(
                        new SimpleSignature(true, none, mpNumber /* year */, mpNumber /* month */, pNumber /* date */, pNumber /* hours */, pNumber /* minutes */, pNumber /* seconds */, pNumber /* milliseconds*/),
                        new SimpleSignature(true, none, mpNumberIfNotString),
                        new SimpleSignature(true, none)
                )
        );
        addConstructorSig(signatures, ERROR, pString);

        // STRING FUNCTIONS
        addStaticVarSig(signatures, STRING_FROMCHARCODE, false, pNumber);
        addSig(signatures, STRING_TOSTRING, rString);
        addSig(signatures, STRING_VALUEOF, rString);
        addSig(signatures, STRING_CHARAT, rCoerceString, mpNumber);
        addSig(signatures, STRING_CHARCODEAT, rCoerceString, mpNumber);
        addVarSig(signatures, STRING_CONCAT, false, rCoerceString, pString);
        addSig(signatures, STRING_INDEXOF, rCoerceString, mpString, pNumber);
        addSig(signatures, STRING_LASTINDEXOF, rCoerceString, mpString, pNumber);
        addSig(signatures, STRING_LOCALECOMPARE, rCoerceString, mpString);
        addSig(signatures, STRING_MATCH, rCoerceString, mpRegExp);
        addSig(signatures, STRING_REPLACE, rCoerceString, mpStringIfNotRegExp, mpStringIfNotFunction);
        addSig(signatures, STRING_SEARCH, rCoerceString, mpRegExp);
        addSig(signatures, STRING_SLICE, rCoerceString, pNumber, pNumber);
        addSig(signatures, STRING_SUBSTRING, rCoerceString, pNumber, pNumber);
        addSig(signatures, STRING_SUBSTR, rCoerceString, pNumber, pNumber);
        addSig(signatures, STRING_SPLIT, rCoerceString, pStringIfNotRegExp, pNumber);
        addSig(signatures, STRING_TOLOWERCASE, rCoerceString);
        addSig(signatures, STRING_TOLOCALELOWERCASE, rCoerceString);
        addSig(signatures, STRING_TOUPPERCASE, rCoerceString);
        addSig(signatures, STRING_TOLOCALEUPPERCASE, rCoerceString);
        addSig(signatures, STRING_TRIM, rCoerceString);
        addSig(signatures, STRING_STARTSWITH, rCoerceString, mpStringIfNotRegExp, pInteger);
        addSig(signatures, STRING_ENDSWITH, rCoerceString, mpStringIfNotRegExp, pInteger);

        // NUMBER FUNCTIONS
        addStaticSig(signatures, NUMBER_ISFINITE, false, mpDontCare);
        addSig(signatures, NUMBER_TOFIXED, rNumber, pInteger);
        addSig(signatures, NUMBER_TOEXPONENTIAL, rNumber, pInteger);
        addSig(signatures, NUMBER_TOPRECISION, rNumber, pInteger);
        addSig(signatures, NUMBER_TOSTRING, rNumber, pInteger);
        addSig(signatures, NUMBER_TOLOCALESTRING, rNumber, pInteger);
        addSig(signatures, NUMBER_VALUEOF, rNumber);

        // REGEXP FUNCTIONS
        addSig(signatures, REGEXP_COMPILE, rRegExp, pString /* close enough for very deprecated API */, pStringIfNotUndefined);
        addSig(signatures, REGEXP_EXEC, rRegExp, pString);
        addSig(signatures, REGEXP_TEST, rRegExp, pString);
        addSig(signatures, REGEXP_TOSTRING, rRegExp);

        // ARRAY FUNCTIONS

        // OBJECT FUNCTIONS

        // FUNCTION FUNCTIONS

        // BOOLEAN FUNCTIONS
        addSig(signatures, BOOLEAN_TOSTRING, rBoolean);
        addSig(signatures, BOOLEAN_VALUEOF, rBoolean);

        // DATE FUNCTIONS

        // ERROR FUNCTIONS
        addSig(signatures, ERROR_TOSTRING, none);

        // JSON FUNCTIONS
        addSig(signatures, JSON_PARSE, none, pString);
        addSig(signatures, JSON_STRINGIFY, none, mpDontCare);

        return signatures;
    }

    private void addConstructorVarSig(Map<HostObject, Signature> signatures, ECMAScriptObjects hostObject, Parameter... parameters) {
        addStaticVarSig(signatures, hostObject, true, parameters);
    }

    private void addConstructorSig(Map<HostObject, Signature> signatures, ECMAScriptObjects hostObject, Parameter... parameters) {
        addStaticSig(signatures, hostObject, true, parameters);
    }

    private interface Signature {

        boolean shouldStopPropagation(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c);
    }

    private interface Coercion {

        boolean isDelayed(Value v, Solver.SolverInterface c);
    }

    private interface Requirement {

        boolean maybeSatisfied(Value v);

        boolean maybeNotSatisfied(Value v);
    }

    private static class DelayedCoercions {
        // TODO refactor content of this class

        public static boolean number(Value v, Solver.SolverInterface c) {
            return Conversion.toNumber(v, c).isNone();
        }

        public static boolean integer(Value v, Solver.SolverInterface c) {
            return Conversion.toInteger(v, c).isNone();
        }

        public static boolean string(Value v, Solver.SolverInterface c) {
            return Conversion.toString(v, c).isNone();
        }

        public static boolean bool(Value v, Solver.SolverInterface c) {
            return false;
        }

        public static boolean object(Value v, Solver.SolverInterface c) {
            return Conversion.toObject(c.getNode(), v, c).isNone();
        }

        public static boolean regExp(Value v, Solver.SolverInterface c) {
            return false; // FIXME this might involve toString on v, and exceptions from the RegExp constructor!
        }

        public static boolean regExpIfNotString(Value v, Solver.SolverInterface c) {
            Set<ObjectLabel> nonStringObjects = v.getObjectLabels().stream().filter(l -> l.getKind() != ObjectLabel.Kind.STRING).collect(Collectors.toSet());
            Value left = v.restrictToNotStr().restrictToNotObject().join(Value.makeObject(nonStringObjects));
            if (left.isNone()) {
                return false;
            }
            return regExp(left, c);
        }

        public static boolean stringIfNotFunction(Value v, Solver.SolverInterface c) {
            Set<ObjectLabel> nonFunctions = v.getObjectLabels().stream().filter(l -> l.getKind() != ObjectLabel.Kind.FUNCTION).collect(Collectors.toSet());
            Value left = v.restrictToNotObject().join(Value.makeObject(nonFunctions));
            if (left.restrictToNotObject().isNone()) {
                return false;
            }
            return string(left, c);
        }

        public static boolean stringIfNotRegExpOrUndefined(Value v, Solver.SolverInterface c) {
            Set<ObjectLabel> nonRegExps = v.getObjectLabels().stream().filter(l -> l.getKind() != ObjectLabel.Kind.REGEXP).collect(Collectors.toSet());
            Value left = v.restrictToNotUndef().restrictToNotObject().join(Value.makeObject(nonRegExps));
            if (left.isNone()) {
                return false;
            }
            return string(left, c);
        }

        public static boolean stringIfNotUndefined(Value v, Solver.SolverInterface c) {
            Value left = v.restrictToNotUndef();
            if (left.isNone()) {
                return false;
            }
            return string(left, c);
        }

        public static boolean objectIfNotNullUndefined(Value v, Solver.SolverInterface c) {
            Value left = v.restrictToNotNullNotUndef();
            if (left.isNone()) {
                return false;
            }
            return object(left, c);
        }

        public static boolean stringIfNotRegExp(Value v, Solver.SolverInterface c) {
            Set<ObjectLabel> nonRegExps = v.getObjectLabels().stream().filter(l -> l.getKind() != ObjectLabel.Kind.REGEXP).collect(Collectors.toSet());
            Value left = v.restrictToNotObject().join(Value.makeObject(nonRegExps));
            if (left.isNone()) {
                return false;
            }
            return string(left, c);
        }

        public static boolean numberIfNotString(Value v, Solver.SolverInterface c) {
            Set<ObjectLabel> nonStringObjects = v.getObjectLabels().stream().filter(l -> l.getKind() != ObjectLabel.Kind.STRING).collect(Collectors.toSet());
            Value left = v.restrictToNotStr().restrictToNotObject().join(Value.makeObject(nonStringObjects));
            if (left.isNone()) {
                return false;
            }
            return number(left, c);
        }
    }

    private static class SimpleSignature implements Signature {

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
            int lastMust;
            for (lastMust = 0; lastMust < parameters.size(); lastMust++) {
                if (!parameters.get(lastMust).mustBePresent) {
                    break;
                }
            }
            this.minArguments = lastMust;
            // TODO currently not checking that there are 'mays' before 'musts'
        }

        @Override
        public boolean shouldStopPropagation(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
            if (call.isConstructorCall() && !isConstructor) {
                return true;
            }
            warnAboutArgumentCounts(hostobject, call, c);
            List<Value> parameterValues = newList();
            for (int i = 0; i < parameters.size(); i++) {
                parameterValues.add(NativeFunctions.readParameter(call, c.getState(), i));
            }

            Set<Pair<Value, Requirement>> requirements = newSet();
            Set<Pair<Value, Coercion>> coercions = newSet();
            pairValueAndDescription(c.getState().readThis(), this.base, false, coercions, requirements, c);
            for (int i = 0; i < parameters.size(); i++) {
                Parameter parameter = parameters.get(i);
                boolean absentButOptional = (!call.isUnknownNumberOfArgs() && call.getNumberOfArgs() <= i) && !parameter.mustBePresent;
                pairValueAndDescription(parameterValues.get(i), parameter.valueDescription, absentButOptional, coercions, requirements, c);
            }

            boolean definitelyFailingRequirement = requirements.stream()
                    .anyMatch(r -> isRequirementDefinetelyFailing(r.getFirst(), r.getSecond(), c));
            boolean delayedCoercion = coercions.stream()
                    .anyMatch(pair -> pair.getSecond().isDelayed(pair.getFirst(), c));

            boolean stopPropagation = definitelyFailingRequirement || delayedCoercion;
            return stopPropagation;
        }

        private void pairValueAndDescription(Value value, ValueDescription description, boolean absentButOptional, Set<Pair<Value, Coercion>> coercions, Set<Pair<Value, Requirement>> requirements, GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, Analysis>.SolverInterface c) {
            if (description.requirement.isPresent()) {
                requirements.add(Pair.make(value, description.requirement.get()));
            }
            if (!absentButOptional) {
                if (description.coercion.isPresent()) {
                    coercions.add(Pair.make(value, description.coercion.get()));
                }
            }
        }

        private void warnAboutArgumentCounts(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
            if (c.isScanning()) {
                c.getMonitoring().visitNativeFunctionCall(call.getSourceNode(), hostobject, call.isUnknownNumberOfArgs(), call.isUnknownNumberOfArgs() ? -1 : call.getNumberOfArgs(), minArguments, maxArguments);
            }
        }
    }

    private static class Requirements {

        // TODO unify implementations in this class

        public static Requirement isBoolean = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                boolean b = v.isMaybeTrue() || v.isMaybeFalse()
                        || v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() == ObjectLabel.Kind.BOOLEAN);
                return b;
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.restrictToNotObject().isMaybeOtherThanBool()
                        || v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() != ObjectLabel.Kind.BOOLEAN);
            }
        };

        public static Requirement isString = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                boolean b = v.isMaybeFuzzyStr()
                        || v.isMaybeSingleStr()
                        || v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() == ObjectLabel.Kind.STRING);
                return b;
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.restrictToNotObject().isMaybeOtherThanStr()
                        || v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() != ObjectLabel.Kind.STRING);
            }
        };

        public static Requirement isNumber = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                boolean b = v.isMaybeFuzzyNum()
                        || v.isMaybeSingleNum()
                        || v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() == ObjectLabel.Kind.NUMBER);
                return b;
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.restrictToNotObject().isMaybeOtherThanNum()
                        || v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() != ObjectLabel.Kind.NUMBER);
            }
        };

        public static Requirement isRegExp = new Requirement() {
            @Override
            public boolean maybeSatisfied(Value v) {
                return v.getObjectLabels().stream()
                        .anyMatch(l -> l.getKind() == ObjectLabel.Kind.REGEXP);
            }

            @Override
            public boolean maybeNotSatisfied(Value v) {
                return v.isMaybeOtherThanObject() ||
                        v.getObjectLabels().stream()
                                .anyMatch(l -> l.getKind() != ObjectLabel.Kind.REGEXP);
            }
        };
    }

    /**
     * Generalization of other signatures. Capable of picking the appropriate signatures based on the number of arguments at the call site.
     */
    private static class ArityOverloadedSignature implements Signature {

        private final Map<Pair<Integer, Integer>, Signature> signatures;

        public ArityOverloadedSignature(SimpleSignature... simpleSignatures) {
            signatures = newMap();
            Arrays.asList(simpleSignatures).forEach(sig -> signatures.put(Pair.make(sig.minArguments, sig.maxArguments), sig));

            if (signatures.size() != simpleSignatures.length) {
                throw new AnalysisException("Ambiguous overloading: " + simpleSignatures);
            }
        }

        @Override
        public boolean shouldStopPropagation(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
            Collection<Signature> targets = pickTargetsByArity(call);
            return targets.stream().anyMatch(s -> s.shouldStopPropagation(hostobject, call, c));
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

    private static class VarSignature implements Signature {

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
            if (this.varParameter.mustBePresent) {
                throw new AnalysisException("The last VarSig parameter should not be mandatory.");
            }
        }

        @Override
        public boolean shouldStopPropagation(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
            boolean shouldStopAccordingToSimpleSig = shouldStopAccordingToSimpleSig(hostobject, call, c);
            ValueDescription description = varParameter.valueDescription;
            Optional<Coercion> coercion = description.coercion;
            Optional<Requirement> requirement = description.requirement;
            boolean shouldStopAccordingToCoercion = false;
            boolean shouldStopAccordingToRequirement = false;
            if (call.isUnknownNumberOfArgs()) {
                Value unknownArg = NativeFunctions.readUnknownParameter(call);
                if (coercion.isPresent()) {
                    shouldStopAccordingToCoercion = coercion.get().isDelayed(unknownArg, c);
                }
                if (requirement.isPresent()) {
                    shouldStopAccordingToRequirement = isRequirementDefinetelyFailing(unknownArg, requirement.get(), c);
                }
            } else {
                for (int i = fixedParameterCount; i < call.getNumberOfArgs(); i++) {
                    Value arg = NativeFunctions.readParameter(call, c.getState(), i);
                    if (coercion.isPresent()) {
                        shouldStopAccordingToCoercion |= coercion.get().isDelayed(arg, c);
                    }
                    if (requirement.isPresent()) {
                        shouldStopAccordingToRequirement |= isRequirementDefinetelyFailing(arg, requirement.get(), c);
                    }
                }
            }
            boolean shouldStopAccordingToVarParameter = shouldStopAccordingToCoercion || shouldStopAccordingToRequirement;
            return shouldStopAccordingToSimpleSig || shouldStopAccordingToVarParameter;
        }

        private boolean shouldStopAccordingToSimpleSig(HostObject hostobject, final FunctionCalls.CallInfo call, GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, Analysis>.SolverInterface c) {
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
                public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
                    return callee_state.readThisObjects();
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
            }, c);
        }
    }

    private static class ValueDescription {

        private Optional<Requirement> requirement;

        private Optional<Coercion> coercion;

        public ValueDescription(Optional<Requirement> requirement, Optional<Coercion> coercion) {
            this.requirement = requirement;
            this.coercion = coercion;
        }
    }

    private abstract class Parameter {

        final ValueDescription valueDescription;

        private final boolean mustBePresent;

        public Parameter(ValueDescription valueDescription, boolean mustBePresent) {
            this.valueDescription = valueDescription;
            this.mustBePresent = mustBePresent;
        }
    }

    private class Maybe extends Parameter {

        public Maybe(ValueDescription valueDescription) {
            super(valueDescription, false);
        }
    }

    private class Must extends Parameter {

        public Must(ValueDescription valueDescription) {
            super(valueDescription, true);
        }

        Maybe toMaybe() {
            return new Maybe(this.valueDescription);
        }
    }
}
