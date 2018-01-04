/*
 * Copyright 2009-2018 Aarhus University
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

package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.HostAPIs;
import dk.brics.tajs.flowgraph.TAJSFunctionName;
import dk.brics.tajs.lattice.HostObject;

/**
 * TAJS_-functions that serve as utility functions for the analysis.
 */
public class TAJSFunction implements HostObject {

    private TAJSFunctionName name;

    public TAJSFunction(TAJSFunctionName name) {
        assert name != null;
        this.name = name;
    }

    public TAJSFunctionName getName() {
        return name;
    }

    public HostAPIs getAPI() {
        return HostAPIs.TAJS;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TAJSFunction that = (TAJSFunction) o;

        return name == that.name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
