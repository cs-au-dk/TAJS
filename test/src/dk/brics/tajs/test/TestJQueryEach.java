package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for jQuery.each behaviour. If that function is imprecise, then jQuery can not be analyzed.
 */
@SuppressWarnings("static-method")
public class TestJQueryEach {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableQuiet();
        Options.get().enableTiming();
        Options.get().enableTest();
        Options.get().enableParameterSensitivity();
        Options.get().enableUnreachable();
        Options.get().enableLoopUnrolling(100);
    }

    @Test
    public void full() {
        // no calls to each, just a check that everything is fine
        Misc.init();
        String[] args = {"test/jquery-each/full.js"};
        Misc.run(args);
    }

    @Test
    public void arrays() {
        Misc.init();
        String[] args = {"test/jquery-each/eachOnArrays.js"};
        Misc.run(args);
    }

    @Test
    public void objects() {
        // notice the parameter sensitivity on the function passed to $.each!
        Misc.init();
        String[] args = {"test/jquery-each/eachOnObjects.js"};
        Misc.run(args);
    }

    @Test
    public void arrayVsObject() {
        // notice the parameter sensitivity on the function passed to $.each!
        Misc.init();
        String[] args = {"test/jquery-each/eachOnArrayVsObject.js"};
        Misc.run(args);
    }

    @Test
    public void ajax() {
        Misc.init();
        String[] args = {"test/jquery-each/full_ajaxExtensions.js"};
        Misc.run(args);
    }
}
