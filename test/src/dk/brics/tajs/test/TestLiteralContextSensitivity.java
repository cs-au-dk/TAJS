package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.StaticDeterminacyContextSensitivityStrategy;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.ContextArguments;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.ExperimentalOptions;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.monitors.OrdinaryExitReachableCheckerMonitor;
import dk.brics.tajs.util.Collections;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newSet;
import static org.junit.Assert.assertEquals;

public class TestLiteralContextSensitivity {

    final Set<Value> none = Collections.<Value>newSet();

    @Before
    public void before() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
        Options.get().enableContextSensitiveHeap();

        ExperimentalOptions.ExperimentalOptionsManager.set(new ExperimentalOptions(StaticDeterminacyContextSensitivityStrategy.StaticDeterminacyOptions.OOPSLA2014));
    }

    @Test
    public void oneFixed() {
        test("o", none, "var o ; function f(p1, p2){o = {x: 'A'};}; f();");
    }

    @Test
    public void oneDep() {
        test("o", set("x"), "var o ; function f(p1, p2){o = {x: p1};}; f('x');");
    }

    @Test
    public void twoDep() {
        test("o", set("x", "y"), "var o ; function f(p1, p2){o = {x: p1, y: p2};}; f('x', 'y');");
    }

    @Test
    public void dependOnOneGetAll() {
        test("o", set("x", "y"), "var o ; function f(p1, p2){o = {x: p1};}; f('x', 'y');");
    }

    @Test
    public void dependOnTwoGetOne() {
        test("o", set("x"), "var o ; function f(p1, p2){o = {x: p1, y: p2};}; var y = Math.random()? 'y1': 'y2'; f('x', y);");
    }

    @Test
    public void forIn() {
        test("o", set("x"), "var o ; function f(p1, p2){for(v in {x: 43}){o = {};}}; f('x');");
    }

    @Test
    public void oneFixed_array() {
        test("o", none, "var o ; function f(p1, p2){o = ['A'];}; f();");
    }

    @Ignore // no longer allocating arrays precisely...
    @Test
    public void oneDep_array() {
        test("o", set("x"), "var o ; function f(p1, p2){o = [p1];}; f('x');");
    }

    @Ignore // no longer allocating arrays precisely...
    @Test
    public void twoDep_array() {
        test("o", set("x", "y"), "var o ; function f(p1, p2){o = [p1, p2];}; f('x', 'y');");
    }

    @Ignore // no longer allocating arrays precisely...
    @Test
    public void dependOnOneGetAll_array() {
        test("o", set("x", "y"), "var o ; function f(p1, p2){o = [p1];}; f('x', 'y');");
    }

    @Ignore // no longer allocating arrays precisely...
    @Test
    public void dependOnTwoGetOne_array() {
        test("o", set("x"), "var o ; function f(p1, p2){o = [p1, p2];}; var y = Math.random()? 'y1': 'y2'; f('x', y);");
    }

    private Set<Value> set(String... stringValues) {
        final Set<Value> values = newSet();
        for (String string : stringValues) {
            values.add(Value.makeStr(string));
        }

        return values;
    }

    private void test(String objectVariable, Set<Value> contextValues, String... sourceLines) {
        TestMonitor testMonitor = new TestMonitor();
        CompositeMonitoring.buildFromList(testMonitor, new Monitoring());
        Misc.runSource(sourceLines, CompositeMonitoring.buildFromList(testMonitor, new OrdinaryExitReachableCheckerMonitor()));
        Set<ObjectLabel> objectLabels = testMonitor.state.readVariable(objectVariable, null).getObjectLabels();
        Set<Value> values = newSet();
        for (ObjectLabel objectLabel : objectLabels) {
            HeapContext heapContext = objectLabel.getHeapContext();
            ContextArguments arguments;
            if(heapContext instanceof HeapContext) {
                arguments = ((HeapContext) heapContext).getFunctionArguments();
            }else{
                arguments = null;
            }
            if (arguments != null)
                values.addAll(arguments.getArguments().stream().filter(e -> e != null).collect(Collectors.toList()));
        }
        values.remove(null);
        assertEquals(contextValues, values);
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
