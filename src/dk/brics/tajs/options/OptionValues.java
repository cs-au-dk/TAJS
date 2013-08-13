package dk.brics.tajs.options;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Pair;

public class OptionValues {
	
	private static void copyFields(OptionValues from, OptionValues to) {
		for (Field f : OptionValues.class.getDeclaredFields()) {
			f.setAccessible(true);
			try {
				Object value = f.get(from);
				if (value instanceof Cloneable) {
					for (Method possibleClone : value.getClass().getDeclaredMethods()) {
						possibleClone.setAccessible(true);
						if ("clone".equals(possibleClone.getName()) && possibleClone.getParameterTypes().length == 0) {
							value = possibleClone.invoke(value);
						}
					}
				}
				f.set(to, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void parse(OptionValues base, String... args) {
		CmdLineParser parser = new CmdLineParser(base);
		try {
			parser.parseArgument(args);
			{
				// special cases which has side effects when enabled
				// TODO avoid these brittle side effects?
				if (base.iterationBoundString != null && !base.iterationBoundString.isEmpty()) {
					base.iterationBound = Integer.parseInt(base.iterationBoundString);
				}
				if (base.ignoredLibrariesString != null && !base.ignoredLibrariesString.isEmpty()) {
					base.ignoreLibraries = true;
					base.ignoredLibraries = Collections.newSet(Arrays.asList(base.ignoredLibrariesString.split(",")));
				}
				if (base.test) {
					base.enableTest();
				}
				if (base.testFlowgraphBuilder) {
					base.enableTestFlowGraphBuiler();
				}
				if (base.debug) {
					base.enableDebug();
				}
			}
			if (base.help) {
				base.describe();
			}
		} catch (CmdLineException e) {
			parser.printUsage(System.err);
			throw new RuntimeException(e);
		}
	}

	@Option(name = "-debug-output-on-crash", usage = "Tries to output extra debug information on crashes")
	private boolean debugOutputOnCrash;

	@Option(name = "-context-specialization", usage = "Enables context specialization")
	private boolean contextSpecialization;
	
	@Option(name = "-no-local-path-sensitivity", usage = "Disable local path sensitivity")
	private boolean noLocalPathSensitivity;
	
	@Option(name = "-no-context-sensitivity", usage = "Disable context sensitivity")
	private boolean noContextSensitivity;
	
	@Option(name = "-no-recency", usage = "Disable recency abstraction")
	private boolean noRecency;
	
	@Option(name = "-no-modified", usage = "Disable modified flags")
	private boolean noModified;
	
	@Option(name = "-no-exceptions", usage = "Disable implicit exception flow")
	private boolean noExceptions;
	
	@Option(name = "-no-gc", usage = "Disable abstract garbage collection")
	private boolean noGc;
	
	@Option(name = "-no-lazy", usage = "Disable lazy propagation")
	private boolean noLazy;
	
	@Option(name = "-flowgraph-optimization", usage = "Enable flowgraph optimization")
	private boolean flowgraphOptimization;
	
	@Option(name = "-no-copy-on-write", usage = "Disable copy-on-write")
	private boolean noCopyOnWrite;
	
	@Option(name = "-no-hybrid-collections", usage = "Disable hybrid collections")
	private boolean noHybridCollections;
	
	@Option(name = "-no-charged-calls", usage = "Disable charged calls")
	private boolean noChargedCalls;
	
	@Option(name = "-low-severity", usage = "Enable low severity messages")
	private boolean lowSeverity;
	
	@Option(name = "-unsound", usage = "Enable unsound assumptions")
	private boolean unsound;
	
	@Option(name = "-flowgraph", usage = "Output flowgraph.dot")
	private boolean flowgraph;
	
	@Option(name = "-callgraph", usage = "Output callgraph.dot")
	private boolean callgraph;
	
	@Option(name = "-debug", usage = "Output debug information")
	private boolean debug;
	
	@Option(name = "-collect-variable-info", usage = "Output type and line information on reachable variables")
	private boolean collectVariableInfo;
	
	@Option(name = "-newflow", usage = "Report summary of new flow at function entries")
	private boolean newflow;

	@Option(name = "-states", usage = "Output intermediate abstract states")
	private boolean states;
	
	@Option(name = "-test", usage = "Test mode (implies quiet), ensures predictable iteration orders")
	private boolean test;
	
	@Option(name = "-test-flowgraph-builder", usage = "Test flow graph builder (implies test mode)")
	private boolean testFlowgraphBuilder;
	
	@Option(name = "-timing", usage = "Report analysis time")
	private boolean timing;
	
	@Option(name = "-statistics", usage = "Report statistics")
	private boolean statistics;

	@Option(name = "-memory-usage", usage = "Report the memory usage of the analysis")
	private boolean memoryUsage;
	
	@Option(name = "-quiet", usage = "Only output results, not progress")
	private boolean quiet;
	
	@Option(name = "-dom", usage = "Enable Mozilla DOM browser model")
	private boolean includeDom;
	
	@Option(name = "-propagate-dead-flow", usage = "Propagate empty values")
	private boolean propagateDeadFlow;

	@Option(name = "-correlation-tracking", usage = "Enable correlation tracking")
	private boolean correlationTrackingMode;
	
	@Option(name = "-unroll-one-and-a-half", usage = "Enable 1 1/2 loop unrolling")
	private boolean unrollOneAndAHalf;
	
	@Option(name = "-always-canput", usage = "Assume [[CanPut]] always succeeds")
	private boolean alwaysCanput;
	
	@Option(name = "-eval-statistics", usage = "Don't fail on use of eval and innerHTML, but record their use")
	private boolean evalStatistics;
	
	@Option(name = "-coverage", usage = "Output a view of the source with unreachable lines highlighted")
	private boolean coverage;
	
	@Option(name = "-single-event-handler-loop", usage = "Use a single non-deterministic event loop for events")
	private boolean singleEventHandlerLoop;

	@Option(name = "-no-messages", usage = "Don't output analysis messages")
	private boolean no_messages;
	
	@Option(name = "-single-event-handler-type", usage = "Do not distinguish between different types of event handlers")
	private boolean single_event_handler_type;
	
	@Option(name = "-introduce-error", usage = "Measure precision by randomly introducing syntax errors")
	private boolean introduceError;
	
	@Option(name = "-ignore-html-content", usage = "Ignore the content of the HTML page, i.e. id, names, event handlers etc.")
	private boolean ignore_html_content;
	
	@Option(name = "-dsl", usage = "Load DSL object models")
	private boolean dsl;
	
	@Option(name = "-uneval", usage = "Try to remove calls to eval")
	private boolean unevalMode;
	
	@Option(name = "-serialize-final-state", usage = "Serialize the final analysis state")
	private boolean serializeFinalState;
	
	@Option(name = "-polymorphic", usage = "Enables use of polymorphic objects")
	private boolean polymorphic;
	
	@Option(name = "-return-json", usage = "Assume that AJAX calls return JSON")
	private boolean ajaxReturnsJson;
	
	@Option(name = "-help", usage = "prints this message")
	private boolean help;
	
	@Option(name = "-iteration-bound", usage = "Bounds the number of iterations the solver performs")
	private String iterationBoundString;
	private int iterationBound = -1;
	
	@Option(name = "-for-in", usage = "Enable for-in specialization")
	private boolean forInSpecialization;

	@Option(name = "-ignore-libraries", usage = "Ignore unreachable code messages from libraries (the library names must be separated by a single comma!)")
	private String ignoredLibrariesString;
	private boolean ignoreLibraries;
	private Set<String> ignoredLibraries = new LinkedHashSet<>();

	private int maxSuspiciousnessLevel = -1;

	@Argument
	private List<String> arguments = new ArrayList<>();

	public OptionValues() {
		this(null, null);
	}

	public OptionValues(OptionValues base) {
		this(base, null);
	}

	public OptionValues(OptionValues base, String[] args) {
		if (base != null) {
			copyFields(base, this);
		}
		if (args != null) {
			parse(this, args);
		}
	}

	public OptionValues(String[] args) {
		this(null, args);
	}

	@Override
	protected OptionValues clone() {
		return new OptionValues(this);
	}

	/**
	 * Prints a description of the available options.
	 */
	public void describe() {
		new CmdLineParser(this).printUsage(System.out);
	}

	/**
	 * Assume AJAX returns anything.
	 */
	public void disableAjaxReturnsJson() {
		ajaxReturnsJson = false;
	}

	/**
	 * Do not assume [[CanPut]] always works.
	 */
	public void disableAlwaysCanPut() {
		alwaysCanput = false;
	}

	/**
	 * Disables a bounded number of iterations
	 */
	public void disableBoundedIterations() {
		iterationBoundString = "-1";
		iterationBound = -1;
	}
	
	/**
	 * Disable callgraph dumping.
	 */
	public void disableCallgraph() {
		callgraph = false;
	}

	/**
	 * Disable variable information collection.
	 */
	public void disableCollectVariableInfo() {
		collectVariableInfo = false;
	}

	/**
	 * Disable context specialization.
	 */
	public void disableContextSpecialization() {
		contextSpecialization = false;
	}

	/**
	 * Disable correlation tracking.
	 */
	public void disableCorrelationTracking() {
		correlationTrackingMode = false;
	}

	/**
	 * Disable debug mode.
	 * Also sets log level to Level.INFO.
	 */
	public void disableDebug() {
		debug = false;
		Logger.getRootLogger().setLevel(Level.INFO);
	}

	public void disableDebugOutputOnCrash() {
		debugOutputOnCrash = false;
	}

	/**
	 * Disable the DOM DSL.
	 */
	public void disableDSL() {
		dsl = false;
	}

	/**
	 * Disable flowgraph dumping.
	 */
	public void disableFlowgraph() {
		flowgraph = false;
	}

	/**
	 * Disable flow graph optimization.
	 */
	public void disableFlowgraphOptimization() {
		flowgraphOptimization = false;
	}

	/**
	 * Do not ignore HTML content.
	 */
	public void disableIgnoreHTMLContent() {
		ignore_html_content = false;
	}

	/**
	 * Do not ignore libraries.
	 */
	public void disableIgnoreLibraries() {
		ignoreLibraries = false;
		ignoredLibrariesString = "";
		ignoredLibraries = new LinkedHashSet<>();

	}

	/**
	 * Disable DOM model.
	 */
	public void disableIncludeDom() {
		includeDom = false;
	}

	/**
	 * Do not print low severity messages.
	 */
	public void disableLowSeverity() {
		lowSeverity = false;
	}

	/**
	 * Disable memory usage statistics.
	 */
	public void disableMemoryUsage() {
		memoryUsage = false;
	}

	/**
	 * Disable reporting summaries at flow edges.
	 */
	public void disableNewflow() {
		newflow = false;
	}

	/**
	 * Disables no charged calls.
	 */
	public void disableNoChargedCalls() {
		noChargedCalls = false;
	}

	/**
	 * Enable context sensitivity.
	 */
	public void disableNoContextSensitivity() {
		noContextSensitivity = false;
	}

	/**
	 * Enable copy on write.
	 */
	public void disableNoCopyOnWrite() {
		noCopyOnWrite = false;
	}

	/**
	 * Disable ignoring exceptions.
	 */
	public void disableNoExceptions() {
		noExceptions = false;
	}

	/**
	 * Enable abstract garbage collection.
	 */
	public void disableNoGc() {
		noGc = false;
	}

	/**
	 * Enable hybrid collections.
	 */
	public void disableNoHybridCollections() {
		noHybridCollections = false;
	}

	/**
	 * Enable lazy propagation.
	 */
	public void disableNoLazy() {
		noLazy = false;
	}

	/**
	 * Disable local path sensitivity.
	 */
	public void disableNoLocalPathSensitivity() {
		noLocalPathSensitivity = false;
	}

	/**
	 * Disables no analysis messages.
	 */
	public void disableNoMessages() {
		no_messages = false;
	}

	/**
	 * Enable modified flags.
	 */
	public void disableNoModified() {
		noModified = false;
	}

	/**
	 * Enable recency abstraction.
	 */
	public void disableNoRecency() {
		noRecency = false;
	}

	/**
	 * Disable polymorphic values.
	 */
	public void disablePolymorphic() {
		polymorphic = false;
	}

	/**
	 * Disable propagation of dead data flow.
	 */
	public void disablePropagateDeadFlow() {
		propagateDeadFlow = false;
	}

	/**
	 * Disable quiet mode.
	 */
	public void disableQuiet() {
		quiet = false;
	}

	/**
	 * Enables serialization of final analysis state
	 */
	public void disableSerializeFinalState() {
		serializeFinalState = false;
	}

	/**
	 * Disable single event handler loop.
	 */
	public void disableSingleEventHandlerLoop() {
		singleEventHandlerLoop = false;
	}

	/**
	 * Disable single event handler type.
	 */
	public void disableSingleEventHandlerType() {
		single_event_handler_type = false;
	}

	/**
	 * Disable output of intermediate states.
	 */
	public void disableStates() {
		states = false;
	}

	/**
	 * Disable variable statistics.
	 */
	public void disableStatistics() {
		statistics = false;
	}

	/**
	 * Disable test mode.
	 */
	public void disableTest() {
		test = false;
		quiet = false;
		lowSeverity = false;
	}

	/**
	 * Disable testing of flow graph builder. Also disables test mode.
	 */
	public void disableTestFlowGraphBuilder() {
		disableTest();
		testFlowgraphBuilder = false;
	}

	/**
	 * Disable timing of TAJS.
	 */
	public void disableTiming() {
		timing = false;
	}

	/**
	 * Disable uneval mode.
	 */
	public void disableUnevalMode() {
		unevalMode = false;
	}

	/**
	 * Disable 1 1/2 loop unrolling.
	 */
	public void disableUnrollOneAndAHalf() {
		unrollOneAndAHalf = false;
	}

	/**
	 * Disable unsound shortcuts.
	 */
	public void disableUnsound() {
		unsound = false;
	}

	/**
	 * Assume AJAX returns JSON data.
	 */
	public void enableAjaxReturnsJson() {
		ajaxReturnsJson = true;
	}

	/**
	 * Assume [[CanPut]] always works.
	 */
	public void enableAlwaysCanPut() {
		alwaysCanput = true;
	}

	/**
	 * Enables a bounded number of iterations
	 */
	public void enableBoundedIterations(int bound) {
		iterationBoundString = bound + "";
		iterationBound = bound;
	}
	
	/**
	 * Enable callgraph dumping.
	 */
	public void enableCallgraph() {
		callgraph = true;
	}

	/**
	 * Enable variable information collection.
	 */
	public void enableCollectVariableInfo() {
		collectVariableInfo = true;
	}

	/**
	 * Enable context specialization.
	 */
	public void enableContextSpecialization() {
		contextSpecialization = true;
	}

	/**
	 * Enable correlation tracking.
	 */
	public void enableCorrelationTracking() {
		correlationTrackingMode = true;
	}

	/**
	 * Enable coverage view.
	 */
	public void enableCoverage() {
		coverage = true;
	}

	/**
	 * Enable debug mode.
	 * Also sets log level to Level.DEBUG.
	 */
	public void enableDebug() {
		debug = true;
		Logger.getRootLogger().setLevel(Level.DEBUG);
	}

	public void enableDebugOutputOnCrash() {
		debugOutputOnCrash = true;
	}

	/**
	 * Enable the DOM DSL.
	 */
	public void enableDSL() {
		dsl = true;
	}

	/**
	 * Introduce random errors.
	 */
	public void enableErrorBatchMode() {
		introduceError = true;
	}

	/**
	 * Enable eval statistics.
	 */
	public void enableEvalStatistics() {
		evalStatistics = true;
	}

	/**
	 * Enable flowgraph dumping.
	 */
	public void enableFlowgraph() {
		flowgraph = true;
	}

	/**
	 * Enable flow graph optimization.
	 */
	public void enableFlowgraphOptimization() {
		flowgraphOptimization = true;
	}

	/**
	 * Ignore HTML content.
	 */
	public void enableIgnoreHTMLContent() {
		ignore_html_content = true;
	}

	/**
	 * Ignore libraries.
	 */
	public void enableIgnoreLibraries() {
		ignoreLibraries = true;
	}

	/**
	 * Enable DOM model.
	 */
	public void enableIncludeDom() {
		includeDom = true;
	}

	/**
	 * Print low severity messages.
	 */
	public void enableLowSeverity() {
		lowSeverity = true;
	}

	/**
	 * Enable memory usage statistics.
	 */
	public void enableMemoryUsage() {
		memoryUsage = true;
	}

	/**
	 * Enable reporting summaries at flow edges.
	 */
	public void enableNewflow() {
		newflow = true;
	}

	/**
	 * Enables no charged calls.
	 */
	public void enableNoChargedCalls() {
		noChargedCalls = true;
	}

	/**
	 * Disable context sensitivity.
	 */
	public void enableNoContextSensitivity() {
		noContextSensitivity = true;
	}

	/**
	 * Disable copy on write.
	 */
	public void enableNoCopyOnWrite() {
		noCopyOnWrite = true;
	}

	/**
	 * Enable ignoring exceptions.
	 */
	public void enableNoExceptions() {
		noExceptions = true;
	}

	/**
	 * Disable abstract garbage collection.
	 */
	public void enableNoGc() {
		noGc = true;
	}

	/**
	 * Disable hybrid collections.
	 */
	public void enableNoHybridCollections() {
		noHybridCollections = true;
	}

	/**
	 * Disable lazy propagation.
	 */
	public void enableNoLazy() {
		noLazy = true;
	}

	/**
	 * Disable local path sensitivity.
	 */
	public void enableNoLocalPathSensitivity() {
		noLocalPathSensitivity = true;
	}

	/**
	 * Enables no analysis messages.
	 */
	public void enableNoMessages() {
		no_messages = true;
	}

	/**
	 * Disable modified flags.
	 */
	public void enableNoModified() {
		noModified = true;
	}

	/**
	 * Disable recency abstraction.
	 */
	public void enableNoRecency() {
		noRecency = true;
	}

	/**
	 * Enable polymorphic values.
	 */
	public void enablePolymorphic() { // TODO: enable polymorphic by default?
		polymorphic = true;
	}

	/**
	 * Enable propagation of dead data flow.
	 */
	public void enablePropagateDeadFlow() {
		propagateDeadFlow = true;
	}

	/**
	 * Enable quiet mode.
	 */
	public void enableQuiet() {
		quiet = true;
	}

	/**
	 * Enables serialization of final analysis state
	 */
	public void enableSerializeFinalState() {
		serializeFinalState = true;
	}

	/**
	 * Enable single event handler loop.
	 */
	public void enableSingleEventHandlerLoop() {
		singleEventHandlerLoop = true;
	}

	/**
	 * Enable single event handler type.
	 */
	public void enableSingleEventHandlerType() {
		single_event_handler_type = true;
	}

	/**
	 * Enable output of intermediate states.
	 */
	public void enableStates() {
		states = true;
	}

	/**
	 * Enable variable statistics.
	 */
	public void enableStatistics() {
		statistics = true;
	}

	/**
	 * Avoid nondeterministic output.
	 * Also sets quiet mode and low_severity.
	 */
	public void enableTest() {
		test = true;
		quiet = true;
		lowSeverity = true;
	}

	/**
	 * Enable testing of flow graph builder. Implies test mode.
	 */
	public void enableTestFlowGraphBuiler() {
		enableTest();
		testFlowgraphBuilder = true;
	}

	/**
	 * Enable timing of TAJS.
	 */
	public void enableTiming() {
		timing = true;
	}

	/**
	 * Enable uneval mode.
	 */
	public void enableUnevalMode() {
		unevalMode = true;
	}

	/**
	 * Enable 1 1/2 loop unrolling.
	 */
	public void enableUnrollOneAndAHalf() {
		unrollOneAndAHalf = true;
	}

	/**
	 * Enable unsound shortcuts.
	 */
	public void enableUnsound() {
		unsound = true;
	}

	public List<String> getArguments() {
		return arguments;
	}
	
	public int getIterationBound() {
		return iterationBound;
	}
	
	/**
	 * Get the set of ignored libraries.
	 */
	public Set<String> getLibraries() {
		return ignoredLibraries;
	}

	public int getMaxSuspiciousnessLevel() {
		return maxSuspiciousnessLevel;
	}

	public List<Pair<String, ?>> getOptionValues() {
		try {
			List<Pair<String, ?>> options = new ArrayList<>();
			for (Field f : OptionValues.class.getDeclaredFields()) {
				f.setAccessible(true);
				Option annotation = f.getAnnotation(org.kohsuke.args4j.Option.class);
				if (annotation != null) {
					Object value = f.get(this);
					final String name = annotation.name();
					if (value == null) {
						// skip
					} else if (value instanceof Boolean && (Boolean) value) {
						options.add(Pair.make(name, value));
					} else if (value instanceof Integer && ((Integer) value) != -1) {
						options.add(Pair.make(name, value));
					} else if (value instanceof String && !"".equals(value) && !"-1".equals(value)) {
						options.add(Pair.make(name, value));
					} else if (value instanceof Collection && !((Collection<?>) value).isEmpty()) {
						options.add(Pair.make(name, value));
					}
				}
			}
			java.util.Collections.sort(options, new Comparator<Pair<String, ?>>() {
				@Override
				public int compare(Pair<String, ?> o1, Pair<String, ?> o2) {
					return o1.getFirst().compareTo(o2.getFirst());
				}
			});
			return options;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isAbortOnSuspiciousValueCreationEnabled() {
		return maxSuspiciousnessLevel >= 0;
	}

	public boolean isAlwaysCanPut() {
		return alwaysCanput;
	}

	public boolean isBoundedIterationsEnabled() {
		return iterationBound != -1;
	}

	/**
	 * If set, output callgraph.dot.
	 */
	public boolean isCallGraphEnabled() {
		return callgraph;
	}

	/**
	 * If set, don't use charged calls.
	 */
	public boolean isChargedCallsDisabled() {

		return noChargedCalls;
	}

	/**
	 * If set, summarize information on reachable variables, e.g. their type and location.
	 */
	public boolean isCollectVariableInfoEnabled() {
		return collectVariableInfo;
	}

	/**
	 * If set, do not use context sensitivity.
	 */
	public boolean isContextSensitivityDisabled() {
		return noContextSensitivity;
	}

	/**
	 * If set, use context specialization
	 */
	public boolean isContextSpecializationEnabled() {
		return contextSpecialization;
	}

	/**
	 * If set, do not use copy-on-write.
	 */
	public boolean isCopyOnWriteDisabled() {
		return noCopyOnWrite;
	}

	/**
	 * Is correlation tracking enabled.
	 */
	public boolean isCorrelationTrackingEnabled() {
		return correlationTrackingMode;
	}

	/**
	 * Is coverage enabled.
	 * 
	 * @return True if coverage view is enabled.
	 */
	public boolean isCoverageEnabled() {
		return coverage;
	}

	/**
	 * Are we currently in debug mode.
	 */
	public boolean isDebugEnabled() {
		return debug;
	}

	/**
	 * Are we currently in debug or test mode.
	 */
	public boolean isDebugOrTestEnabled() {
		return debug || test;
	}

	public boolean isDebugOutputOnCrashEnabled() {
		return debugOutputOnCrash;
	}

	/**
	 * If set, the DOM objects and functions are part of the initial state.
	 */
	public boolean isDOMEnabled() {
		return includeDom;
	}

	/**
	 * Is the DOM DSL enabled.
	 */
	public boolean isDSLEnabled() {
		return dsl;
	}

	/**
	 * Do we introduce random errors.
	 */
	public boolean isErrorBatchMode() {
		return introduceError;
	}

	/**
	 * Eval statistics enabled.
	 */
	public boolean isEvalStatistics() {
		return evalStatistics;
	}

	/**
	 * If set, exclude implicit exception flow.
	 */
	public boolean isExceptionsDisabled() {
		return noExceptions;
	}

	/**
	 * If set, output flowgraph.dot.
	 */
	public boolean isFlowGraphEnabled() {
		return flowgraph;
	}

	/**
	 * If set, do not perform flowgraph optimization.
	 */
	public boolean isFlowGraphOptimizationEnabled() {
		return flowgraphOptimization;
	}

	/**
	 * If set, do not use abstract garbage collection.
	 */
	public boolean isGCDisabled() {
		return noGc;
	}

	/**
	 * If set, do not use {@link dk.brics.tajs.util.HybridArrayHashMap} and {@link dk.brics.tajs.util.HybridArrayHashSet}.
	 */
	public boolean isHybridCollectionsDisabled() {
		return noHybridCollections;
	}

	/**
	 * Do we ignore HTML content.
	 */
	public boolean isIgnoreHTMLContent() {
		return ignore_html_content;
	}

	/**
	 * If set, ignore unreachable code warnings from libraries.
	 */
	public boolean isIgnoreLibrariesEnabled() {
		return ignoreLibraries;
	}

	/**
	 * If set, print intermediate abstract states.
	 */
	public boolean isIntermediateStatesEnabled() {
		return states;
	}

	/**
	 * If set, do not use lazy propagation.
	 */
	public boolean isLazyDisabled() {
		return noLazy;
	}

	/**
	 * If set, do not use local path sensitivity, including assume node effects.
	 */
	public boolean isLocalPathSensitivityDisabled() {
		return noLocalPathSensitivity;
	}

	/**
	 * If set, print low severity messages.
	 */
	public boolean isLowSeverityEnabled() {
		return lowSeverity;
	}

	/**
	 * If set, measure memory usage.
	 */
	public boolean isMemoryMeasurementEnabled() {
		return memoryUsage;
	}

	/**
	 * If set, do not use modified flags.
	 */
	public boolean isModifiedDisabled() {
		return noModified;
	}

	/**
	 * If set, report summary of new flow at function entries.
	 */
	public boolean isNewFlowEnabled() {
		return newflow;
	}

	/**
	 * If set, don't output analysis messages.
	 */
	public boolean isNoMessages() {
		return no_messages;
	}

	/**
	 * If set, use polymorphic abstract values.
	 */
	public boolean isPolymorphicEnabled() {
		return polymorphic;
	}

	/**
	 * If set, dead data flow is propagated.
	 */
	public boolean isPropagateDeadFlow() {
		return propagateDeadFlow;
	}

	/**
	 * If set, only report results.
	 */
	public boolean isQuietEnabled() {
		return quiet;
	}

	/**
	 * If set, do not use recency abstraction.
	 */
	public boolean isRecencyDisabled() {
		return noRecency;
	}

	/**
	 * Assume AJAX returns JSON.
	 */
	public boolean isReturnJSON() {
		return ajaxReturnsJson;
	}

	public boolean isSerializeFinalStateEnabled() {
		return serializeFinalState;
	}

	/**
	 * Is there a single event handler loop.
	 * 
	 * @return True if there is a single event handler loop.
	 */
	public boolean isSingleEventHandlerLoop() {
		return singleEventHandlerLoop;
	}

	/**
	 * Are all events treated equally?
	 */
	public boolean isSingleEventHandlerType() {
		return single_event_handler_type;
	}

	/**
	 * If set, report statistics.
	 */
	public boolean isStatisticsEnabled() {
		return statistics;
	}

	/**
	 * If set, avoid nondeterministic output.
	 */
	public boolean isTestEnabled() {
		return test;
	}

	/**
	 * If set, output flow graphs to stdout for testing purposes.
	 */
	public boolean isTestFlowGraphBuilderEnabled() {
		return testFlowgraphBuilder;
	}

	/**
	 * If set, report timings.
	 */
	public boolean isTimingEnabled() {
		return timing;
	}

	/**
	 * Is uneval mode enabled.
	 */
	public boolean isUnevalEnabled() { // FIXME: enable unevalizer by default? (affects use of live variables!)
		return unevalMode;
	}

	/**
	 * Is one and a half loop unrolling enabled.
	 */
	public boolean isUnrollOneAndAHalfEnabled() {
		return unrollOneAndAHalf;
	}

	/**
	 * If set, allow certain unsound tricks.
	 */
	public boolean isUnsoundEnabled() {
		return unsound;
	}

	public void setMaxSuspiciousnessLevel(int level) {
		maxSuspiciousnessLevel = level;
	}

	@Override
	public String toString() {
		List<Pair<String, ?>> optionValues = new ArrayList<>(getOptionValues());
		java.util.Collections.sort(optionValues, new Comparator<Pair<String, ?>>() {
			@Override
			public int compare(Pair<String, ?> o1, Pair<String, ?> o2) {
				return o1.getFirst().compareTo(o2.getFirst());
			}
		});

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Pair<String, ?> value : optionValues) {
			if (!first) {
				sb.append(" ");
			} else {
				first = false;
			}
			sb.append(value.getFirst());
			if (!(value.getSecond() instanceof Boolean)) {
				sb.append(" " + value.getSecond().toString());
			}
		}
		for (String argument : arguments) {
			if (!first) {
				sb.append(" ");
			} else {
				first = false;
			}
			sb.append(argument);
		}
		return sb.toString();
	}

	public void enableForInSpecialization() {
		this.forInSpecialization = true;
	}

	public void disableForInSpecialization() {
		this.forInSpecialization = false;
	}
	
	public boolean isForInSpecializationEnabled(){
		return forInSpecialization;
	}
}
