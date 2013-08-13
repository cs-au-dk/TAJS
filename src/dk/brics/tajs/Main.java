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

package dk.brics.tajs;

import static dk.brics.tajs.util.Collections.newList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom.Document;

import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.analysis.CallContext;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.htmlparser.HTMLParser;
import dk.brics.tajs.htmlparser.JavaScriptSource;
import dk.brics.tajs.htmlparser.JavaScriptSource.EventHandlerJavaScriptSource;
import dk.brics.tajs.js2flowgraph.RhinoASTToFlowgraph;
import dk.brics.tajs.lattice.BlockState;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.SolverSynchronizer;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;

/**
 * Main class for the TAJS program analysis.
 */
public class Main {
	
	private static Logger logger = Logger.getLogger(Main.class); 

	private static final String LOADING_FILES = "Loading files";
	private static final String DATA_FLOW_ANALYSIS = "Data flow analysis";
	private static final String MESSAGES = "Messages";
	private static final String STATISTICS = "Statistics";
	private static final String REACHABLE_VARIABLES_SUMMARY = "Reachable variables summary";
	private static final String COVERAGE_SUMMARY = "Coverage summary";
	private static final String CALL_GRAPH = "Call graph";
	
	private Main() {}

	/**
	 * Runs the analysis on the given source files. Run without arguments to see
	 * the usage. Terminates with System.exit.
	 */
	public static void main(String[] args) {
		Options.reset();
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
		BlockState.reset();
		Obj.reset();
		Value.reset();
		Strings.reset();
		ScopeChain.reset();
	}

	/**
	 * Reads the input and prepares an analysis object.
	 * @return analysis object, null if invalid input
	 * @throws AnalysisException if internal error
	 */
	public static Analysis init(String[] args, SolverSynchronizer sync) throws AnalysisException {
		boolean show_usage = false;
		Options.parse(args);
		List<String> files = Options.getArguments();
		
		if (files.isEmpty()) {
			System.out.println("No source files");
			show_usage = true;
		}
		if (show_usage) {
			System.out.println("TAJS - Type Analyzer for JavaScript");
			System.out.println("Copyright 2009-2013 Aarhus University\n");
			System.out.println("Usage: java -jar tajs-all.jar [OPTION]... [FILE]...\n");
			Options.describe();
			return null;
		}
		if(Options.isDebugEnabled())
			Options.dump();

		enterPhase(LOADING_FILES);

		Document document = null;
		FlowGraph fg;
		try {
			List<String> html_files = newList();
			List<String> js_files = newList();
			for (String fn : files) {
				String l = fn.toLowerCase();
				if (l.endsWith(".html") || l.endsWith(".xhtml") || l.endsWith(".htm"))
					html_files.add(fn);
				else
					js_files.add(fn);
			}
			RhinoASTToFlowgraph builder = new RhinoASTToFlowgraph();
			if (!html_files.isEmpty()) {
				if (html_files.size() > 1)
					throw new AnalysisException("Only one HTML file can be analyzed at a time.");
				if (!Options.isDSLEnabled())
					Options.enableIncludeDom(); // enable DOM if any HTML files are involved, unless DSL is enabled
				HTMLParser p = new HTMLParser();
				document = p.build(html_files.get(0));
                List<EventHandlerJavaScriptSource> eventList = p.getEventHandlerAttributeList();
                List<JavaScriptSource> scriptList = p.getScriptList();
//                List<HtmlSource> htmlSourceList = p.getHtmlSourceList(); // TODO: getHtmlSourceList not used (intended for DSL mode?)
                builder.buildFromHTMLFile(scriptList, eventList); // TODO: correct order of combination of the code/page fragments?
			} // TODO: support analyzing one .html plus a number of .js files as one program?
			if (!js_files.isEmpty()) {
				builder.buildFromJavaScriptFiles(js_files);
			}
			fg = builder.close();
            fg.check();
		} catch (IOException e) {
			logger.error("Unable to parse " + e.getMessage());
			return null;
		}

        if (sync != null)
            sync.setFlowGraph(fg);

        if (Options.isFlowGraphEnabled())
            dumpFlowGraph(fg, false);

		Analysis analysis = new Analysis(sync);
		analysis.getSolver().init(fg, document);

		leavePhase(LOADING_FILES);
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
        prop.put("log4j.appender.tajs.layout.ConversionPattern", "[%p %C{1}] %m%n");
        PropertyConfigurator.configure(prop);
	}

	/**
	 * Runs the analysis.
	 * @throws AnalysisException if internal error
	 */
	public static void run(Analysis analysis) throws AnalysisException {
		long time = System.currentTimeMillis();
        long elapsed = 0;

		enterPhase(DATA_FLOW_ANALYSIS);
		analysis.getSolver().solve();
    	elapsed = (System.currentTimeMillis() - time);
		if (Options.isTimingEnabled())
        	logger.info("Analysis finished in " + elapsed + "ms");
		leavePhase(DATA_FLOW_ANALYSIS);

        if (Options.isFlowGraphEnabled())
            dumpFlowGraph(analysis.getSolver().getFlowGraph(), true);

        if (!Options.isNoMessages()) {
        	enterPhase(MESSAGES);
        	analysis.getSolver().scan();
        	leavePhase(MESSAGES);
        }

		CallGraph<State,CallContext,CallEdge<State>> call_graph = analysis.getSolver().getAnalysisLatticeElement().getCallGraph();
		if (Options.isStatisticsEnabled()) {
			enterPhase(STATISTICS);
			logger.info(analysis.getMonitoring());
            System.out.println(call_graph.getCallGraphStatistics(analysis.getSolver().getFlowGraph()));
//			System.out.println(analysis.getOptimizer());
			System.out.println("BlockState: created=" + BlockState.getNumberOfStatesCreated() + ", makeWritableStore=" + BlockState.getNumberOfMakeWritableStoreCalls());
			System.out.println("Obj: created=" + Obj.getNumberOfObjsCreated() + ", makeWritableProperties=" + Obj.getNumberOfMakeWritablePropertiesCalls());
			System.out.println("Value cache: hits=" + Value.getNumberOfValueCacheHits() + ", misses=" + Value.getNumberOfValueCacheMisses() + ", finalSize=" + Value.getValueCacheSize());
			System.out.println("Value object set cache: hits=" + Value.getNumberOfObjectSetCacheHits() + ", misses=" + Value.getNumberOfObjectSetCacheMisses() + ", finalSize=" + Value.getObjectSetCacheSize());
			System.out.println("ScopeChain cache: hits=" + ScopeChain.getNumberOfCacheHits() + ", misses=" + ScopeChain.getNumberOfCacheMisses() + ", finalSize=" + ScopeChain.getCacheSize());
			System.out.println("Basic blocks: " + analysis.getSolver().getFlowGraph().getNumberOfBlocks());
			leavePhase(STATISTICS);
		}

		if (Options.isCollectVariableInfoEnabled()) {
			enterPhase(REACHABLE_VARIABLES_SUMMARY);
			analysis.getMonitoring().presentTypeInformation();
			leavePhase(REACHABLE_VARIABLES_SUMMARY);
		}

		if (Options.isCoverageEnabled()) {
			enterPhase(COVERAGE_SUMMARY);
			analysis.getMonitoring().generateUnreachableMap();
			leavePhase(COVERAGE_SUMMARY);
		}

		if (Options.isCallGraphEnabled()) {
			enterPhase(CALL_GRAPH);
			logger.info(call_graph);
			File outdir = new File("out");
			if (!outdir.exists()) {
				outdir.mkdir();
			}
			String filename = "out" + File.separator + "callgraph.dot";
			try (FileWriter f = new FileWriter(filename)) {
				logger.info("Writing call graph to " + filename);
				call_graph.toDot(new PrintWriter(f));
			} catch (IOException e) {
				logger.error("Unable to write " + filename + ": " + e.getMessage());
			}
			leavePhase(CALL_GRAPH);
		}
	}

	/**
	 * Outputs the flowgraph (in graphviz dot files).
	 */
	private static void dumpFlowGraph(FlowGraph g, boolean end) {
		try {
			File outdir = new File("out");
			if (!outdir.exists()) {
				outdir.mkdir();
			}
			String path = "out" + File.separator + "flowgraphs";
			File outdir2 = new File(path);
			if (!outdir2.exists()) {
				outdir2.mkdir();
			}
			try (PrintWriter pw = new PrintWriter(new FileWriter(path + File.separator + (end ? "final" : "initial") + ".dot"))) { 
				g.toDot(pw);
			} catch (IOException e) {
				throw new AnalysisException(e);
			}
			System.out.println(g.toString());
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdir();
			}
			g.toDot(path, end);
		} catch (IOException e) {
			throw new AnalysisException(e);
		}
	}

	private static void enterPhase(String name) {
		if (!Options.isQuietEnabled())
			logger.info("===========  " + name + " ===========");
	}

	@SuppressWarnings("unused")
	private static void leavePhase(String name) {
		// if (!Options.isTestEnabled())
		//   logger.info(">END:" + name);
	}
}
