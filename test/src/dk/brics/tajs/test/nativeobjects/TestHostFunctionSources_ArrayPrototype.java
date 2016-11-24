package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.monitoring.OrdinaryExitReachableChecker;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
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
        Options.get().enableUnsound();
        monitor = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableChecker());
    }

    @Test
    public void createsNonEnumerableProperties() {
        Misc.runSource(new String[]{
                "TAJS_assert(Object.keys([]).length === 0);"
        }, monitor);
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
    public void forEach_callbackIsArgumentSensitive() {
        Misc.runSource(new String[]{
                "function f(e){TAJS_assert(e, 'isMaybeSingleStr');}",
                "['b', 'c'].forEach(f);",
                "['d', 'e'].forEach(f);",
        }, monitor);
    }

    @Test
    public void forEach_callbackIsArgumentForNativeCalls() {
        Misc.runSource(new String[]{
                "['b', 'c'].forEach(function(n){",
                "   Object.defineProperty({}, n, {});",
                "});",
        }, monitor);
    }

    @Test
    public void forEach_callbackIsGuardedArgumentSensitive() {
        Misc.runSource(new String[]{
                "function f(e, x){TAJS_assert(e, 'isMaybeSingleStr', x !== true);}",
                "['b', 'c'].forEach(f);",
                "['d', 'e'].forEach(f);",
                "f('foo', true)",
                "f('bar', true)",
        }, monitor);
    }

    @Test
    public void forEach_isArgumentSensitive() {
        Misc.runSource(new String[]{
                "var x = 0;",
                "function f(e){x++;}",
                "function g(e){x++;}",
                "var arr = ['b', 'c'];",
                "arr.forEach(f);",
                "arr[0] = 'd';",
                "arr[1] = 'e';",
                "arr.forEach(g);",
                "TAJS_assert(x === 4);"
        }, monitor);
    }

    @Test
    public void forEach_isObjectSensitive() {
        Misc.runSource(new String[]{
                "var x = 0;",
                "function f(e){x++;}",
                "['b', 'c'].forEach(f);",
                "['d', 'e'].forEach(f);",
                "TAJS_assert(x === 4);"
        }, monitor);
    }

    @Test
    public void map_isOrdered() {
        Misc.runSource(new String[]{
                "var a = [42, 97].map(function(e){return e + 1;});",
                "TAJS_assert(a[0] === 43);",
                "TAJS_assert(a[1] === 98);"
        }, monitor);
    }

    @Test
    public void map_heapSensitiveResult() {
        Misc.runSource(new String[]{
                "var a = [42, 97].map(function(e){return e + 1;});",
                "var b = [56, 64].map(function(e){return e * -1;});",
                "TAJS_assert(a[0] === 43);",
                "TAJS_assert(a[1] === 98);",
                "TAJS_assert(b[0] === -56);",
                "TAJS_assert(b[1] === -64);"
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
