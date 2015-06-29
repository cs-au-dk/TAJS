package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class JSString_splitTest {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void jsstring_split_knownStringsAndExactLimit() {
        Misc.init();
        Misc.runSource("var v = 'fooXbar'.split('X', 2);",
                "TAJS_assert(v[0] === 'foo');",
                "TAJS_assert(v[1] === 'bar');",
                "TAJS_assert(v.length === 2);");
    }

    @Test
    public void jsstring_split_knownStringsAndSmallLimit() {
        Misc.init();
        Misc.runSource("var v = 'fooXbar'.split('X', 1);",
                "TAJS_assert(v[0] === 'foo');",
                "TAJS_assert(v[1] === undefined);",
                "TAJS_assert(v.length === 1);");
    }

    @Test
    public void jsstring_split_knownStringsAndLargeLimit() {
        Misc.init();
        Misc.runSource("var v = 'fooXbar'.split('X', 3);",
                "TAJS_assert(v[0] === 'foo');",
                "TAJS_assert(v[1] === 'bar');",
                "TAJS_assert(v.length === 2);");
    }

    @Test
    public void jsstring_split_knownStringsAndUndefLimit() {
        Misc.init();
        Misc.runSource("var v = 'fooXbar'.split('X');",
                "TAJS_assert(v[0] === 'foo');",
                "TAJS_assert(v[1] === 'bar');",
                "TAJS_assert(v.length === 2);");
    }

    @Test
    public void jsstring_split_knownStringsAndUnknownLimit() {
        Misc.init();
        Misc.runSource("var v = 'fooXbar'.split('X', Math.random());",
                "TAJS_assert(v[0], 'isMaybeSingleStr', true)",
                "TAJS_assert(v[1], 'isMaybeSingleStr', true)",
                "TAJS_assert(v[0], 'isMaybeUndef', true)",
                "TAJS_assert(v[1], 'isMaybeUndef', true)",
                "TAJS_assert(v.length, 'isMaybeNumUInt', true)");
    }

    @Test
    public void jsstring_split_unknownStringsAndKnownLimit() {
        Misc.init();
        Misc.runSource("var v = (Math.random()? 'barXfoo' : 'fooXbar').split('X', 2);",
                "TAJS_assert(v[0], 'isMaybeAnyStr', true)",
                "TAJS_assert(v[1], 'isMaybeAnyStr', true)",
                "TAJS_assert(v.length, 'isMaybeNumUInt', true)");
    }

    @Test
    public void jsstring_split_unknownStringsAndUndefLimit() {
        Misc.init();
        Misc.runSource("var v = (Math.random()? 'barXfoo' : 'fooXbar').split('X');",
                "TAJS_assert(v[0], 'isMaybeAnyStr', true)",
                "TAJS_assert(v[1], 'isMaybeAnyStr', true)",
                "TAJS_assert(v.length, 'isMaybeNumUInt', true)");

    }

    @Test
    public void jsstring_split_unknownStringsAndUnknownLimit() {
        Misc.init();
        Misc.runSource("var v = (Math.random()? 'barXfoo' : 'fooXbar').split('X', Math.random());",
                "TAJS_assert(v[0], 'isMaybeAnyStr', true)",
                "TAJS_assert(v[1], 'isMaybeAnyStr', true)",
                "TAJS_assert(v[0], 'isMaybeUndef', true)",
                "TAJS_assert(v[1], 'isMaybeUndef', true)",
                "TAJS_assert(v.length, 'isMaybeNumUInt', true)");
    }

    @Test
    public void jsstring_split_unknownStringsAndZeroLimit() {
        Misc.init();
        Misc.runSource("var v = (Math.random()? 'barXfoo' : 'fooXbar').split('X', 0);",
                "TAJS_assert(v[0] === undefined);",
                "TAJS_assert(v.length === 0);");
    }

    @Test
    public void jsstring_split_noArgs() {
        Misc.init();
        Misc.runSource("var v = 'fooXbar'.split();",
                "TAJS_assert(v[0] === 'fooXbar');",
                "TAJS_assert(v.length === 1);");

    }

    @Test
    public void jsstring_split_regexSimple() {
        Misc.init();
        Misc.runSource("var v = 'fooXbar'.split(/X/);",
                "TAJS_assert(v[0] === 'foo');",
                "TAJS_assert(v[1] === 'bar');",
                "TAJS_assert(v.length === 2);");

    }

    @Test
    public void jsstring_split_regexSimpleConstructor() {
        Misc.init();
        Misc.runSource("var v = 'fooXbar'.split(new RegExp('X'));",
                "TAJS_assert(v[0] === 'foo');",
                "TAJS_assert(v[1] === 'bar');",
                "TAJS_assert(v.length === 2);");

    }

    @Test
    public void jsstring_split_regexSimpleMultiAlloc() {
        Misc.init();
        Misc.runSource("var re = /X/;",
                "var re2 = /Y/;",
                "var re3 = /Z/;",
                "var v = 'fooXbar'.split(re);",
                "TAJS_assert(v[0] === 'foo');",
                "TAJS_assert(v[1] === 'bar');",
                "TAJS_assert(v.length === 2);");

    }

    @Test
    public void jsstring_split_regexSimpleMultiConstructor() {
        Misc.init();
        Misc.runSource("var re = new RegExp('X');",
                "var re2 = new RegExp('Y');",
                "var re3 = new RegExp('Z');",
                "var v = 'fooXbar'.split(re);",
                "TAJS_assert(v[0] === 'foo');",
                "TAJS_assert(v[1] === 'bar');",
                "TAJS_assert(v.length === 2);");

    }

    @Test
    public void jsstring_split_regexCase() {
        Misc.init();
        Misc.runSource("var v = 'fooXbar'.split(/x/i);",
                "TAJS_assert(v[0] === 'foo');",
                "TAJS_assert(v[1] === 'bar');",
                "TAJS_assert(v.length === 2);");
    }

    @Test
    public void jsstring_split_regexCaseConstructor() {
        Misc.init();
        Misc.runSource("var v = 'fooXbar'.split(new RegExp('x', 'i'));",
                "TAJS_assert(v[0] === 'foo');",
                "TAJS_assert(v[1] === 'bar');",
                "TAJS_assert(v.length === 2);");
    }

    @Test
    public void jsstring_split_regexCaseMultiConstructor() {
        Misc.init();
        Misc.runSource("var re = new RegExp('X', 'i')",
                "var re2 = new RegExp('Y', 'i');",
                "var re3 = new RegExp('Z', 'i');",
                "var v = 'fooXbar'.split(re);",
                "TAJS_assert(v[0] === 'foo');",
                "TAJS_assert(v[1] === 'bar');",
                "TAJS_assert(v.length === 2);");

    }

    @Test
    public void jsstring_split_regexAdvanced() {
        Misc.init();
        Misc.runSource("var v = 'fooXbar'.split(/.X./);",
                "TAJS_assert(v[0] === 'fo');",
                "TAJS_assert(v[1] === 'ar');",
                "TAJS_assert(v.length === 2);");

    }

    @Test
    public void jsstring_split_regexAdvancedConstructor() {
        Misc.init();
        Misc.runSource("var v = 'fooXbar'.split(new RegExp('.X.'));",
                "TAJS_assert(v[0] === 'fo');",
                "TAJS_assert(v[1] === 'ar');",
                "TAJS_assert(v.length === 2);");

    }

}
