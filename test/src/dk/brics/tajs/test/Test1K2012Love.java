package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.monitors.OrdinaryExitReachableCheckerMonitor;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Entries from the js1k.com competition
 * (current status: output uninspected)
 */
public class Test1K2012Love {

    private IAnalysisMonitoring monitoring;

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableIncludeDom();
        Options.get().enableUnevalizer();
        Options.get().enablePolyfillMDN();
        monitoring = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableCheckerMonitor());
    }

    @Test
    public void test1k_2012_love_1001() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1001.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1005() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1005.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1008() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1008.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1010() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1010.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1015() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1015.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1018() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1018.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1019() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1019.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // TODO Fix testcase-bug: undefined reference to 'cvs'
    @Test
    public void test1k_2012_love_1024() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1024.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1025() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1025.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1027() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1027.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2012_love_1028() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1028.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // TODO Fix testcase-bug: missing invocation of main function
    @Test
    public void test1k_2012_love_1030() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1030.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1031() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1031.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1041() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1041.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1044() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1044.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1045() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1045.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1047() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1047.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1048() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1048.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1050() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1050.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1053() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1053.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1054() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1054.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1055() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1055.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1057() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1057.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1061() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1061.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1063() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1063.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class /* eval of crazy string: (("({" + (((((((d).getElementById)(("l"))).value).trim)()).replace)(((RegExp)(("\n"),("g"))),(","))) + "})")*/)
    public void test1k_2012_love_1066() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1066.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1068() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1068.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1071() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1071.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1072() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1072.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1079() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1079.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1091() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1091.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1092() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1092.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2012_love_1093() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1093.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1095() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1095.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1102() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1102.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1103() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1103.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1107() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1107.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1113() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1113.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1115() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1115.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1120() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1120.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2012_love_1134() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1134.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1140() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1140.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1143() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1143.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1148() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1148.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1149() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1149.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1154() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1154.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // TODO Fix testcase-bug: missing invocation of main function
    @Test
    public void test1k_2012_love_1155() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1155.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1156() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1156.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1157() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1157.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of JSON.stringify
    @Test
    public void test1k_2012_love_1160() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1160.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1163() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1163.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1166() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1166.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test(expected = StringIndexOutOfBoundsException.class /* Bug in the JavaScript parser... */)
    public void test1k_2012_love_1168() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1168.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1169() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1169.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1170() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1170.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of JSON.stringify
    @Test
    public void test1k_2012_love_1171() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1171.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing (it seems)
    @Test
    public void test1k_2012_love_1175() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1175.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1176() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1176.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1183() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1183.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1186() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1186.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1188() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1188.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of insertAdjacentHTML
    @Test
    public void test1k_2012_love_1189() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1189.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2012_love_1190() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1190.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1191() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1191.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1195() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1195.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1196() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1196.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1199() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1199.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1201() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1201.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1203() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1203.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1209() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1209.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1218() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1218.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1219() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1219.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1224() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1224.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1225() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1225.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1229() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1229.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1230() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1230.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1232() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1232.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of innerHTML
    @Test
    public void test1k_2012_love_1240() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1240.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1243() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1243.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1245() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1245.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1246() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1246.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1247() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1247.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1249() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1249.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1250() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1250.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1251() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1251.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1252() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1252.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2012_love_1254() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1254.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1257() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1257.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1258() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1258.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2012_love_1260() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1260.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1265() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1265.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2012_love_1269() {
        Misc.init();
        //Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1269.js"};
        Misc.run(args, monitoring);
        //Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1270() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1270.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of webkitAudioContext
    @Test
    public void test1k_2012_love_1271() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1271.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2012_love_1274() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1274.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1275() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1275.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing (& for-in on strings)
    @Test
    public void test1k_2012_love_1276() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1276.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1279() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1279.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1280() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1280.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of WebSocket
    @Test
    public void test1k_2012_love_1281() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1281.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1284() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2012love/shim.js", "test/1k2012love/1284.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }
}
