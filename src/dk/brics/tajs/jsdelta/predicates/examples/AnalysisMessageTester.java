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
import dk.brics.tajs.jsdelta.RunPredicate;
import dk.brics.tajs.monitoring.AnalysisMonitor;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.Message;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class AnalysisMessageTester implements RunPredicate {

    public static void main(String[] args) {
        Main.initLogging();
        DeltaMinimizer.reduce(Paths.get("test-resources/src/v8tests/array-splice.js"));
    }

    @Override
    public boolean test(Path file) {
        try {
            Main.initLogging();
            Main.reset();
            Options.get().enableTest();
            Options.get().enableContextSensitiveHeap();
            Options.get().enableParameterSensitivity();
            Options.get().enableUnevalizer();
            Options.get().enablePolyfillMDN();
            Options.get().getSoundnessTesterOptions().setTest(false);
            Options.get().setAnalysisTimeLimit(1 * 60);

            Options.get().getArguments().add(Paths.get("test-resources/src/v8tests/prologue.js"));
            Options.get().getArguments().add(file);
            AnalysisMonitor monitoring = new AnalysisMonitor();
            Analysis a = Main.init(Options.get(), monitoring, null);
            Main.run(a);
            Set<Message> messages = monitoring.getMessages();
            return messages.stream().anyMatch(message -> message.getStatus() != Message.Status.NONE && message.getMessage().contains("The variable baz has values with different types"));
        } catch (Exception e) {
            return false;
        }
    }
}
