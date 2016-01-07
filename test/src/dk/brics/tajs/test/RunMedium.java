package dk.brics.tajs.test;

import dk.brics.tajs.test.js2flowgraph.TestASTInfo;
import dk.brics.tajs.test.nativeobjects.TestHostFunctionSources_ArrayPrototype;
import dk.brics.tajs.test.nativeobjects.TestHostFunctionSources_ES6Collections;
import dk.brics.tajs.test.nativeobjects.TestHostFunctionSources_TypedArrays;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Medium speed test suite, for thorough testing of TAJS behavior.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // misc. tests, that are not essential to core TAJS behavior
        TestCollections.class,
        TestNoFlowNodeInsertion.class,
        TestAddContextSensitivity.class,
        TestSyntacticObjectSensitivity.class,
        TestUnrollingUnknownValueRecovery.class,
        TestUnrollingTermination.class,
        TestBoundedUnrolling.class,
        TestMicroStringEscaping.class,
        TestASTInfo.class,
        TestClosureContextSensitivity.class,
        TestLiteralContextSensitivity.class,
        TestSourceLocations.class,
        TestFlowgraphBuilder.class,
        TestUneval.class,
        TestJSNativeFunctions.class,
        TestHostFunctionSources_ArrayPrototype.class,
        TestHostFunctionSources_ES6Collections.class,
        TestHostFunctionSources_TypedArrays.class,

        // application tests
        TestSunspider.class,
        TestV8.class,
        TestGoogle.class,
        TestGoogle2.class,
        Test10K.class,
        TestChromeExperiments.class,
        Test1K2012Love.class,
        Test1K2013Spring.class,
        Test1K2014Dragons.class,
        TestKaistAlexaBenchmarksFlowgraph.class,
        TestJQueryPatterns.class,
        TestJQueryLoad.class,
})
public class RunMedium {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunMedium");
    }
}
