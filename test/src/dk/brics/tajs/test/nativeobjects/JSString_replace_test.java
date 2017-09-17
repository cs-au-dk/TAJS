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
        Options.get().enableLoopUnrolling(10);
        Options.get().enableTest();
    }

    @Test
    public void noArgs() {
        Misc.runSource("var v = 'foo'.replace();",
                "TAJS_assertEquals('foo', v);");
    }

    @Test
    public void noArgs2() {
        Misc.runSource("var v = 'foo'.replace(undefined);",
                "TAJS_assertEquals('foo', v);");
    }

    @Test
    public void init() {
        Misc.runSource("var v = 'foo'.replace('f');",
                "TAJS_assertEquals('undefinedoo', v);");
    }

    @Test
    public void notFound() {
        Misc.runSource("var v = 'foo'.replace('x');",
                "TAJS_assertEquals('foo', v);");
    }

    @Test
    public void init2() {
        Misc.runSource("var v = 'foo'.replace('fo');",
                "TAJS_assert(v === 'undefinedo');");
    }

    @Test
    public void replacement() {
        Misc.runSource("var v = 'foo'.replace('oo', 'OO');",
                "TAJS_assert(v === 'fOO');");
    }

    @Test
    public void replacementFunctionOneMatch() {
        Misc.runSource(
                "var x = false;",
                "var v = 'foo'.replace('oo', function(){ x = true; return 'OO'; }); ",
                "TAJS_assertEquals('fOO', v);",
                "TAJS_assertEquals(true, x);"
        );
    }

    @Test
    public void replacementFunctionTwoMatchesNonGlobal() {
        Misc.runSource(
                "var x = false;",
                " var v = 'foo'.replace('o', function(){ x = true; return 'O';});",
                "TAJS_assertEquals('fOo', v);",
                "TAJS_assertEquals(true, x);"
        );
    }

    @Test
    public void replacementFunctionTwoMatchesGlobal() {
        Misc.runSource(
                "var x = false;",
                " var v = 'foo'.replace(/o/g, function(){ x = true; return 'O';});",
                "TAJS_assertEquals('fOO', v);",
                "TAJS_assertEquals(true, x);"
        );
    }

    @Test
    public void replacementFunctionNoMatches() {
        Misc.runSource("var x = false; var v = 'bar'.replace('oo', function(){ x = true; }); TAJS_assert(v === 'bar'); TAJS_assert(x === false);");
    }

    @Test
    public void replacementFunctionMaybeMatch() {
        Misc.runSource("var x = false; var v = (Math.random()?'foo':'bar').replace('oo', function(){ x = true; }); TAJS_assert(v, 'isMaybeAnyStr'); TAJS_assert(x, 'isMaybeTrueButNotFalse', false); TAJS_assert(x, 'isMaybeFalseButNotTrue', false);");
    }

    @Test
    public void replacementFunctionArgumeunts() {
        Misc.runSource("var x = false; var v = 'foo'.replace('oo', function(p1, p2){ TAJS_dumpValue(p1); TAJS_dumpValue(p2); }); ");
    }

    @Test
    public void replacementNumbers() {
        // example from ECMA spec
        Misc.runSource("var v = '$1,$2'.replace(/(\\$(\\d))/g, '$$1-$1$2')",
                "TAJS_assert(v === '$1-$11,$1-$22');");
    }

    @Test
    public void regexAsString() {
        Misc.runSource("var v = 'foo'.replace('[^f]o', 'ba');",
                "TAJS_assert(v === 'foo');");
    }

    @Test
    public void regex() {
        Misc.runSource("var v = 'foo'.replace(/[^f]o/, 'ba');",
                "TAJS_assert(v === 'fba');");
    }

    @Test
    public void unknown() {
        Misc.runSource("var v = 'foo'.replace(Math.random()? 'foo': 'bar');",
                "TAJS_assert(v, 'isMaybeAnyStr', true);");
    }

    @Test
    public void replacementFunction() {
        Misc.runSource("var v = 'foo'.replace('oo', function(){return 'OO'});",
                "TAJS_assert(v === 'fOO');");
    }

    @Test
    public void replacementFunctionTwice() {
        Misc.runSource("var v = 'foo'.replace(/o/g, function(){return 'OO'});",
                "TAJS_assert(v === 'fOOOO');");
    }

    @Test
    public void miscExtraTests() {
        Misc.runSource(
                "var r1 = 'okkkkk'.replace('kk', function() { return 'b' })",
                "var r2 = 'okkkkk'.replace(/kk/, function() { return 'b' })",
                "var r3 = 'okkkkk'.replace(/kk/g, function() { return 'b' })",
                "var r4 = 'okkkkk'.replace(/kk(k)(k)/, function() { return 'b' })",
                "var r5 = 'okkkkk'.replace(/kk(k)(k)/g, function() { return 'b' })"
        );
    }
}
