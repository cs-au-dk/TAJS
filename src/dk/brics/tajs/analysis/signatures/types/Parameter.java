package dk.brics.tajs.analysis.signatures.types;

/**
 * A description of a parameter in a signature.
 */
public interface Parameter {

    ValueDescription getValueDescription();

    boolean isMandatory();
}
