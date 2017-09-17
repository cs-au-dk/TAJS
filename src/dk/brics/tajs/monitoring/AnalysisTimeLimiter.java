/*
 * Copyright 2009-2017 Aarhus University
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

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.util.AnalysisLimitationException;

/**
 * A simple monitoring that will prevent the analysis from running more than a set time.
 * <p>
 * Ignores time spent in building flowgraph and post-processing analysis results.
 */
public class AnalysisTimeLimiter extends DefaultAnalysisMonitoring {

    private static long nanoFactor = 1000 * 1000 * 1000;

    private final long secondsTimeLimit;

    private final int nodeTransferLimit;

    private final boolean crash;

    private long maxNanoTime = -1;

    private int nodeTransfers = 0;

    private boolean analysisWasLimited = false;

    /**
     * @param secondsTimeLimit  the number of second the analysis is allowed to run, or -1 if no limit
     * @param nodeTransferLimit the number of node transfers the analysis is allowed to run, or -1 if no limit
     * @param crashImmediately true if an exception should be thrown when the analysis exceed the time limit
     */
    public AnalysisTimeLimiter(int secondsTimeLimit, int nodeTransferLimit, boolean crashImmediately) {
        this.secondsTimeLimit = secondsTimeLimit;
        this.nodeTransferLimit = nodeTransferLimit;
        this.crash = crashImmediately;
    }

    public AnalysisTimeLimiter(int secondsTimeLimit) {
        this(secondsTimeLimit, -1, false);
    }

    @Override
    public boolean allowNextIteration() {
        if (secondsTimeLimit != -1) {
            // NB: it has been observed that a single iteration took minutes, so we might overshoot the time limit by a lot!
            long now = System.nanoTime();
            boolean timeOut = maxNanoTime != -1 && maxNanoTime < now;
            if (timeOut) {
                if (crash) {
                    long overUsed = (now - maxNanoTime);
                    long used = (secondsTimeLimit * nanoFactor) + overUsed;
                    long allowed = secondsTimeLimit * nanoFactor;
                    long milliFactor = 1000;
                    long nanoMilliFactor = nanoFactor / milliFactor;
                    throw new AnalysisLimitationException.AnalysisTimeException(String.format("Analysis exceeded time limit. Used: %dms. Allowed: %dms.", used / nanoMilliFactor, allowed / nanoMilliFactor));
                }
                analysisWasLimited = true;
                return false;
            }
        }
        if (nodeTransferLimit != -1) {
            if (nodeTransfers > nodeTransferLimit) {
                if (crash) {
                    throw new AnalysisLimitationException.AnalysisTimeException("Analysis exceeded node transfer limit " + nodeTransferLimit);
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void visitPhasePre(AnalysisPhase phase) {
        if (phase == AnalysisPhase.ANALYSIS) {
            if (secondsTimeLimit != -1) {
                long now = System.nanoTime();
                long delta = secondsTimeLimit * nanoFactor;
                long future = now + delta;
                maxNanoTime = future;
            }
        }
    }

    @Override
    public void visitNodeTransferPre(AbstractNode n, State s) {
        if (!s.getSolverInterface().isScanning()) {
            nodeTransfers++;
        }
    }

    /**
     * @return true iff the analysis did not spent more time or transfers than allowed
     */
    public boolean analysisNotExceededLimit() {
        return !analysisWasLimited;
    }
}
