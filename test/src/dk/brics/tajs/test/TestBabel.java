package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.OptionValues;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBabel { // TODO: add to RunFast?

    private OptionValues options = null;

    @Before
    public void before() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
        options = Options.get().clone();
    }

    @After
    public void after() {
        Options.set(options);
    }

    private static String arrowProgram = "(a => a + 2)(3)";
    private static String letProgram = "let a = 10;";

    @Test(expected = AnalysisLimitationException.SyntacticSupportNotImplemented.class)
    public void testArrowProgramWithoutBabel() {
        Misc.runSource(arrowProgram);
        Misc.checkSystemOutput();
    }

    @Test
    public void testArrowProgramWithBabel() {
        Options.get().enableBabel();
        Misc.runSource(arrowProgram);
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.SyntacticSupportNotImplemented.class)
    public void testLetProgramWithoutBabel() {
        Misc.runSource(letProgram);
        Misc.checkSystemOutput();
    }

    @Test
    public void testLetProgramWithBabel() {
        Options.get().enableBabel();
        Misc.runSource(letProgram);
        Misc.checkSystemOutput();
    }
}
