package dk.brics.tajs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests that generally take long time.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestOptionToggling.class,
        TestFeatureToggling.class,
        RunBlendedAnalysis.class,
        TestJQueryUse.class,
        TestJQueryUse_unanalyzable.class,
        TestRevamp2016.class
})
public class RunSlow {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunSlow");
    }
}
