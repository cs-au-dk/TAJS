package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.AnalysisTimeLimiter;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for different usages of jQuery.
 * Tests are from the OOPSLA 2014 benchmarks.
 * <p>
 * Each analysis run in this file is supposed to throw an exception, indicating timeout/lack of precision.
 * The test only succeds if an exception is thrown.
 * <p>
 * See {@link TestJQueryUse} for tests that we are able to analyze successfully.
 * <p>
 * If a test in this file makes the analysis terminate successfully, it should be moved to {@link TestJQueryUse}.
 */
public class TestJQueryUse_unanalyzable {

    @BeforeClass
    public static void beforeClass() {
        Main.initLogging();
    }

    @Test
    public void auto_ajax_loadWithCallback() {
        test("ajax_loadWithCallback");
    }

    @Test
    public void auto_events_bindUnbindBind2() {
        test("events_bindUnbindBind2");
    }

    @Test
    public void auto_fading_fadeTimed() {
        test("fading_fadeTimed");
    }

    @Test
    public void auto_ajax_get() {
        test("ajax_get");
    }

    @Test
    public void auto_ajax_jsonp() {
        test("ajax_jsonp");
    }

    @Test
    public void auto_ajax_load() {
        test("ajax_load");
    }

    @Test
    public void auto_ajax_load2() {
        test("ajax_load2");
    }

    @Test
    public void auto_animate_callback() {
        test("animate_callback");
    }

    @Test
    public void auto_animate_queue() {
        test("animate_queue");
    }

    @Test
    public void auto_animate_simpleSpeed() {
        test("animate_simpleSpeed");
    }

    @Test
    public void auto_dom_get() {
        test("dom_get");
    }

    @Test
    public void auto_dom_getAttribute() {
        test("dom_getAttribute");
    }

    @Test
    public void auto_dom_setAttribute() {
        test("dom_setAttribute");
    }

    @Test
    public void auto_dom_setMultipleCssAttributes() {
        test("dom_setMultipleCssAttributes");
    }

    @Test
    public void auto_dom_setMultipleAttributes() {
        test("dom_setMultipleAttributes");
    }

    @Test
    public void auto_events_unbind() {
        test("events_unbind");
    }

    @Test
    public void auto_events_bind() {
        test("events_bind");
    }

    @Test
    public void auto_events_bind2() {
        test("events_bind2");
    }

    @Test
    public void auto_events_bind3() {
        test("events_bind3");
    }

    @Test
    public void auto_events_bindData() {
        test("events_bindData");
    }

    @Test
    public void auto_events_live() {
        test("events_live");
    }

    @Test
    public void auto_methodChaining_divTest1() {
        test("methodChaining_divTest1");
    }

    @Test
    public void auto_methodChaining_divTest2() {
        test("methodChaining_divTest2");
    }

    @Test
    public void auto_noConflict_fullName() {
        test("noConflict_fullName");
    }

    @Test
    public void auto_noConflict_ready() {
        test("noConflict_ready");
    }

    @Test
    public void auto_noConflict_rename() {
        test("noConflict_rename");
    }

    @Test
    public void auto_ready_divTest1() {
        test("ready_divTest1");
    }

    @Test
    public void auto_ready_divTest2() {
        test("ready_divTest2");
    }

    @Test
    public void auto_selectors_attributeValues() {
        test("selectors_attributeValues");
    }

    @Test
    public void auto_selectors_attributes() {
        test("selectors_attributes");
    }

    @Test
    public void auto_selectors_child() {
        test("selectors_child");
    }

    @Test
    public void auto_selectors_class() {
        test("selectors_class");
    }

    @Test
    public void auto_divTest1() {
        test("divTest1");
    }

    @Test
    public void auto_selectors_descendant() {
        test("selectors_descendant");
    }

    @Test
    public void auto_sliding_downUp() {
        test("sliding_downUp");
    }

    @Test
    public void auto_selectors_id() {
        test("selectors_id");
    }

    @Test
    public void auto_ready_divTest3() {
        test("ready_divTest3");
    }

    @Test
    public void auto_events_live2() {
        test("events_live2");
    }

    @Test
    public void auto_events_bindUnbindBind() {
        test("events_bindUnbindBind");
    }

    @Test
    public void auto_dom_getSet() {
        test("dom_getSet");
    }

    @Test
    public void auto_dom_set() {
        test("dom_set");
    }

    @Test
    public void auto_dom_getSetAttribute() {
        test("dom_getSetAttribute");
    }

    @Test
    public void auto_animate_simple() {
        test("animate_simple");
    }

    @Test
    public void auto_ajax_post() {
        test("ajax_post");
    }

    private void test(String testCase) {
        Main.reset();
        Options.get().enableDeterminacy();
        Options.get().enableIncludeDom();
        Options.get().enableUnevalizer();
        Options.get().enableTest();

        // Successfull tests runs in less than 1 minute on `Intel(R) Core(TM) i7-3520M CPU @ 2.90GHz`
        AnalysisTimeLimiter analysisLimiter = new AnalysisTimeLimiter(90);

        IAnalysisMonitoring monitor = null;
        boolean showLineAnalysis = false; // for debugging
        if (showLineAnalysis) {
            // NB: requires TAJS-meta to be on the classpath
            // monitor = new CompositeMonitoring(analysisLimiter, MonitorFactory.createLineAnalysisMonitor());
        } else {
            monitor = CompositeMonitoring.buildFromList(new Monitoring(), analysisLimiter);
        }

        try {
            Misc.run(new String[]{"test/jquery-use-autogenerated/" + testCase + ".snippet_in_plain.htm.dir/index.htm"}, monitor);

            if (!analysisLimiter.analysisExceededTimeLimit()) {
                Assert.fail("Analysis did not exceed time limit (congratulations, you may have improved precision. Consider moving this test).");
            }
        } catch (AnalysisLimitationException e) {
            // ignore - we expect to encounter this exception or a timeout
        }
    }
}
