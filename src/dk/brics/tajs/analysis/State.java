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

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.BlockState;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.solver.GenericSolver;

/**
 * Abstract state.
 */
public final class State extends BlockState<State,Context,CallEdge<State>> {

	/**
	 * Constructs a new abstract state.
	 */
	public State(GenericSolver<State,Context,CallEdge<State>,?,?>.SolverInterface c, BasicBlock block) {
		super(c, block);
	}
	
	private State(State x) {
		super(x);
	}
	
	@Override
	public State clone() {
		return new State(this);
	}

	@Override
	public String diff(State old) {
		return super.diff((BlockState<State,Context,CallEdge<State>>)old);
	}

	@Override
	public void remove(State other) {
		super.remove((BlockState<State,Context,CallEdge<State>>)other);
	}

	@Override
	public boolean propagate(State s, boolean funentry) {
		return super.propagate((BlockState<State,Context,CallEdge<State>>)s, funentry);
	}
}
