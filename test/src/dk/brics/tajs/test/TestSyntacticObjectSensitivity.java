package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

public class TestSyntacticObjectSensitivity {

    private final String twoObjects = "var a = {v: 42}; var b = {v: 87};";

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void isObjectSensitive() {
        Misc.runSource("", //
                twoObjects, //
                "function f(){",
                "    return this.v;",
                "}", //
                "a.f = f; b.f = f;", //
                "TAJS_assert(a.f() == 42);", //
                "TAJS_assert(b.f() == 87);"
        );
    }

    @Test
    public void isObjectSensitive2() {
        Misc.runSource("", //
                twoObjects, //
                "function f(o){",
                "    this;", //
                "    return o.v;",
                "}", //
                "a.f = f; b.f = f;", //
                "TAJS_assert(a.f(a) == 42);", //
                "TAJS_assert(b.f(b) == 87);"
        );
    }

    @Test
    public void isNotObjectSensitive() {
        Misc.runSource("", //
                twoObjects, //
                "function f(o){",
                "    return o.v;",
                "}", //
                "a.f = f; b.f = f;", //
                "TAJS_assert(a.f(a), 'isMaybeSingleNum', true);", // will be true for first flow
                "TAJS_assert(b.f(b), 'isMaybeSingleNum', false);" // will not be true for second flow
        );
    }
}
