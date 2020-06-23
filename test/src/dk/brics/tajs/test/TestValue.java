package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.NopNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.PartitionedValue;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static dk.brics.tajs.util.Collections.singleton;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("static-method")
public class TestValue {

    private static Logger log = Logger.getLogger(TestValue.class);

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestValue");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableDebug();
    }

    private static void printInfo(Value v) {
        log.info("****" + v);
        log.info("Attributes: " + v.printAttributes());
        log.info("isBottom = " + v.isNone());
        log.info("isNoValue = " + v.isNotPresent());
        log.info("isMaybeValue = " + v.isMaybePresent());
        log.info("isMaybePrimitive = " + v.isMaybePrimitive());
        log.info("isMaybeAbsent = " + v.isMaybeAbsent());
        log.info("isNotAbsent = " + v.isNotAbsent());
        log.info("isMaybeUndef = " + v.isMaybeUndef());
        log.info("isMaybeOtherThanUndef = " + v.isMaybeOtherThanUndef());
        log.info("isNotUndef = " + v.isNotUndef());
        log.info("isMaybeNull = " + v.isMaybeNull());
        log.info("isNotNull = " + v.isNotNull());
        log.info("isMaybeOtherThanNull = " + v.isMaybeOtherThanNull());
        log.info("isNullOrUndef = " + v.isNullOrUndef());
        log.info("isMaybeAnyBool = " + v.isMaybeAnyBool());
        log.info("isTrue = " + v.isMaybeTrueButNotFalse());
        log.info("isFalse = " + v.isMaybeFalseButNotTrue());
        log.info("isNotBool = " + v.isNotBool());
        log.info("isMaybeOtherThanBool = " + v.isMaybeOtherThanBool());
        log.info("isMaybeTrue = " + v.isMaybeTrue());
        log.info("isMaybeFalse = " + v.isMaybeFalse());
        log.info("isNotNum = " + v.isNotNum());
        log.info("isNaN = " + v.isNaN());
        log.info("isMaybeInf = " + v.isMaybeInf());
        log.info("isMaybeNumUInt = " + v.isMaybeNumUInt());
        log.info("isMaybeNumNotUInt = " + v.isMaybeNumOther());
        log.info("getNum = " + v.getNum());
        log.info("isMaybeFuzzyNum = " + v.isMaybeFuzzyNum());
        log.info("isMaybeOtherThanNum = " + v.isMaybeOtherThanNum());
        log.info("isMaybeOtherThanStr = " + v.isMaybeOtherThanStr());
        log.info("isNotStr = " + v.isNotStr());
        log.info("getStr = " + (v.isMaybeSingleStr() ? v.getStr() : "null"));
        log.info("isMaybeStrUInt = " + v.isMaybeStrUInt());
        log.info("isObject = " + v.isMaybeObject());
        log.info("getObjectLabels = " + v.getObjectLabels());
        log.info("isMaybeSingleObjectLabel = " + v.isMaybeSingleObjectLabel());
        log.info("isMaybeSingleObjectLabel = " + v.isMaybeSingleAllocationSite());
        log.info("isNotASummarizedObject = " + v.isNotASummarizedObject());
        log.info("isNotASingletonObject = " + v.isNotASingletonObject());
        log.info("isMaybeModified = " + v.isMaybeModified());
        log.info("isDontEnum = " + v.isDontEnum());
        log.info("isMaybeDontEnum = " + v.isMaybeDontEnum());
        log.info("isMaybeNotDontEnum = " + v.isMaybeNotDontEnum());
        log.info("isNotDontEnum = " + v.isNotDontEnum());
        log.info("isDontDelete = " + v.isDontDelete());
        log.info("isMaybeDontDelete = " + v.isMaybeDontDelete());
        log.info("isMaybeNotDontDelete = " + v.isMaybeNotDontDelete());
        log.info("isNotDontDelete = " + v.isNotDontDelete());
        log.info("isReadOnly = " + v.isReadOnly());
        log.info("isMaybeReadOnly = " + v.isMaybeReadOnly());
        log.info("isMaybeNotReadOnly = " + v.isMaybeNotReadOnly());
        log.info("isNotReadOnly = " + v.isNotReadOnly());
    }

    @Test
    public void value() {
        Misc.start(null);
        SourceLocation.SyntheticLocationMaker sourceLocationMaker = new SourceLocation.SyntheticLocationMaker("synthetic");
        SourceLocation loc = sourceLocationMaker.make(117, -1, 117, -1);
        List<String> args = Collections.emptyList();
        Function f = new Function("foo", args, null, loc);
        FlowGraph fg = new FlowGraph(f);
        BasicBlock b = new BasicBlock(f);
        AbstractNode n = new NopNode(loc);
        b.addNode(n);
        fg.addBlock(b);
        f.complete();
        fg.addFunction(f);
        f.setMaxRegister(1000); // Ok for testing purposes
        Value vBottom = Value.makeNone();
        Value vAbsent = Value.makeAbsent();
        Value vUndef = Value.makeUndef();
        Value vNull = Value.makeNull();
        Value vAnyBool = Value.makeAnyBool();
        Value vTrue = Value.makeBool(true);
        Value vFalse = Value.makeBool(false);
        Value vAnyNum = Value.makeAnyNum();
        Value v65536 = Value.makeNum(65536);
        Value v314159 = Value.makeNum(3.14159);
        Value v127e35 = Value.makeNum(-.127e35);
        Value vNaN = Value.makeNumNaN();
        Value vNaN2 = Value.makeNum(Double.NaN);
        Value vPosInf = Value.makeNum(Double.POSITIVE_INFINITY);
        Value vInf = Value.makeNumInf();
        Value vNumUInt = Value.makeAnyNumUInt();
        Value vNumNotUInt = Value.makeAnyNumOther();
        Value vAnyStr = Value.makeAnyStr();
        Value vEmptyStr = Value.makeStr("");
        Value vSomeStr = Value.makeStr("bar");
        Value vObject1 = Value.makeObject(ObjectLabel.make(n, Kind.OBJECT));
        Value vObject2 = Value.makeObject(ObjectLabel.make(n, Kind.BOOLEAN));
        log.info(vBottom);
        log.info(vAbsent);
        log.info(vUndef);
        log.info(vNull);
        log.info(vAnyBool);
        log.info(vTrue);
        log.info(vFalse);
        log.info(vAnyNum);
        log.info(v65536);
        log.info(v314159);
        log.info(v127e35);
        log.info(vNaN);
        log.info(vNaN2);
        log.info(vPosInf);
        log.info(vInf);
        log.info(vNumUInt);
        log.info(vNumNotUInt);
        log.info(vAnyStr);
        log.info(vEmptyStr);
        log.info(vSomeStr);
        log.info(vObject1);
        log.info(vObject2);
        printInfo(vBottom);
        printInfo(vAbsent);
        printInfo(vUndef);
        printInfo(vNull);
        printInfo(vAnyBool);
        printInfo(vTrue);
        printInfo(vFalse);
        printInfo(vAnyNum);
        printInfo(v65536);
        printInfo(v314159);
        printInfo(v127e35);
        printInfo(vNaN);
        printInfo(vNaN2);
        printInfo(vPosInf);
        printInfo(vAnyStr);
        printInfo(vEmptyStr);
        printInfo(vSomeStr);
        printInfo(vObject1);
        printInfo(vObject2);
        Value v0 = v65536.joinModified();
        printInfo(v0);
        v0 = v0.restrictToNotModified();
        printInfo(v0);
        v0 = v0.restrictToNotModified();
        printInfo(v0);
        v0 = v0.joinModified();
        printInfo(v0);
        v0 = v0.joinModified();
        printInfo(v0);
        Value v1 = vUndef.joinAbsent();
        printInfo(v1);
        v1 = v1.joinAbsent();
        printInfo(v1);
        v1 = vUndef.joinAbsentModified();
        printInfo(v1);
        v1 = v1.restrictToNotAbsent();
        printInfo(v1);
        v1 = v1.restrictToNotAbsent();
        printInfo(v1);
        Value v2 = vUndef.joinAbsentModified();
        printInfo(v2);
        v2 = v2.joinAbsentModified();
        printInfo(v2);
        Value v3 = vUndef.setDontDelete().setDontEnum().setReadOnly();
        printInfo(v3);
        v3 = v3.setDontDelete().setDontEnum().setReadOnly();
        printInfo(v3);
        Value v4 = vNull.setAttributes(v3);
        printInfo(v4);
        v4 = v4.removeAttributes();
        printInfo(v4);
        v4 = v4.restrictToNonAttributes();
        printInfo(v4);
        v4 = v4.setNotDontDelete().setNotDontEnum().setNotReadOnly();
        printInfo(v4);
        v4 = v4.setNotDontDelete().setNotDontEnum().setNotReadOnly();
        printInfo(v4);
        v4 = v3.joinNotDontDelete().joinNotDontEnum().joinNotReadOnly();
        printInfo(v4);
        v4 = v4.joinNotDontDelete().joinNotDontEnum().joinNotReadOnly();
        printInfo(Value.join(vTrue, vFalse, vUndef, v314159, vSomeStr));
        printInfo(Value.join());
        printInfo(Value.join(vTrue, vTrue));
        printInfo(Value.join(Value.makeNum(1), Value.makeNum(2)));
        printInfo(Value.join(Value.makeNum(1), Value.makeNum(2.5)));
        printInfo(Value.join(Value.makeNum(1.5), Value.makeNum(2.5)));
        log.info(vTrue.equals(vFalse));
        log.info(vFalse.equals(vFalse));
        printInfo(vTrue.join(vFalse));
        printInfo(v65536.join(v314159));
        printInfo(vEmptyStr.join(vSomeStr));
        printInfo(vUndef.join(vNull));
        printInfo(vNull.join(vFalse).join(v127e35).join(vSomeStr).join(vObject1));
        printInfo(vObject1.join(vObject2));
        printInfo(vUndef.joinUndef());
        printInfo(vNull.joinUndef());
        printInfo(vNull.joinUndef().restrictToNotUndef());
        printInfo(vNull.joinUndef().restrictToUndef());
        printInfo(vNull.joinUndef().restrictToNotNullNotUndef());
        printInfo(vUndef.joinNull());
        printInfo(vNull.joinNull());
        printInfo(vTrue.joinNull().restrictToNotNull());
        printInfo(vTrue.joinNull().restrictToNull());
        printInfo(vTrue.restrictToNull());
        printInfo(vAnyNum.joinAnyBool());
        printInfo(vAnyNum.joinBool(false));
        printInfo(vAnyNum.joinBool(true).restrictToBool());
        printInfo(vAnyStr.joinAnyNum());
        printInfo(vAnyStr.joinAnyNumUInt());
        printInfo(vAnyStr.joinAnyNumOther());
        printInfo(vAnyNum.restrictToNotNaN());
        printInfo(Value.makeNum(0).joinNum(1));
        printInfo(Value.makeAnyNum().joinNum(Double.NaN));
        printInfo(Value.makeAnyNum().joinNumNaN());
        printInfo(Value.makeNone().joinNum(Double.POSITIVE_INFINITY));
        printInfo(Value.makeNone().joinNumInf());
        printInfo(Value.makeAnyBool().joinNum(42).restrictToNum());
        printInfo(Value.makeStr("foo").joinAnyStr());
        printInfo(Value.makeStr("foo").joinAnyStrUInt());
        printInfo(Value.makeStr("4").joinAnyStrUInt());
        printInfo(Value.makeStr("4").joinStr("5"));
        printInfo(Value.makeAnyBool().join(vSomeStr).restrictToStr());
        printInfo(Value.makeAnyBool().join(vSomeStr).restrictToStrBoolNum());
        Set<ObjectLabel> objs = new HashSet<>();
        objs.add(ObjectLabel.make(ECMAScriptObjects.ARRAY_SORT, Kind.FUNCTION));
        Value v7 = Value.makeObject(objs);
        printInfo(v7);
        v7 = v7.joinAnyStr();
        printInfo(v7);
        List<Value> vs = new ArrayList<>();
        vs.add(vTrue);
        vs.add(vFalse);
        vs.add(Value.makeNum(1.2));
        vs.add(Value.makeNum(3.4));
        printInfo(Value.join(vs));
        printInfo(Value.join(Value.makeNull(), Value.makeBool(true), Value.makeNumNaN()));
        printInfo(Value.join(Value.makeNull(), Value.makeBool(false), Value.makeNumInf()));
        printInfo(Value.makeAnyBool().joinAnyBool());
        printInfo(Value.makeAnyBool().joinBool(true));
        printInfo(Value.makeAnyBool().joinAnyNum().restrictToBool());
        printInfo(Value.makeBool(true).joinAnyNum().restrictToBool());
        printInfo(Value.makeBool(false).joinAnyNum().joinAnyNum().joinAnyNumUInt().restrictToBool());
        printInfo(Value.makeNum(87).joinAnyNumUInt());
        printInfo(Value.makeNum(87).joinNum(42).joinAnyNumUInt());
        printInfo(Value.makeNum(87).joinAnyNumOther());
        printInfo(Value.makeAnyNum().restrictToNotNaN());
        printInfo(Value.makeAnyNum().restrictToNotNaN().joinNumNaN());
        printInfo(Value.makeStr("foo").joinNumInf());
        printInfo(Value.join(Value.makeNum(87), Value.makeAnyNumUInt()));
        printInfo(Value.join(Value.makeNum(87), Value.makeAnyNumOther()));
        printInfo(Value.join(Value.makeAnyNumOther(), Value.makeNum(87)));
        printInfo(Value.join(Value.makeStr("x"), Value.makeAnyStr()));
        printInfo(Value.join(Value.makeAnyStr(), Value.makeStr("x")));
        printInfo(Value.join(Value.makeAnyBool(), Value.makeStr("1"), Value.makeStr("2")));
        printInfo(Value.join(Value.makeAnyBool(), Value.makeStr("x"), Value.makeStr("y")));
        printInfo(vNull.restrictToNotUndef());
        printInfo(vNull.restrictToUndef());
        printInfo(vUndef.restrictToUndef());
        printInfo(vUndef.restrictToNotNull());
        printInfo(vSomeStr.restrictToNotNullNotUndef());
        printInfo(vTrue.joinBool(true));
        printInfo(vNull.restrictToBool());
        printInfo(vAnyNum.joinAnyNumOther());
        printInfo(vAnyBool.restrictToNotNaN());
        printInfo(Value.makeNum(1).joinNum(1));
        printInfo(Value.makeNum(1).joinNum(Double.NaN));
        printInfo(Value.makeNum(1).joinNum(Double.POSITIVE_INFINITY));
        printInfo(Value.makeNum(1).joinNumNaN());
        printInfo(Value.makeNum(1).joinNumInf());
        printInfo(Value.makeNumInf().joinNumInf());
        printInfo(Value.makeAnyStr().joinAnyStr());
        printInfo(Value.makeAnyStr().joinAnyStrUInt());
        printInfo(Value.makeAnyBool().joinAnyNumUInt());
        printInfo(Value.makeAnyBool().joinAnyStrUInt());
        printInfo(Value.makeStr("x").joinStr("x"));
        printInfo(vNull.joinStr("x"));
        printInfo(Value.makeAnyStr().restrictToStr());
        printInfo(vNull.restrictToStr());
        printInfo(v7.restrictToNonSymbolObject());
        printInfo(v7.restrictToNotObject());
        printInfo(v7.restrictToNotObject().restrictToNotObject());
        printInfo(v7.joinNumInf().restrictToNonSymbolObject());
        printInfo(v7.joinNumInf().restrictToNotObject());
        printInfo(v7.restrictToNonSymbolObject().restrictToNonSymbolObject());
        printInfo(Value.makeAbsentModified());
        printInfo(Value.makeAnyNumNotNaNInf());
        Misc.checkSystemOutput();
        // TODO: obtain full coverage of the Value class
    }

    @Test
    public void joinPrefix() {
        Value string = Value.makeStr("A");
        Value prefix = Value.makeNone().joinPrefix("A");
        Value other = Value.makeStr("B");
        Value number = Value.makeAnyNumUInt();
        Value any = Value.makeAnyStr();
        Value string_prefix = string.join(prefix);
        assertEquals(prefix, string_prefix);
        Value prefix_string = prefix.join(string);
        assertEquals(prefix, prefix_string);
        Value other_prefix = other.join(prefix);
        assertEquals(any, other_prefix);
        Value prefix_other = prefix.join(other);
        assertEquals(any, prefix_other);
        Value prefix_other__number = prefix_other.join(number);
        assertEquals(any.joinAnyNumUInt(), prefix_other__number);
        Value number__prefix_other = number.join(prefix_other);
        assertEquals(any.joinAnyNumUInt(), number__prefix_other);
        Value number_prefix = number.join(prefix);
        assertEquals(Value.makeAnyNumUInt().joinPrefix("A"), number_prefix);
        Value prefix_number = prefix.join(number);
        assertEquals(Value.makeAnyNumUInt().joinPrefix("A"), prefix_number);
        Value number_prefix__other = number_prefix.join(other);
        assertEquals(any.joinAnyNumUInt(), number_prefix__other);
        Value other__number_prefix = other.join(number_prefix);
        assertEquals(any.joinAnyNumUInt(), other__number_prefix);
        assertEquals(any, Value.makeNone().joinPrefix("#").joinStr(""));
    }

    @Test
    public void restrictToNotPrefix() {
        Value prefixA = Value.makeStr("Axxx").joinStr("Ayyy");
        Value notPrefix = prefixA.restrictToNotStrPrefix();
        assertTrue(prefixA.isMaybeStrPrefix());
        assertEquals("A", prefixA.getPrefix());
        assertTrue(notPrefix.isNone());
    }

    @Test
    public void restrictToStrictEqualsBug() {
        Value prefix = Value.makeNone().joinPrefix("foo");
        Value anyStr = Value.makeAnyStr();

        Value res = prefix.restrictToStrictEquals(anyStr);
        assertEquals(String.format("Unexpected restrictToStrictEquals-result for %s.restrictToStrictEquals(%s), expected: %s, got %s", prefix, anyStr, prefix, res), prefix, res);

        Value resCommuted = anyStr.restrictToStrictEquals(prefix);
        assertEquals(String.format("Unexpected restrictToStrictEquals-result for %s.restrictToStrictEquals(%s), expected: %s, got %s", anyStr, prefix, prefix, resCommuted), prefix, resCommuted);
    }

    @Test
    public void restrictToStrictEqualsBug2() {
        Value prefix = Value.makeNone().joinPrefix(" ");
        Value anyStr = Value.makeAnyStr();

        Value res = prefix.restrictToStrictEquals(anyStr);
        assertEquals(String.format("Unexpected restrictToStrictEquals-result for %s.restrictToStrictEquals(%s), expected: %s, got %s", prefix, anyStr, prefix, res), prefix, res);

        Value resCommuted = anyStr.restrictToStrictEquals(prefix);
        assertEquals(String.format("Unexpected restrictToStrictEquals-result for %s.restrictToStrictEquals(%s), expected: %s, got %s", anyStr, prefix, prefix, resCommuted), prefix, resCommuted);
    }

    @Test
    public void prefixStrRestrictedToStringSetIssue() {
        Value prefix = Value.makeNone().joinPrefix("bar");
        Value stringSet = Value.makeStrings(Stream.of("a", "b").collect(Collectors.toSet()));

        Value res = prefix.restrictToStrictEquals(stringSet);
        assertTrue(res.isNone());
    }

    @Test
    public void restrictToStrictEquals() {
        List<Pair<Value, Value>> nonBottomRestrictions = Arrays.asList(
                Pair.make(Value.makeAnyStr(), Value.makeAnyStr()),
                Pair.make(Value.makeAnyStr().join(Value.makeAnyBool()), Value.makeAnyStr()),
                Pair.make(Value.makeAnyBool(), Value.makeAnyBool()),
                Pair.make(Value.makeAnyBool(), Value.makeBool(true)),
                Pair.make(Value.makeAnyStr(), Value.makeStr("")),
                Pair.make(Value.makeAnyStrUInt(), Value.makeStr("5")),
                Pair.make(Value.makeAnyStrNotUInt(), Value.makeStr("5.2")),
                Pair.make(Value.makeAnyNumUInt(), Value.makeNum(5)),
                Pair.make(Value.makeAnyNumOther(), Value.makeNum(5.2))
        );

        testRestrictToStrictEquals(nonBottomRestrictions, false);
        List<Pair<Value, Value>> bottomRestrictions = Arrays.asList(
                Pair.make(Value.makeAnyStr(), Value.makeAnyNum()),
                Pair.make(Value.makeAnyStr().join(Value.makeAnyBool()), Value.makeAnyNum()),
                Pair.make(Value.makeAnyNum(), Value.makeAnyBool()),
                Pair.make(Value.makeBool(false), Value.makeBool(true)),
                Pair.make(Value.makeAnyNum(), Value.makeStr("")),
                Pair.make(Value.makeAnyStrUInt(), Value.makeStr("5.2")),
                Pair.make(Value.makeAnyStrNotNumeric(), Value.makeStr("5")),
                Pair.make(Value.makeAnyNumUInt(), Value.makeNum(5.2)),
                Pair.make(Value.makeAnyNumOther(), Value.makeNum(5)),
                Pair.make(Value.makeStr("foobar").join(Value.makeStr("foobaz")), Value.makeStr("bar")),
                Pair.make(Value.makeStr("foobar").join(Value.makeStr("foobaz")), Value.makeStr("foo"))
        );
        testRestrictToStrictEquals(bottomRestrictions, true);
    }

    private void testRestrictToStrictEquals(List<Pair<Value, Value>> pairs, boolean bottomMeetExpected) {
        for (Pair<Value, Value> pair : pairs) {
            Value v1 = pair.getFirst();
            Value v2 = pair.getSecond();
            Value restrictToStrictEquals = v1.restrictToStrictEquals(v2);
            boolean bottomRestriction = restrictToStrictEquals.isNone();
            Value restrictToStrictEquals_commuted = v2.restrictToStrictEquals(v1);
            assertEquals(String.format("Non-commutative restrictToStrictEquals-use: %s.restrictToStrictEquals(%s) = %s vs. %s.restrictToStrictEquals(%s) = %s ", v1, v2, restrictToStrictEquals, v2, v1, restrictToStrictEquals_commuted), restrictToStrictEquals, restrictToStrictEquals_commuted);
            assertEquals(String.format("Unexpected restrictToStrictEquals-result for %s.restrictToStrictEquals(%s), expected bottomness: %s, got %s", v1, v2, bottomMeetExpected, bottomRestriction), bottomMeetExpected, bottomRestriction);
        }
    }

    @Test
    public void testRestrictIdentStrToIdentStrWithExcludedStrings() {
        Value identStr = Value.makeAnyStrIdent();
        Value excludedStrings = Value.makeAnyStrIdent().restrictToNotStrings(Stream.of("a", "b").collect(Collectors.toSet()));
        assertEquals(excludedStrings, identStr.restrictToStrictEquals(excludedStrings));
    }

    @Test
    public void testRestrictToLooseEqualsSoundnessBug() {
        Value str = Value.makeStr("1");
        Value num = Value.makeNum(1);
        assertEquals(str, str.restrictToLooseEquals(num));
        assertEquals(num, num.restrictToLooseEquals(str));
    }

   @Test
   public void testJoinSingleStrToStringSetSoundnessBug() {
        Value stringSet = Value.makeStrings(Stream.of("a", "b", "c").collect(Collectors.toSet()));
        Value stringSetWithAdditionalString = stringSet.joinStr("d");
        assertEquals(4, stringSetWithAdditionalString.getAllKnownStr().size());
        assertTrue(stringSetWithAdditionalString.isMaybeStr("d"));
   }

   @Test
   public void testRestrictToNotStringsInvalidExcludedStringsBug() {
        Value anyOtherNumString = Value.makeAnyStrOtherNum();
        Value anyOtherNumStringRestricted = anyOtherNumString.restrictToNotStrings(Stream.of("a", "NaN").collect(Collectors.toSet()));
        assertTrue(anyOtherNumStringRestricted.restrictToNotStrOtherNum().isNone());
        assertEquals(anyOtherNumStringRestricted.restrictToNotStrings(singleton("NaN")), anyOtherNumStringRestricted);

    }

    @Test
    public void testRestrictToLooseNotEqualsBug() {
        Value v1 = Value.makeStr("");
        Value v2 = Value.makeNum(0.0);
        assertEquals(Value.makeNone(), v1.restrictToLooseNotEquals(v2));
    }

    @Test
    public void testImpreciseJoinOfSingleStringAndPrefixString() {
        Value v1 = Value.makeStr("foo");
        Value v2 = Value.join(Value.makeStr("bar"), Value.makeStr("baz"));
        assertEquals(Value.makeStrings(Stream.of("foo", "bar", "baz").collect(Collectors.toList())), v1.join(v2));
        assertEquals(Value.makeStrings(Stream.of("foo", "bar", "baz").collect(Collectors.toList())), v2.join(v1));
    }

    /*
     * Public non-static methods in Value that return a Value (except 'join'), should be overwritten in PartitionedValue
     */
    @Test
    public void testMethodsOverwrittenInPartitionedValue() {
        Stream<Method> methodsThatShouldBeOverwritten = Stream.of(Value.class.getDeclaredMethods())
                .filter(m -> m.getReturnType() == Value.class)
                .filter(m -> Modifier.isPublic(m.getModifiers()) && !Modifier.isStatic(m.getModifiers()))
                .filter(m -> !m.getName().equals("join"));
        List<Method> methodsMissingInPartitionedValue = methodsThatShouldBeOverwritten.filter(m -> !hasPartitionedValueMethod(m)).collect(Collectors.toList());
        if (!methodsMissingInPartitionedValue.isEmpty()) {
            Assert.fail(String.format("The following methods need to be overwritten in the class PartitionedValue: %s",
                    methodsMissingInPartitionedValue.stream().map(m -> m.toString()).reduce("", (acc, elem) -> String.format("%s \n\t %s", acc, elem))));
        }
    }

    /**
     * Returns whether PartitionedValue has the method m.
     */
    private boolean hasPartitionedValueMethod(Method m) {
        try {
            PartitionedValue.class.getDeclaredMethod(m.getName(), m.getParameterTypes()); //getDeclaredMethod throws exception if the method is not declared in PartitionedValue
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
