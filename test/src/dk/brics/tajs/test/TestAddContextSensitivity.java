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
        Misc.run("test-resources/src/addcontextsensitivity/syntax.js");
    }

    @Test
    public void addContextSensitivity_syntax2() {
        Misc.run("test-resources/src/addcontextsensitivity/syntax2.js");
    }

    @Test
    public void addContextSensitivity_single() {
        Misc.run("test-resources/src/addcontextsensitivity/single.js");
    }

    @Test(expected = AnalysisException.class)
    public void addContextSensitivity_wrongName() {
        Misc.run("test-resources/src/addcontextsensitivity/wrongName.js");
    }

    @Test
    public void addContextSensitivity_multi() {
        Misc.run("test-resources/src/addcontextsensitivity/multi.js");
    }

    @Test
    public void addContextSensitivity_confuse() {
        Misc.run("test-resources/src/addcontextsensitivity/confuse.js");
    }

    @Test
    public void addContextSensitivity_finiteRecursion() {
        Misc.run("test-resources/src/addcontextsensitivity/finiteRecursion.js");
    }

    @Test
    public void addContextSensitivity_finiteStringRecursion() {
        Misc.run("test-resources/src/addcontextsensitivity/finiteStringRecursion.js");
    }
}
