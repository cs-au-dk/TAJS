package dk.brics.tajs.test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.brics.tajs.options.Options;

@SuppressWarnings("static-method")
public class TestResig {

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestResig");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.enableTest();
		// Options.enableNoLazy();
		// Options.enablePolymorphic();
	}

	@Test
	public void resig_ejohn12() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn12.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn13() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn13.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn14() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn14.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn15() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn15.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn17() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn17.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn18() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn18.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn19() throws Exception {
		Misc.init();
        Options.enableIncludeDom();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn19.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn20() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn20.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn21() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn21.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn23() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn23.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn24() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn24.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn25() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn25.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn26() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn26.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn28() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn28.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn30() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn30.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn31() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn31.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn33() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn33.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn34() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn34.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn35() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn35.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn36() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn36.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn38() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn38.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: for-in
	@Test
	public void resig_ejohn40() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn40.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn41() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn41.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn42() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn42.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn43() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn43.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

    @Ignore // TODO
	@Test
	public void resig_ejohn45() throws Exception { // TODO: shouldn't report unreachable code (from Array sort)
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn45.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn47() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn47.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn49() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn49.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn50() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn50.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: jQuery
	@Test
	public void resig_ejohn51() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn51.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void resig_ejohn52() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn52.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void resig_ejohn53() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn53.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn54() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn54.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void resig_ejohn56() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn56.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void resig_ejohn58() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn58.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void resig_ejohn59() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn59.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: HTML DOM
	@Test
	public void resig_ejohn63() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn63.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn65() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn65.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn66() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn66.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn67() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn67.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn69() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn69.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn71() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn71.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn72() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn72.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn74() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn74.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn76() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn76.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn78() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn78.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn80() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn80.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn80b() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn80b.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: for-in
	@Test
	public void resig_ejohn81() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn81.js"};
		Misc.run(args);
		fail("check output"); // Misc.checkSystemOutput();
	}

    @Ignore
	@Test
	public void resig_ejohn83() throws Exception { // TODO: check output
        Options.enableIncludeDom();
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn83.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

    @Ignore
	@Test
	public void resig_ejohn84() throws Exception {  // TODO: check output
        Options.enableIncludeDom();
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn84.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

    @Ignore
	@Test
	public void resig_ejohn85() throws Exception {  // TODO: check output
        Options.enableIncludeDom();
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn85.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

    @Ignore
	@Test
	public void resig_ejohn86() throws Exception { // TODO: check output
        Options.enableIncludeDom();
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn86.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn88() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn88.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void resig_ejohn90() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/resig/ejohn90.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
}
