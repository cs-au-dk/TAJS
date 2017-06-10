package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import dk.brics.tajs.util.ParseError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

@SuppressWarnings("static-method")
public class TestMicro {

	//public static void main(String[] args) {
	//org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestMicro");
	//}

	@Before
	public void init() {
		Main.initLogging();
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
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test00.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_01() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test01.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_02() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test02.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_03() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test03.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_04() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test04.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_05() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test05.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_06() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test06.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_07() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test07.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_08() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test08.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_09() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test09.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_10() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test10.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_11() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test11.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_12() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test12.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_13() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test13.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_14() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test14.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_15() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test15.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_16() throws Exception {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test16.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_17() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test17.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_18() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test18.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_19() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test19.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_20() throws Exception {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test20.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_21() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test21.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_22() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test22.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_23() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test23.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_24() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test24.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_25() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test25.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_26() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test26.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_27() throws Exception {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test27.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_28() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test28.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_29() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test29.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_30() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test30.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_31() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test31.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_32() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test32.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_33() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test33.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_34() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test34.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_35() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test35.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_36() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test36.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_37() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test37.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_38() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test38.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_39() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test39.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_40() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test40.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_41() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test41.js", "test/micro/test41b.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_42() throws Exception {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test42.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_43() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test43.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_44() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test44.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_45() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test45.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_46() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test46.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_47() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test47.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_48() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test48.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_49() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test49.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_50() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test50.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_51() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test51.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_52() throws Exception {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test52.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_53() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test53.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_54() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test54.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_55() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test55.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_56() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test56.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_57() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test57.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_58() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test58.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_59() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test59.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_60() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test60.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_61() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test61.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_62() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test62.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_63() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test63.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_64() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test64.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_65() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test65.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_66() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test66.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_67() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test67.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_68() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test68.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_69() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test69.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_70() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test70.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_71() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test71.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_72() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test72.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_73() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test73.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

    @Test
    public void micro_73simple() throws Exception {
        Options.get().enableFlowgraph();
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/micro/test73simple.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

	@Test
	public void micro_74() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test74.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_75() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test75.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_76() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test76.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_77() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test77.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_78() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test78.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_79() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test79.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_80() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test80.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_81() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test81.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_82() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test82.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_83() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test83.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_84() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test84.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_85() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test85.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_86() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test86.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_87() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test87.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_88() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test88.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_89() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test89.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_90() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test90.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_91() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test91.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_92() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test92.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_93() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test93.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_94() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test94.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_95() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test95.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_96() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test96.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_97() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test97.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_98() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test98.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_99() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test99.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_100() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test100.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test(expected = AssertionError.class /* TODO: GitHub #36 */)
	public void micro_101() throws Exception {
		Misc.init();
		String[] args = {"test/micro/test101.js"};
		Misc.run(args);
	}

	@Test
	public void micro_102() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test102.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_103() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test103.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_104() throws Exception {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test104.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_105() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test105.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_106() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test106.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_107() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test107.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_108() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test108.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_109() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test109.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_110() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test110.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_111() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test111.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_112() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test112.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_113() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test113.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test(expected = ParseError.class)
	public void micro_114() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test114.js"}; // Should be runtime error, but Sec 16 p.149 allows us to report the error early
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_115() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test115.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_116() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test116.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_117() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test117.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_118() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test118.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_119() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test119.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_120() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test120.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_121() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test121.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_122() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test122.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_123() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test123.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test(expected = AssertionError.class /* TODO: GitHub #191 */)
	public void micro_125() throws Exception {
		Misc.init();
		String[] args = {"test/micro/test125.js"};
		Misc.run(args);
	}

	@Test
	public void micro_125a() throws Exception {
		Misc.init();
		String[] args = {"test/micro/test125a.js"};
		Misc.run(args);
	}

	@Test
	public void micro_125b() throws Exception {
		Misc.init();
		String[] args = {"test/micro/test125b.js"};
		Misc.run(args);
	}

	@Test
	public void micro_125c () throws Exception {
		Misc.init();
		String[] args = {"test/micro/test125c.js"};
		Misc.run(args);
	}

	@Test(expected = AssertionError.class /* TODO: GitHub #191 */)
	public void micro_125d () throws Exception {
		Misc.init();
		String[] args = {"test/micro/test125d.js"};
		Misc.run(args);
	}

	@Test
	public void micro_126() throws Exception {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test126.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_127() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test127.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test(expected = AssertionError.class /* TODO: GitHub #191 */)
	public void micro_128() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test128.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_129() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test129.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_130() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test130.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_131() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test131.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_132() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test132.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_133() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test133.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_134() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test134.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_135() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test135.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_136() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test136.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_137() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test137.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_138() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test138.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_139() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test139.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test(expected = ParseError.class)
	public void micro_140a() throws Exception {
		Misc.init();
		String[] args = {"test/micro/test140.js"};
		Misc.run(args);
	}

	@Test(expected = ParseError.class)
	public void micro_140b() throws Exception {
		Misc.init();
		Options.get().enableIncludeDom();
		String[] args = {"test/micro/test140.js"};
		Misc.run(args);
	}

	@Test
	public void micro_141() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test141.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_142() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test142.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test(expected = AssertionError.class /* TODO: GitHub #3 */)
	public void micro_143() throws Exception {
		fail("Add support for getters/setters");
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test143.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test(expected = AssertionError.class /* GitHub #3 */)
	public void micro_144() throws Exception {
		fail("Add support for getters/setters");
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test144.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_145() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test145.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_146() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test146.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_147() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test147.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_148() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test148.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_148b() throws Exception {
		Options.get().enableNoModified(); // maybe-modified can make a difference, even with lazy prop
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test148.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_149() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test149.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_150() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test150.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_151() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test151.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_152() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test152.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_153() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test153.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_154() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test154.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_155() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test155.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_156() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test156.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_157() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test157.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_158() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test158.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_159() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test159.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_160() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test160.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_161() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test161.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_162() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test162.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_163() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test163.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_164() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test164.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_165() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test165.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_166() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test166.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_167() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test167.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_168() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test168.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_169() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test169.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_170() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test170.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_171() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test171.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_172() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test172.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_173() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test173.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_174() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test174.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_175() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test175.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	@Test
	public void micro_176() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test176.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_177() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test177.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_178() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test178.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_179() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test179.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_180() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test180.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_181() throws Exception {
		// Nashorn performs the same hoisting as TAJS // TODO: which hoisting is correct?
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test181.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_182() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test182.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_183() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test183.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_184() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test184.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test(expected = AssertionError.class /* TODO: GitHub #243 */)
	public void micro_185() throws Exception {
		Misc.init();
		String[] args = {"test/micro/test185.js"};
		Misc.run(args);
	}

	@Test(expected = AssertionError.class /* TODO: GitHub #244 */)
	public void micro_186() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test186.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_187() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test187.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_188() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test188.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_189() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test189.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_190() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test190.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_191() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test191.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_192() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test192.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_200() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test200.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_201() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test201.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_202() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test202.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_203() throws Exception {
		Misc.init();
		Options.get().enableLoopUnrolling(100);
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test203.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_204() throws Exception {
		Misc.init();
		Options.get().enableLoopUnrolling(100);
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test204.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_205() throws Exception {
		Misc.init();
		String[] args = {"test/micro/test205.js"};
		Misc.run(args);
	}

	@Test
	public void micro_206() throws Exception {
		Misc.init();
		Options.get().enableLoopUnrolling(100);
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test206.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_207() throws Exception {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test207.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_208() throws Exception {
		Misc.init();
		Options.get().enableNoPolymorphic();
		Options.get().enableNoChargedCalls();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test208.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_209() throws Exception {
		Misc.init();
		Options.get().enableNoPolymorphic();
		Options.get().enableNoChargedCalls();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test209.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_210() throws Exception {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test210.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_210b() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test210b.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testArray() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testArray.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testBoolean() throws Exception {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testBoolean.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testEval() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.get().enableUnevalizer();
		Options.get().enableDoNotExpectOrdinaryExit();
		String[] args = {"test/micro/testEval.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testFunction() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.get().enableUnevalizer();
		String[] args = {"test/micro/testFunction.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testFunctionApply() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testFunctionApply.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testFunctionCall() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testFunctionCall.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testNumber() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testNumber.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testObject() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testObject.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testOO() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testOO.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testRegExp() throws Exception {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testRegExp.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testString() throws Exception {
		Misc.init();
		Options.get().getUnsoundness().setIgnoreLocale(true);
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testString.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testToPrimitive() throws Exception {
		Misc.init();
		String[] args = {"test/micro/testToPrimitive.js"};
		Misc.run(args);
	}

	@Test
	public void micro_testPaper() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testPaper.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testForIn1() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testForIn1.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testForIn2() throws Exception {
		Misc.init();
        Misc.captureSystemOutput();
		String[] args = {"test/micro/testForIn2.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

    @Test
    public void micro_testForIn2simple() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/micro/testForIn2simple.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

	@Test
	public void micro_testForIn3() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "test/micro/testForIn3.js" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testForIn4() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "test/micro/testForIn4.js" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testForIn5() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "test/micro/testForIn5.js" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testForIn6() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "test/micro/testForIn6.js" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testForIn7() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "test/micro/testForIn7.js" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

    @Test
    public void micro_testForIn8() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/micro/testForIn8.js" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

	@Test
	public void micro_testForInHasOwn() throws Exception {
		Misc.init();
		String[] args = { "test/micro/testForInHasOwn.js" };
		Misc.run(args);
	}

    @Test
    public void micro_testForInException() throws Exception {
        Misc.init();
        Options.get().enableFlowgraph();
        String[] args = { "test/micro/testForInException.js" };
        Misc.run(args);
    }

	@Test
	public void micro_testForInPrototypeProperties() throws Exception {
		Misc.init();
		String[] args = { "test/micro/testForInPrototypeProperties.js" };
		Misc.run(args);
	}

	@Test
	public void micro_testForInNoEnumerablePrototypeProperties() throws Exception {
		Misc.init();
		String[] args = { "test/micro/testForInNoEnumerablePrototypeProperties.js" };
		Misc.run(args);
	}

    @Test
	public void micro_testForInEach() throws Exception {
		Misc.init();
		String[] args = {"test/micro/testForInEach.js"};
		Misc.run(args);
	}

	@Test
	public void micro_testCall1() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testCall1.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testCall2() throws Exception {
        Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testCall2.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testDeadAssignmentsAndDPAs() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testDeadAssignmentsAndDPAs.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testDeadAssignmentsAndNatives() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testDeadAssignmentsAndNatives.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testNumAdd() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testNumAdd.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testNumSub() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testNumSub.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testUintAdd() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testUintAdd.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testUintSub() throws Exception {
		// FIXME: unsound, see reference to this test in Operators.java
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testUintSub.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void micro_testConreteStrings() throws Exception {
		Misc.init();
		Misc.runSource("TAJS_assert(/x/.test('x'));",
				"TAJS_assert(/-\\[/.test('-['));",
				"TAJS_assert(/\\[-/.test('[-'));"
		);
	}

	@Test
	public void micro_testReplaceUnclosedCharacterClass() throws Exception {
		Misc.init();
		Misc.runSource("TAJS_assert('['.replace('[', 'x') === 'x');"
		);
	}

	@Test
	public void micro_escapedCharacters() throws Exception {
		Misc.init();
		String[] args = {"test/micro/escapedCharacters.js"};
		Misc.run(args);
	}

	@Test
	public void micro_testFunctionConstructor() throws Exception {
		Misc.init();
		Options.get().enableUnevalizer();
		Misc.runSource("TAJS_assert((typeof new Function('')) === 'function');",
				"TAJS_assert(new Function('')() === undefined);",
				"TAJS_assert((typeof new Function('', '')) === 'function');",
				"TAJS_assert(new Function('', '')() === undefined);"
		);
	}

	@Test
	public void micro_testEmptyFunctionConstructor() throws Exception {
		Misc.init();
		Options.get().enableUnevalizer();
		Misc.runSource("TAJS_assert((typeof new Function()) === 'function');",
				"TAJS_assert(new Function()() === undefined);"
		);
	}

	@Test
	public void micro_testConcreteDecodeURI() throws Exception {
		Misc.init();
		Misc.runSource("TAJS_assert(decodeURI() === 'undefined');",
				"TAJS_assert(decodeURI('http://test.com/%20') === 'http://test.com/ ');"
		);
	}

	@Test
	public void micro_testConcreteDecodeURIComponent() throws Exception {
		Misc.init();
		Misc.runSource("TAJS_assert(decodeURIComponent() === 'undefined');",
				"TAJS_assert(decodeURIComponent('%20') === ' ');"
		);
	}

	@Test
	public void micro_testConcreteEncodeURI() throws Exception {
		Misc.init();
		Misc.runSource("TAJS_assert(encodeURI() === 'undefined');",
				"TAJS_assert(encodeURI('http://test.com/ ') === 'http://test.com/%20');"
		);
	}

	@Test
	public void micro_testConcreteEncodeURIComponent() throws Exception {
		Misc.init();
		Misc.runSource("TAJS_assert(encodeURIComponent() === 'undefined');",
				"TAJS_assert(encodeURIComponent(' ') === '%20');"
		);
	}

	@Test
	public void micro_testConcreteEscape() throws Exception {
		Misc.init();
		Misc.runSource("TAJS_assert(escape() === 'undefined');",
				"TAJS_assert(escape(' ') === '%20');"
		);
	}

	@Test
	public void micro_testConcreteUnescape() throws Exception {
		Misc.init();
		Misc.runSource("TAJS_assert(unescape() === 'undefined');",
				"TAJS_assert(unescape('%20') === ' ');"
		);
	}

	@Test
	public void micro_testConcreteParseInt() throws Exception {
		Misc.init();
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
		Misc.init();
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
		Misc.init();
		Misc.runSource("var U = !!Math.random();",
				"TAJS_assert(Object.prototype.toString.call({}) === '[object Object]');",
				"TAJS_assert(Object.prototype.toString.call([]) === '[object Array]');",
				"TAJS_assert(Object.prototype.toString.call(U? {}: {}) === '[object Object]');",
				"TAJS_assert(Object.prototype.toString.call(U? {}: []), 'isMaybeStrPrefix');"
		);
	}

	@Test
	public void micro_testAbstractGCPrecision() throws Exception {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.runSource("function f(v){return v.p;}",
				"f({p: true})",
				"if(f()){", // definite error (mainly due to abstract GC)
				"	TAJS_assert(false);",
				"}else{",
				"	TAJS_assert(false);",
				"}",
				"TAJS_assert(false);"
		);
	}

	@Test
	public void micro_testLoopBeforeCallSequence() {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testLoopBeforeCallSequence.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void arrayJoin() {
		Misc.init();
		Misc.runSource("",
				"var joined = ['foo',,'baz'].join();",
				"TAJS_assert(joined === 'foo,,baz');",
				""
		);
	}

	@Test
	public void micro_numberNaN() {
		Misc.init();
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
		Misc.init();
		Misc.runSource("",
				"TAJS_assert('111111111111111110000' === (111111111111111111111).toString());",
				"TAJS_assert('-111111111111111110000' === (-111111111111111111111).toString());",
				"TAJS_assert('0.00001' === (0.00001).toString());",
				"TAJS_assert('1e-7' === (0.0000001).toString());",
				"");
	}

	@Test
	public void numberFormatters() {
		Misc.init();
		Misc.runSource("",
				"TAJS_assert('1.1111111111111111e+21' === (1111111111111111111111).toFixed(8));",
				"TAJS_assert('-1.1236e-4' === (-0.000112356).toExponential(4));",
				"TAJS_assert('0.000555000000000000' === (0.000555).toPrecision(15));",
				"");
	}

	@Test
	public void arrayShifting_lit() {
		Misc.init();
		Misc.runSource("",
				"var a = ['foo', 'bar'];",
				"a.shift()",
				"TAJS_assert('bar' === a[0]);",
				"");
	}

	@Test
	public void arrayShifting_pushDirect() {
		Misc.init();
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
		Misc.init();
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
		Misc.init();
		Misc.runSource("",
				"Array.prototype[1] = 'bar';",
				"TAJS_assert([][1] === 'bar');",
				"TAJS_assert(['foo',,'baz'][1] === 'bar');",
				""
		);
	}

	@Test
	public void arrayProtoJoin1() {
		Misc.init();
		Misc.runSource("",
				"Array.prototype[1] = 'bar';",
				"TAJS_assert(['foo',,'baz'].join() === 'foo,bar,baz');",
				""
		);
	}

	@Test
	public void arrayProtoJoin2() {
		Misc.init();
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
		Misc.init();
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
		Misc.init();
		Misc.runSource("",
				"var a = ['foo', 'bar', 'baz']",
				"var v = a.splice(0, 1);",
				"TAJS_assert(v[0], 'isMaybeStrIdentifier');",
				"TAJS_assert(a[0], 'isMaybeStrIdentifier');",
				"");
	}

    @Test
    public void testLargeFunctionBody_1500_calls() throws Exception {
        Misc.init();
        // should not crash
        String[] args = {"test/micro/largeFunctionBody_1500_calls.js"};
        Misc.run(args);
    }

//	@Test
//	public void testLargeFunctionBody_1944_calls() throws Exception {
//		Misc.init();
//		// should not crash
//		String[] args = {"test/micro/largeFunctionBody_1944_calls.js"};
//		Misc.run(args);
//	}

//	@Test
//	public void testLargeFunctionBody_3888_calls() throws Exception {
//		Misc.init();
//		// should not crash
//		String[] args = {"test/micro/largeFunctionBody_3888_calls.js"};
//		Misc.run(args);
//	}

//    @Test
//    public void testLargeFunctionBody_10000_calls() throws Exception {
//        Misc.init();
//        // should not crash
//        String[] args = {"test/micro/largeFunctionBody_10000_calls.js"};
//        Misc.run(args);
//    }

	@Test
	public void infinityToStringOtherNum(){
		Misc.init();
		Misc.runSource("",
				"TAJS_assert((1/0).toString(), 'isMaybeStrOtherNum');",
				"");
	}

//	@Test
//	public void halfValue_toFixed(){ // fails in old JDK, e.g. 1.8.0_31
//		Misc.init();
//		Misc.runSource("",
//				// NB: Older versions of Nashorn produces 0 in both cases!
//				"TAJS_assert((-0.5).toFixed() === '-1');",
//				"TAJS_assert((0.5).toFixed() === '1');",
//				"");
//	}

	@Test
	public void concatNumUInts(){
		Misc.init();
		Misc.runSource("",
				"var uint = Math.random()? '0': '1';",
				"var sequenceOfDigits = uint + uint;",
				"TAJS_assert(sequenceOfDigits, 'isMaybeStrOtherNum');",
				"TAJS_assert(sequenceOfDigits + sequenceOfDigits, 'isMaybeStrOtherIdentifierParts');",
				"");
	}

	@Test
	public void undefinedRegisterCompoundPropertyAssignmentWithCall_orig(){
		// should not crash
		Misc.init();
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
	public void undefinedRegisterCompoundPropertyAssignmentWithCall(){
		// should not crash
		Misc.init();
		Misc.runSource("",
				"var o = {};",
				"function f(){}",
				"o[f()] += 42",
				"");
	}


	@Test
	public void redefinedArrayLiteralConstructor(){
		// should not crash
		Misc.init();
		Misc.runSource("",
				"function Array(x, y, z){};",
		        "[,,,,]",
				"");
	}

	@Test
	public void arrayJoinFuzzySeparator(){
		// should not crash
		Misc.init();
		Misc.runSource("",
				"var sep = Math.random()? 'x': 'y';",
				"[1, 2, 3].join(sep);",
				"");
	}

	@Test
	public void issue197_1(){
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/issue197_1.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void issue197_2(){
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/issue197_2.html"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void stringEscapingForRegExp(){
		Misc.init();
		Misc.runSource("var replaced = '['.replace('[', 'x');",
				"TAJS_assert(replaced === 'x');");
	}

	@Test
	public void eventListenerRegistrationVariants() {
		Misc.init();
		Options.get().enableIncludeDom();
		Misc.captureSystemOutput();
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
		Misc.init();
		Options.get().enableIncludeDom();
		Misc.captureSystemOutput();
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
		Misc.init();
		Options.get().enableIncludeDom();
		Misc.captureSystemOutput();
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
		Misc.init();
		Options.get().enableIncludeDom();
		Misc.captureSystemOutput();
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
	public void DOM_unknownTagName(){
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/unknownTagName.html"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void regexp_compile(){
		Misc.init();
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
		Misc.init();
		Misc.captureSystemOutput();
		Misc.runSource("var U = !!Math.random();",
				"if(U){",
				"	var o1 = Object.create();",
				"	TAJS_assert(false);",
				"}",
				"if(U){",
				"	var o2 = Object.create(null);",
				"	TAJS_dumpObject(o2);",
				"}",
				"if(U){",
				"	var o3 = Object.create(Array);",
				"	TAJS_dumpObject(o3);",
				"}",
				""
		);
		Misc.checkSystemOutput();
	}

	@Test
	public void unicodeCharAtTest(){
		Misc.init();
		Misc.runSource("",
				"var a = '\400'.charAt(1)",
				"TAJS_dumpValue('\400')",
				"TAJS_assert(a === '0')",
				"");
	}

	@Test(expected = AssertionError.class /* GitHub #223 */)
	public void unicodeCharAtTest_file(){
		Misc.init();
		String[] args = {"test/micro/unicodeCharAtTest.js"};
		Misc.run(args);
	}

	@Test // TODO: Regression for GitHub issue #236
	public void absentPresentRecovery_regression() {
		Misc.init();
		Main.reset();
		Options.get().enableTest();
		Options.get().enableContextSensitiveHeap();
		Options.get().enableParameterSensitivity();
		Options.get().enableLoopUnrolling(1);
		String[] args = {"test/micro/absent-present.js"};
		Misc.run(args);
	}

	@Test
	public void testToString1() {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/toString1.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testToString2() {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/toString2.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testToString3() {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/toString3.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testToString4() {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/toString4.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testToString5() {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/toString5.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void arrayLengthNaN() {
		Misc.init();
		Misc.runSource("",
				"var arr = [];",
				"arr.length = Math.random()? 42: undefined;",
				"TAJS_assert(arr.length, 'isMaybeNumUInt');",
				"");
	}

	@Test
	public void invalidArrayLengthThrows() {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.runSource("",
				"var arr = [];",
				"arr.length = undefined;", // should throw a definite exception
				"TAJS_assert(false);",
				"");
	}

	@Test
	public void arraysort1() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/arraysort1.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void arraysort2() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/arraysort2.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void typeofNative1() {
		Misc.init();
		Misc.runSource("",
				"TAJS_assert(typeof Number === 'function');",
				"");
	}

	@Test
	public void typeofNative2() {
		Misc.init();
		Misc.runSource("",
				"TAJS_assert(typeof Date === 'function');",
				"");
	}

	@Test
	public void emptyblock() {
		Misc.init();
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
		Misc.init();
		Options.get().enableUnevalizer();
		Options.get().enableIncludeDom();
		Misc.runSource("setTimeout(Function,0);");
	}

	@Test
	public void indirecteval() {
		Misc.init();
//		Options.get().enableUnevalizer();
		Options.get().enableIncludeDom();
		Misc.runSource("document.addEventListener(\"DOMContentLoaded\", eval);");
	}


	@Test
	public void thisObjectForSetTimeoutAndSetInterval() {
		Options.get().enableIncludeDom();
		Misc.init();
		Misc.runSource("setInterval(function(){TAJS_assert(window === this);}, 0);",
				"setTimeout(function(){TAJS_assert(window === this);}, 0);",
				"");
	}

	@Test
	public void constInitialization() {
		Misc.init();
		Misc.runSource("const x = 42;",
				"TAJS_assert(x === 42);",
				"");
	}

	@Test(expected = AssertionError.class /* GitHub #182*/)
	public void constReassignment() {
		Misc.init();
		Misc.runSource("const x = 42;",
				"x = 87;",
				"TAJS_assert(x === 42);",
				"");
	}

	@Test
	public void objectFreeze_return() {
		Misc.init();
		Options.get().getUnsoundness().setIgnoreMissingNativeModels(true);
		Misc.runSource(
				"var o = {};",
				"TAJS_assert(Object.freeze(o) === o);",
				"");
	}

	@Test(expected = AssertionError.class /* TODO: GitHub #249 */)
	public void objectFreeze_immutable() {
		Misc.init();
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

	@Test(expected = AssertionError.class /* GitHub #249 */)
	public void objectFreeze_strict() {
		Misc.init();
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
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.runSource("Object.keys(undefined);",
				"TAJS_assert(false);"
		);
	}

	@Test
	public void objectKeys_sound() {
		Misc.init();
		Misc.runSource("var o0 = {};",
				"TAJS_assert(Object.keys(o0).length === 0);",

				"var o1 = {p: 42};",
				"TAJS_assert(Object.keys(o1).length === 1);",
				"TAJS_assert(Object.keys(o1)[0] === 'p');",

				"var o2 = {p: 42, q: 87};",
				"TAJS_assert(Object.keys(o2).length === 2);",
				"TAJS_assert(Object.keys(o2)[0], 'isStrIdentifier');",
				"TAJS_assert(Object.keys(o2)[1], 'isStrIdentifier');",

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
		Misc.init();
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
				"TAJS_assert(Object.keys(a0)[0], 'isMaybeStrSomeNonUInt', false);",

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
		Misc.init();
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
		Misc.init();
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
		Misc.init();
		Misc.runSource("TAJS_assert(toString.toString(), 'isMaybeAnyStr');",
				"TAJS_assert((function f(){}).toString(), 'isMaybeAnyStr');"
		);
	}

	@Test
	public void functionToString_prototypejsExample() {
		Options.get().getUnsoundness().setUsePreciseFunctionToString(true);
		Misc.init();
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
		Misc.init();
		Options.get().enableIncludeDom();
		Misc.runSource("document.createElement('div').id = 'ELEMENT';",
				"var element = document.getElementById('ELEMENT');",
				"TAJS_assert(element.addEventListener, 'isMaybeUndef', false);",
				"");
	}

	@Test
	public void testNoImplicitGlobalVarDeclarations() {
		Misc.init();
		Options.get().getUnsoundness().setNoImplicitGlobalVarDeclarations(true);
		Misc.runSource("var v1 = 42;",
				"TAJS_assert(v1 === 42);",
				"v2 = 42;",
				"TAJS_assert(typeof v2 === 'undefined');"
		);
	}

	@Test
	public void recursiveSetTimeoutWithNulls() {
		Misc.init();
		Options.get().enableIncludeDom();
		Misc.captureSystemOutput();
		Misc.runSource("function r(f){",
				"	setTimeout(f, 0);",
				"}",
				"r(function f1(){r(function f2(){r(function f3(){r(null)})})});"
		);
		Misc.checkSystemOutput();
	}
	@Test(expected = AnalysisLimitationException.class /* TODO: GitHub #257*/)
	public void classCastExceptionRegression1() {
		Misc.init();
		Options.get().enableUnevalizer();
		Options.get().enableIncludeDom();
		Misc.runSource("function f() {",
				"}",
				"window[f] = window[f];"
		);
	}

	@Test(expected = AnalysisLimitationException.class /* GitHub #257*/)
	public void classCastExceptionRegression2() {
		Misc.init();
		Options.get().enableUnevalizer();
		Options.get().enableIncludeDom();
		Misc.runSource("setTimeout(Function,0);"
		);
	}

	@Test
	public void classCastExceptionRegression3() {
		Misc.init();
		Options.get().enableIncludeDom();
		Misc.runSource("setTimeout(Array,0);"
		);
	}

	@Test
	public void missingHostFunctionModelOnObject() {
		// check that all static functions on Object have a transfer function // TODO generalize this test?
		Misc.init();
		Misc.runSource("var s = Math.random()? 'foo': 'bar';",
				"Object[s]();"
		);
	}

	@Test // (expected = AnalysisLimitationException.class /* should fix itself once we do some more modeling */)
	public void missingHostFunctionModelOnFunction() {
		Misc.init();
		Options.get().enableUnevalizer();
		Misc.runSource("var s = Math.random()? 'foo': 'bar';",
				"Function[s]();"
		);
	}

	@Test
	public void missingHostFunctionModelOnString() {
		Misc.init();
		Misc.runSource("var s = Math.random()? 'foo': 'bar';",
				"TAJS_dumpValue(String('x')[s]);"
		);
	}

	@Test
	public void missingHostFunctionModelOnNumber() {
		Misc.init();
		Misc.runSource("var s = Math.random()? 'foo': 'bar';",
				"Number(0)[s]();"
		);
	}

	@Test
	public void missingHostFunctionModelOnBoolean() {
		Misc.init();
		Misc.runSource("var s = Math.random()? 'foo': 'bar';",
				"Boolean(true)[s]();"
		);
	}

	@Test
	public void missingHostFunctionModelOnRegExp() {
		Misc.init();
		Misc.runSource("var s = Math.random()? 'foo': 'bar';",
				"RegExp('')[s]();"
		);
	}

	@Test
	public void indirectAccessToFunctionConstructor() {
		Misc.init();
		Misc.runSource("TAJS_assert(Object.constructor === Function);",
				"TAJS_assert(Object.prototype.constructor.constructor === Function);",
				"TAJS_assert(({}).constructor.constructor === Function);"
		);
	}

	@Test
	public void lazyEventHandler1() {
		Misc.init();
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
		Misc.init();
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
		Misc.init();
		Options.get().enableAsyncEvents();
		Misc.captureSystemOutput();
		Misc.runSource("function f(){TAJS_dumpValue('executed');}",
				"TAJS_asyncListen(f);",
				"");
		Misc.checkSystemOutput();
	}

	@Test
	public void asyncEventsWeak() {
		Misc.init();
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
		Misc.init();
		Options.get().enableAsyncEvents();
		Misc.runSource("var x = false;",
				"function f(){x = true;}",
				"TAJS_asyncListen(f);",
				"TAJS_assert(x === false);",
				"");
	}

	@Test
	public void asyncEventsMultiple() {
		Misc.init();
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
		Misc.init();
		Options.get().enableAsyncEvents();
		Misc.runSource("function f(){foo.bar()}",
						"TAJS_asyncListen(f);");
	}

	@Test
	public void asyncEventsRecency() {
		Misc.init();
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
		Misc.init();
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
		Misc.init();
		Misc.runSource("var a = [];",
						"a.join = undefined;",
						"TAJS_assert(a.toString() === '[object Array]');");
	}

	@Test(expected = AnalysisLimitationException.class)
	public void arrayToStringWithRedefinedJoin() {
		Misc.init();
		Misc.runSource("var a = [];",
						"a.join = function(){};",
						"a.toString();");
	}

	@Test
	public void cyclicArrayJoin() {
		Misc.init();
		Misc.runSource("var a = [];",
						"a[0] = a;",
						"TAJS_assert(a.join(), 'isMaybeAnyStr');");
	}

	@Test
	public void localeString_sound() {
		Misc.init();
		Misc.runSource("TAJS_assert(1..toLocaleString(), 'isMaybeAnyStr');");
	}

	@Test
	public void localeString_unsound() {
		Misc.init();
		Options.get().getUnsoundness().setIgnoreLocale(true);
		Misc.runSource("TAJS_assert(1..toLocaleString() === '1');");
	}

    @Test
    public void functionName() {
        Misc.init();
        Misc.runSource("TAJS_assert((function f(){}).name === 'f');");
    }

	@Test
	public void maybeCoercionError() {
		Misc.init();
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
		Misc.init();
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
		Misc.init();
		Misc.runSource("TAJS_assert(-(Math.random()? 2: 3), 'isMaybeNumUInt', false)");
	}

	@Test
	public void stringIndexing() {
		Misc.init();
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
		Misc.init();
		Main.reset();
		Options.get().enableTest();
		Options.get().enableDoNotExpectOrdinaryExit();
		String[] args = {"test/micro/present-no-value-bug.js"};
		Misc.run(args);
	}

	@Test
	public void presentNoValueNoBug() throws Exception {
		Misc.init();
		Main.reset();
		Options.get().enableTest();
		String[] args = {"test/micro/present-no-value-no-bug.js"};
		Misc.run(args);
	}

	@Test
	public void presentNoValueNoBug2() throws Exception {
		Misc.init();
		Main.reset();
		Options.get().enableTest();
		Options.get().enableDoNotExpectOrdinaryExit();
		String[] args = {"test/micro/present-no-value-no-bug-2.js"};
		Misc.run(args);
	}

	@Test
	public void unexpectedValueBug() throws Exception {
		Misc.init();
		Main.reset();
		Options.get().enableTest();
		Options.get().enableNoRecency();
		Options.get().enableDoNotExpectOrdinaryExit();
		String[] args = {"test/micro/unexpectedValueBug.js"};
		Misc.run(args);
	}

	@Test
	public void summarizationBug274() throws Exception {
		Misc.init();
		Main.reset();
		Options.get().enableTest();
		Misc.run("test/micro/summarization-bug-274-minimal.js");
	}

	@Test
	public void stringPropertyAccess() {
		Misc.init();
		Options.get().enableNoRecency();
		Misc.runSource("var s1 = 'foo';",
						"TAJS_assert(s1.search, 'isMaybeObject');",
						"var U = !!Math.random();",
						"var s2 = U? '%W': U? 42: U? 4.2: undefined;",
						"TAJS_assert(s2.search, 'isMaybeObject')");
	}

	@Test
	public void stringPropertyAccess2() {
		Misc.init();
		Misc.runSource("TAJS_assert('foo'.search, 'isMaybeObject');");
	}

	@Test
	public void stringPropertyAccess3() {
		Misc.init();
		Options.get().enableNoRecency();
		Misc.runSource("TAJS_assert('foo'.search, 'isMaybeObject');");
	}

	@Test
	public void objectPropertyAccess() {
		Misc.init();
		Options.get().enableNoRecency();
		Misc.runSource("TAJS_assert(({}).toString, 'isMaybeObject');");
	}

	@Test
	public void primitiveProperties() {
		Misc.init();
		Options.get().enableNoRecency();
		Misc.runSource("TAJS_assert(''.toString, 'isMaybeObject');",
						"TAJS_assert(''.search, 'isMaybeObject');",
						"TAJS_assert(42..toString, 'isMaybeObject');",
						"TAJS_assert(42..toFixed, 'isMaybeObject');",
						"TAJS_assert(true.toString, 'isMaybeObject');");
	}

	@Test
	public void testGlobalObjectLabelResetting() throws Exception {
		Misc.init();
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
		Misc.init();
		Main.reset();
		Options.get().enableTest();
		Options.get().enablePolyfillMDN();
		Misc.run("test/micro/function_bind_creatingBoundFunction.js");
	}

	@Test
	public void testFunction_bind_partiallyAppliedFunctions() {
		Misc.init();
		Main.reset();
		Options.get().enableTest();
		Options.get().enablePolyfillMDN();
		Misc.run("test/micro/function_bind_partiallyAppliedFunctions.js");
	}

	@Test(expected = AssertionError.class /* TODO: github #281 */)
	public void testFunction_bind_boundFunctionsUsedAsConstructors() {
		Misc.init();
		Options.get().enableTest();
		Options.get().enablePolyfillMDN();
		Misc.run("test/micro/function_bind_boundFunctionsUsedAsConstructors.js");
	}

    @Test
    public void substring() {
        Misc.init();
        Misc.runSource("'foo'.substring();");
    }

    @Test
    public void stringMethod_noLazy() {
        Misc.init();
		Options.get().enableNoLazy();
        Misc.runSource("'foo'.substring(2);");
    }

	@Test
	public void numberMethod_noLazy() {
		Misc.init();
		Options.get().enableNoLazy();
		Misc.runSource("42.0.toFixed()");
	}

	@Test
	public void coercionWarnings() {
		Misc.init();
		Misc.captureSystemOutput();
		Misc.runSource("'foo'.charAt();",
						"'foo'.charAt(undefined);",
						"String.fromCharCode();", // does not cause a warning, since argument is optional (i.e. the default undefined is not coerced to NaN)
						"String.fromCharCode(undefined);");
		Misc.checkSystemOutput();
	}

	@Test
	public void __proto__soundness_check() {
		Misc.init();
		Misc.captureSystemOutput();
		Misc.runSource("var x = {n : 5}",
				"var y = {__proto__ : x}",
				"if(y.n == 5) {} else { TAJS_assert(false); }");
	}

	@Test(expected = AssertionError.class)
	public void __proto__soundness_check_error() {
		Misc.init();
		Misc.captureSystemOutput();
		Misc.runSource("var x = {n : 5}",
				"var y = {__proto__ : x}",
				"if(y.n == 5) {TAJS_assert(false);} else {  }");
	}

	@Test
	public void test_bug_9_8_2016_a() throws Exception {
		Misc.init();
		Main.reset();
		Options.get().enableTest();
		String file = "test/micro/bug-9-8-2016-a.js";
		Misc.run(file);
	}

	@Test
	public void test_bug_9_8_2016_b() throws Exception {
		Misc.init();
		Main.reset();
		Options.get().enableTest();
		String file = "test/micro/bug-9-8-2016-b.js";
		Misc.run(file);
	}

	@Test
	public void postfixCoercionResult() {
		Misc.init();
		Misc.runSource("var a = '';",
				"TAJS_assert(a++ === 0);",
				"TAJS_assert(a === 1);");
	}

	@Test
	public void prefixCoercionResult() {
		Misc.init();
		Misc.runSource("var a = '';",
				"TAJS_assert(++a === 1);",
				"TAJS_assert(a === 1);");
	}

	@Test
	public void prefixPropertyResult() {
		Misc.init();
		Misc.runSource("var o = {p: 0};",
				"TAJS_assert(++o.p === 1);",
				"TAJS_assert(o.p === 1);");
	}

	@Test
	public void negation() throws Exception {
		Misc.init();
		Misc.runSource("var UINT = Math.random()? 1: 2;",
						"var OTHER = Math.random()? 0.1: 0.2;",
						"TAJS_assert(-UINT, 'isMaybeNumUInt', false);",
						"TAJS_assert(-UINT, 'isMaybeNumOther', true);",
						"TAJS_assert(-OTHER, 'isMaybeNumUInt', true);",
						"TAJS_assert(-OTHER, 'isMaybeNumOther', true);");
	}

	@Test
	public void keyword() {
		Misc.init();
		Misc.runSource("var byte = 0;");
	}

	@Test
	public void onload() {
		Misc.init();
		Options.get().enableIncludeDom();
		Misc.runSource("var TRIGGERED = false;",
				"window.onload = function(){TRIGGERED = true;}",
				"window.addEventListener('load', function(){TAJS_assert(TRIGGERED, 'isMaybeAnyBool')});");
	}

	@Test
	public void eventhandler_issue224() {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/eventhandler_issue224.html"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void eventhandler_issue224_a() {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/eventhandler_issue224_a.html"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void stringContainment() {
		Misc.init();
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
		Misc.init();
		Misc.runSource("for (var i in 'foo'){",
				"	TAJS_dumpValue(i);",
				"}");
	}

	@Test
	public void le_ge_leq_geq() {
		Misc.init();
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
		Misc.init();
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
		Misc.init();
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
		Misc.init();
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
        Misc.init();
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
        Misc.init();
        Misc.runSource("var x = 0;",
                "function Funky(a, b, c) { return 7; }",
                "Number.prototype.__proto__ = Funky;",
                "TAJS_assertEquals(3, x.length);",
                "TAJS_assertEquals('Funky', Funky.name);",
                "TAJS_assertEquals('Funky', x.name);");
    }

	@Test
	public void toStringValueOfBug() {
		Misc.init();
		Misc.runSource("/a/.test({toString:function(){return {};}, valueOf:function(){return '';}});");
	}

	@Test
	public void exceptionFromNativeFunction1() {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.runSource("1.2.toFixed(10);",
				"3.4.toFixed(100);",
				"TAJS_assert(false);");
	}

	@Test
	public void exceptionFromNativeFunction2() {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.runSource("'x'.match('y');",
				"'z'.match(')');",
				"TAJS_assert(false);");
	}

	@Test
	public void exceptionFromNativeFunction3() {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.runSource("'x'.search('y');",
				"'z'.search(')');",
				"TAJS_assert(false);");
	}

	@Test
	public void plusMinus0() {
		Misc.init();
		Misc.runSource("var uint = Math.random()? 42: 87;",
				"TAJS_assertEquals(uint, uint + 0);",
				"TAJS_assertEquals(uint, uint - 0);",
				"TAJS_assertEquals(uint, 0 + uint);");
	}

    @Test
    public void dateNumber() {
        Misc.init();
        Misc.runSource("TAJS_assert(new Date() - 0, 'isMaybeNumUInt||isMaybeNumOther');",
			"TAJS_assert(+new Date(), 'isMaybeNumUInt||isMaybeNumOther');");
    }

	@Test
	public void tracifier2() { // from unit/iterate_scripts_for_in
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.runSource("var srcs",
                "(srcs = [])",
                "(srcs[\"push\"])(67)");
	}

	@Test
	public void tracifier2_desugared() { // from unit/iterate_scripts_for_in
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.runSource("var srcs",
				"(srcs = [])(srcs.push)(67)");
	}

	@Test
	public void element_onEvent_isMaybeNull() {
		Misc.init();
		Options.get().enableIncludeDom();
		Misc.runSource("TAJS_assert(window.document.body.onkeyup, 'isMaybeNull');",
				"TAJS_assert(window.document.body.onload, 'isMaybeNull');",
				"TAJS_assert(window.document.body.onunload, 'isMaybeNull');");
	}

	@Test
	public void withThis() {
		Misc.init();
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
		Misc.init();
		Misc.runSource("var o = {p: 42}",
				"o.q = 'foo';",
				"TAJS_assertEquals(42, o.p);",
				"TAJS_assertEquals('foo', o.q);");
	}

	@Test
	public void stringProtoModification() {
		Misc.init();
		Misc.runSource("String.prototype.FOO = function () {",
				"	this.indexOf();",
				"};",
				"'X'.FOO();",
				"TAJS_dumpValue('Hello');");
	}

	@Test
	public void stringProtoModification_noPolymorphism() {
		Misc.init();
		Options.get().enableNoPolymorphic();
		Misc.runSource("String.prototype.FOO = function () {",
				"	this.indexOf();",
				"};",
				"'X'.FOO();",
				"TAJS_dumpValue('Hello');");
	}

	@Test
	public void stringLength() {
		Misc.init();
		Misc.runSource("TAJS_assertEquals(3, 'foo'.length);");
	}

	@Test
	public void stringCharacter() {
		Misc.init();
		Misc.runSource("TAJS_assertEquals('b', 'abc'[1]);");
	}

	@Test
	public void throwOnNewNativeNonConstructor() {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.runSource("new Number.toString()",
				"TAJS_assert(false);");
	}

	@Test
	public void noLazyRequiredForSoundnessBug() {
		Misc.init();
		Main.reset();
		Options.get().enableTest();
		Misc.run("test/micro/noLazy-required-for-soundness-bug.js");
		Main.reset();
		Options.get().enableTest();
		Options.get().enableNoLazy();
		Misc.run("test/micro/noLazy-required-for-soundness-bug.js");
	}

    @Test
    public void emptyValueWithoutLazyBug() {
		Misc.init();
        Main.reset();
        Options.get().enableTest();
        Misc.run("test/micro/empty-value-bug.js");

        Main.reset();
        Options.get().enableTest();
        Options.get().enableNoLazy();
        Misc.run("test/micro/empty-value-bug.js");
    }

	@Test
	public void argumentsMutation() {
		Misc.init();
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
        Misc.init();
        Misc.runSource(
                "function f(x, y){",
                "   TAJS_assert(x, 'isMaybeUndef', false);",
                "   TAJS_assert(y, 'isMaybeUndef', false);",
                "}",
                "[1, 2, 3, 4].sort(f);");
    }

    @Test
    public void sortShortArrays() {
        Misc.init();
        Misc.runSource(
                "function f(x, y){",
                "   TAJS_assert(false);",
                "}",
                "[].sort(f);",
                "[1].sort(f);");
    }

	@Test
	public void preciseApplyWithMixedArgumentCounts() {
		Misc.init();
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
				"var U = !!Math.random();",
				"var argumentArray123 = U? argumentArray1: U? argumentArray2: argumentArray3",
				"f.apply(this, argumentArray123);"
		);
	}

//	@Test
//	public void preciseApplyWithUnknownArgumentCounts() {
//		Misc.init();
//		Misc.runSource(
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
//				"var UINT = U? 20: 50;",
//				"var MIXED = U? UINT: U? true: undefined",
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
		Misc.init();
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
		Misc.init();
		Options.get().enableIncludeDom();
		Misc.captureSystemOutput();
		Misc.run("test/micro/domEventHandlerSetter.js");
		Misc.checkSystemOutput();
	}

	@Test
	public void propertyAccess_bug_noLazy() {
		Misc.init();
		Options.get().enableNoLazy();
		Misc.runSource("var v1 = Object.prototype.toString;",
						"TAJS_dumpValue(v1);");
	}

	@Test
	public void domObjectPropertyReadWrites() {
		Misc.init();
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
		Misc.init();
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
		Misc.init();
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
		Misc.init();
		Options.get().enableNoLazy();
		Misc.runSource("var v1 = [].xxx;"
		);
	}

	@Test
	public void domObjectSetterReadWrites() {
		Misc.init();
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
		Misc.init();
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
		Misc.init();
		Misc.runSource("var f = function() {return 42;}",
						"if (Math.random()) { f = function g() {g();} } ",
						"TAJS_dumpValue(f());");
	}

//	@Test
//	public void bad_array_length_write_bug() {
//		Misc.init();
//		Options.get().enablePolyfillMDN();
//		Misc.run(
//				"test/micro/bad-array-length-write-bug.js"
//		});
//	}

	@Test
	public void bad_array_length_write_bug_minimized() {
		Misc.init();
		Misc.run("test/micro/bad-array-length-write-bug_minimized.js");
	}

	@Test
	public void invalidArrayLengths() {
		Misc.init();
		Options.get().enableDoNotExpectOrdinaryExit();
		Misc.captureSystemOutput();
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
		Misc.init();
		Misc.runSource("var a = [];",
						"TAJS_assertEquals(0, a.length);",
						"a.push(42);",
						"TAJS_assertEquals(1, a.length);");
	}

	@Test
	public void nativeLengthUpdate_bug() {
		Misc.init();
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
		Misc.init();
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
		Misc.init();
		Misc.runSource("var o = {};",
						"o.__defineGetter__('length', function () {/* length: undefined */} );",
						"try{",
						"	Array.prototype.splice.call(o, 0, 0, 'x');",
						"}catch(e){/* squelch type errors (not relevant for this test) */}");
	}

	@Test
	public void nonNumberCharactersInImpreciseConcatenation() {
		Misc.init();
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
		Misc.init();
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
        Misc.init();
        Misc.runSource("TAJS_assertEquals(true, Number.isFinite(42));",
                        "TAJS_assertEquals(false, Number.isFinite(Infinity));",
                        "TAJS_assertEquals(false, Number.isFinite(NaN));",
                        "TAJS_assertEquals(false, Number.isFinite('foo'));",
                        "TAJS_assertEquals(false, Number.isFinite('42'));",
                        "TAJS_assertEquals(false, Number.isFinite());");
    }

    @Test
    public void string_startsWith() {
        Misc.init();
        Misc.runSource("TAJS_assert('foo'.startsWith('f'));",
                        "TAJS_assert('foo'.startsWith('fo'));",
                        "TAJS_assert('foo'.startsWith('foo'));",
                        "TAJS_assert(!'foo'.startsWith('fooo'));",
                        "TAJS_assert(!'bar'.startsWith('foo'));");
    }

    @Test
    public void string_endsWith() {
        Misc.init();
        Misc.runSource("TAJS_assert('foo'.endsWith('o'));",
                        "TAJS_assert('foo'.endsWith('oo'));",
                        "TAJS_assert('foo'.endsWith('foo'));",
                        "TAJS_assert(!'foo'.endsWith('ffoo'));",
                        "TAJS_assert(!'bar'.endsWith('foo'));");
    }

    @Test
    public void testPrimitiveCall() {
        Misc.init();
        //Options.get().enableFlowgraph();
        Misc.runSource("var x = 'dyt'.toString();",
                        "TAJS_assertEquals(x, 'dyt');");
    }

	@Test
	public void testToStringCall() {
		Misc.init();
		//Options.get().enableFlowgraph();
		Misc.runSource("var y = { dyt: function() { return this.baat }, baat: 42 };",
						"var z = { toString: function() { return 'dyt'; } };",
						"var x = y[z]();",
						"TAJS_assertEquals(x, 42);");
	}

	@Test
	public void testPartialCreate() {
		Misc.init();
		Misc.runSource("TAJS_makePartial('FUNCTION', 'dummy1');",
						"TAJS_makePartial('OBJECT', 'dummy2');",
						"TAJS_makePartial('ARRAY', 'dummy3');");
	}

	@Test
	public void testPartialIdentity() {
		Misc.init();
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
		Misc.init();
		Misc.runSource("var f = TAJS_makePartial('FUNCTION', 'dummy');",
						"f();");
	}

	@Test
	public void escapingArguments1() {
        Misc.init();
		Misc.runSource(
				new String[]{
						"function mkArgs() { return arguments; }",
						"var args = mkArgs();",
						"args.foo;" // discovers bottom-object -> stops propagation
				});
	}

    @Test
    public void escapingArguments2() {
        Misc.init();
        Misc.runSource("function mkArgs() { return arguments; }",
                        "var args = mkArgs('dyt');",
                        "TAJS_assertEquals(args[0], 'dyt');");
    }

	@Test
	public void toStringCallUndefined() {
		Misc.init();
		Misc.runSource("var x = Object.prototype.toString.call();",
				"TAJS_assertEquals('[object Undefined]', x);"
		);
	}

	@Test
	public void toStringCallNull() {
		Misc.init();
		Misc.runSource("var x = Object.prototype.toString.call(null);",
				"TAJS_assertEquals('[object Null]', x);");
	}

	@Test
	public void toStringApplyUndefined() {
		Misc.init();
		Misc.runSource("var x = Object.prototype.toString.apply();",
				"TAJS_assertEquals('[object Undefined]', x);");
	}

	@Test
	public void toStringApplyNull() {
		Misc.init();
		Misc.runSource("var x = Object.prototype.toString.apply(null);",
				"TAJS_assertEquals('[object Null]', x);");
	}

	@Test
	public void trimCallUndefined() {
		Misc.init();
		Misc.runSource("try { ",
				"  String.prototype.trim.call(undefined);",
				"  TAJS_assert(false);",
				"} catch (e) { ",
				"  TAJS_assert(e instanceof TypeError);",
				"}");
	}

	@Test
	public void trimCallNull() {
		Misc.init();
		Misc.runSource("try { ",
				"  String.prototype.trim.call(null);",
				"  TAJS_assert(false);",
				"} catch (e) { ",
				"  TAJS_assert(e instanceof TypeError);",
				"}");
	}

	@Test
	public void trimCallString() {
		Misc.init();
		Misc.runSource("try { ",
				"  TAJS_assertEquals(String.prototype.trim.call(' x '), 'x');",
				"} catch (e) { ",
				"  TAJS_assert(false);",
				"}");
	}

	@Test
	public void callStringThisNonStrict() {
		Misc.init();
		Misc.runSource("try { ",
				"  function f(){TAJS_assert(this instanceof String)};",
				"  f.call('foo');",
				"} catch (e) { ",
				"  TAJS_assert(false);",
				"}");
	}

    @Test
    public void callStringThisStrict() {
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   'use strict';",
                "   try { ",
                "     function f(){TAJS_assertEquals('foo', this)};",
                "     f.call('foo');",
                "   } catch (e) { ",
                "     TAJS_assert(false);",
                "   }",
                "})();");
    }

    @Test
    public void callStrictWithoutReceiver() {
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   'use strict';",
                "   try { ",
                "     function f(){TAJS_assertEquals(undefined, this)};",
                "     f();",
                "   } catch (e) { ",
                "     TAJS_assert(false);",
                "   }",
                "})();");
    }

    @Test
    public void inheritStrict() {
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   'use strict';",
                "   try { ",
                "     function f(){TAJS_assertEquals(undefined, this)};",
                "     f();",
                "   } catch (e) { ",
                "     TAJS_assert(false);",
                "   }",
                "})();");
    }

    @Test
    public void deepInheritStrict() {
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   'use strict';",
                "   (function(){",
                "       try { ",
                "           function f(){TAJS_assertEquals(undefined, this)};",
                "           f();",
                "       } catch (e) { ",
                "           TAJS_assert(false);",
                "       }",
                "   })();",
                "})();");
    }

    @Test
    public void selfStrict() {
        Misc.init();
        Misc.runSource(
                "(function(){",
                "   try { ",
                "     function f(){'use strict'; TAJS_assertEquals(undefined, this)};",
                "     f();",
                "   } catch (e) { ",
                "     TAJS_assert(false);",
                "   }",
                "})();");
    }

    @Test(expected = AssertionError.class /* Top level 'use strict' is not yet supported (for architectural reasons */)
    public void topLevelStrict() {
        Misc.init();
        Misc.runSource(
                "'use strict';",
                "try { ",
                "  function f(){TAJS_assertEquals(undefined, this)};",
                "  f();",
                "} catch (e) { ",
                "  TAJS_assert(false);",
                "}");
    }

	@Test
	public void hasOwnPropertyCallString() {
		Misc.init();
		Misc.runSource("try { ",
				"TAJS_assert(Object.prototype.hasOwnProperty.call('', 'length'));",
				"} catch (e) { ",
				"  TAJS_assert(false);",
				"}");
	}

//	@Test(expected = AssertionError.class)
//	public void uninvoked__TAJS_assert() {
//		Misc.runSource(
//				"if(false)TAJS_assert(true);"
//		);
//	}

//	@Test(expected = AssertionError.class)
//	public void uninvoked__TAJS_assertEquals() {
//		Misc.runSource(
//				"if(false)TAJS_assertEquals(true, true);"
//		);
//	}

	@Test
	public void uninvoked__TAJS_assertFalse() {
		Misc.runSource("if(false)TAJS_assert(false);");
	}

//    @Test
//    public void htmlErrorWarnings() {
//	    Misc.init();
//	    Misc.captureSystemOutput();
//        Misc.run(
//                "test/micro/bad-html.html"
//        });
//        Misc.checkSystemOutput();
//	}

//	@Test
//	public void typeofComplements_equalityInCondition() {
//		Misc.runSource(
//				"if(typeof TAJS_join('foo', 42) === 'function'){",
//				"	TAJS_assert(false);",
//				"}"
//		);
//	}

//	@Test
//	public void typeofComplements_inequalityInCondition() {
//		Misc.runSource(
//				"if(typeof TAJS_join('foo', 42) !== 'function'){",
//				"}else{",
//				"	TAJS_assert(false);",
//				"}"
//		);
//	}

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
        Misc.init();
        Misc.captureSystemOutput();
        Misc.run("test/micro/bad-html.html");
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
		Misc.init();
        Misc.captureSystemOutput();
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
}
