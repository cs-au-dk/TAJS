/*
 * Copyright 2009-2013 Aarhus University
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

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;

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
     * Constructs a new function.
     * The node set is initially empty, and the entry/exit nodes are not set.
     * The function name is null for anonymous functions.
     *
     * @param name function name, null for anonymous functions
     * @param parameter_names list of parameter names, null can be used in place of an empty list
     * @param outer_function the outer function, null if none
     * @param location source location
     */
	public Function(String name, List<String> parameter_names, Function outer_function, SourceLocation location) {
		this.name = name;
		this.location = location;
		this.parameter_names = parameter_names == null ? java.util.Collections.<String>emptyList() : parameter_names;
        this.outer_function = outer_function;
		this.variable_names = newSet();
		blocks = newList();
		this.uses_this = false;
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
		if (this.index != -1 && !Options.isUnevalEnabled())
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
		return java.util.Collections.unmodifiableSet(variable_names);
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
     * @param pw A receiving print writer.
     * @param standalone Is this a standalone graph?
     * @param main Is this main?
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
			pw.println("label=\"" + (main ? "<main> " : "") + toString() + "\\n" + location
                    + (outer_function != null ? "\\nouter: " + (outer_function.getName() == null ? "<main>" : outer_function.getName()) : "")  +  "\";");
			pw.println("labelloc=\"t\";");
			pw.println("fontsize=18;");
		}
		pw.println("rankdir=\"TD\"");
		Set<BasicBlock> labels = new HashSet<>();
		pw.println("BB_entry" + index + "[shape=none,label=\"\"];");
		pw.println("BB_entry" + index + " -> BB" + entry.getIndex() 
				+ " [tailport=s, headport=n, headlabel=\"    " + entry.getIndex() + "\"]");
		labels.add(entry);
		for (BasicBlock b : blocks) {
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
					pw.print(", headlabel=\"      " + bs.getIndex() /*+ (bs.hasOrder() ? "[" +bs.getOrder() + "]" : "")*/ +"\"");
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
					pw.print(", headlabel=\"      " + ex.getIndex() +"\"");
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
        this.max_register = max_tmp;
    }

    /**
     * Returns the upper bound of the register numbers used in this function. Used to safely extend the flow graph
     * during the analysis.
     */
    public int getMaxRegister() {
        return max_register;
    }
	
	/**
	 * Sets the block orders. Call after construction or modification of the function.
	 */
	public void complete() { // FIXME: use topological order instead of line/column numbers for block order
		int max = 0;
		for (BasicBlock b : blocks) {
			if (!b.isEmpty()) {
				int x = b.getSourceLocation().getLineNumber() * 1000 + b.getSourceLocation().getColumnNumber();
				b.setOrder(x);
				if (x > max)
					max = x;
			}
		}
		if (entry != null)
			entry.setOrder(0);
		if (ordinary_exit != null)
			ordinary_exit.setOrder(max+1);
		if (exceptional_exit != null)
			exceptional_exit.setOrder(max+2);
	}

    /**
     * Perform a consistency check of the function.
     */
    public void check(Function main, Set<Integer> seen_functions, Set<Integer> seen_blocks, Set<Integer> seen_nodes) {
        if (!seen_functions.add(index))
            throw new AnalysisException("Duplicate function index: " + toString());
        if (index == -1)
            throw new AnalysisException("Function has not been added to flow graph: " + toString());
        if (this != main && !hasOuterFunction())
            throw new AnalysisException("Function is missing outer function and is not main: " + toString());
        if (ordinary_exit == null)
            throw new AnalysisException("Function is missing ordinary exit: " + toString());
        if (exceptional_exit == null)
            throw new AnalysisException("Function is missing exceptional exit: " + toString());
        for (BasicBlock bb : blocks)
            bb.check(ordinary_exit, exceptional_exit, seen_blocks, seen_nodes);
    }

    /**
     * Marks the function as having a syntactic 'this' in its source code or not. 
     */
	public void setUsesThis(boolean usesThis) {
		this.uses_this = usesThis;
	}
	
	/**
	 * Returns true if this function has a syntactic 'this' in its source code.
	 */
	public boolean isUsesThis() {
		return uses_this;
	}
}
