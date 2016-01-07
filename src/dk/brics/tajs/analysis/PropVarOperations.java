package dk.brics.tajs.analysis;

import dk.brics.tajs.lattice.Bool;
import dk.brics.tajs.lattice.Obj;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.PropertyReference;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Operations for accessing object properties and variables in abstract states.
 */
public class PropVarOperations {

    private static Logger log = Logger.getLogger(PropVarOperations.class);

    private Solver.SolverInterface c;

    /**
     * Constructs a new PropVarOperations object.
     */
    public PropVarOperations() {
    }

    /**
     * Initializes the connection to the solver.
     */
    public void setSolverInterface(Solver.SolverInterface c) {
        this.c = c;
    }

    /**
     * 8.6.2.1 [[Get]]
     * Returns the value of the given property in the given objects.
     * The internal prototype chains are used.
     * Absent is converted to undefined. Attributes are set to bottom.
     */
    public Value readPropertyValue(Collection<ObjectLabel> objlabels, String propertyname) {
        return readPropertyValue(objlabels, Value.makeTemporaryStr(propertyname));
    }

    /**
     * 8.6.2.1 [[Get]]
     * Returns the value of the given property in the given objects.
     * The internal prototype chains are used.
     * Absent is converted to undefined. Attributes are set to bottom.
     */
    public Value readPropertyValue(Collection<ObjectLabel> objlabels, Str propertystr) {
        Value v = readPropertyRaw(objlabels, propertystr, false);
        if (v.isMaybeAbsent())
            v = v.restrictToNotAbsent().joinUndef();
        v = v.setBottomPropertyData();
        if (log.isDebugEnabled())
            log.debug("readPropertyValue(" + objlabels + "," + propertystr + ") = " + v);
        return v;
    }

    /**
     * Returns the value of the given property in the given objects.
     * The internal prototype chains are used.
     */
    public Value readPropertyWithAttributes(Collection<ObjectLabel> objlabels, String propertyname) {
        return readPropertyWithAttributes(objlabels, Value.makeTemporaryStr(propertyname));
    }

    /**
     * Returns the value of the given property in the given objects.
     * The internal prototype chains are used.
     */
    public Value readPropertyWithAttributes(Collection<ObjectLabel> objlabels, Str propertystr) {
        Value v = readPropertyRaw(objlabels, propertystr, false);
        if (log.isDebugEnabled())
            log.debug("readPropertyWithAttributes(" + objlabels + "," + propertystr + ") = " + v);
        return v;
    }

    /**
     * Returns the value of the given property in the given objects.
     * The internal prototype chains are used.
     *
     * @param only_attributes if set, only attributes (incl. pseudo-attributes) are considered
     */
    private Value readPropertyRaw(Collection<ObjectLabel> objlabels, Str propertystr, boolean only_attributes) {
        Collection<Value> values = newList();
        Collection<ObjectLabel> ol = objlabels;
        Set<ObjectLabel> visited = newSet();
        while (!ol.isEmpty()) {
            Set<ObjectLabel> ol2 = newSet();
            for (ObjectLabel l : ol)
                if (!visited.contains(l)) {
                    visited.add(l);
                    Value v = readPropertyDirect(l, propertystr);
                    // String objects have their own [[GetOwnProperty]], see 15.5.5.2 in the ECMAScript 5 standard.
/*                    if (v.isMaybeAbsent() && l.getKind() == Kind.STRING && (propertystr.isMaybeStrOnlyUInt() || (propertystr.getStr() != null && Strings.isArrayIndex(propertystr.getStr())))) { // TODO: review (note that this is new in ES5)
                        Value tmp = null;
                        Value internal_value = UnknownValueResolver.getInternalValue(l, this, true);
                        Value lenV = UnknownValueResolver.getProperty(l, "length", this, true).restrictToNum();
                        String value = internal_value.getStr();
                        Double obj_len = lenV.getNum();
                        Integer prop_len = propertystr.getStr() == null ? -1 : Integer.valueOf(propertystr.getStr());
                        if (obj_len != null && value != null && prop_len > -1 && prop_len < obj_len)
                            tmp = Value.makeStr(value.substring(prop_len, prop_len + 1));
                        else if (obj_len != null && prop_len > -1 && prop_len >= obj_len)
                            tmp = Value.makeUndef();
                        if (tmp != null)
                            v = v.isNotPresent() ? tmp : v.join(tmp);
                }*/
                    Value v2 = v.restrictToNotAbsent();
                    if (!v2.isNone()) {
                        if (only_attributes)
                            v2 = v2.restrictToAttributes();
                        values.add(v2);
                    }
                    if (v.isMaybeAbsent() || v.isNotPresent()) {
                        Value proto = UnknownValueResolver.getInternalPrototype(l, c.getState(), false);
                        ol2.addAll(proto.getObjectLabels());
                        if (proto.isMaybeAbsent() || proto.isMaybeNull()) {
                            values.add(Value.makeAbsent());
                        }
                    }
                }
            ol = ol2;
        }
        return UnknownValueResolver.join(values, c.getState());
    }

    /**
     * Returns the value of the given property in the objects.
     * The internal prototype chains and scope chains are <em>not</em> used.
     */
    public Value readPropertyDirect(Collection<ObjectLabel> objlabels, String propertyname) {
        State state = c.getState();
        Collection<Value> values = newList();
        for (ObjectLabel obj : objlabels)
            values.add(UnknownValueResolver.getProperty(obj, propertyname, state, true));
        Value v = UnknownValueResolver.join(values, state);
        if (log.isDebugEnabled())
            log.debug("readPropertyDirect(" + objlabels + "," + propertyname + ") = " + v);
        return v;
    }

    /**
     * Returns the join of the values of the given properties of an object.
     * The internal prototype chains and scope chains are <em>not</em> used.
     */
    private Value readPropertyDirect(ObjectLabel objlabel, Str propertystr) {
        State state = c.getState();
        if (propertystr.isMaybeSingleStr())
            return UnknownValueResolver.getProperty(objlabel, propertystr.getStr(), state, true);
        Collection<Value> values = newList();
        if (propertystr.isMaybeStrSomeUInt())
            values.add(UnknownValueResolver.getDefaultArrayProperty(objlabel, state));
        if (propertystr.isMaybeStrSomeNonUInt())
            values.add(UnknownValueResolver.getDefaultNonArrayProperty(objlabel, state));
        // the calls to UnknownValueResolver above have materialized all relevant properties
        for (String propertyname : state.getObject(objlabel, false).getPropertyNames())
            if (propertystr.isMaybeStr(propertyname))
                values.add(UnknownValueResolver.getProperty(objlabel, propertyname, state, false));
        return UnknownValueResolver.join(values, state);
    }

    /**
     * [[HasProperty]].
     * The internal prototype chains are used.
     */
    private Bool hasPropertyRaw(Collection<ObjectLabel> objlabels, String propertyname) {
        Value v = readPropertyRaw(objlabels, Value.makeTemporaryStr(propertyname), true);
        boolean maybe_present = v.isMaybePresent();
        boolean maybe_absent = v.isMaybeAbsent();
        return maybe_present ? (maybe_absent ? Value.makeAnyBool() : Value.makeBool(true)) : (maybe_absent ? Value.makeBool(false) : Value.makeNone());
    }

    /**
     * Checks whether the given property is present in the given objects.
     * The internal prototype chains are used.
     */
    public Bool hasProperty(Collection<ObjectLabel> objlabels, String propertyname) {
        Bool b = hasPropertyRaw(objlabels, propertyname);
        if (log.isDebugEnabled())
            log.debug("hasProperty(" + objlabels + "," + propertyname + ") = " + b);
        return b;
    }

    /**
     * 8.6.2.3 [[CanPut]] Checks whether the given property can be assigned in the given object.
     * The internal prototype chains are used.
     *
     * @param propertystr description of the property names to consider (assumed not to be a single string)
     */
    private Bool canPut(ObjectLabel objlabel, Str propertystr) {
        /*
        ReadOnly properties:
        - the function name in scope object of a function expression
        - 'length' of built-in function objects and string objects
        - 'prototype' of Object, Function, Array, String, etc.
        - Number.MAX_VALUE and related constants in Number
        - 'source', 'global', etc. of regexp objects
        (note: CanPut considers the internal prototype chain...)
        */
        if (Options.get().isAlwaysCanPut())
            return Value.makeBool(true);
        Value v = readPropertyRaw(Collections.singleton(objlabel), propertystr, true); // TODO: possible to optimize modeling of [[CanPut]]? i.e. in some cases omit this call to readPropertyRaw?
        Bool b;
        if (v.isNotPresent() || v.isNotReadOnly())
            b = Value.makeBool(true);
        else if (v.isReadOnly() && !v.isMaybeAbsent())
            b = Value.makeBool(false);
        else
            b = Value.makeAnyBool();
        if (log.isDebugEnabled())
            log.debug("canPut(" + objlabel + ") = " + b);
        return b;
    }

    /**
     * 8.6.2.2 [[Put]]
     * Assigns the given value to the given property of the given objects.
     * Modified is set on all values being written (not considering the put flag).
     *
     * @param put        if set, attributes are cleared or copied from the old value according to 8.6.2.2
     * @param force_weak if set, force weak update disregarding objlabels
     */
    public void writeProperty(Collection<ObjectLabel> objlabels, Str propertystr, Value value, boolean put, boolean force_weak) {
        State state = c.getState();
        // 8.6.2.2 [[Put]]
        // 1. Call the [[CanPut]] method of O with name P.
        // 2. If Result(1) is false, return.
        // 3. If O doesn't have a property with name P, go to step 6.
        // 4. Set the value of the property to V. The attributes of the property are not changed.
        // 5. Return.
        // 6. Create a property with name P, set its value to V and give it empty attributes.
        value.assertNonEmpty();
        if (propertystr.isMaybeSingleStr()) {
            writePropertyRaw(objlabels, propertystr.getStr(), value, put, true, true, force_weak);
        } else {
            for (ObjectLabel objlabel : objlabels) {
                if (propertystr.isMaybeStrSomeUInt()) {
                    Value v = value;
                    if (!put || canPut(objlabel, Value.makeAnyStrUInt()).isMaybeTrue()) { // we can ignore isMaybeFalse since we only do weak update anyway
                        if (put)
                            v = v.removeAttributes(); // 8.6.2.2 item 6
                        Value oldval = UnknownValueResolver.getDefaultArrayProperty(objlabel, state);
                        if (!oldval.isNone()) {
                            Value newval = UnknownValueResolver.join(oldval, v, state);
                            state.getObject(objlabel, true).setDefaultArrayProperty(newval.joinModified());
                        } // otherwise, the abstract object represents zero concrete objects (due to restoring of unmodified properties), so ignore
                    }
                    // TODO: generate warning if [[CanPut]] gives false?
                }
                if (propertystr.isMaybeStrSomeNonUInt()) {
                    Value v = value;
                    if (!put || canPut(objlabel, Value.makeAnyStrNotUInt()).isMaybeTrue()) { // we can ignore isMaybeFalse since we only do weak update anyway
                        if (put)
                            v = v.removeAttributes(); // 8.6.2.2 item 6
                        Value oldval = UnknownValueResolver.getDefaultNonArrayProperty(objlabel, state);
                        if (!oldval.isNone()) {
                            Value newval = UnknownValueResolver.join(oldval, v, state);
                            state.getObject(objlabel, true).setDefaultNonArrayProperty(newval.joinModified());
                        } // otherwise, the abstract object represents zero concrete objects (due to restoring of unmodified properties), so ignore
                    }
                    // TODO: generate warning if [[CanPut]] gives false?
                }
                for (String propertyname : newSet(state.getObject(objlabel, false).getPropertyNames())) // calls to UnknownValueResolver above have materialized all relevant properties
                    if (propertystr.isMaybeStr(propertyname))
                        writePropertyIfCanPut(objlabel, propertyname, value, put, true, true, true);
            }
        }
        if (log.isDebugEnabled())
            log.debug("writeProperty(" + objlabels + "," + propertystr + "," + value + ")");
    }

    /**
     * Assigns the given value to the given object property if [[CanPut]] is true.
     * Takes ReadOnly into account.
     *
     * @param put             if set, attributes are cleared or copied from the old value according to 8.6.2.2
     * @param real            if set, the modified flag is set on written values
     * @param allow_overwrite if set, allow overwriting of existing values (if not set, do nothing if the property already exists)
     * @param force_weak      if set, force weak update
     */
    private void writePropertyIfCanPut(ObjectLabel objlabel, String propertyname, Value value,
                                       boolean put, boolean real, boolean allow_overwrite, boolean force_weak) {
        // 8.6.2.2 [[Put]]
        // 1. Call the [[CanPut]] method of O with name P.
        // 2. If Result(1) is false, return.
        // 3. If O doesn't have a property with name P, go to step 6.
        // 4. Set the value of the property to V. The attributes of the property are not changed.
        // 5. Return.
        // 6. Create a property with name P, set its value to V and give it empty attributes.
        Bool canput = put ? canPut(objlabel, Value.makeTemporaryStr(propertyname)) : Value.makeBool(true);
        if (canput.isMaybeTrue())
            writeProperty(objlabel, propertyname, value, put, real, allow_overwrite, force_weak || canput.isMaybeFalse());
        // TODO: generate warning if [[CanPut]] gives false?
    }

    /**
     * Assigns the given value to the given property of the given objects, with attributes.
     *
     * @param put             if set, attributes are cleared or copied from the old value according to 8.6.2.2
     * @param real            if set, the modified flag is set on written values
     * @param allow_overwrite if set, allow overwriting of existing values (if not set, do nothing if the property already exists)
     * @param force_weak      if set, force weak update disregarding objlabels
     */
    private void writePropertyRaw(Collection<ObjectLabel> objlabels, String propertyname, Value value,
                                  boolean put, boolean real, boolean allow_overwrite, boolean force_weak) {
        for (ObjectLabel objlabel : objlabels)
            writePropertyIfCanPut(objlabel, propertyname, value, put, real, allow_overwrite, force_weak || objlabels.size() != 1);
    }

    /**
     * Assigns the given value to the given property of the given object.
     * Attributes are taken from the given value.
     * Modified is set on all values being written.
     */
    public void writeProperty(ObjectLabel objlabel, String propertyname, Value value) {
        writePropertyWithAttributes(objlabel, propertyname, value.setAttributes(false, false, false));
    }

    /**
     * Assigns the given value to the given property of the given objects, with attributes.
     * Attributes are taken from the given value.
     *
     * @param real if set, the modified flag is set on written values
     */
    public void writePropertyWithAttributes(Collection<ObjectLabel> objlabels, String propertyname, Value value, boolean real) {
        writePropertyWithAttributes(objlabels, propertyname, value, real, false);
    }

    /**
     * Assigns the given value to the given property of the given objects, with attributes.
     * Attributes are taken from the given value.
     *
     * @param real if set, the modified flag is set on written values
     */
    public void writePropertyWithAttributes(Collection<ObjectLabel> objlabels, String propertyname, Value value, boolean real, boolean force_weak) {
        writePropertyRaw(objlabels, propertyname, value, false, real, true, force_weak);
        if (log.isDebugEnabled())
            log.debug("writePropertyWithAttributes(" + objlabels + "," + propertyname + "," + value + "," + value.printAttributes() + ")");
    }

    /**
     * Assigns the given value to the given property of the given objects, with attributes.
     * Attributes are taken from the given value.
     * Modified is set on all values being written.
     */
    public void writePropertyWithAttributes(Collection<ObjectLabel> objlabels, String propertyname, Value value) {
        writePropertyWithAttributes(objlabels, propertyname, value, true);
    }

    /**
     * Assigns the given value to the given property of the given object, with attributes.
     * Attributes are taken from the given value.
     * Modified is set on all values being written.
     */
    public void writePropertyWithAttributes(ObjectLabel objlabel, String propertyname, Value value) {
        writePropertyWithAttributes(Collections.singleton(objlabel), propertyname, value, true);
    }

    /**
     * Assigns the given value to the given property of an object.
     *
     * @param put             if set, attributes are cleared or copied from the old value according to 8.6.2.2
     * @param real            if set, the modified flag is set on written values
     * @param allow_overwrite if set, allow overwriting of existing values (if not set, do nothing if the property already exists)
     * @param force_weak      if set, force weak update
     */
    private void writeProperty(ObjectLabel objlabel, String propertyname, Value value,
                               boolean put, boolean real, boolean allow_overwrite, boolean force_weak) {
        State state = c.getState();
        Value oldvalue = UnknownValueResolver.getProperty(objlabel, propertyname, state, true);
        if (!allow_overwrite && oldvalue.isNotAbsent()) // not allowed to overwrite and definitely present already, so just return
            return; // TODO: warn that the operation has no effect? (double declaration of variable)
        if (put) {
            if (oldvalue.isNotPresent()) // definitely not present already
                value = value.removeAttributes(); // 8.6.2.2 item 6
            else if (oldvalue.isNotAbsent()) // definitely present already
                value = value.setAttributes(oldvalue); // 8.6.2.2 item 4
            else // maybe present already
                value = value.setAttributes(oldvalue).join(value.removeAttributes());
        }
        boolean valueMaybeGetterOrSetter = false;// XXX: value.isMaybeGetterOrSetter();
// see ECMAScript 2015: "12.2.6.1: In ECMAScript 2015, it is no longer an early error to have duplicate property names in Object Initializers."
//        boolean oldvalueMaybeGetterOrSetter = oldvalue.isMaybeGetterOrSetter();
//        boolean maybeSyntaxError = ...;
//        boolean maybeNotSyntaxError = ...;
// FIXME: throw SyntaxError if:
//        1) oldvalue is ordinary property and value is getter/setter,
//        2) oldvalue is getter and value is ordinary or getter,
//        3) oldvalue is setter ans value is ordinary or setter, or
//        4) strict mode and oldvalue is ordinary property and value is ordinary property
// FIXME: cancel ordinary flow if definite SyntaxError
        if (force_weak || !objlabel.isSingleton() || (!allow_overwrite && oldvalue.isMaybePresent())) // weak update
            value = UnknownValueResolver.join(value, oldvalue, state);
        else if (valueMaybeGetterOrSetter) { // strong update, write getter/setter
            value = UnknownValueResolver.join(value, oldvalue.restrictToNotAbsent(), state);
        }
        if (real || oldvalue.isMaybeModified())
            value = value.joinModified();
        checkProperty(value);
        if (put && real && objlabel.isHostObject() && !state.getExecutionContext().isEmpty()) { // FIXME: also call evaluateSetter on dynamic property writes with unknown property
            objlabel.getHostObject().evaluateSetter(objlabel, Value.makeTemporaryStr(propertyname), value, state);
        }
        if (value.equals(oldvalue))
            return; // don't request writable obj if the value doesn't change anyway
        if (objlabel.getKind() == ObjectLabel.Kind.ARRAY && propertyname.equals("length") && put)
            return; // (hopefully) already taken care of by NativeFunctions.updateArrayLength
        state.getObject(objlabel, true).setProperty(propertyname, value);
    }

    /**
     * Checks for the given value that attributes are non-bottom if the value is nonempty.
     */
    private static void checkProperty(Value v) {
        if (v.isMaybePresent() && (!v.hasDontDelete() || !v.hasDontEnum() || !v.hasReadOnly()))
            throw new AnalysisException("Missing attribute information at property value " + v);
    }

    /**
     * Assigns the given value to the given variable.
     *
     * @param varname the variable name
     * @param value   the new value
     * @param real    if set, the modified flag is set on written values
     * @return the set of objects where the variable may be stored (i.e. the base objects)
     */
    public Set<ObjectLabel> writeVariable(String varname, Value value, boolean real) {
        State state = c.getState();
        value.assertNonEmpty();
        // 10.1.4 Identifier Resolution
        // 1. Get the next object in the scope chain. If there isn't one, go to step 5.
        // 2. Call the [[HasProperty]] method of Result(1), passing the Identifier as the property.
        // 3. If Result(2) is true, return a value of type Reference whose base object is Result(1) and whose property name is the Identifier.
        // 4. Go to step 1.
        // 5. Return a value of type Reference whose base object is null and whose property name is the Identifier.
        Set<ObjectLabel> objlabels = newSet();
        boolean definitely_found;
        for (Iterator<Set<ObjectLabel>> it = ScopeChain.iterable(state.getExecutionContext().getScopeChain()).iterator(); it.hasNext(); ) {
            Set<ObjectLabel> sc = it.next();
            definitely_found = true;
            for (ObjectLabel objlabel : sc) {
                Bool h = hasPropertyRaw(Collections.singleton(objlabel), varname);
                if (h.isMaybeTrue() || !it.hasNext() && !Options.get().isNoImplicitGlobalVarDeclarationsEnabled())
                    objlabels.add(objlabel);
                if (h.isMaybeFalse())
                    definitely_found = false;
            }
            if (definitely_found)
                break;
        }
        // 8.6.2.2 [[Put]]
        // 1. Call the [[CanPut]] method of O with name P.
        // 2. If Result(1) is false, return.
        // 3. If O doesn't have a property with name P, go to step 6.
        // 4. Set the value of the property to V. The attributes of the property are not changed.
        // 5. Return.
        // 6. Create a property with name P, set its value to V and give it empty attributes.
        for (ObjectLabel objlabel : objlabels)
            writePropertyIfCanPut(objlabel, varname, value, true, real, true, objlabels.size() != 1);
        if (log.isDebugEnabled())
            log.debug("writeVariable(" + varname + "," + value + ")");
        return objlabels;
    }

    /**
     * Declares the given variable (or function) and assigns the given value to it.
     */
    public void declareAndWriteVariable(String varname, Value value, boolean allow_overwrite) {
        State state = c.getState();
        // For function declarations (p.46): If the variable object already has a property with this name,
        // replace its value and attributes.
        // For variable declarations: If there is already a property of the variable object with
        // the name of a declared variable, the value of the property and its attributes are not changed.
        // For global code (10.2.1): Variable instantiation is performed using the global object as the variable
        // object and using property attributes { DontDelete }.
        // For eval code (10.2.2): Variable instantiation is performed using the calling context's variable
        // object and using empty property attributes.
        // For function code (10.2.3): Variable instantiation is performed using the activation object as the
        // variable object and using property attributes { DontDelete }.
        // TODO: variable instantiation (excluding the activation object arguments property) in eval code should use empty attributes, i.e. without DontDelete (10.2.2)
        value.assertNonEmpty();
        writePropertyRaw(state.getExecutionContext().getVariableObject(), varname, value.restrictToNotAbsent().setAttributes(false, true, false), false, true, allow_overwrite, false);
        if (log.isDebugEnabled())
            log.debug("declareAndWriteVariable(" + varname + "," + value + ")");
    }

    /**
     * Returns the value of the given variable.
     *
     * @param varname   the variable name
     * @param base_objs collection where base objects are added (ignored if null)
     */
    public Value readVariable(String varname, Collection<ObjectLabel> base_objs) {
        State state = c.getState();
        Collection<Value> values = newList();
        boolean definitely_found = false;
        for (Set<ObjectLabel> sc : ScopeChain.iterable(state.getExecutionContext().getScopeChain())) {
            definitely_found = true;
            for (ObjectLabel objlabel : sc) {
                Value v = readPropertyRaw(Collections.singleton(objlabel), Value.makeTemporaryStr(varname), false);
                if (v.isMaybePresent()) { // found one (maybe)
                    values.add(v.setBottomPropertyData());
                    if (base_objs != null)
                        base_objs.add(objlabel); // collecting the object from the scope chain (although the property may be in its prototype chain)
                }
                if (v.isMaybeAbsent())
                    definitely_found = false;
            }
            if (definitely_found)
                break;
        }
        if (!definitely_found)
            values.add(Value.makeAbsent()); // end of scope chain, so add absent
        Value res = UnknownValueResolver.join(values, state);
        if (log.isDebugEnabled())
            log.debug("readVariable(" + varname + ") = " + res + (base_objs != null ? " at " + base_objs : ""));
        return res;
    }

    /**
     * Deletes the given variable.
     *
     * @see #deleteProperty(Collection, Str, boolean)
     */
    public Value deleteVariable(String varname) {
        State state = c.getState();
        // 10.1.4 Identifier Resolution
        // 1. Get the next object in the scope chain. If there isn't one, go to step 5.
        // 2. Call the [[HasProperty]] method of Result(1), passing the Identifier as the property.
        // 3. If Result(2) is true, return a value of type Reference whose base object is Result(1) and whose property name is the Identifier.
        // 4. Go to step 1.
        // 5. Return a value of type Reference whose base object is null and whose property name is the Identifier.
        Set<ObjectLabel> objlabels = newSet();
        boolean definitely_found = false;
        for (Set<ObjectLabel> sc : ScopeChain.iterable(state.getExecutionContext().getScopeChain())) {
            definitely_found = true;
            for (ObjectLabel objlabel : sc) {
                Bool h = hasPropertyRaw(Collections.singleton(objlabel), varname);
                if (h.isMaybeTrue())
                    objlabels.add(objlabel);
                if (h.isMaybeFalse())
                    definitely_found = false;
            }
            if (definitely_found)
                break;
        }
        // 11.4.1 The delete Operator
        // 1. Evaluate UnaryExpression.
        // 2. If Type(Result(1)) is not Reference, return true.
        // 3. Call GetBase(Result(1)).
        // 4. Call GetPropertyName(Result(1)).
        // 5. Call the [[Delete]] method on Result(3), providing Result(4) as the property name to delete.
        // 6. Return Result(5).
        Value res = deleteProperty(objlabels, Value.makeTemporaryStr(varname), false);
        if (!definitely_found)
            res = res.joinBool(true);
        if (log.isDebugEnabled())
            log.debug("deleteVariable(" + varname + ") = " + res);
        return res;
    }

    /**
     * Deletes the given property.
     * Ignored if the property has attribute DontDelete.
     * Returns false if attempting to delete a property with attribute DontDelete, true otherwise.
     */
    public Value deleteProperty(Collection<ObjectLabel> objlabels, Str propertystr, boolean force_weak) {
        Value res = Value.makeNone();
        for (ObjectLabel objlabel : objlabels)
            if (!force_weak && objlabels.size() == 1 && objlabel.isSingleton() && propertystr.isMaybeSingleStr())
                res = strongDeleteProperty(objlabel, propertystr.getStr());
            else
                res = res.joinBool(weakDeleteProperty(objlabel, propertystr));
        if (log.isDebugEnabled())
            log.debug("deleteProperty(" + objlabels + "," + propertystr + ") = " + res);
        return res;
    }

    /**
     * Deletes the given property (strong update).
     */
    private Value strongDeleteProperty(ObjectLabel objlabel, String propertyname) {
        // 8.6.2.5 [[Delete]] (P)
        // When the [[Delete]] method of O is called with property name P, the following steps are taken:
        // 1. If O doesn't have a property with name P, return true.
        // 2. If the property has the DontDelete attribute, return false.
        // 3. Remove the property with name P from O.
        // 4. Return true.
        Value res;
        Value v = UnknownValueResolver.getProperty(objlabel, propertyname, c.getState(), true);
        if (!v.isMaybePresent()) // property is definitely absent already
            res = Value.makeBool(true);
        else if (!v.isMaybeAbsent() && v.isDontDelete()) // definitely present already, definitely can't delete
            res = Value.makeBool(false);
        else {
            Value newval;
            if (v.isNotDontDelete()) { // maybe present already, definitely can delete
                newval = Value.makeAbsentModified(); // FIXME: deleting magic properties? (also other places...)
                res = Value.makeBool(true);
            } else { // don't know, maybe delete
                newval = v.joinAbsentModified();
                res = Value.makeAnyBool();
            }
            Obj obj = c.getState().getObject(objlabel, true);
            obj.setProperty(propertyname, newval);
        }
        return res;
    }

    /**
     * Deletes the given property (weak update).
     */
    private Value weakDeleteProperty(ObjectLabel objlabel, Str propertystr) {
        // 8.6.2.5 [[Delete]] (P)
        // When the [[Delete]] method of O is called with property name P, the following steps are taken:
        // 1. If O doesn't have a property with name P, return true.
        // 2. If the property has the DontDelete attribute, return false.
        // 3. Remove the property with name P from O.
        // 4. Return true.
        Value res = Value.makeNone();
        if (propertystr.isMaybeSingleStr())
            res = res.joinBool(weakDeleteProperty(PropertyReference.makeOrdinaryPropertyReference(objlabel, propertystr.getStr())));
        else {
            if (propertystr.isMaybeStrSomeUInt())
                res = res.joinBool(weakDeleteProperty(PropertyReference.makeDefaultArrayPropertyReference(objlabel)));
            if (propertystr.isMaybeStrSomeNonUInt())
                res = res.joinBool(weakDeleteProperty(PropertyReference.makeDefaultNonArrayPropertyReference(objlabel)));
            // the calls to UnknownValueResolver above have materialized all relevant properties
            for (String propertyname : newSet(c.getState().getObject(objlabel, false).getPropertyNames()))
                if (propertystr.isMaybeStr(propertyname))
                    res = res.joinBool(weakDeleteProperty(PropertyReference.makeOrdinaryPropertyReference(objlabel, propertyname)));
        }
        return res;
    }

    private Value weakDeleteProperty(PropertyReference p) {
        Value res;
        Value v = c.getState().readProperty(p, true);
        if (!v.isMaybePresent()) // property is definitely absent already
            res = Value.makeBool(true);
        else if (!v.isMaybeAbsent() && v.isDontDelete()) // definitely present already, definitely can't delete
            res = Value.makeBool(false);
        else {
            if (v.isNotDontDelete()) // maybe present already, definitely can delete (but still only do weak update)
                res = Value.makeBool(true);
            else // don't know, maybe delete
                res = Value.makeAnyBool();
            c.getState().writeProperty(p, v.joinAbsentModified());
        }
        return res;
    }
}
