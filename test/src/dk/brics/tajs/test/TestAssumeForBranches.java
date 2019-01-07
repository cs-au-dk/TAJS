package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

public class TestAssumeForBranches {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    private void test(String... sourceLines) {
        Misc.runSource(sourceLines);
    }

    @Test
    public void assumeVariableBooleanTrue() {
        test(
                "var v = Math.random()? true: false;",
                "if(v){",
                "   TAJS_assert(v === true);",
                "}");
    }

    @Test
    public void assumeVariableBooleanFalse() {
        test(
                "var v = Math.random()? true: false;",
                "if(v){}else{",
                "   TAJS_assert(v === false);",
                "}");
    }

    @Test
    public void assumePropertyBooleanTrue() {
        test(
                "var o = {}; o.v = Math.random()? true: false;",
                "if(o.v){",
                "   TAJS_assert(o.v === true);",
                "}");
    }

    @Test
    public void assumePropertyBooleanFalse() {
        test(
                "var o = {}; o.v = Math.random()? true: false;",
                "if(o.v){}else{",
                "   TAJS_assert(o.v === false);",
                "}");
    }

    @Test
    public void assumeOnNonBoolVariable() {
        test(
                "var v = Math.random()? '': 42;",
                "if(v){",
                "   TAJS_assert(v == 42);",
                "}");
    }

    @Test
    public void assumeOnNonBoolProperty() {
        test(
                "var o = {}; o.v = Math.random()? '': 42;",
                "if(o.v){",
                "   TAJS_assert(o.v === 42);",
                "}");
    }

    @Test
    public void assume_keepFlowing_1() {
        test(
                "var v = 0;",
                "if(v){",
                "   v",
                "}else{",
                "   v",
                "}",
                "TAJS_assert(v === 0);");
    }

    @Test
    public void assumeOnTernary_keepFlowing_1() {
        test(
                "var f = 0;",
                "f?",
                "   f:",
                "   f;",
                "TAJS_assert(f === 0);");
    }

    @Test
    public void assumeOnTernary_keepFlowing_2() {
        test(
                "var f = 1;",
                "f?",
                "   f:",
                "   f;",
                "TAJS_assert(f === 1);");
    }

    @Test
    public void assumeOnTernary_keepFlowing_3() {
        test(
                "var f = false;",
                "f?",
                "   f:",
                "   f;",
                "TAJS_assert(f === false);");
    }

    @Test
    public void assumeEliminatesValues() {
        test(
                "var v = Math.random()? {}: '';",
                "if(v){",
                "   TAJS_assert(v, 'isMaybeObject');",
                "   TAJS_assert(v, 'isMaybeSingleStr', false);",
                "}else{",
                "   TAJS_assert(v, 'isMaybeObject', false);",
                "   TAJS_assert(v, 'isMaybeSingleStr');",
                "}");
    }
}