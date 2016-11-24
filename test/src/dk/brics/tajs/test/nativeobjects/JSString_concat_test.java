package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class JSString_concat_test {

	@Before
    public void before() {
        Main.reset();
        Main.initLogging();
        Options.get().enableTest();
    }

    @Test
    public void noArgs() {
        Misc.init();
        Misc.runSource("var v = 'foo'.concat();",
                "TAJS_assert(v === 'foo');");
    }

    @Test
    public void undefinedArg() {
        Misc.runSource("var v = 'foo'.concat(undefined);",
                "TAJS_assert(v === 'fooundefined');");
    }

    @Test
    public void oneArg() {
        Misc.runSource("var v = 'foo'.concat('x');",
                "TAJS_assert(v === 'foox');");
    }

    @Test
    public void twoArgs() {
        Misc.runSource("var v = 'foo'.concat('x', 'y');",
                "TAJS_assert(v === 'fooxy');");
    }

    @Test
    public void manyArgs() {
        Misc.runSource("var v = 'foo'.concat('x', 'y', 'z', 'a', 'b', 'c');",
                "TAJS_assert(v === 'fooxyzabc');");
    }

//    @Test
//    public void manyMultiArgs() {
//        Misc.runSource("var v = 'foo'.concat('x', 'y', 'z', 'a', 'b', 'c', Math.random()? 0: '0');",
//                "TAJS_assert(v === 'fooxyzabc0');");
//    }

    @Test
    public void unknown() {
        Misc.runSource("var v = (Math.random()? 'foo': 'bar').concat();",
                "TAJS_assert(v, 'isMaybeAnyStr', true);");
    }

    @Test
    public void unknownArgs() {
        Misc.runSource("var v = String.prototype.concat.apply('foo', Math.random()? []: ['x']);",
                "TAJS_assert(v, 'isMaybeStrIdentifier', true);");
    }
}
