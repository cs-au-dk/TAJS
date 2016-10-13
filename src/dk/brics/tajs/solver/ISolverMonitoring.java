/*
 * Copyright 2009-2016 Aarhus University
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

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;

import java.util.Collection;

/**
 * Records various information during fixpoint solving.
 */
public interface ISolverMonitoring<StateType extends IState<StateType, ContextType, ?>, ContextType extends IContext<ContextType>> {

    /**
     * Registers a node transfer occurrence.
     */
    void visitNodeTransfer(AbstractNode n);

    /**
     * Registers the beginning of a block transfer
     */
    void visitBlockTransfer(BasicBlock b, StateType s);

    /**
     * Registers the end of a block transfer
     */
    void visitPostBlockTransfer(BasicBlock b, StateType s);

    /**
     * Registers new dataflow being propagated.
     */
    void visitNewFlow(BasicBlock b, ContextType c, StateType s, String diff, String info);

    /**
     * Registers a recovery of an unknown value.
     */
    void visitUnknownValueResolve(boolean partial, boolean scanning);

    /**
     * Registers a property recovery graph size.
     */
    void visitRecoveryGraph(int size);

    /**
     * Registers the given function in the scan phase.
     * (Invoked once on each function.)
     */
    void visitFunction(Function f, Collection<StateType> entry_states);

    /**
     * Registers a maybe reachable node.
     */
    void visitReachableNode(AbstractNode n);

    /**
     * Registers a state join operation.
     */
    void visitJoin();

    /**
     * Returns true if the fixpoint solver should continue with its next iteration or abort early and unsoundly.
     */
    boolean allowNextIteration();
}
