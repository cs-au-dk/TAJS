package dk.brics.tajs.test;

// import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import dk.brics.tajs.options.Options;
// import dk.brics.tajs.util.AnalysisException;

@SuppressWarnings("static-method")
public class TestWala {

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestWala");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.enableTest();
		Options.enablePolymorphic();
		// Options.enableNoLazy();
	}

	@Test
	public void testWalaForIn() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/forin.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore
	@Test
	public void testWalaForInct() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableCorrelationTracking();
		String[] args = {"test/wala/forin.js"};
		Misc.run(args);
		Options.disableCorrelationTracking();
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaFunctions() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/functions.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaInherit() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/inherit.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaMoreControlFlow() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/more-control-flow.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaNewFn() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
        boolean newFg = Options.isOldFlowgraphBuilderEnabled();
        Options.disableOldFlowgraphBuilder();
        Options.enableUnevalMode();
		String[] args = {"test/wala/newfn.js"};
		Misc.run(args);
        if (!newFg)
            Options.enableOldFlowgraphBuilder();
        Misc.checkSystemOutput();
	}

	@Test
	public void testWalaObjects() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/objects.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test // TODO: HTML DOM
	public void testWalaPortalExampleSimple() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/portal-example-simple.html"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaSimpleLexical() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/simple-lexical.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaSimple() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/simple.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaSimpler() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/simpler.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaStringOp() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/string-op.js"}; //note: bug in assumption
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaStringPrims() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/string-prims.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}


	@Test
	public void testWalaTry() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
        boolean newFg = Options.isOldFlowgraphBuilderEnabled();
        Options.disableOldFlowgraphBuilder();
		String[] args = {"test/wala/try.js"};
		Misc.run(args);
        if (!newFg)
            Options.enableOldFlowgraphBuilder();
		Misc.checkSystemOutput();
	}

	@Test
	public void testWalaUpward() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/wala/upward.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

    @Test
    public void testWalaArgs() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/wala/args.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }
}
