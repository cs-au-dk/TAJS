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
import dk.brics.tajs.util.Collectors;

import java.util.Map;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * Local must-reaching definitions.
 *
 * The must-reaching definition of a register is the node where the register must have obtained its value.
 * This information is used for filtering at explicit and implicit branches.
 */
public class MustReachingDefs { // TODO: use copy-on-write?

    /**
     * Map from register to the node that must have defined the value of the register, or absent if no such node.
     */
    private Map<Integer, AbstractNode> registerDefs;

    /**
     * Constructs a new empty MustReachingDefs.
     */
    public MustReachingDefs() {
        registerDefs = newMap();
    }

    /**
     * Constructs a new MustReachingDefs as a copy of the given one.
     */
    public MustReachingDefs(MustReachingDefs old) {
        registerDefs = newMap(old.registerDefs);
    }

    /**
     * Empties this MustReachingDefs.
     */
    public void setToBottom() {
        registerDefs.clear();
    }

    /**
     * Empties this MustReachingDefs for the given register.
     */
    public void setToBottom(int reg) {
        registerDefs.remove(reg);
    }

    /**
     * Adds a must-reaching definition for a register.
     */
    public void addReachingDef(int reg, AbstractNode node) {
        discardOldEntries();
        registerDefs.put(reg, node);
    }

    /**
     * Discard old entries if too many.
     */
    private void discardOldEntries() {
        int LIMIT = 50;
        if (registerDefs.size() > LIMIT) {
            registerDefs = registerDefs.entrySet().stream().sorted((e1, e2) -> e2.getValue().getIndex() - e1.getValue().getIndex()).limit(LIMIT / 2).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

    /**
     * Returns the must-reaching definition for the given register, or null if not available.
     */
    public AbstractNode getReachingDef(int reg) {
        return registerDefs.get(reg);
    }

    /**
     * Propagates the given MustReachingDefs into this one.
     * @return if this MustReachingDefs changed
     */
    public boolean propagate(MustReachingDefs must_reaching_defs) {
        Map<Integer, AbstractNode> newRegisterDefs = newMap();
        registerDefs.forEach((reg, node) -> {
            if (node == must_reaching_defs.getReachingDef(reg))
                newRegisterDefs.put(reg, node);
        });
        boolean changed = newRegisterDefs.size() != registerDefs.size();
        registerDefs = newRegisterDefs;
        return changed;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        registerDefs.forEach((reg, node) -> b.append("\n    v").append(reg).append(": node ").append(node.getIndex()).append(" (").append(node.getSourceLocation()).append(')'));
        return b.toString();
    }
}
