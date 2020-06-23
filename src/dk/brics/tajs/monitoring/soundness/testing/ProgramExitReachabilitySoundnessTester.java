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

package dk.brics.tajs.monitoring.soundness.testing;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.options.Options;

import java.util.Map;
import java.util.Set;

/**
 * Simple soundness testing: checks that the static analysis and concrete execution agree on how the program terminates.
 */
public class ProgramExitReachabilitySoundnessTester {

    private final Solver.SolverInterface c;

    private final Set<SoundnessCheck> checks;

    public ProgramExitReachabilitySoundnessTester(Set<SoundnessCheck> checks, Solver.SolverInterface c) {
        this.c = c;
        this.checks = checks;
    }

    public boolean test(String runResult) {
        boolean reachabilityFailure = false;
        switch (runResult) {
            case "success":
                if (Options.get().isDOMEnabled()) {
                    // browser environment always succeeds
                    break;
                }
                BasicBlock ordinaryExit = c.getFlowGraph().getMain().getOrdinaryExit();
                reachabilityFailure = testReachable(ordinaryExit, "Ordinary program exit", checks, c);
                break;
            case "failure":
                BasicBlock exceptionalExit = c.getFlowGraph().getMain().getExceptionalExit();
                reachabilityFailure = testReachable(exceptionalExit, "Exceptional program exit", checks, c);
                break;
            case "timeout":
                break;
            case "syntax error":
            case "instrumentation-timeout":
                throw new RuntimeException("Log file indicates at the instrumentation was unsuccessful. Soundness testing is not possible! (disable it or fix the instrumentation)");
            default:
                throw new UnsupportedOperationException("Unhandled result kind: " + runResult);
        }
        return reachabilityFailure;
    }

    private boolean testReachable(BasicBlock block, String description, Set<SoundnessCheck> checks, Solver.SolverInterface c) {
        Map<Context, State> states = c.getAnalysisLatticeElement().getStates(block);
        boolean unreachable = states.values().stream().allMatch(State::isBottom);
        checks.add(new ReachabilityCheck(block.getSourceLocation(), description, unreachable));
        return unreachable;
    }

    private class ReachabilityCheck extends SoundnessCheckImpl {

        public ReachabilityCheck(SourceLocation sourceLocation, String description, boolean failure) {
            super(sourceLocation, String.format("%s should be reachable", description), failure);
        }

        @Override
        public boolean hasDataFlow() {
            return !isFailure();
        }

        @Override
        public FailureKind getFailureKind() {
            return FailureKind.UNREACHABLE;
        }
    }
}
