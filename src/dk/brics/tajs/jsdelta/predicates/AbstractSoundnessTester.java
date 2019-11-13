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

package dk.brics.tajs.jsdelta.predicates;

import dk.brics.tajs.jsdelta.RunPredicate;
import dk.brics.tajs.monitoring.soundness.SoundnessTesterMonitor;
import dk.brics.tajs.options.Options;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Base class for delta-debugging a soundness error.
 * Implementations should implement a run-method like the following:
 * <pre>
 * protected void run(Path file) {
 *  Main.reset();
 *  Options.get().enableTest();
 *  Options.get().enableNoMessages();
 *
 *  setupSoundnessRelevantOptions();
 *
 *  RunUtil.init();
 *  String[] args = {file.toString()};
 *  RunUtil.run(args);
 * }
 * </pre>
 */
public abstract class AbstractSoundnessTester implements RunPredicate {

    protected abstract void run(Path file);

    protected void setupSoundnessRelevantOptions() {
        try {
            Path logFile = Files.createTempFile(AbstractSoundnessTester.class.getCanonicalName(), ".log");
            Options.get().getSoundnessTesterOptions().setExplicitSoundnessLogFile(Optional.of(logFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Options.get().getSoundnessTesterOptions().setTimeLimitExplicitly(Optional.of(10));
        Options.get().getSoundnessTesterOptions().setRegenerate(true);
    }

    @Override
    public boolean test(Path file) {
        try {
            run(file);
        } catch (SoundnessTesterMonitor.SoundnessException e) {
            return true;
        }
        return false;
    }
}
