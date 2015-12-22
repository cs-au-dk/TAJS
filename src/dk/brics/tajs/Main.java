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

package dk.brics.tajs;

import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.analysis.StaticDeterminacyContextSensitivityStrategy;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.HostEnvSources;
import dk.brics.tajs.flowgraph.JavaScriptSource;
import dk.brics.tajs.flowgraph.JavaScriptSource.Kind;
import dk.brics.tajs.htmlparser.HTMLParser;
import dk.brics.tajs.js2flowgraph.FlowGraphBuilder;
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.AnalysisPhase;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.ExperimentalOptions;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.SolverSynchronizer;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Loader;
import dk.brics.tajs.util.Strings;
import net.htmlparser.jericho.Source;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import static dk.brics.tajs.util.Collections.newList;

/**
 * Main class for the TAJS program analysis.
 */
public class Main {

    private static Logger log = Logger.getLogger(Main.class);

    private Main() {
    }

    /**
     * Runs the analysis on the given source files.
     * Run without arguments to see the usage.
     * Terminates with System.exit.
     */
    public static void main(String[] args) {
        try {
            initLogging();
            Analysis a = init(args, null);
            if (a == null)
                System.exit(-1);
            run(a);
            System.exit(0);
        } catch (AnalysisException e) {
            e.printStackTrace();
            System.exit(-2);
        }
    }

    /**
     * Resets all internal counters and caches.
     */
    public static void reset() {
        StaticDeterminacyContextSensitivityStrategy.SyntacticHints.reset();
        ExperimentalOptions.ExperimentalOptionsManager.reset();
        Options.reset();
        State.reset();
        Value.reset();
        Obj.reset();
        Strings.reset();
        ScopeChain.reset();
    }

    /**
     * Reads the input and prepares an analysis object, using the default monitoring.
     */
    public static Analysis init(String[] args, SolverSynchronizer sync) throws AnalysisException {
        return init(args, new Monitoring(), sync);
    }

    /**
     * Reads the input and prepares an analysis object.
     *
     * @return analysis object, null if invalid input
     * @throws AnalysisException if internal error
     */
    public static Analysis init(String[] args, IAnalysisMonitoring monitoring, SolverSynchronizer sync) throws AnalysisException {
        Analysis analysis = new Analysis(monitoring, sync);

        boolean show_usage = false;
        Options.parse(args);
        Options.get().checkConsistency();
        List<String> files = Options.get().getArguments();

        if (files.isEmpty()) {
            System.out.println("No source files");
            show_usage = true;
        }
        if (show_usage) {
            System.out.println("TAJS - Type Analyzer for JavaScript");
            System.out.println("Copyright 2009-2015 Aarhus University\n");
            System.out.println("Usage: java -jar tajs-all.jar [OPTION]... [FILE]...\n");
            Options.get().describe();
            return null;
        }
        if (Options.get().isDebugEnabled())
            Options.dump();

        enterPhase(AnalysisPhase.LOADING_FILES, analysis.getMonitoring());
        Source document = null;
        FlowGraph fg;
        try {
            // split into JS files and HTML files
            String html_file = null;
            List<String> js_files = newList();
            for (String fn : files) {
                String l = fn.toLowerCase();
                if (l.endsWith(".html") || l.endsWith(".xhtml") || l.endsWith(".htm")) {
                    if (html_file != null)
                        throw new AnalysisException("Only one HTML file can be analyzed at a time.");
                    html_file = fn;
                } else
                    js_files.add(fn);
            }
            FlowGraphBuilder builder = new FlowGraphBuilder(String.join(",", files));
            builder.transformHostFunctionSources(HostEnvSources.get());
            if (!js_files.isEmpty()) {
                if (html_file != null)
                    throw new AnalysisException("Cannot analyze a HTML file and JavaScript files at the same time.");
                // build flowgraph for JS files
                for (String js_file : js_files) {
                    if (!Options.get().isQuietEnabled())
                        log.info("Loading " + js_file);
                    builder.transformStandAloneCode(JavaScriptSource.makeFileCode(js_file, Loader.getString(js_file, "UTF-8")));
                }
            } else {
                // build flowgraph for JavaScript code in or referenced from HTML file
                Options.get().enableIncludeDom(); // always enable DOM if any HTML files are involved
                if (!Options.get().isQuietEnabled())
                    log.info("Loading " + html_file);
                HTMLParser p = new HTMLParser(html_file);
                document = p.getHTML();
                for (JavaScriptSource js : p.getJavaScript()) {
                    if (!Options.get().isQuietEnabled() && js.getKind() == Kind.FILE)
                        log.info("Loading " + js.getFileName());
                    builder.transformWebAppCode(js);
                }
            }
            fg = builder.close();
        } catch (IOException e) {
            log.error("Unable to parse " + e.getMessage());
            return null;
        }
        leavePhase(AnalysisPhase.LOADING_FILES, analysis.getMonitoring());
        if (sync != null)
            sync.setFlowGraph(fg);
        if (Options.get().isFlowGraphEnabled())
            dumpFlowGraph(fg, false);

        analysis.getSolver().init(fg, document);

        monitoring.setFlowgraph(analysis.getSolver().getFlowGraph());
        monitoring.setCallGraph(analysis.getSolver().getAnalysisLatticeElement().getCallGraph());

        return analysis;
    }

    /**
     * Configures log4j.
     */
    public static void initLogging() {
        Properties prop = new Properties();
        prop.put("log4j.rootLogger", "INFO, tajs"); // DEBUG / INFO / WARN / ERROR 
        prop.put("log4j.appender.tajs", "org.apache.log4j.ConsoleAppender");
        prop.put("log4j.appender.tajs.layout", "org.apache.log4j.PatternLayout");
        prop.put("log4j.appender.tajs.layout.ConversionPattern", "%m%n");
        PropertyConfigurator.configure(prop);
    }

    /**
     * Runs the analysis.
     *
     * @throws AnalysisException if internal error
     */
    public static void run(Analysis analysis) throws AnalysisException {
        IAnalysisMonitoring monitoring = analysis.getMonitoring();

        long time = System.currentTimeMillis();

        enterPhase(AnalysisPhase.DATAFLOW_ANALYSIS, monitoring);
        analysis.getSolver().solve();
        leavePhase(AnalysisPhase.DATAFLOW_ANALYSIS, monitoring);

        long elapsed = System.currentTimeMillis() - time;
        if (Options.get().isTimingEnabled())
            log.info("Analysis finished in " + elapsed + "ms");

        if (Options.get().isFlowGraphEnabled())
            dumpFlowGraph(analysis.getSolver().getFlowGraph(), true);

        enterPhase(AnalysisPhase.SCAN, monitoring);
        analysis.getSolver().scan();
        leavePhase(AnalysisPhase.SCAN, monitoring);
    }

    /**
     * Outputs the flowgraph (in graphviz dot files).
     */
    private static void dumpFlowGraph(FlowGraph g, boolean end) {
        try {
            // create directories
            File outdir = new File("out");
            if (!outdir.exists()) {
                outdir.mkdir();
            }
            String path = "out" + File.separator + "flowgraphs";
            File outdir2 = new File(path);
            if (!outdir2.exists()) {
                outdir2.mkdir();
            }
            // dump the flowgraph to file
            try (PrintWriter pw = new PrintWriter(new FileWriter(path + File.separator + (end ? "final" : "initial") + ".dot"))) {
                g.toDot(pw);
            } catch (IOException e) {
                throw new AnalysisException(e);
            }
            // dump each function to file
            g.toDot(path, end);
            // also print flowgraph
            log.info(g.toString());
        } catch (IOException e) {
            throw new AnalysisException(e);
        }
    }

    private static void enterPhase(AnalysisPhase phase, IAnalysisMonitoring monitoring) {
        String phaseName = prettyPhaseName(phase);
        showPhaseStart(phaseName);
        monitoring.beginPhase(phase);
    }

    private static void showPhaseStart(String phaseName) {
        if (!Options.get().isQuietEnabled()) {
            log.info("===========  " + phaseName + " ===========");
        }
    }

    private static void leavePhase(AnalysisPhase phase, IAnalysisMonitoring monitoring) {
        monitoring.endPhase(phase);
    }

    private static String prettyPhaseName(AnalysisPhase phase) {
        switch (phase) {
            case LOADING_FILES:
                return "Loading files";
            case DATAFLOW_ANALYSIS:
                return "Data flow analysis";
            case SCAN:
                return "Scan";
            default:
                throw new RuntimeException("Unhandled phase enum: " + phase);
        }
    }
}
