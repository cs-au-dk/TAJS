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

public class TestHostFunctionSources_ArrayPrototype {

    private IAnalysisMonitoring monitor;

    @Before
    public void before() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
        Options.get().enableLoopUnrolling(100);
        Options.get().enablePolyfillMDN();
        monitor = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableCheckerMonitor());
    }

    @Test
    public void forEach_exists() {
        testFunctionExistence("forEach");
    }

    @Test
    public void indexOf_exists() {
        testFunctionExistence("indexOf");
    }

    @Test
    public void lastIndexOf_exists() {
        testFunctionExistence("lastIndexOf");
    }

    @Test
    public void some_exists() {
        testFunctionExistence("some");
    }

    @Test
    public void every_exists() {
        testFunctionExistence("every");
    }

    @Test
    public void map_exists() {
        testFunctionExistence("map");
    }

    @Test
    public void reduce_exists() {
        testFunctionExistence("reduce");
    }

    @Test
    public void reduceRight_exists() {
        testFunctionExistence("reduceRight");
    }

    @Test
    public void filter_exists() {
        testFunctionExistence("filter");
    }

    @Test
    public void isArray_exits() {
        Misc.runSource(new String[]{
                "TAJS_assert(Array.isArray !== undefined)",
                "TAJS_assert(typeof Array.isArray === 'function');"
        }, monitor);
    }

    @Test
    public void forEach_callsCallback() {
        Misc.runSource(new String[]{
                "var x = 'a';",
                "['b'].forEach(function(){x = 'c'});",
                "TAJS_assert(x === 'c');"
        }, monitor);
    }

    @Test
    public void forEach_callsCallbackWithArgument() {
        Misc.runSource(new String[]{
                "var x = 'a';",
                "['b'].forEach(function(e){x = e});",
                "TAJS_assert(x === 'b');"
        }, monitor);
    }

    @Test
    public void forEach_callsCallbackInOrder() {
        Misc.runSource(new String[]{
                "var x = 'a';",
                "['b', 'c'].forEach(function(e){x = e;});",
                "TAJS_assert(x === 'c');"
        }, monitor);
    }

    @Test
    public void forEach_callsCallbackWithCustomThisArg() {
        Misc.runSource(new String[]{
                "var x = 'a';",
                "['b'].forEach(function(){x = this}, 'c');",
                "TAJS_assert(x == 'c');"
        }, monitor);
    }

    public void testFunctionExistence(String functionName) {
        Misc.runSource(new String[]{
                "TAJS_assert([]." + functionName + " === Array.prototype." + functionName + ")",
                "TAJS_assert([]." + functionName + " !== undefined)",
                "TAJS_assert(typeof []." + functionName + " === 'function');"
        }, monitor);
    }
}
