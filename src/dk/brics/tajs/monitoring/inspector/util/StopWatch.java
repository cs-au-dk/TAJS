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

package dk.brics.tajs.monitoring.inspector.util;

public class StopWatch {

    private long elapsed_nano;

    private long lastStart;

    public StopWatch() {
        elapsed_nano = 0;
        discardAndPause();
    }

    public void pause() {
        checkStarted();
        long stop = System.nanoTime();
        final long delta = stop - lastStart + 1;
        elapsed_nano += delta;
        discardAndPause();
    }

    public void discardAndPause() {
        checkStarted();
        lastStart = -1;
    }

    private void checkStarted() {
        if (lastStart < 0) {
            throw new IllegalStateException("StopWatch has not been started");
        }
    }

    public void startOrContinue() {
        lastStart = System.nanoTime();
    }

    public long getElapsedMicro() {
        return elapsed_nano / (1_000_000);
    }
}
