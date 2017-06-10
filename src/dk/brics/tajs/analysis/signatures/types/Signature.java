package dk.brics.tajs.analysis.signatures.types;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.HostObject;

/**
 * Function signature interface.
 */
public interface Signature {

    /**
     * Checks if the function is invoked with appropriate arguments, also performs related coercions of arguments and propagates type-errors if they have the wrong type.
     *
     * @return true iff the native function should be evaluated.
     */
    boolean shouldStopPropagation(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c);

    /**
     * @return the length property of the function
     */
    int getParametersLength();
}
