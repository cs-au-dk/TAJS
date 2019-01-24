package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Test;

/**
 * Tests for different usages of jQuery.
 * Tests are from the OOPSLA 2014 benchmarks.
 */
public class TestJQueryUse {

    @Test(expected = AnalysisLimitationException.AnalysisModelLimitationException.class)
    public void auto_ajax_abort() {
        test("ajax_abort");
    }

    @Test(expected = AnalysisLimitationException.AnalysisModelLimitationException.class)
    public void auto_ajax_abort2() {
        test("ajax_abort2");
    }

    @Test(expected = AnalysisLimitationException.AnalysisModelLimitationException.class)
    public void auto_ajax_progress() {
        test("ajax_progress");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_ajax_progress2() {
        test("ajax_progress2");
    }

    @Test(expected = AnalysisLimitationException.AnalysisModelLimitationException.class)
    public void auto_ajax_progressErrorCallback() {
        test("ajax_progressErrorCallback");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_animate_stop() {
        test("animate_stop");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_animate_stopQueue() {
        test("animate_stopQueue");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_animate_stopQueue2() {
        test("animate_stopQueue2");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dimensions_get() {
        test("dimensions_get");
    }

    @Test
    public void auto_dimensions_getWindow() {
        test("dimensions_getWindow");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dimensions_set() {
        test("dimensions_set");
    }

    @Test
    public void auto_divTest2() {
        test("divTest2");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dom_appendPrepend() {
        test("dom_appendPrepend");
    }

    @Test(expected = AnalysisLimitationException.AnalysisPrecisionLimitationException.class)
    public void auto_dom_appendPrepend2() {
        test("dom_appendPrepend2");
    }

    @Test(expected = AnalysisLimitationException.AnalysisPrecisionLimitationException.class)
    public void auto_dom_appendPrepend3() {
        test("dom_appendPrepend3");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dom_beforeAfter() {
        test("dom_beforeAfter");
    }

    @Test(expected = AnalysisLimitationException.AnalysisPrecisionLimitationException.class)
    public void auto_dom_beforeAfter2() {
        test("dom_beforeAfter2");
    }

    @Test(expected = AnalysisLimitationException.AnalysisPrecisionLimitationException.class)
    public void auto_dom_beforeAfter3() {
        test("dom_beforeAfter3");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dom_removeEmpty() {
        test("dom_removeEmpty");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dom_removeEmpty2() {
        test("dom_removeEmpty2");
    }

    @Test
    public void auto_dom_setCssAttribute() {
        test("dom_setCssAttribute");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_fading_fadeIn() {
        test("fading_fadeIn");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_fading_fadeInOut() {
        test("fading_fadeInOut");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_fading_fadeToggleSpeed() {
        test("fading_fadeToggleSpeed");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_sliding_down() {
        test("sliding_down");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_sliding_downSpeeds() {
        test("sliding_downSpeeds");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_sliding_toggle() {
        test("sliding_toggle");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_ajax_loadWithCallback() {
        test("ajax_loadWithCallback");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_events_bindUnbindBind2() {
        test("events_bindUnbindBind2");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_fading_fadeTimed() {
        test("fading_fadeTimed");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_ajax_get() {
        test("ajax_get");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_ajax_jsonp() {
        test("ajax_jsonp");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_ajax_load() {
        test("ajax_load");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_ajax_load2() {
        test("ajax_load2");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_animate_callback() {
        test("animate_callback");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_animate_queue() {
        test("animate_queue");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_animate_simpleSpeed() {
        test("animate_simpleSpeed");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dom_get() {
        test("dom_get");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dom_getAttribute() {
        test("dom_getAttribute");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dom_setAttribute() {
        test("dom_setAttribute");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dom_setMultipleCssAttributes() {
        test("dom_setMultipleCssAttributes");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dom_setMultipleAttributes() {
        test("dom_setMultipleAttributes");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_events_unbind() {
        test("events_unbind");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_events_bind() {
        test("events_bind");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_events_bind2() {
        test("events_bind2");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_events_bind3() {
        test("events_bind3");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_events_bindData() {
        test("events_bindData");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_events_live() {
        test("events_live");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_methodChaining_divTest1() {
        test("methodChaining_divTest1");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_methodChaining_divTest2() {
        test("methodChaining_divTest2");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_noConflict_fullName() {
        test("noConflict_fullName");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_noConflict_ready() {
        test("noConflict_ready");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_noConflict_rename() {
        test("noConflict_rename");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_ready_divTest1() {
        test("ready_divTest1");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_ready_divTest2() {
        test("ready_divTest2");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_selectors_attributeValues() {
        test("selectors_attributeValues");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_selectors_attributes() {
        test("selectors_attributes");
    }

    @Test
    public void auto_selectors_child() {
        test("selectors_child");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_selectors_class() {
        test("selectors_class");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_divTest1() {
        test("divTest1");
    }

    @Test
    public void auto_selectors_descendant() {
        test("selectors_descendant");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_sliding_downUp() {
        test("sliding_downUp");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_selectors_id() {
        test("selectors_id");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_ready_divTest3() {
        test("ready_divTest3");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_events_live2() {
        test("events_live2");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_events_bindUnbindBind() {
        test("events_bindUnbindBind");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dom_getSet() {
        test("dom_getSet");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dom_set() {
        test("dom_set");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_dom_getSetAttribute() {
        test("dom_getSetAttribute");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_animate_simple() {
        test("animate_simple");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void auto_ajax_post() {
        test("ajax_post");
    }

    private void test(String testCase) {
        Main.reset();
        Options.get().enableDeterminacy();
        Options.get().enableIncludeDom();
        Options.get().enableUnevalizer();
        Options.get().enableTest();
        // Successful tests runs in less than 15 seconds on `Intel(R) Core(TM) i7-3520M CPU @ 2.90GHz`
        Options.get().setAnalysisTimeLimit(90);
        Options.get().getUnsoundness().setUseFixedRandom(true);
        Options.get().getUnsoundness().setShowUnsoundnessUsage(true);
        Options.get().getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);
        Misc.run("benchmarks/tajs/src/jquery/test-suite/1.10/" + testCase + ".html");
        Misc.checkSystemOutput();
    }
}
