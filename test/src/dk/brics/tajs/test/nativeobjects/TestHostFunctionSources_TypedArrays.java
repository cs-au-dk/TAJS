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

public class TestHostFunctionSources_TypedArrays {

    private IAnalysisMonitoring monitor;

    @Before
    public void before() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
        Options.get().enablePolyfillTypedArrays();
        monitor = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableCheckerMonitor());
    }

    @Test(expected = AssertionError.class /* Missing support for Object.defineProperty & getters/setters */)
    public void ArrayBuffer() {
        Misc.runSource(new String[]{
                "var buffer = new ArrayBuffer(16);",
                "TAJS_assert(typeof buffer === 'object');",
                "TAJS_assert(buffer.byteLength, 'isMaybeNumUInt');",
                "TAJS_assert(typeof buffer.slice === 'function');",
        }, monitor);
    }

    @Test(expected = AssertionError.class /* Missing support for Object.defineProperty & getters/setters */)
    public void DataView() {
        Misc.runSource(new String[]{
                "var buffer = new ArrayBuffer(16);",
                "var view = new DataView(buffer);",
                "TAJS_assert(typeof view.getInt8 === 'function');",
        }, monitor);
    }

    @Test(expected = AssertionError.class /* Missing support for Object.defineProperty & getters/setters */)
    public void Int8Array_basics() {
        arrayBasics("Int8Array");
    }

    @Test(expected = AssertionError.class /* Missing support for Object.defineProperty & getters/setters */)
    public void Uint8Array_basics() {
        arrayBasics("Uint8Array");
    }

    @Test(expected = AssertionError.class /* Missing support for Object.defineProperty & getters/setters */)
    public void Uint8ClampedArray_basics() {
        arrayBasics("Uint8ClampedArray");
    }

    @Test(expected = AssertionError.class /* Missing support for Object.defineProperty & getters/setters */)
    public void Int16Array_basics() {
        arrayBasics("Int16Array");
    }

    @Test(expected = AssertionError.class /* Missing support for Object.defineProperty & getters/setters */)
    public void Uint16Array_basics() {
        arrayBasics("Uint16Array");
    }

    @Test(expected = AssertionError.class /* Missing support for Object.defineProperty & getters/setters */)
    public void Int32Array_basics() {
        arrayBasics("Int32Array");
    }

    @Test(expected = AssertionError.class /* Missing support for Object.defineProperty & getters/setters */)
    public void UInt32Array_basics() {
        arrayBasics("UInt32Array");
    }

    @Test(expected = AssertionError.class /* Missing support for Object.defineProperty & getters/setters */)
    public void Float32Array_basics() {
        arrayBasics("Float32Array");
    }

    @Test(expected = AssertionError.class /* Missing support for Object.defineProperty & getters/setters */)
    public void Float64Array_basics() {
        arrayBasics("Float64Array");
    }

    public void arrayBasics(String constructor) {
        Misc.runSource(new String[]{
                "var buffer = new ArrayBuffer(16);",
                "TAJS_assert(" + constructor + " !== undefined)",
                "TAJS_assert(typeof " + constructor + " === 'function');",
                "var arr = new " + constructor + "(buffer);",
                "TAJS_assert(arr, 'isMaybeObject');",
                "arr[0] = 0;",
                "arr[1] = 1;",
                "TAJS_assert(arr[0] === 0);",
                "TAJS_assert(arr[1] === 1);",
                "TAJS_assert(arr.findIndex(0), 'isMaybeNumUInt');",
                "TAJS_assert(arr.findIndex(0), 'isMaybeNumUInt');"
        }, monitor);
    }
}
