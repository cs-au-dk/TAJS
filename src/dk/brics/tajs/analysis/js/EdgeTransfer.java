/*
 * Copyright 2012 Aarhus University
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

import dk.brics.tajs.analysis.CallContext;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.IEdgeTransfer;

/**
 * Transfer for flow graph edges.
 */
public class EdgeTransfer implements IEdgeTransfer<State, CallContext> {

    /**
     * Constructs a new EdgeTransfer object.
     */
    public EdgeTransfer() {
    }

    @Override
    public boolean transfer(BasicBlock src, BasicBlock dst, State state) {

        // kill infeasible if-flow
        if (!Options.isLocalPathSensitivityDisabled()) {
            AbstractNode n = src.getLastNode();
            if (n instanceof IfNode) { // kill flow at if-nodes if a branch is infeasible
                IfNode ifnode = (IfNode) n;
                if (ifnode.getSuccTrue() != ifnode.getSuccFalse()) {
                    Value cond = state.readRegister(ifnode.getConditionRegister());
                    cond = UnknownValueResolver.getRealValue(cond, state);
                    Value boolcond = Conversion.toBoolean(cond);
                    boolean cond_definitely_true = boolcond.isMaybeTrueButNotFalse();
                    boolean cond_definitely_false = boolcond.isMaybeFalseButNotTrue();
                    if ((dst == ifnode.getSuccTrue() && cond_definitely_false) ||
                            (dst == ifnode.getSuccFalse() && cond_definitely_true))
                        return false;
                }
            }
        }

        return true;
    }
}
