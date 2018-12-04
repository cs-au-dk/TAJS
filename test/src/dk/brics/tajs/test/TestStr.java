package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Strings;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;
import static org.junit.Assert.assertTrue;

public class TestStr { // TODO: coverage of Str operations?

    int TESTS = 100000;
    int STRINGS = 30;
    int OTHERVALUES = 10;
    int STRINGSETS = 10;

    Random rnd = new Random(0);

    String[] strings = new String[STRINGS];
    Value[] othervalues = new Value[OTHERVALUES];
    Value[] values = new Value[STRINGS + OTHERVALUES];
    List<Set<String>> stringsets = newList();

    String last = null;
    Object lastparam = null;

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableDebug();

        strings[0] = "NaN";
        strings[1] = "Infinity";
        strings[2] = "-Infinity";
        strings[3] = "catch";
        strings[4] = "char";
        strings[5] = "class";
        strings[6] = "const";
        strings[7] = "continue";
        strings[8] = "";
        strings[9] = "true";
        strings[10] = "false";
        strings[11] = "0";
        strings[12] = "1";
        for (int i = 13; i < STRINGS; i++) {
            String s;
            switch (rnd.nextInt(6)) {
                case 0:
                    s = Strings.randomString(rnd.nextInt(5));
                    break;
                case 1:
                    s = Integer.toString(rnd.nextInt(10) - 5);
                    break;
                case 2:
                    s = Integer.toString(rnd.nextInt());
                    break;
                case 3:
                    s = Double.toString(rnd.nextDouble());
                    break;
                case 4:
                    s = Double.toString(rnd.nextGaussian());
                    break;
                case 5:
                    int len = rnd.nextInt(5);
                    StringBuilder sb = new StringBuilder(len);
                    for (int j = 0; j < len; j++)
                        sb.append((char) rnd.nextInt());
                    s = sb.toString();
                    break;
                default:
                    throw new RuntimeException();
            }
            strings[i] = s;
        }
        for (int i = 0; i < STRINGS; i++)
            values[i] = Value.makeStr(strings[i]);

        othervalues[0] = Value.makeAnyStrUInt();
        othervalues[1] = Value.makeAnyStrNotNumeric();
        othervalues[2] = Value.makeAnyStr();
        othervalues[3] = Value.makeAnyStrIdent();
        othervalues[4] = Value.makeNone().joinAnyStr();
        othervalues[5] = Value.makeNone().joinAnyStrUInt();
        othervalues[6] = Value.makeNone().joinAnyStrIdentifierParts();
        othervalues[7] = Value.makeNone().joinAnyStrOtherNum();
        othervalues[8] = Value.makeNone().joinAnyStrOther();
        othervalues[9] = Value.makeNone();

        System.arraycopy(othervalues, 0, values, STRINGS, OTHERVALUES);

        for (int i = 0; i < STRINGSETS; i++) {
            int s = rnd.nextInt(3) + 1;
            Set<String> ss = newSet();
            for (int j = 0; j < s; j++)
                ss.add(strings[rnd.nextInt(STRINGS)]);
            stringsets.add(ss);
        }
    }

    Value nextRandomValue() {
        switch (rnd.nextInt(3)) {
            case 0:
                return Value.makeStr(strings[rnd.nextInt(STRINGS)]);
            case 1:
                return othervalues[rnd.nextInt(OTHERVALUES)];
            case 2:
                return Value.makeStrings(stringsets.get(rnd.nextInt(STRINGSETS)));
            default:
                throw new RuntimeException();
        }
    }

    void checkMonotone(Value v1, Value v2, Predicate<Value> pred) {
        assertTrue("last operation: " + last + (lastparam != null ? ", param: " + lastparam : "") + ", should be smaller: " + v1 + ", should be larger: " + v2,
                !pred.test(v1) || pred.test(v2));
    }

    void checkAntiMonotone(Value v1, Value v2, Predicate<Value> pred) {
        checkMonotone(v1, v2, pred.negate());
    }

    void checkMonotone(Value v1, Value v2) {
        lastparam = null;
        checkMonotone(v1, v2, Value::isMaybeStrUInt);
        checkMonotone(v1, v2, Value::isMaybeStrOtherNum);
        checkMonotone(v1, v2, Value::isMaybeStrOther);
        checkMonotone(v1, v2, Value::isMaybeStrIdentifier);
        checkMonotone(v1, v2, Value::isMaybeStrOtherIdentifierParts);
        checkMonotone(v1, v2, Value::isMaybeAnyStr);
        checkMonotone(v1, v2, Value::isMaybeFuzzyStr);
        checkMonotone(v1, v2, Value::isMaybeFuzzyStrOrSymbol);
        checkAntiMonotone(v1, v2, Value::isNotStr);
        for (String s : strings) {
            lastparam = s;
            checkMonotone(v1, v2, w -> w.isMaybeStr(s));
        }
// FIXME
//        for (Value v3 : values) {
//            lastparam = v3;
//            checkMonotone(v1, v2, w -> w.isStrMayContainSubstring(v3));
//        }
    }

    @Test
    public void test() {
        for (int i = 0; i < TESTS; i++) {
            Value v = nextRandomValue();
            int c = rnd.nextInt(5) + 1;
            for (int j = 0; j < c; j++) {
                Value v1 = v;
                if (rnd.nextBoolean()) {
                    switch (rnd.nextInt(2)) {
                        case 0: {
                            Value w = nextRandomValue();
                            boolean widen = rnd.nextBoolean();
                            v = v.join(w, widen);
                            last = "join(" + w + "," + widen + ")";
                            break;
                        }
                        case 1: {
                            Value w = nextRandomValue();
                            boolean widen = rnd.nextBoolean();
                            v = nextRandomValue().join(v, widen);
                            last = "join[reverse](" + w + "," + widen + ")";
                            break;
                        }
                    }
                } else {
                    switch (rnd.nextInt(9)) {
                        case 0:
                            v = v.joinAnyStrUInt();
                            last = "joinAnyStrUInt";
                            break;
                        case 1:
                            v = v.joinAnyStr();
                            last = "joinAnyStr";
                            break;
                        case 2: {
                            String s = strings[rnd.nextInt(STRINGS)];
                            v = v.joinStr(s);
                            last = "joinStr(" + s + ")";
                            break;
                        }
                        case 3: {
                            String s = strings[rnd.nextInt(STRINGS)];
                            s = s.isEmpty() ? " " : s;
                            v = v.joinPrefix(s);
                            last = "joinPrefix(" + s + ")";
                            break;
                        }
                        case 4:
                            v = v.joinAnyStrOther();
                            last = "joinAnyStrOther";
                            break;
                        case 5:
                            v = v.joinAnyStrOtherNum();
                            last = "joinAnyStrOtherNum";
                            break;
                        case 6:
                            v = v.joinAnyStrIdentifier();
                            last = "joinAnyStrIdentifier";
                            break;
                        case 7:
                            v = v.joinAnyStrIdentifierParts();
                            last = "joinAnyStrIdentifierParts";
                            break;
                        case 8:
                            v = v.forgetExcludedIncludedStrings();
                            last = "forgetExcludedIncludedStrings";
                            break;
                    }
                }
                checkMonotone(v1, v);
            }

            Value v1 = v;
            switch (rnd.nextInt(7)) {
                case 0:
                    v = v.restrictToStr();
                    last = "restrictToStr";
                    break;
                case 1:
                    v = v.restrictToNotStr();
                    last = "restrictToNotStr";
                    break;
                case 2:
                    v = v.restrictToNotStrIdentifierParts();
                    last = "restrictToNotStrIdentifierParts";
                    break;
                case 3:
                    v = v.restrictToNotStrPrefix();
                    last = "restrictToNotStrPrefix";
                    break;
                case 4:
                    v = v.restrictToNotStrUInt();
                    last = "restrictToNotStrUInt";
                    break;
                case 5:
                    v = v.restrictToNotStrOtherNum();
                    last = "restrictToNotStrOtherNum";
                    break;
                case 6:
                    Set<String> s = stringsets.get(rnd.nextInt(STRINGSETS));
                    v = v.restrictToNotStrings(s);
                    last = "restrictToNotStrings(" + s + ")";
                    break;
            }
            checkMonotone(v, v1);

            // TODO:
            // isMaybeStrPrefix
            // isStrIdentifier
            // isStrIdentifierParts
            // isMaybeSingleStr
            // isMaybeStrOnlyUInt
            // isMaybeAllKnownStr
            // isMaybeSingleStrOrSymbol
            // mustContainNonIdentifierCharacters
            // mustOnlyBeIdentifierCharacters
        }
    }
}
