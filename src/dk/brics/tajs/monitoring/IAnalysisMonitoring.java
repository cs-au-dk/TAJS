/*
 * Copyright 2009-2016 Aarhus University
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

package dk.brics.tajs.monitoring;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.ISolverMonitoring;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.solver.Message.Severity;

import java.util.Map;
import java.util.Set;

/**
 * Extended monitoring interface.
 * <p>
 * Implementations of the interface should not have side effects on the analysis state before the scan phase begins.
 * <p>
 * In particular, <tt>UnknownValueResolver.getRealValue(v, state);</tt> should not be used before the scan phase.
 */
public interface IAnalysisMonitoring extends ISolverMonitoring<State, Context> {

    /**
     * Adds a message for the given node. If not in scan phase, nothing is done.
     * Uses Status.MAYBE.
     * Uses the message as key (must be a fixed string).
     */
    void addMessage(AbstractNode n, Severity severity, String msg);

    /**
     * Adds a message for the given node. If not in scan phase, nothing is done.
     * Uses Status.MAYBE.
     * The key must be a fixed string.
     */
    void addMessage(AbstractNode n, Severity severity, String key, String msg);

    /**
     * Adds a message for the given node. If not in scan phase, nothing is done.
     * Uses Status.INFO.
     * Uses the message as key (must be a fixed string).
     */
    void addMessageInfo(AbstractNode n, Severity severity, String msg);

    /**
     * Marks the beginning of a phase.
     */
    void beginPhase(AnalysisPhase phase);

    /**
     * Marks the end of a phase.
     */
    void endPhase(AnalysisPhase phase);

    /**
     * Returns the collected messages.
     * (Used by the Eclipse plugin.)
     */
    @SuppressWarnings("unused")
    Set<Message> getMessages();

    /**
     * Returns the collected type information.
     * (Used by the Eclipse plugin.)
     */
    @SuppressWarnings("unused")
    Map<TypeCollector.VariableSummary, Value> getTypeInformation();

    /**
     * Registers the final callgraph of the analysis
     */
    void setCallGraph(CallGraph<State, Context, CallEdge> callGraph);

    /**
     * Registers the flowgraph which will be used by the analysis
     */
    void setFlowgraph(FlowGraph fg);

    /**
     * Registers a potential call/construct to a non-function value.
     *
     * @param n                  node responsible for the call
     * @param maybe_non_function if set, this call may involve a non-function value
     * @param maybe_function     if set, this call may involve a function value
     */
    void visitCall(AbstractNode n, boolean maybe_non_function, boolean maybe_function);

    /**
     * Registers a call to eval.
     *
     * @param n node that may call eval
     * @param v value being eval'ed
     */
    void visitEvalCall(AbstractNode n, Value v);

    /**
     * Checks whether the branch condition is always true or always false.
     *
     * @param n if node
     * @param v the boolean value
     */
    void visitIf(IfNode n, Value v);

    /**
     * Checks whether the 'in' operation may fail.
     *
     * @param n                  node performing the operation
     * @param maybe_v2_object    if the second parameter may be an object value
     * @param maybe_v2_nonobject if the second parameter may be a non-object value
     */
    void visitIn(AbstractNode n, boolean maybe_v2_object, boolean maybe_v2_nonobject);

    /**
     * Registers a write to innerHTML.
     *
     * @param n node where the write occurs
     * @param v value being written
     */
    void visitInnerHTMLWrite(Node n, Value v);

    /**
     * Checks whether the 'instanceof' operation may fail.
     *
     * @param n                               node performing the operation
     * @param maybe_v2_non_function           set if the second parameter may be a non-function value
     * @param maybe_v2_function               set if the second parameter may be a function value
     * @param maybe_v2_prototype_primitive    set if the prototype property of the second parameter may be a primitive value
     * @param maybe_v2_prototype_nonprimitive set if the prototype property of the second parameter may be an object value
     */
    void visitInstanceof(AbstractNode n,
                         boolean maybe_v2_non_function, boolean maybe_v2_function,
                         boolean maybe_v2_prototype_primitive, boolean maybe_v2_prototype_nonprimitive);

    /**
     * Checks the number of parameters for a call to a native function.
     *
     * @param n                   node responsible for the call
     * @param hostobject          the native function being called
     * @param num_actuals_unknown if set, the number of actuals is unknown
     * @param num_actuals         number of actuals (if num_actuals_unknown is not set)
     * @param min                 minimum number of parameters expected
     * @param max                 maximum number of paramaters expected (-1 for any number)
     */
    void visitNativeFunctionCall(AbstractNode n, HostObject hostobject, boolean num_actuals_unknown, int num_actuals, int min, int max);

    /**
     * Checks whether the property access operation may dereference null or undefined.
     *
     * @param n       operation that accesses a property
     * @param baseval base value for the access
     */
    void visitPropertyAccess(Node n, Value baseval);

    /**
     * Warns about reads from unknown properties;
     * also registers a read operation on abstract objects.
     * Properties named 'length' on array objects are ignored.
     *
     * @param n             the node responsible for the read
     * @param objs          the objects being read from
     * @param propertystr   description of the property name
     * @param check_unknown if set, warn about reads from unknown properties
     */
    void visitPropertyRead(AbstractNode n, Set<ObjectLabel> objs, Str propertystr, State state, boolean check_unknown);

    /**
     * Warns about writes to unknown properties;
     * also registers a write operation on abstract objects.
     * Properties named 'length' on array objects are ignored.
     * Writes to the arguments object are also ignored.
     *
     * @param n           the node responsible for the write
     * @param objs        the objects being written to
     * @param propertystr description of the property name
     */
    void visitPropertyWrite(Node n, Set<ObjectLabel> objs, Str propertystr);

    /**
     * Records type information about a var/prop read.
     */
    void visitRead(Node n, Value v, State state);

    /**
     * Checks whether an absent variable is read.
     *
     * @param n (non-this) read variable operation
     * @param v the value being read
     */
    void visitReadNonThisVariable(ReadVariableNode n, Value v);

    /**
     * Checks whether the property read operation accesses an absent property and whether the operation returns null/undefined.
     *
     * @param n           read property operation
     * @param objlabels   objects being read from
     * @param propertystr description of the property name
     * @param maybe       if there may be more than one value
     * @param state       current abstract state
     * @param v           property value with attributes
     */
    void visitReadProperty(ReadPropertyNode n, Set<ObjectLabel> objlabels, Str propertystr, boolean maybe, State state, Value v); // TODO these checks should be done for CallNodes as well!

    /**
     * Checks whether the read of 'this' yields the global object.
     *
     * @param n     (this) read variable operation
     * @param v     the value being read
     * @param state current abstract state
     */
    void visitReadThis(ReadVariableNode n, Value v, State state, ObjectLabel global_obj);

    /**
     * Checks whether the variable read yields null/undefined.
     * Variables named 'undefined' are ignored.
     *
     * @param n read variable operation
     * @param v the value being read
     */
    void visitReadVariable(ReadVariableNode n, Value v, State state);

    /**
     * Checks whether the function is invoked both as a constructor (with 'new') and as a function/method (without 'new').
     *
     * @param f           function being called
     * @param call        node responsible for the call
     * @param constructor if set, the call uses 'new'
     */
    void visitUserFunctionCall(Function f, AbstractNode call, boolean constructor);

    /**
     * Registers that the given variable is read; also checks for suspicious type mixings.
     *
     * @param n (non-this) read variable operation
     * @param v value being read
     */
    void visitVariableAsRead(ReadVariableNode n, Value v, State state);

    /**
     * Registers the name, location, and value of a variable being read or written.
     */
    void visitVariableOrProperty(String var, SourceLocation loc, Value value, Context context, State state);

    /**
     * Registers the return value of a native function call.
     */
    void visitNativeFunctionReturn(AbstractNode node, HostObject hostObject, Value result);
}
