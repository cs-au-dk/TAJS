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

package dk.brics.tajs.analysis.signatures.types;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.Value;

/**
 * A coercion.
 */
public interface Coercion {

    /**
     * Coerces a value (for side-effects and messages).
     *
     * @return true iff the coercion could not be completed at this moment (awaiting dataflow from an implicit call)
     */
    boolean coerce(Value v, Solver.SolverInterface c);
}
