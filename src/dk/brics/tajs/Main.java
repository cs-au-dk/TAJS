/*
 * Copyright 2009-2018 Aarhus University
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
import dk.brics.tajs.analysis.nativeobjects.NodeJSRequire;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.HostEnvSources;
import dk.brics.tajs.flowgraph.JavaScriptSource;
import dk.brics.tajs.flowgraph.JavaScriptSource.Kind;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.js2flowgraph.FlowGraphBuilder;
import dk.brics.tajs.js2flowgraph.HTMLParser;
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.AnalysisPhase;
import dk.brics.tajs.monitoring.AnalysisTimeLimiter;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.MaxMemoryUsageMonitor;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.monitoring.ProgramExitReachabilityChecker;
import dk.brics.tajs.monitoring.TAJSAssertionReachabilityCheckerMonitor;
import dk.brics.tajs.monitoring.inspector.datacollection.InspectorFactory;
import dk.brics.tajs.monitoring.soundness.SoundnessTesterMonitor;
import dk.brics.tajs.options.ExperimentalOptions;
import dk.brics.tajs.options.OptionValues;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.options.TAJSEnvironmentConfig;
import dk.brics.tajs.solver.SolverSynchronizer;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Loader;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.PathAndURLUtils;
import dk.brics.tajs.util.Strings;
import net.htmlparser.jericho.Source;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.kohsuke.args4j.CmdLineException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            if (Options.get().isDebugOrTestEnabled()) {
                throw e;
            }
            log.error("Error: " + e.getMessage());
            //e.printStackTrace();
            System.exit(-2);
        }
    }

    /**
     * Resets all internal counters and caches.
     */
    public static void reset() {
        Canonicalizer.reset();
        ExperimentalOptions.ExperimentalOptionsManager.reset();
        Options.reset();
        State.reset();
        Value.reset();
        Obj.reset();
        Strings.reset();
        ScopeChain.reset();
        NodeJSRequire.reset();
    }

    /**
     * Reads the input and prepares an analysis object, using the default monitoring and command-line arguments.
     */
    public static Analysis init(String[] args, SolverSynchronizer sync) throws AnalysisException {
        OptionValues options = new OptionValues();
        try {
            options.parse(args);
            options.checkConsistency();
            return init(options, Monitoring.make(), sync);
        } catch (CmdLineException e) {
            showHeader();
            log.info(e.getMessage() + "\n");
            log.info("Usage: java -jar tajs-all.jar [OPTION]... [FILE]...\n");
            Options.showUsage();
            return null;
        }
    }

    /**
     * Reads the input and prepares an analysis object.
     *
     * @return analysis object, null if invalid input
     * @throws AnalysisException if internal error
     */
    public static Analysis init(OptionValues options, IAnalysisMonitoring monitoring, SolverSynchronizer sync) throws AnalysisException {
        Options.set(options);
        TAJSEnvironmentConfig.init();

        monitoring = addOptionalMonitors(monitoring);

        Analysis analysis = new Analysis(monitoring, sync);

        showHeader();

        if (Options.get().isDebugEnabled())
            Options.dump();

        enterPhase(AnalysisPhase.INITIALIZATION, analysis.getMonitoring());
        Source document = null;
        FlowGraph fg;
        try {
            // split into JS files and HTML files
            URL htmlFile = null;
            List<URL> js_files = newList();
            List<URL> resolvedFiles = resolveInputs(Options.get().getArguments());
            for (URL fn : resolvedFiles) {
                if (isHTMLFileName(fn.toString())) {
                    if (htmlFile != null)
                        throw new AnalysisException("Only one HTML file can be analyzed at a time");
                    htmlFile = fn;
                } else
                    js_files.add(fn);
            }

            FlowGraphBuilder builder = FlowGraphBuilder.makeForMain(new SourceLocation.StaticLocationMaker(resolvedFiles.get(resolvedFiles.size() - 1)));
            builder.addLoadersForHostFunctionSources(HostEnvSources.getAccordingToOptions());
            if (Options.get().isNodeJS()) {
                NodeJSRequire.init();
                if (resolvedFiles.size() != 1 || htmlFile != null) {
                    throw new AnalysisException("A single JavaScript file is expected for NodeJS analysis");
                }
                // noop, the bootstrapping has been done by addLoadersForHostFunctionSources above
            }
            else if (!js_files.isEmpty()) {
                if (htmlFile != null)
                    throw new AnalysisException("Cannot analyze an HTML file and JavaScript files at the same time");
                // build flowgraph for JS files
                for (URL js_file : js_files) {
                    if (!Options.get().isQuietEnabled())
                        log.info("Loading " + js_file);
                    builder.transformStandAloneCode(Loader.getString(js_file, Charset.forName("UTF-8")), new SourceLocation.StaticLocationMaker(js_file));
                }
            } else {
                // build flowgraph for JavaScript code in or referenced from HTML file
                Options.get().enableIncludeDom(); // always enable DOM if any HTML files are involved
                if (!Options.get().isQuietEnabled())
                    log.info("Loading " + htmlFile);
                HTMLParser p = new HTMLParser(htmlFile);
                document = p.getHTML();
                for (Pair<URL, JavaScriptSource> js : p.getJavaScript()) {
                    if (!Options.get().isQuietEnabled() && js.getSecond().getKind() == Kind.FILE)
                        log.info("Loading " + PathAndURLUtils.getRelativeToWorkingDirectory(PathAndURLUtils.toPath(js.getFirst())));
                    builder.transformWebAppCode(js.getSecond(), new SourceLocation.StaticLocationMaker(js.getFirst()));
                }
            }
            fg = builder.close();
        } catch (IOException e) {
            log.error("Error: Unable to load and parse " + e.getMessage());
            return null;
        }
        if (sync != null)
            sync.setFlowGraph(fg);
        if (Options.get().isFlowGraphEnabled())
            dumpFlowGraph(fg, false);

        analysis.getSolver().init(fg, document);

        leavePhase(AnalysisPhase.INITIALIZATION, analysis.getMonitoring());

        return analysis;
    }

    /**
     * Adds additional monitors according to the options.
     */
    private static IAnalysisMonitoring addOptionalMonitors(IAnalysisMonitoring monitoring) {
        List<IAnalysisMonitoring> extraMonitors = newList();

        // Analysis timeout monitor
        int timeLimit = Options.get().getAnalysisTimeLimit();
        int transferLimit = Options.get().getAnalysisTransferLimit();
        AnalysisTimeLimiter timeLimiter = new AnalysisTimeLimiter(timeLimit, transferLimit, Options.get().isTestEnabled());
        if (timeLimit >= 0 || transferLimit > 0) {
            extraMonitors.add(timeLimiter);
        }

        // Analysis result measuring monitors
        if (Options.get().isMemoryMeasurementEnabled()) {
            extraMonitors.add(new MaxMemoryUsageMonitor());
            // extraMonitors.add(new MemoryUsageDiagnosisMonitor()); // for development use only
        }

        // Analysis results checking monitors
        // Note: the first one to throw an exception will prevent the others from reporting errors
        if (Options.get().getSoundnessTesterOptions().isTest()) {
            extraMonitors.add(new SoundnessTesterMonitor());
        } else if (Options.get().isTestEnabled()) {
            // (no need to test reachability if using soundness testing)
            extraMonitors.add(new ProgramExitReachabilityChecker(true, !Options.get().isDoNotExpectOrdinaryExitEnabled(), true, false, true, timeLimiter::analysisNotExceededLimit));
        }
        extraMonitors.add(new TAJSAssertionReachabilityCheckerMonitor(timeLimiter::analysisNotExceededLimit));

        // put inspector *after* checking
        if (Options.get().isInspectorEnabled()) {
            extraMonitors.add(InspectorFactory.createInspectorMonitor());
        }

        if (!extraMonitors.isEmpty()) {
            extraMonitors.add(0, monitoring);
            monitoring = CompositeMonitoring.buildFromList(extraMonitors);
        }
        return monitoring;
    }

    private static List<URL> resolveInputs(List<Path> files) {
        return files.stream().map(f -> PathAndURLUtils.normalizeFileURL(PathAndURLUtils.toURL(f))).collect(Collectors.toList());
    }

    private static boolean isHTMLFileName(String fileName) {
        String f = fileName.toLowerCase();
        return f.endsWith(".html") || f.endsWith(".xhtml") || f.endsWith(".htm");
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

        enterPhase(AnalysisPhase.ANALYSIS, monitoring);
        analysis.getSolver().solve();
        leavePhase(AnalysisPhase.ANALYSIS, monitoring);

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
            Path outdir = Paths.get("out").resolve("flowgraphs");
            Files.createDirectories(outdir);
            // dump the flowgraph to file
            String fileName = end ? "final" : "initial" + ".dot";
            try (PrintWriter pw = new PrintWriter(new FileWriter(outdir.resolve(fileName).toFile()))) {
                g.toDot(pw);
            } catch (IOException e) {
                throw new AnalysisException(e);
            }
            // dump each function to file
            g.toDot(outdir, end);
            // also print flowgraph
            log.info(g.toString());
        } catch (IOException e) {
            throw new AnalysisException(e);
        }
    }

    private static void enterPhase(AnalysisPhase phase, IAnalysisMonitoring monitoring) {
        String phaseName = prettyPhaseName(phase);
        showPhaseStart(phaseName);
        monitoring.visitPhasePre(phase);
    }

    private static void showHeader() {
        if (!Options.get().isQuietEnabled()) {
            log.info("TAJS - Type Analyzer for JavaScript\n" +
                "Copyright 2009-2018 Aarhus University\n");
        }
    }

    private static void showPhaseStart(String phaseName) {
        if (!Options.get().isQuietEnabled()) {
            log.info("===========  " + phaseName + " ===========");
        }
    }

    private static void leavePhase(AnalysisPhase phase, IAnalysisMonitoring monitoring) {
        monitoring.visitPhasePost(phase);
    }

    private static String prettyPhaseName(AnalysisPhase phase) {
        switch (phase) {
            case INITIALIZATION:
                return "Loading files";
            case ANALYSIS:
                return "Data flow analysis";
            case SCAN:
                return "Scan";
            default:
                throw new RuntimeException("Unhandled phase enum: " + phase);
        }
    }
}
