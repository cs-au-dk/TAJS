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
 * Base class for finding the smallest program that makes two analyses behave differently.
 *
 * @param <T>
 */
public abstract class AbstractAnalysisVariantDifferenceTester<T> implements RunPredicate {

    /**
     * Runs analysis A.
     *
     * @return the value to compare with the return value from {@link #runAnalysisB(Path)}
     */
    protected abstract T runAnalysisA(Path file);

    /**
     * Runs analysis B.
     *
     * @return the value to compare with the return value from {@link #runAnalysisA(Path)}
     */
    protected abstract T runAnalysisB(Path file);

    @Override
    public boolean test(Path file) {
        T resultA;
        try {
            resultA = runAnalysisA(file);
        } catch (Exception e) {
            return false;
        }

        T resultB;
        try {
            resultB = runAnalysisB(file);
        } catch (Exception e) {
            return false;
        }
        boolean sameResult = resultA.equals(resultB);
        return !sameResult;
    }
}
