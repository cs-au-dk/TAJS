package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class JSString_substring_test {

	@Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }


    @Test
    public void noArgs() {
        Misc.init();
        Misc.runSource("var v = 'foo'.substring();",
                "TAJS_assert(v === 'foo');");
    }

    @Test
    public void posArg() {
        Misc.init();
        Misc.runSource("var v = 'foo'.substring(1);",
                "TAJS_assert(v === 'oo');");
    }

    @Test
    public void negArg() {
        Misc.init();
        Misc.runSource("var v = 'foo'.substring(-1);",
                "TAJS_assert(v === 'foo');");
    }

    @Test
    public void interval() {
        Misc.init();
        Misc.runSource("var v = 'foo'.substring(1, 2);",
                "TAJS_assert(v === 'o');");
    }

    @Test
    public void unknown() {
        Misc.init();
        Misc.runSource("var v = 'foo'.substring(Math.random(), 2);",
                "TAJS_assert(v, 'isMaybeAnyStr', true);");
    }
}
