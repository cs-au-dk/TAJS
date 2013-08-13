package dk.brics.tajs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.NopNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;

@SuppressWarnings("static-method")
public class TestValue {

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestValue");
	}

	@Before
	public void init() {
		Options.reset();
		Options.enableTest();
		Options.enableDebug();
	}

	private static void printInfo(Value v) {
		System.out.println("****" + v);
		System.out.println("Attributes: " + v.printAttributes());

		System.out.println("isBottom = " + v.isNone());
		System.out.println("isNoValue = " + v.isNotPresent());
		System.out.println("isMaybeValue = " + v.isMaybePresent());
		System.out.println("isMaybePrimitive = " + v.isMaybePrimitive());

		System.out.println("isMaybeAbsent = " + v.isMaybeAbsent());
		System.out.println("isNotAbsent = " + v.isNotAbsent());

		System.out.println("isMaybeUndef = " + v.isMaybeUndef());
		System.out.println("isMaybeOtherThanUndef = " + v.isMaybeOtherThanUndef());
		System.out.println("isNotUndef = " + v.isNotUndef());

		System.out.println("isMaybeNull = " + v.isMaybeNull());
		System.out.println("isNotNull = " + v.isNotNull());
		System.out.println("isMaybeOtherThanNull = " + v.isMaybeOtherThanNull());
		System.out.println("isNullOrUndef = " + v.isNullOrUndef());

		System.out.println("isMaybeAnyBool = " + v.isMaybeAnyBool());
		System.out.println("isTrue = " + v.isMaybeTrueButNotFalse());
		System.out.println("isFalse = " + v.isMaybeFalseButNotTrue());
		System.out.println("isNotBool = " + v.isNotBool());
		System.out.println("isMaybeOtherThanBool = " + v.isMaybeOtherThanBool());
		System.out.println("isMaybeTrue = " + v.isMaybeTrue());
		System.out.println("isMaybeFalse = " + v.isMaybeFalse());

		System.out.println("isNotNum = " + v.isNotNum());
		System.out.println("isNaN = " + v.isNaN());
		System.out.println("isMaybeInf = " + v.isMaybeInf());
		System.out.println("isMaybeNumUInt = " + v.isMaybeNumUInt());
		System.out.println("isMaybeNumNotUInt = " + v.isMaybeNumOther());
		System.out.println("getNum = " + v.getNum());
		System.out.println("isMaybeFuzzyNum = " + v.isMaybeFuzzyNum());
		System.out.println("isMaybeOtherThanNum = " + v.isMaybeOtherThanNum());

		System.out.println("isMaybeOtherThanStr = " + v.isMaybeOtherThanStr());
		System.out.println("isNotStr = " + v.isNotStr());
		System.out.println("getStr = " + v.getStr());
		System.out.println("isMaybeStrUInt = " + v.isMaybeStrUInt());

		System.out.println("isObject = " + v.isMaybeObject());
		System.out.println("getObjectLabels = " + v.getObjectLabels());

		System.out.println("isMaybeModified = " + v.isMaybeModified());

		System.out.println("isDontEnum = " + v.isDontEnum());
		System.out.println("isMaybeDontEnum = " + v.isMaybeDontEnum());
		System.out.println("isMaybeNotDontEnum = " + v.isMaybeNotDontEnum());
		System.out.println("isNotDontEnum = " + v.isNotDontEnum());

		System.out.println("isDontDelete = " + v.isDontDelete());
		System.out.println("isMaybeDontDelete = " + v.isMaybeDontDelete());
		System.out.println("isMaybeNotDontDelete = " + v.isMaybeNotDontDelete());
		System.out.println("isNotDontDelete = " + v.isNotDontDelete());

		System.out.println("isReadOnly = " + v.isReadOnly());
		System.out.println("isMaybeReadOnly = " + v.isMaybeReadOnly());
		System.out.println("isMaybeNotReadOnly = " + v.isMaybeNotReadOnly());
		System.out.println("isNotReadOnly = " + v.isNotReadOnly());
	}

	@Test
	public void value() {
		Misc.init();
		Misc.captureSystemOutput();

		FlowGraph fg = new FlowGraph();
		SourceLocation loc = new SourceLocation(117, -1, "foo.js");
		List<String> args = Collections.emptyList();
		Function f = new Function("foo", args, null, loc);
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
		Value vObject1 = Value.makeObject(new ObjectLabel(n, Kind.OBJECT));
		Value vObject2 = Value.makeObject(new ObjectLabel(n, Kind.BOOLEAN));

		System.out.println(vBottom);
		System.out.println(vAbsent);
		System.out.println(vUndef);
		System.out.println(vNull);
		System.out.println(vAnyBool);
		System.out.println(vTrue);
		System.out.println(vFalse);
		System.out.println(vAnyNum);
		System.out.println(v65536);
		System.out.println(v314159);
		System.out.println(v127e35);
		System.out.println(vNaN);
		System.out.println(vNaN2);
		System.out.println(vPosInf);
		System.out.println(vInf);
		System.out.println(vNumUInt);
		System.out.println(vNumNotUInt);
		System.out.println(vAnyStr);
		System.out.println(vEmptyStr);
		System.out.println(vSomeStr);
		System.out.println(vObject1);
		System.out.println(vObject2);

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
		v4 = v4.setBottomAttributes();
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

		System.out.println(vTrue.equals(vFalse));
		System.out.println(vFalse.equals(vFalse));

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
		objs.add(new ObjectLabel(ECMAScriptObjects.ARRAY_SORT, Kind.FUNCTION));
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

		printInfo(v7.restrictToObject());
		printInfo(v7.restrictToNotObject());
		printInfo(v7.restrictToNotObject().restrictToNotObject());
		printInfo(v7.joinNumInf().restrictToObject());
		printInfo(v7.joinNumInf().restrictToNotObject());
		printInfo(v7.restrictToObject().restrictToObject());

		printInfo(Value.makeAbsentModified());
		printInfo(Value.makeAnyNumNotNaNInf());

		Misc.checkSystemOutput();

		// TODO: obtain full coverage of the Value class
	}

	private Value num;
	private Value bool;
	private Value str;
	private Value bot;

	@Before
	public void restrict_to_before() {
		num = Value.makeAnyNum();
		bool = Value.makeAnyBool();
		str = Value.makeAnyStr();
		bot = Value.makeNone();
	}

	private void restrictToTest(Value original, Value restrictor, Value expected) {
		final Value restricted = original.restrictTo(restrictor);
		if (!expected.equals(restricted))
			System.err.println(String.format("<%s>.restrictTo(<%s>) = <%s>, expected <%s>", original, restrictor, restricted, expected));
		assertEquals(expected, restricted);
	}

	@Test
	public void restrict_to_sanity() {
		Value f1 = Value.makeBool(false);
		Value f2 = Value.makeBool(false);
		assertTrue(f1.equals(f2));
	}

	@Test
	public void restrict_to_sanity2() {
		Value f1 = Value.makeBool(false);
		Value f2 = Value.makeBool(false);
		assertTrue(f1.equals(f2));
	}

	@Test
	public void restrict_to_self() {
		final Value v = bool;
		restrictToTest(v, v, v);
	}

	@Test
	public void restrict_to_bot() {
		final Value v1 = num;
		final Value v2 = str;
		restrictToTest(v1, v2, bot);
	}

	@Test
	public void restrict_to_multi() {
		final Value v1 = num;
		final Value v2 = str.join(num);
		restrictToTest(v1, v2, num);
	}

	@Test
	public void restrict_to_concreteVSAbstract1() {
		Value v1 = str;
		Value v2 = Value.makeStr("");
		restrictToTest(v1, v2, v2);
	}

	@Test
	public void restrict_to_concreteVSAbstract2() {
		Value v1 = Value.makeAnyNum();
		Value v2 = Value.makeNum(1);
		restrictToTest(v1, v2, v2);
	}

	@Test
	public void restrict_to_concreteVSAbstract3() {
		Value v1 = str;
		Value v2 = Value.makeStr("x");
		restrictToTest(v1, v2, v2);
	}

	@Test
	public void restrict_to_concreteVSAbstract4() {
		Value v1 = str;
		Value v2 = Value.makeStr("-1");
		restrictToTest(v1, v2, v2);
	}

	@Test
	public void restrict_to_concreteVSAbstract5() {
		Value v1 = str;
		Value v2 = Value.makeStr("NaN");
		restrictToTest(v1, v2, v2);
	}

	@Test
	public void restrict_to_stringFlags_agree() {
		Value v1 = Value.makeNone().joinAnyStrUInt();
		Value v2 = Value.makeStr("1");
		restrictToTest(v1, v2, v2);
	}

	@Test
	public void restrict_to_stringFlags2_disagree() {
		Value v1 = Value.makeNone().joinAnyStrUInt();
		Value v2 = Value.makeStr("-1");
		restrictToTest(v1, v2, bot);
	}

	@Test
	public void restrict_to_numberFlags_agree() {
		Value v1 = Value.makeAnyNumUInt();
		Value v2 = Value.makeNum(1);
		restrictToTest(v1, v2, v2);
	}

	@Test
	public void restrict_to_numberFlags_disagree() {
		Value v1 = Value.makeAnyNumUInt();
		Value v2 = Value.makeNum(-1);
		restrictToTest(v1, v2, bot);
	}

	@Test
	public void restrict_to_str_subsumption() {
		Value v1 = bot.joinAnyStrIdentifierParts();
		Value v2 = bot.joinAnyStrUInt();
		restrictToTest(v1, v2, v2);
	}

	@Test
	public void restrict_to_str_subsumption_reverse() {
		Value v1 = bot.joinAnyStrUInt();
		Value v2 = bot.joinAnyStrIdentifierParts();
		restrictToTest(v1, v2, v1);
	}

	@Test
	public void restrict_to_str_subsumption_2() {
		Value v1 = bot.joinAnyStrIdentifierParts();
		Value v2 = bot.joinAnyStrIdentifier();
		restrictToTest(v1, v2, v2);
	}

	@Test
	public void restrict_to_str_subsumption_reverse_2() {
		Value v1 = bot.joinAnyStrIdentifier();
		Value v2 = bot.joinAnyStrIdentifierParts();
		restrictToTest(v1, v2, v1);
	}

	@Test
	public void restrict_to_str_overlap() {
		Value v1 = bot.joinAnyStrIdentifier();
		Value v2 = bot.joinAnyStrOtherNum();
		restrictToTest(v1, v2, v1);
	}

	@Test
	public void restrict_to_str_overlap_reverse() {
		Value v1 = bot.joinAnyStrOtherNum();
		Value v2 = bot.joinAnyStrIdentifier();
		restrictToTest(v1, v2, v1);
	}

	@Test
	public void restrict_to_str_other() {
		Value v1 = bot.joinAnyStrIdentifier();
		Value v2 = bot.joinAnyStrOther();
		restrictToTest(v1, v2, bot);
	}

	@Test
	public void restrict_to_str_disjoint() {
		Value v1 = bot.joinAnyStrUInt();
		Value v2 = bot.joinAnyStrOtherNum();
		restrictToTest(v1, v2, bot);
	}

	@Test
	public void restrict_to_str_advanced() {
		Value v1 = bot.joinAnyStrUInt().joinAnyStrOtherNum();
		Value v2 = bot.joinAnyStrIdentifier();
		restrictToTest(v1, v2, bot.joinAnyStrOtherNum());
	}

	@Test
	public void restrict_to_str_advanced_reverse() {
		Value v1 = bot.joinAnyStrIdentifier();
		Value v2 = bot.joinAnyStrUInt().joinAnyStrOtherNum();
		restrictToTest(v1, v2, v1);
	}

}