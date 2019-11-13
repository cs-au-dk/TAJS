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

import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.jsdelta.DeltaMinimizer;
import dk.brics.tajs.jsdelta.RunPredicate;
import dk.brics.tajs.jsdelta.util.SourceLocationMinimizationUtilities;

import java.nio.file.Path;
import java.util.Set;

/**
 * Base class for finding the smallest set of source locations.
 */
public abstract class AbstractLocationMinimizer implements RunPredicate {

    /**
     * Infers the RunPredicate class to be the caller of this method.
     * <p><b>USE THIS METHOD WITH CAUTION: It inspects the call-stack!</b></p>
     */
    @SuppressWarnings("unchecked")
    protected static final void reduce(Path fileToAnalyze) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        try {
            StackTraceElement caller = stack[2];
            Class<?> callerClass = Class.forName(caller.getClassName());
            Class<RunPredicate> predicate;
            try {
                predicate = (Class<RunPredicate>) callerClass;
            } catch (ClassCastException e) {
                throw new IllegalStateException(String.format("%s#%s() should only be called from classes of type RunPredicate", stack[1].getClassName(), stack[1].getMethodName()));
            }
            DeltaMinimizer.reduce(SourceLocationMinimizationUtilities.makeInitialLocationsFile(fileToAnalyze), predicate);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final boolean test(Path jsonFile) {
        return test(SourceLocationMinimizationUtilities.deserializeLocationsFromJSON(jsonFile));
    }

    /**
     * Implement this instead of {@link #test(Path)}. For this kind of minimizer, the varying input is the set of source locations and not the source file.
     */
    protected abstract boolean test(Set<SourceLocation> sourceLocations);
}
