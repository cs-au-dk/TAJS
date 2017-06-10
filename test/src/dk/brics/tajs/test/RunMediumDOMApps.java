package dk.brics.tajs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests for real DOM applications.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        Test10K.class,
        TestChromeExperiments.class,
        Test1K2012Love.class,
        Test1K2013Spring.class,
        TestJQueryLoad.class
})
public class RunMediumDOMApps {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunMediumDOMApps");
    }
}
