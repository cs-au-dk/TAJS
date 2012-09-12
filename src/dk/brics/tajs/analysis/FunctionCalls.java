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

import java.util.Set;

import org.apache.log4j.Logger;

import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.html.HTMLBuilder;
import dk.brics.tajs.analysis.js.UserFunctionCalls;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.ICallNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Summarized;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.AnalysisException;

/**
 * Models function calls.
 */
public class FunctionCalls {

	private static Logger logger = Logger.getLogger(FunctionCalls.class); 

	private FunctionCalls() {}
	
	/**
	 * Information about a function call.
	 */
	public static interface CallInfo {

		/**
		 * Returns the node where the call originates from.
		 * Note that this may be an indirect call via call/apply.
		 */
		public AbstractNode getSourceNode();
		
		/**
		 * Returns the node in the JavaScript code where this call originates from.
		 * This is different from the ordinary source node for calls inside host functions. 
		 */
		public Node getJSSourceNode();

		/**
		 * Checks whether this is a construct or an ordinary call.
		 */
		public boolean isConstructorCall();
		
		/**
		 * Returns the abstract value describing which function to call.
		 */
		public Value getFunctionValue();

		/**
		 * Creates the object value of 'this'.
		 * Note that this may have side-effects on the callee_state.
		 */
		public Set<ObjectLabel> prepareThis(State caller_state, State callee_state);

		/**
		 * Returns the value of the i'th argument.
		 * The first argument is number 0.
		 * Returns Undef if the argument is not provided.
		 * Can be used even if the number of arguments is unknown.
		 * @see #getUnknownArg()
		 */
		public Value getArg(int i);
		
		/**
		 * Returns the number of arguments.
		 * @see #isUnknownNumberOfArgs()
		 */
		public int getNumberOfArgs();
		
		/**
		 * Returns the value of an unknown argument.
		 * Only to be called if the number of arguments is unknown.
		 */
		public Value getUnknownArg(); // TODO: would simplify things if this could also be used with fixed number of args
		
		/**
		 * Returns true if the number of arguments is unknown.
		 */
		public boolean isUnknownNumberOfArgs();
		
		/**
		 * Returns the result register.
		 */
		public int getResultRegister();

		/**
		 * Returns the base register.
		 */
        // TODO: What is this used for?
        @Deprecated
		public int getBaseRegister();
	}

	/**
	 * Information for an event handler call.
	 */
    public static class EventHandlerCall implements CallInfo {

        private Node sourceNode;

        private Value function;
        
        private Value arg1;

        public EventHandlerCall(Node sourceNode, Value function, Value arg1) {
            this.sourceNode = sourceNode;
            this.function = function;
            this.arg1 = arg1;
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
            return HTMLBuilder.HTML_OBJECT_LABELS; // FIXME: setTimeout/setInterval should use the global object instead (uneval48.js)
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
        public int getBaseRegister() {
            return AbstractNode.NO_VALUE;
        }
    }

	/**
	 * Enters a function described by a CallInfo.
	 */
	public static void callFunction(CallInfo call, State caller_state, Solver.SolverInterface c) {
		Value funval = call.getFunctionValue();
		funval = UnknownValueResolver.getRealValue(funval, caller_state);
		boolean maybe_non_function = funval.isMaybePrimitive();
		boolean maybe_function = false;
		for (ObjectLabel objlabel : funval.getObjectLabels()) {
			if (objlabel.getKind() == Kind.FUNCTION) {
				maybe_function = true;
				if (objlabel.isHostObject()) { // host function
						State newstate = caller_state.clone();
						ExecutionContext old_ec;
						if (!call.isConstructorCall() && 
								!objlabel.getHostObject().equals(ECMAScriptObjects.EVAL) &&
								!objlabel.getHostObject().equals(ECMAScriptObjects.FUNCTION) &&
								!objlabel.getHostObject().equals(DOMObjects.WINDOW_SET_INTERVAL) &&
								!objlabel.getHostObject().equals(DOMObjects.WINDOW_SET_TIMEOUT)) { 
							old_ec = newstate.getExecutionContext();
							newstate.setExecutionContext(new ExecutionContext(old_ec.getScopeChain(), newSet(old_ec.getVariableObject()), newSet(call.prepareThis(caller_state, newstate))));
						} else
							old_ec = null;
						State ts = c.getCurrentState();
						c.setCurrentState(newstate); // note that the calling context is not affected, even though e.g. 'this' may get a different value
						Value res = HostAPIs.evaluate(objlabel.getHostObject(), call, newstate, c);
						c.setCurrentState(ts);
						if (call.getSourceNode().isRegistersDone())
							newstate.clearOrdinaryRegisters();
						if ((!res.isNone() && !newstate.isNone()) || Options.isPropagateDeadFlow()) {
							if (old_ec != null)
								newstate.setExecutionContext(old_ec);
							if (call.getResultRegister() != AbstractNode.NO_VALUE)
								newstate.writeRegister(call.getResultRegister(), res);
							c.propagateToBasicBlock(newstate, call.getSourceNode().getBlock().getSingleSuccessor(), c.getCurrentContext());
						}
				} else { // user-defined function
					UserFunctionCalls.enterUserFunction(objlabel, call, caller_state, c);
					c.getMonitoring().visitUserFunctionCall(objlabel.getFunction(), call.getSourceNode(), call.isConstructorCall());
				}
			} else 
				maybe_non_function = true;
		}
		if (funval.getObjectLabels().isEmpty() && Options.isPropagateDeadFlow()) {
			State newstate = caller_state.clone();
			if (call.getResultRegister() != AbstractNode.NO_VALUE)
				newstate.writeRegister(call.getResultRegister(), Value.makeNone());
			c.propagateToBasicBlock(newstate, call.getSourceNode().getBlock().getSingleSuccessor(), c.getCurrentContext());
		}
		c.getMonitoring().visitCall(c.getCurrentNode(), maybe_non_function, maybe_function);
		if (maybe_non_function) 
			Exceptions.throwTypeError(caller_state, c);
	}
	
	/**
	 * Leaves a function.
	 */
	public static void leaveFunction(NodeAndContext<CallContext> caller, Value returnval, boolean exceptional, Function f, State state, Value thisval, Solver.SolverInterface c) {
        AbstractNode node = caller.getNode();
        ICallNode callnode;
        if (node instanceof ICallNode)
            callnode = (ICallNode) node;
        else
            return; // FIXME: implicit function call to valueOf/toString (node may then not be first in its block!), how do we handle such return flow? by additional call nodes?
		CallContext caller_context = caller.getContext();
        boolean is_constructor = callnode.isConstructorCall();
        int result_reg = callnode.getResultRegister();

		if (logger.isDebugEnabled()) 
			logger.debug("trying call node " + node.getIndex() + ": " + node
					+ " at " + node.getSourceLocation() + "\n" +
					"caller context: " + caller_context + ", callee context: " + c.getCurrentContext());

		// apply inverse renaming
		state.pushRegister(returnval);
		state.pushRegister(thisval);
		c.returnFromFunctionExit(state, node, caller_context, f, c.getCurrentContext());
		if (state.isNone())
			return; // flow was cancelled, probably something needs to be recomputed
		thisval = state.popRegister();
		returnval = state.popRegister();

		// merge newstate with caller state and call edge state
		Summarized callee_summarized = new Summarized(state.getSummarized());
		ObjectLabel activation_obj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ACTIVATION);
		callee_summarized.addDefinitelySummarized(activation_obj);
		ObjectLabel arguments_obj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ARGUMENTS);
		callee_summarized.addDefinitelySummarized(arguments_obj);
		if (is_constructor) {
			ObjectLabel this_obj = new ObjectLabel(node, Kind.OBJECT);
			callee_summarized.addDefinitelySummarized(this_obj);
		} // FIXME: prepareThis may create additional objects (for enterUserFunction)!!!
		returnval = state.mergeFunctionReturn(c.getAnalysisLatticeElement().getStates(node.getBlock()).get(caller_context), 
				c.getAnalysisLatticeElement().getCallGraph().getCallEdge(node, caller_context, f.getEntry(), c.getCurrentContext()).getState(), 
				c.getAnalysisLatticeElement().getState(node.getBlock().getFunction().getEntry(), caller_context), 
				callee_summarized,
				returnval); // TODO: not obvious why this part is in dk.brics.tajs.analysis and the renaming and trimming is done via dk.brics.tajs.solver... 
		if (node.isRegistersDone())
			state.clearOrdinaryRegisters();

		if (exceptional) { 
			// transfer exception value to caller
			Exceptions.throwException(state, returnval, c, node, caller_context);

		} else { 
			returnval = UnknownValueResolver.getRealValue(returnval, state);
			if (is_constructor && returnval.isMaybePrimitive()) { // FIXME: skip this if f is a host function
				// 13.2.2.7-8 replace non-object by the new object (which is kept in 'this')
				returnval = returnval.restrictToObject().join(thisval);
			}

			// transfer ordinary return value to caller
			if (result_reg != AbstractNode.NO_VALUE)
				state.writeRegister(result_reg, returnval);

			// flow to next basic block after call_node
			c.propagateToBasicBlock(state, node.getBlock().getSingleSuccessor(), caller_context);
		}
	}
}
