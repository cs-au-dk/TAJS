package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

public class TestJQueryLoad {

    @Test
    public void load_1_0() {
        Misc.run("benchmarks/tajs/src/jquery/libraries/jquery-1.0.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_1() {
        Misc.run("benchmarks/tajs/src/jquery/libraries/jquery-1.1.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_2() {
        Misc.run("benchmarks/tajs/src/jquery/libraries/jquery-1.2.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_3() {
        Misc.run("benchmarks/tajs/src/jquery/libraries/jquery-1.3.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_4() {
        Misc.run("benchmarks/tajs/src/jquery/libraries/jquery-1.4.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_5() {
        Misc.run("benchmarks/tajs/src/jquery/libraries/jquery-1.5.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_6() {
        Misc.run("benchmarks/tajs/src/jquery/libraries/jquery-1.6.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_7() {
        Misc.run("benchmarks/tajs/src/jquery/libraries/jquery-1.7.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_8() {
        Misc.run("benchmarks/tajs/src/jquery/libraries/jquery-1.8.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_9() {
        Misc.run("benchmarks/tajs/src/jquery/libraries/jquery-1.9.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_10() {
        Misc.run("benchmarks/tajs/src/jquery/libraries/jquery-1.10.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void load_1_11() {
        Misc.run("benchmarks/tajs/src/jquery/libraries/jquery-1.11.js");
        Misc.checkSystemOutput();
    }

    @Before
    public void before() {
        Main.reset();
        Options.get().enableDeterminacy();
        Options.get().enableIncludeDom();
        Options.get().enableUnevalizer();
        Options.get().enableTest();
        Options.get().enableNoStrict(); // soundness testing does not work properly with strict mode
        // Each tests runs in less than 30 seconds on `Intel(R) Core(TM) i7-3520M CPU @ 2.90GHz`
        Options.get().setAnalysisTimeLimit(90);
        //Options.get().enableNoStrict(); // soundness testing does not work properly with strict mode
        Options.get().getUnsoundness().setUseFixedRandom(true);
        Options.get().getUnsoundness().setShowUnsoundnessUsage(true);
    }
}
