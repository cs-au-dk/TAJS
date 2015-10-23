package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.StaticDeterminacyContextSensitivityStrategy;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.ExperimentalOptions;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.monitors.OrdinaryExitReachableCheckerMonitor;
import org.junit.Before;
import org.junit.Test;

/**
 * jQuery code patterns, carefully extracted from the jQuery library to provide small test cases. The analysis should confirm that all assert-statements are true
 */
public class TestJQueryPatterns {

    private IAnalysisMonitoring monitor;

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableIncludeDom();
        Options.get().enableContextSensitiveHeap();
        Options.get().enableParameterSensitivity();

        ExperimentalOptions.ExperimentalOptionsManager.set(new ExperimentalOptions(StaticDeterminacyContextSensitivityStrategy.StaticDeterminacyOptions.OOPSLA2014));

        Options.get().enableLoopUnrolling(100);

        monitor = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableCheckerMonitor());
    }

    @Test
    public void jQuery111_each() {
        Misc.init();
        Misc.run(new String[]{"test/jquery-patterns/jquery-1.11_each.js"}, monitor);
    }

    @Test
    public void jQuery111_each_arrayIterationClosure() {
        Misc.init();
        Misc.run(new String[]{"test/jquery-patterns/jquery-1.11_each.js", "test/jquery-patterns/jQuery-1.11_each_arrayIterationClosure.js"}, monitor);
    }

    @Test
    public void jQuery111_each_objectIterationClosure() {
        Misc.init();
        Misc.run(new String[]{"test/jquery-patterns/jquery-1.11_each.js", "test/jquery-patterns/jquery-1.11_each_objectIterationClosure.js"}, monitor);
    }

    @Test
    public void jQuery111_each_nestedObjectCreationIterationClosure() {
        Misc.init();
        Misc.run(new String[]{"test/jquery-patterns/jquery-1.11_each.js", "test/jquery-patterns/jquery-1.11_each_nestedObjectCreationIterationClosure.js"}, monitor);
    }

    @Test
    public void jQuery111_each_jQuery17_arrayMutation() {
        Misc.init();
        Misc.run(new String[]{"test/jquery-patterns/jquery-1.11_each.js", "test/jquery-patterns/jquery-1.7_each_arrayThisIteration.js"}, monitor);
    }

    @Test
    public void jQuery111_extend_selfExtend() {
        Misc.init();
        Misc.run(new String[]{"test/jquery-patterns/jquery-1.11_extend.js", "test/jquery-patterns/jquery-1.11_extend_selfExtend.js"}, monitor);
    }

    @Test
    public void jQuery111_extend_deepExtend() {
        Misc.init();
        Misc.run(new String[]{"test/jquery-patterns/jquery-1.11_extend.js", "test/jquery-patterns/jquery-1.11_extend_deepExtend.js"}, monitor);
    }

    @Test
    public void jQuery11_each() {
        Misc.init();
        Misc.run(new String[]{"test/jquery-patterns/jquery-1.1_each.js"}, monitor);
    }

    /**
     * This is not really extracted from jquery -- but it is a small example of precision that other tests need
     */
    @Test
    public void heap_and_call_sensitivity() {
        Misc.init();
        Misc.run(new String[]{"test/jquery-patterns/heap-and-call-sensitive-closure.js"}, monitor);
    }
}
