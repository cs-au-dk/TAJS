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

import java.nio.file.Path;

/**
 * Base class for delta-debugging for the occurence of an exception.
 */
public abstract class AbstractExceptionTester implements RunPredicate {

    @Override
    public final boolean test(Path file) {
        try {
            run(file);
        } catch (Exception e) {
            try {
                boolean test = test(e);
                if (test) {
                    return true;
                }
            } catch (Exception testException) {
                // the implemented predicate threw an exception!
                testException.printStackTrace();
                return false;
            }
        }
        return false;
    }

    protected abstract void run(Path file);

    protected abstract boolean test(Exception exception);
}
