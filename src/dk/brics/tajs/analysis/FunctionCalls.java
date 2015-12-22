/*
 * Copyright 2009-2015 Aarhus University
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

import dk.brics.tajs.analysis.dom.DOMBuilder;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.js.UserFunctionCalls;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.util.AnalysisException;

import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Models function calls.
 */
public class FunctionCalls {

    private FunctionCalls() {
    }

    /**
     * Information about a function call.
     */
    public interface CallInfo {

        /**
         * Returns the node where the call originates from.
         * Note that this may be an indirect call via call/apply.
         */
        AbstractNode getSourceNode();

        /**
         * Returns the node in the JavaScript code where this call originates from.
         * This is different from the ordinary source node for calls inside host functions.
         */
        AbstractNode getJSSourceNode();

        /**
         * Checks whether this is a constructor call or an ordinary call.
         */
        boolean isConstructorCall();

        /**
         * Returns the abstract value describing which function to call.
         */
        Value getFunctionValue();

        /**
         * Creates the object value of 'this'.
         * Note that this may have side-effects on the callee_state.
         */
        Set<ObjectLabel> prepareThis(State caller_state, State callee_state);

        /**
         * Returns the value of the i'th argument.
         * The first argument is number 0.
         * Returns Undef if the argument is not provided.
         * Can be used even if the number of arguments is unknown.
         *
         * @see #getUnknownArg()
         */
        Value getArg(int i);

        /**
         * Returns the number of arguments.
         *
         * @see #isUnknownNumberOfArgs()
         */
        int getNumberOfArgs();

        /**
         * Returns the value of an unknown argument.
         * Only to be called if the number of arguments is unknown.
         */
        Value getUnknownArg(); // TODO: would simplify things if this could also be used with fixed number of args

        /**
         * Returns true if the number of arguments is unknown.
         */
        boolean isUnknownNumberOfArgs();

        /**
         * Returns the result register.
         */
        int getResultRegister();

        /**
         * Returns the execution context.
         */
        ExecutionContext getExecutionContext();
    }

    /**
     * Call information for an ordinary call/construct.
     */
    public static class OrdinaryCallInfo implements CallInfo {

        private CallNode n;

        private State state;

        public OrdinaryCallInfo(CallNode n, State state) {
            this.n = n;
            this.state = state;
        }

        @Override
        public CallNode getSourceNode() {
            return n;
        }

        @Override
        public CallNode getJSSourceNode() {
            return n;
        }

        @Override
        public boolean isConstructorCall() {
            return n.isConstructorCall();
        }

        @Override
        public Value getFunctionValue() {
            throw new AnalysisException();
        }

        @Override
        public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
            throw new AnalysisException();
        }

        @Override
        public Value getArg(int i) {
            if (i < n.getNumberOfArgs()) {
                return state.readRegister(n.getArgRegister(i));
            } else
                return Value.makeUndef();
        }

        @Override
        public int getNumberOfArgs() {
            return n.getNumberOfArgs();
        }

        @Override
        public Value getUnknownArg() {
            throw new AnalysisException("Calling getUnknownArg but number of arguments is not unknown");
        }

        @Override
        public boolean isUnknownNumberOfArgs() {
            return false;
        }

        @Override
        public int getResultRegister() {
            return n.getResultRegister();
        }

        @Override
        public ExecutionContext getExecutionContext() {
            return state.getExecutionContext();
        }
    }

    /**
     * Information for an event handler call.
     */
    public static class EventHandlerCall implements CallInfo {

        private Node sourceNode;

        private Value function;

        private Value arg1;

        private State state;

        public EventHandlerCall(Node sourceNode, Value function, Value arg1, State state) {
            this.sourceNode = sourceNode;
            this.function = function;
            this.arg1 = arg1;
            this.state = state;
        }

        @Override
        public AbstractNode getSourceNode() {
            return sourceNode;
        }

        @Override
        public Node getJSSourceNode() {
            return sourceNode;
        }

        @Override
        public boolean isConstructorCall() {
            return false;
        }

        @Override
        public Value getFunctionValue() {
            return function;
        }

        @Override
        public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
            // TODO: improve precision for setTimeout/setInterval: will always have the global object as the this-object
            return DOMBuilder.getAllDOMEventTargets();
        }

        @Override
        public Value getArg(int i) {
            if (arg1 != null && i == 0) {
                return arg1;
            }
            return Value.makeUndef();
        }

        @Override
        public int getNumberOfArgs() {
            if (arg1 != null) {
                return 1;
            }
            return 0;
        }

        @Override
        public Value getUnknownArg() {
            throw new AnalysisException("Calling getUnknownArg but number of arguments is not unknown");
        }

        @Override
        public boolean isUnknownNumberOfArgs() {
            return false;
        }

        @Override
        public int getResultRegister() {
            return AbstractNode.NO_VALUE;
        }

        @Override
        public ExecutionContext getExecutionContext() {
            return state.getExecutionContext();
        }
    }

    /**
     * Convenience class for creating the CallInfo for an implicit call.
     */
    public static abstract class DefaultImplicitCallInfo implements CallInfo {

        private final Solver.SolverInterface c;

        public DefaultImplicitCallInfo(GenericSolver<State, Context, CallEdge, IAnalysisMonitoring, Analysis>.SolverInterface c) {
            this.c = c;
        }

        @Override
        public AbstractNode getSourceNode() {
            return c.getNode();
        }

        @Override
        public AbstractNode getJSSourceNode() {
            return c.getNode();
        }

        @Override
        public boolean isConstructorCall() {
            return false;
        }

        @Override
        public Value getFunctionValue() {
            // NB: could easily be supported, but we do not need it currently
            throw new AnalysisException("Unexpected usage of getFunctionValue");
        }

        @Override
        public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
            return singleton(InitialStateBuilder.GLOBAL);
        }

        @Override
        public int getResultRegister() {
            return AbstractNode.NO_VALUE;
        }

        @Override
        public ExecutionContext getExecutionContext() {
            return c.getState().getExecutionContext();
        }
    }

    /**
     * Enters a function described by a CallInfo.
     */
    public static void callFunction(CallInfo call, Solver.SolverInterface c) {
        State caller_state = c.getState();
        Value funval = call.getFunctionValue();
        funval = UnknownValueResolver.getRealValue(funval, caller_state);
        boolean maybe_non_function = funval.isMaybePrimitive();
        boolean maybe_function = false;
        for (ObjectLabel objlabel : funval.getObjectLabels()) {
            if (objlabel.getKind() == Kind.FUNCTION) {
                maybe_function = true;
                if (objlabel.isHostObject()) { // host function
                    State newstate = caller_state.clone();
                    State ts = c.getState();
                    c.setState(newstate); // note that the calling context is not affected, even though e.g. 'this' may get a different value
                    if (!call.isConstructorCall() &&
                            !objlabel.getHostObject().equals(ECMAScriptObjects.EVAL) &&
                            !objlabel.getHostObject().equals(ECMAScriptObjects.FUNCTION) &&
                            !objlabel.getHostObject().equals(DOMObjects.WINDOW_SET_INTERVAL) &&
                            !objlabel.getHostObject().equals(DOMObjects.WINDOW_SET_TIMEOUT)) {
                        ExecutionContext old_ec = newstate.getExecutionContext();
                        newstate.setExecutionContext(new ExecutionContext(old_ec.getScopeChain(), newSet(old_ec.getVariableObject()), newSet(call.prepareThis(caller_state, newstate))));
                    }
                    Value res = HostAPIs.evaluate(objlabel.getHostObject(), call, newstate, c);
                    newstate = c.getState();
                    if (call.getSourceNode().isRegistersDone())
                        newstate.clearOrdinaryRegisters();
                    if ((!res.isNone() && !newstate.isNone()) || Options.get().isPropagateDeadFlow()) {
                        newstate.setExecutionContext(call.getExecutionContext());
                        if (call.getResultRegister() != AbstractNode.NO_VALUE)
                            newstate.writeRegister(call.getResultRegister(), res);
                        c.propagateToBasicBlock(newstate, call.getSourceNode().getBlock().getSingleSuccessor(), newstate.getContext());
                    }
                    c.setState(ts);
                } else { // user-defined function
                    UserFunctionCalls.enterUserFunction(objlabel, call, caller_state, false, c);
                    c.getMonitoring().visitUserFunctionCall(objlabel.getFunction(), call.getSourceNode(), call.isConstructorCall());
                }
            } else
                maybe_non_function = true;
        }
        if (funval.getObjectLabels().isEmpty() && Options.get().isPropagateDeadFlow()) {
            State newstate = caller_state.clone();
            if (call.getResultRegister() != AbstractNode.NO_VALUE)
                newstate.writeRegister(call.getResultRegister(), Value.makeNone());
            c.propagateToBasicBlock(newstate, call.getSourceNode().getBlock().getSingleSuccessor(), newstate.getContext());
        }
        c.getMonitoring().visitCall(c.getNode(), maybe_non_function, maybe_function);
        if (maybe_non_function)
            Exceptions.throwTypeError(caller_state, c);
    }
}
