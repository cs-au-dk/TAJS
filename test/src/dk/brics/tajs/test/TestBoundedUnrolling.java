package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

public class TestBoundedUnrolling {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void counterWhileLoop_belowBound() {
        Options.get().enableLoopUnrolling(10);
        Misc.runSource(
                "var i = 0;",
                "while(i < 5){ i++; }",
                "TAJS_assert(i === 5);",
                "");
    }

    @Test
    public void counterForLoop_belowBound() {
        Options.get().enableLoopUnrolling(10);
        Misc.runSource(
                "for(var i = 0; i < 5; i++){ }",
                "TAJS_assert(i === 5);",
                "");
    }

    @Test
    public void sequencedCounterForLoops_belowBound() {
        Options.get().enableLoopUnrolling(10);
        Misc.runSource(
                "for(var i = 0; i < 5; i++){ }",
                "for(; i < 10; i++){ }",
                "TAJS_assert(i === 10);",
                "");
    }

    @Test
    public void nestedCounterForLoop_belowBound() {
        Options.get().enableLoopUnrolling(10);
        Misc.runSource(
                "var k = 0;",
                "for(var i = 0; i < 5; i++){",
                "   for(var j = 0; j < 5; j++){",
                "       k++;",
                "   }",
                "}",
                "TAJS_assert(i === 5);",
                "TAJS_assert(j === 5);",
                "TAJS_assert(k === 25);",
                "");
    }

    @Test
    public void stringShrinkingWhileLoop_belowBound() {
        Options.get().enableLoopUnrolling(10);
        Misc.runSource(
                "var s = 'abcdefg';",
                "while(s){ s = s.substring(1); }",
                "TAJS_assert(s === '');",
                "");
    }

    @Test
    public void counterWhileLoop_aboveBound() {
        Options.get().enableLoopUnrolling(2);
        Misc.runSource(
                "var i = 0;",
                "while(i < 5){ i++; }",
                "TAJS_assert(i, 'isMaybeNumUInt');",
                "TAJS_assert(i, 'isMaybeSingleNum', false);",
                "");
    }

    @Test
    public void counterForLoop_aboveBound() {
        Options.get().enableLoopUnrolling(2);
        Misc.runSource(
                "for(var i = 0; i < 5; i++){ }",
                "TAJS_assert(i, 'isMaybeNumUInt');",
                "TAJS_assert(i, 'isMaybeSingleNum', false);",
                "");
    }

    @Test
    public void sequencedCounterForLoops_aboveBound() {
        Options.get().enableLoopUnrolling(2);
        Misc.runSource(
                "for(var i = 0; i < 5; i++){ }",
                "for(; i < 10; i++){ }",
                "TAJS_assert(i, 'isMaybeNumUInt');",
                "TAJS_assert(i, 'isMaybeSingleNum', false);",
                "");
    }

    @Test
    public void nestedCounterForLoop_aboveBound() {
        Options.get().enableLoopUnrolling(2);
        Misc.runSource(
                "var k = 0;",
                "for(var i = 0; i < 5; i++){",
                "   for(var j = 0; j < 5; j++){",
                "       k++;",
                "   }",
                "}",
                "TAJS_assert(i, 'isMaybeNumUInt');",
                "TAJS_assert(j, 'isMaybeNumUInt');",
                "TAJS_assert(k, 'isMaybeNumUInt');",
                "TAJS_assert(i, 'isMaybeSingleNum', false);",
                "TAJS_assert(j, 'isMaybeSingleNum', false);",
                "TAJS_assert(k, 'isMaybeSingleNum', false);",
                "");
    }

    @Test
    public void stringShrinkingWhileLoop_aboveBound() {
        Options.get().enableLoopUnrolling(2);
        Misc.runSource(
                "var s = 'abcdefg';",
                "while(s){ s = s.substring(1); }",
                "TAJS_assert(s, 'isMaybeSingleStr', false);",
                "TAJS_assert(s, 'isMaybeAnyStr||isMaybeStrIdentifier');",
                "");
    }
}
