/*
 * Copyright 2009-2020 Aarhus University
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

package dk.brics.tajs.monitoring.soundness;

import dk.brics.tajs.analysis.HostAPIs;
import dk.brics.tajs.analysis.KnownUnsoundnesses;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.AnalysisPhase;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.TypeCollector;
import dk.brics.tajs.monitoring.soundness.postprocessing.SoundnessTestResult;
import dk.brics.tajs.monitoring.soundness.testing.SoundnessTester;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.PathAndURLUtils;
import org.apache.log4j.Logger;

import java.net.URL;
import java.nio.file.Path;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Monitor that performs soundness testing.
 * <p>
 * Collects type information using {@link TypeCollector} during scan phase, and invokes {@link SoundnessTester} afterwards.
 */
public class SoundnessTesterMonitor extends DefaultAnalysisMonitoring {

    private static final Logger log = Logger.getLogger(SoundnessTesterMonitor.class);

    /**
     * Collector for values of variables and properties.
     */
    private final TypeCollector type_collector = new TypeCollector();

    /**
     * DOM object allocation sites.
     */
    private final Set<SourceLocation> domObjectAllocationSites = newSet();

    private Solver.SolverInterface c;

    private boolean analysisCompleted = false;

    public SoundnessTesterMonitor() { }

    @Override
    public void setSolverInterface(Solver.SolverInterface c) {
        this.c = c;
    }

    /**
     * Collects value of a variable or property.
     */
    @Override
    public void visitVariableOrProperty(AbstractNode node, String var, SourceLocation loc, Value value, Context context, State state) {
        if (analysisCompleted) {
            type_collector.record(var, loc, UnknownValueResolver.getRealValue(value, state), context);
        }
    }

    @Override
    public void visitNodeTransferPost(AbstractNode n, State s) {
        if (c.isScanning()) {
            if (n instanceof DeclareFunctionNode) {
                DeclareFunctionNode declareFunctionNode = (DeclareFunctionNode) n;
                if (!declareFunctionNode.isExpression()) {
                    String functionName = declareFunctionNode.getFunction().getName();
                    if (functionName != null) {
                        type_collector.record(functionName, n.getSourceLocation(), s.readVariableDirect(functionName), s.getContext());
                    }
                }
            }
        }
    }

    /**
     * Collects DOM object allocation sites.
     */
    @Override
    public void visitNativeFunctionCall(AbstractNode n, HostObject hostobject, boolean num_actuals_unknown, int num_actuals, int min, int max) {
        if (analysisCompleted && hostobject.getAPI() == HostAPIs.DOCUMENT_OBJECT_MODEL) {
            if (hostobject.toString().endsWith(" constructor") || hostobject.toString().endsWith(".constructor")) { // quite hacky, but robust
                domObjectAllocationSites.add(n.getSourceLocation());
            }
        }
    }

    /**
     * Before analysis, make sure log file exists, if necessary by generating it -- if selected in the options.
     */
    @Override
    public void visitPhasePre(AnalysisPhase phase) {
        if (phase == AnalysisPhase.ANALYSIS && Options.get().getSoundnessTesterOptions().generateBeforeAnalysis()) {
            generateLog();
        }
    }

    /**
     * After scan phase, perform the soundness test, and generate the log file if necessary.
     */
    @Override
    public void visitPhasePost(AnalysisPhase phase) {
        if (phase == AnalysisPhase.SCAN && analysisCompleted) {
            URL logFile = generateLog();
            if (logFile == null)
                return;
            SoundnessTestResult result = new SoundnessTester(type_collector.getTypeInformation(), domObjectAllocationSites, c).test(logFile);
            if (result.success) {
                if (!Options.get().isNoMessages()) {
                    log.info(result.message);
                }
            } else {
                if (Options.get().getSoundnessTesterOptions().isPrintErrorsWithoutThrowingException()) {
                    System.err.println(result.message);
                } else {
                    throw new SoundnessException(result.message);
                }
            }
        }
    }

    /**
     * Checks that the log file exists or can be created.
     * @return URL of the log file, or null if not available
     */
    private URL generateLog() {
        LogFileHelper logFileHelper = new LogFileHelper();
        Path main = logFileHelper.getMainFile();
        if (KnownUnsoundnesses.isSyntaxFailureFile(main)) {
            log.info(String.format("Log of soundness facts is not available because of syntax errors in source of %s, skipping soundness checking", PathAndURLUtils.toPortableString(main)));
            return null;
        }
        URL logFile = logFileHelper.createOrGetLogFile(); // may have the side-effect of creating the log file on disk
        if (logFile == null) {
            log.info(String.format("Log of soundness facts is not available for %s, skipping soundness checking", PathAndURLUtils.toPortableString(main)));
            return null;
        }
        return logFile;
    }

    @Override
    public void visitIterationDone(String msg) {
        if (msg == null)
            analysisCompleted = true;
    }

    /**
     * Exception signalling that one or more soundness checks failed.
     */
    public static class SoundnessException extends AnalysisException {

        public SoundnessException(String message) {
            super(message);
        }
    }
}
