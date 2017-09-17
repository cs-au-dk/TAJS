package dk.brics.tajs.test;

import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestSoundness {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestSoundness");
    }

    @Before
    public void init() {
        Options.reset();
        Options.get().enableTest();
        Options.get().enableNoMessages();
        Options.get().enableQuiet();
    }

    @Test
    public void soundness_google() throws Exception {
        Misc.run("-test-soundness", "richards.log", "test-resources/src/google/richards.js");
    }
}
