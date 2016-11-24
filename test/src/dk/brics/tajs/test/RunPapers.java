package dk.brics.tajs.test;
/**
 * Medium speed test suite, for thorough testing of TAJS behavior.
 */

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Benchmarks from from misc. research papers.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestJSAI2014.class,
        TestJSAI2015.class,
        TestStrLat2014.class,
        TestSparse2014.class
})
public class RunPapers {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunPapers");
    }
}
