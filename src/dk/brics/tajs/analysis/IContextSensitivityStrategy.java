package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.EndLoopNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

/**
 * Strategy-pattern interface for context sensitivity strategies.
 */
public interface IContextSensitivityStrategy {

    /**
     * Constructs a heap context for a function object.
     */
    HeapContext makeFunctionHeapContext(Function fun, State state, Solver.SolverInterface c);

    /**
     * Constructs a heap context for objects related to a call.
     */
    HeapContext makeActivationAndArgumentsHeapContext(State state, ObjectLabel function, FunctionCalls.CallInfo callInfo, Solver.SolverInterface c);

    /**
     * Constructs a heap context for an object literal.
     */
    HeapContext makeObjectLiteralHeapContext(AbstractNode node, State state);

    /**
     * Constructs the initial context.
     */
    Context makeInitialContext();

    /**
     * Constructs a context for call.
     */
    Context makeFunctionEntryContext(State state, ObjectLabel function, FunctionCalls.CallInfo callInfo, Solver.SolverInterface c);

    /**
     * Constructs a context for entering a for-in body.
     */
    Context makeForInEntryContext(Context currentContext, BeginForInNode n, Value v);

    /**
     * Constructs a context for (re-)entering a loop.
     */
    Context makeNextLoopUnrollingContext(Context currentContext, BeginLoopNode node);

    /**
     * Constructs a context for leaving a loop.
     */
    Context makeLoopExitContext(Context currentContext, EndLoopNode node);

    /**
     * Requests that a parameter is treated context sensitively.
     */
    void requestContextSensitiveParameter(Function function, String parameter);
}
