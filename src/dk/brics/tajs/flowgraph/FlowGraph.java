/*
 * Copyright 2012 Aarhus University
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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collections;

/**
 * Flow graph. A flow graph is divided into functions, one of them representing
 * the main program. Each function contains blocks of nodes. Nodes represent
 * primitive instructions, edges represent control flow.
 */
public class FlowGraph {

	/**
	 * The functions in this flow graph.
	 */
    private Collection<Function> functions;

    /**
     * The top-level function.
     */
    private Function main;

    /**
     * Total number of basic blocks.
     */
    private int number_of_blocks;

    /**
     * Total number of nodes.
     */
    private int number_of_nodes;

    /**
     * Total number of functions.
     */
    private int number_of_functions;

    /**
     * Callbacks of various kinds
     */
    private Map<CallbackKind, Set<Function>> callbacks = Collections.newMap();

    /**
     * Constructs a new uninitialized flow graph.
     */
    public FlowGraph() {
        functions = newList();
    }

    /**
     * Adds a block to this flow graph. Also sets the block index and the node
     * index for each node in the block.
     *
     * @param b The basic block to add.
     */
    public void addBlock(BasicBlock b) {
        if (b == null)
            throw new NullPointerException("Block is null");
        b.getFunction().addBlock(b);
        b.setIndex(number_of_blocks++);
        for (AbstractNode n : b.getNodes())
            n.setIndex(number_of_nodes++);
    }

    /**
     * Returns the total number of nodes in this flow graph.
     */
    public int getNumberOfNodes() {
        return number_of_nodes;
    }

    /**
     * Returns the total number of basic blocks in this flow graph.
     */
    public int getNumberOfBlocks() {
        return number_of_blocks;
    }

    /**
     * Returns the functions, including the main function.
     */
    public Collection<Function> getFunctions() {
        return functions;
    }

    /**
     * Adds the given function to the flow graph. The first function added becomes main.
     */
    public void addFunction(Function f) {
        if (f == null)
            throw new NullPointerException("Adding null function to flow graph");
        if (functions.isEmpty())
            main = f;
        functions.add(f);
        f.setIndex(number_of_functions++);
    }

    /**
     * Adds the given callback to the flow graph. The function must already be added to the flow graph.
     */
    public void addCallback(Function f, CallbackKind kind) {
        if (!functions.contains(f)) {
            throw new IllegalArgumentException("Function not added to flow graph");
        }
        Set<Function> fs = callbacks.get(kind);
        if (fs == null) {
            fs = Collections.newSet();
            callbacks.put(kind, fs);
        }
        fs.add(f);
    }

    /**
     * Retrieves the callbacks of the given kind. Always returns a set (and never null).
     */
    public Set<Function> getCallbacksByKind(CallbackKind kind) {
        Set<Function> result = callbacks.get(kind);
        if (result == null) {
            return Collections.newSet();
        }
        return result;
    }

    /**
     * Deletes a collection of functions from the flow graph.
     */
    public void removeFunctions(Collection<Function> fs) {
        functions.removeAll(fs);
    }

    /**
     * Returns the function with the given name.
     */
    public Function getFunction(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException();
        }
        Set<Function> result = Collections.newSet();
        for (Function function : functions) {
            if (name.equals(function.getName())) {
                result.add(function);
            }
        }
        if (result.size() == 0) {
            return null;
        } else if (result.size() == 1) {
            return result.iterator().next();
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns the main code.
     */
    public Function getMain() {
        return main;
    }

    /**
     * Returns the entry block of the flow graph.
     */
    public BasicBlock getEntryBlock() {
        return main.getEntry();
    }

    /**
     * Runs complete on all the functions in the flow graph.
     */
    public FlowGraph complete() {
        for (Function f : functions)
            f.complete();
        return this;
    }
    
    /**
     * Returns a string description of this flow graph.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Function f : functions) {
            if (f == main)
                b.append("<main> ");
            b.append(f).append('\n');
            for (BasicBlock k : f.getBlocks()) { // TODO: why not calling k.toString here?
                b.append("  block ").append(k.getIndex());
                b.append(':');
                if (f.getEntry() == k)
                    b.append(" [entry]");
                if (f.getOrdinaryExit() == k)
                    b.append(" [exit-ordinary]");
                if (f.getExceptionalExit() == k)
                    b.append(" [exit-exceptional]");
                b.append('\n');
                for (AbstractNode n : k.getNodes()) {
                	b.append("    node ").append(n.getIndex());
        			AbstractNode d = n.getDuplicateOf();
        			if (d != null)
        				b.append("(~").append(d.getIndex()).append(")");
                	b.append(": ").append(n);
        			if (n.isRegistersDone())
        				b.append('*');
        			if (n.isDead())
        				b.append('$');
                	b.append(" (").append(n.getSourceLocation()).append(")\n");
                }
                b.append("    ->[");
                boolean first = true;
                for (BasicBlock s : k.getSuccessors()) {
                    if (first)
                        first = false;
                    else
                        b.append(',');
                    b.append("block ").append(s.getIndex());
                }
                b.append("]");
                BasicBlock exception_handler = k.getExceptionHandler();
                if (exception_handler != null && exception_handler != f.getExceptionalExit()) {
                    b.append(" ~> [ ").append("block ");
                    b.append(k.getExceptionHandler().getIndex());
                    b.append("]");
                }
                b.append("\n");
            }
        }
        return b.toString();
    }

    /**
     * Produces a Graphviz dot representation of this flow graph.
     */
    public void toDot(PrintWriter pw) {
        pw.println("digraph {");
        pw.println("compound=true");
        for (Function f : functions)
            f.toDot(pw, false, f == main);
        pw.println("}");
    }

    /**
     * Produces a Graphviz dot representation of each function in this flow graph.
     * @param dest_dir destination directory
     * @param end if set, the name contains "final", otherwise "initial"
     * @throws IOException if some file operation fails.
     */
    public void toDot(String dest_dir, boolean end) throws IOException {
        for (Function function : functions) {
            String n = function.isMain() ? "Main" : function.getName();
            if (n == null)
                n = "-";
            String name = function.getSourceLocation().getFileName().replace('/', '.').replace('\\', '.').replace(':', '.') + 
            		"." + n + ".line" + function.getSourceLocation().getLineNumber();
            File file = new File(dest_dir + File.separator + (end ? "final-" : "initial-") + name + ".dot");
            try (PrintWriter writer = new PrintWriter(file)) {
            	function.toDot(writer, true, function == main);
            } 
        }
    }

    /**
     * Perform a consistency check of the flow graph (if in debug or test mode).
     */
    public void check() {
    	if (!Options.isDebugOrTestEnabled())
    		return;
    	Set<Integer> seen_blocks = newSet();
    	Set<Integer> seen_nodes = newSet();
    	Set<Integer> seen_functions = newSet();
    	for (Function f : functions)
    		f.check(main, seen_functions, seen_blocks, seen_nodes);
    }
}
