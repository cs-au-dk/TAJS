package dk.brics.tajs.test;

import dk.brics.tajs.test.js2flowgraph.TestASTInfo;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestASTInfo.class,
        TestClosureContextSensitivity.class,
        TestLiteralContextSensitivity.class,
        TestJQueryEach.class,
        TestJQueryPatterns.class,
        TestJQueryLoad.class
})
public class RunMedium {

}
