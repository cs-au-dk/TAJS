package dk.brics.tajs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestValue.class,
        TestAnderson.class,
        TestMicro.class,
        TestResig.class,
        TestWala.class,
        TestUneval.class,
        TestSunspider.class,
        TestFlowgraphBuilder.class,
        TestGoogle.class
})
public class RunRegression {
    // Empty
}
