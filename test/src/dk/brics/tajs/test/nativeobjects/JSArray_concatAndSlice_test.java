package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

public class JSArray_concatAndSlice_test {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void jQueryExampleSimplified() {
        Misc.init();
        Misc.runSource(//
                "var array = [['a', 'b'], ['c', 'd']];",
                "var sliced = array.slice(0, 1);",
                "var complex = array.concat.apply([], sliced);",
                "TAJS_assert(complex.length === 2);",
                "TAJS_assert(complex[0] === 'a');",
                "TAJS_assert(complex[1] === 'b');",
                "");
    }

    @Test
    public void jQueryExampleOrig() {
        Misc.init();
        Misc.runSource(//
                "var fxAttrs = [ // height animations",
                "    [ 'height', 'marginTop', 'marginBottom', 'paddingTop', 'paddingBottom' ], // width animations",
                "    [ 'width', 'marginLeft', 'marginRight', 'paddingLeft', 'paddingRight' ], // opacity animations",
                "    [ 'opacity' ]",
                "];",
                "",
                "function genFx(type, num) {",
                "    var obj = {};",
                "",
                "    var extracted = fxAttrs.concat.apply([], fxAttrs.slice(0, 1));",
                "",
                "    TAJS_assert(extracted[0] === fxAttrs[0][0]);",
                "    TAJS_assert(extracted[1] === fxAttrs[0][1]);",
                "    TAJS_assert(extracted[2] === fxAttrs[0][2]);",
                "}",
                "",
                "genFx('show', 1);",
                "");
    }
}
