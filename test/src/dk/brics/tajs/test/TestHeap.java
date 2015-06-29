package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

public class TestHeap {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void single() {
        Misc.init();
        Misc.runSource("",
                "var o = {};",
                "TAJS_assert(o, 'isMaybeSingleObjectLabel', true);",
                "TAJS_assert(o, 'isNotASummarizedObject', true);",
                "TAJS_assert(o, 'isNotASingletonObject', false);",
                "");
    }

    @Test
    public void single_loop() {
        Misc.init();
        Misc.runSource("",
                "var o;",
                "while(Math.random()){",
                "   o = {};",
                "}",
                "TAJS_assert(o, 'isMaybeSingleObjectLabel', true);",
                "TAJS_assert(o, 'isNotASummarizedObject', true);",
                "TAJS_assert(o, 'isNotASingletonObject', false);",
                "");
    }

    @Test
    public void single_loop_summarize() {
        Misc.init();
        Misc.runSource("",
                "var o;",
                "while(Math.random()){",
                "   var fresh = {};",
                "   if(o === undefined || Math.random()){",
                "       o = fresh;",
                "   }",
                "}",
                "TAJS_assert(o, 'isMaybeSingleAllocationSite', true);",
                "TAJS_assert(o, 'isNotASummarizedObject', false);",
                "TAJS_assert(o, 'isNotASingletonObject', false);",
                ""
        );
    }

    @Test
    public void function_single() {
        Misc.init();
        Misc.runSource("",
                "var o = (function(){return {};}());",
                "TAJS_assert(o, 'isMaybeSingleObjectLabel', true);",
                "TAJS_assert(o, 'isNotASummarizedObject', true);",
                "TAJS_assert(o, 'isNotASingletonObject', false);",
                "");
    }

    @Test
    public void function_single_loop() {
        Misc.init();
        Misc.runSource("",
                "var o = (function(){",
                "   var o;",
                "   while(Math.random()){",
                "      o = {};",
                "   }",
                "   return o;",
                "}());",
                "TAJS_assert(o, 'isMaybeSingleObjectLabel', true);",
                "TAJS_assert(o, 'isNotASummarizedObject', true);",
                "TAJS_assert(o, 'isNotASingletonObject', false);",
                "");
    }

    @Test
    public void function_single_loop_summarize() {
        Misc.init();
        Misc.runSource("",
                "var o = (function(){",
                "   var o;",
                "   while(Math.random()){",
                "     var fresh = {};",
                "     if(o === undefined || Math.random()){",
                "         o = fresh;",
                "      }",
                "   }",
                "   return o;",
                "}());",
                "TAJS_assert(o, 'isMaybeSingleAllocationSite', true);",
                "TAJS_assert(o, 'isNotASummarizedObject', false);",
                "TAJS_assert(o, 'isNotASingletonObject', false);",
                ""
        );
    }

    @Test
    public void single_summarize_with_branch() {
        Misc.init();
        Misc.runSource("",
                "var o;",
                "function f(){return {};}",
                "o = f();",
                "if(Math.random()){",
                "   var fresh = f();",
                "   if(Math.random()){",
                "      o = fresh;",
                "   }",
                "}",
                "TAJS_assert(o, 'isMaybeSingleAllocationSite', true);",
                "TAJS_assert(o, 'isNotASummarizedObject', false);",
                "TAJS_assert(o, 'isNotASingletonObject', false);",
                "");
    }

    @Test
    public void single_summarize_in_function() {
        Misc.init();
        Misc.runSource("",
                "var o;",
                "function f(){var fresh = {}; if(o === undefined || Math.random()){ o = fresh; }}",
                "f();",
                "if(Math.random()){",
                "   f();",
                "}",
                "TAJS_assert(o, 'isMaybeSingleAllocationSite', true);",
                "TAJS_assert(o, 'isNotASummarizedObject', false);",
                "TAJS_assert(o, 'isNotASingletonObject', false);",
                "");
    }
}
