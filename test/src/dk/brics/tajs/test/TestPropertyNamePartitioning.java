package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisResultException;
import org.junit.Before;
import org.junit.Test;

public class TestPropertyNamePartitioning {

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
    public void test1() {
        test("test-resources/src/property-name-partitioning/test1.js");
    }

    @Test
    public void test1AnyStr() {
        test("test-resources/src/property-name-partitioning/test1AnyStr.js");
    }

    @Test
    public void test1Wrapped() {
        test("test-resources/src/property-name-partitioning/test1Wrapped.js");
    }

    @Test
    public void test1forIn() {
        //Options.get().disableForInSpecialization();
        test("test-resources/src/property-name-partitioning/test1forIn.js");
    }

    @Test
    public void test1NumericLoop() {
//        Options.get().enableInspector();
        Options.get().disableLoopUnrolling();
        Options.get().enableFlowgraph();
        test("test-resources/src/property-name-partitioning/test1NumericLoop.js");
    }

    @Test
    public void test1WhileLoop() {
        Options.get().disableLoopUnrolling();
        test("test-resources/src/property-name-partitioning/test1WhileLoop.js");
    }

//    @Test
//    public void test2() {
//        test("test-resources/src/property-name-partitioning/test2.js");
//    }
//
//    @Test
//    public void test2forIn() {
//        Options.get().disableForInSpecialization();
//        test("test-resources/src/property-name-partitioning/test2forIn.js");
//    }
//
//    @Test
//    public void test2NumericLoop() {
//        Options.get().disableLoopUnrolling();
//        test("test-resources/src/property-name-partitioning/test2NumericLoop.js");
//    }
//
//    @Test
//    public void test2WhileLoop() {
//        Options.get().disableLoopUnrolling();
//        test("test-resources/src/property-name-partitioning/test2WhileLoop.js");
//    }
//
//    @Test
//    public void test3() {
//        test("test-resources/src/property-name-partitioning/test3.js");
//    }
//
//    @Test
//    public void test3forIn() {
//        Options.get().disableForInSpecialization();
//        test("test-resources/src/property-name-partitioning/test3forIn.js");
//    }
//
//    @Test
//    public void test3NumericLoop() {
//        Options.get().disableLoopUnrolling();
//        test("test-resources/src/property-name-partitioning/test3NumericLoop.js");
//    }
//
//    @Test
//    public void test3WhileLoop() {
//        Options.get().disableLoopUnrolling();
//        test("test-resources/src/property-name-partitioning/test3WhileLoop.js");
//    }
//
    @Test
    public void test4() {
        test("test-resources/src/property-name-partitioning/test4.js");
    }

    @Test
    public void test4Dynamic() {
        test("test-resources/src/property-name-partitioning/test4Dynamic.js");
    }

    @Test(expected = AnalysisResultException.class) // Requires more reasoning from Filtering. foo[name_obj[name]] = obj[name_obj[name]], where name is AnyStr
    public void test4DynamicImprecise() {
        test("test-resources/src/property-name-partitioning/test4DynamicImprecise.js");
    }

    @Test
    public void test4forIn() {
        //ptions.get().disableForInSpecialization();
        test("test-resources/src/property-name-partitioning/test4forIn.js");
    }

    @Test
    public void test4NumericLoop() {
        Options.get().disableLoopUnrolling();
        test("test-resources/src/property-name-partitioning/test4NumericLoop.js");
    }

    @Test
    public void test4WhileLoop() {
        Options.get().disableLoopUnrolling();
        test("test-resources/src/property-name-partitioning/test4WhileLoop.js");
    }

//    @Test
//    public void test5() {
//        test("test-resources/src/property-name-partitioning/test5.js");
//    }
//
//    @Test
//    public void test5forIn() {
//        Options.get().disableForInSpecialization();
//        test("test-resources/src/property-name-partitioning/test5forIn.js");
//    }
//
//    @Test
//    public void test5NumericLoop() {
//        Options.get().disableLoopUnrolling();
//        test("test-resources/src/property-name-partitioning/test5NumericLoop.js");
//    }
//
//    @Test
//    public void test5WhileLoop() {
//        Options.get().disableLoopUnrolling();
//        test("test-resources/src/property-name-partitioning/test5WhileLoop.js");
//    }
//
//    @Test
//    public void test6() {
//        test("test-resources/src/property-name-partitioning/test6.js");
//    }
//
//    @Test
//    public void test6forIn() {
//        Options.get().disableForInSpecialization();
//        test("test-resources/src/property-name-partitioning/test6forIn.js");
//    }
//
//    @Test
//    public void test6NumericLoop() {
//        Options.get().disableLoopUnrolling();
//        test("test-resources/src/property-name-partitioning/test6NumericLoop.js");
//    }
//
//    @Test
//    public void test6WhileLoop() {
//        Options.get().disableLoopUnrolling();
//        test("test-resources/src/property-name-partitioning/test6WhileLoop.js");
//    }
//
//    @Test
//    public void test7() {
//        test("test-resources/src/property-name-partitioning/test7.js");
//    }
//
//    @Test
//    public void test7forIn() {
//        Options.get().disableForInSpecialization();
//        test("test-resources/src/property-name-partitioning/test7forIn.js");
//    }
//
//    @Test
//    public void test7NumericLoop() {
//        Options.get().disableLoopUnrolling();
//        test("test-resources/src/property-name-partitioning/test7NumericLoop.js");
//    }
//
//    @Test
//    public void test7WhileLoop() {
//        Options.get().disableLoopUnrolling();
//        test("test-resources/src/property-name-partitioning/test7WhileLoop.js");
//    }
//
    @Test
    public void test8() {
        Options.get().enablePolyfillMDN();
        test("test-resources/src/property-name-partitioning/test8.js");
    }

    @Test(expected = AnalysisResultException.class)
    public void test9() {
        Options.get().enablePolyfillMDN();
        test("test-resources/src/property-name-partitioning/test9.js");
    }

    @Test
    public void testUnderscore() {
        Options.get().getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);

        Options.get().enableDeterminacy();
        Options.get().enablePolyfillMDN();
        Options.get().enablePolyfillTypedArrays();
        Options.get().enablePolyfillES6Collections();
        Options.get().enablePolyfillES6Promises();
        Options.get().enableConsoleModel();
        Options.get().enableIncludeDom();

        test("benchmarks/tajs/src/underscore/libraries/underscore-1.8.3.js");
    }

    @Test
    public void testUnderscoreTest() {
        Options.get().getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);

        Options.get().enableDeterminacy();
        Options.get().enablePolyfillMDN();
        Options.get().enablePolyfillTypedArrays();
        Options.get().enablePolyfillES6Collections();
        Options.get().enablePolyfillES6Promises();
        Options.get().enableConsoleModel();
        Options.get().enableIncludeDom();

        test("benchmarks/tajs/src/underscore/test-suite/arrays/test1.html");
    }

    @Test
    public void testLodash3() {
        Options.get().getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);

        Options.get().enableDeterminacy();
        Options.get().enablePolyfillMDN();
        Options.get().enablePolyfillTypedArrays();
        Options.get().enablePolyfillES6Collections();
        Options.get().enablePolyfillES6Promises();
        Options.get().enableConsoleModel();
        Options.get().enableIncludeDom();
        Options.get().setAnalysisTimeLimit(180);

        Options.get().getUnsoundness().setUsePreciseFunctionToString(true);

        test("benchmarks/tajs/src/lodash/libraries/lodash-3.0.0.js");
    }

    @Test
    public void testLodash4() {
        Options.get().getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);

        Options.get().enableDeterminacy();
        Options.get().enablePolyfillMDN();
        Options.get().enablePolyfillTypedArrays();
        Options.get().enablePolyfillES6Collections();
        Options.get().enablePolyfillES6Promises();
        Options.get().enableConsoleModel();
        Options.get().enableIncludeDom();
        Options.get().setAnalysisTimeLimit(120);

        Options.get().getUnsoundness().setUsePreciseFunctionToString(true);

        test("benchmarks/tajs/src/lodash/libraries/lodash-4.17.10.js");
    }

    @Test
    public void testHeapContext1() {
        Options.get().enableDeterminacy();
        Options.get().disableQuiet();
        test("test-resources/src/property-name-partitioning/heapContextTest1.js");
    }

    @Test
    public void testHeapContext2() {
        Options.get().enableDeterminacy();
        test("test-resources/src/property-name-partitioning/heapContextTest2.js");
    }

    @Test
    public void testHeapContext1ThroughParameters() {
        Options.get().enableDeterminacy();
        test("test-resources/src/property-name-partitioning/heapContextTest1ThroughParameters.js");
    }

    @Test
    public void testHeapContext2ThroughParameters() {
        Options.get().enableDeterminacy();
        test("test-resources/src/property-name-partitioning/heapContextTest2ThroughParameters.js");
    }

    @Test
    public void testGetOwnPropertyDescriptorAndDefinePropertyPropNameAlreadyPartitioned() {
        test("test-resources/src/property-name-partitioning/testGetOwnPropertyDescriptorPropNameAlreadyPartitioned.js");
    }

    @Test
    public void testGetOwnPropertyDescriptorAndDefineProperty() {
        test("test-resources/src/property-name-partitioning/testGetOwnPropertyDescriptor.js");
    }

    @Test
    public void testPartitionsShouldBeKilledWhenReturningFromCalls() {
        test("test-resources/src/property-name-partitioning/partitionsShouldBeKilledWhenReturningFromCalls.js");
    }

    @Test
    public void testFreeVariablePartitioningUnsoundIfFreeVariableQualifiersEscape() {
        test("test-resources/src/property-name-partitioning/freeVariablePartitioningUnsoundIfFreeVariableQualifiersEscape.js");
    }

}
