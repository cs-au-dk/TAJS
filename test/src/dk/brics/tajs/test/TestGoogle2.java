package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.monitoring.OrdinaryExitReachableChecker;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestGoogle2 {

	private IAnalysisMonitoring monitoring;

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestGoogle2");
	}
	
	@Before
	public void init() {
		Main.reset();
		Options.get().enableTest();
        monitoring = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableChecker());
    }
	
	@Test
	public void google2_richards() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google2/richards.js"};
		Misc.run(args, monitoring);
		Misc.checkSystemOutput();
	}

	@Test
	public void google2_earley_boyer() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google2/earley-boyer.js"};
		Misc.run(args, monitoring);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: contains parts of the Prototype framework, need 'tweaks'...
	@Test
	public void google2_raytrace() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google2/raytrace.js"};
		Misc.run(args, monitoring);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void google2_splay() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google2/splay.js"};
		Misc.run(args, monitoring);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void google2_regexp() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google2/regexp.js"};
		Misc.run(args, monitoring);
		Misc.checkSystemOutput();
	}

    @Test
    public void google2_crypto() throws Exception {
        Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google2/crypto.js"};
		Misc.run(args, new Monitoring() /* definite crash: bad variable naming! */);
		Misc.checkSystemOutput();
	}

	@Test
	public void google2_deltablue() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google2/deltablue.js"};
		Misc.run(args, monitoring);
		Misc.checkSystemOutput();
	}

	@Test
	public void google2_navier_stokes() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/google2/navier-stokes.js"};
		Misc.run(args, monitoring);
		Misc.checkSystemOutput();
	}
}
