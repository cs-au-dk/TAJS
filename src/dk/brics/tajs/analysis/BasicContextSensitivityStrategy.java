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

package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.EndLoopNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collections;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;

/**
 * Basic call and heap context sensitivities.
 */
public class BasicContextSensitivityStrategy implements IContextSensitivityStrategy {

    private static Logger log = Logger.getLogger(BasicContextSensitivityStrategy.class);

    /**
     * Parameters that should be treated context-sensitively
     */
    private final Map<Function, Set<String>> contextSensitiveParameters = newMap();

    @Override
    public Context makeFunctionHeapContext(Function fun, Solver.SolverInterface c) {
        return makeHeapContext(c.getState().getContext());
    }

    @Override
    public Context makeActivationAndArgumentsHeapContext(State state, ObjectLabel function, FunctionCalls.CallInfo callInfo, Solver.SolverInterface c) {
        return makeHeapContext(makeContextArgumentsForCall(function, state, callInfo));
    }

    @Override
    public Context makeConstructorHeapContext(State state, ObjectLabel function, FunctionCalls.CallInfo callInfo, Solver.SolverInterface c) {
        return makeHeapContext(makeContextArgumentsForCall(function, state, callInfo));
    }

    private Context makeHeapContext(Context functionContext) {
        if (!Options.get().isContextSensitiveHeapEnabled()) {
            return null;
        }
        return functionContext != null ? Context.make(functionContext.getUnknownArg(), functionContext.getParameterNames(), functionContext.getArguments(), functionContext.getFreeVariables()) : Context.makeEmpty();
    }

    private Context makeContextArgumentsForCall(ObjectLabel obj_f, State edge_state, FunctionCalls.CallInfo callInfo) {
        if (!Options.get().isParameterSensitivityEnabled()) {
            return null;
        }
        Function f = obj_f.getFunction();
        Set<String> contextSensitiveParameterNames = this.contextSensitiveParameters.get(f);
        if (contextSensitiveParameterNames == null) {
            return null;
        }
        final Context functionContext;
        // apply the parameter sensitivity on the chosen special vars
        if (!contextSensitiveParameterNames.isEmpty() && callInfo.isUnknownNumberOfArgs()) {
            // sensitive in an unknown argument value
            functionContext = Context.make(callInfo.getUnknownArg(), null, null, null);
        } else {
            // sensitive in specific argument values
            List<Value> arguments = newList();
            for (String parameterName : f.getParameterNames()) {
                Value v;
                if (contextSensitiveParameterNames.contains(parameterName)) {
                    int parameterPosition = f.getParameterNames().indexOf(parameterName);
                    v = FunctionCalls.readParameter(callInfo, edge_state, parameterPosition);
                } else {
                    v = null;
                }
                arguments.add(v);
            }
            functionContext = Context.make(null, f.getParameterNames(), arguments, null);
        }
        return functionContext;
    }

    @Override
    public Context makeObjectLiteralHeapContext(AbstractNode node, State state, Solver.SolverInterface c) {
        return makeHeapContext(null);
    }

    @Override
    public Context makeInitialContext() {
        Context c = Context.makeEmpty();
        if (log.isDebugEnabled())
            log.debug("creating initial context " + c);
        return c;
    }

    @Override
    public Context makeFunctionEntryContext(State state, ObjectLabel function, FunctionCalls.CallInfo callInfo, Solver.SolverInterface c) {
        assert (function.getKind() == ObjectLabel.Kind.FUNCTION);
        Value thisval = null;
        if (!Options.get().isObjectSensitivityDisabled()) {
            if (c.getFlowGraph().getSyntacticInformation().isFunctionWithThisReference(function.getFunction())) {
                thisval = state.readThis();
            }
        }
        Context functionContext = makeContextArgumentsForCall(function, state, callInfo);
        // note: c.loopUnrolling and c.contextAtEntry are null by default, which will kill unrollings across calls
        Context context = functionContext != null ? Context.make(thisval, null, null, null, null, functionContext.getUnknownArg(), functionContext.getParameterNames(), functionContext.getArguments(), functionContext.getFreeVariables(), callInfo.getFreeVariablePartitioning()) : Context.makeThisVal(thisval, callInfo.getFreeVariablePartitioning());
        if (log.isDebugEnabled())
            log.debug("creating function entry context " + context);
        return context;
    }

    @Override
    public Context makeForInEntryContext(Context currentContext, BeginForInNode n, Value v) {
        // reuse currentContext if possible
        int reg = n.getPropertyListRegister();
        if (currentContext.getSpecialRegisters() != null && currentContext.getSpecialRegisters().containsKey(reg) && currentContext.getSpecialRegisters().get(reg).equals(v)) {
            return currentContext;
        }
        // extend specialRegs with the given (register,value)
        Map<Integer, Value> specialRegs = null;
        if (!Options.get().isForInSpecializationDisabled()) {
            specialRegs = (currentContext.getSpecialRegisters() != null) ? newMap(currentContext.getSpecialRegisters()) : Collections.newMap();
            specialRegs.put(reg, v);
        }
        // for-in acts as entry, so update localContextAtEntry
        Context c = Context.make(currentContext.getThisVal(), specialRegs, null, null, currentContext.getLoopUnrolling(),
                currentContext.getUnknownArg(), currentContext.getParameterNames(), currentContext.getArguments(), currentContext.getFreeVariables(), currentContext.getFreeVariablePartitioning());
        if (log.isDebugEnabled())
            log.debug("creating for-in entry context " + c);
        return c;
    }

    @Override
    public Context makeNextLoopUnrollingContext(Context currentContext, BeginLoopNode node) {
        int limit = Options.get().getLoopUnrollings();
        if (limit == -1) // -1 represents default
            limit = 1; // default is 1
        int currentUnrollingCount = currentContext.getLoopUnrolling() != null ? currentContext.getLoopUnrolling().getOrDefault(node, 0) : 0;
        int nextUnrollingCount = currentUnrollingCount + 1;
        if (nextUnrollingCount > limit)
            return currentContext;
        Map<BeginLoopNode, Integer> loopUnrolling = currentContext.getLoopUnrolling() != null ? newMap(currentContext.getLoopUnrolling()) : newMap();
        loopUnrolling.put(node, nextUnrollingCount);
        Context c = currentContext.copyWithLoopUnrolling(loopUnrolling);
        if (log.isDebugEnabled())
            log.debug("creating loop unrolling context " + c);
        return c;
    }

    @Override
    public Context makeLoopExitContext(Context currentContext, EndLoopNode node) {
        if (currentContext.getLoopUnrolling() == null || !currentContext.getLoopUnrolling().containsKey(node.getBeginNode()))
            return currentContext;
        Map<BeginLoopNode, Integer> loopUnrolling = newMap(currentContext.getLoopUnrolling());
        loopUnrolling.remove(node.getBeginNode());
        Context c = currentContext.copyWithLoopUnrolling(loopUnrolling);
        if (log.isDebugEnabled())
            log.debug("creating loop unrolling exit context " + c);
        return c;
    }

    @Override
    public void requestContextSensitiveParameter(Function function, String parameter) {
        addToMapSet(this.contextSensitiveParameters, function, parameter);
    }
}