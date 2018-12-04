package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

/**
 * Semantic unit-tests for the for-in construct with for-in specialization.
 */
@SuppressWarnings("static-method")
public class TestForIn {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void noProperties() {
        Misc.runSource("",
                "var x;",
                "for(var p in {}){",
                "}",
                "TAJS_assert(x, 'isNotUndef', false);",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void oneProperty() {
        Misc.runSource("",
                "var x;",
                "for(var p in {a: 'a'}){",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoProperties() {
        Misc.runSource("",
                "var x;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesCopying() {
        Misc.runSource("",
                "var o1 = {};",
                "var o2 = {a: 'a', b: 'b'};",
                "for(var p in o2){",
                "   o1[p] = o2[p];",
                "}",
                "o1.a.KILL_UNDEFINED",
                "o1.b.KILL_UNDEFINED",
                "TAJS_assert(o1.a === 'a');",
                "TAJS_assert(o1.b === 'b');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void onePropertyContinue() {
        Misc.runSource("",
                "var x;",
                "for(var p in {a: 'a'}){",
                "   x = p;",
                "   continue;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesContinue() {
        Misc.runSource("",
                "var x;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   x = p;",
                "   continue;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void onePropertyBreak() {
        Misc.runSource("",
                "var x;",
                "for(var p in {a: 'a'}){",
                "   x = p;",
                "   break;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesBreak() {
        Misc.runSource("",
                "var x;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   x = p;",
                "   break;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void throwing() {
        Misc.runSource("",
                "for(var p in {a: 'a'}){",
                "   throw 42;",
                "}");
    }

    @Test
    public void catching() {
        Misc.runSource("",
                "try {",
                "   for(var p in {a: 'a'}){",
                "       throw 42;",
                "   }",
                "}catch(e){",
                "}");
    }

    @Test
    public void breaking() {
        Misc.runSource("",
                "for(var p in {a: 'a'}){",
                "   break;",
                "}");
    }

    @Test
    public void returning() {
        Misc.runSource("",
                "(function(){",
                "   for(var p in {a: 'a'}){",
                "       return;",
                "   }",
                "})();");
    }

    @Test
    public void throwingAndCatchingValue() {
        Misc.runSource("",
                "try {",
                "   for(var p in {a: 'a'}){",
                "       throw 42;",
                "   }",
                "}catch(e){",
                "   TAJS_assert(e, 'isMaybeSingleNum');",
                "   TAJS_assert(e, 'isMaybeUndef', false);",
                "}");
    }

    @Test
    public void throwingValueThroughFunction() {
        Misc.runSource("",
                "try {",
                "   (function(){",
                "       for(var p in {a: 'a'}){",
                "           throw 42;",
                "       }",
                "})();",
                "}catch(e){",
                "   TAJS_assert(e, 'isMaybeSingleNum');",
                "   TAJS_assert(e, 'isMaybeUndef', false);",
                "}");
    }

    @Test
    public void returningValue() {
        Misc.runSource("",
                "var v = (function(){",
                "   for(var p in {a: 'a'}){",
                "       return 42;",
                "   }",
                "})();",
                "TAJS_assert(v, 'isMaybeSingleNum');",
                "TAJS_assert(v, 'isMaybeUndef');");
    }

    @Test
    public void onePropertyThrow() {
        Misc.runSource("",
                "var x;",
                "try{",
                "   for(var p in {a: 'a'}){",
                "       x = p;",
                "       throw 42;",
                "   }",
                "} catch(e){}",
                "x.KILL_UNDEFINED;",
                "TAJS_assert(x === 'a');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesThrow() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void onePropertyReturnConstant() {
        Misc.runSource("",
                "function f(){",
                "   for(var p in {a: 'a'}){",
                "       return 'x';",
                "   }",
                "}",
                "var x = f();",
                "x.KILL_UNDEFINED;",
                "TAJS_assert(x === 'x');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void onePropertyReturn() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesReturn() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesMaybeContinue() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void readingVariablesFromOutSideLoop_bug() {
        Misc.runSource("",
                "(function(){",
                "var u = Math.random();",
                "for(var p in {a: 'a'}){",
                "   if(u){",
                "       break;",
                "   }",
                "}",
                "TAJS_dumpValue('OK');",
                "})();");
    }

    @Test
    public void readingPropertiesFromOutSideLoop_bug() {
        Misc.runSource("",
                "var o = { u: Math.random() };",
                "for(var p in {a: 'a'}){",
                "   if(o.u){",
                "       break;",
                "   }",
                "}",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesMaybeBreak() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesContinueOnSpecific() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesBreakOnSpecific() {
        Misc.runSource("",
                "var x;",
                "for(var p in {a: 'a', b: 'b'}){",
                "   if(p === 'a'){",
                "       break;",
                "   }",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesBreakOnSpecific_weakOtherIterations() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesBreakOnSpecific_weakOtherIterations_buggy() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesConflict1() {
        Misc.runSource("",
                "var x = '';",
                "for(var p in {a: 'a', b: 'b'}){",
                "   x += p;",
                "}",
                "TAJS_assert(x, 'isMaybeStrOtherIdentifierParts');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesConflict2() {
        Misc.runSource("",
                "var x = '';",
                "var o = {a: 'a', a: 'b'};",
                "for(var p in o){",
                "   x += o[p];",
                "}",
                "TAJS_assert(x, 'isMaybeStrOtherIdentifierParts');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesConflict3() {
        Misc.runSource("",
                "var x = 0;",
                "var o = {a: 1, b: 2};",
                "for(var p in o){",
                "   x += o[p];",
                "}",
                "TAJS_assert(x, 'isMaybeNumUInt');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoPropertiesConflict3_continue() {
        Misc.runSource("",
                "var x = 0;",
                "var o = {a: 1, b: 2};",
                "for(var p in o){",
                "   x += o[p];",
                "   continue;",
                "}",
                "TAJS_assert(x, 'isMaybeNumUInt');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void addingProperties() {
        Misc.runSource("",
                "var x;",
                "var o = {a: 'a'};",
                "for(var p in o){",
                "   if(p === 'a'){ o.b = 'b'; }",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void deletingProperties1() {
        Misc.runSource("",
                "var x;",
                "var o = {a: 'a', b: 'b'};",
                "for(var p in o){",
                "   delete o.b;",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void deletingProperties2() {
        Misc.runSource("",
                "var x;",
                "var o = {a: 'a', b: 'b'};",
                "for(var p in o){",
                "   delete o.a;",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void mutatingProperty() {
        Misc.runSource("",
                "var x;",
                "var o = {a: 'a'};",
                "for(var p in o){",
                "   x = o.a;",
                "   o.a = 'b'",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void mutatingProperties() {
        Misc.runSource("",
                "var x;",
                "var o = {a: 'a', b: 'b'};",
                "for(var p in o){",
                "   x = o[p];",
                "   o[p] = o[p] + o[p];",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrOtherIdentifierParts');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void arrayProperties() {
        Misc.runSource("",
                "var x;",
                "for(var p in ['a', 'b']){",
                "   x = p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrOnlyUInt');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void repeatedAllocations() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void repeatedGuardedAllocations() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void defaultArrayProperties() {
        Misc.runSource("",
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
                "TAJS_assert(o[42], 'isMaybeStrSomeNumeric');",
                "TAJS_assert(o[42], 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void defaultNonArrayProperties() {
        Misc.runSource("",
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
                "TAJS_assert(o.abc, 'isMaybeSingleStr', false);",
                "TAJS_assert(o.abc, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void twoMaybeProperties() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void nestedForIns() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void forInRegularLoop() {
        Misc.runSource("",
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
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void assignmentToLoopVariable() {
        Misc.runSource("",
                "for(var p in {a: 'a'}){",
                "   p = 42;",
                "}");
    }

    @Test
    public void statementAfterContinue() {
        Misc.runSource("",
                "for(var p in {a: 'a'}){",
                "   continue;",
                "   42;",
                "}");
    }

    @Test
    public void assignmentToLoopVariableAfterContiue() {
        Misc.runSource("",
                "for(var p in {a: 'a'}){",
                "   continue;",
                "   p = 42;",
                "}");
    }

    @Test
    public void twoProperties_propertyAssignment() {
        Misc.runSource("",
                "var x;",
                "var o = {};",
                "for(o.p in {a: 'a', b: 'b'}){",
                "   x = o.p;",
                "}",
                "TAJS_assert(x, 'isMaybeSingleStr', false);",
                "TAJS_assert(x, 'isMaybeStrIdentifier');",
                "TAJS_assert(x, 'isMaybeUndef');",
                "TAJS_dumpValue('OK');");
    }

    @Test
    public void lhsEvaluatedInsideLoop() {
        Misc.runSource("",
                "function lhs(){ TAJS_assert(false); }",
                "for(lhs().p in {}){",
                "}");
    }

    @Test
    public void lhsEvaluatedAfterRhsLoop() {
        Misc.runSource("",
                "function lhs(){ TAJS_assert(isAfterRhs === true); goesIntoLoop = true; return {};}",
                "var isAfterRhs = false;",
                "var goesIntoLoop = false;",
                "function make(){isAfterRhs = true; return {a: 'a'}};",
                "for(lhs().p in make()){",
                "}",
                "TAJS_assert(goesIntoLoop, 'isMaybeAnyBool');",
                "TAJS_assert(goesIntoLoop, 'isMaybeFalseButNotTrue', false);",
                "TAJS_assert(goesIntoLoop, 'isMaybeTrueButNotFalse', false);");
    }

    @Test
    public void regression_NullPointerException() {
        Misc.runSource("",
                "function merge(root){",
                "  for ( var i = 0; i < arguments.length; i++ )",
                "    for ( var key in arguments[i] )",
                "      root[key] = arguments[i][key];",
                "}",
                "merge({p: 'p'}, {q: 'q'});");
    }

    @Test
    public void iterateUndefined() {
        // should not crash
        Misc.runSource("",
                "for(var key in undefined);");
    }

    @Test
    public void iterateNull() {
        // should not crash
        Misc.runSource("",
                "for(var key in null);");
    }

    @Test
    public void iterateNumber() {
        // should not crash
        Misc.runSource("",
                "for(var key in 42);");
    }

    @Test
    public void iterateTrueEmpty() {
        // should not crash
        Misc.runSource("",
                "for(var key in Object.create(null));");
    }

    @Test
    public void iterateEmpty() {
        // should not crash
        Misc.runSource("",
                "for(var key in {});");
    }

    @Test
    public void iterateEmptyArray() {
        // should not crash
        Misc.runSource("",
                "for(var key in []);");
    }

    @Test
    public void nestedForIn() {
        // should not crash
        Misc.runSource(
                "var o = {x: 'x'};",
                "for(var p1 in o){",
                "   for(var p2 in o){}",
                "}");
    }

    @Test
    public void sequencedForIn() {
        // should not crash
        Misc.runSource(
                "var o = {x: 'x'};",
                "for(var p1 in o){}",
                "for(var p2 in o){}");
    }

    @Test
    public void breakingForIn() {
        // should not crash
        Misc.runSource("",
                "for(var p in {a: 'a'}){",
                "   break;",
                "}");
    }

    @Test
    public void continuingForIn() {
        // should not crash
        Misc.runSource("",
                "for(var p in {a: 'a'}){",
                "   continue;",
                "}");
    }

    @Test
    public void concatenateIdentifierString() {
        // should not crash
        Misc.runSource("",
                "var s = 'a';",
                "for(var p in {b: 'b', c: 'c'}){",
                "   s += p;",
                "}",
                "TAJS_assert(s, 'isMaybeStrPrefix');", // optimal precision would give StrIdentifier
                "");
    }

    @Test
    public void concatenateNumberStrings() {
        // should not crash
        Misc.runSource("",
                "var s = '1';",
                "for(var p in {2: '2', 3: '3'}){",
                "   s += p;",
                "}",
                "TAJS_assert(s, 'isMaybeStrPrefix');");
    }

    @Test
    public void unorderedForInImplementation_withLazyPropagation() {
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
        Misc.runSource(
                "var v = true;",
                "for(var p in {'a': 'a'}){",
                "   v = false;",
                "   FAIL;",
                "}",
                "TAJS_assert(v);");
    }

    @Test
    public void functionWithForInAndLazyPropagationRecovery() {
        // should not crash
        Misc.runSource("",
                "var o1 = {};",
                "function f(o2) {",
                "   for (var name in o2) {",
                "	    o1.p = o2[name]",
                "   }",
                "}",
                "f({a: 'a'});");
    }

    @Test
    public void allocationInBody() {
        // should not crash
        Misc.runSource(
                "for(var p in {'a': 'a', b: 'b'}){",
                "   ({});",
                "}");
    }
//    @Test
//    public void copyUnknownProperties_unpresent() {
//
//        Misc.runSource("function x(){};",
//                "function y(){};",
//                "function UNKNOWN(){};",
//                "var o1 = {};",
//                "o1[Math.random()? 'a': 'b'] = UNKNOWN;",
//                "o1.x = x;",
//                "o1.y = y;",
//                "var o2 = {};",
//                "for(var p in o1){",
//                "   o2[p] = o1[p];",
//                "}",
//                "o2.x.KILL_UNDEFINED;",
//                "o2.y.KILL_UNDEFINED;",
//                "o2.z.KILL_UNDEFINED;",
//                "TAJS_assert(o2.toString === Object.prototype.toString, 'isMaybeAnyBool');",
//                "TAJS_assert(o2.toString === UNKNOWN, 'isMaybeAnyBool');",
//                "TAJS_assert(o2.toString !== Object.prototype.toLocaleString);",
//                "TAJS_assert(o2.x === x);",
//                "TAJS_assert(o2.y === y);",
//                "TAJS_assert(o2.z === UNKNOWN);");
//    }
//    @Test
//    public void copyUnknownProperties_present() {
//
//        Misc.runSource("function x(){};",
//                "function y(){};",
//                "function UNKNOWN(){};",
//                "var o1 = {};",
//                "o1[Math.random()? 'a': 'b'] = UNKNOWN;",
//                "o1.x = x;",
//                "o1.y = y;",
//                "var o2 = {x: undefined, y: undefined, z: undefined};",
//                "for(var p in o1){",
//                "   o2[p] = o1[p];",
//                "}",
//                "o2.x.KILL_UNDEFINED;",
//                "o2.y.KILL_UNDEFINED;",
//                "o2.z.KILL_UNDEFINED;",
//                "TAJS_assert(o2.toString === Object.prototype.toString, 'isMaybeAnyBool');",
//                "TAJS_assert(o2.toString === UNKNOWN, 'isMaybeAnyBool');",
//                "TAJS_assert(o2.toString !== Object.prototype.toLocaleString);",
//                "TAJS_assert(o2.x === x);",
//                "TAJS_assert(o2.y === y);",
//                "TAJS_assert(o2.z === UNKNOWN);");
//    }
//    @Test
//    public void copyUnknownProperties_presentAsUnknown() {
//
//        Misc.runSource("function x(){};",
//                "function y(){};",
//                "function UNKNOWN(){};",
//                "function UNKNOWN_ORIG(){};",
//                "var o1 = {};",
//                "o1[Math.random()? 'a': 'b'] = UNKNOWN;",
//                "o1.x = x;",
//                "o1.y = y;",
//                "var o2 = {};",
//                "o2[Math.random()? 'a': 'b'] = UNKNOWN_ORIG;",
//                "o2.x = undefined;",
//                "o2.y = undefined;",
//                "for(var p in o1){",
//                "   o2[p] = o1[p];",
//                "}",
//                "o2.x.KILL_UNDEFINED;",
//                "o2.y.KILL_UNDEFINED;",
//                "o2.z.KILL_UNDEFINED;",
//                "TAJS_assert(o2.toString === Object.prototype.toString, 'isMaybeAnyBool');",
//                "TAJS_assert(o2.toString === UNKNOWN, 'isMaybeAnyBool');",
//                "TAJS_assert(o2.toString !== Object.prototype.toLocaleString);",
//                "TAJS_assert(o2.x === x);",
//                "TAJS_assert(o2.y === y);",
//                "TAJS_assert(o2.z === UNKNOWN_ORIG, 'isMaybeAnyBool')");
//    }
//    @Test
//    public void copyUnknownProperties_presentPrototype() {
//
//        Misc.runSource("function x(){};",
//                "function y(){};",
//                "function UNKNOWN(){};",
//                "var o1 = {};",
//                "o1[Math.random()? 'a': 'b'] = UNKNOWN;",
//                "o1.x = x;",
//                "o1.y = y;",
//                "function K(){}",
//                "K.prototype = {x: undefined, y: undefined, z: undefined};",
//                "var o2 = new K();",
//                "for(var p in o1){",
//                "   o2[p] = o1[p];",
//                "}",
//                "TAJS_assert(o2.toString === Object.prototype.toString, 'isMaybeAnyBool');",
//                "TAJS_assert(o2.toString === UNKNOWN, 'isMaybeAnyBool');",
//                "TAJS_assert(o2.toString !== Object.prototype.toLocaleString);",
//                "TAJS_assertEquals(TAJS_join(x, undefined), o2.x);",
//                "TAJS_assertEquals(TAJS_join(y, undefined), o2.y);",
//                "TAJS_assertEquals(TAJS_join(UNKNOWN, undefined), o2.z);");
//    }
//    @Test
//    public void copyUnknownProperties_array() {
//
//        Misc.runSource("function x(){};",
//                "function y(){};",
//                "function UNKNOWN(){};",
//                "var o1 = [];",
//                "o1[Math.random()? '0': '1'] = UNKNOWN;",
//                "o1[0] = x;",
//                "o1[1] = y;",
//                "var o2 = {};",
//                "for(var p in o1){",
//                "   o2[p] = o1[p];",
//                "}",
//                "o2['0'].KILL_UNDEFINED;",
//                "o2['1'].KILL_UNDEFINED;",
//                "o2['2'].KILL_UNDEFINED;",
//                "TAJS_assert(o2.toString === Object.prototype.toString);",
//                "TAJS_assert(o2[0]=== x);",
//                "TAJS_assert(o2[1] === y);",
//                "TAJS_assert(o2[2] === UNKNOWN);");
//    }
//    @Test
//    public void copyUnknownProperties_arrayNaN() {
//
//        Misc.runSource("function x(){};",
//                "function y(){};",
//                "function UNKNOWN(){};",
//                "var o1 = [];",
//                "o1[(Math.random()? 0: 1) || NaN] = UNKNOWN;",
//                "o1[0] = x;",
//                "o1[1] = y;",
//                "var o2 = {};",
//                "for(var p in o1){",
//                "   o2[p] = o1[p];",
//                "}",
//                "o2['0'].KILL_UNDEFINED;",
//                "o2['1'].KILL_UNDEFINED;",
//                "o2['2'].KILL_UNDEFINED;",
//                "o2['NaN'].KILL_UNDEFINED;",
//                "TAJS_assert(o2.toString === Object.prototype.toString);",
//                "TAJS_assert(o2[0]=== x);",
//                "TAJS_assert(o2[1] === y);",
//                "TAJS_assert(o2[NaN] === UNKNOWN);");
//    }
//    @Test
//    public void copyUnknownProperties_arrayNaN_repeated() {
//
//        Misc.runSource("function x(){};",
//                "function y(){};",
//                "function UNKNOWN(){};",
//                "var o1 = [];",
//                "o1[(Math.random()? 0: 1) || NaN] = UNKNOWN;",
//                "o1[0] = x;",
//                "o1[1] = y;",
//                "var o2 = {};",
//                "for(var p in o1){",
//                "   o2[p] = o1[p];",
//                "}",
//                "o2['0'].KILL_UNDEFINED;",
//                "o2['1'].KILL_UNDEFINED;",
//                "o2['2'].KILL_UNDEFINED;",
//                "o2['NaN'].KILL_UNDEFINED;",
//                "TAJS_assert(o2.toString === Object.prototype.toString);",
//                "TAJS_assert(o2[0]=== x);",
//                "TAJS_assert(o2[1] === y);",
//                "TAJS_assert(o2[NaN] === UNKNOWN);",
//                "var o3 = {};",
//                "for(var p in o2){",
//                "   o3[p] = o2[p];",
//                "}",
//                "o3['0'].KILL_UNDEFINED;",
//                "o3['1'].KILL_UNDEFINED;",
//                "o3['2'].KILL_UNDEFINED;",
//                "o3['NaN'].KILL_UNDEFINED;",
//                "TAJS_assert(o3.toString === Object.prototype.toString);",
//                "TAJS_assert(o3[0]=== x);",
//                "TAJS_assert(o3[1] === y);",
//                "TAJS_assert(o3[NaN] === UNKNOWN);");
//    }

    @Test
    public void compatibleWithLoopUnrolling1() {
        Options.get().enableLoopUnrolling(50);
        Misc.runSource("",
                "for(var p in {a: 'a', b: 'b'}){",
                "   for(var i = 0; i < 3; i++){",
                "       TAJS_assert(i, 'isMaybeSingleNumUInt');",
                "   }",
                "}");
    }

    @Test
    public void compatibleWithLoopUnrolling2() {
        Options.get().enableLoopUnrolling(50);
        Misc.runSource("",
                "for(var i = 0; i < 3; i++){",
                "   for(var p in {a: 'a', b: 'b'}){",
                "       TAJS_assert(i, 'isMaybeSingleNumUInt');",
                "   }",
                "}");
    }

    @Test
    public void compatibleWithLoopUnrolling1_whenDisabled() {
        Options.get().enableLoopUnrolling(50);
        Options.get().enableNoForInSpecialization();
        Misc.runSource("",
                "for(var p in {a: 'a', b: 'b'}){",
                "   for(var i = 0; i < 3; i++){",
                "       TAJS_assert(i, 'isMaybeSingleNumUInt');",
                "   }",
                "}");
    }

    @Test
    public void compatibleWithLoopUnrolling2_whenDisabled() {
        Options.get().enableLoopUnrolling(50);
        Options.get().enableNoForInSpecialization();
        Misc.runSource("",
                "for(var i = 0; i < 3; i++){",
                "   for(var p in {a: 'a', b: 'b'}){",
                "       TAJS_assert(i, 'isMaybeSingleNumUInt');",
                "   }",
                "}");
    }

    @Test
    public void missingPropertyBug() {
        Misc.runSource("var loopEntered = false;",
                "function keys(o){for(var k in o){loopEntered = true;}}",
                "",
                "var o1 = {};",
                "keys({});",
                "o1.foo = 42;",
                "keys(o1);",
                "TAJS_assert(loopEntered, 'isMaybeAnyBool')");
    }

    @Test
    public void missingPropertyBug_fixedWithoutLazy() {
        Options.get().enableNoLazy();
        Misc.runSource("var loopEntered = false;",
                "function keys(o){for(var k in o){loopEntered = true;}}",
                "",
                "var o1 = {};",
                "keys({});",
                "o1.foo = 42;",
                "keys(o1);",
                "TAJS_assert(loopEntered, 'isMaybeAnyBool')");
    }

    @Test
    public void missingPropertyBug_fixedWithAssignmentLocation() {
        Misc.runSource("var loopEntered = false;",
                "function keys(o){for(var k in o){loopEntered = true;}}",
                "",
                "keys({});",
                "var o1 = {};",
                "o1.foo = 42;",
                "keys(o1);",
                "TAJS_assert(loopEntered, 'isMaybeAnyBool')");
    }

    @Test
    public void missingPropertyBug_v2() {
        Misc.runSource("var loopEntered = false;",
                "var o = {};",
                "function baseFor(object) {keys(object);}",
                "function shimKeys(object) {keysIn(object);}",
                "var keys = shimKeys;",
                "function keysIn(object) { for (var key in object) {loopEntered = true;}}",
                "keys();",
                "o.foo = 42;",
                "baseFor(o);",
                "TAJS_assert(loopEntered, 'isMaybeAnyBool')");
    }

    @Test
    public void missingPropertyBug_fixedWithoutLazy_v2() {
        Options.get().enableNoLazy();
        Misc.runSource("var loopEntered = false;",
                "var o = {};",
                "function baseFor(object) {keys(object);}",
                "function shimKeys(object) {keysIn(object);}",
                "var keys = shimKeys;",
                "function keysIn(object) { for (var key in object) {loopEntered = true;}}",
                "keys();",
                "o.foo = 42;",
                "baseFor(o);",
                "TAJS_assert(loopEntered, 'isMaybeAnyBool')");
    }

    @Test
    public void missingPropertyBug_fixedWithAssignmentLocation_v2() {
        Misc.runSource("var loopEntered = false;",
                "function baseFor(object) {keys(object);}",
                "function shimKeys(object) {keysIn(object);}",
                "var keys = shimKeys;",
                "function keysIn(object) { for (var key in object) {loopEntered = true;}}",
                "keys();",
                "var o = {};",
                "o.foo = 42;",
                "baseFor(o);",
                "TAJS_assert(loopEntered, 'isMaybeAnyBool')");
    }

    @Test
    public void missingPropertyBug_v2_minimized() {
        Misc.runSource("var loopEntered = false;",
                "var o = {};",
                "function iterateIndirectIndirect(object) {iterateIndirect(object);}",
                "function iterateIndirect(object) {iterate(object);}",
                "function iterate(object) { for (var key in object) { loopEntered = true;} }",
                "iterateIndirect();",
                "o.foo = 42;",
                "iterateIndirectIndirect(o);",
                "TAJS_assert(loopEntered, 'isMaybeAnyBool')");
    }

    @Test
    public void missingPropertyBug_v2_minimized_fixedWithInliningA() {
        Misc.runSource("var loopEntered = false;",
                "var o = {};",
                "function iterateIndirect(object) {iterate(object);}",
                "function iterate(object) { for (var key in object) { loopEntered = true;} }",
                "iterateIndirect();",
                "o.foo = 42;",
                "iterateIndirect(o);",
                "TAJS_assert(loopEntered, 'isMaybeAnyBool')");
    }

    @Test
    public void missingPropertyBug_v2_minimized_fixedWithInliningB() {
        Misc.runSource("var loopEntered = false;",
                "var o = {};",
                "function iterateIndirectIndirect(object) {iterateIndirect(object);}",
                "function iterateIndirect(object) {iterate(object);}",
                "function iterate(object) { for (var key in object) { loopEntered = true;} }",
                "iterate();",
                "o.foo = 42;",
                "iterateIndirectIndirect(o);",
                "TAJS_assert(loopEntered, 'isMaybeAnyBool')");
    }

    @Test
    public void missingPropertyBug_v2_minimized_fixedWithInliningAB() {
        Misc.runSource("var loopEntered = false;",
                "var o = {};",
                "function iterateIndirect(object) {iterate(object);}",
                "function iterate(object) { for (var key in object) { loopEntered = true;} }",
                "iterate();",
                "o.foo = 42;",
                "iterateIndirect(o);",
                "TAJS_assert(loopEntered, 'isMaybeAnyBool')");
    }

    @Test
    public void missingPropertyBug_v2_minimized_fixedWithInliningABFull() {
        Misc.runSource("var loopEntered = false;",
                "var o = {};",
                "function iterate(object) { for (var key in object) { loopEntered = true;} }",
                "iterate();",
                "o.foo = 42;",
                "iterate(o);",
                "TAJS_assert(loopEntered, 'isMaybeAnyBool')");
    }
}
