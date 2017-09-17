package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestSourceLocations {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableUnevalizer();
    }

    @Test
    public void sourcelocations_javascript() {
        // expect line 1
        Misc.run("test-resources/src/sourcelocations/javascript.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_htmlWithInternalJavascript() {
        // expect line 9
        Misc.run("test-resources/src/sourcelocations/htmlWithInternalJavascript.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_htmlWithExternalJavascript() {
        // expect line 1
        Misc.run("test-resources/src/sourcelocations/htmlWithExternalJavascript.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_htmlWithExternalJavascriptUsingFileProtocol() {
        Misc.run("test-resources/src/sourcelocations/htmlWithExternalJavascriptUsingFileProtocol.html");
    }

    @Test(expected = AnalysisException.class /* no http server running ... */)
    public void sourcelocations_htmlWithExternalJavascriptUsingHTTPProtocol() {
        Misc.run("test-resources/src/sourcelocations/htmlWithExternalJavascriptUsingHTTPProtocol.html");
    }

    @Test(expected = AnalysisException.class)
    public void sourcelocations_htmlWithExternalJavascriptUsingAgnosticProtocol() {
        Misc.run("test-resources/src/sourcelocations/htmlWithExternalJavascriptUsingAgnosticProtocol.html");
    }

    @Test
    public void sourcelocations_htmlWithMiscReferences_root() {
        Misc.run("test-resources/src/sourcelocations/misc-references/in-root.html");
    }

    @Test
    public void sourcelocations_htmlWithMiscReferences_dir() {
        Misc.run("test-resources/src/sourcelocations/misc-references/dir/in-dir.html");
    }

    @Test
    public void sourcelocations_htmlWithMiscReferences_dirdir() {
        Misc.run("test-resources/src/sourcelocations/misc-references/dir/dirdir/in-dirdir.html");
    }

    @Test
    public void sourcelocations_htmlWithInternalAndExternalJavascript() {
        // expect line 9 for html
        // expect line 1 for js
        Misc.run("test-resources/src/sourcelocations/htmlWithInternalAndExternalJavascript.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_htmlWithExternalAndInternalJavascript() {
        // expect line 13 for html
        // expect line 1 for js
        Misc.run("test-resources/src/sourcelocations/htmlWithExternalAndInternalJavascript.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_htmlWithInternalAndInternalJavascript() {
        // expect line 9 for html
        // expect line 14 for html2
        Misc.run("test-resources/src/sourcelocations/htmlWithInternalAndInternalJavascript.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_htmlWithExternalAndExternalJavascript() {
        // expect line 1 for js
        // expect line 2 for js2
        Misc.run("test-resources/src/sourcelocations/htmlWithExternalAndExternalJavascript.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_eval() {
        // expect line 1
        Misc.run("test-resources/src/sourcelocations/eval.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_evalSource() {
        // expect line 2
        Misc.run("test-resources/src/sourcelocations/evalSource.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_eventhandlers() {
        // expect line 8
        Misc.run("test-resources/src/sourcelocations/eventhandlers.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_eventhandlerWithMultiLineEvents() {
        // expect line 10
        Misc.run("test-resources/src/sourcelocations/eventhandlerWithMultiLineEvents.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_htmlWithFunctionInternalJavascript() {
        // expect line 9
        Misc.run("test-resources/src/sourcelocations/htmlWithFunctionInternalJavaScript.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_htmlWithEvalInternalJavascript() {
        // expect line 9
        Misc.run("test-resources/src/sourcelocations/htmlWithEvalInternalJavascript.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_htmlWithMultiLineEvalInternalJavascript() {
        // expect line 10
        Misc.run("test-resources/src/sourcelocations/htmlWithMultiLineEvalInternalJavascript.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_htmlWithFunctionWithMultiLineEvalInternalJavascript() {
        // expect line 11, 13
        Misc.run("test-resources/src/sourcelocations/htmlWithFunctionWithMultiLineEvalInternalJavascript.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_htmlProblematicCase() {
        // TODO:
        // odd line number 6 (compared to testFlowgraphBuilder0156.out which is slightly better):
        // test-resources/src/sourcelocations/htmlProblematicCase.tidy.html:6: [maybe] Uncaught exception, constructed at [test-resources/src/sourcelocations/htmlProblematicCase.tidy.html:17:16]
        Misc.run("test-resources/src/sourcelocations/htmlProblematicCase.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_doubleVar() {
        // expect column 5, 12
        Misc.run("test-resources/src/sourcelocations/doubleVar.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sourcelocations_tripleVar() {
        // expect column 5, 12
        Misc.run("test-resources/src/sourcelocations/tripleVar.js");
        Misc.checkSystemOutput();
    }
}
