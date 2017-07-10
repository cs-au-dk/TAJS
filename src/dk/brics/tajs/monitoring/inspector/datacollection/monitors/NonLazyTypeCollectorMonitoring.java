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

package dk.brics.tajs.monitoring.inspector.datacollection.monitors;

import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.TypeCollector;

public class NonLazyTypeCollectorMonitoring extends DefaultAnalysisMonitoring {
    private final TypeCollector typeCollector;

    public NonLazyTypeCollectorMonitoring (TypeCollector typeCollector) {
        this.typeCollector = typeCollector;
    }

    @Override
    public void visitVariableOrProperty (String var, SourceLocation loc, Value value, Context context, State state) {
        value = UnknownValueResolver.getRealValue(value, state);
        typeCollector.record(var, loc, value, context);
    }
}
