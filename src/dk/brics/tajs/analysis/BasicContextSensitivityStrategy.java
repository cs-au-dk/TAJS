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

package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.EndLoopNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ContextArguments;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.LocalContext;
import dk.brics.tajs.lattice.LocalContext.LoopUnrollingQualifier;
import dk.brics.tajs.lattice.LocalContext.Qualifier;
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
    public HeapContext makeFunctionHeapContext(Function fun, Solver.SolverInterface c) {
        return makeHeapContext(c.getState().getContext().getFunArgs());
    }

    @Override
    public HeapContext makeActivationAndArgumentsHeapContext(State state, ObjectLabel function, Value thisval, FunctionCalls.CallInfo callInfo, Solver.SolverInterface c) {
        return makeHeapContext(makeContextArgumentsForCall(function, state, callInfo)); // TODO: currently ignoring thisval (recency abstraction avoids some of the precision loss...)
    }

    @Override
    public HeapContext makeConstructorHeapContext(State state, ObjectLabel function, FunctionCalls.CallInfo callInfo, Solver.SolverInterface c) {
        return makeHeapContext(makeContextArgumentsForCall(function, state, callInfo));
    }

    private HeapContext makeHeapContext(ContextArguments funargs) {
        if (!Options.get().isContextSensitiveHeapEnabled()) {
            return null;
        }
        return HeapContext.make(funargs, null);
    }

    private ContextArguments makeContextArgumentsForCall(ObjectLabel obj_f, State edge_state, FunctionCalls.CallInfo callInfo) {
        if (!Options.get().isParameterSensitivityEnabled()) {
            return null;
        }
        Function f = obj_f.getFunction();
        Set<String> contextSensitiveParameterNames = this.contextSensitiveParameters.get(f);
        if (contextSensitiveParameterNames == null) {
            return null;
        }
        final ContextArguments funArgs;
        // apply the parameter sensitivity on the chosen special vars
        if (!contextSensitiveParameterNames.isEmpty() && callInfo.isUnknownNumberOfArgs()) {
            // sensitive in an unknown argument value
            funArgs = new ContextArguments(callInfo.getUnknownArg(), null);
        } else {
            // sensitive in specific argument values
            List<Value> contextSensitiveArguments = newList();
            for (String parameterName : f.getParameterNames()) {
                Value v;
                if (contextSensitiveParameterNames.contains(parameterName)) {
                    int parameterPosition = f.getParameterNames().indexOf(parameterName);
                    v = FunctionCalls.readParameter(callInfo, edge_state, parameterPosition);
                } else {
                    v = null;
                }
                contextSensitiveArguments.add(v);
            }
            funArgs = new ContextArguments(f.getParameterNames(), contextSensitiveArguments, null);
        }
        return funArgs;
    }

    @Override
    public HeapContext makeObjectLiteralHeapContext(AbstractNode node, State state) {
        return makeHeapContext(null);
    }

    @Override
    public Context makeInitialContext() {
        Context c = Context.make(null, null, null, null, null);
        if (log.isDebugEnabled())
            log.debug("creating initial context " + c);
        return c;
    }

    @Override
    public Context makeFunctionEntryContext(State state, ObjectLabel function, FunctionCalls.CallInfo callInfo, Value thisval, Solver.SolverInterface c) {
        assert (function.getKind() == ObjectLabel.Kind.FUNCTION);
        // set thisval for object sensitivity (unlike traditional object sensitivity we use abstract values instead of individual object labels)
        /*Value*/ thisval = null; // FIXME: why not use the thisval argument? (github #479)
        if (!Options.get().isObjectSensitivityDisabled()) {
            if (c.getFlowGraph().getSyntacticInformation().isFunctionWithThisReference(function.getFunction())) {
                thisval = state.readThis();
            }
        }
        ContextArguments contextArguments = makeContextArgumentsForCall(function, state, callInfo);

        // note: c.localContext and c.localContextAtEntry are null by default, which will kill unrollings across calls
        Context context = Context.make(thisval, contextArguments, null, null, null);

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
        Context c = Context.make(currentContext.getThisVal(), currentContext.getFunArgs(), specialRegs,
                currentContext.getLocalContext(), currentContext.getLocalContext());

        if (log.isDebugEnabled())
            log.debug("creating for-in entry context " + c);
        return c;
    }

    @Override
    public Context makeNextLoopUnrollingContext(Context currentContext, BeginLoopNode node) {
        LoopUnrollingQualifier key = LoopUnrollingQualifier.make(node);
        // update loopUnrolling
        Map<Qualifier, Value> localContextQualifiers = newMap();
        if (currentContext.getLocalContext() != null) {
            localContextQualifiers.putAll(currentContext.getLocalContext().getQualifiers());
        }
        int nextUnrollingCount;
        if (localContextQualifiers.containsKey(key)) {
            int currentUnrollingCount = localContextQualifiers.get(key).getNum().intValue();
            if (currentUnrollingCount < Options.get().getLoopUnrollings()) {
                nextUnrollingCount = currentUnrollingCount + 1;
            } else {
                // keep at max + 1 (if the count is reset to zero/removed here, it will begin increasing again!)
                if (log.isDebugEnabled())
                    log.debug("Reusing loop unrolling context " + currentContext);
                return currentContext;
            }
        } else {
            nextUnrollingCount = 0;
        }
        localContextQualifiers.put(key, Value.makeNum(nextUnrollingCount));

        Context c = Context.make(currentContext.getThisVal(), currentContext.getFunArgs(), currentContext.getSpecialRegisters(),
                LocalContext.make(localContextQualifiers), currentContext.getLocalContextAtEntry());

        if (log.isDebugEnabled())
            log.debug("creating loop unrolling context " + c);
        return c;
    }

    @Override
    public Context makeLoopExitContext(Context currentContext, EndLoopNode node) {
        LoopUnrollingQualifier key = LoopUnrollingQualifier.make(node.getBeginNode());
        // reuse currentContext if possible
        if (currentContext.getLocalContext() == null || !currentContext.getLocalContext().getQualifiers().containsKey(key))
            return currentContext;

        // remove the begin-loop node from loopUnrolling
        Map<Qualifier, Value> localContextQualifiers = newMap(currentContext.getLocalContext().getQualifiers());
        localContextQualifiers.remove(key);

        Context c = Context.make(currentContext.getThisVal(), currentContext.getFunArgs(), currentContext.getSpecialRegisters(),
                LocalContext.make(localContextQualifiers), currentContext.getLocalContextAtEntry());

        if (log.isDebugEnabled())
            log.debug("creating loop unrolling exit context " + c);
        return c;
    }

    @Override
    public void requestContextSensitiveParameter(Function function, String parameter) {
        addToMapSet(this.contextSensitiveParameters, function, parameter);
    }
}