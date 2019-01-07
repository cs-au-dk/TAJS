package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

public class TestHostFunctionSources_TypedArrays {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enablePolyfillTypedArrays();
    }

    @Test
    public void ArrayBuffer() {
        Misc.runSource(
                "var buffer = new ArrayBuffer(16);",
                "TAJS_assertEquals('object', typeof buffer);",
                "TAJS_assert(buffer.byteLength, 'isMaybeNumUInt');");
    }

    @Test
    public void DataView() {
        Misc.runSource(
                "var buffer = new ArrayBuffer(16);",
                "var view = new DataView(buffer);",
                "TAJS_assertEquals('function', typeof view.getInt8);");
    }

    @Test
    public void Int8Array_basics() {
        arrayBasics("Int8Array");
    }

    @Test
    public void Uint8Array_basics() {
        arrayBasics("Uint8Array");
    }

    @Test
    public void Uint8ClampedArray_basics() {
        arrayBasics("Uint8ClampedArray");
    }

    @Test
    public void Int16Array_basics() {
        arrayBasics("Int16Array");
    }

    @Test
    public void Uint16Array_basics() {
        arrayBasics("Uint16Array");
    }

    @Test
    public void Int32Array_basics() {
        arrayBasics("Int32Array");
    }

    @Test
    public void UInt32Array_basics() {
        arrayBasics("Uint32Array");
    }

    @Test
    public void Float32Array_basics() {
        arrayBasics("Float32Array");
    }

    @Test
    public void Float64Array_basics() {
        arrayBasics("Float64Array");
    }

    private void arrayBasics(String constructor) {
        Misc.runSource(
                "var buffer = new ArrayBuffer(200);",
                "TAJS_assert(" + constructor + " !== undefined)",
                "TAJS_assert(typeof " + constructor + " === 'function');",
                "var arr = new " + constructor + "(buffer);",
                "TAJS_assert(arr, 'isMaybeObject');",
                "",
                "TAJS_assertEquals('object', typeof arr.__proto__);",
                "TAJS_assertEquals('object', typeof arr.__proto__.__proto__);",
                "TAJS_assertEquals('object', typeof arr.__proto__.__proto__.__proto__);",
                "TAJS_assertEquals(null, arr.__proto__.__proto__.__proto__.__proto__);",
                "",
                "arr[2] = 2;",
                "arr[10] = 10;",
                "TAJS_assert(arr[0], 'isMaybeAnyNumNotNaNInf || isMaybeUndef');",
                "TAJS_assert(arr[100], 'isMaybeAnyNumNotNaNInf || isMaybeUndef');",
                "",
                "arr.set([]);",
                "",
                "arr.copyWithin();",
                "",
                "TAJS_assert(arr.every(function(v){return v === 0;}), 'isMaybeAnyBool');",
                "",
                "arr.fill()",
                "",
                "TAJS_assert(arr.filter(function(v){return v === 0;})[100], 'isMaybeAnyNumNotNaNInf || isMaybeUndef')",
                "",
                "TAJS_assert(arr.find(function(v){return v === 0;}), 'isMaybeAnyNumNotNaNInf || isMaybeUndef')",
                "",
                "TAJS_assert(arr.findIndex(function(v){return v === 0;}), 'isMaybeAnyNumNotNaNInf');",
                "",
                "arr.forEach(function(){});",
                "",
                "TAJS_assert(arr.indexOf(100), 'isMaybeAnyNumNotNaNInf');",
                "",
                "TAJS_assert(arr.lastIndexOf(100), 'isMaybeAnyNum || isMaybeUndef');",
                "",
                "TAJS_assert(arr.join(', '), 'isMaybeAnyStr');",
                "",
                "TAJS_assert(arr.map(function(){return true/*preserving typedness!*/;})[100], 'isMaybeAnyNumNotNaNInf || isMaybeUndef')",
                "",
                "TAJS_assert(arr.reduce(function(){return true/*preserving typedness!*/;}, true)[100], 'isMaybeAnyNumNotNaNInf || isMaybeUndef')",
                "",
                "TAJS_assert(arr.reduceRight(function(){return true/*preserving typedness!*/;}, true)[100], 'isMaybeAnyNumNotNaNInf || isMaybeUndef')",
                "",
                "TAJS_assert(arr.reverse()[100], 'isMaybeAnyNumNotNaNInf || isMaybeUndef')",
                "",
                "TAJS_assert(arr.slice(0, 10)[100], 'isMaybeAnyNumNotNaNInf || isMaybeUndef')",
                "",
                "TAJS_assert(arr.some(function(v){return v === 0;}), 'isMaybeAnyBool');",
                "",
                "TAJS_assert(arr.sort(function(v){return v;})[100], 'isMaybeAnyNumNotNaNInf || isMaybeUndef')",
                "",
                "TAJS_assert(arr.subarray(5, 20)[100], 'isMaybeAnyNumNotNaNInf || isMaybeUndef')");
    }
}
