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
	}
	
	@Test
	public void google_richards() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google/richards.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void google_benchpress() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google/benchpress.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void google_splay() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google/splay.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void google_cryptobench() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google/cryptobench.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void google_delta_blue() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google/delta-blue.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
}
