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

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.TAJSFunctionName;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.util.AnalysisResultException;
import dk.brics.tajs.util.Collectors;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static dk.brics.tajs.flowgraph.TAJSFunctionName.TAJS_ASSERT;
import static dk.brics.tajs.flowgraph.TAJSFunctionName.TAJS_ASSERT_EQUALS;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Monitor for checking TAJS_*-assertions are reachable at the end of the scan phase..
 * <p/>
 * To be used for testing: asserts that syntactic TAJS_assert and TAJS_assertEqual calls are invoked.
 * <p>
 * Rationale: TAJS_asserts that are unreachable are probably unintended!
 * <p>
 * Special case: TAJS_assert(false) is not supposed to be reachable.
 */
public class TAJSAssertionReachabilityCheckerMonitor extends DefaultAnalysisMonitoring {

    private FlowGraph fg;

    private Set<CallNode> assertionCallNodes = newSet();

    private Set<CallNode> reachableAssertionCallNodes = newSet();

    private final Supplier<Boolean> analysisReachedFixedPoint;

    public TAJSAssertionReachabilityCheckerMonitor(Supplier<Boolean> analysisReachedFixedPoint) {
        this.analysisReachedFixedPoint = analysisReachedFixedPoint;
    }

    private static Set<CallNode> getAssertionCallNodes(FlowGraph flowGraph) {
        Set<TAJSFunctionName> assertionFunctions = newSet(Arrays.asList(TAJS_ASSERT, TAJS_ASSERT_EQUALS));
        return flowGraph.getFunctions().stream()
                .flatMap(f -> f.getBlocks().stream())
                .flatMap(b -> b.getNodes().stream())
                .filter(n -> n instanceof CallNode)
                .map(cn -> (CallNode) cn)
                .filter(cn -> assertionFunctions.contains(cn.getTajsFunctionName()))
                // ignore trivial TAJS_assert(false), which is used to assert dead code
                // ignore TAJS_assert(x, y, z, false), which is used to allow the assertion to be unreachable anyway
                .filter(cn -> !(cn.getTajsFunctionName() == TAJS_ASSERT && flowGraph.getSyntacticInformation().getTajsCallsWithLiteralFalseAsFirstOrFourthArgument().contains(cn)))
                .collect(Collectors.toSet());
    }

    @Override
    public void visitNodeTransferPre(AbstractNode n, State s) {
        if (assertionCallNodes.contains(n) && !s.isBottom()) { // FIXME: suspicious call to Set.contains (github #503)
            reachableAssertionCallNodes.add((CallNode) n); // safe cast due to set containment
        }
    }

    @Override
    public void setSolverInterface(Solver.SolverInterface c) {
        this.fg = c.getFlowGraph();
    }

    @Override
    public void visitPhasePre(AnalysisPhase phase) {
        if (phase == AnalysisPhase.SCAN) {
            assertionCallNodes.addAll(getAssertionCallNodes(fg));
        }
    }

    @Override
    public void visitPhasePost(AnalysisPhase phase) {
        if (phase == AnalysisPhase.SCAN && analysisReachedFixedPoint.get()) {
            Set<CallNode> unreachable = newSet(assertionCallNodes);
            unreachable.removeAll(reachableAssertionCallNodes);
            if (!unreachable.isEmpty()) {
                List<String> sourceLocationStrings = unreachable.stream().map(AbstractNode::getSourceLocation).sorted(new SourceLocation.Comparator()).map(SourceLocation::toString).collect(Collectors.toList());
                throw new AnalysisResultException("Some TAJS assertions were not invoked: " + String.join(", ", sourceLocationStrings));
            }
        }
    }
}
