package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

public class TestConversion {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void octalStrings() throws Exception {
        Options.get().getSoundnessTesterOptions().setTest(false);
        Misc.runSource(
                "var knownOctalInt = '0x42'",
                "var knownSpacedOctalInt = ' 0x42 '",
                "var knownOctalNonInt = '0x42.2'",
                "var octalPrefix = '0x' + (Math.random() == 0? 'foo': 'bar')",
                "TAJS_assertEquals(66, +knownOctalInt);",
                "TAJS_assertEquals(66, +knownSpacedOctalInt);",
                "TAJS_assertEquals(NaN, +knownOctalNonInt);",
                "TAJS_assertEquals(TAJS_join(NaN, TAJS_make('AnyNumUInt')), +octalPrefix);"
        );
    }
}
