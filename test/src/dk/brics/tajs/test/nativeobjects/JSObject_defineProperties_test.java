package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

public class JSObject_defineProperties_test {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void primitiveTarget() {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("var o = 42;",
                "Object.defineProperties(o, {});",
                "TAJS_assert(false);");
    }

    @Test
    public void none() {
        Misc.runSource("var o = {};",
                "Object.defineProperties(o, {});");
    }

    @Test
    public void single() {
        Misc.runSource("var o = {};",
                "Object.defineProperties(o, {p: {value: 42}});",
                "TAJS_assertEquals(42, o.p);");
    }

    @Test
    public void multi() {
        Misc.runSource("var o = {};",
                "Object.defineProperties(o, {p: {value: 42}, q: {value: true}});",
                "TAJS_assertEquals(42, o.p);",
                "TAJS_assertEquals(true, o.q);");
    }

    @Test
    public void fuzzy() {
        Misc.runSource("var o = {};",
                "var props = {};",
                "props[TAJS_make('AnyStrNotNumeric')] = {value: 42};",
                "props[87] = {value: true};",
                "Object.defineProperties(o, props);",
                "TAJS_assert(o.p, 'isMaybeSingleNum||isMaybeUndef');",
                "TAJS_assert(o.q, 'isMaybeSingleNum||isMaybeUndef');",
                "TAJS_assertEquals(true, o[87]);");
    }
}
