package dk.brics.tajs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestMicro.class,
        TestFlowgraphBuilder.class,
        TestSourceLocations.class,
        TestSyntacticObjectSensitivity.class,
        TestValue.class,
        TestAnderson.class,
        TestJSNativeFunctions.class,
        TestResig.class,
        TestForIn.class,
        TestWala.class,
        TestUneval.class,
        TestSunspider.class,
        TestV8.class,
        TestGoogle.class,
        Test10K.class,
        TestChromeExperiments.class,
        TestHeap.class
})
public class RunRegression {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunRegression");
    }
}
