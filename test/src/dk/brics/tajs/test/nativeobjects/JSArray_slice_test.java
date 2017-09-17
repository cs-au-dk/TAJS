package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

public class JSArray_slice_test {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void emptyToEmpty() {
        Misc.runSource(//
                "var sliced = [].slice(0, 0);",
                "TAJS_assert(sliced.length === 0);",
                "");
    }

    @Test
    public void makeEmpty() {
        Misc.runSource(//
                "var sliced = ['x'].slice(0, 0);",
                "TAJS_assert(sliced.length === 0);",
                "");
    }

    @Test
    public void extractOne() {
        Misc.runSource(//
                "var sliced = ['x'].slice(0, 1);",
                "TAJS_assert(sliced.length === 1);",
                "TAJS_assert(sliced[0] === 'x');",
                "");
    }

    @Test
    public void extractOneNotFirst() {
        Misc.runSource(//
                "var sliced = ['x', 'y'].slice(1, 2);",
                "TAJS_assert(sliced.length === 1);",
                "TAJS_assert(sliced[0] === 'y');",
                "");
    }

    @Test
    public void extractTwo() {
        Misc.runSource(//
                "var sliced = ['x', 'y', 'z'].slice(0, 2);",
                "TAJS_assert(sliced.length === 2);",
                "TAJS_assert(sliced[1] === 'y');",
                "");
    }

    @Test
    public void cloneImplicit() {
        Misc.runSource(//
                "var sliced = ['x', 'y', 'z'].slice();",
                "TAJS_assert(sliced.length === 3);",
                "TAJS_assert(sliced[1] === 'y');",
                "");
    }

    @Test
    public void cloneExplicit() {
        Misc.runSource(//
                "var sliced = ['x', 'y', 'z'].slice(0,3);",
                "TAJS_assert(sliced.length === 3);",
                "TAJS_assert(sliced[1] === 'y');",
                "");
    }

    @Test
    public void cloneExplicitTooLong() {
        Misc.runSource(//
                "var sliced = ['x', 'y', 'z'].slice(0,4);",
                "TAJS_assert(sliced.length === 3);",
                "TAJS_assert(sliced[1] === 'y');",
                "");
    }

    @Test
    public void extractArray() {
        Misc.runSource(//
                "var sliced = [['x0', 'y0'], ['x1', 'y1']].slice(0, 1);",
                "TAJS_assert(sliced.length === 1);",
                "TAJS_assert(sliced[0][1] === 'y0');",
                "");
    }
}
