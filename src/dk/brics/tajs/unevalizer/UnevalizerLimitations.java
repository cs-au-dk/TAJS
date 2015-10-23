package dk.brics.tajs.unevalizer;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.util.AnalysisLimitationException;

/**
 * Utility class for allowing unevalizer limitations unsoundly when needed.
 */
public class UnevalizerLimitations {

    public static Value handle(String msg, AbstractNode node, Solver.SolverInterface c) {
        return handle(msg, node, Value.makeNone(), c);
    }

    public static Value handle(String msg, AbstractNode node, Value value, Solver.SolverInterface c) {
        if (Options.get().isUnsoundEnabled()) {
            c.getMonitoring().addMessage(node, Message.Severity.TAJS_ERROR, msg);
            return value;
        }
        throw new AnalysisLimitationException(node, msg);
    }
}
