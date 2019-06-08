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

package dk.brics.tajs.analysis.js;

import dk.brics.tajs.analysis.ParallelTransfer;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.lattice.FreeVariablePartitioning;
import dk.brics.tajs.lattice.MustEquals;
import dk.brics.tajs.lattice.ObjProperties;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.PKey.StringPKey;
import dk.brics.tajs.lattice.PartitionedValue;
import dk.brics.tajs.lattice.PartitioningQualifier;
import dk.brics.tajs.lattice.Property;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.Strings;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static dk.brics.tajs.util.Collections.mapOf;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Helper functions for value partitioning, used by {@link NodeTransfer} and {@link UserFunctionCalls}.
 */
public class Partitioning {

    private static Logger log = Logger.getLogger(Partitioning.class);

    private final static boolean REPORT_USAGE = false; // if set to true, report usage of partitioned value during analysis

    /**
     * Chooses the partitioning for a binop.
     */
    static Map<AbstractNode, Set<PartitioningQualifier>> getPartitionQualifiersForBinop(BinaryOperatorNode n, Value arg1, Value arg2) {
        if (n.getOperator() != BinaryOperatorNode.Op.ADD || !(arg1 instanceof PartitionedValue || arg2 instanceof PartitionedValue)) {
            // only partition at '+' and if one or both are already partitioned
            return Collections.emptyMap();
        }
        // at least one argument is partitioned
        if (arg1 instanceof PartitionedValue && arg2 instanceof PartitionedValue) {
            // both arguments are partitioned, use intersection of the partition nodes (using union would also be sound)
            PartitionedValue v1 = (PartitionedValue) arg1;
            PartitionedValue v2 = (PartitionedValue) arg2;
            return v1.getPartitionNodes().stream()
                    .filter(pn -> v2.getPartitionNodes().contains(pn))
                    .collect(Collectors.toMap(pn -> pn, v1::getPartitionQualifiers));
        } else {
            // exactly one of the arguments is partitioned (the common case), use its partitions
            return arg1 instanceof PartitionedValue ?
                    ((PartitionedValue) arg1).getPartitionQualifiers() :
                    ((PartitionedValue) arg2).getPartitionQualifiers();
        }
    }

    /**
     * Uses partitioning information from the current context to refine 'v', which is the value of 'varname'.
     * If the context contains partitioning information for 'varname' and 'v' is a partitioned value,
     * then the resulting value is the join of those partitions from 'v'.
     */
    static Value getVariableValueFromPartition(String varname, Value v, Solver.SolverInterface c) {
        if (!(v instanceof PartitionedValue))
            return v;
        FreeVariablePartitioning freeVariablePartitioning = c.getState().getContext().getFreeVariablePartitioning();
        if (freeVariablePartitioning != null && freeVariablePartitioning.hasInfoForVariable(varname)) {
            Set<PartitioningQualifier> partitioningQualifiers = freeVariablePartitioning.getInfoForVariable(varname);
            v = UnknownValueResolver.getRealValue(v, c.getState());
            Optional<AbstractNode> partitionNode = ((PartitionedValue) v).getPartitionNodes().stream().filter(n -> partitioningQualifiers.stream().anyMatch(p -> p.getNode().equals(n))).findAny(); // TODO: currently picking partition node arbitrarily - could compare precision if different partitions are available and pick the best
            if (partitionNode.isPresent()) {
                Value finalV = v;
                v = Value.join(partitioningQualifiers.stream()
                        .filter(p -> p.getNode().equals(partitionNode.get()))
                        .map(q -> PartitionedValue.getPartition(finalV, partitionNode.get(), q))
                        .collect(Collectors.toSet()));
                if (REPORT_USAGE) { // TODO: move to monitoring?
                    Value vi = PartitionedValue.ignorePartitions(finalV);
                    if (!v.equals(vi)) {
                        if (!log.isDebugEnabled() && log.isInfoEnabled())
                            System.out.print("\r");
                        log.warn("Using partitioned value at variable read at " + c.getNode().getSourceLocation() + " (original value: " + vi + ", partitioned value: " + v + ")");
                    }
                }
            }
        }
        return v;
    }

    /**
     * Partitions the uncoerced property name to read, into partitions based on the structure
     * of the base object. It filters the property register to match the partitioned value
     * of the uncoerced property name, and returns the value being read at this property as
     * a partitioned value with the same partitions as the property name.
     *
     * @param n the ReadPropertyNode
     * @param objlabels object labels of base after coercion
     * @param propertyval property value before coercion
     * @param propertystr property value after coercion
     * @param base_objs object labels where the property may be found (may be different from objlabels because of prototypes) are added to this set
     */
    static Value partitionPropValue(ReadPropertyNode n, Set<ObjectLabel> objlabels, Value propertyval, Value propertystr, Set<ObjectLabel> base_objs, Solver.SolverInterface c, Filtering filtering, PropVarOperations pv) {
        // choose partitioning node
        ReadPropertyNode partitionNode;
        if (propertyval instanceof PartitionedValue) { // If the value is already partitioned and the property register must equals the property register of this node, they can use the same partition node.
            MustEquals mustEquals = c.getState().getMustEquals();
            Set<Integer> mustEqualsRegisters = mustEquals.getMustEquals(n.getPropertyRegister()).stream().flatMap(objProp -> mustEquals.getMustEquals(objProp.getObjectLabel(), objProp.getPropertyName()).stream()).collect(Collectors.toSet());
            partitionNode = (ReadPropertyNode) ((PartitionedValue) propertyval).getPartitionNodes().stream().filter(pn -> !pn.equals(n)).filter(pn -> pn instanceof ReadPropertyNode).filter(pn -> mustEqualsRegisters.contains(((ReadPropertyNode) pn).getPropertyRegister())).findAny().orElse(n);
        } else {
            partitionNode = n;
        }
        // choose partitioning qualifiers
        Set<PartitioningQualifier> partitioningQualifiers = newSet();
        if (!partitionNode.equals(n)) { // if reusing an earlier partitioning node, then keep its partition qualifiers
            partitioningQualifiers.addAll(((PartitionedValue) propertyval).getPartitionQualifiers(partitionNode));
        }
        Value propertyvalWithoutPartitions = PartitionedValue.ignorePartitions(propertyval);
        ObjProperties properties = c.getState().getProperties(objlabels, ObjProperties.PropertyQuery.makeQuery().includeSymbols().usePrototypes());
        Set<PKey> pkeyProperties = properties.getMaybe().stream().filter(pkey -> pkey.isMaybeValue(propertystr)).collect(Collectors.toSet()); // finds all maybe-present properties (including prototype chain) that may match propertystr
        Set<String> pkeyPropertyStrings = pkeyProperties.stream().filter(pkey -> pkey instanceof PKey.StringPKey).map(pkey -> pkey.toValue().getStr()).collect(Collectors.toSet());
        Consumer<Property> addPartitionQualifierForProperty = p -> partitioningQualifiers.add(PartitioningQualifier.make(partitionNode, p));
        pkeyProperties.forEach(p -> addPartitionQualifierForProperty.accept(Property.makeOrdinaryProperty(p))); // adds partition qualifiers for ordinary properties
        Value other = propertystr.restrictToNotStrings(pkeyPropertyStrings); // values of propertystr that are not covered already
        if (other.isMaybeStrSomeNumeric())
            addPartitionQualifierForProperty.accept(Property.makeDefaultNumericProperty());
        if (other.isMaybeStrSomeNonNumeric())
            addPartitionQualifierForProperty.accept(Property.makeDefaultOtherProperty());
        if (REPORT_USAGE) { // TODO: move to monitoring?
            if (!log.isDebugEnabled() && log.isInfoEnabled())
                System.out.print("\r");
            log.warn("Partitioning value at " + n.getSourceLocation() + " (" + partitioningQualifiers.size() + " partitions)");
        }
        // partition for the property name
        Map<PartitioningQualifier, Value> propertyNamePartitions = partitioningQualifiers.stream().collect(
                Collectors.toMap(q -> q, q -> {
                    Property prop = q.getProperty();
                    Value propName = prop.toValue();
                    List<Value> vs = newList();
                    switch (prop.getKind()) {
                        case ORDINARY:
                            // include the pkey itself?
                            if (prop.getPropertyName().isMaybeValue(propertyvalWithoutPartitions))
                                vs.add(propName);
                            // include non-string primitive values?
                            if (prop.getPropertyName() instanceof PKey.StringPKey) {
                                String s = ((PKey.StringPKey)prop.getPropertyName()).getStr();
                                if (Strings.isNumeric(s) && propertyvalWithoutPartitions.isMaybeNum(Double.parseDouble(s)))
                                    vs.add(Value.makeNum(Double.parseDouble(s)));
                                if (s.equals("true") && propertyvalWithoutPartitions.isMaybeTrue())
                                    vs.add(Value.makeBool(true));
                                if (s.equals("false") && propertyvalWithoutPartitions.isMaybeFalse())
                                    vs.add(Value.makeBool(false));
                                if (s.equals("undefined") && propertyvalWithoutPartitions.isMaybeUndef())
                                    vs.add(Value.makeUndef());
                                if (s.equals("null") && propertyvalWithoutPartitions.isMaybeNull())
                                    vs.add(Value.makeNull());
                                if (s.equals("NaN") && propertyvalWithoutPartitions.isMaybeNaN())
                                    vs.add(Value.makeNumNaN());
                            }
                            break;
                        case DEFAULT_NUMERIC:
                            // include numbers
                            vs.add(propertyvalWithoutPartitions.restrictToNum());
                            // include numeric strings that haven't been covered already
                            vs.add(propertyvalWithoutPartitions.restrictToStrNumeric().restrictToNotStrings(pkeyPropertyStrings));
                            break;
                        case DEFAULT_OTHER:
                            // include non-string non-numeric primitive values?
                            if (propertyvalWithoutPartitions.isMaybeTrue() && !pkeyPropertyStrings.contains("true"))
                                vs.add(Value.makeBool(true));
                            if (propertyvalWithoutPartitions.isMaybeFalse() && !pkeyPropertyStrings.contains("false"))
                                vs.add(Value.makeBool(false));
                            if (propertyvalWithoutPartitions.isMaybeUndef() && !pkeyPropertyStrings.contains("undefined"))
                                vs.add(Value.makeUndef());
                            if (propertyvalWithoutPartitions.isMaybeNull() && !pkeyPropertyStrings.contains("null"))
                                vs.add(Value.makeNull());
                            if (propertyvalWithoutPartitions.isMaybeNaN() && !pkeyPropertyStrings.contains("NaN"))
                                vs.add(Value.makeNumNaN());
                            // include non-numeric strings that haven't been covered already
                            vs.add(propertyvalWithoutPartitions.restrictToStrNotNumeric().restrictToNotStrings(pkeyPropertyStrings));
                            break;
                    }
                    // include objects (could result in any pkey)
                    vs.add(Value.makeObject(propertyvalWithoutPartitions.restrictToNonSymbolObject().getObjectLabels()));
                    return Value.join(vs).joinMeta(propertyvalWithoutPartitions);
                }));
        // filter according to the property name partitioning
        filtering.assumePartitions(n.getPropertyRegister(), PartitionedValue.make(partitionNode, propertyNamePartitions));
        // partition for the property value
        Map<PartitioningQualifier, Value> valuePartitions = partitioningQualifiers.stream().collect(
                Collectors.toMap(q -> q, q -> {
                    Value propValue = properties.getValue(q.getProperty());
                    if (propValue.isMaybeGetter()) {
                        propValue = propValue.restrictToNotGetter().join(callGetters(objlabels, propValue, c));
                    }
                    if (propValue.isMaybeAbsent())
                        propValue = propValue.restrictToNotAbsent().joinUndef();
                    return PartitionedValue.ignorePartitions(propValue);
                }));
        // build the resulting partitioned value
        Map<AbstractNode, Map<PartitioningQualifier, Value>> valuePartitionsComplete = readValueInOtherPartitions(partitionNode, objlabels, propertystr, base_objs, c, pv);
        valuePartitionsComplete.put(partitionNode, valuePartitions);
        return PartitionedValue.make(valuePartitionsComplete);
    }

    /**
     * Returns the return values from calling the getters in propValue
     */
    private static Value callGetters(Set<ObjectLabel> base, Value propValue, Solver.SolverInterface c) {
        List<Value> values = newList();
        BasicBlock implicitAfterCall = c.getAnalysis().getPropVarOperations().callGetters(values, base, propValue, null);
        return UserFunctionCalls.implicitUserFunctionReturn(values, true, implicitAfterCall, c);
    }

    /**
     * Returns a partitioning map for reading the property in all the existing partitions in propertystr, ignoring existing partitionings from n.
     */
    static Map<AbstractNode, Map<PartitioningQualifier, Value>> readValueInOtherPartitions(ReadPropertyNode n, Set<ObjectLabel> objlabels, Value propertystr, Set<ObjectLabel> base_objs, Solver.SolverInterface c, PropVarOperations pv) {
        return propertystr instanceof PartitionedValue ?
                ((PartitionedValue) propertystr).getPartitionNodes().stream().filter(pn -> !pn.equals(n)).collect(Collectors.toMap(pn -> pn, pn ->
                        ((PartitionedValue) propertystr).getPartitionQualifiers(pn).stream().collect(Collectors.toMap(q -> q, q ->
                                PartitionedValue.ignorePartitions(UnknownValueResolver.getRealValue(pv.readPropertyValue(objlabels, ((PartitionedValue) propertystr).getPartition(pn, q), base_objs), c.getState()))
                        )))) :
                newMap();
    }

    /**
     * Returns the value given as propertystr, but with the strings 'undefined', 'null' and 'NaN' joined into each partition
     * if originalPVal may be undefined, null or NaN, respectively, in the corresponding partition.
     */
    static Value joinUndefNullNaNStrings(PartitionedValue originalPVal, PartitionedValue propertystr) {
        return PartitionedValue.make((originalPVal).getPartitionNodes().stream().collect(
                Collectors.toMap(pn -> pn, pn -> (originalPVal.getPartitionQualifiers(pn).stream().collect(
                        Collectors.toMap(q -> q, q -> {
                            Value origValue = originalPVal.getPartition(pn, q);
                            Value coercedValue = propertystr.getPartition(pn, q);
                            if (origValue.isMaybeUndef())
                                coercedValue = coercedValue.joinStr("undefined");
                            if (origValue.isMaybeNull())
                                coercedValue = coercedValue.joinStr("null");
                            if (origValue.isMaybeNaN())
                                coercedValue = coercedValue.joinStr("NaN");
                            return coercedValue;
                        }))))));
    }

    /**
     * Decides whether or not to use value partitioning at property write operation.
     * Returns true if both the property name value and the value to be written are partitioned values and their partitions have non-disjoint partitioning nodes.
     */
    static boolean usePartitionedWriteProperty(Value propName, Value valueToWrite) {
        if (!Options.get().isPropNamePartitioning() || !(propName instanceof PartitionedValue) || !(valueToWrite instanceof PartitionedValue))
            return false;
         return ((PartitionedValue) propName).getPartitionNodes().stream().anyMatch(pn -> ((PartitionedValue) valueToWrite).getPartitionNodes().contains(pn));
    }

    /**
     * Writes property by using partitions. If the partitions of the propertyname and value to be written are
     * the same, we only write the value at a partition to the property name at the same partition.
     */
    static void writePropertyWithPartitioning(ParallelTransfer pt, Set<ObjectLabel> objlabels, Value propName, Value valueToWrite, WritePropertyNode n, Solver.SolverInterface c, PropVarOperations pv) {
        // pick the partition node with the highest number of single-string partition values for the property name
        Stream<AbstractNode> partitionNodes = ((PartitionedValue) propName).getPartitionNodes().stream().filter(pn -> ((PartitionedValue) valueToWrite).getPartitionNodes().contains(pn));
        AbstractNode partitionNode = partitionNodes.max((pn1, pn2) -> {
            java.util.function.Function<AbstractNode, Long> precisePartitionCounter = pn -> ((PartitionedValue) propName).getPartitionQualifiers(pn).stream().filter(q -> ((PartitionedValue) propName).getPartition(pn, q).isMaybeSingleStr()).count();
            long c1 = precisePartitionCounter.apply(pn1);
            long c2 = precisePartitionCounter.apply(pn2);
            return (c1 > c2) ? 1 : (c1 < c2) ? -1 : (pn2.getIndex() - pn1.getIndex());
        }).orElseGet(() -> {throw new AnalysisException("Empty partitionNodes!?");});
        // perform the write for each partition
        Value resolvedValueToWrite = UnknownValueResolver.getRealValue(valueToWrite, c.getState());
        for (PartitioningQualifier q : ((PartitionedValue)propName).getPartitionQualifiers(partitionNode)) {
            Value partitionedPropName = ((PartitionedValue)propName).getPartition(q.getNode(), q);
            Value partitionedValueToWrite = ((PartitionedValue)resolvedValueToWrite).getPartition(q.getNode(), q);
            if (c.getAnalysis().getUnsoundness().mayIgnorePartition(n, partitionedValueToWrite)) {
                continue;
            }
            if (!partitionedPropName.isNone() && !partitionedValueToWrite.isNone())
                pt.add(() -> pv.writeProperty(objlabels, partitionedPropName, partitionedValueToWrite, true, n.isDecl()));
        }
        if (REPORT_USAGE) { // TODO: move to monitoring?
            if (!log.isDebugEnabled() && log.isInfoEnabled())
                System.out.print("\r");
            log.warn("Using partitioned value at property write at " + n.getSourceLocation()); // TODO: detect if value partitioning led to any difference at the property write operation?
        }
    }

    /**
     * Returns the instantiated functions. If the current context has FreeVariablePartitioning,
     * then annotate the function declaration by that FreeVariablePartitioning.
     * If the free variables in the function delaration only consists of one set of partition qualifiers,
     * then it returns a partitioned value where each partition contains the function but different FreeVariablePartitionings.
     * Otherwise, if the function has a single unpartitioned value, we partition the variable being
     * read to contain one partition with the current value, and the returned function is annotated to
     * use this partition.
     */
    static Value getInstantiatedFunctions(ObjectLabel fn, Function fun, DeclareFunctionNode n, Solver.SolverInterface c) {
        if (Options.get().isPropNamePartitioning() && n.isExpression()) {
            PropVarOperations pv = c.getAnalysis().getPropVarOperations();
            FreeVariablePartitioning inheritingFreeVariablePartitioning = c.getState().getContext().getFreeVariablePartitioning();
            Set<String> freeVariableNames = c.getFlowGraph().getSyntacticInformation().getClosureVariableNamesTransitively(fun);
            if (inheritingFreeVariablePartitioning != null) { // remove the free variables where we already have FreeVariablePartitioning from the current context
                freeVariableNames = freeVariableNames.stream().filter(varname -> !inheritingFreeVariablePartitioning.hasInfoForVariable(varname)).collect(Collectors.toSet());
            }
            // read partitioned values for the free variables
            Map<String, PartitionedValue> partitionedValuesForVarNames = freeVariableNames.stream()
                    .map(v -> Pair.make(v, UnknownValueResolver.getRealValue(pv.readVariable(v, null, false, false), c.getState())))
                    .filter(p -> p.getSecond() instanceof PartitionedValue)
                    .collect(Collectors.toMap(Pair::getFirst, p -> (PartitionedValue) p.getSecond()));
            // find partitioning nodes that are common to the partitioned free variables
            Set<AbstractNode> pns = getIntersectedPartitionNodes(partitionedValuesForVarNames.values());
            if (pns != null && !pns.isEmpty()) { // if pns is not null and empty, it means incomparable partition values
                // build partitioned function value
                Map<AbstractNode, Map<PartitioningQualifier, Value>> res =
                        pns.stream().collect(Collectors.toMap(
                                pn -> pn,
                                pn -> partitionedValuesForVarNames.values().stream()
                                        .flatMap(v -> v.getPartitionQualifiers(pn).stream()
                                        .filter(q -> partitionedValuesForVarNames.values().stream().noneMatch(v1 -> c.getAnalysis().getUnsoundness().mayIgnorePartition(n, v1.getPartition(pn, q))))).distinct()
                                        .collect(Collectors.toMap(
                                                q -> q,
                                                q -> Value.makeObject(fn).setFreeVariablePartitioning(FreeVariablePartitioning.make(partitionedValuesForVarNames.keySet(), q).join(inheritingFreeVariablePartitioning))))));
                if (REPORT_USAGE) { // TODO: move to monitoring?
                    if (!log.isDebugEnabled() && log.isInfoEnabled())
                        System.out.print("\r");
                    log.warn("Partitioning function value based on existing partitioned value of free variable at " + n.getSourceLocation() + " (" + res.values().stream().mapToInt(Map::size).sum() + " partitions)");
                }
                return PartitionedValue.make(res);
            }
            // special case: no partitioned free variables, partition those free variables that have function values
            Map<String, Value> unpartitionedFreeVariables =
                    freeVariableNames.stream()
                            .map(v -> Pair.make(v, c.getState().readVariableDirect(v)))
                            .filter(p -> p.getSecond().getFreeVariablePartitioning() == null && !(p.getSecond() instanceof PartitionedValue) && p.getSecond().getObjectLabels() != null && p.getSecond().getObjectLabels().stream().anyMatch(obj -> obj.getKind() == ObjectLabel.Kind.FUNCTION))
                            .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            if (!unpartitionedFreeVariables.isEmpty()) {
                PartitioningQualifier q = PartitioningQualifier.make(n, Property.makeOrdinaryProperty(PKey.make(Value.makeStr(unpartitionedFreeVariables.values().toString())))); // FIXME: hacky partition qualifier
                unpartitionedFreeVariables.forEach((key, value) -> c.getAnalysis().getPropVarOperations().writePropertyWithAttributes(c.getState().getExecutionContext().getVariableObject(),
                        StringPKey.make(key),
                        PartitionedValue.make(n, mapOf(q, value))));
                if (REPORT_USAGE) { // TODO: move to monitoring?
                    if (!log.isDebugEnabled() && log.isInfoEnabled())
                        System.out.print("\r");
                    log.warn("Partitioning function value based on non-partitioned values of free variable at " + n.getSourceLocation() + " (1 partition)");
                }
                return Value.makeObject(fn).setFreeVariablePartitioning(FreeVariablePartitioning.make(unpartitionedFreeVariables.keySet(), q));
            }
            // no function value partitioning (but keep the FreeVariablePartitioning)
            if (inheritingFreeVariablePartitioning != null) {
                // inherit FreeVariablePartitioning from the current context, if available
                return Value.makeObject(fn).setFreeVariablePartitioning(inheritingFreeVariablePartitioning);
            }
        }
        return Value.makeObject(fn);
    }

    /**
     * Returns a set of all the partitioning nodes contained in all the values. If values it empty,
     * null is returned, otherwise a set will always be returned, even though it might be empty.
     */
    static Set<AbstractNode> getIntersectedPartitionNodes(Collection<PartitionedValue> values) {
        Set<AbstractNode> pns = null;
        if (!values.isEmpty()) {
            pns = newSet(values.iterator().next().getPartitionNodes());
            values.stream().map(PartitionedValue::getPartitionNodes).forEach(pns::retainAll);
        }
        return pns;
    }
}
