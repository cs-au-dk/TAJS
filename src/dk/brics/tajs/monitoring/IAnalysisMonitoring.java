/*
 * Copyright 2009-2018 Aarhus University
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

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ILatticeMonitoring;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.PKeys;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.solver.Message.Severity;

import java.util.Map;
import java.util.Set;

/**
 * Monitoring interface.
 * <p>
 * This interface contains callbacks for solver-specific, lattice-specific, and analysis-specific operations.
 * <p>
 * Implementations of the interface should not have side effects on the analysis state before the scan phase begins.
 * In particular, <tt>UnknownValueResolver.getRealValue(v, state)</tt> should not be used before the scan phase.
 */
public interface IAnalysisMonitoring extends ILatticeMonitoring {

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
     * Sets the solver interface which will be used by the analysis.
     */
    void setSolverInterface(Solver.SolverInterface s);

    /**
     * Invoked at the beginning of a phase.
     */
    void visitPhasePre(AnalysisPhase phase);

    /**
     * Invoked at the end of a phase.
     */
    void visitPhasePost(AnalysisPhase phase);

    /**
     * Invoked when a function call occurs.
     * @param n      node responsible for the call
     * @param funval the function value
     */
    void visitCall(AbstractNode n, Value funval);

    /**
     * Invoked when a call to eval occurs.
     *
     * @param n node that may call eval
     * @param v value being eval'ed
     */
    void visitEvalCall(AbstractNode n, Value v);

    /**
     * Invoked when a IfNode is processed.
     *
     * @param n if node
     * @param v the boolean value
     */
    void visitIf(IfNode n, Value v);

    /**
     * Invoked when an 'in' operation is processed.
     *
     * @param n                  node performing the operation
     * @param maybe_v2_object    if the second parameter may be an object value
     * @param maybe_v2_nonobject if the second parameter may be a non-object value
     */
    void visitIn(AbstractNode n, boolean maybe_v2_object, boolean maybe_v2_nonobject);

    /**
     * Invoked when a write to innerHTML occurs.
     *
     * @param n node where the write occurs
     * @param v value being written
     */
    void visitInnerHTMLWrite(Node n, Value v);

    /**
     * Invoked when an 'instanceof' operation is processed.
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
     * Invoked when a call to a native function occurs.
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
     * Invoked when a property access occurs.
     *
     * @param n       operation that accesses a property
     * @param baseval base value for the access
     */
    void visitPropertyAccess(Node n, Value baseval);

    /**
     * Invoked when a property read operation cccurs.
     *
     * @param n             the node responsible for the read
     * @param objs          the objects being read from
     * @param propertyname  description of the property name
     * @param check_unknown if set, warn about reads from unknown properties
     */
    void visitPropertyRead(AbstractNode n, Set<ObjectLabel> objs, PKeys propertyname, State state, boolean check_unknown);

    /**
     * Invoked when a property write operation cccurs.
     *
     * @param n            the node responsible for the write
     * @param objs         the objects being written to
     * @param propertyname description of the property name
     */
    void visitPropertyWrite(Node n, Set<ObjectLabel> objs, PKeys propertyname);

    /**
     * Invoked when a variable or property read operation cccurs.
     */
    void visitRead(Node n, Value v, State state);

    /**
     * Invoked when a non-this variable read operation cccurs.
     *
     * @param n (non-this) read variable operation
     * @param v the value being read
     */
    void visitReadNonThisVariable(ReadVariableNode n, Value v);

    /**
     * Invoked when a property read operation cccurs.
     *
     * @param n            read property operation
     * @param objlabels    objects being read from
     * @param propertyname description of the property name
     * @param maybe        if there may be more than one value
     * @param state        current abstract state
     * @param v            property value with attributes
     * @param global_obj   the global object
     */
    void visitReadProperty(ReadPropertyNode n, Set<ObjectLabel> objlabels, PKeys propertyname, boolean maybe, State state, Value v, ObjectLabel global_obj); // TODO these checks should be done for CallNodes as well!
    // TODO: we have both visitReadProperty and visitPropertyRead - merge them, or give them better names?

    /**
     * Invoked when 'this' is read.
     *
     * @param n     (this) read variable operation
     * @param v     the value being read
     * @param state current abstract state
     */
    void visitReadThis(ReadVariableNode n, Value v, State state, ObjectLabel global_obj);

    /**
     * Invoked when a variable read operation occurs.
     * Variables named 'undefined' are ignored.
     *
     * @param n read variable operation
     * @param v the value being read
     */
    void visitReadVariable(ReadVariableNode n, Value v, State state); // TODO: why "Variables named 'undefined' are ignored" (see javadoc)? ignored by the caller or the implementation? (also in implementation in Monitoring)
    // TODO: merge visitReadVariable, visitReadThis, visitReadNonThisVariable, visitVariableAsRead?

    /**
     * Invoked when a user-function call occurs.
     *
     * @param f           function being called
     * @param call        node responsible for the call
     * @param constructor if set, the call uses 'new'
     */
    void visitUserFunctionCall(Function f, AbstractNode call, boolean constructor);
    // TODO: merge visitUserFunctionCall, visitNativeFunctionCall, visitEvalCall, visitCall?

    /**
     * Invoked when a variable read operation occurs.
     *
     * @param n       (non-this) read variable operation
     * @param varname the name of the variable
     * @param v       value being read
     */
    void visitVariableAsRead(AbstractNode n, String varname, Value v, State state);

    /**
     * Invoked when a variable or property is read or written.
     */
    void visitVariableOrProperty(String var, SourceLocation loc, Value value, Context context, State state);
    // TODO: merge with other variable/property read/write methods?

    /**
     * Invoked when returning from a native function call.
     */
    void visitNativeFunctionReturn(AbstractNode node, HostObject hostObject, Value result);

    /**
     * Invoked when an event handler is registered.
     */
    void visitEventHandlerRegistration(AbstractNode node, Context context, Value handler);
}
