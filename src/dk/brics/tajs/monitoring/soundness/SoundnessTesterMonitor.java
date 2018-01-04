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

package dk.brics.tajs.monitoring.soundness;

import dk.brics.tajs.analysis.HostAPIs;
import dk.brics.tajs.analysis.KnownUnsoundnesses;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.AnalysisPhase;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.TypeCollector;
import dk.brics.tajs.monitoring.soundness.logfileutilities.LogFileHelper;
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

    private final TypeCollector type_collector = new TypeCollector();

    private final Set<SourceLocation> domObjectAllocationSites = newSet();

    private Solver.SolverInterface c;

    private boolean scanning = false;

    private SoundnessTesterMonitor() {
        if (!Options.get().getSoundnessTesterOptions().isTest()) {
            throw new AnalysisException(String.format("%s requires the soundness testing to be enabled in Options", c.getClass().getSimpleName()));
        }
    }

    public static IAnalysisMonitoring make() {
        SoundnessTesterMonitor soundnessTesterMonitor = new SoundnessTesterMonitor();
        DOMConstructorCallMonitor domConstructorCallMonitor = new DOMConstructorCallMonitor(soundnessTesterMonitor.domObjectAllocationSites);
        return new CompositeMonitoring(domConstructorCallMonitor, soundnessTesterMonitor);
    }

    @Override
    public void setSolverInterface(Solver.SolverInterface c) {
        this.c = c;
    }

    @Override
    public void visitVariableOrProperty(String var, SourceLocation loc, Value value, Context context, State state) {
        if (scanning) {
            type_collector.record(var, loc, UnknownValueResolver.getRealValue(value, state), context);
        }
    }

    @Override
    public void visitPhasePre(AnalysisPhase phase) {
        if (phase == AnalysisPhase.ANALYSIS && Options.get().getSoundnessTesterOptions().generateBeforeAnalysis()) {
            generateLog();
        } else if (phase == AnalysisPhase.SCAN) {
            scanning = true;
        }
    }

    @Override
    public void visitPhasePost(AnalysisPhase phase) {
        if (phase == AnalysisPhase.SCAN) {
            test();
        }
    }

    private URL generateLog() {
        LogFileHelper logFileHelper = new LogFileHelper(Options.get());
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

    private void test() {
        URL logFile = generateLog();
        if (logFile == null) return;

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

    /**
     * Exception signalling that one or more soundness checks failed.
     */
    public static class SoundnessException extends AnalysisException {

        public SoundnessException(String message) {
            super(message);
        }
    }

    private static class DOMConstructorCallMonitor extends DefaultAnalysisMonitoring {

        private final Set<SourceLocation> domObjectAllocationSites;

        public DOMConstructorCallMonitor(Set<SourceLocation> domObjectAllocationSites) {
            this.domObjectAllocationSites = domObjectAllocationSites;
        }

        @Override
        public void visitNativeFunctionCall(AbstractNode n, HostObject hostobject, boolean num_actuals_unknown, int num_actuals, int min, int max) {
            if (hostobject.getAPI() == HostAPIs.DOCUMENT_OBJECT_MODEL) {
                if (hostobject.toString().endsWith(" constructor") || hostobject.toString().endsWith(".constructor")) { // quite hacky, but robust
                    domObjectAllocationSites.add(n.getSourceLocation());
                }
            }
        }
    }
}
