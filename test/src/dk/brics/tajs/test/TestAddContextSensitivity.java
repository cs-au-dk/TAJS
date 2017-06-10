package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestAddContextSensitivity {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableParameterSensitivity();
    }

    @Test
    public void addContextSensitivity_syntax() {
        Misc.init();
        String[] args = {"test/addcontextsensitivity/syntax.js"};
        Misc.run(args);
    }

    @Test
    public void addContextSensitivity_syntax2() {
        Misc.init();
        String[] args = {"test/addcontextsensitivity/syntax2.js"};
        Misc.run(args);
    }

    @Test
    public void addContextSensitivity_single() {
        Misc.init();
        String[] args = {"test/addcontextsensitivity/single.js"};
        Misc.run(args);
    }

    @Test(expected = AnalysisException.class)
    public void addContextSensitivity_wrongName() {
        Misc.init();
        String[] args = {"test/addcontextsensitivity/wrongName.js"};
        Misc.run(args);
    }

    @Test
    public void addContextSensitivity_multi() {
        Misc.init();
        String[] args = {"test/addcontextsensitivity/multi.js"};
        Misc.run(args);
    }

    @Test
    public void addContextSensitivity_confuse() {
        Misc.init();
        String[] args = {"test/addcontextsensitivity/confuse.js"};
        Misc.run(args);
    }

    @Test
    public void addContextSensitivity_finiteRecursion() {
        Misc.init();
        String[] args = {"test/addcontextsensitivity/finiteRecursion.js"};
        Misc.run(args);
    }

    @Test
    public void addContextSensitivity_finiteStringRecursion() {
        Misc.init();
        String[] args = {"test/addcontextsensitivity/finiteStringRecursion.js"};
        Misc.run(args);
    }
}
