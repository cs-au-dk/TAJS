package dk.brics.tajs.lattice;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.options.Options;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static dk.brics.tajs.util.Collections.newList;

/**
 * Parallel transfer functions.
 * Provides functionality for applying multiple transfer functions to the current state and joining the results.
 */
public class ParallelTransfer {

    /**
     * State transfer function.
     * Operates on the current state.
     */
    @FunctionalInterface
    public interface Transfer {
        void call();
    }

    private Solver.SolverInterface c;

    private Collection<Transfer> functions;

    /**
     * Constructs a new parallel transfer function.
     */
    public ParallelTransfer(Solver.SolverInterface c) {
        this.c = c;
        functions = newList();
    }

    /**
     * Adds a transfer function.
     */
    public void add(Transfer f) {
        functions.add(f);
    }

    /**
     * Adds a collection of transfer functions that take a parameter.
     */
    public <T> void addAll(Collection<T> ts, Consumer<T> f) {
        for (T t : ts)
            add(() -> f.accept(t));
    }

    /**
     * Applies the transfer functions in parallel on the current state and joins the results.
     */
    public void complete() {
        State state = c.getState();
        if (functions.isEmpty()) {
            if (!Options.get().isPropagateDeadFlow()) {
                state.setToNone();
            }
        } else if (functions.size() == 1) { // special case of last else case
            functions.iterator().next().call();
        } else { // functions.size() > 1
            List<State> results_except_last = newList();
            Iterator<Transfer> it = functions.iterator();
            while (it.hasNext()) {
                Transfer t = it.next();
                boolean last = !it.hasNext();
                State s = null;
                if (!last) {
                    s = state.clone();
                    c.setState(s);
                }
                t.call();
                if (!last) {
                    results_except_last.add(s);
                    c.setState(state);
                }
            }
            for (State s : results_except_last) {
                state.propagate(s, false);
            }
        }
    }

    /**
     * Applies the transfer function on each of the given elements, in parallel on the current state, and joins the results.
     */
    public static <T> void process(Collection<T> ts, Consumer<T> f, Solver.SolverInterface c) {
        ParallelTransfer pt = new ParallelTransfer(c);
        pt.addAll(ts, f);
        pt.complete();
    }
}
