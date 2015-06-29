package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class JSString_localeCompare_test {

	@Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }


    @Test
    public void noArgs() {
        Misc.init();
        Misc.runSource("var v = 'foo'.localeCompare();",
                "TAJS_assert(v === -1);");
    }

    @Test
    public void undefinedSingeArgs() {
        Misc.init();
        Misc.runSource("var v = 'foo'.localeCompare(undefined);",
                "TAJS_assert(v === -1);");
    }

    @Test
    public void singleGT() {
        Misc.init();
        Misc.runSource("var v = 'foo'.localeCompare('bar');",
                "TAJS_assert(v === 1);");
    }

    @Test
    public void singleLT() {
        Misc.init();
        Misc.runSource("var v = 'foo'.localeCompare('qux');",
                "TAJS_assert(v === -1);");
    }

    @Test
    public void singleEQ() {
        Misc.init();
        Misc.runSource("var v = 'foo'.localeCompare('foo');",
                "TAJS_assert(v === 0);");
    }

    @Test
    public void unknown_charCode() {
        Misc.init();
        Misc.runSource("var v = 'foo'.localeCompare(Math.random()? 'foo': 'bar');",
                "TAJS_assert(v, 'isMaybeNumUInt', true);");
    }
}
