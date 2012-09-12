package dk.brics.tajs.test;

import org.junit.Before;
import org.junit.Test;

import dk.brics.tajs.options.Options;

@SuppressWarnings("static-method")
public class TestGoogle { 

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestGoogle");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.enableTest();
        Options.enablePolymorphic();
	}
	
	@Test
	public void testRichards() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google/richards.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testBenchpress() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google/benchpress.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void testCryptobench() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google/cryptobench.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testDeltaBlue() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google/delta-blue.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
}
