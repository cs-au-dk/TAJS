package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

// TODO inspect tests (GitHub #160)
@SuppressWarnings("static-method")
public class TestApps {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestApps");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableUnevalizer();
        Options.get().setAnalysisTimeLimit(3 * 60);
    }

    @Test
    public void apps_jscrypto_encrypt_cookie() throws Exception {
        Misc.run("benchmarks/tajs/src/apps/jscrypto/encrypt_cookie.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_jscrypto_encrypt_from_form() throws Exception {
        Misc.run("benchmarks/tajs/src/apps/jscrypto/encrypt_from_form.html");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class) // TODO investigate (GitHub #417)
    public void apps_mceditor_simple() throws Exception {
        Misc.run("benchmarks/tajs/src/apps/mceditor/simple.html");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class) // TODO investigate (GitHub #417)
    public void apps_mceditor_full() throws Exception {
        Misc.run("benchmarks/tajs/src/apps/mceditor/full.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_minesweeper() throws Exception {
        Misc.run("benchmarks/tajs/src/apps/minesweeper/minesweeper.html");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class)
    public void apps_paint() throws Exception {
        Misc.run("benchmarks/tajs/src/apps/paint/index.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_projavascript_clock() throws Exception {
        Misc.run("benchmarks/tajs/src/apps/projavascript/clock/07-clock.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_projavascript_gallery() throws Exception {
        Misc.run("benchmarks/tajs/src/apps/projavascript/gallery/index.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_projavascript_sun() throws Exception {
        Misc.run("benchmarks/tajs/src/apps/projavascript/sun/08-sun.html");
        Misc.checkSystemOutput();
    }

    @Ignore // GC Overhead (GitHub #422)
    @Test(expected = AnalysisLimitationException.class) // TODO investigate (GitHub #417)
    public void apps_samegame() throws Exception {
        Misc.run("benchmarks/tajs/src/apps/samegame/index.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_simplecalc() throws Exception {
        Misc.run("benchmarks/tajs/src/apps/simplecalc/math2.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_solitaire() throws Exception {
        Misc.run("benchmarks/tajs/src/apps/solitaire/spider.html"); // FIXME find out why ordinary exit is unreachable
        Misc.checkSystemOutput();
    }
}
