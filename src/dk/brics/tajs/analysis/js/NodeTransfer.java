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

import java.util.Map;
import java.util.Set;

import dk.brics.tajs.analysis.CallContext;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMEventLoop;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.IReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.AssumeNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.CallConversionNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.CatchNode;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.DeletePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.EndForInNode;
import dk.brics.tajs.flowgraph.jsnodes.EnterWithNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.EventEntryNode;
import dk.brics.tajs.flowgraph.jsnodes.ExceptionalReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.HasNextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.LeaveWithNode;
import dk.brics.tajs.flowgraph.jsnodes.NewObjectNode;
import dk.brics.tajs.flowgraph.jsnodes.NextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.NodeVisitor;
import dk.brics.tajs.flowgraph.jsnodes.NopNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.ReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.ThrowNode;
import dk.brics.tajs.flowgraph.jsnodes.TypeofNode;
import dk.brics.tajs.flowgraph.jsnodes.UnaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.lattice.BlockState;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.AnalysisException;

/**
 * Transfer for flow graph nodes.
 */
public class NodeTransfer implements NodeVisitor<State> {

    private Solver.SolverInterface c;
    
    private Monitoring<State,CallContext,CallEdge<State>> m;

    /**
     * Constructs a new TransferFunctions object.
     */
    public NodeTransfer() {
    }

    /**
     * Initializes the connection to the solver.
     */
    public void setSolverInterface(Solver.SolverInterface c) {
        this.c = c;
        m = c.getMonitoring();
    }

    /**
     * Transfer ordinary and exceptional return for the given call node and callee function.
     */
    public Void transferReturn(AbstractNode call_node, Function callee, CallContext caller_context, CallContext callee_context) {
        BasicBlock ordinary_exit = callee.getOrdinaryExit();
        BasicBlock exceptional_exit = callee.getExceptionalExit();
        State ordinary_exit_state = c.getAnalysisLatticeElement().getState(ordinary_exit, callee_context);
        State exceptional_exit_state = c.getAnalysisLatticeElement().getState(exceptional_exit, callee_context);
        NodeAndContext<CallContext> caller = new NodeAndContext<>(call_node, caller_context);
        if (ordinary_exit_state != null) {
            if (ordinary_exit.getFirstNode() instanceof IReturnNode) {
            	IReturnNode returnNode = (IReturnNode) ordinary_exit.getFirstNode();
                transferReturn(returnNode.getReturnValueRegister(), ordinary_exit, ordinary_exit_state.clone(), caller);
            } 
        }
        if (exceptional_exit_state != null) {
        	transferExceptionalReturn(exceptional_exit, exceptional_exit_state.clone(), caller);    
        }
        return null;
    }

    /**
     * 12.3 empty statement.
     */
    @Override
    public void visit(NopNode n, State state) {
        // do nothing
    }

    /**
     * 12.2 variable declaration.
     */
    @Override
    public void visit(DeclareVariableNode n, State state) {
    	state.declareAndWriteVariable(n.getVariableName(), Value.makeUndef(), false);
    }

    /**
     * 11.13 and 7.8 assignment with literal.
     */
    @Override
    public void visit(ConstantNode n, State state) {
        Value v;
        switch (n.getType()) {
            case NULL:
                v = Value.makeNull();
                break;
            case UNDEFINED:
                v = Value.makeUndef();
                break;
            case BOOLEAN:
                v = Value.makeBool(n.getBoolean());
                break;
            case NUMBER:
                v = Value.makeNum(n.getNumber());
                break;
            case STRING:
                v = Value.makeStr(n.getString());
                break;
            default:
                throw new AnalysisException();
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.1.5 object initializer.
     */
    @Override
    public void visit(NewObjectNode n, State state) {
        ObjectLabel objlabel = new ObjectLabel(n, Kind.OBJECT);
        state.newObject(objlabel);
        state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), Value.makeObject(objlabel));
    }

    /**
     * 11.13 and 11.4 assignment with unary operator.
     */
    @Override
    public void visit(UnaryOperatorNode n, State state) {
        Value arg = state.readRegister(n.getArgRegister());
        arg = UnknownValueResolver.getRealValue(arg, state);
        Value v;
        switch (n.getOperator()) {
            case COMPLEMENT:
                v = Operators.complement(arg, n.getArgRegister(), c);
                break;
            case MINUS:
                v = Operators.uminus(arg, n.getArgRegister(), c);
                break;
            case NOT:
                v = Operators.not(arg, c);
                break;
            case PLUS:
                v = Operators.uplus(arg, n.getArgRegister(), c);
                break;
            default:
                throw new AnalysisException();
        }
        if (v.isNotPresent() && !Options.isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.13 and 11.5/6/7/8 assignment with binary operator.
     */
    @Override
    public void visit(BinaryOperatorNode n, State state) {
        Value arg1 = state.readRegister(n.getArg1Register());
        Value arg2 = state.readRegister(n.getArg2Register());
        arg1 = UnknownValueResolver.getRealValue(arg1, state);
        arg2 = UnknownValueResolver.getRealValue(arg2, state);
        Value v;
        switch (n.getOperator()) {
            case ADD:
                v = Operators.add(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c); // TODO: test07.js could improve messages if keeping conversions of the two args separate
                break;
            case AND:
                v = Operators.and(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case DIV:
                v = Operators.div(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case EQ:
                v = Operators.eq(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case GE:
                v = Operators.ge(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case GT:
                v = Operators.gt(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case IN:
                v = Operators.in(state, arg1, n.getArg1Register(), arg2, c);
                break;
            case INSTANCEOF:
                v = Operators.instof(state, arg1, arg2, c);
                break;
            case LE:
                v = Operators.le(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case LT:
                v = Operators.lt(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case MUL:
                v = Operators.mul(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case NE:
                v = Operators.neq(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case OR:
                v = Operators.or(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case REM:
                v = Operators.rem(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case SEQ:
                v = Operators.stricteq(arg1, arg2, c);
                break;
            case SHL:
                v = Operators.shl(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case SHR:
                v = Operators.shr(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case SNE:
                v = Operators.strictneq(arg1, arg2, c);
                break;
            case SUB:
                v = Operators.sub(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case USHR:
                v = Operators.ushr(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            case XOR:
                v = Operators.xor(arg1, n.getArg1Register(), arg2, n.getArg2Register(), c);
                break;
            default:
                throw new AnalysisException();
        }
        /* From ES5, Annex D:
         ECMAScript generally uses a left to right evaluation order, however the Edition 3 specification 
         language for the > and <= operators resulted in a partial right to left order. The specification 
         has been corrected for these operators such that it now specifies a full left to right evaluation 
         order. However, this change of order is potentially observable if side-effects occur during the 
         evaluation process.
         */ // TODO: ES3 vs. ES5 evaluation order of > and <= (determined by flow graph builder)
        if (v.isNotPresent() && !Options.isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.1.2 assignment with right-hand-side identifier reference.
     */
    @Override
    public void visit(ReadVariableNode n, State state) {
        String varname = n.getVariableName();
        Value v;
        if (varname.equals("this")) {
        	// 11.1.1 read 'this' from the execution context
            v = state.readThis();
            m.visitReadThis(n, v, state, InitialStateBuilder.GLOBAL);
        } else { // ordinary variable
            int result_base_reg = n.getResultBaseRegister();
        	Set<ObjectLabel> base_objs = null;
        	if (c.isScanning() || result_base_reg != AbstractNode.NO_VALUE)
        		base_objs = newSet();	
            v = state.readVariable(varname, base_objs);
           	m.visitPropertyRead(n, base_objs, Value.makeTemporaryStr(varname), state);
           	m.visitVariableAsRead(n, v);
            m.visitVariableOrProperty(varname, n.getSourceLocation(), v);
            m.visitReadNonThisVariable(n, v);
            if (v.isMaybeAbsent()) {
                Exceptions.throwReferenceError(state, c);
                if (v.isNotPresent() && !Options.isPropagateDeadFlow()) {
                    state.setToNone();
                    return;
                }
            }
            if (result_base_reg != AbstractNode.NO_VALUE)
            	state.writeRegister(result_base_reg, Value.makeObject(base_objs)); // see 10.1.4
            m.visitRead(n, c.getCurrentContext(), v);
       		m.visitReadVariable(n, v, state); // TODO: combine some of these m.visitXYZ methods?
        }
        if (v.isNotPresent() && !Options.isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v.restrictToNotAbsent());
    }

    /**
     * 11.13 and 11.1.2 assignment with left-hand-side identifier reference.
     */
    @Override
    public void visit(WriteVariableNode n, State state) {
        Value v = state.readRegister(n.getValueRegister());
        m.visitWriteVariable(n, v, state);
        Set<ObjectLabel> objs = state.writeVariable(n.getVariableName(), v, true);
        Function f = n.getBlock().getFunction();
        if (f.getParameterNames().contains(n.getVariableName())) { // XXX: review
            ObjectLabel arguments_obj = new ObjectLabel(f.getEntry().getFirstNode(), Kind.ARGUMENTS);
            state.writeProperty(arguments_obj, Integer.toString(f.getParameterNames().indexOf(n.getVariableName())), v);
        }
       	m.visitPropertyWrite(n, objs, Value.makeTemporaryStr(n.getVariableName()));
       	m.visitVariableOrProperty(n.getVariableName(), n.getSourceLocation(), v);
    }

    /**
     * 11.2.1 assignment with right-hand-side property accessor.
     */
    @Override
    public void visit(ReadPropertyNode n, State state) {
    	// get the base value, coerce with ToObject
        Value baseval = state.readRegister(n.getBaseRegister());
        baseval = UnknownValueResolver.getRealValue(baseval, state);
        m.visitPropertyAccess(n, baseval);
        Set<ObjectLabel> objlabels = Conversion.toObjectLabels(state, n, baseval, c);
        if (objlabels.isEmpty() && !Options.isPropagateDeadFlow()) {
        	state.setToNone();
        	return;
        }
        state.writeRegister(n.getBaseRegister(), Value.makeObject(objlabels)); // if null/undefined, an exception would have been thrown via toObjectLabels
        // get the property name value, separate the undefined/null/NaN components, coerce with ToString
        Value propertyval;
        int propertyreg;
        if (n.isPropertyFixed()) {
            propertyreg = AbstractNode.NO_VALUE;
            propertyval = Value.makeStr(n.getPropertyString());
        } else {
            propertyreg = n.getPropertyRegister();
            propertyval = state.readRegister(propertyreg);
            propertyval = UnknownValueResolver.getRealValue(propertyval, state);
        }
        boolean maybe_undef = propertyval.isMaybeUndef();
        boolean maybe_null = propertyval.isMaybeNull();
        boolean maybe_nan = propertyval.isMaybeNaN();
        propertyval = propertyval.restrictToNotNullNotUndef().restrictToNotNaN();
        Str propertystr = Conversion.toString(propertyval, propertyreg, c);
        // read the object property value, as fixed property name or unknown property name, and separately for "undefined"/"null"/"NaN"
        Value v;
        boolean read_undefined = false;
        boolean read_null = false;
        boolean read_nan = false;
        if (propertystr.isMaybeSingleStr()) {
            String propertyname = propertystr.getStr();
            m.visitReadProperty(n, objlabels, propertystr, maybe_undef || maybe_null || maybe_nan, state);
            v = state.readPropertyValue(objlabels, propertyname);
            m.visitPropertyRead(n, objlabels, propertystr, state);
        } else if (!propertystr.isNotStr()) {
        	m.visitReadProperty(n, objlabels, propertystr, true, state);
        	m.visitPropertyRead(n, objlabels, propertystr, state);
            v = state.readPropertyValue(objlabels, propertystr);
        	read_undefined = propertystr.isMaybeStr("undefined");
        	read_null = propertystr.isMaybeStr("null");
        	read_nan = propertystr.isMaybeStr("NaN");
        } else
            v = Value.makeNone();
        if (maybe_undef && !read_undefined) {
        	m.visitReadProperty(n, objlabels, Value.makeTemporaryStr("undefined"), true, state);
        	v = UnknownValueResolver.join(v, state.readPropertyValue(objlabels, "undefined"), state);
        }
        if (maybe_null && !read_null) {
        	m.visitReadProperty(n, objlabels, Value.makeTemporaryStr("null"), true, state);
        	v = UnknownValueResolver.join(v, state.readPropertyValue(objlabels, "null"), state);
        }
        if (maybe_nan && !read_nan) {
        	m.visitReadProperty(n, objlabels, Value.makeTemporaryStr("NaN"), true, state);
        	v = UnknownValueResolver.join(v, state.readPropertyValue(objlabels, "NaN"), state);
        }
        m.visitVariableOrProperty(n.getPropertyString(), n.getSourceLocation(), v);
        m.visitRead(n, c.getCurrentContext(), v);
        if (v.isNotPresent() && !Options.isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        // store the resulting value
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.2.1 assignment with left-hand-side property accessor.
     */
    @Override
    public void visit(WritePropertyNode n, State state) {
    	// get the base value, coerce with ToObject
        Value baseval = state.readRegister(n.getBaseRegister());
        baseval = UnknownValueResolver.getRealValue(baseval, state);
        m.visitPropertyAccess(n, baseval);
        Set<ObjectLabel> objlabels = Conversion.toObjectLabels(state, n, baseval, c);
        if (objlabels.isEmpty() && !Options.isPropagateDeadFlow()) {
        	state.setToNone();
        	return;
        }
        state.writeRegister(n.getBaseRegister(), Value.makeObject(objlabels)); // if null/undefined, an exception would have been thrown via toObjectLabels
        // get the property name value, separate the undefined/null/NaN components, coerce with ToString
        Value propertyval;
        int propertyreg;
        if (n.isPropertyFixed()) {
        	propertyreg = AbstractNode.NO_VALUE;
            propertyval = Value.makeStr(n.getPropertyString());
        } else {
            propertyreg = n.getPropertyRegister();
            propertyval = state.readRegister(propertyreg);
            propertyval = UnknownValueResolver.getRealValue(propertyval, state);
        }
        boolean maybe_undef = propertyval.isMaybeUndef();
        boolean maybe_null = propertyval.isMaybeNull();
        boolean maybe_nan = propertyval.isMaybeNaN();
        propertyval = propertyval.restrictToNotNullNotUndef().restrictToNotNaN();
        Str propertystr = Conversion.toString(propertyval, propertyreg, c);
        // get the value to be written
        Value v = state.readRegister(n.getValueRegister());
        NativeFunctions.updateArrayLength(n, state, objlabels, propertystr, v, n.getValueRegister(), c);
        // write the object property value, as fixed property name or unknown property name, and separately for "undefined"/"null"/"NaN"
    	state.writeProperty(objlabels, propertystr, v, true, maybe_undef || maybe_null || maybe_nan);
        if (maybe_undef && !propertystr.isMaybeStr("undefined"))
            state.writeProperty(objlabels, Value.makeTemporaryStr("undefined"), v, true, !propertystr.isNotStr());
        if (maybe_null && !propertystr.isMaybeStr("null"))
            state.writeProperty(objlabels, Value.makeTemporaryStr("null"), v, true, !propertystr.isNotStr());
        if (maybe_nan && !propertystr.isMaybeStr("NaN"))
            state.writeProperty(objlabels, Value.makeTemporaryStr("NaN"), v, true, !propertystr.isNotStr());
       	m.visitPropertyWrite(n, objlabels, propertystr); // TODO: more monitoring around here?
        if (Options.isEvalStatistics()
                && propertystr.getStr() != null
                && propertystr.getStr().toLowerCase().equals("innerhtml")) { // TODO: why toLowerCase?
            m.visitInnerHTMLWrite(n, v);
        }     
        m.visitVariableOrProperty(n.getPropertyString(), n.getSourceLocation(), v);
    }

    /**
     * 11.13 and 11.4.1 assignment with 'delete' operator.
     */
    @Override
    public void visit(DeletePropertyNode n, State state) {
        Value v;
        if (n.isVariable()) {
            v = state.deleteVariable(n.getVariableName());
           	m.visitVariableOrProperty(n.getVariableName(), n.getSourceLocation(), v);
        } else {
            Value baseval = state.readRegister(n.getBaseRegister());
            baseval = UnknownValueResolver.getRealValue(baseval, state);
            m.visitPropertyAccess(n, baseval);
            if (baseval.isMaybeNull() || baseval.isMaybeUndef()) {
                Exceptions.throwTypeError(state, c);
                if (baseval.isNullOrUndef() && !Options.isPropagateDeadFlow()) {
                    state.setToNone();
                    return;
                }
            }
            baseval = baseval.restrictToNotNullNotUndef();
            state.writeRegister(n.getBaseRegister(), baseval);
            Set<ObjectLabel> objlabels = Conversion.toObjectLabels(state, n, baseval, c);
            if (objlabels.isEmpty() && !Options.isPropagateDeadFlow()) {
            	state.setToNone();
            	return;
            }
            Value propertyval = state.readRegister(n.getPropertyRegister());
            Str propertystr = Conversion.toString(propertyval, n.getPropertyRegister(), c);
            v = state.deleteProperty(objlabels, propertystr);
        }
        if (v.isNotPresent() && !Options.isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v);
    }

    /**
     * 11.13 and 11.4.3 assignment with 'typeof' operator.
     */
    @Override
    public void visit(TypeofNode n, State state) {
        Value v;
        if (n.isVariable()) {
            Value val = state.readVariable(n.getVariableName(), null); // XXX: should also count as a variable read in Monitoring?
            val = UnknownValueResolver.getRealValue(val, state);
            v = Operators.typeof(val, val.isMaybeAbsent());
            m.visitVariableOrProperty(n.getVariableName(), n.getSourceLocation(), v);
        } else {
            Value val = state.readRegister(n.getArgRegister());
            val = UnknownValueResolver.getRealValue(val, state);
            v = Operators.typeof(val, false);
        }
        if (v.isNotPresent() && !Options.isPropagateDeadFlow()) {
            state.setToNone();
            return;
        }
        if (n.getResultRegister() != AbstractNode.NO_VALUE)
            state.writeRegister(n.getResultRegister(), v);
    }

    /**
     * 12.5 and 12.6 'if'/iteration statement.
     */
    @Override
    public void visit(IfNode n, State state) {
        // do nothing (but see EdgeTransfer)
    	Value val = state.readRegister(n.getConditionRegister());
        val = UnknownValueResolver.getRealValue(val, state);
    	m.visitIf(n, Conversion.toBoolean(val));
    }

    /**
     * 13 function definition.
     */
    @Override
    public void visit(DeclareFunctionNode n, State state) {
        UserFunctionCalls.declareFunction(n, state);
    }

    /**
     * 11.2.2, 11.2.3, 13.2.1, and 13.2.2 'new' / function call.
     */
    @Override
    public void visit(final CallNode n, final State state) {
        FunctionCalls.callFunction(new FunctionCalls.CallInfo() {

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
                return state.readRegister(n.getFunctionRegister());
            }

            @Override
            public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
                return UserFunctionCalls.determineThis(n, caller_state, callee_state, c, n.isConstructorCall(), n.getBaseRegister());
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
            public int getBaseRegister() {
                return n.getBaseRegister();
            }
        }, state, c);
    }

    /**
     * Conversion using ToString or ValueOf.
     */
	@Override
	public void visit(CallConversionNode n, State state) {
        state.writeRegister(n.getResultRegister(), Value.makeAnyStr().joinAnyBool().joinAnyNum());
        // TODO: Implement CallConversionNode transfer function.
	}

	/**
     * 12.9 and 13.2.1 'return' statement.
     */
    @Override
    public void visit(ReturnNode n, State state) {
        transferReturn(n.getReturnValueRegister(), n.getBlock(), state, null);
    }

    /**
     * Transfer for a return statement.
     *
     * @param caller if non-null, only consider this caller
     */
    public void transferReturn(int valueReg, BasicBlock block, State state, NodeAndContext<CallContext> caller) {
        Value v;
        if (valueReg != AbstractNode.NO_VALUE)
            v = state.readRegister(valueReg);
        else
            v = Value.makeUndef();
        UserFunctionCalls.leaveUserFunction(v, false, block.getFunction(), state, c, caller);
    }

    /**
     * 13.2.1 exceptional return.
     */
    @Override
    public void visit(ExceptionalReturnNode n, State state) {
        transferExceptionalReturn(n.getBlock(), state, null);
    }

    /**
     * Transfer for an exceptional-return statement.
     *
     * @param caller if non-null, only consider this caller
     */
    public void transferExceptionalReturn(BasicBlock block, State state, NodeAndContext<CallContext> caller) {
        Value v = state.readRegister(AbstractNode.EXCEPTION_REG);
        state.removeRegister(AbstractNode.EXCEPTION_REG);
        UserFunctionCalls.leaveUserFunction(v, true, block.getFunction(), state, c, caller);
    }

    /**
     * 12.13 'throw' statement.
     */
    @Override
    public void visit(ThrowNode n, State state) {
        Value v = state.readRegister(n.getValueRegister());
        Exceptions.throwException(state.clone(), v, c, n, c.getCurrentContext());
        state.setToNone();
    }

    /**
     * 12.14 'catch' block.
     */
    @Override
    public void visit(CatchNode n, State state) {
        Value v = state.readRegister(AbstractNode.EXCEPTION_REG);
        state.removeRegister(AbstractNode.EXCEPTION_REG);
        if (n.getValueRegister() != AbstractNode.NO_VALUE) {
            state.writeRegister(n.getValueRegister(), v.makeExtendedScope());
        } else {
            ObjectLabel objlabel = new ObjectLabel(n, Kind.OBJECT);
            state.newObject(objlabel);
            state.writeInternalPrototype(objlabel, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
            state.writePropertyWithAttributes(objlabel, n.getVariableName(), v.setAttributes(false, true, false));
            state.writeRegister(n.getScopeObjRegister(), Value.makeObject(objlabel));
            /* From ES5, Annex D:
             In Edition 3, an object is created, as if by new Object()to serve as the scope for resolving 
             the name of the exception parameter passed to a catch clause of a try statement. If the actual 
             exception object is a function and it is called from within the catch clause, the scope object 
             will be passed as the this value of the call. The body of the function can then define new 
             properties on its this value and those property names become visible identifiers bindings 
             within the scope of the catch clause after the function returns. In Edition 5, when an 
             exception parameter is called as a function, undefined is passed as the this value.
             */ // TODO: use ES5 semantics of catch object?
        }
    }

    /**
     * 12.10 enter 'with' statement.
     */
    @Override
    public void visit(EnterWithNode n, State state) {
        Value v = state.readRegister(n.getObjectRegister());
        v = UnknownValueResolver.getRealValue(v, state);
        Set<ObjectLabel> objs = Conversion.toObjectLabels(state, n, v, c);
        if (objs.isEmpty() && !Options.isPropagateDeadFlow()) {
        	state.setToNone();
        	return;
        }
        state.pushScopeChain(objs);
    }

    /**
     * 12.10 leave 'with' statement.
     */
    @Override
    public void visit(LeaveWithNode n, State state) {
    	state.popScopeChain();
    }

    /**
     * 12.6.4 begin 'for-in' statement.
     */
    @Override
    public void visit(BeginForInNode n, State state) { // XXX: review
        Value v1 = state.readRegister(n.getObjectRegister());
        v1 = UnknownValueResolver.getRealValue(v1, state);
        Value v2 = Value.makeNone();
        Value write_value = Value.makeUndef();

        Set<ObjectLabel> objs = Conversion.toObjectLabels(state, n, v1, c);
        BlockState.Properties p = state.getEnumProperties(objs);
        // XXX: -states -polymorphic -test -debug test/micro/test176.js
        if (Options.isCorrelationTrackingEnabled() && p.getMaybe().equals(p.getDefinitely()) && !p.isArray() && !p.isNonArray() && !Options.isOldFlowgraphBuilderEnabled() && !Options.isContextSensitivityDisabled()) { // XXX: Work in progress. Disabled by default.
        	// XXX: the requirement p.getMaybe().equals(p.getDefinitely()) && !p.isArray() && !p.isNonArray() is probably too restrictive in many cases (and it only uses a small part of the information in BlockState.Properties)
        	Function f = n.getBlock().getFunction();
            BasicBlock succ = n.getBlock().getSingleSuccessor();
            CallContext curr_context = c.getCurrentContext();
            c.getMonitoring().visitReachableNode(n);
            // XXX: Hack: c.getAnalysis().getSpecialArgs().addContextSensitivity(f, singleton("tar"), state, c);

            for (String k : p.getDefinitely()) {
                CallContext context = new CallContext(state, f, curr_context, n, k, null); // CallContext only uses the values of 'this', so we can give it 'state'
                State new_state = state.clone();
//                new_state.setToUnknown(); // XXX: call trim instead, see next line
                new_state.trim(c.getAnalysisLatticeElement().getState(succ, context));
                new_state.clearEffects(); // XXX: yes, we should call clearEffects here
//                new_state.clearDeadRegisters(n.getBlock().getLiveRegisters()); // XXX: shouldn't harm to clear dead registers too
                new_state.writeRegister(n.getObjectRegister(), v1); // XXX: why this line?
                if (state.isRegisterDefined(1)) // Return register.
                    new_state.writeRegister(1, state.readRegister(1)); // XXX: why this line?
                new_state.writeRegister(n.getPropertyListRegister(), Value.makeStr(k).makeExtendedScope());
                state.writeRegister(n.getPropertyListRegister(), Value.makeStr(k).makeExtendedScope()); // XXX: move this line before the call to state.clone - then new_state.writeRegister(n.getPropertyListRegister(), Value.makeStr(k)) can be removed
                c.getAnalysisLatticeElement().getCallGraph().addTarget(n, curr_context, succ, context, new CallEdge<>(state.clone()), null);
                m.visitPropertyRead(n, objs, Value.makeStr(k), state);
                c.propagateToBasicBlock(new_state, succ, context);
            }
            if (!p.getDefinitely().isEmpty()) { // XXX: is this what it takes to handle empty objects?
                state.setToNone();
                return;
            }
        } else {
            if (p.isArray() || p.isNonArray()) {
                write_value = Value.makeAnyStr();
                v2 = write_value;
            } else {
                for (String k : p.getDefinitely())
                    v2 = v2.join(Value.makeStr(k));
                for (String k : p.getMaybe())
                    v2 = v2.join(Value.makeStr(k));
                if (!v2.isNone())
                    write_value = v2;
            }
            write_value = p.getDefinitely().isEmpty() ? write_value.joinUndef() : write_value;
        }
        state.writeRegister(n.getPropertyListRegister(), write_value.makeExtendedScope());
        if (!v2.isNone())
            m.visitPropertyRead(n, objs, v2, state);
        // TODO: improve transfer for BeginForInNode? - need path sensitivity or a stronger Str lattice?
        // currently just using AnyString for the property names	
        // store some kind of property name iterator in n.getPropertyQueueRegister()
        // note that deleted properties not yet visited should not be visited
    }

    /**
     * 12.6.4 get next property of 'for-in' statement.
     */
    @Override
    public void visit(NextPropertyNode n, State state) {
        Value inv = state.readRegister(n.getPropertyListRegister());
        state.writeRegister(n.getPropertyRegister(), inv.restrictToStr()); // restrictToStr to remove Undef and PROPERTYLIST 
    }

    /**
     * 12.6.4 check for more properties of 'for-in' statement.
     */
    @Override
    public void visit(HasNextPropertyNode n, State state) {
        Value v = UnknownValueResolver.getRealValue(state.readRegister(n.getPropertyListRegister()), state);
        Value res =	!v.isNotStr() ? Value.makeBool(true) : Value.makeNone();
        if (v.isMaybeUndef())
        	res = res.joinBool(false);
        state.writeRegister(n.getResultRegister(), res);
    }

    /**
     * 12.6.4 end of loop of 'for-in' statement.
     */
    @Override
    public void visit(EndForInNode n, State state) {
        if (c.getCurrentContext().isContextSensitiveOn(n.getStartNode())) {
            BasicBlock succ = n.getBlock().getSingleSuccessor();
            CallContext prev_context = c.getCurrentContext().peel();
            State destination_state = c.getAnalysisLatticeElement().getState(succ, prev_context);
            Value outval = Value.makeNone();
            boolean no_conflict = true;
            if (destination_state != null) {
                Value outreg = UnknownValueResolver.getRealValue(destination_state.readRegister(n.getStartNode().getPropertyListRegister()), destination_state);
                if (outreg.isMaybeSingleStr() || outreg.isMaybeFuzzyStr()) {
                    state.setToNone(); // Kill current flow, there was a merge conflict in a parallel context.
                    return;
                }
                Set<State> parallel_states = newSet();
                for (Map.Entry<CallContext, State> p : c.getAnalysisLatticeElement().getStates(n.getBlock()).entrySet()) {
                    if (p.getKey().isContextSensitiveOn(n.getStartNode()) && !p.getKey().equals(c.getCurrentContext()))
                        parallel_states.add(p.getValue());
                }
//                for (State other : parallel_states)
//                    no_conflict = no_conflict && state.mergeForInEnd(other, n.getLoopVariable());
                if (!no_conflict) { // Conflict.
                    outval = Value.makeAnyStr();
                    destination_state = c.getAnalysisLatticeElement().getState(n.getStartNode().getBlock(), prev_context);
                } else {
                    outval = Value.makeUndef();
                    destination_state = state;
                }
            } else
                destination_state = state;
            destination_state.writeRegister(n.getStartNode().getPropertyListRegister(), outval.joinUndef());
            if (no_conflict) {
                State calledge_state = c.getAnalysisLatticeElement().getCallGraph().getCallEdge(n.getStartNode(), prev_context, succ, c.getCurrentContext()).getState();
                destination_state.copyUnknowns(calledge_state);
            }
            c.propagateToBasicBlockDestructively(destination_state.clone(), succ, prev_context);
            state.setToNone();
        } else {
            Value curr = UnknownValueResolver.getRealValue(state.readRegister(n.getStartNode().getPropertyListRegister()), state);
            curr = curr.isNotUndef() ? curr.joinUndef() : curr;
            state.writeRegister(n.getStartNode().getPropertyListRegister(), curr.makeExtendedScope());
        }
    }

    /**
     * Assumption.
     */
    @Override
    public void visit(AssumeNode n, State state) {
        if (Options.isLocalPathSensitivityDisabled())
            return;
        switch (n.getKind()) {

            case VARIABLE_NON_NULL_UNDEF: {
                if (Options.isDOMEnabled() && n.getVariableName().equals("window")) // XXX: ?
                    return;
                Value v = state.readVariable(n.getVariableName(), null);
                v = UnknownValueResolver.getRealValue(v, state); // TODO: limits use of polymorphic values?
                v = v.restrictToNotNullNotUndef().restrictToNotAbsent();
                if (v.isNotPresent() && !Options.isPropagateDeadFlow())
                    state.setToNone();
                else
                    state.writeVariable(n.getVariableName(), v, false);
                break;
            }

            case PROPERTY_NON_NULL_UNDEF: {
                String propname;
                if (n.isPropertyFixed())
                    propname = n.getPropertyString();
                else {
                    Value propval = state.readRegister(n.getPropertyRegister());
                    propval = UnknownValueResolver.getRealValue(propval, state); // TODO: limits use of polymorphic values?
                    if (!propval.isMaybeSingleStr() || propval.isMaybeOtherThanStr())
                        break; // safe to do nothing here if it gets complicated
                    propname = propval.getStr();
                }
                Set<ObjectLabel> baseobjs = Conversion.toObjectLabels(state, n.getAccessNode(), state.readRegister(n.getBaseRegister()), null); // TODO: omitting side-effects here
                Value v = state.readPropertyWithAttributes(baseobjs, propname);
                if (v.isNotPresent() && !Options.isPropagateDeadFlow())
                    state.setToNone();
                else if (baseobjs.size() == 1 && baseobjs.iterator().next().isSingleton()) {
                    v = UnknownValueResolver.getRealValue(v, state); // TODO: limits use of polymorphic values?
                    v = v.restrictToNotNullNotUndef().restrictToNotAbsent();
                    state.writePropertyWithAttributes(baseobjs, propname, v, false);
                }
                break;
            }
        }
    }

    @Override
    public void visit(EventEntryNode n, State state) {
        // do nothing (see edge transfer)
    }

    @Override
    public void visit(EventDispatcherNode n, final State state) {
        if (Options.isSingleEventHandlerLoop()) {
            DOMEventLoop.singleNondeterministicEventLoop(n, state, c);
        } else {
            DOMEventLoop.multipleNondeterministicEventLoops(n, state, c);
        }
    }
}
