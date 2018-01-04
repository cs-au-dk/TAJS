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

package dk.brics.tajs.monitoring;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.util.AnalysisResultException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newList;

/**
 * Monitor that checks reachability of different kinds, throwing an assertion-error on missing reachability at the end of the scan-phase.
 * (NB: only supporting node-reachability at the moment)
 */
public class ReachabilityChecker extends DefaultAnalysisMonitoring {

    private final ReachabilityMonitor reachabilityMonitor;

    private ReachabilityChecker(ReachabilityMonitor reachabilityMonitor) {
        this.reachabilityMonitor = reachabilityMonitor;
    }

    public static IAnalysisMonitoring make() {
        // TODO enable by OptionValue in very thorough tests?
        ReachabilityMonitor reachabilityMonitor = new ReachabilityMonitor();

        ReachabilityChecker checker = new ReachabilityChecker(reachabilityMonitor);
        return new CompositeMonitoring(reachabilityMonitor, checker);
    }

    @Override
    public void visitPhasePost(AnalysisPhase phase) {
        if (phase == AnalysisPhase.SCAN) {
            check();
        }
    }

    private void check() {
        Set<Function> unreachableFunctions = reachabilityMonitor.getUnreachableFunctions();
        Set<Function> reachableFunctions = reachabilityMonitor.getReachableFunctions();
        Set<AbstractNode> unreachableNodes = reachableFunctions.stream().flatMap(f -> reachabilityMonitor.getUndominatedUnreachableNodes(f, false).stream()).collect(Collectors.toSet());

        List<String> lines = newList();
        // TODO support different levels of checking through provided enums?
        if (!unreachableFunctions.isEmpty()) {
            lines.add("\tUnreachable functions:");
            unreachableFunctions.stream().forEach(f -> lines.add(String.format("\t\t%s: %s", f.getSourceLocation(), f)));
        }
        if (!unreachableNodes.isEmpty()) {
            lines.add("\tUnreachable nodes (undominated, in reachable functions):");
            unreachableNodes.stream().forEach(n -> lines.add(String.format("\t\t%s: %s", n.getSourceLocation(), n)));
        }
        if (!lines.isEmpty()) {
            lines.add(0, "Some nodes are not reachable!");
            throw new AnalysisResultException(String.join(String.format("%n"), lines));
        }
    }
}
