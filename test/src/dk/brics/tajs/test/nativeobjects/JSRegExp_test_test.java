package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class JSRegExp_test_test {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void noArgs() {
        Misc.runSource("var v = /foo/.test();",
                "TAJS_assert(v === false);"
        );
    }

    @Test
    public void init() {
        Misc.runSource("var v = /f/.test('foo');",
                "TAJS_assert(v === true);"
        );
    }

    @Test
    public void notFound() {
        Misc.runSource("var v = /x/.test('foo');",
                "TAJS_assert(v === false);");
    }

    @Test
    public void init2() {
        Misc.runSource("var v = /fo/.test('foo');",
                "TAJS_assert(v === true);"
        );
    }

    @Test
    public void notInit() {
        Misc.runSource("var v = /oo/.test('foo');",
                "TAJS_assert(v === true);"
        );
    }

    @Test
    public void regex() {
        Misc.runSource("var v = /[^f]o/.test('foo');",
                "TAJS_assert(v === true);"
        );
    }

    @Test
    public void multimatch_but_one_result() {
        Misc.runSource("var v = /o/g.test('foo');",
                "TAJS_assert(v === true);"
        );
    }

    @Test
    public void match_not_global_do_not_advance_lastIndex() {
        Misc.runSource("var r = /o/; var v = r.test('foo');",
                "TAJS_assert(v === true);",
                "TAJS_assert(r.lastIndex === 0);"
        );
    }

    @Test
    public void match_global_advance_lastIndex() {
        Misc.runSource("var r = /o/g; var v = r.test('foo');",
                "TAJS_assert(v === true);",
                "TAJS_assertEquals(2, r.lastIndex);"
        );
    }

    @Test
    public void groupMatch() {
        Misc.runSource("var v =/(o)(.)/.test('foxoy')",
                "TAJS_assert(v === true);"
        );
    }

    @Test
    public void unknown() {
        Misc.runSource("var v = /foo/.test(Math.random()? 'foo': 'bar');",
                "TAJS_assert(v, 'isMaybeAnyBool', true);");
    }
}
