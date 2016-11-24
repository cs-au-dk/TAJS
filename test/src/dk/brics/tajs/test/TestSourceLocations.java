package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.util.AnalysisException;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestSourceLocations {
	
	@Before
	public void before() {
		Main.reset();
	}

	@Test
	public void sourcelocations_javascript() {
		// expect line 1
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-test", "test/sourcelocations/javascript.js" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_htmlWithInternalJavascript() {
		// expect line 9
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-test", "test/sourcelocations/htmlWithInternalJavascript.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_htmlWithExternalJavascript() {
		// expect line 1
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-test", "test/sourcelocations/htmlWithExternalJavascript.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

    @Test
    public void sourcelocations_htmlWithExternalJavascriptUsingFileProtocol() {
		Misc.init();
		String[] args = {"-quiet", "-test", "test/sourcelocations/htmlWithExternalJavascriptUsingFileProtocol.html"};
		Misc.run(args);
	}

    @Test(expected = AnalysisException.class /* no http server running ... */)
    public void sourcelocations_htmlWithExternalJavascriptUsingHTTPProtocol() {
		Misc.init();
		String[] args = {"-quiet", "-test", "test/sourcelocations/htmlWithExternalJavascriptUsingHTTPProtocol.html"};
		Misc.run(args);
	}

    @Test(expected = AnalysisException.class)
    public void sourcelocations_htmlWithExternalJavascriptUsingAgnosticProtocol() {
		Misc.init();
		String[] args = {"-quiet", "-test", "test/sourcelocations/htmlWithExternalJavascriptUsingAgnosticProtocol.html"};
        Misc.run(args);
    }

    @Test
    public void sourcelocations_htmlWithMiscReferences_root() {
        Misc.init();
        String[] args = {"-quiet", "-test", "test/sourcelocations/misc-references/in-root.html"};
        Misc.run(args);
    }

    @Test
    public void sourcelocations_htmlWithMiscReferences_dir() {
        Misc.init();
        String[] args = {"-quiet", "-test", "test/sourcelocations/misc-references/dir/in-dir.html"};
        Misc.run(args);
    }

    @Test
    public void sourcelocations_htmlWithMiscReferences_dirdir() {
        Misc.init();
        String[] args = {"-quiet", "-test", "test/sourcelocations/misc-references/dir/dirdir/in-dirdir.html"};
        Misc.run(args);
	}

	@Test
	public void sourcelocations_htmlWithInternalAndExternalJavascript() {
		// expect line 9 for html
		// expect line 1 for js
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-test", "test/sourcelocations/htmlWithInternalAndExternalJavascript.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_htmlWithExternalAndInternalJavascript() {
		// expect line 13 for html
		// expect line 1 for js
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-test", "test/sourcelocations/htmlWithExternalAndInternalJavascript.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_htmlWithInternalAndInternalJavascript() {
		// expect line 9 for html
		// expect line 14 for html2
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-test", "test/sourcelocations/htmlWithInternalAndInternalJavascript.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_htmlWithExternalAndExternalJavascript() {
		// expect line 1 for js
		// expect line 2 for js2
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-test", "test/sourcelocations/htmlWithExternalAndExternalJavascript.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_eval() {
		// expect line 1
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-uneval", "-test", "test/sourcelocations/eval.js" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_evalSource() {
		// expect line 2
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-uneval", "-test", "test/sourcelocations/evalSource.js" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_eventhandlers() {
		// expect line 8
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-uneval", "-test", "test/sourcelocations/eventhandlers.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_eventhandlerWithMultiLineEvents() {
		// expect line 10
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-uneval", "-test", "test/sourcelocations/eventhandlerWithMultiLineEvents.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_htmlWithFunctionInternalJavascript() {
		// expect line 9
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-test", "test/sourcelocations/htmlWithFunctionInternalJavaScript.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_htmlWithEvalInternalJavascript() {
		// expect line 9
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-uneval", "-test", "test/sourcelocations/htmlWithEvalInternalJavascript.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_htmlWithMultiLineEvalInternalJavascript() {
		// expect line 10
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-uneval", "-test", "test/sourcelocations/htmlWithMultiLineEvalInternalJavascript.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_htmlWithFunctionWithMultiLineEvalInternalJavascript() {
		// expect line 11, 13
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-uneval", "-test", "test/sourcelocations/htmlWithFunctionWithMultiLineEvalInternalJavascript.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_htmlProblematicCase() {
		// TODO:
		// odd line number 6 (compared to testFlowgraphBuilder0156.out which is slightly better):
		// test/sourcelocations/htmlProblematicCase.tidy.html:6: [maybe] Uncaught exception, constructed at [test/sourcelocations/htmlProblematicCase.tidy.html:17:16]

		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-uneval", "-test", "test/sourcelocations/htmlProblematicCase.html" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_doubleVar() {
		// expect column 5, 12
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-test", "test/sourcelocations/doubleVar.js" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void sourcelocations_tripleVar() {
		// expect column 5, 12
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = { "-quiet", "-test", "test/sourcelocations/tripleVar.js" };
		Misc.run(args);
		Misc.checkSystemOutput();
	}

}
