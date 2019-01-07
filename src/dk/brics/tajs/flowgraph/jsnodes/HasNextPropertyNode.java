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

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;

/**
 * Has-next-property node.
 * <p>
 * for (<i>v</i><sub><i>property</i></sub> in <i>v</i><sub><i>object</i></sub>) { ... }
 */
public class HasNextPropertyNode extends LoadNode {

    private int propertylist_reg;

    /**
     * Constructs a new has-next-property node.
     */
    public HasNextPropertyNode(int propertylist_reg, int result_reg, SourceLocation location) {
        super(result_reg, location);
        this.propertylist_reg = propertylist_reg;
    }

    /**
     * Returns the property list register.
     */
    public int getPropertyListRegister() {
        return propertylist_reg;
    }

    @Override
    public boolean canThrowExceptions() {
        return false;
    }

    @Override
    public String toString() {
        return "has-next[v" + propertylist_reg + ",v" + getResultRegister() + "]";
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public void check(BasicBlock b) {
        if (propertylist_reg == NO_VALUE)
            throw new AnalysisException("Invalid propertylist register: " + toString());
        if (getResultRegister() == NO_VALUE)
            throw new AnalysisException("No result register for node: " + toString());
    }
}
