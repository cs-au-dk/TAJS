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

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;

/**
 * Begin-with node.
 * <p>
 * with(<i>v</i>) { ... }
 */
public class BeginWithNode extends Node {

    private int object_reg;

    /**
     * Constructs a new begin-with node.
     */
    public BeginWithNode(int object_reg, SourceLocation location) {
        super(location);
        this.object_reg = object_reg;
    }

    /**
     * Returns the object register.
     */
    public int getObjectRegister() {
        return object_reg;
    }

    @Override
    public String toString() {
        return "begin-with[v" + object_reg + "]";
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean canThrowExceptions() {
        return true;
    }

    @Override
    public void check(BasicBlock b) {
        if (object_reg == NO_VALUE)
            throw new AnalysisException("Invalid object register: " + toString());
    }
}
