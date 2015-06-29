package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class JSRegExp_exec_test {

	@Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void noArgs() { // should fail!
        Misc.init();
        Misc.runSource("var v = /foo/.exec();");
    }

    @Test
    public void init() {
        Misc.init();
        Misc.runSource("var v = /f/.exec('foo');",
                "TAJS_assert(v.length === 1);",
                "TAJS_assert(v[0] === 'f');"
        );
    }

    @Test
    public void notFound() {
        Misc.init();
        Misc.runSource("var v = /x/.exec('foo');",
                "TAJS_assert(v === null);");
    }

    @Test
    public void init2() {
        Misc.init();
        Misc.runSource("var v = /fo/.exec('foo');",
                "TAJS_assert(v.length === 1);",
                "TAJS_assert(v[0] === 'fo');"
        );
    }

    @Test
    public void notInit() {
        Misc.init();
        Misc.runSource("var v = /oo/.exec('foo');",
                "TAJS_assert(v.length === 1);",
                "TAJS_assert(v[0] === 'oo');"
        );
    }

    @Test
    public void regex() {
        Misc.init();
        Misc.runSource("var v = /[^f]o/.exec('foo');",
                "TAJS_assert(v.length === 1);",
                "TAJS_assert(v[0] === 'oo');"
        );
    }

    @Test
    public void multimatch_but_one_result() {
        Misc.init();
        Misc.runSource("var v = /o/g.exec('foo');",
                "TAJS_assert(v.length === 1);",
                "TAJS_assert(v[0] === 'o');"
        );
    }

    @Test
    public void groupMatch() {
        Misc.init();
        Misc.runSource("var v =/(o)(.)/.exec('foxoy')",
                "TAJS_assert(v.length === 3);",
                "TAJS_assert(v[0] === 'ox');",
                "TAJS_assert(v[1] === 'o');",
                "TAJS_assert(v[2] === 'x');"
        );
    }

    @Test
    public void match_not_global_do_not_advance_lastIndex() {
        Misc.init();
        Misc.runSource("var r = /o/; var v = r.exec('foo');",
                "TAJS_assert(r.lastIndex === 0);"
        );
    }

    @Test
    public void match_global_advance_lastIndex() {
        Misc.init();
        Misc.runSource("var r = /o/g; var v = r.exec('foo');",
                "TAJS_assert(r.lastIndex, 'isMaybeNumUInt');"
        );
    }

    @Test
    public void unknown() {
        Misc.init();
        Misc.runSource("var v = /foo/.exec(Math.random()? 'foo': 'bar');",
                "TAJS_assert(v, 'isMaybeObject || isMaybeNull');");
    }
}
