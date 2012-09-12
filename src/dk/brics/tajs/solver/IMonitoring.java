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

import java.util.Collection;
import java.util.List;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;

/**
 * Records various information during fixpoint solving.
 */
public interface IMonitoring<BlockStateType extends IBlockState<BlockStateType, ?, ?>, CallContextType extends ICallContext<CallContextType>> {

	/**
	 * Registers a node transfer occurrence.
	 */
	void visitNodeTransfer(AbstractNode n);
	
	/**
	 * Registers a block transfer occurrence.
	 */
	void visitBlockTransfer();
	
	/**
	 * Registers new dataflow being propagated.
	 */
	void visitNewFlow(BasicBlock b, ICallContext<?> c, IBlockState<?, ?, ?> s, String diff, String info);
	
	/**
	 * Registers a recovery of an unknown value.
	 */
	void visitUnknownValueResolve();
	
	/**
	 * Registers a property recovery graph size.
	 */
	void visitRecoveryGraph(int size);
	
	/**
	 * Registers the given function in the scan phase. (Invoked once on each function.)
	 */
	void visitFunction(Function f, Collection<BlockStateType> entry_states);
	
	/**
	 * Registers a maybe reachable node.
	 */
	void visitReachableNode(AbstractNode n);

	/**
	 * Registers a state join operation.
	 */
	void visitJoin();
	
 	/**
	 * Initializes messages. Messages are not generated until this method is called.
	 */
	void beginScanPhase(FlowGraph fg);

 	/**
	 * Finalizes messages.
	 */
	List<Message> endScanPhase(FlowGraph fg);

	/**
	 * Returns the total number of node transfers.
	 */
	int getTotalNumberOfNodeTransfers();
}
