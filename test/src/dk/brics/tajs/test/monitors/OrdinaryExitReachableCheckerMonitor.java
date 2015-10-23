package dk.brics.tajs.test.monitors;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.monitoring.AnalysisPhase;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;

import static org.junit.Assert.assertTrue;

public class OrdinaryExitReachableCheckerMonitor extends DefaultAnalysisMonitoring {

    private boolean seenOrdinaryExit = false;

    @Override
    public void endPhase(AnalysisPhase phase) {
        if (phase == AnalysisPhase.SCAN) {
            assertTrue("Did not observe flow to ordinary program exit!", seenOrdinaryExit);
        }
    }

    @Override
    public void visitPostBlockTransfer(BasicBlock b, State state) {
        if (b.getFunction().isMain() && b.getFunction().getOrdinaryExit() == b) {
            seenOrdinaryExit = true;
        }
    }
}
