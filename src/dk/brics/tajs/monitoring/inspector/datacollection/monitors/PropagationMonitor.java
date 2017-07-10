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

package dk.brics.tajs.monitoring.inspector.datacollection.monitors;

import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.inspector.util.StopWatch;
import dk.brics.tajs.solver.BlockAndContext;

import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newMap;

public class PropagationMonitor extends DefaultAnalysisMonitoring {

    private final Map<BlockAndContext<Context>, Set<BlockAndContext<Context>>> forwardsGraph = newMap();

    private final Map<BlockAndContext<Context>, Set<BlockAndContext<Context>>> backwardsGraph = newMap();

    private final Map<BlockAndContext<Context>, PropagationNumbers> numbers = newMap();

    @Override
    public void visitPropagationPre(BlockAndContext<Context> from, BlockAndContext<Context> to) {
        if (!numbers.containsKey(from)) {
            numbers.put(from, new PropagationNumbers());
        }
        if (!numbers.containsKey(to)) {
            numbers.put(to, new PropagationNumbers());
        }

        numbers.get(from).propagatorWatch.startOrContinue();
        numbers.get(to).propagateeWatch.startOrContinue();
        numbers.get(from).changerWatch.startOrContinue();
        numbers.get(to).changeeWatch.startOrContinue();
    }

    @Override
    public void visitPropagationPost(BlockAndContext<Context> from, BlockAndContext<Context> to, boolean changed) {
        numbers.get(from).propagatorWatch.pause();
        numbers.get(to).propagateeWatch.pause();
        if (changed) {
            numbers.get(from).changerWatch.pause();
            numbers.get(to).changeeWatch.pause();
        } else {
            numbers.get(from).changerWatch.discardAndPause();
            numbers.get(to).changeeWatch.discardAndPause();
        }

        addToMapSet(forwardsGraph, from, to);
        addToMapSet(backwardsGraph, to, from);

        numbers.get(from).propagator++;
        numbers.get(to).propagatee++;
        if (changed) {
            numbers.get(from).changer++;
            numbers.get(to).changee++;
        }
    }

    public PropagationData getData() {
        return new PropagationData(forwardsGraph, backwardsGraph, numbers);
    }

    public static class PropagationData {

        private final Map<BlockAndContext<Context>, Set<BlockAndContext<Context>>> forwardsGraph;

        private final Map<BlockAndContext<Context>, Set<BlockAndContext<Context>>> backwardsGraph;

        private final Map<BlockAndContext<Context>, PropagationNumbers> numbers;

        public PropagationData(Map<BlockAndContext<Context>, Set<BlockAndContext<Context>>> forwardsGraph, Map<BlockAndContext<Context>, Set<BlockAndContext<Context>>> backwardsGraph, Map<BlockAndContext<Context>, PropagationNumbers> numbers) {
            this.forwardsGraph = forwardsGraph;
            this.backwardsGraph = backwardsGraph;
            this.numbers = numbers;
        }

        public Map<BlockAndContext<Context>, Set<BlockAndContext<Context>>> getForwardsGraph() {
            return forwardsGraph;
        }

        public Map<BlockAndContext<Context>, Set<BlockAndContext<Context>>> getBackwardsGraph() {
            return backwardsGraph;
        }

        public Map<BlockAndContext<Context>, PropagationNumbers> getNumbers() {
            return numbers;
        }
    }

    public static class PropagationNumbers {

        private final StopWatch propagatorWatch;

        private final StopWatch propagateeWatch;

        private final StopWatch changerWatch;

        private final StopWatch changeeWatch;

        private int propagator;

        private int propagatee;

        private int changer;

        private int changee;

        public PropagationNumbers() {
            this.propagator = 0;
            this.propagatee = 0;
            this.changer = 0;
            this.changee = 0;
            this.propagatorWatch = new StopWatch();
            this.propagateeWatch = new StopWatch();
            this.changerWatch = new StopWatch();
            this.changeeWatch = new StopWatch();
        }

        public StopWatch getPropagatorWatch() {
            return propagatorWatch;
        }

        public StopWatch getPropagateeWatch() {
            return propagateeWatch;
        }

        public StopWatch getChangerWatch() {
            return changerWatch;
        }

        public StopWatch getChangeeWatch() {
            return changeeWatch;
        }

        public int getPropagator() {
            return propagator;
        }

        public int getPropagatee() {
            return propagatee;
        }

        public int getChanger() {
            return changer;
        }

        public int getChangee() {
            return changee;
        }
    }
}
