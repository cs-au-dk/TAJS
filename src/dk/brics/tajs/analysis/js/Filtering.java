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

package dk.brics.tajs.analysis.js;

import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.jsnodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.TypeofNode;
import dk.brics.tajs.flowgraph.jsnodes.UnaryOperatorNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectProperty;
import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.Restriction;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Pair;

import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Filters dataflow using control sensitivity assumptions.
 */
public class Filtering {

    private Solver.SolverInterface c;

    private PropVarOperations pv;

    public Filtering(Solver.SolverInterface c) {
        this.c = c;
        this.pv = c.getAnalysis().getPropVarOperations();
    }

    /**
     * Restricts the value of the given register.
     */
    private boolean assumeRegisterSatisfies(int reg, Restriction restriction) {
        Value regval = c.getState().readRegister(reg);
        regval = UnknownValueResolver.getRealValue(regval, c.getState()); // TODO: limits use of polymorphic values? (same for other calls to UnknownValueResolver below)
        regval = restriction.restrict(regval);
        if (regval.isNotPresentNotAbsent() && !Options.get().isPropagateDeadFlow()) {
            c.getState().setToBottom();
            return true;
        }
        c.getState().writeRegister(reg, regval, false);
        return false;
    }

    /**
     * Restricts the value of the register that are equal to the given memory location.
     */
    private boolean assumeRegistersEqualToSatisfy(ObjectLabel objlabel, PKey pkey, Restriction restriction) {
        for (int reg : c.getState().getMustEquals().getMustEquals(objlabel, pkey))
            if (c.getState().isRegisterDefined(reg))
                if (assumeRegisterSatisfies(reg, restriction))
                    return true;
        return false;
    }

    /**
     * Restricts the value of the given object property.
     * Accesses the object directly, without using the prototype chain.
     */
    private boolean assumeObjectPropertySatisfies(ObjectLabel objlabel, PKey pkey, Restriction restriction) {
        if (!objlabel.isSingleton())
            throw new AnalysisException("Expected singleton object label");
        if (assumeRegistersEqualToSatisfy(objlabel, pkey, restriction))
            return true;
        Value v = UnknownValueResolver.getProperty(objlabel, pkey, c.getState(), false);
        Value oldv = v;
        v = restriction.restrict(v);
        if (v != oldv)
            c.getState().getObject(objlabel, true).setProperty(pkey, v);
        return false;
    }

    /**
     * Variant of {@link #assumeObjectPropertySatisfies(ObjectLabel, PKey, Restriction)} that uses the prototype chain.
     * (This method is only needed for call nodes where the function is given as a property read.)
     */
    private boolean assumeObjectPropertySatisfies(Set<ObjectLabel> baseobjs, PKey propname, Restriction restriction) {
        Pair<Set<ObjectLabel>, Value> p = pv.readPropertyWithAttributesAndObjs(baseobjs, propname);
        Set<ObjectLabel> objs = p.getFirst();
        Value v = p.getSecond();
        if (objs.size() == 1 && objs.iterator().next().isSingleton()) {
            v = UnknownValueResolver.getRealValue(v, c.getState());
            Value oldv = v;
            v = restriction.restrict(v);
            if (v != oldv)
                pv.writePropertyWithAttributes(objs, propname, v, false, true);
            if (assumeRegistersEqualToSatisfy(objs.iterator().next(), propname, restriction))
                return true;
        }
        return false;
    }

    /**
     * Restricts the value of the given variable.
     * (This method is only needed because 'typeof x' doesn't read x like an ordinary variable read.)
     */
    private boolean assumeVariableSatisfies(String varname, Restriction restriction) {
        if (!varname.equals("this")) { // TODO: could also filter 'this'?
            Set<ObjectLabel> baseObjs = newSet();
            ObjectLabel baseObj;
            pv.readVariable(varname, baseObjs, true, false);
            if (baseObjs.size() == 1 && (baseObj = baseObjs.iterator().next()).isSingleton()) {
                Value v = UnknownValueResolver.getValue(ObjectProperty.makeOrdinary(baseObj, PKey.StringPKey.make(varname)), c.getState(), false); // FIXME: may not be same value as when typeof when executed! - example: typeof x==(x=null)
                Value oldv = v;
                v = restriction.restrict(v);
                if (v.isNotPresentNotAbsent() && !Options.get().isPropagateDeadFlow()) {
                    c.getState().setToBottom();
                    return true;
                }
                if (v != oldv)
                    c.getState().getObject(baseObj, true).setProperty(PKey.StringPKey.make(varname), v);
                if (assumeRegistersEqualToSatisfy(baseObjs.iterator().next(), PKey.StringPKey.make(varname), restriction))
                    return true;
            }
        }
        return false;
    }

    /**
     * Removes those base objects that do not have a matching property whose value satisfies the restriction.
     */
    private boolean assumeHasPropertyWhere(int basereg, String propname, Restriction restriction) {
        for (ObjectProperty base : c.getState().getMustEquals().getMustEquals(basereg)) {
            if (base.getObjectLabel().getKind() == ObjectLabel.Kind.STRING)
                continue; // String objects have their own [[GetOwnProperty]]
            Value v = UnknownValueResolver.getProperty(base.getObjectLabel(), base.getPropertyName(), c.getState(), false);
            Value oldv = v;
            Set<ObjectLabel> objs = newSet(v.getObjectLabels());
            v = v.restrictToNotObject();
            for (ObjectLabel objlabel : objs) {
                // does obj have a property named propname whose value satisfies the restriction? if maybe yes, keep it
                Value v2 = pv.readPropertyWithAttributes(Collections.singleton(objlabel), propname); // TODO: measure time, this is repeating much the work done by the ordinary transfer function earlier... - maybe just omit this reduction?
                v2 = UnknownValueResolver.getRealValue(v2, c.getState());
                if (!restriction.restrict(v2).isNotPresentNotAbsent())
                    v = v.joinObject(objlabel);
            }
            if (v.isNotPresentNotAbsent() && !Options.get().isPropagateDeadFlow()) {
                c.getState().setToBottom();
                return true;
            }
            if (v != oldv)
                c.getState().getObject(base.getObjectLabel(), true).setProperty(base.getPropertyName(), v);
        }
        return false;
    }

    /**
     * Assumes that the values of the given registers are equal.
     * @param strict use strict equality if set, otherwise non-strict
     * @param negated use not-equals if set
     * @return true if the state was set to bottom
     */
    private boolean assumeEqual(int reg1, int reg2, boolean strict, boolean negated) {
        if (c.getState().getMustReachingDefs().getReachingDef(reg2) instanceof TypeofNode) {
            //swap sides
            int t = reg1;
            reg1 = reg2;
            reg2 = t;
        }
        Value val1 = c.getState().readRegister(reg1);
        val1 = UnknownValueResolver.getRealValue(val1, c.getState());
        Value val2 = c.getState().readRegister(reg2);
        val2 = UnknownValueResolver.getRealValue(val2, c.getState());
        AbstractNode arg1def = c.getState().getMustReachingDefs().getReachingDef(reg1);
        if (arg1def instanceof TypeofNode && val2.isMaybeSingleStr() && !val2.isMaybeOtherThanStr()) {
            // typeof ... == "..."
            TypeofNode typeofNode = (TypeofNode) arg1def;
            Restriction r = Restriction.typeofToRestriction(val2.getStr());
            if (r != null) {
                if (negated)
                    r = r.negate();
                if (r != null) {
                    if (typeofNode.isVariable())
                        return assumeVariableSatisfies(typeofNode.getVariableName(), r);
                    else
                        return assume(typeofNode.getArgRegister(), r);
                }
            }
        }
        Restriction.Kind k = strict ? Restriction.Kind.STRICT_EQUAL : Restriction.Kind.LOOSE_EQUAL;
        Restriction r1 = new Restriction(k).set(val2);
        Restriction r2 = new Restriction(k).set(val1);
        if (negated) {
            r1 = r1.negate();
            r2 = r2.negate();
        }
        return assume(reg1, r1) || assume(reg2, r2);
    }

    /**
     * Removes those objects where the specified property is absent.
     * The prototype chain is taken into account.
     * @param negated remove objects where the property is present (instead of absent)
     * @return true if the state was set to bottom
     */
    private boolean assumeIn(int propreg, int objreg, boolean negated) {
        Value propval = c.getState().readRegister(propreg);
        propval = UnknownValueResolver.getRealValue(propval, c.getState());
        if (propval.isMaybeOtherThanStrOrSymbol())
            return false; // other values involve coercion, which we don't want to mess with here
        for (ObjectProperty base : c.getState().getMustEquals().getMustEquals(objreg)) {
            if (base.getObjectLabel().getKind() == ObjectLabel.Kind.STRING)
                continue; // String objects are special, ignore them here
            Value v = UnknownValueResolver.getProperty(base.getObjectLabel(), base.getPropertyName(), c.getState(), false);
            v = UnknownValueResolver.getRealValue(v, c.getState());
            Value oldv = v;
            Set<ObjectLabel> objs = newSet(v.getObjectLabels());
            v = v.restrictToNotObject();
            for (ObjectLabel objlabel : objs) {
                // does obj have a property named propname whose value satisfies the restriction? if maybe yes, keep it
                Value v2 = pv.readPropertyWithAttributes(Collections.singleton(objlabel), propval);
                v2 = UnknownValueResolver.getRealValue(v2, c.getState());
                if (negated ? v2.isMaybeAbsent() : v2.isMaybePresent())
                    v = v.joinObject(objlabel);
            }
            if (v.isNotPresentNotAbsent() && !Options.get().isPropagateDeadFlow()) {
                c.getState().setToBottom();
                return true;
            }
            if (v != oldv)
                c.getState().getObject(base.getObjectLabel(), true).setProperty(base.getPropertyName(), v);
        }
        return false;
    }

    /**
     * Assumes the given register satisfies the restriction.
     * Unlike {@link #assumeRegisterSatisfies(int, Restriction)}, this method also handles additional restrictions derived using must-reaching definitions and must-equals information.
     * @return true if the state was set to bottom
     */
    private boolean assume(int reg, Restriction restriction) {
        if (Options.get().isNoFilteringEnabled() || Options.get().isControlSensitivityDisabled())
            return false;

        // restrict the register itself
        if (assumeRegisterSatisfies(reg, restriction))
            return true;

        // restrict all must-equals object properties
        for (ObjectProperty objprop : c.getState().getMustEquals().getMustEquals(reg))
            if (assumeObjectPropertySatisfies(objprop.getObjectLabel(), objprop.getPropertyName(), restriction))
                return true;

        AbstractNode def = c.getState().getMustReachingDefs().getReachingDef(reg);

        if (def instanceof ReadPropertyNode) {

            // register defined at ReadPropertyNode, remove those base objects that do not have a matching property whose value satisfies the restriction
            ReadPropertyNode n = (ReadPropertyNode) def;
            if (n.isPropertyFixed()) // dynamic property reads may involve toString coercion, which we don't want to mess with here
                if (assumeHasPropertyWhere(n.getBaseRegister(), n.getPropertyString(), restriction))
                    return true;

        } else if (restriction.getKind() == Restriction.Kind.TRUTHY || restriction.getKind() == Restriction.Kind.FALSY) {

            if (def instanceof UnaryOperatorNode) { // recognize negations
                UnaryOperatorNode n = (UnaryOperatorNode) def;
                if (n.getOperator() == UnaryOperatorNode.Op.NOT) {
                    Restriction r = restriction.negate();
                    if (r != null)
                        assume(n.getArgRegister(), r);
                }

            } else if (def instanceof BinaryOperatorNode) {
                boolean negated = restriction.getKind() == Restriction.Kind.FALSY;
                BinaryOperatorNode n = (BinaryOperatorNode) def;
                switch (n.getOperator()) {
                    case NE:
                        return assumeEqual(n.getArg1Register(), n.getArg2Register(), false, !negated);
                    case SNE:
                        return assumeEqual(n.getArg1Register(), n.getArg2Register(), true, !negated);
                    case EQ:
                        return assumeEqual(n.getArg1Register(), n.getArg2Register(), false, negated);
                    case SEQ:
                        return assumeEqual(n.getArg1Register(), n.getArg2Register(), true, negated);
                    case IN:
                        return assumeIn(n.getArg1Register(), n.getArg2Register(), negated);
                    case INSTANCEOF:
                        // TODO
                        break;
                    // TODO: recognize other binary operators?
                }
            }
            // TODO: recognize more operations? x.hasOwnProperty(y)? Object.is?
        }

        return false;
    }

    /**
     * Assumes the value of the given register is truthy.
     * @return true if the state was set to bottom
     */
    public boolean assumeTruthy(int reg) {
        return assume(reg, new Restriction(Restriction.Kind.TRUTHY));
    }

    /**
     * Assumes the value of the given register is falsy.
     * @return true if the state was set to bottom
     */
    public boolean assumeFalsy(int reg) {
        return assume(reg, new Restriction(Restriction.Kind.FALSY));
    }

    /**
     * Assumes the value of the given register is a function.
     * @return true if the state was set to bottom
     */
    public boolean assumeFunction(int reg) {
        return assume(reg, new Restriction(Restriction.Kind.FUNCTION));
    }

    /**
     * Assumes the value of the given register is not null and not undefined.
     * @return true if the state was set to bottom
     */
    public boolean assumeNotNullUndef(int reg) {
        return assume(reg, new Restriction(Restriction.Kind.NOT_NULL_UNDEF));
    }

    /**
     * Variant of {@link #assumeFunction(int)} for call nodes where the function is given as a property read.
     */
    public boolean assumeFunction(int basereg, Set<ObjectLabel> baseobjs, String propname) {
        if (propname == null || Options.get().isNoFilteringEnabled() || Options.get().isControlSensitivityDisabled())
            return false;

        Restriction restriction = new Restriction(Restriction.Kind.FUNCTION);

        // the property can be restricted (provided that strong update is possible)
        if (assumeObjectPropertySatisfies(baseobjs, PKey.StringPKey.make(propname), restriction))
            return true;

        // if the base object was read from a variable, then if its value is an object, that object must have a property named propname whose value maybe satisfies the restriction
        if (assumeHasPropertyWhere(basereg, propname, restriction))
            return true;

        return false;
    }

    /**
     * Assumes the value of the register is equal to the given one.
     */
    public void assumeRegisterEquals(int reg, Value value) {
        assume(reg, new Restriction(Restriction.Kind.STRICT_EQUAL).set(value));
    }
}
