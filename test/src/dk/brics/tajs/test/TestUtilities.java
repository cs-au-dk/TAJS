package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for misc. meta-utilities in TAJS.
 */
public class TestUtilities {

    @Before
    public void init() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void statistics() throws Exception {
        Options.get().enableNewflow();
        String[] args = {"test/micro/test00.js"};
        Misc.run(args);
    }

    @Test
    public void newFlow() throws Exception {
        Options.get().enableNewflow();
        String[] args = {"test/micro/test00.js"};
        Misc.run(args);
    }

    @Test
    public void intermediateStates() throws Exception {
        Options.get().enableNewflow();
        Options.get().enableDebug();
        String[] args = {"test/micro/test00.js"};
        Misc.run(args);
    }

    @Test
    public void stateToDot() throws Exception {
        String[] args = {"test/micro/test00.js"};
        Misc.run(args, new CompositeMonitoring(Monitoring.make(), new DefaultAnalysisMonitoring() {
            @Override
            public void visitBlockTransferPost(BasicBlock b, State state) {
                state.toDot();
            }
        }));
    }

    @Test
    public void timing() throws Exception {
        Options.get().enableTiming();
        String[] args = {"test/micro/test00.js"};
        Misc.run(args);
    }

    @Test
    public void memory() throws Exception {
        Options.get().enableMemoryUsage();
        String[] args = {"test/micro/test00.js"};
        Misc.run(args);
    }

    @Test
    public void evalStatistics() throws Exception {
        Options.get().enableMemoryUsage();
        Options.get().enableUnevalizer();
        String[] args = {"test/uneval/uneval00.js"};
        Misc.run(args);
    }

    @Test
    public void coverage() throws Exception {
        Options.get().enableMemoryUsage();
        String[] args = {"test/micro/test00.js"};
        Misc.run(args);
    }
}
