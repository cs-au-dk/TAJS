package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import dk.brics.tajs.util.AnalysisResultException;
import dk.brics.tajs.util.ParseError;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestFlowgraphBuilder {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestFlowgraphBuilder");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTestFlowGraphBuiler();
        Options.get().enableDoNotExpectOrdinaryExit();
    }

    @Test
    public void flowgraphbuilder_0000() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0000.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0001() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0001.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0002() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0002.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0003() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0003.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0004() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0004.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0005() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0005.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0006() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0006.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0007() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0007.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0008() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0008.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0009() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0009.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0010() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0010.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0011() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0011.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0012() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0012.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0013() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0013.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0014() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0014.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0015() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0015.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0016() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0016.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0017() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0017.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0018() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0018.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0019() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0019.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0020() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0020.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0021() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0021.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0022() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0022.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0023() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0023.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0024() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0024.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0025() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0025.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0026() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0026.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0027() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0027.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0028() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0028.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0029() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0029.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0030() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0030.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0031() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0031.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0032() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0032.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0033() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0033.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0034() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0034.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0035() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0035.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0036() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0036.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0037() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0037.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0038() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0038.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0039() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0039.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = ParseError.class) // unnamed function statement
    public void flowgraphbuilder_0040() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0040.js");
    }

    @Test
    public void flowgraphbuilder_0041() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0041.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0042() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0042.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0043() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0043.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0044() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0044.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0045() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0045.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0046() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0046.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0047() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0047.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0048() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0048.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0049() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0049.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0050() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0050.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0051() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0051.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0052() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0052.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0053() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0053.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0054() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0054.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0055() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0055.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0056() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0056.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0057() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0057.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0058() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0058.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0059() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0059.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0060() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0060.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0061() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0061.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0062() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0062.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0063() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0063.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0064() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0064.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0065() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0065.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0066() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0066.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0067() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0067.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0068() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0068.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0069() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0069.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0070() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0070.js");
    }

    @Test
    public void flowgraphbuilder_0071() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0071.js");
    }

    @Test
    public void flowgraphbuilder_0072() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0072.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0073() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0073.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0074() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0074.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0075() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0075.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0076() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0076.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0077() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0077.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0078() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0078.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0079() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0079.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0080() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0080.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0081() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0081.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0082() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0082.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0083() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0083.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0084() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0084.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0085() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0085.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0086() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0086.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0087() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0087.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0088() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0088.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0089() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0089.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0090() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0090.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0091() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0091.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0092() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0092.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0093() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0093.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0094() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0094.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0095() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0095.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0096() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0096.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0097() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0097.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0098() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0098.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0099() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0099.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0100() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0100.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0101() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0101.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0102() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0102.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0103() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0103.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0104() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0104.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0105() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0105.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0106() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0106.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0107() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0107.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0108() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0108.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0109() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0109.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0110() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0110.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0111() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0111.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0112() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0112.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0113() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0113.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0114() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0114.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0115() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0115.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0116() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0116.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0117() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0117.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0118() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0118.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0119() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0119.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0120() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0120.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0121() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0121.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0122() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0122.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0123() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0123.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0124() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0124.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0125() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0125.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0126() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0126.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0127() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0127.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0128() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0128.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0129() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0129.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0130() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0130.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0131() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0131.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0132() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0132.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0133() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0133.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0134() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0134.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0135() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0135.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0136() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0136.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0137() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0137.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0138() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0138.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0139() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0139.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0140() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0140.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0141() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0141.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0142() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0142.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0143() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0143.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0144() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0144.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.SyntacticSupportNotImplemented.class /* switch with default in non-last position */)
    public void flowgraphbuilder_0145() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0145.js");
    }

    @Test(expected = AnalysisLimitationException.SyntacticSupportNotImplemented.class /* switch with default in non-last position */)
    public void flowgraphbuilder_0146() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0146.js");
    }

    @Test(expected = AnalysisLimitationException.SyntacticSupportNotImplemented.class /* switch with default in non-last position */)
    public void flowgraphbuilder_0147() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0147.js");
    }

    @Test
    public void flowgraphbuilder_0148() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0148a.js", "test-resources/src/flowgraphbuilder/flowgraph_builder0148b.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0149() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0149a.js", "test-resources/src/flowgraphbuilder/flowgraph_builder0149b.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0150() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0150.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0151() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0151.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0152() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0152.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0153() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0153.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0154() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0154.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0155() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0155.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0156() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0156.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0156a() throws Exception {
        Options.get().enableUnevalizer();
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0156a.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0156b() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0156b.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0157() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0157.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0158() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0158.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0159() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0159.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0160() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0160.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0161() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0161.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0162() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0162.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0163() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0163.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0164() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0164.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0165() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0165.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0166() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0166.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0167() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0167.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0168() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0168.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0169() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0169.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0170() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0170.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0171() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0171.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0172() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0172.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0173() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0173.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0174() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0174.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisResultException.class /* GitHub #146 */)
    public void flowgraphbuilder_0175() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0175.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0176() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0176.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0177() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0177.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0178() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0178.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0179() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0179.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0180() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0180.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0181() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0181.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0182() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0182.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0183() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0183.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0184() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0184.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0185() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0185.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0186() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0186.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0187() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0187.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0188() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0188.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0189() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0189.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0190() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0190.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0191() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0191.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0192() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0192.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0193() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0193.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0194() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0194.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0195() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0195.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0196() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0196.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0197() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0197.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0198() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0198.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0199() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0199.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0200() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0200.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0201() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0201.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0201b() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0201b.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0201c() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0201c.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0201d() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0201d.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0201e() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0201e.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0202() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0202.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0203() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0203.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0204() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0204.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0205() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0205.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0206() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0206.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0207() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0207.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0208() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0208.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0209() throws Exception {
        // just check non-crashing
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0209.js");
    }

    @Test
    public void flowgraphbuilder_0210() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0210.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0210b() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0210b.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0210c() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0210c.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0210d() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0210d.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0210e() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0210e.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0211() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0211.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0212() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0212.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0213() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0213.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_0214() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder0214.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0001() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0001.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0002() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0002.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0003() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0003.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0004() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0004.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0005() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0005.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0006() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0006.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0007() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0007.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0008() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0008.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0009() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0009.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0010() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0010.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0011() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0011.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0012() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0012.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0013() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0013.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0014() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0014.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_0015() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_0015.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0001() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0001.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0002() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0002.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0003() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0003.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0004() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0004.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0005() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0005.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0006() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0006.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0007() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0007.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0008() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0008.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0009() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0009.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0010() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0010.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0011() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0011.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0012() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0012.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0013() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0013.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0014() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0014.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0015() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0015.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0016() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0016.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_jump_return_0017() throws Exception {
        Options.get().enableFlowgraph();
        Misc.run("test-resources/src/flowgraphbuilder/flowgraph_builder_jump_return_0017.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_forin() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/forin.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_forin_call() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/forin_call.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_forin_call2() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/forin_call2.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_forin_only_continue() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/forin_only_continue.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_switch_empty() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/switch_empty.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_directive() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/directive.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_multipleLazyOrs() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/multipleLazyOrs.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_switchWithCall() throws Exception {
        Misc.run("test-resources/src/flowgraphbuilder/switchWithCall.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_testExpressionStatementResultRegister() throws Exception {
        // source for this test is weird. But it did expose a bug where expression-statements did not write to the AbstractNode.NO_VALUE register
        Options.get().enableIncludeDom();
        Misc.run("test-resources/src/flowgraphbuilder/testExpressionStatementResultRegister.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void noopEscapes() {
        Misc.run("test-resources/src/flowgraphbuilder/noopEscapes.js");
    }

    @Test
    public void emptyRegexp() {
        Misc.runSource(//
                "TAJS_assert(new RegExp('').source === '(?:)');",
                "TAJS_assert(/(?:)/.source === '(?:)');"
        );
    }

    @Test(expected = AnalysisResultException.class /* GitHub #194 */)
    public void forwardSlashRegExpConstructor() {
        Misc.run("test-resources/src/flowgraphbuilder/forwardSlashRegExpConstructor.js");
    }

    @Test
    public void forwardSlashRegExpLiteral() {
        Misc.run("test-resources/src/flowgraphbuilder/forwardSlashRegExpLiteral.js");
    }

    @Test(expected = ParseError.class)
    public void forwardSlashRegexp_syntaxError() {
        Misc.runSource(//
                "/\\\\//"
        );
    }

    @Test
    public void functionCallPropertyCompoundAssignment() {
        Options.get().enableFlowgraph();
        Misc.runSource("f().x += 42;");
    }

    @Test
    public void emptySwitchCase1() {
        Misc.runSource("",
                "switch (v) {",
                "   case o.p:",
                "}",
                "");
    }

    @Test
    public void emptySwitchCase2() {
        Misc.runSource("",
                "switch (o.p) {",
                "   case o.p:",
                "}",
                "");
    }

    @Test
    public void exceptionInCatch() {
        // should not crash
        Misc.runSource("",
                "try {",
                "   NON_EXISTENT_VAR_1;",
                "} catch (e) {",
                "   NON_EXISTENT_VAR_2;",
                "} finally {",
                "   toString();",
                "}",
                "");
    }

    @Test
    public void forLoopContinue_issue200() {
        Options.get().enableLoopUnrolling(1);
        Misc.runSource(//
                "var first = true;",
                "for (var i = 0; i === 0; i++) {",
                "   TAJS_assert(first);",
                "   TAJS_dumpValue(first);",
                "   first = false;",
                "   continue;",
                "}",
                "");
    }

    @Test(expected = AnalysisLimitationException.SyntacticSupportNotImplemented.class)
    public void flowgraphbuilder_labelledLabelledStatement() {
        Misc.runSource(
                "label1: label2: 42;",
                "");
    }

    @Test(expected = AnalysisLimitationException.SyntacticSupportNotImplemented.class)
    public void labelledLabelledLoop() {
        Misc.runSource(
                "label1: label2: while(X){",
                "   if(Y){continue label1;} ",
                "   if(Z){continue label2;}",
                "};",
                "");
    }

    @Test
    public void flowgraphbuilder_labelledContinue() {
        Misc.runSource(
                "'PRE'",
                "label: for ('INIT'; 'COND'; 'INC') {",
                "   'BODY'",
                "   if('C-COND'){",
                "       continue label;",
                "   }",
                "}",
                "'POST'",
                "");
        Misc.checkSystemOutput();
    }

    @Test
    public void labelledContinueSemantics() {
        Misc.runSource(
                "function labelledContinue(){",
                "   var x = 0;",
                "   var first = true;",
                "   label: for (x++; first;) {",
                "       first = false;",
                "       continue label;",
                "   }",
                "   return x;",
                "}",
                "function unlabelledContinue(){",
                "   var y = 0;",
                "   var first = true;",
                "   label: for (y++; first;) {",
                "       first = false;",
                "       continue;",
                "   }",
                "   return y;",
                "}",
                "TAJS_assert(labelledContinue() === 1);",
                "TAJS_assert(unlabelledContinue() === 1);",
                "");
    }

    @Test
    public void doWhileContinue_issue195() {
        Misc.runSource(
                "'PRE';",
                "do { ",
                "   'BODY1';",
                "   if('BODY-COND1') continue;",
                "   if('BODY-COND2') break;",
                "   'BODY2';",
                "} while ('COND')",
                "'POST'; "
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_hostFunctionSources1() {
        Misc.runSource(
                "'BODY'"
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_hostFunctionSources2() {
        Misc.runSource(
                "function f(){'BODY'}"
        );
        Misc.checkSystemOutput();
    }

    @Test(expected = ParseError.class)
    public void constNoInitialization() {
        Misc.runSource("const x;");
    }

    @Test
    public void constConstDoubleDeclaration() {
        Misc.runSource("const x = 42; const x = 42;");
    }

    @Test
    public void constVarDoubleDeclaration() {
        Misc.runSource("const x = 42; var x = 42;");
    }

    @Test
    public void varConstDoubleDeclaration() {
        Misc.runSource("var x = 42; const x = 42;");
    }

    @Test
    public void constConstDoubleDeclaration_singleStatement() {
        Misc.runSource("const x = 42, x = 42;");
    }

    @Test
    public void postfixCoercionResult() {
        Misc.runSource(
                "x = a++"
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void prefixResult() {
        Misc.runSource(
                "x = ++a"
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void incSourcePositions() {
        Misc.runSource(
                "a += b",
                "c.d += e"
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void postfixSourcePositions() {
        Misc.runSource(
                "a = b++",
                "f()",
                "c.d = e++",
                "f()",
                "f += g++",
                "f()",
                "h.i += j++",
                ""
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_innerHTMLIds() throws Exception {
        Options.get().enableIncludeDom();
        Misc.runSource(
                "document.body.innerHTML = '<div id=\"foo\"></div>';",
                "var e = document.getElementById('foo');",
                "TAJS_assert(e, 'isMaybeObject');",
                "TAJS_assert(e, 'isMaybeNull');"
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void flowgraphbuilder_innerHTMLFunctions() throws Exception {
        Options.get().enableIncludeDom();
        Misc.runSource(
                "var triggered = false;",
                "document.body.innerHTML = '<div onclick=\"triggered = true; TAJS_assert(true);\"></div>';",
                "setTimeout(function(){TAJS_assertEquals(TAJS_make('AnyBool'), triggered)});"
        );
        Misc.checkSystemOutput();
    }

    @Test
    public void doWhileLoopLocations() throws Exception {
        Misc.runSource(
                "'PRE';",
                "do {",
                "   'IN';",
                "} while ('COND')",
                "'POST'"
        );
        Misc.checkSystemOutput();
    }
}


