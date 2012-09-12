package dk.brics.tajs.test;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

@SuppressWarnings("static-method")
public class TestMicro {

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestMicro");
	}

	@Before
	public void init() {
		Options.reset();
		Options.enableTest();
		Options.enablePolymorphic();
		//Options.enableNoModified();
		//Options.enableNoLazy();
		//Options.enableDebug();
		//Options.enableNoRecency();
	}

	@Test
	public void testMicro00() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test00.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro01() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test01.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro02() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test02.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro03() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test03.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro04() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test04.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro05() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test05.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro06() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test06.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro07() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test07.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro08() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test08.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro09() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test09.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro10() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test10.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro11() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test11.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro12() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test12.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro13() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test13.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro14() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test14.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro15() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test15.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro16() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test16.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro17() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test17.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro18() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test18.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro19() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test19.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro20() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test20.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro21() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test21.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro22() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test22.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro23() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test23.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro24() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test24.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro25() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test25.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro26() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test26.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro27() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test27.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro28() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test28.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro29() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test29.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro30() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test30.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro31() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test31.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro32() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test32.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro33() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test33.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro34() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test34.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro35() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test35.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro36() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test36.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro37() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test37.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro38() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test38.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro39() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test39.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro40() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test40.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro41() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test41.js", "test/micro/test41b.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro42() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test42.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro43() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test43.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro44() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test44.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro45() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test45.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro46() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test46.js"};
		Misc.run(args);
		Misc.checkSystemOutput();// TODO: summary function object (related to function object joining?)
	}

	@Test
	public void testMicro47() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test47.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro48() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test48.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro49() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test49.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro50() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test50.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testMicro50ct() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableCorrelationTracking();
		String[] args = {"test/micro/test50.js"};
		Misc.run(args);
		Options.disableCorrelationTracking();
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro51() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test51.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro52() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test52.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro53() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test53.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro54() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test54.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro55() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test55.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro56() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test56.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro57() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test57.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro58() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test58.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro59() throws Exception { // TODO: precision could be improved at concatenation of uint / non-uint strings
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test59.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro60() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test60.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro61() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test61.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro62() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test62.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro63() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test63.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testMicro63ct() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableCorrelationTracking();
		String[] args = {"test/micro/test63.js"};
		Misc.run(args);
		Options.disableCorrelationTracking();
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro64() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test64.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro65() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test65.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro66() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test66.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro67() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test67.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro68() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test68.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro69() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test69.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro70() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test70.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro71() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test71.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro72() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test72.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro73() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test73.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro74() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test74.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testMicro74ct() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableCorrelationTracking();
		String[] args = {"test/micro/test74.js"};
		Misc.run(args);
		Options.disableCorrelationTracking();
		Misc.checkSystemOutput();
	}

	// TODO: Gets the exception handler wrong for a lot of blocks with the old flow graph builder.
	@Test
	public void testMicro75() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test75.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro76() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test76.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro77() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test77.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro78() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test78.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro79() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test79.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro80() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test80.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro81() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test81.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro82() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test82.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro83() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test83.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro84() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test84.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro85() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test85.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro86() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test86.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro87() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test87.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro88() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test88.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro89() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test89.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro90() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test90.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro91() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test91.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro92() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test92.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro93() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test93.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro94() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test94.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro95() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test95.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro96() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test96.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro97() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test97.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro98() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test98.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro99() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test99.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro100() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test100.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro101() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test101.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro102() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test102.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro103() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test103.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro104() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test104.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro105() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test105.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro106() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test106.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro107() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test107.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro108() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test108.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro109() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test109.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro110() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test110.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro111() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test111.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro112() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test112.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro113() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test113.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	// Should be runtime error, but Sec 16 p.149 allows us to report the error early.
	@Test(expected=AnalysisException.class)
	public void testMicro114() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		boolean newFg = Options.isOldFlowgraphBuilderEnabled();
		Options.disableOldFlowgraphBuilder();
		String[] args = {"test/micro/test114.js"};
		Misc.run(args);
		if (!newFg)
			Options.enableOldFlowgraphBuilder();
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro115() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test115.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro116() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test116.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro117() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test117.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro118() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test118.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro119() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test119.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro123() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test123.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testMicro123ct() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableCorrelationTracking();
		String[] args = {"test/micro/test123.js"};
		Misc.run(args);
		Options.disableCorrelationTracking();
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro126() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test126.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro127() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test127.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro128() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test128.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro129() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test129.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro130() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test130.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro131() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test131.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro132() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test132.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro133() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test133.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro134() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test134.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro135() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test135.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro136() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test136.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro137() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test137.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro138() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test138.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro139() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test139.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test(expected=AnalysisException.class)
	public void testMicro140() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		boolean newFg = Options.isOldFlowgraphBuilderEnabled();
		Options.disableOldFlowgraphBuilder();
		String[] args = {"test/micro/test140.js"};
		Misc.run(args);
		if (!newFg)
			Options.enableOldFlowgraphBuilder();
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro141() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test141.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro142() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test142.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: getters/setters
	@Test
	public void testMicro143() throws Exception {
		Assert.fail("Add support for getters/setters");
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test143.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: getters/setters
	@Test
	public void testMicro144() throws Exception {
		Assert.fail("Add support for getters/setters");
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test144.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro145() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test145.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore // FIXME: BlockState (readPropertyRaw?) currently doesn't model ES5-style string index properties
	@Test
	public void testMicro146() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test146.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro147() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test147.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro148() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test148.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro148b() throws Exception {
		Options.enableNoModified(); // maybe-modified can make a difference, even with lazy prop
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test148.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro149() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test149.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro150() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test150.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro151() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test151.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro152() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test152.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro153() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test153.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro154() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test154.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro155() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test155.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro156() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test156.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro157() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test157.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro158() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test158.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro159() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test159.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro160() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test160.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro161() throws Exception { // TODO: pop from scope chain before gc?
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test161.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro162() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test162.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro163() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test163.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro164() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test164.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro165() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test165.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro166() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test166.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro167() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test167.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro168() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test168.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro169() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test169.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro170() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test170.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro171() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test171.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro172() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test172.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro173() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test173.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro174() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test174.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro175() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test175.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	@Test
	public void testMicro176() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test176.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testMicro176ct() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableCorrelationTracking();
		String[] args = {"test/micro/test176.js"};
		Misc.run(args);
		Options.disableCorrelationTracking();
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro177() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test177.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro178() throws Exception { // FIXME: don't report "Reading absent property" when the read is used as a branch condition (see Monitoring.visitReadProperty)
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test178.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testMicro179() throws Exception { // FIXME: for-in
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test179.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void testMicro180() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test180.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro181() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test181.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testMicro182() throws Exception { // FIXME: toString
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test182.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testMicro183() throws Exception { // FIXME: valueOf
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test183.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testMicro184() throws Exception { // TODO: Too few parameters to function String.prototype.substr?
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test184.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testMicro185() throws Exception { // FIXME: Rounding error.
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test185.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro186() throws Exception { // TODO: Improve precision, should give 0.
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test186.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro187() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test187.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro188() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/test188.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testMicro188un() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableUnrollOneAndAHalf();
		String[] args = {"test/micro/test188.js"};
		Misc.run(args);
		Options.enableUnrollOneAndAHalf();
		Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testMicro189() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableCorrelationTracking();
		String[] args = {"test/micro/test189.js"};
		Misc.run(args);
		Options.disableCorrelationTracking();
		Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testMicro189un() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableCorrelationTracking();
		Options.enableUnrollOneAndAHalf();
		String[] args = {"test/micro/test189.js"};
		Misc.run(args);
		Options.disableCorrelationTracking();
		Options.disableUnrollOneAndAHalf();
		Misc.checkSystemOutput();
	}

	@Test
	public void testArray() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testArray.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testBoolean() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testBoolean.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test(expected=AnalysisException.class)
	public void testEval() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableUnevalMode();
		boolean newFg = Options.isOldFlowgraphBuilderEnabled();
		Options.disableOldFlowgraphBuilder();
		String[] args = {"test/micro/testEval.js"};
		Misc.run(args);
		if (!newFg)
			Options.enableOldFlowgraphBuilder();
		Misc.checkSystemOutput();
	}

	@Test
	public void testFunction() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		boolean newFg = Options.isOldFlowgraphBuilderEnabled();
		Options.enableUnevalMode();
		Options.disableOldFlowgraphBuilder();
		String[] args = {"test/micro/testFunction.js"};
		Misc.run(args);
		if (!newFg)
			Options.enableOldFlowgraphBuilder();
		Options.disableUnevalMode();
		Misc.checkSystemOutput();
	}

	@Test
	public void testFunctionApply() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testFunctionApply.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testFunctionCall() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testFunctionCall.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testNumber() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testNumber.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testObject() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testObject.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testOO() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testOO.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testRegExp() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testRegExp.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testString() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testString.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test // TODO: Check output; Handle valueOf/toString correctly. testToPrimitive.js
	public void testToPrimitive() throws Exception { // TODO: testToPrimitive.js:19:1: [info] Abstract value: Str => could be "xy*"
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testToPrimitive.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testPaper() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/micro/testPaper.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
}
