package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import dk.brics.tajs.util.AnalysisResultException;
import org.junit.Before;
import org.junit.Test;

public class JSObject_defineProperty_test {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void callable() {
        Misc.runSource(
                "var o = {};",
                "Object.defineProperty(o, 'p', {});");
    }

    @Test
    public void data() {
        Misc.runSource(
                "var o = {};",
                "Object.defineProperty(o, 'p', {value: 42});");
    }

    @Test
    public void accessor() {
        Misc.runSource(
                "var o = {};",
                "Object.defineProperty(o, 'p', {get: function(){ return 42; }});");
    }

    @Test
    public void dataAndAccessor() {
        Misc.runSource(
                // Should throw definite TypeError:
                // "TypeError:Invalid property.A property cannot both have accessors and be writable or have a value"
                "var o = {};",
                "try{",
                "   Object.defineProperty(o, 'p', {value: 42, get: function(){return 42;}});",
                "   TAJS_assert(false);",
                "}catch(e){}");
    }

    @Test
    public void value() {
        Misc.runSource(
                "var o1 = {};",
                "Object.defineProperty(o1, 'p', {value: 42});",
                "TAJS_assert(o1.p === 42);",
                // default undefined
                "var o2 = {};",
                "Object.defineProperty(o2, 'p', {});",
                "TAJS_assert(o2.hasOwnProperty('p'));",
                "TAJS_assert(o2.p === undefined);");
    }

    @Test
    public void enumerable() {
        Misc.runSource(
                "var o1 = {};",
                "Object.defineProperty(o1, 'p', {enumerable: true});",
                "TAJS_assert(Object.keys(o1).length === 1);",
                "var o2 = {};",
                "Object.defineProperty(o2, 'p', {enumerable: false});",
                "TAJS_assert(Object.keys(o2).length === 0);",
                // default false
                "var o3 = {};",
                "Object.defineProperty(o3, 'p', {enumerable: false});",
                "TAJS_assert(Object.keys(o3).length === 0);");
    }

    @Test
    public void writable() {
        Misc.runSource(
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
                "TAJS_assert(o3.p === undefined);");
    }

    @Test
    public void configurable1() {
        Misc.runSource("var o1 = {};",
                "Object.defineProperty(o1, 'p', {configurable: true, writable: true});",
                "o1.p = 42;",
                "TAJS_assert(o1.p === 42);",
                "delete o1.p",
                "TAJS_assert(o1.hasOwnProperty('p') === false);");
    }

    @Test
    public void configurable2() {
        Misc.runSource("var o2 = {};",
                "Object.defineProperty(o2, 'p', {configurable: false, writable: true});",
                "o2.p = 42;",
                "TAJS_assert(o2.p === 42);",
                "delete o2.p",
                "TAJS_assert(o2.p === 42);");
    }

    @Test
    public void configurable3() {
        Misc.runSource(// default false
                "var o3 = {};",
                "Object.defineProperty(o3, 'p', {configurable: false, writable: true});",
                "o3.p = 42;",
                "TAJS_assert(o3.p === 42);",
                "delete o3.p",
                "TAJS_assert(o3.p === 42);");
    }

    @Test(expected = AnalysisResultException.class) // TODO: GitHub #291
    public void configurable4() {
        Misc.runSource(
                "var o4 = {};",
                "Object.defineProperty(o4, 'p', {configurable: false});",
                "var threwException = false;",
                "try{",
                "   Object.defineProperty(o4, 'p', {value: 42});", // different
                "   TAJS_assert(false);",
                "}catch(e){",
                "   threwException = true;",
                "}",
                "TAJS_assert(threwException);");
    }

    @Test
    public void nonConfigurable1() {
        Misc.runSource(
                "var o = {};",
                "Object.defineProperty(o, 'p', {configurable: false});",
                // OK to redefine with same attributes
                "Object.defineProperty(o, 'p', {configurable: false});",
                "Object.defineProperty(o, 'p', {writable: false});",
                // NOT OK to redefine with other attributes
                "Object.defineProperty(o, 'p', {writable: true});"
                // (fails soundness testing) // TODO: GitHub #291
        );
    }

    @Test
    public void nonConfigurable2() {
        Misc.runSource(
                "var o = {};",
                "Object.defineProperty(o, 'p', {configurable: false});",
                // OK to delete
                "delete o.p;"
        );
    }

    @Test
    public void configurable5() {
        Misc.runSource("var o5 = {};",
                "Object.defineProperty(o5, 'p', {configurable: false});",
                "try{",
                "   Object.defineProperty(o5, 'p', {configurable: false});", // same
                "}catch(e){",
                "   TAJS_assert(false);",
                "}");
    }

    @Test
    public void getter() {
        Misc.runSource(
                "var o = {};",
                "Object.defineProperty(o, 'p', {get: function(){return 42;}});",
                "TAJS_assert(o.p === 42);");
    }

    @Test
    public void setter() {
        Misc.runSource(
                "var o = {};",
                "var x;",
                "Object.defineProperty(o, 'p', {set: function(v){x = v;}});",
                "o.p = 42;",
                "TAJS_assert(x === 42);");
    }

    @Test
    public void getter_setter() {
        Misc.runSource(
                "var o = {};",
                "var x;",
                "Object.defineProperty(o, 'p', {get: function(){return 42;}, set: function(v){x = v;}});",
                "o.p = 87;",
                "TAJS_assertEquals(87, x);",
                "TAJS_assertEquals(42, o.p);");
    }

    @Test
    public void setter_getter() {
        Misc.runSource(
                "var o = {};",
                "var x;",
                "Object.defineProperty(o, 'p', {set: function(v){x = v;}, get: function(){return 42;}});",
                "o.p = 87;",
                "TAJS_assertEquals(87, x);",
                "TAJS_assertEquals(42, o.p);");
    }
}
