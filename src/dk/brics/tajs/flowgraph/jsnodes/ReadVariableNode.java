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
import dk.brics.tajs.util.Strings;

/**
 * Read variable node.
 * <p>
 * <i>v</i><sub><i>result</i></sub> = <i>x</i>
 */
public class ReadVariableNode extends LoadNode {

    private String varname;

    private int result_base_reg;

    private boolean keep_absent; // used for 'typeof variable'

    /**
     * Constructs a new read variable node.
     *
     * @param varname         The name of the variable.
     * @param result_reg      The register for the result.
     * @param result_base_reg The register for the base variable.
     * @param keep_absent     If true, do not model exception if variable is absent.
     * @param location        The source location.
     */
    public ReadVariableNode(String varname, int result_reg, int result_base_reg, boolean keep_absent, SourceLocation location) {
        super(result_reg, location);
        this.varname = varname;
        this.result_base_reg = result_base_reg;
        this.keep_absent = keep_absent;

    }

    /**
     * Returns the source variable name.
     */
    public String getVariableName() {
        return varname;
    }

    /**
     * Returns the result base register.
     * {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} means 'absent', which is used for "this".
     */
    public int getResultBaseRegister() {
        return result_base_reg;
    }

    /**
     * Returns false if this is an ordinary read (throws exception if the variable is absent),
     * returns false if keep absent without modeling exception.
     */
    public boolean isKeepAbsent() {
        return keep_absent;
    }

    @Override
    public String toString() {
        return "read-variable['" + Strings.escape(varname) + "'," +
                (getResultRegister() == NO_VALUE ? "-" : ("v" + getResultRegister())) + "," +
                (result_base_reg != NO_VALUE ? "v" + result_base_reg : "-") + (keep_absent ? ",KEEP" : "") + "]";
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
        if (varname == null)
            throw new AnalysisException("Null variable name: " + this);
    }
}
