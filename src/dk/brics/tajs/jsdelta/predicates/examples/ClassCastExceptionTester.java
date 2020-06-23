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
import dk.brics.tajs.jsdelta.predicates.AbstractExceptionTester;
import dk.brics.tajs.monitoring.AnalysisMonitor;
import dk.brics.tajs.options.Options;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ClassCastExceptionTester extends AbstractExceptionTester {

    public static void main(String[] args) {
        Main.initLogging();
        DeltaMinimizer.reduce(Paths.get("/home/esbena/tmp/classcastexception.js"));
    }

    @Override
    protected void run(Path file) {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
        Options.get().getSoundnessTesterOptions().setTest(false);
        Options.get().getArguments().add(file);
        Analysis a = Main.init(Options.get(), new AnalysisMonitor(), null);
        Main.run(a);
    }

    @Override
    protected boolean test(Exception exception) {
        return exception instanceof ClassCastException;
    }
}
