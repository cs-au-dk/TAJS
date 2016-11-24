package dk.brics.tajs.test;

// import static org.junit.Assert.fail;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestWala {

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestWala");
	}
	
	@Before
	public void init() {
		Main.reset();
		Options.get().enableTest();
		// Options.get().enableNoLazy();
	}

	@Test
	public void wala_forin() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/forin.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void wala_forinct() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/forin.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void wala_functions() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/functions.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void wala_inherit() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/inherit.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void wala_more_control_flow() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/more-control-flow.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void wala_newfn() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
        Options.get().enableUnevalizer();
		String[] args = {"test/wala/newfn.js"};
		Misc.run(args);
        Misc.checkSystemOutput();
	}

	@Test
	public void wala_objects() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/objects.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void wala_portal_example_simple() throws Exception {
		Misc.init();
		String[] args = {"test/wala/portal-example-simple.html"};
		Misc.run(args);
	}

	@Test
	public void wala_simple_lexical() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/simple-lexical.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void wala_simple() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/simple.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void wala_simpler() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/simpler.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void wala_string_op() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/string-op.js"}; //note: bug in assumption
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void wala_string_prims() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/string-prims.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}


	@Test
	public void wala_try() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/try.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void wala_upward() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/upward.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

    @Test
    public void wala_args() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/wala/args.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }
}
