package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisResultException;
import org.junit.Before;
import org.junit.Test;

public class TestMicroDifferentLoopKinds {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableNoStringSets();
        Main.initLogging();
        Options.get().getSoundnessTesterOptions().setTest(true);
    }

    public void test(String file) {
        Misc.run(new String[]{file});
    }

    private void testWithValuePartitioning(String testFile){
        Options.get().enableNoForInSpecialization();
        Options.get().enablePropNamePartitioning();
        test(testFile);
    }

    private void testWithoutValuePartitioning(String testFile) {
        Options.get().enableDeterminacy();
        test(testFile);
    }

    // Tests with value partitioning
    @Test
    public void testCGWithValuePartitioning() {
        testWithValuePartitioning("test-resources/src/micro-different-loop-kinds/CG.js");
    }

    @Test
    public void testCFWithValuePartitioning() {
        testWithValuePartitioning("test-resources/src/micro-different-loop-kinds/CF.js");
    }

    @Test
    public void testAFWithValuePartitioning() {
        testWithValuePartitioning("test-resources/src/micro-different-loop-kinds/AF.js");
    }

    @Test
    public void testAGWithValuePartitioning() {
        testWithValuePartitioning("test-resources/src/micro-different-loop-kinds/AG.js");
    }

    @Test
    public void testIAGWithValuePartitioning() {
        testWithValuePartitioning("test-resources/src/micro-different-loop-kinds/IAG.js");
    }

    @Test
    public void testNoLoopValuePartitioning() {
        testWithValuePartitioning("test-resources/src/micro-different-loop-kinds/NoLoop.js");
    }

    @Test
    public void testAGHeapValuePartitioning() {
        testWithValuePartitioning("test-resources/src/micro-different-loop-kinds/AGHeap.js");
    }

    // Tests with TAJS with determinacy without value partitioning

    @Test
    public void testCFWithoutValuePartitioning() {
        testWithoutValuePartitioning("test-resources/src/micro-different-loop-kinds/CF.js");
    }

    @Test
    public void testCGWithoutValuePartitioning() {
        testWithoutValuePartitioning("test-resources/src/micro-different-loop-kinds/CG.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void testAFWithoutValuePartitioning() {
        testWithoutValuePartitioning("test-resources/src/micro-different-loop-kinds/AF.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void testAGWithoutValuePartitioning() {
        testWithoutValuePartitioning("test-resources/src/micro-different-loop-kinds/AG.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void testIAGWithoutValuePartitioning() {
        testWithoutValuePartitioning("test-resources/src/micro-different-loop-kinds/IAG.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void testNoLoopWithoutValuePartitioning() {
        testWithoutValuePartitioning("test-resources/src/micro-different-loop-kinds/NoLoop.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void testAGHeapWithoutValuePartitioning() {
        testWithoutValuePartitioning("test-resources/src/micro-different-loop-kinds/AGHeap.js");
    }
}
