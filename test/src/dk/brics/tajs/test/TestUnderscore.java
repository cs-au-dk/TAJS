package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

@SuppressWarnings("static-method")
public class TestUnderscore {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestUnderscore");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().getSoundnessTesterOptions().setRootDirFromMainDirectory(Paths.get("../../"));
        Options.get().getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);

        Options.get().enableDeterminacy();
        Options.get().enablePolyfillMDN();
        Options.get().enablePolyfillTypedArrays();
        Options.get().enablePolyfillES6Collections();
        Options.get().enablePolyfillES6Promises();
        Options.get().enableConsoleModel();
        Options.get().enableIncludeDom();

        Options.get().enableBlendedAnalysis();
        Options.get().enableIgnoreUnreached();
    }

    @Test
    public void underscore_1_8_3_test1() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test1.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test2() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test2.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test3() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test3.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test4() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test4.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test5() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test5.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test6() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test6.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test7() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test7.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test8() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test8.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test9() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test9.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test10() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test10.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test11() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test11.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test12() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test12.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test13() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test13.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test14() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test14.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test15() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test15.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test16() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test16.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test17() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test17.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test18() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test18.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test19() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test19.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test20() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test20.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test21() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test21.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test22() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test22.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test23() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test23.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test24() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test24.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test25() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test25.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test26() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test26.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test27() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test27.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test28() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test28.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test29() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/arrays/test29.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test30() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/chaining/test30.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test31() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/chaining/test31.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test32() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/chaining/test32.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test33() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/chaining/test33.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test34() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/chaining/test34.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test35() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/chaining/test35.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test36() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/chaining/test36.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test37() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/chaining/test37.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test38() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/chaining/test38.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test39() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test39.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test40() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test40.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test41() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test41.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test42() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test42.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test43() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test43.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test44() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test44.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test45() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test45.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test46() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test46.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test47() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test47.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test48() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test48.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test49() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test49.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test50() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test50.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test51() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test51.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test52() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test52.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test53() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test53.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test54() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test54.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test55() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test55.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test56() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test56.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test57() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test57.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test58() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test58.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test59() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test59.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test60() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test60.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test61() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test61.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test62() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test62.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test63() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test63.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test64() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test64.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test65() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test65.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test66() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test66.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test67() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test67.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test68() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test68.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test69() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test69.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test70() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test70.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test71() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test71.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test72() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test72.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test73() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test73.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test74() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test74.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test75() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test75.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test76() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test76.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test77() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test77.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test78() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test78.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test79() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test79.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test80() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test80.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test81() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/collections/test81.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test82() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test82.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test83() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test83.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test84() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test84.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test85() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test85.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test86() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test86.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test87() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test87.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test88() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test88.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test89() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test89.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test90() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test90.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test91() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test91.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test92() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test92.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test93() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test93.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test94() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test94.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test95() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test95.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test96() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test96.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test97() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test97.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test98() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test98.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test99() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test99.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test100() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test100.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test101() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test101.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test102() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test102.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test103() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test103.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test104() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test104.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test105() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test105.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test106() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test106.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test107() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test107.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test108() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test108.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test109() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test109.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test110() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test110.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test111() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test111.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test112() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test112.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test113() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/functions/test113.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test114() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test114.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test115() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test115.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test116() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test116.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test117() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test117.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test118() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test118.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test119() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test119.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test120() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test120.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test121() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test121.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test122() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test122.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test123() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test123.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test124() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test124.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test125() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test125.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test126() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test126.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test127() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test127.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test128() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test128.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test129() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test129.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test130() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test130.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test131() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test131.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test132() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test132.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test133() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test133.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test134() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test134.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test135() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test135.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test136() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test136.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test137() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test137.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test138() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test138.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test139() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test139.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test140() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test140.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test141() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test141.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test142() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test142.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test143() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test143.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test144() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test144.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test145() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test145.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test146() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test146.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test147() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test147.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test148() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test148.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test149() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test149.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test150() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test150.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test151() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test151.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test152() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/objects/test152.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test153() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test153.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test154() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test154.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test155() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test155.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test156() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test156.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test157() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test157.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test158() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test158.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test159() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test159.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test160() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test160.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test161() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test161.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test162() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test162.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test163() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test163.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test164() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test164.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test165() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test165.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test166() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test166.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test167() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test167.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test168() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test168.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test169() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test169.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test170() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test170.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test171() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test171.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test172() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test172.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test173() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test173.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test174() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test174.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test175() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test175.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test176() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test176.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test177() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test177.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test178() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test178.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test179() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test179.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test180() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test180.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test181() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test181.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscore_1_8_3_test182() {
        Misc.run("benchmarks/tajs/src/underscore/test-suite/utility/test182.html");
        Misc.checkSystemOutput();
    }

}
