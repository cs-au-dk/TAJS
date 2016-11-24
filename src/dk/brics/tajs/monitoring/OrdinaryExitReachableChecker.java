/*
 * Copyright 2009-2016 Aarhus University
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
import dk.brics.tajs.lattice.State;

/**
 * Monitor that checks whether dataflow appears at the ordinary (non-exceptional) program exit.
 */
public class OrdinaryExitReachableChecker extends DefaultAnalysisMonitoring {

    private final boolean makeAssertionErrorInScanPhase;

    private boolean seenOrdinaryExit = false;

    public OrdinaryExitReachableChecker() {
        this(true);
    }

    /**
     * @param makeAssertionErrorInScanPhase if set, throw {@link AssertionError} if no flow to ordinary program exit
     */
    public OrdinaryExitReachableChecker(boolean makeAssertionErrorInScanPhase) {
        this.makeAssertionErrorInScanPhase = makeAssertionErrorInScanPhase;
    }

    @SuppressWarnings("unused" /* used by TAJS-meta */)
    public boolean isSeenOrdinaryExit() {
        return seenOrdinaryExit;
    }

    @Override
    public void endPhase(AnalysisPhase phase) {
        if (makeAssertionErrorInScanPhase && phase == AnalysisPhase.SCAN && !seenOrdinaryExit) {
            throw new AssertionError("Did not observe flow to ordinary program exit!");
        }
    }

    @Override
    public void visitPostBlockTransfer(BasicBlock b, State state) {
        if (b.getFunction().isMain() && b.getFunction().getOrdinaryExit() == b) {
            seenOrdinaryExit = true;
        }
    }
}
