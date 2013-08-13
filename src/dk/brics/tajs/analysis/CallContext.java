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

package dk.brics.tajs.analysis;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.ICallContext;

/**
 * Call context for context sensitive analysis.
 * <p>
 * This particular context sensitivity strategy distinguishes between abstract states that have different possible values of 'this'.
 * It also distinguishes between abstract states that have different values for the special function arguments (see {@link SpecialArgs}) and special registers.
 * <p>
 * Immutable.
 */
public final class CallContext implements ICallContext<CallContext> { 
	
	private static Logger logger = Logger.getLogger(CallContext.class); 

	private Set<ObjectLabel> thisval; // TODO: canonicalize?

	/**
	 * Value of special arguments in this context, or null if none.
	 */
	private Map<String,Value> specialargs;

	/**
	 * Value of special registers in this context, or null if none.
	 */
	private Map<Integer,Value> specialregisters;
	
    /**
     * The entry of the function where this call context belongs.
     * Note that for-in bodies are viewed as separate functions here.
     */
	private BasicBlock entry;
	
	/**
	 * Enclosing context.
	 * Used only at for-in bodies.
	 */
	private CallContext enclosing;
	
	/**
	 * Constructs a new initial context.
	 * @param callee_state callee state
	 * @param entry entry of the function where this call context belongs
	 */
	public CallContext(State callee_state, BasicBlock entry) {
		this.entry = entry;
		if (!Options.isContextSensitivityDisabled()) {
			if (entry.getFunction().isUsesThis()) {
				thisval = newSet(callee_state.readThisObjects());
			}
			if (logger.isDebugEnabled()) 
				logger.debug("creating initial CallContext");
		}
	}

	/**
	 * Constructs a new call context for an ordinary call.
     * @param callee_state callee state
     * @param entry entry of the function where this call context belongs
     * @param caller_context caller context, null if none
     * @param sa special arguments info, null if none
     */
	public CallContext(State callee_state, BasicBlock entry, SpecialArgs sa) {
		this(callee_state, entry);
		if (!Options.isContextSensitivityDisabled()) {
			if (sa != null)
				specialargs = sa.getSpecialArgValues(callee_state, entry.getFunction());
			// TODO: specialregs become empty here, i.e. context specialization is not passed through functions calls -- need to change that?
			if (logger.isDebugEnabled()) 
				logger.debug("creating ordinary CallContext with " + (thisval != null ? thisval.size() : 0) + " object labels" +
						(specialargs != null ? " and " + specialargs.size() + " special args" : "") +
						" at " + entry.getSourceLocation() + (thisval != null ? ": " + thisval : ""));
		}
	}

	/**
	 * Constructs a new call context for entering a for-in body.
	 * @param enclosing enclosing call context
	 * @param entry entry of the for-in body
	 * @param reg additional special register
	 * @param v value for the additional special register
	 */
	public CallContext(CallContext enclosing, BasicBlock entry, int reg, Value v) {
		thisval = enclosing.thisval;
		specialargs = enclosing.specialargs;
		this.enclosing = enclosing;
		this.entry = entry;
		specialregisters = (enclosing.specialregisters != null) ? newMap(enclosing.specialregisters) : dk.brics.tajs.util.Collections.<Integer,Value>newMap();
		specialregisters.put(reg, v);
		if (logger.isDebugEnabled()) 
			logger.debug("creating for-in CallContext with " + (thisval != null ? thisval.size() : 0) + " object labels" +
					(specialargs != null ? ", " + specialargs.size() + " special args" : "") + 
					(specialregisters != null ? ", " + specialregisters.size() + " special regs" : "") +
					" at " + entry.getSourceLocation() + (thisval != null ? ": " + thisval : ""));
	}

	@Override
	public BasicBlock getEntry() {
		return entry;
	}
	
	/**
	 * Enclosing context, only non-null for for-in specialized contexts.
	 */
	public CallContext getEnclosing() {
		return enclosing;
	}

	@Override
	public boolean equals(Object obj) {
    	 if (!(obj instanceof CallContext))
    		 return false;
		if (!Options.isContextSensitivityDisabled()) {
			CallContext c = (CallContext) obj;
			if (!entry.equals(c.entry))
				return false;
			if ((thisval == null) != (c.thisval == null))
				return false;
			if (thisval != null && !thisval.equals(c.thisval)) // using collection equality
				return false;
			if ((specialargs == null) != (c.specialargs == null))
				return false;
			if (specialargs != null && !specialargs.equals(c.specialargs)) // using collection equality
				return false;
			if ((specialregisters == null) != (c.specialregisters == null))
				return false;
			if (specialregisters != null && !specialregisters.equals(c.specialregisters)) // using collection equality
				return false;
		}
		return true;
	}

	@Override
	public int hashCode() {		
		// see equals
		return (thisval != null ? thisval.hashCode() : 0) * 17 +
				(specialargs == null ? 0 : specialargs.hashCode()) * 7 +
				(specialregisters != null ? specialregisters.hashCode() : 0) * 3;

	}

	@Override
	public String toString() {
		if (!Options.isContextSensitivityDisabled()) 
			return "{entry=" + entry.getIndex() + (thisval != null ? ", this=" + thisval : "") + (specialargs != null ? (", specialargs=" + specialargs) : "") +
                    (specialregisters == null ? "" : (", specialregisters=" + specialregisters)) + "}";
		return "<any>";
	}
}
