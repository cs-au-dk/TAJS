/*
 * Copyright 2009-2018 Aarhus University
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

package dk.brics.tajs.flowgraph.jsnodes;

import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Abstract base class for nodes that are assignments to registers.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = ...
 * <p>
 * Registers must be defined before used on all intra-procedural paths.
 */
public abstract class LoadNode extends Node {

    private int result_reg;

    /**
     * Constructs a new load node.
     *
     * @param result_reg The register to assign to.
     * @param location   The source location.
     */
    public LoadNode(int result_reg, SourceLocation location) {
        super(location);
        this.result_reg = result_reg;
    }

    /**
     * Returns the result register, or {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} if not applicable.
     */
    public int getResultRegister() {
        return result_reg;
    }
}
