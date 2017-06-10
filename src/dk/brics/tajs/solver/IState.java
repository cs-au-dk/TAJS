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

package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.BasicBlock;

import java.util.Map;

/**
 * Interface for abstract states.
 */
public interface IState
        <StateType extends IState<StateType, ContextType, CallEdgeType>,
                ContextType extends IContext<ContextType>,
                CallEdgeType extends ICallEdge<StateType>> extends Cloneable {

    /**
     * Constructs a new state as a copy of this state.
     */
    StateType clone();

    /**
     * Propagates the given state into this state.
     *
     * @return true if changed
     */
    boolean propagate(StateType s, boolean funentry);

    /**
     * Checks whether this abstract state represents the empty set of concrete states.
     * This is an approximation in the sense that not all possible inconsistencies may be discovered,
     * i.e. if true is returned then the abstract state definitely represents the empty set of concrete states
     * but maybe not the other way around.
     */
    boolean isNone();

    /**
     * Returns a brief description of the state.
     */
    String toStringBrief();

    /**
     * Produces a graphviz dot representation of this state.
     */
    String toDot();

    /**
     * Returns a description of the changes from the old state to this state.
     * It is assumed that the old state is less than this state.
     */
    String diff(StateType old);

//    /**
//     * Removes value parts that are in the other state from this state.
//     * It is assumed that this state subsumes the other state.
//     */
//    void remove(StateType other);

    /**
     * Localizes this state according to the given existing state.
     */
    void localize(StateType s);

    /**
     * Transforms this state according to the given edge.
     *
     * @param edge                edge information
     * @param callee_entry_states all states of the callee entry
     * @param callee              the callee function entry
     * @return the context for the transformed state
     */
    ContextType transform(CallEdgeType edge, ContextType edge_context,
                          Map<ContextType, StateType> callee_entry_states, BasicBlock callee);

    /**
     * Transforms this state inversely according to the given edge.
     *
     * @return true if the incoming flow needs to be recomputed
     */
    boolean transformInverse(CallEdgeType edge, BasicBlock callee, ContextType callee_context);

    /**
     * Returns the context for this state.
     */
    ContextType getContext();

    /**
     * Returns the basic block for this state.
     */
    BasicBlock getBasicBlock();
}
