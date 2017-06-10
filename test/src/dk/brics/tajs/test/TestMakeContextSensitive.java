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
        Misc.init();
        String[] args = {"test/makecontextsensitive/syntax.js"};
        Misc.run(args);
    }

    @Test
    public void makecontextsensitive_syntax2() {
        Misc.init();
        String[] args = {"test/makecontextsensitive/syntax2.js"};
        Misc.run(args);
    }

    @Test
    public void makecontextsensitive_single() {
        Misc.init();
        String[] args = {"test/makecontextsensitive/single.js"};
        Misc.run(args);
    }

    @Test
    public void makecontextsensitive_multi() {
        Misc.init();
        String[] args = {"test/makecontextsensitive/multi.js"};
        Misc.run(args);
    }

    @Test
    public void makecontextsensitive_confuse() {
        Misc.init();
        String[] args = {"test/makecontextsensitive/confuse.js"};
        Misc.run(args);
    }

    @Test
    public void makecontextsensitive_finiteRecursion() {
        Misc.init();
        String[] args = {"test/makecontextsensitive/finiteRecursion.js"};
        Misc.run(args);
    }

    @Test
    public void makecontextsensitive_finiteStringRecursion() {
        Misc.init();
        String[] args = {"test/makecontextsensitive/finiteStringRecursion.js"};
        Misc.run(args);
    }

    @Test
    public void makecontextsensitive_objectSensitivity() {
        Misc.init();
        String[] args = {"test/makecontextsensitive/objectSensitivity.js"};
        Misc.run(args);
    }

    @Test
    public void makecontextsensitive_guard() {
        Misc.init();
        String[] args = {"test/makecontextsensitive/guard.js"};
        Misc.run(args);
    }

    @Test
    public void makecontextsensitive_closureVariables() {
        Misc.init();
        String[] args = {"test/makecontextsensitive/closureVariables.js"};
        Misc.run(args);
    }
}
