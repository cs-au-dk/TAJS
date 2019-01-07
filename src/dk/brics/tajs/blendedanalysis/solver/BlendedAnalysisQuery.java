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

package dk.brics.tajs.blendedanalysis.solver;

import dk.brics.tajs.blendedanalysis.InstructionComponent;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.Value;

import java.util.Set;

public class BlendedAnalysisQuery {

    /**
     *  Node, which blended analysis should be performed at.
     */
    private final AbstractNode node;

    /**
     *  Which part of the node, do we want to increase precision for, using blended analysis.
     */
    private final InstructionComponent ic;

    /**
     *  A set of constraints to help filter value log entries in the blended analysis.
     */
    private final Set<Constraint> constraints;

    /**
     *  Sound solution for the query, if we should not use blended analysis anyway.
     */
    private final Value soundDefault;

    /**
     *  Cached hashcode for immutable instance.
     */
    private final int hashCode;

    public BlendedAnalysisQuery(AbstractNode node, InstructionComponent ic, Set<Constraint> constraints, Value soundDefault) {
        this.node = node;
        this.ic = ic;
        this.constraints = constraints;
        this.soundDefault = soundDefault;
        this.hashCode = calculateHashCode();
    }

    public AbstractNode getNode() {
        return node;
    }

    public InstructionComponent getInstructionComponent() {
        return ic;
    }

    public Set<Constraint> getConstraints() {
        return constraints;
    }

    public Value getSoundDefault() {
        return soundDefault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlendedAnalysisQuery that = (BlendedAnalysisQuery) o;

        if (node != null ? !node.equals(that.node) : that.node != null) return false;
        if (ic != null ? !ic.equals(that.ic) : that.ic != null) return false;
        if (constraints != null ? !constraints.equals(that.constraints) : that.constraints != null) return false;
        return soundDefault != null ? soundDefault.equals(that.soundDefault) : that.soundDefault == null;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    private int calculateHashCode() {
        int result = node != null ? node.hashCode() : 0;
        result = 31 * result + (ic != null ? ic.hashCode() : 0);
        result = 31 * result + (constraints != null ? constraints.hashCode() : 0);
        result = 31 * result + (soundDefault != null ? soundDefault.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BlendedAnalysisQuery{" +
                "node=" + node +
                ", ic=" + ic +
                ", constraints=" + constraints +
                ", soundDefault=" + soundDefault +
                '}';
    }
}