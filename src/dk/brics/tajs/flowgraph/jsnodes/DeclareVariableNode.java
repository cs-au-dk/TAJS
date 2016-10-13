/*
 * Copyright 2009-2016 Aarhus University
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
 * Variable declaration node.
 * <p>
 * var <i>x</i>
 */
public class DeclareVariableNode extends Node {

    private String varname;

    /**
     * Constructs a new variable declaration node.
     */
    public DeclareVariableNode(String varname, SourceLocation location) {
        super(location);
        this.varname = varname;
    }

    /**
     * Returns the variable name.
     */
    public String getVariableName() {
        return varname;
    }

    @Override
    public String toString() {
        return "vardecl['" + Strings.escape(varname) + "']";
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
        if (varname == null || varname.isEmpty())
            throw new AnalysisException("Empty variable name:" + toString());
    }
}
