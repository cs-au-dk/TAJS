/*
 * Copyright 2009-2013 Aarhus University
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

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.ICallNode;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

/**
 * Event dispatcher node.
 */
public class EventDispatcherNode extends Node implements ICallNode {

    // TODO: javadoc for enum and each type
    public enum Type { 
        LOAD, 
        UNLOAD, 
        OTHER, 
        SINGLE
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
    public boolean isConstructorCall() {
        return false;
    }

    @Override
    public int getResultRegister() {
        return AbstractNode.NO_VALUE;
    }

    @Override
	public String toString() {
		return "event-dispatcher <" + type + ">";
	}

	@Override
	public <ArgType> void visitBy(NodeVisitor<ArgType> v, ArgType a) {
		v.visit(this, a);
	}

	@Override
	public void check(BasicBlock b) {
        if (!Options.isDOMEnabled())
            throw new AnalysisException("EventDispatcherNode found without DOM enabled: " + toString());
	}
}
