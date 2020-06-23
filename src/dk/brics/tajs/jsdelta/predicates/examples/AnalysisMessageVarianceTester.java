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
import dk.brics.tajs.jsdelta.DeltaMinimizer;
import dk.brics.tajs.jsdelta.predicates.AbstractAnalysisVariantDifferenceTester;
import dk.brics.tajs.monitoring.AnalysisMonitor;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class AnalysisMessageVarianceTester extends AbstractAnalysisVariantDifferenceTester<Set<Message>> {

    public static void main(String[] args) {
        Main.initLogging();
        DeltaMinimizer.reduce(Paths.get("/home/esbena/tmp/messages.js"));
    }

    @Override
    protected Set<Message> runAnalysisA(Path file) {
        Main.reset();
        Options.get().enableTest();
        return run(file);
    }

    @Override
    protected Set<Message> runAnalysisB(Path file) {
        Main.reset();
        return run(file);
    }

    private Set<Message> run(Path file) {
        Main.initLogging();
        Options.get().enableUnevalizer();
        Options.get().enableIncludeDom();
        Options.get().getArguments().add(file);
        AnalysisMonitor monitoring = new AnalysisMonitor();
        Analysis a = Main.init(Options.get(), monitoring, null);
        Main.run(a);
        return monitoring.getMessages();
    }
}