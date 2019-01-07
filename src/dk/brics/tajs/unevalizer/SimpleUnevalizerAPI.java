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

import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.js.UserFunctionCalls;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation.DynamicLocationMaker;
import dk.brics.tajs.js2flowgraph.FlowGraphMutator;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.ParseError;

import java.util.List;

import static dk.brics.tajs.util.Collections.newList;

/**
 * Simplified implementations of the unevalizer API.
 * <ul>
 * <li>Does not do any dependence analysis</li>
 * <li>Only supports determinate inputs</li>
 * </ul>
 */
public class SimpleUnevalizerAPI { // TODO: GitHub #364

    public static Value evaluateFunctionCall(AbstractNode callNode, List<String> parameterNames, String body, Solver.SolverInterface c) {
        return makeFunction(callNode, parameterNames, body, c);
    }

    private static Value makeFunction(AbstractNode callNode, List<String> parameterNames, String body, Solver.SolverInterface c) {
        DynamicLocationMaker sourceLocationMaker = new DynamicLocationMaker(callNode.getSourceLocation());
        try {
            Function function = FlowGraphMutator.extendFlowGraphWithTopLevelFunction(parameterNames, body, c.getFlowGraph(), sourceLocationMaker);
            ObjectLabel functionLabel = UserFunctionCalls.instantiateGlobalScopeFunction(function, callNode, c.getState(), c);
            return Value.makeObject(functionLabel);
        } catch (ParseError e) {
            Exceptions.throwSyntaxError(c, true);
            return Value.makeNone();
        }
    }

    public static Value createSetTimeOutOrSetIntervalFunction(AbstractNode callNode, String body, Solver.SolverInterface c) {
        return makeFunction(callNode, newList(), body, c);
    }
}
