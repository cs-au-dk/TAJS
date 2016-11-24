package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.monitoring.OrdinaryExitReachableChecker;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

/**
 * Semantic unit-tests for the for-in construct with for-in specialization.
 */
@SuppressWarnings("static-method")
public class TestForIn {

    private IAnalysisMonitoring monitor;

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        monitor = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableChecker());
    }

    @Test
    public void noProperties() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "for(var p in {}){",
                "}",
                "TAJS_assert(x, 'isNotUndef', false);",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void oneProperty() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "for(var p in {a: 'a'}){",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoProperties() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesCopying() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var o1 = {};",
                "var o2 = {a: 'a', b: 'b'};",
                "for(var p in o2){",
                "   o1[p] = o2[p];",
                "}",
                "o1.a.KILL_UNDEFINED",
                "o1.b.KILL_UNDEFINED",
                "TAJS_assert(o1.a === 'a');",
                "TAJS_assert(o1.b === 'b');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void onePropertyContinue() {
        Misc.runSource(new String[]{"",
                "var x;",
                "for(var p in {a: 'a'}){",
                "   x = p;",
                "   continue;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesContinue() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   x = p;",
                "   continue;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void onePropertyBreak() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "for(var p in {a: 'a'}){",
                "   x = p;",
                "   break;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesBreak() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   x = p;",
                "   break;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void throwing() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "for(var p in {a: 'a'}){",
                "   throw 42;",
                "}",
                ""}, monitor);
    }

    @Test
    public void catching() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "try {",
                "   for(var p in {a: 'a'}){",
                "       throw 42;",
                "   }",
                "}catch(e){",
                "}",
                ""}, monitor);
    }

    @Test
    public void breaking() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "for(var p in {a: 'a'}){",
                "   break;",
                "}",
                ""}, monitor);
    }

    @Test
    public void returning() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "(function(){",
                "   for(var p in {a: 'a'}){",
                "       return;",
                "   }",
                "})();",
                ""}, monitor);
    }

    @Test
    public void throwingAndCatchingValue() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "try {",
                "   for(var p in {a: 'a'}){",
                "       throw 42;",
                "   }",
                "}catch(e){",
                "   TAJS_assert(e, 'isMaybeSingleNum');",
                "   TAJS_assert(e, 'isMaybeUndef', false);",
                "}",
                ""}, monitor);
    }

    @Test
    public void throwingValueThroughFunction() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "try {",
                "   (function(){",
                "       for(var p in {a: 'a'}){",
                "           return 42;",
                "       }",
                "})();",
                "}catch(e){",
                "   TAJS_assert(e, 'isMaybeSingleNum');",
                "   TAJS_assert(e, 'isMaybeUndef');",
                "}",
                ""}, monitor);
    }

    @Test
    public void returningValue() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var v = (function(){",
                "   for(var p in {a: 'a'}){",
                "       return 42;",
                "   }",
                "})();",
                "TAJS_assert(v, 'isMaybeSingleNum');",
                "TAJS_assert(v, 'isMaybeUndef');",
                ""}, monitor);
    }

    @Test
    public void onePropertyThrow() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "try{",
                "   for(var p in {a: 'a'}){",
                "       x = p;",
                "       throw 42;",
                "   }",
                "} catch(e){}",
                "x.KILL_UNDEFINED;",
                "TAJS_assert(x === 'a');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesThrow() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "try{",
                "   for(var p in {a: 'a', b: 'b'}){",
                "      x = p;",
                "      throw 42;",
                "   }",
                "} catch(e){}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void onePropertyReturnConstant() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "function f(){",
                "   for(var p in {a: 'a'}){",
                "       return 'x';",
                "   }",
                "}",
                "var x = f();",
                "x.KILL_UNDEFINED;",
                "TAJS_assert(x === 'x');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void onePropertyReturn() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "function f(){",
                "   var x;",
                "   for(var p in {a: 'a'}){",
                "       x = p;",
                "       return x;",
                "   }",
                "}",
                "var x = f();",
                "x.KILL_UNDEFINED;",
                "TAJS_assert(x === 'a');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesReturn() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "function f(){",
                "   var x;",
                "   for(var p in {a: 'a', b: 'b'}){",
                "       x = p;",
                "       return x;",
                "   }",
                "}",
                "var x = f();",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesMaybeContinue() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var u = Math.random() === 0;",
                "var x;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   if(u){",
                "       continue;",
                "   }",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void readingVariablesFromOutSideLoop_bug() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "(function(){",
                "var u = Math.random();",
                "for(var p in {a: 'a'}){",
                "   if(u){",
                "       break;",
                "   }",
                "}",
                "TAJS_dumpValue('OK');",
                "})();",
                ""}, monitor);
    }

    @Test
    public void readingPropertiesFromOutSideLoop_bug() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var o = { u: Math.random() };",
                "for(var p in {a: 'a'}){",
                "   if(o.u){",
                "       break;",
                "   }",
                "}",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesMaybeBreak() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var u = Math.random() === 0;",
                "var x;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   x = p;",
                "   if(u){",
                "       break;",
                "   }",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesContinueOnSpecific() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   if(p === 'a'){",
                "       continue;",
                "   }",
                "   x = p;",
                "}",
                "x.KILL_UNDEFINED;",
                "TAJS_assert(p, 'isMaybeSingleStr', false);",
                "TAJS_assert(p, 'isMaybeStrIdentifier');",
                "TAJS_assert(x === 'b');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesBreakOnSpecific() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   if(p === 'a'){",
                "       break;",
                "   }",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesBreakOnSpecific_weakOtherIterations() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "var o = {}",
                "for(var p in {a: 'a', b: 'b'}){",
                "   o[p] = p",
                "   if(p === 'a'){",
                "       break;",
                "   }",
                "}",
                "TAJS_assert(o.a, 'isMaybeSingleStr');",
                "TAJS_assert(o.a, 'isMaybeUndef');",
                "TAJS_assert(o.b, 'isMaybeSingleStr');",
                "TAJS_assert(o.b, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesBreakOnSpecific_weakOtherIterations_buggy() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "var o = {}",
                "var o2 = {a: 'a', b: 'b'}",
                "for(var p in o2){",
                "   o[p] = o2[p]",
                "   if(p === 'a'){",
                "       break;",
                "   }",
                "}",
                "TAJS_assert(o.a, 'isMaybeSingleStr');",
                "TAJS_assert(o.a, 'isMaybeUndef');",
                "TAJS_assert(o.b, 'isMaybeSingleStr');",
                "TAJS_assert(o.b, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesConflict1() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x = '';",
                "for(var p in {a: 'a', b: 'b'}){",
                "   x += p;",
                "}",
                "TAJS_assert(x, 'isMaybeStrIdentifierParts');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesConflict2() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x = '';",
                "var o = {a: 'a', a: 'b'};",
                "for(var p in o){",
                "   x += o[p];",
                "}",
                "TAJS_assert(x, 'isMaybeStrIdentifierParts');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesConflict3() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x = 0;",
                "var o = {a: 1, b: 2};",
                "for(var p in o){",
                "   x += o[p];",
                "}",
                "TAJS_assert(x, 'isMaybeNumUInt');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoPropertiesConflict3_continue() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x = 0;",
                "var o = {a: 1, b: 2};",
                "for(var p in o){",
                "   x += o[p];",
                "   continue;",
                "}",
                "TAJS_assert(x, 'isMaybeNumUInt');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void addingProperties() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "var o = {a: 'a'};",
                "for(var p in o){",
                "   if(p === 'a'){ o.b = 'b'; }",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void deletingProperties1() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "var o = {a: 'a', b: 'b'};",
                "for(var p in o){",
                "   delete o.b;",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void deletingProperties2() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "var o = {a: 'a', b: 'b'};",
                "for(var p in o){",
                "   delete o.a;",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void mutatingProperty() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "var o = {a: 'a'};",
                "for(var p in o){",
                "   x = o.a;",
                "   o.a = 'b'",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void mutatingProperties() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "var o = {a: 'a', b: 'b'};",
                "for(var p in o){",
                "   x = o[p];",
                "   o[p] = o[p] + o[p];",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifierParts');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void arrayProperties() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "for(var p in ['a', 'b']){",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrOnlyUInt');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void repeatedAllocations() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var o;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   o = {};",
                "   o[p] = p;",
                "}",
                "TAJS_assert(o, 'isMaybeUndef');",
                "TAJS_assert(o, 'isMaybeObject');",
                "TAJS_assert(o.a, 'isMaybeSingleStr');",
                "TAJS_assert(o.a, 'isMaybeUndef');",
                "TAJS_assert(o.b, 'isMaybeSingleStr');",
                "TAJS_assert(o.b, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void repeatedGuardedAllocations() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var o;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   o = o? o: {};",
                "   o[p] = p;",
                "}",
                "TAJS_assert(o, 'isMaybeUndef');",
                "TAJS_assert(o, 'isMaybeObject');",
                "TAJS_assert(o.a, 'isMaybeSingleStr');",
                "TAJS_assert(o.a, 'isMaybeUndef');",
                "TAJS_assert(o.b, 'isMaybeSingleStr');",
                "TAJS_assert(o.b, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void defaultArrayProperties() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var o = {};",
                "var x = {a: 'a', b: 'b'};",
                "x[Math.random()? 0: 1] = 'c';",
                "for(var p in x){",
                "   o[p] = p;",
                "}",
                "TAJS_assert(o.a, 'isMaybeSingleStr');",
                "TAJS_assert(o.a, 'isMaybeUndef');",
                "TAJS_assert(o.b, 'isMaybeSingleStr');",
                "TAJS_assert(o.b, 'isMaybeUndef');",
                "TAJS_assert(o[42], 'isMaybeStrOnlyUInt');",
                "TAJS_assert(o[42], 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void defaultNonArrayProperties() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var o = {};",
                "var x = {0: 0, 1: 1};",
                "x[Math.random()? 'a': 'b'] = 'c';",
                "for(var p in x){",
                "   o[p] = x[p];",
                "}",
                "TAJS_assert(o[0], 'isMaybeSingleNum');",
                "TAJS_assert(o[0], 'isMaybeUndef');",
                "TAJS_assert(o[1], 'isMaybeSingleNum');",
                "TAJS_assert(o[1], 'isMaybeUndef');",
                "TAJS_assert(o.abc, 'isMaybeSingleStr');",
                "TAJS_assert(o.abc, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void twoMaybeProperties() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "var o = {};",
                "var U = !!Math.random();",
                "if(U){ o.a = 'a'; o.b = 'b'; }",
                "for(var p in o){",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void nestedForIns() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "var o = {};",
                "for(var p1 in {a: 'a', b: 'b'}){",
                "   for(var p2 in {a: 'a', b: 'b'}){",
                "       x = p1 + p2;",
                "       o[x] = x",
                "   }",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "o.aa.KILL_UNDEFINED;",
                "TAJS_assert(o.aa === 'aa');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void forInRegularLoop() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "var n = 0;",
                "for(var i = 0; i < 3; i++){",
                "   for(var p in {a: 'a', b: 'b'}){",
                "       x = p;",
                "       n++",
                "   }",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_assert(n, 'isMaybeNumUInt');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void assignmentToLoopVariable() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "for(var p in {a: 'a'}){",
                "   p = 42;",
                "}",
                ""}, monitor);
    }

    @Test
    public void statementAfterContinue() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "for(var p in {a: 'a'}){",
                "   continue;",
                "   42;",
                "}",
                ""}, monitor);
    }

    @Test
    public void assignmentToLoopVariableAfterContiue() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "for(var p in {a: 'a'}){",
                "   continue;",
                "   p = 42;",
                "}",
                ""}, monitor);
    }

    @Test
    public void twoProperties_propertyAssignment() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "var x;",
                "var o = {};",
                "for(o.p in {a: 'a', b: 'b'}){",
                "   x = o.p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');",
                ""}, monitor);
    }

    @Test
    public void lhsEvaluatedInsideLoop() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "function lhs(){ TAJS_assert(false); }",
                "for(lhs().p in {}){",
                "}",
                ""}, monitor);
    }

    @Test
    public void lhsEvaluatedAfterRhsLoop() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "function lhs(){ TAJS_assert(isAfterRhs === true); goesIntoLoop = true; return {};}",
                "var isAfterRhs = false;",
                "var goesIntoLoop = false;",
                "function make(){isAfterRhs = true; return {a: 'a'}};",
                "for(lhs().p in make()){",
                "}",
                "TAJS_assert(goesIntoLoop, 'isMaybeAnyBool');",
                "TAJS_assert(goesIntoLoop, 'isMaybeFalseButNotTrue', false);",
                "TAJS_assert(goesIntoLoop, 'isMaybeTrueButNotFalse', false);",
                ""}, monitor);
    }

    @Test
    public void regression_NullPointerException() {
        Misc.init();
        Misc.runSource(new String[]{"",
                "function merge(root){",
                "  for ( var i = 0; i < arguments.length; i++ )",
                "    for ( var key in arguments[i] )",
                "      root[key] = arguments[i][key];",
                "}",
                "merge({p: 'p'}, {q: 'q'});",
                ""}, monitor);
    }

    @Test
    public void iterateUndefined() {
        Misc.init();
        // should not crash
        Misc.runSource(new String[]{"",
                "for(var key in undefined);",
                ""}, monitor);
    }

    @Test
    public void iterateNull() {
        Misc.init();
        // should not crash
        Misc.runSource(new String[]{"",
                "for(var key in null);",
                ""}, monitor);
    }

    @Test
    public void iterateNumber() {
        Misc.init();
        // should not crash
        Misc.runSource(new String[]{"",
                "for(var key in 42);",
                ""}, monitor);
    }

    @Test
    public void iterateTrueEmpty() {
        Misc.init();
        // should not crash
        Misc.runSource(new String[]{"",
                "for(var key in Object.create(null));",
                ""}, monitor);
    }

    @Test
    public void iterateEmpty() {
        Misc.init();
        // should not crash
        Misc.runSource(new String[]{"",
                "for(var key in {});",
                ""}, monitor);
    }

    @Test
    public void iterateEmptyArray() {
        Misc.init();
        // should not crash
        Misc.runSource(new String[]{"",
                "for(var key in []);",
                ""}, monitor);
    }

    @Test
    public void nestedForIn() {
        // should not crash
        Misc.init();
        Misc.runSource(new String[]{
                "var o = {x: 'x'};",
                "for(var p1 in o){",
                "   for(var p2 in o){}",
                "}",
                ""}, monitor);
    }

    @Test
    public void sequencedForIn() {
        // should not crash
        Misc.init();
        Misc.runSource(new String[]{
                "var o = {x: 'x'};",
                "for(var p1 in o){}",
                "for(var p2 in o){}",
                ""}, monitor);
    }

    @Test
    public void breakingForIn() {
        // should not crash
        Misc.init();
        Misc.runSource(new String[]{"",
                "for(var p in {a: 'a'}){",
                "   break;",
                "}",
                ""}, monitor);
    }

    @Test
    public void continuingForIn() {
        // should not crash
        Misc.init();
        Misc.runSource(new String[]{"",
                "for(var p in {a: 'a'}){",
                "   continue;",
                "}",
                ""}, monitor);
    }

    @Test
    public void concatenateIdentifierString() {
        // should not crash
        Misc.init();
        Misc.runSource(new String[]{"",
                "var s = 'a';",
                "for(var p in {b: 'b', c: 'c'}){",
                "   s += p;",
                "}",
                "TAJS_assert(s, 'isMaybeStrIdentifierParts');", // optimal precision would give StrIdentifier
                ""}, monitor);
    }

    @Test
    public void concatenateNumberStrings() {
        // should not crash
        Misc.init();
        Misc.runSource(new String[]{"",
                "var s = '1';",
                "for(var p in {2: '2', 3: '3'}){",
                "   s += p;",
                "}",
                "TAJS_assert(s, 'isMaybeStrIdentifierParts');", // optimal precision would not allow this case
                "TAJS_assert(s, 'isMaybeStrUInt');",
                ""}, monitor);
    }

    @Test
    public void unorderedForInImplementation_withLazyPropagation() {
        Misc.init();
        Misc.captureSystemOutput();
        Misc.runSource(
                "var o1 = {x: 'A', y: 'A'};",
                "var o2 = {};",
                "for(var p in o1){",
                "   o2[p] = o1[p];",
                "   TAJS_dumpObject(o2);",
                "}",
                "TAJS_dumpObject(o2);"
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void unorderedForInImplementation_withSemiLazyPropagation() {
        Misc.init();
        Misc.captureSystemOutput();
        Misc.runSource(
                "var o1 = {x: 'A', y: 'A'};",
                "var o2 = {};",
                "for(var p in o1){",
                "   o2[p] = o1[p];",
                "   o2.x === o2.y",
                "   TAJS_dumpObject(o2);",
                "}",
                "TAJS_dumpObject(o2);"
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void exceptionInBody() {
        Misc.init();
        Misc.runSource(new String[]{
                "var v = true;",
                "for(var p in {'a': 'a'}){",
                "   v = false;",
                "   FAIL;",
                "}",
                "TAJS_assert(v);",
                ""}, monitor);
    }

    @Test
    public void functionWithForInAndLazyPropagationRecovery() {
        // should not crash
        Misc.init();
        Misc.runSource(new String[]{"",
                "var o1 = {};",
                "function f(o2) {",
                "   for (var name in o2) {",
                "	    o1.p = o2[name]",
                "   }",
                "}",
                "f({a: 'a'});",
                ""}, monitor);
    }

    @Test
    public void allocationInBody() {
        // should not crash
        Misc.init();
        Misc.runSource(new String[]{
                "for(var p in {'a': 'a', b: 'b'}){",
                "   ({});",
                "}",
                ""}, monitor);
    }

    @Test
    public void compatibleWithLoopUnrolling1() {
        Misc.init();
        Options.get().enableLoopUnrolling(50);
        Misc.runSource(new String[]{"",
                "for(var p in {a: 'a', b: 'b'}){",
                "   for(var i = 0; i < 3; i++){",
                "       TAJS_assert(i, 'isMaybeSingleNumUInt');",
                "   }",
                "}",
                ""}, monitor);
    }

    @Test
    public void compatibleWithLoopUnrolling2() {
        Misc.init();
        Options.get().enableLoopUnrolling(50);
        Misc.runSource(new String[]{"",
                "for(var i = 0; i < 3; i++){",
                "   for(var p in {a: 'a', b: 'b'}){",
                "       TAJS_assert(i, 'isMaybeSingleNumUInt');",
                "   }",
                "}",
                ""}, monitor);
    }

    @Test
    public void compatibleWithLoopUnrolling1_whenDisabled() {
        Misc.init();
        Options.get().enableLoopUnrolling(50);
        Options.get().enableNoForInSpecialization();
        Misc.runSource(new String[]{"",
                "for(var p in {a: 'a', b: 'b'}){",
                "   for(var i = 0; i < 3; i++){",
                "       TAJS_assert(i, 'isMaybeSingleNumUInt');",
                "   }",
                "}",
                ""}, monitor);
    }

    @Test
    public void compatibleWithLoopUnrolling2_whenDisabled() {
        Misc.init();
        Options.get().enableLoopUnrolling(50);
        Options.get().enableNoForInSpecialization();
        Misc.runSource(new String[]{"",
                "for(var i = 0; i < 3; i++){",
                "   for(var p in {a: 'a', b: 'b'}){",
                "       TAJS_assert(i, 'isMaybeSingleNumUInt');",
                "   }",
                "}",
                ""}, monitor);
    }

}
