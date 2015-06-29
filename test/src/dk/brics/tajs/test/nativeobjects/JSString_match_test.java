package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")

public class JSString_match_test {
    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void noArgs() {
        Misc.init();
        Misc.runSource("var v = 'foo'.match();",
                "TAJS_assert(v.length === 1);",
                "TAJS_assert(v[0] === '');"
        );
    }

    @Test
    public void init() {
        Misc.init();
        Misc.runSource("var v = 'foo'.match('f');",
                "TAJS_assert(v.length === 1);",
                "TAJS_assert(v[0] === 'f');"
        );
    }

    @Test
    public void notFound() {
        Misc.init();
        Misc.runSource("var v = 'foo'.match('x');",
                "TAJS_assert(v === null);");
    }

    @Test
    public void init2() {
        Misc.init();
        Misc.runSource("var v = 'foo'.match('fo');",
                "TAJS_assert(v.length === 1);",
                "TAJS_assert(v[0] === 'fo');"
        );
    }

    @Test
    public void notInit() {
        Misc.init();
        Misc.runSource("var v = 'foo'.match('oo');",
                "TAJS_assert(v.length === 1);",
                "TAJS_assert(v[0] === 'oo');"
        );
    }


    @Test
    public void regex() {
        Misc.init();
        Misc.runSource("var v = 'foo'.match('[^f]o');",
                "TAJS_assert(v.length === 1);",
                "TAJS_assert(v[0] === 'oo');"
        );
    }

    @Test
    public void multiMatch() {
        Misc.init();
        Misc.runSource("var v = 'foo'.match(/o/g);",
                "TAJS_assert(v.length === 2);",
                "TAJS_assert(v[0] === 'o');",
                "TAJS_assert(v[1] === 'o');"
        );
    }

    @Test
    public void unknown() {
        Misc.init();
        Misc.runSource("var v = 'foo'.match(Math.random()? 'foo': 'bar');",
                "TAJS_assert(v, 'isMaybeObject || isMaybeNull', true);");
    }
}
