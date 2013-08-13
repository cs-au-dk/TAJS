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

package dk.brics.tajs.options;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import dk.brics.tajs.util.Pair;

/**
 * Global analysis options.
 * 
 * Implemented as a singleton without the static get() method.
 */
public class Options { // TODO: better management of options (i.e. not static fields)?

	private static final Logger logger = Logger.getLogger(Options.class);

	private static OptionValues optionValues = new OptionValues();

	public static void describe() {
		optionValues.describe();
	}

	public static void disableAjaxReturnsJson() {
		optionValues.disableAjaxReturnsJson();
	}

	public static void disableAlwaysCanPut() {
		optionValues.disableAlwaysCanPut();
	}
	
	public static void disableBoundedIterations() {
		optionValues.disableBoundedIterations();
	}

	public static void disableCallgraph() {
		optionValues.disableCallgraph();
	}

	public static void disableCollectVariableInfo() {
		optionValues.disableCollectVariableInfo();
	}

	public static void disableContextSpecialization() {
		optionValues.disableContextSpecialization();
	}

	public static void disableCorrelationTracking() {
		optionValues.disableCorrelationTracking();
	}

	public static void disableDebug() {
		optionValues.disableDebug();
	}

	public static void disableDebugOutputOnCrash() {
		optionValues.disableDebugOutputOnCrash();
	}

	public static void disableDSL() {
		optionValues.disableDSL();
	}

	public static void disableFlowgraph() {
		optionValues.disableFlowgraph();
	}

	public static void disableFlowgraphOptimization() {
		optionValues.disableFlowgraphOptimization();
	}

	public static void disableIgnoreHTMLContent() {
		optionValues.disableIgnoreHTMLContent();
	}

	public static void disableIgnoreLibraries() {
		optionValues.disableIgnoreLibraries();
	}

	public static void disableIncludeDom() {
		optionValues.disableIncludeDom();
	}

	public static void disableLowSeverity() {
		optionValues.disableLowSeverity();
	}

	public static void disableMemoryUsage() {
		optionValues.disableMemoryUsage();
	}

	public static void disableNewflow() {
		optionValues.disableNewflow();
	}

	public static void disableNoChargedCalls() {
		optionValues.disableNoChargedCalls();
	}

	public static void disableNoContextSensitivity() {
		optionValues.disableNoContextSensitivity();
	}

	public static void disableNoCopyOnWrite() {
		optionValues.disableNoCopyOnWrite();
	}

	public static void disableNoExceptions() {
		optionValues.disableNoExceptions();
	}

	public static void disableNoGc() {
		optionValues.disableNoGc();
	}

	public static void disableNoHybridCollections() {
		optionValues.disableNoHybridCollections();
	}

	public static void disableNoLazy() {
		optionValues.disableNoLazy();
	}

	public static void disableNoLocalPathSensitivity() {
		optionValues.disableNoLocalPathSensitivity();
	}

	public static void disableNoMessages() {
		optionValues.disableNoMessages();
	}

	public static void disableNoModified() {
		optionValues.disableNoModified();
	}

	public static void disableNoRecency() {
		optionValues.disableNoRecency();
	}

	public static void disablePolymorphic() {
		optionValues.disablePolymorphic();
	}

	public static void disablePropagateDeadFlow() {
		optionValues.disablePropagateDeadFlow();
	}

	public static void disableQuiet() {
		optionValues.disableQuiet();
	}

	public static void disableSerializeFinalState() {
		optionValues.disableSerializeFinalState();
	}

	public static void disableSingleEventHandlerLoop() {
		optionValues.disableSingleEventHandlerLoop();
	}

	public static void disableSingleEventHandlerType() {
		optionValues.disableSingleEventHandlerType();
	}

	public static void disableStates() {
		optionValues.disableStates();
	}

	public static void disableStatistics() {
		optionValues.disableStatistics();
	}

	public static void disableTest() {
		optionValues.disableTest();
	}

	public static void disableTestFlowGraphBuilder() {
		optionValues.disableTestFlowGraphBuilder();
	}

	public static void disableTiming() {
		optionValues.disableTiming();
	}

	public static void disableUnevalMode() {
		optionValues.disableUnevalMode();
	}

	public static void disableUnrollOneAndAHalf() {
		optionValues.disableUnrollOneAndAHalf();
	}

	public static void disableUnsound() {
		optionValues.disableUnsound();
	}

	/**
	 * Prints the settings (if in debug mode).
	 */
	public static void dump() {
		for (Pair<String, ?> optionValue : optionValues.getOptionValues()) {
			logger.debug(String.format("%-30s %20s", optionValue.getFirst(), optionValue.getSecond()));
		}
	}

	public static void enableAjaxReturnsJson() {
		optionValues.enableAjaxReturnsJson();
	}

	public static void enableAlwaysCanPut() {
		optionValues.enableAlwaysCanPut();
	}

	public static void enableBoundedIterations(int bound) {
		optionValues.enableBoundedIterations(bound);
	}
	
	public static void enableCallgraph() {
		optionValues.enableCallgraph();
	}

	public static void enableCollectVariableInfo() {
		optionValues.enableCollectVariableInfo();
	}

	public static void enableContextSpecialization() {
		optionValues.enableContextSpecialization();
	}

	public static void enableCorrelationTracking() {
		optionValues.enableCorrelationTracking();
	}

	public static void enableCoverage() {
		optionValues.enableCoverage();
	}

	public static void enableDebug() {
		optionValues.enableDebug();
	}

	public static void enableDebugOutputOnCrash() {
		optionValues.enableDebugOutputOnCrash();
	}

	public static void enableDSL() {
		optionValues.enableDSL();
	}

	public static void enableErrorBatchMode() {
		optionValues.enableErrorBatchMode();
	}

	public static void enableEvalStatistics() {
		optionValues.enableEvalStatistics();
	}

	public static void enableFlowgraph() {
		optionValues.enableFlowgraph();
	}

	public static void enableFlowgraphOptimization() {
		optionValues.enableFlowgraphOptimization();
	}

	public static void enableIgnoreHTMLContent() {
		optionValues.enableIgnoreHTMLContent();
	}

	public static void enableIgnoreLibraries() {
		optionValues.enableIgnoreLibraries();
	}

	public static void enableIncludeDom() {
		optionValues.enableIncludeDom();
	}

	public static void enableLowSeverity() {
		optionValues.enableLowSeverity();
	}

	public static void enableMemoryUsage() {
		optionValues.enableMemoryUsage();
	}

	public static void enableNewflow() {
		optionValues.enableNewflow();
	}

	public static void enableNoChargedCalls() {
		optionValues.enableNoChargedCalls();
	}

	public static void enableNoContextSensitivity() {
		optionValues.enableNoContextSensitivity();
	}

	public static void enableNoCopyOnWrite() {
		optionValues.enableNoCopyOnWrite();
	}

	public static void enableNoExceptions() {
		optionValues.enableNoExceptions();
	}

	public static void enableNoGc() {
		optionValues.enableNoGc();
	}

	public static void enableNoHybridCollections() {
		optionValues.enableNoHybridCollections();
	}

	public static void enableNoLazy() {
		optionValues.enableNoLazy();
	}

	public static void enableNoLocalPathSensitivity() {
		optionValues.enableNoLocalPathSensitivity();
	}

	public static void enableNoMessages() {
		optionValues.enableNoMessages();
	}

	public static void enableNoModified() {
		optionValues.enableNoModified();
	}

	public static void enableNoRecency() {
		optionValues.enableNoRecency();
	}

	public static void enablePolymorphic() {
		optionValues.enablePolymorphic();
	}

	public static void enablePropagateDeadFlow() {
		optionValues.enablePropagateDeadFlow();
	}

	public static void enableQuiet() {
		optionValues.enableQuiet();
	}

	public static void enableSerializeFinalState() {
		optionValues.enableSerializeFinalState();
	}

	public static void enableSingleEventHandlerLoop() {
		optionValues.enableSingleEventHandlerLoop();
	}

	public static void enableSingleEventHandlerType() {
		optionValues.enableSingleEventHandlerType();
	}

	public static void enableStates() {
		optionValues.enableStates();
	}

	public static void enableStatistics() {
		optionValues.enableStatistics();
	}

	public static void enableTest() {
		optionValues.enableTest();
	}

	public static void enableTestFlowGraphBuiler() {
		optionValues.enableTestFlowGraphBuiler();
	}

	public static void enableTiming() {
		optionValues.enableTiming();
	}

	public static void enableUnevalMode() {
		optionValues.enableUnevalMode();
	}

	public static void enableUnrollOneAndAHalf() {
		optionValues.enableUnrollOneAndAHalf();
	}

	public static void enableUnsound() {
		optionValues.enableUnsound();
	}

	public static List<String> getArguments() {
		return optionValues.getArguments();
	}

	public static int getIterationBound() {
		return optionValues.getIterationBound();
	}

	public static Set<String> getLibraries() {
		return optionValues.getLibraries();
	}

	public static int getMaxSuspiciousnessLevel() {
		return optionValues.getMaxSuspiciousnessLevel();
	}

	public static List<Pair<String, ?>> getEnabledOptionValues() {
		return optionValues.getOptionValues();
	}

	public static boolean isAbortOnSuspiciousValueCreationEnabled() {
		return optionValues.isAbortOnSuspiciousValueCreationEnabled();
	}

	public static boolean isAlwaysCanPut() {
		return optionValues.isAlwaysCanPut();
	}

	public static boolean isBoundedIterationsEnabled() {
		return optionValues.isBoundedIterationsEnabled();
	}
	
	public static boolean isCallGraphEnabled() {
		return optionValues.isCallGraphEnabled();
	}

	public static boolean isChargedCallsDisabled() {
		return optionValues.isChargedCallsDisabled();
	}

	public static boolean isCollectVariableInfoEnabled() {
		return optionValues.isCollectVariableInfoEnabled();
	}

	public static boolean isContextSensitivityDisabled() {
		return optionValues.isContextSensitivityDisabled();
	}

	public static boolean isContextSpecializationEnabled() {
		return optionValues.isContextSpecializationEnabled();
	}

	public static boolean isCopyOnWriteDisabled() {
		return optionValues.isCopyOnWriteDisabled();
	}

	public static boolean isCorrelationTrackingEnabled() {
		return optionValues.isCorrelationTrackingEnabled();
	}

	public static boolean isCoverageEnabled() {
		return optionValues.isCoverageEnabled();
	}

	public static boolean isDebugEnabled() {
		return optionValues.isDebugEnabled();
	}

	public static boolean isDebugOrTestEnabled() {
		return optionValues.isDebugOrTestEnabled();
	}

	public static boolean isDebugOutputOnCrashEnabled() {
		return optionValues.isDebugOutputOnCrashEnabled();
	}

	public static boolean isDOMEnabled() {
		return optionValues.isDOMEnabled();
	}

	public static boolean isDSLEnabled() {
		return optionValues.isDSLEnabled();
	}

	public static boolean isErrorBatchMode() {
		return optionValues.isErrorBatchMode();
	}

	public static boolean isEvalStatistics() {
		return optionValues.isEvalStatistics();
	}

	public static boolean isExceptionsDisabled() {
		return optionValues.isExceptionsDisabled();
	}

	public static boolean isFlowGraphEnabled() {
		return optionValues.isFlowGraphEnabled();
	}

	public static boolean isFlowGraphOptimizationEnabled() {
		return optionValues.isFlowGraphOptimizationEnabled();
	}

	public static boolean isGCDisabled() {
		return optionValues.isGCDisabled();
	}

	public static boolean isHybridCollectionsDisabled() {
		return optionValues.isHybridCollectionsDisabled();
	}

	public static boolean isIgnoreHTMLContent() {
		return optionValues.isIgnoreHTMLContent();
	}

	public static boolean isIgnoreLibrariesEnabled() {
		return optionValues.isIgnoreLibrariesEnabled();
	}

	public static boolean isIntermediateStatesEnabled() {
		return optionValues.isIntermediateStatesEnabled();
	}

	public static boolean isLazyDisabled() {
		return optionValues.isLazyDisabled();
	}

	public static boolean isLocalPathSensitivityDisabled() {
		return optionValues.isLocalPathSensitivityDisabled();
	}

	public static boolean isLowSeverityEnabled() {
		return optionValues.isLowSeverityEnabled();
	}

	public static boolean isMemoryMeasurementEnabled() {
		return optionValues.isMemoryMeasurementEnabled();
	}

	public static boolean isModifiedDisabled() {
		return optionValues.isModifiedDisabled();
	}

	public static boolean isNewFlowEnabled() {
		return optionValues.isNewFlowEnabled();
	}

	public static boolean isNoMessages() {
		return optionValues.isNoMessages();
	}

	public static boolean isPolymorphicEnabled() {
		return optionValues.isPolymorphicEnabled();
	}

	public static boolean isPropagateDeadFlow() {
		return optionValues.isPropagateDeadFlow();
	}

	public static boolean isQuietEnabled() {
		return optionValues.isQuietEnabled();
	}

	public static boolean isRecencyDisabled() {
		return optionValues.isRecencyDisabled();
	}

	public static boolean isReturnJSON() {
		return optionValues.isReturnJSON();
	}

	public static boolean isSerializeFinalStateEnabled() {
		return optionValues.isSerializeFinalStateEnabled();
	}

	public static boolean isSingleEventHandlerLoop() {
		return optionValues.isSingleEventHandlerLoop();
	}

	public static boolean isSingleEventHandlerType() {
		return optionValues.isSingleEventHandlerType();
	}

	public static boolean isStatisticsEnabled() {
		return optionValues.isStatisticsEnabled();
	}

	public static boolean isTestEnabled() {
		return optionValues.isTestEnabled();
	}

	public static boolean isTestFlowGraphBuilderEnabled() {
		return optionValues.isTestFlowGraphBuilderEnabled();
	}

	public static boolean isTimingEnabled() {
		return optionValues.isTimingEnabled();
	}

	public static boolean isUnevalEnabled() {
		return optionValues.isUnevalEnabled();
	}

	public static boolean isUnrollOneAndAHalfEnabled() {
		return optionValues.isUnrollOneAndAHalfEnabled();
	}

	public static boolean isUnsoundEnabled() {
		return optionValues.isUnsoundEnabled();
	}

	/**
	 * Parses command line arguments <emph>in addition to</emph> the already set options.
	 */
	public static void parse(String[] args) {
		optionValues = new OptionValues(optionValues, args);
	}

	/**
	 * Resets all options.
	 */
	public static void reset() {
		optionValues = new OptionValues();
	}

	/**
	 * Sets all the options
	 */
	public static void set(OptionValues optionValues) {
		Options.optionValues = optionValues.clone();
	}

	public static void setMaxSuspiciousnessLevel(int level) {
		optionValues.setMaxSuspiciousnessLevel(level);
	}

	private Options() {
		// do nothing
	}

	public static void enableForInSpecialization() {
		optionValues.enableForInSpecialization();
	}
	
	public static void disableForInSpecialization() {
		optionValues.disableForInSpecialization();
	}
	
	public static boolean isForInSpecializationEnabled(){
		return optionValues.isForInSpecializationEnabled();
	}
}
