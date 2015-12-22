package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class JSString_indexof_lastindexof_Test {

	@Before
	public void before() {
		Main.reset();
		Options.get().enableTest();
	}

	@Test
	public void emptyNotFound() {
		Misc.init();
		testIndexOf("", "x", 0, -1);
	}

	@Test
	public void emptyFound() {
		Misc.init();
		testIndexOf("", "", 0, 0);
	}

	@Test
	public void nonEmptyNotFound() {
		Misc.init();
		testIndexOf("x", "y", 0, -1);
	}

	@Test
	public void nonEmptyFound1() {
		Misc.init();
		testIndexOf("x", "x", 0, 0);
	}

	@Test
	public void nonEmptyFound2() {
		Misc.init();
		testIndexOf("xy", "y", 0, 1);
	}

	@Test
	public void nonEmptyFound3() {
		Misc.init();
		testIndexOf("xyz", "yz", 0, 1);
	}

	@Test
	public void nonEmptyFound1_last() {
		Misc.init();
		testLastIndexOf("x", "x", 1, 0);
	}

	@Test
	public void nonEmptyFound2_last() {
		Misc.init();
		testLastIndexOf("xy", "y", 2, 1);
	}

	@Test
	public void nonEmptyFound3_last() {
		Misc.init();
		Misc.init();
		testLastIndexOf("xyz", "yz", 3, 1);
	}

	private void testLastIndexOf(String base, String target, int offset, int expected) {
		Misc.init();
		test(base, target, offset, expected, false);
	}

	@Test
	public void microStringTestPort() {
		Misc.init();
		Misc.runSource(
				"var h = new String('testing indexOf');",
				"TAJS_assert(h.indexOf('i') === 4, 'isMaybeTrueButNotFalse', true);      ",
				"TAJS_assert(h.indexOf('i', 7) === 8, 'isMaybeTrueButNotFalse', true);   ",
				"TAJS_assert(h.indexOf('i', 40) === -1, 'isMaybeTrueButNotFalse', true);  ",
				"var g = new Number(3242);       ",
				"g.indexOf = h.indexOf;          ",
				"TAJS_assert(g.indexOf('.') === -1, 'isMaybeTrueButNotFalse', true);      ",
				"                                ",
				"TAJS_assert(h.lastIndexOf('i') === 8, 'isMaybeTrueButNotFalse', true);  ",
				"TAJS_assert(h.lastIndexOf('i', 7) === 4, 'isMaybeTrueButNotFalse', true)",
				"TAJS_assert(h.lastIndexOf('i', 1) === -1, 'isMaybeTrueButNotFalse', true)",
				"g.lastIndexOf = h.lastIndexOf;  ",
				"TAJS_assert(g.lastIndexOf('.') === -1, 'isMaybeTrueButNotFalse', true);  "
				);

	}

	private void test(String base, String target, int offset, int expected, boolean indexOf) {
		String functionName = indexOf ? "indexOf" : "lastIndexOf";
		final String resultSrc = "var result = '" + base + "'." + functionName + "('" + target + "', " + offset + ");";
		final String comparisonSrc = "var comparison = result === " + expected + ";";
		final String expectedSrc = "TAJS_assert(comparison)";
		Misc.runSource(resultSrc, comparisonSrc, expectedSrc);

	}

	private void testIndexOf(String base, String target, int offset, int expected) {
		test(base, target, offset, expected, true);
	}
}
