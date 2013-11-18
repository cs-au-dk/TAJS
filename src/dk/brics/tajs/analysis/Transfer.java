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

package dk.brics.tajs.analysis;

import org.apache.log4j.Logger;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
import dk.brics.tajs.solver.IBlockTransfer;
import dk.brics.tajs.solver.IEdgeTransfer;
import dk.brics.tajs.solver.INodeTransfer;

/**
 * Transfer for flow graph nodes and edges.
 */
public class Transfer implements 
INodeTransfer<State, Context>, 
IEdgeTransfer<State, Context>, 
IBlockTransfer<State,Context> {

	private static Logger logger = Logger.getLogger(Transfer.class); 

	private dk.brics.tajs.analysis.js.NodeTransfer js_node_transfer;

	private dk.brics.tajs.analysis.js.EdgeTransfer js_edge_transfer;
	
    /**
     * Constructs a new AbstractNodeTransfer object.
     */
    public Transfer() {
    	js_node_transfer = new dk.brics.tajs.analysis.js.NodeTransfer();
    	js_edge_transfer = new dk.brics.tajs.analysis.js.EdgeTransfer();
    }

    /**
     * Initializes the connection to the solver.
     */
    public void setSolverInterface(Solver.SolverInterface c) {
    	js_node_transfer.setSolverInterface(c);
    }

    @Override
    public void transfer(AbstractNode n, State state) {
        n.visitBy(this, state);
    }

	@Override
	public void transferReturn(AbstractNode call_node, BasicBlock callee_entry,
			Context caller_context, Context callee_context, Context edge_context) {
		js_node_transfer.transferReturn(call_node, callee_entry, caller_context, callee_context, edge_context);
	}

	@Override
	public void visit(Node n, State s) {
		n.visitBy(js_node_transfer, s);
		if (!(n instanceof CallNode) && n.isRegistersDone()) // call nodes are treated elsewhere
			s.clearOrdinaryRegisters();
	}

	@Override
	public void transfer(BasicBlock b, State s) {
		// do nothing
	}
	
    @Override
    public Context transfer(BasicBlock src, BasicBlock dst, State state) {
    	Context res = js_edge_transfer.transfer(src, dst, state);
    	if (res == null)
			if (logger.isDebugEnabled()) 
				logger.debug("Killing flow to block " + dst.getIndex());
    	return res;
    }
}
