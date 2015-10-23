/*
 * Copyright 2009-2015 Aarhus University
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

package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.FlowGraphFragment;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.solver.NodeAndContext;

import java.util.Map;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * Cache for the unevalizer.
 */
public class EvalCache {

    /**
     * A map from call sites to entries, where the call site is identified by the call node and context.
     */
    private Map<NodeAndContext<Context>, FlowGraphFragment> cache;

    /**
     * Constructs a new unevalizer cache.
     */
    public EvalCache() {
        cache = newMap();
    }

    /**
     * Stores the abstract value and associated code in the cache.
     */
    public void setCode(NodeAndContext<Context> nc, FlowGraphFragment extension) {
        cache.put(nc, extension);
    }

    /**
     * Returns the flow graph fragment, or null if not found.
     */
    public FlowGraphFragment getCode(NodeAndContext<Context> nc) {
        return cache.get(nc);
    }
}
