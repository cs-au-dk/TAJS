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

package dk.brics.tajs.blendedanalysis.dynamic;

import dk.au.cs.casa.jer.entries.CallEntry;
import dk.au.cs.casa.jer.entries.IEntry;
import dk.au.cs.casa.jer.entries.ValueDescription;
import dk.au.cs.casa.jer.entries.VariableOrPropertyEntry;
import dk.brics.tajs.analysis.KnownUnsoundnesses;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.blendedanalysis.IRefiner;
import dk.brics.tajs.blendedanalysis.InstructionComponent;
import dk.brics.tajs.blendedanalysis.solver.BlendedAnalysisQuery;
import dk.brics.tajs.blendedanalysis.solver.Constraint;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Collectors;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Solves the BlendedAnalysisQueries and uses JalangiRefinerUtilities for interaction with log files.
 */
public class JalangiRefiner implements IRefiner {

    private JalangiRefinerUtilities jalangiRefinerUtilities;

    public JalangiRefiner(JalangiRefinerUtilities jalangiRefinerUtilities) {
        this.jalangiRefinerUtilities = jalangiRefinerUtilities;
    }

    @Override
    public Collection<Value> solveQuery(BlendedAnalysisQuery query) {
        AbstractNode node = query.getNode();
        InstructionComponent ic = query.getInstructionComponent();

        if (!isNodeJalangiCompatible(node))
            return singleton(query.getSoundDefault());

        Set<IEntry> entriesAtSl = newSet();
        if (isCallRefinement(node, ic)) {
            entriesAtSl.addAll(jalangiRefinerUtilities.getCallEntriesAtSourceLocation(node.getSourceLocation()));
        }
        if (!ic.isArgument()) { // Also check VarOrProp entries, since there are no call entries for failing calls.
            int columnOffset = node instanceof CallNode && ((CallNode) node).isConstructorCall() ? 4 : 0; // new f() has node sourcelocation at the beginning of new, but the VarOrProp entries locations is at the beginning of f
            entriesAtSl.addAll(jalangiRefinerUtilities.getVarOrPropEntriesAtSourceLocation(node.getSourceLocation(), columnOffset));
        }

        Set<IEntry> filteredEntriesAtSl = getEntriesMatchingConstraints(entriesAtSl, query.getConstraints());

        Set<Value> values = convertEntriesToTAJSValues(filteredEntriesAtSl, ic, query.getSoundDefault());

        if (jalangiRefinerUtilities.hasDynamicCodeEntriesAtSourceLocation(node.getSourceLocation())) { // Calls to eval is not converted to an ordinary function call by the valuelogger, but by a dynamicCodeEntry instead, so need special case.
            Optional<ObjectLabel> eval = query.getSoundDefault().getObjectLabels().stream().filter(obj -> obj.isHostObject() && obj.getHostObject() == ECMAScriptObjects.EVAL).findAny();
            if (eval.isPresent()) {
                values.add(Value.makeObject(eval.get()));
            } else if (ic.isArgument()) {
                values.add(jalangiRefinerUtilities.getCodeFromDynamicCodeEntryAtSourceLocation(node.getSourceLocation()));
            }
        }
        return values;
    }

    private boolean isNodeJalangiCompatible(AbstractNode node) {
        boolean isHostEnvironmentSource = jalangiRefinerUtilities.isHostEnvironmentSource(node.getSourceLocation());
        if (node.getSourceLocation().getLocation() == null || isHostEnvironmentSource) { //Is a sourcelocation we cannot compare with value log entries
            return false;
        }

        final boolean isSpecialLiteralConstructor;
        if (node instanceof CallNode) {
            final CallNode.LiteralConstructorKinds kind = ((CallNode) node).getLiteralConstructorKind();
            isSpecialLiteralConstructor = kind == CallNode.LiteralConstructorKinds.ARRAY /* TODO make JalangiLogger support Array-literal argument logging */ || kind == CallNode.LiteralConstructorKinds.REGEXP;
        } else {
            isSpecialLiteralConstructor = false;
        }
        boolean knownSoundnessError = KnownUnsoundnesses.isUnsoundLocation(node.getSourceLocation());

        if (isSpecialLiteralConstructor || knownSoundnessError) {
            return false;
        }
        return true;
    }

    private <E extends IEntry> Set<E> getEntriesMatchingConstraints(Set<E> entries, Set<Constraint> constraints) {
        return entries.stream().filter(e -> constraints.stream().allMatch(c -> entryMatchesConstraint(e, c))).collect(Collectors.toSet());
    }

    private <E extends IEntry> Set<Value> convertEntriesToTAJSValues(Set<E> entries, InstructionComponent ic, Value soundDefault) {
        return entries.stream().map(e -> convertEntryToTAJSValue(e, ic, soundDefault)).collect(Collectors.toSet());
    }

    private Value convertEntryToTAJSValue(IEntry entry, InstructionComponent ic, Value soundDefault) {
        ValueDescription valueDescription = getTargetedValueDescription(entry, ic);

        if (valueDescription == null) {
            return Value.makeNone();
        }

        return jalangiRefinerUtilities.convertValueDescriptionToTAJSValue(valueDescription, soundDefault);
    }

    private boolean entryMatchesConstraint(IEntry entry, Constraint c) {
        InstructionComponent instructionComponent = c.getInstructionComponent();
        ValueDescription valueDescription = getTargetedValueDescription(entry, instructionComponent);

        if (valueDescription == null) {
            return false;
        }

        return jalangiRefinerUtilities.valueDescriptionMatchesValue(valueDescription, c.getValue());
    }

    private ValueDescription getTargetedValueDescription(IEntry entry, InstructionComponent instructionComponent) {
        ValueDescription valueDescription;
        if (entry instanceof VariableOrPropertyEntry) {
            VariableOrPropertyEntry e = (VariableOrPropertyEntry) entry;
            if (instructionComponent.isBase()) {
                valueDescription = e.getBase();
            } else if (instructionComponent.isProperty()) {
                valueDescription = e.getVarOrProp();
            } else if (instructionComponent.isTarget()) {
                valueDescription = e.getValueDescription();
            } else {
                throw new UnsupportedOperationException("Unexpected InstructionComponent: " + instructionComponent);
            }
        } else if (entry instanceof CallEntry) {
            CallEntry e = (CallEntry) entry;
            if (instructionComponent.isBase()) {
                valueDescription = e.getBase();
            } else if (instructionComponent.isTarget()) {
                valueDescription = e.getFunction();
            } else if (instructionComponent.isArgument()) {
                valueDescription = instructionComponent.getArgNumber() < e.getArguments().size() ? e.getArguments().get(instructionComponent.getArgNumber()) : null;
            } else {
                throw new UnsupportedOperationException("Unexpected InstructionComponent: " + instructionComponent);
            }
        } else {
            throw new UnsupportedOperationException("Unexpected entry: " + entry);
        }
        return valueDescription;
    }

    private boolean isCallRefinement(AbstractNode node, InstructionComponent ic) {
        return node instanceof CallNode && !ic.isProperty();
    }
}
