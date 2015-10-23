package dk.brics.tajs.monitoring;

import dk.brics.tajs.util.AnalysisException;

/**
 * A simple monitoring that will prevent the analysis from running more than a set time.
 * <p>
 * Ignores time spent in building flowgraph and post-processing analysis results.
 */
public class AnalysisTimeLimiter extends DefaultAnalysisMonitoring {

    private final long secondsTimeLimit;

    private final boolean crash;

    private long maxNanoTime = -1;

    private boolean analysisWasLimited = false;

    /**
     * @param secondsTimeLimit as the number of second the analysis is allowed to run
     * @param crash true if an exception should be thrown when the analysis exceed the time limit
     */
    public AnalysisTimeLimiter(int secondsTimeLimit, boolean crash) {
        this.secondsTimeLimit = secondsTimeLimit;
        this.crash = crash;
    }

    public AnalysisTimeLimiter(int secondsTimeLimit) {
        this(secondsTimeLimit, false);
    }

    @Override
    public boolean allowNextIteration() {
        // NB: it has been observed that a single iteration took minutes, so we might overshoot the time limit by a lot!
        long now = System.nanoTime();
        boolean timeOut = maxNanoTime != -1 && maxNanoTime < now;
        if (timeOut) {
            if (crash) {
                throw new AnalysisException("Analysis exceeded timelimit...");
            }
            analysisWasLimited = true;
            return false;
        }
        return true;
    }

    @Override
    public void beginPhase(AnalysisPhase phase) {
        if (phase == AnalysisPhase.DATAFLOW_ANALYSIS) {
            if (secondsTimeLimit != -1) {
                long now = System.nanoTime();
                long delta = secondsTimeLimit * 1000 * 1000 * 1000;
                long future = now + delta;
                maxNanoTime = future;
            }
        }
    }

    /**
     * @return true iff the analysis spent more time than allowed
     */
    public boolean analysisExceededTimeLimit() {
        return analysisWasLimited;
    }
}
