package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.monitors.OrdinaryExitReachableCheckerMonitor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Entries from the js1k.com competition
 * (current status: output uninspected)
 */
public class Test1K2013Spring {

    private IAnalysisMonitoring monitoring;

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableIncludeDom();
        Options.get().enableUnevalizer();
        monitoring = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableCheckerMonitor());
    }

    @Test
    public void test1k_2013_spring_1307() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1307.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1309() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1309.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1310() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1310.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1311() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1311.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1316() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1316.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1319() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1319.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1323() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1323.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1334() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1334.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1336() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1336.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1337() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1337.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1344() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1344.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1345() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1345.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1350() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1350.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1358() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1358.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1360() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1360.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1362() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1362.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1375() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1375.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1376() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1376.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1377() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1377.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1379() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1379.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1384() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1384.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1388() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1388.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // Ordinary exit unreachable (due to definite error?)! See GitHub #219
    @Test
    public void test1k_2013_spring_1392() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1392.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1398() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1398.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1400() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1400.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1404() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1404.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1411() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1411.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1415() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1415.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // unevalizer stackoverflow. GitHub #143
    @Test
    public void test1k_2013_spring_1417() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1417.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1420() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1420.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1421() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1421.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS precision for eval
    @Test
    public void test1k_2013_spring_1423() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1423.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // Ordinary exit unreachable (due to definite error?)! See GitHub #219
    @Test
    public void test1k_2013_spring_1425() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1425.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS precision for eval
    @Test
    public void test1k_2013_spring_1426() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1426.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1427() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1427.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1428() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1428.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1429() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1429.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1430() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1430.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of Array.map
    @Test
    public void test1k_2013_spring_1436() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1436.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of Array.forEach
    @Test
    public void test1k_2013_spring_1437() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1437.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1438() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1438.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1442() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1442.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1443() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1443.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1449() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1449.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1450() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1450.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1454() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1454.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1457() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1457.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1458() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1458.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1462() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1462.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // Ordinary exit unreachable (due to definite error?)! See GitHub #219
    @Test
    public void test1k_2013_spring_1470() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1470.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of Array.forEach
    @Test
    public void test1k_2013_spring_1472() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1472.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1473() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1473.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1475() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1475.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1478() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1478.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1479() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1479.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1483() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1483.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1484() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1484.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1486() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1486.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1498() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1498.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1502() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1502.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // Ordinary exit unreachable (due to definite error?)! See GitHub #219
    @Test
    public void test1k_2013_spring_1506() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1506.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1507() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1507.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1510() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1510.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1511() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1511.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1514() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1514.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1524() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1524.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1525() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1525.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // Ordinary exit unreachable (due to definite error?)! See GitHub #219
    @Test
    public void test1k_2013_spring_1526() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1526.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // Ordinary exit unreachable (due to definite error?)! See GitHub #219
    @Test
    public void test1k_2013_spring_1528() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1528.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1529() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1529.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1533() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1533.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1535() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1535.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1536() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1536.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1537() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1537.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1539() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1539.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // Ordinary exit unreachable (due to definite error?)! See GitHub #219
    @Test
    public void test1k_2013_spring_1541() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1541.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // Ordinary exit unreachable (due to definite error?)! See GitHub #219
    @Test
    public void test1k_2013_spring_1542() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1542.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS precision for eval
    @Test
    public void test1k_2013_spring_1544() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1544.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1547() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1547.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2013_spring_1557() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2013spring/shim.js", "test/1k2013spring/1557.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }
}
