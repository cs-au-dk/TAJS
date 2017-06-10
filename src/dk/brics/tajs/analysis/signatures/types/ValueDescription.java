package dk.brics.tajs.analysis.signatures.types;

import java.util.Optional;

/**
 * A description of a value in a signature.
 */
public interface ValueDescription {

    Optional<Coercion> getCoercion();

    Optional<Requirement> getRequirement();
}
