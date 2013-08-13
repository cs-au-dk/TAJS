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

package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.BasicBlock;

/**
 * Interface for analysis call contexts.
 * Must be immutable.
 */
public interface ICallContext<CallContextType extends ICallContext<?>> {

	/**
	 * Checks whether this context is equal to the given object. 
	 */
	@Override
	public boolean equals(Object obj);
	
	/**
	 * Computes a hash code for this context.
	 */
	@Override
	public int hashCode();
	
	/**
	 * Returns a description of this call context.
	 */
	@Override
	public String toString();

    /**
     * Returns the entry of the function or for-in body.
     */
    public BasicBlock getEntry();
}
