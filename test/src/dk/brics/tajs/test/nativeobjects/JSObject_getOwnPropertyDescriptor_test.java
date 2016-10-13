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

public class JSObject_getOwnPropertyDescriptor_test {

    private IAnalysisMonitoring monitor;

    @Before
    public void before() {
        Main.reset();
        Main.initLogging();
        Options.get().enableTest();
        monitor = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableCheckerMonitor());
    }

    @Test
    public void propertyPresence() {
        Misc.runSource(new String[]{
                "var o = {p: 42};",
                "var v1 = Object.getOwnPropertyDescriptor(o, 'p');",
                "var v2 = Object.getOwnPropertyDescriptor(o, 'q');",
                "var v3 = Object.getOwnPropertyDescriptor(o, 'toString');",
                "TAJS_assert(42 === v1.value);",
                "TAJS_assert(undefined === v2);",
                "TAJS_assert(undefined === v3);"
        }, monitor);
    }

    @Test
    public void propertyAttributes_plain() {
        Misc.runSource(new String[]{
                "var o = {p: 42};",
                "var v = Object.getOwnPropertyDescriptor(o, 'p');",
                "TAJS_assert(42 === v.value);",
                "TAJS_assert(undefined === v.get);",
                "TAJS_assert(undefined === v.set);",
                "TAJS_assert(true === v.configurable);",
                "TAJS_assert(true === v.writable);",
                "TAJS_assert(true === v.enumerable);"
        }, monitor);
    }

    @Test
    public void propertyAttributes_builtin() {
        Misc.runSource(new String[]{
                "var v = Object.getOwnPropertyDescriptor(Object.prototype, 'toString');",
                "TAJS_assert(Object.prototype.toString === v.value);",
                "TAJS_assert(undefined === v.get);",
                "TAJS_assert(undefined === v.set);",
                "TAJS_assert(true === v.configurable);",
                "TAJS_assert(true === v.writable);",
                "TAJS_assert(false === v.enumerable);"
        }, monitor);
    }

    @Test
    public void propertyAttributes_getter() {
        Misc.runSource(new String[]{
                "var o = {};",
                "o.__defineGetter__('p', function(){});",
                "var v = Object.getOwnPropertyDescriptor(o, 'p');",
                "TAJS_assert(undefined === v.value);",
                "TAJS_assert(v.get, 'isMaybeObject');",
                "TAJS_assert(undefined === v.set);",
                "TAJS_assert(true === v.configurable);",
                "TAJS_assert(undefined === v.writable);",
                "TAJS_assert(true === v.enumerable);"
        }, monitor);
    }

    @Test
    public void propertyAttributes_setter() {
        Misc.runSource(new String[]{
                "var o = {};",
                "o.__defineSetter__('p', function(){});",
                "var v = Object.getOwnPropertyDescriptor(o, 'p');",
                "TAJS_assert(undefined === v.value);",
                "TAJS_assert(undefined === v.get);",
                "TAJS_assert(v.set, 'isMaybeObject');",
                "TAJS_assert(true === v.configurable);",
                "TAJS_assert(undefined === v.writable);",
                "TAJS_assert(true === v.enumerable);"
        }, monitor);
    }

    @Test
    public void propertyAttributes_literalSetter() {
        Misc.runSource(new String[]{
                "var o = {set p (x){}};",
                "var v = Object.getOwnPropertyDescriptor(o, 'p');",
                "TAJS_assert(undefined === v.value);",
                "TAJS_assert(undefined === v.get);",
                "TAJS_assert(v.set, 'isMaybeObject');",
                "TAJS_assert(true === v.configurable);",
                "TAJS_assert(undefined === v.writable);",
                "TAJS_assert(true === v.enumerable);"
        }, monitor);
    }

    @Test
    public void propertyAttributes_literalGetter() {
        Misc.runSource(new String[]{
                "var o = {get p (){}};",
                "var v = Object.getOwnPropertyDescriptor(o, 'p');",
                "TAJS_assert(undefined === v.value);",
                "TAJS_assert(v.get, 'isMaybeObject');",
                "TAJS_assert(undefined === v.set);",
                "TAJS_assert(true === v.configurable);",
                "TAJS_assert(undefined === v.writable);",
                "TAJS_assert(true === v.enumerable);"
        }, monitor);
    }

    @Test
    public void getDefineGet_plain() {
        Misc.runSource(new String[]{
                "var o1 = {p: 42};",
                "var v1 = Object.getOwnPropertyDescriptor(o1, 'p');",
                "var o2 = {};",
                "Object.defineProperty(o2, 'p', v1);",
                "var v2 = Object.getOwnPropertyDescriptor(o2, 'p');",
                "TAJS_assert(42 === v2.value);",
                "TAJS_assert(undefined === v2.get);",
                "TAJS_assert(undefined === v2.set);",
                "TAJS_assert(true === v2.configurable);",
                "TAJS_assert(true === v2.writable);",
                "TAJS_assert(true === v2.enumerable);"
        }, monitor);
    }

    @Test
    public void getDefineGet_builtin() {
        Misc.runSource(new String[]{
                "var v1 = Object.getOwnPropertyDescriptor(Object.prototype, 'toString');",
                "var o2 = {};",
                "Object.defineProperty(o2, 'p', v1);",
                "var v2 = Object.getOwnPropertyDescriptor(o2, 'p');",
                "TAJS_assert(Object.prototype.toString === v2.value);",
                "TAJS_assert(undefined === v2.get);",
                "TAJS_assert(undefined === v2.set);",
                "TAJS_assert(true === v2.configurable);",
                "TAJS_assert(true === v2.writable);",
                "TAJS_assert(false == v2.enumerable);"
        }, monitor);
    }

    @Test
    public void getDefineGet_getter() {
        Misc.runSource(new String[]{
                "var o1 = {};",
                "o1.__defineGetter__('p', function(){});",
                "var v1 = Object.getOwnPropertyDescriptor(o1, 'p');",
                "var o2 = {};",
                "Object.defineProperty(o2, 'p', v1);",
                "var v2 = Object.getOwnPropertyDescriptor(o2, 'p');",
                "TAJS_assert(undefined === v2.value);",
                "TAJS_assert(v2.get, 'isMaybeObject');",
                "TAJS_assert(undefined === v2.set);",
                "TAJS_assert(true === v2.configurable);",
                "TAJS_assert(undefined === v2.writable);",
                "TAJS_assert(true === v2.enumerable);"
        }, monitor);
    }

    @Test
    public void getDefineGet_setter() {
        Misc.runSource(new String[]{
                "var o1 = {};",
                "o1.__defineSetter__('p', function(){});",
                "var v1 = Object.getOwnPropertyDescriptor(o1, 'p');",
                "var o2 = {};",
                "Object.defineProperty(o2, 'p', v1);",
                "var v2 = Object.getOwnPropertyDescriptor(o2, 'p');",
                "TAJS_assert(undefined === v2.value);",
                "TAJS_assert(undefined === v2.get);",
                "TAJS_assert(v2.set, 'isMaybeObject');",
                "TAJS_assert(true === v2.configurable);",
                "TAJS_assert(undefined === v2.writable);",
                "TAJS_assert(true === v2.enumerable);"
        }, monitor);
    }
}
