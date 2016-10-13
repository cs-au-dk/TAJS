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

public class JSObject_defineProperty_test {

    private IAnalysisMonitoring monitor;

    @Before
    public void before() {
        Main.reset();
        Main.initLogging();
        Options.get().enableTest();
        monitor = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableCheckerMonitor());
    }

    @Test
    public void callable() {
        Misc.runSource(new String[]{
                "var o = {};",
                "Object.defineProperty(o, 'p', {});"
        }, monitor);
    }

    @Test
    public void data() {
        Misc.runSource(new String[]{
                "var o = {};",
                "Object.defineProperty(o, 'p', {value: 42});",
        }, monitor);
    }

    @Test
    public void accessor() {
        Misc.runSource(new String[]{
                "var o = {};",
                "Object.defineProperty(o, 'p', {get: function(){ return 42; }});"
        }, monitor);
    }

    @Test
    public void dataAndAccessor() {
        Misc.runSource(new String[]{
                // Should throw definite TypeError:
                // "TypeError:Invalid property.A property cannot both have accessors and be writable or have a value"
                "var o = {};",
                "try{",
                "   Object.defineProperty(o, 'p', {value: 42, get: function(){return 42;}});",
                "   TAJS_assert(false);",
                "}catch(e){}"
        }, monitor);
    }

    @Test
    public void value() {
        Misc.runSource(new String[]{
                "var o1 = {};",
                "Object.defineProperty(o1, 'p', {value: 42});",
                "TAJS_assert(o1.p === 42);",
                // default undefined
                "var o2 = {};",
                "Object.defineProperty(o2, 'p', {});",
                "TAJS_assert(o2.hasOwnProperty('p'));",
                "TAJS_assert(o2.p === undefined);",
        }, monitor);
    }

    @Test
    public void enumerable() {
        Misc.runSource(new String[]{
                        "var o1 = {};",
                        "Object.defineProperty(o1, 'p', {enumerable: true});",
                        "TAJS_assert(Object.keys(o1).length === 1);",
                        "var o2 = {};",
                        "Object.defineProperty(o2, 'p', {enumerable: false});",
                        "TAJS_assert(Object.keys(o2).length === 0);",
                        // default false
                        "var o3 = {};",
                        "Object.defineProperty(o3, 'p', {enumerable: false});",
                        "TAJS_assert(Object.keys(o3).length === 0);"
                },
                monitor);
    }

    @Test
    public void writable() {
        Misc.runSource(new String[]{
                "var o1 = {};",
                "Object.defineProperty(o1, 'p', {writable: true});",
                "o1.p = 42;",
                "TAJS_assert(o1.p === 42);",
                "var o2 = {};",
                "Object.defineProperty(o2, 'p', {writable: false});",
                "o2.p = 42;",
                "TAJS_assert(o2.p === undefined);",
                // default false
                "var o3 = {};",
                "Object.defineProperty(o3, 'p', {writable: false});",
                "o3.p = 42;",
                "TAJS_assert(o3.p === undefined);",
        }, monitor);
    }

    @Test
    public void configurable() {
        Misc.runSource(new String[]{
                "var o1 = {};",
                "Object.defineProperty(o1, 'p', {configurable: true, writable: true});",
                "o1.p = 42;",
                "TAJS_assert(o1.p === 42);",
                "delete o1.p",
                "TAJS_assert(o1.hasOwnProperty('p') === false);",
                "var o2 = {};",
                "Object.defineProperty(o2, 'p', {configurable: false, writable: true});",
                "o2.p = 42;",
                "TAJS_assert(o2.p === 42);",
                "delete o2.p",
                "TAJS_assert(o2.p === 42);",
                // default false
                "var o3 = {};",
                "Object.defineProperty(o3, 'p', {configurable: false, writable: true});",
                "o3.p = 42;",
                "TAJS_assert(o3.p === 42);",
                "delete o3.p",
                "TAJS_assert(o3.p === 42);",
        }, monitor);
    }

    @Test
    public void getter() {
        Misc.runSource(new String[]{
                "var o = {};",
                "Object.defineProperty(o, 'p', {get: function(){return 42;}});",
                "TAJS_assert(o.p === 42);"
        }, monitor);
    }

    @Test
    public void setter() {
        Misc.runSource(new String[]{
                "var o = {};",
                "var x;",
                "Object.defineProperty(o, 'p', {set: function(v){x = v;}});",
                "o.p = 42;",
                "TAJS_assert(x === 42);",
        }, monitor);
    }
}
