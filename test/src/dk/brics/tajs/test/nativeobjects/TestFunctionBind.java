package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.monitoring.OrdinaryExitReachableChecker;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestFunctionBind {

    private IAnalysisMonitoring monitor;

    @Before
    public void before() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
        Options.get().enablePolyfillMDN();
        monitor = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableChecker());
    }

    @Ignore
    @Test
    public void receiver() {
        Misc.runSource(new String[]{
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
                "hm();",
        }, monitor);
    }

    @Test
    public void fakeReceiver() {
        Misc.runSource(new String[]{
                "var o = {};",
                "function f(){TAJS_assert(this === o);}",
                "var fm = f.bind(o);",
                "var o2 = {fm: fm};",
                "o2.fm();"
        }, monitor);
    }

    @Test
    public void args() {
        Misc.runSource(new String[]{
                "function f(v1){TAJS_assert(v1 === 'v1');}",
                "f('v1')",
                "var fbound = f.bind(undefined, 'v1');",
                "fbound();",
                "function g(v1, v2){TAJS_assert(v1 === 'v1'); TAJS_assert(v2 === 'v2');}",
                "g('v1', 'v2')",
                "var gbound = g.bind(undefined, 'v1');",
                "gbound('v2');"
        }, monitor);
    }

    @Test
    public void objectSensitivity() {
        Misc.runSource(new String[]{
                "var o1 = {};",
                "var o2 = {};",
                "function f(v){TAJS_assert(this === v);}",
                "var fbound1 = f.bind(o1);",
                "var fbound2 = f.bind(o2);",
                "fbound1(o1);",
                "fbound2(o2);"
        }, monitor);
    }

    @Test
    public void parameterSensitivity() {
        Misc.runSource(new String[]{
                "function f(x, y){TAJS_assert(x === y);}",
                "var fbound1 = f.bind(undefined, 1);",
                "var fbound2 = f.bind(undefined, 2);",
                "fbound1(1);",
                "fbound2(2);",
        }, monitor);
    }

    @Test
    public void limitedParameterSensitivity() {
        Misc.runSource(new String[]{
                "function f(x){TAJS_assert(x, 'isMaybeNumUInt');}",
                "var fbound1 = f.bind(undefined);",
                "var fbound2 = f.bind(undefined);",
                "fbound1(1);",
                "fbound2(2);",
        }, monitor);
    }

    @Test
    public void nonTerminationBug_minimized() {
        Main.reset();
        Options.get().enableTest();
        Misc.run(new String[]{"test/function-bind/non-termination-bug_minimized.js"});
    }
}
