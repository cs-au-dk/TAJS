package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestMakeContextSensitive {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void makecontextsensitive_syntax() {
        Misc.run("test-resources/src/makecontextsensitive/syntax.js");
    }

    @Test
    public void makecontextsensitive_syntax2() {
        Misc.run("test-resources/src/makecontextsensitive/syntax2.js");
    }

    @Test
    public void makecontextsensitive_single() {
        Misc.run("test-resources/src/makecontextsensitive/single.js");
    }

    @Test
    public void makecontextsensitive_multi() {
        Misc.run("test-resources/src/makecontextsensitive/multi.js");
    }

    @Test
    public void makecontextsensitive_confuse() {
        Misc.run("test-resources/src/makecontextsensitive/confuse.js");
    }

    @Test
    public void makecontextsensitive_finiteRecursion() {
        Misc.run("test-resources/src/makecontextsensitive/finiteRecursion.js");
    }

    @Test
    public void makecontextsensitive_finiteStringRecursion() {
        Misc.run("test-resources/src/makecontextsensitive/finiteStringRecursion.js");
    }

    @Test
    public void makecontextsensitive_objectSensitivity() {
        Misc.run("test-resources/src/makecontextsensitive/objectSensitivity.js");
    }

    @Test
    public void makecontextsensitive_guard() {
        Misc.run("test-resources/src/makecontextsensitive/guard.js");
    }

    @Test
    public void makecontextsensitive_closureVariables() {
        Misc.run("test-resources/src/makecontextsensitive/closureVariables.js");
    }
}
