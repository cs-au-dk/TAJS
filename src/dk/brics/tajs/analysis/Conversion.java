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

package dk.brics.tajs.analysis;

import dk.brics.tajs.analysis.js.UserFunctionCalls;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptFunctions;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteString;
import dk.brics.tajs.analysis.nativeobjects.concrete.InvocationResult;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.Bool;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.util.AnalysisException;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Type conversions for abstract values (Chapter 9).
 * <p>
 * Messages produced here are always of kind MAYBE (never CERTAIN) because conversion generally may occur conditionally.
 */
public class Conversion {

    /**
     * Preferred type for conversion.
     */
    public enum Hint {
        NONE, NUM, STR
    }

    /**
     * Strings that are valid StrDecimalLiteral according to 9.3.1.
     */
    static private final Pattern STR_DECIMAL_LITERAL =
            Pattern.compile("[-+]?(Infinity|([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([eE][-+]?[0-9]+)?)");

    /**
     * Strings that are valid HexIntegerLiteral according to 9.3.1.
     */
    static private final Pattern HEX_INTEGER_LITERAL =
            Pattern.compile("0[xX][0-9a-fA-F]+");

    private Conversion() {
    }

    /**
     * 8.6.2.6 [[DefaultValue]].
     * Can only return primitive values.
     */
    private static Value defaultValue(ObjectLabel obj, Hint hint, Solver.SolverInterface c) {
        // When the [[DefaultValue]] method of O is called with no hint, then it behaves as if the hint were Number,
        // unless O is a Date object (section 15.9), in which case it behaves as if the hint were String.
        if (hint == Hint.NONE)
            hint = obj.getKind() == Kind.DATE ? Hint.STR : Hint.NUM;
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        Value result;
        boolean maybe_typeerror = false;
        if (hint == Hint.STR) {
            // When the [[DefaultValue]] method of O is called with hint String, the following steps are taken:
            // 1. Call the [[Get]] method of object O with argument "toString".
            // 2. If Result(1) is not an object, go to step 5.
            // 3. Call the [[Call]] method of Result(1), with O as the this value and an empty argument list.
            // 4. If Result(3) is a primitive value, return Result(3).
            // 5. Call the [[Get]] method of object O with argument "valueOf".
            // 6. If Result(5) is not an object, go to step 9.
            // 7. Call the [[Call]] method of Result(5), with O as the this value and an empty argument list.
            // 8. If Result(7) is a primitive value, return Result(7).
            // 9. Throw a TypeError exception.
            Value tostring = pv.readPropertyValue(Collections.singleton(obj), "toString");
            visitPropertyRead(obj, "toString", s, c);
            tostring = UnknownValueResolver.getRealValue(tostring, s);
            State tostringState = s.clone(); // TODO: no need to clone if certain that convertToStringOrValueOf will not call any user-defined functions
            c.setState(tostringState);
            result = convertToStringOrValueOf(obj, tostring.getObjectLabels(), true, c);
            result = UnknownValueResolver.getRealValue(result, tostringState);
            c.setState(s);
            if (!isMaybeNonCallable(tostring)) {
                s.setToNone(); // TODO: skip this if we decide to skip the cloning above
            }
            s.propagate(tostringState, false);
            if (isMaybeNonCallable(tostring) || result.isMaybeObject()) {
                Value valueof = pv.readPropertyValue(Collections.singleton(obj), "valueOf");
                visitPropertyRead(obj, "valueOf", s, c);
                valueof = UnknownValueResolver.getRealValue(valueof, s);
                State valueOfState = s.clone(); // TODO: no need to clone if certain that convertToStringOrValueOf will not call any user-defined functions
                c.setState(valueOfState);
                Value result2 = convertToStringOrValueOf(obj, valueof.getObjectLabels(), false, c);
                result2 = UnknownValueResolver.getRealValue(result2, valueOfState);
                result = result.restrictToNotObject().join(result2);
                c.setState(s);
                if (!isMaybeNonCallable(valueof)) {
                    s.setToNone(); // TODO: skip this if we decide to skip the cloning above
                }
                s.propagate(valueOfState, false);
                if (isMaybeNonCallable(valueof) || result.isMaybeObject())
                    maybe_typeerror = true;
            }
        } else if (hint == Hint.NUM) {
            // When the [[DefaultValue]] method of O is called with hint Number, the following steps are taken:
            // 1. Call the [[Get]] method of object O with argument "valueOf".
            // 2. If Result(1) is not an object, go to step 5.
            // 3. Call the [[Call]] method of Result(1), with O as the this value and an empty argument list.
            // 4. If Result(3) is a primitive value, return Result(3).
            // 5. Call the [[Get]] method of object O with argument "toString".
            // 6. If Result(5) is not an object, go to step 9.
            // 7. Call the [[Call]] method of Result(5), with O as the this value and an empty argument list.
            // 8. If Result(7) is a primitive value, return Result(7).
            // 9. Throw a TypeError exception.
            Value valueof = pv.readPropertyValue(Collections.singleton(obj), "valueOf");
            visitPropertyRead(obj, "valueOf", s, c);
            valueof = UnknownValueResolver.getRealValue(valueof, s);
            State valueofState = s.clone(); // TODO: no need to clone if certain that convertToStringOrValueOf will not call any user-defined functions
            c.setState(valueofState);
            result = convertToStringOrValueOf(obj, valueof.getObjectLabels(), false, c);
            result = UnknownValueResolver.getRealValue(result, valueofState);
            c.setState(s);
            if (!isMaybeNonCallable(valueof)) {
                s.setToNone(); // TODO: skip this if we decide to skip the cloning above
            }
            s.propagate(valueofState, false);
            if (isMaybeNonCallable(valueof) || result.isMaybeObject()) {
                Value tostring = pv.readPropertyValue(Collections.singleton(obj), "toString");
                visitPropertyRead(obj, "toString", s, c);
                tostring = UnknownValueResolver.getRealValue(tostring, s);
                State toStringState = s.clone(); // TODO: no need to clone if certain that convertToStringOrValueOf will not call any user-defined functions
                c.setState(toStringState);
                Value result2 = convertToStringOrValueOf(obj, tostring.getObjectLabels(), true, c);
                result2 = UnknownValueResolver.getRealValue(result2, toStringState);
                result = result.restrictToNotObject().join(result2);
                c.setState(s);
                if (!isMaybeNonCallable(tostring)) {
                    s.setToNone(); // TODO: skip this if we decide to skip the cloning above
                }
                s.propagate(toStringState, false);
                if (isMaybeNonCallable(tostring) || result.isMaybeObject())
                    maybe_typeerror = true;
            }
        } else
            throw new AnalysisException();
        if (maybe_typeerror) {
            c.getMonitoring().addMessage(c.getNode(), Severity.HIGH, "TypeError when computing default value for object");
            Exceptions.throwTypeError(c); // no ordinary flow (may be called in a loop, so don't set s to none)
        }
        return result.restrictToNotObject();
    }

    public static boolean isMaybeNonCallable(Value v) {
        if (v.isMaybePrimitive())
            return true;
        for (ObjectLabel obj : v.getObjectLabels())
            if (obj.getKind() != Kind.FUNCTION)
                return true;
        return false;
    }

    private static void visitPropertyRead(ObjectLabel obj, String prop, State s, Solver.SolverInterface c) {
        c.getMonitoring().visitPropertyRead(c.getNode(), Collections.singleton(obj), Value.makeTemporaryStr(prop), s, true);
    }

    /**
     * Invokes the given toString/valueOf functions.
     *
     * @param thiss    the 'this' object
     * @param objs     the toString or valueOf functions
     * @param toString true if toString, false if valueOf
     */
    private static Value convertToStringOrValueOf(ObjectLabel thiss, Set<ObjectLabel> objs, boolean toString, Solver.SolverInterface c) {
        List<Value> result = newList();
        BasicBlock implicitAfterCall = null;
        for (ObjectLabel obj : objs) {
            if (obj.isHostObject()) {
                switch ((HostAPIs) obj.getHostObject().getAPI()) {
                    case ECMASCRIPT_NATIVE:
                        Value v = toString ? ECMAScriptFunctions.internalToString(thiss, (ECMAScriptObjects) obj.getHostObject(), c) : ECMAScriptFunctions.internalValueOf(thiss, (ECMAScriptObjects) obj.getHostObject(), c);
                        if (v != null)
                            result.add(v);
                        break;
                    default:
                        c.getMonitoring().addMessage(c.getNode(), Severity.HIGH, "Implicit call to non-native host object toString/valueOf method"); // FIXME: should not happen? (what about e.g. DOM toString functions?) - github #254
                        result.add(Value.makeAnyStr());
                        break;
                }
            } else if (obj.getKind() == Kind.FUNCTION) {
                implicitAfterCall = UserFunctionCalls.implicitUserFunctionCall(obj, new FunctionCalls.CallInfo() {

                    @Override
                    public AbstractNode getSourceNode() {
                        return c.getNode();
                    }

                    @Override
                    public AbstractNode getJSSourceNode() {
                        return c.getNode();
                    }

                    @Override
                    public boolean isConstructorCall() {
                        return false;
                    }

                    @Override
                    public Value getFunctionValue() {
                        throw new AnalysisException();
                    }

                    @Override
                    public Set<ObjectLabel> prepareThis(State caller_state, State callee_state) {
                        return singleton(thiss);
                    }

                    @Override
                    public Value getArg(int i) {
                        return Value.makeUndef();
                    }

                    @Override
                    public int getNumberOfArgs() {
                        return 0;
                    }

                    @Override
                    public Value getUnknownArg() {
                        return Value.makeUndef();
                    }

                    @Override
                    public boolean isUnknownNumberOfArgs() {
                        return false;
                    }

                    @Override
                    public int getResultRegister() {
                        return AbstractNode.NO_VALUE;
                    }

                    @Override
                    public ExecutionContext getExecutionContext() {
                        return c.getState().getExecutionContext();
                    }
                }, c);
            }
        }
        return UserFunctionCalls.implicitUserFunctionReturn(result, !result.isEmpty(), implicitAfterCall, c);
    }

    /**
     * 9.1 ToPrimitive.
     * Converts a value to a primitive value according to the hint.
     * Has no effect on primitive types but transforms wrapper objects to their wrapped values.
     * May have side-effects on the current state.
     */
    public static Value toPrimitive(Value v, Hint hint, Solver.SolverInterface c) {
        v = UnknownValueResolver.getRealValue(v, c.getState());
        Collection<Value> vs = newList();
        Value nonobj = v.restrictToNotObject(); // The result equals the input argument (no conversion).
        if (!nonobj.isNotPresent())
            vs.add(nonobj);
        boolean first = true;
        State res = null;
        State orig = c.getState();
        for (Iterator<ObjectLabel> it = v.getObjectLabels().iterator(); it.hasNext(); ) {
            ObjectLabel ol = it.next();
            boolean last = !it.hasNext();
            // Return a default value for the Object. The default value of an object is
            // retrieved by calling the internal [[DefaultValue]] method of the object,
            // passing the optional hint PreferredType.
            if (!last) {
                c.setState(orig.clone());
            }
            vs.add(defaultValue(ol, hint, c)); // may have side-effects
            if (first) {
                res = c.getState();
                first = false;
            } else {
                res.propagate(c.getState(), false);
            }
            if (!last) {
                c.setState(orig);
            }
        }
        if (!first) {
            c.setState(res);
        }
        return UnknownValueResolver.join(vs, c.getState());
    }

    /**
     * 9.2 ToBoolean.
     */
    public static Value toBoolean(Value v) {
        Value result = v.restrictToBool(); // The result equals the input argument (no conversion).
        if (v.isMaybeUndef()) {
            // false
            result = result.joinBool(false);
        }
        if (v.isMaybeNull()) {
            // false
            result = result.joinBool(false);
        }
        if (!v.isNotNum()) {
            // The result is false if the argument is +0, -0, or NaN; otherwise the result is true.
            if (v.isMaybeNaN())
                result = result.joinBool(false);
            if (v.isMaybeSingleNum()) {
                if (Math.abs(v.getNum()) == 0.0)
                    result = result.joinBool(false);
                else
                    result = result.joinBool(true);
            } else
                result = result.joinAnyBool();
        }
        if (!v.isNotStr()) {
            // The result is false if the argument is the empty string (its length is zero); otherwise the result is true.
            if (v.isMaybeSingleStr()) {
                if (v.getStr().isEmpty())
                    result = result.joinBool(false);
                else
                    result = result.joinBool(true);
            } else if (v.isMaybeStrOther() || v.isMaybeStrIdentifierParts() || v.isMaybeStrJSON())
                result = result.joinAnyBool();
            else
                result = result.joinBool(true);
        }
        if (v.isMaybeObject()) {
            // true
            result = result.joinBool(true);
        }
        return result;
    }

    /**
     * 9.3 ToNumber.
     */
    public static Value toNumber(Value v, Solver.SolverInterface c) {
        if (v.isMaybeObject()) {
            // Call ToPrimitive(input argument, hint Number).
            v = toPrimitive(v, Hint.NUM, c);
            c.getMonitoring().addMessage(c.getNode(), Severity.LOW, "Converting object to number");
        }
        Value result = v.restrictToNum(); // The result equals the input argument (no conversion).
        if (v.isMaybeUndef()) {
            // NaN
            result = result.joinNumNaN();
            c.getMonitoring().addMessage(c.getNode(), Severity.LOW, "Conversion to number yields NaN");
        }
        if (v.isMaybeNull()) {
            // +0
            result = result.joinNum(0.0);
        }
        result = Value.join(result, fromBooltoNum(v), fromStrtoNum(v, c));
        return result;
    }

    /**
     * 9.3 Boolean to Number.
     */
    public static Value fromBooltoNum(Bool b) {
        // The result is 1 if the argument is true. The result is +0 if the argument is	false.
        if (b.isNotBool())
            return Value.makeNone();
        else if (b.isMaybeAnyBool())
            return Value.makeAnyNumUInt(); // join of 1 and +0
        else if (b.isMaybeFalseButNotTrue())
            return Value.makeNum(0.0);
        else
            return Value.makeNum(1.0);
    }

    /**
     * 9.3.1 String to Number.
     */
    public static Value fromStrtoNum(Str str, Solver.SolverInterface c) {
        Value v;
        if (str.isMaybeSingleStr()) {
            String s = str.getStr();
            if (s.isEmpty())
                return Value.makeNum(0.0);
            else {
                s = s.trim();
                if (STR_DECIMAL_LITERAL.matcher(s).matches())
                    v = Value.makeNum(new Double(s));
                else if (HEX_INTEGER_LITERAL.matcher(s).matches())
                    v = Value.makeNum(Long.parseLong(s.substring(2), 16));
                else
                    v = Value.makeNumNaN();
            }
        } else {
            if (str.isMaybeStrIdentifierParts()
                    || str.isMaybeStrPrefixedIdentifierParts()
                    || str.isMaybeStrJSON()
                    || str.isMaybeStrOther() /* 9.3.1. allows trimming */
                    || (str.isMaybeStrUInt() && str.isMaybeStrOtherNum())) {
                v = Value.makeAnyNum(); // TODO: could be more precise for STR_PREFIX if the prefix string is not a UInt
            } else if (str.isMaybeStrUInt()) {
                v = Value.makeAnyNumUInt();
            } else if (str.isMaybeStrOtherNum()) {
                v = Value.makeAnyNumOther().joinNumNaN().joinNumInf();
            } else {
                v = Value.makeNone();
            }
        }
        if (v.isMaybeNaN())
            c.getMonitoring().addMessage(c.getNode(), Severity.LOW, "Conversion from string to number yields NaN");
        return v;
    }

    /**
     * 9.4 ToInteger.
     */
    public static Value toInteger(Value v, Solver.SolverInterface c) {
        Value num = toNumber(v, c);
        if (num.isNotNum())
            return num;
        if (num.isNaN())
            return Value.makeNum(0);
        if (num.isMaybeSingleNum()) {
            Double d = num.getNum();
            if (d == 0.0 || Double.isInfinite(d))
                return num;
            else
                return Value.makeNum(Math.signum(d) * Math.floor(Math.abs(d)));
        } else {
            Value r = Value.makeNone();
            if (num.isMaybeNaN())
                r = r.joinNum(0);
            if (num.isMaybeInf())
                r = r.joinNumInf();
            if (num.isMaybeNumUInt())
                r = r.joinAnyNumUInt();
            if (num.isMaybeNumOther())
                r = r.joinAnyNumUInt().joinAnyNumOther(); // may overflow UInt32
            return r;
        }
    }

    /**
     * 9.5 ToInt32.
     */
    public static int toInt32(double nm) {
        if (Double.isNaN(nm) || Double.isInfinite(nm))
            return 0;
        double w = Math.signum(nm) * Math.floor(Math.abs(nm));
        Double v = w % 4294967296L;
        if (v < 0)
            v += 4294967296L;
        if (v > 2147483648L)
            v -= 4294967296L;
        return v.intValue();
    }

    /**
     * 9.6 ToUInt32.
     */
    public static long toUInt32(double nm) {
        if (Double.isNaN(nm) || Double.isInfinite(nm))
            return 0L;
        double w = Math.signum(nm) * Math.floor(Math.abs(nm));
        Double v = w % 4294967296L;
        if (v < 0)
            v += 4294967296L;
        return v.longValue();
    }

    /**
     * 9.8 ToString.
     */
    public static Value toString(Value v, Solver.SolverInterface c) {
        v = UnknownValueResolver.getRealValue(v, c.getState());
        // object to string
        if (v.isMaybeObject()) {
            // Call ToPrimitive(input argument, hint String).
            v = toPrimitive(v, Hint.STR, c);
            c.getMonitoring().addMessage(c.getNode(), Severity.LOW, "Converting object to string");
        }
        // string
        Value result = v.restrictToStr(); // Return the input argument (no conversion)
        // boolean to string
        if (!v.isNotBool()) {
            // If the argument is true, then the result is "true".
            // If the argument is false, then the result is "false".
            if (v.isMaybeAnyBool())
                result = result.joinAnyStrIdentifierParts(); // join of "true" and "false"
            else if (v.isMaybeTrueButNotFalse())
                result = result.joinStr("true");
            else
                result = result.joinStr("false");
            c.getMonitoring().addMessage(c.getNode(), Severity.LOW, "Converting boolean to string");
        }
        // number to string
        if (!v.isNotNum()) {
            // 9.8.1 ToString Applied to the Number Type
            if (v.isMaybeSingleNum()) {
                // single number to string
                String s;
                double dbl = v.getNum();
                if (Double.isInfinite(dbl))
                    s = Double.toString(dbl);
                else if (Math.floor(dbl) == dbl) {
                    if (dbl > Integer.MAX_VALUE || dbl < Integer.MIN_VALUE) {
                        // funnky float approximations begin happening in JavaScript around here
                        s = specialNumberToString(dbl, c);
                    } else {
                        // common case, is not dispatched to concrete semantics for efficiency reasons
                        s = Long.toString((long) dbl);
                    }
                } else {
                    // s = Double.toString(dbl); - not used as the formatting varies between Java and JavaScript!!
                    s = specialNumberToString(dbl, c);
                }
                if (s == null) {
                    // concrete semantic failed for some reason
                    result = result.joinAnyStr();
                } else {
                    result = result.joinStr(s);
                }
            } else {
                // NaN to string
                if (v.isMaybeNaN())
                    result = result.joinStr(Double.toString(Double.NaN));
                // uint/other/inf to string
                if (v.isMaybeNumUInt())
                    result = result.joinAnyStrUInt();
                if (v.isMaybeNumOther())
                    result = result.joinAnyStrOtherNum();
                if (v.isMaybeInf())
                    result = result.joinAnyStrOtherNum();
            }
            // TODO: warn about number-to-string conversion? (presumably rarely indicating a bug)
            // c.getMonitoring().addMessage(c.getNode(), Severity.LOW, "Converting number to string");
        }
        // null to string
        if (v.isMaybeNull()) {
            // "null"
            result = result.joinStr("null");
            c.getMonitoring().addMessage(c.getNode(), Severity.LOW, "Converting null to string");
        }
        // undefined to string
        if (v.isMaybeUndef()) {
            // "undefined"
            result = result.joinStr("undefined");
            c.getMonitoring().addMessage(c.getNode(), Severity.LOW, "Converting undefined to string");
        }
        return result;
    }

    /**
     * Conversion of numbers to strings according to a concrete JavaScript semantics. Is only required for the special cases, such as formatting of very large or small numbers.
     */
    private static String specialNumberToString(double dbl, Solver.SolverInterface c) {
        final List<Value> noArgs = newList();
        InvocationResult<ConcreteString> concreteResult = TAJSConcreteSemantics.convertTAJSCallExplicit(Value.makeNum(dbl), "Number.prototype.toString", noArgs, c);
        return concreteResult.getValue().getString();
    }

    /**
     * 9.9 ToObject, returning a Value.
     * Note that this may have side-effects on the current state!
     * However, if the solver interface is null, no side-effects or messages are produced (but all object labels are still returned).
     */
    public static Value toObject(AbstractNode node, Value v, Solver.SolverInterface c) {
        return Value.makeObject(toObjectLabels(node, v, c));
    }

    /**
     * 9.9 ToObject, returning a set of object labels.
     * Note that this may have side-effects on the current state!
     * However, if the solver interface is null, no side-effects or messages are produced (but all object labels are still returned).
     */
    public static Set<ObjectLabel> toObjectLabels(AbstractNode node, Value v, Solver.SolverInterface c) {
        Set<ObjectLabel> result = newSet();
        State state = c != null ? c.getState() : null;
        // Object: The result is the input argument (no conversion).
        result.addAll(v.getObjectLabels());
        // primitive number to object
        if (!v.isNotNum()) {
            // Create a new Number object whose [[value]] property is set to the value of the number.
            ObjectLabel lNum = new ObjectLabel(node, Kind.NUMBER);
            if (c != null) {
                state.newObject(lNum);
                state.writeInternalPrototype(lNum, Value.makeObject(InitialStateBuilder.NUMBER_PROTOTYPE));
                state.writeInternalValue(lNum, v.restrictToNum());
                c.getMonitoring().addMessage(c.getNode(), Severity.LOW, "Converting primitive number to object");
            }
            result.add(lNum);
        }
        // primitive boolean to object
        if (!v.isNotBool()) {
            // Create a new Boolean object whose [[value]] property is set to the value of the boolean.
            ObjectLabel lBool = new ObjectLabel(node, Kind.BOOLEAN);
            if (c != null) {
                state.newObject(lBool);
                state.writeInternalPrototype(lBool, Value.makeObject(InitialStateBuilder.BOOLEAN_PROTOTYPE));
                state.writeInternalValue(lBool, v.restrictToBool());
                c.getMonitoring().addMessage(c.getNode(), Severity.LOW, "Converting primitive boolean to object");
            }
            result.add(lBool);
        }
        // primitive string to object
        if (!v.isNotStr()) {
            // Create a new String object whose [[value]] property is set to the value of the string.
            Value vstring = v.restrictToStr();
            Value vlength = vstring.isMaybeSingleStr() ? Value.makeNum(vstring.getStr().length()) : Value.makeAnyNumUInt();
            ObjectLabel lString = new ObjectLabel(node, Kind.STRING);
            if (c != null) {
                state.newObject(lString);
                state.writeInternalPrototype(lString, Value.makeObject(InitialStateBuilder.STRING_PROTOTYPE));
                state.writeInternalValue(lString, vstring);
                c.getAnalysis().getPropVarOperations().writePropertyWithAttributes(lString, "length", vlength.setAttributes(true, true, true));
                c.getMonitoring().addMessage(c.getNode(), Severity.LOW, "Converting primitive string to object");
            }
            result.add(lString);
        }
        // null to object
        if (!v.isNotNull()) {
            // Throw a TypeError exception.
            if (c != null) {
                Exceptions.throwTypeError(c);
                // TODO: warn about null-to-object conversion? (we already have Monitoring.visitPropertyAccess)
                // c.getMonitoring().addMessage(c.getNode(), Severity.HIGH, "TypeError, attempt to convert null to object");
            }
        }
        // undefined to object
        if (!v.isNotUndef()) {
            // Throw a TypeError exception.
            if (c != null) {
                Exceptions.throwTypeError(c);
                // TODO: warn about undefined-to-object conversion? (we already have Monitoring.visitPropertyAccess)
                // c.getMonitoring().addMessage(c.getNode(), Severity.HIGH, "TypeError, attempt to convert undefined to object");
            }
        }
        return result;
    }
}
