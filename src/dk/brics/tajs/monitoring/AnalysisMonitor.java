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

package dk.brics.tajs.monitoring;

import dk.brics.tajs.Tuple;
import dk.brics.tajs.analysis.HostAPIs;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginWithNode;
import dk.brics.tajs.flowgraph.jsnodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.CatchNode;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.DeletePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.EndForInNode;
import dk.brics.tajs.flowgraph.jsnodes.EndLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.EndWithNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.ExceptionalReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.HasNextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.NewObjectNode;
import dk.brics.tajs.flowgraph.jsnodes.NextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
import dk.brics.tajs.flowgraph.jsnodes.NodeVisitor;
import dk.brics.tajs.flowgraph.jsnodes.NopNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.ReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.ThrowNode;
import dk.brics.tajs.flowgraph.jsnodes.UnaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.PKey.StringPKey;
import dk.brics.tajs.lattice.PKeys;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.ObjReadsWrites.R_Status;
import dk.brics.tajs.monitoring.ObjReadsWrites.W_Status;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.Strings;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import static dk.brics.tajs.util.Collections.addAllToMapSet;
import static dk.brics.tajs.util.Collections.addToMapList;
import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Records various information during analysis.
 */
public class AnalysisMonitor implements IAnalysisMonitoring {

    private static Logger log = Logger.getLogger(AnalysisMonitor.class);

    private boolean pointer_analysis_active = true;

    /**
     * Set if in scan phase (at the fixpoint).
     */
    private boolean scan_phase = false;

    /**
     * Number of node transfers.
     */
    private int node_transfers = 0;

    /**
     * Number of block transfers.
     */
    private int block_transfers = 0;

    /**
     * Number of calls to the UnknownValueResolver where a value is recovered partially in non-scanning mode.
     */
    private int unknown_value_resolve_analyzing_partial = 0;

    /**
     * Number of calls to the UnknownValueResolver where a value is recovered fully in non-scanning mode.
     */
    private int unknown_value_resolve_analyzing_full = 0;

    /**
     * Number of calls to the UnknownValueResolver where a value is recovered partially in scanning mode.
     */
    private int unknown_value_resolve_scanning_partial = 0;

    /**
     * Number of calls to the UnknownValueResolver where a value is recovered fully in scanning mode.
     */
    private int unknown_value_resolve_scanning_full = 0;

    /**
     * Number of abstract state join operations.
     */
    private int joins = 0;

    /**
     * All functions
     */
    private Set<Function> functions;

    /**
     * Number of (non-this) variable read operations.
     */
    private int read_variable_nodes = 0;

    /**
     * Number of property access operations.
     */
    private int property_access_nodes = 0;

    /**
     * Number of fixed-property read operations.
     */
    private int read_fixed_property_nodes = 0;

    /**
     * Number of call/construct nodes.
     */
    private int call_nodes = 0;

    /**
     * Variable read operations that may read an absent variable.
     */
    private Set<ReadVariableNode> absent_variable_read;

    /**
     * Fixed-property read operations that may access an absent property.
     */
    private Set<ReadPropertyNode> absent_fixed_property_read;

    /**
     * Property access operations that may dereference null or undefined.
     */
    private Set<Node> null_undef_base;

    /**
     * Call/construct nodes that may involve non-function values.
     */
    private Set<AbstractNode> call_to_non_function;

    /**
     * Store information about values read during analysis.
     */
    private Map<NodeAndContext<Context>, Value> value_reads;

    /**
     * Values passed to eval.
     */
    private Map<AbstractNode, Value> eval_calls;

    /**
     * Values written to innerHTML.
     */
    private Map<AbstractNode, Value> inner_html_writes;

    /**
     * Descriptions of new dataflow at function entry blocks.
     */
    private Map<BasicBlock, Map<Context, List<String>>> newflows;

    /**
     * Information about read/written properties in all abstract objects.
     * Singleton/summary information is stored for the singleton variant.
     */
    private Map<ObjectLabel, ObjReadsWrites> obj_reads_writes;

    /**
     * Variables and parameters that are declared in each function and maybe read.
     * Undeclared variables belong to the main function.
     */
    private Map<Function, Set<String>> read_variables;

    /**
     * Functions that may be called as constructors.
     */
    private Set<Function> called_as_constructor;

    private TypeCollector type_collector;

//    /**
//     * Counter for {@link #visitNewFlow(BasicBlock, IContext, IState, String, String)}.
//     */
//    private int next_newflow_file;

    /**
     * Number of property recovery graphs of different sizes.
     */
    private Map<Integer, Integer> recovery_graph_sizes;

    /**
     * Collected messages.
     * Maps each message object to itself to make it possible to find existing messages that are "equal" to new ones.
     */
    private Map<Message, Message> messages;

    /**
     * The current flowgraph
     */
    private FlowGraph flowgraph;

    /**
     * The current callgraph
     */
    private CallGraph<State, Context, CallEdge> callgraph;

    private final ReachabilityMonitor reachabilityMonitor;

    private long joinTime;

    private int numSoundnessChecks;

    /**
     * Constructs a new analysis monitoring object.
     */
    public AnalysisMonitor() {
        reachabilityMonitor = new ReachabilityMonitor();
        call_to_non_function = newSet();
        absent_variable_read = newSet();
        null_undef_base = newSet();
        absent_fixed_property_read = newSet();
        newflows = newMap();
        eval_calls = newMap();
        inner_html_writes = newMap();
        value_reads = newMap();
        obj_reads_writes = newMap();
        read_variables = newMap();
        functions = newSet();
        called_as_constructor = newSet();
        type_collector = new TypeCollector();
        recovery_graph_sizes = newMap();
//        next_newflow_file = 1;
        messages = null;
    }

    private void reportDeadAssignments() {
        Set<Pair<AbstractNode, PKey>> potentiallyDeadWrites = newSet();
        Set<Pair<AbstractNode, PKey>> undeadWrites = newSet();
        for (Entry<ObjectLabel, ObjReadsWrites> entry : obj_reads_writes.entrySet()) {
            ObjReadsWrites rw = entry.getValue();
            for (PKey s : rw.getProperties()) {
                // flag if definitely written and definitely not read (excluding 'length' and any-string properties)
                if (rw.getWriteStatus(s) == W_Status.WRITTEN) {
                    for (AbstractNode definiteWriteLocation : rw.getDefiniteWriteLocations(s)) {
                        if (rw.getReadStatus(s) == R_Status.NOT_READ) {
                            potentiallyDeadWrites.add(Pair.make(definiteWriteLocation, s));
                        } else {
                            undeadWrites.add(Pair.make(definiteWriteLocation, s));
                        }
                    }
                }
            }

            if (rw.isUnknownWritten() && !rw.isSomePropertyRead()) {
                for (AbstractNode n : rw.getDefaultWriteLocations()) {
                    String m_s = "Dead assignment, property is never read";
                    Message m = new Message(n, Status.CERTAIN, m_s, Severity.MEDIUM, true);
                    messages.put(m, m);
                }
            }
        }
        Set<Pair<AbstractNode, PKey>> deadWrites = newSet();
        deadWrites.addAll(potentiallyDeadWrites);
        deadWrites.removeAll(undeadWrites);
        for (Pair<AbstractNode, PKey> deadWrite : deadWrites) {
            String m_s = "Dead assignment, property " + deadWrite.getSecond().toStringEscaped() + " is never read";
            Message m = new Message(deadWrite.getFirst(), Status.CERTAIN, m_s, Severity.MEDIUM, true);
            messages.put(m, m);
        }
    }

    private void reportUnusedVariableOrParameter() {
        for (Function f : reachabilityMonitor.getReachableFunctions()) { // Skip reporting unused variables and parameters for dead code.
            Set<String> names = newSet(f.getVariableNames());
            names.addAll(f.getParameterNames());
            Set<String> rv = read_variables.get(f);
            if (rv != null)
                names.removeAll(rv);
            for (String n : names) {
                AbstractNode declNode = getVarDeclNode(f, n);
                addMessage(declNode, Status.CERTAIN, Severity.LOW, "The variable " + Strings.escape(n) + " is never used");
            }
        }
    }

    private static AbstractNode getVarDeclNode(Function f, String n) {
        Collection<BasicBlock> blocks = f.getBlocks();
        if (f.getParameterNames().contains(n))
            return f.getEntry().getFirstNode();
        for (BasicBlock block : blocks) {
            List<AbstractNode> nodes = block.getNodes();
            for (AbstractNode node : nodes) {
                if (node instanceof DeclareVariableNode && ((DeclareVariableNode) node).getVariableName().equals(n)) {
                    return node;
                }
            }
        }
        throw new IllegalArgumentException("Variable " + n + " is not declared in this function!?!");
    }

    @Override
    public void setSolverInterface(Solver.SolverInterface c) {
        flowgraph = c.getFlowGraph();
        callgraph = c.getAnalysisLatticeElement().getCallGraph();
    }

    /**
     * Add messages about shadowing. Shadowing occurs when a declared variable clashes with local functions or parameters.
     * This is a purely syntactic property, no analysis results are used.
     */
    private void reportShadowing(FlowGraph fg) { // FIXME: variable declarations cannot shadow functions or parameters (see 10.1.3 and micro/test181.js) - but the programmer may think so
        for (Function f : fg.getFunctions()) {
            Map<String, DeclareFunctionNode> declared_functions = newMap();
            Map<String, DeclareVariableNode> declared_variables = newMap();
            for (AbstractNode n : f.getEntry().getNodes()) {
                if (n instanceof DeclareFunctionNode) {
                    DeclareFunctionNode node = (DeclareFunctionNode) n;
                    if (node.getFunction().getName() != null) {
                        declared_functions.put(node.getFunction().getName(), node);
                    }
                }
                if (n instanceof DeclareVariableNode) {
                    DeclareVariableNode node = (DeclareVariableNode) n;
                    declared_variables.put(node.getVariableName(), node);
                }
            }
            for (DeclareFunctionNode n : declared_functions.values()) {
                String function_name = n.getFunction().getName();
                if (declared_variables.containsKey(function_name)) {
                    DeclareVariableNode node = declared_variables.get(function_name);
                    addMessage(node, Status.CERTAIN, Severity.LOW, "The variable declaration " + Strings.escape(node.getVariableName()) + " shadows a function");
                }
            }
            for (DeclareVariableNode n : declared_variables.values()) {
                String var_name = n.getVariableName();
                if (f.getParameterNames().contains(var_name)) {
                    DeclareVariableNode node = declared_variables.get(var_name);
                    addMessage(node, Status.CERTAIN, Severity.LOW, "The variable declaration " + Strings.escape(node.getVariableName()) + " shadows a parameter");
                }
            }
        }
    }

    /**
     * Counts number of nodes of different kinds.
     */
    private void visitBeginScanPhase() {
        messages = newMap();
        scan_phase = true;
        for (Function f : flowgraph.getFunctions())
            for (BasicBlock b : f.getBlocks())
                for (AbstractNode n : b.getNodes())
                    n.visitBy((n1) -> n1.visitBy(new NodeVisitor() {

                        @Override
                        public void visit(NopNode n) {
                        }

                        @Override
                        public void visit(DeclareVariableNode n) {
                        }

                        @Override
                        public void visit(ConstantNode n) {
                        }

                        @Override
                        public void visit(NewObjectNode n) {
                        }

                        @Override
                        public void visit(UnaryOperatorNode n) {
                        }

                        @Override
                        public void visit(BinaryOperatorNode n) {
                        }

                        @Override
                        public void visit(ReadVariableNode n) {
                            if (!n.getVariableName().equals("this")) {
                                read_variable_nodes++;
                            }
                        }

                        @Override
                        public void visit(WriteVariableNode n) {
                        }

                        @Override
                        public void visit(ReadPropertyNode n) {
                            property_access_nodes++;
                            if (n.isPropertyFixed()) {
                                read_fixed_property_nodes++;
                            }
                        }

                        @Override
                        public void visit(WritePropertyNode n) {
                            property_access_nodes++;
                        }

                        @Override
                        public void visit(DeletePropertyNode n) {
                            if (!n.isVariable()) {
                                property_access_nodes++;
                            }
                        }

                        @Override
                        public void visit(IfNode n) {
                        }

                        @Override
                        public void visit(DeclareFunctionNode n) {
                        }

                        @Override
                        public void visit(CallNode n) {
                            call_nodes++;
                        }

                        @Override
                        public void visit(ReturnNode n) {
                        }

                        @Override
                        public void visit(ExceptionalReturnNode n) {
                        }

                        @Override
                        public void visit(ThrowNode n) {
                        }

                        @Override
                        public void visit(CatchNode n) {
                        }

                        @Override
                        public void visit(BeginWithNode n) {
                        }

                        @Override
                        public void visit(EndWithNode n) {
                        }

                        @Override
                        public void visit(BeginForInNode n) {
                        }

                        @Override
                        public void visit(NextPropertyNode n) {
                        }

                        @Override
                        public void visit(HasNextPropertyNode n) {
                        }

                        @Override
                        public void visit(EventDispatcherNode n) {
                        }

                        @Override
                        public void visit(EndForInNode n) {
                        }

                        @Override
                        public void visit(BeginLoopNode n) {

                        }

                        @Override
                        public void visit(EndLoopNode n) {

                        }
                    }));
    }

    private void emit() {
        if (!Options.get().isNoMessages()) {
            reportUnreachable();
            reportUnusedVariableOrParameter();
            reportDeadAssignments();
            reportShadowing(flowgraph);
            if (Options.get().isShowVariableInfoEnabled()) {
                type_collector.logTypeInformation();
            }
            // TODO: this de-duplication should happen somewhere else?
            Set<String> emittedOutput = newSet(); // avoid redundant output
            for (Message message : getSortedMessages()) {
                if (emittedOutput.contains(message.toString())) {
                    continue;
                }
                emittedOutput.add(message.toString());
                if (!Options.get().isShowInternalMessagesEnabled()) {
                    boolean isInternal = flowgraph.isHostEnvironmentSource(message.getNode().getSourceLocation());
                    if (isInternal) {
                        continue;
                    }
                }
                message.emit();
            }
        }

        if (Options.get().isNewFlowEnabled()) {
            TreeMap<Integer, String> sorted = new TreeMap<>();
            StringBuilder b = new StringBuilder();
            b.append("New flow at each function for each context:");
            for (Entry<BasicBlock, Map<Context, List<String>>> me1 : newflows.entrySet()) {
                Function f = me1.getKey().getFunction();
                b.append("\n").append(f).append(" at ").append(f.getSourceLocation()).append(":");
                for (Entry<Context, List<String>> me2 : me1.getValue().entrySet()) {
                    b.append("\n  ").append(me2.getKey()).append(" state diffs: ").append(me2.getValue().size());
                    for (String diff : me2.getValue()) {
                        if (diff != null) {
                            b.append("\n    state diff:").append(diff);
                        }
                    }
                    sorted.put(me2.getValue().size(), me1.getKey().getFunction() + " " + me1.getKey().getFunction().getSourceLocation() + ", context " + me2.getKey());
                }
            }
            b.append("\nSorted new flow:");
            for (Entry<Integer, String> me : sorted.entrySet())
                b.append("\n").append(me.getKey()).append(" new flows at ").append(me.getValue());
            log.info(b);
        }

        if (Options.get().isStatisticsEnabled()) {
            StringBuilder b = new StringBuilder();
            b.append("\nCall/construct nodes with potential call to non-function:                     ").append(call_to_non_function.size());
            b.append("\nTotal number of call/construct nodes:                                         ").append(call_nodes);
            b.append("\nCall/construct nodes that are certain to never call non-functions:            ").append(call_nodes > 0 ? ((call_nodes - call_to_non_function.size()) * 1000 / call_nodes) / 10f + "%" : "-");

            b.append("\n\nRead variable nodes with potential absent variable:                           ").append(absent_variable_read.size());
            b.append("\nTotal number of (non-this) read variable nodes:                               ").append(read_variable_nodes);
            b.append("\nRead variable nodes that are certain to never read absent variables:          ").append(read_variable_nodes > 0 ? ((read_variable_nodes - absent_variable_read.size()) * 1000 / read_variable_nodes) / 10f + "%" : "-");

            b.append("\n\nProperty access nodes with potential null/undef base:                         ").append(null_undef_base.size());
            b.append("\nTotal number of property access nodes:                                        ").append(property_access_nodes);
            b.append("\nProperty access nodes that are certain to never have null/undef base:         ").append(property_access_nodes > 0 ? ((property_access_nodes - null_undef_base.size()) * 1000 / property_access_nodes) / 10f + "%" : "-");

            b.append("\n\nProperty reads resulting in singleton types:                                  ").append(getSingletonPropertyReads());
            b.append("\nVariable reads resulting in singleton types:                                  ").append(getSingletonVariableReads());
            // FIXME: only compare with reads with typeSize>0 ? (typeSize is 0 for polymorphic values...)
            float p_var = getVarReadsSize() > 0 ? (float) getSingletonVariableReads() * 100 / getVarReadsSize() : -1;
            float p_prop = getPropReadsSize() > 0 ? (float) getSingletonPropertyReads() * 100 / getPropReadsSize() : -1;
            float p_all = !value_reads.isEmpty() ? (float) (getSingletonPropertyReads() + getSingletonVariableReads()) * 100 / value_reads.size() : -1;
            b.append("\nVariable reads with singleton results:                                        ").append(p_var == -1 ? "-" : p_var + "%");
            b.append("\nProperty reads with singleton results:                                        ").append(p_prop == -1 ? "-" : p_prop + "%");
            b.append("\nReads with singleton results:                                                 ").append(p_all == -1 ? "-" : p_all + "%");

            NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
            b.append("\n\nAverage type size in property reads:                                          ").append(formatter.format(getAveragePropertyTypeSize()));
            b.append("\nAverage type size in variable reads:                                          ").append(formatter.format(getAverageVariableTypeSize()));
            b.append("\nAverage type size in all reads:                                               ").append(formatter.format(geTotalAverageTypeSize()));
            b.append("\nReads with at most one type:                                                  ").append(formatter.format(getReadsWithAtMostOneType()));
            b.append("\nReads with at least two types:                                                ").append(formatter.format(getReadsWithAtleastTwoTypes()));

            b.append("\n\nFixed-property read nodes with potential absent property:                     ").append(absent_fixed_property_read.size());
            b.append("\nTotal number of fixed-property read nodes:                                    ").append(read_fixed_property_nodes);
            b.append("\nFixed-property read nodes that are certain to never have absent property:     ").append(read_fixed_property_nodes > 0 ? ((read_fixed_property_nodes - absent_fixed_property_read.size()) * 1000 / read_fixed_property_nodes) / 10f + "%" : "-");

            b.append("\n\nTotal number of functions:                                                    ").append(functions.size());
            b.append("\nNumber of unreachable functions:                                              ").append(functions.size() - reachabilityMonitor.getReachableFunctions().size()); // FIXME: reports "1" for an empty program

            b.append("\n\nNode transfers:                                                               ").append(node_transfers);
            b.append("\nBlock transfers:                                                              ").append(block_transfers);
            b.append("\nUnknown-value recoveries: \n" + " analysis: partial=").append(unknown_value_resolve_analyzing_partial).append(", full=").append(unknown_value_resolve_analyzing_full).append("\n").append(" scanning: partial=").append(unknown_value_resolve_scanning_partial).append(", full=").append(unknown_value_resolve_scanning_full);

            b.append("\n\nState joins:                                                                  ").append(joins).append("\n\n");

            b.append(callgraph.getCallGraphStatistics());

            b.append("\nBlockState: created=").append(State.getNumberOfStatesCreated()).append(", makeWritableStore=").append(State.getNumberOfMakeWritableStoreCalls());
            b.append("\nObj: created=").append(Obj.getNumberOfObjsCreated()).append(", makeWritableProperties=").append(Obj.getNumberOfMakeWritablePropertiesCalls());
            b.append("\nScopeChain cache: hits=").append(ScopeChain.getNumberOfCacheHits()).append(", misses=").append(ScopeChain.getNumberOfCacheMisses()).append(", finalSize=").append(ScopeChain.getCacheSize());
            b.append("\nBasic blocks: ").append(flowgraph.getNumberOfBlocks());
            b.append("\nRecovery graph sizes: ").append(recovery_graph_sizes);

            b.append("\n\nTotal time for state joins:                                                   ").append(joinTime).append("ms");

            b.append("\n\nCanonicalizer: hits=").append(Canonicalizer.get().getCacheHits()).append(", misses=").append(Canonicalizer.get().getCacheMisses());

            log.info(b);
        }

        if (Options.get().isEvalStatistics()) {
            StringBuilder b = new StringBuilder();
            Set<String> eval_const_use = newSet();
            Set<String> inner_const_use = newSet();
            Map<AbstractNode, String> eval_anystr_use = newMap();
            Map<AbstractNode, String> inner_anystr_use = newMap();
            sortMustFromMaybe(eval_const_use, eval_anystr_use, eval_calls);
            sortMustFromMaybe(inner_const_use, inner_anystr_use, inner_html_writes);
            b.append("Use of eval/innerHTML\n");
            b.append(" Constant eval'ed strings:\n");
            for (String s : eval_const_use) {
                b.append("  ").append(s).append("\n").append("  ==\n");
            }
            b.append(" Constant innerHTML strings:\n");
            for (String s : inner_const_use) {
                b.append("  ").append(s).append("\n").append("  ==\n");
            }
            b.append(" Source locations with maybe values for eval:\n");
            for (Entry<AbstractNode, String> g : eval_anystr_use.entrySet()) {
                b.append("   ").append(g.getKey().getSourceLocation()).append(": ").append(g.getValue());
            }
            b.append("\n Source locations with maybe values for innerHTML:\n");
            for (Entry<AbstractNode, String> g : inner_anystr_use.entrySet()) {
                b.append("   ").append(g.getKey().getSourceLocation()).append(": ").append(g.getValue());
            }
            log.info(b);
        }

        if (Options.get().isCallGraphEnabled()) {
            log.info(callgraph.toString());
            File outdir = new File("out");
            if (!outdir.exists()) {
                outdir.mkdir();
            }
            String filename = "out" + File.separator + "callgraph.dot";
            try (FileWriter f = new FileWriter(filename)) {
                log.info("Writing call graph to " + filename);
                callgraph.toDot(new PrintWriter(f));
            } catch (IOException e) {
                log.error("Unable to write " + filename + ": " + e.getMessage());
            }
        }
    }

    private void reportUnreachable() {
        Set<Function> unreachableFunctions = reachabilityMonitor.getUnreachableFunctions();
        Set<Function> reachableFunctions = reachabilityMonitor.getReachableFunctions();
        unreachableFunctions.forEach(f ->
                addMessage(f.getEntry().getFirstNode(), Status.CERTAIN, Severity.LOW, "Unreachable function" + (f.getName() != null ? " " + Strings.escape(f.getName()) : "")));
        reachableFunctions.stream().flatMap(f -> reachabilityMonitor.getUndominatedUnreachableNodes(f, true).stream()).forEach(n ->
                addMessage(n, Status.CERTAIN, Severity.LOW, "Unreachable code") // see also "The conditional expression is always true/false"
        );
        reportProgramExitReachability(flowgraph.getMain().getOrdinaryExit().getLastNode(), "Ordinary", Severity.HIGH);
        reportProgramExitReachability(flowgraph.getMain().getExceptionalExit().getLastNode(), "Exceptional", Severity.LOW);
    }

    private void reportProgramExitReachability(AbstractNode programExitNode, String kind, Severity severity) {
        if (!reachabilityMonitor.getReachableNodes().contains(programExitNode)) {
            addMessage(programExitNode, Status.CERTAIN, severity, kind + " program exit is unreachable");
        }
    }

    /**
     * Counts node transfers and registers reachability information.
     */
    @Override
    public void visitNodeTransferPre(AbstractNode n, State s) {
        reachabilityMonitor.visitNodeTransferPre(n, s);
        node_transfers++;
    }

    /**
     * Counts block transfers.
     */
    @Override
    public void visitBlockTransferPre(BasicBlock block, State state) {
        block_transfers++;
    }

    /**
     * Counts unknown-value-resolve operations.
     */
    @Override
    public void visitUnknownValueResolve(AbstractNode node, boolean partial, boolean scanning) {
        if (!Options.get().isStatisticsEnabled())
            return;
        if (scanning) {
            if (partial)
                unknown_value_resolve_scanning_partial++;
            else
                unknown_value_resolve_scanning_full++;
        } else {
            if (partial)
                unknown_value_resolve_analyzing_partial++;
            else
                unknown_value_resolve_analyzing_full++;
        }
    }

    /**
     * Counts state join operations and their time.
     */
    @Override
    public void visitJoin(long ms) {
        joins++;
        joinTime += ms;
    }

    /**
     * Collects new dataflow (if enabled).
     */
    @Override
    public void visitNewFlow(BasicBlock b, Context c, State s, String diff, String info) {
        if (Options.get().isNewFlowEnabled() && b.isEntry()) {
            if (diff != null) {
                addToMapList(newflows.computeIfAbsent(b, k -> newMap()), c, diff);
            }
//			if (info != null) {
//				try {
//					File outdir = new File("out" + File.separator + "newflows"); // TODO: separate files for separate contexts?
//					if (!outdir.exists()) {
//						outdir.mkdirs();
//					}
//					try (FileWriter fw = new FileWriter("out" + File.separator + "newflows" + File.separator + "line" + b.getFunction().getSourceLocation().getLineNumber() + "-" +
//							(next_newflow_file++) + "-" + info + ".dot")) {
//						fw.write(s.toDot());
//					}
//				} catch (IOException e) {
//					throw new AnalysisException(e);
//				}
//			}

        }
//					try (FileWriter fw = new FileWriter("out" + File.separator + "newflows" + File.separator + "line" + b.getFunction().getSourceLocation().getLineNumber() + "-" +
//							(next_newflow_file++) + "-" + info + ".dot")) {
//						fw.write(s.toDot());
//					}
//				} catch (IOException e) {
//					throw new AnalysisException(e);
//				}
//			}
//		}
    }

    /**
     * Registers reachability information.
     */
    @Override
    public void visitFunction(Function f, Collection<State> entry_states) {
        reachabilityMonitor.visitFunction(f, entry_states);
        functions.add(f);
    }

    /**
     * Checks whether an absent variable is read.
     */
    @Override
    public void visitReadNonThisVariable(ReadVariableNode n, Value v) {
        if (!scan_phase) {
            return;
        }
        Status s;
        if (v.isMaybeAbsent() && !v.isMaybePresent()) {
            s = Status.CERTAIN;
        } else if (v.isMaybeAbsent()) {
            s = Status.MAYBE;
        } else {
            s = Status.NONE;
        }
        if (s != Status.NONE) {
            absent_variable_read.add(n);
        }
        addMessage(n, s, Severity.HIGH, "ReferenceError, reading absent variable " + Strings.escape(n.getVariableName()));
    }

    /**
     * Checks whether the read of 'this' yields the global object or an imprecise value.
     */
    @Override
    public void visitReadThis(ReadVariableNode n, Value v, State state, ObjectLabel global_obj) {
        if (!scan_phase) {
            return;
        }
        v = UnknownValueResolver.getRealValue(v, state);
        Status s;
        if (v.getObjectLabels().contains(global_obj)) {
            if (v.getObjectLabels().size() == 1 && !v.isMaybePrimitive()) {
                s = Status.CERTAIN;
            } else {
                s = Status.MAYBE;
            }
        } else {
            s = Status.NONE;
        }
        addMessage(n, s, Severity.MEDIUM, "Reading 'this' yields the global object");
        if (v.getObjectLabels().size() > 1) {
            addMessage(n, Status.INFO, Severity.LOW,
                    "'this' refers to multiple abstract objects, which may cause loss of precision"); // ...but still sound!
            if (log.isDebugEnabled())
                log.debug("this = " + v);
        } else if (v.getObjectLabels().size() == 1 && !v.getObjectLabels().iterator().next().isSingleton() && !Options.get().isRecencyDisabled()) {
            addMessage(n, Status.INFO, Severity.LOW,
                    "'this' refers to a summary abstract object, which may cause loss of precision"); // ...but still sound!
            if (log.isDebugEnabled())
                log.debug("this = " + v);
        }
    }

    /**
     * Checks whether the variable read yields null/undefined.
     * Variables named 'undefined' and variable operands to 'typeof' are ignored.
     */
    @Override
    public void visitReadVariable(ReadVariableNode n, Value v, State state) {
        if (!scan_phase || n.isKeepAbsent()) {
            return;
        }
        String varname = n.getVariableName();
        if (varname.equals("undefined")) {
            return;
        }
        v = UnknownValueResolver.getRealValue(v, state);
        Status s;
        if (v.isNullOrUndef()) {
            s = Status.CERTAIN;
        } else if (v.isMaybeNull() || v.isMaybeUndef()) {
            s = Status.MAYBE;
        } else {
            s = Status.NONE;
        }
        addMessage(n, s, Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE, "Variable " + Strings.escape(varname) + " is null/undefined");
    }

    /**
     * Checks whether the branch condition is always true or always false.
     */
    @Override
    public void visitIf(IfNode n, Value v) { // see also "Unreachable code" and "Unreachable function"
        if (!scan_phase || n.isArtificial()) {
            return;
        }
        boolean is_maybe_true = v.isMaybeTrue();
        boolean is_maybe_false = v.isMaybeFalse();

        // NB these messages are more incomplete than what the abstract states would have us believe.
        // Example for the expression `x && y && z`: if `y` is always false, only the latter conjunction is reported as always false.
        // This is caused by sound, precision-improving flowgraph optimizations.
        // If more complete messages are desired, commit 0d3257a (TAJS-private/master-not-reviewed) should be reverted.
        addMessage(n, is_maybe_true ? (is_maybe_false ? Status.MAYBE : Status.CERTAIN) : Status.NONE,
                Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE, "The conditional expression is always true");
        addMessage(n, is_maybe_false ? (is_maybe_true ? Status.MAYBE : Status.CERTAIN) : Status.NONE,
                Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE, "The conditional expression is always false");
    }

    /**
     * Checks whether the 'instanceof' operation may fail with a TypeError.
     */
    @Override
    public void visitInstanceof(AbstractNode n,
                                boolean maybe_v2_non_function, boolean maybe_v2_function,
                                boolean maybe_v2_prototype_primitive, boolean maybe_v2_prototype_nonprimitive) {
        if (!scan_phase) {
            return;
        }
        Status s1;
        if (maybe_v2_non_function) {
            if (!maybe_v2_function) {
                s1 = Status.CERTAIN;
            } else {
                s1 = Status.MAYBE;
            }
        } else {
            s1 = Status.NONE;
        }
        addMessage(n, s1, Severity.HIGH, "TypeError, non-function object at 'instanceof'");
        Status s2;
        if (maybe_v2_prototype_primitive) {
            if (!maybe_v2_prototype_nonprimitive) {
                s2 = Status.CERTAIN;
            } else {
                s2 = Status.MAYBE;
            }
        } else {
            s2 = Status.NONE;
        }
        addMessage(n, s2, Severity.HIGH, "TypeError, non-object prototype at 'instanceof'");
    }

    /**
     * Checks whether the 'in' operation may fail with a TypeError.
     */
    @Override
    public void visitIn(AbstractNode n, boolean maybe_v2_object, boolean maybe_v2_nonobject) {
        if (!scan_phase) {
            return;
        }
        Status s;
        if (maybe_v2_nonobject) {
            if (!maybe_v2_object) {
                s = Status.CERTAIN;
            } else {
                s = Status.MAYBE;
            }
        } else {
            s = Status.NONE;
        }
        addMessage(n, s, Severity.HIGH, "TypeError, non-object at 'in'");
    }

    /**
     * Checks whether the property access operation may dereference null or undefined causing a TypeError.
     */
    @Override
    public void visitPropertyAccess(Node n, Value baseval) {
        if (!scan_phase) {
            return;
        }
        Status s;
        if (baseval.isNullOrUndef()) {
            s = Status.CERTAIN;
        } else if (baseval.isMaybeNull() || baseval.isMaybeUndef()) {
            s = Status.MAYBE;
        } else {
            s = Status.NONE;
        }
        if (s != Status.NONE) {
            null_undef_base.add(n);
        }
        addMessage(n, s, Severity.HIGH, "TypeError, accessing property of null/undefined");
    }

    /**
     * Checks whether the property read operation accesses an absent property and whether the operation returns null/undefined.
     */
    @Override
    public void visitReadProperty(ReadPropertyNode n, Set<ObjectLabel> objlabels, PKeys propertyname, boolean maybe, State state, Value v, ObjectLabel global_obj) {
        if (!scan_phase) {
            return;
        }
        Status s;
        if (!maybe && v.isMaybeAbsent() && !v.isMaybePresent()) {
            s = Status.CERTAIN;
        } else if (v.isMaybeAbsent()) {
            s = Status.MAYBE;
        } else {
            s = Status.NONE;
        }
        if (s != Status.NONE && n.isPropertyFixed()) {
            absent_fixed_property_read.add(n);
        }
        if (n.isPropertyFixed()) {
            if (objlabels.stream().anyMatch(l -> l.isHostObject() && !l.equals(global_obj))) {
                boolean isPartial = objlabels.stream().anyMatch(l -> l.isHostObject() && l.getHostObject().getAPI() == HostAPIs.PARTIAL_HOST_MODEL);
                if (isPartial) {
                    addMessage(n, s, Severity.MEDIUM, "Reading absent property " + Strings.escape(n.getPropertyString(), true) + " of partially modeled host object!");
                } else {
                    addMessage(n, s, Severity.MEDIUM, "Reading absent property " + Strings.escape(n.getPropertyString(), true) + " of host object");
                }
            } else {
                addMessage(n, s, Severity.MEDIUM, "Reading absent property " + Strings.escape(n.getPropertyString(), true));
            }
        } else {
            addMessage(n, s, Severity.LOW, "Reading absent property (computed name)");
        }
        Status s2;
        v = UnknownValueResolver.getRealValue(v, state);
        if (v.isNullOrUndef() && s != Status.CERTAIN) {
            s2 = Status.CERTAIN;
        } else if (v.isMaybeNull() || v.isMaybeUndef()) {
            s2 = Status.MAYBE;
        } else {
            s2 = Status.NONE;
        }
        addMessage(n, s2, Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE, "Property is null/undefined");
    }

    /**
     * Checks for reads from unknown properties;
     * also registers a read operation on abstract objects.
     * Properties named 'length' on array objects are ignored.
     */
    @Override
    public void visitPropertyRead(AbstractNode n, Set<ObjectLabel> objs, PKeys propertyname, State state, boolean check_unknown) {
        if (!scan_phase) {
            return;
        }
        // warn about potential loss of precision
        if (check_unknown && checkPropertyNameMayInterfereWithBuiltInProperties(propertyname)) {
            addMessage(n, Status.INFO, Severity.LOW,
                    "Reading from unknown property that may cause loss of precision"); // ...but still sound!
        }
        // register read operation on abstract object
        if (propertyname.isMaybeSingleStr() && propertyname.getStr().equals("length")) {
            // Proceed if we find an object label that is *not* an array.
            boolean only_array_length = true;
            for (ObjectLabel ol : objs)
                if (ol.getKind() != Kind.ARRAY) {
                    only_array_length = false;
                    break;
                }
            if (only_array_length)
                return;
        }
        Set<ObjectLabel> os = newSet();
        // Only give warnings for objects created by the user (getNode != null) since reading and writing properties
        // related to the DOM might be desirable for side effects.
        for (ObjectLabel objlabel : objs) {
            // TODO: objlabel.isHostObject() exists, but does not do precisely the same thing. Figure out what the correct behavior is.
            if (objlabel.getNode() != null) {
                for (ObjectLabel oo : (!propertyname.isMaybeSingleStr() ? state.getPrototypeWithProperty(objlabel, Value.makeAnyStr()) : state.getPrototypeWithProperty(objlabel, propertyname) /* TODO replace this expression with a lookup using the *coerced* property name! */)) { // TODO: this is also used for ReadVariableNode?
                    if (oo.getNode() != null) { // TODO: Only give warnings for user objects, others maybe DOM or similar with side effects
                        os.add(oo.makeSingleton());
                    }
                }
            }
        }
        for (ObjectLabel objlabel : os) {
            // Record reading of arguments[propertyname] as reading of the actual function arguments as well.
            if (objlabel.getKind() == Kind.ARGUMENTS) {
                Function f = n.getBlock().getFunction();
                List<String> args = f.getParameterNames();
                // Fall through if there are no arguments to the function or reading something
                // other than an array index.
                if (args == null || propertyname.isMaybeSingleStr() && !Strings.isArrayIndex(propertyname.getStr()))
                    continue;
                String arg = null;
                if (propertyname.isMaybeSingleStr() && Integer.valueOf(propertyname.getStr()) < args.size())
                    arg = args.get(Integer.valueOf(propertyname.getStr()));
                // Add all arguments if we aren't sure which we read.
                if (arg == null)
                    addAllToMapSet(read_variables, f, args);
                else
                    addToMapSet(read_variables, f, arg);
            }
            ObjReadsWrites i = obj_reads_writes.computeIfAbsent(objlabel, k -> new ObjReadsWrites());
            if (!propertyname.isMaybeSingleStr()) {
                i.readUnknown();
            } else if (os.size() == 1) {
                i.readDefinite(StringPKey.make(propertyname.getStr()));
            } else {
                i.readMaybe(StringPKey.make(propertyname.getStr()));
            }
        }
    }

    private static boolean checkPropertyNameMayInterfereWithBuiltInProperties(PKeys propertyname) {
        return !propertyname.isMaybeSingleStr() &&
                (propertyname.isMaybeStrIdentifier() || propertyname.isMaybeStrOtherIdentifierParts() ||
                        propertyname.isMaybeStrPrefix() || propertyname.isMaybeStrJSON()); // TODO: more precise pattern of what may interfere?
    }

    /**
     * Checks for writes to unknown properties;
     * also registers a write operation on abstract objects.
     * Properties named 'length' on array objects are ignored.
     * Writes to the arguments object are also ignored.
     */
    @Override
    public void visitPropertyWrite(Node n, Set<ObjectLabel> objs, PKeys propertyname) {
        if (!scan_phase) {
            return;
        }
        // warn about potential loss of precision
        if (checkPropertyNameMayInterfereWithBuiltInProperties(propertyname)) {
            addMessage(n,
                    Status.INFO,
                    Severity.MEDIUM,
                    "Writing to unknown property that may cause loss of precision"); // ...but still sound!
        }
        // register write operation on abstract object
        if (propertyname.isMaybeSingleStr() && propertyname.getStr().equals("length")) {
            // Proceed if we find an object label that is *not* an array.
            boolean only_array_length = true;
            for (ObjectLabel ol : objs)
                if (ol.getKind() != Kind.ARRAY) {
                    only_array_length = false;
                    break;
                }
            if (only_array_length)
                return;
        }
        Set<ObjectLabel> os = newSet();
        for (ObjectLabel o : objs) {
            // Local variables end up in activation record object labels.
            // We do not consider that a property write, so filter them out.
            if (o.getNode() != null && o.getKind() != Kind.ACTIVATION) {
                os.add(o.makeSingleton());
            }
        }
        for (ObjectLabel objlabel : os) {
            ObjReadsWrites i = obj_reads_writes.computeIfAbsent(objlabel, k -> new ObjReadsWrites());
            if (!propertyname.isMaybeSingleStr()) {
                i.writeUnknown(n);
            } else if (objs.size() == 1) {
                i.writeDefinite(StringPKey.make(propertyname.getStr()), n);
            } else {
                i.writeMaybe(StringPKey.make(propertyname.getStr()));
            }
        }
    }

    /**
     * Registers the name, location, and value of a variable or property being read or written.
     * Also checks for suspiciously imprecise values.
     */
    @Override
    public void visitVariableOrProperty(AbstractNode node, String var, SourceLocation loc, Value value, Context context, State state) {
        if (!scan_phase) {
            checkValueSuspicious(node, value);
        }
        if (scan_phase && Options.get().isShowVariableInfoEnabled() || pointer_analysis_active) {
            type_collector.record(var, loc, UnknownValueResolver.getRealValue(value, state), context);
        }
    }

    /**
     * Ignored.
     */
    @Override
    public void visitNativeFunctionReturn(AbstractNode node, HostObject hostObject, Value result) {
        // ignore
    }

    /**
     * Ignored.
     */
    @Override
    public void visitEventHandlerRegistration(AbstractNode node, Context context, Value handler) {
        // ignore
    }

    /**
     * Checks whether the function is invoked both as a constructor (with 'new') and as a function/method (without 'new').
     */
    @Override
    public void visitUserFunctionCall(Function f, AbstractNode call, boolean constructor) { // TODO: avoid warning if the call is a super-call in a constructor
        Status s;
        if (!scan_phase) {
            if (constructor) { // in non-scan phase, collect all constructor calls
                called_as_constructor.add(f);
            }
            return;
        } else if (!constructor && called_as_constructor.contains(f)) { // in scan phase, report non-constructor calls to functions that are also used in constructor calls
            s = Status.CERTAIN;
        } else
            s = Status.NONE;
        addMessage(call, // use the location of the non-constructor call node
                s,
                Severity.MEDIUM,
                "The function" + (f.getName() != null ? " " + Strings.escape(f.getName()) : "") + " is invoked both as constructor and function");
    }

    /**
     * Checks for call/construct to a non-function value causing a TypeError.
     * Also checks for suspiciously imprecise function values.
     */
    @Override
    public void visitCall(AbstractNode n, Value funval) {
        if (!scan_phase) {
            checkValueSuspicious(n, funval);
            return;
        }
        boolean maybe_non_function = funval.isMaybePrimitive() || funval.getObjectLabels().stream().anyMatch(objlabel -> objlabel.getKind() != Kind.FUNCTION);
        boolean maybe_function = funval.getObjectLabels().stream().anyMatch(objlabel -> objlabel.getKind() == Kind.FUNCTION);
        Status s = maybe_non_function ? (maybe_function ? Status.MAYBE : Status.CERTAIN) : Status.NONE;
        if (s != Status.NONE) {
            call_to_non_function.add(n);
        }
        addMessage(n, s, Severity.HIGH, "TypeError, call to non-function");
    }

    /**
     * Registers a call to eval.
     */
    @Override
    public void visitEvalCall(AbstractNode n, Value v) {
        if (!scan_phase || !Options.get().isEvalStatistics()) {
            return;
        }
        Value vv = eval_calls.get(n);
        if (vv != null) {
            v = v.join(vv);
        }
        eval_calls.put(n, v);
    }

    /**
     * Registers a write to innerHTML.
     */
    @Override
    public void visitInnerHTMLWrite(Node n, Value v) {
        if (!scan_phase || !Options.get().isEvalStatistics()) {
            return;
        }
        Value vv = inner_html_writes.get(n);
        if (vv != null) {
            v = v.join(vv);
        }
        inner_html_writes.put(n, v);
    }

    /**
     * Checks the number of parameters for a call to a native function.
     */
    @Override
    public void visitNativeFunctionCall(AbstractNode n, HostObject hostobject, boolean num_actuals_unknown, int num_actuals, int min, int max) {
        if (!scan_phase) {
            return;
        }
        Status s1 = (!num_actuals_unknown && num_actuals < min) ? Status.CERTAIN : Status.NONE;
        addMessage(n, s1, Severity.MEDIUM, "Too few parameters to native function");
        if (max != -1) {
            Status s2 = num_actuals_unknown ? Status.MAYBE : num_actuals > max ? Status.CERTAIN : Status.NONE;
            addMessage(n, s2, Severity.HIGH, "Too many parameters to native function");
        }
    }

    /**
     * Registers type information about a var/prop read.
     */
    @Override
    public void visitRead(Node n, Value v, State state) {
        if (!scan_phase) {
            return;
        }
        v = UnknownValueResolver.getRealValue(v, state); // it is not important to preserve polymorphic values during the scan phase
        value_reads.put(new NodeAndContext<>(n, state.getContext()), v);
    }

    /**
     * Registers that the given variable is read; also checks for suspicious type mixings.
     */
    @Override
    public void visitVariableAsRead(AbstractNode n, String varname, Value v, State state) {
        if (!scan_phase) {
            return;
        }
        // record in read_variables
        Function f = n.getBlock().getFunction();
        // The arguments object is special, but other than that walk upwards in scope to find where the variable belongs.
        while (!"arguments".equals(varname) && !f.getVariableNames().contains(varname) && !f.getParameterNames().contains(varname)) {
            // Only main should be without an outer function.
            if (!f.hasOuterFunction())
                break;
            f = f.getOuterFunction(); // FIXME: what about 'with' and 'catch' blocks?
        }
        addToMapSet(read_variables, f, varname);
        // report suspicious type mixings
        v = UnknownValueResolver.getRealValue(v, state);
        Status s; // TODO: join values across contexts when checking for suspicious type mixings?
        int i = 0;
        if (!v.isNotStr()) {
            i++;
        }
        if (!v.isNotNum()) {
            i++;
        }
        if (!v.isNotBool()) {
            i++;
        }
        if (v.isMaybeObject() || v.isMaybeNull()) {
            i++;
        }
        if (v.isMaybeSymbol()) {
            i++;
        }
        if (i > 1) {
            s = Status.MAYBE;
        } else {
            s = Status.NONE;
        }
        addMessage(n, s, Severity.LOW, "The variable " + Strings.escape(varname) + " has values with different types"); // TODO: also check property reads with different types?
    }

    /**
     * Registers the recovery graph size.
     */
    @Override
    public void visitRecoveryGraph(AbstractNode node, int size) {
        if (!Options.get().isStatisticsEnabled())
            return;
        Integer count = recovery_graph_sizes.get(size);
        if (count == null)
            count = 0;
        recovery_graph_sizes.put(size, count + 1);
    }

    private int getVarReadsSize() {
        int res = 0;
        for (NodeAndContext<Context> n : value_reads.keySet()) {
            if (n.getNode() instanceof ReadVariableNode) {
                res++;
            }
        }
        return res;
    }

    private int getPropReadsSize() {
        int res = 0;
        for (NodeAndContext<Context> n : value_reads.keySet()) {
            if (n.getNode() instanceof ReadPropertyNode) {
                res++;
            }
        }
        return res;
    }

    private double getAverageVariableTypeSize() {
        double res = 0;
        int div = 0;
        for (NodeAndContext<Context> n : value_reads.keySet()) {
            if (n.getNode() instanceof ReadVariableNode) {
                res += value_reads.get(n).typeSize();
                div++;
            }
        }
        if (div == 0) {
            return 0;
        }
        res = (1000 * res) / div;
        return res / 1000;
    }

    private double getAveragePropertyTypeSize() {
        double res = 0;
        int div = 0;
        for (NodeAndContext<Context> n : value_reads.keySet()) {
            if (n.getNode() instanceof ReadPropertyNode) {
                res += value_reads.get(n).typeSize();
                div++;
            }
        }
        if (div == 0) {
            return 0;
        }
        res = (1000 * res) / div;
        return res / 1000;
    }

    private double geTotalAverageTypeSize() {
        double res = 0;
        for (NodeAndContext<Context> n : value_reads.keySet()) {
            res += value_reads.get(n).typeSize();
        }
        res = (1000 * res) / value_reads.size();
        return res / 1000;
    }

    private int getReadsWithAtMostOneType() {
        int res = 0;
        for (NodeAndContext<Context> n : value_reads.keySet()) {
            if (value_reads.get(n).typeSize() <= 1) {
                res++;
            }
        }
        return res;
    }

    private int getReadsWithAtleastTwoTypes() {
        int res = 0;
        for (NodeAndContext<Context> n : value_reads.keySet()) {
            if (value_reads.get(n).typeSize() > 1) {
                res++;
            }
        }
        return res;
    }

    private int getSingletonPropertyReads() {
        int res = 0;
        for (NodeAndContext<Context> n : value_reads.keySet()) {
            if (n.getNode() instanceof ReadPropertyNode) {
                if (value_reads.get(n).typeSize() == 1)
                    res++;
            }
        }
        return res;
    }

    private int getSingletonVariableReads() {
        int res = 0;
        for (NodeAndContext<Context> n : value_reads.keySet()) {
            if (n.getNode() instanceof ReadVariableNode) {
                if (value_reads.get(n).typeSize() == 1)
                    res++;
            }
        }
        return res;
    }

    private static void sortMustFromMaybe(
            Set<String> eval_const_use,
            Map<AbstractNode, String> eval_anystr_use,
            Map<AbstractNode, Value> m) {
        for (Entry<AbstractNode, Value> e : m.entrySet()) {
            AbstractNode n = e.getKey();
            Value v = e.getValue();
            if (v.getStr() != null) {
                eval_const_use.add(v.getStr());
            } else {
                eval_anystr_use.put(n, v.toString());
            }
        }
    }

    /**
     * Adds a message for the given node. If not in scan phase, nothing is done.
     * Uses Status.INFO.
     * Uses the message as key (must be a fixed string).
     */
    @Override
    public void addMessageInfo(AbstractNode n, Severity severity, String msg) {
        addMessage(n, Status.INFO, severity, msg);
    }

    /**
     * Adds a message for the given node. If not in scan phase, nothing is done.
     * Uses Status.MAYBE.
     * Uses the message as key (must be a fixed string).
     */
    @Override
    public void addMessage(AbstractNode n, Severity severity, String msg) {
        addMessage(n, Status.MAYBE, severity, msg);
    }

    /**
     * Adds a message for the given node. If not in scan phase, nothing is done.
     * If the message already exists, the status is joined.
     * Uses the message as key (must be a fixed string).
     */
    private void addMessage(AbstractNode n, Status s, Severity severity, String msg) { // TODO: collect all message generation in AnalysisMonitor? (then make addMessage private?)
        addMessage(n, s, severity, msg, msg);
    }

    /**
     * Adds a message for the given node. If not in scan phase, nothing is done.
     * Uses Status.MAYBE.
     * The key must be a fixed string.
     */
    @Override
    public void addMessage(AbstractNode n, Severity severity, String key, String msg) {
        addMessage(n, Status.MAYBE, severity, key, msg);
    }

    /**
     * Adds a message for the given node. If not in scan phase, nothing is done.
     * If the message already exists, the status is joined.
     * The key must be a fixed string.
     */
    private void addMessage(AbstractNode n, Status s, Severity severity, String key, String msg) {
        if (scan_phase) {
            boolean dump;
            AbstractNode d = n.getDuplicateOf();
            if (d != null) // if n is a duplicate, use the original instead
                n = d;
            boolean ifGeneratedOrPseudoNode = n.getSourceLocation().getLocation() == null;
            Message m = new Message(n, s, key, msg, severity, ifGeneratedOrPseudoNode);
            Message mo = messages.get(m);
            if (mo != null) {
                Status old = mo.getStatus();
                mo.join(m);
                dump = old != mo.getStatus();
            } else {
                messages.put(m, m);
                dump = s != Status.NONE;
            }
            if (dump && log.isDebugEnabled()) {
                log.debug("addMessage: " + m.getStatus() + " " + m.getNode().getSourceLocation() + ": " + m.getMessage());
            }
        }
    }

    /**
     * Returns the collected messages.
     */
    public Set<Message> getMessages() {
        return newSet(messages.values());
    }

    /**
     * Returns the sorted list of messages produced during scanning.
     * Ignores those messages that have Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE if not Status.CERTAIN
     * and those that have Severity.LOW if not the option low-severity is enabled.
     */
    public List<Message> getSortedMessages() {
        List<Message> es = newList();
        for (Message m : messages.values()) {
            if (m.getStatus() != Status.NONE
                    && !(m.getSeverity() == Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE && m.getStatus() != Status.CERTAIN)
                    && !(m.getSeverity() == Severity.LOW && !Options.get().isLowSeverityEnabled())) {
                es.add(m);
            }
        }
        Collections.sort(es);
        return es;
    }

    /**
     * Returns the collected type information.
     * (Used by the Eclipse plugin.)
     * @return
     */
    public Map<Tuple<String, Integer>, Value> getTypeInformation() {
        return type_collector.getPointsToMap();
    }

    @Override
    public boolean allowNextIteration() {
        return true;
    }

    @Override
    public void visitPhasePre(AnalysisPhase phase) {
        if (phase == AnalysisPhase.SCAN) {
            visitBeginScanPhase();
        }
    }

    @Override
    public void visitPhasePost(AnalysisPhase phase) {
        if (phase == AnalysisPhase.SCAN) {
//            reachabilityMonitor.check(); // TODO: currently disabled, enable by option?
            emit();
        }
    }

    /**
     * Ignored.
     */
    @Override
    public void visitBlockTransferPost(BasicBlock b, State state) {
        // ignore
    }

    /**
     * Ignored.
     */
    @Override
    public void visitPropagationPre(BlockAndContext<Context> from, BlockAndContext<Context> to) {
        // ignore
    }

    /**
     * Ignored.
     */
    @Override
    public void visitPropagationPost(BlockAndContext<Context> from, BlockAndContext<Context> to, boolean changed) {
        // ignore
    }

    /**
     * Ignored.
     */
    @Override
    public void visitNodeTransferPost(AbstractNode n, State s) {
        // ignore
    }

    /**
     * Ignored.
     */
    @Override
    public void visitNewObject(AbstractNode node, ObjectLabel label, State s) {
        // ignore
    }

    /**
     * Ignored.
     */
    @Override
    public void visitRenameObject(AbstractNode node, ObjectLabel from, ObjectLabel to, State s) {
        // ignore
    }

    /**
     * Ignored.
     */
    @Override
    public void visitIterationDone(String terminatedEarlyMsg) {
        // ignore
    }

    @Override
    public void visitSoundnessTestingDone(int numSoundnessChecks) {
        this.numSoundnessChecks = numSoundnessChecks;
    }

    public int getNumberOfSoundnessChecks() {
        return numSoundnessChecks;
    }

    private void checkValueSuspicious(AbstractNode n, Value v) {
        if (Options.get().isTestEnabled() || Options.get().isQuietEnabled())
            return;
        int native_functions = 0;
        int user_functions = 0;
        for (ObjectLabel objlabel : v.getObjectLabels()) {
            if (objlabel.getKind() == ObjectLabel.Kind.FUNCTION) {
                if (objlabel.isHostObject()) {
                    native_functions++;
                } else {
                    user_functions++;
                }
                if (native_functions >= 2 && user_functions >= 1) {
                    if (!log.isDebugEnabled() && log.isInfoEnabled()) {
                        System.out.print("\r");
                    }
                    log.warn("Likely significant loss of precision (mix of multiple native and non-native functions) at " +
                            n.getClass().getSimpleName() + " " + n.getSourceLocation());
                    return;
                }
            }
        }
    }
}
