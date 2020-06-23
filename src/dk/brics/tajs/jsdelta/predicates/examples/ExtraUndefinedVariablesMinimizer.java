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

package dk.brics.tajs.jsdelta.predicates.examples;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.jsdelta.predicates.AbstractLocationMinimizer;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class ExtraUndefinedVariablesMinimizer extends AbstractLocationMinimizer {

    private static Path fileToAnalyze = Paths.get("/home/esbena/tmp/extra-undefined.js");

    public static void main(String[] args) {
        Main.initLogging();
        AbstractLocationMinimizer.reduce(fileToAnalyze);
    }

    @Override
    protected boolean test(Set<SourceLocation> sourceLocations) {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
        Options.get().getSoundnessTesterOptions().setTest(false);
        Options.get().getArguments().add(fileToAnalyze);
        IAnalysisMonitoring addUndefinednessToSelectedVariables = new DefaultAnalysisMonitoring() {
            @Override
            public void visitNodeTransferPre(AbstractNode n, State s) {
                if (n instanceof ReadVariableNode && sourceLocations.contains(n.getSourceLocation())) {
                    String variableName = ((ReadVariableNode) n).getVariableName();
                    Analysis analysis = (Analysis) s.getSolverInterface().getAnalysis();
                    PropVarOperations pv = analysis.getPropVarOperations();
                    Value valueWithUndef = pv.readVariable(variableName, null, true, false).joinUndef();
                    pv.writeVariable(variableName, valueWithUndef, true);
                }
            }
        };
        Analysis a = Main.init(Options.get(), addUndefinednessToSelectedVariables, null);
        try {
            Main.run(a);
        } catch (AnalysisLimitationException e) {
            return true;
        }
        return false;
    }
}
