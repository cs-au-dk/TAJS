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

package dk.brics.tajs.analysis.signatures.types;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.HostObject;

/**
 * Function signature interface.
 */
public interface Signature {

    /**
     * Checks if the function is invoked with appropriate arguments, also performs related coercions of arguments and propagates type-errors if they have the wrong type.
     *
     * @return true iff the native function should be evaluated.
     */
    boolean shouldStopPropagation(HostObject hostobject, FunctionCalls.CallInfo call, Solver.SolverInterface c);

    /**
     * @return the length property of the function
     */
    int getParametersLength();
}
