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

package dk.brics.tajs.monitoring;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.solver.Message;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Default, empty, implementation of {@link IAnalysisMonitoring}.
 * Enables easy creation of classes that only needs to implement a few methods.
 */
public class DefaultAnalysisMonitoring implements IAnalysisMonitoring {

    @Override
    public void addMessage(AbstractNode n, Message.Severity severity, String msg) {
    }

    @Override
    public void addMessage(AbstractNode n, Message.Severity severity, String key, String msg) {
    }

    @Override
    public void addMessageInfo(AbstractNode n, Message.Severity severity, String msg) {
    }

    @Override
    public boolean allowNextIteration() {
        return true;
    }

    @Override
    public void beginPhase(AnalysisPhase phase) {

    }

    @Override
    public void endPhase(AnalysisPhase phase) {

    }

    @Override
    public Set<Message> getMessages() {
        return null;
    }

    @Override
    public Map<TypeCollector.VariableSummary, Value> getTypeInformation() {
        return null;
    }

    @Override
    public void setCallGraph(CallGraph<State, Context, CallEdge> callGraph) {

    }

    @Override
    public void setFlowgraph(FlowGraph fg) {
    }

    @Override
    public void visitBlockTransfer(BasicBlock b, State s) {
    }

    @Override
    public void visitCall(AbstractNode n, boolean maybe_non_function, boolean maybe_function) {
    }

    @Override
    public void visitEvalCall(AbstractNode n, Value v) {
    }

    @Override
    public void visitFunction(Function f, Collection<State> entry_states) {
    }

    @Override
    public void visitIf(IfNode n, Value v) {
    }

    @Override
    public void visitIn(AbstractNode n, boolean maybe_v2_object, boolean maybe_v2_nonobject) {
    }

    @Override
    public void visitInnerHTMLWrite(Node n, Value v) {
    }

    @Override
    public void visitInstanceof(AbstractNode n, boolean maybe_v2_non_function, boolean maybe_v2_function, boolean maybe_v2_prototype_primitive, boolean maybe_v2_prototype_nonprimitive) {
    }

    @Override
    public void visitJoin() {
    }

    @Override
    public void visitPostBlockTransfer(BasicBlock b, State state) {
    }

    @Override
    public void visitNativeFunctionCall(AbstractNode n, HostObject hostobject, boolean num_actuals_unknown, int num_actuals, int min, int max) {
    }

    @Override
    public void visitNewFlow(BasicBlock b, Context c, State s, String diff, String info) {
    }

    @Override
    public void visitNodeTransfer(AbstractNode n) {
    }

    @Override
    public void visitPropertyAccess(Node n, Value baseval) {
    }

    @Override
    public void visitPropertyRead(Node n, Set<ObjectLabel> objs, Str propertystr, State state, boolean check_unknown) {
    }

    @Override
    public void visitPropertyWrite(Node n, Set<ObjectLabel> objs, Str propertystr) {
    }

    @Override
    public void visitReachableNode(AbstractNode n) {
    }

    @Override
    public void visitRead(Node n, Value v, State state) {
    }

    @Override
    public void visitReadNonThisVariable(ReadVariableNode n, Value v) {
    }

    @Override
    public void visitReadProperty(ReadPropertyNode n, Set<ObjectLabel> objlabels, Str propertystr, boolean maybe, State state) {
    }

    @Override
    public void visitReadThis(ReadVariableNode n, Value v, State state, ObjectLabel global_obj) {
    }

    @Override
    public void visitReadVariable(ReadVariableNode n, Value v, State state) {
    }

    @Override
    public void visitRecoveryGraph(int size) {
    }

    @Override
    public void visitUnknownValueResolve(boolean partial, boolean scanning) {
    }

    @Override
    public void visitUserFunctionCall(Function f, AbstractNode call, boolean constructor) {
    }

    @Override
    public void visitVariableAsRead(ReadVariableNode n, Value v, State state) {
    }

    @Override
    public void visitVariableOrProperty(String var, SourceLocation loc, Value value, Context context, State state) {
    }

    @Override
    public void visitWriteVariable(WriteVariableNode n, Value v, State state) {
    }
}
