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

package dk.brics.tajs.unevalizer;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisLimitationException;

/**
 * Utility class for allowing unevalizer limitations unsoundly when needed.
 */
public class UnevalizerLimitations {

    public static Value handle(String msg, AbstractNode node, Solver.SolverInterface c) {
        return handle(msg, node, Value.makeNone(), c);
    }

    public static Value handle(String msg, AbstractNode node, Value value, Solver.SolverInterface c) {
        if (c.getAnalysis().getUnsoundness().mayIgnoreImpreciseEval(node)) {
            return value;
        }
        throw new AnalysisLimitationException.AnalysisPrecisionLimitationException(node.getSourceLocation() + ": " + msg);
    }
}
