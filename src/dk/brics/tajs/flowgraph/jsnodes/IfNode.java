/*
 * Copyright 2009-2017 Aarhus University
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

import java.util.Collection;

/**
 * If node.
 * <p>
 * if (<i>v</i><sub><i>condition</i></sub>) {...} else {...}
 * <p>
 * Must be the last node in its block.
 */
public class IfNode extends Node {

    private int condition_reg;

    private BasicBlock succ_true;

    private BasicBlock succ_false;

    /**
     * Constructs a new if node.
     *
     * @param condition_reg The condition register.
     * @param location      The source location.
     */
    public IfNode(int condition_reg, SourceLocation location) {
        super(location);
        this.condition_reg = condition_reg;
    }

    /**
     * Sets the true/false successors.
     *
     * @param succ_true  The basic block if the condition is true.
     * @param succ_false The basic block if the condition is false.
     */
    public void setSuccessors(BasicBlock succ_true, BasicBlock succ_false) {
        this.succ_true = succ_true;
        this.succ_false = succ_false;
    }

    /**
     * Returns the condition register.
     */
    public int getConditionRegister() {
        return condition_reg;
    }

    /**
     * Returns the 'true' successor.
     */
    public BasicBlock getSuccTrue() {
        return succ_true;
    }

    /**
     * Returns the 'false' successor.
     */
    public BasicBlock getSuccFalse() {
        return succ_false;
    }

    @Override
    public String toString() {
        return "if[v" + condition_reg + "](true-block:" + succ_true.getIndex() + ",false-block:" + succ_false.getIndex() + ")";
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
        if (condition_reg == NO_VALUE)
            throw new AnalysisException("Invalid condition register: " + toString());
        if (this != b.getLastNode())
            throw new AnalysisException("If node not at the end of the block: " + b);
        Collection<BasicBlock> successors = b.getSuccessors();
        if (!(successors.contains(succ_false) && successors.contains(succ_true)))
            throw new AnalysisException("Strange successors for if node: " + b);
    }
}
