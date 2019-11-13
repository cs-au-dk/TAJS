package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.soundness.SoundnessTesterMonitor;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

@SuppressWarnings("static-method")
public class TestLodash3 {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestLodash3");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().getSoundnessTesterOptions().setRootDirFromMainDirectory(Paths.get("../../"));
        Options.get().getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);

        Options.get().enableDeterminacy();
        Options.get().enablePolyfillMDN();
        Options.get().enablePolyfillTypedArrays();
        Options.get().enablePolyfillES6Collections();
        Options.get().enablePolyfillES6Promises();
        Options.get().enableConsoleModel();
        Options.get().enableIncludeDom();

        Options.get().enableBlendedAnalysis();
        Options.get().enableIgnoreUnreached();
    }

    @Test
    public void lodash_3_0_0_test1() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_1.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test2() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_2.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test3() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_3.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test4() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_4.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test5() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_5.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test6() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_6.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test7() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_7.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test8() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_8.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test9() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_9.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test10() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_10.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test11() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_11.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test12() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_12.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test13() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_13.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test14() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_14.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test15() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_15.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test16() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_16.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test17() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_17.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test18() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_18.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test19() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_19.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test20() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_20.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test21() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_21.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test22() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_22.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test23() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_23.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test24() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_24.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test25() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_25.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test26() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_26.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test27() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_27.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test28() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_28.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test29() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_29.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test30() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_30.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test31() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_31.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test32() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_32.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test33() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_33.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test34() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_34.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test35() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_35.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test36() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_36.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test37() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_37.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test38() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_38.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test39() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_39.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test40() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_40.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test41() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_41.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test42() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_42.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test43() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_43.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test44() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_44.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test45() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_45.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test46() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_46.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test47() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_47.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test48() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_48.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test49() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_49.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test50() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_50.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test51() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_51.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test52() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_52.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test53() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_53.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test54() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_54.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test55() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_55.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test56() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_56.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test57() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_57.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test58() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_58.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test59() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_59.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test60() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_60.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test61() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_61.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test62() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_62.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test63() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_63.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test64() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_64.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test65() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_65.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test66() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_66.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test67() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_67.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test68() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_68.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test69() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_69.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test70() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_70.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test71() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_71.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test72() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_72.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test73() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_73.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test74() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_74.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test75() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_75.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test76() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_76.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test77() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_77.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test78() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_78.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test79() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_79.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test80() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_80.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test81() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_81.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test82() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_82.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test83() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_83.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test84() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_84.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test85() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_85.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test86() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_86.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test87() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_87.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test88() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_88.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test89() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_89.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test90() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_90.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test91() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_91.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test92() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_92.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test93() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_93.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test94() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_94.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test95() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_95.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test96() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_96.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test97() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_97.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test98() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_98.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test99() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_99.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test100() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_100.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test101() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_101.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test102() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_102.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test103() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_103.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test104() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_104.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test105() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_105.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test106() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_106.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test107() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_107.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test108() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_108.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test109() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_109.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test110() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_110.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test111() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_111.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test112() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_112.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test113() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_113.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test114() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_114.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test115() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_115.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test116() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_116.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test117() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_117.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test118() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_118.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test119() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_119.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test120() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_120.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test121() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_121.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test122() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_122.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test123() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_123.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test124() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_124.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test125() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_125.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test126() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_126.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test127() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_127.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test128() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_128.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test129() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_129.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test130() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_130.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test131() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_131.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test132() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_132.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test133() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_133.html");
        Misc.checkSystemOutput();
    }

    @Test(expected = SoundnessTesterMonitor.SoundnessException.class) // TODO: see FIXME in Filtering#assumeVariableSatisfies
    public void lodash_3_0_0_test134() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_134.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test135() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_135.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test136() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_136.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test137() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_137.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test138() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_138.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test139() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_139.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test140() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_140.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test141() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_141.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test142() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_142.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test143() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_143.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test144() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_144.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test145() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_145.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test146() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_146.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test147() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_147.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test148() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_148.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test149() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_149.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test150() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_150.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test151() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_151.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test152() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_152.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test153() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_153.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test154() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_154.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test155() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_155.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test156() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_156.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test157() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_157.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test158() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_158.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test159() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_159.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test160() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_160.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test161() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_161.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test162() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_162.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test163() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_163.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test164() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_164.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test165() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_165.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test166() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_166.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test167() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_167.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test168() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_168.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test169() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_169.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test170() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_170.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test171() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_171.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test172() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_172.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test173() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_173.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test174() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_174.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test175() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_175.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_3_0_0_test176() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_176.html");
        Misc.checkSystemOutput();
    }

}
