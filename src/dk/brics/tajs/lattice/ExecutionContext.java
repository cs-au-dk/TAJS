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

package dk.brics.tajs.lattice;

import dk.brics.tajs.util.AnalysisException;

import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Abstract execution context.
 */
public final class ExecutionContext implements Cloneable {

    private ScopeChain scope_chain;

    private Set<ObjectLabel> var_obj;

    private Value thisval;

    /**
     * Constructs a new execution context.
     * The new object gets ownership of the given sets.
     */
    public ExecutionContext(ScopeChain scope_chain, Set<ObjectLabel> var_obj, Value thisval) {
        this.scope_chain = scope_chain;
        this.var_obj = var_obj;
        this.thisval = thisval;
    }

    /**
     * Constructs a new empty context.
     */
    public ExecutionContext() {
        scope_chain = null;
        var_obj = newSet();
        thisval = Value.makeNone();
    }

    @Override
    public ExecutionContext clone() {
        return new ExecutionContext(scope_chain, newSet(var_obj), thisval);
    }

    /**
     * Checks whether all sets are empty.
     */
    public boolean isEmpty() {
        return scope_chain == null && var_obj.isEmpty() && thisval.isNone();
    }

    /**
     * Joins the given execution context into this one.
     *
     * @return true if this execution context is changed by the operation
     */
    public boolean add(ExecutionContext other) {
        ScopeChain new_scope_chain = ScopeChain.add(scope_chain, other.scope_chain);
        boolean scope_chain_changed = new_scope_chain != null && !new_scope_chain.equals(scope_chain);
        scope_chain = new_scope_chain;
        Value new_thisval = thisval.join(other.thisval);
        boolean this_changed = !new_thisval.equals(thisval);
        thisval = new_thisval;
        return scope_chain_changed | this_changed | var_obj.addAll(other.var_obj);
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
     * Returns the 'this' value of this execution context.
     */
    public Value getThis() {
        return thisval;
    }

    /**
     * Sets the 'this' value of this execution context.
     */
    public void setThis(Value new_this) {
        thisval = new_this;
    }

    /**
     * Sets the variable object of this execution context.
     */
    public void setVariableObject(Set<ObjectLabel> new_var_obj) {
        var_obj = new_var_obj;
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
        thisval = thisval.summarize(s);
    }

    /**
     * Replaces all occurrences of oldlabel by newlabel.
     */
    public void replaceObjectLabel(ObjectLabel oldlabel, ObjectLabel newlabel, Map<ScopeChain, ScopeChain> cache) {
        scope_chain = ScopeChain.replaceObjectLabel(scope_chain, oldlabel, newlabel, cache);
        var_obj = replaceObjectLabel(var_obj, oldlabel, newlabel);
        thisval = thisval.replaceObjectLabel(oldlabel, newlabel);
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

//    /**
//     * Replaces all object labels according to the given map.
//     */
//    public void replaceObjectLabels(Map<ObjectLabel, ObjectLabel> m, Map<ScopeChain, ScopeChain> cache) {
//        scope_chain = ScopeChain.replaceObjectLabels(scope_chain, m, cache);
//        var_obj = Renaming.apply(m, var_obj);
//        thisval = Renaming.apply(m, thisval);
//    }

//    /**
//     * Removes components in the other state from this state.
//     */
//    public void remove(ExecutionContext other) {
//        scope_chain = ScopeChain.remove(scope_chain, other.scope_chain);
//        var_obj.removeAll(other.var_obj);
//        thisval.removeAll(other.thisval);
//    }

    /**
     * Returns the set of object labels within the execution context.
     */
    public Set<ObjectLabel> getObjectLabels() {
        Set<ObjectLabel> objs = newSet();
        for (Set<ObjectLabel> ls : ScopeChain.iterable(scope_chain))
            objs.addAll(ls);
        objs.addAll(thisval.getObjectLabels());
        objs.addAll(var_obj);
        return objs;
    }

//    /**
//     * Checks whether the given execution context is equal to this one.
//     */
//    @Override
//    public boolean equals(Object obj) { // (currently not used)
//        if (obj == this)
//            return true;
//        if (!(obj instanceof ExecutionContext))
//            return false;
//        ExecutionContext e = (ExecutionContext) obj;
//        if ((scope_chain == null) != (e.scope_chain == null) ||
//                (scope_chain != null && !scope_chain.equals(e.scope_chain)))
//            return false;
//        return var_obj.equals(e.var_obj) && thisval.equals(e.thisval);
//    }
//
//    /**
//     * Computes the hash code for this execution context.
//     */
//    @Override
//    public int hashCode() { // (currently not used)
//        return (scope_chain != null ? scope_chain.hashCode() * 7 : 0) + var_obj.hashCode() * 3 + thisval.hashCode() * 11;
//    }

    /**
     * Returns a string representation of this execution context.
     */
    @Override
    public String toString() {
        return "ScopeChain=" + scope_chain + ", VariableObject=" + var_obj + ", this=" + thisval;
    }
}
