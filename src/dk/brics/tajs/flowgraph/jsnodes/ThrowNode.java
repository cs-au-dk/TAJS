/*
 * Copyright 2009-2015 Aarhus University
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
 * Throw node.
 * <p>
 * throw <i>v</i>
 * <p>
 * Must be the last node in its block.
 */
public class ThrowNode extends Node {

    private int value_reg;

    /**
     * Constructs a new throw node.
     */
    public ThrowNode(int value_reg, SourceLocation location) {
        super(location);
        this.value_reg = value_reg;
    }

    /**
     * Returns the value register.
     */
    public int getValueRegister() {
        return value_reg;
    }

    /**
     * Sets the value register.
     */
    public void setValueRegister(int value_reg) {
        this.value_reg = value_reg;
    }

    @Override
    public String toString() {
        return "throw[v" + value_reg + "]";
    }

    @Override
    public <ArgType> void visitBy(NodeVisitor<ArgType> v, ArgType a) {
        v.visit(this, a);
    }

    @Override
    public boolean canThrowExceptions() {
        return true;
    }
}
