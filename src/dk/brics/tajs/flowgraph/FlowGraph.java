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

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Flow graph. A flow graph is divided into functions, one of them representing
 * the main program. Each function contains blocks of nodes. Nodes represent
 * primitive instructions, edges represent control flow.
 */
public class FlowGraph {

    /**
     * The functions in this flow graph.
     */
    private Set<Function> functions;

    /**
     * The top-level function.
     */
    private final Function main;

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
     * Syntactic hints to be used for context sensitivity.
     */
    private SyntacticHints syntacticHints;

    /**
     * Constructs a new uninitialized flow graph.
     */
    public FlowGraph(Function main) {
        this.main = main;
        this.functions = newSet();
    }

    /**
     * Adds a block to this flow graph. Increases the block and node counts for the flowgraph.
     *
     * @param b The basic block to add.
     */
    public void addBlock(BasicBlock b) {
        if (b == null)
            throw new NullPointerException();
        b.getFunction().addBlock(b);
        number_of_blocks++;
        number_of_nodes += b.getNodes().size();
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
     * Adds the given function to the flow graph.
     */
    public void addFunction(Function f) {
        if (f == null)
            throw new NullPointerException();
        functions.add(f);
        f.setIndex(number_of_functions++);
    }

    /**
     * Deletes a collection of functions from the flow graph.
     */
    public void removeFunctions(Collection<Function> fs) {
        functions.removeAll(fs);
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
    public void complete() {
        for (Function f : functions)
            f.complete();
    }

    /**
     * Returns a string description of this flow graph.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Function f : functions) {
            if (HostEnvSources.isHostEnvSource(f.getSourceLocation())) {
                continue;
            }
            if (f == main)
                b.append("<main> ");
            b.append(f).append('\n');
            List<BasicBlock> sortedBlocks = newList(f.getBlocks());
            java.util.Collections.sort(sortedBlocks, (o1, o2) -> o1.getOrder() - o2.getOrder());
            for (BasicBlock k : sortedBlocks) {
                b.append("  ").append(k).append("\n");
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
        for (Function f : functions) {
            if (HostEnvSources.isHostEnvSource(f.getSourceLocation())) {
                continue;
            }
            f.toDot(pw, false, f == main);
        }
        pw.println("}");
    }

    /**
     * Produces a Graphviz dot representation of each function in this flow graph.
     *
     * @param dir destination directory
     * @param end      if set, the name contains "final", otherwise "initial"
     * @throws IOException if some file operation fails.
     */
    public void toDot(Path dir, boolean end) throws IOException {
        for (Function function : functions) {
            String n = function.isMain() ? "Main" : function.getName();
            if (n == null)
                n = "-";
            SourceLocation loc = function.getSourceLocation();
            if (loc == null) {
                loc = new SourceLocation(0, 0, "", null);
            }
            String name = loc.getPrettyFileName().replace('/', '.').replace('\\', '.').replace(':', '.') + "." + n + ".line" + loc.getLineNumber();
            String fileName = (end ? "final-" : "initial-") + name + ".dot";
            try (PrintWriter writer = new PrintWriter(dir.resolve(fileName).toFile())) {
                function.toDot(writer, true, function == main);
            }
        }
    }

    /**
     * Perform a consistency check of the flow graph (if in debug or test mode).
     */
    public void check() {
        if (!Options.get().isDebugOrTestEnabled())
            return;
        if (main == null)
            throw new AnalysisException("No main function");
        if (!functions.contains(main)) {
            throw new AnalysisException("main function not among functions?!");
        }
        Set<Integer> seen_blocks = newSet();
        Set<Integer> seen_nodes = newSet();
        Set<Integer> seen_functions = newSet();
        for (Function f : functions)
            f.check(main, seen_functions, seen_blocks, seen_nodes);
    }

    /**
     * Returns the syntactic hints for context sensitivity.
     */
    public SyntacticHints getSyntacticHints() {
        return syntacticHints;
    }

    /**
     * Sets the syntactic hints for context sensitivity.
     */
    public void setSyntacticHints(SyntacticHints syntacticHints) {
        this.syntacticHints = syntacticHints;
    }
}
