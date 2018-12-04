package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

public class JSObject_assign_test {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void returnSelf() {
        Misc.runSource("var s1 = {};",
                "var s2 = Object.assign(s1);",
                "TAJS_assertEquals(s1, s2);");
    }

    @Test
    public void returnSelfBox() {
        Misc.runSource("var s1 = 'foo';",
                "var s2 = Object.assign(s1);",
                "TAJS_assertEquals(s1, s2, false);",
                "TAJS_assert(s1 == s2);");
    }

    @Test
    public void simple() {
        Misc.runSource("var o1 = {}",
                "var o2 = {foo: 'foo', bar: 'bar'}",
                "Object.assign(o1, o2);",
                "TAJS_assertEquals('foo', o1.foo);",
                "TAJS_assertEquals('bar', o1.bar);",
                "TAJS_assertEquals(undefined, o1.baz);");
    }

    @Test
    public void fuzzy() {
        Misc.runSource("var o1 = {};",
                "var o2 = {};",
                "o2[TAJS_make('AnyStrNotNumeric')] = 'FUZZY';",
                "Object.assign(o1, o2);",
                "TAJS_assertEquals(TAJS_join(undefined, 'FUZZY'), o1.foo);",
                "TAJS_assertEquals(TAJS_join(undefined), o1[42]);");
    }

    @Test
    public void weak() {
        Misc.runSource("var o1 = {}",
                "var o2 = {foo: 'foo'}",
                "var o3 = {bar: 'bar'}",
                "Object.assign(o1, TAJS_join(o2, o3));",
                "TAJS_assertEquals(TAJS_join(undefined, 'foo'), o1.foo);",
                "TAJS_assertEquals(TAJS_join(undefined, 'bar'), o1.bar);");
    }

    @Test
    public void getter() {
        Misc.runSource("var o1 = {}",
                "var o2 = {get foo(){return 'foo'}}",
                "Object.assign(o1, o2);",
                "TAJS_assertEquals('foo', o1.foo);");
    }
}
