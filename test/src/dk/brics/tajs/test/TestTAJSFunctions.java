package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.monitoring.OrdinaryExitReachableChecker;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import org.junit.Before;
import org.junit.Test;

public class TestTAJSFunctions {

    @Before
    public void init() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void asyncEvents2() {
        Misc.init();
        Options.get().enableAsyncEvents();
        Misc.captureSystemOutput();
        Misc.runSource(
                "function f(){TAJS_dumpValue('executed');}",
                "TAJS_asyncListen(f);",
                "");
        Misc.checkSystemOutput();
    }

    @Test
    public void tajsMake() throws Exception {
        Misc.runSource(new String[]{
                        "TAJS_assert(TAJS_make('AnyBool'), 'isMaybeAnyBool');",
                        "TAJS_assert(TAJS_make('AnyBool'), 'isMaybeAnyStr', false);",
                        "TAJS_assert(TAJS_make('AnyStr'), 'isMaybeAnyStr');",
                        "TAJS_assert(TAJS_make('AnyStr'), 'isMaybeAnyBool', false);",
                },
                new CompositeMonitoring(new Monitoring(), new OrdinaryExitReachableChecker()));
    }

    @Test(expected = AnalysisException.class)
    public void tajsMake_fail() throws Exception {
        Misc.runSource(new String[]{"TAJS_make('x');"});
    }

    @Test
    public void tajsJoin() throws Exception {
        Misc.runSource(new String[]{
                        "TAJS_assert(TAJS_join(true, false), 'isMaybeAnyBool');",
                        "TAJS_assert(TAJS_join(true, 'foo'), 'isMaybeAnyBool', false);",
                },
                new CompositeMonitoring(new Monitoring(), new OrdinaryExitReachableChecker()));
    }

    @Test
    public void tajsAssertEquals() throws Exception {
        Misc.runSource(new String[]{
                        "TAJS_assertEquals(true, true);",
                        "TAJS_assertEquals(TAJS_make('AnyBool'), TAJS_make('AnyBool'), true);",
                        "TAJS_assertEquals(true, false, false);",
                        "TAJS_assertEquals(TAJS_make('AnyBool'), false, false);",
                        "TAJS_assertEquals(false, true, false);",
                        "TAJS_assertEquals(false, TAJS_make('AnyBool'), false);"
                },
                new CompositeMonitoring(new Monitoring(), new OrdinaryExitReachableChecker()));
    }

    @Test(expected = AnalysisException.class)
    public void tajsAssertEqualsFail1() throws Exception {
        Misc.runSource("TAJS_assertEquals();");
    }

    @Test(expected = AnalysisException.class)
    public void tajsAssertEqualsFail2() throws Exception {
        Misc.runSource("TAJS_assertEquals(true, true, true, true);");
    }

    @Test(expected = AnalysisException.class)
    public void tajsAssertEqualsFail3() throws Exception {
        Misc.runSource("TAJS_assertEquals(true, true, TAJS_make('AnyBool'));");
    }

    @Test(expected = AnalysisException.class)
    public void tajsAssertEqualsFail4() throws Exception {
        Misc.runSource("TAJS_assertEquals(true, true, 'foo');");
    }
}
