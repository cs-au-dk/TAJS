package dk.brics.tajs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests for real ECMAScript applications (no DOM).
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestSunspider.class,
//        TestV8.class,
        TestGoogle.class,
        TestGoogle2.class,
})
public class RunMediumECMAApps {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunMediumECMAApps");
    }
}
