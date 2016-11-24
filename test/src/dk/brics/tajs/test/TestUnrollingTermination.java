package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

public class TestUnrollingTermination {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableLoopUnrolling(100);
    }

    @Test
    public void nonLoopInFunction() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "function f(){",
                "   if(false){}",
                "};",
                "f()",
                "");
    }

    @Test
    public void zeroLoopInFunction() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "function f(){",
                "   for(var i = 0; i < 0; i++){ }",
                "};",
                "f();",
                "");
    }

    @Test
    public void zeroLoopInFunction2() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   for(var i = 0; i < 0; i++){ }",
                "})();",
                "");
    }

    @Test
    public void onceLoopInFunction() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   for(var i = 0; i < 1; i++){ }",
                "})();",
                "");
    }

    @Test
    public void onceLoopInFunction2() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "function f(){",
                "   for(var i = 0; i < 1; i++){ }",
                "};",
                "f();",
                "");
    }

    @Test
    public void twiceLoopInFunction() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   for(var i = 0; i < 2; i++){ }",
                "})();",
                "");
    }

    @Test
    public void twiceLoopInFunction2() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "function f(){",
                "   for(var i = 0; i < 2; i++){ }",
                "};",
                "f();",
                "");
    }
}
