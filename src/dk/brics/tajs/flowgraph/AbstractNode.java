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

package dk.brics.tajs.flowgraph;

/**
 * Abstract base class for all nodes.
 */
public abstract class AbstractNode implements Cloneable {

    /**
     * Register number used for absent values.
     */
    public static final int NO_VALUE = -1;

    /**
     * Register number used for exception values.
     */
    public static final int EXCEPTION_REG = 0;

    /**
     * Register number used for return values.
     */
    public static final int RETURN_REG = 1;

    /**
     * First register number used for ordinary values.
     */
    public static final int FIRST_ORDINARY_REG = 2;

    /**
     * Unique index of this node in the flow graph, or -1 if not belonging to a flow graph.
     */
    private int index = -1;

    /**
     * Source location.
     */
    private SourceLocation location;

    /**
     * Basic block containing this node.
     */
    private BasicBlock block;

    /**
     * Artificial code flag.
     * If set, this node is an artifact and should not appear in analysis messages.
     */
    private boolean artificial;

    /**
     * If non-null, this node is a duplicate of the other one (used e.g. for duplication of finally blocks).
     */
    private AbstractNode duplicate_of;

    /**
     * If set, all ordinary registers can be considered dead after this node.
     */
    private boolean registers_done;

    /**
     * Constructs a new node.
     */
    protected AbstractNode(SourceLocation location) {
        this.location = location;
        index = -1;
    }

    /**
     * Marks that this node is a duplicate of the given node.
     */
    public void setDuplicateOf(AbstractNode other) {
        duplicate_of = other;
    }

    /**
     * Returns the node that this node is a duplicate of, or null if this is not a duplicate.
     */
    public AbstractNode getDuplicateOf() {
        return duplicate_of;
    }

    /**
     * Sets the node index.
     * Called when the node is added to a flow graph. Should not be changed subsequently.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Sets the basic block containing this node.
     */
    public void setBlock(BasicBlock block) {
        this.block = block;
    }

    /**
     * Returns the node index.
     * The node index uniquely identifies the node within the flow graph.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the source location.
     */
    public SourceLocation getSourceLocation() {
        return location;
    }

    /**
     * Sets the source location.
     */
    public void setSourceLocation(SourceLocation loc) {
        location = loc;
    }

    /**
     * Returns the block containing this node.
     */
    public BasicBlock getBlock() {
        return block;
    }

    /**
     * Returns a string description of this node.
     */
    @Override
    abstract public String toString();

    /**
     * Returns true if this node may throw exceptions.
     */
    abstract public boolean canThrowExceptions();

    /**
     * Returns true if this node is an artifact and should not appear in analysis messages.
     */
    public boolean isArtificial() {
        return artificial;
    }

    /**
     * Marks this operation as an artifact that should not appear in analysis messages.
     */
    public void setArtificial() {
        artificial = true;
    }

    /**
     * Visits this node with the given visitor.
     */
    abstract public <ArgType> void visitBy(AbstractNodeVisitor<ArgType> v, ArgType a);

    /**
     * Perform a consistency check of this node.
     * Default implementation does nothing.
     *
     * @param b basic block containing this node
     */
    public void check(BasicBlock b) { // TODO: override check() for more node classes?
        // do nothing
    }

    /**
     * Returns the registers done flag.
     *
     * @return true if all ordinary registers can be considered dead at the end of this block
     */
    public boolean isRegistersDone() {
        return registers_done;
    }

    /**
     * Sets the registers done flag.
     *
     * @param registers_done true if all ordinary registers can be considered dead at the end of this block
     */
    public void setRegistersDone(boolean registers_done) {
        this.registers_done = registers_done;
    }

    /**
     * Performs a shallow clone of the node.
     *
     * @return a shallow clone
     */
    @Override
    public AbstractNode clone() {
        try {
            return (AbstractNode) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
