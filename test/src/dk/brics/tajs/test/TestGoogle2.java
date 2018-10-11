package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestGoogle2 {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestGoogle2");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().setAnalysisTimeLimit(60);
    }

    @Test
    public void google2_richards() throws Exception {
        Misc.run("test-resources/src/google2/richards.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class) // TODO investigate (GitHub #417)
    public void google2_earley_boyer() throws Exception {
        Misc.run("test-resources/src/google2/earley-boyer.js");
        Misc.checkSystemOutput();
    }

    @Ignore
    @Test
    public void google2_raytrace() throws Exception {
        // (contains parts of the Prototype framework)
        Misc.run("test-resources/src/google2/raytrace.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void google2_splay() throws Exception {
        Misc.run("test-resources/src/google2/splay.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void google2_regexp() throws Exception {
        Misc.run("test-resources/src/google2/regexp.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void google2_crypto() throws Exception {
        Misc.run("test-resources/src/google2/crypto.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void google2_deltablue() throws Exception {
        Misc.run("test-resources/src/google2/deltablue.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void google2_navier_stokes() throws Exception {
        Misc.run("test-resources/src/google2/navier-stokes.js");
        Misc.checkSystemOutput();
    }
}
