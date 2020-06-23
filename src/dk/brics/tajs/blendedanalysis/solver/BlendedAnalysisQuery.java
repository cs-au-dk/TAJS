/*
 * Copyright 2009-2020 Aarhus University
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

import java.net.URL;
import java.util.Objects;
import java.util.Set;

public class BlendedAnalysisQuery {

    /**
     *  Node, which blended analysis should be performed at.
     */
    private final AbstractNode node;

    /**
     * URL of the module to be filtered.
     */
    private final URL moduleUrl;

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
        this(node, null, ic, constraints, soundDefault);
    }

    public BlendedAnalysisQuery(URL moduleUrl, InstructionComponent ic, Set<Constraint> constraints, Value soundDefault) {
        this(null, moduleUrl, ic, constraints, soundDefault);
    }

    public BlendedAnalysisQuery(AbstractNode node, URL moduleUrl,
                                InstructionComponent ic, Set<Constraint> constraints,
                                Value soundDefault) {
        this.node = node;
        this.moduleUrl = moduleUrl;
        this.ic = ic;
        this.constraints = constraints;
        this.soundDefault = soundDefault;
        this.hashCode = Objects.hash(node, moduleUrl, ic, constraints, soundDefault);
    }

    public AbstractNode getNode() {
        return node;
    }

    public URL getModuleUrl() {
        return moduleUrl;
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

        if (!Objects.equals(node, that.node)) return false;
        if (!Objects.equals(moduleUrl, that.moduleUrl)) return false;
        if (!Objects.equals(ic, that.ic)) return false;
        if (!Objects.equals(constraints, that.constraints)) return false;
        return Objects.equals(soundDefault, that.soundDefault);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return "BlendedAnalysisQuery{" +
                "node=" + node +
                ", moduleUrl=" + moduleUrl +
                ", ic=" + ic +
                ", constraints=" + constraints +
                ", soundDefault=" + soundDefault +
                '}';
    }
}