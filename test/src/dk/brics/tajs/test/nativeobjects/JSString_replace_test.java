package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class JSString_replace_test {

	@Before
    public void before() {
        Main.reset();
        Main.initLogging();
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

    @Test
    public void replacementFunctionOneMatch() {
        Misc.init();
        Misc.runSource("var x = false; var v = 'foo'.replace('oo', function(){ x = true; }); TAJS_assert(v, 'isMaybeAnyStr'); TAJS_assert(x, 'isMaybeTrueButNotFalse', false); TAJS_assert(x, 'isMaybeFalseButNotTrue', false);");
    }

    @Test
    public void replacementFunctionTwoMatchest() {
        Misc.init();
        Misc.runSource("var x = false; var v = 'foo'.replace('o', function(){ x = true; }); TAJS_assert(v, 'isMaybeAnyStr'); TAJS_assert(x, 'isMaybeTrueButNotFalse', false); TAJS_assert(x, 'isMaybeFalseButNotTrue', false);");
    }

    @Test
    public void replacementFunctionNoMatches() {
        Misc.init();
        Misc.runSource("var x = false; var v = 'bar'.replace('oo', function(){ x = true; }); TAJS_assert(v === 'bar'); TAJS_assert(x === false);");

    }

    @Test
    public void replacementFunctionMaybeMatch() {
        Misc.init();
        Misc.runSource("var x = false; var v = (Math.random()?'foo':'bar').replace('oo', function(){ x = true; }); TAJS_assert(v, 'isMaybeAnyStr'); TAJS_assert(x, 'isMaybeTrueButNotFalse', false); TAJS_assert(x, 'isMaybeFalseButNotTrue', false);");
    }

    @Test
    public void replacementFunctionArgumeunts() {
        Misc.init();
        Misc.runSource("var x = false; var v = 'foo'.replace('oo', function(p1, p2){ TAJS_dumpValue(p1); TAJS_dumpValue(p2); }); ");
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
