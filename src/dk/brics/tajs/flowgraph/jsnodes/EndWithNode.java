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

package dk.brics.tajs.flowgraph.jsnodes;

import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * End-with node.
 * <p>
 * with(<i>v</i>) { ... }
 */
public class EndWithNode extends Node {

    /**
     * Constructs a new end-with node.
     */
    public EndWithNode(SourceLocation location) {
        super(location);
    }

    @Override
    public String toString() {
        return "end-with";
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean isArtificial() {
        return true;
    }

    @Override
    public boolean canThrowExceptions() {
        return false;
    }
}
