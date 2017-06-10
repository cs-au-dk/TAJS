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
package dk.brics.tajs.lattice;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.solver.ISolverMonitoring;

/**
 * Monitoring interface.
 * <p>
 * This interface contains callbacks for solver-specific and lattice-specific operations.
 */
public interface ILatticeMonitoring extends ISolverMonitoring<State, Context> {

    /**
     * Invoked when an unknown value is resolved.
     */
    void visitUnknownValueResolve(AbstractNode node, boolean partial, boolean scanning);

    /**
     * Invoked when a recovery graph has been built.
     */
    void visitRecoveryGraph(AbstractNode node, int size);

    /**
     * Invoked when a state join operation is performed.
     */
    void visitJoin();

    /**
     * Invoked when a new abstract object is allocated.
     */
    void visitNewObject(AbstractNode node, ObjectLabel label, State s);

    /**
     * Invoked when an abstract object changes name (from singleton to summary for recency abstraction).
     */
    void visitRenameObject(AbstractNode node, ObjectLabel from, ObjectLabel to, State s);
}
