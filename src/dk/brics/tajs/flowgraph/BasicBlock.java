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

package dk.brics.tajs.flowgraph;

import dk.brics.tajs.flowgraph.jsnodes.ReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.ThrowNode;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Sequence of nodes.
 * Must be non-empty.
 * Has a unique entry node and proceeds through the sequence unless exceptions are thrown.
 */
public class BasicBlock implements Serializable {

    /**
     * Unique index of this block in the flow graph, or -1 if not belonging to a flow graph.
     */
    private int index = -1;

    /**
     * Block order, used for worklist prioritization.
     */
    private int order = -1;

    /**
     * The nodes in this block.
     */
    private List<AbstractNode> nodes;

    /**
     * Successors of this block. Not including successors of call nodes and exception edges.
     */
    private Collection<BasicBlock> successors;

    /**
     * The exception handler block for nodes in this block.
     */
    private BasicBlock exception_handler;

    /**
     * The function containing this block.
     */
    private Function function;

    /**
     * The first block of a collection of blocks that can have an intra-procedural context
     * If `this.entry_block == this`, then this block is an entry_block
     * The entry_block can be thought of as the entry block of a function.
     * This information is ultimately needed by {@link dk.brics.tajs.lattice.UnknownValueResolver}
     */
    private BasicBlock entry_block;

    /**
     * The predecessor block for an entry block.
     * Non-null iff this block is an entry block, i.e. `this.entry_block == this`
     */
    private BasicBlock entry_predecessor_block;

    /**
     * Constructs a new initially empty block of nodes.
     *
     * @param function The function this block belongs to.
     */
    public BasicBlock(Function function) {
        if (function == null)
            throw new NullPointerException();
        this.function = function;
        successors = newSet();
        nodes = newList();
    }

    /**
     * Adds a successor.
     */
    public void addSuccessor(BasicBlock succ) {
        if (succ == null)
            throw new NullPointerException();
        successors.add(succ);
    }

    /**
     * Remove a successor.
     */
    public void removeSuccessor(BasicBlock succ) {
        if (succ == null)
            throw new NullPointerException();
        if (!successors.contains(succ))
            throw new AnalysisException("The basic block is not a successor");
        successors.remove(succ);
    }

    /**
     * Returns the successors of this block.
     */
    public Collection<BasicBlock> getSuccessors() {
        return successors;
    }

    /**
     * Returns the single successor block.
     *
     * @throws AnalysisException if not exactly one successor
     */
    public BasicBlock getSingleSuccessor() {
        if (successors.size() != 1)
            throw new AnalysisException("Expected exactly one successor of basic block " + index);
        return successors.iterator().next();
    }

    /**
     * Sets the block order.
     */
    void setOrder(int order) {
        this.order = order;
    }

    /**
     * Returns the block order.
     */
    public int getOrder() {
        return order;
    }

    /**
     * Sets the block index.
     * Called when the flow graph block structure is updated.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Returns the block index, or -1 if not set.
     * The block index uniquely identifies the block within the flow graph.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Adds a node to this block.
     */
    public void addNode(AbstractNode n) {
        if (n == null)
            throw new NullPointerException("Adding null node to basic block");
        nodes.add(n);
        n.setBlock(this);
    }

    /**
     * Returns the sequence of nodes.
     */
    public List<AbstractNode> getNodes() {
        return nodes;
    }

    /**
     * Returns true if there are no nodes in the basic block.
     */
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    /**
     * Returns the first node.
     */
    public AbstractNode getFirstNode() {
        return nodes.get(0);
    }

    /**
     * Returns the last node.
     */
    public AbstractNode getLastNode() {
        return nodes.get(nodes.size() - 1);
    }

    /**
     * Checks whether this is a function entry block.
     */
    public boolean isEntry() {
        return function.getEntry() == this;
    }

    /**
     * Returns the function containing this block.
     */
    public Function getFunction() {
        return function;
    }

    /**
     * Returns the source location for the first node in the basic block.
     */
    public SourceLocation getSourceLocation() {
        return nodes.isEmpty() ? null : getFirstNode().getSourceLocation();
    }

    /**
     * Returns the exception handler block, or null if not set.
     */
    public BasicBlock getExceptionHandler() {
        return exception_handler;
    }

    /**
     * Sets the exception handler block.
     */
    public void setExceptionHandler(BasicBlock exception_handler) {
        this.exception_handler = exception_handler;
    }

    /**
     * Returns true if this block contains a node that may throw exceptions.
     */
    public boolean canThrowExceptions() {
        for (AbstractNode n : nodes)
            if (n.canThrowExceptions())
                return true;
        return false;
    }

    /**
     * Returns a string description of this block.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("block ").append(index).append(':');
        if (getFunction().getEntry() == this)
            s.append(" [entry]");
        if (getFunction().getOrdinaryExit() == this)
            s.append(" [exit-ordinary]");
        if (getFunction().getExceptionalExit() == this)
            s.append(" [exit-exceptional]");
        s.append('\n');
        for (AbstractNode n : getNodes()) {
            s.append("    node ").append(n.getIndex());
            AbstractNode d = n.getDuplicateOf();
            if (d != null)
                s.append("(~").append(d.getIndex()).append(")");
            s.append(": ").append(n);
            if (n.isRegistersDone())
                s.append('*');
            if (n.getImplicitAfterCall() != null)
                s.append(" [implicitAfterCall]");
            s.append(" (").append(n.getSourceLocation()).append(")\n");
        }
        s.append("    ->[");
        if (Options.get().isDebugOrTestEnabled()) {
            List<BasicBlock> ss = newList(successors);
            sort(ss);
            successors = ss;
        }
        boolean first = true;
        for (BasicBlock b : successors) {
            if (first)
                first = false;
            else
                s.append(',');
            s.append("block ").append(b.getIndex());
        }
        s.append("]");
        if (getExceptionHandler() != null && getExceptionHandler() != getFunction().getExceptionalExit()) {
            s.append(" ~>[block ").append(getExceptionHandler().getIndex()).append("]");
        }
        return s.toString();
    }

    private static void sort(List<BasicBlock> blocks) {
        blocks.sort(Comparator.comparingInt(BasicBlock::getIndex));
    }

    /**
     * Produces a Graphviz dot representation of this block.
     *
     * @param pw         Receiver of the dot representation of this basic block.
     * @param standalone Indicates that this is a complete graph that should be closed.
     */
    public void toDot(PrintWriter pw, boolean standalone) {
        if (standalone) {
            pw.println("digraph {");
            pw.println("rankdir=\"TD\"");
        }
        pw.print("BB" + index + " [shape=record label=\"{");
        boolean first = true;
        for (AbstractNode n : getNodes()) {
            if (first)
                first = false;
            else
                pw.print('|');
            pw.print(n.getIndex());
            AbstractNode d = n.getDuplicateOf();
            if (d != null)
                pw.print("(~" + d.getIndex() + ")");
            pw.print(": " + Strings.escape(n.toString()));
            if (n.isRegistersDone())
                pw.print('*');
        }
        pw.print("}\" ] " + "\n");
        if (standalone) {
            pw.println("}");
            pw.close();
        }
    }

    /**
     * Perform a consistency check of the basic block.
     */
    public void check(BasicBlock entry, BasicBlock ordinary_exit, BasicBlock exceptional_exit, Set<Integer> seen_blocks, Set<Integer> seen_nodes) {
        if (this != ordinary_exit && this != exceptional_exit && !(this.getLastNode() instanceof ThrowNode) && successors.isEmpty())
            throw new AnalysisException("No successor for block: " + toString());
        if (isEmpty())
            throw new AnalysisException("Basic block is empty: " + toString());
        if (getSourceLocation().getLineNumber() < 0)
            throw new AnalysisException("Negative line number in source information for block: " + toString());
        if (order == -1)
            throw new AnalysisException("Block order has not been set: " + toString());
        if (index == -1)
            throw new AnalysisException("Block has not been added to flow graph: " + toString());
        if (entry_block == null)
            throw new AnalysisException("Block does not have an entry_block: " + toString());
        if (entry_block == this && entry_predecessor_block == null && this != entry) {
            throw new AnalysisException("Block with self-entry_block does not have an entry_predecessor_block, and it is not the functionEntry-block: " + toString());
        }
        if (entry_block != this && entry_predecessor_block != null) {
            throw new AnalysisException("Block without self-entry_block has an entry_predecessor_block: " + toString());
        }
        if ((this == entry || this == ordinary_exit || this == exceptional_exit) && entry != entry_block)
            throw new AnalysisException("function-entry or function-exit does not have the function-entry as entry_block. entry_block is: " + entry_block);
        if (!seen_blocks.add(index))
            throw new AnalysisException("Duplicate block index: " + toString());
        if (exceptional_exit == null && canThrowExceptions())
            throw new AnalysisException("No exception handler for block " + toString());
        if (this == ordinary_exit && !(getFirstNode() instanceof ReturnNode))
            throw new AnalysisException("Last node in function is not a return node: " + toString());
        for (AbstractNode node : nodes) {
            if (node.getIndex() == -1)
                throw new AnalysisException("Node has not been added to flow graph: " + node);
            if (!seen_nodes.add(node.getIndex()))
                throw new AnalysisException("Duplicate node index: " + node);
            if (node.getSourceLocation().getLineNumber() < 0)
                throw new AnalysisException("Negative line number in source information for node: " + node);
            node.check(this);
        }
    }

    /**
     * Returns the entry block
     */
    public BasicBlock getEntryBlock() {
        return entry_block;
    }

    /**
     * Sets the entry block
     */
    public void setEntryBlock(BasicBlock entry_block) {
        this.entry_block = entry_block;
    }

    /**
     * Returns the entry_predecessor_block, or null if not set.
     */
    public BasicBlock getEntryPredecessorBlock() {
        return entry_predecessor_block;
    }

    /**
     * Sets the entry_predecessor_block
     */
    public void setEntryPredecessorBlock(BasicBlock entry_predecessor_block) {
        this.entry_predecessor_block = entry_predecessor_block;
    }
}
