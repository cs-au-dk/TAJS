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

import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.util.AnalysisException;

/**
 * Interface for abstract states for block entries.
 */
public interface IBlockState
<BlockStateType extends IBlockState<BlockStateType,CallContextType,CallEdgeType>, 
CallContextType extends ICallContext<CallContextType>,
CallEdgeType extends ICallEdge<BlockStateType>> extends Cloneable {

	/**
	 * Checks that this state is consistent.
     * Does nothing if debug mode and test mode are disabled.
	 * @throws AnalysisException if inconsistent
	 */
	public void check();
	
	/**
	 * Constructs a new state as a copy of this state.
	 */
	public BlockStateType clone();
	
	/**
	 * Propagates the given state into this state.
	 * @return true if changed
	 */
	public boolean propagate(BlockStateType s);
	
    /**
     * Checks whether this abstract state represents the empty set of concrete states.
     * This is an approximation in the sense that not all possible inconsistencies may be discovered,
     * i.e. if true is returned then the abstract state definitely represents the empty set of concrete states
     * but maybe not the other way around.
     */
	public boolean isNone();
	
	/**
	 * Returns a brief description of the state.
	 */
	public String toStringBrief();
	
    /**
     * Produces a graphviz dot representation of this state.
     */
	public String toDot();
	
	/**
	 * Returns a description of the changes from the old state to this state.
	 * It is assumed that the old state is less than this state.
	 */
	public String diff(BlockStateType old);
	
	/**
	 * Removes value parts that are in the other state from this state.
	 * It is assumed that this state subsumes the other state.
	 */
	public void remove(BlockStateType other);
	
	/**
	 * Trims this state according to the given existing state.
	 */
	public void trim(BlockStateType s);
	
	/**
	 * Transforms this state according to the given edge.
	 */
	public void transform(CallEdgeType edge, BlockStateType callee_entry_state, Function callee, CallContextType callee_context);
	
	/**
	 * Transforms this state inversely according to the given edge.
	 */
	public void transformInverse(CallEdgeType edge, BlockStateType callee_entry_state);
}
