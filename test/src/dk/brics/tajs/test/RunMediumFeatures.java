package dk.brics.tajs.test;

import dk.brics.tajs.test.nativeobjects.TestMDNExamples;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests for misc. TAJS features.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestPropertyNamePartitioning.class,
        TestAddContextSensitivity.class,
        TestMakeContextSensitive.class,
        TestSyntacticObjectSensitivity.class,
        TestUnrollingUnknownValueRecovery.class,
        TestUnrollingTermination.class,
        TestSoundnessTesterAssignments.class,
        TestBoundedUnrolling.class,
        TestMicroStringEscaping.class,
        TestASTInfo.class,
        TestClosureContextSensitivity.class,
        TestLiteralContextSensitivity.class,
        TestSourceLocations.class,
        TestJQueryPatterns.class,
        TestMDNExamples.class,
})
public class RunMediumFeatures {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.RunMediumFeatures");
    }
}
