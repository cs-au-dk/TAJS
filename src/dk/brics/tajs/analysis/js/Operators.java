/*
 * Copyright 2009-2015 Aarhus University
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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.Conversion.Hint;
import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.Bool;
import dk.brics.tajs.lattice.Num;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Strings;

import java.util.Collection;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Evaluation of operators on abstract values (Chapter 11).
 */
public class Operators {

    private Operators() {
    }

    /**
     * 11.4.3 <code>typeof</code>
     */
    public static Value typeof(Value v, boolean base_maybe_null) {
        boolean maybe_boolean = !v.isNotBool();
        boolean maybe_number = !v.isNotNum();
        boolean maybe_string = !v.isNotStr();
        boolean maybe_undefined = v.isMaybeUndef() || base_maybe_null;
        boolean maybe_object = v.isMaybeNull();
        boolean maybe_function = false;
        for (ObjectLabel objlabel : v.getObjectLabels())
            if (objlabel.getKind() == Kind.FUNCTION)
                maybe_function = true;
            else
                maybe_object = true;
        int count = (maybe_boolean ? 1 : 0)
                + (maybe_number ? 1 : 0)
                + (maybe_string ? 1 : 0)
                + (maybe_undefined ? 1 : 0)
                + (maybe_object ? 1 : 0)
                + (maybe_function ? 1 : 0);
        if (count > 1)
            return Value.makeAnyStr();
        else { // table p. 47
            String s;
            if (maybe_boolean)
                s = "boolean";
            else if (maybe_number)
                s = "number";
            else if (maybe_string)
                s = "string";
            else if (maybe_undefined)
                s = "undefined";
            else if (maybe_object)
                s = "object";
            else if (maybe_function)
                s = "function";
            else
                return Value.makeNone();
            return Value.makeStr(s);
        }
    }

    /**
     * 11.4.6 <code>+</code> (unary)
     */
    public static Value uplus(Value v, Solver.SolverInterface c) {
        return Conversion.toNumber(v, c);
    }

    /**
     * 11.4.7 <code>-</code> (unary)
     */
    public static Value uminus(Value v, Solver.SolverInterface c) {
        Value nm = Conversion.toNumber(v, c);
        if (nm.isNotNum())
            return Value.makeNone();
        else if (nm.isMaybeSingleNum())
            return Value.makeNum(-nm.getNum());
        else
            return nm;
    }

    /**
     * 11.4.8 <code>~</code> (bitwise not)
     */
    public static Value complement(Value v, Solver.SolverInterface c) {
        Value nm = Conversion.toNumber(v, c);
        if (nm.isNotNum())
            return Value.makeNone();
        else if (nm.isMaybeSingleNum())
            return Value.makeNum(~Conversion.toInt32(nm.getNum()));
        else
            return Value.makeAnyNumNotNaNInf();
    }

    /**
     * 11.4.9 <code>&#33;</code> (logical not)
     */
    public static Value not(Value v) {
        Bool bv = Conversion.toBoolean(v);
        if (bv.isNotBool())
            return Value.makeNone();
        else if (bv.isMaybeTrueButNotFalse())
            return Value.makeBool(false);
        else if (bv.isMaybeFalseButNotTrue())
            return Value.makeBool(true);
        return Value.makeAnyBool();
    }

    /**
     * 11.5.1 <code>*</code>
     */
    public static Value mul(Value v1, Value v2, Solver.SolverInterface c) {
        return numeric(NumericOp.MUL, v1, v2, c);
    }

    /**
     * 11.5.2 <code>/</code>
     */
    public static Value div(Value v1, Value v2, Solver.SolverInterface c) {
        return numeric(NumericOp.DIV, v1, v2, c);
    }

    /**
     * 11.5.3 <code>%</code>
     */
    public static Value rem(Value v1, Value v2, Solver.SolverInterface c) {
        return numeric(NumericOp.MOD, v1, v2, c);
    }

    /**
     * 11.5 Multiplicative operators and 11.6.3 Applying the Additive operators to numbers
     */
    private static Value numeric(NumericOp op, Value v1, Value v2, Solver.SolverInterface c) {
        if (v1.isNotPresent() || v2.isNotPresent())
            return Value.makeNone();
        Value arg1 = Conversion.toNumber(v1, c);
        Value arg2 = Conversion.toNumber(v2, c);
        if (arg1.isMaybeSingleNum() && arg2.isMaybeSingleNum()) {
            Double d1 = arg1.getNum();
            Double d2 = arg2.getNum();
            double r = 0;
            switch (op) {
                case ADD:
                    r = d1 + d2;
                    break;
                case SUB:
                    r = d1 - d2;
                    break;
                case MUL:
                    r = d1 * d2;
                    break;
                case DIV:
                    r = d1 / d2;
                    break;
                case MOD:
                    r = d1 % d2;
                    break;
            }
            return Value.makeNum(r);
        }
        Value r = Value.makeNone();
        if (arg1.isNotNum() || arg2.isNotNum())
            return r;
        if (arg1.isMaybeNaN() || arg2.isMaybeNaN()) {
            r = r.joinNumNaN();
            if (arg1.isNaN() || arg2.isNaN())
                return r;
        }
        switch (op) {
            case ADD:
            case SUB:
                if (arg1.isMaybeInf() && arg2.isMaybeInf())
                    r = r.joinNumNaN();
                if (arg1.isMaybeInf() || arg2.isMaybeInf())
                    r = r.joinNumInf();
                if (((arg1.isMaybeNumUInt() && !arg1.restrictToNotNaN().isMaybeOtherThanNumUInt()) || arg1.isMaybeSingleNumUInt()) &&
                        ((arg2.isMaybeNumUInt() && !arg2.restrictToNotNaN().isMaybeOtherThanNumUInt()) || arg2.isMaybeSingleNumUInt())) {
                    // FIXME: this rule ignores overflows from UInt (use Options.get().isUnsound()) - see github #193
                    // FIXME: this rule assumes Uint - Uint => Uint (use Options.get().isUnsound()) ((a fix will break TestMicro#micro_testUintSub)))

                    // NB: Number.MAX_VALUE + Number.MAX_VALUE == Infinity, but Number.MAX_VALUE + (Number.MAX_VALUE / 100000000000000000) == Number.MAX_VALUE
                    // So it is sound to treat UInt as closed under "small" additions.
                    r = r.joinAnyNumUInt();
                } else {
                    // TODO: ignores overflow to +/-Infinity (use Options.get().isUnsound())
                    r = r.joinAnyNumUInt().joinAnyNumOther();
                }
                break;
            case MUL: // TODO: it would be useful to know if a value is 0, too
                if (arg1.isMaybeInf() && arg2.isMaybeInf())
                    r = r.joinNumInf();
                if (arg1.isMaybeInf() && !arg2.isNotNum())
                    r = r.joinNumNaN().joinNumInf();
                if (!arg1.isNotNum() && arg2.isMaybeInf())
                    r = r.joinNumNaN().joinNumInf();
                if (!arg1.isNotNum() && !arg2.isNotNum())
                    r = r.joinAnyNumUInt().joinAnyNumOther();
                break;
            case DIV:
                if (arg1.isMaybeInf() && arg2.isMaybeInf())
                    r = r.joinNumNaN();
                if (arg1.isMaybeInf() && !arg2.isNotNum())
                    r = r.joinNumInf();
                if (!arg1.isNotNum() && arg2.isMaybeInf()) {
                    Value zero = r.joinNum(0.0).joinNum(-0.0); // TODO: bad for precision?
                    r = r.join(zero);
                }
                if (!arg1.isNotNum() && !arg2.isNotNum())
                    r = r.joinAnyNumUInt().joinAnyNumOther().joinNumNaN().joinNumInf(); // TODO: (use Options.get().isUnsound())   - can avoid NaN here sometimes! requires isZero
                break;
            case MOD:
                if (arg1.isMaybeInf() && arg2.isMaybeInf())
                    r = r.joinNumNaN();
                if (arg1.isMaybeInf() && !arg2.isNotNum())
                    r = r.joinNumNaN();
                if (!arg1.isNotNum() && arg2.isMaybeInf())
                    r = r.join(arg1);
                if (!arg1.isNotNum() && !arg2.isNotNum())
                    r = r.joinAnyNumUInt().joinAnyNumOther().joinNumNaN(); // TODO: (use Options.get().isUnsound())   - can avoid NaN here sometimes! requires isZero (benchpress2.js)
                break;
        }
        return r;
    }

    /**
     * Numeric addition.
     */
    private static Value addNumbers(Value v1, Value v2, Solver.SolverInterface c) {
        return numeric(NumericOp.ADD, v1, v2, c);
    }

    /**
     * 11.6.1 <code>+</code> (binary)
     */
    public static Value add(Value v1, Value v2, Solver.SolverInterface c) {
        Value p1 = Conversion.toPrimitive(v1, Hint.NONE, c);
        Value p2 = Conversion.toPrimitive(v2, Hint.NONE, c);
        Value r1 = p1.restrictToNotStr();
        Value r2 = p2.restrictToNotStr();
        // handle string parts of p1 + string parts of p2
        Value r = addStrings(p1, p2, Value.makeNone());
        // handle string parts of p1 + non-string parts of p2
        if (!p1.isNotStr() && p2.isMaybeOtherThanStr())
            r = addStrings(p1, Conversion.toString(r2, c), r);
        // handle non-string parts of p1 + string parts of p2
        if (!p2.isNotStr() && p1.isMaybeOtherThanStr())
            r = addStrings(Conversion.toString(r1, c), p2, r);
        // handle non-string parts of p1 + non-string parts of p2
        if (r1.isMaybePresent() && r2.isMaybePresent())
            r = r.join(addNumbers(r1, r2, c));
        return r;
    }

    private static Value addStrings(Str s1, Str s2, Value r) { // TODO: could be more precise in some cases...
        if (s1.isMaybeSingleStr()) {
            // s1 is single string, handle string parts of s2
            if (s2.isMaybeSingleStr()) {
                // s1 and s2 are both single strings
                r = r.joinStr(s1.getStr() + s2.getStr());
            } else if (s2.isMaybeFuzzyStr()) {
                // s1 is single string, s2 is fuzzy string
                if (Strings.isIdentifier(s1.getStr())
                        && (s2.isMaybeStrUInt() || s2.isMaybeStrIdentifier() || s2.isMaybeStrIdentifierParts() || (s2.isMaybeStrPrefixedIdentifierParts() && Strings.isIdentifierParts(s2.getPrefix())))
                        && !(s2.isMaybeStrOtherNum() || s2.isMaybeStrOther()))
                    r = r.joinPrefixedIdentifierParts(s1.getStr());
                else if (Strings.isIdentifierParts(s1.getStr())
                        && (s2.isMaybeStrUInt() || s2.isMaybeStrIdentifier() || s2.isMaybeStrIdentifierParts() || (s2.isMaybeStrPrefixedIdentifierParts() && Strings.isIdentifierParts(s2.getPrefix())))
                        && !(s2.isMaybeStrOtherNum() || s2.isMaybeStrOther()))
                    r = r.joinAnyStrIdentifierParts();
                else
                    r = Value.makeAnyStr();
            }
        } else if (s1.isMaybeFuzzyStr()) {
            // s1 is fuzzy string, handle string parts of s2
            if (s2.isMaybeSingleStr()) {
                // s1 is fuzzy string, p2 is single string
                if (s1.isMaybeStrPrefixedIdentifierParts()
                        && Strings.isIdentifierParts(s2.getStr()))
                    r = r.joinPrefixedIdentifierParts(s1.getPrefix());
                else if ((s1.isMaybeStrUInt() || s1.isMaybeStrIdentifier() || s1.isMaybeStrIdentifierParts() || (s1.isMaybeStrPrefixedIdentifierParts() && Strings.isIdentifierParts(s1.getPrefix())))
                        && !(s1.isMaybeStrOtherNum() || s1.isMaybeStrOther())
                        && Strings.isIdentifierParts(s2.getStr()))
                    r = r.joinAnyStrIdentifierParts();
                else
                    r = Value.makeAnyStr();
            } else if (s2.isMaybeFuzzyStr()) {
                // s1 and s2 are both fuzzy strings
                if (s1.isMaybeStrPrefixedIdentifierParts()
                        && (s2.isMaybeStrUInt() || s2.isMaybeStrIdentifier() || s2.isMaybeStrIdentifierParts() || (s2.isMaybeStrPrefixedIdentifierParts() && Strings.isIdentifierParts(s2.getPrefix())))
                        && !(s2.isMaybeStrOtherNum() || s2.isMaybeStrOther()))
                    r = r.joinPrefixedIdentifierParts(s1.getPrefix());
                else if (s1.isMaybeStrUInt() && !s1.isMaybeStrSomeNonUInt() && s2.isMaybeStrUInt() && !s2.isMaybeStrSomeNonUInt()) {
                    r = r.joinAnyStrOtherNum();
                } else if ((s1.isMaybeStrUInt() || s1.isMaybeStrIdentifier() || s1.isMaybeStrIdentifierParts() || (s1.isMaybeStrPrefixedIdentifierParts() && Strings.isIdentifierParts(s1.getPrefix())))
                        && !(s1.isMaybeStrOtherNum() || s1.isMaybeStrOther())
                        && (s2.isMaybeStrUInt() || s2.isMaybeStrIdentifier() || s2.isMaybeStrIdentifierParts() || (s2.isMaybeStrPrefixedIdentifierParts() && Strings.isIdentifierParts(s2.getPrefix())))
                        && !(s2.isMaybeStrOtherNum() || s2.isMaybeStrOther()))
                    r = r.joinAnyStrIdentifierParts();
                else
                    r = Value.makeAnyStr();
            }
        }
        if (s1.isMaybeStrJSON() || s2.isMaybeStrJSON()) // TODO: better precision for "(" + JSON + ")"
            r = r.join(Value.makeJSONStr());
        return r;
    }

    /**
     * 11.6.2 <code>-</code> (binary)
     */
    public static Value sub(Value v1, Value v2, Solver.SolverInterface c) {
        return numeric(NumericOp.SUB, v1, v2, c);
    }

    /**
     * 11.7 Bitwise Shift Operators
     */
    private static Value shiftop(ShiftOp op, Value v1, Value v2, Solver.SolverInterface c) {
        if (v1.isNotPresent() || v2.isNotPresent())
            return Value.makeNone();
        Value arg1 = Conversion.toNumber(v1, c);
        Value arg2 = Conversion.toNumber(v2, c);
        if (arg1.isMaybeSingleNum() && arg2.isMaybeSingleNum()) {
            Double d1 = arg1.getNum();
            Double d2 = arg2.getNum();
            double r = 0;
            switch (op) {
                case LEFTSHIFT:
                    r = Conversion.toInt32(d1) << Conversion.toUInt32(d2);
                    break;
                case SIGNEDRIGHTSHIFT:
                    r = Conversion.toInt32(d1) >> Conversion.toUInt32(d2);
                    break;
                case UNSIGNEDRIGHTSHIFT:
                    r = Conversion.toUInt32(d1) >>> Conversion.toUInt32(d2);
                    break;
            }
            return Value.makeNum(r);
        } else
            return Value.makeNone().joinAnyNumUInt();
    }

    /**
     * 11.7.1 <code>&lt;&lt;</code> (left shift)
     */
    public static Value shl(Value v1, Value v2, Solver.SolverInterface c) {
        return shiftop(ShiftOp.LEFTSHIFT, v1, v2, c);
    }

    /**
     * 11.7.2 <code>&gt;&gt;</code> (signed right shift)
     */
    public static Value shr(Value v1, Value v2, Solver.SolverInterface c) {
        return shiftop(ShiftOp.SIGNEDRIGHTSHIFT, v1, v2, c);
    }

    /**
     * 11.7.3 <code>&gt;&gt;&gt;</code> (unsigned right shift)
     */
    public static Value ushr(Value v1, Value v2, Solver.SolverInterface c) {
        return shiftop(ShiftOp.UNSIGNEDRIGHTSHIFT, v1, v2, c);
    }

    /**
     * 11.8.1 <code>&lt;</code>
     */
    public static Value lt(Value v1, Value v2, Solver.SolverInterface c) {
        return abstractRelationalComparison(v1, v2, c);
    }

    /**
     * 11.8.2 <code>&gt;</code>
     */
    public static Value gt(Value v1, Value v2, Solver.SolverInterface c) {
        return abstractRelationalComparison(v2, v1, c);
    }

    /**
     * 11.8.3 <code>&lt;=</code>
     */
    public static Value le(Value v1, Value v2, Solver.SolverInterface c) {
        return not(abstractRelationalComparison(v2, v1, c));
    }

    /**
     * 11.8.4 <code>&gt;=</code>
     */
    public static Value ge(Value v1, Value v2, Solver.SolverInterface c) {
        return not(abstractRelationalComparison(v1, v2, c));
    }

    /**
     * 11.8.5 The Abstract Relational Comparison Algorithm.
     */
    private static Value abstractRelationalComparison(Value v1, Value v2, Solver.SolverInterface c) {
        Value p1 = Conversion.toPrimitive(v1, Hint.NUM, c);
        Value p2 = Conversion.toPrimitive(v2, Hint.NUM, c);
        if (p1.isMaybeFuzzyStr() || p2.isMaybeFuzzyStr()
                || p1.isMaybeAnyBool() || p2.isMaybeAnyBool()
                || p1.isMaybeFuzzyNum() || p2.isMaybeFuzzyNum())
            return Value.makeAnyBool();  // may be undefined according to items 6 and 7, but changed to false in 11.8.1-4
        else if (p1.isNotStr() || p2.isNotStr()) {
            // at most one argument is a string: perform numeric comparison
            return numericComparison(p1, p2, c);
        } else {
            // (at least) two defined string arguments: perform a string comparison
            Value r;
            String st1 = p1.getStr();
            String st2 = p2.getStr();
            if (st1 != null && st2 != null) {
                if (st1.compareTo(st2) < 0)
                    r = Value.makeBool(true);
                else
                    r = Value.makeBool(false);
            } else
                r = Value.makeNone();
            if (p1.isMaybeOtherThanStr() || p2.isMaybeOtherThanStr())
                r = r.join(numericComparison(p1, p2, c));
            return r;
        }
    }

    /**
     * Numeric comparison, used by abstractRelationalComparison.
     */
    private static Value numericComparison(Value p1, Value p2, Solver.SolverInterface c) {
        if (p1.isNotPresent() || p2.isNotPresent())
            return Value.makeNone();
        Value n1 = Conversion.toNumber(p1, c);
        Value n2 = Conversion.toNumber(p2, c);
        if (n1.isMaybeSingleNum() && n2.isMaybeSingleNum())
            return Value.makeBool(n1.getNum() < n2.getNum());
        if (n1.isNaN() || n2.isNaN())
            return Value.makeBool(false);
        return Value.makeAnyBool();
    }

    /**
     * 11.8.6 <code>instanceof</code>
     */
    public static Value instof(Value v1, Value v2, Solver.SolverInterface c) {
        //  11.8.6 step 5-6
        boolean maybe_v2_non_function = v2.isMaybePrimitive();
        boolean maybe_v2_function = false;
        Set<ObjectLabel> v2_objlabels = v2.getObjectLabels();
        for (ObjectLabel objlabel : v2_objlabels)
            if (objlabel.getKind() == Kind.FUNCTION)
                maybe_v2_function = true;
            else
                maybe_v2_non_function = true;
        //  15.3.5.3 step 1-4
        Value v2_prototype = c.getAnalysis().getPropVarOperations().readPropertyValue(v2_objlabels, "prototype");
        v2_prototype = UnknownValueResolver.getRealValue(v2_prototype, c.getState());
        boolean maybe_v2_prototype_primitive = v2_prototype.isMaybePrimitive();
        boolean maybe_v2_prototype_nonprimitive = v2_prototype.isMaybeObject();
        c.getMonitoring().visitInstanceof(c.getNode(), maybe_v2_non_function, maybe_v2_function,
                maybe_v2_prototype_primitive, maybe_v2_prototype_nonprimitive);
        if (maybe_v2_non_function || maybe_v2_prototype_primitive) {
            Exceptions.throwTypeError(c);
            if ((maybe_v2_non_function && !maybe_v2_function)
                    || (maybe_v2_prototype_nonprimitive && !maybe_v2_prototype_primitive))
                return Value.makeNone();
        }
        return c.getState().hasInstance(v2_prototype.getObjectLabels(), v1);
    }

    /**
     * 11.8.7 <code>in</code>
     */
    public static Value in(Value v1, Value v2, Solver.SolverInterface c) {
        // 11.8.7 step 5
        boolean maybe_v2_object = v2.isMaybeObject();
        boolean maybe_v2_nonobject = v2.isMaybePrimitive();
        c.getMonitoring().visitIn(c.getNode(), maybe_v2_object, maybe_v2_nonobject);
        if (maybe_v2_nonobject) {
            Exceptions.throwTypeError(c);
            if (!maybe_v2_object)
                return Value.makeNone();
        }
        // 11.8.7 step 6-8
        Value v1_str = Conversion.toString(v1, c);
        if (v1_str.isMaybeSingleStr())
            return Value.makeBool(c.getAnalysis().getPropVarOperations().hasProperty(v2.getObjectLabels(), v1_str.getStr()));
        else // TODO: could return false if all objects in v2 are empty
            return Value.makeAnyBool();
    }

    /**
     * 11.9.1 <code>==</code>
     */
    public static Value eq(Value v1, Value v2, Solver.SolverInterface c) {
        return abstractEqualityComparison(v1, v2, c);
    }

    /**
     * 11.9.2 <code>&#33;=</code>
     */
    public static Value neq(Value v1, Value v2, Solver.SolverInterface c) {
        return not(abstractEqualityComparison(v1, v2, c));
    }

    /**
     * 11.9.3 The Abstract Equality Comparison Algorithm.
     */
    private static Value abstractEqualityComparison(Value v1, Value v2, Solver.SolverInterface c) {
        Value r = Value.makeNone();
        if (v1.isMaybeUndef()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(true);
            if (v2.isMaybeNull())
                r = r.joinBool(true);
            if (!v2.isNotBool())
                r = r.joinBool(false);
            if (!v2.isNotNum())
                r = r.joinBool(false);
            if (!v2.isNotStr())
                r = r.joinBool(false);
            if (v2.isMaybeObject())
                r = r.joinBool(false);
        }
        if (v1.isMaybeNull()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(true);
            if (v2.isMaybeNull())
                r = r.joinBool(true);
            if (!v2.isNotBool())
                r = r.joinBool(false);
            if (!v2.isNotNum())
                r = r.joinBool(false);
            if (!v2.isNotStr())
                r = r.joinBool(false);
            if (v2.isMaybeObject())
                r = r.joinBool(false);
        }
        if (!v1.isNotBool()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(false);
            if (v2.isMaybeNull())
                r = r.joinBool(false);
            if (!v2.isNotBool()) {
                Num n1 = Conversion.fromBooltoNum(v1);
                Num n2 = Conversion.fromBooltoNum(v2);
                r = abstractNumberEquality(r, n1, n2);
            }
            if (!v2.isNotNum()) {
                Num n1 = Conversion.fromBooltoNum(v1);
                r = abstractNumberEquality(r, n1, v2);
            }
            if (!v2.isNotStr()) {
                Num n1 = Conversion.fromBooltoNum(v1);
                Num n2 = Conversion.fromStrtoNum(v2, c);
                r = abstractNumberEquality(r, n1, n2);
            }
            if (v2.isMaybeObject()) {
                Num n1 = Conversion.fromBooltoNum(v1);
                Num n2 = Conversion.toNumber(weakToPrimitive(Value.makeObject(v2.getObjectLabels()), Hint.NUM, r, c), c);
                r = abstractNumberEquality(r, n1, n2);
            }
        }
        if (!v1.isNotNum()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(false);
            if (v2.isMaybeNull())
                r = r.joinBool(false);
            if (!v2.isNotBool()) {
                Num n2 = Conversion.fromBooltoNum(v2);
                r = abstractNumberEquality(r, v1, n2);
            }
            if (!v2.isNotNum()) {
                r = abstractNumberEquality(r, v1, v2);
            }
            if (!v2.isNotStr()) {
                Num n2 = Conversion.fromStrtoNum(v2, c);
                r = abstractNumberEquality(r, v1, n2);
            }
            if (v2.isMaybeObject()) {
                Value arg1 = v1.restrictToNum();
                Value arg2 = weakToPrimitive(Value.makeObject(v2.getObjectLabels()), Hint.NONE, r, c);
                r = r.join(abstractEqualityComparison(arg1, arg2, c));
            }
        }
        if (!v1.isNotStr()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(false);
            if (v2.isMaybeNull())
                r = r.joinBool(false);
            if (!v2.isNotBool()) {
                Num n1 = Conversion.fromStrtoNum(v1, c);
                Num n2 = Conversion.fromBooltoNum(v2);
                r = abstractNumberEquality(r, n1, n2);
            }
            if (!v2.isNotNum()) {
                Num n1 = Conversion.fromStrtoNum(v1, c);
                r = abstractNumberEquality(r, n1, v2);
            }
            if (!v2.isNotStr()) {
                if (v1.isMaybeFuzzyStr() || v2.isMaybeFuzzyStr())
                    r = Value.makeAnyBool();
                else {
                    String s1 = v1.getStr();
                    String s2 = v2.getStr();
                    if (s1 != null && s2 != null) {
                        r = r.joinBool(s1.equals(s2));
                    }
                }
            }
            if (v2.isMaybeObject()) {
                Value arg1 = v1.restrictToStr();
                Value arg2 = weakToPrimitive(Value.makeObject(v2.getObjectLabels()), Hint.NONE, r, c);
                r = r.join(abstractEqualityComparison(arg1, arg2, c));
            }
        }
        if (v1.isMaybeObject()) {
            if (v2.isMaybeUndef()) r = r.joinBool(false);
            if (v2.isMaybeNull()) r = r.joinBool(false);
            Value vv1 = Value.makeObject(v1.getObjectLabels());
            if (!v2.isNotBool()) {
                Num n1 = Conversion.toNumber(weakToPrimitive(vv1, Hint.NUM, r, c), c);
                Num n2 = Conversion.fromBooltoNum(v2);
                r = abstractNumberEquality(r, n1, n2);
            }
            if (!v2.isNotNum()) {
                Value arg1 = weakToPrimitive(vv1, Hint.NONE, r, c);
                Value arg2 = v2.restrictToNum();
                r = r.join(abstractEqualityComparison(arg1, arg2, c));
            }
            if (!v2.isNotStr()) {
                Value arg1 = weakToPrimitive(vv1, Hint.NONE, r, c);
                Value arg2 = v2.restrictToStr();
                r = r.join(abstractEqualityComparison(arg1, arg2, c));
            }
            if (v2.isMaybeObject()) {
                r = eqObject(r, v1.getObjectLabels(), v2.getObjectLabels());
            }
        }
        return r;
    }

    private static Value weakToPrimitive(Value v, Hint hint, Value r, Solver.SolverInterface c) {
        State s = c.getState();
        if (!r.isNone()) {
            c.setState(s.clone());
        }
        v = Conversion.toPrimitive(v, hint, c);
        if (!r.isNone()) {
            c.getState().propagate(s, false); // weak update of side-effects of toPrimitive, but only if we already have a partial result
        }
        return v;
    }

    /**
     * Part of 11.9.3 The Abstract Equality Comparison Algorithm and 11.9.6 The Strict Equality Comparison Algorithm.
     */
    private static Value eqObject(Bool r, Collection<ObjectLabel> labels1, Collection<ObjectLabel> labels2) {
        Set<ObjectLabel> labelsInBoth = newSet();
        labelsInBoth.addAll(labels1);
        labelsInBoth.retainAll(labels2);
        if (labelsInBoth.isEmpty())
            return r.joinBool(false);
        else if (labels1.size() == 1 && labels2.size() == 1 && labelsInBoth.iterator().next().isSingleton())
            return r.joinBool(true);
        else
            return r.joinAnyBool();
    }

    /**
     * Part of 11.9.3 The Abstract Equality Comparison Algorithm and 11.9.6 The Strict Equality Comparison Algorithm.
     */
    private static Value abstractNumberEquality(Value r, Num n1, Num n2) {
        if (r.isMaybeAnyBool() || n1.isNotNum() || n2.isNotNum())
            return r;
        if (n1.isMaybeNaN() || n2.isMaybeNaN()) {
            r = r.joinBool(false);
            if (n1.isNaN() || n2.isNaN())
                return r;
        }
        if (n1.isMaybeSingleNum() && n2.isMaybeSingleNum()) {
            double d1 = n1.getNum();
            double d2 = n2.getNum();
            if (d1 == d2 || d1 == 0.0 && d2 == -0.0 || d1 == -0.0 && d2 == 0.0) // FIXME: comparisons with 0.0 or -0.0 always false?!
                // absolute weirdness, but required by standard 11.9.3 points 8 and 9
                return r.joinBool(true);
            else
                return r.joinBool(false);
        }
        return Value.makeAnyBool();
    }

    /**
     * 11.9.4 <code>===</code>
     */
    public static Value stricteq(Value v1, Value v2) {
        return strictEqualityComparison(v1, v2);
    }

    /**
     * 11.9.5 <code>&#33;==</code>
     */
    public static Value strictneq(Value v1, Value v2) {
        return not(strictEqualityComparison(v1, v2));
    }

    /**
     * 11.9.6 The Strict Equality Comparison Algorithm.
     */
    private static Value strictEqualityComparison(Value v1, Value v2) {
        Value r = Value.makeNone();
        if (v1.isMaybeUndef()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(true);
            if (v2.isMaybeNull())
                r = r.joinBool(false);
            if (!v2.isNotBool())
                r = r.joinBool(false);
            if (!v2.isNotNum())
                r = r.joinBool(false);
            if (!v2.isNotStr())
                r = r.joinBool(false);
            if (v2.isMaybeObject())
                r = r.joinBool(false);
        }
        if (v1.isMaybeNull()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(false);
            if (v2.isMaybeNull())
                r = r.joinBool(true);
            if (!v2.isNotBool())
                r = r.joinBool(false);
            if (!v2.isNotNum())
                r = r.joinBool(false);
            if (!v2.isNotStr())
                r = r.joinBool(false);
            if (v2.isMaybeObject())
                r = r.joinBool(false);
        }
        if (!v1.isNotBool()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(false);
            if (v2.isMaybeNull())
                r = r.joinBool(false);
            if (!v2.isNotBool()) {
                if (v1.isMaybeAnyBool() || v2.isMaybeAnyBool())
                    return Value.makeAnyBool();
                else if (v1.isMaybeTrueButNotFalse() && v2.isMaybeTrueButNotFalse() ||
                        v1.isMaybeFalseButNotTrue() && v2.isMaybeFalseButNotTrue())
                    r = r.joinBool(true);
                else r = r.joinBool(false);
            }
            if (!v2.isNotNum())
                r = r.joinBool(false);
            if (!v2.isNotStr())
                r = r.joinBool(false);
            if (v2.isMaybeObject())
                r = r.joinBool(false);
        }
        if (!v1.isNotNum()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(false);
            if (v2.isMaybeNull())
                r = r.joinBool(false);
            if (!v2.isNotBool())
                r = r.joinBool(false);
            if (!v2.isNotNum())
                r = abstractNumberEquality(r, v1, v2);
            if (!v2.isNotStr())
                r = r.joinBool(false);
            if (v2.isMaybeObject())
                r = r.joinBool(false);
        }
        if (!v1.isNotStr()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(false);
            if (v2.isMaybeNull())
                r = r.joinBool(false);
            if (!v2.isNotBool())
                r = r.joinBool(false);
            if (!v2.isNotNum())
                r = r.joinBool(false);
            if (!v2.isNotStr()) {
                if (v1.isMaybeFuzzyStr() || v2.isMaybeFuzzyStr())
                    return Value.makeAnyBool();
                else {
                    String s1 = v1.getStr();
                    String s2 = v2.getStr();
                    if (s1 != null && s2 != null)
                        r = r.joinBool(s1.equals(s2));
                }
            }
            if (v2.isMaybeObject())
                r = r.joinBool(false);
        }
        if (v1.isMaybeObject()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(false);
            if (v2.isMaybeNull())
                r = r.joinBool(false);
            if (!v2.isNotBool())
                r = r.joinBool(false);
            if (!v2.isNotNum())
                r = r.joinBool(false);
            if (!v2.isNotStr())
                r = r.joinBool(false);
            if (v2.isMaybeObject())
                r = eqObject(r, v1.getObjectLabels(), v2.getObjectLabels());
        }
        return r;
    }

    /**
     * 11.10 Binary Bitwise Operators
     */
    private static Value bitwise(BitwiseOp op, Value arg1, Value arg2, Solver.SolverInterface c) {
        arg1 = Conversion.toNumber(arg1, c);
        arg2 = Conversion.toNumber(arg2, c);
        if (arg1.isNotPresent() || arg2.isNotPresent())
            return Value.makeNone();
        else if (arg1.isMaybeSingleNum() && arg2.isMaybeSingleNum()) {
            int i1 = Conversion.toInt32(arg1.getNum());
            int i2 = Conversion.toInt32(arg2.getNum());
            int r = 0;
            switch (op) {
                case AND:
                    r = i1 & i2;
                    break;
                case OR:
                    r = i1 | i2;
                    break;
                case XOR:
                    r = i1 ^ i2;
                    break;
            }
            return Value.makeNum(r);
        } else // TODO: Improve precision: NaN | 0 gives AnyNumUInt (testMicro186).
            return Value.makeAnyNumUInt();
    }

    /**
     * 11.10<code>&amp;</code>
     */
    public static Value and(Value arg1, Value arg2, Solver.SolverInterface c) {
        return bitwise(BitwiseOp.AND, arg1, arg2, c);
    }

    /**
     * 11.10 <code>|</code>
     */
    public static Value or(Value arg1, Value arg2, Solver.SolverInterface c) {
        return bitwise(BitwiseOp.OR, arg1, arg2, c);
    }

    /**
     * 11.10 <code>^</code>
     */
    public static Value xor(Value arg1, Value arg2, Solver.SolverInterface c) {
        return bitwise(BitwiseOp.XOR, arg1, arg2, c);
    }

    private enum NumericOp {ADD, SUB, MUL, DIV, MOD}

    private enum BitwiseOp {AND, OR, XOR}

    private enum ShiftOp {LEFTSHIFT, SIGNEDRIGHTSHIFT, UNSIGNEDRIGHTSHIFT}
}
