package dk.brics.tajs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestCollections.class,
        TestMicro.class,
        TestFlowgraphBuilder.class,
        TestNoFlowNodeInsertion.class,
        TestAssumeNonNullUndef.class,
        TestAddContextSensitivity.class,
        TestSourceLocations.class,
        TestSyntacticObjectSensitivity.class,
        TestAssumeNonNullUndef.class,
        TestValue.class,
        TestAnderson.class,
        TestJSNativeFunctions.class,
        TestResig.class,
        TestForIn.class,
        TestUnrollingUnknownValueRecovery.class,
        TestUnrollingTermination.class,
        TestUnrolling.class,
        TestBoundedUnrolling.class,
        TestWala.class,
        TestUneval.class,
        TestSunspider.class,
        TestV8.class,
        TestGoogle.class,
        Test10K.class,
        TestChromeExperiments.class,
        TestHeap.class,
        Test1K2012Love.class,
        Test1K2013Spring.class,
        Test1K2014Dragons.class,
        TestKaistAlexaBenchmarksFlowgraph.class,
        TestMicroStringEscaping.class
})
public class RunFast {

    public static void main(String[] args) {
            org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunFast");
    }
}
