package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.AnalysisTimeLimiter;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestJQueryLoad { // TODO: code contains "dk.brics.tajs.directives.unreachable" and "TAJS_" strings (for nondet. values), currently analyzing without "-ignore-unreachable" (same for TestJQueryUse and TestJQueryUse_unanalyzable)

    private IAnalysisMonitoring monitor;

    private AnalysisTimeLimiter analysisLimiter;

    @BeforeClass
    public static void beforeClass() {
        Main.initLogging();
    }

    @Test
    public void load_1_0() {
        Misc.run("test/jquery-load/jquery-1.0.js-sliced.js", monitor);
    }

    @Test
    public void load_1_1() {
        Misc.run("test/jquery-load/jquery-1.1.js-sliced.js", monitor);
    }

    @Test
    public void load_1_2() {
        Misc.run("test/jquery-load/jquery-1.2.js-sliced.js", monitor);
    }

    @Test
    public void load_1_3() {
        Misc.run("test/jquery-load/jquery-1.3.js-sliced.js", monitor);
    }

    @Test
    public void load_1_4() {
        Misc.run("test/jquery-load/jquery-1.4.js-sliced.js", monitor);
    }

    @Test
    public void load_1_5() {
        Misc.run("test/jquery-load/jquery-1.5.js-sliced.js", monitor);
    }

    @Test
    public void load_1_6() {
        Misc.run("test/jquery-load/jquery-1.6.js-sliced.js", monitor);
    }

    @Test
    public void load_1_7() {
        Misc.run("test/jquery-load/jquery-1.7.js-sliced.js", monitor);
    }

    @Test
    public void load_1_8() {
        Misc.run("test/jquery-load/jquery-1.8.js-sliced.js", monitor);
    }

    @Test
    public void load_1_9() {
        Misc.run("test/jquery-load/jquery-1.9.js-sliced.js", monitor);
    }

    @Test
    public void load_1_10() {
        Misc.run("test/jquery-load/jquery-1.10.js-sliced.js", monitor);
    }

    @Test
    public void load_1_11() {
        Misc.run("test/jquery-load/jquery-1.11.0.js-sliced.js", monitor);
    }

    @Before
    public void before() {
        Main.reset();
        Options.get().enableDeterminacy();
        Options.get().enableIncludeDom();
        Options.get().enableUnevalizer();
        Options.get().enableTest();

        // Each tests runs in less than 30 seconds on `Intel(R) Core(TM) i7-3520M CPU @ 2.90GHz`
        analysisLimiter = new AnalysisTimeLimiter(1 * 90);

        boolean showLineAnalysis = false; // for debugging
        if (showLineAnalysis) {
            // NB: requires TAJS-meta to be on the classpath
            // monitor = new CompositeMonitoring(analysisLimiter, MonitorFactory.createLineAnalysisMonitor());
        } else {
            monitor = CompositeMonitoring.buildFromList(new Monitoring(), analysisLimiter);
        }
    }

    @After
    public void after() {
        if (analysisLimiter.analysisExceededTimeLimit()) {
            Assert.fail("Analysis exceeded time limit");
        }
    }
}
