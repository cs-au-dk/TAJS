package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.js2flowgraph.FunctionBuilder;
import dk.brics.tajs.options.Options;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestUnrolling {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableLoopUnrolling(100);
    }

    @Test
    public void loopunrolling_flowgraph_strings() {
        // reveals ordinary flows
        Misc.init();
        Options.get().enableFlowgraph();
        Misc.captureSystemOutput();
        Misc.runSourceWithNamedFile("loopunrolling_flowgraph_strings",
                "'PRE'; for('INIT'; 'COND'; 'INC'){ 'BODY'; } 'POST';",
                "");
        Misc.checkSystemOutput();
    }

    @Test
    public void loopunrolling_flowgraph_calls() {
        // reveals exceptional flows
        Misc.init();
        Options.get().enableFlowgraph();
        Misc.captureSystemOutput();
        Misc.runSourceWithNamedFile("loopunrolling_flowgraph_calls",
                "PRE(); for(INIT(); COND(); INC()){ BODY(); } POST();",
                "");
        Misc.checkSystemOutput();
    }

    @Test
    public void loopunrolling_flowgraph_continueBreak() {
        Misc.init();
        Options.get().enableFlowgraph();
        Misc.captureSystemOutput();
        Misc.runSourceWithNamedFile("loopunrolling_flowgraph_continueBreak",
                "'PRE'; for('INIT'; 'COND'; 'INC'){ 'BODY0'; if('CONTINUE'){continue;} 'BODY1'; if('BREAK'){break;}; 'BODY2';} 'POST';",
                "");
        Misc.checkSystemOutput();
    }

    @Test
    public void loopunrolling_flowgraph_continue2() {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableFlowgraph();
        Misc.runSourceWithNamedFile("loopunrolling_flowgraph_continue2.js",
                "while1: while('COND1'){ while2: while('COND2'){continue while2;}}",
                "");
        Misc.checkSystemOutput();
    }

    @Test
    public void loopunrolling_flowgraph_labelledcontinue() {
        Misc.init();
        Options.get().enableFlowgraph();
        Misc.captureSystemOutput();

        Misc.runSourceWithNamedFile("loopunrolling_flowgraph_labelledcontinue.js",
                "function labelled(){label: while('COND'){continue label;}}",
                "function unlabelled(){while('COND'){continue;}}",
                "");
        Misc.checkSystemOutput();
    }

    @Test
    public void deadWhileLoop() {
        Misc.init();
        Misc.runSource(
                "var v = true;",
                "while(false){ v = false; }",
                "TAJS_assert(v);",
                "");
    }

    @Test
    public void deadForLoop() {
        Misc.init();
        Misc.runSource(
                "var v = true;",
                "while(false){ v = false; }",
                "TAJS_assert(v);",
                "");
    }

    @Test
    public void counterWhileLoop() {
        Misc.init();
        Misc.runSource(
                "var i = 0;",
                "while(i < 5){ i++; }",
                "TAJS_assert(i === 5);",
                "");
    }

    @Test
    public void smallCounterForLoop() {
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i === 0; i++){ " ,
                "}",
                "TAJS_assert(i === 1);",
                "");
    }

    @Test
    public void smallCounterForLoopWLoopVariableFunctionCallRead() {
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 3; i++){ (function(){i;})(); }",
                "TAJS_assert(i === 3);",
                "");
    }

    @Test
    public void smallCounterForLoopWLoopVariableFunctionCallReadWrite() {
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 3; i++){ (function(){i = i;})(); }",
                "TAJS_assert(i === 3);",
                "");
    }

    @Test
    public void smallCounterForLoopWObjectAllocation_1() {
        Misc.init();
        Misc.runSource(
                "var o;",
                "for(var i = 0; i < 1; i++){ o = {}; }",
                "TAJS_assert(o, 'isNotASummarizedObject');",
                "");
    }

    @Test
    public void smallCounterForLoopWObjectAllocation_2() {
        Misc.init();
        Misc.runSource(
                "var o;",
                "for(var i = 0; i < 2; i++){ o = {}; }",
                "TAJS_assert(o, 'isNotASummarizedObject');", "");
    }

    @Test
    public void smallCounterForLoopWObjectAllocation_3() {
        Misc.init();
        Misc.runSource(
                "var o;",
                "for(var i = 0; i < 3; i++){ o = {}; }",
                "TAJS_assert(o, 'isNotASummarizedObject');",
                "");
    }

    @Test
    public void smallCounterWrite() {
        Misc.init();
        Misc.runSource(
                "var j = 0;",
                "for(var i = 0; i < 3; i++){ j = i; }",
                "TAJS_assert(j === 2);",
                "");
    }

    @Test
    public void smallCounterForLoopWFunctionAllocation() {
        Misc.init();
        Misc.runSource(
                "var o;",
                "for(var i = 0; i < 3; i++){ o = function(){}; }",
                "TAJS_assert(o, 'isNotASummarizedObject');",
                "");
    }

    @Test
    public void smallCounterForLoopWFunctionCall() {
        Misc.init();
        Misc.runSource(
                "var o;",
                "for(var i = 0; i < 3; i++){ o = (function(){ return {};})(); }",
                "TAJS_assert(o, 'isNotASummarizedObject');",
                "");
    }

    @Test
    public void smallCounterForLoopWOuterVariableFunctionCallRead() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "var j = 0;",
                "for(var i = 0; i < 3; i++){ (function(){j;})(); }",
                "");
    }

    @Test
    public void smallCounterForLoopWOuterVariableFunctionCallWriteConstant() {
        Misc.init();
        Misc.runSource(
                "var j = 0;",
                "for(var i = 0; i < 3; i++){ (function(){j = 1;})(); }",
                "TAJS_assert(j === 1);",
                "");
    }

    @Test
    public void smallCounterForLoopWOuterVariableFunctionCallWrite() {
        Misc.init();
        Options.get().enableNoPolymorphic();
        Misc.runSource(
                "var j = 0;",
                "for(var i = 0; i < 3; i++){ (function(){j = i;})(); }",
                "TAJS_assert(j, 'isMaybeNumUInt');",
                "");
    }

    @Test
    public void smallCounterForLoopWOuterVariableFunctionCallReadWrite() {
        Misc.init();
        Misc.runSource(
                "var j = 0;",
                "for(var i = 0; i < 3; i++){ (function(){j++;})(); }",
                "TAJS_assert(j, 'isMaybeNumUInt');",
                "");
    }

    @Test
    public void smallCounterForLoopWInnerVariableFunctionCallRead() {
        // should not crash
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 3; i++){ var j = 0; (function(){j;})(); }",
                "");
    }

    @Test
    public void smallCounterForLoopWInnerVariableFunctionCallWriteConstant() {
        Misc.init();
        Misc.runSource(
                "var j = 0;",
                "for(var i = 0; i < 3; i++){ var j = 0; (function(){j = 1;})(); TAJS_assert(j === 1); }",
                "");
    }

    @Test
    public void smallCounterForLoopWInnerVariableFunctionCallWrite() {
        Misc.init();
        Misc.runSource(
                "var j = 0;",
                "for(var i = 0; i < 3; i++){ var j = 0; (function(){j = i;})(); TAJS_assert(j, 'isMaybeSingleNum||isMaybeNumUInt'); }",
                "");
    }

    @Test
    public void smallCounterForLoopWInnerVariableFunctionCallReadWrite() {
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 3; i++){ var j = 0; (function(){j++;})(); TAJS_assert(j === 1);}",
                "");
    }

    @Test
    public void counterForLoop() {
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 5; i++){ }",
                "TAJS_assert(i === 5);",
                "");
    }

    @Test
    public void counterForLoopInFunction() {
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   for(var i = 0; i < 5; i++){ }",
                "   TAJS_assert(i === 5);",
                "})();",
                "");
    }

    @Test
    public void counterForLoopThroughClosures() {
        Misc.init();
        Misc.runSource(
                "var i;",
                "(function(){",
                "   for(i = 0; i < 5; i++){ }",
                "})();",
                "TAJS_assert(i === 5);",
                "");
    }

    @Test
    public void sequencedCounterForLoops() {
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 5; i++){ }",
                "for(; i < 10; i++){ }",
                "TAJS_assert(i === 10);",
                "");
    }

    @Test
    public void sequencedForLoopThroughClosures() {
        Misc.init();
        Misc.runSource(
                "var i;",
                "(function(){",
                "   for(i = 0; i < 5; i++){ }",
                "})();",
                "(function(){",
                "   for(; i < 10; i++){ }",
                "})();",
                "TAJS_assert(i === 10);",
                "");
    }

    @Test
    public void nestedCounterForLoop() {
        Misc.init();
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
    public void nestedCounterForLoopWithCalls() {
        Misc.init();
        Misc.runSource(
                "function f(){}",
                "var k = 0;",
                "for(var i = 0; i < 5; i++){",
                "   f();",
                "   for(var j = 0; j < 5; j++){",
                "       f();",
                "       k++;",
                "       f();",
                "   }",
                "   f();",
                "}",
                "TAJS_assert(i === 5);",
                "TAJS_assert(j === 5);",
                "TAJS_assert(k === 25);",
                "");
    }

    @Test
    public void nestedCounterForLoopWithReadingCalls() {
        Misc.init();
        Misc.runSource(
                "var k = 0;",
                "function f(){k;}",
                "for(var i = 0; i < 5; i++){",
                "   f();",
                "   for(var j = 0; j < 5; j++){",
                "       f();",
                "       k++;",
                "       f();",
                "   }",
                "   f();",
                "}",
                "TAJS_assert(i === 5);",
                "TAJS_assert(j === 5);",
                "TAJS_assert(k === 25);",
                "");
    }

    @Test
    public void nestedDeterminacyChecks() {
        Misc.init();
        Misc.runSource(
                "var c1 = true; var c2 = true;",
                "for(var i = 0; c1; i++){",
                "   TAJS_assert(c1);",
                "   c1 = i < 5;",
                "   for(var j = 0; c2; j++){",
                "       TAJS_assert(c2);",
                "       c2 = j < 5;",
                "   }",
                "}",
                "TAJS_assert(!c1);",
                "TAJS_assert(!c2);",
                "");
    }

    @Test
    public void nestedDeterminacyChecks_2() {
        Misc.init();
        Misc.runSource(
                "var c1 = true; var c2 = true;",
                "var c1_copy = c1; var c2_copy = c2;",
                "for(var i = 0; c1_copy; i++){",
                "   TAJS_assert(c1);",
                "   c1 = i < 5;",
                "   c1_copy = c1;",
                "   for(var j = 0; c2_copy; j++){",
                "       TAJS_assert(c2);",
                "       c2 = j < 5;",
                "       c2_copy = c2;",
                "   }",
                "}",
                "TAJS_assert(!c1);",
                "TAJS_assert(!c2);",
                "");
    }

    @Test
    public void breakInCounterForLoop() {
        Misc.init();
        Misc.runSource(
                "for(var i = 0; i < 5; i++){",
                "   if(i === 4){ break; }",
                "}",
                "TAJS_assert(i === 4);",
                "");
    }

    @Test
    public void indeterminateForLoop() {
        Misc.init();
        Misc.runSource(
                "var i = 0;",
                "var R = !!Math.random();",
                "for(;R;){ i++; }",
                "TAJS_assert(i, 'isMaybeNumUInt');",
                "TAJS_assert(i, 'isMaybeSingleNum', false);",
                "");
    }

    @Test
    public void indeterminateStdForLoop() {
        Misc.init();
        Misc.runSource(
                "var R = !!Math.random();",
                "for(var i = 0;R; i++){ }",
                "TAJS_assert(i, 'isMaybeNumUInt');",
                "TAJS_assert(i, 'isMaybeSingleNum', false);",
                "");
    }

    @Test
    public void indeterminateWhileLoop() {
        Misc.init();
        Misc.runSource(
                "var i = 0;",
                "var R = !!Math.random();",
                "while(R){ i++; }",
                "TAJS_assert(i, 'isMaybeNumUInt');",
                "TAJS_assert(i, 'isMaybeSingleNum', false);",
                "");
    }

    @Test
    public void indeterminateWhileLoop_InFunction() {
        Misc.init();
        Misc.runSource(
                "var i = 0;",
                "(function(){",
                "   var R = !!Math.random();",
                "   while(R){ i++; }",
                "})();",
                "TAJS_assert(i, 'isMaybeNumUInt');",
                "TAJS_assert(i, 'isMaybeSingleNum', false);",
                "");
    }

    @Test
    public void stringShrinkingWhileLoop_empty() {
        Misc.init();
        Misc.runSource(
                "var s = 'abcdefg';",
                "while(s){ s = s.substring(1); }",
                "TAJS_assert(s === '');",
                "");
    }

    @Test
    public void stringShrinkingWhileLoop_nonEmpty() {
        Misc.init();
        Misc.runSource(
                "var s = 'abcdefg';",
                "while(s.length !== 1){ s = s.substring(1); }",
                "TAJS_assert(s === 'g');",
                "");
    }

    @Test
    public void closureVariable() {
        Misc.init();
        Misc.runSource(
                "var f;",
                "for(var i = 0; i < 5; i++){",
                "   f = function(){ return i;}; ",
                "}",
                "TAJS_assert(f, 'isNotASummarizedObject');",
                "TAJS_assert(f() === i);",
                "TAJS_assert(f() === 5);",
                "");
    }

    @Test
    public void fixedClosureVariable() {
        Misc.init();
        Misc.runSource(
                "var f;",
                "for(var i = 0; i < 5; i++){",
                "   f = (function(j){return function(){ return j;}})(i); ",
                "}",
                "TAJS_assert(f, 'isNotASummarizedObject');",
                "TAJS_assert(f() === 4);",
                "");
    }

}
