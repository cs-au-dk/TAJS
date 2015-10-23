package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.StaticDeterminacyContextSensitivityStrategy;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.ExperimentalOptions;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.monitors.OrdinaryExitReachableCheckerMonitor;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;
import static org.junit.Assert.assertEquals;

public class TestClosureContextSensitivity {

    @Before
    public void before() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
        Options.get().enableContextSensitiveHeap();
        Options.get().enableParameterSensitivity();

        Options.get().enableLoopUnrolling(100);

        ExperimentalOptions.ExperimentalOptionsManager.set(new ExperimentalOptions(StaticDeterminacyContextSensitivityStrategy.StaticDeterminacyOptions.OOPSLA2014));
    }

    @Test
    public void metaTestConstant() {
        test("v0", "RESULT = 'v0';");
    }

    @Test
    public void metaTestNonClosure() {
        test("v0", "function f(){ return 'v0'}; RESULT = f();");
    }

    @Test
    public void ensureLoopunrollingWorks() {
        Misc.init();
        Options.get().enableFlowgraph();
        Misc.runSource(
                "var i = 0;",
                "while(i < 5){ i++; }",
                "TAJS_assert(i === 5);",
                "");
    }

    @Test
    public void badLoopClosure() {
        test("v2",
                "(function(){",
                "var fs = [];",
                "for(var i = 0; i <= 1; i++){",
                "   fs[i] = function(){return 'v' + i}",
                "};",
                "RESULT = fs[0]();",
                "}());");
    }

    @Test
    public void goodLoopClosure() {
        test("v0",
                "(function(){",
                "var fs = [];",
                "for(var i = 0; i <= 1; i++){",
                "   fs[i] = (function(j){return function(){var r = 'v' + j; return r;}}(i))",
                "};",
                "TAJS_dumpValue(i)",
                "RESULT = fs[0]();",
                "}());");
    }

    @Test
    public void localVariableClosure() {
        test("v0",
                "(function(){",
                "var fs = [];",
                "for(var i = 0; i <= 1; i++){",
                "   fs[i] = (",
                "       function(j){",
                "           return (function(){",
                "               var k = j;",
                "               return function(){return 'v' + k;}}()",
                "           );",
                "       }(i)",
                "   );",
                "};",
                "RESULT = fs[0]();",
                "}());");
    }

    @Test
    public void outerVariableClosure() {
        test("v0",
                "(function(){",
                "var fs = [];",
                "for(var i = 0; i <= 1; i++){",
                "   fs[i] = (",
                "       function(j){",
                "           var k = j;",
                "           return (function(){",
                "               return function(){return 'v' + k;}}()",
                "           );",
                "       }(i)",
                "   );",
                "};",
                "RESULT = fs[0]();",
                "}());");
    }

    @Test
    public void globalVariableClosure() {
        test("v1",
                "(function(){",
                "var k;",
                "var fs = [];",
                "for(var i = 0; i <= 1; i++){",
                "   fs[i] = (",
                "       function(j){",
                "           k = j",
                "           return (function(){",
                "               return function(){return 'v' + k;}}()",
                "           );",
                "       }(i + '')",
                "   );",
                "};",
                "RESULT = fs[0]();",
                "}());");
    }

    @Test
    public void globalNonVariableClosure() {
        test("v1",
                "(function(){",
                "var fs = [];",
                "for(var i = 0; i <= 1; i++){",
                "   fs[i] = (",
                "       function(j){",
                "           k = j",
                "           return (function(){",
                "               return function(){return 'v' + k;}}()",
                "           );",
                "       }(i + '')",
                "   );",
                "};",
                "RESULT = fs[0]();",
                "}());");
    }

    private Set<Value> set(String... stringValues) {
        final Set<Value> values = newSet();
        for (String string : stringValues) {
            values.add(Value.makeStr(string));
        }

        return values;
    }

    private void test(String expectedValue, String... sourceLines) {
        Value expected = Value.makeStr(expectedValue);
        test(expected, sourceLines);
    }

    private void test(Value expected, String... sourceLines) {
        TestMonitor testMonitor = new TestMonitor();
        IAnalysisMonitoring monitor = CompositeMonitoring.buildFromList(testMonitor, new Monitoring(), new OrdinaryExitReachableCheckerMonitor());
        Misc.runSource(sourceLines, monitor);
        assertEquals(expected.toString(), testMonitor.state.readVariable("RESULT", null).toString());
    }

    private class TestMonitor extends DefaultAnalysisMonitoring {

        private State state;

        @Override
        public void visitPostBlockTransfer(BasicBlock b, State state) {
            if (b.getFunction().isMain() && b.getFunction().getOrdinaryExit() == b) {
                this.state = state;
            }
        }
    }
}
