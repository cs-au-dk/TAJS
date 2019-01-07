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

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

/**
 * Event dispatcher node.
 * <p>
 * Must be the only node in its block. The block must have precisely one successor.
 */
public class EventDispatcherNode extends Node {

    /**
     * Different kinds of event dispatching.
     */
    public enum Type {

        /**
         * Content loaded event.
         */
        DOM_CONTENT_LOADED,

        /**
         * Load DOM event.
         */
        DOM_LOAD,

        /**
         * Unload DOM event.
         */
        DOM_UNLOAD,

        /**
         * Other DOM events than load and unload.
         */
        DOM_OTHER,

        /**
         * Events that are scheduled for asynchronous execution.
         */
        ASYNC
    }

    private Type type;

    /**
     * Constructs a new event dispatcher node.
     */
    public EventDispatcherNode(Type type, SourceLocation location) {
        super(location);
        this.type = type;
        setArtificial();
    }

    /**
     * Returns the event type.
     */
    public Type getType() {
        return type;
    }

    @Override
    public boolean canThrowExceptions() {
        return true;
    }

    @Override
    public String toString() {
        return "event-dispatcher <" + type.name().replace("DOM_", "") + ">";
    }

    @Override
    public void visitBy(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public void check(BasicBlock b) {
        if (!Options.get().isDOMEnabled() && !Options.get().isAsyncEventsEnabled())
            throw new AnalysisException("EventDispatcherNode found without DOM or AsyncEvents enabled: " + toString());
        if (b.getNodes().size() != 1)
            throw new AnalysisException("Node should have its own basic block: " + toString());
        if (b.getSuccessors().size() > 1)
            throw new AnalysisException("More than one successor for call node block: " + b);
    }
}
