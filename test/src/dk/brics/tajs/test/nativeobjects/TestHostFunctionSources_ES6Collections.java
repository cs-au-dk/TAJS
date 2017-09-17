package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

public class TestHostFunctionSources_ES6Collections {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enablePolyfillES6Collections();
    }

    @Test
    public void Map_basics() {
        basics("Map");
    }

    @Test
    public void Set_basics() {
        basics("Set");
    }

    @Test
    public void WeakMap_basics() {
        basics("WeakMap");
    }

    @Test
    public void WeakSet_basics() {
        basics("WeakSet");
    }

    @Test
    public void missingSetterSupport() {
        Misc.runSource("TAJS_assert((new Set()).size, 'isMaybeNumUInt||isMaybeUndef')");
    }

    @Test
    public void Set_forEach() {
        Misc.runSource(
                "var s = new Set();",
                "var o = {};",
                "s.add(o);",
                "s.forEach(function(v){v.KILL_UNDEFINED; TAJS_assert(v === o);});");
    }

    @Test
    public void Map_forEach() {
        Misc.runSource(
                "var m = new Map();",
                "var ok = {};",
                "var ov = {};",
                "m.set(ok, ov);",  // not very precise analysis of this function -> last assert is really weak
                "m.forEach(function(v, k){",
                "   k.KILL_UNDEFINED;",
                "   v.KILL_UNDEFINED;",
                "   TAJS_assert(v === ov);",
                "   TAJS_assert(typeof k === 'object');",
                "});");
    }

    private void basics(String functionName) {
        Misc.runSource(
                "TAJS_assert(" + functionName + " !== undefined)",
                "TAJS_assert(typeof " + functionName + " === 'function');",
                "var o = new " + functionName + "();",
                "TAJS_assert(o, 'isMaybeObject');",
                "TAJS_assert(typeof o.has === 'function');",
                "TAJS_assert(typeof o.delete === 'function');",
                "TAJS_assert(o.has(42), 'isMaybeAnyBool');",
                ("Set".equals(functionName) ? "   o.add(42);" : "   o.set(42);"),
                "TAJS_assert(o.has(42), 'isMaybeAnyBool');",
                "TAJS_assert(o.has('foo'), 'isMaybeAnyBool');",
                "o.delete(42);",
                "TAJS_assert(o.has(42), 'isMaybeAnyBool');");
    }
}
