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

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;

/**
 * Write property node.
 * <p>
 * <i>v</i><sub><i>base</i></sub>[<i>v</i><sub><i>property</i></sub>] = <i>v</i><br>
 * or<br>
 * <i>v</i><sub><i>base</i></sub>.<i>property</i> = <i>v</i><br>
 * <p>
 * Note that reading a property may overwrite the base register due to ToObject coercion.
 */
public class WritePropertyNode extends Node {

    private int base_reg;

    private int property_reg = NO_VALUE;

    private String property_str;

    private int value_reg;

    /**
     * Constructs a new write property node with variable property name.
     *
     * @param base_reg     The register for the base value.
     * @param property_reg The register for the property value.
     * @param value_reg    The register for the value to write.
     * @param location     The source location.
     */
    public WritePropertyNode(int base_reg, int property_reg, int value_reg, SourceLocation location) {
        super(location);
        this.base_reg = base_reg;
        this.property_reg = property_reg;
        this.value_reg = value_reg;
    }

    /**
     * Constructs a new write property node with fixed property name.
     *
     * @param base_reg     The register for the base value.
     * @param property_str The property string.
     * @param value_reg    The register holding the value to write.
     * @param location     The source location.
     */
    public WritePropertyNode(int base_reg, String property_str, int value_reg, SourceLocation location) {
        super(location);
        this.base_reg = base_reg;
        this.property_str = property_str;
        this.value_reg = value_reg;
    }

    /**
     * Returns the base register.
     */
    public int getBaseRegister() {
        return base_reg;
    }

    /**
     * Sets the base register.
     */
    public void setBaseRegister(int base_reg) {
        this.base_reg = base_reg;
    }

    /**
     * Returns the property register, or {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} if not applicable.
     */
    public int getPropertyRegister() {
        return property_reg;
    }

    /**
     * Set the property register.
     */
    public void setPropertyRegister(int property_reg) {
        this.property_reg = property_reg;
    }

    /**
     * Returns the property string, or null if not fixed.
     */
    public String getPropertyString() {
        return property_str;
    }

    /**
     * Returns true if the property is a fixed string.
     */
    public boolean isPropertyFixed() {
        return property_str != null;
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
        return "write-property[v" + base_reg
                + (property_str != null ? ",'" + property_str + "'" : ",v" + property_reg)
                + ",v" + value_reg + "]";
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
        if (base_reg == NO_VALUE)
            throw new AnalysisException("Base register is NO_VALUE:" + toString());
        if (property_reg == NO_VALUE && property_str == null)
            throw new AnalysisException("Both property register and property string are undefined: " + toString());
        if (value_reg == NO_VALUE)
            throw new AnalysisException("No real destination for write property node: " + toString());
    }
}
