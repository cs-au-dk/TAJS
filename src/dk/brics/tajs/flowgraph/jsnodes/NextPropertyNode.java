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
 * Next-property node.
 * <p>
 * for (<i>v</i><sub><i>property</i></sub> in <i>v</i><sub><i>object</i></sub>) { ... }
 */
public class NextPropertyNode extends Node {

    private int propertylist_reg;

    private int property_reg;

    /**
     * Constructs a new next-property node.
     */
    public NextPropertyNode(int propertylist_reg, int property_reg, SourceLocation location) {
        super(location);
        this.propertylist_reg = propertylist_reg;
        this.property_reg = property_reg;
    }

    /**
     * Returns the property queue register.
     */
    public int getPropertyListRegister() {
        return propertylist_reg;
    }

    /**
     * Returns the property register.
     */
    public int getPropertyRegister() {
        return property_reg;
    }

    /**
     * Sets the property register.
     */
    public void setPropertyRegister(int property_reg) {
        this.property_reg = property_reg;
    }

    @Override
    public String toString() {
        return "next-property[v" + propertylist_reg + ",v" + property_reg + "]";
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean canThrowExceptions() {
        return false;
    }

    @Override
    public void check(BasicBlock b) {
        if (propertylist_reg == NO_VALUE)
            throw new AnalysisException("Invalid propertylist register: " + toString());
    }
}
