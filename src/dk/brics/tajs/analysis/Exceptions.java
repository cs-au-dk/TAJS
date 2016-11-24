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

package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

/**
 * Models exceptions.
 */
public class Exceptions {

    private Exceptions() {
    }

    /**
     * Models a TypeError exception being thrown at the current node.
     * Does not modify the current state.
     * Don't forget to set the ordinary state to none if the exception will definitely occur.
     */
    public static void throwTypeError(Solver.SolverInterface c) {
        if (Options.get().isExceptionsDisabled())
            return;
        c.withState(c.getState().clone(), () ->
                throwException(c.getState(), makeException(InitialStateBuilder.TYPE_ERROR_PROTOTYPE, c), c, c.getNode()));
    }

    /**
     * Models a ReferenceError exception being thrown at the current node.
     * Does not modify the current state.
     * Don't forget to set the ordinary state to none if the exception will definitely occur.
     */
    public static void throwReferenceError(Solver.SolverInterface c) {
        if (Options.get().isExceptionsDisabled())
            return;
        c.withState(c.getState().clone(), () ->
                throwException(c.getState(), makeException(InitialStateBuilder.REFERENCE_ERROR_PROTOTYPE, c), c, c.getNode()));
    }

    /**
     * Models a RangeError exception being thrown at the current node.
     * Does not modify the current state.
     * Don't forget to set the ordinary state to none if the exception will definitely occur.
     */
    public static void throwRangeError(Solver.SolverInterface c) {
        if (Options.get().isExceptionsDisabled())
            return;
        c.withState(c.getState().clone(), () ->
                throwException(c.getState(), makeException(InitialStateBuilder.RANGE_ERROR_PROTOTYPE, c), c, c.getNode()));
    }

    /**
     * Models a SyntaxError exception being thrown at the current node.
     * Does not modify the current state.
     * Don't forget to set the ordinary state to none if the exception will definitely occur.
     */
    public static void throwSyntaxError(Solver.SolverInterface c) {
        if (Options.get().isExceptionsDisabled())
            return;
        c.withState(c.getState().clone(), () ->
                throwException(c.getState(), makeException(InitialStateBuilder.SYNTAX_ERROR_PROTOTYPE, c), c, c.getNode()));
    }

    /**
     * Constructs an exception value.
     * Does not modify the given state.
     */
    private static Value makeException(ObjectLabel prototype, Solver.SolverInterface c) {
        ObjectLabel ex = new ObjectLabel(c.getNode(), Kind.ERROR);
        c.getState().newObject(ex);
        c.getState().writeInternalPrototype(ex, Value.makeObject(prototype));
        c.getAnalysis().getPropVarOperations().writeProperty(ex, "message", Value.makeAnyStr());
        return Value.makeObject(ex);
    }

    /**
     * Models an exception being thrown at the given node.
     * Modifies the given state.
     * Don't forget to set the state to none if the exception will definitely occur.
     */
    public static void throwException(State state, Value v, Solver.SolverInterface c, AbstractNode source) {
        if (!source.canThrowExceptions())
            throw new AnalysisException("Exception at non-throwing-exception node!?");
        if (v.isMaybePresent() || Options.get().isPropagateDeadFlow()) {
            BasicBlock handlerblock = source.getBlock().getExceptionHandler();
            state.writeRegister(AbstractNode.EXCEPTION_REG, v);
            c.propagateToBasicBlock(state, handlerblock, state.getContext());
        }
    }
}
