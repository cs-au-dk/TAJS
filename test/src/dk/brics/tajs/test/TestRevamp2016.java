package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Before;
import org.junit.Test;

public class TestRevamp2016 {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableDeterminacy();
        Options.get().enableIncludeDom();
        Options.get().enableUnevalizer();
//        Options.get().getSoundnessTesterOptions().setTest(false);
        Options.get().enableNoStrict(); // soundness testing does not work properly with strict mode
        Options.get().getUnsoundness().setUseFixedRandom(true);
        Options.get().getUnsoundness().setShowUnsoundnessUsage(true);
        Options.get().setAnalysisTimeLimit(2 * 60);
    }

    @Test
    public void Revamp2016_jquery_app0() throws Exception {
        Misc.run("test-resources/src/revamp/jquery/jquery_app0.html");
    }

    @Test
    public void Revamp2016_jquery_app1() throws Exception {
        Misc.run("test-resources/src/revamp/jquery/jquery_app1.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_jquery_app2() throws Exception {
        Misc.run("test-resources/src/revamp/jquery/jquery_app2.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_jquery_app3() throws Exception {
        Misc.run("test-resources/src/revamp/jquery/jquery_app3.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_jquery_app4() throws Exception {
        Misc.run("test-resources/src/revamp/jquery/jquery_app4.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_jquery_tutorial_example_a() throws Exception {
        Misc.run("test-resources/src/revamp/jquery/jquery_tutorial_example_a.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_jquery_tutorial_example_b() throws Exception {
        Misc.run("test-resources/src/revamp/jquery/jquery_tutorial_example_b.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_jquery_tutorial_example_c() throws Exception {
        Misc.run("test-resources/src/revamp/jquery/jquery_tutorial_example_c.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_jquery_tutorial_example_d() throws Exception {
        Misc.run("test-resources/src/revamp/jquery/jquery_tutorial_example_d.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_jquery_tutorial_example_e() throws Exception {
        Misc.run("test-resources/src/revamp/jquery/jquery_tutorial_example_e.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_jquery_tutorial_example_f() throws Exception {
        Misc.run("test-resources/src/revamp/jquery/jquery_tutorial_example_f.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_prototype_ajax() throws Exception {
        Misc.run("test-resources/src/revamp/prototype/ajax.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_prototype_classes() throws Exception {
        Misc.run("test-resources/src/revamp/prototype/classes.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_prototype_justload() throws Exception {
        Misc.run("test-resources/src/revamp/prototype/justload.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_prototype_observe() throws Exception {
        Misc.run("test-resources/src/revamp/prototype/observe.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_prototype_query() throws Exception {
        Misc.run("test-resources/src/revamp/prototype/query.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_prototype_trythese() throws Exception {
        Misc.run("test-resources/src/revamp/prototype/trythese.html");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void Revamp2016_scriptaculous_justload() throws Exception {
        Misc.run("test-resources/src/revamp/scriptaculous/justload.html");
    }
}
