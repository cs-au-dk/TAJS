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

package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

/**
 * Models exceptions.
 */
public class Exceptions {

	private Exceptions() {}
	
	/**
	 * Models a TypeError exception being thrown at the current node.
	 * Does not modify the given state.
	 * Don't forget to set the ordinary state to none if the exception will definitely occur.
	 */
	public static void throwTypeError(State state, Solver.SolverInterface c) {
		if (Options.isExceptionsDisabled())
			return;
		state = state.clone();
		throwException(state, makeException(state, InitialStateBuilder.TYPE_ERROR_PROTOTYPE, c), c, c.getCurrentNode(), c.getCurrentContext());
	}
	
	/**
	 * Models a ReferenceError exception being thrown at the current node.
	 * Does not modify the given state.
	 * Don't forget to set the ordinary state to none if the exception will definitely occur.
	 */
	public static void throwReferenceError(State state, Solver.SolverInterface c) {
		if (Options.isExceptionsDisabled())
			return;
		state = state.clone();
		throwException(state, makeException(state, InitialStateBuilder.REFERENCE_ERROR_PROTOTYPE, c), c, c.getCurrentNode(), c.getCurrentContext());
	}
	
	/**
	 * Models a RangeError exception being thrown at the current node.
	 * Does not modify the given state.
	 * Don't forget to set the ordinary state to none if the exception will definitely occur.
	 */
	public static void throwRangeError(State state, Solver.SolverInterface c) {
		if (Options.isExceptionsDisabled())
			return;
		state = state.clone();
		throwException(state, makeException(state, InitialStateBuilder.RANGE_ERROR_PROTOTYPE, c), c, c.getCurrentNode(), c.getCurrentContext());
	}
	
	/**
	 * Models a SyntaxError exception being thrown at the current node.
	 * Does not modify the given state.
	 * Don't forget to set the ordinary state to none if the exception will definitely occur.
	 */
	public static void throwSyntaxError(State state, Solver.SolverInterface c) {
		if (Options.isExceptionsDisabled())
			return;
		state = state.clone();
		throwException(state, makeException(state, InitialStateBuilder.SYNTAX_ERROR_PROTOTYPE, c), c, c.getCurrentNode(), c.getCurrentContext());
	}
	
	/**
	 * Constructs an exception value.
	 * Does not modify the given state.
	 */
	private static Value makeException(State state, ObjectLabel prototype, Solver.SolverInterface c) {
		ObjectLabel ex = new ObjectLabel(c.getCurrentNode(), Kind.ERROR);
		state.newObject(ex);
		state.writeInternalPrototype(ex, Value.makeObject(prototype));
		state.writeProperty(ex, "message", Value.makeAnyStr());
		return Value.makeObject(ex);
	}
	
	/**
	 * Models an exception being thrown at the given node.
	 * Modifies the given state.
	 * Don't forget to set the state to none if the exception will definitely occur.
	 */
	public static void throwException(State state, Value v, Solver.SolverInterface c, AbstractNode source, CallContext context) {
		if (!source.canThrowExceptions())
			throw new AnalysisException("Exception at non-throwing-exception node!?");
		if (v.isMaybePresent() || Options.isPropagateDeadFlow()) {
			BasicBlock handlerblock = source.getBlock().getExceptionHandler();
			state.writeRegister(AbstractNode.EXCEPTION_REG, v);
			c.propagateToBasicBlock(state, handlerblock, context);
		}
	}
}
