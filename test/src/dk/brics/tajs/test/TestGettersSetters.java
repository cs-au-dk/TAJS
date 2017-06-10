package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.ParseError;
import org.junit.Before;
import org.junit.Test;

public class TestGettersSetters {

    @Before
    public void init() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void gs1() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/getterssetters/gs1.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void gs2() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/getterssetters/gs2.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test(expected = ParseError.class)
    public void gs3() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/getterssetters/gs3.js"};
        Misc.run(args);
    }

    @Test
    public void mdn1() {
        Misc.run("test/getterssetters/mdn1.js");
    }

    @Test
    public void mdn2() {
        Misc.run("test/getterssetters/mdn2.js");
    }

    @Test
    public void so1() {
        Misc.run("test/getterssetters/so1.js");
    }

    @Test
    public void so2() {
        Misc.run("test/getterssetters/so2.js");
    }

    @Test
    public void so3() {
        Misc.run("test/getterssetters/so3.js");
    }

    @Test
    public void so4() {
        Misc.run("test/getterssetters/so4.js");
    }

    @Test
    public void so5() {
        Misc.run("test/getterssetters/so5.js");
    }

    @Test
    public void so6() {
        Misc.run("test/getterssetters/so6.js");
    }

    @Test
    public void so7() { // FIXME: infinite recursion in getter, which triggers a "RangeError: Maximum call stack size exceeded"
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test/getterssetters/so7.js");
    }

    @Test
    public void so8() {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test/getterssetters/so8.js");
    }

    @Test
    public void so9() {
        Misc.run("test/getterssetters/so9.js");
    }

    @Test
    public void so10() {
        Misc.run("test/getterssetters/so10.js");
    }

    @Test
    public void so11() {
        Misc.run("test/getterssetters/so11.js");
    }

    @Test
    public void so12() {
        Misc.run("test/getterssetters/so12.js");
    }

    @Test
    public void so13() {
        Misc.run("test/getterssetters/so13.js");
    }

    @Test
    public void so14() {
        Misc.run("test/getterssetters/so14.js");
    }

    @Test
    public void so15() {
        Misc.run("test/getterssetters/so15.js");
    }

    @Test
    public void so16() {
        Misc.run("test/getterssetters/so16.js");
    }

    @Test
    public void so17() {
        Misc.run("test/getterssetters/so17.js");
    }

    @Test
    public void prototypes() {
        Misc.run("test/getterssetters/prototypes.js");
    }

    @Test
    public void weak_defineGetterSetter() {
        Misc.run("test/getterssetters/weak_defineGetterSetter.js");
    }

    @Test
    public void fuzzyStrIdentifier_defineGetterSetter() {
        Misc.run("test/getterssetters/fuzzyStrIdentifier_defineGetterSetter.js");
    }

    @Test
    public void fuzzyMisc_defineGetterSetter() {
        Misc.run("test/getterssetters/fuzzyMisc_defineGetterSetter.js");
    }

    @Test
    public void fuzzyIdentifier_defineGetterSetter() {
        Misc.run("test/getterssetters/fuzzyIdentifier_defineGetterSetter.js");
    }

    @Test
    public void fuzzyUInt_defineGetterSetter() {
        Misc.run("test/getterssetters/fuzzyUInt_defineGetterSetter.js");
    }

    @Test
    public void fuzzyStrUInt_defineGetterSetter() {
        Misc.run("test/getterssetters/fuzzyStrUInt_defineGetterSetter.js");
    }

    @Test
    public void implicits() {
        Misc.run("test/getterssetters/implicits.js");
    }

    @Test
    public void implicits_unsound() {
        Misc.run("test/getterssetters/implicits_unsound.js");
    }

    @Test
    public void setterReturn() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/getterssetters/setterReturn.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void callToWeakSetter() throws Exception {
        Options.get().enableShowVariableInfo();
        Misc.init();
        String[] args = {"test/getterssetters/callToWeakSetter.js"};
        Misc.run(args);
    }

    @Test
    public void callToWeakRecursiveGetter() throws Exception {
        Misc.init();
        String[] args = {"test/getterssetters/callToWeakRecursiveGetter.js"};
        Misc.run(args);
    }

    @Test
    public void badDefineGetter() throws Exception {
        Misc.init();
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource(
                "var UINT = Math.random() ? 0 : 1;",
                "({}).__defineGetter__(UINT, UINT);",
                "TAJS_assert(false);");
    }

    @Test
    public void badDefineSetter1() throws Exception {
        Misc.init();
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource(
                "var UINT = Math.random() ? 0 : 1;",
                "({}).__defineSetter__(UINT, UINT);",
                "TAJS_assert(false);");
    }

    @Test
    public void badDefineSetter2() throws Exception {
        Misc.init();
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource(
                "({}).__defineSetter__(UINT, 0);",
                "TAJS_assert(false);");
    }

    @Test
    public void badDefineSetter3() throws Exception {
        Misc.init();
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource(
                "({}).__defineSetter__(0, 0);",
                "TAJS_assert(false);");
    }

    @Test
    public void maybeBadDefineSetter() throws Exception {
        Misc.init();
        Misc.runSource(
                "var UINT = Math.random() ? 0 : 1;",
                "var maybeFunction = Math.random()? function(){}: UINT;",
                "({}).__defineSetter__(UINT, maybeFunction);");
    }

}
