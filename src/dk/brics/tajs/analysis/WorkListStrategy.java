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

package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.ExceptionalReturnNode;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.IWorkListStrategy;

/**
 * Work list strategy.
 */
public class WorkListStrategy implements IWorkListStrategy<Context> {

    private CallGraph<State, Context, CallEdge> call_graph;

    /**
     * Constructs a new WorkListStrategy object.
     */
    public WorkListStrategy() {
    }

    /**
     * Sets the call graph.
     */
    public void setCallGraph(CallGraph<State, Context, CallEdge> call_graph) {
        this.call_graph = call_graph;
    }

    @Override
    public int compare(IEntry<Context> e1, IEntry<Context> e2) {
        BasicBlock n1 = e1.getBlock();
        BasicBlock n2 = e2.getBlock();
        int serial1 = e1.getSerial();
        int serial2 = e2.getSerial();

        if (serial1 == serial2)
            return 0;

        final int E1_FIRST = -1;
        final int E2_FIRST = 1;

        // low priority for event dispatcher node
        if (n1.getFirstNode() instanceof EventDispatcherNode && !(n2.getFirstNode() instanceof EventDispatcherNode))
            return E2_FIRST;
        if (n2.getFirstNode() instanceof EventDispatcherNode && !(n1.getFirstNode() instanceof EventDispatcherNode))
            return E1_FIRST;

        // low priority for exceptional return nodes
        if (n1.getFirstNode() instanceof ExceptionalReturnNode && !(n2.getFirstNode() instanceof ExceptionalReturnNode))
            return E2_FIRST;
        if (n2.getFirstNode() instanceof ExceptionalReturnNode && !(n1.getFirstNode() instanceof ExceptionalReturnNode))
            return E1_FIRST;

        if (n1.getFunction().equals(n2.getFunction()) && e1.getContext().equals(e2.getContext())) {
            // same function and same context: use block order
            if (n1.getOrder() < n2.getOrder())
                return E1_FIRST;
            else if (n2.getOrder() < n1.getOrder())
                return E2_FIRST;
        }

        int function_context_order1 = call_graph.getBlockContextOrder(BlockAndContext.makeEntry(e1.getBlock(), e1.getContext()));
        int function_context_order2 = call_graph.getBlockContextOrder(BlockAndContext.makeEntry(e2.getBlock(), e2.getContext()));

        // different function/context: order by occurrence number
        if (function_context_order1 < function_context_order2)
            return E2_FIRST;
        else if (function_context_order2 < function_context_order1)
            return E1_FIRST;

        // strategy: breadth first
        return serial1 - serial2;
    }
}
