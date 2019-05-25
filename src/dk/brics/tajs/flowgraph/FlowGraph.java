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

package dk.brics.tajs.flowgraph;

import dk.brics.tajs.flowgraph.syntaticinfo.RawSyntacticInformation;
import dk.brics.tajs.flowgraph.syntaticinfo.SyntacticQueries;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
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
     * Total number of user-code (i.e. not from host environment and not marked as artificial) nodes.
     */
    private int number_of_user_code_nodes;

    /**
     * Total number of functions.
     */
    private int number_of_functions;

    /**
     * Syntactic hints to be used for context sensitivity.
     */
    private RawSyntacticInformation rawSyntacticInformation;

    /**
     * Sources in the flowgraph that belongs to the host-environment, as opposed to user-provided source code.
     */
    private Set<URL> hostEnvironmentLocations;

    /**
     * Cache of dynamically created functions.
     */
    private Map<FunctionCacheKey, Function> functionCache = newMap();

    /**
     * Additional syntactic information to be used for mapping between flowgraph locations and locations from value logs (mainly used for soundness testing)
     */
    private ValueLogLocationInformation valueLogLocationInformation;

    /**
     * Constructs a new uninitialized flow graph.
     */
    public FlowGraph(Function main) {
        this.main = main;
        this.functions = newSet();
        this.rawSyntacticInformation = new RawSyntacticInformation();
        this.hostEnvironmentLocations = newSet();
        this.valueLogLocationInformation = new ValueLogLocationInformation();
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
        for (AbstractNode n : b.getNodes()) {
            if (isUserCode(n))
                number_of_user_code_nodes++;
        }
    }

    /**
     * Returns the total number of nodes in this flow graph.
     */
    public int getNumberOfNodes() {
        return number_of_nodes;
    }

    /**
     * Returns the total number of user-code (i.e. not from host environment and not marked as artificial) nodes in this flow graph.
     */
    public int getNumberOfUserCodeNodes() {
        return number_of_user_code_nodes;
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
            if (isHostEnvironmentSource(f.getSourceLocation())) {
                continue;
            }
            if (f == main)
                b.append("<main> ");
            b.append(f).append('\n');
            List<BasicBlock> sortedBlocks = newList(f.getBlocks());
            sortedBlocks.sort(Comparator.comparingInt(BasicBlock::getTopologicalOrder));
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
            if (isHostEnvironmentSource(f.getSourceLocation())) {
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
            String prettyFileName;
            int lineNumber;
            if (loc == null) {
                lineNumber = 0;
                prettyFileName = "";
            } else {
                lineNumber = loc.getLineNumber();
                prettyFileName = loc.toUserFriendlyString(false);
            }
            String name = prettyFileName.replace('/', '.').replace('\\', '.').replace(':', '.') + "." + n + ".line" + lineNumber;
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
     * Returns the syntactic information.
     */
    public SyntacticQueries getSyntacticInformation() {
        return rawSyntacticInformation.getQueryView();
    }

    /**
     * Adds additional syntactic information to this flowgraph
     */
    public void addSyntacticInformation(RawSyntacticInformation rawSyntacticInformation, ValueLogLocationInformation valueLogLocationInformation) {
        // remember to keep this up to date when new fields are added!!!
        this.rawSyntacticInformation.getVariableDependencies().putAll(rawSyntacticInformation.getVariableDependencies());
        this.rawSyntacticInformation.getCorrelatedAccessFunctions().addAll(rawSyntacticInformation.getCorrelatedAccessFunctions());
        this.rawSyntacticInformation.getInForIn().addAll(rawSyntacticInformation.getInForIn());
        this.rawSyntacticInformation.getLoopVariables().putAll(rawSyntacticInformation.getLoopVariables());
        this.rawSyntacticInformation.getTajsCallsWithLiteralFalseAsFirstOrFourthArgument().addAll(rawSyntacticInformation.getTajsCallsWithLiteralFalseAsFirstOrFourthArgument());
        this.rawSyntacticInformation.getNodeWithBaseReferences().putAll(rawSyntacticInformation.getNodeWithBaseReferences());
        this.rawSyntacticInformation.getSimpleReads().putAll(rawSyntacticInformation.getSimpleReads());
        this.rawSyntacticInformation.getExpressionRegisters().putAll(rawSyntacticInformation.getExpressionRegisters());
        this.rawSyntacticInformation.getConditionRefined1ArgumentVariables().putAll(rawSyntacticInformation.getConditionRefined1ArgumentVariables());
        this.rawSyntacticInformation.getConditionRefinedArgumentVariables().putAll(rawSyntacticInformation.getConditionRefinedArgumentVariables());
        this.rawSyntacticInformation.getStackVariables().putAll(rawSyntacticInformation.getStackVariables());
        this.rawSyntacticInformation.getFunctionClosureVariables().putAll(rawSyntacticInformation.getFunctionClosureVariables());
        this.rawSyntacticInformation.getFunctionsWithThisReference().addAll(rawSyntacticInformation.getFunctionsWithThisReference());

        this.valueLogLocationInformation.getDeclaredAccessorAllocationSites().addAll(valueLogLocationInformation.getDeclaredAccessorAllocationSites());
        this.valueLogLocationInformation.getTajsLocation2jalangiLocation().putAll(valueLogLocationInformation.getTajsLocation2jalangiLocation());
    }

    /**
     * Registers a file as belonging to the host-environment.
     */
    public void registerHostEnvironmentSource(URL file) {
        this.hostEnvironmentLocations.add(file);
    }

    /**
     * Checks if the given location belongs to the host environment.
     */
    public boolean isHostEnvironmentSource(SourceLocation sourceLocation) {
        return hostEnvironmentLocations.contains(sourceLocation.getLocation());
    }

    /**
     * Checks if the given node belongs to user code, i.e. it is not from host environment and not marked as artificial.
     */
    public boolean isUserCode(AbstractNode n) {
        return !isHostEnvironmentSource(n.getSourceLocation()) && n.getSourceLocation().getLocation() != null && !n.isArtificial();
    }

    /**
     * Returns the cache of dynamically created functions. Should be consulted before new dynamic functions are added to the flowgraph.
     */
    public Map<FunctionCacheKey, Function> getFunctionCache() {
        return functionCache;
    }

    public ValueLogLocationInformation getValueLogLocationInformation() {
        return valueLogLocationInformation;
    }

    /**
     * Cache key for dynamically created functions.
     */
    public interface FunctionCacheKey {

    }

    /**
     * Cache key for functions that are created from string expressions at runtime.
     */
    public static class FunctionDynamicSourceCacheKey implements FunctionCacheKey {

        private final SourceLocation location;

        private final List<String> parameterNames;

        private final String source;

        public FunctionDynamicSourceCacheKey(SourceLocation location, List<String> parameterNames, String source) {
            this.location = location;
            this.parameterNames = parameterNames;
            this.source = source;
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FunctionDynamicSourceCacheKey that = (FunctionDynamicSourceCacheKey) o;

            if (location != null ? !location.equals(that.location) : that.location != null)
                return false;
            if (parameterNames != null ? !parameterNames.equals(that.parameterNames) : that.parameterNames != null)
                return false;
            return source != null ? source.equals(that.source) : that.source == null;
        }

        @Override
        public int hashCode() {
            int result = location != null ? location.hashCode() : 0;
            result = 31 * result + (parameterNames != null ? parameterNames.hashCode() : 0);
            result = 31 * result + (source != null ? source.hashCode() : 0);
            return result;
        }
    }

    /**
     * Cache key for functions that are created from files at runtime.
     */
    public static class FunctionFileSourceCacheKey implements FunctionCacheKey {

        private final URL file;

        private final List<String> parameterNames;

        public FunctionFileSourceCacheKey(URL file, List<String> parameterNames) {
            this.file = file;
            this.parameterNames = parameterNames;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FunctionFileSourceCacheKey that = (FunctionFileSourceCacheKey) o;

            if (file != null ? !file.equals(that.file) : that.file != null) return false;
            return parameterNames != null ? parameterNames.equals(that.parameterNames) : that.parameterNames == null;
        }

        @Override
        public int hashCode() {
            int result = file != null ? file.hashCode() : 0;
            result = 31 * result + (parameterNames != null ? parameterNames.hashCode() : 0);
            return result;
        }
    }
}
