/*
 * Copyright 2009-2016 Aarhus University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

/**
 * Processing of asynchronous events (that do not involve HTML DOM).
 */
public class AsyncEvents {

    private static final String maySetKey = EventDispatcherNode.Type.ASYNC.name();

    private AsyncEvents() {}

    public static void listen(AbstractNode node, Value handler, Solver.SolverInterface c) {
        State state = c.getState();
        handler = UnknownValueResolver.getRealValue(handler, state);
        Set<ObjectLabel> objectLabels = Conversion.toObjectLabels(node, handler, c);
        state.getExtras().addToMaySet(maySetKey, objectLabels);
    }

    public static void emit(EventDispatcherNode n, Solver.SolverInterface c) {
        if (n.getType() != EventDispatcherNode.Type.ASYNC) {
            return;
        }

        State state = c.getState();
        Set<ObjectLabel> handlers = state.getExtras().getFromMaySet(maySetKey);
        List<Value> args = newList();

        State callState = state.clone();
        c.withState(callState, () ->
                FunctionCalls.callFunction(new FunctionCalls.EventHandlerCall(n, Value.makeObject(handlers), args, Collections.singleton(InitialStateBuilder.GLOBAL), callState), c));
    }
}
