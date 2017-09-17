package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class JSObject_keysAndOwnPropertyNames_test {

    @Parameterized.Parameter
    public String oSource;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<String> data() {
        return Arrays.asList(
                "null",
                "undefined",
                "{}",
                "[]",
                "{foo: 42}",
                "[42]",
                "42",
                "'foo'",
                "function(){}",
                "Object",
                "Object.prototype",
                "(Object.prototype.foo = 42,{})",
                "(x=[],x[Math.random()]=42,x)",
                "(x=[],x[42]=42,x)",
                "(x=[],x.p=42,x)"
        );
    }

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void getOwnPropertyNames() {
        test(oSource, "getOwnPropertyNames");
    }

    @Test
    public void keys() {
        test(oSource, "keys");
    }

    private void test(String oSource, String method) {
        if (oSource.equals("null") || oSource.equals("undefined")) {
            Options.get().enableDoNotExpectOrdinaryExit();
        }
        String encoded = Integer.toString(Math.abs(oSource.hashCode()));
        Misc.runSourcePart(encoded,
                "var o = " + oSource + ";",
                "var names = Object." + method + "(o);",
                "names.length;");
    }
}
