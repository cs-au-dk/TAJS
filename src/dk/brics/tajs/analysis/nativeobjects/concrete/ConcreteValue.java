package dk.brics.tajs.analysis.nativeobjects.concrete;

/**
 * A concrete value. Mostly intended as a wrapper class to preserve some type-sanity.
 */
public interface ConcreteValue {

    /**
     * Converts this value to its source code representative.
     */
    String toSourceCode();

    <T> T accept(ConcreteValueVisitor<T> v);
}
