package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class JSString_search_test {

	@Before
    public void before() {
        Main.reset();
        Main.initLogging();
        Options.get().enableTest();
    }


    @Test
    public void noArgs() {
        Misc.init();
        Misc.runSource("var v = 'foo'.search();",
                "TAJS_assert(v === 0);");
    }

    @Test
    public void init() {
        Misc.init();
        Misc.runSource("var v = 'foo'.search('f');",
                "TAJS_assert(v === 0);");
    }

    @Test
    public void notFound() {
        Misc.init();
        Misc.runSource("var v = 'foo'.search('x');",
                "TAJS_assert(v === -1);");
    }

    @Test
    public void init2() {
        Misc.init();
        Misc.runSource("var v = 'foo'.search('fo');",
                "TAJS_assert(v === 0);");
    }

    @Test
    public void notInit() {
        Misc.init();
        Misc.runSource("var v = 'foo'.search('oo');",
                "TAJS_assert(v === 1);");
    }

    @Test
    public void regex() {
        Misc.init();
        Misc.runSource("var v = 'foo'.search('[^f]o');",
                "TAJS_assert(v === 1);");
    }

    @Test
    public void unknown() {
        Misc.init();
        Misc.runSource("var v = 'foo'.search(Math.random()? 'foo': 'bar');",
                "TAJS_assert(v, 'isMaybeNumUInt || isMaybeNumOther', true);");
    }
}
