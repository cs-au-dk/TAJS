/*
 * Copyright 2009-2020 Aarhus University
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
import dk.brics.tajs.util.Strings;

/**
 * Write variable node.
 * <p>
 * <i>x</i> = <i>v</i>
 */
public class WriteVariableNode extends Node {

    private String varname;

    private int value_reg;

    /**
     * Constructs a new write variable node.
     */
    public WriteVariableNode(int value_reg, String varname, SourceLocation location) {
        super(location);
        this.value_reg = value_reg;
        this.varname = varname;
    }

    /**
     * Returns the destination variable name.
     */
    public String getVariableName() {
        return varname;
    }

    /**
     * Returns the register.
     */
    public int getValueRegister() {
        return value_reg;
    }

    /**
     * Sets the register.
     */
    public void setRegister(int value_reg) {
        this.value_reg = value_reg;
    }

    @Override
    public String toString() {
        return "write-variable[v" + value_reg + ",'" + Strings.escape(varname) + "']";
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
        if (value_reg == NO_VALUE)
            throw new AnalysisException("Invalid source register: " + this);
        if (varname == null || varname.isEmpty())
            throw new AnalysisException("Variable name is null: " + this);
    }
}
