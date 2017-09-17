package dk.brics.tajs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests that generally take long time.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestApps.class,
        TestJQueryUse.class,
        TestJQueryUse_unanalyzable.class,
        TestOptionToggling.class,
        TestSmoke.class
})
public class RunSlow {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunSlow");
    }
}
