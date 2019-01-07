package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Entries from the js1k.com competition
 * (current status: output uninspected)
 */
public class Test1K2012Love {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableIncludeDom();
        Options.get().enableUnevalizer();
        Options.get().enablePolyfillMDN();
        Options.get().setAnalysisTransferLimit(100000);
    }

    @Test
    public void test1k_2012_love_1001() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1001.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1005() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1005.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisException.class) // TODO investigate (GitHub #417)
    public void test1k_2012_love_1008() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1008.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1010() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1010.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1015() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1015.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1018() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1018.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1019() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1019.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.AnalysisPrecisionLimitationException.class)
    public void test1k_2012_love_1024() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1024.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1025() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1025.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1027() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1027.js");
        Misc.checkSystemOutput();
    }

    @Test
    @Ignore // TODO investigate (Github #493)
    public void test1k_2012_love_1028() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1028.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1030() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1030.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1031() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1031.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1041() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1041.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1044() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1044.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1045() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1045.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1047() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1047.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1048() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1048.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1050() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1050.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1053() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1053.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1054() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1054.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1055() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1055.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1057() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1057.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1061() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1061.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1063() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1063.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class) // TODO investigate (GitHub #417)
    public void test1k_2012_love_1066() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1066.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1068() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1068.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1071() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1071.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1072() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1072.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1079() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1079.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1091() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1091.js");
        Misc.checkSystemOutput();
    }

    @Ignore // XXX: StackOverflowError in UnevalTools (GitHub #417)
    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    // FIXME nested for-ins (line 95) with unknown property names: quadratic in the number of heap properties, transitively! (GitHub #421)
    public void test1k_2012_love_1092() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1092.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1093() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1093.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1095() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1095.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1102() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1102.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1103() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1103.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1107() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1107.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1113() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1113.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1115() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1115.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1120() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1120.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1134() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1134.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1140() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1140.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1143() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1143.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1148() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1148.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1149() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1149.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1154() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1154.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1155() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1155.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1156() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1156.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class) // TODO investigate (GitHub #417)
    public void test1k_2012_love_1157() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1157.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class) // TODO investigate (GitHub #417)
    public void test1k_2012_love_1160() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1160.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1163() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1163.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1166() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1166.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class) // TODO investigate (GitHub #417)
    public void test1k_2012_love_1168() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1168.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1169() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1169.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1170() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1170.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1171() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1171.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.AnalysisPrecisionLimitationException.class)
    public void test1k_2012_love_1175() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1175.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1176() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1176.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1183() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1183.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1186() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1186.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1188() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1188.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1189() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1189.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1190() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1190.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1191() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1191.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1195() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1195.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1196() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1196.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1199() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1199.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1201() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1201.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1203() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1203.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1209() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1209.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1218() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1218.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1219() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1219.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1224() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1224.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1225() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1225.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1229() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1229.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1230() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1230.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1232() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1232.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1240() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1240.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1243() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1243.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1245() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1245.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1246() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1246.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1247() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1247.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1249() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1249.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1250() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1250.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1251() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1251.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1252() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1252.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1254() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1254.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1257() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1257.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1258() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1258.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1260() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1260.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1265() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1265.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class) // TODO investigate (GitHub #417)
    public void test1k_2012_love_1269() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1269.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1270() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1270.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1271() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1271.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1274() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1274.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1275() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1275.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void test1k_2012_love_1276() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1276.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1279() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1279.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1280() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1280.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1281() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1281.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2012_love_1284() {
        Misc.run("test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1284.js");
        Misc.checkSystemOutput();
    }
}
