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

package dk.brics.tajs.lattice;

import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.IContext;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.DeepImmutable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Context for context sensitive analysis.
 * Immutable.
 */
public final class Context implements IContext<Context>, DeepImmutable {

    /**
     * Cached hashcode for immutable instance.
     */
    private final int hashcode;

    /**
     * Value of 'this' (for object sensitivity), or null if none.
     */
    private final Value thisval;

    /**
     * Value of arguments if unknown number of arguments (for parameter sensitivity), or null if not used.
     */
    private final Value unknownArg;

    /**
     * Parameter names (for parameter sensitivity), null if empty or not used.
     */
    private final List<String> parameterNames;

    /**
     * Argument values (for parameter sensitivity), null if empty or not used.
     * Note that this list may not have the same length as parameterNames!
     */
    private final List<Value> arguments;

    /**
     * Values of selected free variables (for parameter sensitivity), or null if none.
     */
    private final Map<String, Value> freeVariables;

    /**
     * Values of special registers (for for-in specialization), or null if empty or not used.
     */
    private final Map<Integer, Value> specialRegs;

    /**
     * Values used to differentiate objects from the same allocation site.
     */
    private final Map<Qualifier, Value> extraAllocationContexts;

    public interface Qualifier {}

     /**
     * Loop unrolling, null if empty or not used.
     */
    private final Map<BeginLoopNode, Integer> loopUnrolling;

    /**
     * Partitioning information for free variables, null if empty or not used.
     * If freeVariablePartitioning, for example, maps x to {(partitioning-node, pkey), ...},
     * then it is safe to ignore partitions of x that are not in that set, for any choice of partitioning node.
     */
    private final FunctionPartitions functionPartitions;

    /**
     * Context information at the function entry, or null if none.
     *
     * This information is uniquely determined by the other fields (so can be ignored in equals and hashCode).
     */
    private final Context contextAtEntry;

    /**
     * Constructs a new context object.
     */
    private Context(Value thisval, Map<Integer, Value> specialRegs,
                    Context contextAtEntry, Map<Qualifier, Value> extraAllocationContexts,
                    Map<BeginLoopNode, Integer> loopUnrolling,
                    Value unknownArg, List<String> parameterNames, List<Value> arguments, Map<String, Value> freeVariables,
                    FunctionPartitions functionPartitions) {
        // ensure canonical representation of empty maps
        if (specialRegs != null && specialRegs.isEmpty())
            specialRegs = null;
        if (extraAllocationContexts != null && extraAllocationContexts.isEmpty())
            extraAllocationContexts = null;
        if (loopUnrolling != null && loopUnrolling.isEmpty())
            loopUnrolling = null;
        if (freeVariables != null && freeVariables.isEmpty())
            freeVariables = null;
        if (parameterNames != null && parameterNames.isEmpty())
            parameterNames = null;
        if (arguments != null && arguments.isEmpty())
            arguments = null;
        if (contextAtEntry == null)
            contextAtEntry = this;
        this.thisval = thisval;
        this.specialRegs = specialRegs;
        this.contextAtEntry = contextAtEntry;
        this.extraAllocationContexts = extraAllocationContexts;
        this.loopUnrolling = loopUnrolling;
        this.unknownArg = unknownArg;
        this.arguments = arguments;
        List<String> relevantParameterNames = parameterNames != null ? parameterNames.subList(0, arguments == null ? 0 : Math.min(parameterNames.size(), arguments.size())) : null; // TODO: review
        this.parameterNames = relevantParameterNames == null || relevantParameterNames.isEmpty() ? null : parameterNames;
        this.freeVariables = freeVariables == null || freeVariables.isEmpty() ? null : freeVariables;
        this.functionPartitions = functionPartitions;
        if (Options.get().isDebugOrTestEnabled()) {
            if ((this.arguments != null && this.arguments.stream().anyMatch(a -> a != null && a.isPolymorphicOrUnknown())) ||
                    (this.freeVariables != null && this.freeVariables.values().stream().anyMatch(a -> a != null && a.isPolymorphicOrUnknown()))) {
                throw new AnalysisException("Attempting to be context sensitive in polymorphic or unknown value");
            }
        }
        int hashcode = 1;
        hashcode = 31 * hashcode + (thisval != null ? thisval.hashCode() : 0);
        hashcode = 31 * hashcode + (specialRegs != null ? specialRegs.hashCode() : 0);
        hashcode = 31 * hashcode + (extraAllocationContexts != null ? extraAllocationContexts.hashCode() : 0);
        hashcode = 31 * hashcode + (loopUnrolling != null ? loopUnrolling.hashCode() : 0);
        hashcode = 31 * hashcode + (unknownArg != null ? unknownArg.hashCode() : 0);
        hashcode = 31 * hashcode + (freeVariables != null ? freeVariables.hashCode() : 0);
        hashcode = 31 * hashcode + (parameterNames != null ? parameterNames.hashCode() : 0);
        hashcode = 31 * hashcode + (arguments != null ? arguments.hashCode() : 0);
        hashcode = 31 * hashcode + (functionPartitions != null ? functionPartitions.hashCode() : 0);
        this.hashcode = hashcode;
    }

    public static Context make(Value thisval, Map<Integer, Value> specialRegs,
                               Context contextAtEntry, Map<Qualifier, Value> extraAllocationContexts,
                               Map<BeginLoopNode, Integer> loopUnrolling,
                               Value unknownArg, List<String> parameterNames, List<Value> arguments, Map<String, Value> freeVariables, FunctionPartitions partitionings) {
        return Canonicalizer.get().canonicalize(new Context(thisval, specialRegs, contextAtEntry,
                extraAllocationContexts, loopUnrolling, unknownArg, parameterNames, arguments, freeVariables, partitionings));
    }

    public static Context make(Value unknownArg, List<String> parameterNames, List<Value> arguments, Map<String, Value> freeVariables) {
        return make(null, null, null, null, null, unknownArg, parameterNames, arguments, freeVariables, null);
    }

    public static Context makeThisVal(Value thisval, FunctionPartitions partitionings) {
        return make(thisval, null, null, null, null, null, null, null, null, partitionings);
    }

    public static Context makeFreeVars(Map<String, Value> freeVariables) {
        return make(null, null, null, null, null, null, null, null, freeVariables, null);
    }

    public static Context makeQualifiers(Map<Qualifier, Value> extraAllocationContexts) {
        return make(null, null, null, extraAllocationContexts, null, null, null, null, null, null);
    }

    public static Context makeEmpty() {
        return make(null, null, null, null, null, null, null, null, null, null);
    }

    /**
     * Constructs a copy of this context with the given loop unrolling.
     */
    public Context copyWithLoopUnrolling(Map<BeginLoopNode, Integer> loopUnrolling) {
        return make(thisval, specialRegs, contextAtEntry, extraAllocationContexts, loopUnrolling, unknownArg, parameterNames, arguments, freeVariables, functionPartitions);
    }

    /**
     * Constructs a copy of this context, but with an additional extra allocation context.
     */
    public Context copyWithNewExtraAllocationContext(Qualifier q, Value value) {
        Map<Qualifier, Value> m = extraAllocationContexts != null ? newMap(extraAllocationContexts) : newMap();
        m.put(q, value);
        return make(thisval, specialRegs, contextAtEntry, m, loopUnrolling, unknownArg, parameterNames, arguments, freeVariables, functionPartitions);
    }

    /**
     * Returns the this-value.
     */
    public Value getThisVal() {
        return thisval;
    }

    /**
     * Returns the special registers map.
     */
    public Map<Integer, Value> getSpecialRegisters() {
        return specialRegs;
    }

    /**
     * Returns the loop unrolling information.
     */
    public Map<BeginLoopNode, Integer> getLoopUnrolling() {
        return loopUnrolling;
    }

    /**
     * Returns true iff the number and order of arguments to the function are unknown.
     */
    public boolean isUnknownArgs() {
        return unknownArg != null;
    }

    public Value getUnknownArg() {
        return unknownArg;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public boolean hasArguments() {
        return arguments != null;
    }

    /**
     * Returns the arguments the function was invoked with, or null if empty or not used.
     */
    public List<Value> getArguments() {
        return arguments;
    }

    public Map<String, Value> getFreeVariables() {
        return freeVariables;
    }

    public Value getParameterValue(String name) {
        if (parameterNames == null)
            return null;
        int index = parameterNames.indexOf(name);
        if (index == -1)
            return null;
        if (arguments.size() <= index)
            return null;
        return arguments.get(index);
    }

    /**
     * Returns the context at entry.
     */
    @Override
    public Context getContextAtEntry() {
        return contextAtEntry;
    }

    /**
     * Returns the freeVariablePartitioning, or null if empty or not used.
     */
    public FunctionPartitions getFunctionPartitions() {
        return functionPartitions;
    }

    @Override
    public boolean equals(Object obj) {
        if (!Canonicalizer.get().isCanonicalizing())
            return this == obj;
        if (obj == this)
            return true;
        if (!(obj instanceof Context))
            return false;
        Context c = (Context) obj;
        return hashcode == c.hashcode &&
                Objects.equals(thisval, c.thisval) &&
                Objects.equals(specialRegs, c.specialRegs) &&
                Objects.equals(extraAllocationContexts, c.extraAllocationContexts) &&
                Objects.equals(loopUnrolling, c.loopUnrolling) &&
                Objects.equals(unknownArg, c.unknownArg) &&
                Objects.equals(freeVariables, c.freeVariables) &&
                Objects.equals(parameterNames, c.parameterNames) &&
                Objects.equals(arguments, c.arguments) &&
                Objects.equals(functionPartitions, c.functionPartitions);
    }

    @Override
    public int hashCode() {
        return this.hashcode;
    }

    @Override
    public String toString() { // TODO: cache resulting string?
        StringBuilder s = new StringBuilder();
        boolean any = false;
        if (thisval != null) {
//            if (any)
//                s.append(", ");
            s.append("this=").append(thisval);
            any = true;
        }
        if (specialRegs != null) {
            if (any)
                s.append(", ");
            s.append("specialRegs=").append(specialRegs);
            any = true;
        }
        if (extraAllocationContexts != null) {
            if (any)
                s.append(", ");
            s.append(extraAllocationContexts);
            any = true;
        }
        if (loopUnrolling != null) {
            if (any)
                s.append(", ");
            s.append(loopUnrolling);
            any = true;
        }
        String freeVariablesString = freeVariables == null ? "" : freeVariables.toString();
        if (isUnknownArgs()) {
            return "UnknownArg(" + unknownArg + ")" + freeVariablesString;
        }
        Map<String, String> mapLike = newMap();
        List<String> parameterNames = this.parameterNames != null ? this.parameterNames : newList();
        List<Value> arguments = this.arguments != null ? this.arguments : newList();
        int max = Math.max(parameterNames.size(), arguments.size());
        for (int i = 0; i < max; i++) {
            Value argument = arguments.size() > i ? arguments.get(i) : null;
            String parameterName = parameterNames.size() > i ? parameterNames.get(i) : "<" + i + ">";
            mapLike.put(parameterName, argument == null ? "-" : argument.toString());
        }
        String t =  (mapLike.isEmpty() ? "" : mapLike) + freeVariablesString;
        if (!t.isEmpty()) {
            if (any)
                s.append(", ");
            s.append(t);
            any = true;
        }
        if (functionPartitions != null) {
            if (any)
                s.append(", ");
            s.append(functionPartitions);
//            any = true;
        }
        return s.toString();
    }

    /**
     * Utility function for extracting object labels
     */
    public static Set<ObjectLabel> extractTopLevelObjectLabels(Context context) { // TODO: review (+used where? for detecting recursion in StaticDeterminacyContextSensitivityStrategy)
        if (context == null) {
            return newSet();
        }
        return Stream.concat(
                context.extraAllocationContexts != null ?
                        context.extraAllocationContexts.values().stream().flatMap(v -> v.getObjectLabels().stream()) :
                        Stream.empty(),
                extractTopLevelObjectLabels2(context).stream())
                .collect(Collectors.toSet());
    }

    private static Set<ObjectLabel> extractTopLevelObjectLabels2(Context arguments) { // TODO: review, inline?
        Stream<Value> freeVariableValues = arguments.getFreeVariables() != null ? arguments.getFreeVariables().values().stream() : Stream.empty();
        Stream<Value> argumentValues = arguments.hasArguments() ? arguments.getArguments().stream() : Stream.empty();
        Stream<Value> unknownValues = arguments.unknownArg != null ? Stream.of(arguments.unknownArg) : Stream.empty();
        return Stream.concat(unknownValues, Stream.concat(argumentValues, freeVariableValues))
                .filter(Objects::nonNull)
                .flatMap(v -> v.getObjectLabels().stream())
                .collect(Collectors.toSet());
    }
}
