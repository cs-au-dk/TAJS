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
        Options.get().getUnsoundness().setIgnoreLocale(true);
        Options.get().enableTest();
    }

    @Test
    public void noArgs() {
        Misc.runSource("var v = 'foo'.localeCompare();",
                "TAJS_assert(v === -1);");
    }

    @Test
    public void undefinedSingeArgs() {
        Misc.runSource("var v = 'foo'.localeCompare(undefined);",
                "TAJS_assert(v === -1);");
    }

    @Test
    public void singleGT() {
        Misc.runSource("var v = 'foo'.localeCompare('bar');",
                "TAJS_assert(v === 1);");
    }

    @Test
    public void singleLT() {
        Misc.runSource("var v = 'foo'.localeCompare('qux');",
                "TAJS_assert(v === -1);");
    }

    @Test
    public void singleEQ() {
        Misc.runSource("var v = 'foo'.localeCompare('foo');",
                "TAJS_assert(v === 0);");
    }

    @Test
    public void unknown_charCode() {
        Misc.runSource("var v = 'foo'.localeCompare(Math.random()? 'foo': 'bar');",
                "TAJS_assert(v, 'isMaybeNumUInt', true);");
    }
}
