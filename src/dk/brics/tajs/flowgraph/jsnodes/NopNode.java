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

package dk.brics.tajs.flowgraph.jsnodes;

import dk.brics.tajs.flowgraph.SourceLocation;

/**
 * Nop node.
 */
public class NopNode extends Node {

    private final String text;

    /**
     * Constructs a new nop node.
     */
    public NopNode(SourceLocation location) {
        this(null, location);
    }

    /**
     * Constructs a new nop node, with some descriptive text.
     */
    public NopNode(String text, SourceLocation location) {
        super(location);
        this.text = text;
    }

    @Override
    public String toString() {
        if (text != null) {
            return "nop(" + text + ")";
        }
        return "nop";
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean canThrowExceptions() {
        return false;
    }
}
