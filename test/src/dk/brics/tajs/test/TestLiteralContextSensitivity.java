package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.ContextArguments;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Collectors;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import static dk.brics.tajs.util.Collections.newSet;
import static org.junit.Assert.assertEquals;

public class TestLiteralContextSensitivity {

    final Supplier<Set<Value>> none = Collections::<Value>newSet;

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableDeterminacy();
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
    public void correlatedPropertyAccess() {
        test("o", set("x"), "var o ; function f(p1, p2){for(v in {x: 43}){o = {};}([][v] = [][v])}; f('x');");
    }

    @Test
    public void oneFixed_array() {
        test("o", none, "var o ; function f(p1, p2){o = ['A'];}; f();");
    }

    @Test(expected = AssertionError.class /* no longer allocating arrays precisely... */)
    public void oneDep_array() {
        test("o", set("x"), "var o ; function f(p1, p2){o = [p1];}; f('x');");
    }

    @Test(expected = AssertionError.class /* no longer allocating arrays precisely... */)
    public void twoDep_array() {
        test("o", set("x", "y"), "var o ; function f(p1, p2){o = [p1, p2];}; f('x', 'y');");
    }

    @Test(expected = AssertionError.class /* no longer allocating arrays precisely... */)
    public void dependOnOneGetAll_array() {
        test("o", set("x", "y"), "var o ; function f(p1, p2){o = [p1];}; f('x', 'y');");
    }

    @Test(expected = AssertionError.class /* no longer allocating arrays precisely... */)
    public void dependOnTwoGetOne_array() {
        test("o", set("x"), "var o ; function f(p1, p2){o = [p1, p2];}; var y = Math.random()? 'y1': 'y2'; f('x', y);");
    }

    private Supplier<Set<Value>> set(String... stringValues) {
        return () -> {
            final Set<Value> values = newSet();
            for (String string : stringValues) {
                values.add(Value.makeStr(string));
            }
            return values;
        };
    }

    private void test(String objectVariable, Supplier<Set<Value>> contextValues, String... sourceLines) {
        TestMonitor testMonitor = new TestMonitor();
        Misc.runSource(sourceLines, CompositeMonitoring.buildFromList(Monitoring.make(), testMonitor));
        Set<ObjectLabel> objectLabels = testMonitor.state.readVariableDirect(objectVariable).getObjectLabels();
        Set<Value> values = newSet();
        for (ObjectLabel objectLabel : objectLabels) {
            HeapContext heapContext = objectLabel.getHeapContext();
            ContextArguments arguments = heapContext.getFunctionArguments();
            if (arguments != null)
                values.addAll(arguments.getArguments().stream().filter(Objects::nonNull).collect(Collectors.toList()));
        }
        values.remove(null);
        try {
            assertEquals(contextValues.get(), values);
        } catch (Throwable e) {
            e.printStackTrace(System.out);
            throw e;
        }
    }

    private class TestMonitor extends DefaultAnalysisMonitoring {

        private State state;

        @Override
        public void visitBlockTransferPost(BasicBlock b, State state) {
            if (b.getFunction().isMain() && b.getFunction().getOrdinaryExit() == b) {
                this.state = state;
            }
        }
    }
}
