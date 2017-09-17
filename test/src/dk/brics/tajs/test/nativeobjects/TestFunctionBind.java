package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

public class TestFunctionBind {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enablePolyfillMDN();
    }

    @Test
    public void receiver() {
        Misc.runSource(
                "var o = {};",
                "function f(){TAJS_assert(this === o);}",
                "o.m = f;",
                "o.m();",
                "function g(){TAJS_assert(this === o);}",
                "var gm = g.bind(o);",
                "gm();",
                "var GLOBAL = this;",
                "function h(){TAJS_assert(this === GLOBAL);}",
                "var hm = h.bind();",
                "hm();");
    }

    @Test
    public void fakeReceiver() {
        Misc.runSource(
                "var o = {};",
                "function f(){TAJS_assert(this === o);}",
                "var fm = f.bind(o);",
                "var o2 = {fm: fm};",
                "o2.fm();");
    }

    @Test
    public void boundAllArgs1() {
        Misc.runSource(
                "function f(v1){TAJS_assertEquals('v1', v1);}",
                "f('v1')",
                "var fbound = f.bind(undefined, 'v1');",
                "fbound();"
        );
    }

    @Test
    public void boundAllArgs2() {
        Misc.runSource(
                "function f(v1, v2){TAJS_assertEquals('v1', v1); TAJS_assertEquals('v2', v2);}",
                "f('v1', 'v2')",
                "var fbound = f.bind(undefined, 'v1', 'v2');",
                "fbound();"
        );
    }

    @Test
    public void boundSomeArgs() {
        Misc.runSource(
                "function f(v1, v2){TAJS_assertEquals('v1', v1); TAJS_assertEquals('v2', v2);}",
                "f('v1', 'v2')",
                "var fbound = f.bind(undefined, 'v1');",
                "fbound('v2');"
        );
    }

    @Test
    public void objectSensitivity() {
        Misc.runSource(
                "var o1 = {};",
                "var o2 = {};",
                "function f(v){TAJS_assert(this === v);}",
                "var fbound1 = f.bind(o1);",
                "var fbound2 = f.bind(o2);",
                "fbound1(o1);",
                "fbound2(o2);");
    }

    @Test
    public void parameterSensitivity() {
        Misc.runSource(
                "function f(x, y){TAJS_assert(x === y);}",
                "var fbound1 = f.bind(undefined, 1);",
                "var fbound2 = f.bind(undefined, 2);",
                "fbound1(1);",
                "fbound2(2);");
    }

    @Test
    public void limitedParameterSensitivity() {
        Misc.runSource(
                "function f(x){TAJS_assert(x, 'isMaybeNumUInt');}",
                "var fbound1 = f.bind(undefined);",
                "var fbound2 = f.bind(undefined);",
                "fbound1(1);",
                "fbound2(2);");
    }

    @Test
    public void nonTerminationBug_minimized() {
        Main.reset();
        Options.get().enableTest();
        Misc.run("test-resources/src/function-bind/non-termination-bug_minimized.js");
    }
}
