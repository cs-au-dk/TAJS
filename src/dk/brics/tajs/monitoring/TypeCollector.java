/*
 * Copyright 2009-2013 Aarhus University
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

import java.util.LinkedHashMap;
import java.util.Map;

import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.Value;

/**
 * Collects abstract values for variable occurrences during the scanning phase of the analysis.
 */
public class TypeCollector { // Used by the TAJS Eclipse plug-in
	
	/**
	 * Pair of a variable name and a source location.
	 */
	public static class VariableSummary {
		
		private String variable_name;
		
		private SourceLocation source_location;
		
		public VariableSummary(String variable_name, SourceLocation source_location) {
			this.variable_name = variable_name;
			this.source_location = source_location;
		}

		public String getVariableName() {
			return variable_name;
		}

		public SourceLocation getVariableLocation() {
			return source_location;
		}

		@Override
		public int hashCode() {
			return 7 * variable_name.hashCode() + 3 * source_location.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj instanceof VariableSummary) {	
				VariableSummary other = (VariableSummary) obj;
				if (source_location.equals(other.getVariableLocation()) && variable_name.equals(other.getVariableName()))
					return true;
			}
			return false;
		}
	}
	
	private Map<VariableSummary, Value> type_info_map = new LinkedHashMap<>();

	/**
	 * Records a variable name along with its source location and abstract value.
	 * @param variable_name the name of the variable to record
	 * @param source_location the location in the source code
	 * @param value the abstract value of the variable
	 */
	public void record(String variable_name, SourceLocation source_location, Value value) { // TODO: do we need to consider unknown or polymorphic values here?
		VariableSummary new_location = new VariableSummary(variable_name == null ? "null" : variable_name, source_location);
		Value existing_value = type_info_map.get(new_location);
		if (existing_value != null) // TODO: return list of values instead of joining them?
			value = Value.join(existing_value, value); // joining values to accommodate for multiple contexts or imprecise location information
		type_info_map.put(new_location, value);
	}
	
	/**
	 * Returns the collected type information.
	 */
	public Map<VariableSummary, Value> getTypeInformation() { // Used by the TAJS Eclipse plug-in
		return type_info_map;
	}

	/**
	 * Presents the collected type information using {@code System.out.println} in the format
	 *  varname:	location -> type
	 */
	public void presentTypeInformation() { // TODO: present object type information in some better way?
		for (Map.Entry<VariableSummary, Value> entry : type_info_map.entrySet()) {
			System.out.println(entry.getKey().getVariableName() + ":\t" + entry.getKey().getVariableLocation() + "\t->\t" + entry.getValue());
		}
	}
}
