package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

/**
 * Benchmarks from Kaist: downloads of Alexa top 10 pages.
 * (current status: output uninspected & lots of large precision losses ~> uneval failures)
 * (current tests: only testing flowgraph building)
 */
public class TestKaistAlexaBenchmarksFlowgraph {

    private IAnalysisMonitoring monitoring;

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableNoMessages();
        monitoring = CompositeMonitoring.buildFromList(new DefaultAnalysisMonitoring() {
            @Override
            public boolean allowNextIteration() {
                return false;
            }
        }, new Monitoring());
    }

    @Test
    public void google() {
        Misc.init();
        Misc.run(new String[]{"test/kaistalexabenchmarks/1,google.com/main.htm"}, monitoring);
    }

    @Test
    public void facebook() {
        Misc.init();
        Misc.run(new String[]{"test/kaistalexabenchmarks/2,facebook.com/main.htm"}, monitoring);
    }

    @Test
    public void youtube() {
        Misc.init();
        Misc.run(new String[]{"test/kaistalexabenchmarks/3,youtube.com/main.htm"}, monitoring);
    }

    @Test
    public void yahoo() {
        Misc.init();
        Misc.run(new String[]{"test/kaistalexabenchmarks/4,yahoo.com/main.htm"}, monitoring);
    }

    @Test
    public void baidu() {
        Misc.init();
        Misc.run(new String[]{"test/kaistalexabenchmarks/5,baidu.com/main.htm"}, monitoring);
    }

    @Test
    public void wikipedia() {
        Misc.init();
        Misc.run(new String[]{"test/kaistalexabenchmarks/6,wikipedia.org/main.htm"}, monitoring);
    }

    @Test
    public void twitter() {
        Misc.init();
        Misc.run(new String[]{"test/kaistalexabenchmarks/7,twitter.com/main.htm"}, monitoring);
    }

    @Test
    public void amazon() {
        Misc.init();
        Misc.run(new String[]{"test/kaistalexabenchmarks/8,amazon.com/main.htm"}, monitoring);
    }

    @Test
    public void qq() {
        Misc.init();
        Misc.run(new String[]{"test/kaistalexabenchmarks/9,qq.com/main.htm"}, monitoring);
    }

    @Test
    public void live() {
        Misc.init();
        Misc.run(new String[]{"test/kaistalexabenchmarks/10,live.com/main.htm"}, monitoring);
    }
}
