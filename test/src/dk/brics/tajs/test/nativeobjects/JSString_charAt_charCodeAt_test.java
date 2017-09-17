package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class JSString_charAt_charCodeAt_test {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void noArgs() {
        Misc.runSource("var v = 'foo'.charAt();",
                "TAJS_assert(v === 'f');");
    }

    @Test
    public void init() {
        Misc.runSource("var v = 'foo'.charAt('0');",
                "TAJS_assert(v === 'f');");
    }

    @Test
    public void illegal() {
        Misc.runSource("var v = 'foo'.charAt('x');",
                "TAJS_assert(v === 'f');");
    }

    @Test
    public void notInit() {
        Misc.runSource("var v = 'foo'.charAt(2);",
                "TAJS_assert(v === 'o');");
    }

    @Test
    public void outOfBounds() {
        Misc.runSource("var v = 'foo'.charAt(42);",
                "TAJS_assert(v === '');");
    }

    @Test
    public void unknown() {
        Misc.runSource("var v = 'foo'.charAt(Math.random());",
                "TAJS_assert(v, 'isMaybeAnyStr', true);");
    }

    @Test
    public void noArgs_charCode() {
        Misc.runSource("var v = 'foo'.charCodeAt();",
                "TAJS_assert(v === 102);");
    }

    @Test
    public void init_charCode() {
        Misc.runSource("var v = 'foo'.charCodeAt(0);",
                "TAJS_assert(v === 102);");
    }

    @Test
    public void illegal_charCode() {
        Misc.runSource("var v = 'foo'.charCodeAt('x');",
                "TAJS_assert(v === 102);");
    }

    @Test
    public void notInit_charCode() {
        Misc.runSource("var v = 'foo'.charCodeAt(2);",
                "TAJS_assert(v === 111);");
    }

    @Test
    public void outOfBounds_charCode() {
        Misc.runSource("var v = 'foo'.charCodeAt(42);",
                "TAJS_assert(isNaN(v));");
    }

    @Test
    public void unknown_charCode() {
        Misc.runSource("var v = 'foo'.charCodeAt(Math.random());",
                "TAJS_assert(v, 'isMaybeNumUInt || isMaybeNumOther', true);");
    }
}
