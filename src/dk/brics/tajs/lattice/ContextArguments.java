package dk.brics.tajs.lattice;

import dk.brics.tajs.util.AnalysisException;

import java.util.List;
import java.util.Map;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * Representation of arguments to a function.
 */
public class ContextArguments {

    private final Value unknownArg;

    private final Map<String, Value> selectedClosureVariables;

    private final List<String> parameterNames;

    private final List<Value> arguments;

    /**
     * Context arguments for a function invocation with unknown arguments.
     *
     * @param unknownArg all the arguments in a single value
     */
    public ContextArguments(Value unknownArg, Map<String, Value> selectedClosureVariables) {
        this.unknownArg = unknownArg;
        this.arguments = null;
        this.parameterNames = null;
        this.selectedClosureVariables = selectedClosureVariables;
    }

    /**
     * Context arguments for a function invocation with a known order and number of arguments (standard invocation or precise apply/call).
     *
     * @param parameterNames as the syntactically known parameter names of the invoked function (used for pretty printing)
     * @param arguments      as the arguments, in the order they are provided to the function
     * @param selectedClosureVariables as the values of closure-variables
     */
    public ContextArguments(List<String> parameterNames, List<Value> arguments, Map<String, Value> selectedClosureVariables) {
        this.parameterNames = parameterNames;
        this.arguments = arguments;
        this.unknownArg = null;
        this.selectedClosureVariables = selectedClosureVariables;
    }

    /**
     * Returns true iff the number and order of arguments to the function are unknown.
     */
    public boolean isUnknown() {
        return unknownArg != null;
    }

    private String toString(boolean sourcesOnly) {
        String closureVariablesString = selectedClosureVariables == null? "": "&" + selectedClosureVariables;
        if (isUnknown()) {
            return "UnknownArg(" + unknownArg + ")" + closureVariablesString;
        }

        String argumentsString;
        if(arguments != null) {
            final Map<String, String> mapLike = newMap();
            int i = 0;
            for (Value value : arguments) {
                if (parameterNames.size() > i) {
                    mapLike.put(parameterNames.get(i), value == null ? "-" : sourcesOnly ? toSourcesOnly(value) : value.toString());
                }
                i++;
            }
            for (; i < arguments.size(); i++) {
                Value value = arguments.get(i);
                mapLike.put("<" + i + ">", value == null ? "-" : sourcesOnly ? toSourcesOnly(value) : value.toString());
            }
            argumentsString = mapLike.toString();
        }else{
            argumentsString = "{}";
        }

        return argumentsString + closureVariablesString;
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

}
