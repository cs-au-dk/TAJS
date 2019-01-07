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

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.blendedanalysis.BlendedAnalysisOptions;
import dk.brics.tajs.blendedanalysis.InstructionComponent;
import dk.brics.tajs.blendedanalysis.dynamic.JalangiRefiner;
import dk.brics.tajs.blendedanalysis.dynamic.JalangiRefinerUtilities;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

public class BlendedAnalysisManager {

    private Solver.SolverInterface c;

    private JalangiRefiner jalangiRefiner;

    private JalangiRefinerUtilities jalangiRefinerUtilities;

    private Map<BlendedAnalysisQuery, Collection<Value>> queryCache;

    public BlendedAnalysisManager() {
        jalangiRefinerUtilities = new JalangiRefinerUtilities();
        jalangiRefiner = new JalangiRefiner(jalangiRefinerUtilities);
        queryCache = newMap();
    }
    /**
     * Returns the meet of two collections of values.
     */
    private Collection<Value> meetValuesWithUnboxing(Collection<Value> refinedValues, Value soundValue) {
        Set<Value> intersectedValues = newSet();
        for(Value refinedValue : refinedValues) {
            Value intersectedValue = jalangiRefinerUtilities.meetWithUnboxing(refinedValue, soundValue);
            if (!intersectedValue.isNone()) {
                intersectedValues.add(intersectedValue);
            }
        }

        return intersectedValues;
    }

    public void setSolverInterface(Solver.SolverInterface c) {
        this.c = c;
        jalangiRefinerUtilities.setSolverInterface(c);
    }

    public boolean isReachable(AbstractNode n) {
        return jalangiRefinerUtilities.isReachable(n);
    }

    /**
     * Solve the query (if not disallowed by BlendedAnalysisOptions) and return the query result 'meet' the sound default provided by the query.
     */
    public Collection<Value> solveQuery(Value soundDefault, AbstractNode n, Supplier<BlendedAnalysisQuery> querySupplier) {
        if (!blendedAnalysisAllowedAtSourceLocation(n.getSourceLocation())) {
            return singleton(soundDefault);
        }

        BlendedAnalysisQuery query = querySupplier.get();
        if (queryCache.containsKey(query)) {
            return queryCache.get(query);
        }
        Collection<Value> queryResult = jalangiRefiner.solveQuery(query);
        Collection<Value> intersect = meetValuesWithUnboxing(queryResult, query.getSoundDefault());

        if (BlendedAnalysisOptions.get().isDisableRefineToBottom() && Value.join(intersect).isNone()) {
            return singleton(query.getSoundDefault());
        }

        if (n instanceof WritePropertyNode && ((WritePropertyNode) n).isPropertyFixed() && Value.join(intersect).isNone()) {
            // Jalangi does not create writeproperty entries for properties written in object literal. meaning that { foo : 2 }
            // does not have a entry for that property write, but TAJS have a WritePropertyNode for that write.
            return singleton(soundDefault);
        }

        if (query.getInstructionComponent().isBase() && (
                n instanceof WritePropertyNode || n instanceof ReadPropertyNode)) {
            if (query.getSoundDefault().isMaybeUndef() && Value.join(intersect).isNone()) {
                // If a JavaScript exception has been thrown, the value log will not contain an entry for the property read or write
                intersect.add(Value.makeUndef());
            }
        }
        queryCache.put(query, intersect);
        return intersect;
    }

    /**
     * Attempts to solveQuery the base for the given write operation.
     */
    public Collection<Value> getBase(Value defaultres, AbstractNode n, State state) {
        return solveQuery(defaultres, n,
                () -> new BlendedAnalysisQuery(n, InstructionComponent.mkBase(), newSet(), UnknownValueResolver.getRealValue(defaultres, state))
        );
    }

    /**
     * Attempts to solveQuery the property name for the given write operation, relative to the given base.
     */
    public Collection<Value> getPropertyName(Value defaultres, Value base, AbstractNode n, State state) {
        return solveQuery(defaultres, n, () -> {
            Set<Constraint> constraints = newSet();
            constraints.add(new Constraint(InstructionComponent.mkBase(), base));

            return new BlendedAnalysisQuery(n, InstructionComponent.mkProperty(), constraints, UnknownValueResolver.getRealValue(defaultres, state));
        });
    }

    /**
     * Attempts to solveQuery the value for the given write operation, relative to the given base and propertyname.
     */
    public Collection<Value> getValue(Value defaultres, Value base, Value propertyName, AbstractNode n, State state) {
        return solveQuery(defaultres, n, () -> {
            Set<Constraint> constraints = newSet();
            if (base != null) {
                constraints.add(new Constraint(InstructionComponent.mkBase(), base));
            }
            if (propertyName != null) {
                constraints.add(new Constraint(InstructionComponent.mkProperty(), propertyName));
            }

            return new BlendedAnalysisQuery(n, InstructionComponent.mkTarget(), constraints, UnknownValueResolver.getRealValue(defaultres, state));
        });
    }

    /**
     * Attempts to solve the argument value for the given call, relative to the given function, base (if not null), and propertyName (if not null).
     */
    public Value getArg(Value defaultres, int argumentNumber, Value function, Value base, CallNode n, State state) {
        Collection<Value> queryResult = solveQuery(defaultres, n, () -> {
            Set<Constraint> constraints = newSet();
            if (base != null && !base.restrictToNotUndef().isNone() && !base.restrictToNotNull().isNone()) { // TODO: The abstract null check occurs due to TAJS_functions - should getThis on TAJS_functions not be undefined instead???
                constraints.add(new Constraint(InstructionComponent.mkBase(), UnknownValueResolver.getRealValue(base, state)));
            }
            constraints.add(new Constraint(InstructionComponent.mkTarget(), function));

            return new BlendedAnalysisQuery(n, InstructionComponent.mkArg(argumentNumber), constraints, UnknownValueResolver.getRealValue(defaultres, state));
        });

        return UnknownValueResolver.join(queryResult, state);
    }

    public boolean blendedAnalysisAllowedAtSourceLocation(SourceLocation sl) {
        return Options.get().isBlendedAnalysisEnabled() && BlendedAnalysisOptions.get().isBlendedAnalysisAtSourceLocationAllowed(sl);
    }

    public Value getVariableValue(Value defaultres, AbstractNode n, State state) {
        if (n instanceof ReadVariableNode) {
            String variableName = ((ReadVariableNode) n).getVariableName();
            if (jalangiRefinerUtilities.isTAJSVariableNotJalangiVariable(variableName)) {
                return defaultres;
            }
        }

        Collection<Value> queryResult = solveQuery(defaultres, n, () ->
                new BlendedAnalysisQuery(n, InstructionComponent.mkTarget(), newSet(), UnknownValueResolver.getRealValue(defaultres, state)));

        Value res = UnknownValueResolver.join(queryResult, state);
        if (n instanceof ReadVariableNode && defaultres.isMaybeAbsent()) {
            res = res.joinAbsent(); // Valuelogger cannot distinguish unreachable and absent variable
        }
        return res;
    }
}
