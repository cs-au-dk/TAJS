package dk.brics.tajs.util;

import dk.brics.tajs.flowgraph.AbstractNode;

/**
 * Exception for analysis limitations.
 * <p>
 * Unlike {@link AnalysisException}, this exception does not indicate a bug.
 * It indicates critical lack of precision or missing modelling of a native function.
 */
public class AnalysisLimitationException extends RuntimeException {
    // TODO use this exception in more places

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception.
     * @param node as the node the limitation occurs at
     * @param msg  as the description of the limitation
     */
    public AnalysisLimitationException(AbstractNode node, String msg) {
        super(String.format("%s (%s): %s", node.getSourceLocation(), node.toString(), msg));
    }

    /**
     * Constructs a new exception.
     *
     * @param msg as the description of the limitation
     */
    public AnalysisLimitationException(String msg) {
        super(msg);
    }
}
