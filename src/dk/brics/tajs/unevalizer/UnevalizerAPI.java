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

package dk.brics.tajs.unevalizer;

import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.analysis.EvalCache;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.uneval.NormalForm;
import dk.brics.tajs.analysis.uneval.UnevalTools;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.FlowGraphFragment;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.js2flowgraph.FlowGraphMutator;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.CallGraph.ReverseEdge;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.Strings;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newSet;

public class UnevalizerAPI {

    private static final Logger log = Logger.getLogger(UnevalizerAPI.class);

    public static Value evaluateFunctionCall(CallInfo call, GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, Analysis>.SolverInterface c, State state, CallNode callNode, String body, List<String> parameterNames, FlowGraph currentFg) {
        String var = call.getResultRegister() == AbstractNode.NO_VALUE ? null : UnevalTools.gensym();
        String complete_function = (var == null ? "\"" : "\"" + var + " = ") + "(function (" + String.join(",", parameterNames) + ") {" + body + "})\"";

        NormalForm input = UnevalTools.rebuildNormalForm(currentFg, callNode, state, c);
        String unevaled = new Unevalizer().uneval(UnevalTools.unevalizerCallback(currentFg, c, callNode, input, false), complete_function, false, null, call.getSourceNode(), c);

        if (unevaled == null)
            return UnevalizerLimitations.handle("Unevalable eval: " + UnevalTools.rebuildFullExpression(currentFg, callNode, callNode.getArgRegister(0)), call.getSourceNode(), c);

        String unevaledSubst = var == null ? unevaled : unevaled.replace(var, UnevalTools.VAR_PLACEHOLDER); // to avoid the random string in the cache

        if (log.isDebugEnabled())
            log.debug("Unevalized: " + unevaled);

        EvalCache evalCache = c.getAnalysis().getEvalCache();
        NodeAndContext<Context> cc = new NodeAndContext<>(call.getSourceNode(), state.getContext());
        FlowGraphFragment e = evalCache.getCode(cc);

        if (e == null || !e.getKey().equals(unevaledSubst)) {
            e = FlowGraphMutator.extendFlowGraph(currentFg, unevaled, unevaledSubst, e, callNode, false, var);
        }

        evalCache.setCode(cc, e);
        c.propagateToBasicBlock(state.clone(), e.getEntryBlock(), state.getContext());
        return Value.makeNone();
    }

    public static Value evaluateEvalCall(CallInfo call, GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, Analysis>.SolverInterface c, State state, CallNode callNode) {
        if(callNode.getNumberOfArgs() == 0){
            return Value.makeUndef();
        }
        FlowGraph currentFg = c.getFlowGraph();
        boolean ignoreResult = callNode.getResultRegister() == AbstractNode.NO_VALUE;
        String var = ignoreResult ? null : UnevalTools.gensym(); // Do we need the value of the eval call after?
        NormalForm input = UnevalTools.rebuildNormalForm(currentFg, callNode, state, c);

        // Collect special args that should be analyzed context sensitively

        Function f = callNode.getBlock().getFunction();
        Set<String> importantParameters = input.getArgumentsInUse().stream().filter(arg -> f.getParameterNames().contains(arg)).collect(Collectors.toSet());
        addContextSensitivity(f, importantParameters, state, newSet(), c);

        // What we should use as key for the eval cache is the entire tuple from the Uneval paper. Since that
        // might contain infinite sets and other large things we just call the Unevalizer and compare the output
        // of the Unevalizer to the key in the cache. This makes us Uneval more things, but we save the work
        // of re-extending the flow graph every time.
        boolean aliased_call = !"eval".equals(UnevalTools.get_call_name(currentFg, callNode)); // TODO: aliased_call should also affect the execution context?
        String unevaled = new Unevalizer().uneval(UnevalTools.unevalizerCallback(currentFg, c, callNode, input, true), input.getNormalForm(), aliased_call, var, call.getSourceNode(), c);

        if (unevaled == null)
            return UnevalizerLimitations.handle("Unevalable eval: " + UnevalTools.rebuildFullExpression(currentFg, callNode, callNode.getArgRegister(0)), call.getSourceNode(), c);
        if (log.isDebugEnabled())
            log.debug("Unevalized: " + unevaled);

        unevaled = UnevalTools.rebuildFullFromMapping(currentFg, unevaled, input.getMapping(), callNode);

        String unevaledSubst = ignoreResult ? unevaled : unevaled.replace(var, UnevalTools.VAR_PLACEHOLDER); // to avoid the random string in the cache
        EvalCache evalCache = c.getAnalysis().getEvalCache();
        NodeAndContext<Context> cc = new NodeAndContext<>(callNode, state.getContext());
        FlowGraphFragment e = evalCache.getCode(cc);

        // Cache miss.
        if (e == null || !e.getKey().equals(unevaledSubst)) {
            e = FlowGraphMutator.extendFlowGraph(currentFg, unevaled, unevaledSubst, e, callNode, false, var);
        }

        evalCache.setCode(cc, e);
        c.propagateToBasicBlock(state.clone(), e.getEntryBlock(), state.getContext());
        if (Options.get().isFlowGraphEnabled()) {
            try (PrintWriter pw = new PrintWriter(new File("out" + File.separator + "flowgraphs" + File.separator + "uneval-" +
                    callNode.getIndex() + "-" + Integer.toHexString(state.getContext().hashCode()) + ".dot"))) {
                currentFg.toDot(pw);
                pw.flush();
            } catch (Exception ee) {
                throw new AnalysisException(ee);
            }
        }
        return Value.makeNone();
    }

    /**
     * Makes the given function context sensitive on selected parameters, and recursively backward through the call graph.
     * (note that it currently assumes that the parameters are passed unchanged between the functions) (#167)
     *
     * @param f    the function
     * @param args the parameter names
     */
    private static void addContextSensitivity(Function f, Collection<String> args, State s, Set<Pair<BlockAndContext<Context>, Set<String>>> visited, Solver.SolverInterface c) {
        // avoid infinite recursion
        BlockAndContext<Context> blockAndContext = BlockAndContext.makeEntry(s.getBasicBlock(), s.getContext());
        Pair<BlockAndContext<Context>, Set<String>> target = Pair.make(blockAndContext, newSet(args));
        if (visited.contains(target)) {
            return;
        }
        visited = newSet(visited);
        visited.add(target);

        // make the current function context sensitive
        Set<String> targetParameterNames = newSet(f.getParameterNames());
        targetParameterNames.retainAll(args); // (ignores provided non-parameter names)
        if (targetParameterNames.isEmpty()) {
            return;
        }
        targetParameterNames.forEach(parameter ->
                c.getAnalysis().getContextSensitivityStrategy().requestContextSensitiveParameter(f, parameter));

        // make calling functions context sensitive
        CallGraph<?, Context, ?> cg = c.getAnalysisLatticeElement().getCallGraph();
        Set<ReverseEdge<Context>> callers = cg.getSources(blockAndContext);
        for (ReverseEdge<Context> caller : callers) { // propagate recursively backward through the call graph
            if (!(caller.getCallNode() instanceof CallNode)) {
                continue; // ignore pseudo-function calls (from for-in). Solution: unwrap block-and-context until the function-entry is reached
            }
            NormalForm nf = UnevalTools.rebuildNormalForm(c.getFlowGraph(), (CallNode) caller.getCallNode(), s, c);
            addContextSensitivity(caller.getCallNode().getBlock().getFunction(), nf.getArgumentsInUse(), s, visited, c);
        }
    }

    public static Value evaluateSetTimeoutSetIntervalStringCall(CallInfo call, GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, Analysis>.SolverInterface c, State s, Value callbackSourceCode, CallNode callNode) {
        FlowGraph currFg = c.getFlowGraph();
        NormalForm nf = UnevalTools.rebuildNormalForm(currFg, callNode, s, c);

        String uneval_input = callbackSourceCode.getStr() != null ? "\"" + Strings.escapeSource(callbackSourceCode.getStr()) + "\"" : nf.getNormalForm();
        String unevaled = new Unevalizer().uneval(UnevalTools.unevalizerCallback(currFg, c, callNode, nf, false), uneval_input, false, null, call.getSourceNode(), c);
        if (unevaled == null) {
            return UnevalizerLimitations.handle("Could not uneval setTimeout/setInterval string (you should use higher-order functions instead): " + uneval_input, call.getSourceNode(), Value.makeNone(), c);
        }
        log.debug("Unevalized:" + unevaled);

        if (callbackSourceCode.getStr() == null) // Called with non-constant.
            unevaled = UnevalTools.rebuildFullFromMapping(currFg, unevaled, nf.getMapping(), callNode);

        EvalCache evalCache = c.getAnalysis().getEvalCache(); // TODO: refactor to avoid duplicated code (see JSFunction.FUNCTION and JSGlobal.EVAL)
        NodeAndContext<Context> cc = new NodeAndContext<>(callNode, s.getContext());
        FlowGraphFragment e = evalCache.getCode(cc);

        // Cache miss.
        if (e == null || !e.getKey().equals(unevaled)) {
            e = FlowGraphMutator.extendFlowGraph(currFg, unevaled, unevaled, e, callNode, true, null);
        }

        ObjectLabel callbackUnevaled = ObjectLabel.make(e.getEntryFunction());
        evalCache.setCode(cc, e);
        if (Options.get().isFlowGraphEnabled()) {
            try (PrintWriter pw = new PrintWriter(new File("out" + File.separator + "flowgraphs" + File.separator + "uneval-" +
                    callNode.getIndex() + "-" + Integer.toHexString(s.getContext().hashCode()) + ".dot"))) {
                currFg.toDot(pw);
                pw.flush();
            } catch (Exception ee) {
                throw new AnalysisException(ee);
            }
        }
        return Value.makeObject(callbackUnevaled);
    }
}
