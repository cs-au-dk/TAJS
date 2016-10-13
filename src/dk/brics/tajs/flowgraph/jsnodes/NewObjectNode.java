/*
 * Copyright 2009-2016 Aarhus University
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
 * New object node.
 * <p>
 * <i>v</i> = {}
 */
public class NewObjectNode extends LoadNode {

    /**
     * New object node.
     *
     * @param result_reg The result register.
     * @param location   The source location.
     */
    public NewObjectNode(int result_reg, SourceLocation location) {
        super(result_reg, location);
    }

    @Override
    public String toString() {
        return "new[v" + getResultRegister() + "]";
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
