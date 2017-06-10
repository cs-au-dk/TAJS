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

package dk.brics.tajs.lattice;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Representation of arguments to a function.
 */
public class ContextArguments {// TODO: canonicalize? (#140)

    private final Value unknownArg;

    private final Map<String, Value> selectedClosureVariables;

    private final List<String> parameterNames;

    private final List<Value> arguments;

    private ContextArguments(Value unknownArg, List<String> parameterNames, List<Value> arguments, Map<String, Value> selectedClosureVariables) { // TODO: review, compare with 19b80eac3
        List<String> relevantParameterNames = parameterNames != null ? parameterNames.subList(0, arguments == null ? 0 : Math.min(parameterNames.size(), arguments.size())) : null;
        this.unknownArg = unknownArg;
        this.arguments = arguments == null || arguments.isEmpty() ? null : arguments;
        this.parameterNames = relevantParameterNames == null || relevantParameterNames.isEmpty() ? null : parameterNames;
        this.selectedClosureVariables = selectedClosureVariables == null || selectedClosureVariables.isEmpty() ? null : selectedClosureVariables;
        if (Options.get().isDebugOrTestEnabled()) {
            if ((this.arguments != null && this.arguments.stream().anyMatch(a -> a != null && a.isPolymorphicOrUnknown())) || (this.selectedClosureVariables != null && this.selectedClosureVariables.values().stream().anyMatch(a -> a != null && a.isPolymorphicOrUnknown()))) {
                throw new AnalysisException("Attempting to be context sensitive in polymorphic or unknown value");
            }
        }
    }
    /**
     * Context arguments for a function invocation with unknown arguments.
     *
     * @param unknownArg all the arguments in a single value
     */
    public ContextArguments(Value unknownArg, Map<String, Value> selectedClosureVariables) {
        this(unknownArg, null, null, selectedClosureVariables);
    }

    /**
     * Context arguments for a function invocation with a known order and number of arguments (standard invocation or precise apply/call).
     *
     * @param parameterNames as the syntactically known parameter names of the invoked function (used for pretty printing)
     * @param arguments      as the arguments, in the order they are provided to the function
     * @param selectedClosureVariables as the values of closure-variables
     */
    public ContextArguments(List<String> parameterNames, List<Value> arguments, Map<String, Value> selectedClosureVariables) {
        this(null, parameterNames, arguments, selectedClosureVariables);
    }

    /**
     * Returns true iff the number and order of arguments to the function are unknown.
     */
    public boolean isUnknown() {
        return unknownArg != null;
    }

    private String toString(boolean sourcesOnly) { // TODO: review
        String closureVariablesString = selectedClosureVariables == null? "": "&" + selectedClosureVariables;
        if (isUnknown()) {
            return "UnknownArg(" + unknownArg + ")" + closureVariablesString;
        }

        Map<String, String> mapLike = newMap();
        List<String> parameterNames = this.parameterNames != null ? this.parameterNames : newList();
        List<Value> arguments = this.arguments != null ? this.arguments : newList();
        int max = Math.max(parameterNames.size(), arguments.size());
        for (int i = 0; i < max; i++) {
            Value argument = arguments.size() > i ? arguments.get(i) : null;
            String parameterName = parameterNames.size() > i ? parameterNames.get(i) : "<" + i + ">";
            mapLike.put(parameterName, makeArgumentString(argument, sourcesOnly));
        }
        String argumentsString = mapLike.toString();

        return argumentsString + closureVariablesString;
    }

    private String makeArgumentString(Value argument, boolean sourcesOnly) {
        return argument == null ? "-" : sourcesOnly ? toSourcesOnly(argument) : argument.toString();
    }

    private static String toSourcesOnly(Value value) {
        return value.getObjectSourceLocations().toString();
    }

    @Override
    public String toString() {
        return toString(false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContextArguments that = (ContextArguments) o;

        if (unknownArg != null ? !unknownArg.equals(that.unknownArg) : that.unknownArg != null) return false;
        if (selectedClosureVariables != null ? !selectedClosureVariables.equals(that.selectedClosureVariables) : that.selectedClosureVariables != null)
            return false;
        if (parameterNames != null ? !parameterNames.equals(that.parameterNames) : that.parameterNames != null)
            return false;
        return !(arguments != null ? !arguments.equals(that.arguments) : that.arguments != null);
    }

    @Override
    public int hashCode() {
        int result = unknownArg != null ? unknownArg.hashCode() : 0;
        result = 31 * result + (selectedClosureVariables != null ? selectedClosureVariables.hashCode() : 0);
        result = 31 * result + (parameterNames != null ? parameterNames.hashCode() : 0);
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        return result;
    }

    public boolean hasArguments() {
        return arguments != null;
    }

    /**
     * Returns the arguments the function was invoked with.
     */
    public List<Value> getArguments() {
        if (arguments == null) {
            throw new AnalysisException("No arguments!");
        }
        return arguments;
    }

    public Map<String, Value> getSelectedClosureVariables() {
        return selectedClosureVariables;
    }

    public Value getParameterValue(String name) { // TODO: review
        int index = parameterNames.indexOf(name);
        if (index == -1) {
            return null;
        }
        if (arguments.size() <= index) {
            return null;
        }
        return arguments.get(index);
    }

    /**
     * Utility function for extracting object labels
     */
    public static Set<ObjectLabel> extractTopLevelObjectLabels(ContextArguments arguments) { // TODO: review
        if (arguments == null) {
            return newSet();
        }
        Stream<Value> closureParameterValues = arguments.getSelectedClosureVariables() != null ? arguments.getSelectedClosureVariables().values().stream() : Stream.empty();
        Stream<Value> argumentValues = arguments.hasArguments() ? arguments.getArguments().stream() : Stream.empty();
        Stream<Value> unknownValues = arguments.unknownArg != null ? Stream.of(arguments.unknownArg) : Stream.empty();
        return Stream.concat(unknownValues, Stream.concat(argumentValues, closureParameterValues))
                .filter(v -> v != null)
                .flatMap(v -> v.getObjectLabels().stream())
                .collect(Collectors.toSet());
    }
}
