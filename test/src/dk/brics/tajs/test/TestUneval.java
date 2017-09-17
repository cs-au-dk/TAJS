package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("static-method")
public class TestUneval {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestUneval");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableUnevalizer();
        Options.get().enableContextSensitiveHeap();
        Options.get().enableParameterSensitivity();
    }

    @Test
    public void uneval_00() throws Exception {
        Misc.run("test-resources/src/uneval/uneval00.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_01() throws Exception {
        Misc.run("test-resources/src/uneval/uneval01.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_02() throws Exception {
        Misc.run("test-resources/src/uneval/uneval02.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_03() throws Exception {
        Misc.run("test-resources/src/uneval/uneval03.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_04() throws Exception {
        Misc.run("test-resources/src/uneval/uneval04.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_05() throws Exception {
        Misc.run("test-resources/src/uneval/uneval05.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_06() throws Exception {
        Misc.run("test-resources/src/uneval/uneval06.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class /* aliased eval */)
    public void uneval_07() throws Exception {
        Misc.run("test-resources/src/uneval/uneval07.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_08() throws Exception {
        Misc.run("test-resources/src/uneval/uneval08.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_09() throws Exception {
        Misc.run("test-resources/src/uneval/uneval09.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class /* last evaluated expression as eval result */)
    public void uneval_10() throws Exception {
        Misc.run("test-resources/src/uneval/uneval10.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_11() throws Exception {
        Misc.run("test-resources/src/uneval/uneval11.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_12() throws Exception {
        Misc.run("test-resources/src/uneval/uneval12.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_13() throws Exception {
        Misc.run("test-resources/src/uneval/uneval13.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_14() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval14.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_15() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval15.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_16() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval16.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_17() throws Exception {
        Misc.run("test-resources/src/uneval/uneval17.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_18() throws Exception {
        Misc.run("test-resources/src/uneval/uneval18.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_19() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval19.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_20() throws Exception {
        Misc.run("test-resources/src/uneval/uneval20.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_21() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval21.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_22() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval22.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_23() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval23.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_24() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval24.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_25() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval25.html");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class /* Imprecise eval */)
    public void uneval_26() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval26.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_27() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval27.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_27un() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval27.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_28() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval28.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_29() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval29.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_30() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval30.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_31() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval31.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_32() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval32.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_33() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval33.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_34() throws Exception {
        Misc.run("test-resources/src/uneval/uneval34.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_35() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval35.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_36() throws Exception {
        Misc.run("test-resources/src/uneval/uneval36.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_37() throws Exception {
        Misc.run("test-resources/src/uneval/uneval37.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_38() throws Exception {
        Misc.run("test-resources/src/uneval/uneval38.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_39() throws Exception {
        Misc.run("test-resources/src/uneval/uneval39.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_40() throws Exception {
        Misc.run("test-resources/src/uneval/uneval40.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_41() throws Exception {
        Misc.run("test-resources/src/uneval/uneval41.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_42() throws Exception {
        Misc.run("test-resources/src/uneval/uneval42.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_43() throws Exception {
        Misc.run("test-resources/src/uneval/uneval43.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_44() throws Exception {
        Misc.run("test-resources/src/uneval/uneval44.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_45() throws Exception {
        Misc.run("test-resources/src/uneval/uneval45.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_46() throws Exception {
        Misc.run("test-resources/src/uneval/uneval46.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class /* aliased eval */)
    public void uneval_47() throws Exception {
        Misc.run("test-resources/src/uneval/uneval47.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_48() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval48.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_setIntervalVariants() throws Exception {
        //Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval_setIntervalVariants.js");
        //Misc.checkSystemOutput();
    }

    @Test
    public void uneval_stackOverflowRegression() throws Exception {
        // should not crash
        Misc.runSource("function f(arg) {",
                " for (var p in this){}",
                " eval(arg);",
                "}",
                "f(42);",
                "");
    }

    @Test
    public void uneval_undefinedRegisterRegression() throws Exception {
        // should not crash
        Misc.runSource("eval('Object();var t=Number();');");
    }

    @Test
    public void uneval_indexOutOfBoundsRegression() throws Exception {
        // should not crash
        Options.get().enableIncludeDom();
        Misc.runSource("setInterval(42);");
    }

    @Test
    public void uneval_declareFunctionRegression() throws Exception {
        Misc.runSource("eval('function f(){}'); TAJS_assert(typeof f === 'function');");
    }

    @Test
    public void uneval_declareNestedFunctionRegression() throws Exception {
        Misc.runSource("function f (){eval('function g(){}'); TAJS_assert(typeof g === 'function');} f();");
    }

    @Test
    public void uneval_newFunction() throws Exception {
        Misc.runSource("new Function()");
    }

    @Test
    public void uneval_newFunction_call() throws Exception {
        Misc.runSource("var f = new Function();",
                "TAJS_assert(f() === undefined);");
    }

    @Test
    public void uneval_syntaxParts() throws Exception {
        Misc.runSource("var count;",
                "function f () {",
                "  var code = count++ + ' = function(){return ';",
                "  eval(code.substring() + ';}');",
                "};",
                "f();",
                "f();");
    }

    @Test
    public void uneval_newFunction_nonString1() throws Exception {
        Misc.runSource("var f = new Function(42);");
    }

    @Test
    public void uneval_newFunction_nonString2() throws Exception {
        Misc.runSource("var f = new Function('', 42);");
    }

    @Test(expected = AnalysisLimitationException.class)
    public void impreciseFunctionConstructor() {
        Misc.runSource("var x = Math.random()? 'a': 'b'",
                "Function(x)();");
    }

    @Test
    public void impreciseFunctionConstructor_ignored() {
        Options.get().getUnsoundness().setIgnoreImpreciseFunctionConstructor(true);
        Misc.runSource("var x = Math.random()? 'this': 'toString'",
                "TAJS_assert(Function(x)() === undefined);");
    }

    @Test(expected = AnalysisLimitationException.class /* Imprecise eval, but not an internal crash */)
    public void uneval_missing_entry_block() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/uneval/uneval_missing_entry_block.js");
    }

    @Test
    public void freeBreakStatement() {
        Misc.runSource("var exception = false;",
                "try {",
                "   eval('break');",
                "} catch (e) {",
                "   exception = true;",
                "}",
                "TAJS_assert(exception);");
    }

    @Test
    public void freeContinueStatement() {
        Misc.runSource("var exception = false;",
                "try {",
                "   eval('continue');",
                "} catch (e) {",
                "   exception = true;",
                "}",
                "TAJS_assert(exception);");
    }

    @Test
    public void implicictlyBoundBreakStatement() {
        Misc.runSource("var exception = false;",
                "try {",
                "   while(Math.random())eval('break');",
                "} catch (e) {",
                "   exception = true;",
                "}",
                "TAJS_assert(exception, 'isMaybeAnyBool');");
    }

    @Test
    public void implicitlyBoundContinueStatement() {
        Misc.runSource("var exception = false;",
                "try {",
                "   while(Math.random())eval('continue');",
                "} catch (e) {",
                "   exception = true;",
                "}",
                "TAJS_assert(exception, 'isMaybeAnyBool');");
    }

    @Test
    public void boundBreakStatement() {
        Misc.runSource("eval('while(Math.random())break');");
    }

    @Test
    public void boundContinueStatement() {
        Misc.runSource("eval('while(Math.random())continue');");
    }

    @Test
    public void characterEncodings() {
        List<String> rnCharsList = Arrays.asList(
                "foo" + (char) 0xD + (char) 0xA + "bar",
                "foo\r\nbar",
                "foo\nbar");
        rnCharsList.forEach(rnChars -> {
            Pattern p = Pattern.compile("\\R");
            Matcher matcher = p.matcher(rnChars);
            String replaced = matcher.replaceAll("X");
            Assert.assertEquals("fooXbar", replaced);
        });
    }

    @Test
    public void eval_escapes() {
        Misc.runSource("eval('\"use strict\";');");
    }

    @Test
    public void eval_escapes_2() {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("eval('\"use strict\"; var x = \"\\\\\" + \"1\";');");
    }

    @Test
    public void eval_escapes_3() {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("eval('var x = \"\\\\\" + \"1\";');");
    }

    @Test
    public void functionConstructorScope() {
        Misc.runSource(
                "var variable = 'global';",
                "function f(){",
                "	var variable = 'local';",
                "	var usesGlobal = new Function(\"TAJS_assertEquals('global', variable);\");",
                "	var usesLocal = function(){ TAJS_assertEquals('local', variable); };",
                "   usesGlobal();",
                "   usesLocal();",
                "}",
                "f();"
        );
    }

    @Test
    public void setTimeoutFunctionScope() {
        Options.get().enableIncludeDom();
        Misc.runSource(
                "var variable = 'global';",
                "function f(){",
                "	var variable = 'local';",
                "	setTimeout(\"TAJS_assertEquals('global', variable);\", 0);",
                "	setTimeout(function(){ TAJS_assertEquals('local', variable); }, 0);",
                "}",
                "f();"
        );
    }
}
