package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import dk.brics.tajs.util.AnalysisResultException;
import dk.brics.tajs.util.ParseError;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.fail;

@SuppressWarnings("static-method")
public class TestMicro {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableContextSensitiveHeap();
        Options.get().enableParameterSensitivity();
        //Options.get().enableForInSpecialization();
        //Options.get().enableNoPolymorphic();
        //Options.get().enableNoModified();
        //Options.get().enableNoLazy();
        //Options.get().enableDebug();
        //Options.get().enableNoRecency();
    }

    @Test
    public void micro_00() throws Exception {
        Misc.run("test-resources/src/micro/test00.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_01() throws Exception {
        Misc.run("test-resources/src/micro/test01.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_02() throws Exception {
        Misc.run("test-resources/src/micro/test02.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_03() throws Exception {
        Misc.run("test-resources/src/micro/test03.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_04() throws Exception {
        Misc.run("test-resources/src/micro/test04.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_05() throws Exception {
        Misc.run("test-resources/src/micro/test05.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_06() throws Exception {
        Misc.run("test-resources/src/micro/test06.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_07() throws Exception {
        Misc.run("test-resources/src/micro/test07.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_08() throws Exception {
        Misc.run("test-resources/src/micro/test08.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_09() throws Exception {
        Misc.run("test-resources/src/micro/test09.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_10() throws Exception {
        Misc.run("test-resources/src/micro/test10.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_11() throws Exception {
        Misc.run("test-resources/src/micro/test11.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_12() throws Exception {
        Misc.run("test-resources/src/micro/test12.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_13() throws Exception {
        Misc.run("test-resources/src/micro/test13.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_14() throws Exception {
        Misc.run("test-resources/src/micro/test14.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_15() throws Exception {
        Misc.run("test-resources/src/micro/test15.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_16() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/test16.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_17() throws Exception {
        Misc.run("test-resources/src/micro/test17.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_18() throws Exception {
        Misc.run("test-resources/src/micro/test18.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_19() throws Exception {
        Misc.run("test-resources/src/micro/test19.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_20() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/test20.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_21() throws Exception {
        Misc.run("test-resources/src/micro/test21.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_22() throws Exception {
        Misc.run("test-resources/src/micro/test22.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_23() throws Exception {
        Misc.run("test-resources/src/micro/test23.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_24() throws Exception {
        Misc.run("test-resources/src/micro/test24.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_25() throws Exception {
        Misc.run("test-resources/src/micro/test25.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_26() throws Exception {
        Misc.run("test-resources/src/micro/test26.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_27() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/test27.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_28() throws Exception {
        Misc.run("test-resources/src/micro/test28.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_29() throws Exception {
        Misc.run("test-resources/src/micro/test29.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_30() throws Exception {
        Misc.run("test-resources/src/micro/test30.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_31() throws Exception {
        Misc.run("test-resources/src/micro/test31.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_32() throws Exception {
        Misc.run("test-resources/src/micro/test32.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_33() throws Exception {
        Misc.run("test-resources/src/micro/test33.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_34() throws Exception {
        Misc.run("test-resources/src/micro/test34.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_35() throws Exception {
        Misc.run("test-resources/src/micro/test35.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_36() throws Exception {
        Misc.run("test-resources/src/micro/test36.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_37() throws Exception {
        Misc.run("test-resources/src/micro/test37.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_38() throws Exception {
        Misc.run("test-resources/src/micro/test38.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_39() throws Exception {
        Misc.run("test-resources/src/micro/test39.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_40() throws Exception {
        Misc.run("test-resources/src/micro/test40.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_41() throws Exception {
        Misc.run("test-resources/src/micro/test41.js", "test-resources/src/micro/test41b.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_42() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/test42.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_43() throws Exception {
        Misc.run("test-resources/src/micro/test43.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_44() throws Exception {
        Misc.run("test-resources/src/micro/test44.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_45() throws Exception {
        Misc.run("test-resources/src/micro/test45.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_46() throws Exception {
        Misc.run("test-resources/src/micro/test46.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_47() throws Exception {
        Misc.run("test-resources/src/micro/test47.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_48() throws Exception {
        Misc.run("test-resources/src/micro/test48.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_49() throws Exception {
        Misc.run("test-resources/src/micro/test49.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_50() throws Exception {
        Misc.run("test-resources/src/micro/test50.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_51() throws Exception {
        Misc.run("test-resources/src/micro/test51.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_52() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/test52.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_53() throws Exception {
        Misc.run("test-resources/src/micro/test53.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_54() throws Exception {
        Misc.run("test-resources/src/micro/test54.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_55() throws Exception {
        Misc.run("test-resources/src/micro/test55.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_56() throws Exception {
        Misc.run("test-resources/src/micro/test56.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_57() throws Exception {
        Misc.run("test-resources/src/micro/test57.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_58() throws Exception {
        Misc.run("test-resources/src/micro/test58.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_59() throws Exception {
        Misc.run("test-resources/src/micro/test59.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_60() throws Exception {
        Misc.run("test-resources/src/micro/test60.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_61() throws Exception {
        Misc.run("test-resources/src/micro/test61.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_62() throws Exception {
        Misc.run("test-resources/src/micro/test62.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_63() throws Exception {
        Misc.run("test-resources/src/micro/test63.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_64() throws Exception {
        Misc.run("test-resources/src/micro/test64.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_65() throws Exception {
        Misc.run("test-resources/src/micro/test65.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_66() throws Exception {
        Misc.run("test-resources/src/micro/test66.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_67() throws Exception {
        Misc.run("test-resources/src/micro/test67.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_68() throws Exception {
        Misc.run("test-resources/src/micro/test68.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_69() throws Exception {
        Misc.run("test-resources/src/micro/test69.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_70() throws Exception {
        Misc.run("test-resources/src/micro/test70.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_71() throws Exception {
        Misc.run("test-resources/src/micro/test71.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_72() throws Exception {
        Misc.run("test-resources/src/micro/test72.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_73() throws Exception {
        Misc.run("test-resources/src/micro/test73.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_73simple() throws Exception {
        Options.get().enableFlowgraph();
        Misc.run("test-resources/src/micro/test73simple.js");
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME: Str join (github #533)
    @Test
    public void micro_74() throws Exception {
        Misc.run("test-resources/src/micro/test74.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_75() throws Exception {
        Misc.run("test-resources/src/micro/test75.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_76() throws Exception {
        Misc.run("test-resources/src/micro/test76.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_77() throws Exception {
        Misc.run("test-resources/src/micro/test77.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_78() throws Exception {
        Misc.run("test-resources/src/micro/test78.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_79() throws Exception {
        Misc.run("test-resources/src/micro/test79.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_80() throws Exception {
        Misc.run("test-resources/src/micro/test80.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_81() throws Exception {
        Misc.run("test-resources/src/micro/test81.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_82() throws Exception {
        Misc.run("test-resources/src/micro/test82.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_83() throws Exception {
        Misc.run("test-resources/src/micro/test83.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_84() throws Exception {
        Misc.run("test-resources/src/micro/test84.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_85() throws Exception {
        Misc.run("test-resources/src/micro/test85.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_86() throws Exception {
        Misc.run("test-resources/src/micro/test86.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_87() throws Exception {
        Misc.run("test-resources/src/micro/test87.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_88() throws Exception {
        Misc.run("test-resources/src/micro/test88.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_89() throws Exception {
        Misc.run("test-resources/src/micro/test89.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_90() throws Exception {
        Misc.run("test-resources/src/micro/test90.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_91() throws Exception {
        Misc.run("test-resources/src/micro/test91.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_92() throws Exception {
        Misc.run("test-resources/src/micro/test92.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_93() throws Exception {
        Misc.run("test-resources/src/micro/test93.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_94() throws Exception {
        Misc.run("test-resources/src/micro/test94.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_95() throws Exception {
        Misc.run("test-resources/src/micro/test95.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_96() throws Exception {
        Misc.run("test-resources/src/micro/test96.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_97() throws Exception {
        Misc.run("test-resources/src/micro/test97.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_98() throws Exception {
        Misc.run("test-resources/src/micro/test98.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_99() throws Exception {
        Misc.run("test-resources/src/micro/test99.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_100() throws Exception {
        Misc.run("test-resources/src/micro/test100.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_101() throws Exception {
        Misc.run("test-resources/src/micro/test101.js");
    }

    @Test
    public void micro_102() throws Exception {
        Misc.run("test-resources/src/micro/test102.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_103() throws Exception {
        Misc.run("test-resources/src/micro/test103.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_104() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/test104.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_105() throws Exception {
        Misc.run("test-resources/src/micro/test105.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_106() throws Exception {
        Misc.run("test-resources/src/micro/test106.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_107() throws Exception {
        Misc.run("test-resources/src/micro/test107.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_108() throws Exception {
        Misc.run("test-resources/src/micro/test108.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_109() throws Exception {
        Misc.run("test-resources/src/micro/test109.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_110() throws Exception {
        Misc.run("test-resources/src/micro/test110.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_111() throws Exception {
        Misc.run("test-resources/src/micro/test111.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_112() throws Exception {
        Misc.run("test-resources/src/micro/test112.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_113() throws Exception {
        Misc.run("test-resources/src/micro/test113.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = ParseError.class)
    public void micro_114() throws Exception {
        Misc.run("test-resources/src/micro/test114.js"); // Should be runtime error, but Sec 16 p.149 allows us to report the error early
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_115() throws Exception {
        Misc.run("test-resources/src/micro/test115.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_116() throws Exception {
        Misc.run("test-resources/src/micro/test116.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_117() throws Exception {
        Misc.run("test-resources/src/micro/test117.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_118() throws Exception {
        Misc.run("test-resources/src/micro/test118.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_119() throws Exception {
        Misc.run("test-resources/src/micro/test119.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_120() throws Exception {
        Misc.run("test-resources/src/micro/test120.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_121() throws Exception {
        Misc.run("test-resources/src/micro/test121.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_122() throws Exception {
        Misc.run("test-resources/src/micro/test122.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_123() throws Exception {
        Misc.run("test-resources/src/micro/test123.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisResultException.class /* TODO: GitHub #191 */)
    public void micro_125() throws Exception {
        Misc.run("test-resources/src/micro/test125.js");
    }

    @Test
    public void micro_125a() throws Exception {
        Misc.run("test-resources/src/micro/test125a.js");
    }

    @Test
    public void micro_125b() throws Exception {
        Misc.run("test-resources/src/micro/test125b.js");
    }

    @Test
    public void micro_125c() throws Exception {
        Misc.run("test-resources/src/micro/test125c.js");
    }

    @Test(expected = AnalysisResultException.class /* TODO: GitHub #191 */)
    public void micro_125d() throws Exception {
        Misc.run("test-resources/src/micro/test125d.js");
    }

    @Test
    public void micro_126() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/test126.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_127() throws Exception {
        Misc.run("test-resources/src/micro/test127.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisResultException.class /* TODO: GitHub #191 */)
    public void micro_128() throws Exception {
        Misc.run("test-resources/src/micro/test128.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_129() throws Exception {
        Misc.run("test-resources/src/micro/test129.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_130() throws Exception {
        Misc.run("test-resources/src/micro/test130.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_131() throws Exception {
        Misc.run("test-resources/src/micro/test131.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_132() throws Exception {
        Misc.run("test-resources/src/micro/test132.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_133() throws Exception {
        Misc.run("test-resources/src/micro/test133.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_134() throws Exception {
        Misc.run("test-resources/src/micro/test134.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_135() throws Exception {
        Misc.run("test-resources/src/micro/test135.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_136() throws Exception {
        Misc.run("test-resources/src/micro/test136.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_137() throws Exception {
        Misc.run("test-resources/src/micro/test137.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_138() throws Exception {
        Misc.run("test-resources/src/micro/test138.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_139() throws Exception {
        Misc.run("test-resources/src/micro/test139.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = ParseError.class)
    public void micro_140a() throws Exception {
        Misc.run("test-resources/src/micro/test140.js");
    }

    @Test(expected = ParseError.class)
    public void micro_140b() throws Exception {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/micro/test140.js");
    }

    @Test
    public void micro_141() throws Exception {
        Misc.run("test-resources/src/micro/test141.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_142() throws Exception {
        Misc.run("test-resources/src/micro/test142.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AssertionError.class /* TODO: GitHub #3 */)
    public void micro_143() throws Exception {
        fail("Add support for getters/setters");
        Misc.run("test-resources/src/micro/test143.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AssertionError.class /* GitHub #3 */)
    public void micro_144() throws Exception {
        fail("Add support for getters/setters");
        Misc.run("test-resources/src/micro/test144.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_145() throws Exception {
        Misc.run("test-resources/src/micro/test145.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_146() throws Exception {
        Misc.run("test-resources/src/micro/test146.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_147() throws Exception {
        Misc.run("test-resources/src/micro/test147.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_148() throws Exception {
        Misc.run("test-resources/src/micro/test148.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_148b() throws Exception {
        Options.get().enableNoModified(); // maybe-modified can make a difference, even with lazy prop
        Misc.run("test-resources/src/micro/test148.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_149() throws Exception {
        Misc.run("test-resources/src/micro/test149.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_150() throws Exception {
        Misc.run("test-resources/src/micro/test150.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_151() throws Exception {
        Misc.run("test-resources/src/micro/test151.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_152() throws Exception {
        Misc.run("test-resources/src/micro/test152.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_153() throws Exception {
        Misc.run("test-resources/src/micro/test153.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_154() throws Exception {
        Misc.run("test-resources/src/micro/test154.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_155() throws Exception {
        Misc.run("test-resources/src/micro/test155.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_156() throws Exception {
        Misc.run("test-resources/src/micro/test156.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_157() throws Exception {
        Misc.run("test-resources/src/micro/test157.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_158() throws Exception {
        Misc.run("test-resources/src/micro/test158.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_159() throws Exception {
        Misc.run("test-resources/src/micro/test159.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_160() throws Exception {
        Misc.run("test-resources/src/micro/test160.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_161() throws Exception {
        Misc.run("test-resources/src/micro/test161.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_162() throws Exception {
        Misc.run("test-resources/src/micro/test162.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_163() throws Exception {
        Misc.run("test-resources/src/micro/test163.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_164() throws Exception {
        Misc.run("test-resources/src/micro/test164.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_165() throws Exception {
        Misc.run("test-resources/src/micro/test165.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_166() throws Exception {
        Misc.run("test-resources/src/micro/test166.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_167() throws Exception {
        Misc.run("test-resources/src/micro/test167.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_168() throws Exception {
        Misc.run("test-resources/src/micro/test168.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_169() throws Exception {
        Misc.run("test-resources/src/micro/test169.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_170() throws Exception {
        Misc.run("test-resources/src/micro/test170.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_171() throws Exception {
        Misc.run("test-resources/src/micro/test171.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_172() throws Exception {
        Misc.run("test-resources/src/micro/test172.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_173() throws Exception {
        Misc.run("test-resources/src/micro/test173.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_174() throws Exception {
        Misc.run("test-resources/src/micro/test174.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_175() throws Exception {
        Misc.run("test-resources/src/micro/test175.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_176() throws Exception {
        Misc.run("test-resources/src/micro/test176.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_177() throws Exception {
        Misc.run("test-resources/src/micro/test177.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_178() throws Exception {
        Misc.run("test-resources/src/micro/test178.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_179() throws Exception {
        Misc.run("test-resources/src/micro/test179.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_180() throws Exception {
        Misc.run("test-resources/src/micro/test180.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_181() throws Exception {
        // Nashorn performs the same hoisting as TAJS // TODO: which hoisting is correct?
        Misc.run("test-resources/src/micro/test181.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_182() throws Exception {
        Misc.run("test-resources/src/micro/test182.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_183() throws Exception {
        Misc.run("test-resources/src/micro/test183.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_184() throws Exception {
        Misc.run("test-resources/src/micro/test184.js");
        Misc.checkSystemOutput();
    }

    @Ignore // TODO: #243 Nashorn bug, seems to be fixed in Java SDK 11 (but not in jdk1.8.0_201 on Windows)
    @Test
    public void micro_185() throws Exception {
        Misc.run("test-resources/src/micro/test185.js");
    }

    @Test(expected = AnalysisResultException.class /* TODO: GitHub #244 */)
    public void micro_186() throws Exception {
        Misc.run("test-resources/src/micro/test186.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_187() throws Exception {
        Misc.run("test-resources/src/micro/test187.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_188() throws Exception {
        Misc.run("test-resources/src/micro/test188.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_189() throws Exception {
        Misc.run("test-resources/src/micro/test189.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_190() throws Exception {
        Misc.run("test-resources/src/micro/test190.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_191() throws Exception {
        Misc.run("test-resources/src/micro/test191.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_192() throws Exception {
        Misc.run("test-resources/src/micro/test192.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_200() throws Exception {
        Misc.run("test-resources/src/micro/test200.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_201() throws Exception {
        Misc.run("test-resources/src/micro/test201.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_202() throws Exception {
        Misc.run("test-resources/src/micro/test202.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_203() throws Exception {
        Options.get().enableLoopUnrolling(100);
        Misc.run("test-resources/src/micro/test203.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_204() throws Exception {
        Options.get().enableLoopUnrolling(100);
        Misc.run("test-resources/src/micro/test204.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_205() throws Exception {
        Misc.run("test-resources/src/micro/test205.js");
    }

    @Test
    public void micro_206() throws Exception {
        Options.get().enableLoopUnrolling(100);
        Misc.run("test-resources/src/micro/test206.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_207() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/test207.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_208() throws Exception {
        Options.get().enableNoPolymorphic();
        Options.get().enableNoChargedCalls();
        Misc.run("test-resources/src/micro/test208.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_209() throws Exception {
        Options.get().enableNoPolymorphic();
        Options.get().enableNoChargedCalls();
        Misc.run("test-resources/src/micro/test209.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_210() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/test210.js");
        Misc.checkSystemOutput();
    }

//    @Test
//    public void micro_210b() throws Exception {
//        Misc.run("test-resources/src/micro/test210b.js");
//        Misc.checkSystemOutput();
//    }

    @Test
    public void micro_testArray() throws Exception {
        Misc.run("test-resources/src/micro/testArray.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testBoolean() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/testBoolean.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testEval() throws Exception {
        Options.get().enableUnevalizer();
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/testEval.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testFunction() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/micro/testFunction.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testFunctionApply() throws Exception {
        Misc.run("test-resources/src/micro/testFunctionApply.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testFunctionCall() throws Exception {
        Misc.run("test-resources/src/micro/testFunctionCall.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testNumber() throws Exception {
        Misc.run("test-resources/src/micro/testNumber.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testObject() throws Exception {
        Misc.run("test-resources/src/micro/testObject.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testOO() throws Exception {
        Misc.run("test-resources/src/micro/testOO.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testRegExp() throws Exception {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/testRegExp.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testString() throws Exception {
        Options.get().getUnsoundness().setIgnoreLocale(true);
        Misc.run("test-resources/src/micro/testString.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testToPrimitive() throws Exception {
        Misc.run("test-resources/src/micro/testToPrimitive.js");
    }

    @Test
    public void micro_testPaper() throws Exception {
        Misc.run("test-resources/src/micro/testPaper.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testForIn1() throws Exception {
        Misc.run("test-resources/src/micro/testForIn1.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testForIn2() throws Exception {
        Misc.run("test-resources/src/micro/testForIn2.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testForIn2simple() throws Exception {
        Misc.run("test-resources/src/micro/testForIn2simple.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testForIn3() throws Exception {
        Misc.run("test-resources/src/micro/testForIn3.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testForIn4() throws Exception {
        Misc.run("test-resources/src/micro/testForIn4.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testForIn5() throws Exception {
        Misc.run("test-resources/src/micro/testForIn5.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testForIn6() throws Exception {
        Misc.run("test-resources/src/micro/testForIn6.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testForIn7() throws Exception {
        Misc.run("test-resources/src/micro/testForIn7.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testForIn8() throws Exception {
        Misc.run("test-resources/src/micro/testForIn8.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testForInHasOwn() throws Exception {
        Misc.run("test-resources/src/micro/testForInHasOwn.js");
    }

    @Test
    public void micro_testForInException() throws Exception {
        Options.get().enableFlowgraph();
        Misc.run("test-resources/src/micro/testForInException.js");
    }

    @Test
    public void micro_testForInPrototypeProperties() throws Exception {
        Misc.run("test-resources/src/micro/testForInPrototypeProperties.js");
    }

    @Test
    public void micro_testForInNoEnumerablePrototypeProperties() throws Exception {
        Misc.run("test-resources/src/micro/testForInNoEnumerablePrototypeProperties.js");
    }

    @Test
    public void micro_testForInEach() throws Exception {
        Misc.run("test-resources/src/micro/testForInEach.js");
    }

    @Test
    public void micro_testCall1() throws Exception {
        Misc.run("test-resources/src/micro/testCall1.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testCall2() throws Exception {
        Misc.run("test-resources/src/micro/testCall2.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testDeadAssignmentsAndDPAs() throws Exception {
        Misc.run("test-resources/src/micro/testDeadAssignmentsAndDPAs.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testDeadAssignmentsAndNatives() throws Exception {
        Misc.run("test-resources/src/micro/testDeadAssignmentsAndNatives.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testNumAdd() throws Exception {
        Misc.run("test-resources/src/micro/testNumAdd.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testNumSub() throws Exception {
        Misc.run("test-resources/src/micro/testNumSub.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testUintAdd() throws Exception {
        Misc.run("test-resources/src/micro/testUintAdd.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testUintSub() throws Exception {
        // FIXME: unsound, see reference to this test in Operators.java
        Misc.run("test-resources/src/micro/testUintSub.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void micro_testConreteStrings() throws Exception {
        Misc.runSource("TAJS_assert(/x/.test('x'));",
                "TAJS_assert(/-\\[/.test('-['));",
                "TAJS_assert(/\\[-/.test('[-'));"
        );
    }

    @Test
    public void micro_testReplaceUnclosedCharacterClass() throws Exception {
        Misc.runSource("TAJS_assert('['.replace('[', 'x') === 'x');"
        );
    }

    @Test
    public void micro_escapedCharacters() throws Exception {
        Misc.run("test-resources/src/micro/escapedCharacters.js");
    }

    @Test
    public void micro_testFunctionConstructor() throws Exception {
        Options.get().enableUnevalizer();
        Misc.runSource("TAJS_assert((typeof new Function('')) === 'function');",
                "TAJS_assert(new Function('')() === undefined);",
                "TAJS_assert((typeof new Function('', '')) === 'function');",
                "TAJS_assert(new Function('', '')() === undefined);"
        );
    }

    @Test
    public void micro_testEmptyFunctionConstructor() throws Exception {
        Options.get().enableUnevalizer();
        Misc.runSource("TAJS_assert((typeof new Function()) === 'function');",
                "TAJS_assert(new Function()() === undefined);"
        );
    }

    @Test
    public void micro_testConcreteDecodeURI() throws Exception {
        Misc.runSource("TAJS_assert(decodeURI() === 'undefined');",
                "TAJS_assert(decodeURI('http://test.com/%20') === 'http://test.com/ ');"
        );
    }

    @Test
    public void micro_testConcreteDecodeURIComponent() throws Exception {
        Misc.runSource("TAJS_assert(decodeURIComponent() === 'undefined');",
                "TAJS_assert(decodeURIComponent('%20') === ' ');"
        );
    }

    @Test
    public void micro_testConcreteEncodeURI() throws Exception {
        Misc.runSource("TAJS_assert(encodeURI() === 'undefined');",
                "TAJS_assert(encodeURI('http://test.com/ ') === 'http://test.com/%20');"
        );
    }

    @Test
    public void micro_testConcreteEncodeURIComponent() throws Exception {
        Misc.runSource("TAJS_assert(encodeURIComponent() === 'undefined');",
                "TAJS_assert(encodeURIComponent(' ') === '%20');"
        );
    }

    @Test
    public void micro_testConcreteEscape() throws Exception {
        Misc.runSource("TAJS_assert(escape() === 'undefined');",
                "TAJS_assert(escape(' ') === '%20');"
        );
    }

    @Test
    public void micro_testConcreteUnescape() throws Exception {
        Misc.runSource("TAJS_assert(unescape() === 'undefined');",
                "TAJS_assert(unescape('%20') === ' ');"
        );
    }

    @Test
    public void micro_testConcreteParseInt() throws Exception {
        Misc.runSource("TAJS_assert(parseInt(42) === 42);",
                "TAJS_assert(parseInt('42') === 42);",
                "TAJS_assert(parseInt('0x42') === 66);",
                "TAJS_assert(parseInt('42', 5) === 22);",
                "TAJS_assert(isNaN(parseInt('x')));",
                "TAJS_assert(isNaN(parseInt(true)));"
        );
    }

    @Test
    public void micro_testConcreteParseFloat() throws Exception {
        Misc.runSource("TAJS_assert(parseFloat(42) === 42);",
                "TAJS_assert(parseFloat('42') === 42);",
                "TAJS_assert(parseFloat('42.3') === 42.3);",
                "TAJS_assert(parseFloat('0x42') === 0);",
                "TAJS_assert(isNaN(parseFloat('x')));",
                "TAJS_assert(isNaN(parseFloat(true)));"
        );
    }

    @Test
    public void micro_testToString() throws Exception {
        Misc.runSource("var U = !!Math.random();",
                "TAJS_assert(Object.prototype.toString.call({}) === '[object Object]');",
                "TAJS_assert(Object.prototype.toString.call([]) === '[object Array]');",
                "TAJS_assert(Object.prototype.toString.call(U? {}: {}) === '[object Object]');",
                "TAJS_assert(Object.prototype.toString.call(U? {}: []), 'isMaybeStrPrefix');"
        );
    }

//    @Test
//    public void micro_testAbstractGCPrecision() throws Exception {
//        Options.get().enableDoNotExpectOrdinaryExit();
//        Misc.runSource("function f(v){return v.p;}",
//                "f({p: true})",
//                "if(f()){", // definite error (mainly due to abstract GC)
//                "	TAJS_assert(false);",
//                "}else{",
//                "	TAJS_assert(false);",
//                "}",
//                "TAJS_assert(false);"
//        );
//    }

    @Test
    public void micro_testLoopBeforeCallSequence() {
        Misc.run("test-resources/src/micro/testLoopBeforeCallSequence.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void arrayJoin() {
        Misc.runSource("",
                "var joined = ['foo',,'baz'].join();",
                "TAJS_assert(joined === 'foo,,baz');",
                ""
        );
    }

    @Test
    public void micro_numberNaN() {
        Misc.runSource("TAJS_assert(isNaN(NaN));",
                "TAJS_assert(isNaN(Number(NaN)));",
                "TAJS_assert(isNaN(Number('NaN')));",
                "TAJS_assert(isNaN(Number(' NaN ')));",
                "",
                "function toNumber1(val) {return Number(val);}",
                "function toNumber2(val) {return Number(val);}",
                "",
                "toNumber1(' NaN ');",
                "TAJS_assert(toNumber1('123'), 'isMaybeNaN');",
                "",
                "toNumber2('123');",
                "TAJS_assert(toNumber2(' NaN '), 'isMaybeNaN');",
                ""
        );
    }

    @Test
    public void numberToString() {
        Misc.runSource("",
                "TAJS_assert('111111111111111110000' === (111111111111111111111).toString());",
                "TAJS_assert('-111111111111111110000' === (-111111111111111111111).toString());",
                "TAJS_assert('0.00001' === (0.00001).toString());",
                "TAJS_assert('1e-7' === (0.0000001).toString());",
                "");
    }

    @Test
    public void numberFormatters() {
        Misc.runSource("",
                "TAJS_assert('1.1111111111111111e+21' === (1111111111111111111111).toFixed(8));",
                "TAJS_assert('-1.1236e-4' === (-0.000112356).toExponential(4));",
                "TAJS_assert('0.000555000000000000' === (0.000555).toPrecision(15));",
                "");
    }

    @Test
    public void arrayShifting_lit() {
        Misc.runSource("",
                "var a = ['foo', 'bar'];",
                "a.shift()",
                "TAJS_assert('bar' === a[0]);",
                "");
    }

    @Test
    public void arrayShifting_pushDirect() {
        Misc.runSource("",
                "var a = [];",
                "a.push('foo');",
                "a.push('bar');",
                "a.shift()",
                "TAJS_assert('bar' === a[0]);",
                "");
    }

    @Test
    public void arrayShifting_pushIndirect() {
        Misc.runSource("",
                "function push(a, v){a.push(v);}",
                "var a = [];",
                "push(a, 'foo');",
                "push(a, 'bar');",
                "a.shift()",
                "TAJS_assert(a[0], 'isMaybeStrIdentifier');",
                "TAJS_assert(a[0], 'isMaybeUndef');",
                "");
    }

    @Test
    public void arrayProto() {
        Misc.runSource("",
                "Array.prototype[1] = 'bar';",
                "TAJS_assert([][1] === 'bar');",
                "TAJS_assert(['foo',,'baz'][1] === 'bar');",
                ""
        );
    }

    @Test
    public void arrayProtoJoin1() {
        Misc.runSource("",
                "Array.prototype[1] = 'bar';",
                "TAJS_assert(['foo',,'baz'].join() === 'foo,bar,baz');",
                ""
        );
    }

    @Test
    public void arrayProtoJoin2() {
        Misc.runSource("",
                "Array.prototype[1] = 'bar';",
                "var a = []",
                "a[0] = 'foo';",
                "a[2] = 'baz';",
                "TAJS_assert(a.join() === 'foo,bar,baz');",
                ""
        );
    }

    @Test
    public void arrayUnshift() {
        Misc.runSource("",
                "var a = ['foo', 'bar', 'baz']",
                "a.unshift('qux')",
                "TAJS_assert(a.length === 4);",
                "TAJS_assert(a[0] === 'qux');",
                "TAJS_assert(a[1] === 'foo');",
                "TAJS_assert(a[2] === 'bar');",
                "TAJS_assert(a[3] === 'baz');",
                ""
        );
    }

    @Test
    public void arraySplice() {
        Misc.runSource("",
                "var a = ['foo', 'bar', 'baz']",
                "var v = a.splice(0, 1);",
                "TAJS_assert(v[0], 'isMaybeStrIdentifier');",
                "TAJS_assert(a[0], 'isMaybeStrIdentifier');",
                "");
    }

    @Test
    public void testLargeFunctionBody_1500_calls() throws Exception {
        // should not crash
        Misc.run("test-resources/src/micro/largeFunctionBody_1500_calls.js");
    }

    @Test
    public void infinityToStringOtherNum() {
        Misc.runSource("",
                "TAJS_assert((1/0).toString(), 'isMaybeStrOtherNum');",
                "");
    }
//	@Test
//	public void halfValue_toFixed(){ // fails in old JDK, e.g. 1.8.0_31
////		Misc.runSource("",
//				// NB: Older versions of Nashorn produces 0 in both cases!
//				"TAJS_assert((-0.5).toFixed() === '-1');",
//				"TAJS_assert((0.5).toFixed() === '1');",
//				"");
//	}

    @Test
    public void concatNumUInts() {
        Misc.runSource("",
                "var uint = TAJS_make('AnyStrUInt');",
                "var sequenceOfDigits = uint + uint;",
                "TAJS_assert(sequenceOfDigits, 'isMaybeStrOtherNum');",
                "TAJS_assert(sequenceOfDigits + sequenceOfDigits, 'isMaybeStrOtherIdentifierParts');",
                "");
    }

    @Test
    public void undefinedRegisterCompoundPropertyAssignmentWithCall_orig() {
        // should not crash
        Misc.runSource("",
                "var UINT = Math.random()? 0: 1",
                "var rp = [UINT];",
                "function x(i){return i;};",
                "var i = UINT;",
                "var p = [UINT]",
                "var n = UINT",
                "rp[x(i)] &= ~p[n]",
                "");
    }

    @Test
    public void undefinedRegisterCompoundPropertyAssignmentWithCall() {
        // should not crash
        Misc.runSource("",
                "var o = {};",
                "function f(){}",
                "o[f()] += 42",
                "");
    }

    @Test
    public void redefinedArrayLiteralConstructor() {
        // should not crash
        Misc.runSource("",
                "function Array(x, y, z){};",
                "[,,,,]",
                "");
    }

    @Test
    public void arrayJoinFuzzySeparator() {
        // should not crash
        Misc.runSource("",
                "var sep = Math.random()? 'x': 'y';",
                "[1, 2, 3].join(sep);",
                "");
    }

    @Test
    public void issue197_1() {
        Misc.run("test-resources/src/micro/issue197_1.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void issue197_2() {
        Misc.run("test-resources/src/micro/issue197_2.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void stringEscapingForRegExp() {
        Misc.runSource("var replaced = '['.replace('[', 'x');",
                "TAJS_assert(replaced === 'x');");
    }

    @Test
    public void eventListenerRegistrationVariants() {
        Options.get().enableIncludeDom();
        Misc.runSource("var element = document.body;",
                "window.addEventListener('click', function(){ TAJS_dumpValue('window.addEventListener(\\'click\\', ...)'); });",
                "element.addEventListener('click', function(){ TAJS_dumpValue('element.addEventListener(\\'click\\', ...)'); });",
                // dead: not prefixed by 'on'
                "window.click = function(){ TAJS_dumpValue('window.click = ...'); };",
                "element.click = function(){ TAJS_dumpValue('element.click = ...'); };",
                "window.addEventListener('onclick', function(){ TAJS_dumpValue('window.addEventListener(\\'onclick\\', ...)'); });",
                "element.addEventListener('onclick', function(){ TAJS_dumpValue('element.addEventListener(\\'onclick\\', ...)'); });",
                "window.onclick = function(){ TAJS_dumpValue('window.onclick = ...'); };",
                "element.onclick = function(){ TAJS_dumpValue('element.onclick = ...'); };",
                "window.addEventListener('mousedown', function(){ TAJS_dumpValue('window.addEventListener(\\'mousedown\\', ...)'); });",
                "element.addEventListener('mousedown', function(){ TAJS_dumpValue('element.addEventListener(\\'mousedown\\', ...)'); });",
                // dead: not prefixed by 'on'
                "window.mousedown = function(){ TAJS_dumpValue('window.mousedown = ...'); };",
                "element.mousedown = function(){ TAJS_dumpValue('element.mousedown = ...'); };",
                "window.addEventListener('onmousedown', function(){ TAJS_dumpValue('window.addEventListener(\\'onmousedown\\', ...)'); });",
                "element.addEventListener('onmousedown', function(){ TAJS_dumpValue('element.addEventListener(\\'onmousedown\\', ...)'); });",
                "window.onmousedown = function(){ TAJS_dumpValue('window.onmousedown = ...'); };",
                "element.onmousedown = function(){ TAJS_dumpValue('element.onmousedown = ...'); };",
                "");
        Misc.checkSystemOutput();
    }

    @Test
    public void eventListenerRegistrationInEventHandlerVariants() {
        Options.get().enableIncludeDom();
        Misc.runSource("function direct_click_click_registration(){",
                "	TAJS_dumpValue('direct_click_click_registration invoked');",
                "	function indirect_click_click_registration(){ TAJS_dumpValue('indirect_click_click_registration invoked');}",
                "	window.addEventListener('click', indirect_click_click_registration);",
                "}",
                "window.addEventListener('click', direct_click_click_registration);",
                "",
                "function direct_load_load_registration(){",
                "	TAJS_dumpValue('direct_load_load_registration invoked');",
                "	function indirect_load_load_registration(){ TAJS_dumpValue('indirect_load_load_registration invoked');}",
                "	window.addEventListener('load', indirect_load_load_registration);",
                "}",
                "window.addEventListener('load', direct_load_load_registration);",
                "",
                "function direct_load_click_registration(){",
                "	TAJS_dumpValue('direct_load_click_registration invoked');",
                "	function indirect_load_click_registration(){ TAJS_dumpValue('indirect_load_click_registration invoked');}",
                "	window.addEventListener('click', indirect_load_click_registration);",
                "}",
                "window.addEventListener('load', direct_load_click_registration);",
                "",
                "function direct_click_load_registration(){",
                "	TAJS_dumpValue('direct_click_load_registration invoked');",
                "	function indirect_click_load_registration(){ TAJS_dumpValue('indirect_click_load_registration invoked');}",
                "	window.addEventListener('load', indirect_click_load_registration);", // should not trigger: load can not fire after click
                "}",
                "window.addEventListener('click', direct_click_load_registration);",
                "");
        Misc.checkSystemOutput();
    }

    @Test
    public void eventListenerRegistrationInEventHandler_sameKind() {
        Options.get().enableIncludeDom();
        Misc.runSource("function mousedownHandler(){",
                "	TAJS_dumpValue('mousedownHandler triggered');",
                "	addEventListener('mouseup', mouseupHandler);",
                "}",
                "function mouseupHandler(){",
                "	TAJS_dumpValue('mouseupHandler triggered');",
                "}",
                "addEventListener('mousedown', mousedownHandler);"
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void eventListenerRegistrationInEventHandler_differentKind() {
        Options.get().enableIncludeDom();
        Misc.runSource("function mousedownHandler(){",
                "	TAJS_dumpValue('mousedownHandler triggered');",
                "	addEventListener('mouseup', keydownHandler);",
                "}",
                "function keydownHandler(){",
                "	TAJS_dumpValue('keydownHandler triggered');",
                "}",
                "addEventListener('mousedown', mousedownHandler);"
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void DOM_unknownTagName() {
        Misc.run("test-resources/src/micro/unknownTagName.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void regexp_compile() {
        Misc.runSource("var r = /x/;",
                "TAJS_assert(r.source === 'x');",
                "r.compile('y');",
                "TAJS_assert(r.source === 'y');",
                "r.compile('y', 'i');",
                "TAJS_assert(r.ignoreCase);",
                "r.compile(/z/);",
                "TAJS_assert(r.source === 'z');",
                ""
        );
    }

    @Test
    public void object_create() {
        Misc.runSource(
                "if(!TAJS_make('AnyBool')){",
                "	var o1 = Object.create();",
                "	TAJS_assert(false);",
                "}",
                "if(TAJS_make('AnyBool')){",
                "	var o2 = Object.create(null);",
                "	TAJS_dumpObject(o2);",
                "}",
                "if(TAJS_make('AnyBool')){",
                "	var o3 = Object.create(Array);",
                "	TAJS_dumpObject(o3);",
                "}",
                ""
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void unicodeCharAtTest() {
        Misc.runSource("",
                "var a = '\400'.charAt(1)",
                "TAJS_dumpValue('\400')",
                "TAJS_assert(a === '0')",
                "");
    }

    @Test(expected = AnalysisResultException.class /* GitHub #223 */)
    public void unicodeCharAtTest_file() {
        Misc.run("test-resources/src/micro/unicodeCharAtTest.js");
    }

    @Test // TODO: Regression for GitHub issue #236
    public void absentPresentRecovery_regression() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableContextSensitiveHeap();
        Options.get().enableParameterSensitivity();
        Options.get().enableLoopUnrolling(1);
        Misc.run("test-resources/src/micro/absent-present.js");
    }

    @Test
    public void testToString1() {
        Misc.run("test-resources/src/micro/toString1.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void testToString2() {
        Misc.run("test-resources/src/micro/toString2.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void testToString3() {
        Misc.run("test-resources/src/micro/toString3.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void testToString4() {
        Misc.run("test-resources/src/micro/toString4.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void testToString5() {
        Misc.run("test-resources/src/micro/toString5.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void arrayLengthNaN() {
        Misc.runSource("",
                "var arr = [];",
                "arr.length = Math.random()? 42: undefined;",
                "TAJS_assert(arr.length, 'isMaybeNumUInt');",
                "");
    }

    @Test
    public void invalidArrayLengthThrows() {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("",
                "var arr = [];",
                "arr.length = undefined;", // should throw a definite exception
                "TAJS_assert(false);",
                "");
    }

    @Test
    public void arraysort1() throws Exception {
        Misc.run("test-resources/src/micro/arraysort1.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void arraysort2() throws Exception {
        Misc.run("test-resources/src/micro/arraysort2.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void typeofNative1() {
        Misc.runSource("",
                "TAJS_assert(typeof Number === 'function');",
                "");
    }

    @Test
    public void typeofNative2() {
        Misc.runSource("",
                "TAJS_assert(typeof Date === 'function');",
                "");
    }

    @Test
    public void emptyblock() {
        Options.get().enableFlowgraph();
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("",
                "function f () {",
                "}",
                "function g () {",
                "}",
                "function h() {",
                "  while (true){",
                "    f[f] = function () { g(); };",
                "  }",
                "}",
                "h();",
                "");
    }

    @Test(expected = AnalysisLimitationException.class /* TODO: github #257 */)
    public void castbug() {
        Options.get().enableUnevalizer();
        Options.get().enableIncludeDom();
        Misc.runSource("setTimeout(Function,0);");
    }

    @Test
    public void indirecteval() {
//		Options.get().enableUnevalizer();
        Options.get().enableIncludeDom();
        Misc.runSource("document.addEventListener(\"DOMContentLoaded\", eval);");
    }

    @Test
    public void thisObjectForSetTimeoutAndSetInterval() {
        Options.get().enableIncludeDom();
        Misc.runSource("setInterval(function(){TAJS_assert(window === this);}, 0);",
                "setTimeout(function(){TAJS_assert(window === this);}, 0);",
                "");
    }

    @Test
    public void constInitialization() {
        Misc.runSource("const x = 42;",
                "TAJS_assert(x === 42);",
                "");
    }

    @Test(expected = AnalysisResultException.class /* GitHub #182*/)
    public void constReassignment() {
        Misc.runSource("const x = 42;",
                "x = 87;",
                "TAJS_assert(x === 42);",
                "");
    }

    @Test
    public void objectFreeze_return() {
        Options.get().getUnsoundness().setIgnoreMissingNativeModels(true);
        Misc.runSource(
                "var o = {};",
                "TAJS_assert(Object.freeze(o) === o);",
                "");
    }

    @Test(expected = AnalysisResultException.class /* TODO: GitHub #249 */)
    public void objectFreeze_immutable() {
        Options.get().getUnsoundness().setIgnoreMissingNativeModels(true);
        Misc.runSource(
                "var o = {p: 'foo'};",
                "Object.freeze(o)",
                "o.p = 42;",
                "o.q = true;",
                "TAJS_assert(o.p === 'foo');",
                "TAJS_assert(o.q === undefined);",
                "");
    }

    @Test(expected = AnalysisResultException.class /* GitHub #249 */)
    public void objectFreeze_strict() {
        Options.get().getUnsoundness().setIgnoreMissingNativeModels(true);
        Misc.runSource(
                "'use strict';",
                "var o = {};",
                "Object.freeze(o)",
                "o.p = 42;",
                "TAJS_assert(false);",
                "");
    }

    @Test
    public void objectKeys_typeError() {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("Object.keys(undefined);",
                "TAJS_assert(false);"
        );
    }

    @Test
    public void objectKeys_sound() {
        Misc.runSource("var o0 = {};",
                "TAJS_assert(Object.keys(o0).length === 0);",
                "var o1 = {p: 42};",
                "TAJS_assert(Object.keys(o1).length === 1);",
                "TAJS_assert(Object.keys(o1)[0] === 'p');",
                "var o2 = {p: 42, q: 87};",
                "TAJS_assert(Object.keys(o2)[0], 'isStrIdentifier||isMaybeUndef');",
                "TAJS_assert(Object.keys(o2)[1], 'isStrIdentifier||isMaybeUndef');",
                "var oMaybe1 = {};",
                "if(Math.random()){oMaybe1.p = 42;}",
                "TAJS_assert(Object.keys(oMaybe1).length, 'isMaybeNumUInt');",
                "TAJS_assert(Object.keys(oMaybe1)[0], 'isMaybeSingleStr||isMaybeUndef');",
                "var oUnknownArrayIndices = {};",
                "oUnknownArrayIndices[Math.random()? 0: 1] = 42;",
                "var key = Object.keys(oUnknownArrayIndices)[0];",
                "TAJS_assert(key, 'isMaybeStrUInt');",
                "TAJS_assert(key, 'isMaybeUndef');",
                "var oUnknownNonArrayIndices = {};",
                "oUnknownNonArrayIndices[Math.random()? 'a': 'b'] = 42;",
                "var key = Object.keys(oUnknownNonArrayIndices)[0];",
                "TAJS_assert(key, 'isMaybeAnyStr', false);",
                "TAJS_assert(key, 'isMaybeStrIdentifier');",
                "TAJS_assert(key, 'isMaybeUndef');"
        );
    }

    @Test
    public void objectKeys_unknownProperties() {
        Misc.runSource("var o0 = {};",
                "o0[Math.random()? 'a': 'b'] = 42;",
                "TAJS_assert(Object.keys(o0).length, 'isMaybeNumUInt');",
                "TAJS_assert(Object.keys(o0)[0], 'isMaybeSingleStr', false);",
                "TAJS_assert(Object.keys(o0)[0], 'isMaybeStrSomeNonUInt');",
                "TAJS_assert(Object.keys(o0)[0], 'isMaybeStrSomeUInt', false);",
                "var o1 = {p: 42};",
                "o1[Math.random()? 'a': 'b'] = 42;",
                "TAJS_assert(Object.keys(o1).length, 'isMaybeNumUInt');",
                "TAJS_assert(Object.keys(o1)[0], 'isMaybeSingleStr', false);",
                "TAJS_assert(Object.keys(o1)[0], 'isMaybeStrSomeNonUInt');",
                "TAJS_assert(Object.keys(o1)[0], 'isMaybeStrSomeUInt', false);",
                "var o2 = {p: 42, q: 87};",
                "o2[Math.random()? 'a': 'b'] = 42;",
                "TAJS_assert(Object.keys(o2).length, 'isMaybeNumUInt');",
                "TAJS_assert(Object.keys(o2)[0], 'isMaybeSingleStr', false);",
                "TAJS_assert(Object.keys(o2)[0], 'isMaybeStrSomeNonUInt');",
                "TAJS_assert(Object.keys(o2)[0], 'isMaybeStrSomeUInt', false);",
                "var a0 = {};",
                "a0[Math.random()? 0: 1] = 42;",
                "TAJS_assert(Object.keys(a0).length, 'isMaybeNumUInt');",
                "TAJS_assert(Object.keys(a0)[0], 'isMaybeSingleStr', false);",
                "TAJS_assert(Object.keys(a0)[0], 'isMaybeStrSomeUInt');",
                "TAJS_assert(Object.keys(a0)[0], 'isMaybeStrSomeNonNumeric', false);",
                "var a1 = {p: 42};",
                "a1[Math.random()? 0: 1] = 42;",
                "TAJS_assert(Object.keys(a1).length, 'isMaybeNumUInt');",
                "TAJS_assert(Object.keys(a1)[0], 'isMaybeSingleStr', false);",
                "TAJS_assert(Object.keys(a1)[0], 'isMaybeStrSomeUInt');",
                "TAJS_assert(Object.keys(a1)[0], 'isMaybeStrSomeNonUInt', true);",
                "var a2 = {p: 42, q: 87};",
                "a2[Math.random()? 0: 1] = 42;",
                "TAJS_assert(Object.keys(a2).length, 'isMaybeNumUInt');",
                "TAJS_assert(Object.keys(a2)[0], 'isMaybeSingleStr', false);",
                "TAJS_assert(Object.keys(a2)[0], 'isMaybeStrSomeUInt');",
                "TAJS_assert(Object.keys(a2)[0], 'isMaybeStrSomeNonUInt', true);"
        );
    }

    @Test
    public void objectKeys_unsound() {
        Options.get().getUnsoundness().setUseOrderedObjectKeys(true);
        Misc.runSource("var o0 = {};",
                "TAJS_assert(Object.keys(o0).length === 0);",
                "var o1 = {p: 42};",
                "TAJS_assert(Object.keys(o1).length === 1);",
                "TAJS_assert(Object.keys(o1)[0] === 'p');",
                "var o2 = {p: 42, q: 87};",
                "TAJS_assert(Object.keys(o2).length === 2);",
                "TAJS_assert(Object.keys(o2)[0] === 'p');",
                "TAJS_assert(Object.keys(o2)[1] === 'q');",
                "var oMaybe1 = {};",
                "if(Math.random()){oMaybe1.p = 42;}",
                "TAJS_assert(Object.keys(oMaybe1).length, 'isMaybeNumUInt');",
                "TAJS_assert(Object.keys(oMaybe1)[0], 'isMaybeSingleStr||isMaybeUndef');",
                "var oUnknownArrayIndices = {};",
                "oUnknownArrayIndices[Math.random()? 0: 1] = 42;",
                "var key = Object.keys(oUnknownArrayIndices)[0];",
                "TAJS_assert(key, 'isMaybeStrUInt');",
                "TAJS_assert(key, 'isMaybeUndef');",
                "var oUnknownNonArrayIndices = {};",
                "oUnknownNonArrayIndices[Math.random()? 'a': 'b'] = 42;",
                "var key = Object.keys(oUnknownNonArrayIndices)[0];",
                "TAJS_assert(key, 'isMaybeAnyStr', false);",
                "TAJS_assert(key, 'isMaybeStrIdentifier');",
                "TAJS_assert(key, 'isMaybeUndef');"
        );
    }

    @Test
    public void functionToString_unsound() {
        Options.get().getUnsoundness().setUsePreciseFunctionToString(true);
        Misc.runSource(// plain content
                "TAJS_assertEquals((function foo(bar){baz;}).toString(), 'function foo(bar){baz;}');",
                // whitespace
                "TAJS_assertEquals((function foo ( bar ){ baz ; }).toString(), 'function foo ( bar ){ baz ; }');",
                // removed parts (nb: nashorn-semantics, google-chrome removes some of the comments)
                "TAJS_assertEquals((function /**/foo/**/(/**/bar/**/)/**/{/**/baz;/**/}).toString(), 'function /**/foo/**/(/**/bar/**/)/**/{/**/baz;/**/}');",
                // newlines
                "TAJS_assertEquals((function foo(bar){\nbaz;\n}).toString(), 'function foo(bar){\\nbaz;\\n}');",
                // identical functions
                "var f = !!Math.random()? function f(){}: function f(){};",
                "TAJS_assertEquals(f.toString(), 'function f(){}');",
                // different functions
                "var fg = !!Math.random()? function f(){}: function g(){};",
                "TAJS_assert(fg.toString(), 'isMaybeStrPrefix');",
                // natives
                "TAJS_assertEquals(toString.toString(), 'function toString() { [native code] }');",
                "TAJS_assertEquals(Array.prototype.push.toString(), 'function push() { [native code] }');"
        );
    }

    @Test
    public void functionToString_sound() {
        Misc.runSource("TAJS_assert(toString.toString(), 'isMaybeAnyStr');",
                "TAJS_assert((function f(){}).toString(), 'isMaybeAnyStr');"
        );
    }

    @Test
    public void functionToString_prototypejsExample() {
        Options.get().getUnsoundness().setUsePreciseFunctionToString(true);
        Misc.runSource(// prototype.js depends on Function.prototype.toString for extracting argument names
                "function f(a, b, c){foo;}",
                "var args = f.toString().match(/^[\\s\\(]*function[^(]*\\((.*?)\\)/)[1].split(',');",
                "TAJS_assert(args.length === 3);",
                "TAJS_assert(args[0] === 'a');",
                "TAJS_assert(args[2] === ' c');" /* this is before a call to strip! */
        );
    }

    @Test
    public void addEventListenerOnAllHTMLElements() {
        Options.get().enableIncludeDom();
        Misc.runSource("document.createElement('div').id = 'ELEMENT';",
                "var element = document.getElementById('ELEMENT');",
                "TAJS_assert(element.addEventListener, 'isMaybeUndef', false);",
                "");
    }

    @Test
    public void testNoImplicitGlobalVarDeclarations() {
        Options.get().getUnsoundness().setNoImplicitGlobalVarDeclarations(true);
        Misc.runSource("var v1 = 42;",
                "TAJS_assert(v1 === 42);",
                "v2 = 42;",
                "TAJS_assert(typeof v2 === 'undefined');"
        );
    }

    @Test
    public void recursiveSetTimeoutWithNulls() {
        Options.get().enableIncludeDom();
        Misc.runSource("function r(f){",
                "	setTimeout(f, 0);",
                "}",
                "r(function f1(){r(function f2(){r(function f3(){r(null)})})});"
        );
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class /* TODO: GitHub #257*/)
    public void classCastExceptionRegression1() {
        Options.get().enableUnevalizer();
        Options.get().enableIncludeDom();
        Misc.runSource("function f() {",
                "}",
                "window[f] = window[f];"
        );
    }

    @Test(expected = AnalysisLimitationException.class /* GitHub #257*/)
    public void classCastExceptionRegression2() {
        Options.get().enableUnevalizer();
        Options.get().enableIncludeDom();
        Misc.runSource("setTimeout(Function,0);"
        );
    }

    @Test
    public void classCastExceptionRegression3() {
        Options.get().enableIncludeDom();
        Misc.runSource("setTimeout(Array,0);"
        );
    }

    @Test
    public void missingHostFunctionModelOnObject() {
        // check that all static functions on Object have a transfer function // TODO generalize this test?
        Misc.runSource("var s = Math.random()? 'foo': 'bar';",
                "Object[s]();"
        );
    }

    @Test // (expected = AnalysisLimitationException.class /* should fix itself once we do some more modeling */)
    public void missingHostFunctionModelOnFunction() {
        Options.get().enableUnevalizer();
        Misc.runSource("var s = Math.random()? 'foo': 'bar';",
                "Function[s]();"
        );
    }

    @Test
    public void missingHostFunctionModelOnString() {
        Misc.runSource("var s = Math.random()? 'foo': 'bar';",
                "TAJS_dumpValue(String('x')[s]);"
        );
    }

    @Test
    public void missingHostFunctionModelOnNumber() {
        Misc.runSource("var s = Math.random()? 'foo': 'bar';",
                "Number(0)[s]();"
        );
    }

    @Test
    public void missingHostFunctionModelOnBoolean() {
        Misc.runSource("var s = Math.random()? 'foo': 'bar';",
                "Boolean(true)[s]();"
        );
    }

    @Test
    public void missingHostFunctionModelOnRegExp() {
        Misc.runSource("var s = Math.random()? 'foo': 'bar';",
                "RegExp('')[s]();"
        );
    }

    @Test
    public void indirectAccessToFunctionConstructor() {
        Misc.runSource("TAJS_assert(Object.constructor === Function);",
                "TAJS_assert(Object.prototype.constructor.constructor === Function);",
                "TAJS_assert(({}).constructor.constructor === Function);"
        );
    }

    @Test
    public void lazyEventHandler1() {
        Options.get().enableIncludeDom();
        Misc.runSource("var x = false;",
                "function f(h){",
                "	setTimeout(h, 0);",
                "}",
                "f(function g(){x = true;});",
                "setTimeout(function(){TAJS_assert(x, 'isMaybeAnyBool');}, 0);"
        );
    }

    @Test
    public void lazyEventHandler2() {
        Options.get().enableIncludeDom();
        Misc.runSource("var x = false;",
                "function f(h){",
                "	window.onclick = h;",
                "}",
                "f(function g(){x = true;});",
                "setTimeout(function(){TAJS_assert(x, 'isMaybeAnyBool');}, 0);"
        );
    }

    @Test
    public void asyncEvents() {
        Options.get().enableAsyncEvents();
        Misc.runSource("function f(){TAJS_dumpValue('executed');}",
                "TAJS_asyncListen(f);",
                "");
        Misc.checkSystemOutput();
    }

    @Test
    public void asyncEventsWeak() {
        Options.get().enableAsyncEvents();
        Misc.runSource("var x = false;",
                "function f(){x = true;}",
                "function g(){TAJS_assert(x, 'isMaybeAnyBool');}",
                "TAJS_asyncListen(f);",
                "TAJS_asyncListen(g);",
                "");
    }

    @Test
    public void asyncEventsDelayed() {
        Options.get().enableAsyncEvents();
        Misc.runSource("var x = false;",
                "function f(){x = true;}",
                "TAJS_asyncListen(f);",
                "TAJS_assert(x === false);",
                "");
    }

    @Test
    public void asyncEventsMultiple() {
        Options.get().enableAsyncEvents();
        Misc.runSource("var i = 0;",
                "function f(){i++;}",
                "function g(){TAJS_assert(i, 'isMaybeNumUInt');}",
                "TAJS_asyncListen(f);",
                "TAJS_asyncListen(g);",
                "");
    }

    @Test
    public void asyncEventsCrashing() {
        Options.get().enableAsyncEvents();
        Misc.runSource("function f(){foo.bar()}",
                "TAJS_asyncListen(f);");
    }

    @Test
    public void asyncEventsRecency() {
        Options.get().enableAsyncEvents();
        Misc.runSource("function mk(x){",
                "	return function(){",
                "		TAJS_assert(x, 'isMaybeSingleStr', false);",
                "	}",
                "}",
                "var f = mk('f')",
                "TAJS_asyncListen(f);",
                "var g = mk('g')",
                "TAJS_asyncListen(g);",
                "");
    }

    @Test
    public void toStringConversions() {
        Options.get().getUnsoundness().setUsePreciseFunctionToString(true);
        Misc.runSource("TAJS_assertEquals([].toString(), '');",
                "TAJS_assertEquals([] + '', '');",
                "TAJS_assertEquals(({}).toString(), '[object Object]');",
                "TAJS_assertEquals(({}) + '', '[object Object]');",
                "TAJS_assertEquals(/x/.toString(), '/x/');",
                "TAJS_assertEquals(/x/ + '', '/x/');",
                "TAJS_assertEquals('x'.toString(), 'x');",
                "TAJS_assertEquals('x' + '', 'x');",
                "TAJS_assertEquals((function(x){}).toString(), 'function(x){}');",
                "TAJS_assertEquals((function(x){}) + '', 'function(x){}');",
                "TAJS_assertEquals(true.toString(), 'true');",
                "TAJS_assertEquals(true + '', 'true');",
                "TAJS_assertEquals(0..toString(), '0');",
                "TAJS_assertEquals(0 + '', '0');");
    }

    @Test
    public void arrayToStringWithUndefinedJoin() {
        Misc.runSource("var a = [];",
                "a.join = undefined;",
                "TAJS_assert(a.toString() === '[object Array]');");
    }

    @Test(expected = AnalysisLimitationException.class)
    public void arrayToStringWithRedefinedJoin() {
        Misc.runSource("var a = [];",
                "a.join = function(){};",
                "a.toString();");
    }

    @Test
    public void cyclicArrayJoin() {
        Misc.runSource("var a = [];",
                "a[0] = a;",
                "TAJS_assert(a.join(), 'isMaybeAnyStr');");
    }

    @Test
    public void localeString_sound() {
        Misc.runSource("TAJS_assert(1..toLocaleString(), 'isMaybeAnyStr');");
    }

    @Test
    public void localeString_unsound() {
        Options.get().getUnsoundness().setIgnoreLocale(true);
        Misc.runSource("TAJS_assert(1..toLocaleString() === '1');");
    }

    @Test
    public void functionName() {
        Misc.runSource("TAJS_assert((function f(){}).name === 'f');");
    }

    @Test
    public void maybeCoercionError() {
        Misc.runSource("var x = {toString: {}};",
                "var o = {y: 'z'};",
                "var thrown = false;",
                "var notThrown = false;",
                "try{",
                "	TAJS_assert(o[Math.random()? x: 'y'] === 'z');",
                "	notThrown = true;",
                "}catch(e){",
                "	thrown = true;",
                "}",
                "TAJS_assert(notThrown, 'isMaybeAnyBool');",
                "TAJS_assert(thrown, 'isMaybeAnyBool');");
    }

    @Test
    public void stringConcatenation_identifierAndNumbersExclusions() {
        Misc.runSource("var x = TAJS_join('x y', '234', 'foo', 'NaN', '11ar');",
                "var y = '<foobar';",
                "var z = x + y;",
                "TAJS_assert(x, 'isMaybeAnyStr');",
                "TAJS_assert(z, 'isMaybeStrOther');",
                "TAJS_assert('foo' !== z);",
                "TAJS_assert('234' !== z);",
                "TAJS_assert('Infinity' !== z);",
                "TAJS_assert('3.14' !== z);");
    }

    @Test
    public void negateUInt() {
        Misc.runSource("TAJS_assert(-(Math.random()? 2: 3), 'isMaybeNumUInt', false)");
    }

    @Test
    public void stringIndexing() {
        Misc.runSource("var s = 'xyz';",
                "var S = new String('xyz');",
                "var str = Math.random()? 'xyz': 'abc';",
                "var UInt = Math.random()? 0: 1;",
                "TAJS_assert(s[0] === 'x');",
                "TAJS_assert(s['0'] === 'x');",
                "TAJS_assert(s[1] === 'y');",
                "TAJS_assert(s['1'] === 'y');",
                "TAJS_assert(s[3] === undefined);",
                "TAJS_assert(s[-1] === undefined);",
                "TAJS_assert(s[UInt], 'isMaybeUndef');",
                "TAJS_assert(s[UInt], 'isMaybeStrIdentifier');",
                "TAJS_assert(str[0], 'isMaybeUndef');",
                "TAJS_assert(str[0], 'isMaybeAnyStr');",
                "TAJS_assert(str[UInt], 'isMaybeUndef');",
                "TAJS_assert(str[UInt], 'isMaybeAnyStr');",
                "TAJS_assert(S[0] === 'x');",
                "TAJS_assert(S[UInt], 'isMaybeUndef');",
                "TAJS_assert(S[UInt], 'isMaybeStrIdentifier');",
                "S[0] = 0;",
                "TAJS_assert(S[0], 'isMaybeSingleStr');");
    }

    @Test
    public void presentNoValueBug() throws Exception {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/present-no-value-bug.js");
    }

    @Test
    public void presentNoValueNoBug() throws Exception {
        Main.reset();
        Options.get().enableTest();
        Misc.run("test-resources/src/micro/present-no-value-no-bug.js");
    }

    @Test
    public void presentNoValueNoBug2() throws Exception {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/present-no-value-no-bug-2.js");
    }

    @Test
    public void unexpectedValueBug() throws Exception {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableNoRecency();
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.run("test-resources/src/micro/unexpectedValueBug.js");
    }

    @Test
    public void summarizationBug274() throws Exception {
        Main.reset();
        Options.get().enableTest();
        Misc.run("test-resources/src/micro/summarization-bug-274-minimal.js");
    }

    @Test
    public void stringPropertyAccess() {
        Options.get().enableNoRecency();
        Misc.runSource("var s1 = 'foo';",
                "TAJS_assert(s1.search, 'isMaybeObject');",
                "var s2 = TAJS_make('AnyBool')? '%W': TAJS_make('AnyBool')? 42: TAJS_make('AnyBool')? 4.2: undefined;",
                "TAJS_assert(s2.search, 'isMaybeObject')");
    }

    @Test
    public void stringPropertyAccess2() {
        Misc.runSource("TAJS_assert('foo'.search, 'isMaybeObject');");
    }

    @Test
    public void stringPropertyAccess3() {
        Options.get().enableNoRecency();
        Misc.runSource("TAJS_assert('foo'.search, 'isMaybeObject');");
    }

    @Test
    public void objectPropertyAccess() {
        Options.get().enableNoRecency();
        Misc.runSource("TAJS_assert(({}).toString, 'isMaybeObject');");
    }

    @Test
    public void primitiveProperties() {
        Options.get().enableNoRecency();
        Misc.runSource("TAJS_assert(''.toString, 'isMaybeObject');",
                "TAJS_assert(''.search, 'isMaybeObject');",
                "TAJS_assert(42..toString, 'isMaybeObject');",
                "TAJS_assert(42..toFixed, 'isMaybeObject');",
                "TAJS_assert(true.toString, 'isMaybeObject');");
    }

    @Test
    public void testGlobalObjectLabelResetting() throws Exception {
        Main.reset();
        Options.get().enableTest();
        Misc.runSourcePart("a", "this.x = 42; TAJS_assert(this.x === 42)");
        Main.reset();
        Options.get().enableTest();
        Options.get().enableNoRecency();
        Misc.runSourcePart("b", "", "this.x = 42; TAJS_assert(this.x, 'isMaybeSingleNum'); TAJS_assert(this.x, 'isMaybeUndef');");
        Main.reset();
        Options.get().enableTest();
        Misc.runSourcePart("c", "", "", "this.x = 42; TAJS_assert(x === 42)");
    }

    @Test
    public void testFunction_bind_creatingBoundFunction() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enablePolyfillMDN();
        Misc.run("test-resources/src/micro/function_bind_creatingBoundFunction.js");
    }

    @Test
    public void testFunction_bind_partiallyAppliedFunctions() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enablePolyfillMDN();
        Misc.run("test-resources/src/micro/function_bind_partiallyAppliedFunctions.js");
    }

    @Test(expected = AnalysisResultException.class /* TODO: github #281 */)
    public void testFunction_bind_boundFunctionsUsedAsConstructors() {
        Options.get().enableTest();
        Options.get().enablePolyfillMDN();
        Misc.run("test-resources/src/micro/function_bind_boundFunctionsUsedAsConstructors.js");
    }

    @Test
    public void substring() {
        Misc.runSource("'foo'.substring();");
    }

    @Test
    public void stringMethod_noLazy() {
        Options.get().enableNoLazy();
        Misc.runSource("'foo'.substring(2);");
    }

    @Test
    public void numberMethod_noLazy() {
        Options.get().enableNoLazy();
        Misc.runSource("42.0.toFixed()");
    }

    @Test
    public void coercionWarnings() {
        Misc.runSource("'foo'.charAt();",
                "'foo'.charAt(undefined);",
                "String.fromCharCode();", // does not cause a warning, since argument is optional (i.e. the default undefined is not coerced to NaN)
                "String.fromCharCode(undefined);");
        Misc.checkSystemOutput();
    }

    @Test
    public void __proto__soundness_check() {
        Misc.runSource("var x = {n : 5}",
                "var y = {__proto__ : x}",
                "if(y.n == 5) {} else { TAJS_assert(false); }");
    }

    @Test(expected = AnalysisResultException.class)
    public void __proto__soundness_check_error() {
        Misc.runSource("var x = {n : 5}",
                "var y = {__proto__ : x}",
                "if(y.n == 5) {TAJS_assert(false);} else {  }");
    }

    @Test
    public void test_bug_9_8_2016_a() throws Exception {
        Main.reset();
        Options.get().enableTest();
        String file = "test-resources/src/micro/bug-9-8-2016-a.js";
        Misc.run(file);
    }

    @Test
    public void test_bug_9_8_2016_b() throws Exception {
        Main.reset();
        Options.get().enableTest();
        String file = "test-resources/src/micro/bug-9-8-2016-b.js";
        Misc.run(file);
    }

    @Test
    public void postfixCoercionResult() {
        Misc.runSource("var a = '';",
                "TAJS_assert(a++ === 0);",
                "TAJS_assert(a === 1);");
    }

    @Test
    public void prefixCoercionResult() {
        Misc.runSource("var a = '';",
                "TAJS_assert(++a === 1);",
                "TAJS_assert(a === 1);");
    }

    @Test
    public void prefixPropertyResult() {
        Misc.runSource("var o = {p: 0};",
                "TAJS_assert(++o.p === 1);",
                "TAJS_assert(o.p === 1);");
    }

    @Test
    public void negation() throws Exception {
        Misc.runSource("var UINT = Math.random()? 1: 2;",
                "var OTHER = Math.random()? 0.1: 0.2;",
                "TAJS_assert(-UINT, 'isMaybeNumUInt', false);",
                "TAJS_assert(-UINT, 'isMaybeNumOther', true);",
                "TAJS_assert(-OTHER, 'isMaybeNumUInt', true);",
                "TAJS_assert(-OTHER, 'isMaybeNumOther', true);");
    }

    @Test
    public void keyword() {
        Misc.runSource("var byte = 0;");
    }

    @Test
    public void onload() {
        Options.get().enableIncludeDom();
        Misc.runSource("var TRIGGERED = false;",
                "window.onload = function(){TRIGGERED = true;}",
                "window.addEventListener('load', function(){TAJS_assert(TRIGGERED, 'isMaybeAnyBool')});");
    }

    @Test
    public void eventhandler_issue224() {
        Misc.run("test-resources/src/micro/eventhandler_issue224.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void eventhandler_issue224_a() {
        Misc.run("test-resources/src/micro/eventhandler_issue224_a.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void stringContainment() {
        Misc.runSource("var ANY_STR = TAJS_make('AnyStr');",
                "var IDENT_STR = TAJS_join('foo', 'bar');",
                "var PREFIX_IDENT_STR = TAJS_join('xfoo', 'xbar');",
                "TAJS_assert(ANY_STR.indexOf('x'), 'isMaybeNumUInt');",
                "TAJS_assert(ANY_STR.indexOf('.'), 'isMaybeNumUInt');",
                "TAJS_assert(IDENT_STR.indexOf('x'), 'isMaybeNumUInt');",
                "TAJS_assert(IDENT_STR.indexOf('.') === -1);",
                "TAJS_assert(PREFIX_IDENT_STR.indexOf('x'), 'isMaybeNumUInt');",
                "TAJS_assert(PREFIX_IDENT_STR.indexOf('.') === -1);");
    }

    @Test
    public void forInString() {
        Misc.runSource("for (var i in 'foo'){",
                "	TAJS_dumpValue(i);",
                "}");
    }

    @Test
    public void le_ge_leq_geq() {
        Misc.runSource("TAJS_assertEquals(false, 1 > 2);",
                "TAJS_assertEquals(true, 2 > 1);",
                "TAJS_assertEquals(true, 1 < 2);",
                "TAJS_assertEquals(false, 2 < 1);",
                "TAJS_assertEquals(false, 1 >= 2);",
                "TAJS_assertEquals(true, 2 >= 1);",
                "TAJS_assertEquals(true, 1 <= 2);",
                "TAJS_assertEquals(false, 2 <= 1);");
    }

    @Test
    public void le_ge_leq_geq_NAN() {
        Misc.runSource("TAJS_assertEquals(false, 1 > NaN);",
                "TAJS_assertEquals(false, 2 > NaN);",
                "TAJS_assertEquals(false, 1 < NaN);",
                "TAJS_assertEquals(false, 2 < NaN);",
                "TAJS_assertEquals(false, 1 >= NaN);",
                "TAJS_assertEquals(false, 2 >= NaN);",
                "TAJS_assertEquals(false, 1 <= NaN);",
                "TAJS_assertEquals(false, 2 <= NaN);");
    }

    @Test
    public void le_ge_leq_geq_NAN_negate() {
        Misc.runSource("TAJS_assert(!(1 > NaN));",
                "TAJS_assert(!(2 > NaN));",
                "TAJS_assert(!(1 < NaN));",
                "TAJS_assert(!(2 < NaN));",
                "TAJS_assert(!(1 >= NaN));",
                "TAJS_assert(!(2 >= NaN));",
                "TAJS_assert(!(1 <= NaN));",
                "TAJS_assert(!(2 <= NaN));");
    }

    @Test
    public void le_ge_leq_geq_coercedNAN() {
        Misc.runSource("TAJS_assertEquals(false, 1 > undefined);",
                "TAJS_assertEquals(false, 2 > undefined);",
                "TAJS_assertEquals(false, 1 < undefined);",
                "TAJS_assertEquals(false, 2 < undefined);",
                "TAJS_assertEquals(false, 1 >= undefined);",
                "TAJS_assertEquals(false, 2 >= undefined);",
                "TAJS_assertEquals(false, 1 <= undefined);",
                "TAJS_assertEquals(false, 2 <= undefined);");
    }

    @Test
    public void le_ge_leq_geq_str() {
        Misc.runSource("TAJS_assertEquals(false, 'a' > 'b');",
                "TAJS_assertEquals(true, 'b' > 'a');",
                "TAJS_assertEquals(true, 'a' < 'b');",
                "TAJS_assertEquals(false, 'b' < 'a');",
                "TAJS_assertEquals(false, 'a' >= 'b');",
                "TAJS_assertEquals(true, 'b' >= 'a');",
                "TAJS_assertEquals(true, 'a' <= 'b');",
                "TAJS_assertEquals(false, 'b' <= 'a');");
    }

    @Test
    public void bonusPrototypesOnPrimitives() {
        Misc.runSource("var x = 0;",
                "function Funky(a, b, c) { return 7; }",
                "Number.prototype.__proto__ = Funky;",
                "TAJS_assertEquals(3, x.length);",
                "TAJS_assertEquals('Funky', Funky.name);",
                "TAJS_assertEquals('Funky', x.name);");
    }

    @Test
    public void toStringValueOfBug() {
        Misc.runSource("/a/.test({toString:function(){return {};}, valueOf:function(){return '';}});");
    }

    @Test
    public void exceptionFromNativeFunction1() {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("1.2.toFixed(10);",
                "3.4.toFixed(100);",
                "TAJS_assert(false);");
    }

    @Test
    public void exceptionFromNativeFunction2() {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("'x'.match('y');",
                "'z'.match(')');",
                "TAJS_assert(false);");
    }

    @Test
    public void exceptionFromNativeFunction3() {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("'x'.search('y');",
                "'z'.search(')');",
                "TAJS_assert(false);");
    }

    @Test
    public void plusMinus0() {
        Misc.runSource("var uint = Math.random()? 42: 87;",
                "TAJS_assertEquals(uint, uint + 0);",
                "TAJS_assertEquals(uint, uint - 0);",
                "TAJS_assertEquals(uint, 0 + uint);");
    }

    @Test
    public void dateNumber() {
        Misc.runSource("TAJS_assert(new Date() - 0, 'isMaybeNumUInt||isMaybeNumOther');",
                "TAJS_assert(+new Date(), 'isMaybeNumUInt||isMaybeNumOther');");
    }

    @Test
    public void tracifier2() { // from unit/iterate_scripts_for_in
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("var srcs",
                "(srcs = [])",
                "(srcs[\"push\"])(67)");
    }

    @Test
    public void tracifier2_desugared() { // from unit/iterate_scripts_for_in
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("var srcs",
                "(srcs = [])(srcs.push)(67)");
    }

    @Test
    public void element_onEvent_isMaybeNull() {
        Options.get().enableIncludeDom();
        Misc.runSource("TAJS_assert(window.document.body.onkeyup, 'isMaybeNull');",
                "TAJS_assert(window.document.body.onload, 'isMaybeNull');",
                "TAJS_assert(window.document.body.onunload, 'isMaybeNull');");
    }

    @Test
    public void withThis() {
        Misc.runSource("var global = this",
                "with ({}){",
                "	TAJS_assertEquals(global, this);",
                "	(function(){",
                "		TAJS_assertEquals(global, this);",
                "	})();",
                "	f = function(){",
                "		TAJS_assertEquals(global, this);",
                "	};",
                "	f();",
                "	function g(){",
                "		TAJS_assertEquals(global, this);",
                "	};",
                "	g();",
                "}");
    }

    @Test
    public void sanity_writeProperty() {
        Misc.runSource("var o = {p: 42}",
                "o.q = 'foo';",
                "TAJS_assertEquals(42, o.p);",
                "TAJS_assertEquals('foo', o.q);");
    }

    @Test
    public void stringProtoModification() {
        Misc.runSource("String.prototype.FOO = function () {",
                "	this.indexOf();",
                "};",
                "'X'.FOO();",
                "TAJS_dumpValue('Hello');");
    }

    @Test
    public void stringProtoModification_noPolymorphism() {
        Options.get().enableNoPolymorphic();
        Misc.runSource("String.prototype.FOO = function () {",
                "	this.indexOf();",
                "};",
                "'X'.FOO();",
                "TAJS_dumpValue('Hello');");
    }

    @Test
    public void stringLength() {
        Misc.runSource("TAJS_assertEquals(3, 'foo'.length);");
    }

    @Test
    public void stringCharacter() {
        Misc.runSource("TAJS_assertEquals('b', 'abc'[1]);");
    }

    @Test
    public void throwOnNewNativeNonConstructor() {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("new Number.toString()",
                "TAJS_assert(false);");
    }

    @Test
    public void noLazyRequiredForSoundnessBug() {
        Main.reset();
        Options.get().enableTest();
        Misc.run("test-resources/src/micro/noLazy-required-for-soundness-bug.js");
        Main.reset();
        Options.get().enableTest();
        Options.get().enableNoLazy();
        Misc.run("test-resources/src/micro/noLazy-required-for-soundness-bug.js");
    }

    @Test
    public void emptyValueWithoutLazyBug() {
        Main.reset();
        Options.get().enableTest();
        Misc.run("test-resources/src/micro/empty-value-bug.js");
        Main.reset();
        Options.get().enableTest();
        Options.get().enableNoLazy();
        Misc.run("test-resources/src/micro/empty-value-bug.js");
    }

    @Test
    public void argumentsMutation() {
        Options.get().getSoundnessTesterOptions().setTest(false); // use of TAJS_make
        Misc.runSource(
                "function f(){",
                "	TAJS_assertEquals('foo', arguments[0]);",
                "	TAJS_assertEquals(undefined, arguments[1]);",
                "	TAJS_assertEquals(1, arguments.length);",
                "",
                "	arguments[0] = 42;",
                "	arguments[1] = true;",
                "	TAJS_assertEquals(42, arguments[0]);",
                "	TAJS_assertEquals(true, arguments[1]);",
                "	TAJS_assertEquals(1, arguments.length);",
                "",
                "	Array.prototype.push.call(arguments, false);",
                "	TAJS_assertEquals(42, arguments[0]);",
                "	TAJS_assertEquals(false, arguments[1]);",
                "	TAJS_assertEquals(undefined, arguments[2]);",
                "	TAJS_assertEquals(2, arguments.length);",
                "",
                "	Array.prototype.pop.call(arguments);",
                "	TAJS_assertEquals(42, arguments[0]);",
                "	TAJS_assertEquals(undefined, arguments[1]);",
                "	TAJS_assertEquals(undefined, arguments[2]);",
                "	TAJS_assertEquals(1, arguments.length);",
                "}",
                "f('foo')");
    }

    @Test
    public void sortPropertyPrecision() {
        Misc.runSource(
                "function f(x, y){",
                "   TAJS_assert(x, 'isMaybeUndef', false);",
                "   TAJS_assert(y, 'isMaybeUndef', false);",
                "}",
                "[1, 2, 3, 4].sort(f);");
    }

    @Test
    public void sortShortArrays() {
        Misc.runSource(
                "function f(x, y){",
                "   TAJS_assert(false);",
                "}",
                "[].sort(f);",
                "[1].sort(f);");
    }

    @Test
    public void preciseApplyWithMixedArgumentCounts() {
        Misc.runSource("function f(x, y, z){",
                "   TAJS_assertEquals(true, x);",
                "   TAJS_assertEquals(false, y);",
                "   TAJS_assertEquals(undefined, z);",
                "	if(arguments.length == 13){",
                "		var v12 = arguments[12];",
                "		v12.KILL_UNDEFINED;",
                "   	TAJS_assertEquals(12, v12);",
                "	}",
                "}",
                "var argumentArray1 = [true, false];",
                "var argumentArray2 = [true, false, undefined];",
                "var argumentArray3 = [true, false, undefined,3,4,5,6,7,8,9,10,11,12];",
                "f.apply(this, argumentArray1);",
                "f.apply(this, argumentArray2);",
                "f.apply(this, argumentArray3);",
                "var argumentArray123 = TAJS_make('AnyBool')? argumentArray1: TAJS_make('AnyBool')? argumentArray2: argumentArray3",
                "f.apply(this, argumentArray123);"
        );
    }
//	@Test
//	public void preciseApplyWithUnknownArgumentCounts() {
////		Misc.runSource(
//				"function f(){",
//				"	TAJS_assertEquals(0, arguments[0])",
//				"	TAJS_assertEquals(1, arguments[1])",
//				"	TAJS_assertEquals(2, arguments[2])",
//				"	TAJS_assertEquals(3, arguments[3])",
//				"	TAJS_assertEquals(4, arguments[4])",
//				"	TAJS_assertEquals(5, arguments[5])",
//				"	TAJS_assertEquals(6, arguments[6])",
//				"	TAJS_assertEquals(7, arguments[7])",
//				"	TAJS_assertEquals(8, arguments[8])",
//				"	TAJS_assertEquals(9, arguments[9])",
//				"	TAJS_assertEquals(MIXED, arguments[10])",
//				"	TAJS_assertEquals(MIXED, arguments[11])",
//				"}",
//				"var U = !!Math.random();",
//				"var UINT = U? 0: 50;",
//				"var MIXED = TAJS_join(UINT, true, undefined);",
//				"var args = []",
//				"args[UINT] = true;",
//				"args[0] = 0;",
//				"args[1] = 1;",
//				"args[2] = 2;",
//				"args[3] = 3;",
//				"args[4] = 4;",
//				"args[5] = 5;",
//				"args[6] = 6;",
//				"args[7] = 7;",
//				"args[8] = 8;",
//				"args[9] = 9;",
//				"args[10] = 10;",
//				"args[11] = 11;",
//				"args[12] = 12;",
//				"args[13] = 13;",
//				"f.apply(this, args);"
//		);
//	}

    @Test
    public void preciseApplyWithUnknownArgumentCounts_small() {
        Misc.runSource("function f(x){",
                "	TAJS_assertEquals(0, x)",
                "}",
                "var U = !!Math.random();",
                "var UINT = U? 20: 50;",
                "var args = []",
                "args[UINT] = true;",
                "args[0] = 0;",
                "f.apply(this, args);"
        );
    }

    @Test
    public void domEventHandlerSetter() {
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/micro/domEventHandlerSetter.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void propertyAccess_bug_noLazy() {
        Options.get().enableNoLazy();
        Misc.runSource("var v1 = Object.prototype.toString;",
                "TAJS_dumpValue(v1);");
    }

    @Test
    public void domObjectPropertyReadWrites() {
        Options.get().enableIncludeDom();
        Misc.runSource("var p1 = true;",
                "window.p2 = false;",
                "var e = document.body;",
                "e.p3 = 0;",
                "p4 = 1;",
                "function p5(){}",
                "TAJS_assertEquals(true, p1);", // strong update on variable writes
                "TAJS_assertEquals(false, window.p2);", // strong update on singleton property writes
                "TAJS_assertEquals(TAJS_join(undefined, 0), e.p3);", // weak update on summary property writes
                "TAJS_assertEquals(1, p4);", // strong update on implicit variable writes
                "TAJS_assert(p5, 'isMaybeObject');", // strong update on function declarations
                "p1 = false;",
                "window.p2 = true;",
                "e.p3 = 1;",
                "p4 = 0;",
                "TAJS_assertEquals(false, p1);", // strong update on variable writes
                "TAJS_assertEquals(true, window.p2);", // strong update on singleton property writes
                "TAJS_assertEquals(TAJS_join(undefined, 0, 42), e.p3);", // weak update on summary property writes
                "TAJS_assertEquals(0, p4);", // strong update on implicit variable writes
                // test typeof-accesses
                "TAJS_assert(typeof p1 === 'boolean');",
                "TAJS_assert(typeof window.p2 === 'boolean');",
                "TAJS_assert(typeof p4 === 'number');",
                "TAJS_assert(typeof p5 === 'function');"
        );
    }

    @Test
    public void arrayLiteralUses() {
        Options.get().enableNoLazy();
        Misc.runSource("var v1 = [].length;",
                "var v2 = [].sort;",
                "var v3 = [].sort()",
                "var v4 = [].sort(function f(){})",
                "function g(){}",
                "var v5 = [].sort(g)"
        );
    }

    @Test
    public void arrayPropertyAccesses_noLazy() {
        Options.get().enableNoLazy();
        Misc.runSource("var v1 = [].xxx;",
                "var v2 = [].length;",
                "var v3 = [].toString;",
                "var v4 = ['x'][0];",
                "TAJS_assertEquals(undefined, v1);",
                "TAJS_assertEquals(0, v2);",
                "TAJS_assertEquals(Array.prototype.toString, v3);",
                "TAJS_assertEquals('x', v4);"
        );
    }

    @Test
    public void arrayPropertyAccesses_bug_noLazy() {
        Options.get().enableNoLazy();
        Misc.runSource("var v1 = [].xxx;"
        );
    }

    @Test
    public void domObjectSetterReadWrites() {
        Options.get().enableIncludeDom();
        Misc.runSource("var onclick = true;",
                "window.ondblclick = false;",
                "var e = document.body;",
                "e.onmousemove = 0;",
                "onmouseover = 1;",
                "function onmouseout(){}",
                "TAJS_assert(onclick, 'isMaybeTrueButNotFalse');", // weak update on variable writes
                "TAJS_assert(onclick, 'isMaybeNull');", // weak update on variable writes
                "TAJS_assert(onclick, 'isMaybeObject');", // weak update on variable writes
                "TAJS_assert(window.ondblclick, 'isMaybeFalseButNotTrue');", // weak update on singleton property writes
                "TAJS_assert(window.ondblclick, 'isMaybeNull');", // weak update on singleton property writes
                "TAJS_assert(window.ondblclick, 'isMaybeObject');", // weak update on singleton property writes
                "TAJS_assert(e.onmousemove, 'isMaybeSingleNum');", // weak update on summary property writes
                "TAJS_assert(e.onmousemove, 'isMaybeNull');", // weak update on summary property writes
                "TAJS_assert(e.onmousemove, 'isMaybeObject');", // weak update on summary property writes
                "TAJS_assert(onmouseover, 'isMaybeSingleNum');", // weak update on implicit variable writes
                "TAJS_assert(onmouseover, 'isMaybeNull');", // weak update on implicit variable writes
                "TAJS_assert(onmouseover, 'isMaybeObject');", // weak update on implicit variable writes
                "TAJS_assert(onmouseout, 'isMaybeObject');", // weak update on function declarations
                "TAJS_assert(onmouseout, 'isMaybeNull');" // weak update on function declarations
        );
    }

    @Test
    public void writingToArrayLengthInWith() {
        Misc.runSource("var x = []",
                "with (x) {length = 87}",
                "TAJS_assertEquals(87, x.length);",
                "var y = []",
                "with (y) {length = {valueOf: function() {return 42}}}",
                "TAJS_assertEquals(42, y.length);"
        );
    }

    @Test
    public void maybe_infinite_loop() {
        Misc.runSource("var f = function() {return 42;}",
                "if (Math.random()) { f = function g() {g();} } ",
                "TAJS_dumpValue(f());");
    }

    @Test
    public void bad_array_length_write_bug() {
        Options.get().enablePolyfillMDN();
        Misc.run(
                "test-resources/src/micro/bad-array-length-write-bug.js"
        );
    }

    @Test
    public void bad_array_length_write_bug_minimized() {
        Misc.run("test-resources/src/micro/bad-array-length-write-bug_minimized.js");
    }

    @Test
    public void invalidArrayLengths() {
        Options.get().enableDoNotExpectOrdinaryExit();
        Misc.runSource("var a = [];",
                "a.length = 1;", // OK for sanity
                "a.length = NaN;",
                "a.length = {};",
                "a.length = -1;",
                "a.length = 42.2;");
        Misc.checkSystemOutput();
    }

    @Test
    public void nativeLengthUpdate() {
        Misc.runSource("var a = [];",
                "TAJS_assertEquals(0, a.length);",
                "a.push(42);",
                "TAJS_assertEquals(1, a.length);");
    }

    @Test
    public void nativeLengthUpdate_bug() {
        Misc.runSource("var a = [];",
                "function push(){",
                "	a.push(42);",
                "}",
                "TAJS_assertEquals(0, a.length);",
                "push();",
                "TAJS_assertEquals(1, a.length);");
    }

    @Test
    public void nativeLengthUpdate_bug_noLazy() {
        Options.get().enableNoLazy();
        Misc.runSource("var a = [];",
                "function push(){",
                "	a.push(42);",
                "}",
                "TAJS_assertEquals(0, a.length);",
                "push();",
                "TAJS_assertEquals(1, a.length);");
    }

    @Test
    public void bad_default_array_property() {
        Misc.runSource("var o = {};",
                "o.__defineGetter__('length', function () {/* length: undefined */} );",
                "try{",
                "	Array.prototype.splice.call(o, 0, 0, 'x');",
                "}catch(e){/* squelch type errors (not relevant for this test) */}");
    }

    @Test
    public void nonNumberCharactersInImpreciseConcatenation() {
        Options.get().getSoundnessTesterOptions().setTest(false);
        Misc.runSource("var STR = TAJS_make('AnyStr');",
                "var STR_OTHER_NUM = '' + TAJS_make('AnyNumOther');",
                "var NON_NUMBER_CHARS = 'x';",
                "var NUMBER_CHARS_UINT = '9';",
                "var NUMBER_CHARS_Y = 'y';",
                "",
                "TAJS_assert(STR + NON_NUMBER_CHARS, 'isMaybeStrOtherNum', false);",
                "TAJS_assert(STR + NUMBER_CHARS_UINT, 'isMaybeStrOtherNum');",
                "TAJS_assert(STR + NUMBER_CHARS_Y, 'isMaybeStrOtherNum');",
                "",
                "TAJS_assert(NON_NUMBER_CHARS + STR, 'isMaybeStrOtherNum', false);",
                "TAJS_assert(NUMBER_CHARS_UINT + STR, 'isMaybeStrPrefix');",
                "TAJS_assert(NUMBER_CHARS_Y + STR, 'isMaybeStrPrefix');",
                "",
                "TAJS_assert(STR_OTHER_NUM + NON_NUMBER_CHARS, 'isMaybeStrOtherNum', false);",
                "TAJS_assert(STR_OTHER_NUM + NUMBER_CHARS_UINT, 'isMaybeStrOtherNum');",
                "TAJS_assert(STR_OTHER_NUM + NUMBER_CHARS_Y, 'isMaybeStrOtherNum');",
                "",
                "TAJS_assert(NON_NUMBER_CHARS + STR_OTHER_NUM, 'isMaybeStrOtherNum', false);",
                "TAJS_assert(NUMBER_CHARS_UINT + STR_OTHER_NUM, 'isMaybeStrPrefix');",
                "TAJS_assert(NUMBER_CHARS_Y + STR_OTHER_NUM, 'isMaybeStrPrefix');");
    }

    @Test
    public void consoleModel() {
        Options.get().enableConsoleModel();
        Misc.runSource("console.log()");
    }

    @Test
    public void commonAsync() {
        Options.get().enableAsyncEvents();
        Options.get().enableCommonAsyncPolyfill();
        Misc.runSource("var timeoutTriggered = false;",
                "var intervalTriggered = false;",
                "var r1 = setTimeout(function(){timeoutTriggered = true;});",
                "var r2 = setInterval(function(){intervalTriggered = true;}, 1000);",
                "var r3 = clearInterval(42);",
                "var r4 = clearTimeout(42);",
                "TAJS_asyncListen(function(){",
                "	TAJS_assert(timeoutTriggered, 'isMaybeAnyBool');",
                "	TAJS_assert(intervalTriggered, 'isMaybeAnyBool');",
                "	TAJS_assert(r1, 'isMaybeNumUInt');",
                "	TAJS_assert(r2, 'isMaybeNumUInt');",
                "	TAJS_assert(r3, 'isMaybeUndef');",
                "	TAJS_assert(r4, 'isMaybeUndef');",
                "});");
    }

    @Test
    public void staticNumberIsFinite() {
        Misc.runSource("TAJS_assertEquals(true, Number.isFinite(42));",
                "TAJS_assertEquals(false, Number.isFinite(Infinity));",
                "TAJS_assertEquals(false, Number.isFinite(NaN));",
                "TAJS_assertEquals(false, Number.isFinite('foo'));",
                "TAJS_assertEquals(false, Number.isFinite('42'));",
                "TAJS_assertEquals(false, Number.isFinite());");
    }

    @Test
    public void string_startsWith() {
        Misc.runSource("TAJS_assert('foo'.startsWith('f'));",
                "TAJS_assert('foo'.startsWith('fo'));",
                "TAJS_assert('foo'.startsWith('foo'));",
                "TAJS_assert(!'foo'.startsWith('fooo'));",
                "TAJS_assert(!'bar'.startsWith('foo'));");
    }

    @Test
    public void string_endsWith() {
        Misc.runSource("TAJS_assert('foo'.endsWith('o'));",
                "TAJS_assert('foo'.endsWith('oo'));",
                "TAJS_assert('foo'.endsWith('foo'));",
                "TAJS_assert(!'foo'.endsWith('ffoo'));",
                "TAJS_assert(!'bar'.endsWith('foo'));");
    }

    @Test
    public void testPrimitiveCall() {
        //Options.get().enableFlowgraph();
        Misc.runSource("var x = 'dyt'.toString();",
                "TAJS_assertEquals(x, 'dyt');");
    }

    @Test
    public void testToStringCall() {
        //Options.get().enableFlowgraph();
        Misc.runSource("var y = { dyt: function() { return this.baat }, baat: 42 };",
                "var z = { toString: function() { return 'dyt'; } };",
                "var x = y[z]();",
                "TAJS_assertEquals(x, 42);");
    }

    @Test
    public void testPartialCreate() {
        Options.get().getSoundnessTesterOptions().setTest(false);
        Misc.runSource("TAJS_makePartial('FUNCTION', 'dummy1');",
                "TAJS_makePartial('OBJECT', 'dummy2');",
                "TAJS_makePartial('ARRAY', 'dummy3');");
    }

    @Test
    public void testPartialIdentity() {
        Options.get().getSoundnessTesterOptions().setTest(false);
        Misc.runSource("var v1 = TAJS_makePartial('OBJECT', 'dummy1');",
                "var v2a = TAJS_makePartial('OBJECT', 'dummy2');",
                "var v2b = TAJS_makePartial('OBJECT', 'dummy2');",
                "var v2c = TAJS_makePartial('OBJECT', 'dummy2');",
                "TAJS_assert(v1 !== v2a);",
                "TAJS_assert(v1 !== v2b);",
                "TAJS_assert(v1 !== v2c);",
                "TAJS_assert(v2a !== v2c);",
                "TAJS_assert(v2b !== v2c);",
                "TAJS_assert(v2a === v2b, 'isMaybeAnyBool');");
    }

    @Test(expected = AnalysisLimitationException.AnalysisModelLimitationException.class)
    public void testPartialInvoke() {
        Misc.runSource("var f = TAJS_makePartial('FUNCTION', 'dummy');",
                "f();");
    }

    @Test
    public void escapingArguments1() {
        Misc.runSource(
                "function mkArgs() { return arguments; }",
                "var args = mkArgs();",
                "args.foo;" // discovers bottom-object -> stops propagation
        );
    }

    @Test
    public void escapingArguments2() {
        Options.get().getSoundnessTesterOptions().setTest(false);
        Misc.runSource("function mkArgs() { return arguments; }",
                "var args = mkArgs('dyt');",
                "TAJS_assertEquals(args[0], 'dyt');");
    }

    @Test
    public void toStringCallUndefined() {
        Misc.runSource("var x = Object.prototype.toString.call();",
                "TAJS_assertEquals('[object Undefined]', x);"
        );
    }

    @Test
    public void toStringCallNull() {
        Misc.runSource("var x = Object.prototype.toString.call(null);",
                "TAJS_assertEquals('[object Null]', x);");
    }

    @Test
    public void toStringApplyUndefined() {
        Misc.runSource("var x = Object.prototype.toString.apply();",
                "TAJS_assertEquals('[object Undefined]', x);");
    }

    @Test
    public void toStringApplyNull() {
        Misc.runSource("var x = Object.prototype.toString.apply(null);",
                "TAJS_assertEquals('[object Null]', x);");
    }

    @Test
    public void trimCallUndefined() {
        Misc.runSource("try { ",
                "  String.prototype.trim.call(undefined);",
                "  TAJS_assert(false);",
                "} catch (e) { ",
                "  TAJS_assert(e instanceof TypeError);",
                "}");
    }

    @Test
    public void trimCallNull() {
        Misc.runSource("try { ",
                "  String.prototype.trim.call(null);",
                "  TAJS_assert(false);",
                "} catch (e) { ",
                "  TAJS_assert(e instanceof TypeError);",
                "}");
    }

    @Test
    public void trimCallString() {
        Misc.runSource("try { ",
                "  TAJS_assertEquals(String.prototype.trim.call(' x '), 'x');",
                "} catch (e) { ",
                "  TAJS_assert(false);",
                "}");
    }

    @Test
    public void callStringThisNonStrict() {
        Misc.runSource("try { ",
                "  function f(){TAJS_assert(this instanceof String)};",
                "  f.call('foo');",
                "} catch (e) { ",
                "  TAJS_assert(false);",
                "}");
    }

    @Test
    public void hasOwnPropertyCallString() {
        Misc.runSource("try { ",
                "TAJS_assert(Object.prototype.hasOwnProperty.call('', 'length'));",
                "} catch (e) { ",
                "  TAJS_assert(false);",
                "}");
    }

    @Test
    public void whiteSpaceToNumberCoercion() {
        Misc.runSource("TAJS_assert(0 == '\\r');",
                "TAJS_assert(0 == '');",
                "TAJS_assert(0 == ' ');",
                "TAJS_assert(0 == '  ');",
                "TAJS_assert(0 == '\\n');",
                "TAJS_assert(0 == '\\r');",
                "TAJS_assert(0 == '\\r\\n');",
                "TAJS_assert(0 == ' \\r\\n ');"
        );
    }

    @Test
    public void regExpConstructionWithUndefinedOrAbsent() {
        Misc.runSource("var re;",
                "re = /undefined/",
                "TAJS_assertEquals('/undefined/', re.toString());",
                "re = new RegExp();",
                "TAJS_assertEquals('/(?:)/', re.toString());",
                "re = new RegExp(undefined);",
                "TAJS_assertEquals('/(?:)/', re.toString());",
                "re.compile()",
                "TAJS_assertEquals('/(?:)/', re.toString());",
                "re.compile(undefined)",
                "TAJS_assertEquals('/undefined/', re.toString());"
        );
    }

    @Test
    public void htmlErrorWarnings() {
        Misc.run("test-resources/src/micro/bad-html.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void polymorphicGetter() {
        Misc.runSource("function f(x) {var t = x.p; return t;}",
                "var a1 = {p: 42};",
                "var a2 = {get p() {return 'foo'}};",
                "var t1 = f(a1);",
                "var t2 = f(a2);",
                "TAJS_assertEquals(t1, 42);",
                "TAJS_assert(t2, 'isMaybeSingleNum || isMaybeSingleStr');"
        );
    }

    @Test
    public void sizzle1() {
        Misc.runSource("var done = false;",
                "var Obj = {",
                "  f: function () {},",
                "  g: function () {",
                "    done = true;",
                "    tokenize();",
                "  }",
                "};",
                "function tokenize() {",
                "  if (done) return;",
                "  var type;",
                "  for (type in Obj) {",
                "    // TAJS_dumpState();",
                "    Obj[type]();",
                "    TAJS_dumpValue(type);",
                "  }",
                "}",
                "tokenize();");
        Misc.checkSystemOutput();
    }

    @Test
    public void joinTwoPrefixStringsModifiedBug() throws Exception {
        Misc.runSource("var anyString = Math.random() ? 'a' : 'b'",
                "function f(x) {",
                "	var expected = 'str' + anyString", // STR_PREFIX[str]
                "	var actual = x + anyString", // Without bugfix: STR_PREFIX[string]
                "	TAJS_assertEquals(expected, actual)",
                "}",
                "f('string')",
                "f('str')");
    }

    @Ignore // TODO: Escaped line breaks in strings parsed incorrectly (github #432)
    @Test
    public void multilineStrings() {
        Misc.run("test-resources/src/micro/multilineStrings.js");
        Misc.checkSystemOutput();
    }

    @Ignore // TODO: fails with java.nio.file.ProviderNotFoundException: Provider "https" not found (github #438)
    @Test
    public void httpURLInHTMLFile() {
        Misc.run("test-resources/src/micro/http-url.html");
    }

    @Test
    public void objectAssignWithParameterValue() {
        Misc.runSource("function inherit(properties) {\n" +
                "    Object.assign({}, properties);\n" +
                "}\n" +
                "inherit({});");
    }

    @Test
    public void symbol1() {
        Misc.runSource(
                "var o1 = Symbol(\"gen-symbol\");",
                "var o2 = Symbol.iterator;",
                "var o3 = Symbol.unscopables;",
                "var o4 = Symbol.species;",
                "TAJS_dumpValue(o1);",
                "TAJS_dumpValue(o2);",
                "TAJS_dumpValue(o3);",
                "TAJS_dumpValue(o4);",
                "TAJS_assert(true);");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol2() {
        Misc.runSource(
                "var o1 = Symbol(\"gen-symbol\");",
                "TAJS_dumpValue(o1);",
                "var obj = {};",
                "obj[o1] = 5;",
                "TAJS_dumpValue(obj);",
                "TAJS_assertEquals(obj[o1],obj[o1]);");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol2b() {
        Misc.runSource(
                "var o1 = Symbol(\"gen-symbol\");",
                "if (Math.random()) o1 = Symbol(\"2\");",
                "TAJS_dumpValue(o1);",
                "var obj = {};",
                "obj[o1] = 5;",
                "TAJS_dumpValue(obj);",
                "TAJS_assertEquals(obj[o1],obj[o1]);");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol3() {
        Misc.runSource(
                "function mk() { return Symbol(\"gen-symbol\"); }",
                "var s1 = mk();",
                "TAJS_dumpValue(s1);",
                "var s2 = mk();",
                "TAJS_dumpValue(s2);",
                "TAJS_assertEquals(s1, s2, false);"
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol4() {
        Misc.runSource(
                "var o1 = {};",
                "var x = Symbol.iterator;",
                "o1[x] = 5;",
                "TAJS_assertEquals(o1[Symbol.iterator], 5);");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol4bis() {
        Misc.runSource(
                "var o1 = {};",
                "o1[Symbol.iterator] = 5;",
                "TAJS_assertEquals(o1[Symbol.iterator], 5);");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol5() {
        Misc.runSource(
                "var o1 = {};",
                "TAJS_dumpValue(typeof o1[Symbol.iterator]);",
                "TAJS_assert(typeof o1[Symbol.iterator] == 'undefined');");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol6() {
        Misc.runSource(
                "var o1 = {};",
                "o1[Symbol.iterator] = 5;",
                "TAJS_dumpObject(o1);",
                "TAJS_assertEquals(o1[Symbol.iterator], 5);",
                "TAJS_assert(typeof o1[Symbol.iterator] != 'undefined');",
                "delete o1[Symbol.iterator];",
                "TAJS_dumpValue(o1[Symbol.iterator]);",
                "TAJS_assert(typeof o1[Symbol.iterator] == 'undefined');");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol7() {
        Misc.runSource(
                "var o1 = {};",
                "o1[Symbol.iterator] = 5;",
                "o1['s'] = 6;",
                "for(var k in o1) {",
                "  TAJS_assert(k == 's');",
                "}");
        Misc.checkSystemOutput();
    }


    @Test
    public void symbol8() {
        Misc.runSource(
                "var o1 = {};",
                "o1[Symbol.iterator] = 5;",
                "var r = Symbol.iterator in o1;",
                "TAJS_dumpValue(r);");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol9() {
        Misc.runSource("var COLOR_RED    = Symbol('Red');",
                "var COLOR_ORANGE = Symbol('Orange');",
                "var COLOR_YELLOW = Symbol('Yellow');",
                "var COLOR_GREEN  = Symbol('Green');",
                "var COLOR_BLUE   = Symbol('Blue');",
                "var COLOR_VIOLET = Symbol('Violet');",
                "function getComplement(color) {",
                "  switch (color) {",
                "    case COLOR_RED:",
                "      return COLOR_GREEN;",
                "    case COLOR_ORANGE:",
                "      return COLOR_BLUE;",
                "    case COLOR_YELLOW:",
                "      return COLOR_VIOLET;",
                "    case COLOR_GREEN:",
                "      return COLOR_RED;",
                "    case COLOR_BLUE:",
                "      return COLOR_ORANGE;",
                "    case COLOR_VIOLET:",
                "      return COLOR_YELLOW;",
                "    default:",
                "      throw new Exception('Unknown color: '+color);",
                "  }",
                "}",
                "TAJS_assert(getComplement(COLOR_GREEN) == COLOR_RED);");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol10() {
        Misc.runSource(
                "var sym = Symbol('desc');",
                "try {",
                "  var str1 = 'astring' + sym; // TypeError",
                "}", "catch(e) {",
                "  TAJS_dumpValue('Here');",
                "}");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol11() {
        Misc.runSource(
                "var o1 = {};",
                "o1[Symbol.iterator] = 5;",
                "TAJS_dumpValue(typeof Object.keys(o1)[0] == 'undefined');");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol12() {
        Misc.runSource(
                "var s = Symbol();",
                "TAJS_assert(typeof s == 'symbol');");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol13() {
        Misc.runSource(
                "var sym = Symbol('desc');",
                "TAJS_dumpValue(sym);",
                "TAJS_dumpValue(typeof sym);",
                "TAJS_dumpValue(String(sym));",
                "TAJS_assert(typeof String(sym) == 'string');");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol14() {
        Misc.runSource(
                "var p = Object.getPrototypeOf(Symbol('66'));",
                "TAJS_dumpValue(p);",
                "var c = p.constructor;",
                "TAJS_dumpValue(c);",
                "var s = Symbol;",
                "TAJS_dumpValue(s);",
                "TAJS_assert(c == s)");
        Misc.checkSystemOutput();
    }

    @Test
    public void symbol15() {
        Misc.runSource(
                "function mkSym() {",
                "  return Symbol(\"test\");",
                "}",
                "var o1 = mkSym();",
                "var obj = {};",
                "obj[o1] = 5;",
                "var o2 = mkSym();",
                "obj[o2] = \"foo\";",
                "var t = obj[o1];",
                "TAJS_dumpValue(t);",
                "var t2 = obj[o2];",
                "TAJS_dumpValue(t2);",
                "TAJS_assertEquals(obj[o1],obj[o1]);",
                "TAJS_assertEquals(obj[o2],obj[o2]);");
        Misc.checkSystemOutput();
    }

    @Test
    public void defineSymbol() {
        Misc.runSource(
                "TAJS_dumpValue(Symbol.species);",
                "var o = {}",
                "var p = Symbol.species",
                "Object.defineProperty(o, p, {",
                "    configurable: true,",
                "    enumerable: false,",
                "    value: 4",
                "});",
                "TAJS_dumpValue(o[p]);"
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void testRemovedStrOtherIdentifierPartsImpliesStrIdentifierInvariant() {
        Misc.runSource(
                "var x = TAJS_join('38x','27x');",
                "TAJS_assert(x, 'isMaybeStrIdentifier', false);"
        );
    }

    @Test
    public void testComparisonWithZeroAndUIntPos() {
        Misc.runSource(
                "var x = 0;",
                "var y = TAJS_join(1, 2);",
                "TAJS_dumpValue(y);",
                "TAJS_assert(y > x);",
                "TAJS_assert(x < y);"
        );
    }

    @Test
    public void testMissingProperty() {
        Misc.runSource(
                "function doBind() {" +
                        "    return function wrap() {};" +
                        "};" +
                        "function extend(axios, b) {" +
                        "    for (var key in b) {" +
                        "        axios[\"request\"] = doBind();" +
                        "    }" +
                        "}" +
                        "var axios = doBind();" +
                        "extend(axios, {foo: 1});" +
                        "TAJS_assertEquals(\"undefined\", typeof axios.request, false)"
        );
    }

    @Test
    public void strictUndefined() {
        Options.get().getSoundnessTesterOptions().setTest(false); // FIXME: needed because of #473
        Misc.runSource(
                        "(function () {",
                        "'use strict';",
                        "TAJS_assertEquals(undefined, this);",
                        "function f() { TAJS_assertEquals(undefined, this) }",
                        "f();",
                        "})();");
    }

    @Test
    public void maybeUndefAsFirstArgumentToAddition_unsound() {
        Options.get().getUnsoundness().setIgnoreUnlikelyUndefinedAsFirstArgumentToAddition(true);
        Misc.runSource("",
                "var x = TAJS_join('foo', undefined);",
                "var y = 'bar';",
                "var z = x + y;",
                "TAJS_assertEquals('foobar', z);");
    }

    @Test
    public void testFixedRandom1() {
        Misc.runSource(
                "TAJS_dumpValue('foo' + (new Date()).getTime());",
                "TAJS_dumpValue('foo' + -(new Date()));",
                "TAJS_dumpValue('foo' + Math.random());",
                "TAJS_dumpValue('foo' + (Math.random() + '').replace('.', ''));",
                "TAJS_dumpValue('foo' + ('1.10.0' + Math.random() ).replace(/\\D/g, ''));");
        Misc.checkSystemOutput();
    }

    @Test
    public void testFixedRandom2() {
        Options.get().getUnsoundness().setUseFixedRandom(true);
        Options.get().getUnsoundness().setShowUnsoundnessUsage(true);
        Options.get().getSoundnessTesterOptions().setTest(false);
        Misc.runSource(
                "TAJS_dumpValue('foo' + (new Date()).getTime());",
                "TAJS_dumpValue('foo' + -(new Date()));",
                "TAJS_dumpValue('foo' + Math.random());",
                "TAJS_dumpValue('foo' + (Math.random() + '').replace('.', ''));",
                "TAJS_dumpValue('foo' + ('1.10.0' + Math.random() ).replace(/\\D/g, ''));");
        Misc.checkSystemOutput();
    }

    @Test
    public void finerpropertyretrival1() throws Exception {
        Options.get().getSoundnessTesterOptions().setTest(false);
        Misc.runSource("" +
                        "var POLLUTION_MARKER = {};",
                "var P = TAJS_makeExcludedStrings('protop');",
                "var opr = {protop: 8};",
                "var o = {inheritorp: 9};",
                "o.__proto__ = opr;",
                "o[P] = POLLUTION_MARKER;",
                "var o2 = {};",
                "for(var x  in o) {",
                "  o2[x] = o[x];", // protop should not appear explicitly as x
                "}",
                "TAJS_assert(o['protop'] !== POLLUTION_MARKER);",
                "TAJS_dumpValue(o['inheritorp']);",
                "");
    }

    @Test
    public void widen1() {
        Misc.runSource(
                "var x = 'foo'",
                "while (x.length < 10) {",
                "  TAJS_dumpValue(x);",
                "  x = x + 'bar';",
                "}",
                "TAJS_dumpValue(x);");
        Misc.checkSystemOutput();
    }

    @Test
    public void widen2() {
        Misc.runSource(
                "function f(a) {",
                        "  TAJS_dumpValue(a);",
                        "  if (a.length < 10)",
                        "    return f(a + 'bar');",
                        "  return a;",
                        "}",
                        "TAJS_dumpValue(f('foo'));");
        Misc.checkSystemOutput();
    }

    @Test
    public void recencySummarizationSoundnessBug() {
        Options.get().enableDeterminacy();
        Options.get().enableNoLazy();
        Misc.runSource(
                "function create(prototype) {",
                        "  return Object.create(prototype);",
                        "}",
                        "var y = create({bar: 2});",
                        "var created = create(y);",
                        "created.bar");

    }

    @Test
    public void recencySummarizationSoundnessBugWithoutRecency() {
        Options.get().enableDeterminacy();
        Options.get().enableNoLazy();
        Options.get().enableNoRecency();
        Misc.runSource(
                "function create(prototype) {",
                "  return Object.create(prototype);",
                "}",
                "var y = create({bar: 2});",
                "var created = create(y);",
                "created.bar");

    }

    @Test
    public void assumes() throws Exception {
        Misc.run("test-resources/src/micro/assumes.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void toStringAssumes() throws Exception {
        Misc.run("test-resources/src/micro/toStringAssumes.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void assumeHasPropertyWhereSoundnessBug() throws Exception {
        Misc.run("test-resources/src/micro/assumeHasPropertyWhereSoundnessBug.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void testDelayReturnFlowUntilDischargedSoundnessBug() {
        Misc.runSource(
                "var hasBeenCalled = false;",
                "",
                "var debouncedIncr = function() {",
                "  var x = hasBeenCalled;",
                "  if (!hasBeenCalled) {",
                "    hasBeenCalled = true;",
                "    var result = (function(){ debouncedIncr();}());",
                "  }",
                "};",
                "",
                "debouncedIncr();");
    }

    @Test
    public void domExceptionObjectToStringSoundnessBug() {
        Options.get().enableIncludeDom();
        Misc.runSource("try {",
                "  document.querySelectorAll('<_<');",
                "} catch (e) {",
                "  TAJS_assertEquals('[object DOMException]', Object.prototype.toString.call(e));",
                "}"
        );
    }

    @Test
    public void getOwnPropertySymbolsSoundnessBug() {
        Misc.runSource("var length = Object.getOwnPropertySymbols({a:1, b:2, c:3}).length;",
                "TAJS_assertEquals(0, length);");
    }

    @Test
    public void arrayPushStrongUpdateSoundnessBug() {
        Misc.runSource("function createArr() { return [] }",
                "var summ1 = createArr();",
                "var summ2 = createArr();",
                "var singleton = createArr();",
                "summ1.push(1)",
                "TAJS_assert(0 === summ2.length, 'isMaybeTrue')");
    }

    @Test
    public void testMustEqualsBaseCoercion() {
        Misc.runSource("var x = 'foo';",
                "(x[2]) ? typeof x === 'object' && x : false");
    }

    @Test
    public void testShorthandPropertyNames() {
        Misc.runSource("var a = 2;",
                "var b = {a};",
                "TAJS_assertEquals(b.a, 2);");
    }

    @Test
    public void testArrayConcatSoundnessBug() {
        Misc.runSource("var a = [1];",
                "var b = [2, 3];",
                "var c = Math.random() ? a : b;",
                "var y = [4].concat(c)",
                "var l = y.length");
    }

    @Test
    public void testMustEquals() {
        Misc.runSource("var x = TAJS_make('AnyStr');",
                "var y = TAJS_make('AnyStr');",
                "if (x === 'foo' && y === 'bar') {}",
                "if (x === 'foo') {TAJS_assert(true)}");
    }
}
