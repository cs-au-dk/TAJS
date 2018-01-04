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
import dk.brics.tajs.lattice.PKeys;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.Message;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Decorator monitor that can be toggled on and off.
 */
public class TogglableMonitor implements IAnalysisMonitoring {

    private final IAnalysisMonitoring orig;

    private final DefaultAnalysisMonitoring noop;

    private IAnalysisMonitoring active;

    public TogglableMonitor(IAnalysisMonitoring togglable) {
        this.orig = togglable;
        this.noop = new DefaultAnalysisMonitoring(); // TODO: no need to use DefaultAnalysisMonitoring, use use a boolean instead
        this.active = orig;
    }

    @Override
    public void visitNodeTransferPre(AbstractNode n, State s) {
        active.visitNodeTransferPre(n, s);
    }

    @Override
    public void visitNodeTransferPost(AbstractNode n, State s) {
        active.visitNodeTransferPost(n, s);
    }

    @Override
    public void visitBlockTransferPre(BasicBlock b, State s) {
        active.visitBlockTransferPost(b, s);
    }

    @Override
    public void visitBlockTransferPost(BasicBlock b, State s) {
        active.visitBlockTransferPost(b, s);
    }

    @Override
    public void visitNewFlow(BasicBlock b, Context c, State s, String diff, String info) {
        active.visitNewFlow(b, c, s, diff, info);
    }

    @Override
    public void visitUnknownValueResolve(AbstractNode node, boolean partial, boolean scanning) {
        active.visitUnknownValueResolve(node, partial, scanning);
    }

    @Override
    public void visitRecoveryGraph(AbstractNode node, int size) {
        active.visitRecoveryGraph(node, size);
    }

    @Override
    public void addMessage(AbstractNode n, Message.Severity severity, String msg) {
        active.addMessage(n, severity, msg);
    }

    @Override
    public void visitFunction(Function f, Collection<State> entry_states) {
        active.visitFunction(f, entry_states);
    }

    @Override
    public void visitJoin() {
        active.visitJoin();
    }

    @Override
    public void addMessage(AbstractNode n, Message.Severity severity, String key, String msg) {
        active.addMessage(n, severity, key, msg);
    }

    @Override
    public boolean allowNextIteration() {
        return active.allowNextIteration();
    }

    @Override
    public void addMessageInfo(AbstractNode n, Message.Severity severity, String msg) {
        active.addMessageInfo(n, severity, msg);
    }

    @Override
    public void visitPhasePre(AnalysisPhase phase) {
        active.visitPhasePre(phase);
    }

    @Override
    public void visitPropagationPre(BlockAndContext<Context> from, BlockAndContext<Context> to) {
        active.visitPropagationPre(from, to);
    }

    @Override
    public void visitPhasePost(AnalysisPhase phase) {
        active.visitPhasePost(phase);
    }

    @Override
    public Set<Message> getMessages() {
        return active.getMessages();
    }

    @Override
    public void visitPropagationPost(BlockAndContext<Context> from, BlockAndContext<Context> to, boolean changed) {
        active.visitPropagationPost(from, to, changed);
    }

    @Override
    public Map<TypeCollector.VariableSummary, Value> getTypeInformation() {
        return active.getTypeInformation();
    }

    @Override
    public void visitNewObject(AbstractNode node, ObjectLabel label, State s) {
        active.visitNewObject(node, label, s);
    }

    @Override
    public void setSolverInterface(Solver.SolverInterface c) {
        active.setSolverInterface(c);
    }

    @Override
    public void visitRenameObject(AbstractNode node, ObjectLabel from, ObjectLabel to, State s) {
        active.visitRenameObject(node, from, to, s);
    }

    @Override
    public void visitCall(AbstractNode n, Value funval) {
        active.visitCall(n, funval);
    }

    @Override
    public void visitEvalCall(AbstractNode n, Value v) {
        active.visitEvalCall(n, v);
    }

    @Override
    public void visitIf(IfNode n, Value v) {
        active.visitIf(n, v);
    }

    @Override
    public void visitIn(AbstractNode n, boolean maybe_v2_object, boolean maybe_v2_nonobject) {
        active.visitIn(n, maybe_v2_object, maybe_v2_nonobject);
    }

    @Override
    public void visitInnerHTMLWrite(Node n, Value v) {
        active.visitInnerHTMLWrite(n, v);
    }

    @Override
    public void visitInstanceof(AbstractNode n, boolean maybe_v2_non_function, boolean maybe_v2_function, boolean maybe_v2_prototype_primitive, boolean maybe_v2_prototype_nonprimitive) {
        active.visitInstanceof(n, maybe_v2_non_function, maybe_v2_function, maybe_v2_prototype_primitive, maybe_v2_prototype_nonprimitive);
    }

    @Override
    public void visitNativeFunctionCall(AbstractNode n, HostObject hostobject, boolean num_actuals_unknown, int num_actuals, int min, int max) {
        active.visitNativeFunctionCall(n, hostobject, num_actuals_unknown, num_actuals, min, max);
    }

    @Override
    public void visitPropertyAccess(Node n, Value baseval) {
        active.visitPropertyAccess(n, baseval);
    }

    @Override
    public void visitPropertyRead(AbstractNode n, Set<ObjectLabel> objs, PKeys propertyname, State state, boolean check_unknown) {
        active.visitPropertyRead(n, objs, propertyname, state, check_unknown);
    }

    @Override
    public void visitPropertyWrite(Node n, Set<ObjectLabel> objs, PKeys propertyname) {
        active.visitPropertyWrite(n, objs, propertyname);
    }

    @Override
    public void visitRead(Node n, Value v, State state) {
        active.visitRead(n, v, state);
    }

    @Override
    public void visitReadNonThisVariable(ReadVariableNode n, Value v) {
        active.visitReadNonThisVariable(n, v);
    }

    @Override
    public void visitReadProperty(ReadPropertyNode n, Set<ObjectLabel> objlabels, PKeys propertyname, boolean maybe, State state, Value v, ObjectLabel global_objv) {
        active.visitReadProperty(n, objlabels, propertyname, maybe, state, v, global_objv);
    }

    @Override
    public void visitReadThis(ReadVariableNode n, Value v, State state, ObjectLabel global_obj) {
        active.visitReadThis(n, v, state, global_obj);
    }

    @Override
    public void visitReadVariable(ReadVariableNode n, Value v, State state) {
        active.visitReadVariable(n, v, state);
    }

    @Override
    public void visitUserFunctionCall(Function f, AbstractNode call, boolean constructor) {
        active.visitUserFunctionCall(f, call, constructor);
    }

    @Override
    public void visitVariableAsRead(AbstractNode n, String varname, Value v, State state) {
        active.visitVariableAsRead(n, varname, v, state);
    }

    @Override
    public void visitVariableOrProperty(String var, SourceLocation loc, Value value, Context context, State state) {
        active.visitVariableOrProperty(var, loc, value, context, state);
    }

    @Override
    public void visitNativeFunctionReturn(AbstractNode node, HostObject hostObject, Value result) {
        active.visitNativeFunctionReturn(node, hostObject, result);
    }

    @Override
    public void visitEventHandlerRegistration(AbstractNode node, Context context, Value handler) {
        active.visitEventHandlerRegistration(node, context, handler);
    }

    @Override
    public void visitIterationDone() {
        active.visitIterationDone();
    }

    public Toggler getController() {
        return new Toggler();
    }

    public class Toggler {

        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            active = enabled ? orig : noop;
        }
    }
}
