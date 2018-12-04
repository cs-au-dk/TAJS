/*
 * Copyright 2009-2018 Aarhus University
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
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;
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
        if (v.isNone()) {
            return Value.makeNone();
        }
        boolean maybe_boolean = !v.isNotBool();
        boolean maybe_number = !v.isNotNum();
        boolean maybe_string = !v.isNotStr();
        boolean maybe_undefined = v.isMaybeUndef() || base_maybe_null;
        boolean maybe_object = v.isMaybeNull();
        boolean maybe_symbol = false;
        boolean maybe_function = false;
        for (ObjectLabel objlabel : v.getObjectLabels()) {
            if (objlabel.getKind() == Kind.FUNCTION) {
                maybe_function = true;
            } else if (objlabel.getKind() == Kind.SYMBOL) {
                maybe_symbol = true;
            } else {
                maybe_object = true;
            }
        }

        Set<String> values = newSet();
        Set<String> notValues = newSet();
        // table p. 47
        (maybe_boolean ? values : notValues).add("boolean");
        (maybe_number ? values : notValues).add("number");
        (maybe_string ? values : notValues).add("string");
        (maybe_undefined ? values : notValues).add("undefined");
        (maybe_function ? values : notValues).add("function");
        (maybe_object ? values : notValues).add("object");
        (maybe_symbol ? values : notValues).add("symbol");

        if (values.isEmpty()) {
            throw new AnalysisException("No case for `typeof " + v + "`???");
        }

        return Value.join(values.stream().map(Value::makeStr).collect(Collectors.toSet())).restrictToNotStrings(notValues);
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
        if (nm.isMaybeAnyNum()) {
            return nm;
        }
        if (nm.isNotNum()) {
            return Value.makeNone();
        }
        if (nm.isMaybeSingleNum()) {
            return Value.makeNum(-nm.getNum());
        }
        Value result = Value.makeNone();
        if (nm.isMaybeNumUIntPos()) {
            result = result.joinAnyNumOther();
        }
        if (nm.isMaybeNumOther()) {
            result = result.joinAnyNumUInt().joinAnyNumOther();
        }
        if (nm.isMaybeNaN()) {
            result = result.joinNumNaN();
        }
        if (nm.isMaybeInf()) {
            result = result.joinNumInf();
        }
        if (nm.isMaybeZero()) {
            result = result.joinNum(0); // will become abstract zero (both positive and negative)
        } else {
            result = result.restrictToNotNumZero();
        }
        return result;
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
                if (arg1.isMaybeInf() && arg2.isMaybeInf())
                    r = r.joinNumNaN();
                if (arg1.isMaybeInf() || arg2.isMaybeInf())
                    r = r.joinNumInf();
                if (arg1.isMaybeSingleNum() && arg1.getNum() == 0) {
                    r = r.join(arg2); // 0 + x === x
                } else if (arg2.isMaybeSingleNum() && arg2.getNum() == 0) {
                    r = r.join(arg1); // x + 0 === x
                } else if (((arg1.isMaybeNumUInt() && !arg1.restrictToNotNaN().isMaybeOtherThanNumUInt()) || arg1.isMaybeSingleNumUInt()) &&
                        ((arg2.isMaybeNumUInt() && !arg2.restrictToNotNaN().isMaybeOtherThanNumUInt()) || arg2.isMaybeSingleNumUInt())) {
                    r = r.joinAnyNumUInt();
                    if (!c.getAnalysis().getUnsoundness().mayAssumeClosedUIntAddition(c.getNode())) {
                        r = r.joinAnyNumOther().joinNumInf();
                    }
                } else {
                    r = r.joinAnyNumUInt().joinAnyNumOther();
                    if (isExtremeSingleNumber(arg1) || isExtremeSingleNumber(arg2)) {
                        // avoids ignoring deliberate overflows
                        r = r.joinNumInf();
                    }
                }

                if (!arg1.isMaybeSameNumberWhenNegated(arg2)) {
                    r = r.restrictToNotNumZero();
                }
                break;
            case SUB:
                if (arg1.isMaybeInf() && arg2.isMaybeInf())
                    r = r.joinNumNaN();
                if (arg1.isMaybeInf() || arg2.isMaybeInf())
                    r = r.joinNumInf();
                if (arg2.isMaybeSingleNum() && arg2.getNum() == 0) {
                    r = r.join(arg1); // x - 0 === x
                } else {
                    r = r.joinAnyNumUInt().joinAnyNumOther();
                    if (isExtremeSingleNumber(arg1) || isExtremeSingleNumber(arg2)) {
                        // avoids ignoring deliberate overflows
                        r = r.joinNumInf();
                    }
                }
                if (!arg1.restrictToNotNaN().restrictToNotNumInf().isMaybeSameNumber(arg2)) {
                    r = r.restrictToNotNumZero();
                }
                break;
            case MUL:
                if (arg1.isMaybeInf() && arg2.isMaybeInf())
                    r = r.joinNumInf();
                if (arg1.isMaybeInf() && !arg2.isNotNum())
                    r = r.joinNumNaN().joinNumInf();
                if (!arg1.isNotNum() && arg2.isMaybeInf())
                    r = r.joinNumNaN().joinNumInf();
                if (!arg1.isNotNum() && !arg2.isNotNum()) {
                    r = r.joinAnyNumUInt().joinNumInf().joinAnyNumOther();
                }
                if (!arg1.isMaybeZero() && !arg2.isMaybeZero()) {
                    r = r.restrictToNotNumZero();
                }
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
                if (!arg1.isNotNum() && !arg2.isNotNum()) {
                    r = r.joinAnyNumUInt().joinAnyNumOther().joinNumNaN(); // TODO: (use Options.get().isUnsound())
                }
                if (!arg1.isMaybeZero() && !arg2.isMaybeInf() && !arg1.isMaybeNumOther() /* very small numbers can be divided to zero! */) {
                    r = r.restrictToNotNumZero();
                }
                if (arg2.isMaybeZero())
                    r = r.joinNumInf();
                break;
            case MOD:
                if (arg1.isMaybeInf() && arg2.isMaybeInf())
                    r = r.joinNumNaN();
                if (arg1.isMaybeInf() && !arg2.isNotNum())
                    r = r.joinNumNaN();
                if (!arg1.isNotNum() && arg2.isMaybeInf())
                    r = r.join(arg1);
                if (!arg1.isNotNum() && !arg2.isNotNum())
                    r = r.joinAnyNumUInt().joinAnyNumOther(); // TODO: (use Options.get().isUnsound())
                if (arg2.isMaybeZero())
                    r = r.joinNumNaN();
                break;
        }
        return r;
    }

    private static boolean isExtremeSingleNumber(Value v) {
        return v.isMaybeSingleNum() && (v.getNum() == Double.MAX_VALUE || v.getNum() == Double.MIN_VALUE);
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
        if (c.getAnalysis().getUnsoundness().mayIgnoreUnlikelyUndefinedAsFirstArgumentToAddition(c.getNode(), p1)) {
            p1 = p1.restrictToNotUndef();
        }
        Value p2 = Conversion.toPrimitive(v2, Hint.NONE, c);
        Value r1 = p1.restrictToNotStr();
        Value r2 = p2.restrictToNotStr();
        // handle string parts of p1 + string parts of p2
        Value r = addStrings(p1, p2, Value.makeNone());
        // handle string parts of p1 + non-string parts of p2
        if (!p1.isNotStr() && p2.isMaybeOtherThanStr())
            r = r.join(addStrings(p1, Conversion.toString(r2, c), r));
        // handle non-string parts of p1 + string parts of p2
        if (!p2.isNotStr() && p1.isMaybeOtherThanStr())
            r = r.join(addStrings(Conversion.toString(r1, c), p2, r));
        // handle non-string parts of p1 + non-string parts of p2
        if (r1.isMaybePresent() && r2.isMaybePresent())
            r = r.join(addNumbers(r1, r2, c));
        return r;
    }

    private static Value addStrings(Str s1, Str s2, Value r) { // TODO: could be more precise in some cases...
        if (s1.isMaybeAllKnownStr() && s2.isMaybeAllKnownStr()) {
            // s1 and s2 are both known strings
            if (s1.isMaybeSingleStr() && s2.isMaybeSingleStr() && s1.getIncludedStrings() == null && s2.getIncludedStrings() == null) {
                r = r.joinStr(s1.getStr() + s2.getStr());
            } else {
                Set<Value> vs = newSet();
                Set<String> vs1 = s1.getAllKnownStr();
                Set<String> vs2 = s2.getAllKnownStr();
                for (String ss1 : vs1) {
                    for (String ss2 : vs2) {
                        vs.add(Value.makeStr(ss1 + ss2));
                    }
                }
                r = r.join(Value.join(vs));
                if ((vs.size() > Options.Constants.STRING_CONCAT_SETS_BOUND) && vs1.size() > 1 && vs2.size() > 1) {
                    // widen
                    r = r.forgetExcludedIncludedStrings();
                }
            }
        } else if (s1.isMaybeSingleStr()) {
            // s1 is single string, handle string parts of s2
            if (s2.isMaybeSingleStr()) {
                // s1 and s2 are both single strings
                r = r.joinStr(s1.getStr() + s2.getStr()); // (covered by case above)
            } else if (s1.getStr().isEmpty()) {
                r = r.join(s2.restrictToStr());
            } else if (s2.isMaybeStrPrefix()) {
                r = r.joinPrefix(s1.getStr() + s2.getPrefix());
            } else if (s2.isMaybeFuzzyStr()) {
                // s1 is single string, s2 is fuzzy string
                r = r.joinPrefix(s1.getStr());
            }
        } else if (s1.isMaybeFuzzyStr()) {
            // s1 is fuzzy string, handle string parts of s2
            if (s2.isMaybeSingleStr()) {
                // s1 is fuzzy string, p2 is single string
                if (s2.getStr().isEmpty()) {
                    r = r.join(s1.restrictToStr());
                } else if (s1.isMaybeStrPrefix())
                    r = r.joinPrefix(s1.getPrefix());
                else if ((s1.isMaybeStrUInt() || s1.isMaybeStrIdentifier() || s1.isMaybeStrOtherIdentifierParts())
                        && !(s1.isMaybeStrOtherNum() || s1.isMaybeStrOther())
                        && Strings.isIdentifierParts(s2.getStr()))
                    r = r.joinAnyStrIdentifierParts();
                else
                    r = Value.makeAnyStr();
            } else if (s2.isMaybeFuzzyStr()) {
                // s1 and s2 are both fuzzy strings
                if (s1.isMaybeStrPrefix())
                    r = r.joinPrefix(s1.getPrefix());
                else if (s1.isMaybeStrUInt() && !s1.isMaybeStrSomeNonUInt() && s2.isMaybeStrUInt() && !s2.isMaybeStrSomeNonUInt()) {
                    r = r.joinAnyStrOtherNum().joinAnyStrUInt();
                } else if ((s1.isMaybeStrUInt() || s1.isMaybeStrIdentifier() || s1.isMaybeStrOtherIdentifierParts())
                        && !(s1.isMaybeStrOtherNum() || s1.isMaybeStrOther())
                        && (s2.isMaybeStrUInt() || s2.isMaybeStrIdentifier() || s2.isMaybeStrOtherIdentifierParts())
                        && !(s2.isMaybeStrOtherNum() || s2.isMaybeStrOther()))
                    r = r.joinAnyStrIdentifierParts();
                else
                    r = Value.makeAnyStr();
            }
        }

        String s1characters = s1.isMaybeSingleStr() ? s1.getStr() : s1.isMaybeStrPrefix() ? s1.getPrefix() : "";
        String s2characters = s2.isMaybeSingleStr() ? s2.getStr() : s2.isMaybeStrPrefix() ? s2.getPrefix() : "";
        boolean containsNonIdentifierCharacters = (!s1characters.isEmpty() && !Strings.isIdentifierParts(s1characters)) || (!s2characters.isEmpty() && !Strings.isIdentifierParts(s2characters));
        if (containsNonIdentifierCharacters) {
            r = r.restrictToNotStrIdentifierParts();
        }
        boolean containsNonDigits = (!s1characters.isEmpty() && !Strings.isArrayIndex(s1characters)) || (!s2characters.isEmpty() && !Strings.isArrayIndex(s2characters));
        if (containsNonDigits) {
            r = r.restrictToNotStrUInt();
        }
        boolean containsNonOtherNumCharacters = Strings.containsNonNumberCharacters(s1characters) || Strings.containsNonNumberCharacters(s2characters);
        if (containsNonOtherNumCharacters) {
            r = r.restrictToNotStrOtherNum();
        }
        if (s1.isMaybeStrJSON() || s2.isMaybeStrJSON()) // FIXME: hack to handle "(" + JSON + ")", github #374
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
            return Value.makeAnyNumNotNaNInf();
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
        if (v1.isMaybeSingleNum() && v1.getNum().equals(0.0) && !v1.isMaybeOtherThanNum() && v2.isMaybeNumUIntPos() && !v2.isMaybeZero() && !v2.isMaybeOtherThanNumUInt()) {
            // 0 is less than UIntPos
            return Value.makeBool(true);
        }
        return abstractRelationalComparison(v1, v2, c);
    }

    /**
     * 11.8.2 <code>&gt;</code>
     */
    public static Value gt(Value v1, Value v2, Solver.SolverInterface c) {
        if (v1.isMaybeNumUIntPos() && !v1.isMaybeZero() && !v1.isMaybeOtherThanNumUInt() && v2.isMaybeSingleNum() && v2.getNum().equals(0.0) && !v2.isMaybeOtherThanNum()) {
            // UIntPos is greater than 0
            return Value.makeBool(true);
        }
        return abstractRelationalComparison(v2, v1, c);
    }

    /**
     * 11.8.3 <code>&lt;=</code>
     */
    public static Value le(Value v1, Value v2, Solver.SolverInterface c) {
        return abstractRelationalComparison(v2, v1, true, c);
    }

    /**
     * 11.8.4 <code>&gt;=</code>
     */
    public static Value ge(Value v1, Value v2, Solver.SolverInterface c) {
        return abstractRelationalComparison(v1, v2, true, c);
    }

    /**
     * 11.8.5 The Abstract Relational Comparison Algorithm.
     */
    private static Value abstractRelationalComparison(Value v1, Value v2, Solver.SolverInterface c) {
        return abstractRelationalComparison(v1, v2, false, c);
    }

    private static Value abstractRelationalComparison(Value v1, Value v2, boolean negateResultIfAllowed, Solver.SolverInterface c) {
        Value p1 = Conversion.toPrimitive(v1, Hint.NUM, c);
        Value p2 = Conversion.toPrimitive(v2, Hint.NUM, c);
        if (p1.isMaybeFuzzyStr() || p2.isMaybeFuzzyStr() // TODO: could improve precision using Value.isStringDisjoint?
                || p1.isMaybeAnyBool() || p2.isMaybeAnyBool()
                || (p1.isMaybeFuzzyNum() && !p1.isNaN()) || (p2.isMaybeFuzzyNum() && !p2.isNaN()))
            return Value.makeAnyBool();  // may be undefined according to items 6 and 7, but changed to false in 11.8.1-4
        else if (p1.isNotStr() || p2.isNotStr()) {
            // at most one argument is a string: perform numeric comparison
            return numericComparison(p1, p2, negateResultIfAllowed, c);
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
                r = negateResultIfAllowed ? not(r) : r;
            } else
                r = Value.makeNone();
            if (p1.isMaybeOtherThanStr() || p2.isMaybeOtherThanStr()) {
                r = r.join(numericComparison(p1, p2, negateResultIfAllowed, c));
            }
            return r;
        }
    }

    private static Value numericComparison(Value p1, Value p2, boolean negateResultIfAllowed, Solver.SolverInterface c) {
        Pair<Value, Boolean> comparisonResult = numericComparison(p1, p2, c);
        Value result = comparisonResult.getFirst();
        Boolean mayNegate = comparisonResult.getSecond();
        boolean negate = mayNegate && negateResultIfAllowed;
        return negate ? not(result) : result;
    }

    /**
     * Numeric comparison, used by abstractRelationalComparison.
     *
     * @return pair of comparison result and a boolean that is true if the result may be negated (NaN comparisons always yield false!)
     */
    private static Pair<Value, Boolean> numericComparison(Value p1, Value p2, Solver.SolverInterface c) {
        if (p1.isNotPresent() || p2.isNotPresent())
            return Pair.make(Value.makeNone(), true);
        Value n1 = Conversion.toNumber(p1, c);
        Value n2 = Conversion.toNumber(p2, c);
        if (n1.isMaybeSingleNum() && n2.isMaybeSingleNum())
            return Pair.make(Value.makeBool(n1.getNum() < n2.getNum()), true);
        if (n1.isNaN() || n2.isNaN())
            return Pair.make(Value.makeBool(false), false);
        return Pair.make(Value.makeAnyBool(), true);
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
        boolean maybe_v2_prototype_nonobject = v2_prototype.isMaybePrimitiveOrSymbol();
        boolean maybe_v2_prototype_object = v2_prototype.isMaybeObject();
        c.getMonitoring().visitInstanceof(c.getNode(), maybe_v2_non_function, maybe_v2_function,
                maybe_v2_prototype_nonobject, maybe_v2_prototype_object);
        if (maybe_v2_non_function || maybe_v2_prototype_nonobject) {
            Exceptions.throwTypeError(c);
            if (!maybe_v2_function || !maybe_v2_prototype_object)
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
        boolean maybe_v2_nonobject = v2.isMaybePrimitiveOrSymbol();
        c.getMonitoring().visitIn(c.getNode(), maybe_v2_object, maybe_v2_nonobject);
        if (maybe_v2_nonobject) {
            Exceptions.throwTypeError(c);
            if (!maybe_v2_object)
                return Value.makeNone();
        }
        // 11.8.7 step 6-8
        Value v1_strorsymbol = Conversion.toString(v1.restrictToNotSymbol(), c).join(v1.restrictToSymbol());
        Value res = Value.makeBool(c.getAnalysis().getPropVarOperations().hasProperty(v2.getObjectLabels(), v1_strorsymbol));

        if (c.getAnalysis().getUnsoundness().mayAssumeInOperatorReturnsTrueWhenSoundResultIsMaybeTrueAndPropNameIsNumber(c.getNode(), v1, res)) {
            res = Value.makeBool(true);
        }

        return res;
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
            if (v2.isMaybeObjectOrSymbol())
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
            if (v2.isMaybeObjectOrSymbol())
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
                Value n2 = v2.restrictToNum();
                r = abstractNumberEquality(r, n1, n2);
            }
            if (!v2.isNotStr()) {
                Num n1 = Conversion.fromBooltoNum(v1);
                Num n2 = Conversion.fromStrtoNum(v2, c);
                r = abstractNumberEquality(r, n1, n2);
            }
            if (v2.isMaybeObject()) {
                Num n1 = Conversion.fromBooltoNum(v1);
                Num n2 = Conversion.toNumber(weakToPrimitive(v2.restrictToObject(), Hint.NUM, r, c), c);
                r = abstractNumberEquality(r, n1, n2);
            }
            if (v2.isMaybeSymbol()) {
                Num n1 = Conversion.fromBooltoNum(v1);
                Num n2 = v2.restrictToSymbol();
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
                Value n1 = v1.restrictToNum();
                Value n2 = weakToPrimitive(v2.restrictToObject(), Hint.NONE, r, c);
                r = r.join(abstractEqualityComparison(n1, n2, c));
            }
            if (v2.isMaybeSymbol()) {
                r = r.joinBool(false);
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
                r = r.join(stringEqualityComparison(v1, v2));
            }
            if (v2.isMaybeObject()) {
                Value n1 = v1.restrictToStr();
                Value n2 = weakToPrimitive(v2.restrictToObject(), Hint.NONE, r, c);
                r = r.join(abstractEqualityComparison(n1, n2, c));
            }
            if (v2.isMaybeSymbol()) {
                r = r.joinBool(false);
            }
        }
        if (v1.isMaybeSymbol()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(false);
            if (v2.isMaybeNull())
                r = r.joinBool(false);
            if (!v2.isNotBool()) {
                Value n1 = v1.restrictToSymbol();
                Num n2 = Conversion.fromBooltoNum(v2);
                r = abstractNumberEquality(r, n1, n2);
            }
            if (!v2.isNotNum()) {
                r = r.joinBool(false);
            }
            if (!v2.isNotStr()) {
                r = r.joinBool(false);
            }
            if (v2.isMaybeObject()) {
                Value n1 = v1.restrictToSymbol();
                Value n2 = weakToPrimitive(v2.restrictToObject(), Hint.NONE, r, c);
                r = r.join(abstractEqualityComparison(n1, n2, c));
            }
            if (v2.isMaybeSymbol()) {
                r = eqObjectOrSymbol(r, v1.getSymbols(), v2.getSymbols());
            }
        }
        if (v1.isMaybeObject()) {
            if (v2.isMaybeUndef())
                r = r.joinBool(false);
            if (v2.isMaybeNull())
                r = r.joinBool(false);
            Value vv1 = v1.restrictToObject();
            if (!v2.isNotBool()) {
                Num n1 = Conversion.toNumber(weakToPrimitive(vv1, Hint.NUM, r, c), c);
                Num n2 = Conversion.fromBooltoNum(v2);
                r = abstractNumberEquality(r, n1, n2);
            }
            if (!v2.isNotNum()) {
                Value n1 = weakToPrimitive(vv1, Hint.NONE, r, c);
                Value n2 = v2.restrictToNum();
                r = r.join(abstractEqualityComparison(n1, n2, c));
            }
            if (!v2.isNotStr()) {
                Value n1 = weakToPrimitive(vv1, Hint.NONE, r, c);
                Value n2 = v2.restrictToStr();
                r = r.join(abstractEqualityComparison(n1, n2, c));
            }
            if (v2.isMaybeObject()) {
                r = eqObjectOrSymbol(r, vv1.getObjectLabels(), v2.restrictToObject().getObjectLabels());
            }
            if (v2.isMaybeSymbol()) {
                Value n1 = weakToPrimitive(vv1, Hint.NONE, r, c);
                Value n2 = v2.restrictToSymbol();
                r = r.join(abstractEqualityComparison(n1, n2, c));
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
            c.getState().propagate(s, false, false); // weak update of side-effects of toPrimitive, but only if we already have a partial result
        }
        return v;
    }

    /**
     * Part of 11.9.3 The Abstract Equality Comparison Algorithm and 11.9.6 The Strict Equality Comparison Algorithm.
     */
    private static Value eqObjectOrSymbol(Bool r, Collection<ObjectLabel> labels1, Collection<ObjectLabel> labels2) {
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
            // (NB Java-== does not distinguish -0.0 and +0.0)
            return r.joinBool(d1 == d2);
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
            if (v2.isMaybeObjectOrSymbol())
                r = r.joinBool(false);
        }
        if (r.isMaybeAnyBool()) {
            return r;
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
            if (v2.isMaybeObjectOrSymbol())
                r = r.joinBool(false);
        }
        if (r.isMaybeAnyBool()) {
            return r;
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
            if (v2.isMaybeObjectOrSymbol())
                r = r.joinBool(false);
        }
        if (r.isMaybeAnyBool()) {
            return r;
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
            if (v2.isMaybeObjectOrSymbol())
                r = r.joinBool(false);
        }
        if (r.isMaybeAnyBool()) {
            return r;
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
                r = r.join(stringEqualityComparison(v1, v2));
            }
            if (v2.isMaybeObjectOrSymbol())
                r = r.joinBool(false);
        }
        if (r.isMaybeAnyBool()) {
            return r;
        }
        if (v1.isMaybeObjectOrSymbol()) {
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
            if (v2.isMaybeObjectOrSymbol())
                r = eqObjectOrSymbol(r, v1.getObjectLabels(), v2.getObjectLabels());
        }
        return r;
    }

    private static Value stringEqualityComparison(Str v1, Str v2) {
        if (!v1.isMaybeFuzzyStr() && !v2.isMaybeFuzzyStr()) {
            return Value.makeBool(v1.getStr().equals(v2.getStr()));
        }
        if (doesSingleStringAndAbstractStringDisagree(v1, v2) || doesSingleStringAndAbstractStringDisagree(v2, v1)) {
            return Value.makeBool(false);
        }
        if (doesStrUIntAndStrIdentifierDisagree(v1, v2) || doesStrUIntAndStrIdentifierDisagree(v2, v1)) {
            return Value.makeBool(false);
        }
        return Value.makeAnyBool();
    }

    private static boolean doesSingleStringAndAbstractStringDisagree(Str v1, Str v2) {
        return v1.isMaybeSingleStr() && !v2.isMaybeStr(v1.getStr());
    }

    private static boolean doesStrUIntAndStrIdentifierDisagree(Str v1, Str v2) {
        return v1.isMaybeStrUInt() && !v1.isMaybeAnyStr() && v2.isStrIdentifier();
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
            return Value.makeAnyNumNotNaNInf();

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
