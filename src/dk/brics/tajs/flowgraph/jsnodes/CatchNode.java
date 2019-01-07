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
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;

/**
 * Catch node.
 * <p>
 * catch(<i>x</i>)
 * <p>
 * Must be the first node in its block.
 */
public class CatchNode extends Node {

    private String varname;

    private int value_reg = NO_VALUE;

    private int scopeobj_reg = NO_VALUE;

    /**
     * Constructs a new catch node where the exception is stored in a program variable.
     *
     * @param varname      The variable name.
     * @param scopeobj_reg The object to be added to the scope chain when entering the catch block.
     * @param location     The source location.
     */
    public CatchNode(String varname, int scopeobj_reg, SourceLocation location) {
        super(location);
        this.varname = varname;
        this.scopeobj_reg = scopeobj_reg;
    }

    /**
     * Construct a new catch node where the exception is stored in a register.
     * (Used for finally blocks.)
     *
     * @param value_reg Register with the exception value.
     * @param location  The source location.
     */
    public CatchNode(int value_reg, SourceLocation location) {
        super(location);
        this.value_reg = value_reg;
    }

    /**
     * Returns the variable name, or null if not using a program variable.
     */
    public String getVariableName() {
        return varname;
    }

    /**
     * Returns the object to be added to the scope chain when entering the catch block,
     * or {@link AbstractNode#NO_VALUE} if not using a program variable.
     */
    public int getScopeObjRegister() {
        return scopeobj_reg;
    }

    /**
     * Returns the result register,
     * or {@link AbstractNode#NO_VALUE} if not using a register.
     */
    public int getValueRegister() {
        return value_reg;
    }

    @Override
    public String toString() {
        if (varname != null)
            return "catch[" + Strings.escape(varname) + ",v" + scopeobj_reg + "]";
        else
            return "catch[v" + value_reg + "]";
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
        if (varname == null && value_reg == NO_VALUE)
            throw new AnalysisException("Both varname and value register are undefined: " + toString());
    }
}
