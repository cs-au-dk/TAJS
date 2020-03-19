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

package dk.brics.tajs.options;

import dk.brics.tajs.util.AnalysisException;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Option values.
 * <p>
 * All options are disabled by default.
 */
public class OptionValues {

    @Option(name = "-no-control-sensitivity", usage = "Disable control sensitivity")
    private boolean noControlSensitivity;

    @Option(name = "-no-object-sensitivity", usage = "Disable object sensitivity")
    private boolean noObjectSensitivity;

    @Option(name = "-no-recency", usage = "Disable recency abstraction")
    private boolean noRecency;

    @Option(name = "-no-modified", usage = "Disable modified flags")
    private boolean noModified;

    @Option(name = "-no-gc", usage = "Disable abstract garbage collection")
    private boolean noGc;

    @Option(name = "-no-lazy", usage = "Disable lazy propagation")
    private boolean noLazy;

    @Option(name = "-no-copy-on-write", usage = "Disable copy-on-write")
    private boolean noCopyOnWrite;

    @Option(name = "-no-hybrid-collections", usage = "Disable hybrid collections")
    private boolean noHybridCollections;

    @Option(name = "-no-charged-calls", usage = "Disable charged calls")
    private boolean noChargedCalls;

    @Option(name = "-no-concrete", usage = "Disable concrete interpretation for selected native functions")
    private boolean noConcreteNative;

    @Option(name = "-no-for-in", usage = "Disable for-in specialization")
    private boolean noForInSpecialization;

    @Option(name = "-no-user-events", usage = "Disable modeling of user events")
    private boolean noUserEvents;

//	@Optional(name = "-flowgraph-optimization", usage = "Enable flowgraph optimization")
//	private boolean flowgraphOptimization;

    @Option(name = "-context-specialization", usage = "Enable context specialization")
    private boolean contextSpecialization;

    @Option(name = "-low-severity", usage = "Enable low severity messages")
    private boolean lowSeverity;

    @Option(name = "-unsound", usage = "Enable unsound assumptions")
    private String unsoundnessString;

    private UnsoundnessOptionValues unsoundness = new UnsoundnessOptionValues(null, null);

    @Option(name = "-flowgraph", usage = "Output flowgraph.dot")
    private boolean flowgraph;

    @Option(name = "-callgraph", usage = "Output callgraph.dot")
    private boolean callgraph;

    @Option(name = "-debug", usage = "Output debug information")
    private boolean debug;

    @Option(name = "-show-variable-info", usage = "Output type and line information for variables")
    private boolean showVariableInfo;

    @Option(name = "-newflow", usage = "Report summary of new flow at function entries")
    private boolean newflow;

    @Option(name = "-states", usage = "Output intermediate abstract states (implies -debug)")
    private boolean states;

    @Option(name = "-test", usage = "Test mode (ensures deterministic behavior, performs extra runtime checks, and implies -test-soundness)")
    private boolean test;

    @Option(name = "-test-flowgraph-builder", usage = "Test flow graph builder (implies -test)")
    private boolean testFlowgraphBuilder;

    @Option(name = "-timing", usage = "Report analysis time")
    private boolean timing;

    @Option(name = "-statistics", usage = "Report statistics")
    private boolean statistics;

    @Option(name = "-memory-usage", usage = "Report the memory usage of the analysis (makes analysis slower due to GC'ing)")
    private boolean memoryUsage;

    @Option(name = "-quiet", usage = "Only output results, not progress (makes analysis slightly faster)")
    private boolean quiet;

    @Option(name = "-dom", usage = "Enable Mozilla DOM browser model")
    private boolean includeDom;

    @Option(name = "-propagate-dead-flow", usage = "Propagate empty values")
    private boolean propagateDeadFlow;

    @Option(name = "-always-canput", usage = "Assume [[CanPut]] always succeeds")
    private boolean alwaysCanput;

    @Option(name = "-eval-statistics", usage = "Report uses of eval and innerHTML")
    private boolean evalStatistics;

//	@Optional(name = "-introduce-error", usage = "Measure precision by randomly introducing syntax errors")
//	private boolean introduceError;

    @Option(name = "-no-messages", usage = "Disable analysis messages")
    private boolean no_messages;

    @Option(name = "-single-event-handler-type", usage = "Do not distinguish between different types of event handlers")
    private boolean single_event_handler_type;

    @Option(name = "-ignore-html-content", usage = "Ignore the content of the HTML page")
    private boolean ignore_html_content;

    @Option(name = "-uneval", usage = "Try to remove calls to eval")
    private boolean unevalizer;

    @Option(name = "-no-polymorphic", usage = "Disable use of polymorphic objects")
    private boolean no_polymorphic;

    @Option(name = "-return-json", usage = "Assume that AJAX calls return JSON")
    private boolean ajaxReturnsJson;

    @Option(name = "-ignore-libraries", usage = "Ignore unreachable code messages from libraries (library names separated by comma)")
    private String ignoredLibrariesString;

    private Set<String> ignoredLibraries = new LinkedHashSet<>();

    @Option(name = "-context-sensitive-heap", usage = "Enable selective context sensitive heap abstractions (ignored if -determinacy is enabled)")
    private boolean contextSensitiveHeap;

    @Option(name = "-parameter-sensitivity", usage = "Enable usage of different contexts for (some) calls based on the argument values (ignored if -determinacy is enabled)")
    private boolean parameterSensitivity;

    @Option(name = "-ignore-unreached", usage = "Ignore code that is unreached according to the log file")
    private boolean ignoreUnreached;

    @Option(name = "-loop-unrolling", usage = "Enable unrolling of loops up to [n] times (default: 1)")
    private int loopUnrollings = -1; // -1 represents the default value

    @Option(name = "-determinacy", usage = "Enable the techniques described in 'Determinacy in Static Analysis of jQuery', OOPSLA 2014")
    private boolean determinacy;

    @Option(name = "-polyfill-mdn", usage = "Enable use of polyfills from the Mozilla Developer Network web pages")
    private boolean polyfillMDN;

    @Option(name = "-polyfill-es6-collections", usage = "Enable use of polyfills for ES6 collections")
    private boolean polyfillES6Collections;

    @Option(name = "-polyfill-typed-arrays", usage = "Enable use of polyfills for typed arrays (Int8Array, Float64Array, etc.)")
    private boolean polyfillTypedArrays;

    @Option(name = "-polyfill-es6-promises", usage = "Enable use of polyfills for ES6 promises")
    private boolean polyfillES6Promises;

    @Option(name = "-polyfill-es6-iterators", usage = "Enable use of polyfills for ES6 iterators")
    private boolean polyfillES6Iterators;

    @Option(name = "-async-events", usage = "Enable execution of asynchronous event handlers with TAJS_asyncListen")
    private boolean asyncEvents;

    @Option(name = "-no-string-sets", usage = "Disables the use of string sets")
    private boolean noStringSets;

    @Option(name = "-test-soundness", usage = "Test that the analysis fixpoint over-approximates concrete behaviors")
    private boolean testSoundness;

    @Option(name = "-generate-log", usage = "Produce the log to be used by -test-soundness (unless already up-to-date)")
    private boolean generateLog;

    @Option(name = "-log-file", usage = "Specify the location of the soundness log file to use")
    private String logFile;

    @Option(name = "-config", usage = "The location of tajs.properties properties file")
    private String config;

    @Option(name = "-show-internal-messages", usage = "Show messages for host functions modeled as JavaScript source code")
    private boolean showInternalMessages;

    @Option(name = "-console-model", usage = "Add a model of the console object")
    private boolean consoleModel;

    @Option(name = "-common-async-polyfill", usage = "Add a model of the setTimeout/setInterval functions")
    private boolean commonAsyncPolyfill;

    @Option(name = "-no-strict", usage = "Disable support for the 'use strict' directive")
    private boolean noStrict;

    @Option(name = "-deterministic-collections", usage = "Use collections with deterministic iteration order")
    private boolean deterministicCollections;

    @Option(name = "-specialize-all-boxed-primitives", usage = "Enable the specialized boxing of all primitives, instead of only concrete primitives")
    private boolean specializeAllBoxedPrimitives;

    @Option(name = "-time-limit", usage = "Limit how many seconds the analysis is allowed to run")
    private int analysisTimeLimit = -1;

    @Option(name = "-transfer-limit", usage = "Limit how many node transfers the analysis is allowed to perform")
    private int analysisTransferLimit = -1;

    @Option(name = "-do-not-expect-ordinary-exit", usage = "Do not expect the program to reach the ordinary exit (for testing)")
    private boolean doNotExpectOrdinaryExit;

    @Option(name = "-inspector", usage = "Start TAJS Inspector after analysis")
    private boolean inspector;

    @Option(name = "-analysis-limitations-warn-only", usage = "If analysis limitation encountered, emit warning but continue (only used with -test)")
    private boolean analysisLimitationWarnOnly;

    @Option(name = "-nodejs", usage = "Use Node.js environment for analysis and soundness testing (currently only 'require' is supported)")
    private boolean nodejs;

    @Option(name = "-type-filtering", usage = "Use TypeScript type declarations for dataflow filtering (use with -nodejs)")
    private boolean typeFiltering;

    @Option(name = "-babel", usage = "Enables Babel preprocessing for source files")
    private boolean babel;

    @Option(name = "-type-checks", usage = "Enables type checking (currently only used with ReaGenT)")
    private boolean typeCheckEnabled;

    @Option(name = "-blended-analysis", usage = "Filter abstract values based on values observed concretely")
    private boolean blendedAnalysis;

    @Option(name = "-no-filtering", usage = "Disable filtering")
    private boolean noFiltering;

    @Option(name = "-property-name-partitioning", usage = "Partitions the property name value at imprecise dynamic property reads")
    private boolean propNamePartitioning;

    @Option(name = "-type-partitioning", usage = "Partitions the argument at calls with one argument based on the types of that argument")
    private boolean typePartitioning;

    @Option(name = "-free-variable-partitioning", usage = "Partitions free variables and function expressions with free variables")
    private boolean freeVariablePartitioning;

    @Option(name = "-no-string-replace-polyfill", usage = "Disables the use of the String.prototype.replace polyfill")
    private boolean noStringReplacePolyfill;

    @Option(name = "-no-error-capture-stack-trace-polyfill", usage = "Disables the use of Error.captureStackTrace polyfill")
    private boolean noErrorCaptureStackTracePolyfill;

    @Option(name = "-pointer", usage="Provide the source Pointer for points to set evaluation")
    private String pointer;

    @Option(name="-line", usage="Provide line number for points to set evaluation")
    private Integer line;

    @Argument
    private List<Path> arguments = new ArrayList<>();

    private SoundnessTesterOptions soundnessTesterOptions = new SoundnessTesterOptions();

    /**
     * Constructs a new <code>OptionValues</code> object with default settings.
     */
    public OptionValues() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OptionValues that = (OptionValues) o;

        if (noControlSensitivity != that.noControlSensitivity) return false;
        if (noObjectSensitivity != that.noObjectSensitivity) return false;
        if (noRecency != that.noRecency) return false;
        if (noModified != that.noModified) return false;
        if (noGc != that.noGc) return false;
        if (noLazy != that.noLazy) return false;
        if (noCopyOnWrite != that.noCopyOnWrite) return false;
        if (noHybridCollections != that.noHybridCollections) return false;
        if (noChargedCalls != that.noChargedCalls) return false;
        if (noConcreteNative != that.noConcreteNative) return false;
        if (noForInSpecialization != that.noForInSpecialization) return false;
        if (noUserEvents != that.noUserEvents) return false;
        if (contextSpecialization != that.contextSpecialization) return false;
        if (lowSeverity != that.lowSeverity) return false;
        if (flowgraph != that.flowgraph) return false;
        if (callgraph != that.callgraph) return false;
        if (debug != that.debug) return false;
        if (showVariableInfo != that.showVariableInfo) return false;
        if (newflow != that.newflow) return false;
        if (states != that.states) return false;
        if (test != that.test) return false;
        if (testFlowgraphBuilder != that.testFlowgraphBuilder) return false;
        if (timing != that.timing) return false;
        if (statistics != that.statistics) return false;
        if (memoryUsage != that.memoryUsage) return false;
        if (quiet != that.quiet) return false;
        if (includeDom != that.includeDom) return false;
        if (propagateDeadFlow != that.propagateDeadFlow) return false;
        if (alwaysCanput != that.alwaysCanput) return false;
        if (evalStatistics != that.evalStatistics) return false;
        if (no_messages != that.no_messages) return false;
        if (single_event_handler_type != that.single_event_handler_type) return false;
        if (ignore_html_content != that.ignore_html_content) return false;
        if (unevalizer != that.unevalizer) return false;
        if (no_polymorphic != that.no_polymorphic) return false;
        if (ajaxReturnsJson != that.ajaxReturnsJson) return false;
        if (contextSensitiveHeap != that.contextSensitiveHeap) return false;
        if (parameterSensitivity != that.parameterSensitivity) return false;
        if (ignoreUnreached != that.ignoreUnreached) return false;
        if (loopUnrollings != that.loopUnrollings) return false;
        if (determinacy != that.determinacy) return false;
        if (typeCheckEnabled != that.typeCheckEnabled) return false;
        if (polyfillMDN != that.polyfillMDN) return false;
        if (polyfillES6Collections != that.polyfillES6Collections) return false;
        if (polyfillTypedArrays != that.polyfillTypedArrays) return false;
        if (polyfillES6Promises != that.polyfillES6Promises) return false;
        if (polyfillES6Iterators != that.polyfillES6Iterators) return false;
        if (asyncEvents != that.asyncEvents) return false;
        if (noStringSets != that.noStringSets) return false;
        if (testSoundness != that.testSoundness) return false;
        if (generateLog != that.generateLog) return false;
        if (showInternalMessages != that.showInternalMessages) return false;
        if (consoleModel != that.consoleModel) return false;
        if (commonAsyncPolyfill != that.commonAsyncPolyfill) return false;
        if (noStrict != that.noStrict) return false;
        if (deterministicCollections != that.deterministicCollections) return false;
        if (specializeAllBoxedPrimitives != that.specializeAllBoxedPrimitives) return false;
        if (analysisTimeLimit != that.analysisTimeLimit) return false;
        if (doNotExpectOrdinaryExit != that.doNotExpectOrdinaryExit) return false;
        if (inspector != that.inspector) return false;
        if (babel != that.babel) return false;
        if (propNamePartitioning != that.propNamePartitioning) return false;
        if (typePartitioning != that.typePartitioning) return false;
        if (freeVariablePartitioning != that.freeVariablePartitioning) return false;
        if (noStringReplacePolyfill != that.noStringReplacePolyfill) return false;
        if (noErrorCaptureStackTracePolyfill != that.noErrorCaptureStackTracePolyfill) return false;
        if (!Objects.equals(unsoundnessString, that.unsoundnessString)) return false;
        if (!Objects.equals(unsoundness, that.unsoundness)) return false;
        if (!Objects.equals(ignoredLibrariesString, that.ignoredLibrariesString)) return false;
        if (!Objects.equals(ignoredLibraries, that.ignoredLibraries)) return false;
        if (!Objects.equals(logFile, that.logFile)) return false;
        if (!Objects.equals(config, that.config)) return false;
        if (!Objects.equals(arguments, that.arguments)) return false;
        if (blendedAnalysis != that.blendedAnalysis) return false;
        if (noFiltering != that.noFiltering) return false;
        if (pointer != that.pointer) return false;
        if (line != that.line) return false;
        return Objects.equals(soundnessTesterOptions, that.soundnessTesterOptions);
    }

    @Override
    public int hashCode() {
        int result = (noControlSensitivity ? 1 : 0);
        result = 31 * result + (noObjectSensitivity ? 1 : 0);
        result = 31 * result + (noRecency ? 1 : 0);
        result = 31 * result + (noModified ? 1 : 0);
        result = 31 * result + (noGc ? 1 : 0);
        result = 31 * result + (noLazy ? 1 : 0);
        result = 31 * result + (noCopyOnWrite ? 1 : 0);
        result = 31 * result + (noHybridCollections ? 1 : 0);
        result = 31 * result + (noChargedCalls ? 1 : 0);
        result = 31 * result + (noConcreteNative ? 1 : 0);
        result = 31 * result + (noForInSpecialization ? 1 : 0);
        result = 31 * result + (noUserEvents ? 1 : 0);
        result = 31 * result + (contextSpecialization ? 1 : 0);
        result = 31 * result + (lowSeverity ? 1 : 0);
        result = 31 * result + (unsoundnessString != null ? unsoundnessString.hashCode() : 0);
        result = 31 * result + (unsoundness != null ? unsoundness.hashCode() : 0);
        result = 31 * result + (flowgraph ? 1 : 0);
        result = 31 * result + (callgraph ? 1 : 0);
        result = 31 * result + (debug ? 1 : 0);
        result = 31 * result + (showVariableInfo ? 1 : 0);
        result = 31 * result + (newflow ? 1 : 0);
        result = 31 * result + (states ? 1 : 0);
        result = 31 * result + (test ? 1 : 0);
        result = 31 * result + (testFlowgraphBuilder ? 1 : 0);
        result = 31 * result + (timing ? 1 : 0);
        result = 31 * result + (statistics ? 1 : 0);
        result = 31 * result + (memoryUsage ? 1 : 0);
        result = 31 * result + (quiet ? 1 : 0);
        result = 31 * result + (includeDom ? 1 : 0);
        result = 31 * result + (propagateDeadFlow ? 1 : 0);
        result = 31 * result + (alwaysCanput ? 1 : 0);
        result = 31 * result + (evalStatistics ? 1 : 0);
        result = 31 * result + (no_messages ? 1 : 0);
        result = 31 * result + (single_event_handler_type ? 1 : 0);
        result = 31 * result + (ignore_html_content ? 1 : 0);
        result = 31 * result + (unevalizer ? 1 : 0);
        result = 31 * result + (no_polymorphic ? 1 : 0);
        result = 31 * result + (ajaxReturnsJson ? 1 : 0);
        result = 31 * result + (ignoredLibrariesString != null ? ignoredLibrariesString.hashCode() : 0);
        result = 31 * result + (ignoredLibraries != null ? ignoredLibraries.hashCode() : 0);
        result = 31 * result + (contextSensitiveHeap ? 1 : 0);
        result = 31 * result + (parameterSensitivity ? 1 : 0);
        result = 31 * result + (ignoreUnreached ? 1 : 0);
        result = 31 * result + loopUnrollings;
        result = 31 * result + (determinacy ? 1 : 0);
        result = 31 * result + (polyfillMDN ? 1 : 0);
        result = 31 * result + (polyfillES6Collections ? 1 : 0);
        result = 31 * result + (polyfillTypedArrays ? 1 : 0);
        result = 31 * result + (polyfillES6Promises ? 1 : 0);
        result = 31 * result + (polyfillES6Iterators ? 1 : 0);
        result = 31 * result + (asyncEvents ? 1 : 0);
        result = 31 * result + (noStringSets ? 1 : 0);
        result = 31 * result + (testSoundness ? 1 : 0);
        result = 31 * result + (generateLog ? 1 : 0);
        result = 31 * result + (logFile != null ? logFile.hashCode() : 0);
        result = 31 * result + (config != null ? config.hashCode() : 0);
        result = 31 * result + (showInternalMessages ? 1 : 0);
        result = 31 * result + (consoleModel ? 1 : 0);
        result = 31 * result + (commonAsyncPolyfill ? 1 : 0);
        result = 31 * result + (noStrict ? 1 : 0);
        result = 31 * result + (deterministicCollections ? 1 : 0);
        result = 31 * result + (specializeAllBoxedPrimitives ? 1 : 0);
        result = 31 * result + analysisTimeLimit;
        result = 31 * result + (doNotExpectOrdinaryExit ? 1 : 0);
        result = 31 * result + (inspector ? 1 : 0);
        result = 31 * result + (babel ? 1 : 0);
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        result = 31 * result + (soundnessTesterOptions != null ? soundnessTesterOptions.hashCode() : 0);
        result = 31 * result + (typeCheckEnabled ? 1 : 0);
        result = 31 * result + (blendedAnalysis ? 1 : 0);
        result = 31 * result + (propNamePartitioning ? 1 : 0);
        result = 31 * result + (typePartitioning ? 1 : 0);
        result = 31 * result + (freeVariablePartitioning? 1 : 0);
        result = 31 * result + (noFiltering ? 1 : 0);
        result = 31 * result + (noStringReplacePolyfill ? 1 : 0);
        result = 31 * result + (noErrorCaptureStackTracePolyfill ? 1 : 0);
        result = 31 * result + (pointer != null ? pointer.hashCode() : 0);
        result = 31 * result + (line != null ? 1 : 0);
        return result;
    }

    public void parse(String[] args) throws CmdLineException {
            CmdLineParser parser = new CmdLineParser(this);
            try {
                parser.parseArgument(args);
                // handle flags that have side-effects, for example imply other flags
                if (ignoredLibrariesString != null && !ignoredLibrariesString.isEmpty()) {
                    ignoredLibraries = newSet(Arrays.asList(ignoredLibrariesString.split(",")));
                }
                if (testSoundness) {
                    soundnessTesterOptions.setTest(true);
                }
                if (generateLog) {
                    soundnessTesterOptions.setGenerate(true);
                }
                if (logFile != null) {
                    soundnessTesterOptions.setExplicitSoundnessLogFile(Optional.of(Paths.get(logFile)));
                }
                if (determinacy) {
                    enableDeterminacy();
                }
                if (test) {
                    enableTest();
                }
                if (testFlowgraphBuilder) {
                    enableTestFlowGraphBuiler();
                }
                if (debug || states) {
                    enableDebug();
                }
                if (unsoundnessString != null) {
                    unsoundness = new UnsoundnessOptionValues(unsoundness, unsoundnessString.split(","));
                }
            } catch (CmdLineException e) {
                throw new CmdLineException(null, "Bad arguments: " + e.getMessage(), null);
        }
    }

    @Override
    public OptionValues clone() {
        OptionValues options = new OptionValues();
        OptionsUtil.cloneAllFields(this, options);
        return options;
    }

    public Map<String, Object> getOptionValues() {
        try {
            Map<String, Object> options = new TreeMap<>();
            for (Field f : OptionValues.class.getDeclaredFields()) {
                f.setAccessible(true);
                Option annotation = f.getAnnotation(Option.class);
                if (annotation != null) {
                    Object value = f.get(this);
                    String name = annotation.name();
                    if (value != null && ((value instanceof Boolean && (Boolean) value) ||
                            (value instanceof Integer && ((Integer) value) != -1) ||
                            (value instanceof String && !"".equals(value) && !"-1".equals(value)) ||
                            (value instanceof Collection && !((Collection<?>) value).isEmpty()))) {
                        options.put(name, value);
                    }
                }
            }
            return options;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new AnalysisException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Entry<String, Object> me : getOptionValues().entrySet()) {
            if (!first) {
                sb.append(" ");
            } else {
                first = false;
            }
            sb.append(me.getKey());
            if (!(me.getValue() instanceof Boolean)) {
                sb.append(" ").append(me.getValue());
            }
        }
        String u = unsoundness.toString();
        if (!u.isEmpty()) {
            if (!first) {
                sb.append(" ");
            } else {
                first = false;
            }
            sb.append("-unsound ");
            sb.append(u);
        }
        for (Path argument : arguments) {
            if (!first) {
                sb.append(" ");
            } else {
                first = false;
            }
            sb.append(argument);
        }
        return sb.toString();
    }

    public void disableAjaxReturnsJson() {
        ajaxReturnsJson = false;
    }

    public void disableAlwaysCanPut() {
        alwaysCanput = false;
    }

    public void disableCallgraph() {
        callgraph = false;
    }

    public void disableShowVariableInfo() {
        showVariableInfo = false;
    }

    public void disableContextSensitiveHeap() {
        contextSensitiveHeap = false;
    }

    public void disableContextSpecialization() {
        contextSpecialization = false;
    }

    public void disableControlSensitivity() {
        noControlSensitivity = true;
    }

    public void disableDebug() {
        debug = false;
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getRootLogger().getAppender("tajs").setLayout(new PatternLayout("%m%n"));
    }

    public void disableFlowgraph() {
        flowgraph = false;
    }

//	public void disableFlowgraphOptimization() {
//		flowgraphOptimization = false;
//	}

    public void disableNoForInSpecialization() {
        noForInSpecialization = false;
    }

    public void disableNoUserEvents() {
        noUserEvents = false;
    }

    public void disableIgnoreHTMLContent() {
        ignore_html_content = false;
    }

    public void disableIgnoreLibraries() {
        ignoredLibrariesString = "";
        ignoredLibraries = newSet();
    }

    public void disableIncludeDom() {
        includeDom = false;
    }

    public void disableLowSeverity() {
        lowSeverity = false;
    }

    public void disableLoopUnrolling() {
        loopUnrollings = -1;
    }

    public void disableMemoryUsage() {
        memoryUsage = false;
    }

    public void disableNewflow() {
        newflow = false;
    }

    public void disableNoChargedCalls() {
        noChargedCalls = false;
    }

    public void disableNoCopyOnWrite() {
        noCopyOnWrite = false;
    }

    public void disableNoGc() {
        noGc = false;
    }

    public void disableNoHybridCollections() {
        noHybridCollections = false;
    }

    public void disableNoLazy() {
        noLazy = false;
    }

    public void disableNoMessages() {
        no_messages = false;
    }

    public void disableNoModified() {
        noModified = false;
    }

    public void disableNoObjectSensitivity() {
        noObjectSensitivity = false;
    }

    public void disableNoPolymorphic() {
        no_polymorphic = false;
    }

    public void disableNoRecency() {
        noRecency = false;
    }

    public void disableDeterminacy() {
        determinacy = false;
    }

    public void disableParameterSensitivity() {
        parameterSensitivity = false;
    }

    public void disablePropagateDeadFlow() {
        propagateDeadFlow = false;
    }

    public void disableQuiet() {
        quiet = false;
    }

    public void disableSingleEventHandlerType() {
        single_event_handler_type = false;
    }

    public void disableStates() {
        states = false;
    }

    public void disableStatistics() {
        statistics = false;
    }

    public void disableTest() {
        test = false;
    }

    public void disableTestFlowGraphBuilder() {
        testFlowgraphBuilder = false;
    }

    public void disableTiming() {
        timing = false;
    }

    public void disableUnevalizer() {
        unevalizer = false;
    }

    public void disableIgnoreUnreached() {
        ignoreUnreached = false;
    }

    public void enableAjaxReturnsJson() {
        ajaxReturnsJson = true;
    }

    public void enableAlwaysCanPut() {
        alwaysCanput = true;
    }

    public void enableCallgraph() {
        callgraph = true;
    }

    public void enableShowVariableInfo() {
        showVariableInfo = true;
    }

//	public void enableErrorBatchMode() {
//		introduceError = true;
//	}

    public void enableContextSensitiveHeap() {
        contextSensitiveHeap = true;
    }

    public void enableContextSpecialization() {
        contextSpecialization = true;
    }

//	public void enableFlowgraphOptimization() {
//		flowgraphOptimization = true;
//	}

    public void enableControlSensitivity() {
        noControlSensitivity = false;
    }

    public void enableDebug() {
        debug = true;
        Logger.getRootLogger().setLevel(Level.DEBUG);
        Appender a = Logger.getRootLogger().getAppender("tajs");
        if (a != null)
            a.setLayout(new PatternLayout("[%p %C{1}] %m%n"));
    }

    public void enableEvalStatistics() {
        evalStatistics = true;
    }

    public void enableFlowgraph() {
        flowgraph = true;
    }

    public void enableNoForInSpecialization() {
        noForInSpecialization = true;
    }

    public void enableNoUserEvents() {
        noUserEvents = true;
    }

    public void enableIgnoreHTMLContent() {
        ignore_html_content = true;
    }

    public void enableIncludeDom() {
        includeDom = true;
    }

    public void enableLowSeverity() {
        lowSeverity = true;
    }

    public void enableLoopUnrolling(int loopUnrollings) {
        this.loopUnrollings = loopUnrollings;
    }

    public void enableMemoryUsage() {
        memoryUsage = true;
    }

    public void enableNewflow() {
        newflow = true;
    }

    public void enableNoChargedCalls() {
        noChargedCalls = true;
    }

    public void enableNoCopyOnWrite() {
        noCopyOnWrite = true;
    }

    public void enableNoGc() {
        noGc = true;
    }

    public void enableNoHybridCollections() {
        noHybridCollections = true;
    }

    public void enableNoLazy() {
        noLazy = true;
    }

    public void enableNoMessages() {
        no_messages = true;
    }

    public void enableNoModified() {
        noModified = true;
    }

    public void enableNoObjectSensitivity() {
        noObjectSensitivity = true;
    }

    public void enableNoPolymorphic() {
        no_polymorphic = true;
    }

    public void enableNoRecency() {
        noRecency = true;
    }

    public void enableDeterminacy() {
        determinacy = true;
        enableLoopUnrolling(50);
    }

    public void enableParameterSensitivity() {
        parameterSensitivity = true;
    }

    public void enablePropagateDeadFlow() {
        propagateDeadFlow = true;
    }

    public void enableQuiet() {
        quiet = true;
    }

    public void enableSingleEventHandlerType() {
        single_event_handler_type = true;
    }

    public void enableStates() {
        states = true;
    }

    public void enableStatistics() {
        statistics = true;
    }

    public void enableTest() {
        test = true;
        quiet = true;
        lowSeverity = true;
        deterministicCollections = true;
        enableTestSoundness();
    }

    public void enableTestFlowGraphBuiler() {
        enableTest();
        testFlowgraphBuilder = true;
    }

    public void enableTestSoundness() {
        testSoundness = true;
        soundnessTesterOptions.setTest(true);
        soundnessTesterOptions.setGenerate(true);
    }

    public void disableTestSoundness() {
        testSoundness = false;
        soundnessTesterOptions.setTest(false);
        soundnessTesterOptions.setGenerate(false);
    }

    public void enableTiming() {
        timing = true;
    }

    public void enableUnevalizer() {
        unevalizer = true;
    }

    public void enableIgnoreUnreached() {
        ignoreUnreached = true;
    }

    public List<Path> getArguments() {
        return arguments;
    }

    public Set<String> getLibraries() {
        return ignoredLibraries;
    }

    public int getLoopUnrollings() {
        return loopUnrollings;
    }

    public boolean isAlwaysCanPut() {
        return alwaysCanput;
    }

    public boolean isCallGraphEnabled() {
        return callgraph;
    }

//	public boolean isErrorBatchMode() {
//		return introduceError;
//	}

    public boolean isChargedCallsDisabled() {

        return noChargedCalls;
    }

    public boolean isShowVariableInfoEnabled() {
        return showVariableInfo;
    }

    public boolean isContextSensitiveHeapEnabled() {
        return contextSensitiveHeap;
    }

//	public boolean isFlowGraphOptimizationEnabled() {
//		return flowgraphOptimization;
//	}

    public boolean isContextSpecializationEnabled() {
        return contextSpecialization;
    }

    public boolean isControlSensitivityDisabled() {
        return noControlSensitivity;
    }

    public boolean isCopyOnWriteDisabled() {
        return noCopyOnWrite;
    }

    public boolean isDebugEnabled() {
        return debug;
    }

    public boolean isDebugOrTestEnabled() {
        return debug || test;
    }

    public boolean isDOMEnabled() {
        return includeDom;
    }

    public boolean isEvalStatistics() {
        return evalStatistics;
    }

    public boolean isFlowGraphEnabled() {
        return flowgraph;
    }

    public boolean isForInSpecializationDisabled() {
        return noForInSpecialization;
    }

    public boolean isGCDisabled() {
        return noGc;
    }

    public boolean isHybridCollectionsDisabled() {
        return noHybridCollections;
    }

    public boolean isIgnoreHTMLContent() {
        return ignore_html_content;
    }

    public boolean isIgnoreLibrariesEnabled() {
        return !ignoredLibraries.isEmpty();
    } // TODO: unused?

    public boolean isIntermediateStatesEnabled() {
        return states;
    }

    public boolean isLazyDisabled() {
        return noLazy;
    }

    public boolean isLowSeverityEnabled() {
        return lowSeverity;
    }

    public boolean isMemoryMeasurementEnabled() {
        return memoryUsage;
    }

    public boolean isModifiedDisabled() {
        return noModified;
    }

    public boolean isNewFlowEnabled() {
        return newflow;
    }

    public boolean isNoMessages() {
        return no_messages;
    }

    public boolean isObjectSensitivityDisabled() {
        return noObjectSensitivity;
    }

    public boolean isDeterminacyEnabled() {
        return determinacy;
    }

    public boolean isParameterSensitivityEnabled() {
        return parameterSensitivity;
    }

    public boolean isPolymorphicDisabled() {
        return no_polymorphic;
    }

    public boolean isPropagateDeadFlow() {
        return propagateDeadFlow;
    }

    public boolean isQuietEnabled() {
        return quiet;
    }

    public boolean isRecencyDisabled() {
        return noRecency;
    }

    public boolean isReturnJSON() {
        return ajaxReturnsJson;
    }

    public boolean isSingleEventHandlerType() {
        return single_event_handler_type;
    }

    public boolean isStatisticsEnabled() {
        return statistics;
    }

    public boolean isTestEnabled() {
        return test;
    }

    public boolean isTestFlowGraphBuilderEnabled() {
        return testFlowgraphBuilder;
    }

    public boolean isTimingEnabled() {
        return timing;
    }

    public boolean isUnevalizerEnabled() { // TODO: (#9) enable unevalizer by default? (affects use of live variables!)
        return unevalizer;
    }

    public boolean isIgnoreUnreachedEnabled() {
        return ignoreUnreached;
    }

    public boolean isConcreteNativeDisabled() {
        return noConcreteNative;
    }

    public void checkConsistency() throws CmdLineException {
        if (arguments == null || arguments.isEmpty()) {
            throw new CmdLineException(null, "No arguments provided!", null);
        }
        if (generateLog) {
            if (logFile == null) {
                throw new CmdLineException(null, "-generate-log requires -log-file", null);
            }
            if (!testSoundness && !blendedAnalysis) {
                throw new CmdLineException(null, "-generate-log requires -test-soundness or -blended-analysis", null); // TODO: could support -generate-log without -test-soundness (and then without running the static analysis!)
            }
        }
        if (blendedAnalysis && unsoundness.isUseFixedRandom()) {
            throw new CmdLineException(null, "-blended-analysis and -unsound -use-fixed-random are not allowed together", null);
        }
    }

    public void enablePolyfillMDN() {
        polyfillMDN = true;
    }

    public void disablePolyfillMDN() {
        polyfillMDN = false;
    }

    public void enablePolyfillES6Collections() {
        polyfillES6Collections = true;
    }

    public void enablePolyfillTypedArrays() {
        polyfillTypedArrays = true;
    }

    public void enablePolyfillES6Promises() {
        polyfillES6Promises = true;
    }

    public void enablePolyfillES6Iterators() {
        polyfillES6Iterators = true;
    }

    public boolean isPolyfillMDNEnabled() {
        return polyfillMDN;
    }

    public boolean isPolyfillES6CollectionsEnabled() {
        return polyfillES6Collections;
    }

    public boolean isPolyfillTypedArraysEnabled() {
        return polyfillTypedArrays;
    }

    public boolean isPolyfillES6PromisesEnabled() {
        return polyfillES6Promises;
    }

    public boolean isPolyfillES6IteratorsEnabled() {
        return polyfillES6Iterators;
    }

    public void enableAsyncEvents() {
        asyncEvents = true;
    }

    public boolean isAsyncEventsEnabled() {
        return asyncEvents;
    }

    public String getConfig() {
        return config;
    }

    public UnsoundnessOptionValues getUnsoundness() {
        return unsoundness;
    }

    public void setUnsoundness(UnsoundnessOptionValues unsoundness) {
        this.unsoundness = unsoundness;
    }

    public boolean isNoStringSets() {
        return noStringSets;
    }

    public void enableNoStringSets() {
        noStringSets = true;
    }

    public SoundnessTesterOptions getSoundnessTesterOptions() {
        return soundnessTesterOptions;
    }

    public void enableShowInternalMessages() {
        showInternalMessages = true;
    }

    public boolean isShowInternalMessagesEnabled() {
        return showInternalMessages;
    }

    public boolean isConsoleModelEnabled() {
        return consoleModel;
    }

    public void enableConsoleModel() {
        consoleModel = true;
    }

    public boolean isCommonAsyncPolyfillEnabled() {
        return commonAsyncPolyfill;
    }

    public void enableCommonAsyncPolyfill() {
        commonAsyncPolyfill = true;
    }

    public boolean isNoStrictEnabled() {
        return noStrict;
    }

    public void enableNoStrict() {
        noStrict = true;
    }

    public boolean isUserEventsDisabled() {
        return noUserEvents;
    }

    public void enableDeterministicCollections() {
        deterministicCollections = true;
    }

    public boolean isDeterministicCollectionsEnabled() {
        return deterministicCollections;
    }

    public void enableSpecializeAllBoxedPrimitives() {
        specializeAllBoxedPrimitives = true;
    }

    public boolean isSpecializeAllBoxedPrimitivesEnabled() {
        return specializeAllBoxedPrimitives;
    }

    public void setAnalysisTimeLimit(int seconds) {
        analysisTimeLimit = seconds;
    }

    public int getAnalysisTimeLimit() {
        return analysisTimeLimit;
    }

    public void setAnalysisTransferLimit(int transfers) {
        analysisTransferLimit = transfers;
    }

    public int getAnalysisTransferLimit() {
        return analysisTransferLimit;
    }

    public boolean isDoNotExpectOrdinaryExitEnabled() {
        return doNotExpectOrdinaryExit;
    }

    public void enableDoNotExpectOrdinaryExit() {
        doNotExpectOrdinaryExit = true;
    }

    public void disableDoNotExpectOrdinaryExit() {
        doNotExpectOrdinaryExit = false;
    }

    public boolean isInspectorEnabled() {
        return inspector;
    }

    public void enableInspector() {
        inspector = true;
    }

    public boolean isAnalysisLimitationWarnOnly() {
        return analysisLimitationWarnOnly;
    }

    public void enableAnalysisLimitationWarnOnly() {
        analysisLimitationWarnOnly = true;
    }

    public void disableAanalysisLimitationWarnOnly() {
        analysisLimitationWarnOnly = false;
    }

    public boolean isNodeJS() {
        return nodejs;
    }

    public void enableNodeJS() {
        nodejs = true;
    }

    public void disableNodeJS() {
        nodejs = false;
    }

    public boolean isTypeFilteringEnabled() {
        return typeFiltering;
    }

    public void enableTypeFiltering() {
        typeFiltering = true;
    }

    public void disableTypeFiltering() {
        typeFiltering = false;
    }

    public boolean isBabelEnabled() {
        return babel;
    }

    public void enableBabel() {
        babel = true;
    }

    public void disableBabel() {
        babel = false;
    }

    public boolean isTypeCheckEnabled() {
        return typeCheckEnabled;
    }

    public void enableTypeCheck() {
        typeCheckEnabled = true;
    }

    public boolean isBlendedAnalysisEnabled() {
        return blendedAnalysis;
    }

    public void enableBlendedAnalysis() {
        blendedAnalysis = true;
    }

    public void disableBlendedAnalysis() {
        blendedAnalysis = false;
    }

    public boolean isNoFilteringEnabled() {
        return noFiltering;
    }

    public void enableNoFiltering() {
        noFiltering = true;
    }

    public void diableNoFilteringer() {
        noFiltering = false;
    }

    public boolean isPropNamePartitioning() {
        return propNamePartitioning;
    }

    public void enablePropNamePartitioning() {
        propNamePartitioning = true;
    }

    public void disablePropNamePartitioning() {
        propNamePartitioning = false;
    }

    public boolean isTypePartitioningEnabled() {
        return typePartitioning;
    }

    public void enableTypePartitioning() {
        typePartitioning = true;
    }

    public void disableTypePartitioning() {
        typePartitioning = false;
    }

    public boolean isFreeVariablePartitioning() {
        return freeVariablePartitioning;
    }

    public void enableFreeVariablePartitioning() {
        freeVariablePartitioning = true;
    }

    public void disableFreeVariablePartitioning() {
        freeVariablePartitioning = false;
    }

    public void enableNoStringReplacePolyfill() {
        noStringReplacePolyfill = true;
    }

    public void disableNoStringReplacePolyfill() {
        noStringReplacePolyfill = false;
    }

    public boolean isNoStringReplacePolyfillEnabled() {
        return noStringReplacePolyfill;
    }

    public void enableNoErrorCaptureStackTracePolyfill() {
        noErrorCaptureStackTracePolyfill = true;
    }

    public boolean isNoErrorCaptureStackTracePolyfillEnabled() {
        return noErrorCaptureStackTracePolyfill;
    }

    public String getPointerVariable(){
        return pointer;
    }

    public Integer getPointerLine(){
        return line;
    }
}
