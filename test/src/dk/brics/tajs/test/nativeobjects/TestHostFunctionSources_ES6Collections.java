package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import dk.brics.tajs.test.monitors.OrdinaryExitReachableCheckerMonitor;
import org.junit.Before;
import org.junit.Test;

public class TestHostFunctionSources_ES6Collections {

    private IAnalysisMonitoring monitor;

    @Before
    public void before() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
        Options.get().enablePolyfillES6Collections();
        monitor = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableCheckerMonitor());
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

    @Test(expected = AssertionError.class /* GitHub #3 */)
    public void missingSetterSupport() {
        Misc.runSource(new String[]{"TAJS_assert((new Set()).size, 'isMaybeNumUInt')"}, monitor);
    }

    @Test
    public void Set_forEach() {
        Misc.runSource(new String[]{
                "var s = new Set();",
                "var o = {};",
                "s.add(o);",
                "s.forEach(function(v){v.KILL_UNDEFINED; TAJS_assert(v === o);});"
        }, monitor);
    }

    @Test
    public void Map_forEach() {
        Misc.runSource(new String[]{
                "var m = new Map();",
                "var ok = {};",
                "var ov = {};",
                "m.set(ok, ov);",  // not very precise analysis of this function -> last assert is really weak
                "m.forEach(function(v, k){",
                "   k.KILL_UNDEFINED;",
                "   v.KILL_UNDEFINED;",
                "   TAJS_assert(v === ov);",
                "   TAJS_assert(typeof k === 'object');",
                "});"
        }, monitor);
    }

    public void basics(String functionName) {
        Misc.runSource(new String[]{
                "TAJS_assert(" + functionName + " !== undefined)",
                "TAJS_assert(typeof " + functionName + " === 'function');",
                "var o = new " + functionName + "();",
                "TAJS_assert(o, 'isMaybeObject');",
                "TAJS_assert(typeof o.has === 'function');",
                "TAJS_assert(typeof o.delete === 'function');",
                "TAJS_assert(o.has(42) === false);",
                "if(o instanceof Map || o instanceof WeakMap){",
                "   o.set(42);",
                "}else{",
                "   o.add(42);",
                "}",
                "TAJS_assert(o.has(42), 'isMaybeAnyBool');",
                "TAJS_assert(o.has('foo'), 'isMaybeAnyBool');",
                "o.delete(42);",
                "TAJS_assert(o.has(42), 'isMaybeAnyBool');",
        }, monitor);
    }
}
