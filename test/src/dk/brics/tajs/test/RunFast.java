package dk.brics.tajs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Fast test suite for testing essential TAJS behavior.
 * It is probably sufficient to run this test suite after minor implementation changes.
 * Except for TestMicro, each class in the suite should run in less than a second.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // misc. tests
        TestMicro.class,
        // application tests
        TestWala.class,
        TestResig.class,
        TestJQueryEach.class,
        TestAnderson.class,
        // specific tests
        TestValue.class,
        TestTAJSFunctions.class,
        TestHeap.class,
        TestForIn.class,
        TestUnrolling.class,
        TestAssumeNonNullUndef.class,
        TestAssumeForBranches.class,
        TestGettersSetters.class,
        TestStrict.class,
        TestConversion.class,
})
public class RunFast {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunFast");
    }
}
