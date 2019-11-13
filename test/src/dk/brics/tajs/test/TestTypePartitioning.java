package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisResultException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestTypePartitioning {

    @Before
    public void before() {
        Main.reset();
        Main.initLogging();
        Options.get().enableTest();
        Options.get().enableDeterminacy();
    }

    public void test(String file) {
        Misc.run(new String[]{file});
    }

    @Test
    public void testSinglePredicateCallInsideConditionalWithTypePartitioning() {
        Options.get().enableTypePartitioning();
        test("test-resources/src/type-partitioning/testSinglePredicateCallInsideConditional.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void testSinglePredicateCallInsideConditional() {
        test("test-resources/src/type-partitioning/testSinglePredicateCallInsideConditional.js");
    }

    @Test
    public void testSinglePredicateCallBeforeConditionalWithTypePartitioning() {
        Options.get().enableTypePartitioning();
        test("test-resources/src/type-partitioning/testSinglePredicateCallBeforeConditional.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void testSinglePredicateCallBeforeConditional() {
        test("test-resources/src/type-partitioning/testSinglePredicateCallBeforeConditional.js");
    }

    @Test
    public void testSingleNonDeterminatePredicateCallBeforeConditionalWithTypePartitioning() {
        Options.get().enableTypePartitioning();
        test("test-resources/src/type-partitioning/testSingleNonDeterminatePredicateCallBeforeConditional.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void testSingleNonDeterminatePredicateCallBeforeConditional() {
        test("test-resources/src/type-partitioning/testSingleNonDeterminatePredicateCallBeforeConditional.js");
    }

    @Test
    public void testDisjunctionPredicateCallInsideConditionalTest1WithTypePartitioning() {
        Options.get().enableTypePartitioning();
        test("test-resources/src/type-partitioning/testDisjunctionPredicateCallInsideConditionalTest1.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void testDisjunctionPredicateCallInsideConditionalTest1() {
        test("test-resources/src/type-partitioning/testDisjunctionPredicateCallInsideConditionalTest1.js");
    }

    @Test
    public void testDisjunctionPredicateCallInsideConditionalTest2WithTypePartitioning() {
        Options.get().enableTypePartitioning();
        test("test-resources/src/type-partitioning/testDisjunctionPredicateCallInsideConditionalTest2.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void testDisjunctionPredicateCallInsideConditionalTest2() {
        test("test-resources/src/type-partitioning/testDisjunctionPredicateCallInsideConditionalTest2.js");
    }

    @Test
    public void testConjunctionPredicateCallInsideConditionalTest1WithTypePartitioning() {
        Options.get().enableTypePartitioning();
        test("test-resources/src/type-partitioning/testConjunctionPredicateCallInsideConditionalTest1.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void testConjunctionPredicateCallInsideConditionalTest1() {
        test("test-resources/src/type-partitioning/testConjunctionPredicateCallInsideConditionalTest1.js");
    }

    @Test
    public void testConjunctionPredicateCallInsideConditionalTest2WithTypePartitioning() {
        Options.get().enableTypePartitioning();
        test("test-resources/src/type-partitioning/testConjunctionPredicateCallInsideConditionalTest2.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void testConjunctionPredicateCallInsideConditionalTest2() {
        test("test-resources/src/type-partitioning/testConjunctionPredicateCallInsideConditionalTest2.js");
    }
}
