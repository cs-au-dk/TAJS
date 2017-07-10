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

package dk.brics.tajs.monitoring.inspector.dataprocessing;

import dk.brics.inspector.api.model.lines.LineValue;
import dk.brics.inspector.api.model.lines.LineValueKind;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.jsnodes.AssumeNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginWithNode;
import dk.brics.tajs.flowgraph.jsnodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.CatchNode;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.DeletePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.EndForInNode;
import dk.brics.tajs.flowgraph.jsnodes.EndLoopNode;
import dk.brics.tajs.flowgraph.jsnodes.EndWithNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.ExceptionalReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.HasNextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.LoadNode;
import dk.brics.tajs.flowgraph.jsnodes.NewObjectNode;
import dk.brics.tajs.flowgraph.jsnodes.NextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
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
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.INodeTransfer;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;

import java.net.URL;
import java.util.Set;
import java.util.stream.Stream;

import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Computes the abstract values that are syntactically present at a specific line.
 */
public class LineValueComputer {

    private final SyntaxMatcher syntaxMatcher;

    private final DomainMapper mapper;

    private final Solver.SolverInterface c;

    private final IntermediaryStateComputer intermediaryStateComputer;

    public LineValueComputer(SyntaxMatcher syntaxMatcher, DomainMapper mapper, Solver.SolverInterface c) {
        this.syntaxMatcher = syntaxMatcher;
        this.mapper = mapper;
        this.c = c;
        this.intermediaryStateComputer = new IntermediaryStateComputer(c);
    }

    public Set<LineValue> get(URL file, int line) {
        Set<AbstractNode> nodes = syntaxMatcher.getNodesForLine(file, line);
        Set<Pair<LineValueDescription, Value>> contextSensitive = nodes.stream()
                .map(AbstractNode::getBlock)
                .distinct()
                .flatMap(b -> c.getAnalysisLatticeElement().getStates(b).keySet().stream())
                .flatMap(context -> nodes.stream()
                        .flatMap(n -> {
                            Set<Pair<LineValueDescription, Value>> values = getValuesForNode(n, context);
                            return values.stream();
                        }))
                .collect(Collectors.toSet());
        Set<Pair<LineValueDescription, Value>> contextInsensitive = contextSensitive.stream()
                .collect(Collectors.groupingBy(
                        e -> new LineValueDescription(e.getFirst().node, e.getFirst().description, e.getFirst().kind, null),
                        java.util.stream.Collectors.mapping(Pair::getSecond, Collectors.toSet())))
                .entrySet().stream()
                .map(e -> Pair.make(e.getKey(), Value.join(e.getValue())))
                .collect(Collectors.toSet());
        Set<LineValue> lineValues = Stream.of(contextSensitive, contextInsensitive)
                .flatMap(Set::stream)
                .map(e -> new LineValue(
                        e.getFirst().kind,
                        String.format("%s @ %s", e.getFirst().description, e.getFirst().node.toString()),
                        mapper.makeCompositeValue(e.getSecond()),
                        e.getFirst().context != null ? mapper.makeDescribedLocation(e.getFirst().node, e.getFirst().context) : mapper.makeDescribedLocation(e.getFirst().node)
                ))
                .collect(Collectors.toSet());
        return lineValues;
    }

    private Set<Pair<LineValueDescription, Value>> getValuesForNode(AbstractNode node, Context context) {
        // TODO introduce caching? This leads to quadratic number of node transfers wrt. the block size when all nodes in a block are queried! (but blocks are small, so it is probabaly not that expensive)
        State state = intermediaryStateComputer.makeIntermediaryPostState(node, context);

        //
        // ugly visitor pattern hackery
        //
        NodeLineValueVisitor visitor = new NodeLineValueVisitor(state);
        node.visitBy(new INodeTransfer<State, Context>() {
            @Override
            public void transfer(AbstractNode n) {
                // unused
            }

            @Override
            public void transferReturn(AbstractNode call_node, BasicBlock callee_entry, Context caller_context, Context callee_context, Context edge_context, boolean implicit) {
                // unused
            }

            @Override
            public void visit(Node n) {
                intermediaryStateComputer.withTempState(n, state, () -> {
                    if (n instanceof LoadNode) {
                        // convenience handling of all result-registers
                        visitor.visit((LoadNode) n);
                    }
                    n.visitBy(visitor);
                });
            }
        });
        return visitor.getCollectedValues();
    }

    private class NodeLineValueVisitor implements NodeVisitor {

        private final State state;

        private Set<Pair<LineValueDescription, Value>> collectedValues = newSet();

        public NodeLineValueVisitor(State state) {
            this.state = state;
        }

        @Override
        public void visit(AssumeNode n) {
            visitPropertyAccess(n, n.getBaseRegister(), n.getPropertyRegister(), n.getPropertyString());
            addVariable(n, n.getVariableName(), AbstractNode.NO_VALUE);
        }

        private void addVariable(AbstractNode n, String variableName, int resultBaseRegister) {
            if (variableName != null) {
                Value value;
                if ("this".equals(variableName)) {
                    value = state.readThis();
                } else {
                    Set<ObjectLabel> base_objs = null;
                    if (c.isScanning() || resultBaseRegister != AbstractNode.NO_VALUE)
                        base_objs = newSet();
                    value = c.getAnalysis().getPropVarOperations().readVariable(variableName, base_objs);
                }
                add(n, LineValueKind.VARIABLE, variableName, value);
            }
        }

        private void add(AbstractNode n, LineValueKind kind, String description, Value value) {
            LineValueDescription lineValueDescription = new LineValueDescription(n, description, kind, state.getContext());
            Value realValue = UnknownValueResolver.getRealValue(value, state);
            if (!realValue.isNone()) {
                collectedValues.add(Pair.make(lineValueDescription, realValue));
            }
        }

        private void addDynamicProperty(AbstractNode n, int baseRegister, int propertyRegister) {
            if (registerExists(baseRegister) && registerExists(propertyRegister)) {
                add(n, LineValueKind.DYNAMIC_PROPERTY, formatRegister(baseRegister) + "[" + formatRegister(propertyRegister) + "]", readProperty(n, baseRegister, state.readRegister(propertyRegister)));
            }
        }

        private void addFixedProperty(AbstractNode n, int baseRegister, String propertyName) {
            if (registerExists(baseRegister) && propertyName != null) {
                add(n, LineValueKind.FIXED_PROPERTY, formatRegister(baseRegister) + "." + propertyName, readProperty(n, baseRegister, Value.makeStr(propertyName)));
            }
        }

        private Value readProperty(AbstractNode node, int baseRegister, Value propertyName) {
            Value uncoercedBase = UnknownValueResolver.getRealValue(state.readRegister(baseRegister), state);
            Set<ObjectLabel> base = Conversion.toObjectLabels(node, uncoercedBase, false, null);
            propertyName = UnknownValueResolver.getRealValue(propertyName, state);
            // TODO Unify the string-coercion implementation for dynamic property-names (GitHub #402)
//             Set<Value> propertyNames = new SplittingUtil(SplittingUtil.Strategies::kinds, SplittingUtil.Strategies::singletons).getToStringCoercions(propertyName, false, SplittingUtil.Purpose.DPA, c);
            Set<Value> propertyNames = singleton(Conversion.toString(propertyName, c)); // TODO: replace with previous line to use splitting
            Set<Value> values = propertyNames.stream()
                    .map(name -> c.getAnalysis().getPropVarOperations().readPropertyValue(base, name)).collect(Collectors.toSet());
            return UnknownValueResolver.join(values, state);
        }

        private void addRegister(AbstractNode n, int register) {
            if (registerExists(register)) {
                add(n, LineValueKind.REGISTER, formatRegister(register), state.readRegister(register));
            }
        }

        private boolean registerExists(int register) {
            return register != AbstractNode.NO_VALUE && state.getRegisters().size() > register && state.getRegisters().get(register) != null;
        }

        private String formatRegister(int register) {
            return "<" + register + ">";
        }

        @Override
        public void visit(BinaryOperatorNode n) {
            addRegister(n, n.getArg1Register());
            addRegister(n, n.getArg2Register());
        }

        @Override
        public void visit(CallNode n) {
            for (int i = 0; i < n.getNumberOfArgs(); i++) {
                addRegister(n, n.getArgRegister(i));
            }
            visitPropertyAccess(n, n.getBaseRegister(), n.getPropertyRegister(), n.getPropertyString());
            addRegister(n, n.getFunctionRegister());
        }

        @Override
        public void visit(CatchNode n) {
            addRegister(n, n.getValueRegister());
            addRegister(n, n.getScopeObjRegister());
            addVariable(n, n.getVariableName(), AbstractNode.NO_VALUE);
        }

        @Override
        public void visit(ConstantNode n) {
            // NOOP (handled as LoadNode)
        }

        @Override
        public void visit(DeletePropertyNode n) {
            addVariable(n, n.getVariableName(), AbstractNode.NO_VALUE);
            visitPropertyAccess(n, n.getBaseRegister(), n.getPropertyRegister(), n.getPropertyString());
        }

        @Override
        public void visit(BeginWithNode n) {
            addRegister(n, n.getObjectRegister());
        }

        @Override
        public void visit(ExceptionalReturnNode n) {
            // NOOP
        }

        @Override
        public void visit(DeclareFunctionNode n) {
            // NOOP (handled as LoadNode)
        }

        @Override
        public void visit(BeginForInNode n) {
            addRegister(n, n.getObjectRegister());
            addRegister(n, n.getPropertyListRegister());
        }

        @Override
        public void visit(IfNode n) {
            addRegister(n, n.getConditionRegister());
        }

        @Override
        public void visit(EndWithNode n) {
            // NOOP
        }

        @Override
        public void visit(NewObjectNode n) {
            // NOOP (handled as LoadNode)
        }

        @Override
        public void visit(NextPropertyNode n) {
            addRegister(n, n.getPropertyRegister());
            addRegister(n, n.getPropertyListRegister());
        }

        @Override
        public void visit(HasNextPropertyNode n) {
            addRegister(n, n.getPropertyListRegister());
        }

        @Override
        public void visit(NopNode n) {
            // NOOP
        }

        @Override
        public void visit(ReadPropertyNode n) {
            visitPropertyAccess(n, n.getBaseRegister(), n.getPropertyRegister(), n.getPropertyString());
        }

        @Override
        public void visit(ReadVariableNode n) {
            addRegister(n, n.getResultBaseRegister());
            addVariable(n, n.getVariableName(), n.getResultBaseRegister());
        }

        @Override
        public void visit(ReturnNode n) {
            addRegister(n, n.getReturnValueRegister());
        }

        @Override
        public void visit(ThrowNode n) {
            addRegister(n, n.getValueRegister());
        }

        @Override
        public void visit(TypeofNode n) {
            addRegister(n, n.getArgRegister());
            addVariable(n, n.getVariableName(), AbstractNode.NO_VALUE);
        }

        @Override
        public void visit(UnaryOperatorNode n) {
            addRegister(n, n.getArgRegister());
        }

        @Override
        public void visit(DeclareVariableNode n) {
            addVariable(n, n.getVariableName(), AbstractNode.NO_VALUE);
        }

        @Override
        public void visit(WritePropertyNode n) {
            visitPropertyAccess(n, n.getBaseRegister(), n.getPropertyRegister(), n.getPropertyString());
        }

        private void visitPropertyAccess(AbstractNode n, int baseRegister, int propertyRegister, String propertyString) {
            addRegister(n, baseRegister);
            addRegister(n, propertyRegister);
            addDynamicProperty(n, baseRegister, propertyRegister);
            addFixedProperty(n, baseRegister, propertyString);
        }

        @Override
        public void visit(WriteVariableNode n) {
            addVariable(n, n.getVariableName(), AbstractNode.NO_VALUE);
        }

        @Override
        public void visit(EventDispatcherNode n) {
            // NOOP
        }

        @Override
        public void visit(EndForInNode n) {
            // NOOP
        }

        @Override
        public void visit(BeginLoopNode n) {
            // NOOP
        }

        @Override
        public void visit(EndLoopNode n) {
            // NOOP
        }

        public Set<Pair<LineValueDescription, Value>> getCollectedValues() {
            return collectedValues;
        }

        public void visit(LoadNode n) {
            addRegister(n, n.getResultRegister());
        }
    }

    private class LineValueDescription {

        private final AbstractNode node;

        private final String description;

        private final LineValueKind kind;

        private final Context context;

        public LineValueDescription(AbstractNode node, String description, LineValueKind kind, Context context) {
            this.node = node;
            this.description = description;
            this.kind = kind;
            this.context = context;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LineValueDescription that = (LineValueDescription) o;

            if (node != null ? !node.equals(that.node) : that.node != null) return false;
            if (description != null ? !description.equals(that.description) : that.description != null) return false;
            if (kind != that.kind) return false;
            return context != null ? context.equals(that.context) : that.context == null;
        }

        @Override
        public int hashCode() {
            int result = node != null ? node.hashCode() : 0;
            result = 31 * result + (description != null ? description.hashCode() : 0);
            result = 31 * result + (kind != null ? kind.hashCode() : 0);
            result = 31 * result + (context != null ? context.hashCode() : 0);
            return result;
        }
    }
}
