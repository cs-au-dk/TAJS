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

package dk.brics.tajs.analysis.uneval;

import java.util.Collection;
import java.util.Map;

import dk.brics.tajs.util.Collections;

/**
 * Represents an expression on normal form. An expression on normal form has the following form:
 * id
 * fn(id)
 * id <op> id ... <op> id
 * 
 * The variables in a normal form are TAJS registers, but to be independent of that representation they are represented by strings. 
 * A mapping is stored, that maps from these strings back to TAJS registers. 
 */
public class NormalForm { 

	/**
	 * The expression
	 */
	private String normal_form;
	
	/**
	 * Mapping to TAJS registers
	 */
	private Map<String,Integer> mapping;
	
	/**
	 * Names of the arguments used by this expression
	 */
	private Collection<String> arguments_in_use;
	
	public NormalForm(String normal_form, Map<String, Integer> mapping, Collection<String> args) {
		super();
		this.normal_form = normal_form;
		this.mapping = Collections.newMap(mapping);
		this.arguments_in_use = Collections.newSet(args);
	}

	public String getNormalForm() {
		return normal_form;
	}

	public Map<String, Integer> getMapping() {
		return mapping;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((arguments_in_use == null) ? 0 : arguments_in_use.hashCode());
		result = prime * result + ((mapping == null) ? 0 : mapping.hashCode());
		result = prime * result
				+ ((normal_form == null) ? 0 : normal_form.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NormalForm other = (NormalForm) obj;
		if (arguments_in_use == null) {
			if (other.arguments_in_use != null)
				return false;
		} else if (!arguments_in_use.equals(other.arguments_in_use))
			return false;
		if (mapping == null) {
			if (other.mapping != null)
				return false;
		} else if (!mapping.equals(other.mapping))
			return false;
		if (normal_form == null) {
			if (other.normal_form != null)
				return false;
		} else if (!normal_form.equals(other.normal_form))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NormalForm [normal_form=" + normal_form + ", mapping="
				+ mapping + ", arguments_in_use=" + arguments_in_use + "]";
	}

	/**
	 * 
	 * @return The list of arguments being read in this expression.
	 */
	public Collection<String> getArgumentsInUse() {
		return arguments_in_use;
	}
}
