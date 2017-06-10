package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class JSString_toLowerUpperCase_test {

	@Before
    public void before() {
        Main.reset();
        Main.initLogging();
        Options.get().getUnsoundness().setIgnoreLocale(true);
        Options.get().enableTest();
    }

    @Test
    public void lower() {
        Misc.init();
        Misc.runSource("var v = 'fooBar'.toLowerCase();",
                "TAJS_assert(v === 'foobar');");
    }

    @Test
    public void upper() {
        Misc.init();
        Misc.runSource("var v = 'fooBar'.toUpperCase();",
                "TAJS_assert(v === 'FOOBAR');");
    }

    @Test
    public void localeLower() {
        Misc.init();
        Misc.runSource("var v = 'fooBar'.toLocaleLowerCase();",
                "TAJS_assert(v === 'foobar');");
    }

    @Test
    public void localeUpper() {
        Misc.init();
        Misc.runSource("var v = 'fooBar'.toLocaleUpperCase();",
                "TAJS_assert(v === 'FOOBAR');");
    }

    @Test
    public void lower_unknown() {
        Misc.init();
        Misc.runSource("var v = (Math.random()? 'foo': 'bar').toLowerCase();",
                "TAJS_assert(v, 'isMaybeAnyStr', true);");
    }

    @Test
    public void upper_unknown() {
        Misc.init();
        Misc.runSource("var v = (Math.random()? 'foo': 'bar').toUpperCase();",
                "TAJS_assert(v, 'isMaybeAnyStr', true);");
    }

    @Test
    public void localeLower_unknown() {
        Misc.init();
        Misc.runSource("var v = (Math.random()? 'foo': 'bar').toLocaleLowerCase();",
                "TAJS_assert(v, 'isMaybeAnyStr', true);");
    }

    @Test
    public void localeUpper_unknown() {
        Misc.init();
        Misc.runSource("var v = (Math.random()? 'foo': 'bar').toLocaleUpperCase();",
                "TAJS_assert(v, 'isMaybeAnyStr', true);");
    }

}

