package dk.brics.tajs.analysis.signatures.types;

import dk.brics.tajs.lattice.Value;

/**
 * A requirement.
 */
public interface Requirement {

    /**
     * @return false iff the requirement is definitely not satisfied.
     */
    boolean maybeSatisfied(Value v);

    /**
     * @return false iff the requirement is definitely satisfied.
     */
    boolean maybeNotSatisfied(Value v);
}
