package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.monitoring.OrdinaryExitReachableChecker;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.ParseError;
import org.junit.Before;
import org.junit.Test;

public class TestGettersSetters {

    private CompositeMonitoring monitor;

    @Before
    public void init() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
        monitor = new CompositeMonitoring(new Monitoring(), new OrdinaryExitReachableChecker());
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
        Misc.run(new String[]{"test/getterssetters/mdn1.js"}, monitor);
    }

    @Test
    public void mdn2() {
        Misc.run(new String[]{"test/getterssetters/mdn2.js"}, monitor);
    }

    @Test
    public void so1() {
        Misc.run(new String[]{"test/getterssetters/so1.js"}, monitor);
    }

    @Test
    public void so2() {
        Misc.run(new String[]{"test/getterssetters/so2.js"}, monitor);
    }

    @Test
    public void so3() {
        Misc.run(new String[]{"test/getterssetters/so3.js"}, monitor);
    }

    @Test
    public void so4() {
        Misc.run(new String[]{"test/getterssetters/so4.js"}, monitor);
    }

    @Test
    public void so5() {
        Misc.run(new String[]{"test/getterssetters/so5.js"}, monitor);
    }

    @Test
    public void so6() {
        Misc.run(new String[]{"test/getterssetters/so6.js"}, monitor);
    }

    @Test
    public void so7() { // FIXME: infinite recursion in getter, which triggers a "RangeError: Maximum call stack size exceeded"
        Misc.run(new String[]{"test/getterssetters/so7.js"}, new Monitoring());
    }

    @Test
    public void so8() {
        Misc.run(new String[]{"test/getterssetters/so8.js"}, new Monitoring());
    }

    @Test
    public void so9() {
        Misc.run(new String[]{"test/getterssetters/so9.js"}, new Monitoring());
    }

    @Test
    public void so10() {
        Misc.run(new String[]{"test/getterssetters/so10.js"}, new Monitoring());
    }

    @Test
    public void so11() {
        Misc.run(new String[]{"test/getterssetters/so11.js"}, new Monitoring());
    }

    @Test
    public void so12() {
        Misc.run(new String[]{"test/getterssetters/so12.js"}, new Monitoring());
    }

    @Test
    public void so13() {
        Misc.run(new String[]{"test/getterssetters/so13.js"}, new Monitoring());
    }

    @Test
    public void so14() {
        Misc.run(new String[]{"test/getterssetters/so14.js"}, new Monitoring());
    }

    @Test
    public void so15() {
        Misc.run(new String[]{"test/getterssetters/so15.js"}, new Monitoring());
    }

    @Test
    public void so16() {
        Misc.run(new String[]{"test/getterssetters/so16.js"}, new Monitoring());
    }

    @Test
    public void so17() {
        Misc.run(new String[]{"test/getterssetters/so17.js"}, new Monitoring());
    }

    @Test
    public void prototypes() {
        Misc.run(new String[]{"test/getterssetters/prototypes.js"}, new Monitoring());
    }

    @Test
    public void weak_defineGetterSetter() {
        Misc.run(new String[]{"test/getterssetters/weak_defineGetterSetter.js"}, new Monitoring());
    }

    @Test
    public void fuzzyStrIdentifier_defineGetterSetter() {
        Misc.run(new String[]{"test/getterssetters/fuzzyStrIdentifier_defineGetterSetter.js"}, new Monitoring());
    }

    @Test
    public void fuzzyMisc_defineGetterSetter() {
        Misc.run(new String[]{"test/getterssetters/fuzzyMisc_defineGetterSetter.js"}, new Monitoring());
    }

    @Test
    public void fuzzyIdentifier_defineGetterSetter() {
        Misc.run(new String[]{"test/getterssetters/fuzzyIdentifier_defineGetterSetter.js"}, new Monitoring());
    }

    @Test
    public void fuzzyUInt_defineGetterSetter() {
        Misc.run(new String[]{"test/getterssetters/fuzzyUInt_defineGetterSetter.js"}, new Monitoring());
    }

    @Test
    public void fuzzyStrUInt_defineGetterSetter() {
        Misc.run(new String[]{"test/getterssetters/fuzzyStrUInt_defineGetterSetter.js"}, new Monitoring());
    }

    @Test
    public void implicits() {
        Misc.run(new String[]{"test/getterssetters/implicits.js"}, new CompositeMonitoring(new Monitoring(), new OrdinaryExitReachableChecker()));
    }

    @Test
    public void implicits_unsound() {
        Misc.run(new String[]{"test/getterssetters/implicits_unsound.js"}, new CompositeMonitoring(new Monitoring(), new OrdinaryExitReachableChecker()));
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
        Misc.run(args, new CompositeMonitoring(new Monitoring(), new OrdinaryExitReachableChecker()));
    }

    @Test
    public void badDefineGetter() throws Exception {
        Misc.init();
        Misc.runSource(
                "var UINT = Math.random() ? 0 : 1;",
                "({}).__defineGetter__(UINT, UINT);",
                "TAJS_assert(false);");
    }

    @Test
    public void badDefineSetter1() throws Exception {
        Misc.init();
        Misc.runSource(
                "var UINT = Math.random() ? 0 : 1;",
                "({}).__defineSetter__(UINT, UINT);",
                "TAJS_assert(false);");
    }

    @Test
    public void badDefineSetter2() throws Exception {
        Misc.init();
        Misc.runSource(
                "({}).__defineSetter__(UINT, 0);",
                "TAJS_assert(false);");
    }

    @Test
    public void badDefineSetter3() throws Exception {
        Misc.init();
        Misc.runSource(
                "({}).__defineSetter__(0, 0);",
                "TAJS_assert(false);");
    }

    @Test
    public void maybeBadDefineSetter() throws Exception {
        Misc.init();
        Misc.runSource(new String[]{
                "var UINT = Math.random() ? 0 : 1;",
                "var maybeFunction = Math.random()? function(){}: UINT;",
                "({}).__defineSetter__(UINT, maybeFunction);"
        }, new CompositeMonitoring(new Monitoring(), new OrdinaryExitReachableChecker()));
    }

}
