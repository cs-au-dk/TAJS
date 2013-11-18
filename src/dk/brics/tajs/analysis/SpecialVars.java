/*
 * Copyright 2009-2013 Aarhus University
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
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Pair;

/**
 * Variables relevant for context sensitivity.
 */
public class SpecialVars { // FIXME: need some kind of widening to avoid infinitely many contexts (bound the size of each Set<String> in specialvars)

	/**
	 * Map from functions to names of special variables used for context sensitivity.
	 */
	private Map<Function, Set<String>> specialvars;
	
	/**
	 * Constructs a new empty SpecialVars object.
	 */
	public SpecialVars() {
		specialvars = Collections.newMap();
	}
	
	/**
     * Add elements to the map for context sensitivity.
     * Inner functions inherit special variables from outer functions.
     * @param f function
     * @param var variable that should be taken into account for context sensitivity
     */
    public void addToSpecialVars(Function f, String var) {
        Collections.addToMapSet(specialvars, f, var);
    }

	/**
	 * Returns the names of the special variables for the given function.
	 */
	public Set<String> getSpecialVars(Function f) {
		Set<String> vars = newSet();
		for (; f != null; f = f.getOuterFunction()) {
			Set<String> vs = specialvars.get(f);
			if (vs != null) 
				vars.addAll(vs);
		}
		if (vars.isEmpty())
			vars = null;
		return vars;
	}

	/**
     * Makes the given function context sensitive on selected parameters, and recursively backward through the call graph.
     * @param f the function
     * @param args the parameter names
     */
    public void addContextSensitivity(Function f, Collection<String> args, State s, Solver.SolverInterface c) { 
    	// TODO: explain in the javadoc how this works (note that it's currently a hack that assumes that the parameters are passed unchanged between the functions)
        if (args.isEmpty())
            return;
        boolean added = false;
        for (String param : f.getParameterNames()) {
            if (args.contains(param)) {
                addToSpecialVars(f, param); 
                added = true;
            }
        }
        if (added) { // TODO: explain the interaction with the unevalizer...
        	CallGraph<?, Context, ?>  cg = c.getAnalysisLatticeElement().getCallGraph();
        	Set<Pair<NodeAndContext<Context>,Context>> callers = cg.getSources(new BlockAndContext<>(f.getEntry(), s.getContext())); // TODO: use getEntryBlockAndContext? 
        	for (Pair<NodeAndContext<Context>,Context> caller : callers) { // propagate recursively backward through the call graph 
        		NormalForm nf = UnevalTools.rebuildNormalForm(c.getFlowGraph(), (CallNode) caller.getFirst().getNode(), s, c);
        		addContextSensitivity(caller.getFirst().getNode().getBlock().getFunction(), nf.getArgumentsInUse(), s, c);
        	}
        }
    }
}
