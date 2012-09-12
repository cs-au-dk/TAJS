/*
 * Copyright 2012 Aarhus University
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
 * Interface for work list strategies.
 */
public interface IWorkListStrategy<CallContextType extends ICallContext<?>> {

	/**
	 * Compares two work list entries.
	 * A negative value means that the first has higher priority than the second,
	 * a positive value means that the second has higher priority than the first.
	 * This must be a stable comparison.
	 */
	public int compare(IEntry<CallContextType> e1, IEntry<CallContextType> e2);
	
	/**
	 * Interface for work list entries.
	 */
	public interface IEntry<CallContextType extends ICallContext<?>> {
		
		/**
		 * Returns the block.
		 */
		public BasicBlock getBlock();
		
		/**
		 * Returns the context.
		 */
		public CallContextType getContext();
		
		/**
		 * Returns the entry serial number.
		 */
		public int getSerial();
	}
}
