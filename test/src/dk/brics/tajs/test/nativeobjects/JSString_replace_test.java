package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import dk.brics.tajs.util.AnalysisException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("static-method")
public class JSString_replace_test {

	@Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void noArgs() {
        Misc.init();
        Misc.runSource("var v = 'foo'.replace();", // bug in native implementation!
                "TAJS_assert(v === 'foo');");
    }

    @Test
    public void noArgs2() {
        Misc.init();
        Misc.runSource("var v = 'foo'.replace(undefined);",
                "TAJS_assert(v === 'foo');");
    }

    @Test
    public void init() {
        Misc.init();
        Misc.runSource("var v = 'foo'.replace('f');",
                "TAJS_assert(v === 'undefinedoo');");
    }

    @Test
    public void notFound() {
        Misc.init();
        Misc.runSource("var v = 'foo'.replace('x');",
                "TAJS_assert(v === 'foo');");
    }

    @Test
    public void init2() {
        Misc.init();
        Misc.runSource("var v = 'foo'.replace('fo');",
                "TAJS_assert(v === 'undefinedo');");
    }

    @Test
    public void replacement() {
        Misc.init();
        Misc.runSource("var v = 'foo'.replace('oo', 'OO');",
                "TAJS_assert(v === 'fOO');");
    }

    @Ignore // FIXME missing model of String.prototype.replace(..., function(){..})
    @Test(expected = AnalysisException.class /* Fails early due to unsupported callback */)
    public void replacementFunction() {
        Misc.init();
        Misc.runSource("var v = 'foo'.replace('oo', function(){});");
    }

    @Test
    public void replacementFunctionNoMatches() {
        Misc.init();
        Misc.runSource("var v = 'foo'.replace('xx', function(){});",
                "TAJS_assert(v === 'foo');");

    }

    @Test
    public void replacementNumbers() {
        // example from ECMA spec
        Misc.init();
        Misc.runSource("var v = '$1,$2'.replace(/(\\$(\\d))/g, '$$1-$1$2')",
                "TAJS_assert(v === '$1-$11,$1-$22');");
    }

    @Test
    public void regexAsString() {
        Misc.init();
        Misc.runSource("var v = 'foo'.replace('[^f]o', 'ba');",
                "TAJS_assert(v === 'foo');");
    }

    @Test
    public void regex() {
        Misc.init();
        Misc.runSource("var v = 'foo'.replace(/[^f]o/, 'ba');",
                "TAJS_assert(v === 'fba');");
    }

    @Test
    public void unknown() {
        Misc.init();
        Misc.runSource("var v = 'foo'.replace(Math.random()? 'foo': 'bar');",
                "TAJS_assert(v, 'isMaybeAnyStr', true);");
    }
}
