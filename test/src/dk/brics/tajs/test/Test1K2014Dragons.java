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
public class Test1K2014Dragons {

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
    public void test1k_2014_dragons_1615() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1615.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore
    // FIXME TAJS bug with multiple files: function declarations in the file loaded second are overriden by assignments in the file that is loaded first
    // GitHub issue #227
    @Test
    public void test1k_2014_dragons_1620() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1620.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1641() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1641.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1648() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1648.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1656() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1656.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1664() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1664.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1667() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1667.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1670() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1670.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2014_dragons_1672() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1672.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1673() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1673.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore
    // FIXME TAJS bug with multiple files: function declarations in the file loaded second are overriden by assignments in the file that is loaded first
    @Test
    public void test1k_2014_dragons_1677() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1677.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1680() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1680.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of JSON.parse + JSON.stringify
    @Test
    public void test1k_2014_dragons_1684() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1684.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1700() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1700.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1702() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1702.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2014_dragons_1704() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1704.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1706() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1706.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1709() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1709.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of document.styleSheets
    @Test
    public void test1k_2014_dragons_1720() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1720.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class /* Function.bind not modeled */)
    public void test1k_2014_dragons_1722() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1722.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1734() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1734.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model DOM-element-ids in the global scope
    @Test
    public void test1k_2014_dragons_1742() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1742.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1748() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1748.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of document.styleSheets
    @Test
    public void test1k_2014_dragons_1751() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1751.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1753() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1753.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1755() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1755.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1763() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1763.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1765() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1765.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1766() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1766.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // TODO Fix testcase-bug: window.onresize invoked
    @Test
    public void test1k_2014_dragons_1770() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1770.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1771() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1771.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore
// FIXME TAJS bug with multiple files: function declarations in the file loaded second are overriden by assignments in the file that is loaded first
    @Test
    public void test1k_2014_dragons_1775() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1775.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of WebGLRenderingContext.xxx
    @Test
    public void test1k_2014_dragons_1776() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1776.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model DOM-element-ids in the global scope
    @Test
    public void test1k_2014_dragons_1777() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1777.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1782() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1782.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1787() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1787.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2014_dragons_1790() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1790.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of console
    @Test
    public void test1k_2014_dragons_1793() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1793.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1799() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1799.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore
    // FIXME TAJS bug with multiple files: function declarations in the file loaded second are overriden by assignments in the file that is loaded first
    @Test
    public void test1k_2014_dragons_1801() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1801.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1803() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1803.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1804() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1804.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of WebGLRenderingContext.xxx
    @Test
    public void test1k_2014_dragons_1807() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1807.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1812() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1812.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model DOM-element-ids in the global scope
    @Test
    public void test1k_2014_dragons_1815() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1815.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of innerHTML
    @Test
    public void test1k_2014_dragons_1820() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1820.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1822() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1822.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1824() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1824.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of string-forin
    @Test
    public void test1k_2014_dragons_1832() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1832.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1835() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1835.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // github #302
    @Test
    public void test1k_2014_dragons_1836() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1836.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1837() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1837.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1847() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1847.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1848() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1848.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model DOM-element-ids in the global scope
    @Test
    public void test1k_2014_dragons_1850() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1850.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2014_dragons_1866() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1866.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of WebGLRenderingContext.xxx
    @Test
    public void test1k_2014_dragons_1868() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1868.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1873() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1873.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1891() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1891.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // github #302
    @Test
    public void test1k_2014_dragons_1898() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1898.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1900() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1900.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // TODO Fix testcase-bug: referencing absent variable: '_'
    @Test
    public void test1k_2014_dragons_1903() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1903.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1904() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1904.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of WebGLRenderingContext.xxx
    @Test
    public void test1k_2014_dragons_1910() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1910.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1911() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1911.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1912() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1912.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // TODO Fix testcase-bug: call to function commented out(!?!?)
    @Test
    public void test1k_2014_dragons_1914() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1914.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1919() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1919.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // TODO Fix testcase-bug: missing invocation ...
    @Test
    public void test1k_2014_dragons_1929() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1929.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1931() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1931.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1939() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1939.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1943() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1943.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1951() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1951.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // testcase-bug: debug flags & similar (removed possibility of 100% TAJS coverage...)
    @Test
    public void test1k_2014_dragons_1952() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1952.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of AudioContext.createAnalyser
    @Test
    public void test1k_2014_dragons_1953() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1953.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1959() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1959.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class /* eval precision */)
    public void test1k_2014_dragons_1964() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1964.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // TODO Fix testcase-bug: window.onresize invoked
    @Test
    public void test1k_2014_dragons_1967() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1967.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1969() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1969.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1971() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1971.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Ignore // FIXME TAJS model of String-indexing
    @Test
    public void test1k_2014_dragons_1972() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1972.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void test1k_2014_dragons_1973() {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/1k2014dragons/shims.js", "test/1k2014dragons/1973.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }
}


