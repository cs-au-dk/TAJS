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

package dk.brics.tajs.monitoring;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.util.AnalysisResultException;

import java.util.function.Supplier;

/**
 * Monitor that checks whether dataflow appears at the ordinary/exceptional program exit at the end of the scan phase.
 */
public class ProgramExitReachabilityChecker extends DefaultAnalysisMonitoring {

    private final boolean makeAssertionErrorInScanPhase;

    private final boolean requireOrdinaryExit;

    private final boolean allowOrdinaryExit;

    private final boolean requireExceptionalExit;

    private final boolean allowExceptionalExit;

    private final Supplier<Boolean> analysisReachedFixedPoint;

    private boolean seenOrdinaryExit = false;

    private boolean seenExceptionalExit = false;

    /**
     * @param makeAssertionErrorInScanPhase if set, throw {@link AssertionError} if flow to the program exit does not satisfy the other paramaters.
     */
    public ProgramExitReachabilityChecker(boolean makeAssertionErrorInScanPhase, boolean requireOrdinaryExit, boolean allowOrdinaryExit, boolean requireExceptionalExit, boolean allowExceptionalExit, Supplier<Boolean> analysisReachedFixedPoint) {
        this.makeAssertionErrorInScanPhase = makeAssertionErrorInScanPhase;
        this.requireOrdinaryExit = requireOrdinaryExit;
        this.allowOrdinaryExit = allowOrdinaryExit;
        this.requireExceptionalExit = requireExceptionalExit;
        this.allowExceptionalExit = allowExceptionalExit;
        this.analysisReachedFixedPoint = analysisReachedFixedPoint;
    }

    @SuppressWarnings("unused" /* used by TAJS-meta */)
    public boolean isSeenOrdinaryExit() {
        return seenOrdinaryExit;
    }

    @Override
    public void visitPhasePost(AnalysisPhase phase) {
        if (makeAssertionErrorInScanPhase && phase == AnalysisPhase.SCAN) {
            if (analysisReachedFixedPoint.get()) {
                if (requireOrdinaryExit && !seenOrdinaryExit) {
                    throw new AnalysisResultException("Did not observe flow to ordinary program exit!");
                }
                if (requireExceptionalExit && !seenExceptionalExit) {
                    throw new AnalysisResultException("Did not observe flow to exceptional program exit!");
                }
            }
            if (!allowOrdinaryExit && seenOrdinaryExit) {
                throw new AnalysisResultException("Observed flow to ordinary program exit!");
            }
            if (!allowExceptionalExit && seenExceptionalExit) {
                throw new AnalysisResultException("Observed flow to exceptional program exit!");
            }
        }
    }

    @Override
    public void visitBlockTransferPost(BasicBlock b, State state) {
        Function function = b.getFunction();
        if (function.isMain()) {
            if (function.getOrdinaryExit() == b) {
                seenOrdinaryExit = true;
            }
            if (function.getExceptionalExit() == b) {
                seenExceptionalExit = true;
            }
        }
    }
}
