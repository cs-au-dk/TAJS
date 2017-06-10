/*
 * Copyright 2009-2017 Aarhus University
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

package dk.brics.tajs.analysis.js;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.IEdgeTransfer;

/**
 * Transfer for flow graph edges.
 */
public class EdgeTransfer implements IEdgeTransfer<State, Context> {

    /**
     * Constructs a new EdgeTransfer object.
     */
    public EdgeTransfer() {
    }

    @Override
    public Context transfer(BasicBlock src, BasicBlock dst, State state) {
        AbstractNode n = src.getLastNode();

        if (n instanceof CallNode || n instanceof EventDispatcherNode) // these nodes have no ordinary flow along basic block edges
            return null;

        if (!Options.get().isControlSensitivityDisabled()) {
            if (n instanceof IfNode) {
                IfNode ifnode = (IfNode) n;
                if (ifnode.getSuccTrue() != ifnode.getSuccFalse()) {
                    Value cond = state.readRegister(ifnode.getConditionRegister());
                    cond = UnknownValueResolver.getRealValue(cond, state);
                    // restrict the conditional register
                    if (ifnode.getSuccTrue() == dst) {
                        // at true branch, cond must be truthy
                        cond = cond.restrictToTruthy();
                    } else {
                        // at false branch, cond must be falsy
                        cond = cond.restrictToFalsy();
                    }
                    if (cond.isNone()) {
                        // branch is infeasible, kill flow entirely
                        return null;
                    }
                    // store the restricted register (may be used later for && or ||)
                    state.writeRegister(ifnode.getConditionRegister(), cond);
                }
            }
        }

        if (n instanceof IfNode && n.isRegistersDone()) {
            state.clearOrdinaryRegisters();
        }

        return state.getContext();
    }
}
