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

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;

import java.util.Collection;

/**
 * Monitoring interface.
 * <p>
 * This interface contains callbacks for solver-specific operations.
 */
public interface ISolverMonitoring<StateType extends IState<StateType, ContextType, ?>, ContextType extends IContext<ContextType>> {

    /**
     * Returns true if the fixpoint solver should continue with its next iteration or abort early and unsoundly.
     */
    boolean allowNextIteration();

    /**
     * Invoked immediately before processing a node transfer.
     */
    void visitNodeTransferPre(AbstractNode n, StateType s);

    /**
     * Invoked immediately after processing a node transfer.
     */
    void visitNodeTransferPost(AbstractNode n, StateType s);

    /**
     * Invoked immediately before processing a block transfer.
     */
    void visitBlockTransferPre(BasicBlock b, StateType s);

    /**
     * Invoked immediately after processing a block transfer.
     */
    void visitBlockTransferPost(BasicBlock b, StateType s);

    /**
     * Invoked when new dataflow is being propagated.
     */
    void visitNewFlow(BasicBlock b, ContextType c, StateType s, String diff, String info);

    /**
     * Invoked when a function is encountered in the scan phase.
     * (Invoked once on each function.)
     */
    void visitFunction(Function f, Collection<StateType> entry_states);

    /**
     * Invoked immediately before propagating dataflow from one location to another.
     * (This does not include the merging of states that occurs in some specialized transfers, such as {@link dk.brics.tajs.analysis.ParallelTransfer}.)
     */
    void visitPropagationPre(BlockAndContext<ContextType> from, BlockAndContext<ContextType> to);

    /**
     * Invoked immediately after propagating dataflow from one location to another.
     *
     * @param changed true if the destination state was changed
     * @see #visitPropagationPre(BlockAndContext, BlockAndContext)
     */
    void visitPropagationPost(BlockAndContext<ContextType> from, BlockAndContext<ContextType> to, boolean changed);

    /**
     * Invoked when dataflow solver iteration is done.
     */
    void visitIterationDone();
}
