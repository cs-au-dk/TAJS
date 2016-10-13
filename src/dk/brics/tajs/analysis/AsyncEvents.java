package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;

import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;

public class AsyncEvents {

    private static AsyncEvents instance;

    private final String maySetKey = EventDispatcherNode.Type.ASYNC.name();

    private AsyncEvents() {
        if (!Options.get().isAsyncEventsEnabled()) {
            throw new AnalysisException("Async events are not enabled");
        }
    }

    public static AsyncEvents get() {
        if (instance == null) {
            instance = new AsyncEvents();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public void listen(AbstractNode node, Value handler, Solver.SolverInterface c) {
        State state = c.getState();
        handler = UnknownValueResolver.getRealValue(handler, state);
        Set<ObjectLabel> objectLabels = Conversion.toObjectLabels(node, handler, c);
        state.getExtras().addToMaySet(maySetKey, objectLabels);
    }

    public void emit(EventDispatcherNode n, Solver.SolverInterface c) {
        if (n.getType() != EventDispatcherNode.Type.ASYNC) {
            return;
        }

        State state = c.getState();
        Set<ObjectLabel> handlers = state.getExtras().getFromMaySet(maySetKey);
        List<Value> args = newList();

        State callState = state.clone();
        c.setState(callState);
        FunctionCalls.callFunction(new FunctionCalls.EventHandlerCall(n, Value.makeObject(handlers), args, Collections.singleton(InitialStateBuilder.GLOBAL), callState), c);
        c.setState(state);
    }
}
