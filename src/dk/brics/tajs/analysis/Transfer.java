/*
 * Copyright 2009-2019 Aarhus University
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

import dk.brics.tajs.analysis.js.EdgeTransfer;
import dk.brics.tajs.analysis.js.NodeTransfer;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.solver.IEdgeTransfer;
import dk.brics.tajs.solver.INodeTransfer;
import org.apache.log4j.Logger;

/**
 * Transfer for flow graph nodes and edges.
 */
public class Transfer implements
        INodeTransfer<State, Context>,
        IEdgeTransfer<Context> {

    private static Logger log = Logger.getLogger(Transfer.class);

    private NodeTransfer js_node_transfer;

    private EdgeTransfer js_edge_transfer;

    /**
     * Constructs a new AbstractNodeTransfer object.
     */
    public Transfer() {
        js_node_transfer = new NodeTransfer();
        js_edge_transfer = new EdgeTransfer();
    }

    /**
     * Initializes the connection to the solver.
     */
    public void setSolverInterface(Solver.SolverInterface c) {
        js_node_transfer.setSolverInterface(c);
        js_edge_transfer.setSolverInterface(c);
    }

    @Override
    public void transfer(AbstractNode n) {
        n.visitBy(this);
    }

    @Override
    public void transferReturn(AbstractNode call_node, BasicBlock callee_entry,
                               Context caller_context, Context callee_context, Context edge_context, boolean implicit) {
        js_node_transfer.transferReturn(call_node, callee_entry, caller_context, callee_context, edge_context, implicit);
    }

    @Override
    public void visit(Node n) {
        n.visitBy(js_node_transfer);
        if (!(n instanceof CallNode) && !(n instanceof IfNode) && n.isRegistersDone()) // call and if nodes are treated elsewhere
            js_node_transfer.getSolverInterface().getState().clearOrdinaryRegisters();
    }

    @Override
    public Context transfer(BasicBlock src, BasicBlock dst) {
        Context res = js_edge_transfer.transfer(src, dst);
        if (res == null)
            if (log.isDebugEnabled())
                log.debug("Killing flow to block " + dst.getIndex());
        return res;
    }
}
