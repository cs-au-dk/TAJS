package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.monitoring.OrdinaryExitReachableChecker;
import dk.brics.tajs.options.OptionValues;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.solver.Message.Status;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("static-method")
public class TestAssumeNonNullUndef {

    private static final String maybe_undef_var_x = "var x = Math.random()? undefined: {};";

    private static final String maybe_undef_prop_p_on_o = "var o = {}; o.p = Math.random()? undefined: {};";

    @Before
    public void before() {
        Main.reset();
        Main.initLogging();
    }

    @Test
    public void flowgraphForWriteVarProp() {
        Misc.runSource("this.p = 42;");
    }

    @Test
    public void prop_one() {
        t(1, maybe_undef_prop_p_on_o,
                "o.p.p;");
    }

    @Test
    public void prop_one_becauseAssertion() {
        t(1, maybe_undef_prop_p_on_o,
                "o.p.p;",
                "o.p.p;");
    }

    @Test
    public void prop_one_becauseAssertionInIf() {
        t(1, maybe_undef_prop_p_on_o,
                "if(o.p.p){}",
                "o.p.p;");
    }

    @Test
    public void prop_one_readRead() {
        t(1, maybe_undef_prop_p_on_o,
                "o.p.p;",
                "o.p.p;");
    }

    @Test
    public void prop_one_readReadRead() {
        t(1, maybe_undef_prop_p_on_o,
                "o.p.p;",
                "o.p.p;",
                "o.p.p;");
    }

    @Test
    public void prop_one_writeRead() {
        t(1, maybe_undef_prop_p_on_o,
                "o.p.p = 42;",
                "o.p.p;");
    }

    @Test
    public void prop_one_writeReadRead() {
        t(1, maybe_undef_prop_p_on_o,
                "o.p.p = 42;",
                "o.p.p;",
                "o.p.p;");
    }

    @Test
    public void prop_two() {
        t(2, maybe_undef_prop_p_on_o,
                "if(Math.random()){",
                "   o.p.p;",
                "}else{",
                "   o.p.p;",
                "}");
    }

    @Test
    public void prop_two_becauseAssertion() {
        t(2, maybe_undef_prop_p_on_o,
                "if(Math.random()){",
                "   o.p.p;",
                "}else{",
                "   o.p.p;",
                "}",
                "o.p.p;");
    }

    @Test
    public void prop_two_becauseNoAssertion() {
        t(2, maybe_undef_prop_p_on_o,
                "if(Math.random()){",
                "   o.p.p;",
                "}",
                "o.p.p;");
    }

    @Test
    public void var_none() {
        t(0,
                ";");
    }

    @Test
    public void var_one() {
        t(1, maybe_undef_var_x,
                "x.p;");
    }

    @Test
    public void var_one_becauseAssertion() {
        t(1, maybe_undef_var_x,
                "x.p;",
                "x.p;");
    }

    @Test
    public void var_one_becauseAssertionInIf() {
        t(1, maybe_undef_var_x,
                "if(x.p){}",
                "x.p;");
    }

    @Test
    public void var_one_readRead() {
        t(1, maybe_undef_var_x,
                "x.p;",
                "x.p;");
    }

    @Test
    public void var_one_readReadRead() {
        t(1, maybe_undef_var_x,
                "x.p;",
                "x.p;",
                "x.p;");
    }

    @Test
    public void var_one_writeRead() {
        t(1, maybe_undef_var_x,
                "x.p = 42;",
                "x.p;");
    }

    @Test
    public void var_one_writeReadRead() {
        t(1, maybe_undef_var_x,
                "x.p = 42;",
                "x.p;",
                "x.p;");
    }

    @Test
    public void var_two() {
        t(2, maybe_undef_var_x,
                "if(Math.random()){",
                "   x.p;",
                "}else{",
                "   x.p;",
                "}");
    }

    @Test
    public void var_two_becauseAssertion() {
        t(2, maybe_undef_var_x,
                "if(Math.random()){",
                "   x.p;",
                "}else{",
                "   x.p;",
                "}",
                "x.p;");
    }

    @Test
    public void var_two_becauseNoAssertion() {
        t(2, maybe_undef_var_x,
                "if(Math.random()){",
                "   x.p;",
                "}",
                "x.p;");
    }

    public void t(int expectedWarnings, String... source) {
        testWithSource(expectedWarnings, source);
    }

    public void testWithSource(final int expectedWarnings, final String... source) {
        OptionValues baseOptions = new OptionValues();
        baseOptions.enableTest();
        IAnalysisMonitoring monitoring = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableChecker());

        Misc.runSource(source, monitoring);
        Set<Message> nullUndefWarnings = new HashSet<>();
        for (Message message : monitoring.getMessages()) {
            if (message.getStatus() == Status.MAYBE && message.getMessage().endsWith(" is null/undefined")) {
                nullUndefWarnings.add(message);
            }
        }
        Assert.assertEquals(expectedWarnings, nullUndefWarnings.size());
    }
}
