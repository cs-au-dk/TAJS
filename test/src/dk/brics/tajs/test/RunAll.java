package dk.brics.tajs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        // fast tests
        RunFast.class,
        // medium tests
        RunMedium.class,
        // slow tests
        TestJQueryUse.class,
        TestJQueryUse_unanalyzable.class
})
public class RunAll {

}
