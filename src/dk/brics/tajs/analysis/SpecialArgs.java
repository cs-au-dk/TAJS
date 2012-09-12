/*
 * Copyright 2012 Aarhus University
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

package dk.brics.tajs.analysis;

import static dk.brics.tajs.util.Collections.newSet;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import dk.brics.tajs.analysis.uneval.NormalForm;
import dk.brics.tajs.analysis.uneval.UnevalTools;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.Collections;

/**
 * Function arguments relevant for context sensitivity.
 */
public class SpecialArgs { // FIXME: need some kind of widening to avoid infinitely many contexts (bound the size of each Set<String> in special_args)

	/**
	 * Map from functions to argument names of special arguments used for context sensitivity.
	 */
	private Map<Function, Set<String>> special_args;
	
	/**
	 * Constructs a new empty SpecialArgs object.
	 */
	public SpecialArgs() {
		special_args = Collections.newMap();
	}
	
	/**
     * Add elements to the map for context sensitivity.
     * @param f function
     * @param arg argument that should be taken into account for context sensitivity
     */
    public void addToSpecialArgs(Function f, String arg) {
        Collections.addToMapSet(special_args, f, arg);
    }

    /**
     * Returns the values of the special arguments, or null if empty.
     * @param state state
     * @param f function
     */
	public Set<Value> getSpecialArgValues(State state, Function f) {
		Set<String> args = special_args.get(f);
		if (args != null) {
			Set<Value> values = newSet();
			for (String s : args) {
				values.add(state.readVariable(s, null));
			}
			return values;
		}
		return null;
	}

    /**
     * Makes a single function f context sensitive on the parameters args. Does nothing if args are not parameters to f.
     */
    public void addContextSensitivity(Function f, Collection<String> args, State s, Solver.SolverInterface c) { // TODO: explain in the javadoc how this works (note that it's currently a hack that assumes that the parameters are passed unchanged between the functions)
        // Avoid doing the search if there is nothing to search for.
        if (args.isEmpty())
            return;

        boolean added = false;
        for (String param : f.getParameterNames()) {
            if (args.contains(param)) {
                addToSpecialArgs(f, param); 
                added = true;
            }
        }
        if (added) {
        	CallGraph<?, CallContext, ?>  cg = c.getAnalysisLatticeElement().getCallGraph();
        	Set<NodeAndContext<CallContext>> callers = cg.getSources(f.getEntry(), c.getCurrentContext());
        	for (NodeAndContext<CallContext> caller : callers) {
        		NormalForm nf = UnevalTools.rebuildNormalForm(c.getFlowGraph(), (CallNode) caller.getNode(), s, c);
        		addContextSensitivity(caller.getNode().getBlock().getFunction(), nf.getArgumentsInUse(), s, c);
        	}
        }
    }
}
