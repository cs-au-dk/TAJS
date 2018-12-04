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
import org.junit.Test;

public class TestJQueryLoad { // TODO: code contains "dk.brics.tajs.directives.unreachable", currently analyzing without "-ignore-unreachable" (same for TestJQueryUse and TestJQueryUse_unanalyzable)

    private IAnalysisMonitoring monitor;

    private AnalysisTimeLimiter analysisLimiter;

    @Test
    public void load_1_0() {
        Misc.run("benchmarks/tajs/src/jquery-load/jquery-1.0.js-sliced.js", monitor);
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_1() {
        Misc.run("benchmarks/tajs/src/jquery-load/jquery-1.1.js-sliced.js", monitor);
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_2() {
        Misc.run("benchmarks/tajs/src/jquery-load/jquery-1.2.js-sliced.js", monitor);
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_3() {
        Misc.run("benchmarks/tajs/src/jquery-load/jquery-1.3.js-sliced.js", monitor);
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_4() {
        Misc.run("benchmarks/tajs/src/jquery-load/jquery-1.4.js-sliced.js", monitor);
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_5() {
        Misc.run("benchmarks/tajs/src/jquery-load/jquery-1.5.js-sliced.js", monitor);
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_6() {
        Misc.run("benchmarks/tajs/src/jquery-load/jquery-1.6.js-sliced.js", monitor);
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_7() {
        Misc.run("benchmarks/tajs/src/jquery-load/jquery-1.7.js-sliced.js", monitor);
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_8() {
        Misc.run("benchmarks/tajs/src/jquery-load/jquery-1.8.js-sliced.js", monitor);
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_9() {
        Misc.run("benchmarks/tajs/src/jquery-load/jquery-1.9.js-sliced.js", monitor);
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_10() {
        Misc.run("benchmarks/tajs/src/jquery-load/jquery-1.10.js-sliced.js", monitor);
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_11() {
        Misc.run("benchmarks/tajs/src/jquery-load/jquery-1.11.0.js-sliced.js", monitor);
        Misc.checkSystemOutput();
    }

    @Before
    public void before() {
        Main.reset();
        Options.get().enableDeterminacy();
        Options.get().enableIncludeDom();
        Options.get().enableUnevalizer();
        Options.get().enableTest();
//        Options.get().getSoundnessTesterOptions().setTest(false);
        Options.get().enableNoStrict(); // soundness testing does not work properly with strict mode
        Options.get().getUnsoundness().setUseFixedRandom(true);
        Options.get().getUnsoundness().setShowUnsoundnessUsage(true);
        // Each tests runs in less than 30 seconds on `Intel(R) Core(TM) i7-3520M CPU @ 2.90GHz`
        analysisLimiter = new AnalysisTimeLimiter(1 * 90);
        monitor = CompositeMonitoring.buildFromList(Monitoring.make(), analysisLimiter);
    }

    @After
    public void after() {
        if (!analysisLimiter.analysisNotExceededLimit()) {
            Assert.fail("Analysis exceeded time limit");
        }
    }
}
