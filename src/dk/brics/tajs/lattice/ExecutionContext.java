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

package dk.brics.tajs.lattice;

import static dk.brics.tajs.util.Collections.newSet;

import java.util.Map;
import java.util.Set;

import dk.brics.tajs.util.AnalysisException;

/**
 * Abstract execution context.
 */
public final class ExecutionContext implements Cloneable { // TODO: move all this into BlockState?
	
	private ScopeChain scope_chain;
	
	private Set<ObjectLabel> var_obj; // TODO: do we really need the variable object explicitly in ExecutionContext? or can we always just take the first object from the scope chain? (see 'with'...)
	
	private Set<ObjectLabel> this_obj;

	/**
	 * Constructs a new execution context.
	 * The new object gets ownership of the given sets.
	 */
	public ExecutionContext(ScopeChain scope_chain, Set<ObjectLabel> var_obj, Set<ObjectLabel> this_obj) {
		this.scope_chain = scope_chain;
		this.var_obj = var_obj;
		this.this_obj = this_obj;
	}
	
	/**
	 * Constructs a new empty context.
	 */
	public ExecutionContext() {
		scope_chain = null;
		var_obj = newSet();
		this_obj = newSet();
	}
	
	@Override
	public ExecutionContext clone() {
		return new ExecutionContext(scope_chain, newSet(var_obj), newSet(this_obj));
	}

	/**
	 * Checks whether all sets are empty.
	 */
	public boolean isEmpty() {
		return scope_chain == null && var_obj.isEmpty() && this_obj.isEmpty();
	}
	
	/**
	 * Joins the given execution context into this one.
	 * @return true if this execution context is changed by the operation
	 */
	public boolean add(ExecutionContext other) {
		ScopeChain new_scope_chain = ScopeChain.add(scope_chain, other.scope_chain);
		boolean scope_chain_changed = new_scope_chain != null && !new_scope_chain.equals(scope_chain);
		scope_chain = new_scope_chain;
		return scope_chain_changed | var_obj.addAll(other.var_obj) | this_obj.addAll(other.this_obj);
	}
	
	/**
	 * Returns the scope chain of this execution context.
	 */
	public ScopeChain getScopeChain() {
		return scope_chain;
	}

	/**
	 * Returns the variable object of this execution context.
	 */
	public Set<ObjectLabel> getVariableObject() {
		return var_obj;
	}
	
	/**
	 * Returns the 'this' object of this execution context.
	 */
	public Set<ObjectLabel> getThisObject() {
		return this_obj;
	}
	
	/**
	 * Pushes a new item onto the scope chain.
	 */
	public void pushScopeChain(Set<ObjectLabel> objlabels) {
		scope_chain = ScopeChain.make(objlabels, scope_chain);
	}
	
	/**
	 * Pops the top item off the scope chain.
	 */
	public void popScopeChain() {
		if (scope_chain == null)
			throw new AnalysisException("popScopeChain while chain is empty");
        scope_chain = scope_chain.next();
	}

	/**
	 * Summarizes this execution context.
	 */
	public void summarize(Summarized s) {
		scope_chain = ScopeChain.summarize(scope_chain, s);
		var_obj = s.summarize(var_obj);
		this_obj = s.summarize(this_obj);
	}

	/**
	 * Replaces all occurrences of oldlabel by newlabel.
	 */
	public void replaceObjectLabel(ObjectLabel oldlabel, ObjectLabel newlabel, Map<ScopeChain,ScopeChain> cache) {
		scope_chain = ScopeChain.replaceObjectLabel(scope_chain, oldlabel, newlabel, cache);
		var_obj = replaceObjectLabel(var_obj, oldlabel, newlabel);
		this_obj = replaceObjectLabel(this_obj, oldlabel, newlabel); 
	}
	
	/**
	 * Replaces all occurrences of oldlabel by newlabel.
	 */
	private static Set<ObjectLabel> replaceObjectLabel(Set<ObjectLabel> objlabels, ObjectLabel oldlabel, ObjectLabel newlabel) {
		Set<ObjectLabel> res = newSet();
		for (ObjectLabel objlabel : objlabels)
			res.add(objlabel.equals(oldlabel) ? newlabel : objlabel);
		return res;
	}
	
	/**
	 * Replaces all object labels according to the given map.
	 */
	public void replaceObjectLabels(Map<ObjectLabel, ObjectLabel> m, Map<ScopeChain,ScopeChain> cache) {
		scope_chain = ScopeChain.replaceObjectLabels(scope_chain, m, cache);
		var_obj = Renaming.apply(m, var_obj);
		this_obj = Renaming.apply(m, this_obj); 
	}
	
	/**
	 * Removes components in the other state from this state.
	 */
	public void remove(ExecutionContext other) {
		scope_chain = ScopeChain.remove(this.scope_chain, other.scope_chain);
		var_obj.removeAll(other.var_obj);
		this_obj.removeAll(other.this_obj);
	}
		
	/**
	 * Returns the set of object labels within the execution context.
	 */
	public  Set<ObjectLabel> getObjectLabels() {
		Set<ObjectLabel> objs = newSet();
		for (Set<ObjectLabel> ls : ScopeChain.iterable(scope_chain))
			objs.addAll(ls);
		objs.addAll(this_obj);
		objs.addAll(var_obj);
		return objs;
	}
	
	/**
	 * Checks whether the given execution context is equal to this one.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ExecutionContext))
			return false;
		ExecutionContext e = (ExecutionContext)obj;
		if ((scope_chain == null) != (e.scope_chain == null) || 
				(scope_chain != null && !scope_chain.equals(e.scope_chain)))
			return false;
		return  var_obj.equals(e.var_obj) && this_obj.equals(e.this_obj);
	}

	/**
	 * Computes the hash code for this execution context.
	 */
	@Override
	public int hashCode() {
		return (scope_chain != null ? scope_chain.hashCode() * 7 : 0) + var_obj.hashCode() * 3 + this_obj.hashCode() * 11;
	}

	/**
	 * Returns a string representation of this execution context.
	 */
	@Override
	public String toString() {
		return "ScopeChain=" + scope_chain + ", VariableObject=" + var_obj + ", this=" + this_obj;
	}
}
