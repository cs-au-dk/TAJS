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

package dk.brics.tajs.monitoring;

import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.Value;
import org.apache.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Collects abstract values for variable occurrences during the scanning phase of the analysis.
 * Used by the TAJS Eclipse plug-in.
 */
public class TypeCollector {

    private static Logger log = Logger.getLogger(TypeCollector.class);

    /**
     * Tuple of a variable name, source location, and a context.
     */
    public static class VariableSummary {

        private final String variable_name;

        private final SourceLocation source_location;

        private final Context context;

        public VariableSummary(String variable_name, SourceLocation source_location, Context context) {
            this.variable_name = variable_name;
            this.source_location = source_location;
            this.context = context;
        }

        public String getVariableName() {
            return variable_name;
        }

        public SourceLocation getVariableLocation() {
            return source_location;
        }

        public Context getContext() {
            return context;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VariableSummary that = (VariableSummary) o;

            if (variable_name != null ? !variable_name.equals(that.variable_name) : that.variable_name != null)
                return false;
            if (source_location != null ? !source_location.equals(that.source_location) : that.source_location != null)
                return false;
            return !(context != null ? !context.equals(that.context) : that.context != null);
        }

        @Override
        public int hashCode() {
            int result = variable_name != null ? variable_name.hashCode() : 0;
            result = 31 * result + (source_location != null ? source_location.hashCode() : 0);
            result = 31 * result + (context != null ? context.hashCode() : 0);
            return result;
        }
    }

    private final Map<VariableSummary, Value> type_info_map = new LinkedHashMap<>();

    /**
     * Records a variable name along with its source location and abstract value.
     *
     * @param variable_name   the name of the variable to record
     * @param source_location the location in the source code
     * @param value           the abstract value of the variable
     */
    public void record(String variable_name, SourceLocation source_location, Value value, Context context) { // TODO: do we need to consider unknown or polymorphic values here?
        VariableSummary new_location = new VariableSummary(variable_name == null ? "null" : variable_name, source_location, context);
        Value existing_value = type_info_map.get(new_location);
        if (existing_value != null)
            value = Value.join(existing_value, value); // joining values to accommodate for multiple contexts or imprecise location information
        type_info_map.put(new_location, value);
    }

    /**
     * Returns the collected type information.
     */
    public Map<VariableSummary, Value> getTypeInformation() {
        return type_info_map;
    }

    /**
     * Presents the collected type information in the format varname: location -&gt; type.
     */
    public void logTypeInformation() {
        for (Entry<VariableSummary, Value> entry : type_info_map.entrySet()) {
            log.info(entry.getKey().getVariableName() + ":\t" + entry.getKey().getVariableLocation() + "\t->\t" + entry.getValue());
        }
    }
}
