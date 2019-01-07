package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

@SuppressWarnings("static-method")
public class TestLodash4 {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestLodash4");
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
    public void lodash_4_17_10_test1() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_1.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test2() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_2.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test3() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_3.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test4() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_4.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test5() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_5.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test6() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_6.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test7() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_7.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test8() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_8.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test9() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_9.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test10() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_10.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test11() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_11.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test12() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_12.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test13() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_13.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test14() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_14.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test15() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_15.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test16() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_16.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test17() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_17.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test18() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_18.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test19() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_19.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test20() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_20.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test21() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_21.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test22() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_22.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test23() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_23.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test24() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_24.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test25() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_25.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test26() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_26.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test27() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_27.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test28() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_28.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test29() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_29.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test30() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_30.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test31() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_31.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test32() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_32.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test33() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_33.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test34() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_34.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test35() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_35.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test36() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_36.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test37() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_37.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test38() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_38.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test39() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_39.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test40() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_40.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test41() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_41.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test42() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_42.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test43() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_43.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test44() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_44.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test45() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_45.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test46() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_46.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test47() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_47.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test48() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_48.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test49() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_49.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test50() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_50.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test51() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_51.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test52() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_52.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test53() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_53.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test54() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_54.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test55() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_55.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test56() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_56.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test57() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_57.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test58() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_58.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test59() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_59.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test60() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_60.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test61() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_61.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test62() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_62.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test63() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_63.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test64() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_64.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test65() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_65.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test66() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_66.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test67() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_67.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test68() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_68.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test69() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_69.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test70() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_70.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test71() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_71.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test72() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_72.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test73() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_73.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test74() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_74.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test75() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_75.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test76() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_76.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test77() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_77.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test78() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_78.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test79() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_79.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test80() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_80.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test81() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_81.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test82() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_82.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test83() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_83.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test84() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_84.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test85() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_85.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test86() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_86.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test87() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_87.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test88() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_88.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test89() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_89.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test90() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_90.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test91() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_91.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test92() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_92.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test93() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_93.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test94() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_94.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test95() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_95.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test96() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_96.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test97() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_97.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test98() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_98.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test99() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_99.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test100() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_100.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test101() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_101.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test102() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_102.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test103() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_103.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test104() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_104.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test105() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_105.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test106() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_106.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test107() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_107.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test108() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_108.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test109() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_109.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test110() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_110.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test111() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_111.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test112() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_112.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test113() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_113.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test114() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_114.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test115() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_115.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test116() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_116.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test117() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_117.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test118() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_118.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test119() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_119.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test120() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_120.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test121() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_121.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test122() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_122.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test123() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_123.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test124() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_124.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test125() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_125.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test126() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_126.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test127() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_127.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test128() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_128.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test129() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_129.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test130() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_130.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test131() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_131.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test132() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_132.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test133() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_133.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test134() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_134.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test135() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_135.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test136() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_136.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test137() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_137.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test138() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_138.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test139() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_139.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test140() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_140.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test141() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_141.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test142() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_142.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test143() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_143.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test144() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_144.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test145() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_145.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test146() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_146.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test147() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_147.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test148() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_148.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test149() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_149.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test150() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_150.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test151() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_151.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test152() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_152.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test153() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_153.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test154() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_154.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test155() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_155.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test156() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_156.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test157() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_157.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test158() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_158.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test159() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_159.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test160() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_160.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test161() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_161.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test162() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_162.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test163() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_163.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test164() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_164.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test165() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_165.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test166() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_166.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test167() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_167.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test168() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_168.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test169() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_169.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test170() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_170.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test171() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_171.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test172() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_172.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test173() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_173.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test174() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_174.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test175() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_175.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test176() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_176.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test177() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_177.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test178() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_178.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test179() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_179.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test180() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_180.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test181() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_181.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test182() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_182.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test183() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_183.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test184() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_184.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test185() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_185.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test186() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_186.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test187() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_187.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test188() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_188.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test189() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_189.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test190() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_190.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test191() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_191.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test192() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_192.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test193() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_193.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test194() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_194.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test195() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_195.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test196() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_196.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test197() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_197.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test198() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_198.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test199() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_199.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test200() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_200.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test201() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_201.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test202() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_202.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test203() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_203.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test204() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_204.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test205() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_205.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test206() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_206.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test207() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_207.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test208() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_208.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test209() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_209.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test210() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_210.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test211() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_211.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test212() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_212.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test213() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_213.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test214() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_214.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test215() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_215.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test216() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_216.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test217() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_217.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test218() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_218.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test219() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_219.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test220() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_220.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test221() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_221.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test222() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_222.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test223() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_223.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test224() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_224.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test225() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_225.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test226() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_226.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test227() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_227.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test228() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_228.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test229() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_229.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test230() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_230.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test231() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_231.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test232() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_232.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test233() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_233.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test234() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_234.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test235() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_235.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test236() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_236.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test237() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_237.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test238() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_238.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test239() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_239.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test240() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_240.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test241() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_241.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test242() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_242.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test243() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_243.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test244() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_244.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test245() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_245.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test246() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_246.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test247() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_247.html");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.AnalysisPrecisionLimitationException.class)
    public void lodash_4_17_10_test248() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_248.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test249() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_249.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test250() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_250.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test251() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_251.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test252() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_252.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test253() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_253.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test254() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_254.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test255() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_255.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test256() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_256.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test257() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_257.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test258() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_258.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test259() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_259.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test260() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_260.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test261() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_261.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test262() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_262.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test263() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_263.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test264() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_264.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test265() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_265.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test266() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_266.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test267() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_267.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test268() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_268.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test269() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_269.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test270() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_270.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test271() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_271.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test272() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_272.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test273() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_273.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test274() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_274.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test275() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_275.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test276() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_276.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test277() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_277.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test278() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_278.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test279() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_279.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test280() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_280.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test281() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_281.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test282() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_282.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test283() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_283.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test284() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_284.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test285() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_285.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test286() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_286.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test287() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_287.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test288() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_288.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test289() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_289.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test290() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_290.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test291() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_291.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test292() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_292.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test293() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_293.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test294() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_294.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test295() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_295.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test296() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_296.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test297() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_297.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test298() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_298.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test299() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_299.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test300() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_300.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test301() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_301.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test302() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_302.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test303() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_303.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test304() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_304.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test305() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_305.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void lodash_4_17_10_test306() {
        Misc.run("benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_306.html");
        Misc.checkSystemOutput();
    }

}
