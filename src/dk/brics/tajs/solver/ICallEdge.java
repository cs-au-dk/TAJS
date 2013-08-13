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

/**
 * Interface for call edges.
 */
public interface ICallEdge<BlockStateType extends IBlockState<?,?,?>> {

	/**
	 * Returns the state at this edge.
	 */
	public BlockStateType getState();
	
	/**
	 * Sets the state at this edge.
	 */
	public void setState(BlockStateType s);
	
	/**
	 * Visitor for call edges.
	 */
	public static interface Visitor<CallEdgeType extends ICallEdge<?>> {

		/**
		 * Called when visiting a call edge.
		 */
		public void visit(NodeAndContext<?> caller, CallEdgeType edge, BlockAndContext<?> callee);
	}
}
