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

package dk.brics.tajs.monitoring;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.BlockAndContext;
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
    public void visitPhasePre(AnalysisPhase phase) {
    }

    @Override
    public void visitPhasePost(AnalysisPhase phase) {
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
    public void setSolverInterface(Solver.SolverInterface c) {
    }

    @Override
    public void visitBlockTransferPre(BasicBlock b, State s) {
    }

    @Override
    public void visitCall(AbstractNode n, Value funval) {
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
    public void visitBlockTransferPost(BasicBlock b, State state) {
    }

    @Override
    public void visitNativeFunctionCall(AbstractNode n, HostObject hostobject, boolean num_actuals_unknown, int num_actuals, int min, int max) {
    }

    @Override
    public void visitNewFlow(BasicBlock b, Context c, State s, String diff, String info) {
    }

    @Override
    public void visitNodeTransferPre(AbstractNode n, State s) {
    }

    @Override
    public void visitNodeTransferPost(AbstractNode n, State s) {
    }

    @Override
    public void visitPropertyAccess(Node n, Value baseval) {
    }

    @Override
    public void visitPropertyRead(AbstractNode n, Set<ObjectLabel> objs, Str propertystr, State state, boolean check_unknown) {
    }

    @Override
    public void visitPropertyWrite(Node n, Set<ObjectLabel> objs, Str propertystr) {
    }

    @Override
    public void visitRead(Node n, Value v, State state) {
    }

    @Override
    public void visitReadNonThisVariable(ReadVariableNode n, Value v) {
    }

    @Override
    public void visitReadProperty(ReadPropertyNode n, Set<ObjectLabel> objlabels, Str propertystr, boolean maybe, State state, Value v, ObjectLabel global_obj) {
    }

    @Override
    public void visitReadThis(ReadVariableNode n, Value v, State state, ObjectLabel global_obj) {
    }

    @Override
    public void visitReadVariable(ReadVariableNode n, Value v, State state) {
    }

    @Override
    public void visitRecoveryGraph(AbstractNode node, int size) {
    }

    @Override
    public void visitUnknownValueResolve(AbstractNode node, boolean partial, boolean scanning) {
    }

    @Override
    public void visitUserFunctionCall(Function f, AbstractNode call, boolean constructor) {
    }

    @Override
    public void visitVariableAsRead(AbstractNode n, String varname, Value v, State state) {
    }

    @Override
    public void visitVariableOrProperty(String var, SourceLocation loc, Value value, Context context, State state) {
    }

    @Override
    public void visitNativeFunctionReturn(AbstractNode node, HostObject hostObject, Value result) {
    }

    @Override
    public void visitEventHandlerRegistration(AbstractNode node, Context context, Value handler) {
    }

    @Override
    public void visitPropagationPre(BlockAndContext<Context> from, BlockAndContext<Context> to) {
    }

    @Override
    public void visitPropagationPost(BlockAndContext<Context> from, BlockAndContext<Context> to, boolean changed) {
    }

    @Override
    public void visitNewObject(AbstractNode node, ObjectLabel label, State s) {
    }

    @Override
    public void visitRenameObject(AbstractNode node, ObjectLabel from, ObjectLabel to, State s) {
    }

    @Override
    public void visitIterationDone() {
    }
}
