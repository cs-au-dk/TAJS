package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestAnderson {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestAnderson");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void anderson_1() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/anderson/anderson1.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_2() throws Exception {
        Misc.run("test-resources/src/anderson/anderson2.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_3() throws Exception {
        Misc.run("test-resources/src/anderson/anderson3.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_4() throws Exception {
        Misc.run("test-resources/src/anderson/anderson4.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_5() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/anderson/anderson5.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_6() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/anderson/anderson6.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_7() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/anderson/anderson7.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_8() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/anderson/anderson8.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_9() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/anderson/anderson9.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_10() throws Exception {
        Misc.run("test-resources/src/anderson/anderson10.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_11() throws Exception {
        Misc.run("test-resources/src/anderson/anderson11.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_12() throws Exception {
        Misc.run("test-resources/src/anderson/anderson12.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_13() throws Exception {
        Misc.run("test-resources/src/anderson/anderson13.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_14() throws Exception {
        Misc.run("test-resources/src/anderson/anderson14.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_15() throws Exception {
        Misc.run("test-resources/src/anderson/anderson15.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_16() throws Exception {
        Misc.run("test-resources/src/anderson/anderson16.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_17() throws Exception {
        Misc.run("test-resources/src/anderson/anderson17.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_18() throws Exception {
        Misc.run("test-resources/src/anderson/anderson18.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_19() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/anderson/anderson19.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_20() throws Exception {
        Misc.run("test-resources/src/anderson/anderson20.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_21() throws Exception {
        Misc.run("test-resources/src/anderson/anderson21.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_22() throws Exception {
        Misc.run("test-resources/src/anderson/anderson22.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_23() throws Exception {
        Misc.run("test-resources/src/anderson/anderson23.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_24() throws Exception {
        Misc.run("test-resources/src/anderson/anderson24.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_25() throws Exception {
        Misc.run("test-resources/src/anderson/anderson25.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_26() throws Exception {
        Misc.run("test-resources/src/anderson/anderson26.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_27() throws Exception {
        Misc.run("test-resources/src/anderson/anderson27.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void anderson_28() throws Exception {
        Misc.run("test-resources/src/anderson/anderson28.js");
        Misc.checkSystemOutput();
    }
}
