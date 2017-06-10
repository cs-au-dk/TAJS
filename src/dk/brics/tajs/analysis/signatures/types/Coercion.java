package dk.brics.tajs.analysis.signatures.types;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.Value;

/**
 * A coercion.
 */
public interface Coercion {

    /**
     * Coerces a value (for side-effects and messages).
     *
     * @return true iff the coercion could not be completed at this moment (awaiting dataflow from an implicit call)
     */
    boolean coerce(Value v, Solver.SolverInterface c);
}
