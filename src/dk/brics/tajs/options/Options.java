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

package dk.brics.tajs.options;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Global analysis options.
 */
public class Options { // TODO: better management of options (i.e. not static fields)?

	private static Logger logger = Logger.getLogger(Options.class); 

	private static boolean debug;
	private static boolean collect_variable_info;
	private static boolean states; 
	private static boolean test;
	private static boolean test_flowgraph_builder;
	private static boolean timing;
	private static boolean flowgraph;
	private static boolean callgraph;
	private static boolean no_recency;
	private static boolean quiet;
	private static boolean no_hybrid_collections;
	private static boolean no_modified;
	private static boolean no_lazy;
	private static boolean polymorphic;
	private static boolean no_context_sensitivity;
	private static boolean no_local_path_sensitivity;
	private static boolean no_copy_on_write;
	private static boolean no_gc;
	private static boolean unsound;
	private static boolean include_dom;
	private static boolean old_flowgraph_builder;
	private static boolean no_exceptions;
	private static boolean newflow;
	private static boolean memory_usage;
	private static boolean flowgraph_optimization;
	private static boolean propagate_dead_flow;
	private static boolean low_severity;
	private static boolean statistics;
	private static boolean ajax_returns_json;
	private static boolean always_canput;
	private static boolean eval_statistics; // FIXME: eval_statistics is unsound - do we really want it?
	private static boolean coverage;
	private static boolean error_batch_mode;
	private static boolean uneval_mode;
	private static boolean correlation_tracking_mode;
	private static boolean unroll_one_and_a_half;
	private static boolean renaming;
	private static boolean no_charged_calls;

	private static boolean ignore_libraries;
	private static Set<String> ignored_libraries = new LinkedHashSet<>();

	// DOM customization
	private static boolean single_event_handler_loop;
	private static boolean single_event_handler_type;
	private static boolean ignore_html_content;

	// DSL Language
	private static boolean dsl;

	private Options() {
		// do nothing
	}

	/**
	 * Returns a description of the available options.
	 */
	public static String describe() {
		return "Options:\n" +
				"  -no-local-path-sensitivity  Disable local path sensitivity\n" +
				"  -no-context-sensitivity     Disable context sensitivity\n" +
				"  -no-recency                 Disable recency abstraction\n" +
				"  -no-modified                Disable modified flags\n" +
				"  -no-exceptions              Disable implicit exception flow\n" +
				"  -no-gc                      Disable abstract garbage collection\n" +
				"  -no-lazy                    Disable lazy propagation\n" +
				"  -no-flowgraph-optimization  Disable flowgraph optimization\n" +
				"  -no-copy-on-write           Disable copy-on-write\n" +
				"  -no-hybrid-collections      Disable hybrid collections\n" +
				"  -no-charged-calls           Disable charged calls\n" +
				"  -low-severity               Enable low severity messages\n" +
				"  -unsound                    Enable unsound assumptions\n" +
				"  -flowgraph                  Output flowgraph.dot\n" +
				"  -callgraph                  Output callgraph.dot\n" +
				"  -debug                      Output debug information\n" +
				"  -collect-variable-info      Output type and line information on reachable variables\n" +
				"  -newflow                    Report summary of new flow at function entries\n" +
				"  -states                     Output intermediate abstract states\n" +
				"  -test                       Test mode (implies quiet), ensures predictable iteration orders\n" +
				"  -test-flowgraph-builder     Test flow graph builder (implies test mode)\n" +
				"  -timing                     Report analysis time\n" +
				"  -statistics                 Report statistics\n" +
				"  -memory-usage               Report the memory usage of the analysis\n" +
				"  -quiet                      Only output results, not progress\n" +
				"  -dom                        Enable Mozilla DOM browser model\n" +
				"  -old-flowgraph-builder      Enable the old flow graph builder\n" +
				"  -propagate-dead-flow        Propagate empty values\n" +
				"  -correlation-tracking       Enable correlation tracking\n" +
				"  -unroll-one-and-a-half      Enable 1 1/2 loop unrolling\n" +
				"  -ignore-libraries           Ignore unreachable code messages from libraries (may not be the last option before files!)\n" +
				"  -return-json                Assume that AJAX calls return JSON\n" +
				"  -always-canput              Assume [[CanPut]] always succeeds\n" +
				"  -eval-statistics            Don't fail on use of eval and innerHTML, but record their use\n" +
				"  -coverage                   Output a view of the source with unreachable lines highlighted\n" +
				"  -single-event-handler-loop  Use a single non-deterministic event loop for events\n" +
				"  -single-event-handler-type  Do not distinguish between different types of event handlers\n" +
				"  -introduce-error            Measure precision by randomly introducing syntax errors\n" +
				"  -ignore-html-content        Ignore the content of the HTML page, i.e. id, names, event handlers etc.\n" +
				"  -dsl                        Load DSL object models.\n" +
				"  -uneval                     Try to remove calls to eval\n";
	}

	/**
	 * Sets the given option.
	 *
	 * @param option The option to set.
	 * @return True if understood.
	 */
	public static boolean set(String option) { // TODO: use e.g. Apache Commons CLI for options parsing?
		if (option.equals("-no-local-path-sensitivity"))
			enableNoLocalPathSensitivity();
		else if (option.equals("-no-context-sensitivity"))
			enableNoContextSensitivity();
		else if (option.equals("-no-recency"))
			enableNoRecency();
		else if (option.equals("-no-modified"))
			enableNoModified();
		else if (option.equals("-no-lazy"))
			enableNoLazy();
		else if (option.equals("-polymorphic"))
			enablePolymorphic();
		else if (option.equals("-no-gc"))
			enableNoGc();
		else if (option.equals("-unsound"))
			enableUnsound();
		else if (option.equals("-no-exceptions"))
			enableNoExceptions();
		else if (option.equals("-debug"))
			enableDebug();
		else if (option.equals("-collect-variable-info"))
			enableCollectVariableInfo();
		else if (option.equals("-newflow")) {
			enableNewflow();
			enableStatistics();
		} else if (option.equals("-flowgraph"))
			enableFlowgraph();
		else if (option.equals("-callgraph"))
			enableCallgraph();
		else if (option.equals("-states"))
			enableStates();
		else if (option.equals("-test")) {
			enableTest();
			enableQuiet();
		} else if (option.equals("-test-flowgraph-builder")) {
			enableTestFlowGraphBuiler();
			enableQuiet();
		} else if (option.equals("-timing"))
			enableTiming();
		else if (option.equals("-statistics"))
			enableStatistics();
		else if (option.equals("-memory-usage"))
			enableMemoryUsage();
		else if (option.equals("-quiet"))
			enableQuiet();
		else if (option.equals("-no-copy-on-write"))
			enableNoCopyOnWrite();
		else if (option.equals("-no-hybrid-collections"))
			enableNoHybridCollections();
		else if (option.equals("-dom"))
			enableIncludeDom();
		else if (option.equals("-old-flowgraph-builder")) {
			enableOldFlowgraphBuilder();
		} else if (option.equals("-flowgraph-optimization"))
			enableFlowgraphOptimization();
		else if (option.equals("-propagate-dead-flow"))
			enablePropagateDeadFlow();
		else if (option.equals("-correlation-tracking"))
			enableCorrelationTracking();
		else if (option.equals("-unroll-one-and-a-half"))
			enableUnrollOneAndAHalf();
		else if (option.equals("-low-severity"))
			enableLowSeverity();
		else if (option.equals("-ignore-libraries"))
			enableIgnoreLibraries();
		else if (option.equals("-return-json"))
			enableAjaxReturnsJson();
		else if (option.equals("-always-canput"))
			enableAlwaysCanPut();
		else if (option.equals("-eval-statistics"))
			enableEvalStatistics();
		else if (option.equals("-coverage"))
			enableCoverage();
		else if (option.equals("-single-event-handler-loop"))
			enableSingleEventHandlerLoop();
		else if (option.equals("-single-event-handler-type"))
			enableSingleEventHandlerType();
		else if (option.equals("-introduce-error "))
			enableErrorBatchMode();
		else if (option.equals("-ignore-html-content"))
			enableIgnoreHTMLContent();
		else if (option.equals("-dsl"))
			enableDSL();
		else if (option.equals("-uneval")) {
			enableIncludeDom();
			enableUnevalMode();
		} else if (option.equals("-renaming"))
			enableRenaming();
		else if (option.equals("-no-charged-calls"))
			enableNoChargedCalls();
		else
			return false;
		return true;
	}

	/**
	 * Resets all options.
	 */
	public static void reset() {
		disableNoLocalPathSensitivity();
		disableNoContextSensitivity();
		disableNoRecency();
		disableNoModified();
		disableNoLazy();
		disablePolymorphic();
		disableNoGc();
		disableUnsound();
		disableNoExceptions();
		disableDebug();
		disableCollectVariableInfo();
		disableNewflow();
		disableFlowgraph();
		disableCallgraph();
		disableStates();
		disableQuiet();
		disableTiming();
		disableStatistics();
		disableMemoryUsage();
		disableQuiet();
		disableNoCopyOnWrite();
		disableNoHybridCollections();
		disableIncludeDom();
		disableOldFlowgraphBuilder();
		disableFlowgraphOptimization();
		disablePropagateDeadFlow();
		disableCorrelationTracking();
		disableUnrollOneAndAHalf();
		disableLowSeverity();
		disableAjaxReturnsJson();
		disableAlwaysCanPut();
		disableSingleEventHandlerLoop();
		disableSingleEventHandlerType();
		disableIgnoreHTMLContent();
		disableDSL();
		disableUnevalMode();
		disableTestFlowGraphBuilder();
		disableRenaming();
		disableNoChargedCalls();
		ignore_libraries = false;
		ignored_libraries = new LinkedHashSet<>();
	}

	/**
	 * Prints the settings (if in debug mode).
	 */
	public static void dump() {
		if (debug) {
			logger.debug("Options affecting analysis precision:" );
			logger.debug("  no-local-path-sensitivity: " + no_local_path_sensitivity);
			logger.debug("  no-context-sensitivity:    " + no_context_sensitivity);
			logger.debug("  no-recency:                " + no_recency);
			logger.debug("  no-modified:               " + no_modified);
			logger.debug("  no-exceptions:             " + no_exceptions);
			logger.debug("  no-lazy:                   " + no_lazy);
			logger.debug("  no-gc:                     " + no_gc);
			logger.debug("  unsound:                   " + unsound);
			logger.debug("  polymorphic:               " + polymorphic);
			logger.debug("  propagate-dead-flow:       " + propagate_dead_flow);
			logger.debug("  correlation-tracking:      " + correlation_tracking_mode);
			logger.debug("  unroll-one-and-a-half:     " + unroll_one_and_a_half);
			logger.debug("  ignore-libraries:          " + ignore_libraries);
			logger.debug("  return-json:               " + ajax_returns_json);
			logger.debug("  always-canput:             " + always_canput);
			logger.debug("  single-event-handler-loop: " + single_event_handler_loop);
			logger.debug("  single-event-handler-type: " + single_event_handler_type);
			logger.debug("  ignore-html-content:       " + ignore_html_content);
			logger.debug("  uneval:                    " + uneval_mode);
			logger.debug("  renaming:                  " + renaming);
			logger.debug("  no-charged-calls:          " + no_charged_calls);
			logger.debug("Other options:");
			logger.debug("  flowgraph:                 " + flowgraph);
			logger.debug("  callgraph:                 " + callgraph);
			logger.debug("  debug:                     " + debug);
			logger.debug("  low-severity:              " + low_severity);
			logger.debug("  collect-variable-info:     " + collect_variable_info);
			logger.debug("  newflow:                   " + newflow);
			logger.debug("  states:                    " + states);
			logger.debug("  test:                      " + test);
			logger.debug("  test-flowgraph-builder:    " + test_flowgraph_builder);
			logger.debug("  statistics:                " + statistics);
			logger.debug("  timing:                    " + timing);
			logger.debug("  memory-usage:              " + memory_usage);
			logger.debug("  no-copy-on-write:          " + no_copy_on_write);
			logger.debug("  no-hybrid-collections:     " + no_hybrid_collections);
			logger.debug("  dom:                       " + include_dom);
			logger.debug("  old-flowgraph-builder:     " + old_flowgraph_builder);
			logger.debug("  flowgraph-optimization:    " + flowgraph_optimization);
			logger.debug("  eval-statistics:           " + eval_statistics);
			logger.debug("  dsl:                       " + dsl);
		}
	}

	/**
	 * Are we currently in debug mode.
	 */
	public static boolean isDebugEnabled() {
		return debug;
	}

	/**
	 * Are we currently in debug or test mode.
	 */
	public static boolean isDebugOrTestEnabled() {
		return debug || test;
	}

	/**
	 * If set, summarize information on reachable variables, e.g. their type and location.
	 */
	public static boolean isCollectVariableInfoEnabled() {
		return collect_variable_info;
	}

	/**
	 * If set, output flowgraph.dot.
	 */
	public static boolean isFlowGraphEnabled() {
		return flowgraph;
	}

	/**
	 * If set, output callgraph.dot.
	 */
	public static boolean isCallGraphEnabled() {
		return callgraph;
	}

	/**
	 * If set, print intermediate abstract states.
	 */
	public static boolean isIntermediateStatesEnabled() {
		return states;
	}

	/**
	 * If set, avoid nondeterministic output.
	 */
	public static boolean isTestEnabled() {
		return test;
	}

	/**
	 * If set, output flow graphs to stdout for testing purposes.
	 */
	public static boolean isTestFlowGraphBuilderEnabled() {
		return test_flowgraph_builder;
	}

	/**
	 * If set, report statistics.
	 */
	public static boolean isStatisticsEnabled() {
		return statistics;
	}

	/**
	 * If set, report timings.
	 */
	public static boolean isTimingEnabled() {
		return timing;
	}

	/**
	 * If set, do not use recency abstraction.
	 */
	public static boolean isRecencyDisabled() {
		return no_recency;
	}

	/**
	 * If set, only report results.
	 */
	public static boolean isQuietEnabled() {
		return quiet;
	}

	/**
	 * If set, do not use {@link dk.brics.tajs.util.HybridArrayHashMap} 
	 * and {@link dk.brics.tajs.util.HybridArrayHashSet}.
	 */
	public static boolean isHybridCollectionsDisabled() {
		return no_hybrid_collections;
	}

	/**
	 * If set, do not use modified flags.
	 */
	public static boolean isModifiedDisabled() {
		return no_modified;
	}

	/**
	 * If set, do not use lazy propagation.
	 */
	public static boolean isLazyDisabled() {
		return no_lazy;
	}

	/**
	 * If set, use polymorphic abstract values.
	 */
	public static boolean isPolymorphicEnabled() {
		return polymorphic;
	}

	/**
	 * If set, do not use context sensitivity.
	 */
	public static boolean isContextSensitivityDisabled() {
		return no_context_sensitivity;
	}

	/**
	 * If set, do not use local path sensitivity, including assume node effects.
	 */
	public static boolean isLocalPathSensitivityDisabled() {
		return no_local_path_sensitivity;
	}

	/**
	 * If set, do not use copy-on-write.
	 */
	public static boolean isCopyOnWriteDisabled() {
		return no_copy_on_write;
	}

	/**
	 * If set, do not use abstract garbage collection.
	 */
	public static boolean isGCDisabled() {
		return no_gc;
	}

	/**
	 * If set, allow certain unsound tricks.
	 */
	public static boolean isUnsoundEnabled() {
		return unsound;
	}

	/**
	 * If set, print low severity messages.
	 */
	public static boolean isLowSeverityEnabled() {
		return low_severity;
	}

	/**
	 * If set, report summary of new flow at function entries.
	 */
	public static boolean isNewFlowEnabled() {
		return newflow;
	}

	/**
	 * If set, exclude implicit exception flow.
	 */
	public static boolean isExceptionsDisabled() {
		return no_exceptions;
	}

	/**
	 * If set, ignore unreachable code warnings from libraries.
	 */
	public static boolean isIgnoreLibrariesEnabled() {
		return ignore_libraries;
	}

	/**
	 * If set, the DOM objects and functions are part of the initial state.
	 */
	public static boolean isDOMEnabled() {
		return include_dom;
	}

	/**
	 * If set, use the old flow graph builder.
	 */
	public static boolean isOldFlowgraphBuilderEnabled() {
		return old_flowgraph_builder;
	}

	/**
	 * If set, measure memory usage.
	 */
	public static boolean isMemoryMeasurementEnabled() {
		return memory_usage;
	}

	/**
	 * If set, do not perform flowgraph optimization.
	 */
	public static boolean isFlowGraphOptimizationEnabled() {
		return flowgraph_optimization;
	}

	/**
	 * If set, dead data flow is propagated.
	 */
	public static boolean isPropagateDeadFlow() {
		return propagate_dead_flow;
	}

	/**
	 * Is correlation tracking enabled.
	 */
	public static boolean isCorrelationTrackingEnabled() {
		return correlation_tracking_mode;
	}

	/**
	 * Is one and a half loop unrolling enabled.
	 */
	public static boolean isUnrollOneAndAHalfEnabled() {
		return unroll_one_and_a_half;
	}

	/**
	 * Assume AJAX returns JSON.
	 */
	public static boolean isReturnJSON() {
		return ajax_returns_json;
	}

	public static boolean isAlwaysCanPut() {
		return always_canput;
	}

	/**
	 * Eval statistics enabled.
	 */
	public static boolean isEvalStatistics() {
		return eval_statistics;
	}

	/**
	 * If set, use renaming.
	 */
	public static boolean isRenamingEnabled() {
		return renaming;
	}

	/**
	 * If set, don't use charged calls.
	 */
	public static boolean isChargedCallsDisabled() {
		return no_charged_calls;
	}

	/**
	 * Add a file to the set of ignored library files.
	 */
	public static void addLibrary(String fileName) {
		ignored_libraries.add(fileName);
	}

	/**
	 * Get the set of ignored libraries.
	 */
	public static Set<String> getLibraries() {
		return ignored_libraries;
	}

	/**
	 * Enable the DOM DSL.
	 */
	public static void enableDSL() {
		dsl = true;
	}

	/**
	 * Disable the DOM DSL.
	 */
	public static void disableDSL() {
		dsl = false;
	}

	/**
	 * Avoid nondeterministic output.
	 * Also sets quiet mode and low_severity.
	 */
	public static void enableTest() {
		test = true;
		quiet = true;
		low_severity = true;
	}

	/**
	 * Disable test mode.
	 */
	public static void disableTest() {
		test = false;
		quiet = false;
		low_severity = false;
	}

	/**
	 * Enable testing of flow graph builder. Implies test mode.
	 */
	public static void enableTestFlowGraphBuiler() {
		enableTest();
		test_flowgraph_builder = true;
	}

	/**
	 * Disable testing of flow graph builder. Also disables test mode.
	 */
	public static void disableTestFlowGraphBuilder() {
		disableTest();
		test_flowgraph_builder = false;
	}

	/**
	 * Enable debug mode.
	 * Also sets log level to Level.DEBUG.
	 */
	public static void enableDebug() {
		debug = true;
		Logger.getRootLogger().setLevel(Level.DEBUG);
	}

	/**
	 * Disable debug mode.
	 * Also sets log level to Level.INFO.
	 */
	public static void disableDebug() {
		debug = false;
		Logger.getRootLogger().setLevel(Level.INFO);
	}

	/**
	 * Enable variable information collection.
	 */
	public static void enableCollectVariableInfo() {
		collect_variable_info = true;
	}

	/**
	 * Disable variable information collection.
	 */
	public static void disableCollectVariableInfo() {
		collect_variable_info = false;
	}

	/**
	 * Enable output of intermediate states.
	 */
	public static void enableStates() {
		states = true;
	}

	/**
	 * Disable output of intermediate states.
	 */
	public static void disableStates() {
		states = false;
	}

	/**
	 * Enable timing of TAJS.
	 */
	public static void enableTiming() {
		timing = true;
	}

	/**
	 * Disable timing of TAJS.
	 */
	public static void disableTiming() {
		timing = false;
	}

	/**
	 * Enable flowgraph dumping.
	 */
	public static void enableFlowgraph() {
		flowgraph = true;
	}

	/**
	 * Disable flowgraph dumping.
	 */
	public static void disableFlowgraph() {
		flowgraph = false;
	}

	/**
	 * Enable callgraph dumping.
	 */
	public static void enableCallgraph() {
		callgraph = true;
	}

	/**
	 * Disable callgraph dumping.
	 */
	public static void disableCallgraph() {
		callgraph = false;
	}

	/**
	 * Disable recency abstraction.
	 */
	public static void enableNoRecency() {
		no_recency = true;
	}

	/**
	 * Enable recency abstraction.
	 */
	public static void disableNoRecency() {
		no_recency = false;
	}

	/**
	 * Enable quiet mode.
	 */
	public static void enableQuiet() {
		quiet = true;
	}

	/**
	 * Disable quiet mode.
	 */
	public static void disableQuiet() {
		quiet = false;
	}

	/**
	 * Disable hybrid collections.
	 */
	public static void enableNoHybridCollections() {
		no_hybrid_collections = true;
	}

	/**
	 * Enable hybrid collections.
	 */
	public static void disableNoHybridCollections() {
		no_hybrid_collections = false;
	}

	/**
	 * Disable modified flags.
	 */
	public static void enableNoModified() {
		no_modified = true;
	}

	/**
	 * Enable modified flags.
	 */
	public static void disableNoModified() {
		no_modified = false;
	}

	/**
	 * Disable lazy propagation.
	 */
	public static void enableNoLazy() {
		no_lazy = true;
	}

	/**
	 * Enable lazy propagation.
	 */
	public static void disableNoLazy() {
		no_lazy = false;
	}

	/**
	 * Enable polymorphic values.
	 */
	public static void enablePolymorphic() { // TODO: enable polymorphic by default?
		polymorphic = true;
	}

	/**
	 * Disable polymorphic values.
	 */
	public static void disablePolymorphic() {
		polymorphic = false;
	}

	/**
	 * Disable context sensitivity.
	 */
	public static void enableNoContextSensitivity() {
		no_context_sensitivity = true;
	}

	/**
	 * Enable context sensitivity.
	 */
	public static void disableNoContextSensitivity() {
		no_context_sensitivity = false;
	}

	/**
	 * Disable local path sensitivity.
	 */
	public static void enableNoLocalPathSensitivity() {
		no_local_path_sensitivity = true;
	}

	/**
	 * Disable local path sensitivity.
	 */
	public static void disableNoLocalPathSensitivity() {
		no_local_path_sensitivity = false;
	}

	/**
	 * Disable copy on write.
	 */
	public static void enableNoCopyOnWrite() {
		no_copy_on_write = true;
	}

	/**
	 * Enable copy on write.
	 */
	public static void disableNoCopyOnWrite() {
		no_copy_on_write = false;
	}

	/**
	 * Disable abstract garbage collection.
	 */
	public static void enableNoGc() {
		no_gc = true;
	}

	/**
	 * Enable abstract garbage collection.
	 */
	public static void disableNoGc() {
		no_gc = false;
	}

	/**
	 * Enable unsound shortcuts.
	 */
	public static void enableUnsound() {
		unsound = true;
	}

	/**
	 * Disable unsound shortcuts.
	 */
	public static void disableUnsound() {
		unsound = false;
	}

	/**
	 * Enable DOM model.
	 */
	public static void enableIncludeDom() {
		include_dom = true;
	}

	/**
	 * Disable DOM model.
	 */
	public static void disableIncludeDom() {
		include_dom = false;
	}

	/**
	 * Enable the old flow graph builder.
	 */
	public static void enableOldFlowgraphBuilder() {
		old_flowgraph_builder = true;
	}

	/**
	 * Disable the old flow graph builder.
	 */
	public static void disableOldFlowgraphBuilder() {
		old_flowgraph_builder = false;
	}

	/**
	 * Enable ignoring exceptions.
	 */
	public static void enableNoExceptions() {
		no_exceptions = true;
	}

	/**
	 * Disable ignoring exceptions.
	 */
	public static void disableNoExceptions() {
		no_exceptions = false;
	}

	/**
	 * Enable reporting summaries at flow edges.
	 */
	public static void enableNewflow() {
		newflow = true;
	}

	/**
	 * Disable reporting summaries at flow edges.
	 */
	public static void disableNewflow() {
		newflow = false;
	}

	/**
	 * Enable memory usage statistics.
	 */
	public static void enableMemoryUsage() {
		memory_usage = true;
	}

	/**
	 * Disable memory usage statistics.
	 */
	public static void disableMemoryUsage() {
		memory_usage = false;
	}

	/**
	 * Enable flow graph optimization.
	 */
	public static void enableFlowgraphOptimization() {
		flowgraph_optimization = true;
	}

	/**
	 * Disable flow graph optimization.
	 */
	public static void disableFlowgraphOptimization() {
		flowgraph_optimization = false;
	}

	/**
	 * Enable propagation of dead data flow.
	 */
	public static void enablePropagateDeadFlow() {
		propagate_dead_flow = true;
	}

	/**
	 * Disable propagation of dead data flow.
	 */
	public static void disablePropagateDeadFlow() {
		propagate_dead_flow = false;
	}

	/**
	 * Enable correlation tracking.
	 */
	public static void enableCorrelationTracking() {
		correlation_tracking_mode = true;
	}

	/**
	 * Disable correlation tracking.
	 */
	public static void disableCorrelationTracking() {
		correlation_tracking_mode = false;
	}

	/**
	 * Enable 1 1/2 loop unrolling.
	 */
	public static void enableUnrollOneAndAHalf() {
		unroll_one_and_a_half = true;
	}

	/**
	 * Disable 1 1/2 loop unrolling.
	 */
	public static void disableUnrollOneAndAHalf() {
		unroll_one_and_a_half = false;
	}


	/**
	 * Print low severity messages.
	 */
	public static void enableLowSeverity() {
		low_severity = true;
	}

	/**
	 * Do not print low severity messages.
	 */
	public static void disableLowSeverity() {
		low_severity = false;
	}

	/**
	 * Enable variable statistics.
	 */
	public static void enableStatistics() {
		statistics = true;
	}

	/**
	 * Disable variable statistics.
	 */
	public static void disableStatistics() {
		statistics = false;
	}

	/**
	 * Assume AJAX returns JSON data.
	 */
	public static void enableAjaxReturnsJson() {
		ajax_returns_json = true;
	}

	/**
	 * Assume AJAX returns anything.
	 */
	public static void disableAjaxReturnsJson() {
		ajax_returns_json = false;
	}

	/**
	 * Assume [[CanPut]] always works.
	 */
	public static void enableAlwaysCanPut() {
		always_canput = true;
	}

	/**
	 * Do not assume [[CanPut]] always works.
	 */
	public static void disableAlwaysCanPut() {
		always_canput = false;
	}

	/**
	 * Ignore libraries.
	 */
	public static void enableIgnoreLibraries() {
		ignore_libraries = true;
	}

	/**
	 * Do not ignore libraries.
	 */
	public static void disableIgnoreLibraries() {
		ignore_libraries = false;
	}

	/**
	 * Is coverage enabled.
	 *
	 * @return True if coverage view is enabled.
	 */
	public static boolean isCoverageEnabled() {
		return coverage;
	}

	/**
	 * Is there a single event handler loop.
	 *
	 * @return True if there is a single event handler loop.
	 */
	public static boolean isSingleEventHandlerLoop() {
		return single_event_handler_loop;
	}

	/**
	 * Do we introduce random errors.
	 */
	public static boolean isErrorBatchMode(){
		return error_batch_mode;
	}

	/**
	 * Enable single event handler loop.
	 */
	public static void enableSingleEventHandlerLoop() {
		single_event_handler_loop = true;
	}

	/**
	 * Disable single event handler loop.
	 */
	public static void disableSingleEventHandlerLoop() {
		single_event_handler_loop = false;
	}

	/**
	 * Are all events treated equally?
	 */
	public static boolean isSingleEventHandlerType() {
		return single_event_handler_type;
	}

	/**
	 * Enable single event handler type.
	 */
	public static void enableSingleEventHandlerType() {
		single_event_handler_type = true;
	}

	/**
	 * Disable single event handler type.
	 */
	public static void disableSingleEventHandlerType() {
		single_event_handler_type = false;
	}

	/**
	 * Introduce random errors.
	 */
	private static void enableErrorBatchMode() {
		error_batch_mode  = true;		
	}

	/**
	 * Enable coverage view.
	 */
	private static void enableCoverage() {
		coverage = true;
	}

	/**
	 * Enable eval statistics.
	 */
	private static void enableEvalStatistics() {
		eval_statistics = true;		
	}

	/**
	 * Do we ignore HTML content.
	 */
	public static boolean isIgnoreHTMLContent() {
		return ignore_html_content;
	}

	/**
	 * Ignore HTML content.
	 */
	public static void enableIgnoreHTMLContent() {
		ignore_html_content = true;
	}

	/**
	 * Do not ignore HTML content.
	 */
	public static void disableIgnoreHTMLContent() {
		ignore_html_content = false;
	}

	/**
	 * Is the DOM DSL enabled.
	 */
	public static boolean isDSLEnabled() {
		return dsl;
	}

	/**
	 * Is uneval mode enabled.
	 */
	public static boolean isUnevalEnabled() { // FIXME: enable unevalizer by default? (affects use of live variables!)
		return uneval_mode;
	}

	/**
	 * Disable uneval mode.
	 */
	public static void disableUnevalMode() {
		uneval_mode = false;
	}

	/**
	 * Enable uneval mode.
	 */
	public static void enableUnevalMode() {
		uneval_mode = true;
	}

	/**
	 * Enable renaming.
	 */
	public static void enableRenaming() { // TODO: enable renaming by default?
		renaming = true;
	}

	/**
	 * Disable renaming.
	 */
	public static void disableRenaming() {
		renaming = false;
	}
	
	/**
	 * Enables no charged calls.
	 */
	public static void enableNoChargedCalls() {
		no_charged_calls = true;
	}

	/**
	 * Disables no charged calls.
	 */
	public static void disableNoChargedCalls() {
		no_charged_calls = false;
	}
}
