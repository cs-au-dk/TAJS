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

package dk.brics.tajs.analysis.js;

import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import dk.brics.tajs.analysis.CallContext;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.Summarized;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.NodeAndContext;

/**
 * Models calls to user-defined (non-host) functions.
 */
public class UserFunctionCalls {

	private static Logger logger = Logger.getLogger(UserFunctionCalls.class); 

	private UserFunctionCalls() {}

    /**
     * Declares a function.
     */
	public static void declareFunction(DeclareFunctionNode n, State state) {
        Function fun = n.getFunction();
        boolean is_expression = n.isExpression();
        int result_reg = n.getResultRegister();
		// TODO: join function objects (p.72)? (if same n and same scope)
        ObjectLabel fn = new ObjectLabel(fun);
        // 13.2 step 2 and 3
        state.newObject(fn);
        Value f = Value.makeObject(fn);
        // 13.2 step 4
        state.writeInternalPrototype(fn, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        // 13.2 step 7
        ScopeChain scope = state.getScopeChain();
        if (is_expression && fun.getName() != null) {
            // p.79 (function expression with identifier)
            ObjectLabel front = new ObjectLabel(fun.getEntry().getFirstNode(), Kind.OBJECT);
            state.newObject(front);
            scope = ScopeChain.make(Collections.singleton(front), scope);
            state.writePropertyWithAttributes(front, fun.getName(), f.setAttributes(false, true, true));
            /* From ES5, Annex D:
             In Edition 3, the algorithm for the production FunctionExpression with an Identifier adds an object 
             created as if by new Object() to the scope chain to serve as a scope for looking up the name of the 
             function. The identifier resolution rules (10.1.4 in Edition 3) when applied to such an object will, 
             if necessary, follow the object's prototype chain when attempting to resolve an identifier. This
             means all the properties of Object.prototype are visible as identifiers within that scope. In 
             practice most implementations of Edition 3 have not implemented this semantics. Edition 5 changes 
             the specified semantics by using a Declarative Environment Record to bind the name of the function.
             */ // TODO: use ES5 semantics of function expressions with identifier?
        } else if (!is_expression && fun.getName() != null) {
            // p.79 (function declaration)
            state.declareAndWriteVariable(fun.getName(), f, true);
        }
        state.writeObjectScope(fn, scope);
        // 13.2 step 8
        state.writePropertyWithAttributes(fn, "length", Value.makeNum(fun.getParameterNames().size()).setAttributes(true, true, true));
        // 13.2 step 9 
        ObjectLabel prototype = new ObjectLabel(n, Kind.OBJECT);
        state.newObject(prototype);
        state.writeInternalPrototype(prototype, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        // 13.2 step 10
        state.writePropertyWithAttributes(prototype, "constructor", Value.makeObject(fn).setAttributes(true, false, false));
        // 13.2 step 11
        state.writePropertyWithAttributes(fn, "prototype", Value.makeObject(prototype).setAttributes(false, true, false));
        state.writeInternalValue(prototype, Value.makeNum(Double.NaN)); // TODO: as in Rhino (?)
        if (result_reg != AbstractNode.NO_VALUE)
            state.writeRegister(result_reg, f);
	}

    /**
     * Enters a user-defined function.
     */
	public static void enterUserFunction(ObjectLabel obj_f, CallInfo call, State caller_state, Solver.SolverInterface c) {
		Function f = obj_f.getFunction();
		AbstractNode n = call.getSourceNode();

		if (logger.isDebugEnabled()) 
			logger.debug("enterUserFunction from call node " + n.getIndex() + " at " + n.getSourceLocation() 
					+ " to " + f+ " at " + f.getSourceLocation());
		
		State callee_state = caller_state.clone();

		// new stack frame
		callee_state.stackObjectLabels();
		callee_state.clearRegisters();

		Summarized s = new Summarized();
		Set<ObjectLabel> this_objs = call.prepareThis(caller_state, callee_state);
		if (call.isConstructorCall()) {
			// 13.2.2.1-2 create new object
			ObjectLabel new_obj = new ObjectLabel(n, Kind.OBJECT); // same as new object in determineThis
			s.addDefinitelySummarized(new_obj);
			// 13.2.2.3-5 provide [[Prototype]]
			Value prototype = caller_state.readPropertyDirect(Collections.singleton(obj_f), "prototype");
			prototype = UnknownValueResolver.getRealValue(prototype, caller_state);
			if (prototype.isMaybePrimitive()) 
				prototype = prototype.restrictToObject().joinObject(InitialStateBuilder.OBJECT_PROTOTYPE);
			callee_state.writeInternalPrototype(new_obj, prototype);
		}
		// 10.2.3 enter new execution context, 13.2.1 transfer parameters, 10.1.6/8 provide 'arguments' object
		ObjectLabel varobj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ACTIVATION); // better to use entry than invoke here
		callee_state.newObject(varobj); 
		s.addDefinitelySummarized(varobj);
		ObjectLabel argobj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ARGUMENTS);
		callee_state.newObject(argobj);
		s.addDefinitelySummarized(argobj);
		ScopeChain obj_f_sc = caller_state.readObjectScope(obj_f);
		ScopeChain sc = ScopeChain.make(Collections.singleton(varobj), obj_f_sc);

// XXX: (experiment with splitting states if 'this' holds multiple abstract objects)
//		for (ObjectLabel this_obj : this_objs) {
//			State copy = callee_state.clone(); // don't clone for the last one
//			// TODO: group all summary object labels?
		
		callee_state.setExecutionContext(new ExecutionContext(sc, singleton(varobj), newSet(this_objs)));
		callee_state.declareAndWriteVariable("arguments", Value.makeObject(argobj), true);
		callee_state.writeInternalPrototype(argobj, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
		callee_state.writePropertyWithAttributes(argobj, "callee", Value.makeObject(obj_f).setAttributes(true, false, false));
		int num_formals = f.getParameterNames().size();
		int num_actuals = call.getNumberOfArgs();
		boolean num_actuals_unknown = call.isUnknownNumberOfArgs();
		callee_state.writePropertyWithAttributes(argobj, "length",
				(num_actuals_unknown ? Value.makeAnyNumUInt() : Value.makeNum(num_actuals)).setAttributes(true, false, false));
		if (num_actuals_unknown)
			callee_state.writeProperty(Collections.singleton(argobj), Value.makeAnyStrUInt(), call.getUnknownArg().setAttributes(true, false, false), false, false);
		for (int i = 0; i < num_formals || (!num_actuals_unknown && i < num_actuals); i++) {
			Value v;
			if (num_actuals_unknown || i < num_actuals) {
				v = call.getArg(i).summarize(s);
				callee_state.writePropertyWithAttributes(argobj, Integer.toString(i), v.setAttributes(true, false, false));
			} else
				v = Value.makeUndef(); // 10.1.3
			if (i < num_formals)
				callee_state.declareAndWriteVariable(f.getParameterNames().get(i), v, true); // 10.1.3
		}
		// FIXME: properties of 'arguments' should be shared with the formal parameters (see 10.1.8 item 4) - easy solution that does not require Reference types?
		// (see comment at NodeTransfer/WriteVariable... - also needs the other way around...)

		CallContext caller_context = c.getCurrentContext();
		CallContext callee_context = new CallContext(callee_state, f, caller_context, null, null, c.getAnalysis().getSpecialArgs());
		c.propagateToFunctionEntry(new CallEdge<>(callee_state), n, caller_context, f, callee_context);

//			callee_state = copy;
//		}
	}

    /**
     * Determines the value of 'this' when entering a function (other than call/apply and other than constructor call to host function).
     * May have side-effects on callee_state.
     */
	public static Set<ObjectLabel> determineThis(AbstractNode n, 
			State caller_state, State callee_state, Solver.SolverInterface c, 
			boolean is_constructor_call, int base_reg) {
		Set<ObjectLabel> this_obj = newSet();
		if (is_constructor_call) {
			// 13.2.2.1-2 create new object
			ObjectLabel t = new ObjectLabel(n, Kind.OBJECT); // same as new object in enterUserFunction
			callee_state.newObject(t);
			this_obj.add(t);
		} else {
			// 10.2.3.3 and 11.2.3.6
			if (base_reg == AbstractNode.NO_VALUE) { // 11.2.1.5
				this_obj.add(InitialStateBuilder.GLOBAL);
			} else {
				// FIXME: see 10.2.3, 11.2.3 and 11.2.1.5 - is following call to toObjectLabels correct?
				Set<ObjectLabel> t = Conversion.toObjectLabels(callee_state, n, caller_state.readRegister(base_reg), c); // TODO: likely loss of precision if multiple object labels (or a summary object) as 'this' value
				// TODO: disable conversion warnings for this call to Conversion.toObjectLabels? (test/micro/test163.js)
				// 11.2.3 and 10.1.6: replace activation objects by the global object
				for (ObjectLabel objlabel : t)
					if (objlabel.getKind() == Kind.ACTIVATION)
						this_obj.add(InitialStateBuilder.GLOBAL);
					else
						this_obj.add(objlabel);
			}
		}
		return this_obj;
	}

    /**
     * Leaves a user-defined function.
     */
	public static void leaveUserFunction(Value returnval, boolean exceptional, Function f, State state, Solver.SolverInterface c,
			NodeAndContext<CallContext> specific_caller) {

		if (f.isMain()) { // TODO: also report uncaught exceptions and return immediately for event handlers
			final String msgkey = "Uncaught exception";
			AbstractNode n = f.getOrdinaryExit().getLastNode();
			if (exceptional) {
				returnval = UnknownValueResolver.getRealValue(returnval, state);
				TreeSet<SourceLocation> objs = new TreeSet<>(returnval.getObjectSourceLocations());
				if (!objs.isEmpty()) { // use object source locations
					c.getMonitoring().addMessage(n, Severity.LOW, msgkey, "Uncaught exception, constructed at " + objs); // TODO: give user-defined exceptions higher severity level?
				} else { // alternatively, use the primitive values in the message
					Value v = returnval.restrictToNotObject();
					c.getMonitoring().addMessage(n, Severity.LOW, msgkey, "Uncaught exception: " + v);
				}
			}
			return; // do nothing when leaving the main function
		}
		
		if (c.isScanning())
			return;

		if (logger.isDebugEnabled()) 
			logger.debug("leaveUserFunction from " + f + " at " + f.getSourceLocation());
	
		// collect garbage
		state.clearRegisters();
		state.writeRegister(0, returnval); // ensures that returnval is treated as live
		state.reduce();
		state.clearRegisters();

		if (specific_caller != null)
			FunctionCalls.leaveFunction(specific_caller, returnval, exceptional, f, state, state.readThis(), c);
		else {
			// try each call node that calls f with the current callee context
			CallGraph<State, CallContext, CallEdge<State>> cg = c.getAnalysisLatticeElement().getCallGraph();
			Set<NodeAndContext<CallContext>> es = cg.getSources(f.getEntry(), c.getCurrentContext());
			for (Iterator<NodeAndContext<CallContext>> i = es.iterator(); i.hasNext(); ) {
				NodeAndContext<CallContext> p = i.next();
				if (c.isCallEdgeCharged(p.getNode(), p.getContext(), f, c.getCurrentContext()))
					FunctionCalls.leaveFunction(p, returnval, exceptional, f, i.hasNext() ? state.clone() : state, state.readThis(), c);
			}
		}
	}
}
