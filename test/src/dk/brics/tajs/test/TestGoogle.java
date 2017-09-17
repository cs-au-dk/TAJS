package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestGoogle {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestGoogle");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void google_richards() throws Exception {
        Misc.run("test-resources/src/google/richards.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void google_benchpress() throws Exception {
        Misc.run("test-resources/src/google/benchpress.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void google_splay() throws Exception {
        Misc.run("test-resources/src/google/splay.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void google_cryptobench() throws Exception {
        Misc.run("test-resources/src/google/cryptobench.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void google_delta_blue() throws Exception {
        Misc.run("test-resources/src/google/delta-blue.js");
        Misc.checkSystemOutput();
    }
}
