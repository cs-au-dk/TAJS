package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

/**
 * Misc tests for string-escaping
 */
public class TestMicroStringEscaping {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void issue197_3() {
        Misc.run("test-resources/src/micro/issue197_3.js");
    }

    @Test
    public void issue197_3_minified() {
        Misc.run("test-resources/src/micro/issue197_3_minified.js");
    }

    @Test
    public void stringReplace_withBackslashes() {
        Misc.runSource("TAJS_assert('\\\\x'.replace('x', 'y') === '\\\\y');");
    }

    @Test
    public void stringReplace_withBackslashes2() {
        Misc.runSource("TAJS_assert(String.prototype.replace.call('\\\\x', 'x', 'y') === '\\\\y');");
    }

    @Test
    public void stringReplace_withBackslashes3() {
        Misc.runSource("TAJS_assert(String.prototype.replace.call(new Object('\\\\x'), 'x', 'y') === '\\\\y');");
    }

    @Test
    public void stringObject() {
        Misc.runSource("TAJS_assert(new String('\\\\x') == '\\\\x');");
    }

    @Test
    public void stringObject2() {
        Misc.runSource("TAJS_assert(new String('\\\\x') + '' === '\\\\x');");
    }

    @Test
    public void stringObject3() {
        Misc.runSource("TAJS_assert(new Object('\\\\x') + '' === '\\\\x');");
    }

    @Test
    public void stringReplace_withBackslashes_inFile() {
        Misc.run("test-resources/src/micro/stringReplace_withBackslashes.js");
    }

    @Test
    public void stringWithBackslashes() {
        Misc.runSource("TAJS_dumpValue('\\\\x')");
        Misc.checkSystemOutput();
    }
}
