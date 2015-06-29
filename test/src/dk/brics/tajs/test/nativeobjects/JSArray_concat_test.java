package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

public class JSArray_concat_test {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void noneNone() {
        Misc.init();
        Misc.runSource(//
                "var concatenated = [].concat();",
                "TAJS_assert(concatenated.length === 0);",
                "");
    }

    @Test
    public void noneOne() {
        Misc.init();
        Misc.runSource(//
                "var concatenated = [].concat('x');",
                "TAJS_assert(concatenated.length === 1);",
                "TAJS_assert(concatenated[0] === 'x');",
                "");
    }

    @Test
    public void oneNone() {
        Misc.init();
        Misc.runSource(//
                "var concatenated = ['x'].concat();",
                "TAJS_assert(concatenated.length === 1);",
                "TAJS_assert(concatenated[0] === 'x');",
                "");
    }

    @Test
    public void oneOne() {
        Misc.init();
        Misc.runSource(//
                "var concatenated = ['x'].concat('y');",
                "TAJS_assert(concatenated.length === 2);",
                "TAJS_assert(concatenated[0] === 'x');",
                "TAJS_assert(concatenated[1] === 'y');",
                "");
    }

    @Test
    public void twoOne() {
        Misc.init();
        Misc.runSource(//
                "var concatenated = ['x', 'y'].concat('z');",
                "TAJS_assert(concatenated.length === 3);",
                "TAJS_assert(concatenated[0] === 'x');",
                "TAJS_assert(concatenated[1] === 'y');",
                "TAJS_assert(concatenated[2] === 'z');",
                "");
    }

    @Test
    public void oneTwo() {
        Misc.init();
        Misc.runSource(//
                "var concatenated = ['x'].concat('y', 'z');",
                "TAJS_assert(concatenated.length === 3);",
                "TAJS_assert(concatenated[0] === 'x');",
                "TAJS_assert(concatenated[1] === 'y');",
                "TAJS_assert(concatenated[2] === 'z');",
                "");
    }

    @Test
    public void oneArrayOne() {
        Misc.init();
        Misc.runSource(//
                "var concatenated = ['x'].concat(['y']);",
                "TAJS_assert(concatenated.length === 2);",
                "TAJS_assert(concatenated[0] === 'x');",
                "TAJS_assert(concatenated[1] === 'y');",
                "");
    }

    @Test
    public void oneArrayTwo() {
        Misc.init();
        Misc.runSource(//
                "var concatenated = ['x'].concat(['y', 'z']);",
                "TAJS_assert(concatenated.length === 3);",
                "TAJS_assert(concatenated[0] === 'x');",
                "TAJS_assert(concatenated[1] === 'y');",
                "TAJS_assert(concatenated[2] === 'z');",
                "");
    }

    @Test
    public void oneTwoArrays() {
        Misc.init();
        Misc.runSource(//
                "var concatenated = ['x'].concat(['y'], ['z']);",
                "TAJS_assert(concatenated.length === 3);",
                "TAJS_assert(concatenated[0] === 'x');",
                "TAJS_assert(concatenated[1] === 'y');",
                "TAJS_assert(concatenated[2] === 'z');",
                "");
    }

    @Test
    public void oneMixedArrays() {
        Misc.init();
        Misc.runSource(//
                "var concatenated = ['x'].concat(['y'], 'z');",
                "TAJS_assert(concatenated.length === 3);",
                "TAJS_assert(concatenated[0] === 'x');",
                "TAJS_assert(concatenated[1] === 'y');",
                "TAJS_assert(concatenated[2] === 'z');",
                "");
    }

    @Test
    public void oneNestedArray() {
        Misc.init();
        Misc.runSource(//
                "var concatenated = ['x'].concat([['y', 'z']]);",
                "TAJS_assert(concatenated.length === 2);",
                "TAJS_assert(concatenated[0] === 'x');",
                "TAJS_assert(concatenated[1].length === 2);",
                "");
    }

    @Test
    public void lazyAndArrayConcat() {
        Misc.init();
        Misc.runSource(//
                "var array = [",
                "    [ 'a', 'b', 'c'],",
                "    [ 'x', 'y', 'z']",
                "];",
                "",
                "",
                "var sliced = array.slice(1, 2);",
                "var array1 = array.concat.apply([], sliced);",
                "",
                "TAJS_assert(array1[0] === array[1][0]);",
                "TAJS_assert(array1[1] === array[1][1]);",
                "TAJS_assert(array1[2] === array[1][2]);",

                "function f() {",
                "    var sliced = array.slice(1, 2);",
                "    var array1 = array.concat.apply([], sliced);",
                "",
                "    TAJS_assert(array1[0] === array[1][0]);",
                "    TAJS_assert(array1[1] === array[1][1]);",
                "    TAJS_assert(array1[2] === array[1][2]);",
                "}",
                "",
                "f();",
                "");
    }
}
