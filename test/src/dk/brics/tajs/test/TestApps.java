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
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/apps/jscrypto/encrypt_cookie.html"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_jscrypto_encrypt_from_form() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/apps/jscrypto/encrypt_from_form.html"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

//    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)  // FIXME inspect reason
//    public void apps_mceditor_simple() throws Exception {
//        Misc.init();
//        Misc.captureSystemOutput();
//        String[] args = {"test/apps/mceditor/simple.html"};
//        Misc.run(args);
//        Misc.checkSystemOutput();
//    }

//    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class) // FIXME inspect reason
//    public void apps_mceditor_full() throws Exception {
//        Misc.init();
//        Misc.captureSystemOutput();
//        String[] args = {"test/apps/mceditor/full.html"};
//        Misc.run(args);
//        Misc.checkSystemOutput();
//    }

    @Test
    public void apps_minesweeper() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/apps/minesweeper/minesweeper.html"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class)
    public void apps_paint() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/apps/paint/index.html"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_projavascript_clock() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/apps/projavascript/clock/07-clock.html"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_projavascript_gallery() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/apps/projavascript/gallery/index.html"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_projavascript_sun() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/apps/projavascript/sun/08-sun.html"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME GC overhead!
    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void apps_samegame() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/apps/samegame/index.html"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_simplecalc() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/apps/simplecalc/math2.html"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void apps_solitaire() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/apps/solitaire/spider.html"};
        Misc.run(args); // FIXME find out why ordinary exit is unreachable
        Misc.checkSystemOutput();
    }
}
