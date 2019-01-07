/*
 * Copyright 2009-2019 Aarhus University
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

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Return node.
 * <p>
 * return [<i>v</i>]
 * <p>
 * Must be the only node in its block.
 */
public class ReturnNode extends Node {

    private int value_reg;

    /**
     * Constructs a new return node.
     * Variable number {@link AbstractNode#NO_VALUE} represents absent value (implicitly 'undefined').
     */
    public ReturnNode(int value_reg, SourceLocation location) {
        super(location);
        this.value_reg = value_reg;
    }

    /**
     * Returns the return value register.
     * Variable number {@link AbstractNode#NO_VALUE} represents absent value (implicitly 'undefined').
     */
    public int getReturnValueRegister() {
        return value_reg;
    }

    /**
     * Sets the return value register.
     */
    public void setReturnValueRegister(int value_var) {
        value_reg = value_var;
    }

    @Override
    public String toString() {
        return "return" + (value_reg != NO_VALUE ? "[v" + value_reg + "]" : "");
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean canThrowExceptions() {
        return false;
    }
}
