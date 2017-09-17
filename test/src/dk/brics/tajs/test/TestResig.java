package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisResultException;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestResig {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestResig");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void resig_ejohn12() throws Exception {
        Misc.run("test-resources/src/resig/ejohn12.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn13() throws Exception {
        Misc.run("test-resources/src/resig/ejohn13.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn14() throws Exception {
        Misc.run("test-resources/src/resig/ejohn14.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn15() throws Exception {
        Misc.run("test-resources/src/resig/ejohn15.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn17() throws Exception {
        Misc.run("test-resources/src/resig/ejohn17.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn18() throws Exception {
        Misc.run("test-resources/src/resig/ejohn18.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn19() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/resig/ejohn19.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn20() throws Exception {
        Misc.run("test-resources/src/resig/ejohn20.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn21() throws Exception {
        Misc.run("test-resources/src/resig/ejohn21.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn23() throws Exception {
        Misc.run("test-resources/src/resig/ejohn23.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn24() throws Exception {
        Misc.run("test-resources/src/resig/ejohn24.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn25() throws Exception {
        Misc.run("test-resources/src/resig/ejohn25.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn26() throws Exception {
        Misc.run("test-resources/src/resig/ejohn26.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn28() throws Exception {
        Misc.run("test-resources/src/resig/ejohn28.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn30() throws Exception {
        Misc.run("test-resources/src/resig/ejohn30.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn31() throws Exception {
        Misc.run("test-resources/src/resig/ejohn31.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn33() throws Exception {
        Misc.run("test-resources/src/resig/ejohn33.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn34() throws Exception {
        Misc.run("test-resources/src/resig/ejohn34.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn35() throws Exception {
        Misc.run("test-resources/src/resig/ejohn35.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn36() throws Exception {
        Misc.run("test-resources/src/resig/ejohn36.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn38() throws Exception {
        Misc.run("test-resources/src/resig/ejohn38.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn40() throws Exception {
        Misc.run("test-resources/src/resig/ejohn40.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn41() throws Exception {
        Misc.run("test-resources/src/resig/ejohn41.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn42() throws Exception {
        Misc.run("test-resources/src/resig/ejohn42.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn43() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/resig/ejohn43.js"); // is supposed to throw definite exception
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn45() throws Exception {
        Misc.run("test-resources/src/resig/ejohn45.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn45_bug() throws Exception {
        Misc.run("test-resources/src/resig/ejohn45-bug.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn47() throws Exception {
        Misc.run("test-resources/src/resig/ejohn47.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn49() throws Exception {
        Misc.run("test-resources/src/resig/ejohn49.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn50() throws Exception {
        Misc.run("test-resources/src/resig/ejohn50.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisResultException.class /* referring to jQuery, which is not included ... */)
    public void resig_ejohn51() throws Exception {
        Misc.run("test-resources/src/resig/ejohn51.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn52() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/resig/ejohn52.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn53() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/resig/ejohn53.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn54() throws Exception {
        Misc.run("test-resources/src/resig/ejohn54.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn56() throws Exception {
        Options.get().enableIncludeDom();
        Options.get().enableLoopUnrolling(5);
        Misc.run("test-resources/src/resig/ejohn56.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn58() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/resig/ejohn58.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn59() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/resig/ejohn59.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn63() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/resig/ejohn63.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn65() throws Exception {
        Misc.run("test-resources/src/resig/ejohn65.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn66() throws Exception {
        Misc.run("test-resources/src/resig/ejohn66.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn67() throws Exception {
        Misc.run("test-resources/src/resig/ejohn67.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn69() throws Exception {
        Misc.run("test-resources/src/resig/ejohn69.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn71() throws Exception {
        Misc.run("test-resources/src/resig/ejohn71.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn72() throws Exception {
        Misc.run("test-resources/src/resig/ejohn72.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn74() throws Exception {
        Misc.run("test-resources/src/resig/ejohn74.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn76() throws Exception {
        Misc.run("test-resources/src/resig/ejohn76.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn78() throws Exception {
        Misc.run("test-resources/src/resig/ejohn78.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn80() throws Exception {
        Misc.run("test-resources/src/resig/ejohn80.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn80b() throws Exception {
        Misc.run("test-resources/src/resig/ejohn80b.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn81() throws Exception {
        Misc.run("test-resources/src/resig/ejohn81.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn83() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/resig/ejohn83.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn84() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/resig/ejohn84.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn85() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/resig/ejohn85.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn86() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/resig/ejohn86.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn88() throws Exception {
        Misc.run("test-resources/src/resig/ejohn88.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void resig_ejohn90() throws Exception {
        Misc.run("test-resources/src/resig/ejohn90.js");
        Misc.checkSystemOutput();
    }
}
