package dk.brics.tajs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Blended analysis tests.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestUnderscore.class,
        TestLodash3.class,
        TestLodash4.class
})
public class RunBlendedAnalysis {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunBlendedAnalysis");
    }
}
