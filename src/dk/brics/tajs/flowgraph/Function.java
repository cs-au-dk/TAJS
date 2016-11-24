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

package dk.brics.tajs.flowgraph;

import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Function sub-graph.
 * Whenever a function has been created or modified, {@link #complete()} must be called.
 */
public class Function {

    /**
     * Unique index of this function in the flow graph, or -1 if not belonging to a flow graph.
     */
    private int index = -1;

    /**
     * Basic blocks in this function.
     */
    private Collection<BasicBlock> blocks;

    /**
     * Source location of the entry of this function.
     */
    private SourceLocation location;

    /**
     * Entry basic block.
     */
    private BasicBlock entry;

    /**
     * Ordinary exit block.
     */
    private BasicBlock ordinary_exit;

    /**
     * Exceptional exit block.
     */
    private BasicBlock exceptional_exit;

    /**
     * Name of this function, or null for anonymous functions.
     */
    private String name;

    /**
     * Outer function, or null if none.
     */
    private Function outer_function;

    /**
     * Parameter names.
     */
    private List<String> parameter_names;

    /**
     * Variable names declared in the function.
     */
    private Set<String> variable_names;

    /**
     * Maximum register used in this function. Used to safely extend the flow graph during the analysis.
     */
    private int max_register;

    /**
     * True iff this function has a syntactic 'this' in its source code.
     */
    private boolean uses_this;


    /**
     * The node where this function is declared.
     */
    private DeclareFunctionNode node;

    /**
     * The variables read by the function that are defined in an outer function.
     */
    private Set<String> closureVariableNames;

    /**
     * The source code of the function, null if full source code is not available (e.g. eventhandlers in html).
     */
    private final String source;

    /**
     * Constructs a new function.
     * The node set is initially empty, and the entry/exit nodes are not set.
     * The function name is null for anonymous functions.
     *
     * @param name            function name, null for anonymous functions
     * @param parameter_names list of parameter names, null can be used in place of an empty list
     * @param outer_function  the outer function, null if none
     * @param location        source location
     * @param source          full source code of function, null if not available
     */
    public Function(String name, List<String> parameter_names, Function outer_function, SourceLocation location, String source) {
        assert (location != null);
        this.name = name;
        this.location = location;
        this.parameter_names = parameter_names == null ? Collections.<String>emptyList() : parameter_names;
        this.outer_function = outer_function;
        this.source = source;
        variable_names = newSet();
        uses_this = false;
        blocks = newList();
        closureVariableNames = newSet();
    }

    /**
     * @see #Function(String, List, Function, SourceLocation, String)
     */
    public Function(String name, List<String> parameter_names, Function outer_function, SourceLocation location) {
        this(name, parameter_names, outer_function, location, null);
    }

    /**
     * Returns true if this is the main function.
     */
    public boolean isMain() {
        return outer_function == null;
    }

    /**
     * Sets the function index.
     * Called when the function is added to a flow graph.
     */
    void setIndex(int index) {
        if (this.index != -1 && !Options.get().isUnevalizerEnabled())
            throw new IllegalArgumentException("Function already belongs to a flow graph: " + getSourceLocation());
        this.index = index;
    }

    /**
     * Returns the function index.
     * The function index uniquely identifies the function within the flow graph.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the entry block.
     */
    public void setEntry(BasicBlock entry) {
        this.entry = entry;
    }

    /**
     * Sets the ordinary exit block.
     * This block must consist of a single ReturnNode.
     */
    public void setOrdinaryExit(BasicBlock ordinary_exit) {
        this.ordinary_exit = ordinary_exit;
    }

    /**
     * Sets the exceptional exit basic block.
     * This block must consist of a single ExceptionalReturnNode.
     */
    public void setExceptionalExit(BasicBlock exceptional_exit) {
        this.exceptional_exit = exceptional_exit;
    }

    /**
     * Returns the function name, or null if the function is anonymous.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of parameter names.
     */
    public List<String> getParameterNames() {
        return parameter_names;
    }

    /**
     * Adds a variable name (only during flow graph construction).
     */
    public void addVariableName(String varname) {
        variable_names.add(varname);
    }

    /**
     * Returns the set of variable names declared in this function.
     */
    public Set<String> getVariableNames() {
        return Collections.unmodifiableSet(variable_names);
    }

    /**
     * Returns the collection of basic blocks.
     */
    public Collection<BasicBlock> getBlocks() {
        return blocks;
    }

    /**
     * Adds a basic block to the function.
     */
    public void addBlock(BasicBlock bb) {
        blocks.add(bb);
    }

    /**
     * Removes a collection of basic blocks from the function.
     */
    public void removeBlocks(Collection<BasicBlock> bbs) {
        blocks.removeAll(bbs);
    }

    /**
     * Returns the entry block.
     */
    public BasicBlock getEntry() {
        return entry;
    }

    /**
     * Returns the ordinary exit block.
     */
    public BasicBlock getOrdinaryExit() {
        return ordinary_exit;
    }

    /**
     * Returns the exceptional exit block.
     */
    public BasicBlock getExceptionalExit() {
        return exceptional_exit;
    }

    /**
     * Sets the source location.
     */
    public void setSourceLocation(SourceLocation location) {
        this.location = location;
    }

    /**
     * Returns a source location for this function.
     */
    public SourceLocation getSourceLocation() {
        return location;
    }

    /**
     * Returns true iff the function has an outer function.
     */
    public boolean hasOuterFunction() {
        return outer_function != null;
    }

    /**
     * Returns the outer function, or null if there is no outer function.
     */
    public Function getOuterFunction() {
        return outer_function;
    }

    /**
     * Returns a string description of this function.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("function");
        if (name != null)
            b.append(' ').append(Strings.escape(name));
        b.append('(');
        boolean first = true;
        for (String s : parameter_names) {
            if (first)
                first = false;
            else
                b.append(',');
            b.append(Strings.escape(s));
        }
        b.append(')');
        return b.toString();
    }

    /**
     * Produces a Graphviz dot representation of this function.
     *
     * @param pw         A receiving print writer.
     * @param standalone Is this a standalone graph?
     * @param main       Is this main?
     */
    public void toDot(PrintWriter pw, boolean standalone, boolean main) {
//        List<String> colors = Collections.newList();
//        colors.add("black");
//        colors.add("red1");
//        colors.add("green1");
//        colors.add("blue1");
        if (standalone) {
            pw.println("digraph {");
        } else {
            pw.println("subgraph " + "cluster" + index + " {");
            String outerFunction = outer_function == null ? "" : "\\nouter: " + (outer_function.isMain() ? "<main>" : (outer_function.getName() == null ? "<anonymous>" : outer_function.getName()));
            pw.println("label=\"" + (main ? "<main> " : "") + toString() + "\\n" + location + outerFunction + "\";");
            pw.println("labelloc=\"t\";");
            pw.println("fontsize=18;");
        }
        pw.println("rankdir=\"TD\"");
        Set<BasicBlock> labels = newSet();
        pw.println("BB_entry" + index + "[shape=none,label=\"\"];");
        pw.println("BB_entry" + index + " -> BB" + entry.getIndex()
                + " [tailport=s, headport=n, headlabel=\"    " + entry.getIndex() + "\"]");
        labels.add(entry);
        List<BasicBlock> sortedBlocks = newList(this.blocks);
        java.util.Collections.sort(sortedBlocks, (o1, o2) -> o1.getOrder() - o2.getOrder());
        for (BasicBlock b : sortedBlocks) {
            b.toDot(pw, false);
//	        int color_index = 0;
            for (BasicBlock bs : b.getSuccessors()) {
//                String color = colors.get(color_index);
//                color_index = (color_index + 1) % colors.size();
                pw.print("BB" + b.getIndex() + " -> BB" + bs.getIndex()
                        + " [tailport=s, headport=n");
//				pw.print(", color=" + color);
                if (!labels.contains(bs)) {
                    labels.add(bs);
                    pw.print(", headlabel=\"      " + bs.getIndex() /*+ (bs.hasOrder() ? "[" +bs.getOrder() + "]" : "")*/ + "\"");
                }
                pw.println("]");
            }
            BasicBlock ex = b.getExceptionHandler();
            if (ex != null && b.canThrowExceptions()) {
                // if (ex != null && ex != b) {
                pw.print("BB" + b.getIndex() + " -> BB" + ex.getIndex()
                        + " [tailport=s, headport=n, color=gray");
                if (!labels.contains(ex)) {
                    labels.add(ex);
                    pw.print(", headlabel=\"      " + ex.getIndex() + "\"");
                }
                pw.println("]");
            }
        }
        pw.println("}");
        if (standalone) {
            pw.flush();
        }
    }

    /**
     * Sets the upper bound of the register numbers used in this function. Used to safely extend the flow graph
     * during the analysis.
     */
    public void setMaxRegister(int max_tmp) {
        max_register = max_tmp;
    }

    /**
     * Returns the upper bound of the register numbers used in this function. Used to safely extend the flow graph
     * during the analysis.
     */
    public int getMaxRegister() {
        return max_register;
    }

    /**
     * Marks the function as having a syntactic 'this' in its source code or not.
     */
    public void setUsesThis(boolean uses_this) {
        this.uses_this = uses_this;
    }

    /**
     * Returns true if this function has a syntactic 'this' in its source code.
     */
    public boolean isUsesThis() {
        return uses_this;
    }

    /**
     * Returns the (mutable) set of variables read by the function that are defined in an outer function.
     */
    public Set<String> getClosureVariableNames() {
        return closureVariableNames;
    }

    /**
     * Sets the block orders. Call after construction or modification of the function.
     */
    public void complete() {
        // Force the ordinary and exceptional exit to be last, it produces prettier dotfiles without changing anything else
        Set<BasicBlock> topologicalBlocks = newSet(blocks);
        Set<BasicBlock> nonTopologicalBlocks = newSet(Arrays.asList(ordinary_exit, exceptional_exit));
        topologicalBlocks.removeAll(nonTopologicalBlocks);

        // a function can have unreachable blocks, forming more roots of the block-graph than just the entry-block
        // for pretty printing, the roots are sorted according to their source location
        Set<BasicBlock> roots = newSet(blocks);
        blocks.forEach(b -> {
            roots.removeAll(b.getSuccessors());
            roots.remove(b.getExceptionHandler());
        });
        List<BasicBlock> rootOrder = newList(roots);
        Collections.sort(rootOrder, (o1, o2) -> o1.getSourceLocation().compareTo(o2.getSourceLocation()));

        int i = 0;
        for (BasicBlock block : produceDependencyOrder(topologicalBlocks, nonTopologicalBlocks, rootOrder)) {
            block.setOrder(i++);
        }

        if (ordinary_exit != null)
            ordinary_exit.setOrder(i++);
        if (exceptional_exit != null)
            exceptional_exit.setOrder(i);
    }

    /**
     * Produces a topological sorting of blocks with a depth-first search that ignores cycles.
     * Algorithm: wikipedia on topological sorting with depth-first (Cormen2001/Tarjan1976).
     * - slightly modified to produce prettier orders
     */
    private static List<BasicBlock> produceDependencyOrder(Collection<BasicBlock> blocks, Set<BasicBlock> ignored, List<BasicBlock> rootOrder) {
        List<BasicBlock> sorted = newList();
        Set<BasicBlock> notPermanentlyMarked = newSet(blocks);
        Set<BasicBlock> temporarilyMarked = newSet();

        // Implementation choice in topological sort: process roots only, and in reverse order
        List<BasicBlock> reverseRootOrder = newList(rootOrder);
        Collections.reverse(reverseRootOrder);
        for (BasicBlock root : reverseRootOrder) {
            visit(root, temporarilyMarked, notPermanentlyMarked, sorted, ignored);
        }
        if (!notPermanentlyMarked.isEmpty()) {
            // sanity check that it is ok to iterate roots instead of "notPermanentlyMarked"
            throw new AnalysisException("Bad topological sort implementation");
        }
        Collections.reverse(sorted); // the list has been built backwards...
        return sorted;
    }

    private static void visit(BasicBlock n, Set<BasicBlock> temporarilyMarked, Set<BasicBlock> notPermanentlyMarked, List<BasicBlock> sorted, Set<BasicBlock> ignored) {
        if (temporarilyMarked.contains(n)) {
            return;
        }
        if (notPermanentlyMarked.contains(n)) {
            temporarilyMarked.add(n);
            List<BasicBlock> successors = newList(n.getSuccessors());
            BasicBlock exceptionHandler = n.getExceptionHandler();
            if (exceptionHandler != null) {
                successors.add(exceptionHandler);
            }

            // Implementation choice in topological sort: process multiple successors in reverse source position order.
            // A benefit of this is that loop back edges receive lower order than the loop successors.
            Collections.sort(successors, (o1, o2) -> -o1.getSourceLocation().compareTo(o2.getSourceLocation()));

            successors.removeAll(ignored);
            for (BasicBlock m : successors) {
                visit(m, temporarilyMarked, notPermanentlyMarked, sorted, ignored);
            }
            notPermanentlyMarked.remove(n);
            temporarilyMarked.remove(n);
            sorted.add(n);
        }
    }

    /**
     * Perform a consistency check of the function.
     */
    public void check(Function main, Set<Integer> seen_functions, Set<Integer> seen_blocks, Set<Integer> seen_nodes) {
        if (!seen_functions.add(index))
            throw new AnalysisException("Duplicate function index: " + index + " for " + toString());
        if (index == -1)
            throw new AnalysisException("Function has not been added to flow graph: " + toString());
        if ((this == main) != isMain())
            throw new AnalysisException("Function is confused about main: " + toString());
        if (ordinary_exit == null)
            throw new AnalysisException("Function is missing ordinary exit: " + toString());
        if (exceptional_exit == null)
            throw new AnalysisException("Function is missing exceptional exit: " + toString());
        for (BasicBlock bb : blocks)
            bb.check(entry, ordinary_exit, exceptional_exit, seen_blocks, seen_nodes);
    }

    /**
     * Returns the node where this function is declared.
     */
    public DeclareFunctionNode getNode() {
        return node;
    }

    /**
     * Sets the node where this function is declared.
     */
    public void setNode(DeclareFunctionNode node) {
        this.node = node;
    }

    /**
     * Returns the source code of the function, null if full source code is not available (e.g. eventhandlers in html).
     */
    public String getSource() {
        return source;
    }
}
