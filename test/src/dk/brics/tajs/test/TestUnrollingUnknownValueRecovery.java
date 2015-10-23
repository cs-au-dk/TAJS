package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.js2flowgraph.FunctionBuilder;
import dk.brics.tajs.options.Options;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestUnrollingUnknownValueRecovery {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableLoopUnrolling(100);
    }

    @Test
    public void onlyLocals1() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 1; i++){ }",
                "");
    }

    @Test
    public void onlyLocals2() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 1; i++){ }",
                "i;",
                "");
    }

    @Test
    public void globalProperty_ReadBefore() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "toString;",
                "for(var i = 0; i < 1; i++){ }",
                "");
    }

    @Test
    public void globalProperty_ReadBeforeReadAfter() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "toString;",
                "for(var i = 0; i < 1; i++){ }",
                "toString;",
                "");
    }

    @Test
    public void globalProperty_WriteBeforeReadAfter() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "toString = 42;",
                "for(var i = 0; i < 1; i++){ }",
                "toString;",
                "");
    }

    @Test
    public void globalProperty_WriteBeforeWriteInsideReadAfter() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "toString = 42;",
                "for(var i = 0; i < 1; i++){ toString = 87; }",
                "toString;",
                "");
    }

    @Test
    public void globalProperty_ReadAfter() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 1; i++){ }",
                "toString;",
                "");
    }

    @Test
    public void globalProperty_ReadInsideReadAfter() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 1; i++){ toString; }",
                "toString;",
                "");
    }

    @Test
    public void globalProperty_WriteInsideReadAfter() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 1; i++){ toString = 42; }",
                "toString;",
                "");
    }

    @Test
    public void globalProperty_ReadInside() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 1; i++){ toString; }",
                "");
    }

    @Test
    public void globalProperty_WriteInside() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 1; i++){ toString = 42; }",
                "");
    }

    @Test
    public void localVariable_WriteInsideNestedBodyReadAfter() {
        Misc.init();
        Misc.runSource(
                "var k = 0;",
                "for(var i = 0; i < 5; i++){",
                "   for(var j = 0; j < 5; j++){",
                "       k = 42;",
                "   }",
                "}",
                "k;",
                "");
    }

    @Test
    public void localVariable_ReadInNestedConditionWriteInsideNestedBodyReadAfterLoops() {
        Misc.init();
        Misc.runSource(
                "var k = true;",
                "for(var i = 0; i < 2; i++){",
                "   while(k){",
                "       k = false;",
                "   }",
                "}",
                "k;",
                "");
    }

    @Test
    public void localVariable_WriteInsideCopyToLocalInsideReadAfter() {
        Misc.init();
        Misc.runSource(
                "var k = true; var l = true;",
                "while(l){",
                "   k = false;",
                "   l = k;",
                "}",
                "k;",
                "");
    }

    @Test
    public void localVariable_WriteInsideNestedCopyToLocalInsideNestedReadAfter() {
        Misc.init();
        Misc.runSource(
                "var k = true; var l = k;",
                "for(var i = 0; i < 2; i++){",
                "   while(l){",
                "       k = false;",
                "       l = k;",
                "   }",
                "}",
                "k;",
                "");
    }
}
