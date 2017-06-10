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

package dk.brics.tajs.solver;

import dk.brics.tajs.flowgraph.BasicBlock;

/**
 * Interface for edge transfer function classes.
 */
public interface IEdgeTransfer<StateType extends IState<StateType, ?, ?>,
        ContextType extends IContext<?>> {

    /**
     * Returns non-null context if flow should occur on the given edge and state.
     */
    ContextType transfer(BasicBlock src, BasicBlock dst, StateType state);
}
