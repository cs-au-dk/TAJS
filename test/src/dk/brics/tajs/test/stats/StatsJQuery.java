package dk.brics.tajs.test.stats;

import dk.brics.tajs.options.OptionValues;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;
import java.util.Optional;

import static dk.brics.tajs.test.stats.StatsStandard.testJQueryLoad;
import static dk.brics.tajs.test.stats.StatsStandard.testJQueryLoad_ignoreUnreached;

public class StatsJQuery {

    public static String[][] testJQueryUse = {
            // analyzable in OOPSLA'14:
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_abort.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_abort2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_progress.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_progress2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_progressErrorCallback.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_stop.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_stopQueue.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_stopQueue2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dimensions_get.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dimensions_getWindow.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dimensions_set.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/divTest2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_appendPrepend.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_appendPrepend2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_appendPrepend3.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_beforeAfter.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_beforeAfter2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_beforeAfter3.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_removeEmpty.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_removeEmpty2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setCssAttribute.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeIn.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeInOut.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeToggleSpeed.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_down.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_downSpeeds.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_toggle.html"},
            // unanalyzable in OOPSLA'14:
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_loadWithCallback.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bindUnbindBind2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeTimed.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_get.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_jsonp.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_load.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_load2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_callback.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_queue.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_simpleSpeed.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_get.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_getAttribute.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setAttribute.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setMultipleCssAttributes.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setMultipleAttributes.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_unbind.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bind.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bind2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bind3.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bindData.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_live.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/methodChaining_divTest1.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/methodChaining_divTest2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/noConflict_fullName.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/noConflict_ready.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/noConflict_rename.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ready_divTest1.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ready_divTest2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_attributeValues.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_attributes.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_child.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_class.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/divTest1.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_descendant.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_downUp.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_id.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ready_divTest3.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_live2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bindUnbindBind.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_getSet.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_set.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_getSetAttribute.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_simple.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_post.html"},
    };

    public static String[][] testJQueryUse_ignoreUnreached = {
            // analyzable in OOPSLA'14:
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_abort.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_abort2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_progress.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_progress2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_progressErrorCallback.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_stop.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_stopQueue.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_stopQueue2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dimensions_get.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dimensions_getWindow.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dimensions_set.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/divTest2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_appendPrepend.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_appendPrepend2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_appendPrepend3.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_beforeAfter.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_beforeAfter2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_beforeAfter3.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_removeEmpty.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_removeEmpty2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setCssAttribute.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeIn.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeInOut.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeToggleSpeed.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_down.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_downSpeeds.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_toggle.html"},
            // unanalyzable in OOPSLA'14:
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_loadWithCallback.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bindUnbindBind2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeTimed.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_get.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_jsonp.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_load.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_load2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_callback.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_queue.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_simpleSpeed.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_get.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_getAttribute.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setAttribute.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setMultipleCssAttributes.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setMultipleAttributes.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_unbind.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bind.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bind2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bind3.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bindData.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_live.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/methodChaining_divTest1.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/methodChaining_divTest2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/noConflict_fullName.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/noConflict_ready.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/noConflict_rename.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ready_divTest1.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ready_divTest2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_attributeValues.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_attributes.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_child.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_class.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/divTest1.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_descendant.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_downUp.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_id.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ready_divTest3.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_live2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bindUnbindBind.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_getSet.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_set.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_getSetAttribute.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_simple.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_post.html"},
    };

    public static void main(String[] args) throws IOException, CmdLineException {
        String outfile = args.length > 0 ? args[0] : "jquery";
        OptionValues defaultOptions = new OptionValues();
        defaultOptions.getUnsoundness().setIgnoreSomePrototypesDuringDynamicPropertyReads(true);
        defaultOptions.getUnsoundness().setIgnoreImpreciseEvals(true);
        defaultOptions.getUnsoundness().setIgnoreUnlikelyUndefinedAsFirstArgumentToAddition(true);
        defaultOptions.getUnsoundness().setAssumeInOperatorReturnsTrueWhenSoundResultIsMaybeTrueAndPropNameIsNumber(true);
        defaultOptions.getUnsoundness().setIgnoreUnlikelyPropertyReads(true);
        defaultOptions.getUnsoundness().setUseFixedRandom(true);
        defaultOptions.getUnsoundness().setShowUnsoundnessUsage(true);
        defaultOptions.getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);
        Stats.run(outfile, 120, 200000, Optional.of(defaultOptions),
                testJQueryLoad_ignoreUnreached,
                testJQueryLoad,
                testJQueryUse_ignoreUnreached,
                testJQueryUse
        );
    }
}
