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

package dk.brics.tajs.lattice;

import dk.brics.tajs.flowgraph.AbstractNode;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Information about partitionings associated with an abstract states.
 */
public class PartitioningInfo {

    /**
     * Set of nodes where (value/type) partitioning has been introduced in this function.
     */
    private Set<AbstractNode> maybePartitionedNodes;

    /**
     * Definitely-dead partitions.
     */
    private Set<PartitionToken> deadPartitions;

    /**
     * Maybe-live partitions.
     */
    private Set<PartitionToken> alivePartitions;

    public PartitioningInfo(Set<PartitionToken> deadPartitions, Set<PartitionToken> alivePartitions) {
        maybePartitionedNodes = newSet();
        this.deadPartitions = deadPartitions;
        this.alivePartitions= alivePartitions;
    }

    public PartitioningInfo(PartitioningInfo p) {
        maybePartitionedNodes = newSet(p.maybePartitionedNodes);
        deadPartitions = newSet(p.deadPartitions);
        alivePartitions = newSet(p.alivePartitions);
    }

    public boolean propagate(PartitioningInfo s) {
        return maybePartitionedNodes.addAll(s.maybePartitionedNodes) | deadPartitions.retainAll(s.deadPartitions) | alivePartitions.addAll(s.alivePartitions);
    }

    public boolean isEmpty() {
        return maybePartitionedNodes.isEmpty();
    }

    @Override
    public String toString() {
        return "maybePartitionedNodes=" + maybePartitionedNodes;
    }

    /**
     * Sets the given partitions to none in properties of the variable object, if they are present.
     */
    public void setPartitionsToNone(State s, Collection<PartitionToken> partitions) {
        for (ObjectLabel objectLabel : s.getExecutionContext().getVariableObject()) {
            Obj object = s.getObject(objectLabel, true);
            newMap(object.getProperties()).forEach((key, val) -> {
                if (val instanceof PartitionedValue && !val.isPolymorphicOrUnknown()) {
                    AtomicBoolean killedPartitions = new AtomicBoolean(false);
                    Map<AbstractNode, Map<PartitionToken, Value>> newVal = ((PartitionedValue) val).getPartitionTokens().entrySet().stream().collect(java.util.stream.Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().stream().collect(java.util.stream.Collectors.toMap(q -> q, q -> {
                                        if (partitions.contains(q)) {
                                            killedPartitions.set(true);
                                            return Value.makeNone();
                                        } else {
                                            return ((PartitionedValue) val).getPartition(e.getKey(), q);
                                        }
                                    }
                            ))));
                    if (killedPartitions.get())
                        object.setProperty(key, PartitionedValue.make(newVal).joinMeta(object.getProperty(key)));
                }
            });
        }
    }

    /**
     * Record that partitioning has been made at the given node, and remove all partitions that belong to that node.
     */
    public void addPartitionedNodeAndRemoveExistingPartitions(State s, AbstractNode n) {
        maybePartitionedNodes.add(n);
        removePartitions(s, singleton(n));
    }

    private void removePartitions(State s, Set<AbstractNode> removePartitionNodes) {
        s.applyToAllValues(v -> PartitionedValue.removePartitions(v, removePartitionNodes));
    }

    public Set<AbstractNode> getMaybePartitionNodes() {
        return maybePartitionedNodes;
    }

    public void clearMaybePartitioned() {
        maybePartitionedNodes = newSet();
    }

    public void addMaybePartitionedNodes(Set<AbstractNode> maybePartitionNodes) {
        maybePartitionedNodes.addAll(maybePartitionNodes);
    }

    public Set<PartitionToken> getDeadPartitions() {
        return deadPartitions;
    }

    public void addDeadPartitions(Collection<PartitionToken> newDeadPartitions) {
        deadPartitions.addAll(newDeadPartitions);
    }

    public Set<PartitionToken> getAlivePartitions() {
        return alivePartitions;
    }

    public void addAlivePartitions(Collection<PartitionToken> newAlivePartitions) {
        alivePartitions.addAll(newAlivePartitions);
    }
}
