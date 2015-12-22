package dk.brics.tajs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * All tests in the project.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // fast tests
        RunFast.class,
        // medium tests
        RunMedium.class,
        // slow tests
        TestJQueryUse.class,
        TestJQueryUse_unanalyzable.class,
        TestApps.class
})
public class RunAll {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunAll");
    }
}
