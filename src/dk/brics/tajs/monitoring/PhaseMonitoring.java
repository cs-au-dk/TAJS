/*
 * Copyright 2009-2019 Aarhus University
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
import java.util.Set;

/**
 * Delegating monitor which has a monitor during the analysis-phase and another during the scan-phase.
 *
 * The monitors that are run before the scan phase should be side effect free wrt. the analysis state.
 *
 * Composing multiple monitors for each phase should be done with {@link CompositeMonitor}
 */
public class PhaseMonitoring<PreScanMonitorType extends IAnalysisMonitoring, ScanMonitorType extends IAnalysisMonitoring> implements IAnalysisMonitoring {

    private final PreScanMonitorType preScanMonitor;

    private final ScanMonitorType scanMonitor;

    private IAnalysisMonitoring activeMonitor;

    public PhaseMonitoring(PreScanMonitorType preScanMonitor, ScanMonitorType scanMonitor) {
        this.preScanMonitor = preScanMonitor;
        this.scanMonitor = scanMonitor;

        this.activeMonitor = preScanMonitor;
    }

    public PreScanMonitorType getPreScanMonitor() {
        return preScanMonitor;
    }

    public ScanMonitorType getScanMonitor() {
        return scanMonitor;
    }

    @Override
    public void addMessage(AbstractNode n, Message.Severity severity, String msg) {
        activeMonitor.addMessage(n, severity, msg);
    }

    @Override
    public void addMessage(AbstractNode n, Message.Severity severity, String key, String msg) {
        activeMonitor.addMessage(n, severity, key, msg);
    }

    @Override
    public void addMessageInfo(AbstractNode n, Message.Severity severity, String msg) {
        activeMonitor.addMessageInfo(n, severity, msg);
    }

    @Override
    public boolean allowNextIteration() {
        return activeMonitor.allowNextIteration();
    }

    @Override
    public void visitPhasePre(AnalysisPhase phase) {
        if (phase == AnalysisPhase.SCAN) {
            activeMonitor = scanMonitor;
        }
        activeMonitor.visitPhasePre(phase);
    }

    @Override
    public void visitPhasePost(AnalysisPhase phase) {
        activeMonitor.visitPhasePost(phase);
    }

    @Override
    public void setSolverInterface(Solver.SolverInterface c) {
        preScanMonitor.setSolverInterface(c);
        scanMonitor.setSolverInterface(c);
    }

    @Override
    public void visitBlockTransferPre(BasicBlock b, State s) {
        activeMonitor.visitBlockTransferPre(b, s);
    }

    @Override
    public void visitCall(AbstractNode n, Value funval) {
        activeMonitor.visitCall(n, funval);
    }

    @Override
    public void visitEvalCall(AbstractNode n, Value v) {
        activeMonitor.visitEvalCall(n, v);
    }

    @Override
    public void visitFunction(Function f, Collection<State> entry_states) {
        activeMonitor.visitFunction(f, entry_states);
    }

    @Override
    public void visitIf(IfNode n, Value v) {
        activeMonitor.visitIf(n, v);
    }

    @Override
    public void visitIn(AbstractNode n, boolean maybe_v2_object, boolean maybe_v2_nonobject) {
        activeMonitor.visitIn(n, maybe_v2_object, maybe_v2_nonobject);
    }

    @Override
    public void visitInnerHTMLWrite(Node n, Value v) {
        activeMonitor.visitInnerHTMLWrite(n, v);
    }

    @Override
    public void visitInstanceof(AbstractNode n, boolean maybe_v2_non_function, boolean maybe_v2_function, boolean maybe_v2_prototype_primitive, boolean maybe_v2_prototype_nonprimitive) {
        activeMonitor.visitInstanceof(n, maybe_v2_non_function, maybe_v2_function, maybe_v2_prototype_primitive, maybe_v2_prototype_nonprimitive);
    }

    @Override
    public void visitJoin(long ms) {
        activeMonitor.visitJoin(ms);
    }

    @Override
    public void visitNativeFunctionCall(AbstractNode n, HostObject hostobject, boolean num_actuals_unknown, int num_actuals, int min, int max) {
        activeMonitor.visitNativeFunctionCall(n, hostobject, num_actuals_unknown, num_actuals, min, max);
    }

    @Override
    public void visitNewFlow(BasicBlock b, Context c, State s, String diff, String info) {
        activeMonitor.visitNewFlow(b, c, s, diff, info);
    }

    @Override
    public void visitNodeTransferPre(AbstractNode n, State s) {
        activeMonitor.visitNodeTransferPre(n, s);
    }

    @Override
    public void visitNodeTransferPost(AbstractNode n, State s) {
        activeMonitor.visitNodeTransferPost(n, s);
    }

    @Override
    public void visitBlockTransferPost(BasicBlock b, State state) {
        activeMonitor.visitBlockTransferPost(b, state);
    }

    @Override
    public void visitPropertyAccess(Node n, Value baseval) {
        activeMonitor.visitPropertyAccess(n, baseval);
    }

    @Override
    public void visitPropertyRead(AbstractNode n, Set<ObjectLabel> objs, PKeys propertyname, State state, boolean check_unknown) {
        activeMonitor.visitPropertyRead(n, objs, propertyname, state, check_unknown);
    }

    @Override
    public void visitPropertyWrite(Node n, Set<ObjectLabel> objs, PKeys propertyname) {
        activeMonitor.visitPropertyWrite(n, objs, propertyname);
    }

    @Override
    public void visitRead(Node n, Value v, State state) {
        activeMonitor.visitRead(n, v, state);
    }

    @Override
    public void visitReadNonThisVariable(ReadVariableNode n, Value v) {
        activeMonitor.visitReadNonThisVariable(n, v);
    }

    @Override
    public void visitReadProperty(ReadPropertyNode n, Set<ObjectLabel> objlabels, PKeys propertyname, boolean maybe, State state, Value v, ObjectLabel global_obj) {
        activeMonitor.visitReadProperty(n, objlabels, propertyname, maybe, state, v, global_obj);
    }

    @Override
    public void visitReadThis(ReadVariableNode n, Value v, State state, ObjectLabel global_obj) {
        activeMonitor.visitReadThis(n, v, state, global_obj);
    }

    @Override
    public void visitReadVariable(ReadVariableNode n, Value v, State state) {
        activeMonitor.visitReadVariable(n, v, state);
    }

    @Override
    public void visitRecoveryGraph(AbstractNode node, int size) {
        activeMonitor.visitRecoveryGraph(node, size);
    }

    @Override
    public void visitUnknownValueResolve(AbstractNode node, boolean partial, boolean scanning) {
        activeMonitor.visitUnknownValueResolve(node, partial, scanning);
    }

    @Override
    public void visitUserFunctionCall(Function f, AbstractNode call, boolean constructor) {
        activeMonitor.visitUserFunctionCall(f, call, constructor);
    }

    @Override
    public void visitVariableAsRead(AbstractNode n, String varname, Value v, State state) {
        activeMonitor.visitVariableAsRead(n, varname, v, state);
    }

    @Override
    public void visitVariableOrProperty(AbstractNode node, String var, SourceLocation loc, Value value, Context context, State state) {
        activeMonitor.visitVariableOrProperty(node, var, loc, value, context, state);
    }

    @Override
    public void visitNativeFunctionReturn(AbstractNode node, HostObject hostObject, Value result) {
        activeMonitor.visitNativeFunctionReturn(node, hostObject, result);
    }

    @Override
    public void visitEventHandlerRegistration(AbstractNode node, Context context, Value handler) {
        activeMonitor.visitEventHandlerRegistration(node, context, handler);
    }

    @Override
    public void visitPropagationPre(BlockAndContext<Context> from, BlockAndContext<Context> to) {
        activeMonitor.visitPropagationPre(from, to);
    }

    @Override
    public void visitPropagationPost(BlockAndContext<Context> from, BlockAndContext<Context> to, boolean changed) {
        activeMonitor.visitPropagationPost(from, to, changed);
    }

    @Override
    public void visitNewObject(AbstractNode node, ObjectLabel label, State s) {
        activeMonitor.visitNewObject(node, label, s);
    }

    @Override
    public void visitRenameObject(AbstractNode node, ObjectLabel from, ObjectLabel to, State s) {
        activeMonitor.visitRenameObject(node, from, to, s);
    }

    @Override
    public void visitIterationDone(String terminatedEarlyMsg) {
        activeMonitor.visitIterationDone(terminatedEarlyMsg);
    }

    @Override
    public void visitSoundnessTestingDone(int numSoundnessChecks) {
        activeMonitor.visitSoundnessTestingDone(numSoundnessChecks);
    }
}
