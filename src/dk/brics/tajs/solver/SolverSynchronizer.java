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
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;

/**
 * Synchronizer for solver events.
 */
abstract public class SolverSynchronizer {
	
	/**
	 * If set, single-stepping is enabled. 
	 */
	private boolean single_step;
	
	/**
	 * Constructs a new synchronizer.
	 */
	public SolverSynchronizer() {}
	
	/**
	 * Checks whether single-stepping is enabled.
	 */
	public synchronized boolean isSingleStep() {
		return single_step;
	}
	
	/**
	 * Enable/disable single-stepping.
	 */
	public synchronized void setSingleStep(boolean enable) {
		this.single_step = enable;
	}

	/**
	 * Waits for notification if single-stepping is enabled.
	 */
	synchronized void waitIfSingleStep() {
		if (single_step)
			try {
				waiting();
				wait(); // TODO: wrap in while loop to check condition
			} catch (InterruptedException e) {
				// ignore
			}
	}
	
	/**
	 * Sends a notification to run/single-step.
	 */
	public synchronized void notifyRunOrSingleStep() {
		notify();
	}
	
	/**
	 * Callback, invoked when initialing wait.
	 */
	abstract public void waiting();

	/**
	 * Callback, invoked when flow graph has been constructed.
	 */
    abstract public void setFlowGraph(FlowGraph g);

    /**
     * Callback, invoked when selecting a block for processing.
     */
    abstract public  void markActiveBlock(BasicBlock b);

    /**
     * Callback, invoked when a block is added to the worklist.
     */
    abstract public  void markPendingBlock(BasicBlock b);

    /**
     * Callback, invoked when a call edge is added.
     */
    abstract public void callEdgeAdded(Function source, Function target);
}
