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
        // specific tests
        TestValue.class,
        TestHeap.class,
        TestForIn.class,
        TestUnrolling.class,
        TestAssumeNonNullUndef.class,

        // misc. tests
        TestMicro.class,

        // application tests
        TestAnderson.class,
        TestJQueryEach.class,
        TestWala.class,
        TestResig.class,
})
public class RunFast {

    public static void main(String[] args) {
            org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunFast");
    }
}
