/*
 * Copyright 2012 Aarhus University
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

import static dk.brics.tajs.util.Collections.newSet;

import java.util.LinkedList;
import java.util.Set;

import org.apache.log4j.Logger;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.ICallContext;
import dk.brics.tajs.util.Pair;

/**
 * Call context for context sensitive analysis.
 * <p>
 * This particular context sensitivity strategy distinguishes between abstract states that have different possible values of 'this'.
 * It also distinguishes between abstract states that have different values for the special function arguments (see {@link SpecialArgs}).
 */
public final class CallContext implements ICallContext<CallContext> { 
	
	private static Logger logger = Logger.getLogger(CallContext.class); 

	private Set<ObjectLabel> thisval; // TODO: canonicalize?

	/**
	 * Value of special arguments in this context, or null if none.
	 */
	private Set<Value> specialargs; // TODO: change from Set<Value> to Map<Integer,Value> (where the integer is the parameter number)?

    /**
     * Call nodes that should be treated context sensitively.
     */
    private LinkedList<Pair<AbstractNode, String>> beginForInNodes; // FIXME: discuss...
	
	/**
	 * Constructs a new initial context.
	 * @param callee_state callee state
	 * @param f callee function
	 */
	public CallContext(State callee_state, Function f) {
		this(callee_state, f, null, null, null, null); // caller_context and call_node are null when the initial context is constructed
	}

	/**
	 * Constructs a new context.
     * @param callee_state callee state
     * @param f callee function
     * @param caller_context caller context, null if none
     * @param call_node call node, null if none
     * @param property property name, null if none
     * @param sa special arguments info, null if none
     */
	public CallContext(State callee_state, Function f, CallContext caller_context, AbstractNode call_node, String property, SpecialArgs sa) {
		if (!Options.isContextSensitivityDisabled()) {
			thisval = newSet(callee_state.readThisObjects());
			if (sa != null)
				specialargs = sa.getSpecialArgValues(callee_state, f);
			if (call_node != null && property != null) {
                if (caller_context != null)
                    thisval = newSet(caller_context.thisval); // FIXME: looks strange that the thisval is overwritten here -- maybe split the constructor into two or more variants with a shared private one for the common parts?
                beginForInNodes = new LinkedList<>();
			    if (caller_context != null && caller_context.beginForInNodes != null)
				    beginForInNodes.addAll(caller_context.beginForInNodes);
                beginForInNodes.add(Pair.make(call_node, property));
            }
//			if (beginForInNodes.size() > K)
//				beginForInNodes.remove(0);
			if (logger.isDebugEnabled()) 
				logger.debug("creating CallContext with " + thisval.size() + " object labels " +
						"and " + (specialargs != null ? specialargs.size() : 0) + " special args " +
						"and " + (beginForInNodes != null ? beginForInNodes.size() : 0) + " property nodes " +
						"at " + f + " " + f.getSourceLocation() + ": " + thisval);
		}
	}

    /**
     * Constructs a new entry context from the current context.
     */
    private CallContext(Set<ObjectLabel> thisval, Set<Value> specialargs, LinkedList<Pair<AbstractNode, String>> beginForInNodes) {
        this.thisval = thisval;
        this.specialargs = specialargs;
        this.beginForInNodes = beginForInNodes;
    }

     @Override
	public boolean equals(Object obj) {
		if (!Options.isContextSensitivityDisabled()) {
			if (!(obj instanceof CallContext))
				return false;
			CallContext c = (CallContext) obj;
			if ((thisval == null) != (c.thisval == null))
				return false;
			if (thisval != null && !thisval.equals(c.thisval)) // using set equality
				return false;
			if ((specialargs == null) != (c.specialargs == null))
				return false;
			if (specialargs != null && !specialargs.equals(c.specialargs)) // using collection equality
				return false;
			if ((beginForInNodes == null) != (c.beginForInNodes == null))
				return false;
			if (beginForInNodes != null && !beginForInNodes.equals(c.beginForInNodes))
				return false;
		}
		// default: join
		return true;
	}

	@Override
	public int hashCode() {		
		// see equals
		return (thisval != null ? thisval.hashCode() : 0) * 17 + (specialargs == null ? 0 : specialargs.hashCode()) * 7
		+ (beginForInNodes != null ? beginForInNodes.hashCode() : 0) * 3;
		
	}

	@Override
	public String toString() {
		if (!Options.isContextSensitivityDisabled()) 
			return "{this=" + thisval.toString() + (specialargs != null ? (", specialargs=" + specialargs) : "") +
                    (beginForInNodes == null ? "" : (", beginForInNodes=" + beginForInNodes)) + "}";
		return "<any>";
	}

    /**
     * Peels a single layer of the for-in context sensitivity nodes off.
     */
    public CallContext peel() {
        if (beginForInNodes == null || beginForInNodes.size() <= 1)
            return new CallContext(thisval, specialargs, null); // One or zero BeginForInNodes in the context, so return the function entry context
        LinkedList<Pair<AbstractNode, String>> new_property_nodes = new LinkedList<>();
        // Peel off the innermost BeginForInNode in the context.
        for (int i = 0; i < beginForInNodes.size() - 1; i++) {
            new_property_nodes.add(beginForInNodes.get(i)); // TODO: get(i) in a LinkedList is inefficient
        }
        return new CallContext(thisval, specialargs, new_property_nodes);
    }

    /**
     * Returns true if the context is sensitive on the block that belongs to node.
     */
    public boolean isContextSensitiveOn(AbstractNode n) { // TODO: this method is always called on the BeginForInNode that matches a currentEndForIn node, so we always only check the first entry, right? if so, simplify the code
        if (beginForInNodes != null)
            for (Pair<AbstractNode, String> p : beginForInNodes)
                if (p.getFirst().equals(n))
                    return true;
        return false;
    }

	@Override
	public BlockAndContext<CallContext> toEntry(BasicBlock b) {
       if (beginForInNodes == null || !beginForInNodes.getLast().getFirst().getBlock().getFunction().equals(b.getFunction())) // TODO: what's the idea here?
		    return new BlockAndContext<>(b.getFunction().getEntry(), this); // go to the function entry , same context
       BasicBlock bb = beginForInNodes.getLast().getFirst().getBlock().getSingleSuccessor();
       if (b.getIndex() >= bb.getIndex()) // TODO: are we exploiting the block index order? (that seems vulnerable)
            return new BlockAndContext<>(bb, this);// TODO: what's the idea here?
       CallContext cc = this.peel();
       BasicBlock target = cc.beginForInNodes == null ? b.getFunction().getEntry() : cc.toEntry(bb).getBlock(); // TODO: what's the idea here?
       return new BlockAndContext<>(target, cc);
	}
}
