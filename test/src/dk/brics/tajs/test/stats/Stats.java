package dk.brics.tajs.test.stats;

import com.google.gson.stream.JsonWriter;
import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.DefaultNodeVisitor;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.PKeys;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.AnalysisMonitor;
import dk.brics.tajs.monitoring.AnalysisPhase;
import dk.brics.tajs.monitoring.CompositeMonitor;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.PhaseMonitoring;
import dk.brics.tajs.monitoring.ProgressMonitor;
import dk.brics.tajs.options.OptionValues;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.AnalysisException;
import org.kohsuke.args4j.CmdLineException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

public class Stats {

    public static void run(String outfile, int secondsTimeLimit, int nodeTransferLimit, Optional<OptionValues> initialOptions, String[][]... tests) throws IOException, CmdLineException {
        Path statDir = Paths.get("out/stats");
        Path f = statDir.resolve(outfile + ".jsonp");
        Path statsFile = statDir.resolve("stats.html");
        Path datafilesFile = statDir.resolve("datafiles.jsonp");
        //noinspection ResultOfMethodCallIgnored
        statDir.toFile().mkdirs();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("stats.html")) {
            if (is == null)
                throw new IOException("stats.html not found");
            Files.copy(is, statsFile, StandardCopyOption.REPLACE_EXISTING);
        }
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("datafiles.jsonp")) {
            if (is == null)
                throw new IOException("datafiles.jsonp not found");
            Files.copy(is, datafilesFile, StandardCopyOption.REPLACE_EXISTING);
        }
        try (FileWriter fw = new FileWriter(f.toFile())) {
            Main.initLogging();
            JsonWriter w = new JsonWriter(fw);
            String machine = System.getenv("CI_RUNNER_DESCRIPTION");
            if (machine == null)
                machine = System.getenv("COMPUTERNAME");
            if (machine == null)
                machine = System.getenv("HOSTNAME");
            if (machine == null)
                machine = "?";
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            long cpustart = threadMXBean.getCurrentThreadCpuTime();
            fw.write("timestamp = " + System.currentTimeMillis() + ";\n");
            fw.write("machine = \"" + machine + "\";\n");
            fw.write("defaultOptions = \"" + (initialOptions.isPresent() ? initialOptions.get() : "") + "\";\n");
            fw.write("data = ");
            w.beginArray();
            int numberOfTests = Arrays.stream(tests).mapToInt(ts -> ts.length).sum();
            int currentTest = 0;
            for (String[][] testset : tests) {
                for (String[] testArgs : testset) {
                    System.out.format("[%d/%d] %s\n", ++currentTest, numberOfTests, String.join(" ", testArgs));
                    OptionValues options = initialOptions.map(OptionValues::clone).orElseGet(OptionValues::new);
                    options.parse(testArgs);
                    options.checkConsistency();
                    options.enableNoMessages();
                    options.setAnalysisTimeLimit(secondsTimeLimit);
                    options.setAnalysisTransferLimit(nodeTransferLimit);
                    options.enableAnalysisLimitationWarnOnly();
                    if (System.getProperty("statsquiet", "false").equalsIgnoreCase("true"))
                        options.enableQuiet();

                    ProgressMonitor progressMonitor = new ProgressMonitor(false);
                    SuspiciousnessMonitor suspiciousnessMonitor = new SuspiciousnessMonitor();
                    TerminationMonitor terminationMonitor = new TerminationMonitor();
                    PrecisionMonitor precisionMonitor = new PrecisionMonitor();
                    Analysis a = null;
                    Throwable throwable = null;
                    try {
                        a = Main.init(options, CompositeMonitor.make(new AnalysisMonitor(), progressMonitor, suspiciousnessMonitor, terminationMonitor, precisionMonitor), null);
                        if (a == null)
                            throw new AnalysisException("Error during initialization");
                        Main.run(a);
                    } catch (Throwable e) {
                        if (terminationMonitor.getTerminatedEarlyMsg() == null)
                            System.out.println("Error: " + e.getMessage());
                        throwable = e;
                    }
                    w.beginObject();
                    String name = testArgs[testArgs.length - 1];
                    String exceptionMsg = throwable != null ? throwable.getMessage() : null;
                    String terminatedEarlyMsg = terminationMonitor.getTerminatedEarlyMsg();
                    String errorMsg = exceptionMsg != null ? exceptionMsg : terminatedEarlyMsg != null ? terminatedEarlyMsg : "";
                    w.name("name").value(name.replace("test-resources/src", "").replace("benchmarks/tajs/src", ""));
                    w.name("options").value(Arrays.stream(testArgs).filter(s -> !s.endsWith(".js") && !s.endsWith(".html")).collect(java.util.stream.Collectors.joining(" ")));
                    if (errorMsg.isEmpty()) {
                        w.name("error").value("");
                    } else {
                        w.name("error").value(categorizeErrorMsg(errorMsg) + ": " + (errorMsg.length() > 500 ? errorMsg.substring(0, 500) + "..." : errorMsg));
                    }
                    if (a != null) {
                        long time = progressMonitor.getPreScanMonitor().getAnalysisTime();
                        w.name("time").value(((double)time)/1000);
                        w.name("node_transfers").value(progressMonitor.getPreScanMonitor().getNodeTransfers());
                        w.name("visited_usercode_node").value(progressMonitor.getPreScanMonitor().getVisitedNonHostNodes().size());
                        w.name("transfers_per_visited_node").value(!progressMonitor.getPreScanMonitor().getVisitedNonHostNodes().isEmpty() ? ((double) progressMonitor.getPreScanMonitor().getNodeTransfers()) / progressMonitor.getPreScanMonitor().getVisitedNonHostNodes().size() : -1);
                        w.name("visited_div_total_nodes").value(a.getSolver().getFlowGraph().getNumberOfUserCodeNodes() != 0 ? ((double) progressMonitor.getPreScanMonitor().getVisitedNonHostNodes().size()) / a.getSolver().getFlowGraph().getNumberOfUserCodeNodes() : -1);
                        w.name("total_usercode_nodes").value(a.getSolver().getFlowGraph().getNumberOfUserCodeNodes());
                        w.name("abstract_states").value(a.getSolver().getAnalysisLatticeElement().getNumberOfStates());
                        w.name("states_per_block").value(((double) a.getSolver().getAnalysisLatticeElement().getNumberOfStates()) / a.getSolver().getFlowGraph().getNumberOfBlocks());
                        w.name("average_state_size").value(((double) progressMonitor.getPreScanMonitor().getStateSize()) / a.getSolver().getAnalysisLatticeElement().getNumberOfStates());
                        w.name("average_node_transfer_time").value(progressMonitor.getPreScanMonitor().getNodeTransfers() != 0 ? ((double)time / progressMonitor.getPreScanMonitor().getNodeTransfers()) : -1);
                        w.name("callgraph_edges").value(a.getSolver().getAnalysisLatticeElement().getCallGraph().getSizeIgnoringContexts());
                        w.name("total_call_nodes").value(suspiciousnessMonitor.getScanMonitor().getNumberOfCallNodes());
                        w.name("callnodes_to_nonfunction").value(suspiciousnessMonitor.getScanMonitor().getCallToNonFunction().size());
                        w.name("callnodes_to_mixed_functions").value(suspiciousnessMonitor.getScanMonitor().getCallToMixedFunctions().size());
                        w.name("callnodes_polymorphic").value(suspiciousnessMonitor.getScanMonitor().getCallPolymorphic().size());
                        w.name("mixed_readwrites").value(suspiciousnessMonitor.getScanMonitor().getMixedReadOrWrite().size());
                        w.name("average_types").value(precisionMonitor.getScanMonitor().getAverageNumberOfTypesAtReads());
                        w.name("unique_type").value(precisionMonitor.getScanMonitor().getFractionUniqueTypesAtReads());
                        w.name("unique_callee").value(precisionMonitor.getScanMonitor().getFractionUniqueCallees());
                    }
                    // TODO: average and max suspiciousness at function value at call, property name at dynamic-property-accesses, value at read-property, value at read-variable
                    w.endObject();
                    w.flush();
                    Main.reset();
                    System.gc();
                }
            }
            w.endArray();
            fw.write(";\ncputime = " + (threadMXBean.getCurrentThreadCpuTime() - cpustart) + ";\n");
        }
        System.out.println("Output written to " + f + ", open stats.html?" + outfile + " in a browser to view the results");
    }

    private static String categorizeErrorMsg(String errorMsg) {
        if (errorMsg.contains("Likely significant loss of precision (mix of multiple native and non-native functions)")
                || errorMsg.contains("Too imprecise calls to Function")
                || errorMsg.contains("Unevalable eval")) {
            return "[Precision loss]";
        }
        if (errorMsg.contains("not yet supported") || errorMsg.contains("let is not supported") || errorMsg.contains("No support for")) {
            return "[Syntactic limitations]";
        }
        if (errorMsg.contains("No transfer function for native function")
                || errorMsg.contains("Error.captureStackTrace")
                || errorMsg.contains("Array.prototype.values")) {
            return "[EcmaScript modelling]";
        }
        if (errorMsg.contains("No model for Partial")) {
            return "[Node modelling]";
        }
        if (errorMsg.contains("Terminating fixpoint solver early and unsoundly!")
                || errorMsg.contains("GC overhead limit exceeded")
                || errorMsg.contains("Java heap space")) {
            return "[Scalability]";
        }
        if (errorMsg.contains("TAJS_nodeRequireResolve")) {
            return "[Require]";
        }
        if (errorMsg.contains("SoundnessTesting failed")) {
            return "[Soundness failure]";
        }
        if (errorMsg.contains("Log file indicates at the instrumentation was unsuccessful")
                || errorMsg.contains("Unhandled result kind: syntax-error")
                || errorMsg.contains("Something went wrong while checking location")) {
            return "[Log file error]";
        }
        return "[Other]";
    }

    public static class TerminationMonitor extends DefaultAnalysisMonitoring {

        private String terminatedEarlyMsg = null;

        public String getTerminatedEarlyMsg() {
            return terminatedEarlyMsg;
        }

        @Override
        public void visitIterationDone(String terminatedEarlyMsg) {
            this.terminatedEarlyMsg = terminatedEarlyMsg;
        }

    }

    public static class SuspiciousnessMonitor extends PhaseMonitoring<DefaultAnalysisMonitoring, SuspiciousnessMonitor.ScanSuspiciousnessMonitor> { // TODO: would be nice to be able to extract information from other monitors...

        public SuspiciousnessMonitor() {
            super(new DefaultAnalysisMonitoring(), new SuspiciousnessMonitor.ScanSuspiciousnessMonitor());
        }

        public static class ScanSuspiciousnessMonitor extends DefaultAnalysisMonitoring {

            private Solver.SolverInterface c;

            /**
             * Number of call/construct nodes.
             */
            private int call_nodes = 0;

            /**
             * Call/construct nodes that may involve non-function non-undefined values.
             */
            private Set<AbstractNode> call_to_non_function_or_undef = newSet();

            /**
             * Variable/property read/write nodes where the value may involve a mix of two or more native and one or more user-defined functions in the same call context.
             */
            private Set<AbstractNode> mixed_readwrites = newSet();

            /**
             * Call/construct nodes that may involve a mix of two or more native and one or more user-defined functions in the same call context.
             */
            private Set<AbstractNode> call_to_mixed_functions = newSet();

            /**
             * Call/construct nodes that may involve calls to multiple user-defined functions in the same call context (ignoring callee contexts).
             */
            private Set<AbstractNode> call_polymorphic = newSet();

            public int getNumberOfCallNodes() {
                return call_nodes;
            }

            public Set<AbstractNode> getCallToNonFunction() {
                return call_to_non_function_or_undef;
            }

            public Set<AbstractNode> getMixedReadOrWrite() {
                return mixed_readwrites;
            }

            public Set<AbstractNode> getCallToMixedFunctions() {
                return call_to_mixed_functions;
            }

            public Set<AbstractNode> getCallPolymorphic() {
                return call_polymorphic;
            }

            @Override
            public void setSolverInterface(Solver.SolverInterface c) {
                this.c = c;
            }

            @Override
            public void visitPhasePre(AnalysisPhase phase) {
                for (Function f : c.getFlowGraph().getFunctions()) {
                    for (BasicBlock b : f.getBlocks()) {
                        for (AbstractNode n : b.getNodes()) {
                            n.visitBy(new DefaultNodeVisitor() {
                                @Override
                                public void visit(CallNode n) {
                                    call_nodes++;
                                }
                            });
                        }
                    }
                }
            }

            private long getNumberOfNativeFunctions(Set<ObjectLabel> objs) {
                return objs.stream().filter(objlabel -> objlabel.isHostObject() && objlabel.getKind() == ObjectLabel.Kind.FUNCTION).map(ObjectLabel::getHostObject).count();
            }

            private long getNumberOfNonNativeFunctions(Set<ObjectLabel> objs) {
                return objs.stream().filter(objlabel -> !objlabel.isHostObject() && objlabel.getKind() == ObjectLabel.Kind.FUNCTION).map(ObjectLabel::getHostObject).count();
            }

            private boolean isMixed(Value v) {
                Set<ObjectLabel> objs = v.getObjectLabels();
                return getNumberOfNativeFunctions(objs) >= 2 && getNumberOfNonNativeFunctions(objs) >= 1;
            }

            @Override
            public void visitVariableOrProperty(AbstractNode n, String var, SourceLocation loc, Value value, Context context, State state) {
                if (isMixed(value)) {
                    mixed_readwrites.add(n);
                }
            }

            @Override
            public void visitCall(AbstractNode n, Value funval) {
                boolean is_primitive_non_undef = funval.restrictToNotUndef().isMaybePrimitive();
                boolean is_non_function_object = funval.getObjectLabels().stream().anyMatch(objlabel -> objlabel.getKind() != ObjectLabel.Kind.FUNCTION);
                if (is_primitive_non_undef || is_non_function_object) {
                    call_to_non_function_or_undef.add(n);
                }
                if (isMixed(funval)) {
                    call_to_mixed_functions.add(n);
                }
                if (getNumberOfNonNativeFunctions(funval.getObjectLabels()) > 1) {
                    call_polymorphic.add(n);
                }
            }
        }
    }

    public static class PrecisionMonitor extends PhaseMonitoring<DefaultAnalysisMonitoring, PrecisionMonitor.ScanPrecisionMonitor> { // TODO: would be nice to be able to extract information from other monitors...

        public PrecisionMonitor() {
            super(new DefaultAnalysisMonitoring(), new ScanPrecisionMonitor());
        }

        public static class ScanPrecisionMonitor extends DefaultAnalysisMonitoring {

            /**
             * Number of call/construct nodes visited context sensitively.
             */
            private int call_nodes_context_sensitively = 0;

            /**
             * Number of reachable read variable nodes context sensitively.
             */
            private int number_read_variables_with_single_type = 0;

            /**
             * Number of reachable read property nodes context sensitively.
             */
            private int number_read_property_with_single_type = 0;

            /**
             * Call/construct nodes that may involve calls to multiple user-defined functions in the same call context (ignoring callee contexts).
             */
            private Set<AbstractNode> call_polymorphic = newSet();

            /**
             * ReadVariableNode/ReadPropertyNode and contexts that yield values of different type.
             */
            private Map<NodeAndContext<Context>, Integer> polymorphic_reads_context_sensitive = newMap();

            public ScanPrecisionMonitor() {}

            /**
             * Returns the average number of types read at ReadVariableNodes and ReadPropertyNodes (context sensitively).
             */
            public float getAverageNumberOfTypesAtReads() {
                float numberPreciseReads = number_read_property_with_single_type + number_read_variables_with_single_type;
                float totalNumberTypes = numberPreciseReads + polymorphic_reads_context_sensitive.values().stream().reduce(0, Integer::sum);
                float totalNumberReads = numberPreciseReads + polymorphic_reads_context_sensitive.size();
                return totalNumberReads != 0 ? totalNumberTypes / totalNumberReads : -1;
            }

            /**
             * Returns the fraction of ReadVariableNodes and ReadPropertyNodes with unique type (context sensitively).
             */
            public float getFractionUniqueTypesAtReads() {
                float numberPreciseReads = number_read_property_with_single_type + number_read_variables_with_single_type;
                float totalNumberReads = numberPreciseReads + polymorphic_reads_context_sensitive.size();
                return totalNumberReads != 0 ? numberPreciseReads / totalNumberReads : -1;
            }

            /**
             * Returns the fraction of CallNodes with unique callee (context sensitively).
             */
            public float getFractionUniqueCallees() {
                float numberCallNodes = call_nodes_context_sensitively;
                float numberPreciseCallNodes = numberCallNodes - call_polymorphic.size();
                return numberCallNodes != 0 ? numberPreciseCallNodes / numberCallNodes : -1;
            }

            @Override
            public void visitNodeTransferPre(AbstractNode n, State s) {
                if (n instanceof CallNode) {
                    call_nodes_context_sensitively++;
                }
            }

            @Override
            public void visitReadProperty(ReadPropertyNode n, Set<ObjectLabel> objlabels, PKeys propertyname, boolean maybe, State state, Value v, ObjectLabel global_obj) {
                int numberTypes = UnknownValueResolver.getRealValue(v, state).typeSize();
                if (numberTypes <= 1) {
                    number_read_property_with_single_type++;
                } else {
                    polymorphic_reads_context_sensitive.put(new NodeAndContext<>(n, state.getContext()), numberTypes);
                }
            }

            @Override
            public void visitReadVariable(ReadVariableNode n, Value v, State state) {
                int numberTypes = UnknownValueResolver.getRealValue(v, state).typeSize();
                if (numberTypes <= 1) {
                    number_read_variables_with_single_type++;
                } else {
                    polymorphic_reads_context_sensitive.put(new NodeAndContext<>(n, state.getContext()), numberTypes);
                }
            }

            @Override
            public void visitCall(AbstractNode n, Value funval) {
                if (funval.getObjectLabels().size() > 1) {
                    call_polymorphic.add(n);
                }
            }
        }
    }
}
