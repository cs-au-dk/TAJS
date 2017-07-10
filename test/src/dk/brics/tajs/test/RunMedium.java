package dk.brics.tajs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Medium speed test suite, for thorough testing of TAJS behavior.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        RunMediumModels.class,
        RunMediumFeatures.class,
        RunMediumECMAApps.class,
        RunMediumDOMApps.class,

        // code inspection
        PackageDependencyTest.class
})
public class RunMedium {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunMedium");
    }
}
